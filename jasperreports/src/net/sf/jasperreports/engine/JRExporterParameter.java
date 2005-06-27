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
package net.sf.jasperreports.engine;


import net.sf.jasperreports.engine.export.JRExportProgressMonitor;

/**
 * TODO: intro.
 * <p>
 * The {@link JasperPrint} object needed for the export can be specified in many ways: an instance of <tt>JasperPrint</tt>,
 * an input stream, a file on disk, or an URL. The export engine will search for this object through parameters in the following
 * order: JASPER_PRINT_LIST, JASPER_PRINT, INPUT_STREAM, INPUT_URL, INPUT_FILE, INPUT_FILE_NAME.
 * <p>
 * The output type of the export process can also vary: a string buffer, an output stream / writer of a file on disk. The order of
 * parameters used by JasperReports when looking for the output depends on the final document format and is explained in detail
 * for each format (see documentation for the children of this class).
 * <p>
 * JasperReports allows users to export only a page range from the entire report or even a single page. The engine first
 * searches for the PAGE_INDEX parameter. If this is not present, it looks for the START_PAGE_INDEX and END_PAGE_INDEX
 * parameters. The engine will try to narrow the page range (which is initially the entire report) by using these two
 * parameters, if present.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExporterParameter
{


	/**
	 *
	 */
	private String name = null;


	/**
	 *
	 */
	protected JRExporterParameter(String name)
	{
		this.name = name;
	}


	/**
	 *
	 */
	public String toString()
	{
		return this.name;
	}


	/**
	 * The {@link JasperPrint} object that will be exported. If you already have a JasperPrint object, you can pass it
	 * to the export engine.
	 */
	public static final JRExporterParameter JASPER_PRINT = new JRExporterParameter("JasperPrint Object");


	/**
	 * A list of {@link JasperPrint} objects to be exported. If you need to concatenate several reports into the same
	 * document, you can use this feature.
	 */
	public static final JRExporterParameter JASPER_PRINT_LIST = new JRExporterParameter("JasperPrint List");


	/**
	 * The input stream that the exported {@link JasperPrint} object will be read from. If you want to read the JasperPrint
	 * object from an input stream (like a web location), you can pass the stream to this parameter.
	 */
	public static final JRExporterParameter INPUT_STREAM = new JRExporterParameter("InputStream Object");


	/**
	 * The URL that the {@link JasperPrint} object will be read from. If the JasperPrint object is available as a web
	 * resource, you can use this parameter, instead of opening a HTTP connection and read from the input stream.
	 */
	public static final JRExporterParameter INPUT_URL = new JRExporterParameter("URL Object");


	/**
	 * A <tt>java.io.File</tt> pointing to a file representing the serialized form of the {@link JasperPrint} object. This is
	 * useful if the JasperPrint object is representing a file on disk.
	 */
	public static final JRExporterParameter INPUT_FILE = new JRExporterParameter("Input File");


	/**
	 * A file representing the serialized form of the {@link JasperPrint} object. You can use this parameter to specify a file
	 * name where the object can be found.
	 */
	public static final JRExporterParameter INPUT_FILE_NAME = new JRExporterParameter("Input File Name");


	/**
	 * The string buffer to send the export output to. Useful for just storing the result in a string for later use.
	 */
	public static final JRExporterParameter OUTPUT_STRING_BUFFER = new JRExporterParameter("Output StringBuffer Object");


	/**
	 * The <tt>java.io.Writer</tt> instance that will be used to send the export output to. This is useful for sending
	 * the export result to a character stream, such as the <tt>PrintWriter</tt> of a servlet.
	 */
	public static final JRExporterParameter OUTPUT_WRITER = new JRExporterParameter("Output Writer Object");


	/**
	 * The <tt>java.io.OutputStream</tt> instance that will be used to send the export output to. This is useful for sending
	 * the export result to an output stream, such as a <tt>ServletOutputStream</tt>.
	 */
	public static final JRExporterParameter OUTPUT_STREAM = new JRExporterParameter("OutputStream Object");


	/**
	 * The <tt>java.io.File</tt> instance that will be used to specify the file name of the exported report. This is useful when
	 * exporting to a file and the <tt>File</tt> instance is already there.
	 */
	public static final JRExporterParameter OUTPUT_FILE = new JRExporterParameter("Output File");


	/**
	 * The file name of the exported report. This is an alternative to the OUTPUT_FILE parameter.
	 */
	public static final JRExporterParameter OUTPUT_FILE_NAME = new JRExporterParameter("Output File Name");


	/**
	 * An integer value representing the index of the page to be exported. This is useful when only one page of the entire
	 * report is needed for export.
	 */
	public static final JRExporterParameter PAGE_INDEX = new JRExporterParameter("Page Index");


	/**
	 * An integer value representing the start index of the page range to be exported. This is useful when only a range of
	 * pages is needed for export.
	 */
	public static final JRExporterParameter START_PAGE_INDEX = new JRExporterParameter("Start Page Index");


	/**
	 * An integer value representing the end index of the page range to be exported. This is useful when only a range of
	 * pages is needed for export.
	 */
	public static final JRExporterParameter END_PAGE_INDEX = new JRExporterParameter("End Page Index");


	/**
	 * The character encoding used for export.
	 */
	public static final JRExporterParameter CHARACTER_ENCODING = new JRExporterParameter("Character Encoding");


	/**
	 * A {@link JRExportProgressMonitor} instance for monitoring export status. This is useful for users who need to be
	 * notified after each page is exported (a GUI tool that shows a progress bar might need this feature).
	 */
	public static final JRExporterParameter PROGRESS_MONITOR = new JRExporterParameter("Progress Monitor");


	/**
	 * A parameter that allows users to move the entire content of each page horizontally. It is mostly useful for printing,
	 * when the report data does not fit inside the page margins.
	 */
	public static final JRExporterParameter OFFSET_X = new JRExporterParameter("Offset X");


	/**
	 * A parameter that allows users to move the entire content of each page vertically. It is mostly useful for printing,
	 * when the report data does not fit inside the page margins.
	 */
	public static final JRExporterParameter OFFSET_Y = new JRExporterParameter("Offset Y");


}
