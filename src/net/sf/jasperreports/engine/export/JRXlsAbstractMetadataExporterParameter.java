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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Contains parameters useful for export to XLS format based on metadata.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
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
	 * An array of strings representing the comma-separated names of the columns that should be exported.
	 * NOTE: The order of the columns is important and for accurate results they should be in the same order as the original columns.
	 */
	public static final JRXlsAbstractMetadataExporterParameter COLUMN_NAMES = new JRXlsAbstractMetadataExporterParameter("Column Names");
	
	
	/**
	 * Properties having this prefix contain comma-separated column names.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_COLUMN_NAMES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xls.column.names";
	
	
	/**
	 * A boolean that specifies whether the export header (the column names) should be written or not.
	 * 
	 *  @see JRProperties
	 */
	public static final JRXlsAbstractMetadataExporterParameter WRITE_HEADER = new JRXlsAbstractMetadataExporterParameter("Write Header");
	

	/**
	 * Property whose value is used as default for the {@link #WRITE_HEADER WRITE_HEADER} export parameter.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_WRITE_HEADER = JRProperties.PROPERTY_PREFIX + "export.xls.write.header";

	
	/**
	 * A string that represents the name for the column that should appear in the XLS export.
	 * It must be one of the values in {@link #COLUMN_NAMES COLUMN_NAMES}, if provided. 
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_COLUMN_NAME = JRProperties.PROPERTY_PREFIX + "export.xls.column.name";
	
	
	/**
	 * Property that specifies whether the value associated with {@link #PROPERTY_COLUMN_NAME PROPERTY_COLUMN_NAME} should be repeated or not
	 * when it is missing.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_REPEAT_VALUE = JRProperties.PROPERTY_PREFIX + "export.xls.repeat.value";
	
	
	/**
	 * Property that specifies what value to associate with {@link #PROPERTY_COLUMN_NAME PROPERTY_COLUMN_NAME}.
	 * <p>
	 * The property itself defaults to the text value of the report element that this property is assigned to.
	 * </p>
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_DATA = JRProperties.PROPERTY_PREFIX + "export.xls.data";
	
}
