/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.data.cache;

import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TypedColumnStore implements ColumnStore
{

	private static final Log log = LogFactory.getLog(TypedColumnStore.class);
	
	private final ColumnDataCacheHandler cacheHandler;
	private Class<?> baseType;
	private boolean concreteTypeInitialized;
	private ColumnStore valueStore;
	private int count;
	private ColumnStore nullStore;
	
	public TypedColumnStore(ColumnDataCacheHandler cacheHandler, Class<?> baseType)
	{
		this.cacheHandler = cacheHandler;
		this.baseType = baseType;
		this.concreteTypeInitialized = !baseType.isInterface() && Modifier.isFinal(baseType.getModifiers());
		
		this.count = 0;
	}
	
	public void addValue(Object value)
	{
		if (value == null)
		{
			ensureNullStore();
			nullStore.addValue(true);
		}
		else
		{
			Class<?> valueType = value.getClass();
			if (!concreteTypeInitialized)
			{
				if (!baseType.isInstance(value))
				{
					// this shouldn't normally happen
					if (log.isDebugEnabled())
					{
						log.debug(this + ": value not instance of type " + baseType);
					}
					
					cacheHandler.disableCaching();
					return;
				}
				
				// deduce the base type from the first non-null value
				if (log.isDebugEnabled())
				{
					log.debug(this + ": base type deduced from value is " + valueType);
				}
				
				baseType = valueType;
				concreteTypeInitialized = true;
			}
			else if (!baseType.equals(valueType))
			{
				// TODO lucianc check if extending type, i.e. Integer on Number field
				// the value type differs from the base type, not caching
				if (log.isDebugEnabled())
				{
					log.debug(this + ": value type " + valueType + " differs from base type " + baseType);
				}
				
				cacheHandler.disableCaching();
				return;
			}
			
			if (ensureValueStore())
			{
				valueStore.addValue(value);
				
				// we need all values in the null store
				if (nullStore != null)
				{
					nullStore.addValue(false);
				}
			}
		}

		++count;
	}

	protected void ensureNullStore()
	{
		if (nullStore == null)
		{
			nullStore = cacheHandler.createColumnStore(Boolean.class);
			
			if (log.isDebugEnabled())
			{
				log.debug(this + ": created null store " + nullStore);
			}
			
			// we need all values in the null store
			for (int i = 0; i < count; ++i)
			{
				nullStore.addValue(false);
			}
		}
	}
	
	protected boolean ensureValueStore()
	{
		if (valueStore == null)
		{
			valueStore = cacheHandler.createColumnStore(baseType);
			
			if (log.isDebugEnabled())
			{
				log.debug(this + ": created value store " + valueStore + " for type " + baseType);
			}
			
			if (valueStore == null)
			{
				cacheHandler.disableCaching();
				return false;
			}
		}
		return true;
	}

	public ColumnValues createValues()
	{
		ColumnValues values = valueStore == null ? EmptyColumnValues.instance() : valueStore.createValues();
		ColumnValues finalValues;
		if (nullStore == null)
		{
			// no nulls
			finalValues = values;
			
			if (log.isDebugEnabled())
			{
				log.debug(this + ": created not null values of size " + values.size());
			}
		}
		else
		{
			ColumnValues nullValues = nullStore.createValues();
			finalValues = new NullableValues(nullValues, values);
			
			if (log.isDebugEnabled())
			{
				log.debug(this + ": created not nulluable values of size " + nullValues.size()
						+ ", non null size " + values.size());
			}
		}
		return finalValues;
	}

	public String toString()
	{
		return "TypedColumnStore@" + hashCode();
	}
	
}
