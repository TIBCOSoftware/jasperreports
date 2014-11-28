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
package net.sf.jasperreports.engine.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVirtualizationHelper;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Utility class that helps load serialized objects found in various locations 
 * such as files, URLs, and input streams.
 * <p>
 * Many JasperReports processes, like report compilation, report filling and exporting,
 * often work with serialized objects. Sometimes it is useful to manually load those
 * serialized objects before submitting them to the desired JasperReport process.
 * </p><p>
 * The most interesting method exposed by this class is
 * {@link #getLocationInputStream(String) getLocationInputStream(String location)}. 
 * When calling this method to load an InputStream object from
 * the supplied location, the program first tries to interpret the location as a valid resource name. If
 * this fails, then the program assumes that the supplied location is the name of a file on
 * disk and tries to read from it. If no file is found at that location, it will assume that the location 
 * represents a valid URL. Only after this third
 * try fails an exception is thrown.
 * </p>
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class JRLoader
{

	private static final Log log = LogFactory.getLog(JRLoader.class);

	/**
	 *
	 */
	//private static boolean wasWarning;


	/**
	 *
	 */
	public static Object loadObjectFromFile(String fileName) throws JRException
	{
		return loadObject(new File(fileName));
	}


	/**
	 *
	 */
	public static Object loadObject(File file) throws JRException
	{
		return loadObject(DefaultJasperReportsContext.getInstance(), file);
	}


	/**
	 *
	 */
	public static Object loadObject(JasperReportsContext jasperReportsContext, File file) throws JRException
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
			BufferedInputStream bufferedIn = new BufferedInputStream(fis);
			ois = new ContextClassLoaderObjectInputStream(jasperReportsContext, bufferedIn);
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
		return loadObject(DefaultJasperReportsContext.getInstance(), url);
	}


	/**
	 *
	 */
	public static Object loadObject(JasperReportsContext jasperReportsContext, URL url) throws JRException
	{
		Object obj = null;

		InputStream is = null;
		ObjectInputStream ois = null;

		try
		{
			is = url.openStream();
			ois = new ContextClassLoaderObjectInputStream(jasperReportsContext, is);
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
		return loadObject(DefaultJasperReportsContext.getInstance(), is);
	}


	/**
	 *
	 */
	public static Object loadObject(JasperReportsContext jasperReportsContext, InputStream is) throws JRException
	{
		Object obj = null;

		ObjectInputStream ois = null;

		try
		{
			ois = new ContextClassLoaderObjectInputStream(jasperReportsContext, is);
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

		return obj;
	}


	/**
	 *
	 */
	public static InputStream getInputStream(File file) throws JRException
	{
		if (!file.exists() || !file.isFile())
		{
			throw new JRException( new FileNotFoundException(String.valueOf(file)) );//FIXMEREPO this probably useless
		}

		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(file);
		}
		catch (IOException e)
		{
			throw new JRException("Error opening input stream from file : " + file, e);
		}

		return fis;
	}


	/**
	 *
	 */
	public static InputStream getInputStream(URL url) throws JRException
	{
		InputStream is = null;

		try
		{
			is = url.openStream();
		}
		catch (IOException e)
		{
			throw new JRException("Error opening input stream from URL : " + url, e);
		}

		return is;
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

	public static InputStream loadToMemoryInputStream(InputStream is) throws JRException
	{
		if (is instanceof ByteArrayInputStream)
		{
			return is;
		}
		
		try
		{
			byte[] bytes = loadBytes(is);
			return new ByteArrayInputStream(bytes);
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				if (log.isWarnEnabled())
				{
					log.warn("Failed to close input stream " + is, e);
				}
			}
		}
	}

	/**
	 *
	 */
	public static byte[] loadBytesFromResource(String resourceName) throws JRException
	{
		return loadBytesFromResource(resourceName, null);
	}


	/**
	 *
	 */
	public static byte[] loadBytesFromResource(String resourceName, ClassLoader classLoader) throws JRException
	{
		URL url = JRResourcesUtil.findClassLoaderResource(resourceName, classLoader);
		if (url != null)
		{
			return loadBytes(url);
		}

		throw new JRException("Resource '" + resourceName + "' not found.");
	}
		
	
	/**
	 * Tries to open an input stream for a location.
	 * <p>
	 * The method tries to interpret the location as a file name, a resource name or
	 * an URL.  If any of these succeed, an input stream is created and returned.
	 * 
	 * @param location the location
	 * @return an input stream if the location is an existing file name, a resource name on
	 * the classpath or an URL or <code>null</code> otherwise.
	 * 
	 * @throws JRException
	 */
	public static InputStream getLocationInputStream(String location) throws JRException//FIXME deprecate this?
	{
		InputStream is = null;
		
		is = getResourceInputStream(location);
		
		if (is == null)
		{
			is = getFileInputStream(location);
		}
		
		if (is == null)
		{
			is = getURLInputStream(location);
		}
		
		return is;
	}


	/**
	 * Tries to open a file for reading.
	 * 
	 * @param filename the file name
	 * @return an input stream for the file or <code>null</code> if the file was not found
	 * @throws JRException
	 */
	public static InputStream getFileInputStream(String filename) throws JRException
	{
		InputStream is = null;
		
		File file = new File(filename);
		if (file.exists() && file.isFile())
		{
			try
			{
				is = new FileInputStream(file);
			}
			catch (FileNotFoundException e)
			{
				throw new JRException("Error opening file " + filename, e);
			}
		}
		
		return is;
	}


	/**
	 * Tries to open an input stream for a resource.
	 *  
	 * @param resource the resource name
	 * @return an input stream for the resource or <code>null</code> if the resource was not found
	 */
	public static InputStream getResourceInputStream(String resource)
	{
		InputStream is = null;
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();		
		if (classLoader != null)
		{
			is = classLoader.getResourceAsStream(resource);
		}
		
		if (is == null)
		{
			classLoader = JRLoader.class.getClassLoader();
			if (classLoader != null)
			{
				is = classLoader.getResourceAsStream(resource);
			}
			
			if (is == null)
			{
				is = JRPropertiesUtil.class.getResourceAsStream("/" + resource);
			}
		}

		return is;
	}

	/**
	 * Scans the context classloader and the classloader of this class for all 
	 * resources that have a specified name, and returns a list of
	 * {@link URL}s for the found resources.
	 * 
	 * @param resource the resource names
	 * @return a list of {@link URL}s of resources with the specified name;
	 * the list is empty if no resources have been found for the name
	 * @see ClassLoader#getResources(String)
	 */
	public static List<URL> getResources(String resource)
	{
		//skip duplicated resources
		Set<URL> resources = new LinkedHashSet<URL>();
		collectResources(resource, JRLoader.class.getClassLoader(), 
				resources);
		collectResources(resource, Thread.currentThread().getContextClassLoader(), 
				resources);
		return new ArrayList<URL>(resources);
	}


	protected static void collectResources(String resourceName,
			ClassLoader classLoader, Set<URL> resources)
	{
		if (classLoader != null)
		{
			try
			{
				for (Enumeration<URL> urls = classLoader.getResources(resourceName);
						urls.hasMoreElements();)
				{
					URL url = urls.nextElement();
					resources.add(url);
				}
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}

	/**
	 * Scans the context classloader and the classloader of this class for all 
	 * resources that have a specified name, and returns a list of
	 * {@link ClassLoaderResource} objects for the found resources.
	 * 
	 * <p>
	 * The returned list contains the URLs of the resources, and for each resource
	 * the highest classloader in the classloader hierarchy on which the resource
	 * was found.
	 * </p>
	 * 
	 * @param resource the resource names
	 * @return a list of resources with the specified name;
	 * the list is empty if no resources have been found for the name
	 * @see ClassLoader#getResources(String)
	 */
	public static List<ClassLoaderResource> getClassLoaderResources(
			String resource)
	{
		Map<URL, ClassLoaderResource> resources = 
			new LinkedHashMap<URL, ClassLoaderResource>();
		collectResources(resource, JRLoader.class.getClassLoader(), resources);
		//TODO check if the classloader is the same
		collectResources(resource, Thread.currentThread()
				.getContextClassLoader(), resources);
		return new ArrayList<ClassLoaderResource>(resources.values());
	}

	protected static void collectResources(String resourceName,
			ClassLoader classLoader, Map<URL, ClassLoaderResource> resources)
	{
		if (classLoader == null)
		{
			return;
		}
		
		try
		{
			// creating a list of parent classloaders, with the highest in the
			// hierarchy first
			LinkedList<ClassLoader> classloaders = new LinkedList<ClassLoader>();
			ClassLoader ancestorLoader = classLoader;
			while (ancestorLoader != null)
			{
				classloaders.addFirst(ancestorLoader);
				
				try
				{
					ancestorLoader = ancestorLoader.getParent();
				}
				catch (SecurityException e)
				{
					// if we're not allowed to get the parent, stop here.
					// resources will be listed with the first classloader that
					// we're allowed to access.
					// one case when this happens is applets.
					// FIXME revisit logging on SecurityException for applet
					ancestorLoader = null;
				}
			}

			for (ClassLoader ancestor : classloaders)
			{
				for (Enumeration<URL> urls = ancestor.getResources(resourceName); 
						urls.hasMoreElements();)
				{
					URL url = urls.nextElement();
					// if this is the first time we see this resource, add it
					// with the current classloader.
					// this way a resource will be added with the most first
					// ancestor classloader that has it.
					if (!resources.containsKey(url))
					{
						if (log.isDebugEnabled())
						{
							log.debug("Found resource " + resourceName 
									+ " at "+ url + " in classloader " + ancestor);
						}
						
						ClassLoaderResource resource = new ClassLoaderResource(
								url, ancestor);
						resources.put(url, resource);
					}
				}
			}
		} catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 * Returns the resource URL for a specified resource name.
	 * 
	 * @param resource the resource name
	 * @return the URL of the resource having the specified name, or
	 * <code>null</code> if none found
	 * @see ClassLoader#getResource(String)
	 */
	public static URL getResource(String resource)
	{
		URL location = null;
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();		
		if (classLoader != null)
		{
			location = classLoader.getResource(resource);
		}
		
		if (location == null)
		{
			classLoader = JRLoader.class.getClassLoader();
			if (classLoader != null)
			{
				location = classLoader.getResource(resource);
			}
			
			if (location == null)
			{
				location = JRPropertiesUtil.class.getResource("/" + resource);
			}
		}

		return location;
	}

	/**
	 * Tries to open an input stream for an URL.
	 * 
	 * @param spec the string to parse as an URL
	 * @return an input stream for the URL or null if <code>spec</code> is not a valid URL
	 * @throws JRException
	 */
	public static InputStream getURLInputStream(String spec) throws JRException
	{
		InputStream is = null;
		
		try
		{
			URL url = new URL(spec);
			is = url.openStream();
		}
		catch (MalformedURLException e)
		{
		}
		catch (IOException e)
		{
			throw new JRException("Error opening URL " + spec, e);
		}
		
		return is;
	}
	
	/**
	 * Loads a JasperPrint object from a file, optionally using a virtualizer for
	 * the object.
	 * 
	 * @param fileName the file name
	 * @param virtualizer the virtualizer
	 * @return a JasperPrint object
	 * @throws JRException
	 * @see JRVirtualizationHelper#setThreadVirtualizer(JRVirtualizer)
	 */
	public static JasperPrint loadJasperPrintFromFile(String fileName, JRVirtualizer virtualizer) throws JRException
	{
		if (virtualizer != null)
		{
			JRVirtualizationHelper.setThreadVirtualizer(virtualizer);
		}
		try
		{
			return (JasperPrint) loadObjectFromFile(fileName);
		}
		finally
		{
			if (virtualizer != null)
			{
				JRVirtualizationHelper.clearThreadVirtualizer();
			}
		}
	}
	
	/**
	 * Loads a JasperPrint object from a file, optionally using a virtualizer for
	 * the object.
	 * 
	 * @param file the file
	 * @param virtualizer the virtualizer
	 * @return a JasperPrint object
	 * @throws JRException
	 * @see JRVirtualizationHelper#setThreadVirtualizer(JRVirtualizer)
	 */
	public static JasperPrint loadJasperPrint(File file, JRVirtualizer virtualizer) throws JRException
	{
		if (virtualizer != null)
		{
			JRVirtualizationHelper.setThreadVirtualizer(virtualizer);
		}
		try
		{
			return (JasperPrint) loadObject(file);
		}
		finally
		{
			if (virtualizer != null)
			{
				JRVirtualizationHelper.clearThreadVirtualizer();
			}
		}
	}

	/**
	 * Loads a JasperPrint object from a URL, optionally using a virtualizer for
	 * the object.
	 * 
	 * @param url the URL
	 * @param virtualizer the virtualizer
	 * @return a JasperPrint object
	 * @throws JRException
	 * @see JRVirtualizationHelper#setThreadVirtualizer(JRVirtualizer)
	 */
	public static JasperPrint loadJasperPrint(URL url, JRVirtualizer virtualizer) throws JRException
	{
		if (virtualizer != null)
		{
			JRVirtualizationHelper.setThreadVirtualizer(virtualizer);
		}
		try
		{
			return (JasperPrint) loadObject(url);
		}
		finally
		{
			if (virtualizer != null)
			{
				JRVirtualizationHelper.clearThreadVirtualizer();
			}
		}
	}
	
	/**
	 * Loads a JasperPrint object from a stream, optionally using a virtualizer for
	 * the object.
	 * 
	 * @param is the stream
	 * @param virtualizer the virtualizer
	 * @return a JasperPrint object
	 * @throws JRException
	 * @see JRVirtualizationHelper#setThreadVirtualizer(JRVirtualizer)
	 */
	public static JasperPrint loadJasperPrint(InputStream is, JRVirtualizer virtualizer) throws JRException
	{
		if (virtualizer != null)
		{
			JRVirtualizationHelper.setThreadVirtualizer(virtualizer);
		}
		try
		{
			return (JasperPrint) loadObject(is);
		}
		finally
		{
			if (virtualizer != null)
			{
				JRVirtualizationHelper.clearThreadVirtualizer();
			}
		}
	}
	
	private JRLoader()
	{
	}
}
