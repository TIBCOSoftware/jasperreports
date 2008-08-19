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

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;

/**
 * A {@link ComponentsBundleFactory} which works by loading a Spring beans XML
 * file and using a bean as {@link ComponentsBundle}.
 *
 * <p>
 * The factory requires a property named
 * <code>net.sf.jasperreports.component.&lt;bundle ID&gt;.spring.beans.resource</code>
 * to be present in the properties map passed to
 * {@link #createComponentsBundle(String, JRPropertiesMap)}.
 * The value of this property must resolve to a resource name which is loaded
 * from the context classloader, and parsed as a Spring beans XML file.
 * 
 * <p>
 * Once the Spring beans XML file is loaded, this factory fetches a bean and
 * returns it as the {@link ComponentsBundle} instance.
 * The name of this bean can be specified via a property named
 * <code>net.sf.jasperreports.component.&lt;bundle ID&gt;.spring.bundle.bean</code>
 * (in the properties map).  If such as property is not preset, the default
 * bean name <code>componentsBundle</code> is used.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class SpringComponentsBundleFactory implements ComponentsBundleFactory
{

	private final static Log log = LogFactory.getLog(SpringComponentsBundleFactory.class);
	
	/**
	 * The suffix of the property that gives the Spring beans XML resource name.
	 */
	public static final String PROPERTY_SUFFIX_SPRING_BEANS_RESOURCE = ".spring.beans.resource";

	/**
	 * The suffix of the property that gives the name of the bean to be
	 * returned as {@link ComponentsBundle} instance.
	 * 
	 * @see #DEFAULT_COMPONENTS_BUNDLE_BEAN
	 */
	public static final String PROPERTY_SUFFIX_SPRING_BUNDLE_BEAN = ".spring.bundle.bean";

	/**
	 * The default bean name of the {@link ComponentsBundle} instance.
	 */
	public static final String DEFAULT_COMPONENTS_BUNDLE_BEAN = "componentsBundle";
	
	public ComponentsBundle createComponentsBundle(String bundleId,
			JRPropertiesMap properties)
	{
		BeanFactory beanFactory = getBeanFactory(bundleId, properties);
		String beanName = getComponentsBundleBeanName(bundleId, properties);
		if (log.isDebugEnabled())
		{
			log.debug("Retrieving components bundle for " + bundleId 
					+ " using bean " + beanName);
		}
		ComponentsBundle component = (ComponentsBundle) beanFactory.getBean(
				beanName, ComponentsBundle.class);
		return component;
	}

	protected BeanFactory getBeanFactory(String componentId,
			JRPropertiesMap properties)
	{
		String resourceProp = DefaultComponentsRegistry.PROPERTY_COMPONENT_PREFIX
				+ componentId + PROPERTY_SUFFIX_SPRING_BEANS_RESOURCE;
		String resource = properties.getProperty(resourceProp);
		if (resource == null)
		{
			throw new JRRuntimeException("No Spring resource property set");
		}
		
		URL resourceLocation = JRLoader.getResource(resource);
		if (resourceLocation == null)
		{
			throw new JRRuntimeException("Could not find Spring resource " + resource 
					+ " for component " + componentId);
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("Creating Spring beans factory for component " + componentId 
					+ " using "+ resourceLocation);
		}
		
		XmlBeanFactory beanFactory = new XmlBeanFactory(new UrlResource(resourceLocation));
		return beanFactory;
	}
	
	protected String getComponentsBundleBeanName(String componentId,
			JRPropertiesMap properties)
	{
		String nameProp = DefaultComponentsRegistry.PROPERTY_COMPONENT_PREFIX
				+ componentId + PROPERTY_SUFFIX_SPRING_BUNDLE_BEAN;
		String name = properties.getProperty(nameProp);
		if (name == null)
		{
			name = DEFAULT_COMPONENTS_BUNDLE_BEAN;
		}
		return name;
	}
}
