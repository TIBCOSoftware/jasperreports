/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
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
import java.awt.color.ICC_Profile;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintAnchor;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.export.legacy.BorderOffset;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRPdfaIccProfileNotFoundException;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextAttribute;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfICCBased;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Exports a JasperReports document to PDF format. It has binary output type and exports the document to
 * a free-form layout.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRPdfExporter extends JRAbstractExporter
{

	private static final Log log = LogFactory.getLog(JRPdfExporter.class);
	
	public static final String PDF_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.pdf.";

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String PDF_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "pdf";
	
	private static final String EMPTY_BOOKMARK_TITLE = "";

	/**
	 *
	 */
	protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";

	protected static boolean fontsRegistered;

	protected class ExporterContext extends BaseExporterContext implements JRPdfExporterContext
	{
		public String getExportPropertiesPrefix()
		{
			return PDF_EXPORTER_PROPERTIES_PREFIX;
		}

		public PdfWriter getPdfWriter()
		{
			return pdfWriter;
		}
	}
	
	/**
	 *
	 */
	protected Document document;
	protected PdfContentByte pdfContentByte;
	protected PdfWriter pdfWriter;

	protected Document imageTesterDocument;
	protected PdfContentByte imageTesterPdfContentByte;
	
	protected JRPdfExporterTagHelper tagHelper = new JRPdfExporterTagHelper(this);

	protected JRExportProgressMonitor progressMonitor;

	protected int reportIndex;

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
	protected int permissions;
	protected Character pdfVersion;
	protected String pdfJavaScript;
	protected String printScaling;
	
	private boolean collapseMissingBookmarkLevels;

	/**
	 *
	 */
	protected Map<JRRenderable,com.lowagie.text.Image> loadedImagesMap;
	protected Image pxImage;

	private BookmarkStack bookmarkStack;

	/**
	 * @deprecated
	 */
	private Map<FontKey,PdfFont> fontMap;

	protected JRPdfExporterContext exporterContext = new ExporterContext();
	
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
						JRLoader.loadBytesFromResource("net/sf/jasperreports/engine/images/pixel.GIF")
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
	
			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(PDF_EXPORTER_PROPERTIES_PREFIX);
			}

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

			forceSvgShapes = //FIXME certain properties need to be read from each individual document in batch mode; check all exporters and all props
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

			pdfVersion = 
				getCharacterParameter(
						JRPdfExporterParameter.PDF_VERSION,
						JRPdfExporterParameter.PROPERTY_PDF_VERSION
						);

			fontMap = (Map<FontKey,PdfFont>) parameters.get(JRExporterParameter.FONT_MAP);

			setHyperlinkProducerFactory();

			pdfJavaScript = 
				getStringParameter(
					JRPdfExporterParameter.PDF_JAVASCRIPT,
					JRPdfExporterParameter.PROPERTY_PDF_JAVASCRIPT
					);

			printScaling =
				getStringParameter(
					JRPdfExporterParameter.PRINT_SCALING,
					JRPdfExporterParameter.PROPERTY_PRINT_SCALING
					);

			tagHelper.setTagged( 
				getBooleanParameter(
					JRPdfExporterParameter.IS_TAGGED,
					JRPdfExporterParameter.PROPERTY_TAGGED,
					false
					)
				);
			
			// no export param for this
			collapseMissingBookmarkLevels = JRProperties.getBooleanProperty(jasperPrint, 
					JRPdfExporterParameter.PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS, false);

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
			pdfWriter = PdfWriter.getInstance(document, os);
			pdfWriter.setCloseStream(false);

			tagHelper.setPdfWriter(pdfWriter);
			
			if (pdfVersion != null)
			{
				pdfWriter.setPdfVersion(pdfVersion.charValue());
			}
			if (isCompressed)
			{
				pdfWriter.setFullCompression();
			}
			if (isEncrypted)
			{
				pdfWriter.setEncryption(
					is128BitKey,
					userPassword,
					ownerPassword,
					permissions
					);
			}
			

			if (printScaling != null) 
			{
				if (JRPdfExporterParameter.PRINT_SCALING_DEFAULT.equals(printScaling))
				{
					pdfWriter.addViewerPreference(PdfName.PRINTSCALING, PdfName.APPDEFAULT);
				}
				else if (JRPdfExporterParameter.PRINT_SCALING_NONE.equals(printScaling))
				{
					pdfWriter.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);
				}
			}

			// Add meta-data parameters to generated PDF document
			// mtclough@users.sourceforge.net 2005-12-05
			String title = (String)parameters.get(JRPdfExporterParameter.METADATA_TITLE);
			if( title != null )
			{
				document.addTitle(title);
			}
			String author = (String)parameters.get(JRPdfExporterParameter.METADATA_AUTHOR);
			if( author != null )
			{
				document.addAuthor(author);
			}
			String subject = (String)parameters.get(JRPdfExporterParameter.METADATA_SUBJECT);
			if( subject != null )
			{
				document.addSubject(subject);
			}
			String keywords = (String)parameters.get(JRPdfExporterParameter.METADATA_KEYWORDS);
			if( keywords != null )
			{
				document.addKeywords(keywords);
			}
			String creator = (String)parameters.get(JRPdfExporterParameter.METADATA_CREATOR);
			if( creator != null )
			{
				document.addCreator(creator);
			}
			else
			{
				document.addCreator("JasperReports (" + jasperPrint.getName() + ")");
			}
			
			// BEGIN: PDF/A support
			String pdfaConformance = getStringParameter( JRPdfExporterParameter.PDFA_CONFORMANCE, JRPdfExporterParameter.PROPERTY_PDFA_CONFORMANCE);
			boolean gotPdfa = false;
			if (pdfaConformance != null && !JRPdfExporterParameter.PDFA_CONFORMANCE_NONE.equalsIgnoreCase(pdfaConformance)) 
			{
				if (JRPdfExporterParameter.PDFA_CONFORMANCE_1A.equalsIgnoreCase(pdfaConformance))
				{
					pdfWriter.setPDFXConformance(PdfWriter.PDFA1A);
					gotPdfa = true;
				}
				else if (JRPdfExporterParameter.PDFA_CONFORMANCE_1B.equalsIgnoreCase(pdfaConformance))
				{
					pdfWriter.setPDFXConformance(PdfWriter.PDFA1B);
					gotPdfa = true;
				}
			}

			if (gotPdfa) 
			{
				pdfWriter.createXmpMetadata();
			} else 
			{
				pdfWriter.setRgbTransparencyBlending(true);
			}
			// END: PDF/A support

			document.open();
			
			// BEGIN: PDF/A support
			if (gotPdfa) {
				String iccProfilePath = getStringParameter( JRPdfExporterParameter.PDFA_ICC_PROFILE_PATH, JRPdfExporterParameter.PROPERTY_PDFA_ICC_PROFILE_PATH);
				if (iccProfilePath != null) {
					PdfDictionary pdfDictionary = new PdfDictionary(PdfName.OUTPUTINTENT);
					pdfDictionary.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("sRGB IEC61966-2.1"));
					pdfDictionary.put(PdfName.INFO, new PdfString("sRGB IEC61966-2.1"));
					pdfDictionary.put(PdfName.S, PdfName.GTS_PDFA1);
					
					InputStream iccIs = RepositoryUtil.getInputStream(iccProfilePath);
					PdfICCBased pdfICCBased = new PdfICCBased(ICC_Profile.getInstance(iccIs));
					pdfICCBased.remove(PdfName.ALTERNATE);
					pdfDictionary.put(PdfName.DESTOUTPUTPROFILE, pdfWriter.addToBody(pdfICCBased).getIndirectReference());

					pdfWriter.getExtraCatalog().put(PdfName.OUTPUTINTENTS, new PdfArray(pdfDictionary));
				} else {
					throw new JRPdfaIccProfileNotFoundException();
				}
			}
			// END: PDF/A support
			
			if(pdfJavaScript != null)
			{
				pdfWriter.addJavaScript(pdfJavaScript);
			}

			pdfContentByte = pdfWriter.getDirectContent();

			tagHelper.init(pdfContentByte);
			
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
				setJasperPrint(jasperPrintList.get(reportIndex));
				loadedImagesMap = new HashMap<JRRenderable,com.lowagie.text.Image>();
				
				Rectangle pageSize;
				switch (jasperPrint.getOrientationValue())
				{
				case LANDSCAPE:
					// using rotate to indicate landscape page
					pageSize = new Rectangle(jasperPrint.getPageHeight(),
							jasperPrint.getPageWidth()).rotate();
					break;
				default:
					pageSize = new Rectangle(jasperPrint.getPageWidth(), 
							jasperPrint.getPageHeight());
					break;
				}
				document.setPageSize(pageSize);
				
				BorderOffset.setLegacy(
					JRProperties.getBooleanProperty(jasperPrint, BorderOffset.PROPERTY_LEGACY_BORDER_OFFSET, false)
					);

				List<JRPrintPage> pages = jasperPrint.getPages();
				if (pages != null && pages.size() > 0)
				{
					if (isModeBatch)
					{
						document.newPage();

						if( isCreatingBatchModeBookmarks )
						{
							//add a new level to our outline for this report
							addBookmark(0, jasperPrint.getName(), 0, 0);
						}

						startPageIndex = 0;
						endPageIndex = pages.size() - 1;
					}

					for(int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new JRException("Current thread interrupted.");
						}

						JRPrintPage page = pages.get(pageIndex);

						document.newPage();
						
						pdfContentByte = pdfWriter.getDirectContent();

						pdfContentByte.setLineCap(2);//PdfContentByte.LINE_CAP_PROJECTING_SQUARE since iText 1.02b

						writePageAnchor(pageIndex);

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
				catch (Exception e)
				{
					// ignore, let the original exception propagate
				}

				try
				{
					imageTesterDocument.close();
				}
				catch (Exception e)
				{
					// ignore, let the original exception propagate
				}
			}
		}

		//return os.toByteArray();
	}


	protected void writePageAnchor(int pageIndex) throws DocumentException 
	{
		Map<Attribute,Object> attributes = new HashMap<Attribute,Object>();
		JRFontUtil.getAttributesWithoutAwtFont(attributes, new JRBasePrintText(jasperPrint.getDefaultStyleProvider()));
		Font pdfFont = getFont(attributes, getLocale(), false);
		Chunk chunk = new Chunk(" ", pdfFont);
		
		chunk.setLocalDestination(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + (pageIndex + 1));

		tagHelper.startPageAnchor();
		
		ColumnText colText = new ColumnText(pdfContentByte);
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

		tagHelper.endPageAnchor();
	}

	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, DocumentException, IOException
	{
		tagHelper.startPage();
		
		Collection<JRPrintElement> elements = page.getElements();
		exportElements(elements);
		
		tagHelper.endPage();

		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}

	protected void exportElements(Collection<JRPrintElement> elements) throws DocumentException, IOException, JRException
	{
		if (elements != null && elements.size() > 0)
		{
			for(Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();)
			{
				JRPrintElement element = it.next();

				if (filter == null || filter.isToExport(element))
				{
					tagHelper.startElement(element);

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
						exportFrame((JRPrintFrame)element);
					}
					else if (element instanceof JRGenericPrintElement)
					{
						exportGenericElement((JRGenericPrintElement) element);
					}

					tagHelper.endElement(element);
				}
			}
		}
	}


	/**
	 *
	 */
	protected void exportLine(JRPrintLine line)
	{
		float lineWidth = line.getLinePen().getLineWidth().floatValue(); 
		if (lineWidth > 0f)
		{
			preparePen(pdfContentByte, line.getLinePen(), PdfContentByte.LINE_CAP_BUTT);

			if (line.getWidth() == 1)
			{
				if (line.getHeight() != 1)
				{
					//Vertical line
					if (line.getLinePen().getLineStyleValue() == LineStyleEnum.DOUBLE)
					{
						pdfContentByte.moveTo(
							line.getX() + getOffsetX() + 0.5f - lineWidth / 3,
							jasperPrint.getPageHeight() - line.getY() - getOffsetY()
							);
						pdfContentByte.lineTo(
							line.getX() + getOffsetX() + 0.5f - lineWidth / 3,
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight()
							);

						pdfContentByte.stroke();
						
						pdfContentByte.moveTo(
							line.getX() + getOffsetX() + 0.5f + lineWidth / 3,
							jasperPrint.getPageHeight() - line.getY() - getOffsetY()
							);
						pdfContentByte.lineTo(
							line.getX() + getOffsetX() + 0.5f + lineWidth / 3,
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight()
							);
					}
					else
					{
						pdfContentByte.moveTo(
							line.getX() + getOffsetX() + 0.5f,
							jasperPrint.getPageHeight() - line.getY() - getOffsetY()
							);
						pdfContentByte.lineTo(
							line.getX() + getOffsetX() + 0.5f,
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight()
							);
					}
				}
			}
			else
			{
				if (line.getHeight() == 1)
				{
					//Horizontal line
					if (line.getLinePen().getLineStyleValue() == LineStyleEnum.DOUBLE)
					{
						pdfContentByte.moveTo(
							line.getX() + getOffsetX(),
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - 0.5f + lineWidth / 3
							);
						pdfContentByte.lineTo(
							line.getX() + getOffsetX() + line.getWidth(),
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - 0.5f + lineWidth / 3
							);

						pdfContentByte.stroke();
						
						pdfContentByte.moveTo(
							line.getX() + getOffsetX(),
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - 0.5f - lineWidth / 3
							);
						pdfContentByte.lineTo(
							line.getX() + getOffsetX() + line.getWidth(),
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - 0.5f - lineWidth / 3
							);
					}
					else
					{
						pdfContentByte.moveTo(
							line.getX() + getOffsetX(),
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - 0.5f
							);
						pdfContentByte.lineTo(
							line.getX() + getOffsetX() + line.getWidth(),
							jasperPrint.getPageHeight() - line.getY() - getOffsetY() - 0.5f
							);
					}
				}
				else
				{
					//Oblique line
					if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
					{
						if (line.getLinePen().getLineStyleValue() == LineStyleEnum.DOUBLE)
						{
							double xtrans = lineWidth / (3 * Math.sqrt(1 + Math.pow(line.getWidth(), 2) / Math.pow(line.getHeight(), 2))); 
							double ytrans = lineWidth / (3 * Math.sqrt(1 + Math.pow(line.getHeight(), 2) / Math.pow(line.getWidth(), 2))); 
							
							pdfContentByte.moveTo(
								line.getX() + getOffsetX() + (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() + (float)ytrans
								);
							pdfContentByte.lineTo(
								line.getX() + getOffsetX() + line.getWidth() + (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() + (float)ytrans
								);

							pdfContentByte.stroke();
							
							pdfContentByte.moveTo(
								line.getX() + getOffsetX() - (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - (float)ytrans
								);
							pdfContentByte.lineTo(
								line.getX() + getOffsetX() + line.getWidth() - (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() - (float)ytrans
								);
						}
						else
						{
							pdfContentByte.moveTo(
								line.getX() + getOffsetX(),
								jasperPrint.getPageHeight() - line.getY() - getOffsetY()
								);
							pdfContentByte.lineTo(
								line.getX() + getOffsetX() + line.getWidth(),
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight()
								);
						}
					}
					else
					{
						if (line.getLinePen().getLineStyleValue() == LineStyleEnum.DOUBLE)
						{
							double xtrans = lineWidth / (3 * Math.sqrt(1 + Math.pow(line.getWidth(), 2) / Math.pow(line.getHeight(), 2))); 
							double ytrans = lineWidth / (3 * Math.sqrt(1 + Math.pow(line.getHeight(), 2) / Math.pow(line.getWidth(), 2))); 
							
							pdfContentByte.moveTo(
								line.getX() + getOffsetX() + (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() - (float)ytrans
								);
							pdfContentByte.lineTo(
								line.getX() + getOffsetX() + line.getWidth() + (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - (float)ytrans
								);

							pdfContentByte.stroke();

							pdfContentByte.moveTo(
								line.getX() + getOffsetX() - (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight() + (float)ytrans
								);
							pdfContentByte.lineTo(
								line.getX() + getOffsetX() + line.getWidth() - (float)xtrans,
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() + (float)ytrans
								);
						}
						else
						{
							pdfContentByte.moveTo(
								line.getX() + getOffsetX(),
								jasperPrint.getPageHeight() - line.getY() - getOffsetY() - line.getHeight()
								);
							pdfContentByte.lineTo(
								line.getX() + getOffsetX() + line.getWidth(),
								jasperPrint.getPageHeight() - line.getY() - getOffsetY()
								);
						}
					}
				}
			}

			pdfContentByte.stroke();

			pdfContentByte.setLineDash(0f);
			pdfContentByte.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
		}
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle)
	{
		pdfContentByte.setRGBColorFill(
			rectangle.getBackcolor().getRed(),
			rectangle.getBackcolor().getGreen(),
			rectangle.getBackcolor().getBlue()
			);

		preparePen(pdfContentByte, rectangle.getLinePen(), PdfContentByte.LINE_CAP_PROJECTING_SQUARE);

		float lineWidth = rectangle.getLinePen().getLineWidth().floatValue();
		float lineOffset = BorderOffset.getOffset(rectangle.getLinePen());
		
		if (rectangle.getModeValue() == ModeEnum.OPAQUE)
		{
			pdfContentByte.roundRectangle(
				rectangle.getX() + getOffsetX(),
				jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight(),
				rectangle.getWidth(),
				rectangle.getHeight(),
				rectangle.getRadius()
				);

			pdfContentByte.fill();
		}

		if (lineWidth > 0f)
		{
			if (rectangle.getLinePen().getLineStyleValue() == LineStyleEnum.DOUBLE)
			{
				pdfContentByte.roundRectangle(
					rectangle.getX() + getOffsetX() - lineWidth / 3,
					jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight() - lineWidth / 3,
					rectangle.getWidth() + 2 * lineWidth / 3,
					rectangle.getHeight() + 2 * lineWidth / 3,
					rectangle.getRadius()
					);

				pdfContentByte.stroke();
				
				pdfContentByte.roundRectangle(
					rectangle.getX() + getOffsetX() + lineWidth / 3,
					jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight() + lineWidth / 3,
					rectangle.getWidth() - 2 * lineWidth / 3,
					rectangle.getHeight() - 2 * lineWidth / 3,
					rectangle.getRadius()
					);
				
				pdfContentByte.stroke();
			}
			else
			{
				pdfContentByte.roundRectangle(
					rectangle.getX() + getOffsetX() + lineOffset,
					jasperPrint.getPageHeight() - rectangle.getY() - getOffsetY() - rectangle.getHeight() + lineOffset,
					rectangle.getWidth() - 2 * lineOffset,
					rectangle.getHeight() - 2 * lineOffset,
					rectangle.getRadius()
					);

				pdfContentByte.stroke();
			}
		}

		pdfContentByte.setLineDash(0f);
	}


	/**
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse)
	{
		pdfContentByte.setRGBColorFill(
			ellipse.getBackcolor().getRed(),
			ellipse.getBackcolor().getGreen(),
			ellipse.getBackcolor().getBlue()
			);

		preparePen(pdfContentByte, ellipse.getLinePen(), PdfContentByte.LINE_CAP_PROJECTING_SQUARE);

		float lineWidth = ellipse.getLinePen().getLineWidth().floatValue();
		float lineOffset = BorderOffset.getOffset(ellipse.getLinePen());
		
		if (ellipse.getModeValue() == ModeEnum.OPAQUE)
		{
			pdfContentByte.ellipse(
				ellipse.getX() + getOffsetX(),
				jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight(),
				ellipse.getX() + getOffsetX() + ellipse.getWidth(),
				jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY()
				);

			pdfContentByte.fill();
		}

		if (lineWidth > 0f)
		{
			if (ellipse.getLinePen().getLineStyleValue() == LineStyleEnum.DOUBLE)
			{
				pdfContentByte.ellipse(
					ellipse.getX() + getOffsetX() - lineWidth / 3,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight() - lineWidth / 3,
					ellipse.getX() + getOffsetX() + ellipse.getWidth() + lineWidth / 3,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() + lineWidth / 3
					);

				pdfContentByte.stroke();

				pdfContentByte.ellipse(
					ellipse.getX() + getOffsetX() + lineWidth / 3,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight() + lineWidth / 3,
					ellipse.getX() + getOffsetX() + ellipse.getWidth() - lineWidth / 3,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - lineWidth / 3
					);

				pdfContentByte.stroke();
			}
			else
			{
				pdfContentByte.ellipse(
					ellipse.getX() + getOffsetX() + lineOffset,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - ellipse.getHeight() + lineOffset,
					ellipse.getX() + getOffsetX() + ellipse.getWidth() - lineOffset,
					jasperPrint.getPageHeight() - ellipse.getY() - getOffsetY() - lineOffset
					);

				pdfContentByte.stroke();
			}
		}

		pdfContentByte.setLineDash(0f);
	}


	/**
	 *
	 */
	public void exportImage(JRPrintImage printImage) throws DocumentException, IOException,  JRException
	{
		if (printImage.getModeValue() == ModeEnum.OPAQUE)
		{
			pdfContentByte.setRGBColorFill(
				printImage.getBackcolor().getRed(),
				printImage.getBackcolor().getGreen(),
				printImage.getBackcolor().getBlue()
				);
			pdfContentByte.rectangle(
				printImage.getX() + getOffsetX(),
				jasperPrint.getPageHeight() - printImage.getY() - getOffsetY(),
				printImage.getWidth(),
				- printImage.getHeight()
				);
			pdfContentByte.fill();
		}

		int topPadding = printImage.getLineBox().getTopPadding().intValue();
		int leftPadding = printImage.getLineBox().getLeftPadding().intValue();
		int bottomPadding = printImage.getLineBox().getBottomPadding().intValue();
		int rightPadding = printImage.getLineBox().getRightPadding().intValue();

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
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, printImage.getOnErrorTypeValue());
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

				switch(printImage.getScaleImageValue())
				{
					case CLIP :
					{
						// Image load might fail, from given image data. 
						// Better to test and replace the renderer now, in case of lazy load error.
						renderer = 
							JRImageRenderer.getOnErrorRendererForDimension(
								renderer, 
								printImage.getOnErrorTypeValue()
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
						if (printImage.getModeValue() == ModeEnum.OPAQUE)
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
					case FILL_FRAME :
					{
						if (printImage.isUsingCache() && loadedImagesMap.containsKey(renderer))
						{
							image = loadedImagesMap.get(renderer);
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
										printImage.getOnErrorTypeValue()
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
					case RETAIN_SHAPE :
					default :
					{
						if (printImage.isUsingCache() && loadedImagesMap.containsKey(renderer))
						{
							image = loadedImagesMap.get(renderer);
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
										printImage.getOnErrorTypeValue()
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

						xoffset = (int)(xalignFactor * (availableImageWidth - image.getPlainWidth()));
						yoffset = (int)(yalignFactor * (availableImageHeight - image.getPlainHeight()));

						xoffset = (xoffset < 0 ? 0 : xoffset);
						yoffset = (yoffset < 0 ? 0 : yoffset);

						break;
					}
				}

				if (image != null)
				{
					chunk = new Chunk(image, 0, 0);

					scaledWidth = image.getScaledWidth();
					scaledHeight = image.getScaledHeight();
				}
			}
			else
			{
				double normalWidth = availableImageWidth;
				double normalHeight = availableImageHeight;

				double displayWidth = availableImageWidth;
				double displayHeight = availableImageHeight;

				double ratioX = 1f;
				double ratioY = 1f;
				
				Rectangle2D clip = null;

				Dimension2D dimension = renderer.getDimension();
				if (dimension != null)
				{
					normalWidth = dimension.getWidth();
					normalHeight = dimension.getHeight();
					displayWidth = normalWidth;
					displayHeight = normalHeight;
					
					float xalignFactor = getXAlignFactor(printImage);
					float yalignFactor = getYAlignFactor(printImage);

					switch (printImage.getScaleImageValue())
					{
						case CLIP:
						{
							xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
							yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));
							clip =
								new Rectangle2D.Double(
									- xoffset,
									- yoffset,
									availableImageWidth,
									availableImageHeight
									);
							break;
						}
						case FILL_FRAME:
						{
							ratioX = availableImageWidth / normalWidth;
							ratioY = availableImageHeight / normalHeight;
							normalWidth *= ratioX;
							normalHeight *= ratioY;
							xoffset = 0;
							yoffset = 0;
							break;
						}
						case RETAIN_SHAPE:
						default:
						{
							ratioX = availableImageWidth / normalWidth;
							ratioY = availableImageHeight / normalHeight;
							ratioX = ratioX < ratioY ? ratioX : ratioY;
							ratioY = ratioX;
							normalWidth *= ratioX;
							normalHeight *= ratioY;
							xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
							yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));
							break;
						}
					}
				}

				PdfTemplate template = pdfContentByte.createTemplate((float)displayWidth, (float)displayHeight);

				Graphics2D g = forceSvgShapes
					? template.createGraphicsShapes((float)displayWidth, (float)displayHeight)
					: template.createGraphics(availableImageWidth, availableImageHeight, new LocalFontMapper());

				if (clip != null)
				{
					g.setClip(clip);
				}
				
				if (printImage.getModeValue() == ModeEnum.OPAQUE)
				{
					g.setColor(printImage.getBackcolor());
					g.fillRect(0, 0, (int)displayWidth, (int)displayHeight);
				}

				Rectangle2D rectangle = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);

				renderer.render(g, rectangle);
				g.dispose();

				pdfContentByte.saveState();
				pdfContentByte.addTemplate(
					template,
					(float)ratioX, 0f, 0f, (float)ratioY,
					printImage.getX() + getOffsetX() + xoffset,
					jasperPrint.getPageHeight()
						- printImage.getY() - getOffsetY()
						- (int)normalHeight
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

				tagHelper.startImage(printImage);
				
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

				tagHelper.endImage();
			}
		}


		if (
			printImage.getLineBox().getTopPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getLeftPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getBottomPen().getLineWidth().floatValue() <= 0f &&
			printImage.getLineBox().getRightPen().getLineWidth().floatValue() <= 0f
			)
		{
			if (printImage.getLinePen().getLineWidth().floatValue() > 0f)
			{
				exportPen(printImage.getLinePen(), printImage);
			}
		}
		else
		{
			/*   */
			exportBox(
				printImage.getLineBox(),
				printImage
				);
		}
	}


	private float getXAlignFactor(JRPrintImage printImage)
	{
		float xalignFactor = 0f;
		switch (printImage.getHorizontalAlignmentValue())
		{
			case RIGHT :
			{
				xalignFactor = 1f;
				break;
			}
			case CENTER :
			{
				xalignFactor = 0.5f;
				break;
			}
			case LEFT :
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
		switch (printImage.getVerticalAlignmentValue())
		{
			case BOTTOM :
			{
				yalignFactor = 1f;
				break;
			}
			case MIDDLE :
			{
				yalignFactor = 0.5f;
				break;
			}
			case TOP :
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
		if (link != null)
		{
			switch(link.getHyperlinkTypeValue())
			{
				case REFERENCE :
				{
					if (link.getHyperlinkReference() != null)
					{
						switch(link.getHyperlinkTargetValue())
						{
							case BLANK :
							{
								chunk.setAction(
									PdfAction.javaScript(
										"if (app.viewerVersion < 7)"
											+ "{this.getURL(\"" + link.getHyperlinkReference() + "\");}"
											+ "else {app.launchURL(\"" + link.getHyperlinkReference() + "\", true);};",
										pdfWriter
										)
									);
								break;
							}
							case SELF :
							default :
							{
								chunk.setAnchor(link.getHyperlinkReference());
								break;
							}
						}
					}
					break;
				}
				case LOCAL_ANCHOR :
				{
					if (link.getHyperlinkAnchor() != null)
					{
						chunk.setLocalGoto(link.getHyperlinkAnchor());
					}
					break;
				}
				case LOCAL_PAGE :
				{
					if (link.getHyperlinkPage() != null)
					{
						chunk.setLocalGoto(JR_PAGE_ANCHOR_PREFIX + reportIndex + "_" + link.getHyperlinkPage().toString());
					}
					break;
				}
				case REMOTE_ANCHOR :
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
				case REMOTE_PAGE :
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
				case CUSTOM :
				{
					if (hyperlinkProducerFactory != null)
					{
						String hyperlink = hyperlinkProducerFactory.produceHyperlink(link);
						if (hyperlink != null)
						{
							switch(link.getHyperlinkTargetValue())
							{
								case BLANK :
								{
									chunk.setAction(
										PdfAction.javaScript(
											"if (app.viewerVersion < 7)"
												+ "{this.getURL(\"" + hyperlink + "\");}"
												+ "else {app.launchURL(\"" + hyperlink + "\", true);};",
											pdfWriter
											)
										);
									break;
								}
								case SELF :
								default :
								{
									chunk.setAnchor(hyperlink);
									break;
								}
							}
						}
					}
				}
				case NONE :
				default :
				{
					break;
				}
			}
		}
	}


	/**
	 *
	 */
	protected Phrase getPhrase(AttributedString as, String text, JRPrintText textElement)
	{
		Phrase phrase = new Phrase();
		int runLimit = 0;

		AttributedCharacterIterator iterator = as.getIterator();
		Locale locale = getTextLocale(textElement);
		 
		boolean firstChunk = true;
		while(runLimit < text.length() && (runLimit = iterator.getRunLimit()) <= text.length())
		{
			Map<Attribute,Object> attributes = iterator.getAttributes();
			Chunk chunk = getChunk(attributes, text.substring(iterator.getIndex(), runLimit), locale);
			
			if (firstChunk)
			{
				// only set anchor + bookmark for the first chunk in the text
				setAnchor(chunk, textElement, textElement);
			}
			
			JRPrintHyperlink hyperlink = textElement;
			if (hyperlink.getHyperlinkTypeValue() == HyperlinkTypeEnum.NONE)
			{
				hyperlink = (JRPrintHyperlink)attributes.get(JRTextAttribute.HYPERLINK);
			}
			
			setHyperlinkInfo(chunk, hyperlink);
			phrase.add(chunk);

			iterator.setIndex(runLimit);
			firstChunk = false;
		}

		return phrase;
	}


	/**
	 *
	 */
	protected Chunk getChunk(Map<Attribute,Object> attributes, String text, Locale locale)
	{
		// underline and strikethrough are set on the chunk below
		Font font = getFont(attributes, locale, false);

		Chunk chunk = new Chunk(text, font);
		
		if (hasUnderline(attributes))
		{
			// using the same values as sun.font.Fond2D
			chunk.setUnderline(null, 0, 1f / 18, 0, -1f / 12, 0);
		}
		
		if (hasStrikethrough(attributes))
		{
			// using the same thickness as sun.font.Fond2D.
			// the position is calculated in Fond2D based on the ascent, defaulting 
			// to iText default position which depends on the font size
			chunk.setUnderline(null, 0, 1f / 18, 0, 1f / 3, 0);
		}

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
				chunk.setTextRise(font.getCalculatedLeading(1f)/2);
			}
			else if (TextAttribute.SUPERSCRIPT_SUB.equals(script))
			{
				chunk.setTextRise(-font.getCalculatedLeading(1f)/2);
			}
		}

//		if (splitCharacter != null)
//		{
//			//TODO use line break offsets if available?
//			chunk.setSplitCharacter(splitCharacter);
//		}

		return chunk;
	}

	protected boolean hasUnderline(Map<Attribute,Object> textAttributes)
	{
		Integer underline = (Integer) textAttributes.get(TextAttribute.UNDERLINE);
		return TextAttribute.UNDERLINE_ON.equals(underline);
	}

	protected boolean hasStrikethrough(Map<Attribute,Object> textAttributes)
	{
		Boolean strike = (Boolean) textAttributes.get(TextAttribute.STRIKETHROUGH);
		return TextAttribute.STRIKETHROUGH_ON.equals(strike);
	}


	/**
	 * Creates a PDF font.
	 * 
	 * @param attributes the text attributes of the font
	 * @param locale the locale for which to create the font
	 * @param setFontLines whether to set underline and strikethrough as font style
	 * @return the PDF font for the specified attributes
	 */
	protected Font getFont(Map<Attribute,Object> attributes, Locale locale, boolean setFontLines)
	{
		JRFont jrFont = new JRBaseFont(attributes);

		Exception initialException = null;

		Color forecolor = (Color)attributes.get(TextAttribute.FOREGROUND);

		// use the same font scale ratio as in JRStyledText.getAwtAttributedString
		float fontSizeScale = 1f;
		Integer scriptStyle = (Integer) attributes.get(TextAttribute.SUPERSCRIPT);
		if (scriptStyle != null && (
				TextAttribute.SUPERSCRIPT_SUB.equals(scriptStyle)
				|| TextAttribute.SUPERSCRIPT_SUPER.equals(scriptStyle)))
		{
			fontSizeScale = 2f / 3;
		}
		
		Font font = null;
		PdfFont pdfFont = null;
		FontKey key = new FontKey(jrFont.getFontName(), jrFont.isBold(), jrFont.isItalic());

		if (fontMap != null && fontMap.containsKey(key))
		{
			pdfFont = fontMap.get(key);
		}
		else
		{
			FontInfo fontInfo = JRFontUtil.getFontInfo(jrFont.getFontName(), locale);
			if (fontInfo == null)
			{
				//fontName NOT found in font extensions
				pdfFont = new PdfFont(jrFont.getPdfFontName(), jrFont.getPdfEncoding(), jrFont.isPdfEmbedded());
			}
			else
			{
				//fontName found in font extensions
				FontFamily family = fontInfo.getFontFamily();
				FontFace face = fontInfo.getFontFace();
				int faceStyle = java.awt.Font.PLAIN;

				if (face == null)
				{
					//fontName matches family name in font extension
					if (jrFont.isBold() && jrFont.isItalic())
					{
						face = family.getBoldItalicFace();
						faceStyle = java.awt.Font.BOLD | java.awt.Font.ITALIC;
					}
					
					if (face == null && jrFont.isBold())
					{
						face = family.getBoldFace();
						faceStyle = java.awt.Font.BOLD;
					}
					
					if (face == null && jrFont.isItalic())
					{
						face = family.getItalicFace();
						faceStyle = java.awt.Font.ITALIC;
					}
					
					if (face == null)
					{
						face = family.getNormalFace();
						faceStyle = java.awt.Font.PLAIN;
					}
						
//					if (face == null)
//					{
//						throw new JRRuntimeException("Font family '" + family.getName() + "' does not have the normal font face.");
//					}
				}
				else
				{
					//fontName matches face name in font extension; not family name
					faceStyle = fontInfo.getStyle();
				}
				
				String pdfFontName = null;
				int pdfFontStyle = java.awt.Font.PLAIN;
				if (jrFont.isBold() && jrFont.isItalic())
				{
					pdfFontName = family.getBoldItalicPdfFont();
					pdfFontStyle = java.awt.Font.BOLD | java.awt.Font.ITALIC;
				}
				
				if (pdfFontName == null && jrFont.isBold())
				{
					pdfFontName = family.getBoldPdfFont();
					pdfFontStyle = java.awt.Font.BOLD;
				}
				
				if (pdfFontName == null && jrFont.isItalic())
				{
					pdfFontName = family.getItalicPdfFont();
					pdfFontStyle = java.awt.Font.ITALIC;
				}
				
				if (pdfFontName == null)
				{
					pdfFontName = family.getNormalPdfFont();
					pdfFontStyle = java.awt.Font.PLAIN;
				}

				if (pdfFontName == null)
				{
					//in theory, face file cannot be null here
					pdfFontName = (face == null || face.getFile() == null ? jrFont.getPdfFontName() : face.getFile());
					pdfFontStyle = faceStyle;//FIXMEFONT not sure this is correct, in case we inherit pdfFontName from default properties
				}

//				String ttf = face.getFile();
//				if (ttf == null)
//				{
//					throw new JRRuntimeException("The '" + face.getName() + "' font face in family '" + family.getName() + "' returns a null file.");
//				}
				
				pdfFont = 
					new PdfFont(
						pdfFontName, 
						family.getPdfEncoding() == null ? jrFont.getPdfEncoding() : family.getPdfEncoding(),
 						family.isPdfEmbedded() == null ? jrFont.isPdfEmbedded() : family.isPdfEmbedded().booleanValue(), 
						jrFont.isBold() && ((pdfFontStyle & java.awt.Font.BOLD) == 0), 
						jrFont.isItalic() && ((pdfFontStyle & java.awt.Font.ITALIC) == 0)
						);
			}
		}

		int pdfFontStyle = (pdfFont.isPdfSimulatedBold() ? Font.BOLD : 0)
				| (pdfFont.isPdfSimulatedItalic() ? Font.ITALIC : 0);
		if (setFontLines)
		{
			pdfFontStyle |= (jrFont.isUnderline() ? Font.UNDERLINE : 0)
					| (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0);
		}
		
		try
		{
			font = FontFactory.getFont(
				pdfFont.getPdfFontName(),
				pdfFont.getPdfEncoding(),
				pdfFont.isPdfEmbedded(),
				jrFont.getFontSize() * fontSizeScale,
				pdfFontStyle,
				forecolor
				);

			// check if FontFactory didn't find the font
			if (font.getBaseFont() == null && font.getFamily() == Font.UNDEFINED)
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
				bytes = RepositoryUtil.getBytes(pdfFont.getPdfFontName());
			}
			catch(JRException e)
			{
				throw //NOPMD
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
					jrFont.getFontSize() * fontSizeScale,
					pdfFontStyle,
					forecolor
					);
		}

		return font;
	}


	/**
	 *
	 */
	public void exportText(JRPrintText text) throws DocumentException
	{
		PdfTextRenderer textRenderer = PdfTextRenderer.getInstance();//FIXMETAB optimize this
		
		textRenderer.initialize(this, pdfContentByte, text, getOffsetX(), getOffsetY());
		
		JRStyledText styledText = textRenderer.getStyledText();

		if (styledText == null)
		{
			return;
		}

		double angle = 0;

		switch (text.getRotationValue())
		{
			case LEFT :
			{
				angle = Math.PI / 2;
				break;
			}
			case RIGHT :
			{
				angle = - Math.PI / 2;
				break;
			}
			case UPSIDE_DOWN :
			{
				angle = Math.PI;
				break;
			}
			case NONE :
			default :
			{
			}
		}

		AffineTransform atrans = new AffineTransform();
		atrans.rotate(angle, textRenderer.getX(), jasperPrint.getPageHeight() - textRenderer.getY());
		pdfContentByte.transform(atrans);

		if (text.getModeValue() == ModeEnum.OPAQUE)
		{
			Color backcolor = text.getBackcolor();
			pdfContentByte.setRGBColorFill(
				backcolor.getRed(),
				backcolor.getGreen(),
				backcolor.getBlue()
				);
			pdfContentByte.rectangle(
				textRenderer.getX(),
				jasperPrint.getPageHeight() - textRenderer.getY(),
				textRenderer.getWidth(),
				- textRenderer.getHeight()
				);
			pdfContentByte.fill();
		}

		if (styledText.length() > 0)
		{
			tagHelper.startText();
			
			/*   */
			textRenderer.render();

			tagHelper.endText();
		}

		atrans = new AffineTransform();
		atrans.rotate(-angle, textRenderer.getX(), jasperPrint.getPageHeight() - textRenderer.getY());
		pdfContentByte.transform(atrans);

		/*   */
		exportBox(
			text.getLineBox(),
			text
			);
	}


	/**
	 *
	 */
	protected void exportBox(JRLineBox box, JRPrintElement element)
	{
		exportTopPen(box.getTopPen(), box.getLeftPen(), box.getRightPen(), element);
		exportLeftPen(box.getTopPen(), box.getLeftPen(), box.getBottomPen(), element);
		exportBottomPen(box.getLeftPen(), box.getBottomPen(), box.getRightPen(), element);
		exportRightPen(box.getTopPen(), box.getBottomPen(), box.getRightPen(), element);

		pdfContentByte.setLineDash(0f);
		pdfContentByte.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
	}


	/**
	 *
	 */
	protected void exportPen(JRPen pen, JRPrintElement element)
	{
		exportTopPen(pen, pen, pen, element);
		exportLeftPen(pen, pen, pen, element);
		exportBottomPen(pen, pen, pen, element);
		exportRightPen(pen, pen, pen, element);

		pdfContentByte.setLineDash(0f);
		pdfContentByte.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
	}


	/**
	 *
	 */
	protected void exportTopPen(
		JRPen topPen, 
		JRPen leftPen, 
		JRPen rightPen, 
		JRPrintElement element)
	{
		if (topPen.getLineWidth().floatValue() > 0f)
		{
			float leftOffset = leftPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(leftPen);
			float rightOffset = rightPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(rightPen);
			
			preparePen(pdfContentByte, topPen, PdfContentByte.LINE_CAP_BUTT);
			
			if (topPen.getLineStyleValue() == LineStyleEnum.DOUBLE)
			{
				float topOffset = topPen.getLineWidth().floatValue();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() - leftOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() + topOffset / 3
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() + rightOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() + topOffset / 3
					);
				pdfContentByte.stroke();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() + leftOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - topOffset / 3
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() - rightOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - topOffset / 3
					);
				pdfContentByte.stroke();
			}
			else
			{
				float topOffset =  BorderOffset.getOffset(topPen);
				pdfContentByte.moveTo(
					element.getX() + getOffsetX() - leftOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - topOffset
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() + rightOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - topOffset
					);
				pdfContentByte.stroke();
			}
		}
	}


	/**
	 *
	 */
	protected void exportLeftPen(JRPen topPen, JRPen leftPen, JRPen bottomPen, JRPrintElement element)
	{
		if (leftPen.getLineWidth().floatValue() > 0f)
		{
			float topOffset = topPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(topPen);
			float bottomOffset = bottomPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(bottomPen);

			preparePen(pdfContentByte, leftPen, PdfContentByte.LINE_CAP_BUTT);

			if (leftPen.getLineStyleValue() == LineStyleEnum.DOUBLE)
			{
				float leftOffset = leftPen.getLineWidth().floatValue();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() - leftOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() + topOffset
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() - leftOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() - bottomOffset
					);
				pdfContentByte.stroke();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() + leftOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - topOffset / 3
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + leftOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + bottomOffset / 3
					);
				pdfContentByte.stroke();
			}
			else
			{
				float leftOffset =  BorderOffset.getOffset(leftPen);
				pdfContentByte.moveTo(
					element.getX() + getOffsetX() + leftOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() + topOffset
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + leftOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() - bottomOffset
					);
				pdfContentByte.stroke();
			}
		}
	}


	/**
	 *
	 */
	protected void exportBottomPen(JRPen leftPen, JRPen bottomPen, JRPen rightPen, JRPrintElement element)
	{
		if (bottomPen.getLineWidth().floatValue() > 0f)
		{
			float leftOffset = leftPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(leftPen);
			float rightOffset = rightPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(rightPen);
			
			preparePen(pdfContentByte, bottomPen, PdfContentByte.LINE_CAP_BUTT);
			
			if (bottomPen.getLineStyleValue() == LineStyleEnum.DOUBLE)
			{
				float bottomOffset = bottomPen.getLineWidth().floatValue();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() - leftOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() - bottomOffset / 3
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() + rightOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() - bottomOffset / 3
					);
				pdfContentByte.stroke();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() + leftOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + bottomOffset / 3
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() - rightOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + bottomOffset / 3
					);
				pdfContentByte.stroke();
			}
			else
			{
				float bottomOffset =  BorderOffset.getOffset(bottomPen);
				pdfContentByte.moveTo(
					element.getX() + getOffsetX() - leftOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + bottomOffset
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() + rightOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + bottomOffset
					);
				pdfContentByte.stroke();
			}
		}
	}


	/**
	 *
	 */
	protected void exportRightPen(JRPen topPen, JRPen bottomPen, JRPen rightPen, JRPrintElement element)
	{
		if (rightPen.getLineWidth().floatValue() > 0f)
		{
			float topOffset = topPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(topPen);
			float bottomOffset = bottomPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(bottomPen);

			preparePen(pdfContentByte, rightPen, PdfContentByte.LINE_CAP_BUTT);

			if (rightPen.getLineStyleValue() == LineStyleEnum.DOUBLE)
			{
				float rightOffset = rightPen.getLineWidth().floatValue();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() + element.getWidth() + rightOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() + topOffset
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() + rightOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() - bottomOffset
					);
				pdfContentByte.stroke();

				pdfContentByte.moveTo(
					element.getX() + getOffsetX() + element.getWidth() - rightOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - topOffset / 3
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() - rightOffset / 3,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() + bottomOffset / 3
					);
				pdfContentByte.stroke();
			}
			else
			{
				float rightOffset =  BorderOffset.getOffset(rightPen);
				pdfContentByte.moveTo(
					element.getX() + getOffsetX() + element.getWidth() - rightOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() + topOffset
					);
				pdfContentByte.lineTo(
					element.getX() + getOffsetX() + element.getWidth() - rightOffset,
					jasperPrint.getPageHeight() - element.getY() - getOffsetY() - element.getHeight() - bottomOffset
					);
				pdfContentByte.stroke();
			}
		}
	}


	/**
	 *
	 */
	private static void preparePen(PdfContentByte pdfContentByte, JRPen pen, int lineCap)
	{
		float lineWidth = pen.getLineWidth().floatValue();

		if (lineWidth <= 0)
		{
			return;
		}
		
		pdfContentByte.setLineWidth(lineWidth);
		pdfContentByte.setLineCap(lineCap);

		Color color = pen.getLineColor();
		pdfContentByte.setRGBColorStroke(
			color.getRed(),
			color.getGreen(),
			color.getBlue()
			);

		switch (pen.getLineStyleValue())
		{
			case DOUBLE :
			{
				pdfContentByte.setLineWidth(lineWidth / 3);
				pdfContentByte.setLineDash(0f);
				break;
			}
			case DOTTED :
			{
				switch (lineCap)
				{
					case PdfContentByte.LINE_CAP_BUTT :
					{
						pdfContentByte.setLineDash(lineWidth, lineWidth, 0f);
						break;
					}
					case PdfContentByte.LINE_CAP_PROJECTING_SQUARE :
					{
						pdfContentByte.setLineDash(0, 2 * lineWidth, 0f);
						break;
					}
				}
				break;
			}
			case DASHED :
			{
				switch (lineCap)
				{
					case PdfContentByte.LINE_CAP_BUTT :
					{
						pdfContentByte.setLineDash(5 * lineWidth, 3 * lineWidth, 0f);
						break;
					}
					case PdfContentByte.LINE_CAP_PROJECTING_SQUARE :
					{
						pdfContentByte.setLineDash(4 * lineWidth, 4 * lineWidth, 0f);
						break;
					}
				}
				break;
			}
			case SOLID :
			default :
			{
				pdfContentByte.setLineDash(0f);
				break;
			}
		}
	}


	protected static synchronized void registerFonts ()
	{
		if (!fontsRegistered)
		{
			List<PropertySuffix> fontFiles = JRProperties.getProperties(JRProperties.PDF_FONT_FILES_PREFIX);
			if (!fontFiles.isEmpty())
			{
				for (Iterator<PropertySuffix> i = fontFiles.iterator(); i.hasNext();)
				{
					JRProperties.PropertySuffix font = i.next();
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

			List<PropertySuffix> fontDirs = JRProperties.getProperties(JRProperties.PDF_FONT_DIRS_PREFIX);
			if (!fontDirs.isEmpty())
			{
				for (Iterator<PropertySuffix> i = fontDirs.iterator(); i.hasNext();)
				{
					JRProperties.PropertySuffix dir = i.next();
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
		LinkedList<Bookmark> stack;

		BookmarkStack()
		{
			stack = new LinkedList<Bookmark>();
		}

		void push(Bookmark bookmark)
		{
			stack.add(bookmark);
		}

		Bookmark pop()
		{
			return stack.removeLast();
		}

		Bookmark peek()
		{
			return stack.getLast();
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
		// searching for parent
		while(parent.level >= level)
		{
			bookmarkStack.pop();
			parent = bookmarkStack.peek();
		}

		if (!collapseMissingBookmarkLevels)
		{
			// creating empty bookmarks in order to preserve the bookmark level
			for (int i = parent.level + 1; i < level; ++i)
			{
				Bookmark emptyBookmark = new Bookmark(parent, parent.pdfOutline.getPdfDestination(), EMPTY_BOOKMARK_TITLE);
				bookmarkStack.push(emptyBookmark);
				parent = emptyBookmark;
			}
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
		if (frame.getModeValue() == ModeEnum.OPAQUE)
		{
			int x = frame.getX() + getOffsetX();
			int y = frame.getY() + getOffsetY();

			Color backcolor = frame.getBackcolor();
			pdfContentByte.setRGBColorFill(
				backcolor.getRed(),
				backcolor.getGreen(),
				backcolor.getBlue()
				);
			pdfContentByte.rectangle(
				x,
				jasperPrint.getPageHeight() - y,
				frame.getWidth(),
				- frame.getHeight()
				);
			pdfContentByte.fill();
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

		exportBox(frame.getLineBox(), frame);
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
			// not setting underline and strikethrough as we only need the base font.
			// underline and strikethrough will not work here because PdfGraphics2D
			// doesn't check the font attributes.
			Map<Attribute,Object> atts = new HashMap<Attribute,Object>();
			atts.putAll(font.getAttributes());
			return getFont(atts, null, false).getBaseFont();
		}

		public java.awt.Font pdfToAwt(BaseFont font, int size)
		{
			return null;
		}
	}


	/**
	 *
	 */
	protected void exportGenericElement(JRGenericPrintElement element)
	{
		GenericElementPdfHandler handler = (GenericElementPdfHandler) 
				GenericElementHandlerEnviroment.getHandler(
						element.getGenericType(), PDF_EXPORTER_KEY);
		
		if (handler != null)
		{
			handler.exportElement(exporterContext, element);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("No PDF generic element handler for " 
						+ element.getGenericType());
			}
		}
	}

	
	/**
	 *
	 */
	protected String getExporterKey()
	{
		return PDF_EXPORTER_KEY;
	}
}
