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
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

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
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: CrosstabApp.java 3148 2009-10-23 14:57:10Z shertage $
 */
public class CrosstabApp
{


	/**
	 *
	 */
	public void executeTask(String taskName)
	{
		try
		{
			Method method = getClass().getMethod(taskName, new Class[]{});
			method.invoke(this, new Object[]{});
		}
		catch (InvocationTargetException e)
		{
			e.getCause().printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

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
				
		new CrosstabApp().executeTask(args[0]);
	}
	
	
	/**
	 *
	 */
	public void fill() throws JRException, ClassNotFoundException, SQLException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jasper"))
			{
				long start = System.currentTimeMillis();
				JasperFillManager.fillReportToFile(
					new File(parentFile, reportFile).getAbsolutePath(), 
					null, 
					getConnection()
					);
				System.err.println("Report : " + reportFile + ". Filling time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void print() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jasper"))
			{
				long start = System.currentTimeMillis();
				JasperPrintManager.printReport(
					new File(parentFile, reportFile).getAbsolutePath(), 
					true
					);
				System.err.println("Report : " + reportFile + ". Printing time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void pdf() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToPdfFile(
					new File(parentFile, reportFile).getAbsolutePath()
					);
				System.err.println("Report : " + reportFile + ". PDF creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void xml() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToXmlFile(
					new File(parentFile, reportFile).getAbsolutePath(),
					false
					);
				System.err.println("Report : " + reportFile + ". XML creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void xmlEmbed() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToXmlFile(
					new File(parentFile, reportFile).getAbsolutePath(), 
					true
					);
				System.err.println("Report : " + reportFile + ". XML creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void html() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToHtmlFile(
					new File(parentFile, reportFile).getAbsolutePath()
					);
				System.err.println("Report : " + reportFile + ". HTML creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void rtf() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
			
				JRRtfExporter exporter = new JRRtfExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". RTF creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void xls() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
			
				JRXlsExporter exporter = new JRXlsExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". XLS creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void jxl() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile); 
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");
			
				JExcelApiExporter exporter = new JExcelApiExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". XLS creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void csv() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
			
				JRCsvExporter exporter = new JRCsvExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". CSV creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void odt() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
			
				JROdtExporter exporter = new JROdtExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". ODT creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void ods() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".ods");
			
				JROdsExporter exporter = new JROdsExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". ODT creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void docx() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".docx");
			
				JRDocxExporter exporter = new JRDocxExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". DOCX creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void xlsx() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xlsx");
			
				JRXlsxExporter exporter = new JRXlsxExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". XLSX creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void xhtml() throws JRException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jrprint"))
			{
				long start = System.currentTimeMillis();
				File sourceFile = new File(parentFile, reportFile);
	
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
	
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".x.html");
			
				JRXhtmlExporter exporter = new JRXhtmlExporter();
			
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			
				exporter.exportReport();

				System.err.println("Report : " + reportFile + ". XHTML creation time : " + (System.currentTimeMillis() - start));
			}
		}
	}
	
	
	/**
	 *
	 */
	public void run() throws JRException, ClassNotFoundException, SQLException
	{
		File parentFile = new File("build/reports");
		String[] files = parentFile.list();
		for(int i = 0; i < files.length; i++)
		{
			String reportFile = files[i];
			if (reportFile.endsWith(".jasper"))
			{
				long start = System.currentTimeMillis();
				JasperRunManager.runReportToPdfFile(
					new File(parentFile, reportFile).getAbsolutePath(), 
					null, 
					getConnection()
					);
				System.err.println("Report : " + reportFile + ". PDF running time : " + (System.currentTimeMillis() - start));
			}
		}
	}


	/**
	 *
	 */
	private static void usage()
	{
		System.out.println( "CrosstabApp usage:" );
		System.out.println( "\tjava CrosstabApp task" );
		System.out.println( "\tTasks : fill | print | pdf | xml | xmlEmbed | html | rtf | xls | jxl | csv | odt | ods | docx | xls | xhtml | run" );
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
