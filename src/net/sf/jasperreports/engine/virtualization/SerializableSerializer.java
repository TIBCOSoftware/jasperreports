/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.virtualization;

import java.io.IOException;
import java.lang.reflect.Constructor;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class SerializableSerializer<T extends VirtualizationSerializable> implements ObjectSerializer<T>
{
	
	private static final Class<?>[] NO_ARGS_TYPES = new Class<?>[0];
	private static final Object[] NO_ARGS = new Object[0];
	
	private final int typeValue;
	private final Class<T> type;
	
	private final Constructor<T> constructor;
	
	public SerializableSerializer(int typeValue, Class<T> type)
	{
		this.typeValue = typeValue;
		this.type = type;
		
		this.constructor = initConstructor();
	}

	private Constructor<T> initConstructor()
	{
		try
		{
			return type.getConstructor(NO_ARGS_TYPES);
		}
		catch (NoSuchMethodException e)
		{
			throw new JRRuntimeException("Failed to initialize " +
					"virtualization serializable class " + type.getName(), e);
		}
		catch (SecurityException e)
		{
			throw new JRRuntimeException("Failed to initialize " +
					"virtualization serializable class " + type.getName(), e);
		}
	}

	@Override
	public int typeValue()
	{
		return typeValue;
	}

	@Override
	public ReferenceType defaultReferenceType()
	{
		return ReferenceType.IDENTITY;
	}

	@Override
	public boolean defaultStoreReference()
	{
		// assuming that instances are not shared, can be parametrized if required
		return false;
	}

	@Override
	public void write(T value, VirtualizationOutput out)
			throws IOException
	{
		if (!type.isInstance(value))
		{
			throw new JRRuntimeException("Unexpected value " + value 
					+ " of type " + value.getClass().getName()
					+ ", expecting " + type.getName());
		}
		
		value.writeVirtualized(out);
	}

	@Override
	public T read(VirtualizationInput in) throws IOException
	{
		T object;
		try
		{
			object = constructor.newInstance(NO_ARGS);
		} 
		catch (Exception e)
		{
			throw new JRRuntimeException("Failed to instantiate class " 
					+ type.getName(), e);
		} 
		
		object.readVirtualized(in);
		return object;
	}

}
