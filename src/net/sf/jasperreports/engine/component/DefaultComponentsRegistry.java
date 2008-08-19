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
package net.sf.jasperreports.engine.component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.ClassUtils;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default {@link ComponentsRegistry component registry} implementation.
 *
 * <p>
 * The implementation builds a component registry by scanning the context
 * classloader for resources named <code>jasperreports_component.properties</code>.
 * 
 * <p>
 * Each such resource is loaded as a properties file, and properties that start
 * with <code>net.sf.jasperreports.component.factory.</code> are identified.
 * 
 * <p>
 * Each such property should have as value the name of a 
 * {@link ComponentsBundleFactory} implementation.  The bundle factory class is
 * instantiated, and {@link ComponentsBundleFactory#createComponentsBundle(String, JRPropertiesMap)}
 * is called on it, using the propery suffix as bundle ID and passing the
 * properties map.  The bundle factory can collect properties that apply to the
 * specific bundle by using a property prefix obtain by appending the bundle ID
 * to "<code>net.sf.jasperreports.component.</code>".
 * 
 * <p>
 * If instantiating a components bundle results in an exception, the bundle
 * is skipped and an error message is logged.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class DefaultComponentsRegistry implements ComponentsRegistry
{

	private static final Log log = LogFactory.getLog(DefaultComponentsRegistry.class);
	
	/**
	 * The name of propery file resources that are used to load components bundles.
	 */
	public final static String COMPONENT_RESOURCE_NAME = "jasperreports_component.properties";
	
	/**
	 * The property prefix of components bundle factories.
	 */
	public final static String PROPERTY_COMPONENT_FACTORY_PREFIX = 
			JRProperties.PROPERTY_PREFIX + "component.factory.";
	
	/**
	 * A prefix that can be used to provide bundle-specific properties,
	 * by appending the bundle ID and a fixed property prefix to it. 
	 */
	public static final String PROPERTY_COMPONENT_PREFIX = 
			JRProperties.PROPERTY_PREFIX + "component.";
	
	private final static Object NULL_CACHE_KEY = new Object();
	
	private final ReferenceMap cache = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.HARD);
	
	public Collection getComponentBundles()
	{
		Map components = getCachedComponents();
		return components.values();
	}
	
	protected Map getCachedComponents()
	{
		Object cacheKey = getCacheKey();
		synchronized (cache)
		{
			Map components = (Map) cache.get(cacheKey);
			if (components == null)
			{
				components = findComponents();
				cache.put(cacheKey, components);
			}
			return components;
		}
	}

	protected Object getCacheKey()
	{
		Object key = Thread.currentThread().getContextClassLoader();
		if (key == null)
		{
			key = NULL_CACHE_KEY;
		}
		return key;
	}

	protected Map findComponents()
	{
		Map components = new HashMap();
		List resources = JRLoader.getResources(COMPONENT_RESOURCE_NAME);
		for (Iterator it = resources.iterator(); it.hasNext();)
		{
			URL resource = (URL) it.next();
			
			if (log.isDebugEnabled())
			{
				log.debug("Loading components from resource " + resource);
			}
			
			List resourceComponents = loadResourceComponents(resource);
			for (Iterator cit = resourceComponents.iterator(); cit.hasNext();)
			{
				ComponentsBundle component = (ComponentsBundle) cit.next();
				String namespace = component.getXmlParser().getNamespace();
				if (components.put(namespace, component) != null)
				{
					log.warn("Found two components for namespace " + namespace);
				}
			}
		}
		return components;
	}
	
	protected List loadResourceComponents(URL resource)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Scanning resource " + resource + " for component bundles");
		}
		
		List components = new ArrayList();
		JRPropertiesMap props = JRPropertiesMap.loadProperties(resource);
		List factoryProps = JRProperties.getProperties(props, PROPERTY_COMPONENT_FACTORY_PREFIX);
		for (Iterator it = factoryProps.iterator(); it.hasNext();)
		{
			JRProperties.PropertySuffix factoryProp = 
				(JRProperties.PropertySuffix) it.next();
			String bundleId = factoryProp.getSuffix();
			String factoryClass = factoryProp.getValue();
			
			try
			{
				ComponentsBundle componentsBundle = instantiateComponentsBundle(
						props, bundleId, factoryClass);
				components.add(componentsBundle);
			}
			catch (Exception e)
			{
				//skip this bundle
				log.error("Error instantiating components bundle for " + bundleId
						+ " from resource " + resource, e);
			}
		}
		return components;
	}

	protected ComponentsBundle instantiateComponentsBundle(
			JRPropertiesMap props, String bundleId, String factoryClass)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Instantiating components bundle for " + bundleId
					+ " using factory class " + factoryClass);
		}
		
		ComponentsBundleFactory factory = (ComponentsBundleFactory) 
				ClassUtils.instantiateClass(factoryClass, ComponentsBundleFactory.class);
		ComponentsBundle componentsBundle = factory.createComponentsBundle(bundleId, props);
		return componentsBundle;
	}

	public ComponentsBundle getComponentsBundle(String namespace)
	{
		Map components = getCachedComponents();
		ComponentsBundle componentsBundle = (ComponentsBundle) components.get(namespace);
		if (componentsBundle == null)
		{
			throw new JRRuntimeException("No components bundle registered for namespace " + namespace);
		}
		return componentsBundle;
	}
	
	public ComponentManager getComponentManager(ComponentKey componentKey)
	{
		String namespace = componentKey.getNamespace();
		ComponentsBundle componentsBundle = getComponentsBundle(namespace);
		
		String name = componentKey.getName();
		ComponentManager manager = componentsBundle.getComponentManager(name);
		return manager;
	}
}
