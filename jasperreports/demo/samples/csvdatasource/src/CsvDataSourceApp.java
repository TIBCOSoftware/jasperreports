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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CsvDataSourceApp extends AbstractSampleApp
{
	

	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new CsvDataSourceApp(), args);
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
		long start = System.currentTimeMillis();

		// data source filling
		{
			Map parameters = new HashMap();
			parameters.put("ReportTitle", "Address Report");
			parameters.put("DataFile", "CsvDataSource.txt - CSV data source");
			Set states = new HashSet();
			states.add("Active");
			states.add("Trial");
			parameters.put("IncludedStates", states);

			String[] columnNames = new String[]{"city", "id", "name", "address", "state"};
			JRCsvDataSource dataSource = new JRCsvDataSource(JRLoader.getLocationInputStream("data/CsvDataSource.txt"));
			dataSource.setRecordDelimiter("\r\n");
//				dataSource.setUseFirstRowAsHeader(true);
			dataSource.setColumnNames(columnNames);
			
			JasperFillManager.fillReportToFile("build/reports/CsvDataSourceReport.jasper", parameters, dataSource);
			System.err.println("Report : CsvDataSourceReport.jasper. Filling time : " + (System.currentTimeMillis() - start));
		}

		
		// query executer filling
		{
			start = System.currentTimeMillis();
			Map parameters = new HashMap();
			parameters.put("ReportTitle", "Address Report");
			parameters.put("DataFile", "CsvDataSource.txt - CSV query executer");
			Set states = new HashSet();
			states.add("Active");
			states.add("Trial");
			parameters.put("IncludedStates", states);

			JasperFillManager.fillReportToFile("build/reports/CsvQueryExecuterReport.jasper", parameters);
			System.err.println("Report : CsvQueryExecuterReport.jasper. Filling time : " + (System.currentTimeMillis() - start));
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
			JasperPrintManager.printReport(reportFile.getAbsolutePath(), true);
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
			JasperExportManager.exportReportToPdfFile(reportFile.getAbsolutePath());
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
			JasperExportManager.exportReportToXmlFile(reportFile.getAbsolutePath(), false);
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
			JasperExportManager.exportReportToXmlFile(reportFile.getAbsolutePath(), true);
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
			JasperExportManager.exportReportToHtmlFile(reportFile.getAbsolutePath());
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
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			
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
	
			System.err.println("Report : " + sourceFile + ". ODS creation time : " + (System.currentTimeMillis() - start));
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
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			
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
	
	
}
