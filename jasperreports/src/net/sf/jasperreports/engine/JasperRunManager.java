/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
