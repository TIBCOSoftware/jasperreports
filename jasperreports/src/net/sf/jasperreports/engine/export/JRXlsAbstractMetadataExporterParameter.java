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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.XlsMetadataExporterConfiguration;
import net.sf.jasperreports.export.XlsMetadataReportConfiguration;


/**
 * Contains parameters useful for export to XLS format based on metadata.
 * @deprecated Replaced by {@link XlsMetadataExporterConfiguration}.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JRXlsAbstractMetadataExporterParameter extends JRXlsAbstractExporterParameter
{


	/**
	 *
	 */
	protected JRXlsAbstractMetadataExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * @deprecated Replaced by {@link XlsMetadataReportConfiguration#getColumnNames()}.
	 */
	public static final JRXlsAbstractMetadataExporterParameter COLUMN_NAMES = new JRXlsAbstractMetadataExporterParameter("Column Names");
	
	
	/**
	 * @deprecated Replaced by {@link XlsMetadataReportConfiguration#PROPERTY_COLUMN_NAMES_PREFIX}.
	 */
	public static final String PROPERTY_COLUMN_NAMES_PREFIX = XlsMetadataReportConfiguration.PROPERTY_COLUMN_NAMES_PREFIX;
	
	
	/**
	 * @deprecated Replaced by {@link XlsMetadataReportConfiguration#isWriteHeader()}.
	 */
	public static final JRXlsAbstractMetadataExporterParameter WRITE_HEADER = new JRXlsAbstractMetadataExporterParameter("Write Header");
	

	/**
	 * @deprecated Replaced by {@link XlsMetadataReportConfiguration#PROPERTY_WRITE_HEADER}.
	 */
	public static final String PROPERTY_WRITE_HEADER = XlsMetadataReportConfiguration.PROPERTY_WRITE_HEADER;

	
	/**
	 * @deprecated Replaced by {@link JRXlsAbstractMetadataExporter#PROPERTY_COLUMN_NAME}.
	 */
	public static final String PROPERTY_COLUMN_NAME = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.column.name";
	
	
	/**
	 * @deprecated Replaced by {@link JRXlsAbstractMetadataExporter#PROPERTY_REPEAT_VALUE}.
	 */
	public static final String PROPERTY_REPEAT_VALUE = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.repeat.value";
	
	
	/**
	 * @deprecated Replaced by {@link JRXlsAbstractMetadataExporter#PROPERTY_DATA}.
	 */
	public static final String PROPERTY_DATA = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.data";
	
}
