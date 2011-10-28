/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class MondrianApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args) 
	{
		main(new MondrianApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		pdf();
		xmlEmbed();
		xml();
		html();
		rtf();
		xls();
		jxl();
		csv();
		odt();
		ods();
		docx();
		xlsx();
		pptx();
		xhtml();
	}
	
	
	/**
	 *
	 */
	public void fill() throws JRException
	{
		fill("metadata/monConnection.properties", "build/reports/MondrianReport.jasper");
		fill("metadata/FoodMartConnection.properties", "build/reports/FoodMartReport.jasper");
	}
	
	
	/**
	 *
	 */
	private void fill(String connProps, String fileName) throws JRException
	{
		long start = System.currentTimeMillis();
		Connection conn = null;
		try
		{
			conn = getConnection(connProps);
			if (conn != null)
			{
				Map parameters = new HashMap();
				parameters.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, conn);
				
				JasperFillManager.fillReportToFile(fileName, parameters);
				System.err.println("Report : " + fileName + ". Filling time : " + (System.currentTimeMillis() - start));
			}
		}
		catch (FileNotFoundException e)
		{
			throw new JRException(e);
		}
		catch (IOException e)
		{
			throw new JRException(e);
		}
		finally
		{
			if (conn != null)
			{
				conn.close();
			}
		}
	}
	
	
	/**
	 *
	 */
	public void print() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperPrintManager.printReport(
				reportFile.getAbsolutePath(), 
				true
				);
			System.err.println("Report : " + reportFile + ". Printing time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void pdf() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.exportReportToPdfFile(
				reportFile.getAbsolutePath()
				);
			System.err.println("Report : " + reportFile + ". PDF creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xml() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.exportReportToXmlFile(
				reportFile.getAbsolutePath(),
				false
				);
			System.err.println("Report : " + reportFile + ". XML creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xmlEmbed() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.exportReportToXmlFile(
				reportFile.getAbsolutePath(), 
				true
				);
			System.err.println("Report : " + reportFile + ". XML creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void html() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.exportReportToHtmlFile(
				reportFile.getAbsolutePath()
				);
			System.err.println("Report : " + reportFile + ". HTML creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void rtf() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
		
			JRRtfExporter exporter = new JRRtfExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". RTF creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xls() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
		
			JRXlsExporter exporter = new JRXlsExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". XLS creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void jxl() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");
		
			JExcelApiExporter exporter = new JExcelApiExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". XLS creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void csv() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
		
			JRCsvExporter exporter = new JRCsvExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". CSV creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void odt() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
		
			JROdtExporter exporter = new JROdtExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". ODT creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void ods() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".ods");
		
			JROdsExporter exporter = new JROdsExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". ODT creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void docx() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".docx");
		
			JRDocxExporter exporter = new JRDocxExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". DOCX creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xlsx() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xlsx");
		
			JRXlsxExporter exporter = new JRXlsxExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". XLSX creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void pptx() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pptx");
		
			JRPptxExporter exporter = new JRPptxExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". PPTX creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xhtml() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".x.html");
		
			JRXhtmlExporter exporter = new JRXhtmlExporter();
		
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". XHTML creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	private static Connection getConnection(String propertiesFileName) throws FileNotFoundException, IOException
	{
		if (propertiesFileName == null) {
			throw new RuntimeException("connection properties file not set");
		}
		ConnectionData data = getConnectionData(propertiesFileName);
		Connection connection = null;
		if (data.isEnabled())
		{
			connection = 
				DriverManager.getConnection(
					"Provider=mondrian;" + 
					"JdbcDrivers=" + data.getJdbcDrivers() + ";" +
					"Jdbc=" + data.getJdbcUrl() + ";" +
					"JdbcUser=" + data.getJdbcUser() + ";" +
					"JdbcPassword=" + data.getJdbcPassword() + ";" +
					"Catalog=" + data.getCatalogUri() + ";", 
					null
					);
		}
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

		String enabled = properties.getProperty("enabled");
		if (enabled != null)
		{
			data.setEnabled(Boolean.valueOf(enabled)); 
		}
		
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
		private boolean enabled = true;
		private String jdbcDrivers;
		private String jdbcUrl;
		private String jdbcUser;
		private String jdbcPassword;
		private String dataSourceName;
		private String catalogUri;

		public ConnectionData()
		{
		}

		public boolean isEnabled()
		{
			return enabled;
		}

		public void setEnabled(boolean enabled)
		{
			this.enabled = enabled;
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
