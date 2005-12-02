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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.util.JRLoader;


/**
 * Façade class for the JasperReports engine. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JasperRunManager
{


	/**
	 * Fills a report and saves it directly into a PDF file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static String runReportToPdfFile(
		String sourceFileName, 
		Map parameters, 
		Connection conn
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/*   */
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

		/*   */
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pdf");
		String destFileName = destFile.toString();

		JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
		
		return destFileName;
	}


	/**
	 * Fills a report and saves it directly into a PDF file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param sourceFileName the name of the compiled report file
	 * @param parameters the parameters map
	 * @return the name of the generated PDF file
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static String runReportToPdfFile(
		String sourceFileName, 
		Map parameters 
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/*   */
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

		/*   */
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pdf");
		String destFileName = destFile.toString();

		JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
		
		return destFileName;
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, conn);

		JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
	}


	/**
	 * Fills a report and saves it directly into a PDF file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param destFileName PDF destination file name
	 * @param parameters     report parameters map
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static void runReportToPdfFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters 
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters);

		JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, conn);

		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}


	/**
	 * Fills a report and sends it directly to an OutputStream in PDF format. 
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param inputStream compiled report input stream
	 * @param outputStream PDF output stream
	 * @param parameters parameters map
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static void runReportToPdfStream(
		InputStream inputStream, 
		OutputStream outputStream, 
		Map parameters 
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters);

		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, conn);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param parameters     report parameters map
	 * @return binary PDF output
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static byte[] runReportToPdf(
		String sourceFileName, 
		Map parameters 
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters);

		return JasperExportManager.exportReportToPdf(jasperPrint);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, conn);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param inputStream  input stream to read the compiled report design object from
	 * @param parameters   report parameters map
	 * @return binary PDF output
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static byte[] runReportToPdf(
		InputStream inputStream, 
		Map parameters 
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters);

		return JasperExportManager.exportReportToPdf(jasperPrint);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}


	/**
	 * Fills a report and returns byte array object containing the report in PDF format.
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param jasperReport the compiled report
	 * @param parameters the parameters map
	 * @return binary PDF output
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static byte[] runReportToPdf(
		JasperReport jasperReport, 
		Map parameters 
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	
	/**
	 * Fills a report and saves it directly into a PDF file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static String runReportToPdfFile(
		String sourceFileName, 
		Map parameters, 
		JRDataSource jrDataSource
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/*   */
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);

		/*   */
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pdf");
		String destFileName = destFile.toString();

		JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
		
		return destFileName;
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, jrDataSource);

		/*   */
		JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, jrDataSource);

		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, jrDataSource);

		return JasperExportManager.exportReportToPdf(jasperPrint);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, jrDataSource);

		return JasperExportManager.exportReportToPdf(jasperPrint);
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
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}


	/**
	 * Fills a report and saves it directly into a HTML file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static String runReportToHtmlFile(
		String sourceFileName, 
		Map parameters, 
		Connection conn
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/*   */
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

		/*   */
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".html");
		String destFileName = destFile.toString();

		JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
		
		return destFileName;
	}


	/**
	 * Fills a report and saves it directly into a HTML file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param sourceFileName the name of the compiled report file
	 * @param parameters the parameters map
	 * @return the name of the generated HTML file
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static String runReportToHtmlFile(
		String sourceFileName, 
		Map parameters 
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/*   */
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

		/*   */
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".html");
		String destFileName = destFile.toString();

		JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
		
		return destFileName;
	}

	
	/**
	 * Fills a report and saves it directly into a HTML file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToHtmlFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters, 
		Connection conn
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, conn);

		JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
	}


	/**
	 * Fills a report and saves it directly into a HTML file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 * 
	 * @param sourceFileName source file containing the compile report design
	 * @param destFileName name of the destination HTML file
	 * @param parameters     report parameters map
	 * @throws JRException
	 * @see net.sf.jasperreports.engine.fill.JRFiller#fillReport(JasperReport, Map)
	 */
	public static void runReportToHtmlFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters 
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters);

		JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
	}


	/**
	 * Fills a report and saves it directly into a HTML file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static String runReportToHtmlFile(
		String sourceFileName, 
		Map parameters, 
		JRDataSource jrDataSource
		) throws JRException
	{
		File sourceFile = new File(sourceFileName);

		/*   */
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);

		/*   */
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".html");
		String destFileName = destFile.toString();

		JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
		
		return destFileName;
	}

	
	/**
	 * Fills a report and saves it directly into a HTML file. 
	 * The intermediate JasperPrint object is not saved on disk.
	 */
	public static void runReportToHtmlFile(
		String sourceFileName, 
		String destFileName, 
		Map parameters, 
		JRDataSource jrDataSource
		) throws JRException
	{
		/*   */
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, jrDataSource);

		/*   */
		JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
	}

	
}
