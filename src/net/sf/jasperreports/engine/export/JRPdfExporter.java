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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import dori.jasper.engine.JRAbstractExporter;
import dori.jasper.engine.JRAlignment;
import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRGraphicElement;
import dori.jasper.engine.JRHyperlink;
import dori.jasper.engine.JRImage;
import dori.jasper.engine.JRLine;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintEllipse;
import dori.jasper.engine.JRPrintImage;
import dori.jasper.engine.JRPrintLine;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintRectangle;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.base.JRBaseFont;
import dori.jasper.engine.util.JRImageLoader;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.util.JRStyledText;
import dori.jasper.engine.util.JRStyledTextParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPdfExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	private static final String EMPTY_STRING = "";


	/**
	 *
	 */
	protected Document document = null;
	protected PdfContentByte pdfContentByte = null;

	protected Document imageTesterDocument = null;
	protected PdfContentByte imageTesterPdfContentByte = null;

	protected JRExportProgressMonitor progressMonitor = null;

	/**
	 *
	 */
	protected boolean isEncrypted = false;
	protected boolean is128BitKey = false;
	protected String userPassword = null;
	protected String ownerPassword = null;
	protected int permissions = 0;

	/**
	 *
	 */
	protected JRFont defaultFont = null;

	/**
	 *
	 */
	protected JRStyledTextParser styledTextParser = new JRStyledTextParser();


	/**
	 *
	 */
	protected JRFont getDefaultFont()
	{
		if (defaultFont == null)
		{
			defaultFont = jasperPrint.getDefaultFont();
			if (defaultFont == null)
			{
				defaultFont = new JRBaseFont();
			}
		}

		return defaultFont;
	}


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setInput();

		/*   */
		setPageRange();

		Boolean isEncryptedParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_ENCRYPTED);
		if (isEncryptedParameter != null)
		{
			isEncrypted = isEncryptedParameter.booleanValue();
		}
		
		Boolean is128BitKeyParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_128_BIT_KEY);
		if (is128BitKeyParameter != null)
		{
			is128BitKey = is128BitKeyParameter.booleanValue();
		}
		
		userPassword = (String)parameters.get(JRPdfExporterParameter.USER_PASSWORD);
		ownerPassword = (String)parameters.get(JRPdfExporterParameter.OWNER_PASSWORD);

		Integer permissionsParameter = (Integer)parameters.get(JRPdfExporterParameter.PERMISSIONS);
		if (permissionsParameter != null)
		{
			permissions = permissionsParameter.intValue();
		}

		OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
		if (os != null)
		{
			exportReportToStream(os);
		}
		else
		{
			File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
			if (destFile == null)
			{
				String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
				if (fileName != null)
				{
					destFile = new File(fileName);
				}
				else
				{
					throw new JRException("No output specified for the exporter.");
				}
			}

			try
			{
				os = new FileOutputStream(destFile);
				exportReportToStream(os);
				os.flush();
			}
			catch (IOException e)
			{
				throw new JRException("Error trying to export to file : " + destFile, e);
			}
			finally
			{
				if (os != null)
				{
					try
					{
						os.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		}
	}


	/**
	 *
	 */
	protected void exportReportToStream(OutputStream os) throws JRException
	{
		//ByteArrayOutputStream baos = new ByteArrayOutputStream();

		document =
			new Document(
				new Rectangle(
					jasperPrint.getPageWidth(),
					jasperPrint.getPageHeight()
				)
			);

		imageTesterDocument =
			new Document(
				new Rectangle(
					10, //jasperPrint.getPageWidth(),
					10 //jasperPrint.getPageHeight()
				)
			);

		try
		{
			PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
			pdfWriter.setCloseStream(false);

			if (isEncrypted)
			{
				pdfWriter.setEncryption(
					is128BitKey, 
					userPassword, 
					ownerPassword, 
					permissions
					);
			}

			document.open();

			PdfWriter imageTesterPdfWriter = 
				PdfWriter.getInstance(
					imageTesterDocument, 
					new ByteArrayOutputStream()
					);
			imageTesterDocument.open();
			imageTesterDocument.newPage();
		 	imageTesterPdfContentByte = imageTesterPdfWriter.getDirectContent();
		 	imageTesterPdfContentByte.setLiteral("\n");

			List pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				Chunk chunk = null;
				ColumnText colText = null;
				JRPrintPage page = null;
				for(int i = startPageIndex; i <= endPageIndex; i++)
				{
					if (Thread.currentThread().isInterrupted())
					{
						throw new JRException("Current thread interrupted.");
					}
				
					page = (JRPrintPage)pages.get(i);

					document.newPage();

		 			pdfContentByte = pdfWriter.getDirectContent();

					pdfContentByte.setLineCap(2);

					chunk = new Chunk(" ");
					chunk.setLocalDestination("JR_PAGE_ANCHOR_" + (i + 1));

					colText = new ColumnText(pdfContentByte);
					colText.setSimpleColumn(
						new Phrase(chunk),
						0,
						jasperPrint.getPageHeight(),
						1,
						1,
						0,
						Element.ALIGN_LEFT
						);

					colText.go();

					/*   */
					exportPage(page);
				}
			}
			else
			{
				document.newPage();
				pdfContentByte = pdfWriter.getDirectContent();
				pdfContentByte.setLiteral("\n");
			}
		}
		catch(DocumentException e)
		{
			throw new JRException("PDF Document error : " + jasperPrint.getName(), e);
		}
		catch(IOException e)
		{
			throw new JRException("Error generating PDF report : " + jasperPrint.getName(), e);
		}
		finally
		{
			document.close();
			imageTesterDocument.close();
		}

		//return os.toByteArray();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, DocumentException, IOException
	{
		JRPrintElement element = null;
		Collection elements = page.getElements();
		if (elements != null && elements.size() > 0)
		{
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement)it.next();

				if (element instanceof JRPrintLine)
				{
					exportLine((JRPrintLine)element);
				}
				else if (element instanceof JRPrintRectangle)
				{
					exportRectangle((JRPrintRectangle)element);
				}
				else if (element instanceof JRPrintEllipse)
				{
					exportEllipse((JRPrintEllipse)element);
				}
				else if (element instanceof JRPrintImage)
				{
					exportImage((JRPrintImage)element);
				}
				else if (element instanceof JRPrintText)
				{
					exportText((JRPrintText)element);
				}
			}
		}
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}
	

	/**
	 *
	 */
	protected void exportLine(JRPrintLine line)
	{
		if (line.getPen() != JRGraphicElement.PEN_NONE)
		{
			pdfContentByte.setRGBColorStroke(
				line.getForecolor().getRed(),
				line.getForecolor().getGreen(),
				line.getForecolor().getBlue()
				);
	
			float borderCorrection = 0f;

			switch (line.getPen())
			{
				case JRGraphicElement.PEN_DOTTED :
				{
					borderCorrection = 0f;
					pdfContentByte.setLineWidth(1f);
					pdfContentByte.setLineDash(5f, 3f, 0f);
					break;
				}
				case JRGraphicElement.PEN_4_POINT :
				{
					borderCorrection = 0f;
					pdfContentByte.setLineWidth(4f);
					pdfContentByte.setLineDash(0f);
					break;
				}
				case JRGraphicElement.PEN_2_POINT :
				{
					borderCorrection = 0f;
					pdfContentByte.setLineWidth(2f);
					pdfContentByte.setLineDash(0f);
					break;
				}
				case JRGraphicElement.PEN_NONE :
				{
					//never reached due to the initial if statement
					break;
				}
				case JRGraphicElement.PEN_THIN :
				{
					borderCorrection = 0.25f;
					pdfContentByte.setLineWidth(0.5f);
					pdfContentByte.setLineDash(0f);
					break;
				}
				case JRGraphicElement.PEN_1_POINT :
				default :
				{
					borderCorrection = 0f;
					pdfContentByte.setLineWidth(1f);
					pdfContentByte.setLineDash(0f);
					break;
				}
			}
			
			int x1 = 0;
			int y1 = 0;
			int x2 = 0;
			int y2 = 0;

			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				if (line.getWidth() > 1)
				{
					if (line.getHeight() > 1)
					{
						x1 = -1;
						y1 = 1;
						x2 = 1;
						y2 = -1;
					}
					else
					{
						x1 = -1;
						y1 = 1;
						x2 = 1;
						y2 = 1;
					}
				}
				else
				{
					if (line.getHeight() > 1)
					{
						x1 = -1;
						y1 = 1;
						x2 = -1;
						y2 = -1;
					}
					else
					{
						x1 = -1;
						y1 = 1;
						x2 = -1;
						y2 = 1;
					}
				}

				pdfContentByte.moveTo(
					line.getX() + x1 * borderCorrection,
					jasperPrint.getPageHeight() - line.getY() + y1 * borderCorrection
					);
				pdfContentByte.lineTo(
					line.getX() + line.getWidth() + x2 * borderCorrection - 1,
					jasperPrint.getPageHeight() - line.getY() - line.getHeight() + y2 * borderCorrection + 1
					);
			}
			else
			{
				if (line.getWidth() > 1)
				{
					if (line.getHeight() > 1)
					{
						x1 = -1;
						y1 = -1;
						x2 = 1;
						y2 = 1;
					}
					else
					{
						x1 = -1;
						y1 = -1;
						x2 = 1;
						y2 = -1;
					}
				}
				else
				{
					if (line.getHeight() > 1)
					{
						x1 = 1;
						y1 = -1;
						x2 = 1;
						y2 = 1;
					}
					else
					{
						x1 = 1;
						y1 = -1;
						x2 = 1;
						y2 = -1;
					}
				}

				pdfContentByte.moveTo(
					line.getX() + x1 * borderCorrection, 
					jasperPrint.getPageHeight() - line.getY() - line.getHeight() + y1 * borderCorrection + 1
					);
				pdfContentByte.lineTo(
					line.getX() + line.getWidth() + x2 * borderCorrection - 1,
					jasperPrint.getPageHeight() - line.getY() + y2 * borderCorrection
					);
			}
		
			pdfContentByte.stroke();
		}
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle)
	{
		pdfContentByte.setRGBColorStroke(
			rectangle.getForecolor().getRed(),
			rectangle.getForecolor().getGreen(),
			rectangle.getForecolor().getBlue()
			);
		pdfContentByte.setRGBColorFill(
			rectangle.getBackcolor().getRed(),
			rectangle.getBackcolor().getGreen(),
			rectangle.getBackcolor().getBlue()
			);

		float borderCorrection = 0f;

		switch (rectangle.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(1f);
				pdfContentByte.setLineDash(5f, 3f, 0f);
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(4f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(2f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				borderCorrection = 0.25f;
				pdfContentByte.setLineWidth(0.5f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				borderCorrection = 0.5f;
				pdfContentByte.setLineWidth(0f);
				pdfContentByte.setLineDash(0f);

				pdfContentByte.setRGBColorStroke(
					rectangle.getBackcolor().getRed(),
					rectangle.getBackcolor().getGreen(),
					rectangle.getBackcolor().getBlue()
					);

				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(1f);
				pdfContentByte.setLineDash(0f);
				break;
			}
		}

		if (rectangle.getMode() == JRElement.MODE_OPAQUE)
		{
			pdfContentByte.roundRectangle(
				rectangle.getX() - borderCorrection,
				jasperPrint.getPageHeight() - rectangle.getY() - rectangle.getHeight() - borderCorrection + 1,
				rectangle.getWidth() + 2 * borderCorrection - 1,
				rectangle.getHeight() + 2 * borderCorrection - 1,
				rectangle.getRadius()
				);

			pdfContentByte.fillStroke();
		}
		else
		{
			if (rectangle.getPen() != JRGraphicElement.PEN_NONE)
			{
				pdfContentByte.roundRectangle(
					rectangle.getX() - borderCorrection,
					jasperPrint.getPageHeight() - rectangle.getY() - rectangle.getHeight() - borderCorrection + 1,
					rectangle.getWidth() + 2 * borderCorrection - 1,
					rectangle.getHeight() + 2 * borderCorrection - 1,
					rectangle.getRadius()
					);

				pdfContentByte.stroke();
			}
		}
	}


	/**
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse)
	{
		pdfContentByte.setRGBColorStroke(
			ellipse.getForecolor().getRed(),
			ellipse.getForecolor().getGreen(),
			ellipse.getForecolor().getBlue()
			);
		pdfContentByte.setRGBColorFill(
			ellipse.getBackcolor().getRed(),
			ellipse.getBackcolor().getGreen(),
			ellipse.getBackcolor().getBlue()
			);

		float borderCorrection = 0f;

		switch (ellipse.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(1f);
				pdfContentByte.setLineDash(5f, 3f, 0f);
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(4f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(2f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				borderCorrection = 0.25f;
				pdfContentByte.setLineWidth(0.5f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				borderCorrection = 0.5f;
				pdfContentByte.setLineWidth(0f);
				pdfContentByte.setLineDash(0f);

				pdfContentByte.setRGBColorStroke(
					ellipse.getBackcolor().getRed(),
					ellipse.getBackcolor().getGreen(),
					ellipse.getBackcolor().getBlue()
					);

				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				borderCorrection = 0f;
				pdfContentByte.setLineWidth(1f);
				pdfContentByte.setLineDash(0f);
				break;
			}
		}

		if (ellipse.getMode() == JRElement.MODE_OPAQUE)
		{
			pdfContentByte.ellipse(
				ellipse.getX() - borderCorrection,
				jasperPrint.getPageHeight() - ellipse.getY() - ellipse.getHeight() - borderCorrection + 1,
				ellipse.getX() + ellipse.getWidth() + borderCorrection - 1,
				jasperPrint.getPageHeight() - ellipse.getY() + borderCorrection
				);

			pdfContentByte.fillStroke();
		}
		else
		{
			if (ellipse.getPen() != JRGraphicElement.PEN_NONE)
			{
				pdfContentByte.ellipse(
					ellipse.getX() - borderCorrection,
					jasperPrint.getPageHeight() - ellipse.getY() - ellipse.getHeight() - borderCorrection + 1,
					ellipse.getX() + ellipse.getWidth() + borderCorrection - 1,
					jasperPrint.getPageHeight() - ellipse.getY() + borderCorrection
					);

				pdfContentByte.stroke();
			}
		}
	}


	/**
	 *
	 */
	protected void exportImage(JRPrintImage printImage) throws DocumentException, IOException
	{
		pdfContentByte.setRGBColorFill(
			printImage.getBackcolor().getRed(),
			printImage.getBackcolor().getGreen(),
			printImage.getBackcolor().getBlue()
			);

		int borderOffset = 0;
		float borderCorrection = 0f;
		float lineWidth = 1f;
		boolean isLineDotted = false;

		switch (printImage.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderOffset = 0;
				borderCorrection = 0f;
				lineWidth = 1f;
				isLineDotted = true;
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderOffset = 2;
				borderCorrection = 0f;
				lineWidth = 4f;
				isLineDotted = false;
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderOffset = 1;
				borderCorrection = 0f;
				lineWidth = 2f;
				isLineDotted = false;
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				borderOffset = 0;
				borderCorrection = 0.25f;
				lineWidth = 0.5f;
				isLineDotted = false;
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				borderOffset = 0;
				borderCorrection = 0.5f;
				lineWidth = 1f;
				isLineDotted = false;

				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				borderOffset = 0;
				borderCorrection = 0f;
				lineWidth = 1f;
				isLineDotted = false;
				break;
			}
		}

		if (printImage.getMode() == JRElement.MODE_OPAQUE)
		{
			pdfContentByte.setRGBColorStroke(
				printImage.getBackcolor().getRed(),
				printImage.getBackcolor().getGreen(),
				printImage.getBackcolor().getBlue()
				);
			pdfContentByte.setLineWidth(0.1f);
			pdfContentByte.setLineDash(0f);
			pdfContentByte.rectangle(
				printImage.getX() - borderCorrection,
				jasperPrint.getPageHeight() - printImage.getY() + borderCorrection,
				printImage.getWidth() + 2 * borderCorrection - 1,
				- printImage.getHeight() - 2 * borderCorrection + 1
				);
			pdfContentByte.fillStroke();
		}


		int availableImageWidth = printImage.getWidth() - 2 * borderOffset;
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = printImage.getHeight() - 2 * borderOffset;
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;

		int xoffset = 0;
		int yoffset = 0;

		if (
			printImage.getImageData() != null &&
			availableImageWidth > 0 && 
			availableImageHeight > 0
			)
		{
			//java.awt.Image awtImage = JRImageLoader.loadImage(printImage.getImageData());

			//com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(awtImage, printImage.getBackcolor());
			//com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(awtImage, null);
			com.lowagie.text.Image image = null;

			float xalignFactor = 0f;
			switch (printImage.getHorizontalAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
				{
					xalignFactor = 1f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER :
				{
					xalignFactor = 0.5f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_LEFT :
				default :
				{
					xalignFactor = 0f;
					break;
				}
			}

			float yalignFactor = 0f;
			switch (printImage.getVerticalAlignment())
			{
				case JRAlignment.VERTICAL_ALIGN_BOTTOM :
				{
					yalignFactor = 1f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_MIDDLE :
				{
					yalignFactor = 0.5f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_TOP :
				default :
				{
					yalignFactor = 0f;
					break;
				}
			}

			switch(printImage.getScaleImage())
			{
				case JRImage.SCALE_IMAGE_CLIP :
				{
					java.awt.Image awtImage = JRImageLoader.loadImage(printImage.getImageData());
					//image = com.lowagie.text.Image.getInstance(awtImage, null);

					int awtWidth = awtImage.getWidth(null);
					int awtHeight = awtImage.getHeight(null);

					xoffset = (int)(xalignFactor * (availableImageWidth - awtWidth));
					yoffset = (int)(yalignFactor * (availableImageHeight - awtHeight));

					int minWidth = Math.min(awtWidth, availableImageWidth);
					int minHeight = Math.min(awtHeight, availableImageHeight);
					
					BufferedImage bi = 
						new BufferedImage(minWidth, minHeight, BufferedImage.TYPE_INT_RGB);

					Graphics g = bi.getGraphics();
					g.setColor(printImage.getBackcolor());
					g.fillRect(0, 0, minWidth, minHeight);
					g.drawImage(
						awtImage, 
						(xoffset > 0 ? 0 : xoffset), 
						(yoffset > 0 ? 0 : yoffset), 
						null
						);

					xoffset = (xoffset < 0 ? 0 : xoffset);
					yoffset = (yoffset < 0 ? 0 : yoffset);

					//awtImage = bi.getSubimage(0, 0, minWidth, minHeight);
					awtImage = bi;

					//image = com.lowagie.text.Image.getInstance(awtImage, printImage.getBackcolor());
					image = com.lowagie.text.Image.getInstance(awtImage, null);

					break;
				}
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					try
					{
						image = com.lowagie.text.Image.getInstance(printImage.getImageData());
						imageTesterPdfContentByte.addImage(image, 10, 0, 0, 10, 0, 0);
					}
					catch(Exception e)
					{
						java.awt.Image awtImage = JRImageLoader.loadImage(printImage.getImageData());
						image = com.lowagie.text.Image.getInstance(awtImage, null);
					}
					image.scaleAbsolute(availableImageWidth, availableImageHeight);
					break;
				}
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
				default :
				{
					try
					{
						image = com.lowagie.text.Image.getInstance(printImage.getImageData());
						imageTesterPdfContentByte.addImage(image, 10, 0, 0, 10, 0, 0);
					}
					catch(Exception e)
					{
						java.awt.Image awtImage = JRImageLoader.loadImage(printImage.getImageData());
						image = com.lowagie.text.Image.getInstance(awtImage, null);
					}
					image.scaleToFit(availableImageWidth, availableImageHeight);
					
					xoffset = (int)(xalignFactor * (availableImageWidth - image.plainWidth()));
					yoffset = (int)(yalignFactor * (availableImageHeight - image.plainHeight()));

					xoffset = (xoffset < 0 ? 0 : xoffset);
					yoffset = (yoffset < 0 ? 0 : yoffset);

					break;
				}
			}

			/*
			image.setAbsolutePosition(
				printImage.getX() + borderOffset,
				jasperPrint.getPageHeight() - printImage.getY() - image.scaledHeight() - borderOffset
				);

			pdfContentByte.addImage(image);
			*/

			Chunk chunk = new Chunk(image, -0.5f, 0.5f);

			if (printImage.getAnchorName() != null)
			{
				chunk.setLocalDestination(printImage.getAnchorName());
			}

			switch(printImage.getHyperlinkType())
			{
				case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
				{
					if (printImage.getHyperlinkReference() != null)
					{
						chunk.setAnchor(printImage.getHyperlinkReference());
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
				{
					if (printImage.getHyperlinkAnchor() != null)
					{
						chunk.setLocalGoto(printImage.getHyperlinkAnchor());
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
				{
					if (printImage.getHyperlinkPage() != null)
					{
						chunk.setLocalGoto("JR_PAGE_ANCHOR_" + printImage.getHyperlinkPage().toString());
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
				{
					if (
						printImage.getHyperlinkReference() != null &&
						printImage.getHyperlinkAnchor() != null
						)
					{
						chunk.setRemoteGoto(
							printImage.getHyperlinkReference(),
							printImage.getHyperlinkAnchor()
							);
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
				{
					if (
						printImage.getHyperlinkReference() != null &&
						printImage.getHyperlinkPage() != null
						)
					{
						chunk.setRemoteGoto(
							printImage.getHyperlinkReference(),
							printImage.getHyperlinkPage().intValue()
							);
					}
					break;
				}
				case JRHyperlink.HYPERLINK_TYPE_NONE :
				default :
				{
					break;
				}
			}

			ColumnText colText = new ColumnText(pdfContentByte);
			colText.setSimpleColumn(
				new Phrase(chunk),
				printImage.getX() + xoffset + borderOffset,
				jasperPrint.getPageHeight() - printImage.getY() - image.scaledHeight() - yoffset - borderOffset,
				printImage.getX() + xoffset + borderOffset + image.scaledWidth(),
				jasperPrint.getPageHeight() - printImage.getY() - yoffset - borderOffset,
				image.scaledHeight(),
				Element.ALIGN_LEFT
				);

			colText.go();
		}


		if (printImage.getPen() != JRGraphicElement.PEN_NONE)
		{
			pdfContentByte.setRGBColorStroke(
				printImage.getForecolor().getRed(),
				printImage.getForecolor().getGreen(),
				printImage.getForecolor().getBlue()
				);

			pdfContentByte.setLineWidth(lineWidth);

			if (isLineDotted)
			{
				pdfContentByte.setLineDash(5f, 3f, 0f);
			}
			else
			{
				pdfContentByte.setLineDash(0f);
			}

			pdfContentByte.rectangle(
				printImage.getX() - borderCorrection,
				jasperPrint.getPageHeight() - printImage.getY() + borderCorrection,
				printImage.getWidth() + 2 * borderCorrection - 1,
				- printImage.getHeight() - 2 * borderCorrection + 1
				);

			pdfContentByte.stroke();
		}
	}


	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		JRStyledText styledText = null;

		String text = textElement.getText();
		if (text != null)
		{
			JRFont font = textElement.getFont();
			if (font == null)
			{
				font = getDefaultFont();
			}

			Map attributes = new HashMap(); 
			attributes.putAll(font.getAttributes());
			attributes.put(TextAttribute.FOREGROUND, textElement.getForecolor());
			if (textElement.getMode() == JRElement.MODE_OPAQUE)
			{
				attributes.put(TextAttribute.BACKGROUND, textElement.getBackcolor());
			}

			if (textElement.isStyledText())
			{
				try
				{
					styledText = styledTextParser.parse(attributes, text);
				}
				catch (SAXException e)
				{
					//ignore if invalid styled text and treat like normal text
				}
			}
		
			if (styledText == null)
			{
				styledText = new JRStyledText();
				styledText.append(text);
				styledText.addRun(new JRStyledText.Run(attributes, 0, text.length()));
			}
		}
		
		return styledText;
	}


	/**
	 * 
	 */
	protected Chunk getHyperlinkInfoChunk(JRPrintText text)
	{
		Chunk chunk = new Chunk(EMPTY_STRING);
		
		if (text.getAnchorName() != null)
		{
			chunk.setLocalDestination(text.getAnchorName());
		}

		switch(text.getHyperlinkType())
		{
			case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
			{
				if (text.getHyperlinkReference() != null)
				{
					chunk.setAnchor(text.getHyperlinkReference());
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
			{
				if (text.getHyperlinkAnchor() != null)
				{
					chunk.setLocalGoto(text.getHyperlinkAnchor());
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
			{
				if (text.getHyperlinkPage() != null)
				{
					chunk.setLocalGoto("JR_PAGE_ANCHOR_" + text.getHyperlinkPage().toString());
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
			{
				if (
					text.getHyperlinkReference() != null &&
					text.getHyperlinkAnchor() != null
					)
				{
					chunk.setRemoteGoto(
						text.getHyperlinkReference(),
						text.getHyperlinkAnchor()
						);
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
			{
				if (
					text.getHyperlinkReference() != null &&
					text.getHyperlinkPage() != null
					)
				{
					chunk.setRemoteGoto(
						text.getHyperlinkReference(),
						text.getHyperlinkPage().intValue()
						);
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_NONE :
			default :
			{
				break;
			}
		}
		
		return chunk;
	}
	

	/**
	 *
	 */
	protected Phrase getPhrase(JRStyledText styledText, Chunk hyperlinkInfoChunk) throws JRException, DocumentException, IOException
	{
		Phrase phrase = new Phrase();

		String text = styledText.getText();
		
		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
		
		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			Chunk chunk = getChunk(iterator.getAttributes(), text.substring(iterator.getIndex(), runLimit));
			chunk.setMarkupAttributes(hyperlinkInfoChunk.getMarkupAttributes());
			phrase.add(chunk);

			iterator.setIndex(runLimit);
		}

		return phrase;
	}


	/**
	 *
	 */
	protected Chunk getChunk(Map attributes, String text) throws JRException, DocumentException, IOException
	{
		JRFont jrFont = new JRBaseFont(attributes);
		
		BaseFont baseFont = null;
		Exception initialException = null;

		try
		{
			baseFont =
				BaseFont.createFont(
					jrFont.getPdfFontName(),
					jrFont.getPdfEncoding(),
					jrFont.isPdfEmbedded(),
					true,
					null,
					null
					);
		}
		catch(Exception e)
		{
			initialException = e;
		}

		if (baseFont == null)
		{
			byte[] bytes = null;

			try
			{
				bytes = JRLoader.loadBytesFromLocation(jrFont.getPdfFontName());
			}
			catch(JRException e)
			{
				throw 
					new JRException(
						"Could not load the following font : " 
						+ "\npdfFontName   : " + jrFont.getPdfFontName() 
						+ "\npdfEncoding   : " + jrFont.getPdfEncoding() 
						+ "\nisPdfEmbedded : " + jrFont.isPdfEmbedded(), 
						initialException
						);
			}

			baseFont =
				BaseFont.createFont(
					jrFont.getPdfFontName(),
					jrFont.getPdfEncoding(),
					jrFont.isPdfEmbedded(),
					true,
					bytes,
					null
					);
		}

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		Color backcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		/*
		if (forecolor == null)
		{
			forecolor = Color.black;
		}
		*/
		
		Font font =
			new Font(
				baseFont,
				(float)jrFont.getSize(),
				//((jrFont.isBold())?Font.BOLD:0) +
				//((jrFont.isItalic())?Font.ITALIC:0) +
				(jrFont.isUnderline() ? Font.UNDERLINE : 0) 
					| (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0),
				forecolor
				);

		Chunk chunk = new Chunk(text, font);
		
		if (backcolor != null)
		{
			chunk.setBackground(backcolor);
		}
		
		return chunk;
	}


	/**
	 *
	 */
	protected void exportText(JRPrintText text) throws JRException, DocumentException, IOException
	{
		JRStyledText styledText = getStyledText(text);

		if (styledText == null)
		{
			return;
		}

		int textLength = styledText.length();
		
		int x = text.getX();
		int y = text.getY();
		int width = text.getWidth();
		int height = text.getHeight();
		
		double angle = 0;
		
		switch (text.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				y = text.getY() + text.getHeight();
				width = text.getHeight();
				height = text.getWidth();
				angle = Math.PI / 2;
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				x = text.getX() + text.getWidth();
				width = text.getHeight();
				height = text.getWidth();
				angle = - Math.PI / 2;
				break;
			}
			case JRTextElement.ROTATION_NONE :
			default :
			{
			}
		}
		
		AffineTransform atrans = new AffineTransform();
		atrans.rotate(angle, x, jasperPrint.getPageHeight() - y);
		pdfContentByte.transform(atrans);
		
		if (text.getMode() == JRElement.MODE_OPAQUE)
		{
			pdfContentByte.setRGBColorStroke(
				text.getBackcolor().getRed(),
				text.getBackcolor().getGreen(),
				text.getBackcolor().getBlue()
				);
			pdfContentByte.setRGBColorFill(
				text.getBackcolor().getRed(),
				text.getBackcolor().getGreen(),
				text.getBackcolor().getBlue()
				);
			pdfContentByte.setLineWidth(1f);
			pdfContentByte.setLineDash(0f);
			pdfContentByte.rectangle(
				x,
				jasperPrint.getPageHeight() - y,
				width - 1,
				- height + 1
				);
			pdfContentByte.fillStroke();
		}
		else
		{
			/*
			pdfContentByte.setRGBColorStroke(
				text.getForecolor().getRed(),
				text.getForecolor().getGreen(),
				text.getForecolor().getBlue()
				);
			pdfContentByte.setLineWidth(0.1f);
			pdfContentByte.setLineDash(0f);
			pdfContentByte.rectangle(
				text.getX(),
				jasperPrint.getPageHeight() - text.getY(),
				text.getWidth(),
				- text.getHeight()
				);
			pdfContentByte.stroke();
			*/
		}

		if (textLength == 0)
		{
			return;
		}

		int horizontalAlignment = Element.ALIGN_LEFT;
		switch (text.getTextAlignment())
		{
			case JRAlignment.HORIZONTAL_ALIGN_LEFT :
			{
				horizontalAlignment = Element.ALIGN_LEFT;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_CENTER :
			{
				horizontalAlignment = Element.ALIGN_CENTER;
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
			{
				horizontalAlignment = Element.ALIGN_RIGHT; 
				break;
			}
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED : 
			{
				horizontalAlignment = Element.ALIGN_JUSTIFIED; 
				break;
			}
			default : 
			{
				horizontalAlignment = Element.ALIGN_LEFT; 
			}
		}

		float verticalOffset = 0f;
		switch (text.getVerticalAlignment())
		{
			case JRTextElement.VERTICAL_ALIGN_TOP :
			{
				verticalOffset = 0f;
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_MIDDLE :
			{
				verticalOffset = ((float)height - text.getTextHeight()) / 2f;
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_BOTTOM :
			{
				verticalOffset = height - text.getTextHeight();
				break;
			}
			default :
			{
				verticalOffset = 0f;
			}
		}

		ColumnText colText = new ColumnText(pdfContentByte);
		colText.setSimpleColumn(
			getPhrase(styledText, getHyperlinkInfoChunk(text)),
			x, 
			jasperPrint.getPageHeight() 
				- y 
				- verticalOffset
				- text.getLeadingOffset(), 
				//+ text.getLineSpacingFactor() * text.getFont().getSize(), 
			x + width, 
			jasperPrint.getPageHeight() 
				- y 
				- height,
			0,//text.getLineSpacingFactor(),// * text.getFont().getSize(),
			horizontalAlignment
			);

		colText.setLeading(0, text.getLineSpacingFactor());// * text.getFont().getSize());

		colText.go();

		atrans = new AffineTransform();
		atrans.rotate(-angle, x, jasperPrint.getPageHeight() - y);
		pdfContentByte.transform(atrans);
	}

		
}
