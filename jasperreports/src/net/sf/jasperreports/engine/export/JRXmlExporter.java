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
 * Joakim Sandström - sanjoa@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.util.JRValueStringUtils;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.w3c.tools.codec.Base64Encoder;


/**
 * Exports a JasperReports document to an XML file that contains the same data as a {@link net.sf.jasperreports.engine.JasperPrint}
 * object, but in XML format, instead of a serialized class. Such XML files can be parsed back into <tt>JasperPrint</tt>
 * object using the {@link net.sf.jasperreports.engine.xml.JRPrintXmlLoader} utility class. Their structure is validated
 * against an internal DTD file called jasperprint.dtd
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlExporter extends JRAbstractExporter
{

	/**
	 *
	 */
	protected static final String DEFAULT_XML_ENCODING = "UTF-8";
	protected static final String DEFAULT_OBJECT_TYPE = "java.lang.String";
	protected static final String HTML_FILES_SUFFIX = "_files";
	protected static final String IMAGE_PREFIX = "img_";

	/**
	 *
	 */
	protected JRXmlWriteHelper xmlWriter = null;
	protected String encoding = null;
	
	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageNameToImageDataMap = null;
	protected Map fontsMap = new HashMap();
	protected Map stylesMap = new HashMap();

	/**
	 *
	 */
	protected String dtdLocation = null;
	protected boolean isEmbeddingImages = true;
	protected File destFile = null;
	protected File imagesDir = null;

	/**
	 * 
	 */
	private static int imageId = 0;


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
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
			setPageRange();
	
			dtdLocation = (String)parameters.get(JRXmlExporterParameter.DTD_LOCATION);
			if (dtdLocation == null)
			{
				dtdLocation = JRXmlConstants.JASPERPRINT_SYSTEM_ID;
			}
			
			encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
			if (encoding == null)
			{
				encoding = DEFAULT_XML_ENCODING;
			}
			
			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null)
			{
				StringBuffer buffer = exportReportToBuffer();
				sb.append(buffer.toString());
			}
			else
			{
				Writer outWriter = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (outWriter != null)
				{
					try
					{
						exportReportToStream(outWriter);
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to writer : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
					if (os != null)
					{
						try
						{
							exportReportToStream(new OutputStreamWriter(os, encoding));
						}
						catch (Exception e)
						{
							throw new JRException("Error writing to OutputStream : " + jasperPrint.getName(), e);
						}
					}
					else
					{
						destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
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
						
						imagesDir = new File(destFile.getParent(), destFile.getName() + HTML_FILES_SUFFIX);
						
						Boolean isEmbeddingImagesParameter = (Boolean)parameters.get(JRXmlExporterParameter.IS_EMBEDDING_IMAGES);
						if (isEmbeddingImagesParameter == null)
						{
							isEmbeddingImagesParameter = Boolean.TRUE;
						}
						isEmbeddingImages = isEmbeddingImagesParameter.booleanValue();
						
						exportReportToFile();
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
	protected void exportReportToFile() throws JRException
	{
		//if (!isEmbeddingImages)
		{
			rendererToImagePathMap = new HashMap();
			imageNameToImageDataMap = new HashMap();
		}
				
		Writer writer = null;
		try
		{
			OutputStream fileOutputStream = new FileOutputStream(destFile);
			writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, encoding));
			exportReportToStream(writer);
		}
		catch (IOException e)
		{
			throw new JRException("Error writing to file : " + destFile, e);
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch(IOException e)
				{
				}
			}
		}
		
		if (!isEmbeddingImages)
		{
			Collection imageNames = imageNameToImageDataMap.keySet();
			if (imageNames != null && imageNames.size() > 0)
			{
				if (!imagesDir.exists())
				{
					imagesDir.mkdir();
				}
	
				for(Iterator it = imageNames.iterator(); it.hasNext();)
				{
					String imageName = (String)it.next();
					byte[] imageData = (byte[])imageNameToImageDataMap.get(imageName);

					File imageFile = new File(imagesDir, imageName);

					OutputStream fos = null;
					try
					{
						fos = new FileOutputStream(imageFile);
						fos.write(imageData, 0, imageData.length);
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to image file : " + imageFile, e);
					}
					finally
					{
						if (fos != null)
						{
							try
							{
								fos.close();
							}
							catch(IOException e)
							{
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 *
	 */
	protected StringBuffer exportReportToBuffer() throws JRException
	{
		StringWriter buffer = new StringWriter();
		try
		{
			exportReportToStream(buffer);
		}
		catch (IOException e)
		{
			throw new JRException("Error while exporting report to buffer", e);
		}
		return buffer.getBuffer();
	}


	protected void exportReportToStream(Writer writer) throws JRException, IOException
	{
		xmlWriter = new JRXmlWriteHelper(writer);
		
		xmlWriter.writeProlog(encoding);
		xmlWriter.writePublicDoctype(JRXmlConstants.ELEMENT_jasperPrint, JRXmlConstants.JASPERPRINT_PUBLIC_ID, dtdLocation);

		xmlWriter.startElement(JRXmlConstants.ELEMENT_jasperPrint);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, jasperPrint.getName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_pageWidth, jasperPrint.getPageWidth());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_pageHeight, jasperPrint.getPageHeight());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_orientation, jasperPrint.getOrientation(), JRXmlConstants.getOrientationMap(), JRReport.ORIENTATION_PORTRAIT);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_formatFactoryClass, jasperPrint.getFormatFactoryClass());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_locale, jasperPrint.getLocaleCode());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_timezone, jasperPrint.getTimeZoneId());		
		
		exportProperties(jasperPrint);
		
		JROrigin[] origins = jasperPrint.getOrigins();
		if (origins != null && origins.length > 0)
		{
			for(int i = 0; i < origins.length; i++)
			{
				exportOrigin(origins[i]);
			}
		}

		JRReportFont[] fonts = jasperPrint.getFonts();
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				fontsMap.put(fonts[i].getName(), fonts[i]);
				exportReportFont(fonts[i]);
			}
		}
		
		JRStyle[] styles = jasperPrint.getStyles();
		if (styles != null && styles.length > 0)
		{
			for(int i = 0; i < styles.length; i++)
			{
				stylesMap.put(styles[i].getName(), styles[i]);
				exportStyle(styles[i]);
			}
		}


		List pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{
			JRPrintPage page = null;
			for(int i = startPageIndex; i <= endPageIndex; i++)
			{
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = (JRPrintPage)pages.get(i);
	
				/*   */
				exportPage(page);
			}
		}

		xmlWriter.closeElement();
		
		writer.flush();
	}


	protected void exportProperties(JRPropertiesHolder propertiesHolder) throws IOException
	{
		JRPropertiesMap propertiesMap = propertiesHolder.getPropertiesMap();
		if (propertiesMap != null)
		{
			String[] propertyNames = propertiesMap.getPropertyNames();
			if (propertyNames != null && propertyNames.length > 0)
			{
				for(int i = 0; i < propertyNames.length; i++)
				{
					String value = propertiesMap.getProperty(propertyNames[i]);
					if (value != null)
					{
						xmlWriter.startElement(JRXmlConstants.ELEMENT_property);
						xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, propertyNames[i]);
						xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, value);
						xmlWriter.closeElement();
					}
				}
			}
		}
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportReportFont(JRReportFont font) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_reportFont);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, font.getName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isDefault, font.isDefault());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, font.getFontName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_size, font.getFontSize());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, font.isBold());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, font.isItalic());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, font.isUnderline());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, font.isStrikeThrough());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, font.getPdfFontName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, font.getPdfEncoding());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, font.isPdfEmbedded());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 */
	protected void exportStyle(JRStyle style) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_style);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, style.getName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isDefault, style.isDefault());

		if (style.getStyle() != null)
		{
			JRStyle baseStyle = 
				(JRStyle)stylesMap.get(
						style.getStyle().getName()
					);
			if(baseStyle != null)
			{
				xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, style.getStyle().getName());
			}
			else
			{
				throw 
					new JRRuntimeException(
						"Referenced report style not found : " 
						+ style.getStyle().getName()
						);
			}
		}
	
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_mode, style.getOwnMode(), JRXmlConstants.getModeMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, style.getOwnForecolor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_pen, style.getOwnPen(), JRXmlConstants.getPenMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_fill, style.getOwnFill(), JRXmlConstants.getFillMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_radius, style.getOwnRadius());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, style.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, style.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, style.getOwnRotation(), JRXmlConstants.getRotationMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacing, style.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isStyledText, style.isOwnStyledText());
		//xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, style.getOwnPattern());//FIXME if pattern in text field is equal to this, then it should be removed there (inheritance)
		//xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_border, style.getOwnBorder(), JRXmlConstants.getPenMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_borderColor, style.getOwnBorderColor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_padding, style.getOwnPadding());
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topBorder, style.getOwnTopBorder(), JRXmlConstants.getPenMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topBorderColor, style.getOwnTopBorderColor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topPadding, style.getOwnTopPadding());
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftBorder, style.getOwnLeftBorder(), JRXmlConstants.getPenMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftBorderColor, style.getOwnLeftBorderColor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftPadding, style.getOwnLeftPadding());
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomBorder, style.getOwnBottomBorder(), JRXmlConstants.getPenMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomBorderColor, style.getOwnBottomBorderColor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomPadding, style.getOwnBottomPadding());
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightBorder, style.getOwnRightBorder(), JRXmlConstants.getPenMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightBorderColor, style.getOwnRightBorderColor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightPadding, style.getOwnRightPadding());

		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, style.getOwnFontName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_fontSize, style.getOwnFontSize());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, style.isOwnBold());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, style.isOwnItalic());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());

		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 */
	protected void exportOrigin(JROrigin origin) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_origin);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_report, origin.getReportName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_group, origin.getGroupName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_band, origin.getBandType(), JRXmlConstants.getBandTypeMap());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_page);

		Collection elements = page.getElements();
		exportElements(elements);

		xmlWriter.closeElement();
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void exportElements(Collection elements) throws IOException, JRException
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
	 * @throws IOException 
	 *
	 */
	protected void exportLine(JRPrintLine line) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_line);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_direction, line.getDirection(), JRXmlConstants.getDirectionMap(), JRLine.DIRECTION_TOP_DOWN);

		exportReportElement(line);
		exportGraphicElement(line);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportReportElement(JRPrintElement element) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_reportElement);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_key, element.getKey());
		JRStyle style = element.getStyle();
		if (style != null)
		{
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_style, style.getName());
		}
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_mode, element.getOwnMode(), JRXmlConstants.getModeMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_x, element.getX() + getOffsetX());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_y, element.getY() + getOffsetY());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_width, element.getWidth());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_height, element.getHeight());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, element.getOwnForecolor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, element.getOwnBackcolor());
		JROrigin origin = element.getOrigin();
		if (origin != null)
		{
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_origin, jasperPrint.getOriginsMap().get(origin));
		}
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportGraphicElement(JRPrintGraphicElement element) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_graphicElement);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_pen, element.getOwnPen(), JRXmlConstants.getPenMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_fill, element.getOwnFill(), JRXmlConstants.getFillMap());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_rectangle);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_radius, rectangle.getOwnRadius());

		exportReportElement(rectangle);
		exportGraphicElement(rectangle);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_ellipse);

		exportReportElement(ellipse);
		exportGraphicElement(ellipse);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws JRException 
	 * @throws IOException 
	 *
	 */
	protected void exportImage(JRPrintImage image) throws JRException, IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_image);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, image.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, image.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, image.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isLazy, image.isLazy(), false);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_onErrorType, image.getOnErrorType(), JRXmlConstants.getOnErrorTypeMap(), JRImage.ON_ERROR_TYPE_ERROR);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, image.getLinkType());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, image.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_anchorName, image.getAnchorName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkReference, image.getHyperlinkReference());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkAnchor, image.getHyperlinkAnchor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkPage, image.getHyperlinkPage());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTooltip, image.getHyperlinkTooltip());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		exportReportElement(image);
		exportBox(image);
		exportGraphicElement(image);
		

		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			xmlWriter.startElement(JRXmlConstants.ELEMENT_imageSource);
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isEmbedded, isEmbeddingImages && !image.isLazy(), false);
	
			String imageSource = "";
			
			if (renderer.getType() == JRRenderable.TYPE_SVG)
			{
				renderer = 
					new JRWrappingSvgRenderer(
						renderer, 
						new Dimension(image.getWidth(), image.getHeight()),
						JRElement.MODE_OPAQUE == image.getMode() ? image.getBackcolor() : null
						);
			}
				
			if (isEmbeddingImages && !image.isLazy())
			{
				try
				{
					ByteArrayInputStream bais = new ByteArrayInputStream(renderer.getImageData());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					Base64Encoder encoder = new Base64Encoder(bais, baos);
					encoder.process();
					
					imageSource = new String(baos.toByteArray(), DEFAULT_XML_ENCODING);
				}
				catch (IOException e)
				{
					throw new JRException("Error embedding image into XML.", e);
				}
			}
			else
			{
				if (renderer.getType() == JRRenderable.TYPE_IMAGE && rendererToImagePathMap.containsKey(renderer))
				{
					imageSource = (String)rendererToImagePathMap.get(renderer);
				}
				else
				{
					if (image.isLazy())
					{
						imageSource = ((JRImageRenderer)renderer).getImageLocation();
					}
					else
					{
						imageSource = IMAGE_PREFIX + getNextImageId();
						imageNameToImageDataMap.put(imageSource, renderer.getImageData());
						
						imageSource = new File(imagesDir, imageSource).getPath();
					}

					rendererToImagePathMap.put(renderer, imageSource);
				}
			}
			
			xmlWriter.writeCDATA(imageSource);
			xmlWriter.closeElement();
		}
		
		exportHyperlinkParameters(image);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportText(JRPrintText text) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_text);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_textAlignment, text.getOwnHorizontalAlignment(), JRXmlConstants.getHorizontalAlignMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_verticalAlignment, text.getOwnVerticalAlignment(), JRXmlConstants.getVerticalAlignMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_textHeight, text.getTextHeight());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, text.getOwnRotation(), JRXmlConstants.getRotationMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_runDirection, text.getRunDirection(), JRXmlConstants.getRunDirectionMap(), JRPrintText.RUN_DIRECTION_LTR);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacing, text.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isStyledText, text.isOwnStyledText());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacingFactor, text.getLineSpacingFactor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leadingOffset, text.getLeadingOffset());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, text.getLinkType());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, text.getHyperlinkTarget(), JRXmlConstants.getHyperlinkTargetMap(), JRHyperlink.HYPERLINK_TARGET_SELF);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_anchorName, text.getAnchorName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkReference, text.getHyperlinkReference());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkAnchor, text.getHyperlinkAnchor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkPage, text.getHyperlinkPage());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTooltip, text.getHyperlinkTooltip());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, text.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_valueClass, text.getValueClassName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, text.getPattern());		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_formatFactoryClass, text.getFormatFactoryClass());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_locale, text.getLocaleCode());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_timezone, text.getTimeZoneId());		
		
		exportReportElement(text);
		exportBox(text);

		exportFont(text);

		if (text.getText() != null)
		{
			xmlWriter.writeCDATAElement(JRXmlConstants.ELEMENT_textContent, text.getText());
		}
		
		exportHyperlinkParameters(text);

		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	private void exportBox(JRBox box) throws IOException
	{
		if (box != null)
		{
			xmlWriter.startElement(JRXmlConstants.ELEMENT_box);

			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_border, box.getOwnBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_borderColor, box.getOwnBorderColor());			
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_padding, box.getOwnPadding());
		
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topBorder, box.getOwnTopBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topBorderColor, box.getOwnTopBorderColor());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topPadding, box.getOwnTopPadding());
		
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftBorder, box.getOwnLeftBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftBorderColor, box.getOwnLeftBorderColor());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftPadding, box.getOwnLeftPadding());
		
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomBorder, box.getOwnBottomBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomBorderColor, box.getOwnBottomBorderColor());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomPadding, box.getOwnBottomPadding());

		
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightBorder, box.getOwnRightBorder(), JRXmlConstants.getPenMap());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightBorderColor, box.getOwnRightBorderColor());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightPadding, box.getOwnRightPadding());
		
			xmlWriter.closeElement(true);
		}
	}


	/**
	 *
	 */
	protected void exportFont(JRFont font) throws IOException
	{
		if (font != null)
		{
			xmlWriter.startElement(JRXmlConstants.ELEMENT_font);

			if(font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_reportFont, font.getReportFont().getName());
				}
				else
				{
					throw 
						new JRRuntimeException(
							"Referenced report font not found : " 
							+ font.getReportFont().getName()
							);
				}
			}
		
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, font.getOwnFontName());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_size, font.getOwnFontSize());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, font.isOwnBold());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, font.isOwnItalic());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, font.isOwnUnderline());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, font.isOwnStrikeThrough());
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, font.getOwnPdfFontName());
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, font.getOwnPdfEncoding());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, font.isOwnPdfEmbedded());
			xmlWriter.closeElement(true);
		}
	}
	
	
	protected void exportFrame(JRPrintFrame frame) throws IOException, JRException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_frame);
		
		setFrameElementsOffset(frame, true);
		try
		{
			exportReportElement(frame);
			exportBox(frame);
			exportElements(frame.getElements());

			xmlWriter.closeElement();
		}
		finally
		{
			restoreElementOffsets();
		}
	}
	
	
	/**
	 * 
	 */
	private static synchronized int getNextImageId(){
		return imageId++;
	}


	protected void exportHyperlinkParameters(JRPrintHyperlink hyperlink) throws IOException
	{
		JRPrintHyperlinkParameters hyperlinkParameters = hyperlink.getHyperlinkParameters();
		if (hyperlinkParameters != null)
		{
			for (Iterator it = hyperlinkParameters.getParameters().iterator(); it.hasNext();)
			{
				JRPrintHyperlinkParameter parameter = (JRPrintHyperlinkParameter) it.next();
				exportHyperlinkParameter(parameter);
			}
		}
	}


	protected void exportHyperlinkParameter(JRPrintHyperlinkParameter parameter) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_hyperlinkParameter);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, parameter.getName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_class, parameter.getValueClass(), DEFAULT_OBJECT_TYPE);
		
		if (parameter.getValue() != null)
		{
			String data = JRValueStringUtils.serialize(parameter.getValueClass(), parameter.getValue());
			xmlWriter.writeCDATAElement(JRXmlConstants.ELEMENT_hyperlinkParameterValue, data);
		}
		
		xmlWriter.closeElement();
	}
}
