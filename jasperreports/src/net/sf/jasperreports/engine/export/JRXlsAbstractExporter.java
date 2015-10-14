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

/*
 * Contributors:
 * Greg Hilton
 */

package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.SimplePrintPageFormat;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.ExportInterruptedException;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PrintPartUnrollExporterInput;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;


/**
 * Superclass for the Excel exporters.
 * <h3>Excel Exporters</h3>
 * For generating Excel files, there are currently two different exporter implementations
 * available in JasperReports. The first to appear was the
 * {@link net.sf.jasperreports.engine.export.JRXlsExporter} implementation, which
 * uses the Apache POI library to export documents to the Microsoft Excel 2003 file format (XLS). 
 * <p/>
 * Later on, with the introduction of the Microsoft Excel 2007 file format (XLSX), a new
 * exporter was added to JasperReports to support it. This exporter implementation is the
 * {@link net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter}; it does not rely
 * on any third party library to produce XLSX files.
 * <p/>
 * Because in XLS and XLSX all document content is placed inside cells, the Excel exporters
 * are considered typical grid exporters, and having their known limitations.
 * <h3>Configuring Sheets</h3>
 * An Excel file is structured in multiple sheets, and both exporters can be configured either
 * to put all pages inside the source {@link net.sf.jasperreports.engine.JasperPrint} document 
 * on one sheet (one after the another), or to put each page on a separate sheet in the resulting 
 * Excel file. The choice is made by setting the 
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#isOnePagePerSheet() isOnePagePerSheet()}
 * exporter configuration setting, which is set to <code>Boolean.FALSE</code> by default.
 * <p/>
 * When {@link net.sf.jasperreports.export.XlsReportConfiguration#isOnePagePerSheet() isOnePagePerSheet()} 
 * is set to true, or when you have to execute a batch export to XLS, multiple sheets are created in the worksheet.
 * You can also combine two exporter settings to customize the maximum number of rows per
 * page, and display each page of the report in a separate sheet. To do this, set the number
 * of rows per sheet for {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_MAXIMUM_ROWS_PER_SHEET net.sf.jasperreports.export.xls.max.rows.per.sheet} property and
 * set true for {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_ONE_PAGE_PER_SHEET net.sf.jasperreports.export.xls.one.page.per.sheet} property.
 * <p/>
 * The JasperReports Excel exporters provide a simple but efficient sheet-naming
 * mechanism. They use the {@link net.sf.jasperreports.export.XlsReportConfiguration#getSheetNames() getSheetNames()} 
 * exporter configuration setting to read custom sheet names
 * from the String array passed as value. This exporter setting can hold an array of
 * strings, which are passed as sheet names in order. If no value is supplied for this
 * setting or if the value contains fewer sheet names than actually needed
 * by the final document, then the sheets are named by default <code>Page i</code> (where i represents
 * the one-based sheet index).
 * <p/>
 * Taking into account the sheet name's length limitation in Excel (31 characters), if a sheet
 * name contains more than 31 characters it will be truncated as follows: the name of the
 * sheet will be given by the first 31 characters minus the sheet index length characters of
 * the document's name, followed by the sheet index, so that the entire name has exactly 31
 * characters.
 * <p/>
 * For example, if the second sheet name is TheQuickBrownFoxJumpsOverTheLazyDog
 * (35 chars), it will become TheQuickBrownFoxJumpsOverTheLa2 (the final zyDog gets
 * truncated, and the second sheet index 2 will end the name).
 * The name of the 12345-th sheet will become TheQuickBrownFoxJumpsOverT12345
 * (the final heLazyDog gets truncated, in order to make the exact room for 12345).
 * <p/>
 * Multiple sheet names can be specified in the JRXML file as well, using the
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_SHEET_NAMES_PREFIX net.sf.jasperreports.export.xls.sheet.names.&lt;arbitrary_name&gt;} property 
 * at report level. Add the following to the JRXML file:
 * <pre>
 * &lt;property name="net.sf.jasperreports.export.xls.sheet.names.all" value="Sheet A/Sheet B/Sheet C" /&gt;
 * </pre>
 * Keep in mind the naming order is important, sheets will be named in the same order the names are 
 * provided in this property. 
 * <p/>
 * Sheets can be also named at element level, using the {@link #PROPERTY_SHEET_NAME net.sf.jasperreports.export.xls.sheet.name} 
 * element property. This name will be provided for the sheet the element belongs to.
 * <h3>Flow-Oriented Output</h3>
 * The JasperPrint documents are page-oriented. When they are exported to a single sheet
 * Excel document, all the pages are rendered consecutively. Because all exporters try
 * to adhere as closely as possible to the quality and layout of the source document's
 * <code>Graphics2D</code> or PDF format, the page breaks are visible in Excel format. Sometimes this
 * is not desirable. One way to make page breaks less obvious and the layout more flow-based
 * is to suppress all the remaining blank space between cells on the vertical axis.
 * <p/>
 * When set to <code>Boolean.TRUE</code>, the 
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#isRemoveEmptySpaceBetweenRows() isRemoveEmptySpaceBetweenRows()} 
 * exporter configuration flag ensures that all empty rows on the resulting sheet are collapsed. By
 * default, the exporter preserves all the white space for a precise page layout.
 * The provided /demo/samples/nopagebreak sample shows you how to use this
 * setting when exporting to XLS to produce a more flow-based document layout.
 * <p/>
 * To completely ignore pagination, use the built-in fill-time parameter
 * {@link net.sf.jasperreports.engine.JRParameter#IS_IGNORE_PAGINATION IS_IGNORE_PAGINATION}.
 * You can modify the API to remove the empty space between rows and columns as well.
 * You need to set {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS net.sf.jasperreports.export.xls.remove.empty.space.between.rows}
 * property and/or {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS net.sf.jasperreports.export.xls.remove.empty.space.between.columns}
 * property to true.
 * <p/>
 * Keep in mind these settings are limited by your report layout. If it is too far away
 * from a grid layout, these options cannot work. On a good grid layout, once you set
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS net.sf.jasperreports.export.xls.remove.empty.space.between.rows} property and/or
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS net.sf.jasperreports.export.xls.remove.empty.space.between.columns} property to
 * true, the empty spaces are removed.
 * <h3>Cell Types</h3>
 * Inside the proprietary document format that JasperReports uses (represented by the
 * {@link net.sf.jasperreports.engine.JasperPrint} object), all text elements are 
 * considered alphanumeric values. This means that if a numeric text field of type 
 * <code>java.lang.Double</code> is placed in the report template at design time, all 
 * the text elements inside the {@link net.sf.jasperreports.engine.JasperPrint} object 
 * resulting from it will hold <code>java.lang.String</code> values, even though 
 * they are actually numbers. Therefore, in a sense, data type information is lost 
 * during report filling. This is because the main goal of JasperReports is to create 
 * documents for viewing and printing, not necessarily for further data manipulation inside 
 * tools like Excel, where formulas could be added to numeric cells.
 * <p/>
 * However, these resulting text elements found in the generated documents nowadays hold
 * enough data type information (in addition to the alphanumeric content) for the original
 * value of the text element to be re-created, if needed.
 * <p/>
 * Both Excel exporters support the 
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#isDetectCellType() isDetectCellType()} 
 * configuration flag, which forces the recreation
 * of the original cell value in accordance with its declared data type, as specified
 * in the report template. 
 * <p/>
 * Cell type detection is turned off by default.
 * You can have JasperReports automatically detect the cell type by modifying the API. Set the
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_DETECT_CELL_TYPE net.sf.jasperreports.export.xls.detect.cell.type} property to true. When you do this, instead
 * of being prompted by Excel to convert the value manually, the value is automatically
 * converted.
 * <h3>Format Pattern Conversions</h3>
 * It is important to keep in mind that standard Java format patterns are not 
 * completely supported by Microsoft Excel. There are rather few data patterns 
 * that make a perfect match between Java and Excel.
 * <p/>
 * In the case that the Java pattern stored in the generated report does not match any of the
 * supported Excel cell patterns, there is still a way to choose an appropriate Excel format
 * pattern. The solution is to use the 
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#getFormatPatternsMap() getFormatPatternsMap()} 
 * export configuration setting and supply
 * a <code>java.util.Map</code> as value. This map should contain Java format patterns as keys and
 * corresponding Excel format patterns as values.
 * <p/>
 * Another way to adjust the format pattern to Excel-compatible values is to set the 
 * {@link #PROPERTY_CELL_PATTERN net.sf.jasperreports.export.xls.pattern} property at element level.
 * <h3>Font Size Correction</h3>
 * Currently, there is no way to control the line spacing in a spreadsheet cell, which results
 * in the cell text not fitting exactly within the cell boundaries. As a workaround, in order to
 * force the cell text to fit, one can use the 
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#isFontSizeFixEnabled() isFontSizeFixEnabled()} 
 * exporter configuration flag to decrease the font size by one point when generating the cell format.
 * Alternatively, one can use the {@link net.sf.jasperreports.export.XlsReportConfiguration#PROPERTY_FONT_SIZE_FIX_ENABLED net.sf.jasperreports.export.xls.font.size.fix.enabled}
 * property at report level.
 * <h3>Background Color</h3>
 * Empty space found on each page in the source {@link net.sf.jasperreports.engine.JasperPrint} 
 * document normally results in empty cells on the corresponding sheet inside the Excel file. 
 * The background color of these empty cells is specified by the configuration of the Excel 
 * viewer itself. This makes the cells appear transparent. To force the document's background 
 * to be white, set the 
 * {@link net.sf.jasperreports.export.XlsReportConfiguration#isWhitePageBackground() isWhitePageBackground()} 
 * exporter configuration flag to <code>Boolean.TRUE</code>.
 * <h3>Excel Color Palette</h3>
 * In JasperReports, any color can be used for the background or the foreground of a report
 * element. However, when exporting to Excel format, only a limited set of colors is
 * supported, through what is called a <i>color palette</i>.
 * <p/>
 * If the colors used in a report template do not match the colors in the color palette, then
 * the Excel exporter will use a special algorithm to determine the closest matches by
 * comparing the RGB levels. However, the results might not always be as expected. A 
 * possibility to optimize the use of supported colors is to create a custom color palette. This 
 * can be achieved by setting to true the 
 * {@link net.sf.jasperreports.export.XlsExporterConfiguration#isCreateCustomPalette() isCreateCustomPalette()} 
 * export configuration flag. If the flag is set, the nearest not yet modified color from the palette is chosen
 * and modified to exactly match the report color.  If all the colors from the palette
 * are modified (the palette has a fixed size), the nearest color from the palette is
 * chosen for further report colors.
 * <p/>
 * To see other various exporter configuration settings, please consult the 
 * {@link net.sf.jasperreports.export.XlsReportConfiguration} and 
 * {@link net.sf.jasperreports.export.XlsExporterConfiguration} classes.
 * 
 * @see net.sf.jasperreports.export.XlsExporterConfiguration
 * @see net.sf.jasperreports.export.XlsReportConfiguration
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRXlsAbstractExporter<RC extends XlsReportConfiguration, C extends XlsExporterConfiguration, E extends JRExporterContext> 
	extends JRAbstractExporter<RC, C, OutputStreamExporterOutput, E>
{

	public static final String XLS_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.";
	public static final String DEFAULT_SHEET_NAME_PREFIX = "Page ";
	
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
	public static final String PROPERTY_CELL_FORMULA = XLS_EXPORTER_PROPERTIES_PREFIX + "formula";

	/**
	 * Property that stores the pattern which has to be applied to a given cell in an excel sheet.
	 */
	public static final String PROPERTY_CELL_PATTERN = XLS_EXPORTER_PROPERTIES_PREFIX + "pattern";

	/**
	 * Property used to set the name of the sheet containing a given element. Its value overrides the report-level settings for the sheet name. 
	 * If several elements in the sheet contain this property, the engine will consider the value of the last exported element's property.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_NAME = XLS_EXPORTER_PROPERTIES_PREFIX + "sheet.name";

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_WRAP_TEXT}.
	 */
	public static final String PROPERTY_WRAP_TEXT = XlsReportConfiguration.PROPERTY_WRAP_TEXT;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_FIT_WIDTH}.
	 */
	public static final String PROPERTY_FIT_WIDTH = XlsReportConfiguration.PROPERTY_FIT_WIDTH;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_FIT_HEIGHT}.
	 */
	public static final String PROPERTY_FIT_HEIGHT = XlsReportConfiguration.PROPERTY_FIT_HEIGHT;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_CELL_LOCKED}.
	 */
	public static final String PROPERTY_CELL_LOCKED = XlsReportConfiguration.PROPERTY_CELL_LOCKED;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_CELL_HIDDEN}.
	 */
	public static final String PROPERTY_CELL_HIDDEN = XlsReportConfiguration.PROPERTY_CELL_HIDDEN;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_HEADER_LEFT}.
	 */
	public static final String PROPERTY_SHEET_HEADER_LEFT = XlsReportConfiguration.PROPERTY_SHEET_HEADER_LEFT;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_HEADER_CENTER}.
	 */
	public static final String PROPERTY_SHEET_HEADER_CENTER = XlsReportConfiguration.PROPERTY_SHEET_HEADER_CENTER;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_HEADER_RIGHT}.
	 */
	public static final String PROPERTY_SHEET_HEADER_RIGHT = XlsReportConfiguration.PROPERTY_SHEET_HEADER_RIGHT;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_FOOTER_LEFT}.
	 */
	public static final String PROPERTY_SHEET_FOOTER_LEFT = XlsReportConfiguration.PROPERTY_SHEET_FOOTER_LEFT;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_FOOTER_CENTER}.
	 */
	public static final String PROPERTY_SHEET_FOOTER_CENTER = XlsReportConfiguration.PROPERTY_SHEET_FOOTER_CENTER;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_FOOTER_RIGHT}.
	 */
	public static final String PROPERTY_SHEET_FOOTER_RIGHT = XlsReportConfiguration.PROPERTY_SHEET_FOOTER_RIGHT;

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_DIRECTION}.
	 */
	public static final String PROPERTY_SHEET_DIRECTION = XlsReportConfiguration.PROPERTY_SHEET_DIRECTION;
	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_FREEZE_ROW}.
	 */
	public static final String PROPERTY_FREEZE_ROW = XlsReportConfiguration.PROPERTY_FREEZE_ROW;
	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_FREEZE_COLUMN}.
	 */
	public static final String PROPERTY_FREEZE_COLUMN = XlsReportConfiguration.PROPERTY_FREEZE_COLUMN;
	
	/**
	 * This property indicates the horizontal edge of the freeze pane, relative to the current cell. If set, it overrides the 
	 * PROPERTY_FREEZE_ROW value.
	 * Allowed values are:
	 * <ul>
	 * <li><code>Top</code> - The current row is the first unlocked row in the sheet. All rows above are 'frozen'.</li>
	 * <li><code>Bottom</code> - The current row is the last 'frozen' row in the sheet. All rows below are unlocked.</li>
	 * </ul>
	 */
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
	public static final String PROPERTY_COLUMN_WIDTH = XLS_EXPORTER_PROPERTIES_PREFIX + "column.width";

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_COLUMN_WIDTH_RATIO}.
	 */
	public static final String PROPERTY_COLUMN_WIDTH_RATIO = XlsReportConfiguration.PROPERTY_COLUMN_WIDTH_RATIO;
	
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
	public static final String PROPERTY_ROW_OUTLINE_LEVEL_PREFIX = XLS_EXPORTER_PROPERTIES_PREFIX + "row.outline.level.";
	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_USE_TIMEZONE}.
	 */
	public static final String PROPERTY_USE_TIMEZONE = XlsReportConfiguration.PROPERTY_USE_TIMEZONE;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_WORKBOOK_TEMPLATE}.
	 */
	public static final String PROPERTY_WORKBOOK_TEMPLATE = XlsExporterConfiguration.PROPERTY_WORKBOOK_TEMPLATE;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS}.
	 */
	public static final String PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS = XlsExporterConfiguration.PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS;

	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_IGNORE_ANCHORS}.
	 */
	public static final String PROPERTY_IGNORE_ANCHORS = XlsReportConfiguration.PROPERTY_IGNORE_ANCHORS;
	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_PAGE_SCALE}.
	 */
	public static final String PROPERTY_PAGE_SCALE = XlsReportConfiguration.PROPERTY_PAGE_SCALE;
	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_FIRST_PAGE_NUMBER}.
	 */
	public static final String PROPERTY_FIRST_PAGE_NUMBER = XlsReportConfiguration.PROPERTY_FIRST_PAGE_NUMBER;
	

	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHOW_GRIDLINES}.
	 */
	public static final String PROPERTY_SHOW_GRIDLINES = XlsReportConfiguration.PROPERTY_SHOW_GRIDLINES;
	
	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_IMAGE_ANCHOR_TYPE}.
	 */
	public static final String PROPERTY_IMAGE_ANCHOR_TYPE = XlsReportConfiguration.PROPERTY_IMAGE_ANCHOR_TYPE;

	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_IGNORE_HYPERLINK}.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = XlsReportConfiguration.PROPERTY_IGNORE_HYPERLINK;
	

	/**
	 * Element level property specifying if a sheet will break before the row displaying that element. 
	 * It is very useful especially when displaying each report's group on a separate sheet is intended. 
	 */
	public static final String PROPERTY_BREAK_BEFORE_ROW = XLS_EXPORTER_PROPERTIES_PREFIX + "break.before.row";
	

	/**
	 * Element level property specifying if a sheet will break after the row displaying that element. 
	 * It is very useful especially when displaying each report's group on a separate sheet is intended. 
	 */
	public static final String PROPERTY_BREAK_AFTER_ROW = XLS_EXPORTER_PROPERTIES_PREFIX + "break.after.row";
	

	protected static class TextAlignHolder
	{
		public final HorizontalTextAlignEnum horizontalAlignment;
		public final VerticalTextAlignEnum verticalAlignment;
		public final RotationEnum rotation;

		public TextAlignHolder(HorizontalTextAlignEnum horizontalAlignment, VerticalTextAlignEnum verticalAlignment, RotationEnum rotation)
		{
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
		}
	}

	/**
	 *
	 */
	protected List<Object> loadedFonts = new ArrayList<Object>();

	/**
	 *
	 */
	protected ExporterNature nature;

	/**
	 *
	 */
	protected String[] sheetNames;
	
	protected int reportIndex;
	protected int pageIndex;
	protected PrintPageFormat pageFormat;
	protected Map<Integer, Boolean> onePagePerSheetMap = new HashMap<Integer, Boolean>();
	protected int sheetsBeforeCurrentReport;
	protected Map<Integer, Integer> sheetsBeforeCurrentReportMap = new HashMap<Integer, Integer>();
	

	/**
	 *
	 */
	protected JRFont defaultFont;

	/**
	 * Used for counting the total number of sheets.
	 */
	protected int sheetIndex;

	/**
	 * Used for iterating through sheet names.
	 */
	protected int sheetNamesIndex;

	/**
	 * Used when indexing the identical sheet name. Contains sheet names as keys and the number of 
	 * occurrences of each sheet name as values.
	 */
	protected Map<String,Integer> sheetNamesMap;

//	protected int gridRowFreezeIndex;
//	protected int gridColumnFreezeIndex;
//	
//	protected int maxRowFreezeIndex;
//	protected int maxColumnFreezeIndex;
//	
//	protected boolean isFreezeRowEdge;
//	protected boolean isFreezeColumnEdge;
	
	protected String autoFilterStart;		
	protected String autoFilterEnd;		

	protected boolean firstPageNotSet;
	
	protected Boolean keepTemplateSheets;
	protected String workbookTemplate;
	
	protected String invalidCharReplacement;
	protected int maxColumnIndex;
	
	protected SheetInfo sheetInfo;
	
	public static class SheetInfo
	{
		public Integer sheetFirstPageIndex;
		public String sheetName;
		public Integer sheetFirstPageNumber;		
		public Integer sheetPageScale;		
		public Boolean sheetShowGridlines;
		public Color tabColor;
		public Boolean ignoreCellBorder;
		public Boolean ignoreCellBackground;
		public Boolean whitePageBackground;
		public Integer columnFreezeIndex;
		public Integer rowFreezeIndex;

	}

	/**
	 *
	 */
	protected JRFont getDefaultFont()
	{
		return defaultFont;
	}


	/**
	 * @see #JRXlsAbstractExporter(JasperReportsContext)
	 */
	public JRXlsAbstractExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public JRXlsAbstractExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		initExport();

		ensureOutput();
		
		OutputStream outputStream = getExporterOutput().getOutputStream();

		try
		{
			exportReportToStream(outputStream);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			getExporterOutput().close();
			resetExportContext();
		}
	}

	
	/**
	 *
	 */
	@Override
	protected void ensureInput()
	{
		super.ensureInput();

		exporterInput = new PrintPartUnrollExporterInput(exporterInput);

		jasperPrint = exporterInput.getItems().get(0).getJasperPrint();//this is just for the sake of getCurrentConfiguration() calls made prior to any setCurrentExporterInputItem() call
	}


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected void ensureOutput()
	{
		if (exporterOutput == null)
		{
			exporterOutput = 
				new net.sf.jasperreports.export.parameters.ParametersOutputStreamExporterOutput(
					getJasperReportsContext(),
					getParameters(),
					getCurrentJasperPrint()
					);
		}
	}


	@Override
	protected void initExport()
	{
		super.initExport();

		sheetIndex = 0;
		sheetInfo = null;
		onePagePerSheetMap.clear();
		sheetsBeforeCurrentReport = 0;
		sheetsBeforeCurrentReportMap.clear();
	}


	@Override
	protected void initReport()
	{
		super.initReport();

		setSheetNames();
		
//		gridRowFreezeIndex = Math.max(0, getPropertiesUtil().getIntegerProperty(jasperPrint, XlsReportConfiguration.PROPERTY_FREEZE_ROW, 0) - 1);
//		gridColumnFreezeIndex = Math.max(0, getColumnIndex(getPropertiesUtil().getProperty(jasperPrint, XlsReportConfiguration.PROPERTY_FREEZE_COLUMN)));	
		if(jasperPrint.hasProperties() && jasperPrint.getPropertiesMap().containsProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS))
		{
			// allows null values for the property
			invalidCharReplacement = jasperPrint.getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS);
		}
		else
		{
			invalidCharReplacement = getPropertiesUtil().getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS, jasperPrint);
		}
	}
	
	protected void exportReportToStream(OutputStream os) throws JRException, IOException
	{
		openWorkbook(os);
		sheetNamesMap = new HashMap<String,Integer>();
		pageFormat = null;
		boolean pageExported = false;
		List<ExporterInputItem> items = exporterInput.getItems();

		for(reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);

			setCurrentExporterInputItem(item);
			
			defaultFont = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
			
			if(!hasGlobalSheetNames())
			{
				sheetNamesIndex = 0;
			}

			XlsReportConfiguration configuration = getCurrentItemConfiguration();

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				if (configuration.isOnePagePerSheet())
				{
					for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new ExportInterruptedException();
						}

						JRPrintPage page = pages.get(pageIndex);
						
						pageFormat = jasperPrint.getPageFormat(pageIndex);
						
						/*   */
						exportPage(page, /*xCuts*/null, /*startRow*/0, /*defaultSheetName*/null);
					}
				}
				else
				{
					/*
					 * Make a pass and calculate the X cuts for all pages on this sheet.
					 * The Y cuts can be calculated as each page is exported.
					 */
					CutsInfo xCuts = 
						JRGridLayout.calculateXCuts(
							getNature(), jasperPrint, startPageIndex, endPageIndex,
							configuration.getOffsetX() == null ? 0 : configuration.getOffsetX() 
							);
					
					//clear the filter's internal cache that might have built up
					if (filter instanceof ResetableExporterFilter)
					{
						((ResetableExporterFilter)filter).reset();
					}
					int startRow = 0;

					for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new ExportInterruptedException();
						}
						JRPrintPage page = pages.get(pageIndex);
						pageFormat = jasperPrint.getPageFormat(pageIndex);
						startRow = exportPage(page, xCuts, startRow, jasperPrint.getName());//FIXMEPART
					}
					//updateColumns(xCuts);
				}
				pageExported = true;
			} 
			else if(reportIndex == items.size() -1 && !pageExported)
			{
				exportEmptyReport();
			}
			sheetsBeforeCurrentReport = configuration.isOnePagePerSheet() ? sheetIndex : sheetsBeforeCurrentReport + 1;
		}
		closeSheet();
		closeWorkbook(os);
	}

	/**
	 *
	 * @return the number of rows added.
	 */
	protected int exportPage(JRPrintPage page, CutsInfo xCuts, int startRow, String defaultSheetName) throws JRException
	{
		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		
		int maxRowsPerSheet = getMaxRowsPerSheet();
		boolean isRemoveEmptySpaceBetweenRows = configuration.isRemoveEmptySpaceBetweenRows();
		boolean isRemoveEmptySpaceBetweenColumns = configuration.isRemoveEmptySpaceBetweenColumns();
		boolean isCollapseRowSpan = configuration.isCollapseRowSpan();
		
		JRGridLayout layout =
			new JRGridLayout(
				getNature(),
				page.getElements(),
				pageFormat.getPageWidth(),
				pageFormat.getPageHeight(),
				configuration.getOffsetX() == null ? 0 : configuration.getOffsetX(), 
				configuration.getOffsetY() == null ? 0 : configuration.getOffsetY(),
				xCuts
				);

		Grid grid = layout.getGrid();

		boolean createXCuts = (xCuts == null); 
		
		if (createXCuts) 
		{
			xCuts = layout.getXCuts();
		}

		CutsInfo yCuts = layout.getYCuts();
		
		if (createXCuts || startRow == 0)
		{
			exportSheet(xCuts, yCuts, 0, defaultSheetName);
		}

		XlsRowLevelInfo levelInfo = new XlsRowLevelInfo(); 

		int skippedRows = 0;
		int rowIndex = startRow;
		int rowCount = grid.getRowCount();
		for(int y = 0; y < rowCount; y++)
		{
			Cut yCut = yCuts.getCut(y);

			rowIndex = y - skippedRows + startRow;

			//if number of rows is too large a new sheet is created and populated with remaining rows
			if(
				(maxRowsPerSheet > 0 && rowIndex >= maxRowsPerSheet)
				|| yCut.isBreak() 
				)
			{
				//updateColumns(xCuts);
				
				setRowLevels(levelInfo, null);
				
				exportSheet(xCuts, yCuts, y, defaultSheetName);
				
				startRow = 0;
				rowIndex = 0;
				skippedRows = y;
			}
			
			if (
				yCut.isCutNotEmpty()
				|| ((!isRemoveEmptySpaceBetweenRows || yCut.isCutSpanned())
				&& !isCollapseRowSpan)
				)
			{
				GridRow gridRow = grid.getRow(y);

				int emptyCellColSpan = 0;
				//int emptyCellWidth = 0;

				mergeAndSetRowLevels(levelInfo, (SortedMap<String, Boolean>)yCut.getProperty(PROPERTY_ROW_OUTLINE_LEVEL_PREFIX), rowIndex);

				setRowHeight(
					rowIndex,
					isCollapseRowSpan
						?  layout.getMaxRowHeight(y)//FIXME consider putting these in cuts
						: JRGridLayout.getRowHeight(gridRow),
					yCuts.getCut(y),
					levelInfo
					);

				int emptyCols = 0;
				int rowSize = gridRow.size();
				for(int xCutIndex = 0; xCutIndex < rowSize; xCutIndex++)
				{
					Cut xCut = xCuts.getCut(xCutIndex);

					boolean isEmptyCol = !(xCut.isCutNotEmpty() || xCut.isCutSpanned());//FIXMEXLS we could do this only once
					emptyCols += isRemoveEmptySpaceBetweenColumns && isEmptyCol ? 1 : 0;
					
					int colIndex = xCutIndex - emptyCols;
					if(colIndex > maxColumnIndex)
					{
						throw 
							new JRException(
								EXCEPTION_MESSAGE_KEY_COLUMN_INDEX_BEYOND_LIMIT, 
								new Object[]{colIndex, maxColumnIndex});
					}
					
					JRExporterGridCell gridCell = gridRow.get(xCutIndex);

					//setCell(gridCell, colIndex, rowIndex);
					
					if (gridCell.getType() == JRExporterGridCell.TYPE_OCCUPIED_CELL)
					{
						if (emptyCellColSpan > 0)
						{
							//tableHelper.exportEmptyCell(gridCell, emptyCellColSpan);
							emptyCellColSpan = 0;
							//emptyCellWidth = 0;
						}

						addOccupiedCell((OccupiedGridCell)gridCell, colIndex, rowIndex);
					}
					else if(gridCell.getType() == JRExporterGridCell.TYPE_ELEMENT_CELL)
					{
						if (emptyCellColSpan > 0)
						{
//							if (emptyCellColSpan > 1)
//							{
//								//sbuffer.append(" colspan=" + emptyCellColSpan);
//								//sheet.addMergedRegion(new Region(y, (short)(x - emptyCellColSpan - 1), y, (short)(x - 1)));
//							}
							emptyCellColSpan = 0;
							//emptyCellWidth = 0;
						}

						JRPrintElement element = gridCell.getElement();
						
						/* the following code is no more necessary since these properties are stored in SheetInfo via CutsInfo */
						
//						String rowFreeze = getPropertiesUtil().getProperty(element, PROPERTY_FREEZE_ROW_EDGE);
//						
//						int rowFreezeIndex = rowFreeze == null 
//								? 0 
//								: (EdgeEnum.BOTTOM.getName().equals(rowFreeze) 
//										? rowIndex + gridCell.getRowSpan()
//										: rowIndex
//										);
//							
//						String columnFreeze = getPropertiesUtil().getProperty(element, PROPERTY_FREEZE_COLUMN_EDGE);
//							
//						int columnFreezeIndex = columnFreeze == null 
//							? 0
//							: (EdgeEnum.RIGHT.getName().equals(columnFreeze) 
//									? colIndex + gridCell.getColSpan()
//									: colIndex
//									);
//
//						if(rowFreezeIndex > 0 || columnFreezeIndex > 0)
//						{
//							setFreezePane(rowFreezeIndex, columnFreezeIndex, rowFreezeIndex > 0, columnFreezeIndex > 0);
//						}
						
//						String sheetName = element.getPropertiesMap().getProperty(JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAME);
//						if(sheetName != null)
//						{
//							setSheetName(sheetName);
//						}


//						boolean start = getPropertiesUtil().getBooleanProperty(element, PROPERTY_AUTO_FILTER_START, false);
//						if(start && rowIndex < MAX_ROW_INDEX)
//						{
//							autoFilterStart = getColumnName(colIndex) + (rowIndex + 1);
//						}
//						
//						boolean end = getPropertiesUtil().getBooleanProperty(element, PROPERTY_AUTO_FILTER_END, false);
//						if(end && rowIndex < MAX_ROW_INDEX)
//						{
//							autoFilterEnd = getColumnName(colIndex) + (rowIndex + 1);
//						}
//						
						String autofilter = getPropertiesUtil().getProperty(element, PROPERTY_AUTO_FILTER);
						if("Start".equals(autofilter))
						{
							autoFilterStart = getColumnName(colIndex) + (rowIndex + 1);
						}
						else if("End".equals(autofilter))
						{
							autoFilterEnd = getColumnName(colIndex) + (rowIndex + 1);
						}
						
						if (element instanceof JRPrintLine)
						{
							exportLine((JRPrintLine)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintRectangle)
						{
							exportRectangle((JRPrintRectangle)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintEllipse)
						{
							exportRectangle((JRPrintEllipse)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintImage)
						{
							exportImage((JRPrintImage) element, gridCell, colIndex, rowIndex, emptyCols, y, layout);
						}
						else if (element instanceof JRPrintText)
						{
							exportText((JRPrintText)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintFrame)
						{
							exportFrame((JRPrintFrame) element, gridCell, colIndex, y);//FIXME rowIndex?
						}
						else if (element instanceof JRGenericPrintElement)
						{
							exportGenericElement((JRGenericPrintElement) element, gridCell, colIndex, rowIndex, emptyCols, y, layout);
						}

						//colIndex += gridCell.getColSpan() - 1;
					}
					else
					{
						emptyCellColSpan++;
						//emptyCellWidth += gridCell.getWidth();
						if (!isRemoveEmptySpaceBetweenColumns || !isEmptyCol)
						{
							addBlankCell(gridCell, colIndex, rowIndex);
						}
					}
				}

//				if (emptyCellColSpan > 0)
//				{
//					if (emptyCellColSpan > 1)
//					{
//						//sbuffer.append(" colspan=" + emptyCellColSpan);
//						//sheet.addMergedRegion(new Region(y, (short)x, y, (short)(x + emptyCellColSpan - 1)));
//					}
//				}
				
				//increment row index to return proper value
				++rowIndex;
			}
			else
			{
				skippedRows++;
//				setRowHeight(y, 0);
//
//				for(int x = 0; x < grid[y].length; x++)
//				{
//					addBlankCell(x, y);
//					setCell(x, y);
//				}
			}
		}

		if (configuration.isForcePageBreaks())
		{
			addRowBreak(rowCount - skippedRows + startRow - 1);
		}
		
		if(autoFilterStart != null)
		{
			setAutoFilter(autoFilterStart + ":" + (autoFilterEnd != null ? autoFilterEnd : autoFilterStart));
		}
		else if(autoFilterEnd != null)
		{
			setAutoFilter(autoFilterEnd + ":" + autoFilterEnd);
		}
		
//		if (createXCuts)
//		{
//			updateColumns(xCuts);
//		}
		
		setRowLevels(levelInfo, null);
		
		JRExportProgressMonitor progressMonitor = configuration.getProgressMonitor();
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
		
		// Return the number of rows added
		return rowIndex;
	}
	

	protected SheetInfo getSheetProps(CutsInfo xCuts, CutsInfo yCuts, int startCutIndex)
	{
		SheetInfo sheetInfo = new SheetInfo();
		
		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		int maxRowsPerSheet = getMaxRowsPerSheet();
		boolean isRemoveEmptySpaceBetweenRows = configuration.isRemoveEmptySpaceBetweenRows();
		boolean isCollapseRowSpan = configuration.isCollapseRowSpan();
		sheetInfo.tabColor = configuration.getSheetTabColor();
		sheetInfo.ignoreCellBackground = configuration.isIgnoreCellBackground();
		sheetInfo.whitePageBackground = configuration.isWhitePageBackground();
		sheetInfo.ignoreCellBorder = configuration.isIgnoreCellBorder();
		boolean elementLevelRowFreeze = false, elementLevelColumnFreeze = false;
		sheetInfo.rowFreezeIndex = configuration.getFreezeRow() == null
			? -1
			: Math.max(0, configuration.getFreezeRow() - 1);
		sheetInfo.columnFreezeIndex = configuration.getFreezeColumn() == null
			? -1
			: Math.max(0, getColumnIndex(configuration.getFreezeColumn()));
		
		int skippedRows = 0;
		int rowIndex = 0;
		int rowCount = yCuts.size() - 1;
		for(int y = startCutIndex; y < rowCount; y++)
		{
			Cut yCut = yCuts.getCut(y);
			
			rowIndex = y - skippedRows;

			if(
				y > startCutIndex &&
				((maxRowsPerSheet > 0 && rowIndex >= maxRowsPerSheet)
				|| yCut.isBreak()) 
				)
			{
				break;
			}
			
			if (
				yCut.isCutNotEmpty()
				|| ((!isRemoveEmptySpaceBetweenRows || yCut.isCutSpanned())
				&& !isCollapseRowSpan)
				)
			{
				String sheetName = (String)yCut.getProperty(PROPERTY_SHEET_NAME);
				if (sheetName != null)
				{
					sheetInfo.sheetName = sheetName;
				}

				String color = (String)yCut.getProperty(XlsReportConfiguration.PROPERTY_SHEET_TAB_COLOR);
				Color tabColor = JRColorUtil.getColor(color, null);
				if (tabColor != null)
				{
					sheetInfo.tabColor = tabColor;
				}

				Integer firstPageNumber = (Integer)yCut.getProperty(XlsReportConfiguration.PROPERTY_FIRST_PAGE_NUMBER);
				if (firstPageNumber != null)
				{
					sheetInfo.sheetFirstPageNumber = firstPageNumber;
				}
				Boolean showGridlines = (Boolean)yCut.getProperty(XlsReportConfiguration.PROPERTY_SHOW_GRIDLINES);
				if (showGridlines != null)
				{
					sheetInfo.sheetShowGridlines = showGridlines;
				}
				
				Boolean ignoreCellBackground = (Boolean)yCut.getProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND);
				if (ignoreCellBackground != null)
				{
					sheetInfo.ignoreCellBackground = ignoreCellBackground;
				}
				
				Boolean ignoreCellBorder = (Boolean)yCut.getProperty(XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER);
				if (ignoreCellBorder != null)
				{
					sheetInfo.ignoreCellBorder = ignoreCellBorder;
				}
				
				Boolean whitePageBackground = (Boolean)yCut.getProperty(XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND);
				if (whitePageBackground != null)
				{
					sheetInfo.whitePageBackground = whitePageBackground;
				}

				Integer pageScale = (Integer)yCut.getProperty(XlsReportConfiguration.PROPERTY_PAGE_SCALE);
				sheetInfo.sheetPageScale = (isValidScale(pageScale))
								? pageScale 
								: configuration.getPageScale();
				Integer freezeColumn = (Integer)yCut.getProperty(PROPERTY_FREEZE_COLUMN_EDGE);
				if(freezeColumn != null && (!elementLevelColumnFreeze || freezeColumn > sheetInfo.columnFreezeIndex))
				{
					sheetInfo.columnFreezeIndex = Math.max(0, freezeColumn);
					elementLevelColumnFreeze = true;
				}
				Integer freezeRow = (Integer)yCut.getProperty(PROPERTY_FREEZE_ROW_EDGE);
				if(freezeRow != null && (!elementLevelRowFreeze || freezeRow > sheetInfo.rowFreezeIndex))
				{
					sheetInfo.rowFreezeIndex = Math.max(0, freezeRow);
					elementLevelRowFreeze = true;
				}
				sheetInfo.sheetPageScale = (isValidScale(pageScale))
								? pageScale 
								: configuration.getPageScale();
				
				++rowIndex;
			}
			else
			{
				skippedRows++;
			}
		}
		
		return sheetInfo;
	}
	

	protected void exportSheet(CutsInfo xCuts, CutsInfo yCuts, int startCutIndex, String defaultSheetName)
	{
		if (sheetInfo != null)
		{
			closeSheet();
		}

		sheetInfo = getSheetProps(xCuts, yCuts, startCutIndex);
		
		sheetInfo.sheetName = getSheetName(sheetInfo.sheetName, defaultSheetName);
		
		sheetInfo.sheetFirstPageIndex = pageIndex;
		
		createSheet(xCuts, sheetInfo);

		// we need to count all sheets generated for all exported documents
		sheetIndex++;
		sheetNamesIndex++;
		resetAutoFilters();

		setFreezePane(sheetInfo.rowFreezeIndex, sheetInfo.columnFreezeIndex);
		setColumnWidths(xCuts);
	}
	

	protected void mergeAndSetRowLevels(XlsRowLevelInfo levelInfo, SortedMap<String, Boolean> rowLevelMap, int rowIndex)
	{
		SortedMap<String, Integer> crtLevelMap = levelInfo.getLevelMap();

		if (rowLevelMap != null)
		{
			for(String level : rowLevelMap.keySet())
			{
				Boolean isEndMarker = rowLevelMap.get(level);
				
				//check if this level group is already open
				if (crtLevelMap.containsKey(level))
				{
					//the level group is already open
					
					if (isEndMarker)
					{
						//the level group needs to be closed, together with all its child level groups
						setRowLevels(levelInfo, level);

						//clean up current level group and nested level groups as they were closed 
						for(Iterator<String> it = crtLevelMap.keySet().iterator(); it.hasNext();)
						{
							if (level.compareTo(it.next()) <= 0)
							{
								it.remove();
							}
						}
					}
				}
				else
				{
					//the level group is not yet open

					//we check to see if this level is higher than existing levels
					if (crtLevelMap.size() > 0 && level.compareTo(crtLevelMap.firstKey()) < 0)
					{
						//the level is higher than existing levels, so it has to close them all
						setRowLevels(levelInfo, level);
						
						//clean up nested level groups as they were closed; the current one is not yet among them 
						for(Iterator<String> it = crtLevelMap.keySet().iterator(); it.hasNext();)
						{
							if (level.compareTo(it.next()) < 0)
							{
								it.remove();
							}
						}
					}
					
					//create the current level group
//					XlsRowLevelRange range = new XlsRowLevelRange();
//					range.setStartIndex(rowIndex);
					//range.setEndIndex(rowIndex);
					//range.setName(groupName);
					crtLevelMap.put(level, rowIndex);
				}
			}
		}

		levelInfo.setEndIndex(rowIndex);
	}
	
	
	protected void setColumnWidths(CutsInfo xCuts)
	{
		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		
		boolean isRemoveEmptySpaceBetweenColumns = configuration.isRemoveEmptySpaceBetweenColumns();

		float sheetRatio = 1f; 

		Map<String, Object> xCutsProperties = xCuts.getPropertiesMap();
		Float columnWidthRatio = (Float)xCutsProperties.get(XlsReportConfiguration.PROPERTY_COLUMN_WIDTH_RATIO);
		if (columnWidthRatio != null && columnWidthRatio > 0f)
		{
			sheetRatio = columnWidthRatio;
		}
		else
		{
			columnWidthRatio = configuration.getColumnWidthRatio();
			if (columnWidthRatio != null && columnWidthRatio > 0f)
			{
				sheetRatio = columnWidthRatio;
			}
		}
		
		int emptyCols = 0;
		for(int xCutIndex = 0; xCutIndex < xCuts.size() - 1; xCutIndex++)
		{
			Cut xCut = xCuts.getCut(xCutIndex);

			if (!isRemoveEmptySpaceBetweenColumns || (xCut.isCutNotEmpty() || xCut.isCutSpanned()))
			{
				Integer width = (Integer)xCut.getProperty(PROPERTY_COLUMN_WIDTH);
				width = 
					width == null 
					? (int)((xCuts.getCutOffset(xCutIndex + 1) - xCuts.getCutOffset(xCutIndex)) * sheetRatio) 
					: width;  
				
				boolean isAutoFit = xCut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN) 
						&& (Boolean)xCut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN);
				
				setColumnWidth(xCutIndex - emptyCols, width, isAutoFit);
			}
			else
			{
				emptyCols ++;
			}
		}
	}
	
	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		JRStyledTextAttributeSelector selector = textElement.getModeValue() == ModeEnum.OPAQUE 
				? allSelector 
				: noBackcolorSelector;
		return textElement.getFullStyledText(selector);
	}

	/**
	 *
	 */
	protected static TextAlignHolder getTextAlignHolder(JRPrintText textElement)
	{
		HorizontalTextAlignEnum horizontalAlignment;
		VerticalTextAlignEnum verticalAlignment;
		RotationEnum rotation = textElement.getRotationValue();

		switch (textElement.getRotationValue())
		{
			case LEFT :
			{
				switch (textElement.getHorizontalTextAlign())
				{
					case LEFT :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalTextAlignEnum.TOP;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
					}
				}

				switch (textElement.getVerticalTextAlign())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalTextAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
					}
				}

				break;
			}
			case RIGHT :
			{
				switch (textElement.getHorizontalTextAlign())
				{
					case LEFT :
					{
						verticalAlignment = VerticalTextAlignEnum.TOP;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalTextAlignEnum.TOP;
					}
				}

				switch (textElement.getVerticalTextAlign())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalTextAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
					}
				}

				break;
			}
			case UPSIDE_DOWN:
			case NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalTextAlign();
				verticalAlignment = textElement.getVerticalTextAlign();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}

	/**
	 *
	 */
	protected int getImageBorderCorrection(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			if (lineWidth >= 2f)
			{
				return 2;
			}

			return 1;
		}
		
		return getCurrentItemConfiguration().isImageBorderFixEnabled() ? 1 : 0;
	}

	
	/**
	 *
	 */
	private String getSheetName(String sheetName, String defaultSheetName)
	{
		if(sheetName != null)
		{
			if (sheetNames != null && sheetNamesIndex < sheetNames.length) 
			{
				sheetNames[sheetNamesIndex] = sheetName;
			}
			return getSheetName(sheetName);
		}
		return getSheetName(defaultSheetName);
	}
	
	private String getSheetName(String sheetName)
	{
		if (sheetNames != null && sheetNamesIndex < sheetNames.length)
		{
			sheetName = sheetNames[sheetNamesIndex];
		}
		
		if (sheetName == null)
		{
			// no sheet name was specified or if it was null
			return DEFAULT_SHEET_NAME_PREFIX + (sheetIndex + 1);
		}

		// sheet name specified; assuming it is first occurrence
		int crtIndex = Integer.valueOf(1);
		String txtIndex = "";
		String validSheetName = sheetName.length() < 32 ? sheetName : sheetName.substring(0, 31);
		
		if(sheetNamesMap.containsKey(validSheetName))
		{
			// sheet names must be unique; altering sheet name using number of occurrences
			crtIndex = sheetNamesMap.get(validSheetName).intValue() + 1;
			txtIndex = String.valueOf(crtIndex);
		}

		sheetNamesMap.put(validSheetName, crtIndex);

		String name = sheetName;
		if(txtIndex.length() > 0)
		{
			name += " " + txtIndex;
		}
		
		if (name.length() > 31)
		{
			name = (sheetName + " ").substring(0, 31 - txtIndex.length()) + txtIndex;
		}
		
		return name;
	}

	// Berechnungsvorschriften f�r die DIN Formate A, B, und C.
	// Die Angabe der Breite/H�he erfolgt in [mm].

	protected final int calculateWidthForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (-0.25 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateHeightForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (0.25 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateWidthForDinBN(int n)
	{
		return (int) (Math.pow(2.0, -(n / 2.0)) * 1000.0);
	}

	protected final int calculateHeightForDinBN(int n)
	{
		return (int) (Math.pow(2.0, (0.5 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateWidthForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (-0.125 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateHeightForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (0.375 - (n / 2.0))) * 1000.0);
	}

	/**
	 * 
	 */
	protected boolean isWrapText(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_WRAP_TEXT)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_WRAP_TEXT, getCurrentItemConfiguration().isWrapText());
		}
		return getCurrentItemConfiguration().isWrapText();
	}

	/**
	 * 
	 */
	protected boolean isCellLocked(JRPrintElement element)
	{

		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_CELL_LOCKED)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_CELL_LOCKED, getCurrentItemConfiguration().isCellLocked());
		}
		return getCurrentItemConfiguration().isCellLocked();
	}

	/**
	 * 
	 */
	protected boolean isShrinkToFit(JRPrintElement element)
	{
		if (
				element.hasProperties()
				&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_SHRINK_TO_FIT)
				)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_SHRINK_TO_FIT, getCurrentItemConfiguration().isShrinkToFit());
		}
		return getCurrentItemConfiguration().isShrinkToFit();
	}
	
	/**
	 * 
	 */
	protected boolean isIgnoreTextFormatting(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_IGNORE_TEXT_FORMATTING)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(
											element, 
											XlsReportConfiguration.PROPERTY_IGNORE_TEXT_FORMATTING, 
											getCurrentItemConfiguration().isIgnoreTextFormatting());
		}
		return getCurrentItemConfiguration().isIgnoreTextFormatting();
	}

	/**
	 * 
	 */
	protected String getFormula(JRPrintText text)
	{
		String formula = JRPropertiesUtil.getOwnProperty(text, JRXlsAbstractExporter.PROPERTY_CELL_FORMULA);
		if( formula != null)
		{
			formula = formula.trim();
			if(formula.startsWith("="))
			{
				formula = formula.substring(1);
			}
		}
		return formula;
	}

	/**
	 * 
	 */
	protected void setSheetNames()
	{
		String[] sheetNamesArray = getCurrentItemConfiguration().getSheetNames();
		
		List<String> sheetNamesList = JRStringUtil.split(sheetNamesArray, "/");
		sheetNames = sheetNamesList == null ? null : (String[]) sheetNamesList.toArray(new String[sheetNamesList.size()]);
	}
	
	/**
	 * 
	 */
	protected boolean hasGlobalSheetNames()//FIXMEEXPORT check sheet names
	{
		Boolean globalSheetNames = null;
		
		boolean isOverrideHintsDefault = 
			propertiesUtil.getBooleanProperty(
				ReportExportConfiguration.PROPERTY_EXPORT_CONFIGURATION_OVERRIDE_REPORT_HINTS
				);
		if (
			itemConfiguration != null 
			&& itemConfiguration.getSheetNames() != null
			)
		{
			boolean isExporterConfigOverrideHints = 
				itemConfiguration.isOverrideHints() == null 
				? isOverrideHintsDefault 
				: itemConfiguration.isOverrideHints();
			if (isExporterConfigOverrideHints)
			{
				globalSheetNames = true;
			}
		}

		if (globalSheetNames == null)
		{
			XlsReportConfiguration lcItemConfiguration = (XlsReportConfiguration)crtItem.getConfiguration();
			if (
				lcItemConfiguration != null 
				&& lcItemConfiguration.getSheetNames() != null
				)
			{
				boolean isItemConfigOverrideHints = 
					lcItemConfiguration.isOverrideHints() == null 
					? isOverrideHintsDefault : 
					lcItemConfiguration.isOverrideHints();
				if (isItemConfigOverrideHints)
				{
					globalSheetNames = false;
				}
			}
		}

		if (globalSheetNames == null)
		{
			List<PropertySuffix> properties = 
				JRPropertiesUtil.getProperties(
					getCurrentJasperPrint(), 
					XlsReportConfiguration.PROPERTY_SHEET_NAMES_PREFIX
					);
			globalSheetNames = properties == null || properties.isEmpty();
		}
		
		return globalSheetNames;
	}
	
	/**
	 * 
	 */
	protected boolean isCellHidden(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_CELL_HIDDEN)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_CELL_HIDDEN, getCurrentItemConfiguration().isCellHidden());
		}
		return getCurrentItemConfiguration().isCellHidden();
	}

	/**
	 * 
	 */
	protected String getConvertedPattern(JRPrintText text, String pattern)
	{
		String convertedPattern = JRPropertiesUtil.getOwnProperty(text, PROPERTY_CELL_PATTERN);
		if (convertedPattern == null || convertedPattern.trim().length() == 0)
		{
			Map<String, String> formatPatternsMap = getCurrentItemConfiguration().getFormatPatternsMap();
			if (formatPatternsMap != null && formatPatternsMap.containsKey(pattern))
			{
				return formatPatternsMap.get(pattern);
			}
			return pattern == null || pattern.trim().length() == 0 ? null : pattern;
		}
		return convertedPattern;
	}

	/**
	 * 
	 * @return the calculated column index
	 */
	protected int getColumnIndex(String columnName)
	{
		int index = -1;
		if(columnName != null)
		{
			String upperColumnName = columnName.toUpperCase();
			if(upperColumnName.matches("[A-Z]*"))
			{
				for(int i = 0; i < upperColumnName.length(); i++)
				{
					index += (upperColumnName.charAt(i) - 64) * (int)Math.pow(26, upperColumnName.length() - i - 1);
				}
			}
		}
		return index;
	}
	
	/**
	 * 
	 * @return the calculated column name
	 */
	protected String getColumnName(int columnIndex)
	{
		int i = columnIndex/26 + 64;
		int j = columnIndex%26 + 65;
		return (i > 64 ? String.valueOf((char)i) : "") + (char)j;
	}
	
	protected void resetAutoFilters()
	{
		autoFilterStart = null;
		autoFilterEnd = null;
	}
	
	/**
	 * 
	 */
	protected boolean isUseTimeZone(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(XlsReportConfiguration.PROPERTY_USE_TIMEZONE)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, XlsReportConfiguration.PROPERTY_USE_TIMEZONE, getCurrentItemConfiguration().isUseTimeZone());
		}
		return getCurrentItemConfiguration().isUseTimeZone();
	}
	
	protected Date translateDateValue(JRPrintText text, Date value)
	{
		if (isUseTimeZone(text))
		{
			// translate the date to the timezone used at fill time
			TimeZone tz = getTextTimeZone(text);
			value = JRDataUtils.translateToTimezone(value, tz);
		}
		return value;
	}
	
	protected boolean isValidScale(Integer scale)
	{
		return scale != null && scale > 9 && scale < 401;
	}
	
	protected Integer getMaxRowsPerSheet()
	{
		return getCurrentItemConfiguration().getMaxRowsPerSheet();
	}

	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#isKeepWorkbookTemplateSheets()}.
	 */
	public boolean isWorkbookTemplateKeepSheets() {
		return keepTemplateSheets;
	}


	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#isKeepWorkbookTemplateSheets()}.
	 */
	public void setWorkbookTemplateKeepSheets(boolean keepTemplateSheets) {
		this.keepTemplateSheets = keepTemplateSheets;
	}


	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#getWorkbookTemplate()}.
	 */
	public String getWorkbookTemplate() {
		return workbookTemplate;
	}


	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#getWorkbookTemplate()}.
	 */
	public void setWorkbookTemplate(String workbookTemplate) {
		this.workbookTemplate = workbookTemplate;
	}
	
	
	protected ExporterNature getNature()
	{
		return nature;
	}
	

	protected void exportEmptyReport() throws JRException, IOException 
	{
		pageFormat = jasperPrint.getPageFormat();
		exportPage(new JRBasePrintPage(), null, 0, jasperPrint.getName());
	}


	/**
	 *
	 * @param colIndex The 0-based integer column index
	 * @return The column name computed from the 0-based column index
	 */
	public static String getColumIndexName(int colIndex, int maxColIndex)
	{
		
		if(colIndex < 0)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_NEGATIVE_COLUMN_INDEX, 
					new Object[]{colIndex});
		} 
		else if(colIndex > maxColIndex)	
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_COLUMN_INDEX_BEYOND_LIMIT, 
					new Object[]{colIndex, maxColIndex});
		}
		else if (colIndex < 26)
		{
			// first 26 column names are single letters
			return String.valueOf((char)(colIndex + 65));
		} 
		else if (colIndex < 702) 	// 702 = 26 + 26^2
		{
			// next 626 (= 26^2) column names are 2-letter names
			return String.valueOf((char)(colIndex/26 + 64)) 
				+ String.valueOf((char)(colIndex%26 + 65));
		} 
		else 
		{
			// next 17576 (= 26^3) column names are 3-letter names;
			// anyway, the 0-based column index may not exceed maxColIndex value
			return String.valueOf((char)((colIndex-26)/676 + 64)) 
				+ String.valueOf((char)(((colIndex-26)%676)/26 + 65)) 
				+ String.valueOf((char)(colIndex%26 + 65));
		}
	}

	//abstract methods

	protected abstract void openWorkbook(OutputStream os) throws JRException, IOException;

	protected abstract void createSheet(CutsInfo xCuts, SheetInfo sheetInfo);

	protected abstract void closeSheet();

	protected abstract void closeWorkbook(OutputStream os) throws JRException, IOException;

	protected abstract void setColumnWidth(int col, int width, boolean autoFit);
	
	protected abstract void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo) throws JRException;

	protected abstract void addRowBreak(int rowIndex);

//	protected abstract void setCell(JRExporterGridCell gridCell, int colIndex, int rowIndex);

	protected abstract void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException;

	protected abstract void addOccupiedCell(OccupiedGridCell occupiedGridCell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportText(JRPrintText text, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportImage(JRPrintImage image, JRExporterGridCell cell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException;

	protected abstract void exportRectangle(JRPrintGraphicElement element, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportLine(JRPrintLine line, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportFrame(JRPrintFrame frame, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportGenericElement(JRGenericPrintElement element, JRExporterGridCell cell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException;

	protected abstract void setFreezePane(int rowIndex, int colIndex);
	
	/**
	 * @deprecated to be removed; replaced by {@link #setFreezePane(int, int)}
	 */ 
	protected abstract void setFreezePane(int rowIndex, int colIndex, boolean isRowEdge, boolean isColumnEdge);
	
	protected abstract void setSheetName(String sheetName);//FIXMEXLS this is not needed anymore, or if it is, then how is xlsx working?
	
	protected abstract void setAutoFilter(String autoFilterRange);
	
	protected abstract void setRowLevels(XlsRowLevelInfo levelInfo, String level);

}
