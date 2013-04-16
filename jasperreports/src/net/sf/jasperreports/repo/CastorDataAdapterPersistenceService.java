/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CastorDataAdapterPersistenceService extends CastorObjectPersistenceService
{
	/**
	 * @deprecated Replaced by {@link #CastorDataAdapterPersistenceService(JasperReportsContext)}. 
	 */
	public CastorDataAdapterPersistenceService()
	{
		super(DefaultJasperReportsContext.getInstance());
	}
	
	/**
	 * 
	 */
	public CastorDataAdapterPersistenceService(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}

	
	/**
	 * 
	 */
	public Resource load(String uri, RepositoryService repositoryService) //FIXMEREPO consider changing the DataAdapter as resource
	{
		DataAdapter dataAdapter = null;
		
		ObjectResource resource = (ObjectResource)super.load(uri, repositoryService);
		
		if (resource != null)
		{
			dataAdapter = (DataAdapter)resource.getValue();
		}
		
		return dataAdapter;
	}

}
