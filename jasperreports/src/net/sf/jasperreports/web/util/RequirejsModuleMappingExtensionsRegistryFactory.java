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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.extensions.DefaultExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.ListExtensionRegistry;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class RequirejsModuleMappingExtensionsRegistryFactory implements ExtensionsRegistryFactory
{

	/**
	 * 
	 */
	public final static String REQUIREJS_MAPPING_PROPERTY_PREFIX =
		DefaultExtensionsRegistry.PROPERTY_REGISTRY_PREFIX + "requirejs.module.";

	public static final String URL_SUFFIX = "$url";
	
	/**
	 * 
	 */
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties)
	{
		List<PropertySuffix> requirejsModuleProperties = JRPropertiesUtil.getProperties(properties, REQUIREJS_MAPPING_PROPERTY_PREFIX);
		List<RequirejsModuleMapping> requirejsModules = new ArrayList<RequirejsModuleMapping>();
		for (Iterator<PropertySuffix> it = requirejsModuleProperties.iterator(); it.hasNext();)
		{
			PropertySuffix requirejsModuleProp = it.next();
			String suffix = requirejsModuleProp.getSuffix();
			boolean isClasspathResource = !suffix.endsWith(URL_SUFFIX);

			requirejsModules.add(
					new RequirejsModuleMapping(
							isClasspathResource ? suffix : suffix.substring(0, suffix.indexOf(URL_SUFFIX)),
							requirejsModuleProp.getValue(),
							isClasspathResource
					)
			);
		}
		
		return new ListExtensionRegistry<RequirejsModuleMapping>(RequirejsModuleMapping.class, requirejsModules);
	}

}
