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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.IndexedDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Report data cache handler that collects data in column stores.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ColumnDataCacheHandler implements DataCacheHandler, Serializable
{

	private static final Log log = LogFactory.getLog(ColumnDataCacheHandler.class);

	private final static int DEFAULT_BUFFER_STORE_SIZE = 4096;
	
	private boolean enabled;
	private volatile boolean populated;
	private int bufferStoreSize = DEFAULT_BUFFER_STORE_SIZE;
	
	private Map<Object, ColumnCacheData> cachedData = new HashMap<Object, ColumnCacheData>();
	
	public ColumnDataCacheHandler()
	{
		enabled = true;
	}
	
	public boolean isCachingEnabled()
	{
		return enabled;
	}
	
	protected void disableCaching()
	{
		if (log.isDebugEnabled())
		{
			log.debug("caching disabled");
		}
		
		this.enabled = false;
	}

	public void setCachePopulated()
	{
		populated = true;
	}

	public boolean isCachePopulated()
	{
		return populated;
	}

	public DataCollector getCollector(Object key)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Creating ColumnDataCollector for " + key);
		}
		
		ColumnDataCollector cacher = new ColumnDataCollector(key);
		return cacher;
	}

	public boolean hasCachedData(Object key)
	{
		return cachedData.containsKey(key);
	}

	public IndexedDataSource getCachedData(Object key)
	{
		ColumnCacheData cacheData = cachedData.get(key);
		if (cacheData == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("No cached data exists for " + key);
			}
			return null;
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("Found cached data source of " + cacheData.size() + " records for " + key);
		}
		
		IndexedDataSource dataSource = cacheData.createDataSource();
		return dataSource;
	}
	
	protected void addCacheData(Object key, ColumnCacheData data)
	{
		cachedData.put(key, data);
	}
	
	public ColumnStore createColumnStore(JRField field)
	{
		return new TypedColumnStore(this, field.getValueClass());
	}
	
	public ColumnStore createColumnStore(Class<?> type)
	{
		BufferColumnStore bufferStore = null;
		if (Integer.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToIntegerTransformer.instance());
		}
		else if (Long.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToLongTransformer.instance());
		}
		else if (Short.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToShortTransformer.instance());
		}
		else if (Byte.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToByteTransformer.instance());
		}
		else if (Character.class.equals(type))
		{
			bufferStore = new LongArrayStore(bufferStoreSize, NumberToCharTransformer.instance());
		}
		else if (Double.class.equals(type))
		{
			bufferStore = new DoubleArrayStore(bufferStoreSize);
		}
		else if (Float.class.equals(type))
		{
			bufferStore = new FloatArrayStore(bufferStoreSize); 
		}
		else if (String.class.equals(type))
		{
			bufferStore = new ObjectArrayStore(bufferStoreSize); 
		}
		else if (Date.class.equals(type))
		{
			bufferStore = new DateStore(bufferStoreSize, NumberToDateTransformer.instance()); 
		}
		else if (java.sql.Date.class.equals(type))
		{
			bufferStore = new DateStore(bufferStoreSize, NumberToSQLDateTransformer.instance()); 
		}
		else if (Timestamp.class.equals(type))
		{
			bufferStore = new TimestampStore(bufferStoreSize);
		}
		else if (Time.class.equals(type))
		{
			bufferStore = new DateStore(bufferStoreSize, NumberToSQLTimeTransformer.instance()); 
		}
		else if (BigInteger.class.equals(type))
		{
			bufferStore = new BigIntegerStore(bufferStoreSize);
		}
		else if (BigDecimal.class.equals(type))
		{
			bufferStore = new BigDecimalStore(bufferStoreSize);
		}
		else if (Boolean.class.equals(type))
		{
			bufferStore = new BooleanStore(bufferStoreSize);
		}
		
		ColumnStore store = null;
		if (bufferStore != null)
		{
			store = new BlockColumnStore(bufferStore);
			
			if (log.isDebugEnabled())
			{
				log.debug("created block store " + store + " with buffer " + bufferStore
						+ ", buffer size " + bufferStoreSize);
			}
		}
		return store;
	}
	
	class ColumnDataCollector implements DataCollector
	{
		private final Object key;
		private JRField[] fields;
		private ColumnStore[] columns;
		private int size;
		private boolean ended;
		
		public ColumnDataCollector(Object key)
		{
			this.key = key;
		}

		public void init(JRField[] datasetFields)
		{
			fields = (datasetFields == null) ? new JRField[0] : datasetFields;
			
			columns = new ColumnStore[fields.length];
			for (int i = 0; i < fields.length; i++)
			{
				JRField field = fields[i];
				ColumnStore columnStore = createColumnStore(field);
				if (columnStore == null)
				{
					if (log.isDebugEnabled())
					{
						log.debug("Field " + field.getName() + " of type " + field.getValueClassName()
								+ " from " + key + " not cacheable");
					}
					
					disableCaching();
					return;
				}
				
				if (log.isDebugEnabled())
				{
					log.debug("created store " + columnStore + " for field " + field.getName()
							+ " at key " + key);
				}
				
				columns[i] = columnStore;
			}
			
			size = 0;
			ended = false;
		}

		public void addRecord(Object[] values)
		{
			if (!isCachingEnabled())
			{
				// nothing to do
				return;
			}
			
			if (values == null || values.length != columns.length)
			{
				throw new IllegalArgumentException();
			}
			
			for (int i = 0; i < columns.length; i++)
			{
				columns[i].addValue(values[i]);
			}
			
			++size;
		}

		public void end()
		{
			if (!isCachingEnabled())
			{
				// nothing to do
				return;
			}
			
			String[] fieldNames;
			if (fields == null)
			{
				fieldNames = new String[0];
			}
			else
			{
				fieldNames = new String[fields.length];
				for (int i = 0; i < fields.length; i++)
				{
					fieldNames[i] = fields[i].getName();
				}
			}
			
			ColumnValues[] values = new ColumnValues[columns.length];
			for (int i = 0; i < columns.length; i++)
			{
				values[i] = columns[i].createValues();
			}
			
			ColumnCacheData data = new ColumnCacheData(fieldNames, size, values);
			addCacheData(key, data);
			
			if (log.isDebugEnabled())
			{
				log.debug("Recorded cached data source of " + size + " records");
			}
			
			ended = true;
		}

		public boolean hasEnded()
		{
			return ended;
		}
	}
}
