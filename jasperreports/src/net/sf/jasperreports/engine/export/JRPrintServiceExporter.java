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
package net.sf.jasperreports.engine.export;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.print.JRPrinterAWT;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrintServiceExporter extends JRAbstractExporter implements Printable
{


	/**
	 *
	 */
	protected JRGraphics2DExporter exporter = null;
	protected boolean displayPageDialog = false;
	protected boolean displayPageDialogOnlyOnce = false;
	protected boolean displayPrintDialog = false;
	protected boolean displayPrintDialogOnlyOnce = false;

	protected int reportIndex = 0;
	
	private PrintService printService = null;
	private Boolean[] printStatus = null;
	
	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		/*   */
		setOffset();

		try
		{
			/*   */
			setExportContext();
	
			/*   */
			setInput();

			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}
	
			PrintServiceAttributeSet printServiceAttributeSet = 
				(PrintServiceAttributeSet)parameters.get(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET);
			if (printServiceAttributeSet == null)
			{
				printServiceAttributeSet = new HashPrintServiceAttributeSet();
			}

			Boolean pageDialog = (Boolean)parameters.get(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG);
			if (pageDialog != null)
			{
				displayPageDialog = pageDialog.booleanValue();
			}
	
			Boolean pageDialogOnlyOnce = (Boolean)parameters.get(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG_ONLY_ONCE);
			if (displayPageDialog && pageDialogOnlyOnce != null)
			{
				// it can be (eventually) set to true only if displayPageDialog is true
				displayPageDialogOnlyOnce = pageDialogOnlyOnce.booleanValue();
			}
	
			Boolean printDialog = (Boolean)parameters.get(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG);
			if (printDialog != null)
			{
				displayPrintDialog = printDialog.booleanValue();
			}
	
			Boolean printDialogOnlyOnce = (Boolean)parameters.get(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG_ONLY_ONCE);
			if (displayPrintDialog && printDialogOnlyOnce != null)
			{
//				 it can be (eventually) set to true only if displayPrintDialog is true
				displayPrintDialogOnlyOnce = printDialogOnlyOnce.booleanValue();
			}
			PrinterJob printerJob = PrinterJob.getPrinterJob();
			
			JRPrinterAWT.initPrinterJobFields(printerJob);
			
			printerJob.setPrintable(this);
			
			printStatus = null;
			
			// determining the print service only once
			printService = (PrintService) parameters.get(JRPrintServiceExporterParameter.PRINT_SERVICE);
			if (printService == null) {
				PrintService[] services = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);
				if (services.length > 0)
					printService = services[0];
			}
			
			if (printService == null)
			{
				throw new JRException("No suitable print service found.");
			}

			try 
			{
				printerJob.setPrintService(printService);
			}
			catch (PrinterException e) 
			{ 
				throw new JRException(e);
			}

			PrintRequestAttributeSet printRequestAttributeSet = null;
			if(displayPrintDialogOnlyOnce || displayPageDialogOnlyOnce)
			{
				printRequestAttributeSet = new HashPrintRequestAttributeSet();
				setDefaultPrintRequestAttributeSet(printRequestAttributeSet);
				setOrientation((JasperPrint)jasperPrintList.get(0), printRequestAttributeSet);
				if(displayPageDialogOnlyOnce)
				{
					if(printerJob.pageDialog(printRequestAttributeSet) == null)
						return;
					else
						displayPageDialog = false;
				}
				if(displayPrintDialogOnlyOnce)
				{
					if(!printerJob.printDialog(printRequestAttributeSet))
					{
						printStatus = new Boolean[]{Boolean.FALSE};
						return;
					}
					else
					{
						displayPrintDialog = false;
					}
				}
			}
			
			List status = new ArrayList();
			// fix for bug ID artf1455 from jasperforge.org bug database
			for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
			{
				setJasperPrint((JasperPrint)jasperPrintList.get(reportIndex));

				exporter = new JRGraphics2DExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, parameters.get(JRExporterParameter.PROGRESS_MONITOR));
				exporter.setParameter(JRExporterParameter.OFFSET_X, parameters.get(JRExporterParameter.OFFSET_X));
				exporter.setParameter(JRExporterParameter.OFFSET_Y, parameters.get(JRExporterParameter.OFFSET_Y));
				exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, parameters.get(JRGraphics2DExporterParameter.ZOOM_RATIO));
				exporter.setParameter(JRExporterParameter.CLASS_LOADER, classLoader);
				exporter.setParameter(JRExporterParameter.URL_HANDLER_FACTORY, urlHandlerFactory);
				exporter.setParameter(JRExporterParameter.FILE_RESOLVER, fileResolver);
				if (parameters.containsKey(JRExporterParameter.FILTER))
				{
					exporter.setParameter(JRExporterParameter.FILTER, filter);
				}
				exporter.setParameter(JRGraphics2DExporterParameter.MINIMIZE_PRINTER_JOB_SIZE, parameters.get(JRGraphics2DExporterParameter.MINIMIZE_PRINTER_JOB_SIZE));
				
				if(displayPrintDialog || displayPageDialog ||
						(!displayPrintDialogOnlyOnce && !displayPageDialogOnlyOnce))
				{
					printRequestAttributeSet = new HashPrintRequestAttributeSet();
					setDefaultPrintRequestAttributeSet(printRequestAttributeSet);
					setOrientation(jasperPrint, printRequestAttributeSet);
				}
		
				try 
				{
					
					if (!isModeBatch)
					{
						printRequestAttributeSet.add(new PageRanges(startPageIndex + 1, endPageIndex + 1));
					}

					printerJob.setJobName("JasperReports - " + jasperPrint.getName());

					if (displayPageDialog)
					{
						printerJob.pageDialog(printRequestAttributeSet);
					}
					if (displayPrintDialog)
					{
						if (printerJob.printDialog(printRequestAttributeSet))
						{
							status.add(Boolean.TRUE);
							printerJob.print(printRequestAttributeSet);
						}
						else
						{
							status.add(Boolean.FALSE);
						}
					}
					else
					{
						printerJob.print(printRequestAttributeSet);
					}
				}
				catch (PrinterException e) 
				{ 
					throw new JRException(e);
				}
			}
			
			printStatus = (Boolean[]) status.toArray(new Boolean[status.size()]);
			printService = printerJob.getPrintService();
		}
		finally
		{
			resetExportContext();
		}
	}


	/**
	 *
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
		if (Thread.currentThread().isInterrupted())
		{
			throw new PrinterException("Current thread interrupted.");
		}

		if ( pageIndex < 0 || pageIndex >= jasperPrint.getPages().size() )
		{
			return Printable.NO_SUCH_PAGE;
		}
		
		exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, graphics);
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		
		try
		{
			exporter.exportReport();
		}
		catch (JRException e)
		{
			throw new PrinterException(e.getMessage());
		}

		return Printable.PAGE_EXISTS;
	}


	private void setOrientation(JasperPrint jPrint,PrintRequestAttributeSet printRequestAttributeSet)
	{
		if (!printRequestAttributeSet.containsKey(MediaPrintableArea.class))
		{
			int printableWidth;
			int printableHeight;
			switch (jPrint.getOrientation())
			{
				case JRReport.ORIENTATION_LANDSCAPE:
					printableWidth = jPrint.getPageHeight();
					printableHeight = jPrint.getPageWidth();
					break;
				default:
					printableWidth = jPrint.getPageWidth();
					printableHeight = jPrint.getPageHeight();
					break;
			}
			
			printRequestAttributeSet.add(
				new MediaPrintableArea(
					0f, 
					0f, 
					printableWidth / 72f,
					printableHeight / 72f,
					MediaPrintableArea.INCH
					)
				);
		}

		if (!printRequestAttributeSet.containsKey(OrientationRequested.class))
		{
			OrientationRequested orientation;
			switch (jPrint.getOrientation())
			{
				case JRReport.ORIENTATION_LANDSCAPE:
					orientation = OrientationRequested.LANDSCAPE;
					break;
				default:
					orientation = OrientationRequested.PORTRAIT;
					break;
			}
			printRequestAttributeSet.add(orientation);
		}
		
	}
	
	private void setDefaultPrintRequestAttributeSet(PrintRequestAttributeSet printRequestAttributeSet)
	{
		PrintRequestAttributeSet printRequestAttributeSetParam = 
			(PrintRequestAttributeSet)parameters.get(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET);
		if (printRequestAttributeSetParam != null)
		{
			printRequestAttributeSet.addAll(printRequestAttributeSetParam);
		}
	}

	// artf1936
	public static boolean checkAvailablePrinters() 
	{
		PrintService[] ss = java.awt.print.PrinterJob.lookupPrintServices();
		for (int i=0;i<ss.length;i++) {
			Attribute[] att = ss[i].getAttributes().toArray();
			for (int j=0;j<att.length;j++) {
				if (att[j].equals(PrinterIsAcceptingJobs.ACCEPTING_JOBS)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns an array of <tt>java.lang.Boolean</tt> values, one for each appearence of the print dialog during the last export operation.
	 * A Boolean.TRUE value in this array means that for that particular occurrence of the print dialog, the OK button was hit. 
	 * A Boolean.FALSE value means the respective print dialog was cancelled.
	 */
	public Boolean[] getPrintStatus() 
	{
		return printStatus;
	}

	/**
	 * Returns the {@link PrintService} instance used by the exporter last time the exportReport() method was run.
	 */
	public PrintService getPrintService() 
	{
		return printService;
	}
	
}
