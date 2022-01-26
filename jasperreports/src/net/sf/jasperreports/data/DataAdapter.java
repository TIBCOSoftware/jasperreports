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
package net.sf.jasperreports.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import net.sf.jasperreports.data.bean.BeanDataAdapterImpl;
import net.sf.jasperreports.data.csv.CsvDataAdapterImpl;
import net.sf.jasperreports.data.ds.DataSourceDataAdapterImpl;
import net.sf.jasperreports.data.ejbql.EjbqlDataAdapterImpl;
import net.sf.jasperreports.data.empty.EmptyDataAdapterImpl;
import net.sf.jasperreports.data.excel.ExcelDataAdapterImpl;
import net.sf.jasperreports.data.hibernate.HibernateDataAdapterImpl;
import net.sf.jasperreports.data.hibernate.spring.SpringHibernateDataAdapterImpl;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapterImpl;
import net.sf.jasperreports.data.jndi.JndiDataAdapterImpl;
import net.sf.jasperreports.data.json.JsonDataAdapterImpl;
import net.sf.jasperreports.data.mondrian.MondrianDataAdapterImpl;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapterImpl;
import net.sf.jasperreports.data.qe.QueryExecuterDataAdapterImpl;
import net.sf.jasperreports.data.random.RandomDataAdapterImpl;
import net.sf.jasperreports.data.xls.XlsDataAdapterImpl;
import net.sf.jasperreports.data.xlsx.XlsxDataAdapterImpl;
import net.sf.jasperreports.data.xml.RemoteXmlDataAdapterImpl;
import net.sf.jasperreports.data.xml.XmlDataAdapterImpl;
import net.sf.jasperreports.data.xmla.XmlaDataAdapterImpl;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
@JsonSubTypes({
	@JsonSubTypes.Type(value = BeanDataAdapterImpl.class),
	@JsonSubTypes.Type(value = CsvDataAdapterImpl.class),
	@JsonSubTypes.Type(value = DataSourceDataAdapterImpl.class),
	@JsonSubTypes.Type(value = EjbqlDataAdapterImpl.class),
	@JsonSubTypes.Type(value = EmptyDataAdapterImpl.class),
	@JsonSubTypes.Type(value = ExcelDataAdapterImpl.class),
	@JsonSubTypes.Type(value = HibernateDataAdapterImpl.class),
	@JsonSubTypes.Type(value = SpringHibernateDataAdapterImpl.class),
	@JsonSubTypes.Type(value = JdbcDataAdapterImpl.class),
	@JsonSubTypes.Type(value = JndiDataAdapterImpl.class),
	@JsonSubTypes.Type(value = JsonDataAdapterImpl.class),
	@JsonSubTypes.Type(value = MondrianDataAdapterImpl.class),
	@JsonSubTypes.Type(value = DataSourceProviderDataAdapterImpl.class),
	@JsonSubTypes.Type(value = QueryExecuterDataAdapterImpl.class),
	@JsonSubTypes.Type(value = RandomDataAdapterImpl.class),
	@JsonSubTypes.Type(value = XlsDataAdapterImpl.class),
	@JsonSubTypes.Type(value = XlsxDataAdapterImpl.class),
	@JsonSubTypes.Type(value = XmlDataAdapterImpl.class),
	@JsonSubTypes.Type(value = RemoteXmlDataAdapterImpl.class),
	@JsonSubTypes.Type(value = XmlaDataAdapterImpl.class)
})
public interface DataAdapter
{
	/**
	 * 
	 */
	@JsonProperty(index=1)
	public String getName();
	
	/**
	 * 
	 */
	public void setName(String name);
}
