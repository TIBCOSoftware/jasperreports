/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JRVirtualizationContext;
import net.sf.jasperreports.engine.util.VirtualizationSerializer;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class VirtualizationInput extends ObjectInputStream
{
	public static final String EXCEPTION_MESSAGE_KEY_READ_OBJECT_FAILED = "engine.virtualization.input.read.object.failed";

	private final VirtualizationSerializer serializer;
	private final JRVirtualizationContext virtualizationContext;
	
	private final SerializerRegistry serializerRegistry = DefaultSerializerRegistry.getInstance();

	@SuppressWarnings("unchecked")
	private final List<Object>[] readObjects = new List[SerializationConstants.OBJECT_TYPE_COUNT];

	public VirtualizationInput(InputStream in,
			VirtualizationSerializer serializer,
			JRVirtualizationContext virtualizationContext) throws IOException
	{
		super(in);
		
		this.serializer = serializer;
		this.virtualizationContext = virtualizationContext;
	}

	public JRVirtualizationContext getVirtualizationContext()
	{
		return virtualizationContext;
	}

	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException
	{
		Class<?> clazz;
		try
		{
			clazz = super.resolveClass(desc);
			readShort();
		}
		catch (ClassNotFoundException e)
		{
			int loaderIdx = readShort();
			clazz = serializer.resolveClass(desc, loaderIdx);
			if (clazz == null)
			{
				throw e;
			}
		}

		return clazz;
	}

	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException,
			ClassNotFoundException
	{
		int classIdx = readIntCompressed();
		Class<?> clazz = serializer.getClassForDescriptorIdx(classIdx);
		ObjectStreamClass descriptor = ObjectStreamClass.lookupAny(clazz);
		return descriptor;
	}
	
	public int readIntCompressed() throws IOException
	{
		return SerializationUtils.readIntCompressed(this);
	}
	
	public Object readJRObject() throws IOException
	{
		return readJRObject(null);
	}
	
	public Object readJRObject(Boolean storeReference) throws IOException
	{
		int type = readUnsignedByte();
		if (type == SerializationConstants.OBJECT_NULL)
		{
			return null;
		}
		
		if (type == SerializationConstants.OBJECT_ARBITRARY)
		{
			try
			{
				return readObject();
			}
			catch (ClassNotFoundException e)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_READ_OBJECT_FAILED,
						(Object[])null,
						e);
			}
		}
		
		if ((type & SerializationConstants.OBJECT_REF_MASK) != 0)
		{
			int typeValue = type ^ SerializationConstants.OBJECT_REF_MASK;
			int objectIdx = readIntCompressed();
			return resolveReference(typeValue, objectIdx);
		}
		
		@SuppressWarnings("rawtypes")
		ObjectSerializer typeSerializer = serializerRegistry.getSerializer(type);
		Object readObject = typeSerializer.read(this);
		
		if (storeReference == null ? typeSerializer.defaultStoreReference() : storeReference)
		{
			putReference(type, readObject);
		}
		
		return readObject;
	}
	
	protected Object resolveReference(int typeValue, int objectIndex)
	{
		List<Object> objects = readObjects[typeValue - SerializationConstants.OBJECT_TYPE_OFFSET];
		return objects.get(objectIndex);
	}
	
	protected void putReference(int typeValue, Object value)
	{
		List<Object> objects = readObjects[typeValue - SerializationConstants.OBJECT_TYPE_OFFSET];
		if (objects == null)
		{
			objects = new ArrayList<Object>();
			readObjects[typeValue - SerializationConstants.OBJECT_TYPE_OFFSET] = objects;
		}
		
		objects.add(value);
	}
}
