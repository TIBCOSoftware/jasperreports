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
package net.sf.jasperreports.data;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ParameterContributorContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class DataAdapterServiceUtil
{
	public static final String EXCEPTION_MESSAGE_KEY_SERVICE_FACTORY_NOT_REGISTERED = "data.adapter.service.factory.not.registered";
	
	private ParameterContributorContext paramContribContext;


	/**
	 *
	 */
	private DataAdapterServiceUtil(ParameterContributorContext paramContribContext)
	{
		this.paramContribContext = paramContribContext;
	}
	
	
	/**
	 *
	 */
	public static DataAdapterServiceUtil getInstance(ParameterContributorContext paramContribContext)
	{
		return new DataAdapterServiceUtil(paramContribContext);
	}
	
	
	/**
	 * @deprecated Replaced by {@link #getInstance(ParameterContributorContext)}.
	 */
	public static DataAdapterServiceUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return getInstance(new ParameterContributorContext(jasperReportsContext, null, null));
	}
	
	
	/**
	 *
	 */
	public DataAdapterService getService(DataAdapter dataAdapter)
	{
		JasperReportsContext jasperReportsContext = paramContribContext.getJasperReportsContext();
		
		List<DataAdapterContributorFactory> bundles = jasperReportsContext.getExtensions(
				DataAdapterContributorFactory.class);
		for (Iterator<DataAdapterContributorFactory> it = bundles.iterator(); it.hasNext();)
		{
			DataAdapterContributorFactory factory = it.next();
			DataAdapterService service = factory.getDataAdapterService(paramContribContext, dataAdapter);
			if (service != null)
			{
				return service;
			}
		}

		@SuppressWarnings("deprecation")
		List<DataAdapterServiceFactory> depBundles = jasperReportsContext.getExtensions(
				DataAdapterServiceFactory.class);
		for (@SuppressWarnings("deprecation")
		Iterator<DataAdapterServiceFactory> it = depBundles.iterator(); it.hasNext();)
		{
			@SuppressWarnings("deprecation")
			DataAdapterServiceFactory factory = it.next();
			@SuppressWarnings("deprecation")
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
}
