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
 * Contains parameters useful for export in plain text format.
 * <p>
 * The text exporter can send data to a string buffer, output stream, character stream or file on disk. The engine looks
 * among the export parameters in order to find the selected output type in this order: OUTPUT_STRING_BUFFER, OUTPUT_WRITER,
 * OUTPUT_STREAM, OUTPUT_FILE, OUTPUT_FILE_NAME.
 *
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRTextExporterParameter extends JRExporterParameter
{
	/**
	 *
	 */
	public JRTextExporterParameter(String name)
	{
		super(name);
	}


	/**
	 * An integer representing the pixel/character horizontal ratio.
	 */
	public static final JRTextExporterParameter CHARACTER_WIDTH = new JRTextExporterParameter("Character Width");


	/**
	 * An integer representing the pixel/character horizontal ratio.
	 */
	public static final JRTextExporterParameter CHARACTER_HEIGHT = new JRTextExporterParameter("Character Height");


	/**
	 * An integer representing the page width in characters.
	 */
	public static final JRTextExporterParameter PAGE_WIDTH = new JRTextExporterParameter("Page Width");


	/**
	 * An integer representing the page height in characters.
	 */
	public static final JRTextExporterParameter PAGE_HEIGHT = new JRTextExporterParameter("Page Height");


	/**
	 * A string representing text that will be inserted between pages of the generated report. By default, JasperReports
	 * separates pages by two empty lines, but this behaviour can be overriden by this parameter.
	 */
	public static final JRTextExporterParameter BETWEEN_PAGES_TEXT = new JRTextExporterParameter("Between Pages Text");


	/**
	 * A string representing the separator between two lines of text. This parameter is useful since line separators can
	 * vary from one operating system to another. The default value is "\n".
	 */
	public static final JRTextExporterParameter LINE_SEPARATOR = new JRTextExporterParameter("Line Separator");

}
