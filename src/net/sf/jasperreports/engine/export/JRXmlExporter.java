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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
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
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

import org.w3c.tools.codec.Base64Encoder;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlExporter extends JRAbstractExporter
{

	/**
	 *
	 */
	protected PrintWriter writer = null;
	protected JRExportProgressMonitor progressMonitor = null;
	protected Map rendererToImagePathMap = null;
	protected Map imageNameToImageDataMap = null;
	protected Map fontsMap = new HashMap();

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
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		/*   */
		setInput();

		/*   */
		setPageRange();

		dtdLocation = (String)parameters.get(JRXmlExporterParameter.DTD_LOCATION);
		if (dtdLocation == null)
		{
			dtdLocation = "http://jasperreports.sourceforge.net/dtds/jasperprint.dtd";
		}
		
		StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			StringBuffer buffer = exportReportToBuffer();
			sb.append(buffer);
		}
		else
		{
			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				try
				{
					Writer osw = new OutputStreamWriter(os, "UTF-8");
					exportReportToStream(osw);
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
				
				try
				{
					exportReportToFile();
				}
				catch (IOException e)
				{
					throw new JRException("Error writing report " + jasperPrint.getName() + " to file " + destFile, e);
				}
			}
		}
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportReportToFile() throws JRException, IOException
	{
		//if (!isEmbeddingImages)
		{
			rendererToImagePathMap = new HashMap();
			imageNameToImageDataMap = new HashMap();
		}
				
		Writer fWriter = null;
		try
		{
			fWriter = new BufferedWriter(new FileWriter(destFile));
			exportReportToStream(fWriter);
			fWriter.flush();
		}
		catch (IOException e)
		{
			throw new JRException("Error writing to file : " + destFile, e);
		}
		finally
		{
			if (fWriter != null)
			{
				try
				{
					fWriter.close();
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

					try
					{
						OutputStream fos = new FileOutputStream(imageFile);
						fos.write(imageData, 0, imageData.length);
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to image file : " + imageFile, e);
					}
					finally
					{
						if (fWriter != null)
						{
							try
							{
								fWriter.close();
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
	 * @throws IOException 
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
			throw new JRException("Error exporting report to buffer", e);
		}
		return buffer.getBuffer();
	}


	protected void exportReportToStream(Writer out) throws JRException, IOException
	{
		writer = new PrintWriter(out);
		
		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.print("<!DOCTYPE jasperPrint PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"");
		writer.print(dtdLocation);
		writer.print("\">\n");
		writer.print("\n");

		writer.print("<jasperPrint name=\"");
		writer.print(jasperPrint.getName());
		writer.print("\"");

		writer.print(" pageWidth=\"");
		writer.print(jasperPrint.getPageWidth());
		writer.print("\"");

		writer.print(" pageHeight=\"");
		writer.print(jasperPrint.getPageHeight());
		writer.print("\"");

		if (jasperPrint.getOrientation() != JRReport.ORIENTATION_PORTRAIT)
		{
			writer.print(" orientation=\"");
			writer.print((String)JRXmlConstants.getOrientationMap().get(new Byte(jasperPrint.getOrientation())));
			writer.print("\"");
		}

		writer.print(">\n");
		
		JRReportFont[] fonts = jasperPrint.getFonts();
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				fontsMap.put(fonts[i].getName(), fonts[i]);
				exportReportFont(fonts[i]);
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

		writer.print("</jasperPrint>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportReportFont(JRReportFont font) throws IOException
	{
		writer.print("\t<reportFont");

		writer.print(" name=\"");
		writer.print(font.getName());
		writer.print("\"");

		writer.print(" isDefault=\"");
		writer.print(font.isDefault());
		writer.print("\"");

		writer.print(" fontName=\"");
		writer.print(font.getFontName());
		writer.print("\"");

		writer.print(" size=\"");
		writer.print(font.getSize());
		writer.print("\"");

		writer.print(" isBold=\"");
		writer.print(font.isBold());
		writer.print("\"");

		writer.print(" isItalic=\"");
		writer.print(font.isItalic());
		writer.print("\"");

		writer.print(" isUnderline=\"");
		writer.print(font.isUnderline());
		writer.print("\"");

		writer.print(" isStrikeThrough=\"");
		writer.print(font.isStrikeThrough());
		writer.print("\"");

		writer.print(" pdfFontName=\"");
		writer.print(font.getPdfFontName());
		writer.print("\"");

		writer.print(" pdfEncoding=\"");
		writer.print(font.getPdfEncoding());
		writer.print("\"");

		writer.print(" isPdfEmbedded=\"");
		writer.print(font.isPdfEmbedded());
		writer.print("\"");

		writer.print("/>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		writer.print("\t<page>\n");

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

		writer.print("\t</page>\n");
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}
	
	
	/**
	 * @throws IOException 
	 *
	 */
	protected void exportLine(JRPrintLine line) throws IOException
	{
		writer.print("\t\t<line");

		if (line.getDirection() != JRLine.DIRECTION_TOP_DOWN)
		{
			writer.print(" direction=\"");
			writer.print((String)JRXmlConstants.getDirectionMap().get(new Byte(line.getDirection())));
			writer.print("\"");
		}

		writer.print(">\n");

		exportReportElement(line);
		exportGraphicElement(line);
		
		writer.print("\t\t</line>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportReportElement(JRPrintElement element) throws IOException
	{
		writer.print("\t\t\t<reportElement");

		if (
			(element instanceof JRPrintLine && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintRectangle && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintEllipse && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintImage && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRPrintText && element.getMode() != JRElement.MODE_TRANSPARENT)
			)
		{
			writer.print(" mode=\"");
			writer.print((String)JRXmlConstants.getModeMap().get(new Byte(element.getMode())));
			writer.print("\"");
		}

		writer.print(" x=\"");
		writer.print(element.getX() + globalOffsetX);
		writer.print("\"");

		writer.print(" y=\"");
		writer.print(element.getY() + globalOffsetY);
		writer.print("\"");

		writer.print(" width=\"");
		writer.print(element.getWidth());
		writer.print("\"");

		writer.print(" height=\"");
		writer.print(element.getHeight());
		writer.print("\"");

		if (element.getForecolor().getRGB() != Color.black.getRGB())
		{
			writer.print(" forecolor=\"#");
			writer.print(getHexaColor(element.getForecolor()));
			writer.print("\"");
		}

		if (element.getBackcolor().getRGB() != Color.white.getRGB())
		{
			writer.print(" backcolor=\"#");
			writer.print(getHexaColor(element.getBackcolor()));
			writer.print("\"");
		}

		writer.print("/>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportGraphicElement(JRPrintGraphicElement element) throws IOException
	{
		writer.print("\t\t\t<graphicElement");

		if (
			(element instanceof JRPrintLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintEllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			writer.print(" pen=\"");
			writer.print((String)JRXmlConstants.getPenMap().get(new Byte(element.getPen())));
			writer.print("\"");
		}

		if (element.getFill() != JRGraphicElement.FILL_SOLID)
		{
			writer.print(" fill=\"");
			writer.print((String)JRXmlConstants.getFillMap().get(new Byte(element.getFill())));
			writer.print("\"");
		}

		writer.print("/>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle) throws IOException
	{
		writer.print("\t\t<rectangle");

		if (rectangle.getRadius() != 0)
		{
			writer.print(" radius=\"");
			writer.print(rectangle.getRadius());
			writer.print("\"");
		}

		writer.print(">\n");

		exportReportElement(rectangle);
		exportGraphicElement(rectangle);
		
		writer.print("\t\t</rectangle>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse) throws IOException
	{
		writer.print("\t\t\t<ellipse>\n");

		exportReportElement(ellipse);
		exportGraphicElement(ellipse);
		
		writer.print("\t\t</ellipse>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportImage(JRPrintImage image) throws JRException, IOException
	{
		writer.print("\t\t<image");

		if (image.getScaleImage() != JRImage.SCALE_IMAGE_RETAIN_SHAPE)
		{
			writer.print(" scaleImage=\"");
			writer.print((String)JRXmlConstants.getScaleImageMap().get(new Byte(image.getScaleImage())));
			writer.print("\"");
		}
		
		if (image.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			writer.print(" hAlign=\"");
			writer.print((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(image.getHorizontalAlignment())));
			writer.print("\"");
		}
		
		if (image.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			writer.print(" vAlign=\"");
			writer.print((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(image.getVerticalAlignment())));
			writer.print("\"");
		}
		
		if (image.isLazy())
		{
			writer.print(" isLazy=\"");
			writer.print(image.isLazy());
			writer.print("\"");
		}

		if (image.getOnErrorType() != JRImage.ON_ERROR_TYPE_ERROR)
		{
			writer.print(" onErrorType=\"");
			writer.print((String)JRXmlConstants.getOnErrorTypeMap().get(new Byte(image.getOnErrorType())));
			writer.print("\"");
		}
		
		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			writer.print(" hyperlinkType=\"");
			writer.print((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(image.getHyperlinkType())));
			writer.print("\"");
		}

		if (image.getHyperlinkTarget() != JRHyperlink.HYPERLINK_TARGET_SELF)
		{
			writer.print(" hyperlinkTarget=\"");
			writer.print((String)JRXmlConstants.getHyperlinkTargetMap().get(new Byte(image.getHyperlinkTarget())));
			writer.print("\"");
		}

		if (image.getAnchorName() != null)
		{
			writer.print(" anchorName=\"");
			writer.print(image.getAnchorName());
			writer.print("\"");
		}
				
		if (image.getHyperlinkReference() != null)
		{
			writer.print(" hyperlinkReference=\"");
			writer.print(image.getHyperlinkReference());
			writer.print("\"");
		}

		if (image.getHyperlinkAnchor() != null)
		{
			writer.print(" hyperlinkAnchor=\"");
			writer.print(image.getHyperlinkAnchor());
			writer.print("\"");
		}

		if (image.getHyperlinkPage() != null)
		{
			writer.print(" hyperlinkPage=\"");
			writer.print(image.getHyperlinkPage());
			writer.print("\"");
		}

		writer.print(">\n");


		exportReportElement(image);
		exportBox(image.getBox());
		exportGraphicElement(image);
		

		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			writer.print("\t\t\t<imageSource");
	
			if (isEmbeddingImages && !image.isLazy())
			{
				writer.print(" isEmbedded=\"");
				writer.print(isEmbeddingImages && !image.isLazy());
				writer.print("\"");
			}
	
			writer.print("><![CDATA[");
	
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
						imageSource = "img_" + String.valueOf(imageNameToImageDataMap.size());
						imageNameToImageDataMap.put(imageSource, renderer.getImageData());
						
						imageSource = new File(imagesDir, imageSource).getPath();
					}

					rendererToImagePathMap.put(renderer, imageSource);
				}
			}
			
			writer.print(imageSource);
	
			writer.print("]]></imageSource>\n");
		}
		
		writer.print("\t\t</image>\n");
	}


	/**
	 * @throws IOException 
	 *
	 */
	protected void exportText(JRPrintText text) throws IOException
	{
		writer.print("\t\t<text");

		if (text.getTextAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			writer.print(" textAlignment=\"");
			writer.print((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(text.getTextAlignment())));
			writer.print("\"");
		}

		if (text.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			writer.print(" verticalAlignment=\"");
			writer.print((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(text.getVerticalAlignment())));
			writer.print("\"");

			writer.print(" textHeight=\"");
			writer.print(text.getTextHeight());
			writer.print("\"");
		}

		if (text.getRotation() != JRTextElement.ROTATION_NONE)
		{
			writer.print(" rotation=\"");
			writer.print((String)JRXmlConstants.getRotationMap().get(new Byte(text.getRotation())));
			writer.print("\"");
		}

		if (text.getRunDirection() != JRPrintText.RUN_DIRECTION_LTR)
		{
			writer.print(" runDirection=\"");
			writer.print((String)JRXmlConstants.getRunDirectionMap().get(new Byte(text.getRunDirection())));
			writer.print("\"");
		}

		if (text.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			writer.print(" lineSpacing=\"");
			writer.print((String)JRXmlConstants.getLineSpacingMap().get(new Byte(text.getLineSpacing())));
			writer.print("\"");
		}

		if (text.isStyledText())
		{
			writer.print(" isStyledText=\"");
			writer.print(text.isStyledText());
			writer.print("\"");
		}

		writer.print(" lineSpacingFactor=\"");
		writer.print(text.getLineSpacingFactor());
		writer.print("\"");

		writer.print(" leadingOffset=\"");
		writer.print(text.getLeadingOffset());
		writer.print("\"");

		if (text.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			writer.print(" hyperlinkType=\"");
			writer.print((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(text.getHyperlinkType())));
			writer.print("\"");
		}

		if (text.getHyperlinkTarget() != JRHyperlink.HYPERLINK_TARGET_SELF)
		{
			writer.print(" hyperlinkTarget=\"");
			writer.print((String)JRXmlConstants.getHyperlinkTargetMap().get(new Byte(text.getHyperlinkTarget())));
			writer.print("\"");
		}

		if (text.getAnchorName() != null)
		{
			writer.print(" anchorName=\"");
			writer.print(text.getAnchorName());
			writer.print("\"");
		}
				
		if (text.getHyperlinkReference() != null)
		{
			writer.print(" hyperlinkReference=\"");
			writer.print(text.getHyperlinkReference());
			writer.print("\"");
		}

		if (text.getHyperlinkAnchor() != null)
		{
			writer.print(" hyperlinkAnchor=\"");
			writer.print(text.getHyperlinkAnchor());
			writer.print("\"");
		}

		if (text.getHyperlinkPage() != null)
		{
			writer.print(" hyperlinkPage=\"");
			writer.print(text.getHyperlinkPage());
			writer.print("\"");
		}

		writer.print(">\n");

		exportReportElement(text);
		exportBox(text.getBox());

		String font = exportFont(text.getFont());
		if (font != null)
		{
			writer.print("\t\t\t" + font + "\n");
		}

		if (text.getText() != null)
		{
			writer.print("\t\t\t<textContent><![CDATA[");
			writer.print(text.getText());
			writer.print("]]></textContent>\n");
		}

		writer.print("\t\t</text>\n");
	}


	/**
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
				writer.print("\t\t\t<box");
				writer.print(tmpBuffer.toString());
				writer.print("/>\n");
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

			if (font.getOwnSize() != null)
			{
				tmpBuffer.append(" size=\"");
				tmpBuffer.append(font.getOwnSize());
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

}
