/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsExporterParameter extends JRExporterParameter
{


	/**
	 *
	 */
	protected JRXlsExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * A boolean value specifying whether each report page should be written in a different XLS sheet
	 */
	public static final JRXlsExporterParameter IS_ONE_PAGE_PER_SHEET = new JRXlsExporterParameter("Is One Page per Sheet");


	/**
	 * A boolean value specifying whether the empty spaces that could appear between rows should be removed or not.
	 */
	public static final JRXlsExporterParameter IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS = new JRXlsExporterParameter("Is Remove Empty Space Between Rows");


	/**
	 * A boolean value specifying whether the page background should be white or the default XLS background color. This background
	 * may vary depending on the XLS viewer properties or the operating system color scheme.
	 */
	public static final JRXlsExporterParameter IS_WHITE_PAGE_BACKGROUND = new JRXlsExporterParameter("Is White Page Background");
	public static final JRXlsExporterParameter IS_AUTO_DETECT_CELL_TYPE = new JRXlsExporterParameter("Is Auto Detect Cell Type");


}
