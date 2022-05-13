/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2022 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated To be removed.
 */
public class CastorRepositoryPersistenceServiceFactory implements PersistenceServiceFactory
{
	private static final CastorRepositoryPersistenceServiceFactory INSTANCE = new CastorRepositoryPersistenceServiceFactory();
	
	/**
	 * 
	 */
	public static CastorRepositoryPersistenceServiceFactory getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	public <K extends RepositoryService, L extends Resource> PersistenceService getPersistenceService(
		JasperReportsContext jasperReportsContext,
		Class<K> repositoryServiceType, 
		Class<L> resourceType
		) 
	{
		if (DataAdapterResource.class.isAssignableFrom(resourceType))
		{
			return new CastorDataAdapterPersistenceService(jasperReportsContext);
		}
		else if (CastorResource.class.isAssignableFrom(resourceType))
		{
			return new CastorObjectPersistenceService(jasperReportsContext);
		}

		return null;
	}
}
