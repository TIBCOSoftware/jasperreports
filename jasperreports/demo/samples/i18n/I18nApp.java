/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import net.sf.jasperreports.view.*;
import java.util.*;
import java.io.*;

import javax.swing.JOptionPane;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class I18nApp
{


	/**
	 *
	 */
	private static final String TASK_COMPILE = "compile";
	private static final String TASK_FILL = "fill";
	private static final String TASK_FILL_DEFAULT = "fillDefault";
	private static final String TASK_VIEW = "view";
	private static final String TASK_PDF = "pdf";
	private static final String TASK_XML = "xml";
	private static final String TASK_XML_EMBED = "xmlEmbed";
	private static final String TASK_HTML = "html";
	private static final String TASK_XLS = "xls";
	private static final String TASK_CSV = "csv";
	
	
	/**
	 *
	 */
	public static void main(String[] args)
	{
		if(args.length != 2)
		{
			usage();
			return;
		}

		String taskName = args[0];
		String fileName = args[1];

		try
		{
			if (TASK_COMPILE.equals(taskName))
			{
				long start = System.currentTimeMillis();
				JasperCompileManager.compileReportToFile(fileName);
				System.err.println("Compile time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_FILL.equals(taskName))
			{
				Locale locale = chooseLocale();
				if (locale != null)
				{
					Map parameters = new HashMap();
					parameters.put("number", new Integer((int)(1000 * Math.random())));
					parameters.put(JRParameter.REPORT_LOCALE, locale);
					long start = System.currentTimeMillis();
					JasperFillManager.fillReportToFile(fileName, parameters, new JREmptyDataSource());
					System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				}
				System.exit(0);
			}
			else if (TASK_FILL_DEFAULT.equals(taskName))
			{
				Map parameters = new HashMap();
				parameters.put("number", new Integer((int)(1000 * Math.random())));
				long start = System.currentTimeMillis();
				JasperFillManager.fillReportToFile(fileName, parameters, new JREmptyDataSource());
				System.err.println("Filling time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_VIEW.equals(taskName))
			{
				Locale locale = chooseLocale();
				if (locale != null)
				{
					Locale.setDefault(locale);
					JasperViewer.viewReport(fileName, false, true);
				}
				else
				{
					System.exit(0);
				}
			}
			else if (TASK_PDF.equals(taskName))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToPdfFile(fileName);
				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XML.equals(taskName))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToXmlFile(fileName, false);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToXmlFile(fileName, true);
				System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_HTML.equals(taskName))
			{
				long start = System.currentTimeMillis();
				JasperExportManager.exportReportToHtmlFile(fileName);
				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XLS.equals(taskName))
			{
				long start = System.currentTimeMillis();

				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
				JRXlsExporter exporter = new JRXlsExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				
				exporter.exportReport();

				System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_CSV.equals(taskName))
			{
				long start = System.currentTimeMillis();

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
	private static Locale chooseLocale()
	{
		Locale[] locales = 
			new Locale[]
		   {
				Locale.GERMANY,
				Locale.US,
				Locale.FRANCE,
				new Locale("pt", "PT"),
				new Locale("ro", "RO")
		   };
									  
		return 
			(Locale)JOptionPane.showInputDialog(
				null,
				"Choose the locale",
				"Locale",
				JOptionPane.PLAIN_MESSAGE,
				null,
				locales,
				null
				);
	}


	/**
	 *
	 */
	private static void usage()
	{
		System.out.println( "I18nApp usage:" );
		System.out.println( "\tjava I18nApp task file" );
		System.out.println( "\tTasks : compile | fill | fillDefault | view | pdf | xml | xmlEmbed | html | xls | csv" );
	}


}
