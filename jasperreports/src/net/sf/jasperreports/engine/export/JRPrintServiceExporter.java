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
package dori.jasper.engine.export;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;

import dori.jasper.engine.JRAbstractExporter;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.util.JRLoader;


/**
 *
 */
public class JRPrintServiceExporter extends JRAbstractExporter implements Printable
{


	/**
	 *
	 */
	private JasperPrint jasperPrint = null;
	private JRGraphics2DExporter exporter = null;
	private PrintRequestAttributeSet printRequestAttributeSet = null;
	private PrintServiceAttributeSet printServiceAttributeSet = null;
	//private DocFlavor docFlavor = null;
	private boolean displayPageDialog = false;
	private boolean displayPrintDialog = false;


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		this.jasperPrint = (JasperPrint)this.parameters.get(JRExporterParameter.JASPER_PRINT);
		if (jasperPrint == null)
		{
			InputStream is = (InputStream)this.parameters.get(JRExporterParameter.INPUT_STREAM);
			if (is != null)
			{
				this.jasperPrint = (JasperPrint)JRLoader.loadObject(is);
			}
			else
			{
				URL url = (URL)this.parameters.get(JRExporterParameter.INPUT_URL);
				if (url != null)
				{
					this.jasperPrint = (JasperPrint)JRLoader.loadObject(url);
				}
				else
				{
					File file = (File)this.parameters.get(JRExporterParameter.INPUT_FILE);
					if (file != null)
					{
						this.jasperPrint = (JasperPrint)JRLoader.loadObject(file);
					}
					else
					{
						String fileName = (String)this.parameters.get(JRExporterParameter.INPUT_FILE_NAME);
						if (fileName != null)
						{
							this.jasperPrint = (JasperPrint)JRLoader.loadObject(fileName);
						}
						else
						{
							throw new JRException("No input source supplied to the exporter.");
						}
					}
				}
			}
		}

		this.exporter = new JRGraphics2DExporter();
		this.exporter.setParameter(JRExporterParameter.JASPER_PRINT, this.jasperPrint);

		this.printRequestAttributeSet = 
			(PrintRequestAttributeSet)this.parameters.get(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET);
		if (this.printRequestAttributeSet == null)
		{
			this.printRequestAttributeSet = new HashPrintRequestAttributeSet();
		}

		AttributeSet attributeSet = new HashAttributeSet();
		attributeSet.addAll(this.printRequestAttributeSet);

		this.printServiceAttributeSet = 
			(PrintServiceAttributeSet)this.parameters.get(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET);
		if (this.printRequestAttributeSet != null)
		{
			attributeSet.addAll(this.printServiceAttributeSet);
		}

		//this.docFlavor = (DocFlavor)this.parameters.get(JRPrintServiceExporterParameter.DOC_FLAVOR);
		//if (this.docFlavor == null)
		//{
		//
		//}
		
		Boolean pageDialog = (Boolean)this.parameters.get(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG);
		if (pageDialog != null)
		{
			this.displayPageDialog = pageDialog.booleanValue();
		}

		Boolean printDialog = (Boolean)this.parameters.get(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG);
		if (printDialog != null)
		{
			this.displayPrintDialog = printDialog.booleanValue();
		}

		PrinterJob printerJob = PrinterJob.getPrinterJob();       
		printerJob.setPrintable(this);
		
		//PrintService[] services = PrintServiceLookup.lookupPrintServices(this.docFlavor, attributeSet);
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, attributeSet);

		if (services.length > 0) 
		{
			try 
			{
				printerJob.setPrintService(services[0]);

				if (!this.printRequestAttributeSet.containsKey(MediaPrintableArea.class))
				{
					this.printRequestAttributeSet.add(
						new MediaPrintableArea(
							0f, 
							0f, 
							(float)this.jasperPrint.getPageWidth() / 72f,
							(float)this.jasperPrint.getPageHeight() / 72f,
							MediaPrintableArea.INCH
							)
						);
				}

				if (this.displayPageDialog)
				{
					printerJob.pageDialog(this.printRequestAttributeSet);
				}
				
				if (this.displayPrintDialog)
				{
					if (printerJob.printDialog(this.printRequestAttributeSet))
					{
						printerJob.print(this.printRequestAttributeSet);
					}
				}
				else
				{
					printerJob.print(this.printRequestAttributeSet);
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
		
		this.exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, (Graphics2D)graphics);
		this.exporter.setParameter(JRGraphics2DExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		
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
