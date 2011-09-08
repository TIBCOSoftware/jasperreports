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
package net.sf.jasperreports.extensions;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.ClassUtils;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class that provides means of setting and accessing
 * {@link ExtensionsRegistry} instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see #getExtensionsRegistry()
 */
public final class ExtensionsEnvironment
{

	private ExtensionsEnvironment()
	{
	}
	
	private static final Log log = LogFactory.getLog(ExtensionsEnvironment.class); 

	protected final static Object NULL_CACHE_KEY = new Object();
	
	/**
	 * A property that provides the default {@link ExtensionsRegistry} 
	 * implementation class. 
	 * 
	 * <p>
	 * This property is only read at initialization time, therefore changing
	 * the property value at a later time will have no effect. 
	 */
	public static final String PROPERTY_EXTENSIONS_REGISTRY_CLASS = 
		JRProperties.PROPERTY_PREFIX + "extensions.registry.class";
	
	private static ExtensionsRegistry systemRegistry;
	private static final ThreadLocal<ExtensionsRegistry> threadRegistry = new InheritableThreadLocal<ExtensionsRegistry>();
	
	static
	{
		systemRegistry = createDefaultRegistry();
	}
	
	private static ExtensionsRegistry createDefaultRegistry()
	{
		String registryClass = JRProperties.getProperty(PROPERTY_EXTENSIONS_REGISTRY_CLASS);
		
		if (log.isDebugEnabled())
		{
			log.debug("Instantiating extensions registry class " + registryClass);
		}
		
		ExtensionsRegistry registry = (ExtensionsRegistry) ClassUtils.
			instantiateClass(registryClass, ExtensionsRegistry.class);
		return registry;
	}
	
	/**
	 * Returns the system default extensions registry object.
	 * 
	 * <p>
	 * This is either the one instantiated based on {@link #PROPERTY_EXTENSIONS_REGISTRY_CLASS},
	 * or the one set by {@link #setSystemExtensionsRegistry(ExtensionsRegistry)}.
	 * 
	 * @return the system default extensions registry object
	 */
	public static synchronized ExtensionsRegistry getSystemExtensionsRegistry()
	{
		return systemRegistry;
	}

	/**
	 * Sets the system default extensions registry.
	 * 
	 * @param extensionsRegistry the extensions registry
	 */
	public static synchronized void setSystemExtensionsRegistry(ExtensionsRegistry extensionsRegistry)
	{
		if (extensionsRegistry == null)
		{
			throw new JRRuntimeException("Cannot set a null extensions registry.");
		}
		
		systemRegistry = extensionsRegistry;
	}

	/**
	 * Returns the thread extensions registry, if any.
	 * 
	 * @return the thread extensions registry
	 */
	public static ExtensionsRegistry getThreadExtensionsRegistry()
	{
		return threadRegistry.get();
	}

	/**
	 * Sets the thread extensions registry.
	 * 
	 * @param extensionsRegistry
	 * @see #getExtensionsRegistry()
	 */
	public static void setThreadExtensionsRegistry(ExtensionsRegistry extensionsRegistry)
	{
		threadRegistry.set(extensionsRegistry);
	}

	/**
	 * Resets (to null) the thread extensions registry.
	 * 
	 * @see #setThreadExtensionsRegistry(ExtensionsRegistry)
	 */
	public static void resetThreadExtensionsRegistry()
	{
		threadRegistry.set(null);
	}
	
	/**
	 * Returns the extensions registry to be used in the current context.
	 * 
	 * <p>
	 * The method returns the thread extensions registry (as returned by 
	 * {@link #getThreadExtensionsRegistry()}) if it exists, and the system
	 * registry (as returned by {@link #getSystemExtensionsRegistry()}) otherwise.
	 * 
	 * @return the context extensions registry
	 */
	public static ExtensionsRegistry getExtensionsRegistry()
	{
		ExtensionsRegistry registry = getThreadExtensionsRegistry();
		if (registry == null)
		{
			registry = getSystemExtensionsRegistry();
		}
		return registry;
	}
	
	/**
	 * Returns an object that can be used as cache key for extension-related
	 * caches.
	 * 
	 * @return an extension-related cache key
	 */
	public static Object getExtensionsCacheKey()
	{
		Object key = Thread.currentThread().getContextClassLoader();
		if (key == null)
		{
			key = NULL_CACHE_KEY;
		}
		return key;
	}
	
}
