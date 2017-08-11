/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.repo;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.extensions.DefaultExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;
import net.sf.jasperreports.properties.PropertyConstants;


/**
 * @author Narcis Marcu(narcism@users.sourceforge.net)
 */
public class HttpRepositoryServiceExtensionsRegistryFactory implements ExtensionsRegistryFactory
{

	/**
	 * 
	 */
	public final static String HTTP_REPOSITORY_PROPERTY_PREFIX =
		DefaultExtensionsRegistry.PROPERTY_REGISTRY_PREFIX + "http.repository.service";
	
	/**
	 * Specifies the file repository root location.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_REPOSITORY,
			scopes = {PropertyScope.GLOBAL, PropertyScope.EXTENSION},
			sinceVersion = PropertyConstants.VERSION_6_4_2
			)
	public final static String PROPERTY_HTTP_REPOSITORY_URL = HTTP_REPOSITORY_PROPERTY_PREFIX + "url";
	
	@Override
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		String repositoryUrl = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getProperty(properties, PROPERTY_HTTP_REPOSITORY_URL);

		return new SingletonExtensionRegistry<>(RepositoryService.class, new HttpRepositoryService(DefaultJasperReportsContext.getInstance(), repositoryUrl));
	}
}
