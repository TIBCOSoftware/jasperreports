/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package dori.jasper.engine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import dori.jasper.engine.export.JRHtmlExporter;
import dori.jasper.engine.export.JRPdfExporter;
import dori.jasper.engine.export.JRXmlExporter;
import dori.jasper.engine.export.JRXmlExporterParameter;
import dori.jasper.engine.util.JRLoader;


/**
 * Façade class for the JasperReports engine. 
 */
public class JasperExportManager
{


	/**
	 *
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
	 *
	 */
	public static void exportReportToPdfFile(
		String sourceFileName, 
		String destFileName
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFileName);

		exportReportToPdfFile(jasperPrint, destFileName);
	}

	
	/**
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
	 */
	public static void exportReportToXmlFile(
		String sourceFileName, 
		String destFileName,
		boolean isEmbeddingImages
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFileName);

		exportReportToXmlFile(
			jasperPrint, 
			destFileName,
			isEmbeddingImages
			);
	}

	
	/**
	 *
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
		exporter.setParameter(JRXmlExporterParameter.IS_EMBEDDING_IMAGES, Boolean.valueOf(isEmbeddingImages));
		
		exporter.exportReport();
	}


	/**
	 *
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
	 *
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
	 *
	 */
	public static String exportReportToXml(JasperPrint jasperPrint) throws JRException
	{
		StringBuffer sbuffer = new StringBuffer();

		JRXmlExporter exporter = new JRXmlExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRXmlExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
		
		exporter.exportReport();
		
		return sbuffer.toString();
	}


	/**
	 *
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
	 *
	 */
	public static void exportReportToHtmlFile(
		String sourceFileName, 
		String destFileName
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFileName);

		exportReportToHtmlFile(
			jasperPrint, 
			destFileName
			);
	}

	
	/**
	 *
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


}
