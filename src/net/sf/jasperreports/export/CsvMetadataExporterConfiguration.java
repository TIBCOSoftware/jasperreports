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
package net.sf.jasperreports.export;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * Interface containing settings used by the metadata based CSV exporter.
 *
 * @see JRCsvMetadataExporter
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface CsvMetadataExporterConfiguration extends CsvExporterConfiguration
{
	/**
	 * Property whose value is used as default for the {@link #isWriteHeader()} export configuration setting.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_WRITE_HEADER = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.write.header";

	/**
	 * Properties having this prefix contain comma-separated column names and are used as default 
	 * for the {@link #getColumnNames()} export configuration setting.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_COLUMN_NAMES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.column.names";

	/**
	 * Property specifying the name of the column that should appear in the CSV export.
	 * It must be one of the values in {@link #getColumnNames()}, if provided. 
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_COLUMN_NAME = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.column.name";
	
	/**
	 * Property that specifies whether the value associated with {@link #PROPERTY_COLUMN_NAME PROPERTY_COLUMN_NAME} should be repeated or not
	 * when it is missing.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_REPEAT_VALUE = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.repeat.value";
	
	/**
	 * Property that specifies what value to associate with {@link #PROPERTY_COLUMN_NAME PROPERTY_COLUMN_NAME}.
	 * <p>
	 * The property itself defaults to the text value of the report element that this property is assigned to.
	 * </p>
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_DATA = JRPropertiesUtil.PROPERTY_PREFIX + "export.csv.data";

	/**
	 * Returns a boolean that specifies whether the export header (the column names) should be written or not.
	 * The default is controlled by the {@link #PROPERTY_WRITE_HEADER} configuration property.
	 */
	@ExporterProperty(PROPERTY_WRITE_HEADER)
	public Boolean isWriteHeader();

	/**
	 * Returns an array of strings representing the comma-separated names of the columns that should be exported.
	 * NOTE: The order of the columns is important and, for accurate results, they should be in the same order as the original columns.
	 * The default is controlled by configuration properties having the {@link #PROPERTY_COLUMN_NAMES_PREFIX} name prefix.
	 */
	@ExporterProperty(PROPERTY_COLUMN_NAMES_PREFIX)
	public String[] getColumnNames();
}
