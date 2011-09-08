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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Instances of this class are used for defining and setting exporter parameters.
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
	private String name;


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
	 * Property whose value is used as default for the {@link #CHARACTER_ENCODING CHARACTER_ENCODING} export parameter.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_CHARACTER_ENCODING = JRProperties.PROPERTY_PREFIX + "export.character.encoding";


	/**
	 * A {@link net.sf.jasperreports.engine.export.JRExportProgressMonitor JRExportProgressMonitor} instance for monitoring export status. This is useful for users who need to be
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


	/**
	 * @deprecated Replaced by {@link net.sf.jasperreports.engine.fonts.FontFamily#getExportFont(String) FontFamily.getExportFont(String)}.
	 */
	public static final JRExporterParameter FONT_MAP = new JRExporterParameter("Font Map");


	/**
	 * 
	 */
	public static final JRExporterParameter CLASS_LOADER = new JRExporterParameter("Class Loader");

	
	/**
	 * URL stream handler factory to be used while exporting the report.
	 * <p/>
	 * The values should be of type {@link java.net.URLStreamHandlerFactory java.net.URLStreamHandlerFactory}.
	 * 
	 * @see net.sf.jasperreports.engine.util.JRResourcesUtil#createURL(String, java.net.URLStreamHandlerFactory)
	 */
	public static final JRExporterParameter URL_HANDLER_FACTORY = new JRExporterParameter("URL Handler Factory");

	
	/**
	 * The {@link net.sf.jasperreports.engine.util.FileResolver FileResolver} instance to be used while exporting the report.
	 */
	public static final JRExporterParameter FILE_RESOLVER = new JRExporterParameter("File REsolver");

	
	/**
	 * A {@link net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory JRHyperlinkProducerFactory} which should be used for custom 
	 * hyperlink types during export.
	 */
	public static final JRExporterParameter HYPERLINK_PRODUCER_FACTORY = new JRExporterParameter("Hyperlink producer factory");

	
	/**
	 * Instance of the {@link net.sf.jasperreports.engine.export.ExporterFilter ExporterFilter} interface to be used by the exporter to filter the elements to be exported.
	 */
	public static final JRExporterParameter FILTER = new JRExporterParameter("Filter");


	/**
	 * A (per system) property that establishes the priority of export parameters against
	 * report hints.
	 * 
	 * If the property is true, export parameters override report hints; this is the
	 * default behavior.
	 * 
	 * This property cannot be used as a report export hint.
	 */
	public static final String PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS = 
		JRProperties.PROPERTY_PREFIX + "export.parameters.override.report.hints";
	
	/**
	 * Export parameter that can override the 
	 * {@link #PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS}
	 * property.
	 */
	public static final JRExporterParameter PARAMETERS_OVERRIDE_REPORT_HINTS = new JRExporterParameter("Parameters override hints flag");

	
	/**
	 * Indicates whether page margins should be ignored when the report is exported using a grid-based exporter
	 * <p>
	 * The type of the parameter is <code>java.lang.Boolean</code>.
	 * </p>
	 * <p>
	 * If set to <code>true</code>, any page in the document will be exported without taking into account its margins.
	 * </p>
	 * @see JRExporterParameter#PROPERTY_IGNORE_PAGE_MARGINS
	 */
	public static final JRExporterParameter IGNORE_PAGE_MARGINS = new JRExporterParameter("Ignore page margins");
	

	/**
	 * This property serves as default value for the {@link #IGNORE_PAGE_MARGINS IGNORE_PAGE_MARGINS}
	 * export parameter.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_IGNORE_PAGE_MARGINS = JRProperties.PROPERTY_PREFIX + "export.ignore.page.margins";

}
