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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
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
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class MondrianApp
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
	private static final String TASK_RUN = "run";
	
	
	/**
	 *
	 */
	public static void main(String[] args)
	{
		String fileName = null;
		String taskName = null;
		String propertiesFileName = null;

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
			if ( args[k].startsWith("-F") )
				fileName = args[k].substring(2);
			if ( args[k].startsWith("-P") )
				propertiesFileName = args[k].substring(2);
			
			k++;	
		}

		try
		{
			long start = System.currentTimeMillis();
			if (TASK_FILL.equals(taskName))
			{
				Connection conn = getConnection(propertiesFileName);
				try
				{
					Map parameters = new HashMap();
					parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
					
					JasperFillManager.fillReportToFile(fileName, parameters);
					System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				}
				finally
				{
					conn.close();
				}
			}
			else if (TASK_PRINT.equals(taskName))
			{
				JasperPrintManager.printReport(fileName, true);
				System.err.println("Printing time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_PDF.equals(taskName))
			{
				JasperExportManager.exportReportToPdfFile(fileName);
				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
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
			else if (TASK_RTF.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
				
				JRRtfExporter exporter = new JRRtfExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
				exporter.exportReport();

				System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_XLS.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
				JRXlsExporter exporter = new JRXlsExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				
				exporter.exportReport();

				System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_JXL.equals(taskName))
			{
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");

				JExcelApiExporter exporter = new JExcelApiExporter();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);

				exporter.exportReport();

				System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_CSV.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
				
				JRCsvExporter exporter = new JRCsvExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
				exporter.exportReport();

				System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_ODT.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
				
				JROdtExporter exporter = new JROdtExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
				exporter.exportReport();

				System.err.println("ODT creation time : " + (System.currentTimeMillis() - start));
			}
			else if (TASK_RUN.equals(taskName))
			{
				Connection conn = getConnection(propertiesFileName);
				try
				{
					Map parameters = new HashMap();
					parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
					
					JasperRunManager.runReportToPdfFile(fileName, parameters);
					System.err.println("PDF running time : " + (System.currentTimeMillis() - start));
				}
				finally
				{
					conn.close();
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
		System.out.println( "MondrianApp usage:" );
		System.out.println( "\tjava MondrianApp -Ttask -Ffile -Pproperties" );
		System.out.println( "\tTasks : fill | print | pdf | xml | xmlEmbed | html | rtf | xls | csv | odt | run" );
		System.out.println( "\tproperties : properties file for Mondrian connection" );
	}


	private static Connection getConnection(String propertiesFileName) throws FileNotFoundException, IOException
	{
		if (propertiesFileName == null) {
			throw new RuntimeException("connection properties file not set");
		}
		ConnectionData data = getConnectionData(propertiesFileName);
		Connection connection = 
			DriverManager.getConnection(
					"Provider=mondrian;" + 
					"JdbcDrivers=" + data.getJdbcDrivers() + ";" +
					"Jdbc=" + data.getJdbcUrl() + ";" +
					"JdbcUser=" + data.getJdbcUser() + ";" +
					"JdbcPassword=" + data.getJdbcPassword() + ";" +
					"Catalog=" + data.getCatalogUri() + ";", 
					null, false);
		
		return connection;
	}

	private static Properties loadConnectionProperties(String path) throws FileNotFoundException, IOException
	{
		File connectionFile = new File(path);
		Properties properties = new Properties();
		properties.load(new FileInputStream(connectionFile));
		return properties;
	}

	private static ConnectionData getConnectionData(String propertiesFileName) throws IOException
	{
		Properties properties = loadConnectionProperties(propertiesFileName);

		ConnectionData data = (new MondrianApp()).new ConnectionData();
		String prop;

		String dataSourceName = properties.getProperty("dataSourceName");
		if (dataSourceName == null || dataSourceName.length() == 0) {
			prop = properties.getProperty("jdbcDrivers");
			if (prop != null && prop.length() > 0)
				data.setJdbcDrivers(prop);
			else
				throw new RuntimeException("Invalid JDBC driver");

			prop = properties.getProperty("jdbcUrl");
			if (prop != null && prop.length() > 0)
				data.setJdbcUrl(prop);
			else
				throw new RuntimeException("Invalid JDBC URL");

			prop = properties.getProperty("jdbcUser");
			if (prop != null && prop.length() > 0)
				data.setJdbcUser(prop);
			else
				data.setJdbcUser("");

			prop = properties.getProperty("jdbcPassword");
			if (prop != null && prop.length() > 0)
				data.setJdbcPassword(prop);
			else
				data.setJdbcPassword("");
		}
		else
			data.setDataSourceName(dataSourceName);

		prop = properties.getProperty("catalogUri");
		if (prop != null && prop.length() > 0) {
			data.setCatalogUri(prop);
		} else
			throw new RuntimeException("Invalid catalog URI");

		return data;
	}

	
	/**
	 * 
	 */
	private class ConnectionData
	{
		private String jdbcDrivers;
		private String jdbcUrl;
		private String jdbcUser;
		private String jdbcPassword;
		private String dataSourceName;
		private String catalogUri;

		public ConnectionData()
		{
		}

		public String getCatalogUri()
		{
			return catalogUri;
		}

		public void setCatalogUri(String catalogUri)
		{
			this.catalogUri = catalogUri;
		}

		public String getDataSourceName()
		{
			return dataSourceName;
		}

		public void setDataSourceName(String dataSourceName)
		{
			this.dataSourceName = dataSourceName;
		}

		public String getJdbcDrivers()
		{
			return jdbcDrivers;
		}

		public void setJdbcDrivers(String jdbcDrivers)
		{
			this.jdbcDrivers = jdbcDrivers;
		}

		public String getJdbcPassword()
		{
			return jdbcPassword;
		}

		public void setJdbcPassword(String jdbcPassword)
		{
			this.jdbcPassword = jdbcPassword;
		}

		public String getJdbcUrl()
		{
			return jdbcUrl;
		}

		public void setJdbcUrl(String jdbcUrl)
		{
			this.jdbcUrl = jdbcUrl;
		}

		public String getJdbcUser()
		{
			return jdbcUser;
		}

		public void setJdbcUser(String jdbcUser)
		{
			this.jdbcUser = jdbcUser;
		}
	}
}
