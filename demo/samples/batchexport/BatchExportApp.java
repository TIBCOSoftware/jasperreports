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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class BatchExportApp
{


	/**
	 *
	 */
	private static final String TASK_FILL = "fill";
	private static final String TASK_PDF = "pdf";
	private static final String TASK_HTML = "html";
	private static final String TASK_RTF = "rtf";
	private static final String TASK_XLS = "xls";
	private static final String TASK_CSV = "csv";
	
	
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
				JasperFillManager.fillReportToFile("Report1.jasper", null, new JREmptyDataSource(2));
				JasperFillManager.fillReportToFile("Report2.jasper", null, new JREmptyDataSource(2));
				JasperFillManager.fillReportToFile("Report3.jasper", null, new JREmptyDataSource(2));
				System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_PDF.equals(taskName))
			{
				List jasperPrintList = new ArrayList();
				jasperPrintList.add(JRLoader.loadObject("Report1.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report2.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report3.jrprint"));
				
				JRPdfExporter exporter = new JRPdfExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
				exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
				
				exporter.exportReport();
				
				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_HTML.equals(taskName))
			{
				List jasperPrintList = new ArrayList();
				jasperPrintList.add(JRLoader.loadObject("Report1.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report2.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report3.jrprint"));
				
				JRHtmlExporter exporter = new JRHtmlExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
				
				exporter.exportReport();
				
				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_RTF.equals(taskName))
			{
				List jasperPrintList = new ArrayList();
				jasperPrintList.add(JRLoader.loadObject("Report1.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report2.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report3.jrprint"));
				
				JRRtfExporter exporter = new JRRtfExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
				
				exporter.exportReport();

				System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XLS.equals(taskName))
			{
				List jasperPrintList = new ArrayList();
				jasperPrintList.add(JRLoader.loadObject("Report1.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report2.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report3.jrprint"));
				
				JRXlsExporter exporter = new JRXlsExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				
				exporter.exportReport();

				System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_CSV.equals(taskName))
			{
				List jasperPrintList = new ArrayList();
				jasperPrintList.add(JRLoader.loadObject("Report1.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report2.jrprint"));
				jasperPrintList.add(JRLoader.loadObject("Report3.jrprint"));
				
				JRCsvExporter exporter = new JRCsvExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
				
				exporter.exportReport();

				System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
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
		System.out.println( "BatchExportApp usage:" );
		System.out.println( "\tjava BatchExportApp -Ttask -Ffile" );
		System.out.println( "\tTasks : fill | pdf | html | rtf | xls | csv" );
	}


}
