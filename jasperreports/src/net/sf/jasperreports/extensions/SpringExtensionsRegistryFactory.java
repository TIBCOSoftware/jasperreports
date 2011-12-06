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

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;

/**
 * A {@link ExtensionsRegistryFactory} which works by loading a Spring beans XML
 * file and using beans of specific types as extensions.
 *
 * <p>
 * The factory requires a property named
 * <code>net.sf.jasperreports.extension.&lt;registry ID&gt;.spring.beans.resource</code>
 * to be present in the properties map passed to
 * {@link #createRegistry(String, JRPropertiesMap)}.
 * The value of this property must resolve to a resource name which is loaded
 * from the context classloader, and parsed as a Spring beans XML file.
 * 
 * <p>
 * Once the Spring beans XML file is loaded, this factory creates a
 * {@link SpringExtensionsRegistry} instance which will use the bean factory.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class SpringExtensionsRegistryFactory implements
		ExtensionsRegistryFactory
{
	
	private static final Log log = LogFactory.getLog(
			SpringExtensionsRegistryFactory.class);
	
	/**
	 * The suffix of the property that gives the Spring beans XML resource name.
	 */
	public static final String PROPERTY_SUFFIX_SPRING_BEANS_RESOURCE = 
		".spring.beans.resource";

	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		ListableBeanFactory beanFactory = getBeanFactory(registryId, properties);
		return new SpringExtensionsRegistry(beanFactory);
	}

	protected ListableBeanFactory getBeanFactory(String registryId,
			JRPropertiesMap properties)
	{
		String resourceProp = DefaultExtensionsRegistry.PROPERTY_REGISTRY_PREFIX
				+ registryId + PROPERTY_SUFFIX_SPRING_BEANS_RESOURCE;
		String resource = properties.getProperty(resourceProp);
		if (resource == null)
		{
			throw new JRRuntimeException("No Spring resource property set");
		}
		
		URL resourceLocation = JRLoader.getResource(resource);
		if (resourceLocation == null)
		{
			throw new JRRuntimeException("Could not find Spring resource " 
					+ resource + " for extensions registry " + registryId);
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("Creating Spring beans factory for extensions registry " 
					+ registryId + " using "+ resourceLocation);
		}
		
		return new XmlBeanFactory(new UrlResource(resourceLocation));
	}

}
