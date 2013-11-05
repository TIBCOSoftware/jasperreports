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

import java.util.Map;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface XlsExporterConfiguration extends ExporterConfiguration
{
	/**
	 * Property whose value is used as default state of the {@link #isOnePagePerSheet()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_ONE_PAGE_PER_SHEET = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.one.page.per.sheet";

	/**
	 * Property whose value is used as default state of the {@link #isRemoveEmptySpaceBetweenRows()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.remove.empty.space.between.rows";

	/**
	 * Property whose value is used as default state of the {@link #isRemoveEmptySpaceBetweenColumns()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.remove.empty.space.between.columns";

	/**
	 * Property whose value is used as default state of the {@link #isWhitePageBackground()} export configuration flag.
	 * <p/>
	 * This property is set by default (<code>true</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_WHITE_PAGE_BACKGROUND = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.white.page.background";

	/**
	 * This property serves as default for the {@link #isWrapText()} export configuration flag.
	 * <p>
	 * The property itself defaults to <code>true</code>.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_WRAP_TEXT = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "wrap.text";

	/**
	 * This property serves as default for the {@link #isCellLocked()} export configuration flag.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_CELL_LOCKED = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "cell.locked";

	/**
	 * This property serves as default for the {@link #isCellHidden()} export configuration flag.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_CELL_HIDDEN = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "cell.hidden";

	/**
	 * Property whose value is used as default state of the {@link #isDetectCellType()} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_DETECT_CELL_TYPE = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.detect.cell.type";

	/**
	 * Property whose value is used as default state of the {@link #isFontSizeFixEnabled()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_FONT_SIZE_FIX_ENABLED = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.font.size.fix.enabled";

	/**
	 * Property whose value is used as default state of the {@link #isImageBorderFixEnabled()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_IMAGE_BORDER_FIX_ENABLED = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.image.border.fix.enabled";

	/**
	 * Property whose value is used as default state of the {@link #isIgnoreGraphics()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_IGNORE_GRAPHICS = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.ignore.graphics";

	/**
	 * Property whose value is used as default state of the {@link #isCreateCustomPalette()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_CREATE_CUSTOM_PALETTE = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.create.custom.palette";

	/**
	 * Property whose value is used as default state of the {@link #isCollapseRowSpan()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_COLLAPSE_ROW_SPAN = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.collapse.row.span";

	/**
	 * Property whose value is used as default state of the {@link #isIgnoreCellBorder()} export configuration flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_IGNORE_CELL_BORDER = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.ignore.cell.border";

	/**
	 * Property whose value is used as default state of the {@link #isIgnoreCellBackground()} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_IGNORE_CELL_BACKGROUND = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.ignore.cell.background";

	/**
	 * Property whose value is used as default of the {@link #getMaxRowsPerSheet()} export configuration setting.
	 * <p/>
	 * This property is by default to zero.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_MAXIMUM_ROWS_PER_SHEET = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.max.rows.per.sheet";

	/**
	 * This property provides a default value for the {@link #getSheetHeaderLeft()} export configuration setting.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_HEADER_LEFT = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.header.left";

	/**
	 * This property provides a default value for the {@link #getSheetHeaderCenter()} export configuration setting.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_HEADER_CENTER = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.header.center";

	/**
	 * This property provides a default value for the {@link #getSheetHeaderRight()} export configuration setting.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_HEADER_RIGHT = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.header.right";

	/**
	 * This property provides a default value for the {@link #getSheetFooterLeft()} export configuration setting.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_FOOTER_LEFT = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.footer.left";

	/**
	 * This property provides a default value for the {@link #getSheetFooterCenter()} export configuration setting.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_FOOTER_CENTER = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.footer.center";

	/**
	 * This property provides a default value for the {@link #getSheetFooterRight()} export configuration setting.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_FOOTER_RIGHT = JRXlsAbstractExporter.XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.footer.right";

	/**
	 * Property whose value is used as default value of the {@link #getPassword()} export configuration setting.
	 * <p/>
	 * This property is by default not set (<code>null</code>).
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_PASSWORD = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.password";

	/**
	 * Property prefix used in order to identify a set of properties holding sheet names, very useful when 
	 * indicating sheet names in the jrxml template is required. These properties' values are collected in an 
	 * ordered list, therefore it is important the order they are written in. If set, these values are considered 
	 * as defaults for the {@link #getSheetNames()} export configuration setting.
	 * <p/>
	 * A property starting with this prefix can hold one or many sheet names. In order to be well parsed, 
	 * sheet names contained in such a property's value should be separated by a slash character ("/")
	 * <p/>
	 * These properties are by default not set.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_NAMES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.sheet.names.";

	/**
	 * Returns a boolean value specifying whether each report page should be written in a different XLS sheet.
	 * @see #PROPERTY_ONE_PAGE_PER_SHEET
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class, 
		parameterName="IS_ONE_PAGE_PER_SHEET"
		)
	@ExporterProperty(
		value=PROPERTY_ONE_PAGE_PER_SHEET, 
		booleanDefault=false
		)
	public Boolean isOnePagePerSheet();

	/**
	 * Returns a boolean value specifying whether the empty spaces that could appear between rows should be removed or not.
	 * @see #PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS"
		)
	@ExporterProperty(
		value=PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, 
		booleanDefault=false
		)
	public Boolean isRemoveEmptySpaceBetweenRows();

	/**
	 * Returns a boolean value specifying whether the empty spaces that could appear between columns should be removed or not.
	 * @see #PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class, 
		parameterName="IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS"
		)
	@ExporterProperty(
		value=PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, 
		booleanDefault=false
		)
	public Boolean isRemoveEmptySpaceBetweenColumns();

	/**
	 * Returns a boolean value specifying whether the page background should be white or the default XLS background color. This background
	 * may vary depending on the XLS viewer properties or the operating system color scheme.
	 * @see #PROPERTY_WHITE_PAGE_BACKGROUND
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class, 
		parameterName="IS_WHITE_PAGE_BACKGROUND"
		)
	@ExporterProperty(
		value=PROPERTY_WHITE_PAGE_BACKGROUND, 
		booleanDefault=false
		)
	public Boolean isWhitePageBackground();
	
	/**
	 * Flag used to indicate whether the exporter should take into consideration the type of the
	 * original text field expressions and set the cell types and values accordingly.
	 * <p>
	 * Text fields having numerical or date expressions save type and formatting (format pattern, locale and time zone)
	 * information in the {@link net.sf.jasperreports.engine.JasperPrint JasperPrint}/{@link net.sf.jasperreports.engine.JRPrintText JRPrintText}
	 * object created by the report fill process.
	 * </p>
	 * <p>
	 * When this flag is set, the exporter will parse back the <code>String</code> value of numerical/date texts.
	 * Numerical/date cells will be created and the original pattern of the text will be included
	 * as part of the cell style.
	 * </p>
	 * <p>
	 * Note that this mechanism would not work when the text field overflows and splits on two pages/columns.
	 * Also, it is required that the text field expression has a numerical or date type set.
	 * </p>
	 * <p>
	 * This flag is off by default to ensure backwards compatibility.
	 * </p>
	 * @see #PROPERTY_DETECT_CELL_TYPE
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_DETECT_CELL_TYPE"
		)
	@ExporterProperty(
		value=PROPERTY_DETECT_CELL_TYPE,
		booleanDefault=false
		)
	public Boolean isDetectCellType();

	/**
	 * Flag for decreasing font size so that texts fit into the specified cell height.
	 * @see #PROPERTY_FONT_SIZE_FIX_ENABLED
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_FONT_SIZE_FIX_ENABLED"
		)
	@ExporterProperty(
		value=PROPERTY_FONT_SIZE_FIX_ENABLED,
		booleanDefault=false
		)
	public Boolean isFontSizeFixEnabled();
	
	/**
	 * Flag for forcing the minimum image padding to 1 pixel, to avoid situations where the image hides the cell border.
	 * @see #PROPERTY_IMAGE_BORDER_FIX_ENABLED
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_IMAGE_BORDER_FIX_ENABLED"
		)
	@ExporterProperty(
		value=PROPERTY_IMAGE_BORDER_FIX_ENABLED,
		booleanDefault=false
		)

	public Boolean isImageBorderFixEnabled();
	
	/**
	 * Flag for ignoring graphic elements and exporting text elements only.
	 * @see #PROPERTY_IGNORE_GRAPHICS
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_IGNORE_GRAPHICS"
		)
	@ExporterProperty(
		value=PROPERTY_IGNORE_GRAPHICS,
		booleanDefault=false
		)
	public Boolean isIgnoreGraphics();
	
	/**
	 * Returns a boolean value specifying whether the standard color palette should be customized
	 * so that the XLS result uses the original report colors.
	 * <p/>
	 * The default state of this flag is given by the
	 * {@link #PROPERTY_CREATE_CUSTOM_PALETTE net.sf.jasperreports.export.xls.create.custom.palette} property.
	 * <p/>
	 * The colors used in the result XLS are determined in the following manner:
	 * <ol>
	 * 	<li>
	 * If this flag is not set, the nearest color from the standard XLS palette is chosen
	 * for a report color.
	 * 	</li>
	 * 	<li>
	 * If the flag is set, the nearest not yet modified color from the palette is chosen
	 * and modified to exactly match the report color.  If all the colors from the palette
	 * are modified (the palette has a fixed size), the nearest color from the palette is
	 * chosen for further report colors.
	 * 	</li>
	 * </ol> 
	 * 
	 * @see #PROPERTY_CREATE_CUSTOM_PALETTE
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="CREATE_CUSTOM_PALETTE"
		)
	@ExporterProperty(
		value=PROPERTY_CREATE_CUSTOM_PALETTE, 
		booleanDefault=false
		)
	public Boolean isCreateCustomPalette();
	
	/**
	 * Flag for collapsing row span and avoid merging cells across rows.
	 * @see #PROPERTY_COLLAPSE_ROW_SPAN
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_COLLAPSE_ROW_SPAN"
		)
	@ExporterProperty(
		value=PROPERTY_COLLAPSE_ROW_SPAN,
		booleanDefault=false
		)
	public Boolean isCollapseRowSpan();
	
	/**
	 * Flag for ignoring the cell border.
	 * @see #PROPERTY_IGNORE_CELL_BORDER
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_IGNORE_CELL_BORDER"
		)
	@ExporterProperty(
		value=PROPERTY_IGNORE_CELL_BORDER,
		booleanDefault=false
		)
	public Boolean isIgnoreCellBorder();
	
	/**
	 * Flag for ignoring the cell background color.
	 * @see #PROPERTY_IGNORE_CELL_BACKGROUND
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="IS_IGNORE_CELL_BACKGROUND"
		)
	@ExporterProperty(
		value=PROPERTY_IGNORE_CELL_BACKGROUND,
		booleanDefault=false
		)
	public Boolean isIgnoreCellBackground();
	
	/**
	 * This flag indicates whether text wrapping is allowed in a given cell.
	 * @see #PROPERTY_WRAP_TEXT
	 */
	@ExporterProperty(
		value=PROPERTY_WRAP_TEXT,
		booleanDefault=true
		)
	public Boolean isWrapText();
	
	/**
	 * This flag indicates whether the cell is locked.
	 * @see #PROPERTY_CELL_LOCKED
	 */
	@ExporterProperty(
		value=PROPERTY_CELL_LOCKED,
		booleanDefault=true
		)
	public Boolean isCellLocked();
	
	/**
	 * This flag indicates whether the cell content is hidden.
	 * @see #PROPERTY_CELL_HIDDEN
	 */
	@ExporterProperty(
		value=PROPERTY_CELL_HIDDEN,
		booleanDefault=false
		)
	public Boolean isCellHidden();
	
	/**
	 * Returns an integer value specifying the maximum number of rows allowed to be shown in a sheet.
	 * When set, a new sheet is created for the remaining rows to be displayed. Negative values or zero means that no limit has been set.
	 * @see #PROPERTY_MAXIMUM_ROWS_PER_SHEET
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="MAXIMUM_ROWS_PER_SHEET"
		)
	@ExporterProperty(
		value=PROPERTY_MAXIMUM_ROWS_PER_SHEET,
		intDefault=0
		)
	public Integer getMaxRowsPerSheet();
	
	/**
	 * Indicates whether page margins should be ignored when the report is exported using a grid-based exporter
	 * <p>
	 * If set to <code>true</code>, any page in the document will be exported without taking into account its margins.
	 * </p>
	 * @see #PROPERTY_IGNORE_PAGE_MARGINS
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.JRExporterParameter.class,
		parameterName="IGNORE_PAGE_MARGINS"
		)
	@ExporterProperty(
		value=PROPERTY_IGNORE_PAGE_MARGINS, 
		booleanDefault=false
		)
	public Boolean isIgnorePageMargins();
	
	/**
	 * This property stores the text content of the sheet header's left side.
	 * @see #PROPERTY_SHEET_HEADER_LEFT
	 */
	@ExporterProperty(PROPERTY_SHEET_HEADER_LEFT)
	public String getSheetHeaderLeft();
	
	/**
	 * This property stores the text content of the sheet header's center.
	 * @see #PROPERTY_SHEET_HEADER_CENTER
	 */
	@ExporterProperty(PROPERTY_SHEET_HEADER_CENTER)
	public String getSheetHeaderCenter();
	
	/**
	 * This property stores the text content of the sheet header's right side.
	 * @see #PROPERTY_SHEET_HEADER_RIGHT
	 */
	@ExporterProperty(PROPERTY_SHEET_HEADER_RIGHT)
	public String getSheetHeaderRight();
	
	/**
	 * This property stores the text content of the sheet footer's left side.
	 * @see #PROPERTY_SHEET_FOOTER_LEFT
	 */
	@ExporterProperty(PROPERTY_SHEET_FOOTER_LEFT)
	public String getSheetFooterLeft();
	
	/**
	 * This property stores the text content of the sheet footer's center.
	 * @see #PROPERTY_SHEET_FOOTER_CENTER
	 */
	@ExporterProperty(PROPERTY_SHEET_FOOTER_CENTER)
	public String getSheetFooterCenter();
	
	/**
	 * This property stores the text content of the sheet footer's right side.
	 * @see #PROPERTY_SHEET_FOOTER_RIGHT
	 */
	@ExporterProperty(PROPERTY_SHEET_FOOTER_RIGHT)
	public String getSheetFooterRight();

	/**
	 * Returns a String value representing the password in case of password protected documents.
	 * @see #PROPERTY_PASSWORD 
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JExcelApiExporterParameter.class,
		parameterName="PASSWORD"
		)
	@ExporterProperty(PROPERTY_PASSWORD)
	public String getPassword();

	/**
	 * Returns an array of strings representing custom sheet names. 
	 * This is useful when used with the <i>isOnePagePerSheet()</i> setting.
	 * @see #PROPERTY_SHEET_NAMES_PREFIX
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class,
		parameterName="SHEET_NAMES"
		)
	@ExporterProperty(PROPERTY_SHEET_NAMES_PREFIX)
	public String[] getSheetNames();
	
	/**
	 * This export parameter should be used when converting java format patterns to equivalent proprietary
	 * format patterns. It should be constructed as a Map containing java format patterns as keys and the
	 * correspondent proprietary format pattern as correspondent value
	 * <p/>
	 * @see org.apache.poi.hssf.usermodel.HSSFDataFormat
	 * @see jxl.write.NumberFormat
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		parameterClass=net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter.class, 
		parameterName="FORMAT_PATTERNS_MAP"
		)
	public Map<String, String> getFormatPatternsMap();
}
