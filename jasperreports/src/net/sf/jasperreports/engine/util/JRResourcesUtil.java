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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.repo.ResourceBundleResource;


/**
 * Provides methods for resource resolution via class loaders or URL stream handlers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public final class JRResourcesUtil
{
	/**
	 * 
	 */
	private static final String PROPERTIES_FILE_EXTENSION = ".properties";

	/** 
	 * @deprecated To be removed.
	 */
	private static FileResolver globalFileResolver;
	/** 
	 * @deprecated To be removed.
	 */
	private static ThreadLocalStack localFileResolverStack = new ThreadLocalStack();

	/** 
	 * @deprecated To be removed.
	 */
	private static URLStreamHandlerFactory globalURLHandlerFactory;
	/** 
	 * @deprecated To be removed.
	 */
	private static ThreadLocalStack localURLHandlerFactoryStack = new ThreadLocalStack();

	/** 
	 *
	 */
	private static ClassLoader globalClassLoader;
	/** 
	 *
	 */
	private static ThreadLocalStack localClassLoaderStack = new ThreadLocalStack();

	
	/**
	 * Tries to parse a <code>String</code> as an URL.
	 * 
	 * @param spec the <code>String</code> to parse
	 * @param urlHandlerFactory an URL stream handler factory to use
	 * @return an URL if the parsing is successful
	 * @see #getURLHandler(String, URLStreamHandlerFactory)
	 * @see #getURLHandlerFactory(URLStreamHandlerFactory)
	 */
	public static URL createURL(String spec, URLStreamHandlerFactory urlHandlerFactory)
	{
		URLStreamHandler handler = getURLHandler(spec, urlHandlerFactory);
		URL url;
		try
		{
			if (handler == null)
			{
				url = new URL(spec);
			}
			else
			{
				url = new URL(null, spec, handler);
			}
		}
		catch (MalformedURLException e)
		{
			url = null;
		}
		return url;
	}
	
	
	/**
	 * Returns an URL stream handler for an URL specified as a <code>String</code>.
	 * 
	 * @param spec the <code>String</code> to parse as an URL
	 * @param urlHandlerFact an URL stream handler factory
	 * @return an URL stream handler if one was found for the protocol of the URL
	 * @see #getURLHandlerFactory(URLStreamHandlerFactory)
	 */
	public static URLStreamHandler getURLHandler(String spec, URLStreamHandlerFactory urlHandlerFact)
	{
		URLStreamHandlerFactory urlHandlerFactory = urlHandlerFact;//getURLHandlerFactory(urlHandlerFact);

		URLStreamHandler handler = null;
		if (urlHandlerFactory != null)
		{
			String protocol = getURLProtocol(spec);
			if (protocol != null)
			{
				handler = urlHandlerFactory.createURLStreamHandler(protocol);
			}
		}
		return handler;
	}

	
	private static String getURLProtocol(String urlSpec)
	{
		String protocol = null;
		
		String spec = urlSpec.trim();
		int colon = spec.indexOf(':');
		if (colon > 0)
		{
			String proto = spec.substring(0, colon);
			if (protocolValid(proto))
			{
				protocol = proto;
			}
		}
		
		return protocol;
	}

	private static boolean protocolValid(String protocol)
	{
		int length = protocol.length();
		if (length < 1)
		{
			return false;
		}
		
		if (!Character.isLetter(protocol.charAt(0)))
		{
			return false;
		}
		
		for (int i = 1; i < length; ++i)
		{
			char c = protocol.charAt(i);
			if (!(Character.isLetterOrDigit(c) || c == '+' || c == '-' || c == '.'))
			{
				return false;
			}
		}
		
		return true;
	}

	
	/**
	 * Returns a file resolver.
	 * <p/>
	 * The first not null value from the following is returned:
	 * <ul>
	 * 	<li>the value of the parameter</li>
	 * 	<li>the thread local file resolver</li>
	 * 	<li>the global file resolver</li>
	 * </ul>
	 * 
	 * @param fileRes a file resolver that will be returned if not null
	 * @return a file resolver
	 * @see #setGlobalFileResolver(FileResolver)
	 * @see #setThreadFileResolver(FileResolver)
	 * @deprecated To be removed.
	 */
	public static FileResolver getFileResolver(FileResolver fileRes)
	{
		FileResolver fileResolver = fileRes;
		if (fileResolver == null)
		{
			fileResolver = getThreadFileResolver();
			if (fileResolver == null)
			{
				fileResolver = globalFileResolver;
			}
		}
		return fileResolver;
	}

	
	/**
	 * Returns the global file resolver.
	 * 
	 * @return the global file resolver
	 * @see #setGlobalFileResolver(FileResolver)
	 * @deprecated To be removed.
	 */
	public static FileResolver getGlobalFileResolver()
	{
		return globalFileResolver;
	}

	
	/**
	 * Returns the thread local file resolver.
	 * 
	 * @return the thread local file resolver.
	 * @see #setThreadFileResolver(FileResolver)
	 * @deprecated To be removed.
	 */
	public static FileResolver getThreadFileResolver()
	{
		return (FileResolver) localFileResolverStack.top();
	}

	
	/**
	 * Sets the thread local file resolver.
	 * 
	 * @param fileResolver a file resolver.
	 * @see #getFileResolver(FileResolver)
	 * @see #resetThreadFileResolver()
	 * @deprecated To be removed.
	 */
	public static void setThreadFileResolver(FileResolver fileResolver)
	{
		localFileResolverStack.push(fileResolver);
	}
	
	
	/**
	 * Resets the the thread local file resolver to its previous value.
	 * @deprecated To be removed.
	 */
	public static void resetThreadFileResolver()
	{
		localFileResolverStack.pop();
	}

	/**
	 * Sets a global file resolver to be used for file resolution.
	 * 
	 * @param fileResolver the file resolver
	 * @see #getFileResolver(FileResolver)
	 * @deprecated To be removed.
	 */
	public static void setGlobalFileResolver(FileResolver fileResolver)
	{
		globalFileResolver = fileResolver;
	}

	
	/**
	 * Attempts to find a file using a file resolver.
	 * 
	 * @param location file name
	 * @param fileRes a file resolver
	 * @return the file, if found
	 */
	public static File resolveFile(String location, FileResolver fileRes)
	{
		FileResolver fileResolver = fileRes;//getFileResolver(fileRes);
		
		if (fileResolver != null)
		{
			return fileResolver.resolveFile(location);
		}

		File file = new File(location);
		if (file.exists() && file.isFile())
		{
			return file;
		}
		
		return null;
	}


	/**
	 * Returns an URL steam handler factory.
	 * <p/>
	 * The first not null value from the following is returned:
	 * <ul>
	 * 	<li>the value of the parameter</li>
	 * 	<li>the thread local URL stream handler factory</li>
	 * 	<li>the global URL stream handler factory</li>
	 * </ul>
	 * 
	 * @param urlHandlerFact an URL steam handler factory that will be returned if not null
	 * @return an URL steam handler factory
	 * @see #setGlobalURLHandlerFactory(URLStreamHandlerFactory)
	 * @see #setThreadURLHandlerFactory(URLStreamHandlerFactory)
	 * @deprecated To be removed.
	 */
	public static URLStreamHandlerFactory getURLHandlerFactory(URLStreamHandlerFactory urlHandlerFact)
	{
		URLStreamHandlerFactory urlHandlerFactory = urlHandlerFact;
		if (urlHandlerFactory == null)
		{
			urlHandlerFactory = getThreadURLStreamHandlerFactory();
			if (urlHandlerFactory == null)
			{
				urlHandlerFactory = globalURLHandlerFactory;
			}
		}
		return urlHandlerFactory;
	}

	
	/**
	 * Returns the global URL stream handler factory.
	 * 
	 * @return the global URL stream handler factory
	 * @see #setGlobalURLHandlerFactory(URLStreamHandlerFactory)
	 * @deprecated To be removed.
	 */
	public static URLStreamHandlerFactory getGlobalURLStreamHandlerFactory()
	{
		return globalURLHandlerFactory;
	}

	
	/**
	 * Returns the thread local URL stream handler factory.
	 * 
	 * @return the thread local URL stream handler factory.
	 * @see #setThreadURLHandlerFactory(URLStreamHandlerFactory)
	 * @deprecated To be removed.
	 */
	public static URLStreamHandlerFactory getThreadURLStreamHandlerFactory()
	{
		return (URLStreamHandlerFactory) localURLHandlerFactoryStack.top();
	}

	
	/**
	 * Sets the thread local URL stream handler factory.
	 * 
	 * @param urlHandlerFactory an URL stream handler factory.
	 * @see #getURLHandlerFactory(URLStreamHandlerFactory)
	 * @see #resetThreadURLHandlerFactory()
	 * @deprecated To be removed.
	 */
	public static void setThreadURLHandlerFactory(URLStreamHandlerFactory urlHandlerFactory)
	{
		localURLHandlerFactoryStack.push(urlHandlerFactory);
	}
	
	
	/**
	 * Resets the the thread local URL stream handler factory to its previous value.
	 * @deprecated To be removed.
	 */
	public static void resetThreadURLHandlerFactory()
	{
		localURLHandlerFactoryStack.pop();
	}

	/**
	 * Sets a global URL stream handler facotry to be used for resource resolution.
	 * 
	 * @param urlHandlerFactory the URL stream handler factory
	 * @see #getURLHandlerFactory(URLStreamHandlerFactory)
	 * @deprecated To be removed.
	 */
	public static void setGlobalURLHandlerFactory(URLStreamHandlerFactory urlHandlerFactory)
	{
		globalURLHandlerFactory = urlHandlerFactory;
	}

	
	/**
	 * Returns a class loader.
	 * <p/>
	 * The first not null value from the following is returned:
	 * <ul>
	 * 	<li>the value of the parameter</li>
	 * 	<li>the thread local class loader</li>
	 * 	<li>the global class loader</li>
	 * </ul>
	 * 
	 * @param clsLoader a class loader that will be returned if not null
	 * @return a class loader.
	 * @see #setGlobalClassLoader(ClassLoader)
	 * @see #setThreadClassLoader(ClassLoader)
	 */
	public static ClassLoader getClassLoader(ClassLoader clsLoader)
	{
		ClassLoader classLoader = clsLoader;
		if (classLoader == null)
		{
			classLoader = getThreadClassLoader();
			if (classLoader == null)
			{
				classLoader = globalClassLoader;
			}
		}
		return classLoader;
	}

	
	/**
	 * Returns the global class loader.
	 * 
	 * @return the global class loader.
	 * @see #setGlobalClassLoader(ClassLoader)
	 */
	public static ClassLoader getGlobalClassLoader()
	{
		return globalClassLoader;
	}

	
	/**
	 * Returns the thread local class loader.
	 * 
	 * @return the thread local class loader.
	 * @see #setThreadClassLoader(ClassLoader)
	 */
	public static ClassLoader getThreadClassLoader()
	{
		return (ClassLoader) localClassLoaderStack.top();
	}


	/**
	 * Sets the thread local class loader.
	 * 
	 * @param classLoader a class loader
	 * @see #getClassLoader(ClassLoader)
	 * @see #resetThreadURLHandlerFactory()
	 */
	public static void setThreadClassLoader(ClassLoader classLoader)
	{
		localClassLoaderStack.push(classLoader);
	}

	
	/**
	 * Resets the the thread local class loader to its previous value.
	 */
	public static void resetClassLoader()
	{
		localClassLoaderStack.pop();
	}

	
	/**
	 * Sets a global class loader to be used for resource resolution.
	 * 
	 * @param classLoader the class loader
	 * @see #getClassLoader(ClassLoader)
	 */
	public static void setGlobalClassLoader(ClassLoader classLoader)
	{
		globalClassLoader = classLoader;
	}
	
	
	/**
	 * Attempts to find a resource using a class loader.
	 * <p/>
	 * The following sources are tried:
	 * <ul>
	 * 	<li>the class loader returned by {@link #getClassLoader(ClassLoader) getClassLoader(ClassLoader)}</li>
	 * 	<li>the context class loader</li>
	 * 	<li><code>clazz.getClassLoader()</code></li>
	 * 	<li><code>clazz.getResource()</code></li>
	 * </ul>
	 * 
	 * @param location the resource name
	 * @param clsLoader a class loader
	 * @param clazz a class
	 * @return the resource URL if found
	 * @deprecated Replaced by {@link #findClassLoaderResource(String, ClassLoader)}.
	 */
	public static URL findClassLoaderResource(String location, ClassLoader clsLoader, Class<?> clazz)
	{
		ClassLoader classLoader = getClassLoader(clsLoader);
		
		URL url = null;
		
		if (classLoader != null)
		{
			url = classLoader.getResource(location);
		}
		
		if (url == null)
		{
			classLoader = Thread.currentThread().getContextClassLoader();

			if (classLoader != null)
			{
				url = classLoader.getResource(location);
			}

			if (url == null)
			{
				classLoader = clazz.getClassLoader();
				if (classLoader == null)
				{
					url = clazz.getResource("/" + location);
				}
				else
				{
					url = classLoader.getResource(location);
				}
			}
		}
		
		return url;
	}

	/**
	 * Attempts to find a resource using a class loader.
	 * <p/>
	 * The following sources are tried:
	 * <ul>
	 * 	<li>the class loader returned by {@link #getClassLoader(ClassLoader) getClassLoader(ClassLoader)}</li>
	 * 	<li>the context class loader</li>
	 * 	<li><code>JRLoader.class.getClassLoader()</code></li>
	 * 	<li><code>JRLoader.class.getResource()</code></li>
	 * </ul>
	 * 
	 * @param location the resource name
	 * @param clsLoader a class loader
	 * @return the resource URL if found
	 */
	public static URL findClassLoaderResource(String location, ClassLoader clsLoader)
	{
		ClassLoader classLoader = getClassLoader(clsLoader);
		
		URL url = null;
		
		if (classLoader != null)
		{
			url = classLoader.getResource(location);
		}
		
		if (url == null)
		{
			classLoader = Thread.currentThread().getContextClassLoader();

			if (classLoader != null)
			{
				url = classLoader.getResource(location);
			}

			if (url == null)
			{
				classLoader = JRLoader.class.getClassLoader();
				if (classLoader == null)
				{
					url = JRLoader.class.getResource("/" + location);
				}
				else
				{
					url = classLoader.getResource(location);
				}
			}
		}
		
		return url;
	}

	/**
	 * Loads a resource bundle for a given base name and locale.
	 * 
	 * <p>
	 * This methods calls {@link #loadResourceBundle(String, Locale, ClassLoader)} with a null classloader.
	 * </p>
	 * 
	 * @param baseName the base name
	 * @param locale the locale
	 * @return the resource bundle for the given base name and locale
	 */
	public static ResourceBundle loadResourceBundle(JasperReportsContext jasperReportsContext, String baseName, Locale locale)
	{
		ResourceBundle resourceBundle = null;
		MissingResourceException ex = null;
		try
		{
			resourceBundle = loadResourceBundle(baseName, locale, null);
		}
		catch (MissingResourceException e)
		{
			ex = e;
		}
		
		if (resourceBundle == null)
		{
			CustomControl control = new CustomControl();
			List<Locale> locales = control.getCandidateLocales(baseName, locale);
			for (Locale lc : locales)
			{
				String suffix = lc.toString();
				suffix = (suffix.trim().length() > 0 ? "_" : "") + suffix;
				ResourceBundleResource resourceBundleResource = null; 
				try
				{
					resourceBundleResource = 
							RepositoryUtil.getInstance(jasperReportsContext).getResourceFromLocation(
								baseName + suffix + PROPERTIES_FILE_EXTENSION, 
								ResourceBundleResource.class
								);
				}
				catch (JRException e)
				{
				}
				if (resourceBundleResource != null)
				{
					resourceBundle = resourceBundleResource.getResourceBundle();
					break;
				}
			}
		}
		
		if (resourceBundle == null)
		{
			throw ex;
		}
		
		return resourceBundle;
	}

	/**
	 * Loads a resource bundle for a given base name and locale.
	 * 
	 * <p>
	 * This methods calls {@link #loadResourceBundle(String, Locale, ClassLoader)} with a null classloader.
	 * </p>
	 * 
	 * @param baseName the base name
	 * @param locale the locale
	 * @return the resource bundle for the given base name and locale
	 */
	public static ResourceBundle loadResourceBundle(String baseName, Locale locale)
	{
		return loadResourceBundle(baseName, locale, null);
	}
	
	/**
	 * Loads a resource bundle for a given base name and locale.
	 * 
	 * <p>
	 * The method attempts to load the resource bundle using the following classloaders
	 * (and stops at the first successful attempt):
	 * <ul>
	 * 	<li>the class loader returned by {@link #getClassLoader(ClassLoader) getClassLoader(ClassLoader)}</li>
	 * 	<li>the context class loader</li>
	 * 	<li><code>JRClassLoader.class.getClassLoader()</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @param baseName the base name
	 * @param locale the locale
	 * @param clsLoader 
	 * @return the resource bundle for the given base name and locale
	 * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
	 */
	public static ResourceBundle loadResourceBundle(String baseName, Locale locale, ClassLoader clsLoader)
	{
		ResourceBundle resourceBundle = null;
		
		ClassLoader classLoader = getClassLoader(clsLoader);
		if (classLoader != null)
		{
			try
			{
				resourceBundle = ResourceBundle.getBundle(baseName, locale, classLoader);
			}
			catch (MissingResourceException e)
			{
			}
		}
		
		if (resourceBundle == null)
		{
			classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader != null)
			{
				try
				{
					resourceBundle = ResourceBundle.getBundle(baseName, locale, classLoader);
				}
				catch (MissingResourceException e)
				{
				}
			}
		}

		if (resourceBundle == null)
		{
			classLoader = JRClassLoader.class.getClassLoader();
			if (classLoader == null)
			{
				resourceBundle = ResourceBundle.getBundle(baseName, locale);
			}
			else
			{
				resourceBundle = ResourceBundle.getBundle(baseName, locale, classLoader);
			}
		}

		return resourceBundle;
	}
	

	private JRResourcesUtil()
	{
	}
}


/**
 * 
 */
class CustomControl extends ResourceBundle.Control
{
	public CustomControl()
	{
	}
}
