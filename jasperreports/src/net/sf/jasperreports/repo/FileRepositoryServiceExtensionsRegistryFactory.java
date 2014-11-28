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
package net.sf.jasperreports.repo;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.extensions.DefaultExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FileRepositoryServiceExtensionsRegistryFactory implements ExtensionsRegistryFactory
{

	/**
	 * 
	 */
	public final static String FILE_REPOSITORY_PROPERTY_PREFIX = 
		DefaultExtensionsRegistry.PROPERTY_REGISTRY_PREFIX + "file.repository.";
	
	/**
	 * Specifies the file repository root location.
	 */
	public final static String PROPERTY_FILE_REPOSITORY_ROOT = FILE_REPOSITORY_PROPERTY_PREFIX + "root";
	
	/**
	 * Flag property that indicates whether the absolute path to be used instead, when resources are not found in the file repository.
	 */
	public final static String PROPERTY_FILE_REPOSITORY_RESOLVE_ABSOLUTE_PATH = FILE_REPOSITORY_PROPERTY_PREFIX + "resolve.absolute.path";
	
	/**
	 * 
	 */
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		String root = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getProperty(properties, PROPERTY_FILE_REPOSITORY_ROOT);
		boolean resolveAbsolutePath = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getBooleanProperty(properties, PROPERTY_FILE_REPOSITORY_RESOLVE_ABSOLUTE_PATH, false);

		return new SingletonExtensionRegistry<RepositoryService>(RepositoryService.class, new FileRepositoryService(DefaultJasperReportsContext.getInstance(), root, resolveAbsolutePath));
	}
}
