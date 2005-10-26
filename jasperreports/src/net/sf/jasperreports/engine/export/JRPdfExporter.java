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

/*
 * Contributors:
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 * Ling Li - lonecatz@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintAnchor;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Exports a JasperReports document to PDF format. It has binary output type and exports the document to
 * a free-form layout.
 * <p>
 * Since classic AWT fonts can be sometimes very different from PDF fonts, a font mapping feature was added.
 * By using the {@link JRExporterParameter#FONT_MAP} parameter, a logical font like "sansserif" can be mapped
 * to a system specific font, like "Helvetica-BoldOblique". PDF font mapping is a little more complicated, because
 * for a logical font, PDF cand provide two or more fonts, from the same family but with different styles (like
 * "Helvetica", "Helvetica-Bold", "Helvetica-BoldOblique"). So every key in the map is a simple bean containing
 * font family, bold and italic flag, and every value is another bean containing the PDF font name, encoding and
 * embedding flag.
 * @see FontKey
 * @see PdfFont
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPdfExporter extends JRAbstractExporter
{

	private static final String EMPTY_BOOKMARK_TITLE = "";

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	protected static boolean fontsRegistered = false;

	/**
	 *
	 */
	protected Document document = null;
	protected PdfContentByte pdfContentByte = null;

	protected Document imageTesterDocument = null;
	protected PdfContentByte imageTesterPdfContentByte = null;

	protected JRExportProgressMonitor progressMonitor = null;

	protected int reportIndex = 0;

	/**
	 *
	 */
	protected boolean isCreatingBatchModeBookmarks = false;
	protected boolean isEncrypted = false;
	protected boolean is128BitKey = false;
	protected String userPassword = null;
	protected String ownerPassword = null;
	protected int permissions = 0;
	protected Character pdfVersion = null;

	/**
	 *
	 */
	protected Map loadedImagesMap = null;
	protected Image pxImage = null;
	
	private BookmarkStack bookmarkStack = null;

	private Map fontMap = null;
	/**
	 *
	 */
	protected Image getPxImage()
	{
		if (pxImage == null)
		{
			try
			{
				pxImage = 
					Image.getInstance(
						JRImageLoader.loadImageDataFromLocation("net/sf/jasperreports/engine/images/pixel.GIF", null)
						);
			}
			catch(Exception e)
			{
				throw new JRRuntimeException(e);
			}
		}

		return pxImage;
	}


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		registerFonts();
		
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		/*   */
		setClassLoader();

		/*   */
		setInput();

		/*   */
		if (!isModeBatch)
		{
			setPageRange();
		}
		
		Boolean isCreatingBatchModeBookmarksParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS);
		if(isCreatingBatchModeBookmarksParameter != null){
			isCreatingBatchModeBookmarks = isCreatingBatchModeBookmarksParameter.booleanValue();
		}

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

		pdfVersion = (Character) parameters.get(JRPdfExporterParameter.PDF_VERSION);

		fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);

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

		/*   */
		resetClassLoader();
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

			if (pdfVersion != null)
				pdfWriter.setPdfVersion(pdfVersion.charValue());

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

			pdfContentByte = pdfWriter.getDirectContent();
			
			initBookmarks();

			PdfWriter imageTesterPdfWriter = 
				PdfWriter.getInstance(
					imageTesterDocument, 
					new ByteArrayOutputStream()
					);
			imageTesterDocument.open();
			imageTesterDocument.newPage();
			imageTesterPdfContentByte = imageTesterPdfWriter.getDirectContent();
			imageTesterPdfContentByte.setLiteral("\n");

			for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
			{
				jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
				defaultFont = null;
				loadedImagesMap = new HashMap();
				document.setPageSize(new Rectangle(jasperPrint.getPageWidth(), jasperPrint.getPageHeight()));
				
				List pages = jasperPrint.getPages();
				if (pages != null && pages.size() > 0)
				{
					if (isModeBatch)
					{
						document.newPage();
						
						if( isCreatingBatchModeBookmarks ){
							//add a new level to our outline for this report
							addBookmark(0, jasperPrint.getName(), 0, 0);
						}

						startPageIndex = 0;
						endPageIndex = pages.size() - 1;
					}

					Chunk chunk = null;
					ColumnText colText = null;
					JRPrintPage page = null;
					for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.currentThread().isInterrupted())
						{
							throw new JRException("Current thread interrupted.");
						}
				
						page = (JRPrintPage)pages.get(pageIndex);

						document.newPage();

						pdfContentByte = pdfWriter.getDirectContent();

						pdfContentByte.setLineCap(2);

						chunk = new Chunk(" ");
						chunk.setLocalDestination(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1));

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
		Collection elements = page.getElements();
		exportElements(elements);
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void exportElements(Collection elements) throws DocumentException, IOException, JRException
	{
		if (elements != null && elements.size() > 0)
		{
			JRPrintElement element;
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
				else if (element instanceof JRPrintFrame)
				{
					exportFrame((JRPrintFrame) element);
				}
			}
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
					line.getX() + getOffsetX() + x1 * borderCorrection,
					jasperPrint.getPageHeight() - line.getY() - getOffsetY() + y1 * borderCorrection
					);
				pdfContentByte.lineTo(
					line.getX() + getOffsetX() + line.getWidth() + x2 * borderCorrection - 1,
					jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() + y2 * borderCorrection + 1
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
					line.getX() + getOffsetX() + x1 * borderCorrection, 
					jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() + y1 * borderCorrection + 1
					);
				pdfContentByte.lineTo(
					line.getX() + getOffsetX() + line.getWidth() + x2 * borderCorrection - 1,
					jasperPrint.getPageHeight() - line.getY() - getOffsetY() + y2 * borderCorrection
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
				rectangle.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight() - borderCorrection + 1,
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
					rectangle.getX() + getOffsetX() - borderCorrection,
					jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight() - borderCorrection + 1,
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
				ellipse.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight() - borderCorrection + 1,
				ellipse.getX() + getOffsetX() + ellipse.getWidth() + borderCorrection - 1,
				jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() + borderCorrection
				);

			pdfContentByte.fillStroke();
		}
		else
		{
			if (ellipse.getPen() != JRGraphicElement.PEN_NONE)
			{
				pdfContentByte.ellipse(
					ellipse.getX() + getOffsetX() - borderCorrection,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight() - borderCorrection + 1,
					ellipse.getX() + getOffsetX() + ellipse.getWidth() + borderCorrection - 1,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() + borderCorrection
					);

				pdfContentByte.stroke();
			}
		}
	}


	/**
	 *
	 */
	protected void exportImage(JRPrintImage printImage) throws DocumentException, IOException,  JRException
	{
		pdfContentByte.setRGBColorFill(
			printImage.getBackcolor().getRed(),
			printImage.getBackcolor().getGreen(),
			printImage.getBackcolor().getBlue()
			);

		float borderCorrection = 0f;//FIXME NOW border correction could different on all 4 sides of the box
		float lineWidth = 1f;
		boolean isLineDotted = false;

		switch (printImage.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderCorrection = 0f;
				lineWidth = 1f;
				isLineDotted = true;
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderCorrection = 0f;
				lineWidth = 4f;
				isLineDotted = false;
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderCorrection = 0f;
				lineWidth = 2f;
				isLineDotted = false;
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				borderCorrection = 0.25f;
				lineWidth = 0.5f;
				isLineDotted = false;
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				borderCorrection = 0.5f;
				lineWidth = 1f;
				isLineDotted = false;

				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
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
				printImage.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - printImage.getY() - getOffsetY() + borderCorrection,
				printImage.getWidth() + 2 * borderCorrection - 1,
				- printImage.getHeight() - 2 * borderCorrection + 1
				);
			pdfContentByte.fillStroke();
		}

		int topPadding = printImage.getTopPadding();
		int leftPadding = printImage.getLeftPadding();
		int bottomPadding = printImage.getBottomPadding();
		int rightPadding = printImage.getRightPadding();

		int availableImageWidth = printImage.getWidth() - leftPadding - rightPadding;
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = printImage.getHeight() - topPadding - bottomPadding;
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;

		JRRenderable renderer = printImage.getRenderer();
		
		if (
			renderer != null &&
			availableImageWidth > 0 && 
			availableImageHeight > 0
			)
		{
			int xoffset = 0;
			int yoffset = 0;

			Chunk chunk = null;

			float scaledWidth = availableImageWidth;
			float scaledHeight = availableImageHeight;

			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{
				//java.awt.Image awtImage = JRImageLoader.loadImage(printImage.getImageData());

				//com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(awtImage, printImage.getBackcolor());
				//com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(awtImage, null);
				com.lowagie.text.Image image = null;

				float xalignFactor = getXAlignFactor(printImage);
				float yalignFactor = getYAlignFactor(printImage);

				switch(printImage.getScaleImage())
				{
					case JRImage.SCALE_IMAGE_CLIP :
					{
						int normalWidth = availableImageWidth;
						int normalHeight = availableImageHeight;
					
						Dimension2D dimension = renderer.getDimension();
						if (dimension != null)
						{
							normalWidth = (int)dimension.getWidth();
							normalHeight = (int)dimension.getHeight();
						}

						xoffset = (int)(xalignFactor * (availableImageWidth - normalWidth));
						yoffset = (int)(yalignFactor * (availableImageHeight - normalHeight));

						int minWidth = Math.min(normalWidth, availableImageWidth);
						int minHeight = Math.min(normalHeight, availableImageHeight);
					
						BufferedImage bi = 
							new BufferedImage(minWidth, minHeight, BufferedImage.TYPE_INT_ARGB);

						Graphics2D g = bi.createGraphics();
						if (printImage.getMode() == JRElement.MODE_OPAQUE)
						{
							g.setColor(printImage.getBackcolor());
							g.fillRect(0, 0, minWidth, minHeight);
						}
						renderer.render(
							g,
							new java.awt.Rectangle(
								(xoffset > 0 ? 0 : xoffset), 
								(yoffset > 0 ? 0 : yoffset),
								normalWidth,
								normalHeight
								) 
							);
						g.dispose();

						xoffset = (xoffset < 0 ? 0 : xoffset);
						yoffset = (yoffset < 0 ? 0 : yoffset);

						//awtImage = bi.getSubimage(0, 0, minWidth, minHeight);

						//image = com.lowagie.text.Image.getInstance(awtImage, printImage.getBackcolor());
						image = com.lowagie.text.Image.getInstance(bi, null);

						break;
					}
					case JRImage.SCALE_IMAGE_FILL_FRAME :
					{
						if (loadedImagesMap.containsKey(renderer))
						{
							image = (com.lowagie.text.Image)loadedImagesMap.get(renderer);
						}
						else
						{
							try
							{
								image = com.lowagie.text.Image.getInstance(renderer.getImageData());
								imageTesterPdfContentByte.addImage(image, 10, 0, 0, 10, 0, 0);
							}
							catch(Exception e)
							{
								java.awt.Image awtImage = 
									JRImageRenderer.getInstance(
										renderer.getImageData(),
										printImage.getOnErrorType()
										).getImage();
								image = com.lowagie.text.Image.getInstance(awtImage, null);
							}

							loadedImagesMap.put(renderer, image);
						}

						image.scaleAbsolute(availableImageWidth, availableImageHeight);
						break;
					}
					case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
					default :
					{
						if (loadedImagesMap.containsKey(renderer))
						{
							image = (com.lowagie.text.Image)loadedImagesMap.get(renderer);
						}
						else
						{
							try
							{
								image = com.lowagie.text.Image.getInstance(renderer.getImageData());
								imageTesterPdfContentByte.addImage(image, 10, 0, 0, 10, 0, 0);
							}
							catch(Exception e)
							{
								java.awt.Image awtImage = JRImageLoader.loadImage(renderer.getImageData());
								image = com.lowagie.text.Image.getInstance(awtImage, null);
							}

							loadedImagesMap.put(renderer, image);
						}

						image.scaleToFit(availableImageWidth, availableImageHeight);
					
						xoffset = (int)(xalignFactor * (availableImageWidth - image.plainWidth()));
						yoffset = (int)(yalignFactor * (availableImageHeight - image.plainHeight()));

						xoffset = (xoffset < 0 ? 0 : xoffset);
						yoffset = (yoffset < 0 ? 0 : yoffset);

						break;
					}
				}
				
				chunk = new Chunk(image, -0.5f, 0.5f);
				
				scaledWidth = image.scaledWidth();
				scaledHeight = image.scaledHeight();
			}
			else
			{
				double normalWidth = availableImageWidth;
				double normalHeight = availableImageHeight;
				
				Dimension2D dimension = renderer.getDimension();
				if (dimension != null)
				{
					float xalignFactor = getXAlignFactor(printImage);
					float yalignFactor = getYAlignFactor(printImage);
					
					switch (printImage.getScaleImage())
					{
						case JRImage.SCALE_IMAGE_CLIP:
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
							xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
							yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));
							break;
						}
						case JRImage.SCALE_IMAGE_FILL_FRAME:
						{
							xoffset = 0;
							yoffset = 0;
							break;
						}
						case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
						default:
						{
							normalWidth = dimension.getWidth();
							normalHeight = dimension.getHeight();
							double ratioX = availableImageWidth / normalWidth;
							double ratioY = availableImageHeight / normalHeight;
							double ratio = ratioX < ratioY ? ratioX : ratioY;
							normalWidth *= ratio;
							normalHeight *= ratio;
							xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
							yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));
							break;
						}
					}
				}

				PdfTemplate template = pdfContentByte.createTemplate(availableImageWidth, availableImageHeight);
				Graphics2D g = template.createGraphicsShapes(availableImageWidth, availableImageHeight);
				
				if (printImage.getMode() == JRElement.MODE_OPAQUE)
				{
					g.setColor(printImage.getBackcolor());
					g.fillRect(0, 0, 
						normalWidth <= availableImageWidth ? (int) normalWidth : availableImageWidth, 
						normalHeight <= availableImageHeight ? (int) normalHeight : availableImageHeight);
				}

				Rectangle2D rectangle = new Rectangle2D.Double(
						(xoffset > 0 ? 0 : xoffset), 
						(yoffset > 0 ? 0 : yoffset), 
						normalWidth, 
						normalHeight);
				
				renderer.render(g, rectangle);
				g.dispose();
				
				xoffset = (xoffset < 0 ? 0 : xoffset);
				yoffset = (yoffset < 0 ? 0 : yoffset);

				pdfContentByte.saveState();
				pdfContentByte.addTemplate(
					template,
					printImage.getX() + getOffsetX() + xoffset,
					jasperPrint.getPageHeight()
						- printImage.getY() - getOffsetY()
						- availableImageHeight
						- yoffset
					);
				pdfContentByte.restoreState();

				Image image = getPxImage();
				image.scaleAbsolute(availableImageWidth, availableImageHeight);
				chunk = new Chunk(image, 0, 0);
			}

			/*
			image.setAbsolutePosition(
				printImage.getX() + offsetX + borderOffset,
				jasperPrint.getPageHeight() - printImage.getY() - offsetY - image.scaledHeight() - borderOffset
				);

			pdfContentByte.addImage(image);
			*/


			setAnchor(chunk, printImage, printImage);

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
						chunk.setLocalGoto(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + printImage.getHyperlinkPage().toString());
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
				printImage.getX() + leftPadding + getOffsetX() + xoffset,
				jasperPrint.getPageHeight() - printImage.getY() - topPadding - getOffsetY() - scaledHeight - yoffset,
				printImage.getX() + leftPadding + getOffsetX() + xoffset + scaledWidth,
				jasperPrint.getPageHeight() - printImage.getY() - topPadding - getOffsetY() - yoffset,
				scaledHeight,
				Element.ALIGN_LEFT
				);

			colText.go();
		}


		if (
			printImage.getTopBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getLeftBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getBottomBorder() == JRGraphicElement.PEN_NONE &&
			printImage.getRightBorder() == JRGraphicElement.PEN_NONE
			)
		{
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
					printImage.getX() + getOffsetX() - borderCorrection,
					jasperPrint.getPageHeight() - printImage.getY() - getOffsetY() + borderCorrection,
					printImage.getWidth() + 2 * borderCorrection - 1,
					- printImage.getHeight() - 2 * borderCorrection + 1
					);
	
				pdfContentByte.stroke();
			}
		}
		else
		{
			/*   */
			exportBox(
				printImage,
				printImage
				);
		}
	}


	private float getXAlignFactor(JRPrintImage printImage)
	{
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
		return xalignFactor;
	}


	private float getYAlignFactor(JRPrintImage printImage)
	{
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
		return yalignFactor;
	}


	/**
	 * 
	 */
	protected void setHyperlinkInfo(Chunk chunk, JRPrintText text)
	{
		setAnchor(chunk, text, text);

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
					chunk.setLocalGoto(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + text.getHyperlinkPage().toString());
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
	}
	

	/**
	 *
	 */
	protected Phrase getPhrase(JRStyledText styledText, JRPrintText textElement) throws JRException, DocumentException, IOException
	{
		Phrase phrase = new Phrase();

		String text = styledText.getText();
		
		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();
		
		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			Chunk chunk = getChunk(iterator.getAttributes(), text.substring(iterator.getIndex(), runLimit));
			setHyperlinkInfo(chunk, textElement);
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
		
		Exception initialException = null;

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		Color backcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		/*
		if (forecolor == null)
		{
			forecolor = Color.black;
		}
		*/

		Font font = null;
		try
		{
			FontKey key = new FontKey(jrFont.getFontName(), jrFont.isBold(), jrFont.isItalic());
			if (fontMap != null && fontMap.containsKey(key)) {
				PdfFont pdfFont = (PdfFont) fontMap.get(key);
				font = FontFactory.getFont(
					pdfFont.getPdfFontName(),
					pdfFont.getPdfEncoding(),
					pdfFont.isPdfEmbedded(),
					jrFont.getFontSize(),
//					(jrFont.isBold() ? Font.BOLD : 0) | (jrFont.isItalic() ? Font.ITALIC : 0) |
					(jrFont.isUnderline() ? Font.UNDERLINE : 0) | (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0),
					forecolor
					);
			}
			else {
				font = FontFactory.getFont(
					jrFont.getPdfFontName(),
					jrFont.getPdfEncoding(),
					jrFont.isPdfEmbedded(),
					jrFont.getFontSize(),
//					(jrFont.isBold() ? Font.BOLD : 0) | (jrFont.isItalic() ? Font.ITALIC : 0) |
					(jrFont.isUnderline() ? Font.UNDERLINE : 0) | (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0),
					forecolor
					);
			}
			
			// check if FontFactory didn't find the font
			if (font.getBaseFont() == null && font.family() == Font.UNDEFINED)
			{
				font = null;
			}
		}
		catch(Exception e)
		{
			initialException = e;
		}

		if (font == null)
		{
			byte[] bytes = null;

			try
			{
				bytes = JRLoader.loadBytesFromLocation(jrFont.getPdfFontName(), classLoader);
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

			BaseFont baseFont =
				BaseFont.createFont(
					jrFont.getPdfFontName(),
					jrFont.getPdfEncoding(),
					jrFont.isPdfEmbedded(),
					true,
					bytes,
					null
					);
			
			font =
				new Font(
					baseFont,
					jrFont.getFontSize(),
					//((jrFont.isBold())?Font.BOLD:0) +
					//((jrFont.isItalic())?Font.ITALIC:0) +
					(jrFont.isUnderline() ? Font.UNDERLINE : 0) 
						| (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0),
					forecolor
					);
		}

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
		
		int x = text.getX() + getOffsetX();
		int y = text.getY() + getOffsetY();
		int width = text.getWidth();
		int height = text.getHeight();
		int topPadding = text.getTopPadding();
		int leftPadding = text.getLeftPadding();
		int bottomPadding = text.getBottomPadding();
		int rightPadding = text.getRightPadding();

		int xFillCorrection = 0;
		int yFillCorrection = 0;
		
		double angle = 0;
		
		switch (text.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				y = text.getY() + getOffsetY() + text.getHeight();
				xFillCorrection = 1;
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = leftPadding;
				leftPadding = bottomPadding;
				bottomPadding = rightPadding;
				rightPadding = tmpPadding;
				angle = Math.PI / 2;
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				x = text.getX() + getOffsetX() + text.getWidth();
				yFillCorrection = -1;
				width = text.getHeight();
				height = text.getWidth();
				int tmpPadding = topPadding;
				topPadding = rightPadding;
				rightPadding = bottomPadding;
				bottomPadding = leftPadding;
				leftPadding = tmpPadding;
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
				x + xFillCorrection,
				jasperPrint.getPageHeight() - y + yFillCorrection,
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
				text.getX() + offsetX,
				jasperPrint.getPageHeight() - text.getY() - offsetY,
				text.getWidth(),
				- text.getHeight()
				);
			pdfContentByte.stroke();
			*/
		}

		if (textLength > 0)
		{
			int horizontalAlignment = Element.ALIGN_LEFT;
			switch (text.getHorizontalAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_LEFT :
				{
					if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_LTR)
					{
						horizontalAlignment = Element.ALIGN_LEFT; 
					}
					else
					{
						horizontalAlignment = Element.ALIGN_RIGHT; 
					}
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER :
				{
					horizontalAlignment = Element.ALIGN_CENTER;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
				{
					if (text.getRunDirection() == JRPrintText.RUN_DIRECTION_LTR)
					{
						horizontalAlignment = Element.ALIGN_RIGHT; 
					}
					else
					{
						horizontalAlignment = Element.ALIGN_LEFT; 
					}
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
				case JRAlignment.VERTICAL_ALIGN_TOP :
				{
					verticalOffset = 0f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_MIDDLE :
				{
					verticalOffset = (height - topPadding - bottomPadding - text.getTextHeight()) / 2f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_BOTTOM :
				{
					verticalOffset = height - topPadding - bottomPadding - text.getTextHeight();
					break;
				}
				default :
				{
					verticalOffset = 0f;
				}
			}

			ColumnText colText = new ColumnText(pdfContentByte);
			colText.setSimpleColumn(
				getPhrase(styledText, text),
				x + leftPadding, 
				jasperPrint.getPageHeight() 
					- y
					- topPadding
					- verticalOffset
					- text.getLeadingOffset(), 
					//+ text.getLineSpacingFactor() * text.getFont().getSize(), 
				x + width - rightPadding, 
				jasperPrint.getPageHeight() 
					- y 
					- height
					+ bottomPadding,
				0,//text.getLineSpacingFactor(),// * text.getFont().getSize(),
				horizontalAlignment
				);

			colText.setLeading(0, text.getLineSpacingFactor());// * text.getFont().getSize());
			colText.setRunDirection(
				text.getRunDirection() == JRPrintText.RUN_DIRECTION_LTR
				? PdfWriter.RUN_DIRECTION_LTR : PdfWriter.RUN_DIRECTION_RTL
				);

			colText.go();
		}

		atrans = new AffineTransform();
		atrans.rotate(-angle, x, jasperPrint.getPageHeight() - y);
		pdfContentByte.transform(atrans);

		/*   */
		exportBox(
			text,
			text
			);
	}

		
	/**
	 *
	 */
	protected void exportBox(JRBox box, JRPrintElement element)
	{
		if (box.getTopBorder() != JRGraphicElement.PEN_NONE)
		{
			float borderCorrection = prepareBorder(pdfContentByte, box.getTopBorder());
			Color color = box.getTopBorderColor() == null ? element.getForecolor() : box.getTopBorderColor(); 
			pdfContentByte.setRGBColorStroke(
					color.getRed(),
					color.getGreen(),
					color.getBlue()
					);
			pdfContentByte.moveTo(
				element.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() + borderCorrection
				);
			pdfContentByte.lineTo(
				element.getX() + getOffsetX() + element.getWidth() - 1 + borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() + borderCorrection
				);
			pdfContentByte.stroke();
		}

		if (box.getLeftBorder() != JRGraphicElement.PEN_NONE)
		{
			float borderCorrection = prepareBorder(pdfContentByte, box.getLeftBorder());
			Color color = box.getLeftBorderColor() == null ? element.getForecolor() : box.getLeftBorderColor(); 
			pdfContentByte.setRGBColorStroke(
					color.getRed(),
					color.getGreen(),
					color.getBlue()
					);
			pdfContentByte.moveTo(
				element.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() + borderCorrection
				);
			pdfContentByte.lineTo(
				element.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + 1 - borderCorrection
				);
			pdfContentByte.stroke();
		}

		if (box.getBottomBorder() != JRGraphicElement.PEN_NONE)
		{
			float borderCorrection = prepareBorder(pdfContentByte, box.getBottomBorder());
			Color color = box.getBottomBorderColor() == null ? element.getForecolor() : box.getBottomBorderColor(); 
			pdfContentByte.setRGBColorStroke(
					color.getRed(),
					color.getGreen(),
					color.getBlue()
					);
			pdfContentByte.moveTo(
				element.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + 1 - borderCorrection
				);
			pdfContentByte.lineTo(
				element.getX() + getOffsetX() + element.getWidth() - 1 + borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + 1 - borderCorrection
				);
			pdfContentByte.stroke();
		}

		if (box.getRightBorder() != JRGraphicElement.PEN_NONE)
		{
			float borderCorrection = prepareBorder(pdfContentByte, box.getRightBorder());
			Color color = box.getRightBorderColor() == null ? element.getForecolor() : box.getRightBorderColor(); 
			pdfContentByte.setRGBColorStroke(
					color.getRed(),
					color.getGreen(),
					color.getBlue()
					);
			pdfContentByte.moveTo(
				element.getX() + getOffsetX() + element.getWidth() - 1 + borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() + borderCorrection
				);
			pdfContentByte.lineTo(
				element.getX() + getOffsetX() + element.getWidth() - 1 + borderCorrection,
				jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + 1 - borderCorrection
				);
			pdfContentByte.stroke();
		}
	}

		
	/**
	 * 
	 */
	private static float prepareBorder(PdfContentByte pdfContentByte, byte border)
	{
		float borderCorrection = 0f;
		
		switch (border)
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
				pdfContentByte.setLineWidth(1f);
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
		
		return borderCorrection;
	}


	protected static synchronized void registerFonts ()
	{
		if (!fontsRegistered)
		{
			List fontFiles = JRProperties.getProperties(JRProperties.PDF_FONT_FILES_PREFIX);
			if (!fontFiles.isEmpty())
			{
				for (Iterator i = fontFiles.iterator(); i.hasNext();)
				{
					JRProperties.PropertySuffix font = (JRProperties.PropertySuffix) i.next();
					String file = font.getValue();
					if (file.toLowerCase().endsWith(".ttc"))
					{
						FontFactory.register(file);
					}
					else
					{
						String alias = font.getSuffix();
						FontFactory.register(file, alias);
					}
				}
			}
			
			List fontDirs = JRProperties.getProperties(JRProperties.PDF_FONT_DIRS_PREFIX);
			if (!fontDirs.isEmpty())
			{
				for (Iterator i = fontDirs.iterator(); i.hasNext();)
				{
					JRProperties.PropertySuffix dir = (JRProperties.PropertySuffix) i.next();
					FontFactory.registerDirectory(dir.getValue());
				}
			}
			
			fontsRegistered = true;
		}
	}

	
	static protected class Bookmark
	{
		final PdfOutline pdfOutline;
		final int level;
		
		Bookmark(Bookmark parent, int x, int top, String title)
		{
			this(parent, new PdfDestination(PdfDestination.XYZ, x, top, 0), title);
		}
		
		Bookmark(Bookmark parent, PdfDestination destination, String title)
		{
			this.pdfOutline = new PdfOutline(parent.pdfOutline, destination, title, false);
			this.level = parent.level + 1;
		}
		
		Bookmark(PdfOutline pdfOutline, int level)
		{
			this.pdfOutline = pdfOutline;
			this.level = level;
		}
	}
	
	static protected class BookmarkStack
	{
		LinkedList stack;
		
		BookmarkStack()
		{
			stack = new LinkedList();
		}
		
		void push(Bookmark bookmark)
		{
			stack.add(bookmark);
		}
		
		Bookmark pop()
		{
			return (Bookmark) stack.removeLast();
		}
		
		Bookmark peek()
		{
			return (Bookmark) stack.getLast();
		}
	}
	

	protected void initBookmarks()
	{
		bookmarkStack = new BookmarkStack();
		
		int rootLevel = isModeBatch && isCreatingBatchModeBookmarks ? -1 : 0;
		Bookmark bookmark = new Bookmark(pdfContentByte.getRootOutline(), rootLevel);
		bookmarkStack.push(bookmark);
	}
	
	
	protected void addBookmark(int level, String title, int x, int y)
	{
		Bookmark parent = bookmarkStack.peek();
		while(parent.level > level - 1)
		{
			bookmarkStack.pop();
			parent = bookmarkStack.peek();
		}
		
		for (int i = parent.level + 1; i < level; ++i)
		{
			Bookmark emptyBookmark = new Bookmark(parent, parent.pdfOutline.getPdfDestination(), EMPTY_BOOKMARK_TITLE);
			bookmarkStack.push(emptyBookmark);
			parent = emptyBookmark;
		}

		Bookmark bookmark = new Bookmark(parent, x, jasperPrint.getPageHeight() - y, title);
		bookmarkStack.push(bookmark);
	}

	
	protected void setAnchor(Chunk chunk, JRPrintAnchor anchor, JRPrintElement element)
	{
		String anchorName = anchor.getAnchorName();
		if (anchorName != null)
		{
			chunk.setLocalDestination(anchorName);
			
			if (anchor.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
			{
				addBookmark(anchor.getBookmarkLevel(), anchor.getAnchorName(), element.getX(), element.getY());
			}
		}
	}
	

	protected void exportFrame(JRPrintFrame frame) throws DocumentException, IOException, JRException
	{
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			int x = frame.getX() + getOffsetX();
			int y = frame.getY() + getOffsetY();
			
			Color backcolor = frame.getBackcolor();
			pdfContentByte.setRGBColorStroke(
					backcolor.getRed(),
					backcolor.getGreen(),
					backcolor.getBlue()
					);
				pdfContentByte.setRGBColorFill(
					backcolor.getRed(),
					backcolor.getGreen(),
					backcolor.getBlue()
					);
				pdfContentByte.setLineWidth(1f);
				pdfContentByte.setLineDash(0f);
				pdfContentByte.rectangle(
					x,
					jasperPrint.getPageHeight() - y,
					frame.getWidth() - 1,
					- frame.getHeight() + 1
					);
				pdfContentByte.fillStroke();
		}
		
		setFrameElementsOffset(frame, false);
		try
		{
			exportElements(frame.getElements());
		}
		finally
		{
			restoreElementOffsets();
		}
		
		exportBox(frame, frame);
	}
}
