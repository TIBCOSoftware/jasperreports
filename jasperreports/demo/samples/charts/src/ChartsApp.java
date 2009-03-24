/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
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
 * 539 Bryant Street, Suite 100
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartsApp.java 2317 2008-08-27 09:06:42Z teodord $
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
		"SubDatasetChartReport",
		"MeterChartReport",
		"MultipleAxisChartReport",
		"ThermometerChartReport",
		"HighLowChartReport",
		"CandlestickChartReport",
		"StackedAreaChartReport",
		"GanttChartReport"
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
				Map parameters = new HashMap();
				parameters.put("MaxOrderID", new Integer(12500));
				
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperFillManager.fillReportToFile(
						new File(new File(fileName), reportNames[i] + ".jasper").getAbsolutePath(), 
						parameters, 
						getConnection()
						);
					System.err.println("Report : " + reportNames[i] + ". Filling time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_PDF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToPdfFile(
						new File(new File(fileName), reportNames[i] + ".jrprint").getAbsolutePath()
						);
					System.err.println("Report : " + reportNames[i] + ". PDF export time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_HTML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToHtmlFile(
						new File(new File(fileName), reportNames[i] + ".jrprint").getAbsolutePath()
						);
					System.err.println("Report : " + reportNames[i] + ". Html export time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_WRITE_XML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperCompileManager.writeReportToXmlFile(
						new File(new File(fileName), reportNames[i] + ".jasper").getAbsolutePath(), 
						new File(new File(fileName), reportNames[i] + ".jasper.jrxml").getAbsolutePath()
						);
					System.err.println("Report : " + reportNames[i] + ". XML write time : " + (System.currentTimeMillis() - start));
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
		System.out.println( "ChartsApp usage:" );
		System.out.println( "\tjava ChartsApp task" );
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
