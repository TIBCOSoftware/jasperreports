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
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.stanford.ejalbert.BrowserLauncher;

import net.sf.jasperreports.engine.JREmptyDataSource;
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
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class OFCApp
{

	private static final int HTTP_PORT = 7162;

	/**
	 *
	 */
	private static final String TASK_FILL = "fill";
	private static final String TASK_XML = "xml";
	private static final String TASK_XML_EMBED = "xmlEmbed";
	private static final String TASK_HTML = "html";
	private static final String TASK_VIEW = "view";
	
	
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
				
		String taskName = args.length > 0 ? args[0] : null;
		String fileName = args.length > 1 ? args[1] : null;

		try
		{
			long start = System.currentTimeMillis();
			if (TASK_FILL.equals(taskName))
			{
				JasperFillManager.fillReportToFile(fileName, null, getConnection());
				System.err.println("Filling time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_XML.equals(taskName))
			{
				JasperExportManager.exportReportToXmlFile(fileName, false);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				JasperExportManager.exportReportToXmlFile(fileName, true);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_HTML.equals(taskName))
			{
				JasperExportManager.exportReportToHtmlFile(fileName);
				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_VIEW.equals(taskName))
			{
				launchReport();
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

	protected static void launchReport() throws Exception
	{
		System.out.println("Starting an embedded web server on port " + HTTP_PORT);
		System.out.println("Kill/interrupt this process to shutdown the server");
		
		//starting the web server
		new NanoHTTPD(HTTP_PORT);
		
		//launching the browser
		launchBrowser();
		
		//waiting for a long time so that the server does not shut down
		try
		{
			Thread.sleep(48 * 60 * 60 * 1000);
		}
		catch (InterruptedException e)
		{
			//interrupted
		}
	}

	protected static void launchBrowser()
	{
		String reportURI = "http://localhost:" + HTTP_PORT + "/OpenFlashChartReport.html";
		
		System.out.println("Launching a browser to for " + reportURI);
		System.out.println("If a browser is not launched, please navigate to this URL manually");
		
		boolean launched = false;
		try
		{
			// attempting Java 1.6
			// ugly reflection code to avoid a dependency on Java 1.6
			Class desktopClass = Class.forName("java.awt.Desktop");
			Method isDesktopSupportedMethod = desktopClass.getMethod("isDesktopSupported", 
					(java.lang.Class[]) null);
			Boolean isDesktopSupported = (Boolean) isDesktopSupportedMethod.invoke(null, 
					(java.lang.Object[]) null);
			if (isDesktopSupported.booleanValue())
			{
				Method getDesktopMethod = desktopClass.getMethod("getDesktop", 
						(java.lang.Class[]) null);
				Object desktop = getDesktopMethod.invoke(null, (java.lang.Object[]) null);
				Method browseMethod = desktopClass.getMethod("browse", new Class[]{URI.class});
				browseMethod.invoke(desktop, new Object[]{new URI(reportURI)});
				launched = true;
			}
		}
		catch (Exception e)
		{
			// not Java 1.6?
			System.out.println("Failed to launch a browser using Java 1.6 APIs");
		}
		
		if (!launched)
		{
			try
			{
				new BrowserLauncher().openURLinBrowser(reportURI);
			}
			catch (Exception e)
			{
				System.out.println("Failed to launch a browser: " + e.getMessage());
			}
		}
	}
	
	/**
	 *
	 */
	private static void usage()
	{
		System.out.println( "OFCApp usage:" );
		System.out.println( "\tjava OFCApp task file" );
		System.out.println( "\tTasks : fill | xml | xmlEmbed | html | view" );
	}


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


}
