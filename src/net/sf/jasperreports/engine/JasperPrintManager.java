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

import dori.jasper.engine.print.JRPrinterAWT;
import dori.jasper.engine.util.JRLoader;


/**
 * Façade class for the JasperReports engine.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperPrintManager
{


	/**
	 *
	 */
	public static String printReportToPdfFile(String sourceFileName) throws JRException
	{
		return JasperExportManager.exportReportToPdfFile(sourceFileName);
	}


	/**
	 *
	 */
	public static void printReportToPdfFile(
		String sourceFileName,
		String destFileName
		) throws JRException
	{
		JasperExportManager.exportReportToPdfFile(sourceFileName, destFileName);
	}


	/**
	 *
	 */
	public static void printReportToPdfFile(
		JasperPrint jasperPrint,
		String destFileName
		) throws JRException
	{
		JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
	}


	/**
	 *
	 */
	public static void printReportToPdfStream(
		InputStream inputStream,
		OutputStream outputStream
		) throws JRException
	{
		JasperExportManager.exportReportToPdfStream(inputStream, outputStream);
	}


	/**
	 *
	 */
	public static void printReportToPdfStream(
		JasperPrint jasperPrint,
		OutputStream outputStream
		) throws JRException
	{
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}


	/**
	 *
	 */
	public static byte[] printReportToPdf(JasperPrint jasperPrint) throws JRException
	{
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}


	/**
	 *
	 */
	public static String printReportToXmlFile(
		String sourceFileName,
		boolean isEmbeddingImages
		) throws JRException
	{
		return 
			JasperExportManager.exportReportToXmlFile(
				sourceFileName, 
				isEmbeddingImages
				);
	}


	/**
	 *
	 */
	public static void printReportToXmlFile(
		String sourceFileName,
		String destFileName,
		boolean isEmbeddingImages
		) throws JRException
	{
		JasperExportManager.exportReportToXmlFile(
			sourceFileName, 
			destFileName,
			isEmbeddingImages
			);
	}


	/**
	 *
	 */
	public static void printReportToXmlFile(
		JasperPrint jasperPrint,
		String destFileName,
		boolean isEmbeddingImages
		) throws JRException
	{
		JasperExportManager.exportReportToXmlFile(
			jasperPrint,
			destFileName,
			isEmbeddingImages
			);
	}


	/**
	 *
	 */
	public static void printReportToXmlStream(
		InputStream inputStream,
		OutputStream outputStream
		) throws JRException
	{
		JasperExportManager.exportReportToXmlStream(
			inputStream,
			outputStream
			);
	}


	/**
	 *
	 */
	public static void printReportToXmlStream(
		JasperPrint jasperPrint,
		OutputStream outputStream
		) throws JRException
	{
		JasperExportManager.exportReportToXmlStream(
			jasperPrint,
			outputStream
			);
	}


	/**
	 *
	 */
	public static String printReportToXml(JasperPrint jasperPrint) throws JRException
	{
		return JasperExportManager.exportReportToXml(jasperPrint);
	}


	/**
	 *
	 */
	public static boolean printReport(
		String sourceFileName,
		boolean withPrintDialog
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFileName);

		return printReport(jasperPrint, withPrintDialog);
	}


	/**
	 *
	 */
	public static boolean printReport(
		InputStream inputStream,
		boolean withPrintDialog
		) throws JRException
	{
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(inputStream);

		return printReport(jasperPrint, withPrintDialog);
	}


	/**
	 *
	 */
	public static boolean printReport(
		JasperPrint jasperPrint,
		boolean withPrintDialog
		) throws JRException
	{
		return printPages(
			jasperPrint,
			0,
			jasperPrint.getPages().size() - 1,
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
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFileName);

		return printPage(jasperPrint, pageIndex, withPrintDialog);
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
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(inputStream);

		return printPage(jasperPrint, pageIndex, withPrintDialog);
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
		return printPages(
			jasperPrint,
			pageIndex,
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
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFileName);

		return printPages(
			jasperPrint,
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
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(inputStream);

		return printPages(
			jasperPrint,
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
		return JRPrinterAWT.printPages(
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
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFileName);

		return printPageToImage(jasperPrint, pageIndex, zoom);
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
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(inputStream);

		return printPageToImage(jasperPrint, pageIndex, zoom);
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
		return JRPrinterAWT.printPageToImage(jasperPrint, pageIndex, zoom);
	}


}
