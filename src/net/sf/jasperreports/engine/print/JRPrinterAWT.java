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
package net.sf.jasperreports.engine.print;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.util.JRGraphEnvInitializer;


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
			lastPageIndex >= jasperPrint.getPages().size()
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

		// fix for bug ID 6255588 from Sun bug database
		initPrinterJobFields(printJob);
		
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
			exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, graphics);
			exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
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
		exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, pageImage.getGraphics());
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, new Float(zoom));
		exporter.exportReport();

		return pageImage;
	}


	/**
	 * Fix for bug ID 6255588 from Sun bug database
	 * @param job print job that the fix applies to
	 */
	public static void initPrinterJobFields(PrinterJob job)
	{
		Class klass = job.getClass();
		try {
			Class printServiceClass = Class.forName("javax.print.PrintService");
			Method method = klass.getMethod("getPrintService", (Class[])null);
			Object printService = method.invoke(job, (Object[])null);
			method = klass.getMethod("setPrintService", new Class[]{printServiceClass});
			method.invoke(job, new Object[] {printService});
		} catch (NoSuchMethodException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (ClassNotFoundException e) {
		}
	}
}
