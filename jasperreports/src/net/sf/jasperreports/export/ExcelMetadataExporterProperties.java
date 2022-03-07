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
package net.sf.jasperreports.export;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public interface ExcelMetadataExporterProperties {
	
	/**
	 * A string that represents the name for the column that should appear in the XLS export.
	 * It must be one of the values in {@link XlsMetadataReportConfiguration#getColumnNames()}, if provided. 
	 * 
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_0_2
			)
	public static final String PROPERTY_COLUMN_NAME = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.column.name";
	
	/**
	 * Property that specifies whether the value associated with {@link #PROPERTY_COLUMN_NAME PROPERTY_COLUMN_NAME} should be repeated or not
	 * when it is missing.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * 
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			defaultValue = PropertyConstants.BOOLEAN_FALSE,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_0_2,
			valueType = Boolean.class
			)
	public static final String PROPERTY_REPEAT_VALUE = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.repeat.value";
	
	/**
	 * Property that specifies what value to associate with {@link #PROPERTY_COLUMN_NAME PROPERTY_COLUMN_NAME}.
	 * <p>
	 * The property itself defaults to the text value of the report element that this property is assigned to.
	 * </p>
	 * 
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.TEXT_ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_0_2
			)
	public static final String PROPERTY_DATA = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.data";
	
	/**
	 * Element-level property used to adjust the column width to values suitable for Excel metadata output, taking into account 
	 * that when column headers are included in the report, they come with no available width settings.
	 * Also keep in mind that column widths are measured in Excel in Normal style default character width units. The pixel-to-character width 
	 * translation depends on the default normal style character width, so it cannot be always accurately fitted. In this case, 
	 * one can adjust the current column width by setting this property with an integer value measured in pixels. The JR engine 
	 * will perform the pixel-to-character width mapping using this value instead of the element's <code>width</code> attribute.
	 * <br/>
	 * If defined, this property will override both the {@link ExcelExporterProperties#PROPERTY_COLUMN_WIDTH PROPERTY_COLUMN_WIDTH} value for 
	 * the current column and the the element's <code>width</code> attribute
	 * 
	 * @see ExcelExporterProperties#PROPERTY_COLUMN_WIDTH
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_6_19_1,
			valueType = Integer.class
			)
	public static final String PROPERTY_COLUMN_WIDTH_METADATA = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.column.width.metadata";

	public static final String CURRENT_ROW_HEIGHT = "CURRENT_ROW_HEIGHT";

	public static final String CURRENT_ROW_AUTOFIT = "CURRENT_ROW_AUTOFIT";
	
}
