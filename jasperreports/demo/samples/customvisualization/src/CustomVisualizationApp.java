/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.customvisualization.CVConstants;
import net.sf.jasperreports.customvisualization.export.CVElementPhantomJSImageProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOdsReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class CustomVisualizationApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new CustomVisualizationApp(), args);
	}
	
	
	@Override
	public void test() throws JRException
	{
		fill();
		pdf();
		xmlEmbed();
		xml();
		html();
		rtf();
		xls();
		csv();
		odt();
		ods();
		docx();
		xlsx();
		pptx();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
		File[] files = getFiles(new File("build/reports"), "jasper");
		for (int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperFillManager.fillReportToFile(
				reportFile.getAbsolutePath(), 
				null 
				);
			System.err.println("Report : " + reportFile + ". Filling time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void view() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		JasperViewer.viewReport(jasperReportsContext, "build/reports/LeafletMarkers.jrprint", false, true);
	}
	
	
	/**
	 *
	 */
	public void viewXml() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		JasperViewer.viewReport(jasperReportsContext, "build/reports/LeafletMarkers.jrpxml", true, true);
	}


	/**
	 *
	 */
	public void print() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		JasperPrintManager.getInstance(jasperReportsContext).print("build/reports/LeafletMarkers.jrprint", true);
		System.err.println("Printing time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void pdf() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for (int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.getInstance(jasperReportsContext).exportToPdfFile(
				reportFile.getAbsolutePath()
				);
			System.err.println("Report : " + reportFile + ". PDF export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xml() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for (int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.getInstance(jasperReportsContext). exportToXmlFile(
				reportFile.getAbsolutePath(), false
				);
			System.err.println("Report : " + reportFile + ". XML export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xmlEmbed() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for (int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.getInstance(jasperReportsContext). exportToXmlFile(
				reportFile.getAbsolutePath(), true
				);
			System.err.println("Report : " + reportFile + ". XML export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void html() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for (int i = 0; i < files.length; i++)
		{
			File reportFile = files[i];
			long start = System.currentTimeMillis();
			JasperExportManager.getInstance(jasperReportsContext).exportToHtmlFile(
				reportFile.getAbsolutePath()
				);
			System.err.println("Report : " + reportFile + ". HTML export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void rtf() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for (int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
		
			JRRtfExporter exporter = new JRRtfExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". RTF export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xls() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
		
			JRXlsExporter exporter = new JRXlsExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			exporter.setConfiguration(configuration);
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". XLS export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void csv() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
		
			JRCsvExporter exporter = new JRCsvExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". CSV export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void odt() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
		
			JROdtExporter exporter = new JROdtExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". ODT export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void ods() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".ods");
		
			JROdsExporter exporter = new JROdsExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleOdsReportConfiguration configuration = new SimpleOdsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			exporter.setConfiguration(configuration);
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". ODS export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void docx() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".docx");
		
			JRDocxExporter exporter = new JRDocxExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". DOCX export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void xlsx() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xlsx");
		
			JRXlsxExporter exporter = new JRXlsxExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setOnePagePerSheet(true);
			exporter.setConfiguration(configuration);
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". XLSX export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	public void pptx() throws JRException
	{
		JasperReportsContext jasperReportsContext = getJasperReportsContext();
		
		File[] files = getFiles(new File("build/reports"), "jrprint");
		for(int i = 0; i < files.length; i++)
		{
			long start = System.currentTimeMillis();
			File sourceFile = files[i];

			JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

			File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pptx");
		
			JRPptxExporter exporter = new JRPptxExporter(jasperReportsContext);
		
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
		
			exporter.exportReport();

			System.err.println("Report : " + sourceFile + ". PPTX export time : " + (System.currentTimeMillis() - start));
		}
	}
	
	
	/**
	 *
	 */
	private JasperReportsContext getJasperReportsContext() throws JRException
	{
		JasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();

		String scriptsDirectory = null;

		try
		{
			scriptsDirectory = new File(".", "build/classes/scripts").getCanonicalPath();
		}
		catch (IOException e)
		{
			throw new JRException(e);
		}

		String phantomJsPath = jasperReportsContext.getProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_EXECUTABLE_PATH);
		
		if (phantomJsPath == null)
		{
			phantomJsPath = System.getProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_EXECUTABLE_PATH);
			if (phantomJsPath != null)
			{
				System.out.println("Loading phantomjs from: " + phantomJsPath);
				jasperReportsContext.setProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_EXECUTABLE_PATH, phantomJsPath);
			}
			else
			{
				File guessedPath = new File("/usr/local/bin/phantomjs");
				if (guessedPath.exists())
				{
					System.out.println("Found phantomjs at " + guessedPath.getAbsolutePath());
					jasperReportsContext.setProperty(CVElementPhantomJSImageProvider.PROPERTY_PHANTOMJS_EXECUTABLE_PATH, guessedPath.getAbsolutePath());
				}
				else
				{
					System.out.println("No phantomjs property set, assuming it is in the path");
				}
			}
		}
		else
		{
			System.out.println("PhantomJs path defined in jasperreports.properties");
		}
				
		jasperReportsContext.setProperty(CVConstants.CV_REQUIREJS_PROPERTY, "file://" + scriptsDirectory + "/require.js");
		
		jasperReportsContext.setProperty("cv.keepTemporaryFiles", "true");
		
		return jasperReportsContext;
	}
}
