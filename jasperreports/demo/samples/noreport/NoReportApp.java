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
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.base.JRBasePrintLine;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class NoReportApp
{


	/**
	 *
	 */
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
				JasperPrint jasperPrint = getJasperPrint();
				JRSaver.saveObject(jasperPrint, fileName);
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
				JasperExportManager.exportReportToPdfFile(fileName);
				System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_RTF.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
				
				JRRtfExporter exporter = new JRRtfExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
				exporter.exportReport();

				System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
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
				JasperExportManager.exportReportToHtmlFile(fileName);
				System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_XLS.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
				
				JRXlsExporter exporter = new JRXlsExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				
				exporter.exportReport();

				System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
				System.exit(0);
			}
			else if (TASK_JXL.equals(taskName))
			{
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");

				JExcelApiExporter exporter = new JExcelApiExporter();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);

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
			else if (TASK_ODT.equals(taskName))
			{
				File sourceFile = new File(fileName);
		
				JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		
				File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
				
				JROdtExporter exporter = new JROdtExporter();
				
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
				
				exporter.exportReport();

				System.err.println("ODT creation time : " + (System.currentTimeMillis() - start));
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
		System.out.println( "NoReportApp usage:" );
		System.out.println( "\tjava NoReportApp -Ttask -Ffile" );
		System.out.println( "\tTasks : fill | print | pdf | xml | xmlEmbed | html | rtf | xls | jxl | csv | odt" );
	}


	/**
	 *
	 */
	private static JasperPrint getJasperPrint() throws JRException
	{
		//JasperPrint
		JasperPrint jasperPrint = new JasperPrint();
		jasperPrint.setName("NoReport");
		jasperPrint.setPageWidth(595);
		jasperPrint.setPageHeight(842);
		
		//Fonts
		JRDesignStyle normalStyle = new JRDesignStyle();
		normalStyle.setName("Arial_Normal");
		normalStyle.setDefault(true);
		normalStyle.setFontName("Arial");
		normalStyle.setFontSize(8);
		normalStyle.setPdfFontName("Helvetica");
		normalStyle.setPdfEncoding("Cp1252");
		normalStyle.setPdfEmbedded(false);
		jasperPrint.addStyle(normalStyle);

		JRDesignStyle boldStyle = new JRDesignStyle();
		boldStyle.setName("Arial_Bold");
		boldStyle.setFontName("Arial");
		boldStyle.setFontSize(8);
		boldStyle.setBold(true);
		boldStyle.setPdfFontName("Helvetica-Bold");
		boldStyle.setPdfEncoding("Cp1252");
		boldStyle.setPdfEmbedded(false);
		jasperPrint.addStyle(boldStyle);

		JRDesignStyle italicStyle = new JRDesignStyle();
		italicStyle.setName("Arial_Italic");
		italicStyle.setFontName("Arial");
		italicStyle.setFontSize(8);
		italicStyle.setItalic(true);
		italicStyle.setPdfFontName("Helvetica-Oblique");
		italicStyle.setPdfEncoding("Cp1252");
		italicStyle.setPdfEmbedded(false);
		jasperPrint.addStyle(italicStyle);
		
		JRPrintPage page = new JRBasePrintPage();

		JRPrintLine line = new JRBasePrintLine(jasperPrint.getDefaultStyleProvider());
		line.setX(40);
		line.setY(50);
		line.setWidth(515);
		line.setHeight(0);
		page.addElement(line);
		
		JRPrintImage image = new JRBasePrintImage(jasperPrint.getDefaultStyleProvider());
		image.setX(45);
		image.setY(55);
		image.setWidth(165);
		image.setHeight(40);
		image.setScaleImage(JRImage.SCALE_IMAGE_CLIP);
		image.setRenderer(
			JRImageRenderer.getInstance(
				JRLoader.loadBytesFromLocation("jasperreports.gif")
				)
			);
		page.addElement(image);

		JRPrintText text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
		text.setX(210);
		text.setY(55);
		text.setWidth(345);
		text.setHeight(30);
		text.setTextHeight(text.getHeight());
		text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
		text.setLineSpacingFactor(1.3133681f);
		text.setLeadingOffset(-4.955078f);
		text.setStyle(boldStyle);
		text.setFontSize(18);
		text.setText("JasperReports Project Description");
		page.addElement(text);

		text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
		text.setX(210);
		text.setY(85);
		text.setWidth(325);
		text.setHeight(15);
		text.setTextHeight(text.getHeight());
		text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
		text.setLineSpacingFactor(1.329241f);
		text.setLeadingOffset(-4.076172f);
		text.setStyle(italicStyle);
		text.setFontSize(12);
		text.setText((new SimpleDateFormat("EEE, MMM d, yyyy")).format(new Date()));
		page.addElement(text);

		text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
		text.setX(40);
		text.setY(150);
		text.setWidth(515);
		text.setHeight(200);
		text.setTextHeight(text.getHeight());
		text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED);
		text.setLineSpacingFactor(1.329241f);
		text.setLeadingOffset(-4.076172f);
		text.setStyle(normalStyle);
		text.setFontSize(14);
		text.setText(
			"JasperReports is a powerful report-generating tool that has the ability to deliver rich content onto the screen, to the printer or into PDF, HTML, XLS, CSV or XML files.\n\n" +
			"It is entirely written in Java and can be used in a variety of Java enabled applications, including J2EE or Web applications, to generate dynamic content.\n\n" +
			"Its main purpose is to help creating page oriented, ready to print documents in a simple and flexible manner."
			);
		page.addElement(text);

		jasperPrint.addPage(page);

		return jasperPrint;
	}
	

}
