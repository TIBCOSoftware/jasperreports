/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Provides methods for resource resolution via class loaders or URL stream handlers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRResourcesUtil
{
	private static URLStreamHandlerFactory globalURLHandlerFactory;
	private static ThreadLocalStack localURLHandlerFactoryStack = new ThreadLocalStack();

	private static ClassLoader globalClassLoader;
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
	 * @param urlHandlerFactory an URL stream handler factory
	 * @return an URL stream handler if one was found for the protocol of the URL
	 * @see #getURLHandlerFactory(URLStreamHandlerFactory)
	 */
	public static URLStreamHandler getURLHandler(String spec, URLStreamHandlerFactory urlHandlerFactory)
	{
		urlHandlerFactory = getURLHandlerFactory(urlHandlerFactory);

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

	
	private static String getURLProtocol(String spec)
	{
		String protocol = null;
		
		spec = spec.trim();
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
	 * Returns an URL steam handler factory.
	 * <p/>
	 * The first not null value from the following is returned:
	 * <ul>
	 * 	<li>the value of the parameter</li>
	 * 	<li>the thread local URL stream handler factory</li>
	 * 	<li>the global URL stream handler factory</li>
	 * </ul>
	 * 
	 * @param urlHandlerFactory an URL steam handler factory that will be returned if not null
	 * @return an URL steam handler factory
	 * @see #setGlobalURLHandlerFactory(URLStreamHandlerFactory)
	 * @see #setThreadURLHandlerFactory(URLStreamHandlerFactory)
	 */
	public static URLStreamHandlerFactory getURLHandlerFactory(URLStreamHandlerFactory urlHandlerFactory)
	{
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
	 */
	public static void setThreadURLHandlerFactory(URLStreamHandlerFactory urlHandlerFactory)
	{
		localURLHandlerFactoryStack.push(urlHandlerFactory);
	}
	
	
	/**
	 * Resets the the thread local URL stream handler factory to its previous value.
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
	 * @param classLoader a class loader that will be returned if not null
	 * @return a class loader.
	 * @see #setGlobalClassLoader(ClassLoader)
	 * @see #setThreadClassLoader(ClassLoader)
	 */
	public static ClassLoader getClassLoader(ClassLoader classLoader)
	{
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
	 * 	<li>the class loader returned by {@link #getClassLoader(ClassLoader) <code>getClassLoader(classLoader)</code>}</li>
	 * 	<li>the context class loader</li>
	 * 	<li><code>clazz.getClassLoader()</code></li>
	 * 	<li><code>clazz.getResource()</code></li>
	 * </ul>
	 * 
	 * @param location the resource name
	 * @param classLoader a class loader
	 * @param clazz a class
	 * @return the resource URL if found
	 */
	public static URL findClassLoaderResource(String location, ClassLoader classLoader, Class clazz)
	{
		classLoader = getClassLoader(classLoader);
		
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
	 * 	<li>the class loader returned by {@link #getClassLoader(ClassLoader) <code>getClassLoader(classLoader)</code>}</li>
	 * 	<li>the context class loader</li>
	 * 	<li><code>JRClassLoader.class.getClassLoader()</code></li>
	 * </ul>
	 * </p>
	 * 
	 * @param baseName the base name
	 * @param locale the locale
	 * @param classLoader 
	 * @return the resource bundle for the given base name and locale
	 * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
	 */
	public static ResourceBundle loadResourceBundle(String baseName, Locale locale, ClassLoader classLoader)
	{
		ResourceBundle resourceBundle = null;
		
		classLoader = getClassLoader(classLoader);
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
	
}
