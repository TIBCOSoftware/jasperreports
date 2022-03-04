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
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public interface ExcelExporterProperties {

	public static final String XLS_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.";
	public static final String DEFAULT_SHEET_NAME_PREFIX = "Page ";
	public static final String DEFAULT_DEFINED_NAME_SCOPE = "workbook";
	public static final String DEFAULT_DEFINED_NAME_SCOPE_SEPARATOR = "\\u007C";	// the '|' character
	
	public static final String EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL = "export.xls.common.cannot.add.cell";
	public static final String EXCEPTION_MESSAGE_KEY_CANNOT_MERGE_CELLS = "export.xls.common.cannot.merge.cells";
	public static final String EXCEPTION_MESSAGE_KEY_CELL_FORMAT_TEMPLATE_ERROR = "export.xls.common.cell.format.template.error";
	public static final String EXCEPTION_MESSAGE_KEY_COLUMN_INDEX_BEYOND_LIMIT = "export.xls.common.column.index.beyond.limit";
	public static final String EXCEPTION_MESSAGE_KEY_LOADED_FONTS_ERROR = "export.xls.common.loaded.fonts.error";
	public static final String EXCEPTION_MESSAGE_KEY_NEGATIVE_COLUMN_INDEX = "export.xls.common.negative.column.index";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR = "export.xls.common.report.generation.error";
	public static final String EXCEPTION_MESSAGE_KEY_TEMPLATE_NOT_FOUND = "export.xls.common.template.not.found";

	/**
	 * Property that stores the formula which has to be applied to a given cell in an excel sheet.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.TEXT_ELEMENT},
			sinceVersion = PropertyConstants.VERSION_3_0_0
			)
	public static final String PROPERTY_CELL_FORMULA = XLS_EXPORTER_PROPERTIES_PREFIX + "formula";

	/**
	 * Property that stores the pattern which has to be applied to a given cell in an excel sheet.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.TEXT_ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_1
			)
	public static final String PROPERTY_CELL_PATTERN = XLS_EXPORTER_PROPERTIES_PREFIX + "pattern";

	/**
	 * Property used to set the name of the sheet containing a given element. Its value overrides the report-level settings for the sheet name. 
	 * If several elements in the sheet contain this property, the engine will consider the value of the last exported element's property.
	 * 
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_3
			)
	public static final String PROPERTY_SHEET_NAME = XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.name";

	/**
	 * This property indicates the horizontal edge of the freeze pane, relative to the current cell. If set, it overrides the 
	 * PROPERTY_FREEZE_ROW value.
	 * Allowed values are:
	 * <ul>
	 * <li><code>Top</code> - The current row is the first unlocked row in the sheet. All rows above are 'frozen'.</li>
	 * <li><code>Bottom</code> - The current row is the last 'frozen' row in the sheet. All rows below are unlocked.</li>
	 * </ul>
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_1,
			valueType = EdgeEnum.class
			)
	public static final String PROPERTY_FREEZE_ROW_EDGE = XLS_EXPORTER_PROPERTIES_PREFIX + "freeze.row.edge";
	
	/**
	 * This property indicates the vertical edge of the freeze pane, relative to the current cell. If set, it overrides the 
	 * PROPERTY_FREEZE_COLUMN and PROPERTY_FREEZE_SHEET_COLUMNS values.
	 * Allowed values are:
	 * <ul>
	 * <li><code>Left</code> - The current column is the first unlocked column in the sheet. All columns to the left are 'frozen'.</li>
	 * <li><code>Right</code> - The current column is the last 'frozen' column in the sheet. All columns to the right are unlocked.</li>
	 * </ul>
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_1,
			valueType = EdgeEnum.class
			)
	public static final String PROPERTY_FREEZE_COLUMN_EDGE = XLS_EXPORTER_PROPERTIES_PREFIX + "freeze.column.edge";
	
	/**
	 * Flag property that indicates whether Excel should autofit the current row height.
	 * Allowed values are:
	 * <ul>
	 * <li><code>true</code></li>
	 * <li><code>false</code> - this is the default value.</li>
	 * </ul>
	 * 
	 * @see JRPropertiesUtil
	 * @since 4.5.1
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			defaultValue = PropertyConstants.BOOLEAN_FALSE,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_5_1,
			valueType = Boolean.class
			)
	public static final String PROPERTY_AUTO_FIT_ROW = XLS_EXPORTER_PROPERTIES_PREFIX + "auto.fit.row";
	
	/**
	 * Flag property that indicates whether Excel should autofit the current column width.
	 * Allowed values are:
	 * <ul>
	 * <li><code>true</code></li>
	 * <li><code>false</code> - this is the default value.</li>
	 * </ul>
	 * 
	 * @see JRPropertiesUtil
	 * @since 4.5.1
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			defaultValue = PropertyConstants.BOOLEAN_FALSE,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_5_1,
			valueType = Boolean.class
			)
	public static final String PROPERTY_AUTO_FIT_COLUMN = XLS_EXPORTER_PROPERTIES_PREFIX + "auto.fit.column";
	
	/**
	 * This element-level property is used to indicate the boundaries of the autofilter data range in the current sheet. 
	 * Allowed values are:
	 * <ul>
	 * <li><code>Start</code> - The current cell will be marked as autofilter heading cell, and column data below/to the right of 
	 * this cell can be considered as part of the autofilter data range. The starting point of the autofilter data range will be 
	 * the next cell below the current cell (ie if the current cell reference is B4, the autofilter range will start with the "B5" 
	 * cell reference: "B5:M20").
	 * <br/>
	 * If multiple autofilter <code>Start</code> values are found in the same sheet, only the last one will be considered. If the 
	 * <code>Start</code> value is present but no <code>End</code> value is found in the sheet, then only the current column will 
	 * be considered for the data range.
	 * <br/>
	 * If the autofilter <code>Start</code> value is set on the same row as the autofilter <code>End</code> value, the data range 
	 * will include all data below this heading row, placed between the start column and the end column.</li>
	 * <li><code>End</code> - The current cell will be marked as autofilter ending cell, and column data in this cell and 
	 * above/to the left can be considered as part of the autofilter data range. The ending cell in the data range is the 
	 * current cell reference (ie if the current cell reference is M20, the autofilter range will end in "M20": "B5:M20"). 
	 * The heading cell for the current column will be placed in the same column on the row containing the autofilter <code>Start</code> value. 
	 * <br/>
	 * <b>Caution:</b> If no autofilter <code>Start</code> value is found in the sheet, the autofilter <code>End</code> value will be considered 
	 * as <code>Start</code> value instead. 
	 * <br/>
	 * If multiple autofilter <code>End</code> value are found in the same sheet, only the last one will be considered. 
	 * <br/>
	 * If the autofilter <code>Start</code> value is set on the same row as the autofilter <code>End</code> value, the data range 
	 * will include all data below this heading row, placed between the start column and the end column.</li>
	 * </ul>
	 * 
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_3
			)
	public static final String PROPERTY_AUTO_FILTER = XLS_EXPORTER_PROPERTIES_PREFIX + "auto.filter";
	
	/**
	 * Element-level property used to adjust the column width to values suitable for Excel output, taking into account 
	 * that column widths are measured in Excel in Normal style default character width units. The pixel-to-character width 
	 * translation depends on the default normal style character width, so it cannot be always accurately fitted. In this case, 
	 * one can adjust the current column width by setting this property with an integer value measured in pixels. The JR engine 
	 * will perform the pixel-to-character width mapping using this value instead of the element's <code>width</code> attribute.
	 * <br/>
	 * If defined, this property will override the {@link XlsReportConfiguration#PROPERTY_COLUMN_WIDTH_RATIO PROPERTY_COLUMN_WIDTH_RATIO} value for the current column
	 * 
	 * @see XlsReportConfiguration#PROPERTY_COLUMN_WIDTH_RATIO
	 * @see JRPropertiesUtil
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_3,
			valueType = Integer.class
			)
	public static final String PROPERTY_COLUMN_WIDTH = XLS_EXPORTER_PROPERTIES_PREFIX + "column.width";

	/**
	 * Property prefix used to indicate the current outline row level, and when necessary, the ending row of the current outline row 
	 * group with the given level. The suffix 
	 * of these properties is associated with the outline level, while the property value indicates if the current row group should 
	 * continue or should end. The most recommended practice is to use the outline level itself as property suffix, although this is not 
	 * mandatory. The suffix may take any other string value, but one has to keep in mind that suffixes are used as sorted row level 
	 * descriptors. For instance, because "aaa" &lt; "bbb", the outline level associated with the "aaa" suffix will be smaller than 
	 * the level associated with the "bbb" suffix. The most intuitive representation of the row levels uses the row level as property suffix.
	 * <br/>
	 * In such a case, the {@link #PROPERTY_ROW_OUTLINE_LEVEL_PREFIX net.sf.jasperreports.export.xls.outline.level.2} property means that its value is correlated with 
	 * the outline level 2, so the current row belongs to a level 2 row group. Based on Office Open XML specs, allowed values for outline 
	 * levels are positive integers from 1 to 7.
	 * <br/>
	 * The value of this property could be any expression (including <code>null</code>). When such a property occurrence is met, the suffix 
	 * indicates the outline level for that row. If multiple properties with the same prefix are defined for the same row, the deepest 
	 * outline level is considered for that row. To end an outline row group one has to set the related outline level property with the 
	 * <code>End</code> value. This is a special property value instructing the JR engine that the current row group of that level ends 
	 * on the current row. 
	 * 
	 * @see JRPropertiesUtil
	 */
	@Property(
			name = "net.sf.jasperreports.export.xls.row.outline.level.{arbitrary_level}",
			category = PropertyConstants.CATEGORY_EXPORT,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_4_1_3
			)
	public static final String PROPERTY_ROW_OUTLINE_LEVEL_PREFIX = XLS_EXPORTER_PROPERTIES_PREFIX + "row.outline.level.";
	

	/**
	 * Element level property specifying if a sheet will break before the row displaying that element. 
	 * It is very useful especially when displaying each report's group on a separate sheet is intended. 
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			defaultValue = PropertyConstants.BOOLEAN_FALSE,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_3_0_0,
			valueType = Boolean.class
			)
	public static final String PROPERTY_BREAK_BEFORE_ROW = XLS_EXPORTER_PROPERTIES_PREFIX + "break.before.row";
	

	/**
	 * Element level property specifying if a sheet will break after the row displaying that element. 
	 * It is very useful especially when displaying each report's group on a separate sheet is intended. 
	 */
	@Property(
			category = PropertyConstants.CATEGORY_EXPORT,
			defaultValue = PropertyConstants.BOOLEAN_FALSE,
			scopes = {PropertyScope.ELEMENT},
			sinceVersion = PropertyConstants.VERSION_3_0_0,
			valueType = Boolean.class
			)
	public static final String PROPERTY_BREAK_AFTER_ROW = XLS_EXPORTER_PROPERTIES_PREFIX + "break.after.row";

}
