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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * Fa�ade class for exporting generated reports into more popular
 * formats such as PDF, HTML and XML.
 * This class contains convenience methods for exporting to only these 3 formats.
 * <p> 
 * For exporting to XLS and CSV format or for using special exporter parameters, 
 * the specific exporter class should be used directly.  
 * 
 * @see net.sf.jasperreports.engine.JasperPrint
 * @see net.sf.jasperreports.engine.export.JRHtmlExporter
 * @see net.sf.jasperreports.engine.export.JRPdfExporter
 * @see net.sf.jasperreports.engine.export.JRXmlExporter
 * @see net.sf.jasperreports.engine.export.JRXlsExporter
 * @see net.sf.jasperreports.engine.export.JRCsvExporter
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class JasperExportManager
{


	/**
	 * Exports the generated report file specified by the parameter into PDF format.
	 * The resulting PDF file has the same name as the report object inside the source file,
	 * plus the <code>*.pdf</code> extension and it is located in the same directory as the source file.
	 *  
	 * @param sourceFileName source file containing the generated report
	 * @return resulting PDF file name
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static String exportReportToPdfFile(String sourceFileName) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/* We need the report name. */
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pdf");
		String destFileName = destFile.toString();
		
		exportReportToPdfFile(jasperPrint, destFileName);
		
		return destFileName;
	}


	/**
	 * Exports the generated report file specified by the first parameter into PDF format,
	 * the result being placed in the second file parameter.
	 *  
	 * @param sourceFileName source file containing the generated report
	 * @param destFileName   file name to place the PDF content into
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToPdfFile(
		String sourceFileName, 
		String destFileName
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObjectFromFile(sourceFileName);

		exportReportToPdfFile(jasperPrint, destFileName);
	}

	
	/**
	 * Exports the generated report file specified by the first parameter into PDF format,
	 * the result being placed in the second file parameter.
	 *
	 * @param jasperPrint  report object to export 
	 * @param destFileName file name to place the PDF content into
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToPdfFile(
		JasperPrint jasperPrint, 
		String destFileName
		) throws JRException
	{
		/*   */
		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFileName);
		
		exporter.exportReport();
	}


	/**
	 * Exports the generated report read from the supplied input stream into PDF format and
	 * writes the results to the output stream specified by the second parameter.
	 *
	 * @param inputStream  input stream to read the generated report object from
	 * @param outputStream output stream to write the resulting PDF content to
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToPdfStream(
		InputStream inputStream, 
		OutputStream outputStream
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(inputStream);

		exportReportToPdfStream(jasperPrint, outputStream);
	}

	
	/**
	 * Exports the generated report object received as first parameter into PDF format and
	 * writes the results to the output stream specified by the second parameter.
	 * 
	 * @param jasperPrint  report object to export 
	 * @param outputStream output stream to write the resulting PDF content to
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToPdfStream(
		JasperPrint jasperPrint, 
		OutputStream outputStream
		) throws JRException
	{
		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		
		exporter.exportReport();
	}


	/**
	 * Exports the generated report object received as parameter into PDF format and
	 * returns the binary content as a byte array.
	 * 
	 * @param jasperPrint report object to export 
	 * @return byte array representing the resulting PDF content 
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static byte[] exportReportToPdf(JasperPrint jasperPrint) throws JRException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		
		exporter.exportReport();
		
		return baos.toByteArray();
	}

	
	/**
	 * Exports the generated report file specified by the parameter into XML format.
	 * The resulting XML file has the same name as the report object inside the source file,
	 * plus the <code>*.jrpxml</code> extension and it is located in the same directory as the source file.
	 * <p>
	 * When exporting to XML format, the images can be either embedded in the XML content
	 * itself using the Base64 encoder or be referenced as external resources.
	 * If not embedded, the images are placed as distinct files inside a directory
	 * having the same name as the XML destination file, plus the "_files" suffix. 
	 * 
	 * @param sourceFileName    source file containing the generated report
	 * @param isEmbeddingImages flag that indicates whether the images should be embedded in the
	 *                          XML content itself using the Base64 encoder or be referenced as external resources
	 * @return XML representation of the generated report
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static String exportReportToXmlFile(
		String sourceFileName, 
		boolean isEmbeddingImages
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/* We need the report name. */
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jrpxml");
		String destFileName = destFile.toString();
		
		exportReportToXmlFile(
			jasperPrint, 
			destFileName,
			isEmbeddingImages
			);
		
		return destFileName;
	}


	/**
	 * Exports the generated report file specified by the first parameter into XML format,
	 * placing the result into the second file parameter.
	 * <p>
	 * If not embedded into the XML content itself using the Base64 encoder, 
	 * the images are placed as distinct files inside a directory having the same name 
	 * as the XML destination file, plus the "_files" suffix. 
	 * 
	 * @param sourceFileName    source file containing the generated report
	 * @param destFileName      file name to place the XML representation into
	 * @param isEmbeddingImages flag that indicates whether the images should be embedded in the
	 *                          XML content itself using the Base64 encoder or be referenced as external resources
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToXmlFile(
		String sourceFileName, 
		String destFileName,
		boolean isEmbeddingImages
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObjectFromFile(sourceFileName);

		exportReportToXmlFile(
			jasperPrint, 
			destFileName,
			isEmbeddingImages
			);
	}

	
	/**
	 * Exports the generated report object received as parameter into XML format,
	 * placing the result into the second file parameter.
	 * <p>
	 * If not embedded into the XML content itself using the Base64 encoder, 
	 * the images are placed as distinct files inside a directory having the same name 
	 * as the XML destination file, plus the "_files" suffix. 
	 * 
	 * @param jasperPrint       report object to export
	 * @param destFileName      file name to place the XML representation into
	 * @param isEmbeddingImages flag that indicates whether the images should be embedded in the
	 *                          XML content itself using the Base64 encoder or be referenced as external resources
	 *  
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToXmlFile(
		JasperPrint jasperPrint, 
		String destFileName,
		boolean isEmbeddingImages
		) throws JRException
	{
		JRXmlExporter exporter = new JRXmlExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFileName);
		exporter.setParameter(JRXmlExporterParameter.IS_EMBEDDING_IMAGES,
			isEmbeddingImages ? Boolean.TRUE : Boolean.FALSE);
		
		exporter.exportReport();
	}


	/**
	 * Exports the generated report object read from the supplied input stream into XML format,
	 * and writes the result to the output stream specified by the second parameter.
	 * The images are embedded into the XML content itself using the Base64 encoder. 
	 * 
	 * @param inputStream  input stream to read the generated report object from
	 * @param outputStream output stream to write the resulting XML representation to
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToXmlStream(
		InputStream inputStream, 
		OutputStream outputStream
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(inputStream);

		exportReportToXmlStream(jasperPrint, outputStream);
	}

	
	/**
	 * Exports the generated report object supplied as the first parameter into XML format,
	 * and writes the result to the output stream specified by the second parameter.
	 * The images are embedded into the XML content itself using the Base64 encoder. 
	 * 
	 * @param jasperPrint  report object to export
	 * @param outputStream output stream to write the resulting XML representation to
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToXmlStream(
		JasperPrint jasperPrint, 
		OutputStream outputStream
		) throws JRException
	{
		JRXmlExporter exporter = new JRXmlExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		
		exporter.exportReport();
	}


	/**
	 * Exports the generated report object supplied as parameter into XML format
	 * and returs the result as String.
	 * The images are embedded into the XML content itself using the Base64 encoder. 
	 * 
	 * @param jasperPrint report object to export
	 * @return XML representation of the generated report
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static String exportReportToXml(JasperPrint jasperPrint) throws JRException
	{
		StringBuffer sbuffer = new StringBuffer();

		JRXmlExporter exporter = new JRXmlExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
		
		exporter.exportReport();
		
		return sbuffer.toString();
	}


	/**
	 * Exports the generated report file specified by the parameter into HTML format.
	 * The resulting HTML file has the same name as the report object inside the source file,
	 * plus the <code>*.html</code> extension and it is located in the same directory as the source file.
	 * The images are placed as distinct files inside a directory having the same name 
	 * as the HTML destination file, plus the "_files" suffix. 
	 * 
	 * @param sourceFileName source file containing the generated report
	 * @return resulting HTML file name
	 * @see net.sf.jasperreports.engine.export.JRHtmlExporter
	 */
	public static String exportReportToHtmlFile(
		String sourceFileName
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/* We need the report name. */
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".html");
		String destFileName = destFile.toString();
		
		exportReportToHtmlFile(
			jasperPrint, 
			destFileName
			);
		
		return destFileName;
	}


	/**
	 * Exports the generated report file specified by the first parameter into HTML format,
	 * placing the result into the second file parameter.
	 * <p>
	 * The images are placed as distinct files inside a directory having the same name 
	 * as the HTML destination file, plus the "_files" suffix. 
	 * 
	 * @param sourceFileName source file containing the generated report
	 * @param destFileName   file name to place the HTML content into
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToHtmlFile(
		String sourceFileName, 
		String destFileName
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObjectFromFile(sourceFileName);

		exportReportToHtmlFile(
			jasperPrint, 
			destFileName
			);
	}

	
	/**
	 * Exports the generated report object received as parameter into HTML format,
	 * placing the result into the second file parameter.
	 * <p>
	 * The images are placed as distinct files inside a directory having the same name 
	 * as the HTML destination file, plus the "_files" suffix. 
	 * 
	 * @param jasperPrint  report object to export
	 * @param destFileName file name to place the HTML content into
	 * @see net.sf.jasperreports.engine.export.JRPdfExporter
	 */
	public static void exportReportToHtmlFile(
		JasperPrint jasperPrint, 
		String destFileName
		) throws JRException
	{
		JRHtmlExporter exporter = new JRHtmlExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFileName);
		
		exporter.exportReport();
	}


	private JasperExportManager()
	{
	}
}
