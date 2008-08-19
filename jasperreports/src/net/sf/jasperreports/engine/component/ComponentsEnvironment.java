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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.ClassUtils;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * A class that provides means of setting and accessing
 * {@link ComponentsRegistry} instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see #getComponentsRegistry()
 */
public final class ComponentsEnvironment
{

	private ComponentsEnvironment()
	{
	}
	
	private static final Log log = LogFactory.getLog(ComponentsEnvironment.class); 
	
	/**
	 * A property that provides the default {@link ComponentsRegistry} 
	 * implementation class. 
	 * 
	 * <p>
	 * This property is only read at initialization time, therefore changing
	 * the property value at a later time will have no effect. 
	 */
	public static final String PROPERTY_COMPONENTS_REGISTRY_CLASS = 
		JRProperties.PROPERTY_PREFIX + "components.registry.class";
	
	private static ComponentsRegistry systemRegistry;
	private static final ThreadLocal threadRegistry = new InheritableThreadLocal();
	
	static
	{
		systemRegistry = createDefaultRegistry();
	}
	
	private static ComponentsRegistry createDefaultRegistry()
	{
		String registryClass = JRProperties.getProperty(PROPERTY_COMPONENTS_REGISTRY_CLASS);
		
		if (log.isDebugEnabled())
		{
			log.debug("Instantiating components registry class " + registryClass);
		}
		
		ComponentsRegistry registry = (ComponentsRegistry) ClassUtils.
			instantiateClass(registryClass, ComponentsRegistry.class);
		return registry;
	}
	
	/**
	 * Returns the system default components registry object.
	 * 
	 * <p>
	 * This is either the one instantiated based on {@link #PROPERTY_COMPONENTS_REGISTRY_CLASS},
	 * or the one set by {@link #setSystemComponentsRegistry(ComponentsRegistry)}.
	 * 
	 * @return the system default components registry object
	 */
	public static synchronized ComponentsRegistry getSystemComponentsRegistry()
	{
		return systemRegistry;
	}

	/**
	 * Sets the system default components registry.
	 * 
	 * @param componentsRegistry the components registry
	 */
	public static synchronized void setSystemComponentsRegistry(ComponentsRegistry componentsRegistry)
	{
		if (componentsRegistry == null)
		{
			throw new JRRuntimeException("Cannot set a null components registry.");
		}
		
		systemRegistry = componentsRegistry;
	}

	/**
	 * Returns the thread components registry, if any.
	 * 
	 * @return the thread components registry
	 */
	public static ComponentsRegistry getThreadComponentsRegistry()
	{
		return (ComponentsRegistry) threadRegistry.get();
	}

	/**
	 * Sets the thread components registry.
	 * 
	 * @param componentsRegistry
	 * @see #getComponentsRegistry()
	 */
	public static void setThreadComponentsRegistry(ComponentsRegistry componentsRegistry)
	{
		threadRegistry.set(componentsRegistry);
	}

	/**
	 * Resets (to null) the thread components registry.
	 * 
	 * @see #setThreadComponentsRegistry(ComponentsRegistry)
	 */
	public static void resetThreadComponentsRegistry()
	{
		threadRegistry.set(null);
	}
	
	/**
	 * Returns the component registry to be used in the current context.
	 * 
	 * <p>
	 * The method returns the thread component registry (as returned by 
	 * {@link #getThreadComponentsRegistry()}) if it exists, and the system
	 * registry (as returned by {@link #getSystemComponentsRegistry()}) otherwise.
	 * 
	 * @return the context component registry
	 */
	public static ComponentsRegistry getComponentsRegistry()
	{
		ComponentsRegistry registry = getThreadComponentsRegistry();
		if (registry == null)
		{
			registry = getSystemComponentsRegistry();
		}
		return registry;
	}
	
}
