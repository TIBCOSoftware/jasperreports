/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
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
