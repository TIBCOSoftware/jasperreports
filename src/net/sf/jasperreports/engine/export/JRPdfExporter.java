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

/*
 * Contributors:
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 * Ling Li - lonecatz@users.sourceforge.net
 * Martin Clough - mtclough@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
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
import net.sf.jasperreports.engine.util.BreakIteratorSplitCharacter;
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
import com.lowagie.text.SplitCharacter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.FontMapper;
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

	/**
	 * @deprecated Replaced by {@link JRPdfExporterParameter#PROPERTY_FORCE_SVG_SHAPES}.
	 */
	public static final String PDF_FORCE_SVG_SHAPES = JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES;

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
	protected boolean forceSvgShapes;
	protected boolean isCreatingBatchModeBookmarks;
	protected boolean isCompressed;
	protected boolean isEncrypted;
	protected boolean is128BitKey;
	protected String userPassword;
	protected String ownerPassword;
	protected int permissions = 0;
	protected Character pdfVersion;
	protected String pdfJavaScript;

	/**
	 *
	 */
	protected Map loadedImagesMap = null;
	protected Image pxImage = null;

	private BookmarkStack bookmarkStack = null;

	private Map fontMap = null;

	private SplitCharacter splitCharacter;
	protected JRHyperlinkProducerFactory hyperlinkProducerFactory;

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
						JRLoader.loadBytesFromLocation("net/sf/jasperreports/engine/images/pixel.GIF", null)
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

			isCreatingBatchModeBookmarks = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS,
					JRPdfExporterParameter.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS,
					false
					);

			forceSvgShapes = 
				getBooleanParameter(
					JRPdfExporterParameter.FORCE_SVG_SHAPES,
					JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES,
					false
					);

			isCompressed = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_COMPRESSED,
					JRPdfExporterParameter.PROPERTY_COMPRESSED,
					false
					);

			isEncrypted = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_ENCRYPTED,
					JRPdfExporterParameter.PROPERTY_ENCRYPTED,
					false
					);

			is128BitKey = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_128_BIT_KEY,
					JRPdfExporterParameter.PROPERTY_128_BIT_KEY,
					false
					);

			userPassword = 
				getStringParameter(
					JRPdfExporterParameter.USER_PASSWORD,
					JRPdfExporterParameter.PROPERTY_USER_PASSWORD
					);

			ownerPassword = 
				getStringParameter(
					JRPdfExporterParameter.OWNER_PASSWORD,
					JRPdfExporterParameter.PROPERTY_OWNER_PASSWORD
					);

			Integer permissionsParameter = (Integer)parameters.get(JRPdfExporterParameter.PERMISSIONS);
			if (permissionsParameter != null)
			{
				permissions = permissionsParameter.intValue();
			}

			String strPdfVersion = 
				getStringParameter(
					JRPdfExporterParameter.PDF_VERSION,
					JRPdfExporterParameter.PROPERTY_PDF_VERSION
					);
			pdfVersion = 
				(strPdfVersion == null || strPdfVersion.length() == 0) 
				? null 
				: new Character(strPdfVersion.charAt(0));

			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);

			setSplitCharacter();
			setHyperlinkProducerFactory();

			pdfJavaScript = 
				getStringParameter(
					JRPdfExporterParameter.PDF_JAVASCRIPT,
					JRPdfExporterParameter.PROPERTY_PDF_JAVASCRIPT
					);

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
		finally
		{
			resetExportContext();
		}
	}


	protected void setSplitCharacter()
	{
		boolean useFillSplitCharacter;
		Boolean useFillSplitCharacterParam = (Boolean) parameters.get(JRPdfExporterParameter.FORCE_LINEBREAK_POLICY);
		if (useFillSplitCharacterParam == null)
		{
			useFillSplitCharacter = 
				JRProperties.getBooleanProperty(
					jasperPrint.getPropertiesMap(),
					JRPdfExporterParameter.PROPERTY_FORCE_LINEBREAK_POLICY,
					false
					);
		}
		else
		{
			useFillSplitCharacter = useFillSplitCharacterParam.booleanValue();
		}

		if (useFillSplitCharacter)
		{
			splitCharacter = new BreakIteratorSplitCharacter();
		}
	}


	protected void setHyperlinkProducerFactory()
	{
		hyperlinkProducerFactory = (JRHyperlinkProducerFactory) parameters.get(JRExporterParameter.HYPERLINK_PRODUCER_FACTORY);
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

		boolean closeDocuments = true;
		try
		{
			PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
			pdfWriter.setCloseStream(false);

			if (pdfVersion != null)
				pdfWriter.setPdfVersion(pdfVersion.charValue());

			if (isCompressed)
				pdfWriter.setFullCompression();

			if (isEncrypted)
			{
				pdfWriter.setEncryption(
					is128BitKey,
					userPassword,
					ownerPassword,
					permissions
					);
			}

			// Add meta-data parameters to generated PDF document
			// mtclough@users.sourceforge.net 2005-12-05
			String title = (String)parameters.get(JRPdfExporterParameter.METADATA_TITLE);
			if( title != null )
				document.addTitle(title);

			String author = (String)parameters.get(JRPdfExporterParameter.METADATA_AUTHOR);
			if( author != null )
				document.addAuthor(author);

			String subject = (String)parameters.get(JRPdfExporterParameter.METADATA_SUBJECT);
			if( subject != null )
				document.addSubject(subject);

			String keywords = (String)parameters.get(JRPdfExporterParameter.METADATA_KEYWORDS);
			if( keywords != null )
				document.addKeywords(keywords);

			String creator = (String)parameters.get(JRPdfExporterParameter.METADATA_CREATOR);
			if( creator != null )
				document.addCreator(creator);
			else
				document.addCreator("JasperReports (" + jasperPrint.getName() + ")");

			document.open();

			if(pdfJavaScript != null)
				pdfWriter.addJavaScript(pdfJavaScript);

			pdfContentByte = pdfWriter.getDirectContent();

			initBookmarks();

			PdfWriter imageTesterPdfWriter =
				PdfWriter.getInstance(
					imageTesterDocument,
					new NullOutputStream() // discard the output
					);
			imageTesterDocument.open();
			imageTesterDocument.newPage();
			imageTesterPdfContentByte = imageTesterPdfWriter.getDirectContent();
			imageTesterPdfContentByte.setLiteral("\n");

			for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
			{
				jasperPrint = (JasperPrint)jasperPrintList.get(reportIndex);
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

						pdfContentByte.setLineCap(2);//PdfContentByte.LINE_CAP_PROJECTING_SQUARE since iText 1.02b

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

			closeDocuments = false;
			document.close();
			imageTesterDocument.close();
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
			if (closeDocuments) //only on exception
			{
				try
				{
					document.close();
				}
				catch (Throwable e)
				{
					// ignore, let the original exception propagate
				}

				try
				{
					imageTesterDocument.close();
				}
				catch (Throwable e)
				{
					// ignore, let the original exception propagate
				}
			}
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

			switch (line.getPen())
			{
				case JRGraphicElement.PEN_DOTTED :
				{
					pdfContentByte.setLineWidth(1f);
					pdfContentByte.setLineDash(5f, 3f, 0f);
					break;
				}
				case JRGraphicElement.PEN_4_POINT :
				{
					pdfContentByte.setLineWidth(4f);
					pdfContentByte.setLineDash(0f);
					break;
				}
				case JRGraphicElement.PEN_2_POINT :
				{
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
					pdfContentByte.setLineWidth(0.5f);
					pdfContentByte.setLineDash(0f);
					break;
				}
				case JRGraphicElement.PEN_1_POINT :
				default :
				{
					pdfContentByte.setLineWidth(1f);
					pdfContentByte.setLineDash(0f);
					break;
				}
			}

			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				pdfContentByte.moveTo(
					line.getX() + getOffsetX(),
					jasperPrint.getPageHeight() - line.getY() - getOffsetY()
					);
				pdfContentByte.lineTo(
					line.getX() + getOffsetX() + line.getWidth() - 1,
					jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() + 1
					);
			}
			else
			{
				pdfContentByte.moveTo(
					line.getX() + getOffsetX(),
					jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() + 1
					);
				pdfContentByte.lineTo(
					line.getX() + getOffsetX() + line.getWidth() - 1,
					jasperPrint.getPageHeight() - line.getY() - getOffsetY()
					);
			}

			pdfContentByte.stroke();

			pdfContentByte.setLineDash(0f);
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

		float borderCorrection = prepareGraphicElement(rectangle);

		if (rectangle.getMode() == JRElement.MODE_OPAQUE)
		{
			pdfContentByte.roundRectangle(
					rectangle.getX() + getOffsetX() - borderCorrection,
					jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight() - borderCorrection + 1,
					rectangle.getWidth() + 2 * borderCorrection - 1,
					rectangle.getHeight() + 2 * borderCorrection - 1,
					rectangle.getRadius()
					);

			if (rectangle.getPen() == JRGraphicElement.PEN_DOTTED)
			{
				pdfContentByte.fill();

				pdfContentByte.roundRectangle(
						rectangle.getX() + getOffsetX(),
						jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight() + 1,
						rectangle.getWidth() - 1,
						rectangle.getHeight() - 1,
						rectangle.getRadius()
						);
				pdfContentByte.stroke();
			}
			else
			{
				pdfContentByte.fillStroke();
			}
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

		pdfContentByte.setLineDash(0f);
	}


	protected float prepareGraphicElement(JRPrintGraphicElement element)
	{
		float borderCorrection = 0f;

		switch (element.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderCorrection = element.getMode() == JRElement.MODE_OPAQUE ? 0.5f : 0f;
				pdfContentByte.setLineWidth(1f);
				pdfContentByte.setLineDash(5f, 3f, 0f);
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderCorrection = 0.5f;
				pdfContentByte.setLineWidth(4f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderCorrection = 0.5f;
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
					element.getBackcolor().getRed(),
					element.getBackcolor().getGreen(),
					element.getBackcolor().getBlue()
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
		return borderCorrection;
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

		float borderCorrection = prepareGraphicElement(ellipse);

		if (ellipse.getMode() == JRElement.MODE_OPAQUE)
		{
			pdfContentByte.ellipse(
				ellipse.getX() + getOffsetX() - borderCorrection,
				jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight() - borderCorrection + 1,
				ellipse.getX() + getOffsetX() + ellipse.getWidth() + borderCorrection - 1,
				jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() + borderCorrection
				);

			if (ellipse.getPen() == JRGraphicElement.PEN_DOTTED)
			{
				pdfContentByte.fill();

				pdfContentByte.ellipse(
						ellipse.getX() + getOffsetX(),
						jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight() + 1,
						ellipse.getX() + getOffsetX() + ellipse.getWidth() - 1,
						jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY()
						);
				pdfContentByte.stroke();
			}
			else
			{
				pdfContentByte.fillStroke();
			}
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

		pdfContentByte.setLineDash(0f);
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

		if (printImage.getMode() == JRElement.MODE_OPAQUE)
		{
			float borderCorrection = .5f;

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
			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{
				// Image renderers are all asked for their image data at some point. 
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, printImage.getOnErrorType());
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			int xoffset = 0;
			int yoffset = 0;

			Chunk chunk = null;

			float scaledWidth = availableImageWidth;
			float scaledHeight = availableImageHeight;

			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{
				com.lowagie.text.Image image = null;

				float xalignFactor = getXAlignFactor(printImage);
				float yalignFactor = getYAlignFactor(printImage);

				switch(printImage.getScaleImage())
				{
					case JRImage.SCALE_IMAGE_CLIP :
					{
						// Image load might fail, from given image data. 
						// Better to test and replace the renderer now, in case of lazy load error.
						renderer = 
							JRImageRenderer.getOnErrorRendererForDimension(
								renderer, 
								printImage.getOnErrorType()
								);
						if (renderer == null)
						{
							break;
						}
						
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
						if (printImage.isUsingCache() && loadedImagesMap.containsKey(renderer))
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
								JRImageRenderer tmpRenderer = 
									JRImageRenderer.getOnErrorRendererForImage(
										JRImageRenderer.getInstance(renderer.getImageData()), 
										printImage.getOnErrorType()
										);
								if (tmpRenderer == null)
								{
									break;
								}
								java.awt.Image awtImage = tmpRenderer.getImage();
								image = com.lowagie.text.Image.getInstance(awtImage, null);
							}

							if (printImage.isUsingCache())
							{
								loadedImagesMap.put(renderer, image);
							}
						}

						image.scaleAbsolute(availableImageWidth, availableImageHeight);
						break;
					}
					case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
					default :
					{
						if (printImage.isUsingCache() && loadedImagesMap.containsKey(renderer))
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
								JRImageRenderer tmpRenderer = 
									JRImageRenderer.getOnErrorRendererForImage(
										JRImageRenderer.getInstance(renderer.getImageData()), 
										printImage.getOnErrorType()
										);
								if (tmpRenderer == null)
								{
									break;
								}
								java.awt.Image awtImage = tmpRenderer.getImage();
								image = com.lowagie.text.Image.getInstance(awtImage, null);
							}

							if (printImage.isUsingCache())
							{
								loadedImagesMap.put(renderer, image);
							}
						}

						image.scaleToFit(availableImageWidth, availableImageHeight);

						xoffset = (int)(xalignFactor * (availableImageWidth - image.plainWidth()));
						yoffset = (int)(yalignFactor * (availableImageHeight - image.plainHeight()));

						xoffset = (xoffset < 0 ? 0 : xoffset);
						yoffset = (yoffset < 0 ? 0 : yoffset);

						break;
					}
				}

				if (image != null)
				{
					chunk = new Chunk(image, -0.5f, 0.5f);

					scaledWidth = image.scaledWidth();
					scaledHeight = image.scaledHeight();
				}
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

				Graphics2D g = forceSvgShapes
					? template.createGraphicsShapes(availableImageWidth, availableImageHeight)
					: template.createGraphics(availableImageWidth, availableImageHeight, new LocalFontMapper());

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


			if (chunk != null)
			{
				setAnchor(chunk, printImage, printImage);
				setHyperlinkInfo(chunk, printImage);

				ColumnText colText = new ColumnText(pdfContentByte);
				int upperY = jasperPrint.getPageHeight() - printImage.getY() - topPadding - getOffsetY() - yoffset;
				int lowerX = printImage.getX() + leftPadding + getOffsetX() + xoffset;
				colText.setSimpleColumn(
					new Phrase(chunk),
					lowerX,
					upperY - scaledHeight,
					lowerX + scaledWidth,
					upperY,
					scaledHeight,
					Element.ALIGN_LEFT
					);

				colText.go();
			}
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
				exportBox(getBox(printImage), printImage);
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
	protected void setHyperlinkInfo(Chunk chunk, JRPrintHyperlink link)
	{
		switch(link.getHyperlinkType())
		{
			case JRHyperlink.HYPERLINK_TYPE_REFERENCE :
			{
				if (link.getHyperlinkReference() != null)
				{
					chunk.setAnchor(link.getHyperlinkReference());
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
			{
				if (link.getHyperlinkAnchor() != null)
				{
					chunk.setLocalGoto(link.getHyperlinkAnchor());
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
			{
				if (link.getHyperlinkPage() != null)
				{
					chunk.setLocalGoto(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString());
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
			{
				if (
					link.getHyperlinkReference() != null &&
					link.getHyperlinkAnchor() != null
					)
				{
					chunk.setRemoteGoto(
						link.getHyperlinkReference(),
						link.getHyperlinkAnchor()
						);
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
			{
				if (
					link.getHyperlinkReference() != null &&
					link.getHyperlinkPage() != null
					)
				{
					chunk.setRemoteGoto(
						link.getHyperlinkReference(),
						link.getHyperlinkPage().intValue()
						);
				}
				break;
			}
			case JRHyperlink.HYPERLINK_TYPE_CUSTOM :
			{
				if (hyperlinkProducerFactory != null)
				{
					String hyperlink = hyperlinkProducerFactory.produceHyperlink(link);
					if (hyperlink != null)
					{
						chunk.setAnchor(hyperlink);
					}
				}
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
	protected Phrase getPhrase(JRStyledText styledText, JRPrintText textElement)
	{
		Phrase phrase = new Phrase();

		String text = styledText.getText();

		int runLimit = 0;

		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			Chunk chunk = getChunk(iterator.getAttributes(), text.substring(iterator.getIndex(), runLimit));
			setAnchor(chunk, textElement, textElement);
			setHyperlinkInfo(chunk, textElement);
			phrase.add(chunk);

			iterator.setIndex(runLimit);
		}

		return phrase;
	}


	/**
	 *
	 */
	protected Chunk getChunk(Map attributes, String text)
	{
		Font font = getFont(attributes);

		Chunk chunk = new Chunk(text, font);

		Color backcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
		if (backcolor != null)
		{
			chunk.setBackground(backcolor);
		}

		Object script = attributes.get(TextAttribute.SUPERSCRIPT);
		if (script != null)
		{
			if (TextAttribute.SUPERSCRIPT_SUPER.equals(script))
			{
				chunk.setTextRise(font.leading(1f)/2);
			}
			else if (script != null && TextAttribute.SUPERSCRIPT_SUB.equals(script))
			{
				chunk.setTextRise(-font.leading(1f)/2);
			}
		}

		if (splitCharacter != null)
		{
			chunk.setSplitCharacter(splitCharacter);
		}

		return chunk;
	}


	/**
	 *
	 */
	protected Font getFont(Map attributes)
	{
		JRFont jrFont = new JRBaseFont(attributes);

		Exception initialException = null;

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);
		/*
		if (forecolor == null)
		{
			forecolor = Color.black;
		}
		*/

		Font font = null;
		PdfFont pdfFont = null;
		FontKey key = new FontKey(jrFont.getFontName(), jrFont.isBold(), jrFont.isItalic());

		if (fontMap != null && fontMap.containsKey(key))
		{
			pdfFont = (PdfFont) fontMap.get(key);
		}
		else
		{
			pdfFont = new PdfFont(jrFont.getPdfFontName(), jrFont.getPdfEncoding(), jrFont.isPdfEmbedded());
		}

		try
		{
			font = FontFactory.getFont(
				pdfFont.getPdfFontName(),
				pdfFont.getPdfEncoding(),
				pdfFont.isPdfEmbedded(),
				jrFont.getFontSize(),
				(pdfFont.isPdfSimulatedBold() ? Font.BOLD : 0)
					| (pdfFont.isPdfSimulatedItalic() ? Font.ITALIC : 0)
					| (jrFont.isUnderline() ? Font.UNDERLINE : 0)
					| (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0),
				forecolor
				);

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
				bytes = JRLoader.loadBytesFromLocation(pdfFont.getPdfFontName(), classLoader, urlHandlerFactory);
			}
			catch(JRException e)
			{
				throw
					new JRRuntimeException(
						"Could not load the following font : "
						+ "\npdfFontName   : " + pdfFont.getPdfFontName()
						+ "\npdfEncoding   : " + pdfFont.getPdfEncoding()
						+ "\nisPdfEmbedded : " + pdfFont.isPdfEmbedded(),
						initialException
						);
			}

			BaseFont baseFont = null;

			try
			{
				baseFont =
					BaseFont.createFont(
						pdfFont.getPdfFontName(),
						pdfFont.getPdfEncoding(),
						pdfFont.isPdfEmbedded(),
						true,
						bytes,
						null
						);
			}
			catch(DocumentException e)
			{
				throw new JRRuntimeException(e);
			}
			catch(IOException e)
			{
				throw new JRRuntimeException(e);
			}

			font =
				new Font(
					baseFont,
					jrFont.getFontSize(),
					((pdfFont.isPdfSimulatedBold()) ? Font.BOLD : 0)
						| ((pdfFont.isPdfSimulatedItalic()) ? Font.ITALIC : 0)
						| (jrFont.isUnderline() ? Font.UNDERLINE : 0)
						| (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0),
					forecolor
					);
		}

		return font;
	}


	/**
	 *
	 */
	protected void exportText(JRPrintText text) throws DocumentException
	{
		JRStyledText styledText = getStyledText(text, false);

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
			case JRTextElement.ROTATION_UPSIDE_DOWN :
			{
				x = text.getX() + getOffsetX() + text.getWidth();
				y = text.getY() + getOffsetY() + text.getHeight();
				int tmpPadding = topPadding;
				topPadding = bottomPadding;
				bottomPadding = tmpPadding;
				tmpPadding = leftPadding;
				leftPadding = rightPadding;
				rightPadding = tmpPadding;
				angle = Math.PI;
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
			Color backcolor = text.getBackcolor();
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

		pdfContentByte.setLineDash(0f);
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
				borderCorrection = .5f;
				pdfContentByte.setLineWidth(4f);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderCorrection = .5f;
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


	/**
	 * Output stream implementation that discards all the data.
	 */
	public static class NullOutputStream extends OutputStream
	{
		public NullOutputStream()
		{
		}

		public void write(int b)
		{
			// discard the data
		}

		public void write(byte[] b, int off, int len)
		{
			// discard the data
		}

		public void write(byte[] b)
		{
			// discard the data
		}
	}


	/**
	 *
	 */
	class LocalFontMapper implements FontMapper
	{
		public LocalFontMapper()
		{
		}

		public BaseFont awtToPdf(java.awt.Font font)
		{
			return getFont(font.getAttributes()).getBaseFont();
		}

		public java.awt.Font pdfToAwt(BaseFont font, int size)
		{
			return null;
		}
	}
}
