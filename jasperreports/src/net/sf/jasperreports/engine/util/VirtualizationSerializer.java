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
package net.sf.jasperreports.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizable;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRVirtualizationContext;
import net.sf.jasperreports.engine.fill.VirtualizationObjectInputStream;
import net.sf.jasperreports.engine.fill.VirtualizationObjectOutputStream;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class VirtualizationSerializer
{

	protected static final int CLASSLOADER_IDX_NOT_SET = -1;

	protected static boolean isAncestorClassLoader(ClassLoader loader)
	{
		for (
				ClassLoader ancestor = JRAbstractLRUVirtualizer.class.getClassLoader();
				ancestor != null;
				ancestor = ancestor.getParent())
		{
			if (ancestor.equals(loader))
			{
				return true;
			}
		}
		return false;
	}

	protected final Map<ClassLoader,Integer> classLoadersIndexes = new HashMap<ClassLoader,Integer>();
	protected final List<ClassLoader> classLoadersList = new ArrayList<ClassLoader>();

	protected class ClassLoaderAnnotationObjectOutputStream extends VirtualizationObjectOutputStream
	{
		public ClassLoaderAnnotationObjectOutputStream(OutputStream out, 
				JRVirtualizationContext virtualizationContext) throws IOException
		{
			super(out, virtualizationContext);
		}

		protected void annotateClass(Class<?> clazz) throws IOException
		{
			super.annotateClass(clazz);

			ClassLoader classLoader = clazz.getClassLoader();
			int loaderIdx;
			if (clazz.isPrimitive()
					|| classLoader == null
					|| isAncestorClassLoader(classLoader))
			{
				loaderIdx = CLASSLOADER_IDX_NOT_SET;
			}
			else
			{
				Integer idx = classLoadersIndexes.get(classLoader);
				if (idx == null)
				{
					idx = Integer.valueOf(classLoadersList.size());
					classLoadersIndexes.put(classLoader, idx);
					classLoadersList.add(classLoader);
				}
				loaderIdx = idx.intValue();
			}

			writeShort(loaderIdx);
		}
	}

	protected class ClassLoaderAnnotationObjectInputStream extends VirtualizationObjectInputStream
	{
		public ClassLoaderAnnotationObjectInputStream(InputStream in, 
				JRVirtualizationContext virtualizationContext) throws IOException
		{
			super(in, virtualizationContext);
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
				if (loaderIdx == CLASSLOADER_IDX_NOT_SET)
				{
					throw e;
				}

				ClassLoader loader = classLoadersList.get(loaderIdx);
				clazz = Class.forName(desc.getName(), false, loader);
			}

			return clazz;
		}
	}
	
	public final void writeData(JRVirtualizable o, OutputStream out) throws IOException
	{
		@SuppressWarnings("resource")
		ObjectOutputStream oos = new ClassLoaderAnnotationObjectOutputStream(out, o.getContext());
		oos.writeObject(o.getVirtualData());
		oos.flush();
	}
	
	public final void readData(JRVirtualizable o, InputStream in) throws IOException
	{
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ClassLoaderAnnotationObjectInputStream(in, o.getContext());
		try
		{
			o.setVirtualData(ois.readObject());
		}
		catch (ClassNotFoundException e)
		{
			// should not happen
			throw new JRRuntimeException("Error deserializing virtualized object", e);
		}
	}

}
