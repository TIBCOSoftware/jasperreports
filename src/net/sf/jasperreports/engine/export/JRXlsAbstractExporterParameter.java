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

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Contains parameters useful for export in XLS format.
 * <p>
 * The XLS exporter can send data to an output stream or file on disk. The engine looks among the export parameters in
 * order to find the selected output type in this order: OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsAbstractExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRXlsAbstractExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * A boolean value specifying whether each report page should be written in a different XLS sheet
	 */
	public static final JRXlsAbstractExporterParameter IS_ONE_PAGE_PER_SHEET = new JRXlsAbstractExporterParameter("Is One Page per Sheet");


	/**
	 * Property whose value is used as default state of the {@link #IS_ONE_PAGE_PER_SHEET IS_ONE_PAGE_PER_SHEET} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_ONE_PAGE_PER_SHEET = JRProperties.PROPERTY_PREFIX + "export.xls.one.page.per.sheet";


	/**
	 * A boolean value specifying whether the empty spaces that could appear between rows should be removed or not.
	 */
	public static final JRXlsAbstractExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = new JRXlsAbstractExporterParameter("Is Remove Empty Space Between Rows");


	/**
	 * Property whose value is used as default state of the {@link #IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = JRProperties.PROPERTY_PREFIX + "export.xls.remove.empty.space.between.rows";


	/**
	 * A boolean value specifying whether the empty spaces that could appear between columns should be removed or not.
	 */
	public static final JRXlsAbstractExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS = new JRXlsAbstractExporterParameter("Is Remove Empty Space Between Columns");


	/**
	 * Property whose value is used as default state of the {@link #IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS = JRProperties.PROPERTY_PREFIX + "export.xls.remove.empty.space.between.columns";


	/**
	 * A boolean value specifying whether the page background should be white or the default XLS background color. This background
	 * may vary depending on the XLS viewer properties or the operating system color scheme.
	 */
	public static final JRXlsAbstractExporterParameter IS_WHITE_PAGE_BACKGROUND = new JRXlsAbstractExporterParameter("Is White Page Background");


	/**
	 * Property whose value is used as default state of the {@link #IS_WHITE_PAGE_BACKGROUND IS_WHITE_PAGE_BACKGROUND} export flag.
	 * <p/>
	 * This property is set by default (<code>true</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_WHITE_PAGE_BACKGROUND = JRProperties.PROPERTY_PREFIX + "export.xls.white.page.background";


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
	 */
	public static final JRXlsAbstractExporterParameter IS_DETECT_CELL_TYPE = new JRXlsAbstractExporterParameter("Is Detect Cell Type");


	/**
	 * Property whose value is used as default state of the {@link #IS_DETECT_CELL_TYPE IS_DETECT_CELL_TYPE} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_DETECT_CELL_TYPE = JRProperties.PROPERTY_PREFIX + "export.xls.detect.cell.type";


	/**
	 * An array of strings representing custom sheet names. This is useful when used with the <i>IS_ONE_PAGE_PER_SHEET</i>
	 * parameter.
	 */
	public static final JRXlsAbstractExporterParameter SHEET_NAMES = new JRXlsAbstractExporterParameter("Sheet Names");


	/**
	 * Property prefix used in order to identify a set of properties holding sheet names, very useful when 
	 * indicating sheet names in the jrxml template is required. These properties' values are collected in an 
	 * ordered list, therefore it is important the order they are written in. If set, these values are considered 
	 * as defaults for the {@link #SHEET_NAMES} export parameter.
	 * <p/>
	 * A property starting with this prefix can hold one or many sheet names. In order to be well parsed, 
	 * sheet names contained in such a property's value should be separated by a slash character ("/")
	 * <p/>
	 * These properties are by default not set.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_NAMES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.names.";


	/**
	 * Property used to set the name of the sheet containing a given element. Its value overrides the report-level settings for the sheet name. 
	 * If several elements in the sheet contain this property, the engine will consider the value of the last exported element's property.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_NAME = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.name";


	/**
	 * Flag for decreasing font size so that texts fit into the specified cell height.
	 */
	public static final JRXlsAbstractExporterParameter IS_FONT_SIZE_FIX_ENABLED = new JRXlsAbstractExporterParameter("Is Font Size Fix Enabled");


	/**
	 * Flag for forcing the minimum image padding to 1 pixel, to avoid situations where the image hides the cell border.
	 */
	public static final JRXlsAbstractExporterParameter IS_IMAGE_BORDER_FIX_ENABLED = new JRXlsAbstractExporterParameter("Is Image Border Fix Enabled");


	/**
	 * Property whose value is used as default state of the {@link #IS_FONT_SIZE_FIX_ENABLED IS_FONT_SIZE_FIX_ENABLED} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_FONT_SIZE_FIX_ENABLED = JRProperties.PROPERTY_PREFIX + "export.xls.font.size.fix.enabled";


	/**
	 * Property whose value is used as default state of the {@link #IS_IMAGE_BORDER_FIX_ENABLED IS_IMAGE_BORDER_FIX_ENABLED} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_IMAGE_BORDER_FIX_ENABLED = JRProperties.PROPERTY_PREFIX + "export.xls.image.border.fix.enabled";


	/**
	 * This export parameter should be used when converting java format patterns to equivalent proprietary
	 * format patterns. It should be constructed as a Map containing java format patterns as keys and the
	 * correspondent proprietary format pattern as correspondent value
	 * <p/>
	 * @see org.apache.poi.hssf.usermodel.HSSFDataFormat
	 * @see jxl.write.NumberFormat
	 */
	public static final JRExporterParameter FORMAT_PATTERNS_MAP = new JRXlsExporterParameter("Format Patterns Map");

	
	/**
	 * An integer value specifying the maximum number of rows allowed to be shown in a sheet.
	 * When set, a new sheet is created for the remaining rows to be displayed. Negative values or zero means that no limit has been set.
	 */
	public static final JRExporterParameter MAXIMUM_ROWS_PER_SHEET = new JRXlsExporterParameter("Maximum Rows Per Sheet");

	
	/**
	 * Property whose value is used as default of the {@link #MAXIMUM_ROWS_PER_SHEET MAXIMUM_ROWS_PER_SHEET} export parameter.
	 * <p/>
	 * This property is by default to zero.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_MAXIMUM_ROWS_PER_SHEET = JRProperties.PROPERTY_PREFIX + "export.xls.max.rows.per.sheet";


	/**
	 * Flag for ignoring graphic elements and exporting text elements only.
	 */
	public static final JRXlsAbstractExporterParameter IS_IGNORE_GRAPHICS = new JRXlsAbstractExporterParameter("Is Ignore Graphics");

	
	/**
	 * Property whose value is used as default state of the {@link #IS_IGNORE_GRAPHICS IS_IGNORE_GRAPHICS} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_IGNORE_GRAPHICS = JRProperties.PROPERTY_PREFIX + "export.xls.ignore.graphics";


	/**
	 * Flag for collapsing row span and avoid merging cells across rows.
	 */
	public static final JRXlsAbstractExporterParameter IS_COLLAPSE_ROW_SPAN = new JRXlsAbstractExporterParameter("Is Collapse Row Span");


	/**
	 * Property whose value is used as default state of the {@link #IS_COLLAPSE_ROW_SPAN IS_COLLAPSE_ROW_SPAN} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_COLLAPSE_ROW_SPAN = JRProperties.PROPERTY_PREFIX + "export.xls.collapse.row.span";


	/**
	 * Flag for ignoring the cell border.
	 */
	public static final JRXlsAbstractExporterParameter IS_IGNORE_CELL_BORDER = new JRXlsAbstractExporterParameter("Is Ignore Cell Border");


	/**
	 * Property whose value is used as default state of the {@link #IS_IGNORE_CELL_BORDER IS_IGNORE_CELL_BORDER} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_IGNORE_CELL_BORDER = JRProperties.PROPERTY_PREFIX + "export.xls.ignore.cell.border";


	/**
	 * Flag for ignoring the cell background color.
	 */
	public static final JRXlsAbstractExporterParameter IS_IGNORE_CELL_BACKGROUND = new JRXlsAbstractExporterParameter("Is Ignore Cell Background");


	/**
	 * Property whose value is used as default state of the {@link #IS_IGNORE_CELL_BACKGROUND IS_IGNORE_CELL_BACKGROUND} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_IGNORE_CELL_BACKGROUND = JRProperties.PROPERTY_PREFIX + "export.xls.ignore.cell.background";


	/**
	 * A String value representing the password in case of password protected documents 
	 */
	public static final JRXlsAbstractExporterParameter PASSWORD = new JRXlsAbstractExporterParameter("Password");


	/**
	 * Property whose value is used as default value of the {@link #PASSWORD PASSWORD} export parameter.
	 * <p/>
	 * This property is by default not set (<code>null</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_PASSWORD = JRProperties.PROPERTY_PREFIX + "export.xls.password";

	
	/**
	 * A boolean value specifying whether the standard color palette should be customized
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
	public static final JRExporterParameter CREATE_CUSTOM_PALETTE = new JExcelApiExporterParameter("Create Custom Palette");

	/**
	 * Property whose value is used as default state of the {@link #CREATE_CUSTOM_PALETTE CREATE_CUSTOM_PALETTE} export flag.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_CREATE_CUSTOM_PALETTE = JRProperties.PROPERTY_PREFIX + "export.xls.create.custom.palette";


}
