/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRClassLoader extends ClassLoader
{


	/**
	 *
	 */
	protected JRClassLoader(ClassLoader parent)
	{
		super(parent);
	}


	/**
	 *
	 */
	public static Class loadClassForName(String className) throws ClassNotFoundException
	{
		Class clazz = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null)
		{
			try
			{
				clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
			}
			catch (ClassNotFoundException e)
			{
				//if (log.isWarnEnabled())
				//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRClassLoader class. Using JRClassLoader.class.getClassLoader() instead.");
			}
		}

		if (clazz == null)
		{
			clazz = Class.forName(className, true, JRClassLoader.class.getClassLoader());
		}

		return clazz;
	}


	/**
	 *
	 */
	public static Class loadClassFromFile(String className, File file) throws IOException
	{
		Class clazz = null;

		try
		{
			clazz = 
				(new JRClassLoader(Thread.currentThread().getContextClassLoader()))
					.loadClass(className, file);
		}
		catch(NoClassDefFoundError e)
		{
			//if (log.isWarnEnabled())
			//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRClassLoader class. Using JRClassLoader.class.getClassLoader() instead.");
		}
	
		if (clazz == null)
		{
			clazz = 
				(new JRClassLoader(JRClassLoader.class.getClassLoader()))
					.loadClass(className, file);
		}
		
		return clazz;
	}


	/**
	 *
	 */
	public static Class loadClassFromBytes(String className, byte[] bytecodes) throws IOException
	{
		Class clazz = null;

		try
		{
			clazz = 
				(new JRClassLoader(Thread.currentThread().getContextClassLoader()))
					.loadClass(className, bytecodes);
		}
		catch(NoClassDefFoundError e)
		{
			//if (log.isWarnEnabled())
			//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRClassLoader class. Using JRClassLoader.class.getClassLoader() instead.");
		}
	
		if (clazz == null)
		{
			clazz = 
				(new JRClassLoader(JRClassLoader.class.getClassLoader()))
					.loadClass(className, bytecodes);
		}

		return clazz;
	}


	/**
	 *
	 */
	protected Class loadClass(String className, File file) throws IOException
	{
		Class clazz = null;

		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;

		byte[] bytecodes = new byte[10000];
		int ln = 0;

		try
		{
			fis = new FileInputStream(file);
			baos = new ByteArrayOutputStream();

			while ( (ln = fis.read(bytecodes)) > 0 )
			{
				baos.write(bytecodes, 0, ln);
			}

			baos.flush();
		}
		finally
		{
			if (baos != null)
			{
				try
				{
					baos.close();
				}
				catch(IOException e)
				{
				}
			}

			if (fis != null)
			{
				try
				{
					fis.close();
				}
				catch(IOException e)
				{
				}
			}
		}

		bytecodes = baos.toByteArray();
		clazz = this.defineClass(className, bytecodes, 0, bytecodes.length);

		return clazz;
	}


	/**
	 *
	 */
	protected Class loadClass(String className, byte[] bytecodes) throws IOException
	{
		Class clazz = null;

		clazz = this.defineClass(null, bytecodes, 0, bytecodes.length);

		return clazz;
	}


}
