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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class XlsFeaturesApp extends AbstractSampleApp
{
	

	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new XlsFeaturesApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		xls();
		jxl();
		xlsx();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();

		// data source filling
		{
			Map parameters = new HashMap();
			parameters.put("ReportTitle", "Customers Report");
			parameters.put("Customers", "Customers");
			parameters.put("ReportDate", new Date());
			parameters.put("DataFile", "CsvDataSource.txt - CSV data source");

			String[] columnNames = new String[]{"city", "id", "name", "address", "state"};
			JRCsvDataSource dataSource = new JRCsvDataSource(JRLoader.getLocationInputStream("data/CsvDataSource.txt"));
			dataSource.setRecordDelimiter("\r\n");
//				dataSource.setUseFirstRowAsHeader(true);
			dataSource.setColumnNames(columnNames);
			
			JasperFillManager.fillReportToFile("build/reports/XlsFeaturesReport.jasper", parameters, dataSource);
			System.err.println("Report : XlsFeaturesReport.jasper. Filling time : " + (System.currentTimeMillis() - start));
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
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);
			
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
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);
	
			exporter.exportReport();
	
			System.err.println("Report : " + sourceFile + ". XLS creation time : " + (System.currentTimeMillis() - start));
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
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);
			
			exporter.exportReport();
	
			System.err.println("Report : " + sourceFile + ". XLSX creation time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
}
