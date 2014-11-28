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
package net.sf.jasperreports.engine;

import net.sf.jasperreports.export.CommonExportConfiguration;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.WriterExporterOutput;
import net.sf.jasperreports.export.XlsReportConfiguration;


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
 * @deprecated Replaced by {@link ExporterInput}, {@link ExporterConfiguration} and {@link ExporterOutput}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	 * @deprecated Replaced by {@link SimpleExporterInput#SimpleExporterInput(JasperPrint)}.
	 */
	public static final JRExporterParameter JASPER_PRINT = new JRExporterParameter("JasperPrint Object");


	/**
	 * @deprecated Replaced by {@link SimpleExporterInput#getInstance(java.util.List)}.
	 */
	public static final JRExporterParameter JASPER_PRINT_LIST = new JRExporterParameter("JasperPrint List");


	/**
	 * @deprecated Replaced by {@link SimpleExporterInput#SimpleExporterInput(java.io.InputStream)}.
	 */
	public static final JRExporterParameter INPUT_STREAM = new JRExporterParameter("InputStream Object");


	/**
	 * @deprecated Replaced by {@link SimpleExporterInput#SimpleExporterInput(java.net.URL)}.
	 */
	public static final JRExporterParameter INPUT_URL = new JRExporterParameter("URL Object");


	/**
	 * @deprecated Replaced by {@link SimpleExporterInput#SimpleExporterInput(java.io.File)}.
	 */
	public static final JRExporterParameter INPUT_FILE = new JRExporterParameter("Input File");


	/**
	 * @deprecated Replaced by {@link SimpleExporterInput#SimpleExporterInput(String)}.
	 */
	public static final JRExporterParameter INPUT_FILE_NAME = new JRExporterParameter("Input File Name");


	/**
	 * @deprecated Replaced by {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(StringBuffer)}.
	 */
	public static final JRExporterParameter OUTPUT_STRING_BUFFER = new JRExporterParameter("Output StringBuffer Object");


	/**
	 * @deprecated Replaced by {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(java.io.Writer)}.
	 */
	public static final JRExporterParameter OUTPUT_WRITER = new JRExporterParameter("Output Writer Object");


	/**
	 * @deprecated Replaced by {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(java.io.OutputStream)} 
	 * and {@link SimpleOutputStreamExporterOutput#SimpleOutputStreamExporterOutput(java.io.OutputStream)}.
	 */
	public static final JRExporterParameter OUTPUT_STREAM = new JRExporterParameter("OutputStream Object");


	/**
	 * @deprecated Replaced by {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(java.io.File)}
	 * and {@link SimpleOutputStreamExporterOutput#SimpleOutputStreamExporterOutput(java.io.File)}.
	 */
	public static final JRExporterParameter OUTPUT_FILE = new JRExporterParameter("Output File");


	/**
	 * @deprecated Replaced by {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(String)}
	 * and {@link SimpleOutputStreamExporterOutput#SimpleOutputStreamExporterOutput(String)}.
	 */
	public static final JRExporterParameter OUTPUT_FILE_NAME = new JRExporterParameter("Output File Name");


	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#getPageIndex()}.
	 */
	public static final JRExporterParameter PAGE_INDEX = new JRExporterParameter("Page Index");


	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#getStartPageIndex()}.
	 */
	public static final JRExporterParameter START_PAGE_INDEX = new JRExporterParameter("Start Page Index");


	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#getEndPageIndex()}.
	 */
	public static final JRExporterParameter END_PAGE_INDEX = new JRExporterParameter("End Page Index");


	/**
	 * @deprecated Replaced by {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(java.io.File, String)},
	 * {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(String, String)} 
	 * and {@link SimpleWriterExporterOutput#SimpleWriterExporterOutput(java.io.OutputStream, String)}.
	 */
	public static final JRExporterParameter CHARACTER_ENCODING = new JRExporterParameter("Character Encoding");


	/**
	 * @deprecated Replaced by {@link WriterExporterOutput#PROPERTY_CHARACTER_ENCODING}.
	 */
	public static final String PROPERTY_CHARACTER_ENCODING = WriterExporterOutput.PROPERTY_CHARACTER_ENCODING;


	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#getProgressMonitor()}.
	 */
	public static final JRExporterParameter PROGRESS_MONITOR = new JRExporterParameter("Progress Monitor");


	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#getOffsetX()}.
	 */
	public static final JRExporterParameter OFFSET_X = new JRExporterParameter("Offset X");


	/**
	 * @deprecated Replace by {@link ReportExportConfiguration#getOffsetY()}.
	 */
	public static final JRExporterParameter OFFSET_Y = new JRExporterParameter("Offset Y");


	/**
	 * @deprecated Replaced by {@link JasperReportsContext}.
	 */
	public static final JRExporterParameter CLASS_LOADER = new JRExporterParameter("Class Loader");

	
	/**
	 * URL stream handler factory to be used while exporting the report.
	 * <p/>
	 * The values should be of type {@link java.net.URLStreamHandlerFactory java.net.URLStreamHandlerFactory}.
	 * 
	 * @see net.sf.jasperreports.engine.util.JRResourcesUtil#createURL(String, java.net.URLStreamHandlerFactory)
	 * @deprecated Replaced by {@link JasperReportsContext}.
	 */
	public static final JRExporterParameter URL_HANDLER_FACTORY = new JRExporterParameter("URL Handler Factory");

	
	/**
	 * The {@link net.sf.jasperreports.engine.util.FileResolver FileResolver} instance to be used while exporting the report.
	 * @deprecated Replaced by {@link JasperReportsContext}.
	 */
	public static final JRExporterParameter FILE_RESOLVER = new JRExporterParameter("File REsolver");

	
	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#getHyperlinkProducerFactory()}.
	 */
	public static final JRExporterParameter HYPERLINK_PRODUCER_FACTORY = new JRExporterParameter("Hyperlink producer factory");

	
	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#getExporterFilter()}.
	 */
	public static final JRExporterParameter FILTER = new JRExporterParameter("Filter");


	/**
	 * A global (per context) property that establishes the priority of export parameters against
	 * report hints.
	 * 
	 * If the property is true, export parameters override report hints; this is the
	 * default behavior.
	 * 
	 * This property cannot be used as a report export hint.
	 * @deprecated Replaced by {@link CommonExportConfiguration#PROPERTY_EXPORT_CONFIGURATION_OVERRIDE_REPORT_HINTS}.
	 */
	public static final String PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS = 
		JRPropertiesUtil.PROPERTY_PREFIX + "export.parameters.override.report.hints";
	
	/**
	 * Export parameter that can override the 
	 * {@link #PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS}
	 * property.
	 * @deprecated To be removed.
	 */
	public static final JRExporterParameter PARAMETERS_OVERRIDE_REPORT_HINTS = new JRExporterParameter("Parameters override hints flag");

	
	/**
	 * @deprecated Replaced by {@link HtmlReportConfiguration#isIgnorePageMargins()}
	 * and {@link XlsReportConfiguration#isIgnorePageMargins()}.
	 */
	public static final JRExporterParameter IGNORE_PAGE_MARGINS = new JRExporterParameter("Ignore page margins");
	

	/**
	 * @deprecated Replaced by {@link ReportExportConfiguration#PROPERTY_IGNORE_PAGE_MARGINS}.
	 */
	public static final String PROPERTY_IGNORE_PAGE_MARGINS = ReportExportConfiguration.PROPERTY_IGNORE_PAGE_MARGINS;

}
