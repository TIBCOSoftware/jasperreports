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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.util.ClassLoaderResource;
import net.sf.jasperreports.engine.util.ClassUtils;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default {@link ExtensionsRegistry extension registry} implementation.
 *
 * <p>
 * The implementation builds an extension registry by scanning the context
 * classloader for resources named <code>jasperreports_extension.properties</code>.
 * 
 * <p>
 * Each such resource is loaded as a properties file, and properties that start
 * with <code>net.sf.jasperreports.extension.registry.factory.</code> are identified.
 * 
 * <p>
 * Each such property should have as value the name of a 
 * {@link ExtensionsRegistryFactory} implementation.  The registry factory class is
 * instantiated, and
 * {@link ExtensionsRegistryFactory#createRegistry(String, JRPropertiesMap)}
 * is called on it, using the propery suffix as registry ID and passing the
 * properties map.  The registry factory can collect properties that apply to the
 * specific registry by using a property prefix obtain by appending the registry ID
 * to "<code>net.sf.jasperreports.extension.</code>".
 * 
 * <p>
 * If instantiating an extension registry results in an exception, the registry
 * is skipped and an error message is logged.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class DefaultExtensionsRegistry implements ExtensionsRegistry
{
	
	private final Log log = LogFactory.getLog(DefaultExtensionsRegistry.class);
	
	/**
	 * The name of property file resources that are used to load JasperReports 
	 * extensions.
	 */
	public final static String EXTENSION_RESOURCE_NAME = 
		"jasperreports_extension.properties";
	
	/**
	 * The property prefix of extension registry factories.
	 */
	public final static String PROPERTY_REGISTRY_FACTORY_PREFIX = 
			JRProperties.PROPERTY_PREFIX + "extension.registry.factory.";
	
	/**
	 * A prefix that can be used to provide registry-specific properties,
	 * by appending the registry ID and a fixed property suffix to it. 
	 */
	public static final String PROPERTY_REGISTRY_PREFIX = 
			JRProperties.PROPERTY_PREFIX + "extension.";

	private final ReferenceMap registrySetCache = new ReferenceMap(
			ReferenceMap.WEAK, ReferenceMap.HARD);
	
	private final ReferenceMap registryCache = 
		new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.HARD);

	public <T> List<T> getExtensions(Class<T> extensionType)
	{
		List<ExtensionsRegistry> registries = getRegistries();
		List<T> extensions = new ArrayList<T>(registries.size());
		for (Iterator<ExtensionsRegistry> it = registries.iterator(); it.hasNext();)
		{
			ExtensionsRegistry registry = it.next();
			List<T> registryExtensions = registry.getExtensions(extensionType);
			if (registryExtensions != null && !registryExtensions.isEmpty())
			{
				extensions.addAll(registryExtensions);
			}
		}
		return extensions;
	}
	
	protected List<ExtensionsRegistry> getRegistries()
	{
		List<ExtensionsRegistry> registries;
		Object cacheKey = ExtensionsEnvironment.getExtensionsCacheKey();
		synchronized (registrySetCache)
		{
			registries = (List<ExtensionsRegistry>) registrySetCache.get(cacheKey);
			if (registries == null)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Loading registries for cache key " + cacheKey);
				}
				
				registries = loadRegistries();
				registrySetCache.put(cacheKey, registries);
			}
		}
		return registries;
	}
	
	protected List<ExtensionsRegistry> loadRegistries()
	{
		List<ExtensionsRegistry> allRegistries = new ArrayList<ExtensionsRegistry>();
		List<ClassLoaderResource> extensionResources = loadExtensionPropertyResources();
		for (ClassLoaderResource extensionResource : extensionResources)
		{
			ClassLoader classLoader = extensionResource.getClassLoader();
			Map<URL, List<ExtensionsRegistry>> classLoaderRegistries = getClassLoaderRegistries(classLoader);
			
			URL url = extensionResource.getUrl();
			List<ExtensionsRegistry> registries;
			synchronized (classLoaderRegistries)
			{
				registries = classLoaderRegistries.get(url);
				if (registries == null)
				{
					if (log.isDebugEnabled())
					{
						log.debug("Loading JasperReports extension properties resource " 
								+ url);
					}
					
					JRPropertiesMap props = JRPropertiesMap.loadProperties(url);
					registries = loadRegistries(props);
					
					classLoaderRegistries.put(url, registries);
				}
			}
			
			allRegistries.addAll(registries);
		}
		return allRegistries;
	}

	protected List<ClassLoaderResource> loadExtensionPropertyResources()
	{
		return JRLoader.getClassLoaderResources(
				EXTENSION_RESOURCE_NAME);
	}

	protected Map<URL, List<ExtensionsRegistry>> getClassLoaderRegistries(ClassLoader classLoader)
	{
		synchronized (registryCache)
		{
			Map<URL, List<ExtensionsRegistry>> registries = (Map<URL, List<ExtensionsRegistry>>) registryCache.get(classLoader);
			if (registries == null)
			{
				registries = new HashMap<URL, List<ExtensionsRegistry>>();
				registryCache.put(classLoader, registries);
			}
			return registries;
		}
	}
	
	protected List<ExtensionsRegistry> loadRegistries(JRPropertiesMap properties)
	{
		List<ExtensionsRegistry> registries = new ArrayList<ExtensionsRegistry>();
		List<PropertySuffix> factoryProps = JRProperties.getProperties(properties, 
				PROPERTY_REGISTRY_FACTORY_PREFIX);
		for (Iterator<PropertySuffix> it = factoryProps.iterator(); it.hasNext();)
		{
			JRProperties.PropertySuffix factoryProp = it.next();
			String registryId = factoryProp.getSuffix();
			String factoryClass = factoryProp.getValue();
			
			if (log.isDebugEnabled())
			{
				log.debug("Instantiating registry of type " + factoryClass 
						+ " for property " + factoryProp.getKey());
			}
			
			try
			{
				ExtensionsRegistry registry = instantiateRegistry(
						properties, registryId, factoryClass);
				registries.add(registry);
			}
			catch (Exception e)
			{
				//skip this registry
				log.error("Error instantiating extensions registry for " 
						+ registryId, e);
			}
		}
		return registries;
	}

	protected ExtensionsRegistry instantiateRegistry(
			JRPropertiesMap props, String registryId, String factoryClass)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Instantiating extensions registry for " + registryId
					+ " using factory class " + factoryClass);
		}
		
		ExtensionsRegistryFactory factory = (ExtensionsRegistryFactory) 
				ClassUtils.instantiateClass(factoryClass, ExtensionsRegistryFactory.class);
		return factory.createRegistry(registryId, props);
	}

}
