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

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class DataAdapterServiceUtil
{
	public static final String EXCEPTION_MESSAGE_KEY_SERVICE_FACTORY_NOT_REGISTERED = "data.adapter.service.factory.not.registered";
	
	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private DataAdapterServiceUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	private static DataAdapterServiceUtil getDefaultInstance()
	{
		return new DataAdapterServiceUtil(DefaultJasperReportsContext.getInstance());
	}
	
	
	/**
	 *
	 */
	public static DataAdapterServiceUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new DataAdapterServiceUtil(jasperReportsContext);
	}
	
	
	/**
	 *
	 */
	public DataAdapterService getService(DataAdapter dataAdapter)
	{
		List<DataAdapterServiceFactory> bundles = jasperReportsContext.getExtensions(
				DataAdapterServiceFactory.class);
		for (Iterator<DataAdapterServiceFactory> it = bundles.iterator(); it.hasNext();)
		{
			DataAdapterServiceFactory factory = it.next();
			DataAdapterService service = factory.getDataAdapterService(jasperReportsContext, dataAdapter);
			if (service != null)
			{
				return service;
			}
		}
		throw 
			new JRRuntimeException(
				EXCEPTION_MESSAGE_KEY_SERVICE_FACTORY_NOT_REGISTERED,
				new Object[]{dataAdapter.getName()});
	}
  
	/**
	 * @deprecated Replaced by {@link #getService(DataAdapter)}.
	 */
	public static DataAdapterService getDataAdapterService(DataAdapter dataAdapter)
	{
		return getDefaultInstance().getService(dataAdapter);
	}
 
}
