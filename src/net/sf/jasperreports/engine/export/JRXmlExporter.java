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
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParagraph;
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
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRValueStringUtils;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.XmlValueHandlerUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tools.codec.Base64Encoder;


/**
 * Exports a JasperReports document to an XML file that contains the same data as a {@link net.sf.jasperreports.engine.JasperPrint}
 * object, but in XML format, instead of a serialized class. Such XML files can be parsed back into <tt>JasperPrint</tt>
 * object using the {@link net.sf.jasperreports.engine.xml.JRPrintXmlLoader} utility class. Their structure is validated
 * against an internal XSD file called jasperprint.xsd.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlExporter extends JRAbstractExporter
{

	private static final Log log = LogFactory.getLog(JRXmlExporter.class);
	
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
	
	public static final XmlNamespace JASPERPRINT_NAMESPACE = 
			new XmlNamespace(JRXmlConstants.JASPERPRINT_NAMESPACE, null, JRXmlConstants.JASPERPRINT_XSD_SYSTEM_ID);

	/**
	 *
	 */
	protected JRXmlWriteHelper xmlWriter;
	protected String encoding;
	
	protected JRExportProgressMonitor progressMonitor;
	protected Map<JRRenderable,String> rendererToImagePathMap;
	protected Map<String,byte[]> imageNameToImageDataMap;
//	protected Map fontsMap = new HashMap();
	protected Map<String,JRStyle> stylesMap = new HashMap<String,JRStyle>();

	/**
	 *
	 */
	protected boolean isEmbeddingImages = true;
	protected File destFile;
	protected File imagesDir;

	/**
	 * 
	 */
	private static int imageId;


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
	
			@SuppressWarnings("deprecation")
			String dtdLocation = (String)parameters.get(JRXmlExporterParameter.DTD_LOCATION);
			if (dtdLocation != null)
			{
				log.warn("The JRXmlExporterParameter.DTD_LOCATION export parameter has no effect and should no longer be used.");
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
			rendererToImagePathMap = new HashMap<JRRenderable,String>();
			imageNameToImageDataMap = new HashMap<String,byte[]>();
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
			Collection<String> imageNames = imageNameToImageDataMap.keySet();
			if (imageNames != null && imageNames.size() > 0)
			{
				if (!imagesDir.exists())
				{
					imagesDir.mkdir();
				}
	
				for(Iterator<String> it = imageNames.iterator(); it.hasNext();)
				{
					String imageName = it.next();
					byte[] imageData = imageNameToImageDataMap.get(imageName);

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

	protected XmlNamespace getNamespace()
	{
		return JASPERPRINT_NAMESPACE;
	}

	protected void exportReportToStream(Writer writer) throws JRException, IOException
	{
		xmlWriter = new JRXmlWriteHelper(writer);
		
		xmlWriter.writeProlog(encoding);

		xmlWriter.startElement(JRXmlConstants.ELEMENT_jasperPrint, getNamespace());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, jasperPrint.getName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_pageWidth, jasperPrint.getPageWidth());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_pageHeight, jasperPrint.getPageHeight());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topMargin, jasperPrint.getTopMargin());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftMargin, jasperPrint.getLeftMargin());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomMargin, jasperPrint.getBottomMargin());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightMargin, jasperPrint.getRightMargin());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_orientation, jasperPrint.getOrientationValue(), OrientationEnum.PORTRAIT);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_formatFactoryClass, jasperPrint.getFormatFactoryClass());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_locale, jasperPrint.getLocaleCode());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_timezone, jasperPrint.getTimeZoneId());		
		
		//FIXME this leads to property duplication if a JasperPrint is loaded
		//from a *.jrpxml and exported back to xml
		xmlWriter.startElement(JRXmlConstants.ELEMENT_property);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, PROPERTY_START_PAGE_INDEX);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, String.valueOf(startPageIndex));
		xmlWriter.closeElement();

		xmlWriter.startElement(JRXmlConstants.ELEMENT_property);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, PROPERTY_END_PAGE_INDEX);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, String.valueOf(endPageIndex));
		xmlWriter.closeElement();

		xmlWriter.startElement(JRXmlConstants.ELEMENT_property); //FIXME make this configurable?
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, PROPERTY_PAGE_COUNT);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, jasperPrint.getPages() == null ? null : String.valueOf(jasperPrint.getPages().size()));
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

		JRStyle[] styles = jasperPrint.getStyles();
		if (styles != null && styles.length > 0)
		{
			for(int i = 0; i < styles.length; i++)
			{
				stylesMap.put(styles[i].getName(), styles[i]);
				exportStyle(styles[i]);
			}
		}


		List<JRPrintPage> pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{
			JRPrintPage page = null;
			for(int i = startPageIndex; i <= endPageIndex; i++)
			{
				if (Thread.interrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = pages.get(i);
	
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
					xmlWriter.startElement(JRXmlConstants.ELEMENT_property);
					xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, propertyNames[i]);
					String value = propertiesMap.getProperty(propertyNames[i]);
					if (value != null)
					{
						xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, value);
					}
					xmlWriter.closeElement();
				}
			}
		}
	}


	/**
	 * @throws IOException 
	 */
	protected void exportStyle(JRStyle style) throws IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_style);
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, style.getName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isDefault, style.isDefault(), false);

		if (style.getStyle() != null)
		{
			JRStyle baseStyle = stylesMap.get(style.getStyle().getName());
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
	
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_mode, style.getOwnModeValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_forecolor, style.getOwnForecolor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, style.getOwnBackcolor());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_fill, style.getOwnFillValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_radius, style.getOwnRadius());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, style.getOwnScaleImageValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, style.getOwnHorizontalAlignmentValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, style.getOwnVerticalAlignmentValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, style.getOwnRotationValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_markup, style.getOwnMarkup());
		//xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, style.getOwnPattern());//FIXME if pattern in text field is equal to this, then it should be removed there (inheritance)
		//xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isBlankWhenNull, style.isOwnBlankWhenNull());
		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_fontName, style.getOwnFontName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_fontSize, style.getOwnFontSize());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isBold, style.isOwnBold());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isItalic, style.isOwnItalic());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isUnderline, style.isOwnUnderline());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isStrikeThrough, style.isOwnStrikeThrough());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfFontName, style.getOwnPdfFontName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pdfEncoding, style.getOwnPdfEncoding());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isPdfEmbedded, style.isOwnPdfEmbedded());
		
		exportPen(style.getLinePen());
		exportBox(style.getLineBox());
		exportParagraph(style.getParagraph());
		
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
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_band, origin.getBandTypeValue());
		xmlWriter.closeElement();
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_page);

		exportElements(page.getElements());

		xmlWriter.closeElement();
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	protected void exportElements(Collection<JRPrintElement> elements) throws IOException, JRException
	{
		if (elements != null && elements.size() > 0)
		{
			for(Iterator<JRPrintElement> it = elements.iterator(); it.hasNext();)
			{
				exportElement(it.next());
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
		xmlWriter.startElement(JRXmlConstants.ELEMENT_line);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_direction, line.getDirectionValue(), LineDirectionEnum.TOP_DOWN);

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
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_mode, element.getOwnModeValue());
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
		
		int elementId = element.getSourceElementId();
		if (elementId != JRPrintElement.UNSET_SOURCE_ELEMENT_ID)
		{
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_srcId, elementId);
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
		xmlWriter.startElement(JRXmlConstants.ELEMENT_graphicElement);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_fill, element.getOwnFillValue());
		exportPen(element.getLinePen());
		xmlWriter.closeElement(true);
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPen(JRPen pen) throws IOException
	{
		exportPen(JRXmlConstants.ELEMENT_pen, pen);
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPen(String element, JRPen pen) throws IOException
	{
		xmlWriter.startElement(element);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineWidth, pen.getOwnLineWidth());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineStyle, pen.getOwnLineStyleValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineColor, pen.getOwnLineColor());
		xmlWriter.closeElement(true);
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
	public void exportImage(JRPrintImage image) throws JRException, IOException
	{
		xmlWriter.startElement(JRXmlConstants.ELEMENT_image);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_scaleImage, image.getOwnScaleImageValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hAlign, image.getOwnHorizontalAlignmentValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_vAlign, image.getOwnVerticalAlignmentValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_isLazy, image.isLazy(), false);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_onErrorType, image.getOnErrorTypeValue(), OnErrorTypeEnum.ERROR);
		
		if (hyperlinkProducerFactory == null)
		{
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, image.getLinkType(), HyperlinkTypeEnum.NONE.getName());
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkReference, image.getHyperlinkReference());
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkAnchor, image.getHyperlinkAnchor());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkPage, image.getHyperlinkPage());
		}
		else
		{
			String reference = hyperlinkProducerFactory.produceHyperlink(image);
			if (reference != null)
			{
				xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, HyperlinkTypeEnum.REFERENCE);
				xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkReference, reference);
			}
		}
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, image.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());//FIXMETARGET this exporter is used in the Flash viewer
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTooltip, image.getHyperlinkTooltip());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_anchorName, image.getAnchorName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, image.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);

		exportReportElement(image);
		exportBox(image.getLineBox());
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
					imageSource = rendererToImagePathMap.get(renderer);
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
		xmlWriter.startElement(JRXmlConstants.ELEMENT_text);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_textAlignment, text.getOwnHorizontalAlignmentValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_verticalAlignment, text.getOwnVerticalAlignmentValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_textHeight, text.getTextHeight());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rotation, text.getOwnRotationValue());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_runDirection, text.getRunDirectionValue(), RunDirectionEnum.LTR);
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_markup, text.getOwnMarkup());
		//xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacingFactor, text.getLineSpacingFactor());
		//xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leadingOffset, text.getLeadingOffset());

		if (hyperlinkProducerFactory == null)
		{
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, text.getLinkType());
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkReference, text.getHyperlinkReference());
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkAnchor, text.getHyperlinkAnchor());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkPage, text.getHyperlinkPage());
		}
		else
		{
			String reference = hyperlinkProducerFactory.produceHyperlink(text);
			if (reference != null)
			{
				xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, HyperlinkTypeEnum.REFERENCE);
				xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkReference, reference);
			}
		}
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, text.getLinkTarget());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTooltip, text.getHyperlinkTooltip());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_anchorName, text.getAnchorName());
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, text.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_valueClass, text.getValueClassName());
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_pattern, text.getPattern());		
		xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_formatFactoryClass, text.getFormatFactoryClass());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_locale, text.getLocaleCode());		
		xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_timezone, text.getTimeZoneId());		
		
		exportReportElement(text);
		exportBox(text.getLineBox());

		exportFont(text);
		exportParagraph(text.getParagraph());

		if (text.getOriginalText() != null)
		{
			xmlWriter.writeCDATAElement(JRXmlConstants.ELEMENT_textContent, text.getOriginalText(),
					JRXmlConstants.ATTRIBUTE_truncateIndex, text.getTextTruncateIndex());
		}
		
		xmlWriter.writeCDATAElement(JRXmlConstants.ELEMENT_textTruncateSuffix, text.getTextTruncateSuffix());
		
		short[] lineBreakOffsets = text.getLineBreakOffsets();
		if (lineBreakOffsets != null)
		{
			StringBuffer offsetsString = formatTextLineBreakOffsets(lineBreakOffsets);
			xmlWriter.writeCDATAElement(JRXmlConstants.ELEMENT_lineBreakOffsets, 
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
			xmlWriter.startElement(JRXmlConstants.ELEMENT_box);

			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_padding, box.getOwnPadding());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_topPadding, box.getOwnTopPadding());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftPadding, box.getOwnLeftPadding());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_bottomPadding, box.getOwnBottomPadding());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightPadding, box.getOwnRightPadding());

			exportPen(JRXmlConstants.ELEMENT_pen, box.getPen());
			exportPen(JRXmlConstants.ELEMENT_topPen, box.getTopPen());
			exportPen(JRXmlConstants.ELEMENT_leftPen, box.getLeftPen());
			exportPen(JRXmlConstants.ELEMENT_bottomPen, box.getBottomPen());
			exportPen(JRXmlConstants.ELEMENT_rightPen, box.getRightPen());
			
			xmlWriter.closeElement(true);
		}
	}


	/**
	 * @throws IOException 
	 *
	 */
	private void exportParagraph(JRParagraph paragraph) throws IOException
	{
		if (paragraph != null)
		{
			xmlWriter.startElement(JRXmlConstants.ELEMENT_paragraph);

			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacing, paragraph.getOwnLineSpacing());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_lineSpacingSize, paragraph.getOwnLineSpacingSize());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_firstLineIndent, paragraph.getOwnFirstLineIndent());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_leftIndent, paragraph.getOwnLeftIndent());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_rightIndent, paragraph.getOwnRightIndent());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_spacingBefore, paragraph.getOwnSpacingBefore());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_spacingAfter, paragraph.getOwnSpacingAfter());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_tabStopWidth, paragraph.getOwnTabStopWidth());
			
			/*   */
			TabStop[] tabStops = paragraph.getTabStops();
			if (tabStops != null && tabStops.length > 0)
			{
				for(int i = 0; i < tabStops.length; i++)
				{
					exportTabStop(tabStops[i]);
				}
			}

			xmlWriter.closeElement(true);
		}
	}

	
	/**
	 *
	 */
	public void exportTabStop(TabStop tabStop) throws IOException
	{
		if (tabStop != null)
		{
			xmlWriter.startElement(JRXmlConstants.ELEMENT_tabStop);
			
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_position, tabStop.getPosition());
			xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_alignment, tabStop.getAlignment());

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
			for (Iterator<JRPrintHyperlinkParameter> it = hyperlinkParameters.getParameters().iterator(); it.hasNext();)
			{
				JRPrintHyperlinkParameter parameter = it.next();
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
			xmlWriter.startElement(JRXmlConstants.ELEMENT_genericElement);
			exportReportElement(element);
			
			JRGenericElementType genericType = element.getGenericType();
			xmlWriter.startElement(JRXmlConstants.ELEMENT_genericElementType);
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_namespace, 
					genericType.getNamespace());
			xmlWriter.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, 
					genericType.getName());
			xmlWriter.closeElement();//genericElementType
			
			Set<String> names = element.getParameterNames();
			for (Iterator<String> it = names.iterator(); it.hasNext();)
			{
				String name = it.next();
				Object value = element.getParameterValue(name);
				xmlWriter.startElement(JRXmlConstants.ELEMENT_genericElementParameter);
				xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_name, name);
				if (value != null)
				{
					String valueClass = value.getClass().getName();
					// check if there's a builtin serializer for the value
					boolean builtinSerialization = JRValueStringUtils.hasSerializer(valueClass);
					if (!builtinSerialization)
					{
						// try XML handlers, if none works then default back to the builtin serialization
						builtinSerialization = !XmlValueHandlerUtils.instance().writeToXml(value, this);
					}
					
					if (builtinSerialization)
					{
						String data = JRValueStringUtils.serialize(valueClass, value);
						xmlWriter.startElement(JRXmlConstants.ELEMENT_genericElementParameterValue);
						xmlWriter.addAttribute(JRXmlConstants.ATTRIBUTE_class, valueClass);
						xmlWriter.writeCDATA(data);
						xmlWriter.closeElement();//genericElementParameterValue
					}
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
	
	/**
	 * Returns the XML write helper used by this exporter.
	 * 
	 * The helper can be used to output XML elements and attributes.
	 * 
	 * @return the XML write helper used by this exporter
	 */
	public JRXmlWriteHelper getXmlWriteHelper()
	{
		return xmlWriter;
	}
}
