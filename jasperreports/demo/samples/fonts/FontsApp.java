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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
import java.io.File;
import java.util.HashMap;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class FontsApp
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
	private static final String TASK_CSV = "csv";
	private static final String TASK_RUN = "run";
	
	
	/**
	 *
	 */
	public static void main(String[] args)
	{
		String fileName = null;
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
			if ( args[k].startsWith("-F") )
				fileName = args[k].substring(2);
			
			k++;	
		}

		try
		{
			long start = System.currentTimeMillis();
			if (TASK_FILL.equals(taskName))
			{
				JasperFillManager.fillReportToFile(fileName, null, new JREmptyDataSource());
				System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_PRINT.equals(taskName))
			{
				JasperPrintManager.printReport(fileName, true);
				System.err.println("Printing time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_PDF.equals(taskName))
			{
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pdf");

				JRPdfExporter exporter = new JRPdfExporter();

				HashMap fontMap = new HashMap();
				FontKey key = new FontKey("sansserif", false, true);
				PdfFont font = new PdfFont("COMICBD.TTF", "Cp1252", false);
				fontMap.put(key, font);

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRExporterParameter.FONT_MAP, fontMap);

				exporter.exportReport();

				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XML.equals(taskName))
			{
				JasperExportManager.exportReportToXmlFile(fileName, false);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				JasperExportManager.exportReportToXmlFile(fileName, true);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_HTML.equals(taskName))
			{
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".html");

				JRHtmlExporter exporter = new JRHtmlExporter();

				HashMap fontMap = new HashMap();
				fontMap.put("sansserif", "Arial, Verdana, Tahoma");
				fontMap.put("serif", "Times New Roman");
				fontMap.put("monospaced", "Courier, Courier New");

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRExporterParameter.FONT_MAP, fontMap);

				exporter.exportReport();

				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_RTF.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
				
				HashMap fontMap = new HashMap();
				fontMap.put("sansserif", "Arial");
				fontMap.put("serif", "Times New Roman");
				fontMap.put("monospaced", "Courier");

				JRRtfExporter exporter = new JRRtfExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRExporterParameter.FONT_MAP, fontMap);
				
				exporter.exportReport();

				System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XLS.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
				JRXlsExporter exporter = new JRXlsExporter();
				
				HashMap fontMap = new HashMap();
				fontMap.put("sansserif", "Arial");
				fontMap.put("serif", "Times New Roman");
				fontMap.put("monospaced", "Courier");

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporter.setParameter(JRExporterParameter.FONT_MAP, fontMap);

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
				System.exit(0);
			}
			else if (TASK_RUN.equals(taskName))
			{
				JasperRunManager.runReportToPdfFile(fileName, null, new JREmptyDataSource());
				System.err.println("PDF running time : " + (System.currentTimeMillis() - start));
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
		System.out.println( "FontsApp usage:" );
		System.out.println( "\tjava FontsApp -Ttask -Ffile" );
		System.out.println( "\tTasks : fill | print | pdf | xml | xmlEmbed | html | rtf | xls | csv | run" );
	}


}
