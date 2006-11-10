/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRExporterParameter;


/**
 * Contains parameters useful for export in XLS format.
 * <p>
 * The XLS exporter can send data to an output stream or file on disk. The engine looks among the export parameters in
 * order to find the selected output type in this order: OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @author Sanda Zaharia (szaharia@users.sourceforge.net)
 * @version $Id:$
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
	 * A boolean value specifying whether the empty spaces that could appear between rows should be removed or not.
	 */
	public static final JRXlsAbstractExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = new JRXlsAbstractExporterParameter("Is Remove Empty Space Between Rows");


	/**
	 * A boolean value specifying whether the page background should be white or the default XLS background color. This background
	 * may vary depending on the XLS viewer properties or the operating system color scheme.
	 */
	public static final JRXlsAbstractExporterParameter IS_WHITE_PAGE_BACKGROUND = new JRXlsAbstractExporterParameter("Is White Page Background");


	/**
	 * A boolean value specifying whether the exporter should try to automatically detect cell types based on the cell value.
	 * 
	 * @deprecated The {@link #IS_DETECT_CELL_TYPE IS_DETECT_CELL_TYPE} offers more consistent numerical cell detection.
	 */
	public static final JRXlsAbstractExporterParameter IS_AUTO_DETECT_CELL_TYPE = new JRXlsAbstractExporterParameter("Is Auto Detect Cell Type");
	
	
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
	 * An array of strings representing custom sheet names. This is useful when used with the <i>IS_ONE_PAGE_PER_SHEET</i>
	 * parameter. 
	 */
	public static final JRXlsAbstractExporterParameter SHEET_NAMES = new JRXlsAbstractExporterParameter("Sheet Names");


	/**
	 * Flag for decreasing font size so that texts fit into the specified cell height.
	 */
	public static final JRXlsAbstractExporterParameter IS_FONT_SIZE_FIX_ENABLED = new JRXlsAbstractExporterParameter("Is Font Size Fix Enabled");
	
}
