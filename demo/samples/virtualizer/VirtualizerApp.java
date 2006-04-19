/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class VirtualizerApp
{

	/**
	 * 
	 */
	private static final String TASK_PRINT = "print";

	private static final String TASK_PDF = "pdf";
	
	private static final String TASK_XML = "xml";

	private static final String TASK_XML_EMBED = "xmlEmbed";

	private static final String TASK_HTML = "html";

	private static final String TASK_CSV = "csv";
	
	private static final String TASK_VIEW = "view";
	
	private static final String TASK_EXPORT = "export";

	/**
	 * 
	 */
	public static void main(String[] args)
	{
		String fileName = null;
		String outFileName = null;
		String taskName = null;
		

		if (args.length == 0)
		{
			usage();
			return;
		}

		int k = 0;
		while (args.length > k)
		{
			if (args[k].startsWith("-T"))
				taskName = args[k].substring(2);
			else if (args[k].startsWith("-F"))
				fileName = args[k].substring(2);
			else if (args[k].startsWith("-O"))
				outFileName = args[k].substring(2);

			k++;
		}

		try
		{
			// Virtualization works only with in memory JasperPrint objects.
			// All the operations will first fill the report and then export
			// the filled object.
			
			// creating the data source
			JRDataSource ds = new JREmptyDataSource(1000);
			
			// creating the virtualizer
			JRFileVirtualizer virtualizer = new JRFileVirtualizer(2, "tmp");
			
			// filling the report
			JasperPrint jasperPrint = fillReport(fileName, ds, virtualizer);


			if (TASK_PRINT.equals(taskName))
			{
				JasperPrintManager.printReport(jasperPrint, true);
			}
			else if (TASK_PDF.equals(taskName))
			{
				exportPDF(outFileName, jasperPrint);
			}
			else if (TASK_XML.equals(taskName))
			{
				exportXML(outFileName, jasperPrint, false);
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				exportXML(outFileName, jasperPrint, true);
			}
			else if (TASK_HTML.equals(taskName))
			{
				exportHTML(outFileName, jasperPrint);
			}
			else if (TASK_CSV.equals(taskName))
			{
				exportCSV(outFileName, jasperPrint);
			}		
			else if (TASK_EXPORT.equals(taskName))
			{
				exportPDF(outFileName + ".pdf", jasperPrint);
				exportXML(outFileName + ".jrpxml", jasperPrint, false);
				exportHTML(outFileName + ".html", jasperPrint);
				exportCSV(outFileName + ".csv", jasperPrint);
				
				// manually cleaning up
				virtualizer.cleanup();
			}
			else if (TASK_VIEW.equals(taskName))
			{
				JasperViewer.viewReport(jasperPrint, true);
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

	private static void exportCSV(String outFileName, JasperPrint jasperPrint) throws JRException
	{
		long start = System.currentTimeMillis();
		JRCsvExporter exporter = new JRCsvExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);

		exporter.exportReport();

		System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
	}

	private static void exportHTML(String outFileName, JasperPrint jasperPrint) throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToHtmlFile(jasperPrint, outFileName);
		System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
	}

	private static void exportXML(String outFileName, JasperPrint jasperPrint, boolean embedded) throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile(jasperPrint, outFileName, embedded);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}

	private static void exportPDF(String outFileName, JasperPrint jasperPrint) throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToPdfFile(jasperPrint, outFileName);
		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}
	


	private static JasperPrint fillReport(String fileName, JRDataSource dataSource, JRFileVirtualizer virtualizer) throws JRException
	{
		long start = System.currentTimeMillis();

		// Preparing parameters
		Map parameters = new HashMap();
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

		JasperPrint jasperPrint = JasperFillManager.fillReport(fileName, parameters, dataSource);
		
		virtualizer.setReadOnly(true);

		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
		return jasperPrint;
	}

	/**
	 * 
	 */
	private static void usage()
	{
		System.out.println("VirtualizerApp usage:");
		System.out.println("\tjava VirtualizerApp -Ttask -Ffile");
		System.out.println("\tTasks : print | pdf | xml | xmlEmbed | html | csv | export | view");
	}

}
