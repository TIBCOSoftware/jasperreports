/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 * Joakim Sandstr�m - sanjoa@users.sourceforge.net
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
import java.util.Set;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPen;
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
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRValueStringUtils;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.XmlConstants;

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
	private static final String XML_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xml.";

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String XML_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "xml";

	private static final String PROPERTY_START_PAGE_INDEX = JRProperties.PROPERTY_PREFIX + "export.xml.start.page.index";
	private static final String PROPERTY_END_PAGE_INDEX = JRProperties.PROPERTY_PREFIX + "export.xml.end.page.index";
	private static final String PROPERTY_PAGE_COUNT = JRProperties.PROPERTY_PREFIX + "export.xml.page.count";
	protected static final String DEFAULT_XML_ENCODING = "UTF-8";
	protected static final String DEFAULT_OBJECT_TYPE = "java.lang.String";
	protected static final String XML_FILES_SUFFIX = "_files";
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


	protected class ExporterContext extends BaseExporterContext implements JRXmlExporterContext
	{
		public String getExportPropertiesPrefix()
		{
			return JRXmlExporter.this.getExporterPropertiesPrefix();
		}
	}
	
	protected JRXmlExporterContext exporterContext = new ExporterContext();


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
	
			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(getExporterPropertiesPrefix());
			}

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
			
			setHyperlinkProducerFactory();

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
						
						imagesDir = new File(destFile.getParent(), destFile.getName() + XML_FILES_SUFFIX);
						
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
	protected void setHyperlinkProducerFactory()//FIXMETARGET check if we really need to override this
	{
		hyperlinkProducerFactory = (JRHyperlinkProducerFactory) parameters.get(JRExporterParameter.HYPERLINK_PRODUCER_FACTORY);
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
		xmlWriter.writePublicDoctype(XmlConstants.ELEMENT_jasperPrint, JRXmlConstants.JASPERPRINT_PUBLIC_ID, dtdLocation);

		xmlWriter.startElement(XmlConstants.ELEMENT_jasperPrint);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, jasperPrint.getName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_pageWidth, jasperPrint.getPageWidth());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_pageHeight, jasperPrint.getPageHeight());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_orientation, jasperPrint.getOrientation(), JRXmlConstants.getOrientationMap(), JRReport.ORIENTATION_PORTRAIT);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_formatFactoryClass, jasperPrint.getFormatFactoryClass());		
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_locale, jasperPrint.getLocaleCode());		
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_timezone, jasperPrint.getTimeZoneId());		
		
		//FIXME this leads to property duplication if a JasperPrint is loaded
		//from a *.jrpxml and exported back to xml
		xmlWriter.startElement(XmlConstants.ELEMENT_property);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, PROPERTY_START_PAGE_INDEX);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_value, String.valueOf(startPageIndex));
		xmlWriter.closeElement();

		xmlWriter.startElement(XmlConstants.ELEMENT_property);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, PROPERTY_END_PAGE_INDEX);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_value, String.valueOf(endPageIndex));
		xmlWriter.closeElement();

		xmlWriter.startElement(XmlConstants.ELEMENT_property); //FIXME make this configurable?
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, PROPERTY_PAGE_COUNT);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_value, jasperPrint.getPages() == null ? null : String.valueOf(jasperPrint.getPages().size()));
		xmlWriter.closeElement();

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
				if (Thread.interrupted())
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
		if (propertiesHolder.hasProperties())
		{
			JRPropertiesMap propertiesMap = propertiesHolder.getPropertiesMap();
			String[] propertyNames = propertiesMap.getPropertyNames();
			if (propertyNames != null && propertyNames.length > 0)
			{
				for(int i = 0; i < propertyNames.length; i++)
				{
					xmlWriter.startElement(XmlConstants.ELEMENT_property);
					xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, propertyNames[i]);
					String value = propertiesMap.getProperty(propertyNames[i]);
					if (value != null)
					{
						xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_value, value);
					}
					xmlWriter.closeElement();
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
		xmlWriter.startElement(XmlConstants.ELEMENT_reportFont);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, font.getName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isDefault, font.isDefault());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_fontName, font.getFontName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_size, font.getFontSize());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isBold, font.isBold());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isItalic, font.isItalic());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isUnderline, font.isUnderline());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isStrikeThrough, font.isStrikeThrough());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfFontName, font.getPdfFontName());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfEncoding, font.getPdfEncoding());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isPdfEmbedded, font.isPdfEmbedded());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 */
	protected void exportStyle(JRStyle style) throws IOException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_style);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, style.getName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isDefault, style.isDefault());

		if (style.getStyle() != null)
		{
			JRStyle baseStyle = 
				(JRStyle)stylesMap.get(
						style.getStyle().getName()
					);
			if(baseStyle != null)
			{
				xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_style, style.getStyle().getName());
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
	
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_mode, style.getOwnModeValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_forecolor, style.getOwnForecolor());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_fill, style.getOwnFill(), JRXmlConstants.getFillMap());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_radius, style.getOwnRadius());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_scaleImage, style.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignmentValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_vAlign, style.getOwnVerticalAlignmentValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_rotation, style.getOwnRotationValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_lineSpacing, style.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_markup, style.getOwnMarkup());
		//xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pattern, style.getOwnPattern());//FIXME if pattern in text field is equal to this, then it should be removed there (inheritance)
		//xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_fontName, style.getOwnFontName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_fontSize, style.getOwnFontSize());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isBold, style.isOwnBold());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isItalic, style.isOwnItalic());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());
		
		exportPen(style.getLinePen());
		exportBox(style.getLineBox());
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 */
	protected void exportOrigin(JROrigin origin) throws IOException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_origin);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_report, origin.getReportName());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_group, origin.getGroupName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_band, origin.getBandTypeValue());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_page);

		exportElements(page.getElements());

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
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				exportElement((JRPrintElement)it.next());
			}
		}
	}


	public void exportElement(JRPrintElement element) throws IOException, JRException
	{
		if (filter == null || filter.isToExport(element))
		{
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
			else if (element instanceof JRGenericPrintElement)
			{
				exportGenericElement((JRGenericPrintElement) element);
			}
		}
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportLine(JRPrintLine line) throws IOException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_line);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_direction, line.getDirection(), JRXmlConstants.getDirectionMap(), JRLine.DIRECTION_TOP_DOWN);

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
		xmlWriter.startElement(XmlConstants.ELEMENT_reportElement);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_key, element.getKey());
		JRStyle style = element.getStyle();
		if (style != null)
		{
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_style, style.getName());
		}
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_mode, element.getOwnModeValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_x, element.getX() + getOffsetX());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_y, element.getY() + getOffsetY());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_width, element.getWidth());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_height, element.getHeight());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_forecolor, element.getOwnForecolor());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_backcolor, element.getOwnBackcolor());
		JROrigin origin = element.getOrigin();
		if (origin != null)
		{
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_origin, jasperPrint.getOriginsMap().get(origin));
		}
		
		exportProperties(element);
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportGraphicElement(JRPrintGraphicElement element) throws IOException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_graphicElement);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_fill, element.getOwnFill(), JRXmlConstants.getFillMap());
		exportPen(element.getLinePen());
		xmlWriter.closeElement(true);
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPen(JRPen pen) throws IOException
	{
		exportPen(XmlConstants.ELEMENT_pen, pen);
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPen(String element, JRPen pen) throws IOException
	{
		xmlWriter.startElement(element);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_lineWidth, pen.getOwnLineWidth());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_lineStyle, pen.getOwnLineStyleValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_lineColor, pen.getOwnLineColor());
		xmlWriter.closeElement(true);
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) throws IOException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_rectangle);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_radius, rectangle.getOwnRadius());

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
		xmlWriter.startElement(XmlConstants.ELEMENT_ellipse);

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
		xmlWriter.startElement(XmlConstants.ELEMENT_image);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_scaleImage, image.getOwnScaleImage(), JRXmlConstants.getScaleImageMap());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hAlign, image.getOwnHorizontalAlignmentValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_vAlign, image.getOwnVerticalAlignmentValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isLazy, image.isLazy(), false);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_onErrorType, image.getOnErrorType(), JRXmlConstants.getOnErrorTypeMap(), JRImage.ON_ERROR_TYPE_ERROR);
		
		if (hyperlinkProducerFactory == null)
		{
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, image.getLinkType(), JRHyperlinkHelper.HYPERLINK_TYPE_NONE);
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkReference, image.getHyperlinkReference());
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkAnchor, image.getHyperlinkAnchor());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hyperlinkPage, image.getHyperlinkPage());
		}
		else
		{
			String reference = hyperlinkProducerFactory.produceHyperlink(image);
			if (reference != null)
			{
				xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, JRHyperlinkHelper.HYPERLINK_TYPE_REFERENCE);
				xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkReference, reference);
			}
		}
		
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hyperlinkTarget, image.getLinkTarget(), JRHyperlinkHelper.HYPERLINK_TARGET_SELF);//FIXMETARGET this exporter is used in the Flash viewer
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkTooltip, image.getHyperlinkTooltip());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_anchorName, image.getAnchorName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_bookmarkLevel, image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		exportReportElement(image);
		exportBox(image.getLineBox());
		exportGraphicElement(image);
		

		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			xmlWriter.startElement(XmlConstants.ELEMENT_imageSource);
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isEmbedded, isEmbeddingImages && !image.isLazy(), false);
	
			String imageSource = "";
			
			if (renderer.getType() == JRRenderable.TYPE_SVG)
			{
				renderer = 
					new JRWrappingSvgRenderer(
						renderer, 
						new Dimension(image.getWidth(), image.getHeight()),
						ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
						);
			}
				
			if (image.isLazy())
			{
				imageSource = ((JRImageRenderer)renderer).getImageLocation();
			}
			else if (isEmbeddingImages)
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
					imageSource = IMAGE_PREFIX + getNextImageId();
					imageNameToImageDataMap.put(imageSource, renderer.getImageData());
					
					imageSource = new File(imagesDir, imageSource).getPath();
					rendererToImagePathMap.put(renderer, imageSource);
				}
			}
			
			xmlWriter.writeCDATA(imageSource);
			xmlWriter.closeElement();
		}
		
		if (hyperlinkProducerFactory == null)
		{
			exportHyperlinkParameters(image);
		}
		
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	public void exportText(JRPrintText text) throws IOException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_text);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_textAlignment, text.getOwnHorizontalAlignmentValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_verticalAlignment, text.getOwnVerticalAlignmentValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_textHeight, text.getTextHeight());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_rotation, text.getOwnRotationValue());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_runDirection, text.getRunDirectionValue(), RunDirectionEnum.LTR);
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_lineSpacing, text.getOwnLineSpacing(), JRXmlConstants.getLineSpacingMap());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_markup, text.getOwnMarkup());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_lineSpacingFactor, text.getLineSpacingFactor());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_leadingOffset, text.getLeadingOffset());

		if (hyperlinkProducerFactory == null)
		{
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, text.getLinkType());
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkReference, text.getHyperlinkReference());
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkAnchor, text.getHyperlinkAnchor());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hyperlinkPage, text.getHyperlinkPage());
		}
		else
		{
			String reference = hyperlinkProducerFactory.produceHyperlink(text);
			if (reference != null)
			{
				xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hyperlinkType, JRHyperlinkHelper.HYPERLINK_TYPE_REFERENCE);
				xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkReference, reference);
			}
		}
		
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_hyperlinkTarget, text.getLinkTarget());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_hyperlinkTooltip, text.getHyperlinkTooltip());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_anchorName, text.getAnchorName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_bookmarkLevel, text.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_valueClass, text.getValueClassName());
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pattern, text.getPattern());		
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_formatFactoryClass, text.getFormatFactoryClass());		
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_locale, text.getLocaleCode());		
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_timezone, text.getTimeZoneId());		
		
		exportReportElement(text);
		exportBox(text.getLineBox());

		exportFont(text);

		if (text.getOriginalText() != null)
		{
			xmlWriter.writeCDATAElement(XmlConstants.ELEMENT_textContent, text.getOriginalText(),
					XmlConstants.ATTRIBUTE_truncateIndex, text.getTextTruncateIndex());
		}
		
		xmlWriter.writeCDATAElement(XmlConstants.ELEMENT_textTruncateSuffix, text.getTextTruncateSuffix());
		
		short[] lineBreakOffsets = text.getLineBreakOffsets();
		if (lineBreakOffsets != null)
		{
			StringBuffer offsetsString = formatTextLineBreakOffsets(lineBreakOffsets);
			xmlWriter.writeCDATAElement(XmlConstants.ELEMENT_lineBreakOffsets, 
					offsetsString.toString());
		}
		
		if (hyperlinkProducerFactory == null)
		{
			exportHyperlinkParameters(text);
		}

		xmlWriter.closeElement();
	}


	protected StringBuffer formatTextLineBreakOffsets(short[] lineBreakOffsets)
	{
		StringBuffer offsetsString = new StringBuffer();
		for (int i = 0; i < lineBreakOffsets.length; i++)
		{
			if (i > 0)
			{
				offsetsString.append(JRXmlConstants.LINE_BREAK_OFFSET_SEPARATOR);
			}
			offsetsString.append(lineBreakOffsets[i]);
		}
		return offsetsString;
	}


	/**
	 * @throws IOException 
	 *
	 */
	private void exportBox(JRLineBox box) throws IOException
	{
		if (box != null)
		{
			xmlWriter.startElement(XmlConstants.ELEMENT_box);

			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_padding, box.getOwnPadding());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_topPadding, box.getOwnTopPadding());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_leftPadding, box.getOwnLeftPadding());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_bottomPadding, box.getOwnBottomPadding());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_rightPadding, box.getOwnRightPadding());

			exportPen(XmlConstants.ELEMENT_pen, box.getPen());
			exportPen(XmlConstants.ELEMENT_topPen, box.getTopPen());
			exportPen(XmlConstants.ELEMENT_leftPen, box.getLeftPen());
			exportPen(XmlConstants.ELEMENT_bottomPen, box.getBottomPen());
			exportPen(XmlConstants.ELEMENT_rightPen, box.getRightPen());
			
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
			xmlWriter.startElement(XmlConstants.ELEMENT_font);

			if(font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_reportFont, font.getReportFont().getName());
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
		
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_fontName, font.getOwnFontName());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_size, font.getOwnFontSize());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isBold, font.isOwnBold());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isItalic, font.isOwnItalic());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isUnderline, font.isOwnUnderline());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isStrikeThrough, font.isOwnStrikeThrough());
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfFontName, font.getOwnPdfFontName());
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_pdfEncoding, font.getOwnPdfEncoding());
			xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_isPdfEmbedded, font.isOwnPdfEmbedded());
			xmlWriter.closeElement(true);
		}
	}
	
	
	protected void exportFrame(JRPrintFrame frame) throws IOException, JRException
	{
		xmlWriter.startElement(XmlConstants.ELEMENT_frame);
		
		setFrameElementsOffset(frame, true);
		try
		{
			exportReportElement(frame);
			exportBox(frame.getLineBox());
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
		xmlWriter.startElement(XmlConstants.ELEMENT_hyperlinkParameter);
		xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, parameter.getName());
		xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_class, parameter.getValueClass(), DEFAULT_OBJECT_TYPE);
		
		if (parameter.getValue() != null)
		{
			String data = JRValueStringUtils.serialize(parameter.getValueClass(), parameter.getValue());
			xmlWriter.writeCDATAElement(XmlConstants.ELEMENT_hyperlinkParameterValue, data);
		}
		
		xmlWriter.closeElement();
	}


	/**
	 *
	 */
	protected void exportGenericElement(JRGenericPrintElement element) throws IOException
	{
		GenericElementXmlHandler handler = (GenericElementXmlHandler) 
		GenericElementHandlerEnviroment.getHandler(
				element.getGenericType(), getExporterKey());

		if (handler != null)
		{
			handler.exportElement(exporterContext, element);
		}
		else
		{
			xmlWriter.startElement(XmlConstants.ELEMENT_genericElement);
			exportReportElement(element);
			
			JRGenericElementType genericType = element.getGenericType();
			xmlWriter.startElement(XmlConstants.ELEMENT_genericElementType);
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_namespace, 
					genericType.getNamespace());
			xmlWriter.addEncodedAttribute(XmlConstants.ATTRIBUTE_name, 
					genericType.getName());
			xmlWriter.closeElement();//genericElementType
			
			Set names = element.getParameterNames();
			for (Iterator it = names.iterator(); it.hasNext();)
			{
				String name = (String) it.next();
				Object value = element.getParameterValue(name);
				xmlWriter.startElement(XmlConstants.ELEMENT_genericElementParameter);
				xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_name, name);
				if (value != null)
				{
					String valueClass = value.getClass().getName();
					String data = JRValueStringUtils.serialize(valueClass, value);
					xmlWriter.startElement(XmlConstants.ELEMENT_genericElementParameterValue);
					xmlWriter.addAttribute(XmlConstants.ATTRIBUTE_class, valueClass);
					xmlWriter.writeCDATA(data);
					xmlWriter.closeElement();//genericElementParameterValue
				}
				xmlWriter.closeElement();//genericElementParameter
			}
			
			xmlWriter.closeElement();//genericElement
		}
	}


	/**
	 *
	 */
	protected String getExporterPropertiesPrefix()
	{
		return XML_EXPORTER_PROPERTIES_PREFIX;
	}

	
	/**
	 *
	 */
	protected String getExporterKey()
	{
		return XML_EXPORTER_KEY;
	}
}
