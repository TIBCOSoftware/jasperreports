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
package dori.jasper.engine.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRReport;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.export.JRGraphics2DExporter;
import dori.jasper.engine.export.JRGraphics2DExporterParameter;
import dori.jasper.engine.util.JRGraphEnvInitializer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPrinterAWT implements Printable
{


	/**
	 *
	 */
	private JasperPrint jasperPrint = null;
	private int pageOffset = 0;


	/**
	 *
	 */
	protected JRPrinterAWT(JasperPrint jrPrint) throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();
		
		jasperPrint = jrPrint;
	}


	/**
	 *
	 */
	public static boolean printPages(
		JasperPrint jrPrint,
		int firstPageIndex,
		int lastPageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		JRPrinterAWT printer = new JRPrinterAWT(jrPrint);
		return printer.printPages(
			firstPageIndex, 
			lastPageIndex, 
			withPrintDialog
			);
	}


	/**
	 *
	 */
	public static Image printPageToImage(
		JasperPrint jrPrint,
		int pageIndex,
		float zoom
		) throws JRException
	{
		JRPrinterAWT printer = new JRPrinterAWT(jrPrint);
		return printer.printPageToImage(pageIndex, zoom);
	}


	/**
	 *
	 */
	private boolean printPages(
		int firstPageIndex,
		int lastPageIndex,
		boolean withPrintDialog
		) throws JRException
	{
		boolean isOK = true;

		if (
			firstPageIndex < 0 ||
			firstPageIndex > lastPageIndex ||
			lastPageIndex > jasperPrint.getPages().size()
			)
		{
			throw new JRException(
				"Invalid page index range : " +
				firstPageIndex + " - " +
				lastPageIndex + " of " +
				jasperPrint.getPages().size()
				);
		}

		pageOffset = firstPageIndex;

		PrinterJob printJob = PrinterJob.getPrinterJob();
		PageFormat pageFormat = printJob.defaultPage();
		Paper paper = pageFormat.getPaper();

		switch (jasperPrint.getOrientation())
		{
			case JRReport.ORIENTATION_LANDSCAPE :
			{
				pageFormat.setOrientation(PageFormat.LANDSCAPE);
				paper.setSize(jasperPrint.getPageHeight(), jasperPrint.getPageWidth());
				paper.setImageableArea(
					0,
					0,
					jasperPrint.getPageHeight(),
					jasperPrint.getPageWidth()
					);
				break;
			}
			case JRReport.ORIENTATION_PORTRAIT :
			default :
			{
				pageFormat.setOrientation(PageFormat.PORTRAIT);
				paper.setSize(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
				paper.setImageableArea(
					0,
					0,
					jasperPrint.getPageWidth(),
					jasperPrint.getPageHeight()
					);
			}
		}

		pageFormat.setPaper(paper);

		Book book = new Book();
		book.append(this, pageFormat, lastPageIndex - firstPageIndex + 1);
		printJob.setPageable(book);
		try
		{
			if (withPrintDialog)
			{
				if (printJob.printDialog())
				{
					printJob.print();
				}
				else
				{
					isOK = false;
				}
			}
			else
			{
				printJob.print();
			}
		}
		catch (Exception ex)
		{
			throw new JRException("Error printing report.", ex);
		}

		return isOK;
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

		pageIndex += pageOffset;

		if ( pageIndex < 0 || pageIndex >= jasperPrint.getPages().size() )
		{
			return Printable.NO_SUCH_PAGE;
		}

		try
		{
			JRGraphics2DExporter exporter = new JRGraphics2DExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, this.jasperPrint);
			exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, (Graphics2D)graphics);
			exporter.setParameter(JRGraphics2DExporterParameter.PAGE_INDEX, new Integer(pageIndex));
			exporter.exportReport();
		}
		catch (JRException e)
		{
			e.printStackTrace();
			throw new PrinterException(e.getMessage());
		}

		return Printable.PAGE_EXISTS;
	}


	/**
	 *
	 */
	private Image printPageToImage(int pageIndex, float zoom) throws JRException
	{
		Image pageImage = new BufferedImage(
			(int)(jasperPrint.getPageWidth() * zoom) + 1,
			(int)(jasperPrint.getPageHeight() * zoom) + 1,
			BufferedImage.TYPE_INT_RGB
			);

		JRGraphics2DExporter exporter = new JRGraphics2DExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, this.jasperPrint);
		exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, (Graphics2D)pageImage.getGraphics());
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, new Float(zoom));
		exporter.exportReport();

		return pageImage;
	}


}
