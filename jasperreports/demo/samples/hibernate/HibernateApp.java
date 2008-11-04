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
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
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
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class HibernateApp
{


	private static final String TASK_FILL = "fill";
	private static final String TASK_PRINT = "print";
	private static final String TASK_PDF = "pdf";
	private static final String TASK_RTF = "rtf";
	private static final String TASK_XML = "xml";
	private static final String TASK_XML_EMBED = "xmlEmbed";
	private static final String TASK_HTML = "html";
	private static final String TASK_XLS = "xls";
	private static final String TASK_JXL = "jxl";
	private static final String TASK_CSV = "csv";
	private static final String TASK_ODT = "odt";
	private static final String TASK_RUN = "run";
	
	
	private static final String[] reportNames = {
		"AddressesReport",
		"HibernateQueryReport"
	};
	
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			usage();
			return;
		}
				
		String taskName = args[0];
		String fileName = args[1];

		try
		{
			if (TASK_FILL.equals(taskName))
			{
				Session session = createSession();
				Transaction transaction = session.beginTransaction();

				Map params = getParameters(session);
				
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperFillManager.fillReportToFile(reportNames[i] + ".jasper", params);
					System.err.println("Report : " + reportNames[i] + ". Filling time : " + (System.currentTimeMillis() - start));
				}
				
				transaction.rollback();
				session.close();
			}
			else if (TASK_PRINT.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperPrintManager.printReport(reportNames[i] + ".jrprint", true);
					System.err.println("Report : " + reportNames[i] + ". Printing time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_PDF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToPdfFile(reportNames[i] + ".jrprint");
					System.err.println("Report : " + reportNames[i] + ". PDF creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_RTF.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
				
					JRRtfExporter exporter = new JRRtfExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". RTF creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_XML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToXmlFile(reportNames[i] + ".jrprint", false);
					System.err.println("Report : " + reportNames[i] + ". XML creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_XML_EMBED.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToXmlFile(reportNames[i] + ".jrprint", true);
					System.err.println("Report : " + reportNames[i] + ". XML creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_HTML.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperExportManager.exportReportToHtmlFile(reportNames[i] + ".jrprint");
					System.err.println("Report : " + reportNames[i] + ". HTML creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_XLS.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
					JRXlsExporter exporter = new JRXlsExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". XLS creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_JXL.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					File sourceFile = new File(fileName);

					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");

					JExcelApiExporter exporter = new JExcelApiExporter();

					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);

					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". XLS creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_CSV.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
				
					JRCsvExporter exporter = new JRCsvExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". CSV creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_ODT.equals(taskName))
			{
				for(int i = 0; i < reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					File sourceFile = new File(reportNames[i] + ".jrprint");
		
					JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
					File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
				
					JROdtExporter exporter = new JROdtExporter();
				
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
					exporter.exportReport();

					System.err.println("Report : " + reportNames[i] + ". ODT creation time : " + (System.currentTimeMillis() - start));
				}
			}
			else if (TASK_RUN.equals(taskName))
			{
				Session session = createSession();
				Transaction transaction = session.beginTransaction();
				
				Map params = getParameters(session);
				for(int i = 0; i< reportNames.length; i++)
				{
					long start = System.currentTimeMillis();
					JasperRunManager.runReportToPdfFile(reportNames[i] + ".jrprint", params);
					System.err.println("Report : " + reportNames[i] + ". PDF running time : " + (System.currentTimeMillis() - start));
				}

				transaction.rollback();
				session.close();
			}
			else
			{
				usage();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static Map getParameters(Session session)
	{
		Map parameters = new HashMap();
		parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
		parameters.put("ReportTitle", "Address Report");
		List cityFilter = new ArrayList(3);
		cityFilter.add("Boston");
		cityFilter.add("Chicago");
		cityFilter.add("Oslo");
		parameters.put("CityFilter", cityFilter);
		parameters.put("OrderClause", "city");
		return parameters;
	}

	private static Session createSession()
	{
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		
		return sessionFactory.openSession();
	}

	private static void usage()
	{
		System.out.println( "HibernateApp usage:" );
		System.out.println( "\tjava HibernateApp task file" );
		System.out.println( "\tTasks : compile | fill | fillIgnorePagination | print | pdf | xml | xmlEmbed | html | rtf | xls | jxl | csv | odt | run" );
	}
}
