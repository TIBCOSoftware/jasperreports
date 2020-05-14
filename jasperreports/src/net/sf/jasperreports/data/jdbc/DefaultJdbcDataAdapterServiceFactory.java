/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.data.jdbc;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.engine.ParameterContributorContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class DefaultJdbcDataAdapterServiceFactory implements JdbcDataAdapterContributorFactory
{
	/**
	 *
	 */
	private static final DefaultJdbcDataAdapterServiceFactory INSTANCE = new DefaultJdbcDataAdapterServiceFactory();

	/**
	 *
	 */
	private DefaultJdbcDataAdapterServiceFactory()
	{
	}

	/**
	 *
	 */
	public static DefaultJdbcDataAdapterServiceFactory getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	public DataAdapterService getDataAdapterService(ParameterContributorContext context, JdbcDataAdapter jdbcDataAdapter)
	{
		DataAdapterService dataAdapterService = null;
		
		if (GbqSimbaDataAdapterService.GBQ_SIMBA_DRIVER_CLASS.equals(jdbcDataAdapter.getDriver()))
		{
			dataAdapterService = new GbqSimbaDataAdapterService(context, jdbcDataAdapter);
		}
		
		return dataAdapterService;
	}
}
