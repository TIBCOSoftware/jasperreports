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
package net.sf.jasperreports.engine.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class JRLoader
{

	private static final Log log = LogFactory.getLog(JRLoader.class);

	/**
	 *
	 */
	//private static boolean wasWarning;


	/**
	 * @deprecated Replaced by {@link #loadObjectFromFile(String)}.
	 */
	public static Object loadObject(String fileName) throws JRException
	{
		return loadObjectFromFile(fileName);
	}


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
			ois = new ContextClassLoaderObjectInputStream(bufferedIn);
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
			ois = new ContextClassLoaderObjectInputStream(is);
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
			ois = new ContextClassLoaderObjectInputStream(is);
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
	 * @deprecated Replaced by {@link RepositoryUtil#getResource(String, Class)}.
	 */
	public static Object loadObjectFromLocation(String location) throws JRException
	{
		return loadObjectFromLocation(location, null, null, null);
	}


	/**
	 * @deprecated Replaced by {@link RepositoryUtil#getResource(String, Class)}.
	 */
	public static Object loadObjectFromLocation(String location, ClassLoader classLoader) throws JRException
	{
		return loadObjectFromLocation(location, classLoader, null, null);
	}

	
	/**
	 * @deprecated Replaced by {@link RepositoryUtil#getResource(String, Class)}.
	 */
	public static Object loadObjectFromLocation(
		String location, 
		ClassLoader classLoader,
		URLStreamHandlerFactory urlHandlerFactory,
		FileResolver fileResolver
		) throws JRException
	{
		URL url = JRResourcesUtil.createURL(location, urlHandlerFactory);
		if (url != null)
		{
			return loadObject(url);
		}

		File file = JRResourcesUtil.resolveFile(location, fileResolver);
		if (file != null)
		{
			return loadObject(file);
		}

		url = JRResourcesUtil.findClassLoaderResource(location, classLoader);
		if (url != null)
		{
			return loadObject(url);
		}

		throw new JRException("Could not load object from location : " + location);
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
	 * @deprecated Replaced by {@link RepositoryUtil#getInputStream(String)}.
	 */
	public static InputStream getInputStreamFromLocation(String location) throws JRException
	{
		return getInputStreamFromLocation(location, null, null, null);
	}
	
	
	/**
	 * @deprecated Replaced by {@link RepositoryUtil#getInputStream(String)}.
	 */
	public static InputStream getInputStreamFromLocation(
		String location, 
		ClassLoader classLoader,
		URLStreamHandlerFactory urlHandlerFactory,
		FileResolver fileResolver
		) throws JRException
	{
		URL url = JRResourcesUtil.createURL(location, urlHandlerFactory);
		if (url != null)
		{
			return getInputStream(url);
		}

		File file = JRResourcesUtil.resolveFile(location, fileResolver);
		if (file != null)
		{
			return getInputStream(file);
		}

		url = JRResourcesUtil.findClassLoaderResource(location, classLoader);
		if (url != null)
		{
			return getInputStream(url);
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
	 * @deprecated Replaced by {@link RepositoryUtil#getBytes(String)}.
	 */
	public static byte[] loadBytesFromLocation(String location) throws JRException
	{
		return loadBytesFromLocation(location, null, null, null);
	}


	/**
	 * @deprecated Replaced by {@link RepositoryUtil#getBytes(String)}.
	 */
	public static byte[] loadBytesFromLocation(String location, ClassLoader classLoader) throws JRException
	{
		return loadBytesFromLocation(location, classLoader, null, null);
	}
		
	
	/**
	 * @deprecated Replaced by {@link RepositoryUtil#getBytes(String)}.
	 */
	public static byte[] loadBytesFromLocation(
		String location, 
		ClassLoader classLoader,
		URLStreamHandlerFactory urlHandlerFactory
		) throws JRException
	{
		return loadBytesFromLocation(location, classLoader, urlHandlerFactory, null);
	}
		
	
	/**
	 * @deprecated Replaced by {@link RepositoryUtil#getBytes(String)}.
	 */
	public static byte[] loadBytesFromLocation(
		String location, 
		ClassLoader classLoader,
		URLStreamHandlerFactory urlHandlerFactory,
		FileResolver fileResolver
		) throws JRException
	{
		URL url = JRResourcesUtil.createURL(location, urlHandlerFactory);
		if (url != null)
		{
			return loadBytes(url);
		}

		File file = JRResourcesUtil.resolveFile(location, fileResolver);
		if (file != null)
		{
			return loadBytes(file);
		}

		url = JRResourcesUtil.findClassLoaderResource(location, classLoader);
		if (url != null)
		{
			return loadBytes(url);
		}

		throw new JRException("Byte data not found at location : " + location);
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
				is = JRProperties.class.getResourceAsStream("/" + resource);
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
				location = JRProperties.class.getResource("/" + resource);
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
	
	
	private JRLoader()
	{
	}
}
