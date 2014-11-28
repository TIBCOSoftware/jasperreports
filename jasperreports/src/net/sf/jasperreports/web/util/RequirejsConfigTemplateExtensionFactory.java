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
package net.sf.jasperreports.web.util;

import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class RequirejsConfigTemplateExtensionFactory implements ExtensionsRegistryFactory
{

	private static final Log log = LogFactory.getLog(RequirejsConfigTemplateExtensionFactory.class);

	public static final String EXTENSION_PROPERTY_TEMPLATE_PREFIX = 
			JRPropertiesUtil.PROPERTY_PREFIX + "requirejs.config.template.";

	public static final String EXTENSION_PROPERTY_ENABLED_PREFIX = 
			JRPropertiesUtil.PROPERTY_PREFIX + "requirejs.config.enabled.";
	
	public static final String EXTENSION_PROPERTY_PATH_PREFIX = 
			JRPropertiesUtil.PROPERTY_PREFIX + "requirejs.config.path.";
	
	public static final String EXTENSION_PROPERTY_RESOURCE_PREFIX = 
			JRPropertiesUtil.PROPERTY_PREFIX + "requirejs.config.resource.";

	//same as EXTENSION_PROPERTY_ENABLED_PREFIX, but used in a different context
	public static final String PROPERTY_ENABLED_PREFIX = 
			JRPropertiesUtil.PROPERTY_PREFIX + "requirejs.config.enabled.";

	@Override
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties)
	{
		String template = properties.getProperty(EXTENSION_PROPERTY_TEMPLATE_PREFIX + registryId);
		
		String enabledProp = properties.getProperty(EXTENSION_PROPERTY_ENABLED_PREFIX + registryId);
		boolean enabled = JRPropertiesUtil.asBoolean(enabledProp, true);
		
		if (log.isDebugEnabled())
		{
			log.debug("creating requirejs template contributor " + registryId 
					+ ", template: " + template + ", enabled: " + enabled);
		}
		
		RequirejsTemplateConfigContributor templateContributor = new RequirejsTemplateConfigContributor();
		templateContributor.setTemplateName(template);
		setPaths(registryId, properties, templateContributor);
		setResources(registryId, properties, templateContributor);
		
		RequirejsConfigContributorSwitchDecorator switchDecorator = new RequirejsConfigContributorSwitchDecorator();
		switchDecorator.setDefaultEnabled(enabled);
		switchDecorator.setPropertyName(PROPERTY_ENABLED_PREFIX + registryId);
		switchDecorator.setContributor(templateContributor);
		
		ExtensionsRegistry registry = new SingletonExtensionRegistry<RequirejsConfigContributor>(
				RequirejsConfigContributor.class, switchDecorator);
		return registry;
	}

	protected void setPaths(String registryId, JRPropertiesMap properties,
			RequirejsTemplateConfigContributor templateContributor)
	{
		String pathPropPrefix = EXTENSION_PROPERTY_PATH_PREFIX + registryId + ".";
		List<PropertySuffix> pathProps = JRPropertiesUtil.getProperties(properties, pathPropPrefix);
		for (PropertySuffix pathProp : pathProps)
		{
			String suffix = pathProp.getSuffix();
			String path = pathProp.getValue();
			
			if (log.isDebugEnabled())
			{
				log.debug("setting path " + suffix + " to " + path);
			}
			templateContributor.addPath(suffix, path);
		}
	}

	protected void setResources(String registryId, JRPropertiesMap properties,
			RequirejsTemplateConfigContributor templateContributor)
	{
		String pathPropPrefix = EXTENSION_PROPERTY_RESOURCE_PREFIX + registryId + ".";
		List<PropertySuffix> pathProps = JRPropertiesUtil.getProperties(properties, pathPropPrefix);
		for (PropertySuffix pathProp : pathProps)
		{
			String suffix = pathProp.getSuffix();
			String path = pathProp.getValue();
			
			if (log.isDebugEnabled())
			{
				log.debug("setting resource " + suffix + " to " + path);
			}
			templateContributor.addResourcePath(suffix, path);
		}
	}

}
