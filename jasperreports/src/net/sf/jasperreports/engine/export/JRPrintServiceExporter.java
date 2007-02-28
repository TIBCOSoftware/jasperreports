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

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;

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
			
			// determining the print service only once
			PrintService selectedService = (PrintService) parameters.get(JRPrintServiceExporterParameter.PRINT_SERVICE);
			if (selectedService == null) {
				PrintService[] services = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);
				if (services.length > 0)
					selectedService = services[0];
			}
			
			if (selectedService == null)
			{
				throw new JRException("No suitable print service found.");
			}

			try 
			{
				printerJob.setPrintService(selectedService);
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
						return;
					else
						displayPrintDialog = false;
				}
			}
			
			// fix for bug ID artf1455 from jasperforge.org bug database
			for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
			{
				jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);

				exporter = new JRGraphics2DExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, parameters.get(JRExporterParameter.PROGRESS_MONITOR));
				exporter.setParameter(JRExporterParameter.OFFSET_X, parameters.get(JRExporterParameter.OFFSET_X));
				exporter.setParameter(JRExporterParameter.OFFSET_Y, parameters.get(JRExporterParameter.OFFSET_Y));
				exporter.setParameter(JRExporterParameter.CLASS_LOADER, classLoader);
				exporter.setParameter(JRExporterParameter.URL_HANDLER_FACTORY, urlHandlerFactory);
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
							printerJob.print(printRequestAttributeSet);
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
}
