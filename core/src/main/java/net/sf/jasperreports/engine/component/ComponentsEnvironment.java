/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.engine.component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

/**
 * A class that provides access to {@link ComponentsBundle component bundles}.
 * 
 * <p>
 * Component bundles are registered as JasperReports extensions of type
 * {@link ComponentsBundle} via the central extension framework (see
 * {@link ExtensionsEnvironment}).
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public final class ComponentsEnvironment
{
	
	private static final Log log = LogFactory.getLog(ComponentsEnvironment.class);
	public static final String EXCEPTION_MESSAGE_KEY_BUNDLE_NOT_REGISTERED = "components.bundle.not.registered";
	public static final String EXCEPTION_MESSAGE_KEY_COMPONENT_MANAGER_NOT_FOUND = "components.component.manager.not.found";
	
	private final ReferenceMap<Object, List<ComponentsBundle>> cache = 
		new ReferenceMap<>(
			ReferenceMap.ReferenceStrength.WEAK, ReferenceMap.ReferenceStrength.HARD
			);
	
	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private ComponentsEnvironment(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	public static ComponentsEnvironment getInstance(JasperReportsContext jasperReportsContext)
	{
		return new ComponentsEnvironment(jasperReportsContext);
	}
	
	
	/**
	 * Returns the set of all component bundles present in the registry. 
	 * 
	 * @return the set of component bundles
	 */
	public Collection<ComponentsBundle> getBundles()
	{
		List<ComponentsBundle> bundles = getCachedBundles();
		return bundles;
	}
	
	protected List<ComponentsBundle> getCachedBundles()
	{
		Object cacheKey = ExtensionsEnvironment.getExtensionsCacheKey();
		synchronized (cache)
		{
			List<ComponentsBundle> components = cache.get(cacheKey);
			if (components == null)
			{
				components = findBundles();
				cache.put(cacheKey, components);
			}
			return components;
		}
	}

	protected List<ComponentsBundle> findBundles()
	{
		return jasperReportsContext.getExtensions(ComponentsBundle.class);
	}
	
	/**
	 * Returns a component bundle that corresponds to a namespace.
	 * 
	 * @param namespace a component bundle namespace
	 * @return the corresponding component bundle
	 * @throws JRRuntimeException if no bundle corresponding to the namespace
	 * is found in the registry
	 */
	public ComponentsBundle getBundle(String namespace)
	{
		List<ComponentsBundle> bundles = getBundles(namespace);
		if (bundles.isEmpty())
		{
			throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_BUNDLE_NOT_REGISTERED,
				new Object[]{namespace});
		}
		
		if (bundles.size() > 1)
		{
			log.warn("Found " + bundles.size() + " component bundles for namespace " + namespace);
		}
		return bundles.get(0);
	}
	
	/**
	 * Returns the component bundles that correspond to a namespace.
	 * 
	 * @param namespace a component bundle namespace
	 * @return the corresponding component bundles
	 */
	public List<ComponentsBundle> getBundles(String namespace)
	{
		List<ComponentsBundle> components = getCachedBundles();
		return components.stream().filter(bundle -> bundle.getNamespace().equals(namespace))
				.collect(Collectors.toList());
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
	public ComponentManager getManager(ComponentKey componentKey)
	{
		String namespace = componentKey.getNamespace();
		List<ComponentsBundle> bundles = getBundles(namespace);
		String name = componentKey.getName();
		List<ComponentManager> managers = bundles.stream()
				.map(bundle -> bundle.getComponentManager(name))
				.filter(manager -> manager != null)
				.collect(Collectors.toList());

		if (managers.isEmpty())
		{
			throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_COMPONENT_MANAGER_NOT_FOUND,
				new Object[]{name, namespace});
		}
		
		if (managers.size() > 1)
		{
			log.warn("Found " + managers.size() + " components for name " + name 
					+ ", namespace " + namespace);			
		}
		return managers.get(0);
	}
}
