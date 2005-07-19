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

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
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
	protected JRExportProgressMonitor progressMonitor = null;
	protected PrintRequestAttributeSet printRequestAttributeSet = null;
	protected PrintServiceAttributeSet printServiceAttributeSet = null;
	//protected DocFlavor docFlavor = null;
	protected boolean displayPageDialog = false;
	protected boolean displayPrintDialog = false;


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		/*   */
		setInput();

		/*   */
		setPageRange();

		exporter = new JRGraphics2DExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, progressMonitor);
		exporter.setParameter(JRExporterParameter.OFFSET_X, parameters.get(JRExporterParameter.OFFSET_X));
		exporter.setParameter(JRExporterParameter.OFFSET_Y, parameters.get(JRExporterParameter.OFFSET_Y));

		printRequestAttributeSet = 
			(PrintRequestAttributeSet)parameters.get(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET);
		if (printRequestAttributeSet == null)
		{
			printRequestAttributeSet = new HashPrintRequestAttributeSet();
		}

		printServiceAttributeSet = 
			(PrintServiceAttributeSet)parameters.get(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET);
		if (printServiceAttributeSet == null)
		{
			printServiceAttributeSet = new HashPrintServiceAttributeSet();
		}

		//docFlavor = (DocFlavor)parameters.get(JRPrintServiceExporterParameter.DOC_FLAVOR);
		//if (docFlavor == null)
		//{
		//
		//}
		
		Boolean pageDialog = (Boolean)parameters.get(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG);
		if (pageDialog != null)
		{
			displayPageDialog = pageDialog.booleanValue();
		}

		Boolean printDialog = (Boolean)parameters.get(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG);
		if (printDialog != null)
		{
			displayPrintDialog = printDialog.booleanValue();
		}

		PrinterJob printerJob = PrinterJob.getPrinterJob();

		// fix for bug ID 6255588 from Sun bug database
		JRPrinterAWT.initPrinterJobFields(printerJob);

		printerJob.setPrintable(this);
		
		//PrintService[] services = PrintServiceLookup.lookupPrintServices(docFlavor, attributeSet);
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);

		if (services.length > 0) 
		{
			try 
			{
				printerJob.setPrintService(services[0]);

				if (!printRequestAttributeSet.containsKey(MediaPrintableArea.class))
				{
					printRequestAttributeSet.add(
						new MediaPrintableArea(
							0f, 
							0f, 
							jasperPrint.getPageWidth() / 72f,
							jasperPrint.getPageHeight() / 72f,
							MediaPrintableArea.INCH
							)
						);
				}

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
		else
		{
			throw new JRException("No suitable print service found.");
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


}
