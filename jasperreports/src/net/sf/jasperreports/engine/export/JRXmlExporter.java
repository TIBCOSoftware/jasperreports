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
 * Joakim Sandström - sanjoa@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
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
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.w3c.tools.codec.Base64Encoder;


/**
 * Exports a JasperReports document to an XML file that contains the same data as a {@link net.sf.jasperreports.engine.JasperPrint}
 * object, but in XML format, instead of a serialized class. Such XML files can be parsed back into <tt>JasperPrint</tt>
 * object using the {@link net.sf.jasperreports.engine.xml.JRPrintXmlLoader} utility class. Their structure is validated
 * against an internal DTD file called jasperprint.dtd
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlExporter extends JRAbstractExporter
{

	/**
	 *
	 */
	protected Writer writer = null;
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
	protected static final int colorMask = Integer.parseInt("FFFFFF", 16);
	
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

		/*   */
		setClassLoader();

		/*   */
		setInput();

		/*   */
		setPageRange();

		dtdLocation = (String)parameters.get(JRXmlExporterParameter.DTD_LOCATION);
		if (dtdLocation == null)
		{
			dtdLocation = "http://jasperreports.sourceforge.net/dtds/jasperprint.dtd";
		}
		
		encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = "UTF-8";
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
					writer = outWriter;
					exportReportToStream();
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
						writer = new OutputStreamWriter(os, encoding);
						exportReportToStream();
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
					
					imagesDir = new File(destFile.getParent(), destFile.getName() + "_files");
					
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

		/*   */
		resetClassLoader();
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
				
		try
		{
			OutputStream fileOutputStream = new FileOutputStream(destFile);
			writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, encoding));
			exportReportToStream();
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
		writer = buffer;
		try
		{
			exportReportToStream();
		}
		catch (IOException e)
		{
			throw new JRException("Error while exporting report to buffer", e);
		}
		return buffer.getBuffer();
	}


	protected void exportReportToStream() throws JRException, IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
		writer.write("<!DOCTYPE jasperPrint PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"");
		writer.write(dtdLocation);
		writer.write("\">\n");
		writer.write("\n");

		writer.write("<jasperPrint name=\"");
		writer.write(jasperPrint.getName());
		writer.write("\"");

		writer.write(" pageWidth=\"");
		writer.write(String.valueOf(jasperPrint.getPageWidth()));
		writer.write("\"");

		writer.write(" pageHeight=\"");
		writer.write(String.valueOf(jasperPrint.getPageHeight()));
		writer.write("\"");

		if (jasperPrint.getOrientation() != JRReport.ORIENTATION_PORTRAIT)
		{
			writer.write(" orientation=\"");
			writer.write((String)JRXmlConstants.getOrientationMap().get(new Byte(jasperPrint.getOrientation())));
			writer.write("\"");
		}

		writer.write(">\n");
		
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

		writer.write("</jasperPrint>\n");
		
		writer.flush();
	}


	/**
	 * @throws IOException 
	 */
	protected void exportReportFont(JRReportFont font) throws IOException
	{
		writer.write("\t<reportFont");

		writer.write(" name=\"");
		writer.write(font.getName());
		writer.write("\"");

		writer.write(" isDefault=\"");
		writer.write(String.valueOf(font.isDefault()));
		writer.write("\"");

		writer.write(" fontName=\"");
		writer.write(font.getFontName());
		writer.write("\"");

		writer.write(" size=\"");
		writer.write(String.valueOf(font.getFontSize()));
		writer.write("\"");

		writer.write(" isBold=\"");
		writer.write(String.valueOf(font.isBold()));
		writer.write("\"");

		writer.write(" isItalic=\"");
		writer.write(String.valueOf(font.isItalic()));
		writer.write("\"");

		writer.write(" isUnderline=\"");
		writer.write(String.valueOf(font.isUnderline()));
		writer.write("\"");

		writer.write(" isStrikeThrough=\"");
		writer.write(String.valueOf(font.isStrikeThrough()));
		writer.write("\"");

		writer.write(" pdfFontName=\"");
		writer.write(font.getPdfFontName());
		writer.write("\"");

		writer.write(" pdfEncoding=\"");
		writer.write(font.getPdfEncoding());
		writer.write("\"");

		writer.write(" isPdfEmbedded=\"");
		writer.write(String.valueOf(font.isPdfEmbedded()));
		writer.write("\"");

		writer.write("/>\n");
	}


	/**
	 * @throws IOException 
	 */
	protected void exportStyle(JRStyle style) throws IOException
	{
		//FIXME STYLE
		writer.write("\t<style");

		writer.write(" name=\"");
		writer.write(style.getName());
		writer.write("\"");

		writer.write(" isDefault=\"");
		writer.write(String.valueOf(style.isDefault()));
		writer.write("\"");

		writer.write(" fontName=\"");
		writer.write(style.getFontName());
		writer.write("\"");

		writer.write(" fontSize=\"");
		writer.write(String.valueOf(style.getFontSize()));
		writer.write("\"");

		writer.write(" isBold=\"");
		writer.write(String.valueOf(style.isBold()));
		writer.write("\"");

		writer.write(" isItalic=\"");
		writer.write(String.valueOf(style.isItalic()));
		writer.write("\"");

		writer.write(" isUnderline=\"");
		writer.write(String.valueOf(style.isUnderline()));
		writer.write("\"");

		writer.write(" isStrikeThrough=\"");
		writer.write(String.valueOf(style.isStrikeThrough()));
		writer.write("\"");

		writer.write(" pdfFontName=\"");
		writer.write(style.getPdfFontName());
		writer.write("\"");

		writer.write(" pdfEncoding=\"");
		writer.write(style.getPdfEncoding());
		writer.write("\"");

		writer.write(" isPdfEmbedded=\"");
		writer.write(String.valueOf(style.isPdfEmbedded()));
		writer.write("\"");

		writer.write("/>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		writer.write("\t<page>\n");

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

		writer.write("\t</page>\n");
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}
	
	
	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportLine(JRPrintLine line) throws IOException
	{
		writer.write("\t\t<line");

		if (line.getDirection() != JRLine.DIRECTION_TOP_DOWN)
		{
			writer.write(" direction=\"");
			writer.write((String)JRXmlConstants.getDirectionMap().get(new Byte(line.getDirection())));
			writer.write("\"");
		}

		writer.write(">\n");

		exportReportElement(line);
		exportGraphicElement(line);
		
		writer.write("\t\t</line>\n");
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportReportElement(JRPrintElement element) throws IOException
	{
		writer.write("\t\t\t<reportElement");

		if (
			(element instanceof JRPrintLine && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintRectangle && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintEllipse && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintImage && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRPrintText && element.getMode() != JRElement.MODE_TRANSPARENT)
			)
		{
			writer.write(" mode=\"");
			writer.write((String)JRXmlConstants.getModeMap().get(new Byte(element.getMode())));
			writer.write("\"");
		}

		writer.write(" x=\"");
		writer.write(String.valueOf(element.getX() + globalOffsetX));
		writer.write("\"");

		writer.write(" y=\"");
		writer.write(String.valueOf(element.getY() + globalOffsetY));
		writer.write("\"");

		writer.write(" width=\"");
		writer.write(String.valueOf(element.getWidth()));
		writer.write("\"");

		writer.write(" height=\"");
		writer.write(String.valueOf(element.getHeight()));
		writer.write("\"");

		if (element.getForecolor().getRGB() != Color.black.getRGB())
		{
			writer.write(" forecolor=\"#");
			writer.write(getHexaColor(element.getForecolor()));
			writer.write("\"");
		}

		if (element.getBackcolor().getRGB() != Color.white.getRGB())
		{
			writer.write(" backcolor=\"#");
			writer.write(getHexaColor(element.getBackcolor()));
			writer.write("\"");
		}

		writer.write("/>\n");
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportGraphicElement(JRPrintGraphicElement element) throws IOException
	{
		writer.write("\t\t\t<graphicElement");

		if (
			(element instanceof JRPrintLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintEllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			writer.write(" pen=\"");
			writer.write((String)JRXmlConstants.getPenMap().get(new Byte(element.getPen())));
			writer.write("\"");
		}

		if (element.getFill() != JRGraphicElement.FILL_SOLID)
		{
			writer.write(" fill=\"");
			writer.write((String)JRXmlConstants.getFillMap().get(new Byte(element.getFill())));
			writer.write("\"");
		}

		writer.write("/>\n");
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) throws IOException
	{
		writer.write("\t\t<rectangle");

		if (rectangle.getRadius() != 0)
		{
			writer.write(" radius=\"");
			writer.write(String.valueOf(rectangle.getRadius()));
			writer.write("\"");
		}

		writer.write(">\n");

		exportReportElement(rectangle);
		exportGraphicElement(rectangle);
		
		writer.write("\t\t</rectangle>\n");
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) throws IOException
	{
		writer.write("\t\t\t<ellipse>\n");

		exportReportElement(ellipse);
		exportGraphicElement(ellipse);
		
		writer.write("\t\t</ellipse>\n");
	}


	/**
	 * @throws JRException 
	 * @throws IOException 
	 *
	 */
	protected void exportImage(JRPrintImage image) throws JRException, IOException
	{
		writer.write("\t\t<image");

		if (image.getScaleImage() != JRImage.SCALE_IMAGE_RETAIN_SHAPE)
		{
			writer.write(" scaleImage=\"");
			writer.write((String)JRXmlConstants.getScaleImageMap().get(new Byte(image.getScaleImage())));
			writer.write("\"");
		}
		
		if (image.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			writer.write(" hAlign=\"");
			writer.write((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(image.getHorizontalAlignment())));
			writer.write("\"");
		}
		
		if (image.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			writer.write(" vAlign=\"");
			writer.write((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(image.getVerticalAlignment())));
			writer.write("\"");
		}
		
		if (image.isLazy())
		{
			writer.write(" isLazy=\"");
			writer.write(String.valueOf(image.isLazy()));
			writer.write("\"");
		}

		if (image.getOnErrorType() != JRImage.ON_ERROR_TYPE_ERROR)
		{
			writer.write(" onErrorType=\"");
			writer.write((String)JRXmlConstants.getOnErrorTypeMap().get(new Byte(image.getOnErrorType())));
			writer.write("\"");
		}
		
		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			writer.write(" hyperlinkType=\"");
			writer.write((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(image.getHyperlinkType())));
			writer.write("\"");
		}

		if (image.getHyperlinkTarget() != JRHyperlink.HYPERLINK_TARGET_SELF)
		{
			writer.write(" hyperlinkTarget=\"");
			writer.write((String)JRXmlConstants.getHyperlinkTargetMap().get(new Byte(image.getHyperlinkTarget())));
			writer.write("\"");
		}

		if (image.getAnchorName() != null)
		{
			writer.write(" anchorName=\"");
			writer.write(image.getAnchorName());
			writer.write("\"");
		}
				
		if (image.getHyperlinkReference() != null)
		{
			writer.write(" hyperlinkReference=\"");
			writer.write(image.getHyperlinkReference());
			writer.write("\"");
		}

		if (image.getHyperlinkAnchor() != null)
		{
			writer.write(" hyperlinkAnchor=\"");
			writer.write(image.getHyperlinkAnchor());
			writer.write("\"");
		}

		if (image.getHyperlinkPage() != null)
		{
			writer.write(" hyperlinkPage=\"");
			writer.write(String.valueOf(image.getHyperlinkPage()));
			writer.write("\"");
		}

		if (image.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
		{
			writer.write(" bookmarkLevel=\"");
			writer.write(String.valueOf(image.getBookmarkLevel()));
			writer.write("\"");
		}

		writer.write(">\n");


		exportReportElement(image);
		exportBox(image.getBox());
		exportGraphicElement(image);
		

		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			writer.write("\t\t\t<imageSource");
	
			if (isEmbeddingImages && !image.isLazy())
			{
				writer.write(" isEmbedded=\"");
				writer.write(String.valueOf(isEmbeddingImages && !image.isLazy()));
				writer.write("\"");
			}
	
			writer.write("><![CDATA[");
	
			String imageSource = "";
			
			if (renderer.getType() == JRRenderable.TYPE_SVG)
			{
				renderer = 
					new JRWrappingSvgRenderer(
						renderer, 
						new Dimension(image.getWidth(), image.getHeight()),
						image.getBackcolor()
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
					
					imageSource = new String(baos.toByteArray(), "UTF-8");
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
						imageSource = "img_" + getNextImageId();
						imageNameToImageDataMap.put(imageSource, renderer.getImageData());
						
						imageSource = new File(imagesDir, imageSource).getPath();
					}

					rendererToImagePathMap.put(renderer, imageSource);
				}
			}
			
			writer.write(imageSource);
	
			writer.write("]]></imageSource>\n");
		}
		
		writer.write("\t\t</image>\n");
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	protected void exportText(JRPrintText text) throws IOException
	{
		writer.write("\t\t<text");

		if (text.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			writer.write(" textAlignment=\"");
			writer.write((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(text.getHorizontalAlignment())));
			writer.write("\"");
		}

		if (text.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			writer.write(" verticalAlignment=\"");
			writer.write((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(text.getVerticalAlignment())));
			writer.write("\"");

			writer.write(" textHeight=\"");
			writer.write(String.valueOf(text.getTextHeight()));
			writer.write("\"");
		}

		if (text.getRotation() != JRTextElement.ROTATION_NONE)
		{
			writer.write(" rotation=\"");
			writer.write((String)JRXmlConstants.getRotationMap().get(new Byte(text.getRotation())));
			writer.write("\"");
		}

		if (text.getRunDirection() != JRPrintText.RUN_DIRECTION_LTR)
		{
			writer.write(" runDirection=\"");
			writer.write((String)JRXmlConstants.getRunDirectionMap().get(new Byte(text.getRunDirection())));
			writer.write("\"");
		}

		if (text.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			writer.write(" lineSpacing=\"");
			writer.write((String)JRXmlConstants.getLineSpacingMap().get(new Byte(text.getLineSpacing())));
			writer.write("\"");
		}

		if (text.isStyledText())
		{
			writer.write(" isStyledText=\"");
			writer.write(String.valueOf(text.isStyledText()));
			writer.write("\"");
		}

		writer.write(" lineSpacingFactor=\"");
		writer.write(String.valueOf(text.getLineSpacingFactor()));
		writer.write("\"");

		writer.write(" leadingOffset=\"");
		writer.write(String.valueOf(text.getLeadingOffset()));
		writer.write("\"");

		if (text.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			writer.write(" hyperlinkType=\"");
			writer.write((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(text.getHyperlinkType())));
			writer.write("\"");
		}

		if (text.getHyperlinkTarget() != JRHyperlink.HYPERLINK_TARGET_SELF)
		{
			writer.write(" hyperlinkTarget=\"");
			writer.write((String)JRXmlConstants.getHyperlinkTargetMap().get(new Byte(text.getHyperlinkTarget())));
			writer.write("\"");
		}

		if (text.getAnchorName() != null)
		{
			writer.write(" anchorName=\"");
			writer.write(text.getAnchorName());
			writer.write("\"");
		}
				
		if (text.getHyperlinkReference() != null)
		{
			writer.write(" hyperlinkReference=\"");
			writer.write(text.getHyperlinkReference());
			writer.write("\"");
		}

		if (text.getHyperlinkAnchor() != null)
		{
			writer.write(" hyperlinkAnchor=\"");
			writer.write(text.getHyperlinkAnchor());
			writer.write("\"");
		}

		if (text.getHyperlinkPage() != null)
		{
			writer.write(" hyperlinkPage=\"");
			writer.write(String.valueOf(text.getHyperlinkPage()));
			writer.write("\"");
		}

		if (text.getBookmarkLevel() != JRAnchor.NO_BOOKMARK)
		{
			writer.write(" bookmarkLevel=\"");
			writer.write(String.valueOf(text.getBookmarkLevel()));
			writer.write("\"");
		}

		writer.write(">\n");

		exportReportElement(text);
		exportBox(text);

		String font = exportFont(text);//FIXME STYLE test minimum XML output
		if (font != null)
		{
			writer.write("\t\t\t" + font + "\n");
		}

		if (text.getText() != null)
		{
			writer.write("\t\t\t<textContent><![CDATA[");
			writer.write(text.getText());
			writer.write("]]></textContent>\n");
		}

		writer.write("\t\t</text>\n");
	}


	/**
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	private void exportBox(JRBox box) throws IOException
	{
		if (box != null)
		{
			StringBuffer tmpBuffer = new StringBuffer();

			if (box.getBorder() != JRGraphicElement.PEN_NONE)
			{
				tmpBuffer.append(" border=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getBorder())));
				tmpBuffer.append("\"");
			}
			if (box.getBorderColor() != null)
			{
				tmpBuffer.append(" borderColor=\"#");
				tmpBuffer.append(getHexaColor(box.getBorderColor()));
				tmpBuffer.append("\"");
			}
			if (box.getPadding() > 0)
			{
				tmpBuffer.append(" padding=\"");
				tmpBuffer.append(box.getPadding());
				tmpBuffer.append("\"");
			}
		

			if (box.getOwnTopBorder() != null)
			{
				tmpBuffer.append(" topBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnTopBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnTopBorderColor() != null)
			{
				tmpBuffer.append(" topBorderColor=\"#");
				tmpBuffer.append(getHexaColor(box.getOwnTopBorderColor()));
				tmpBuffer.append("\"");
			}
			if (box.getOwnTopPadding() != null)
			{
				tmpBuffer.append(" topPadding=\"");
				tmpBuffer.append(box.getOwnTopPadding());
				tmpBuffer.append("\"");
			}

			
			if (box.getOwnLeftBorder() != null)
			{
				tmpBuffer.append(" leftBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnLeftBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnLeftBorderColor() != null)
			{
				tmpBuffer.append(" leftBorderColor=\"#");
				tmpBuffer.append(getHexaColor(box.getOwnLeftBorderColor()));
				tmpBuffer.append("\"");
			}
			if (box.getOwnLeftPadding() != null)
			{
				tmpBuffer.append(" leftPadding=\"");
				tmpBuffer.append(box.getOwnLeftPadding());
				tmpBuffer.append("\"");
			}

			
			if (box.getOwnBottomBorder() != null)
			{
				tmpBuffer.append(" bottomBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnBottomBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnBottomBorderColor() != null)
			{
				tmpBuffer.append(" bottomBorderColor=\"#");
				tmpBuffer.append(getHexaColor(box.getOwnBottomBorderColor()));
				tmpBuffer.append("\"");
			}
			if (box.getOwnBottomPadding() != null)
			{
				tmpBuffer.append(" bottomPadding=\"");
				tmpBuffer.append(box.getOwnBottomPadding());
				tmpBuffer.append("\"");
			}

			
			if (box.getOwnRightBorder() != null)
			{
				tmpBuffer.append(" rightBorder=\"");
				tmpBuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(box.getOwnRightBorder().byteValue())));
				tmpBuffer.append("\"");
			}
			if (box.getOwnRightBorderColor() != null)
			{
				tmpBuffer.append(" rightBorderColor=\"#");
				tmpBuffer.append(getHexaColor(box.getOwnRightBorderColor()));
				tmpBuffer.append("\"");
			}
			if (box.getOwnRightPadding() != null)
			{
				tmpBuffer.append(" rightPadding=\"");
				tmpBuffer.append(box.getOwnRightPadding());
				tmpBuffer.append("\"");
			}

			
			if (tmpBuffer.length() > 0)
			{
				writer.write("\t\t\t<box");
				writer.write(tmpBuffer.toString());
				writer.write("/>\n");
			}
		}
	}


	/**
	 *
	 */
	protected String exportFont(JRFont font)
	{
		String fontChunk = null;
		
		if (font != null)
		{
			StringBuffer tmpBuffer = new StringBuffer();
		
			if(font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)fontsMap.get(
						font.getReportFont().getName()
						);
				if(baseFont != null)
				{
					tmpBuffer.append(" reportFont=\"");
					tmpBuffer.append(font.getReportFont().getName());
					tmpBuffer.append("\"");
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
		
			if (font.getOwnFontName() != null)
			{
				tmpBuffer.append(" fontName=\"");
				tmpBuffer.append(font.getOwnFontName());
				tmpBuffer.append("\"");
			}

			if (font.getOwnFontSize() != null)
			{
				tmpBuffer.append(" size=\"");
				tmpBuffer.append(font.getOwnFontSize());
				tmpBuffer.append("\"");
			}

			if (font.isOwnBold() != null)
			{
				tmpBuffer.append(" isBold=\"");
				tmpBuffer.append(font.isOwnBold());
				tmpBuffer.append("\"");
			}

			if (font.isOwnItalic() != null)
			{
				tmpBuffer.append(" isItalic=\"");
				tmpBuffer.append(font.isOwnItalic());
				tmpBuffer.append("\"");
			}
	
			if (font.isOwnUnderline() != null)
			{
				tmpBuffer.append(" isUnderline=\"");
				tmpBuffer.append(font.isOwnUnderline());
				tmpBuffer.append("\"");
			}
	
			if (font.isOwnStrikeThrough() != null)
			{
				tmpBuffer.append(" isStrikeThrough=\"");
				tmpBuffer.append(font.isOwnStrikeThrough());
				tmpBuffer.append("\"");
			}

			if (font.getOwnPdfFontName() != null)
			{
				tmpBuffer.append(" pdfFontName=\"");
				tmpBuffer.append(font.getOwnPdfFontName());
				tmpBuffer.append("\"");
			}

			if (font.getOwnPdfEncoding() != null)
			{
				tmpBuffer.append(" pdfEncoding=\"");
				tmpBuffer.append(font.getOwnPdfEncoding());
				tmpBuffer.append("\"");
			}

			if (font.isOwnPdfEmbedded() != null)
			{
				tmpBuffer.append(" isPdfEmbedded=\"");
				tmpBuffer.append(font.isOwnPdfEmbedded());
				tmpBuffer.append("\"");
			}

			if (tmpBuffer.length() > 0)
			{
				fontChunk = "<font" + tmpBuffer.toString() + "/>";
			}
		}
		
		return fontChunk;
	}


	/**
	 * 
	 */
	private String getHexaColor(Color color)
	{
		String hexa = Integer.toHexString(color.getRGB() & colorMask).toUpperCase();
		return ("000000" + hexa).substring(hexa.length());
	}

	/**
	 * 
	 */
	private static synchronized int getNextImageId(){
		return imageId++;
	}

}
