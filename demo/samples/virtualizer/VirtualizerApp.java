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
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
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
			JRDataSource ds = new GeneratedDataSource((byte) 5,(byte) 3, 200);
			
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

	private static JasperPrint fillReport(String fileName, JRDataSource dataSource, JRFileVirtualizer virtualizer) throws JRException, ClassNotFoundException, SQLException
	{
		long start = System.currentTimeMillis();

		// Preparing parameters
		Image image = Toolkit.getDefaultToolkit().createImage("dukesign.jpg");
		MediaTracker traker = new MediaTracker(new Panel());
		traker.addImage(image, 0);
		try
		{
			traker.waitForID(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Map parameters = new HashMap();
		parameters.put("ReportTitle", "The First Jasper Report Ever");
		parameters.put("MaxOrderID", new Integer(10500));
		parameters.put("SummaryImage", image);
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
		System.out.println("JasperApp usage:");
		System.out.println("\tjava JasperApp -Ttask -Ffile");
		System.out.println("\tTasks : fill | print | pdf | xml | xmlEmbed | html | xls | csv | run");
	}

	private static class GeneratedDataSource implements JRDataSource 
	{
		private final char[] country;
		private final int ordersPerCountry;
		private final Random r;
		private final char lastLetter;
		
		private static final char firstLetter = 'A'; 
		
		private int orderCount;

		/**
		 * @param noOfLetters the number of letters (starting form 'A') to be used for generated country names 
		 * @param countryNameLength the length of a generated country name
		 * @param ordersPerCountry how many orders to generate per country
		 */
		GeneratedDataSource (byte noOfLetters, byte countryNameLength, int ordersPerCountry)
		{
			this.lastLetter = (char) (firstLetter + noOfLetters - 1);
			this.country = new char[countryNameLength];
			
			this.ordersPerCountry = ordersPerCountry;
			this.r = new Random(System.currentTimeMillis());
			
			init();
		}
		
		private void init()
		{
			orderCount = 0;
			
			for (int i = 0; i < country.length; i++)
			{
				country[i] = 'A';
			}
		}

		public boolean next() throws JRException
		{
			if (++orderCount <= ordersPerCountry)
			{
				return true;
			}
			
			int i;
			for (i = country.length - 1; i >=0 && country[i] == lastLetter; --i);
			
			if (i >= 0)
			{
				++country[i];
				for(++i; i < country.length; ++i)
				{
					country[i] = firstLetter;
				}
				
				orderCount = 1;
				
				return true;
			}
			
			return false;
		}

		public Object getFieldValue(JRField jrField) throws JRException
		{
			String name = jrField.getName();
			if (name.equals("ShippedDate"))
			{
				return null;
			}
			else if (name.equals("ShipCountry"))
			{
				return String.copyValueOf(country);
			}
			else if (name.equals("RequiredDate"))
			{
				return null;
			}
			else if (name.equals("CustomerID"))
			{
				return null;
			}
			else if (name.equals("OrderID"))
			{
				return new Integer(orderCount);
			}
			else if (name.equals("ShipName"))
			{
				return "Ship" + orderCount;
			}
			else if (name.equals("ShipVia"))
			{
				return null;
			}
			else if (name.equals("ShipPostalCode"))
			{
				return null;
			}
			else if (name.equals("OrderDate"))
			{
				return new Timestamp(System.currentTimeMillis());
			}
			else if (name.equals("ShipCity"))
			{
				return "SF";
			}
			else if (name.equals("ShipAddress"))
			{
				return null;
			}
			else if (name.equals("EmployeeID"))
			{
				return null;
			}
			else if (name.equals("ShipRegion"))
			{
				return null;
			}
			else if (name.equals("Freight"))
			{
				return new Double(r.nextDouble());
			}
			else
			{
				throw new RuntimeException("Wrong field " + name);
			}
		}
	}
}
