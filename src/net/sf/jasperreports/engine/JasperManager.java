/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
package net.sf.jasperreports.engine;

import java.awt.Image;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 * General purpose façade class for the JasperReports engine.
 * It delegates almost all its functionality to the other specialized 
 * façade classes for compiling, filling, printing or exporting reports.
 * 
 * @see net.sf.jasperreports.engine.JasperCompileManager
 * @see net.sf.jasperreports.engine.JasperFillManager
 * @see net.sf.jasperreports.engine.JasperPrintManager
 * @see net.sf.jasperreports.engine.JasperExportManager
 * @see net.sf.jasperreports.engine.JasperRunManager
 * @see net.sf.jasperreports.engine.util.JRLoader
 * @see net.sf.jasperreports.engine.xml.JRXmlLoader
 * @deprecated Use the specialized façade classes for specific operations
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperManager
{


	/**
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdfFile(String, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdfFile(String, String, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdfStream(InputStream, OutputStream, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdf(String, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdf(InputStream, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdf(JasperReport, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdfFile(String, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdfFile(String, String, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdfStream(InputStream, OutputStream, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdf(String, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdf(InputStream, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperRunManager#runReportToPdf(JasperReport, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperExportManager#exportReportToPdfFile(String)}.
	 */
	public static String printReportToPdfFile(String sourceFileName) throws JRException
	{
		return JasperExportManager.exportReportToPdfFile(sourceFileName);
	}


	/**
	 * @deprecated Replaced by {@link JasperExportManager#exportReportToPdfFile(String, String)}.
	 */
	public static void printReportToPdfFile(
		String sourceFileName,
		String destFileName
		) throws JRException
	{
		JasperExportManager.exportReportToPdfFile(
			sourceFileName,
			destFileName
			);
	}


	/**
	 * @deprecated Replaced by {@link JasperExportManager#exportReportToPdfFile(JasperPrint, String)}.
	 */
	public static void printReportToPdfFile(
		JasperPrint jasperPrint,
		String destFileName
		) throws JRException
	{
		JasperExportManager.exportReportToPdfFile(
			jasperPrint,
			destFileName
			);
	}


	/**
	 * @deprecated Replaced by {@link JasperExportManager#exportReportToPdfStream(InputStream, OutputStream)}.
	 */
	public static void printReportToPdfStream(
		InputStream inputStream,
		OutputStream outputStream
		) throws JRException
	{
		JasperExportManager.exportReportToPdfStream(
			inputStream,
			outputStream
			);
	}


	/**
	 * @deprecated Replaced by {@link JasperExportManager#exportReportToPdfStream(JasperPrint, OutputStream)}.
	 */
	public static void printReportToPdfStream(
		JasperPrint jasperPrint,
		OutputStream outputStream
		) throws JRException
	{
		JasperExportManager.exportReportToPdfStream(
			jasperPrint,
			outputStream
			);
	}


	/**
	 * @deprecated Replaced by {@link JasperExportManager#exportReportToPdf(JasperPrint)}.
	 */
	public static byte[] printReportToPdf(JasperPrint jasperPrint) throws JRException
	{
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}


	/**
	 * @deprecated Replaced by {@link JasperPrintManager#printReport(String, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printReport(InputStream, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printReport(JasperPrint, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPage(String, int, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPage(InputStream, int, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPage(JasperPrint, int, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPages(String, int, int, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPages(InputStream, int, int, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPages(JasperPrint, int, int, boolean)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPageToImage(String, int, float)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPageToImage(InputStream, int, float)}.
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
	 * @deprecated Replaced by {@link JasperPrintManager#printPageToImage(JasperPrint, int, float)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToFile(String, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToFile(String, String, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToFile(JasperReport, String, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReport(String, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToStream(InputStream, OutputStream, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToStream(JasperReport, OutputStream, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReport(InputStream, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReport(JasperReport, Map, Connection)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToFile(String, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToFile(String, String, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToFile(JasperReport, String, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReport(String, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToStream(InputStream, OutputStream, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReportToStream(JasperReport, OutputStream, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReport(InputStream, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperFillManager#fillReport(JasperReport, Map, JRDataSource)}.
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
	 * @deprecated Replaced by {@link JasperCompileManager#compileReportToFile(String)}.
	 */
	public static String compileReportToFile(String sourceFileName) throws JRException
	{
		return JasperCompileManager.compileReportToFile(sourceFileName);
	}


	/**
	 * @deprecated Replaced by {@link JasperCompileManager#compileReportToFile(String, String)}.
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
	 * @deprecated Replaced by {@link JasperCompileManager#compileReportToFile(JasperDesign, String)}.
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
	 * @deprecated Replaced by {@link JasperCompileManager#compileReport(String)}.
	 */
	public static JasperReport compileReport(String sourceFileName) throws JRException
	{
		return JasperCompileManager.compileReport(sourceFileName);
	}


	/**
	 * @deprecated Replaced by {@link JasperCompileManager#compileReportToStream(InputStream, OutputStream)}.
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
	 * @deprecated Replaced by {@link JasperCompileManager#compileReportToStream(JasperDesign, OutputStream)}.
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
	 * @deprecated Replaced by {@link JasperCompileManager#compileReport(InputStream)}.
	 */
	public static JasperReport compileReport(InputStream inputStream) throws JRException
	{
		return JasperCompileManager.compileReport(inputStream);
	}


	/**
	 * @deprecated Replaced by {@link JasperCompileManager#compileReport(JasperDesign)}.
	 */
	public static JasperReport compileReport(JasperDesign jasperDesign) throws JRException
	{
		return JasperCompileManager.compileReport(jasperDesign);
	}

	/**
	 * @deprecated Replaced by {@link JasperCompileManager#verifyDesign(JasperDesign)}.
	 */
	public static Collection verifyDesign(JasperDesign jasperDesign) throws JRException
	{
		return JasperCompileManager.verifyDesign(jasperDesign);
	}
		

	/**
	 * @deprecated Use {@link JRLoader#loadObject(String)} with the appropriate cast.
	 */
	public static JasperDesign loadDesign(String fileName) throws JRException
	{
		return (JasperDesign)JRLoader.loadObject(fileName);
	}


	/**
	 * @deprecated Use {@link JRLoader#loadObject(InputStream)} with the appropriate cast.
	 */
	public static JasperDesign loadDesign(InputStream inputStream) throws JRException
	{
		return (JasperDesign)JRLoader.loadObject(inputStream);
	}


	/**
	 * @deprecated Use {@link JRXmlLoader#load(String)} instead.
	 */
	public static JasperDesign loadXmlDesign(String fileName) throws JRException
	{
		return JRXmlLoader.load(fileName);
	}


	/**
	 * @deprecated Use {@link JRXmlLoader#load(InputStream)} instead.
	 */
	public static JasperDesign loadXmlDesign(InputStream inputStream) throws JRException
	{
		return JRXmlLoader.load(inputStream);
	}


	/**
	 * @deprecated Use {@link JRLoader#loadObject(String)} with the appropriate cast.
	 */
	public static JasperReport loadReport(String fileName) throws JRException
	{
		return (JasperReport)JRLoader.loadObject(fileName);
	}


	/**
	 * @deprecated Use {@link JRLoader#loadObject(InputStream)} with the appropriate cast.
	 */
	public static JasperReport loadReport(InputStream inputStream) throws JRException
	{
		return (JasperReport)JRLoader.loadObject(inputStream);
	}


	/**
	 * @deprecated Use {@link JRLoader#loadObject(String)} with the appropriate cast.
	 */
	public static JasperPrint loadPrint(String fileName) throws JRException
	{
		return (JasperPrint)JRLoader.loadObject(fileName);
	}


	/**
	 * @deprecated Use {@link JRLoader#loadObject(InputStream)} with the appropriate cast.
	 */
	public static JasperPrint loadPrint(InputStream inputStream) throws JRException
	{
		return (JasperPrint)JRLoader.loadObject(inputStream);
	}


}
