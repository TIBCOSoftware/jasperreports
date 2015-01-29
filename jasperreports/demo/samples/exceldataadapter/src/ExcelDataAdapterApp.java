/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class ExcelDataAdapterApp extends AbstractSampleApp
{
	

	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new ExcelDataAdapterApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		pdf();
		html();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();
		//Preparing parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ReportTitle", "Address Report");
		Set<String> states = new HashSet<String>();
		states.add("Active");
		states.add("Trial");
		parameters.put("IncludedStates", states);

		//query executer mode
		parameters.put("DataFile", "XLS query executer mode for Excel data adapter");
		JasperFillManager.fillReportToFile("build/reports/ExcelXlsQeDataAdapterReport.jasper", parameters);
		parameters.put("DataFile", "XLSX query executer mode for Excel data adapter");
		JasperFillManager.fillReportToFile("build/reports/ExcelXlsxQeDataAdapterReport.jasper", parameters);
		
		JasperReport jasperReport = (JasperReport)JRLoader.loadObjectFromFile("build/reports/ExcelXlsQeDataAdapterReport.jasper");
		jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsQeDataAdapter.xml");
		JasperFillManager.fillReportToFile(jasperReport, "build/reports/XlsQeDataAdapterReport.jrprint", parameters);

		jasperReport = (JasperReport)JRLoader.loadObjectFromFile("build/reports/ExcelXlsxQeDataAdapterReport.jasper");
		jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsxQeDataAdapter.xml");
		JasperFillManager.fillReportToFile(jasperReport, "build/reports/XlsxQeDataAdapterReport.jrprint", parameters);
		
//		SimpleJasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();
//		@SuppressWarnings("deprecation")
//		String deprecatedFactory = net.sf.jasperreports.engine.query.JRXlsQueryExecuterFactory.class.getName();
//		jasperReportsContext.setProperty("net.sf.jasperreports.query.executer.factory.xls", deprecatedFactory);
//		jasperReportsContext.setProperty("net.sf.jasperreports.query.executer.factory.XLS", deprecatedFactory);
		jasperReport = (JasperReport)JRLoader.loadObjectFromFile("build/reports/ExcelXlsQeDataAdapterReport.jasper");
		jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsQeDataAdapter.xml");
//		JasperFillManager.getInstance(jasperReportsContext).fillToFile(jasperReport, "build/reports/JxlQeDataAdapterReport.jrprint", parameters);
		JasperFillManager.fillReportToFile(jasperReport, "build/reports/JxlQeDataAdapterReport.jrprint", parameters);
		
		//data source mode
		parameters.put("DataFile", "Excel data adapter for XLS data source");
		JasperFillManager.fillReportToFile("build/reports/ExcelXlsDataAdapterReport.jasper", parameters);
		parameters.put("DataFile", "Excel data adapter for XLSX data source");
		JasperFillManager.fillReportToFile("build/reports/ExcelXlsxDataAdapterReport.jasper", parameters);
		
		jasperReport = (JasperReport)JRLoader.loadObjectFromFile("build/reports/ExcelXlsDataAdapterReport.jasper");
		jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsDataAdapter.xml");
		JasperFillManager.fillReportToFile(jasperReport, "build/reports/XlsDataAdapterReport.jrprint", parameters);

		jasperReport = (JasperReport)JRLoader.loadObjectFromFile("build/reports/ExcelXlsxDataAdapterReport.jasper");
		jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsxDataAdapter.xml");
		JasperFillManager.fillReportToFile(jasperReport, "build/reports/XlsxDataAdapterReport.jrprint", parameters);
		
		jasperReport = (JasperReport)JRLoader.loadObjectFromFile("build/reports/ExcelXlsDataAdapterReport.jasper");
		jasperReport.setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "data/XlsDataAdapter.xml");
//		JasperFillManager.getInstance(jasperReportsContext).fillToFile(jasperReport, "build/reports/JxlDataAdapterReport.jrprint", parameters);
		JasperFillManager.fillReportToFile(jasperReport, "build/reports/JxlDataAdapterReport.jrprint", parameters);
		
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}


	/**
	 *
	 */
	public void print() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperPrintManager.printReport("build/reports/ExcelXlsDataAdapterReport.jrprint", true);
		System.err.println("Printing time : " + (System.currentTimeMillis() - start));
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
			String fileName = reportFile.getAbsolutePath();
			JasperExportManager.exportReportToPdfFile(
				fileName, 
				fileName.substring(0, fileName.indexOf(".jrprint")) + ".pdf"
				);
			System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
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
			String fileName = reportFile.getAbsolutePath();
			JasperExportManager.exportReportToHtmlFile(
					fileName, 
					fileName.substring(0, fileName.indexOf(".jrprint")) + ".html"
				);
			System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
		}
		
	}


}
