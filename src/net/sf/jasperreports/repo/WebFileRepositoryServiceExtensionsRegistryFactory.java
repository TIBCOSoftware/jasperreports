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
package net.sf.jasperreports.repo;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.extensions.DefaultExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FileRepositoryServiceExtensionsRegistryFactory.java 4595 2011-09-08 15:55:10Z teodord $
 */
public class WebFileRepositoryServiceExtensionsRegistryFactory implements ExtensionsRegistryFactory
{

	/**
	 * 
	 */
	public final static String WEB_FILE_REPOSITORY_PROPERTY_PREFIX = 
		DefaultExtensionsRegistry.PROPERTY_REGISTRY_PREFIX + "web.file.repository.";
	public final static String PROPERTY_WEB_FILE_REPOSITORY_ROOT = WEB_FILE_REPOSITORY_PROPERTY_PREFIX + "root";
	public final static String PROPERTY_WEB_FILE_REPOSITORY_RESOLVE_ABSOLUTE_PATH = WEB_FILE_REPOSITORY_PROPERTY_PREFIX + "resolve.absolute.path";
	
	/**
	 * 
	 */
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		String root = JRProperties.getProperty(properties, PROPERTY_WEB_FILE_REPOSITORY_ROOT);
		boolean resolveAbsolutePath = JRProperties.getBooleanProperty(properties, PROPERTY_WEB_FILE_REPOSITORY_RESOLVE_ABSOLUTE_PATH, false);

		return new SingletonExtensionRegistry<RepositoryService>(RepositoryService.class, new WebFileRepositoryService(root, resolveAbsolutePath));
	}
}
