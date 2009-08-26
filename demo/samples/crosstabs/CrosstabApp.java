/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 *(at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class CrosstabApp
{


	/**
	 *
	 */
	private static final String TASK_FILL = "fill";
	private static final String TASK_PRINT = "print";
	private static final String TASK_PDF = "pdf";
	private static final String TASK_XML = "xml";
	private static final String TASK_XML_EMBED = "xmlEmbed";
	private static final String TASK_HTML = "html";
	private static final String TASK_RTF = "rtf";
	private static final String TASK_XLS = "xls";
	private static final String TASK_JXL = "jxl";
	private static final String TASK_CSV = "csv";
	private static final String TASK_ODT = "odt";
	private static final String TASK_DOCX = "docx";
	private static final String TASK_XHTML = "xhtml";
	private static final String TASK_RUN = "run";
	
	private static final String[] reportNames = {
		"OrdersReport",
		"ProductsReport",
		"ShipmentsReport",
		"LateOrdersReport"
	};
	
	/**
	 *
	 */
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			usage();
			return;
		}
				
		String taskName = args[0];
		String fileName = args[1];

		try
		{
			if (TASK_FILL.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperFillManager.fillReportToFile(
						new File(new File(fileName),  reportNames[i] + ".jasper").getAbsolutePath(), 
						null, 
						getConnection()
						);
					System.err.println("Report : " + reportNames[i] + ". Filling time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_PRINT.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperPrintManager.printReport(
						new File(new File(fileName),  reportNames[i] + ".jrprint").getAbsolutePath(), 
						true
						);
					System.err.println("Report : " + reportNames[i] + ". Printing time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_PDF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToPdfFile(
						new File(new File(fileName),  reportNames[i] + ".jrprint").getAbsolutePath()
						);
					System.err.println("Report : " + reportNames[i] + ". PDF creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_XML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToXmlFile(
						new File(new File(fileName),  reportNames[i] + ".jrprint").getAbsolutePath(), 
						false
						);
					System.err.println("Report : " + reportNames[i] + ". XML creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToXmlFile(
						new File(new File(fileName),  reportNames[i] + ".jrprint").getAbsolutePath(), 
						true
						);
					System.err.println("Report : " + reportNames[i] + ". XML creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_HTML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToHtmlFile(
						new File(new File(fileName),  reportNames[i] + ".jrprint").getAbsolutePath()
						);
					System.err.println("Report : " + reportNames[i] + ". HTML creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_RTF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(new File(fileName), reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
				
					JRRtfExporter exporter = new JRRtfExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". RTF creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_XLS.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(new File(fileName), reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
					JRXlsExporter exporter = new JRXlsExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". XLS creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_JXL.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(new File(fileName), reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");
				
					JExcelApiExporter exporter = new JExcelApiExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". XLS creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_CSV.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(new File(fileName), reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
				
					JRCsvExporter exporter = new JRCsvExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". CSV creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_ODT.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(new File(fileName), reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
				
					JROdtExporter exporter = new JROdtExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". ODT creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_DOCX.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(new File(fileName), reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".docx");
				
					JRDocxExporter exporter = new JRDocxExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". DOCX creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_XHTML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(new File(fileName), reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".x.html");
				
					JRXhtmlExporter exporter = new JRXhtmlExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". XHTML creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_RUN.equals(taskName))
			{
				for(int i = 0; i< reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperRunManager.runReportToPdfFile(
						new File(new File(fileName),  reportNames[i] + ".jrprint").getAbsolutePath(), 
						null, 
						getConnection()
						);
					System.err.println("Report : " + reportNames[i] + ". PDF running time : " + (System.currentTimeMillis() - start));
				}
			}
			else
			{
				usage();
			}
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 *
	 */
	private static void usage()
	{
		System.out.println( "CrosstabApp usage:" );
		System.out.println( "\tjava CrosstabApp task" );
		System.out.println( "\tTasks : fill | print | pdf | xml | xmlEmbed | html | rtf | xls | jxl | csv | odt | docx | xhtml | run" );
	}


	/**
	 *
	 */
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		//Change these settings according to your local configuration
		String driver = "org.hsqldb.jdbcDriver";
		String connectString = "jdbc:hsqldb:hsql://localhost";
		String user = "sa";
		String password = "";


		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connectString, user, password);
		return conn;
	}

	
	
	public static final Date truncateToMonth(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}
	
	
	public static final Date truncateToYear(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

}
