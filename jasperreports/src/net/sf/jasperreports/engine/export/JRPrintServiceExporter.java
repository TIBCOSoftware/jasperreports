/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
package net.sf.jasperreports.engine.export;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;


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

		printRequestAttributeSet = 
			(PrintRequestAttributeSet)parameters.get(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET);
		if (printRequestAttributeSet == null)
		{
			printRequestAttributeSet = new HashPrintRequestAttributeSet();
		}

		AttributeSet attributeSet = new HashAttributeSet();
		attributeSet.addAll(printRequestAttributeSet);

		printServiceAttributeSet = 
			(PrintServiceAttributeSet)parameters.get(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET);
		if (printServiceAttributeSet != null)
		{
			attributeSet.addAll(printServiceAttributeSet);
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
		printerJob.setPrintable(this);
		
		//PrintService[] services = PrintServiceLookup.lookupPrintServices(docFlavor, attributeSet);
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, attributeSet);

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
							(float)jasperPrint.getPageWidth() / 72f,
							(float)jasperPrint.getPageHeight() / 72f,
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
		
		exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, (Graphics2D)graphics);
		exporter.setParameter(JRGraphics2DExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		
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
