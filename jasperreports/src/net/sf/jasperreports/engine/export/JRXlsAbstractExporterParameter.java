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

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;


/**
 * Contains parameters useful for export in XLS format.
 * <p>
 * The XLS exporter can send data to an output stream or file on disk. The engine looks among the export parameters in
 * order to find the selected output type in this order: OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @deprecated Replaced by {@link XlsExporterConfiguration}.
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
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
	 * @deprecated Replaced by {@link XlsReportConfiguration#isOnePagePerSheet()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_ONE_PAGE_PER_SHEET = new JRXlsAbstractExporterParameter("Is One Page per Sheet");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_ONE_PAGE_PER_SHEET}.
	 */
	public static final String PROPERTY_ONE_PAGE_PER_SHEET = XlsReportConfiguration.PROPERTY_ONE_PAGE_PER_SHEET;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isRemoveEmptySpaceBetweenRows()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = new JRXlsAbstractExporterParameter("Is Remove Empty Space Between Rows");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS}.
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isRemoveEmptySpaceBetweenColumns()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS = new JRXlsAbstractExporterParameter("Is Remove Empty Space Between Columns");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS}.
	 */
	public static final String PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS = XlsReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isWhitePageBackground()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_WHITE_PAGE_BACKGROUND = new JRXlsAbstractExporterParameter("Is White Page Background");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_WHITE_PAGE_BACKGROUND}.
	 */
	public static final String PROPERTY_WHITE_PAGE_BACKGROUND = XlsReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isDetectCellType()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_DETECT_CELL_TYPE = new JRXlsAbstractExporterParameter("Is Detect Cell Type");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_DETECT_CELL_TYPE}.
	 */
	public static final String PROPERTY_DETECT_CELL_TYPE = XlsReportConfiguration.PROPERTY_DETECT_CELL_TYPE;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#getSheetNames()}.
	 */
	public static final JRXlsAbstractExporterParameter SHEET_NAMES = new JRXlsAbstractExporterParameter("Sheet Names");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_SHEET_NAMES_PREFIX}.
	 */
	public static final String PROPERTY_SHEET_NAMES_PREFIX = XlsReportConfiguration.PROPERTY_SHEET_NAMES_PREFIX;


	/**
	 * @deprecated Replaced by {@link JRXlsAbstractExporter#PROPERTY_SHEET_NAME}.
	 */
	public static final String PROPERTY_SHEET_NAME = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.sheet.name";


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isFontSizeFixEnabled()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_FONT_SIZE_FIX_ENABLED = new JRXlsAbstractExporterParameter("Is Font Size Fix Enabled");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isImageBorderFixEnabled()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_IMAGE_BORDER_FIX_ENABLED = new JRXlsAbstractExporterParameter("Is Image Border Fix Enabled");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_FONT_SIZE_FIX_ENABLED}.
	 */
	public static final String PROPERTY_FONT_SIZE_FIX_ENABLED = XlsReportConfiguration.PROPERTY_FONT_SIZE_FIX_ENABLED;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_IMAGE_BORDER_FIX_ENABLED}.
	 */
	public static final String PROPERTY_IMAGE_BORDER_FIX_ENABLED = XlsReportConfiguration.PROPERTY_IMAGE_BORDER_FIX_ENABLED;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#getFormatPatternsMap()}.
	 */
	public static final JRExporterParameter FORMAT_PATTERNS_MAP = new JRXlsExporterParameter("Format Patterns Map");

	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#getMaxRowsPerSheet()}.
	 */
	public static final JRExporterParameter MAXIMUM_ROWS_PER_SHEET = new JRXlsExporterParameter("Maximum Rows Per Sheet");

	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_MAXIMUM_ROWS_PER_SHEET}.
	 */
	public static final String PROPERTY_MAXIMUM_ROWS_PER_SHEET = XlsReportConfiguration.PROPERTY_MAXIMUM_ROWS_PER_SHEET;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isIgnoreGraphics()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_IGNORE_GRAPHICS = new JRXlsAbstractExporterParameter("Is Ignore Graphics");

	
	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_IGNORE_GRAPHICS}.
	 */
	public static final String PROPERTY_IGNORE_GRAPHICS = XlsReportConfiguration.PROPERTY_IGNORE_GRAPHICS;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isCollapseRowSpan()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_COLLAPSE_ROW_SPAN = new JRXlsAbstractExporterParameter("Is Collapse Row Span");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_COLLAPSE_ROW_SPAN}.
	 */
	public static final String PROPERTY_COLLAPSE_ROW_SPAN = XlsReportConfiguration.PROPERTY_COLLAPSE_ROW_SPAN;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isIgnoreCellBorder()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_IGNORE_CELL_BORDER = new JRXlsAbstractExporterParameter("Is Ignore Cell Border");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_IGNORE_CELL_BORDER}.
	 */
	public static final String PROPERTY_IGNORE_CELL_BORDER = XlsReportConfiguration.PROPERTY_IGNORE_CELL_BORDER;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#isIgnoreCellBackground()}.
	 */
	public static final JRXlsAbstractExporterParameter IS_IGNORE_CELL_BACKGROUND = new JRXlsAbstractExporterParameter("Is Ignore Cell Background");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_IGNORE_CELL_BACKGROUND}.
	 */
	public static final String PROPERTY_IGNORE_CELL_BACKGROUND = XlsReportConfiguration.PROPERTY_IGNORE_CELL_BACKGROUND;


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#getPassword()}. 
	 */
	public static final JRXlsAbstractExporterParameter PASSWORD = new JRXlsAbstractExporterParameter("Password");


	/**
	 * @deprecated Replaced by {@link XlsReportConfiguration#PROPERTY_PASSWORD}.
	 */
	public static final String PROPERTY_PASSWORD = XlsReportConfiguration.PROPERTY_PASSWORD;

	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#isCreateCustomPalette()}.
	 */
	public static final JRExporterParameter CREATE_CUSTOM_PALETTE = new JExcelApiExporterParameter("Create Custom Palette");

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_CREATE_CUSTOM_PALETTE}.
	 */
	public static final String PROPERTY_CREATE_CUSTOM_PALETTE = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.create.custom.palette";


}
