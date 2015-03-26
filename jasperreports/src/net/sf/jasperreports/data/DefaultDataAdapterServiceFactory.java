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

import net.sf.jasperreports.data.bean.BeanDataAdapter;
import net.sf.jasperreports.data.bean.BeanDataAdapterService;
import net.sf.jasperreports.data.csv.CsvDataAdapter;
import net.sf.jasperreports.data.csv.CsvDataAdapterService;
import net.sf.jasperreports.data.ds.DataSourceDataAdapter;
import net.sf.jasperreports.data.ds.DataSourceDataAdapterService;
import net.sf.jasperreports.data.ejbql.EjbqlDataAdapter;
import net.sf.jasperreports.data.ejbql.EjbqlDataAdapterService;
import net.sf.jasperreports.data.empty.EmptyDataAdapter;
import net.sf.jasperreports.data.empty.EmptyDataAdapterService;
import net.sf.jasperreports.data.excel.ExcelDataAdapter;
import net.sf.jasperreports.data.excel.ExcelDataAdapterService;
import net.sf.jasperreports.data.hibernate.HibernateDataAdapter;
import net.sf.jasperreports.data.hibernate.HibernateDataAdapterService;
import net.sf.jasperreports.data.hibernate.spring.SpringHibernateDataAdapter;
import net.sf.jasperreports.data.hibernate.spring.SpringHibernateDataAdapterService;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapter;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapterImpl;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapterService;
import net.sf.jasperreports.data.jndi.JndiDataAdapter;
import net.sf.jasperreports.data.jndi.JndiDataAdapterService;
import net.sf.jasperreports.data.json.JsonDataAdapter;
import net.sf.jasperreports.data.json.JsonDataAdapterService;
import net.sf.jasperreports.data.mondrian.MondrianDataAdapter;
import net.sf.jasperreports.data.mondrian.MondrianDataAdapterService;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapter;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapterService;
import net.sf.jasperreports.data.qe.QueryExecuterDataAdapter;
import net.sf.jasperreports.data.qe.QueryExecuterDataAdapterService;
import net.sf.jasperreports.data.xls.AbstractXlsDataAdapterService;
import net.sf.jasperreports.data.xls.XlsDataAdapter;
import net.sf.jasperreports.data.xls.XlsDataAdapterService;
import net.sf.jasperreports.data.xlsx.XlsxDataAdapter;
import net.sf.jasperreports.data.xlsx.XlsxDataAdapterService;
import net.sf.jasperreports.data.xml.XmlDataAdapter;
import net.sf.jasperreports.data.xml.XmlDataAdapterService;
import net.sf.jasperreports.data.xmla.XmlaDataAdapter;
import net.sf.jasperreports.data.xmla.XmlaDataAdapterService;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	public DataAdapterService getDataAdapterService(JasperReportsContext jasperReportsContext, DataAdapter dataAdapter)
	{
		DataAdapterService dataAdapterService = null;
		
		if (dataAdapter instanceof BeanDataAdapter)
		{
			dataAdapterService = new BeanDataAdapterService(jasperReportsContext, (BeanDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof CsvDataAdapter)
		{
			dataAdapterService = new CsvDataAdapterService(jasperReportsContext, (CsvDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof DataSourceDataAdapter)
		{
			dataAdapterService = new DataSourceDataAdapterService(jasperReportsContext, (DataSourceDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof EmptyDataAdapter)
		{
			dataAdapterService = new EmptyDataAdapterService(jasperReportsContext, (EmptyDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof JndiDataAdapter)
		{
			dataAdapterService = new JndiDataAdapterService(jasperReportsContext, (JndiDataAdapter)dataAdapter);//FIXME maybe want some cache here
		}
		else if (dataAdapter instanceof DataSourceProviderDataAdapter)
		{
			dataAdapterService = new DataSourceProviderDataAdapterService(jasperReportsContext, (DataSourceProviderDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof QueryExecuterDataAdapter)
		{
			dataAdapterService = new QueryExecuterDataAdapterService(jasperReportsContext, (QueryExecuterDataAdapter)dataAdapter);
		}
		
		// these following three adapters must be kept in order of inheritance hierarchy
		else if (dataAdapter instanceof ExcelDataAdapter)
		{
			dataAdapterService = new ExcelDataAdapterService(jasperReportsContext, (ExcelDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof XlsxDataAdapter)
		{
			dataAdapterService = new XlsxDataAdapterService(jasperReportsContext, (XlsxDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof XlsDataAdapter)
		{
			if (
				JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(
					AbstractXlsDataAdapterService.PROPERTY_DATA_ADAPTER_USE_LEGACY_JEXCELAPI,
					false
					)
				)
			{
				@SuppressWarnings("deprecation")
				DataAdapterService dep = new net.sf.jasperreports.data.xls.JxlDataAdapterService(jasperReportsContext, (XlsDataAdapter)dataAdapter);
				dataAdapterService = dep;
			}
			else
			{
				dataAdapterService = new XlsDataAdapterService(jasperReportsContext, (XlsDataAdapter)dataAdapter);
			}
		}
		// end excel

		else if (dataAdapter instanceof XmlDataAdapter)
		{
			dataAdapterService = new XmlDataAdapterService(jasperReportsContext, (XmlDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof JsonDataAdapter)
		{
			dataAdapterService = new JsonDataAdapterService(jasperReportsContext, (JsonDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof HibernateDataAdapter)
		{
			dataAdapterService = new HibernateDataAdapterService(jasperReportsContext, (HibernateDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof SpringHibernateDataAdapter)
		{
			dataAdapterService = new SpringHibernateDataAdapterService(jasperReportsContext, (SpringHibernateDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof EjbqlDataAdapter)
		{
			dataAdapterService = new EjbqlDataAdapterService(jasperReportsContext, (EjbqlDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof MondrianDataAdapter)
		{
			dataAdapterService = new MondrianDataAdapterService(jasperReportsContext, (MondrianDataAdapter)dataAdapter);
		}
		else if (dataAdapter instanceof XmlaDataAdapter)
		{
			dataAdapterService = new XmlaDataAdapterService(jasperReportsContext, (XmlaDataAdapter)dataAdapter);
		}
		else if (dataAdapter.getClass().getName().equals(JdbcDataAdapterImpl.class.getName()))
		{
			dataAdapterService = new JdbcDataAdapterService(jasperReportsContext, (JdbcDataAdapter)dataAdapter);
		}
		
		return dataAdapterService;
	}
  
}
