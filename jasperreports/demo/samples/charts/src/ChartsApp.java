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
				
				File parentFile = new File(fileName).getParentFile();
				String[] files = parentFile.list();
				for(int i = 0; i < files.length; i++)
				{
					String reportFile = files[i];
					if (reportFile.endsWith(".jasper"))
					{
						long start = System.currentTimeMillis();
						JasperFillManager.fillReportToFile(
							new File(parentFile, reportFile).getAbsolutePath(), 
							parameters, 
							getConnection()
							);
						System.err.println("Report : " + reportFile + ". Filling time : " + (System.currentTimeMillis() - start));
					}
				}
			}
			else if (TASK_PDF.equals(taskName))
			{
				File parentFile = new File(fileName).getParentFile();
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
						System.err.println("Report : " + reportFile + ". PDF export time : " + (System.currentTimeMillis() - start));
					}
				}
			}
			else if (TASK_HTML.equals(taskName))
			{
				File parentFile = new File(fileName).getParentFile();
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
						System.err.println("Report : " + reportFile + ". Html export time : " + (System.currentTimeMillis() - start));
					}
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
