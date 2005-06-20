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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ChartsApp
{


	/**
	 *
	 */
	private static final String TASK_FILL = "fill";
	private static final String TASK_PDF = "pdf";
	private static final String TASK_HTML = "html";
	private static final String TASK_WRITE_XML = "writeXml";

	
	/**
	 *
	 */
	public static void main(String[] args)
	{
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
			long start = System.currentTimeMillis();
			if (TASK_FILL.equals(taskName))
			{
				Map parameters = new HashMap();
				parameters.put("MaxOrderID", new Integer(12500));
				
				JasperFillManager.fillReportToFile("BarChartReport.jasper", parameters, getConnection());
				System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_PDF.equals(taskName))
			{
				JasperExportManager.exportReportToPdfFile("BarChartReport.jrprint");
				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_HTML.equals(taskName))
			{
				JasperExportManager.exportReportToHtmlFile("BarChartReport.jrprint");
				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_WRITE_XML.equals(taskName))
			{
				JasperCompileManager.writeReportToXmlFile("BarChartReport.jasper");
				System.err.println("XML design creation time : " + (System.currentTimeMillis() - start));
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
		System.out.println( "ChartsApp usage:" );
		System.out.println( "\tjava ChartsApp -Ttask" );
		System.out.println( "\tTasks : fill | pdf | html" );
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


}
