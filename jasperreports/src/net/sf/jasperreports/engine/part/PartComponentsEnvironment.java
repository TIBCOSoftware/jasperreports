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
package net.sf.jasperreports.engine.part;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class that provides access to {@link PartComponentsBundle component bundles}.
 * 
 * <p>
 * Component bundles are registered as JasperReports extensions of type
 * {@link PartComponentsBundle} via the central extension framework (see
 * {@link ExtensionsEnvironment}).
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ComponentsEnvironment.java 6863 2014-02-06 09:59:27Z teodord $
 */
public final class PartComponentsEnvironment
{
	
	private static final Log log = LogFactory.getLog(PartComponentsEnvironment.class);
	public static final String EXCEPTION_MESSAGE_KEY_PART_COMPONENTS_BUNDLE_NOT_REGISTERED = "engine.part.components.bundle.not.registered";
	
	private final ReferenceMap cache = new ReferenceMap(
			ReferenceMap.WEAK, ReferenceMap.HARD);
	
	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private PartComponentsEnvironment(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	public static PartComponentsEnvironment getInstance(JasperReportsContext jasperReportsContext)
	{
		return new PartComponentsEnvironment(jasperReportsContext);
	}
	
	
	/**
	 * Returns the set of all component bundles present in the registry. 
	 * 
	 * @return the set of component bundles
	 */
	public Collection<PartComponentsBundle> getBundles()
	{
		Map<String, PartComponentsBundle> components = getCachedBundles();
		return components.values();
	}
	
	protected Map<String, PartComponentsBundle> getCachedBundles()
	{
		Object cacheKey = ExtensionsEnvironment.getExtensionsCacheKey();
		synchronized (cache)
		{
			Map<String, PartComponentsBundle> components = (Map<String, PartComponentsBundle>) cache.get(cacheKey);
			if (components == null)
			{
				components = findBundles();
				cache.put(cacheKey, components);
			}
			return components;
		}
	}

	protected Map<String, PartComponentsBundle> findBundles()
	{
		Map<String, PartComponentsBundle> components = new HashMap<String, PartComponentsBundle>();
		List<PartComponentsBundle> bundles = jasperReportsContext.getExtensions(PartComponentsBundle.class);
		for (Iterator<PartComponentsBundle> it = bundles.iterator(); it.hasNext();)
		{
			PartComponentsBundle bundle = it.next();
			String namespace = bundle.getXmlParser().getNamespace();
			if (components.containsKey(namespace))
			{
				log.warn("Found two components for namespace " + namespace);
			}
			else
			{
				components.put(namespace, bundle);
			}
		}
		return components;
	}
	
	/**
	 * Returns a component bundle that corresponds to a namespace.
	 * 
	 * @param namespace a component bundle namespace
	 * @return the corresponding component bundle
	 * @throws JRRuntimeException if no bundle corresponding to the namespace
	 * is found in the registry
	 */
	public PartComponentsBundle getBundle(String namespace)
	{
		Map<String, PartComponentsBundle> components = getCachedBundles();
		PartComponentsBundle componentsBundle = components.get(namespace);
		if (componentsBundle == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_PART_COMPONENTS_BUNDLE_NOT_REGISTERED,
					new Object[]{namespace});
		}
		return componentsBundle;
	}
	
	/**
	 * Returns a component manager that corresponds to a particular component
	 * type key.
	 * 
	 * @param componentKey the component type key
	 * @return the manager for the component type
	 * @throws JRRuntimeException if the registry does not contain the specified
	 * component type
	 */
	public PartComponentManager getManager(ComponentKey componentKey)
	{
		String namespace = componentKey.getNamespace();
		PartComponentsBundle componentsBundle = getBundle(namespace);
		
		String name = componentKey.getName();
		return componentsBundle.getComponentManager(name);
	}

}
