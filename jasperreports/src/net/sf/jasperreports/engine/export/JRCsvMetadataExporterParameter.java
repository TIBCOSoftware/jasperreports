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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.export.CsvMetadataExporterConfiguration;


/**
 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration}.
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvMetadataExporterParameter extends JRCsvExporterParameter
{


	/**
	 *
	 */
	protected JRCsvMetadataExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration#getColumnNames()}.
	 */
	public static final JRCsvMetadataExporterParameter COLUMN_NAMES = new JRCsvMetadataExporterParameter("Column Names");
	
	
	/**
	 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration#PROPERTY_COLUMN_NAMES_PREFIX}.
	 */
	public static final String PROPERTY_COLUMN_NAMES_PREFIX = CsvMetadataExporterConfiguration.PROPERTY_COLUMN_NAMES_PREFIX;
	
	
	/**
	 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration#isWriteHeader()}.
	 */
	public static final JRCsvMetadataExporterParameter WRITE_HEADER = new JRCsvMetadataExporterParameter("Write Header");
	

	/**
	 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration#PROPERTY_WRITE_HEADER}.
	 */
	public static final String PROPERTY_WRITE_HEADER = CsvMetadataExporterConfiguration.PROPERTY_WRITE_HEADER;

	
	/**
	 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration#PROPERTY_COLUMN_NAME}.
	 */
	public static final String PROPERTY_COLUMN_NAME = "CsvMetadataExporterConfiguration.PROPERTY_COLUMN_NAME";
	
	
	/**
	 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration#PROPERTY_REPEAT_VALUE}.
	 */
	public static final String PROPERTY_REPEAT_VALUE = "CsvMetadataExporterConfiguration.PROPERTY_REPEAT_VALUE";
	
	
	/**
	 * @deprecated Replaced by {@link CsvMetadataExporterConfiguration#PROPERTY_DATA}.
	 */
	public static final String PROPERTY_DATA = "CsvMetadataExporterConfiguration.PROPERTY_DATA";
	
}
