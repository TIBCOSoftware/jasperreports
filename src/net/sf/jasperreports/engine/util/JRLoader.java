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
package dori.jasper.engine.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import dori.jasper.engine.JRException;


/**
 *
 */
public class JRLoader
{


	/**
	 *
	 */
	//private static boolean wasWarning = false;


	/**
	 *
	 */
	public static Object loadObject(String fileName) throws JRException
	{
		return loadObject(new File(fileName));
	}


	/**
	 *
	 */
	public static Object loadObject(File file) throws JRException
	{
		if (!file.exists() || !file.isFile())
		{
			throw new JRException( new FileNotFoundException(String.valueOf(file)) );
		}

		Object obj = null;

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try
		{
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			obj = ois.readObject();
		}
		catch (IOException e)
		{
			throw new JRException("Error loading object from file : " + file, e);
		}
		catch (ClassNotFoundException e)
		{
			throw new JRException("Class not found when loading object from file : " + file, e);
		}
		finally
		{
			if (ois != null)
			{
				try
				{
					ois.close();
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

		return obj;
	}


	/**
	 *
	 */
	public static Object loadObject(URL url) throws JRException
	{
		Object obj = null;

		InputStream is = null;
		ObjectInputStream ois = null;

		try
		{
			is = url.openStream();
			ois = new ObjectInputStream(is);
			obj = ois.readObject();
		}
		catch (IOException e)
		{
			throw new JRException("Error loading object from URL : " + url, e);
		}
		catch (ClassNotFoundException e)
		{
			throw new JRException("Class not found when loading object from URL : " + url, e);
		}
		finally
		{
			if (ois != null)
			{
				try
				{
					ois.close();
				}
				catch(IOException e)
				{
				}
			}

			if (is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
				}
			}
		}

		return obj;
	}


	/**
	 *
	 */
	public static Object loadObject(InputStream is) throws JRException
	{
		Object obj = null;

		ObjectInputStream ois = null;

		try
		{
			ois = new ObjectInputStream(is);
			obj = ois.readObject();
		}
		catch (IOException e)
		{
			throw new JRException("Error loading object from InputStream", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new JRException("Class not found when loading object from InputStream", e);
		}
		finally
		{
			//FIXME should not close the stream
			if (ois != null)
			{
				try
				{
					ois.close();
				}
				catch(IOException e)
				{
				}
			}
		}

		return obj;
	}


	/**
	 *
	 */
	public static Object loadObjectFromLocation(String location) throws JRException
	{
		try
		{
			URL url = new URL(location);
			return loadObject(url);
		}
		catch(MalformedURLException e)
		{
		}

		File file = new File(location);
		if (file.exists() && file.isFile())
		{
			return loadObject(file);
		}

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(location);
		if (url == null)
		{
			//if (!wasWarning)
			//{
			//	if (log.isWarnEnabled())
			//		log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRLoader class. Using JRLoader.class.getClassLoader() instead.");
			//	wasWarning = true;
			//}
			classLoader = JRLoader.class.getClassLoader();
			url = classLoader.getResource(location);
		}

		if (url != null)
		{
			return loadObject(url);
		}

		throw new JRException("Could not load object from location : " + location);
	}


	/**
	 *
	 */
	public static byte[] loadBytes(File file) throws JRException
	{
		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(file);
			baos = new ByteArrayOutputStream();

			byte[] bytes = new byte[10000];
			int ln = 0;
			while ((ln = fis.read(bytes)) > 0)
			{
				baos.write(bytes, 0, ln);
			}

			baos.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error loading byte data : " + file, e);
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

		return baos.toByteArray();
	}


	/**
	 *
	 */
	public static byte[] loadBytes(URL url) throws JRException
	{
		ByteArrayOutputStream baos = null;
		InputStream is = null;

		try
		{
			is = url.openStream();
			baos = new ByteArrayOutputStream();

			byte[] bytes = new byte[10000];
			int ln = 0;
			while ((ln = is.read(bytes)) > 0)
			{
				baos.write(bytes, 0, ln);
			}

			baos.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error loading byte data : " + url, e);
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

			if (is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
				}
			}
		}

		return baos.toByteArray();
	}


	/**
	 *
	 */
	public static byte[] loadBytes(InputStream is) throws JRException
	{
		ByteArrayOutputStream baos = null;

		try
		{
			baos = new ByteArrayOutputStream();

			byte[] bytes = new byte[10000];
			int ln = 0;
			while ((ln = is.read(bytes)) > 0)
			{
				baos.write(bytes, 0, ln);
			}

			baos.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error loading byte data from input stream.", e);
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
		}

		return baos.toByteArray();
	}


	/**
	 *
	 */
	public static byte[] loadBytesFromLocation(String location) throws JRException
	{
		try
		{
			URL url = new URL(location);
			return loadBytes(url);
		}
		catch(MalformedURLException e)
		{
		}

		File file = new File(location);
		if (file.exists() && file.isFile())
		{
			return loadBytes(file);
		}

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(location);
		if (url == null)
		{
			//if (!wasWarning)
			//{
			//	if (log.isWarnEnabled())
			//		log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRLoader class. Using JRLoader.class.getClassLoader() instead.");
			//	wasWarning = true;
			//}
			classLoader = JRLoader.class.getClassLoader();
			url = classLoader.getResource(location);
		}

		if (url != null)
		{
			return loadBytes(url);
		}

		throw new JRException("Byte data not found at location : " + location);
	}


}
