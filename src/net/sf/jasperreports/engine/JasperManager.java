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

import java.awt.Image;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import dori.jasper.engine.design.JasperDesign;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.xml.JRXmlLoader;


/**
 * Façade class for the JasperReports engine.
 * It has various static methods that simplify the access to the API functionality
 * and can be used to compile an XML report design, to fill a report, to print it,
 * or to generate PDF files.
 */
public class JasperManager
{


	/**
	 * Fills a report and saves it directly into a PDF file.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToPdfFile(
		String sourceFileName,
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperRunManager.runReportToPdfFile(
			sourceFileName,
			parameters,
			conn
			);
	}


	/**
	 * Fills a report and saves it directly into a PDF file.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToPdfFile(
		String sourceFileName,
		String destFileName,
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperRunManager.runReportToPdfFile(
			sourceFileName,
			destFileName,
			parameters,
			conn
			);
	}


	/**
	 * Fills a report and sends it directly to an OutputStream in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToPdfStream(
		InputStream inputStream,
		OutputStream outputStream,
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperRunManager.runReportToPdfStream(
			inputStream,
			outputStream,
			parameters,
			conn
			);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static byte[] runReportToPdf(
		String sourceFileName,
		Map parameters,
		Connection conn
		) throws JRException
	{
		return JasperRunManager.runReportToPdf(
			sourceFileName,
			parameters,
			conn
			);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static byte[] runReportToPdf(
		InputStream inputStream,
		Map parameters,
		Connection conn
		) throws JRException
	{
		return JasperRunManager.runReportToPdf(
			inputStream,
			parameters,
			conn
			);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static byte[] runReportToPdf(
		JasperReport jasperReport,
		Map parameters,
		Connection conn
		) throws JRException
	{
		return JasperRunManager.runReportToPdf(
			jasperReport,
			parameters,
			conn
			);
	}


	/**
	 * Fills a report and saves it directly into a PDF file.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToPdfFile(
		String sourceFileName,
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperRunManager.runReportToPdfFile(
			sourceFileName,
			parameters,
			jrDataSource
			);
	}


	/**
	 * Fills a report and saves it directly into a PDF file.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToPdfFile(
		String sourceFileName,
		String destFileName,
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperRunManager.runReportToPdfFile(
			sourceFileName,
			destFileName,
			parameters,
			jrDataSource
			);
	}


	/**
	 * Fills a report and sends it directly to an OutputStream in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToPdfStream(
		InputStream inputStream,
		OutputStream outputStream,
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperRunManager.runReportToPdfStream(
			inputStream,
			outputStream,
			parameters,
			jrDataSource
			);
	}


	/**
	 * Fills a report and sends it to an output stream in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static byte[] runReportToPdf(
		String sourceFileName,
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		return JasperRunManager.runReportToPdf(
			sourceFileName,
			parameters,
			jrDataSource
			);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static byte[] runReportToPdf(
		InputStream inputStream,
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		return JasperRunManager.runReportToPdf(
			inputStream,
			parameters,
			jrDataSource
			);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static byte[] runReportToPdf(
		JasperReport jasperReport,
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		return JasperRunManager.runReportToPdf(
			jasperReport,
			parameters,
			jrDataSource
			);
	}


	/**
	 *
	 */
	public static String printReportToPdfFile(String sourceFileName) throws JRException
	{
		return JasperPrintManager.printReportToPdfFile(sourceFileName);
	}


	/**
	 *
	 */
	public static void printReportToPdfFile(
		String sourceFileName,
		String destFileName
		) throws JRException
	{
		JasperPrintManager.printReportToPdfFile(
			sourceFileName,
			destFileName
			);
	}


	/**
	 *
	 */
	public static void printReportToPdfFile(
		JasperPrint jasperPrint,
		String destFileName
		) throws JRException
	{
		JasperPrintManager.printReportToPdfFile(
			jasperPrint,
			destFileName
			);
	}


	/**
	 *
	 */
	public static void printReportToPdfStream(
		InputStream inputStream,
		OutputStream outputStream
		) throws JRException
	{
		JasperPrintManager.printReportToPdfStream(
			inputStream,
			outputStream
			);
	}


	/**
	 *
	 */
	public static void printReportToPdfStream(
		JasperPrint jasperPrint,
		OutputStream outputStream
		) throws JRException
	{
		JasperPrintManager.printReportToPdfStream(
			jasperPrint,
			outputStream
			);
	}


	/**
	 *
	 */
	public static byte[] printReportToPdf(JasperPrint jasperPrint) throws JRException
	{
		return JasperPrintManager.printReportToPdf(jasperPrint);
	}


	/**
	 *
	 */
	public static boolean printReport(
		String sourceFileName,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printReport(
			sourceFileName,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printReport(
		InputStream inputStream,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printReport(
			inputStream,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printReport(
		JasperPrint jasperPrint,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printReport(
			jasperPrint,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printPage(
		String sourceFileName,
		int pageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printPage(
			sourceFileName,
			pageIndex,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printPage(
		InputStream inputStream,
		int pageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printPage(
			inputStream,
			pageIndex,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printPage(
		JasperPrint jasperPrint,
		int pageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printPage(
			jasperPrint,
			pageIndex,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printPages(
		String sourceFileName,
		int firstPageIndex,
		int lastPageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printPages(
			sourceFileName,
			firstPageIndex,
			lastPageIndex,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printPages(
		InputStream inputStream,
		int firstPageIndex,
		int lastPageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printPages(
			inputStream,
			firstPageIndex,
			lastPageIndex,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static boolean printPages(
		JasperPrint jasperPrint,
		int firstPageIndex,
		int lastPageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		return JasperPrintManager.printPages(
			jasperPrint,
			firstPageIndex,
			lastPageIndex,
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static Image printPageToImage(
		String sourceFileName,
		int pageIndex,
		float zoom
		) throws JRException
	{
		return JasperPrintManager.printPageToImage(
			sourceFileName,
			pageIndex,
			zoom
			);
	}


	/**
	 *
	 */
	public static Image printPageToImage(
		InputStream inputStream,
		int pageIndex,
		float zoom
		) throws JRException
	{
		return JasperPrintManager.printPageToImage(
			inputStream,
			pageIndex,
			zoom
			);
	}


	/**
	 *
	 */
	public static Image printPageToImage(
		JasperPrint jasperPrint,
		int pageIndex,
		float zoom
		) throws JRException
	{
		return JasperPrintManager.printPageToImage(
			jasperPrint,
			pageIndex,
			zoom
			);
	}


	/**
	 *
	 */
	public static String fillReportToFile(
		String sourceFileName,
		Map parameters,
		Connection conn
		) throws JRException
	{
		return JasperFillManager.fillReportToFile(
			sourceFileName,
			parameters,
			conn
			);
	}


	/**
	 *
	 */
	public static void fillReportToFile(
		String sourceFileName,
		String destFileName,
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperFillManager.fillReportToFile(
			sourceFileName,
			destFileName,
			parameters,
			conn
			);
	}


	/**
	 *
	 */
	public static void fillReportToFile(
		JasperReport jasperReport,
		String destFileName,
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperFillManager.fillReportToFile(
			jasperReport,
			destFileName,
			parameters,
			conn
			);
	}


	/**
	 *
	 */
	public static JasperPrint fillReport(
		String sourceFileName,
		Map parameters,
		Connection conn
		) throws JRException
	{
		return JasperFillManager.fillReport(
			sourceFileName,
			parameters,
			conn
			);
	}


	/**
	 *
	 */
	public static void fillReportToStream(
		InputStream inputStream,
		OutputStream outputStream,
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperFillManager.fillReportToStream(
			inputStream,
			outputStream,
			parameters,
			conn
			);
	}


	/**
	 *
	 */
	public static void fillReportToStream(
		JasperReport jasperReport,
		OutputStream outputStream,
		Map parameters,
		Connection conn
		) throws JRException
	{
		JasperFillManager.fillReportToStream(
			jasperReport,
			outputStream,
			parameters,
			conn
			);
	}


	/**
	 *
	 */
	public static JasperPrint fillReport(
		InputStream inputStream,
		Map parameters,
		Connection conn
		) throws JRException
	{
		return JasperFillManager.fillReport(
			inputStream,
			parameters,
			conn
			);
	}

	
	/**
	 *
	 */
	public static JasperPrint fillReport(
		JasperReport jasperReport, 
		Map parameters, 
		Connection conn
		) throws JRException
	{
		return JasperFillManager.fillReport(
			jasperReport, 
			parameters, 
			conn
			);
	}

	
	/**
	 *
	 */
	public static String fillReportToFile(
		String sourceFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		return JasperFillManager.fillReportToFile(
			sourceFileName, 
			parameters,
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static void fillReportToFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperFillManager.fillReportToFile(
			sourceFileName, 
			destFileName, 
			parameters,
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static void fillReportToFile(
		JasperReport jasperReport, 
		String destFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperFillManager.fillReportToFile(
			jasperReport, 
			destFileName, 
			parameters,
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static JasperPrint fillReport(
		String sourceFileName, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		return JasperFillManager.fillReport(
			sourceFileName, 
			parameters,
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static void fillReportToStream(
		InputStream inputStream, 
		OutputStream outputStream, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperFillManager.fillReportToStream(
			inputStream, 
			outputStream, 
			parameters,
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static void fillReportToStream(
		JasperReport jasperReport, 
		OutputStream outputStream, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		JasperFillManager.fillReportToStream(
			jasperReport, 
			outputStream, 
			parameters,
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static JasperPrint fillReport(
		InputStream inputStream, 
		Map parameters,
		JRDataSource jrDataSource
		) throws JRException
	{
		return JasperFillManager.fillReport(
			inputStream, 
			parameters,
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static JasperPrint fillReport(
		JasperReport jasperReport, 
		Map parameters, 
		JRDataSource jrDataSource
		) throws JRException
	{
		return JasperFillManager.fillReport(
			jasperReport, 
			parameters, 
			jrDataSource
			);
	}

	
	/**
	 *
	 */
	public static String compileReportToFile(String sourceFileName) throws JRException
	{
		return JasperCompileManager.compileReportToFile(sourceFileName);
	}


	/**
	 *
	 */
	public static void compileReportToFile(
		String sourceFileName,
		String destFileName
		) throws JRException
	{
		JasperCompileManager.compileReportToFile(
			sourceFileName,
			destFileName
			);
	}


	/**
	 *
	 */
	public static void compileReportToFile(
		JasperDesign jasperDesign,
		String destFileName
		) throws JRException
	{
		JasperCompileManager.compileReportToFile(
			jasperDesign,
			destFileName
			);
	}


	/**
	 *
	 */
	public static JasperReport compileReport(String sourceFileName) throws JRException
	{
		return JasperCompileManager.compileReport(sourceFileName);
	}


	/**
	 *
	 */
	public static void compileReportToStream(
		InputStream inputStream,
		OutputStream outputStream
		) throws JRException
	{
		JasperCompileManager.compileReportToStream(
			inputStream,
			outputStream
			);
	}


	/**
	 *
	 */
	public static void compileReportToStream(
		JasperDesign jasperDesign,
		OutputStream outputStream
		) throws JRException
	{
		JasperCompileManager.compileReportToStream(
			jasperDesign,
			outputStream
			);
	}


	/**
	 *
	 */
	public static JasperReport compileReport(InputStream inputStream) throws JRException
	{
		return JasperCompileManager.compileReport(inputStream);
	}


	/**
	 *
	 */
	public static JasperReport compileReport(JasperDesign jasperDesign) throws JRException
	{
		return JasperCompileManager.compileReport(jasperDesign);
	}

	/**
	 *
	 */
	public static Collection verifyDesign(JasperDesign jasperDesign) throws JRException
	{
		return JasperCompileManager.verifyDesign(jasperDesign);
	}
		

	/**
	 *
	 */
	public static JasperDesign loadDesign(String fileName) throws JRException
	{
		return (JasperDesign)JRLoader.loadObject(fileName);
	}


	/**
	 *
	 */
	public static JasperDesign loadDesign(InputStream inputStream) throws JRException
	{
		return (JasperDesign)JRLoader.loadObject(inputStream);
	}


	/**
	 *
	 */
	public static JasperDesign loadXmlDesign(String fileName) throws JRException
	{
		return JRXmlLoader.load(fileName);
	}


	/**
	 *
	 */
	public static JasperDesign loadXmlDesign(InputStream inputStream) throws JRException
	{
		return JRXmlLoader.load(inputStream);
	}


	/**
	 *
	 */
	public static JasperReport loadReport(String fileName) throws JRException
	{
		return (JasperReport)JRLoader.loadObject(fileName);
	}


	/**
	 *
	 */
	public static JasperReport loadReport(InputStream inputStream) throws JRException
	{
		return (JasperReport)JRLoader.loadObject(inputStream);
	}


	/**
	 *
	 */
	public static JasperPrint loadPrint(String fileName) throws JRException
	{
		return (JasperPrint)JRLoader.loadObject(fileName);
	}


	/**
	 *
	 */
	public static JasperPrint loadPrint(InputStream inputStream) throws JRException
	{
		return (JasperPrint)JRLoader.loadObject(inputStream);
	}


}
