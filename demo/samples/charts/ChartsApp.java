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
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;


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

	private static final String[] reportNames = 
		{
		"PieChartReport",
		"Pie3DChartReport",
		"BarChartReport",
		"Bar3DChartReport",
		"StackedBarChartReport",
		"StackedBar3DChartReport",
		"XYBarChartTimePeriodReport",
		"XYBarChartTimeSeriesReport",
		"XYBarChartReport",
		"AreaChartReport",
		"XYAreaChartReport",
		"ScatterChartReport",
		"LineChartReport",
		"XYLineChartReport",
		"TimeSeriesChartReport",
		"BubbleChartReport",
		"HighLowChartReport",
		"CandlestickChartReport",
		"SubDatasetChartReport"
		};
	
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
			if (TASK_FILL.equals(taskName))
			{
				Map parameters = new HashMap();
				parameters.put("MaxOrderID", new Integer(12500));
				
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperFillManager.fillReportToFile(reportNames[i] + ".jasper", parameters, getConnection());
					System.err.println("Report : " + reportNames[i] + ". Filling time : " + (System.currentTimeMillis() - start));
				}

				System.exit(0);
			}
			else if (TASK_PDF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToPdfFile(reportNames[i] + ".jrprint");
					System.err.println("Report : " + reportNames[i] + ". PDF export time : " + (System.currentTimeMillis() - start));
				}

				System.exit(0);
			}
			else if (TASK_HTML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToHtmlFile(reportNames[i] + ".jrprint");
					System.err.println("Report : " + reportNames[i] + ". PDF export time : " + (System.currentTimeMillis() - start));
				}

				System.exit(0);
			}
			else if (TASK_WRITE_XML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperCompileManager.writeReportToXmlFile(reportNames[i] + ".jasper", reportNames[i] + ".jasper.jrxml");
					System.err.println("Report : " + reportNames[i] + ". XML write time : " + (System.currentTimeMillis() - start));
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

}
