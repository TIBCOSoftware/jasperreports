/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
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
	private static final String TASK_RUN = "run";
	
	private static final String[] reportNames = {
		"OrdersReport",
		"ProductsReport",
		"ShipmentsReport"
	};
	
	/**
	 *
	 */
	public static void main(String[] args)
	{
		String fileName = null;
		String taskName = null;

		if(args.length == 0)
		{
			usage();
			return;
		}
				
		int k = 0;
		while ( args.length > k )
		{
			if ( args[k].startsWith("-T") )
				taskName = args[k].substring(2);
		
			
			k++;	
		}

		try
		{
			if (TASK_FILL.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperFillManager.fillReportToFile(reportNames[i] + ".jasper", null, getConnection());
					System.err.println("Report : " + reportNames[i] + ". Filling time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_PRINT.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperPrintManager.printReport(reportNames[i] + ".jrprint", true);
					System.err.println("Report : " + reportNames[i] + ". Printing time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_PDF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToPdfFile(reportNames[i] + ".jrprint");
					System.err.println("Report : " + reportNames[i] + ". PDF creation time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_XML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToXmlFile(reportNames[i] + ".jrprint", false);
					System.err.println("Report : " + reportNames[i] + ". XML creation time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToXmlFile(reportNames[i] + ".jrprint", true);
					System.err.println("Report : " + reportNames[i] + ". XML creation time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_HTML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToHtmlFile(reportNames[i] + ".jrprint");
					System.err.println("Report : " + reportNames[i] + ". HTML creation time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_RTF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
				
					JRRtfExporter exporter = new JRRtfExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". RTF creation time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_XLS.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
					JRXlsExporter exporter = new JRXlsExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". XLS creation time : " + (System.currentTimeMillis() - start));
				}
				
				System.exit(0);
			}
			else if (TASK_JXL.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");
				
					JExcelApiExporter exporter = new JExcelApiExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". XLS creation time : " + (System.currentTimeMillis() - start));
				}
				
				System.exit(0);
			}
			else if (TASK_CSV.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++) {
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
				
					JRCsvExporter exporter = new JRCsvExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". CSV creation time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_RUN.equals(taskName))
			{
				for(int i = 0; i< reportNames.length; i++) {
					long start = System.currentTimeMillis();
					JasperRunManager.runReportToPdfFile(reportNames[i] + ".jrprint", null, getConnection());
					System.err.println("Report : " + reportNames[i] + ". PDF running time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else
			{
				usage();
				System.exit(0);
			}
		}
		catch (JRException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 *
	 */
	private static void usage()
	{
		System.out.println( "CrosstabApp usage:" );
		System.out.println( "\tjava CrosstabApp -Ttask" );
		System.out.println( "\tTasks : fill | print | pdf | xml | xmlEmbed | html | rtf | xls | jxl | csv | run" );
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
