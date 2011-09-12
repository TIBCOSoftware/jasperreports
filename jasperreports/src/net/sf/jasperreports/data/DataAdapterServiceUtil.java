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
package net.sf.jasperreports.data;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class DataAdapterServiceUtil
{

	/**
	 *
	 */
	public static DataAdapterService getDataAdapterService(DataAdapter dataAdapter)
	{
		List<DataAdapterServiceFactory> bundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(
				DataAdapterServiceFactory.class);
		for (Iterator<DataAdapterServiceFactory> it = bundles.iterator(); it.hasNext();)
		{
			DataAdapterServiceFactory factory = it.next();
			DataAdapterService service = factory.getDataAdapterService(dataAdapter);
			if (service != null)
			{
				return service;
			}
		}
		throw new JRRuntimeException("No data adapter service factory registered for the '" + dataAdapter.getName() + "' data adapter.");
	}
  
	/**
	 *
	 */
	private DataAdapterServiceUtil()
	{
	}
 
}
