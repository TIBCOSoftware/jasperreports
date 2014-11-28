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
package net.sf.jasperreports.data;

import java.util.List;

import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DataFileResolver
{

	private final JasperReportsContext context;

	public static DataFileResolver instance(JasperReportsContext context)
	{
		// not caching for now
		return new DataFileResolver(context);
	}

	protected DataFileResolver(JasperReportsContext context)
	{
		this.context = context;
	}
	
	public DataFileService getService(DataFile dataFile)
	{
		List<DataFileServiceFactory> factories = context.getExtensions(DataFileServiceFactory.class);
		DataFileService dataService = null;
		if (factories != null)
		{
			for (DataFileServiceFactory factory : factories)
			{
				DataFileService service = factory.createService(context, dataFile);
				if (service != null)
				{
					dataService = service;
					break;
				}
			}
		}
		return dataService;
	}
}
