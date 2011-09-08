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

import net.sf.jasperreports.data.bean.BeanDataAdapter;
import net.sf.jasperreports.data.bean.BeanDataAdapterService;
import net.sf.jasperreports.data.csv.CsvDataAdapter;
import net.sf.jasperreports.data.csv.CsvDataAdapterService;
import net.sf.jasperreports.data.ds.DataSourceDataAdapter;
import net.sf.jasperreports.data.ds.DataSourceDataAdapterService;
import net.sf.jasperreports.data.empty.EmptyDataAdapter;
import net.sf.jasperreports.data.empty.EmptyDataAdapterService;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapter;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapterService;
import net.sf.jasperreports.data.jndi.JndiDataAdapter;
import net.sf.jasperreports.data.jndi.JndiDataAdapterService;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapter;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapterService;
import net.sf.jasperreports.data.qe.QueryExecuterDataAdapter;
import net.sf.jasperreports.data.qe.QueryExecuterDataAdapterService;
import net.sf.jasperreports.data.xls.XlsDataAdapter;
import net.sf.jasperreports.data.xls.XlsDataAdapterService;
import net.sf.jasperreports.data.xlsx.XlsxDataAdapter;
import net.sf.jasperreports.data.xlsx.XlsxDataAdapterService;
import net.sf.jasperreports.data.xml.RemoteXmlDataAdapter;
import net.sf.jasperreports.data.xml.RemoteXmlDataAdapterService;
import net.sf.jasperreports.data.xml.XmlDataAdapter;
import net.sf.jasperreports.data.xml.XmlDataAdapterService;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DefaultDataAdapterServiceFactory implements DataAdapterServiceFactory
{

	/**
	 *
	 */
	private static final DefaultDataAdapterServiceFactory INSTANCE = new DefaultDataAdapterServiceFactory();
	
	/**
	 *
	 */
	public static DefaultDataAdapterServiceFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 *
	 */
	public DataAdapterService getDataAdapterService(DataAdapter dataAdapter)
	{
		DataAdapterService dataAdapterService = null;
		
		if (dataAdapter instanceof BeanDataAdapter)
		{
			dataAdapterService = new BeanDataAdapterService((BeanDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof CsvDataAdapter)
		{
			dataAdapterService = new CsvDataAdapterService((CsvDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof DataSourceDataAdapter)
		{
			dataAdapterService = new DataSourceDataAdapterService((DataSourceDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof EmptyDataAdapter)
		{
			dataAdapterService = new EmptyDataAdapterService((EmptyDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof JdbcDataAdapter)
		{
			dataAdapterService = new JdbcDataAdapterService((JdbcDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof JndiDataAdapter)
		{
			dataAdapterService = new JndiDataAdapterService((JndiDataAdapter)dataAdapter);//FIXME maybe want some cache here
		}
		else if (dataAdapter instanceof DataSourceProviderDataAdapter)
		{
			dataAdapterService = new DataSourceProviderDataAdapterService((DataSourceProviderDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof QueryExecuterDataAdapter)
		{
			dataAdapterService = new QueryExecuterDataAdapterService((QueryExecuterDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof XlsDataAdapter)
		{
			dataAdapterService = new XlsDataAdapterService((XlsDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof XlsxDataAdapter)
		{
			dataAdapterService = new XlsxDataAdapterService((XlsxDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof RemoteXmlDataAdapter)
		{
			dataAdapterService = new RemoteXmlDataAdapterService((RemoteXmlDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof XmlDataAdapter)
		{
			dataAdapterService = new XmlDataAdapterService((XmlDataAdapter)dataAdapter);
		}
		
		return dataAdapterService;
	}
  
}
