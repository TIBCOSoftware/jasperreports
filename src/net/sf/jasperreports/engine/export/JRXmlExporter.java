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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.tools.codec.Base64Encoder;

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
import dori.jasper.engine.JRPrintGraphicElement;
import dori.jasper.engine.JRPrintImage;
import dori.jasper.engine.JRPrintLine;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintRectangle;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JRRenderable;
import dori.jasper.engine.JRReport;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRRuntimeException;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.xml.JRXmlConstants;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	protected StringBuffer sbuffer = null;
	protected JRExportProgressMonitor progressMonitor = null;
	protected Map loadedImagesMap = null;
	protected Map fontsMap = new HashMap();

	/**
	 *
	 */
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
		setInput();

		/*   */
		setPageRange();

		StringBuffer sb = (StringBuffer)parameters.get(JRXmlExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			sb.append(exportReportToBuffer().toString());
		}
		else
		{
			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				String xmlString = exportReportToBuffer().toString();
				try
				{
					byte[] bytes = xmlString.getBytes("UTF-8");
					os.write(bytes, 0, bytes.length);
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


	/**
	 *
	 */
	protected void exportReportToFile() throws JRException
	{
		if (!isEmbeddingImages)
		{
			loadedImagesMap = new HashMap();
		}
		
		String xmlString = exportReportToBuffer().toString();
		
		FileOutputStream fos = null;

		try
		{
			byte[] bytes = xmlString.getBytes("UTF-8");
			fos = new FileOutputStream(destFile);
			fos.write(bytes, 0, bytes.length);
			fos.flush();
		}
		catch (Exception e)
		{
			throw new JRException("Error writing to file : " + destFile, e);
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
		
		if (!isEmbeddingImages)
		{
			Collection imageKeys = loadedImagesMap.keySet();
			if (imageKeys != null && imageKeys.size() > 0)
			{
				if (!imagesDir.exists())
				{
					imagesDir.mkdir();
				}
	
				JRRenderable renderer = null;
				File imageFile = null;
				for(Iterator it = imageKeys.iterator(); it.hasNext();)
				{
					renderer = (JRRenderable)it.next();
					imageFile = new File(imagesDir, (String)loadedImagesMap.get(renderer));
					byte[] imageData = renderer.getImageData();
					try
					{
						fos = new FileOutputStream(imageFile);
						fos.write(imageData, 0, imageData.length);
					}
					catch (Exception e)
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
		sbuffer = new StringBuffer();
		
		sbuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sbuffer.append("<!DOCTYPE jasperPrint PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperprint.dtd\">\n");
		sbuffer.append("\n");

		sbuffer.append("<jasperPrint name=\"");
		sbuffer.append(jasperPrint.getName());
		sbuffer.append("\"");

		sbuffer.append(" pageWidth=\"");
		sbuffer.append(jasperPrint.getPageWidth());
		sbuffer.append("\"");

		sbuffer.append(" pageHeight=\"");
		sbuffer.append(jasperPrint.getPageHeight());
		sbuffer.append("\"");

		if (jasperPrint.getOrientation() != JRReport.ORIENTATION_PORTRAIT)
		{
			sbuffer.append(" orientation=\"");
			sbuffer.append((String)JRXmlConstants.getOrientationMap().get(new Byte(jasperPrint.getOrientation())));
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");
		
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

		sbuffer.append("</jasperPrint>\n");

		return sbuffer;
	}


	/**
	 *
	 */
	protected void exportReportFont(JRReportFont font)
	{
		sbuffer.append("\t<reportFont");

		sbuffer.append(" name=\"");
		sbuffer.append(font.getName());
		sbuffer.append("\"");

		sbuffer.append(" isDefault=\"");
		sbuffer.append(font.isDefault());
		sbuffer.append("\"");

		sbuffer.append(" fontName=\"");
		sbuffer.append(font.getFontName());
		sbuffer.append("\"");

		sbuffer.append(" size=\"");
		sbuffer.append(font.getSize());
		sbuffer.append("\"");

		sbuffer.append(" isBold=\"");
		sbuffer.append(font.isBold());
		sbuffer.append("\"");

		sbuffer.append(" isItalic=\"");
		sbuffer.append(font.isItalic());
		sbuffer.append("\"");

		sbuffer.append(" isUnderline=\"");
		sbuffer.append(font.isUnderline());
		sbuffer.append("\"");

		sbuffer.append(" isStrikeThrough=\"");
		sbuffer.append(font.isStrikeThrough());
		sbuffer.append("\"");

		sbuffer.append(" pdfFontName=\"");
		sbuffer.append(font.getPdfFontName());
		sbuffer.append("\"");

		sbuffer.append(" pdfEncoding=\"");
		sbuffer.append(font.getPdfEncoding());
		sbuffer.append("\"");

		sbuffer.append(" isPdfEmbedded=\"");
		sbuffer.append(font.isPdfEmbedded());
		sbuffer.append("\"");

		sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException
	{
		sbuffer.append("\t<page>\n");

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

		sbuffer.append("\t</page>\n");
		
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
		sbuffer.append("\t\t<line");

		if (line.getDirection() != JRLine.DIRECTION_TOP_DOWN)
		{
			sbuffer.append(" direction=\"");
			sbuffer.append((String)JRXmlConstants.getDirectionMap().get(new Byte(line.getDirection())));
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		exportReportElement(line);
		exportGraphicElement(line);
		
		sbuffer.append("\t\t</line>\n");
	}


	/**
	 *
	 */
	protected void exportReportElement(JRPrintElement element)
	{
		sbuffer.append("\t\t\t<reportElement");

		if (
			(element instanceof JRPrintLine && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintRectangle && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintEllipse && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintImage && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRPrintText && element.getMode() != JRElement.MODE_TRANSPARENT)
			)
		{
			sbuffer.append(" mode=\"");
			sbuffer.append((String)JRXmlConstants.getModeMap().get(new Byte(element.getMode())));
			sbuffer.append("\"");
		}

		sbuffer.append(" x=\"");
		sbuffer.append(element.getX());
		sbuffer.append("\"");

		sbuffer.append(" y=\"");
		sbuffer.append(element.getY());
		sbuffer.append("\"");

		sbuffer.append(" width=\"");
		sbuffer.append(element.getWidth());
		sbuffer.append("\"");

		sbuffer.append(" height=\"");
		sbuffer.append(element.getHeight());
		sbuffer.append("\"");

		if (element.getForecolor().getRGB() != Color.black.getRGB())
		{
			sbuffer.append(" forecolor=\"#");
			String hexa = Integer.toHexString(element.getForecolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			sbuffer.append(hexa);
			sbuffer.append("\"");
		}

		if (element.getBackcolor().getRGB() != Color.white.getRGB())
		{
			sbuffer.append(" backcolor=\"#");
			String hexa = Integer.toHexString(element.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			sbuffer.append(hexa);
			sbuffer.append("\"");
		}

		sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	protected void exportGraphicElement(JRPrintGraphicElement element)
	{
		sbuffer.append("\t\t\t<graphicElement");

		if (
			(element instanceof JRPrintLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintEllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			sbuffer.append(" pen=\"");
			sbuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(element.getPen())));
			sbuffer.append("\"");
		}

		if (element.getFill() != JRGraphicElement.FILL_SOLID)
		{
			sbuffer.append(" fill=\"");
			sbuffer.append((String)JRXmlConstants.getFillMap().get(new Byte(element.getFill())));
			sbuffer.append("\"");
		}

		sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintRectangle rectangle)
	{
		sbuffer.append("\t\t<rectangle");

		if (rectangle.getRadius() != 0)
		{
			sbuffer.append(" radius=\"");
			sbuffer.append(rectangle.getRadius());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		exportReportElement(rectangle);
		exportGraphicElement(rectangle);
		
		sbuffer.append("\t\t</rectangle>\n");
	}


	/**
	 *
	 */
	protected void exportEllipse(JRPrintEllipse ellipse)
	{
		sbuffer.append("\t\t\t<ellipse>\n");

		exportReportElement(ellipse);
		exportGraphicElement(ellipse);
		
		sbuffer.append("\t\t</ellipse>\n");
	}


	/**
	 *
	 */
	protected void exportImage(JRPrintImage image) throws JRException
	{
		sbuffer.append("\t\t<image");

		if (image.getScaleImage() != JRImage.SCALE_IMAGE_RETAIN_SHAPE)
		{
			sbuffer.append(" scaleImage=\"");
			sbuffer.append((String)JRXmlConstants.getScaleImageMap().get(new Byte(image.getScaleImage())));
			sbuffer.append("\"");
		}
		
		if (image.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			sbuffer.append(" hAlign=\"");
			sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(image.getHorizontalAlignment())));
			sbuffer.append("\"");
		}
		
		if (image.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			sbuffer.append(" vAlign=\"");
			sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(image.getVerticalAlignment())));
			sbuffer.append("\"");
		}
		
		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			sbuffer.append(" hyperlinkType=\"");
			sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(image.getHyperlinkType())));
			sbuffer.append("\"");
		}

		if (image.getAnchorName() != null)
		{
			sbuffer.append(" anchorName=\"");
			sbuffer.append(image.getAnchorName());
			sbuffer.append("\"");
		}
				
		if (image.getHyperlinkReference() != null)
		{
			sbuffer.append(" hyperlinkReference=\"");
			sbuffer.append(image.getHyperlinkReference());
			sbuffer.append("\"");
		}

		if (image.getHyperlinkAnchor() != null)
		{
			sbuffer.append(" hyperlinkAnchor=\"");
			sbuffer.append(image.getHyperlinkAnchor());
			sbuffer.append("\"");
		}

		if (image.getHyperlinkPage() != null)
		{
			sbuffer.append(" hyperlinkPage=\"");
			sbuffer.append(image.getHyperlinkPage());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");


		exportReportElement(image);
		exportGraphicElement(image);
		

		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			sbuffer.append("\t\t\t<imageSource");
	
			if (isEmbeddingImages)
			{
				sbuffer.append(" isEmbedded=\"");
				sbuffer.append(isEmbeddingImages);
				sbuffer.append("\"");
			}
	
			sbuffer.append("><![CDATA[");
	
			String imageSource = "";
			
			if (isEmbeddingImages)
			{
				try
				{
					ByteArrayInputStream bais = new ByteArrayInputStream(renderer.getImageData());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					Base64Encoder encoder = new Base64Encoder(bais, baos);
					encoder.process();
					
					imageSource = new String(baos.toByteArray(), "UTF-8");
				}
				catch (Exception e)
				{
					throw new JRException("Error embedding image into XML.", e);
				}
			}
			else
			{
				if (loadedImagesMap.containsKey(renderer))
				{
					imageSource = 
						(
						new File(
							new File(imagesDir.getName()), 
							(String)loadedImagesMap.get(renderer)
							)
						).getPath();
				}
				else
				{
					imageSource = "img_" + String.valueOf(loadedImagesMap.size());
					loadedImagesMap.put(renderer, imageSource);
	
					imageSource = 
						(
						new File(
							new File(imagesDir.getName()), 
							imageSource
							)
						).getPath();
				}
			}
			
			sbuffer.append(imageSource);
	
			sbuffer.append("]]></imageSource>\n");
		}
		
		sbuffer.append("\t\t</image>\n");
	}


	/**
	 *
	 */
	protected void exportText(JRPrintText text)
	{
		sbuffer.append("\t\t<text");

		if (text.getTextAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			sbuffer.append(" textAlignment=\"");
			sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(text.getTextAlignment())));
			sbuffer.append("\"");
		}

		if (text.getVerticalAlignment() != JRTextElement.VERTICAL_ALIGN_TOP)
		{
			sbuffer.append(" verticalAlignment=\"");
			sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(text.getVerticalAlignment())));
			sbuffer.append("\"");

			sbuffer.append(" textHeight=\"");
			sbuffer.append(text.getTextHeight());
			sbuffer.append("\"");
		}

		if (text.getRotation() != JRTextElement.ROTATION_NONE)
		{
			sbuffer.append(" rotation=\"");
			sbuffer.append((String)JRXmlConstants.getRotationMap().get(new Byte(text.getRotation())));
			sbuffer.append("\"");
		}

		if (text.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			sbuffer.append(" lineSpacing=\"");
			sbuffer.append((String)JRXmlConstants.getLineSpacingMap().get(new Byte(text.getLineSpacing())));
			sbuffer.append("\"");
		}

		if (text.isStyledText())
		{
			sbuffer.append(" isStyledText=\"");
			sbuffer.append(text.isStyledText());
			sbuffer.append("\"");
		}

		sbuffer.append(" lineSpacingFactor=\"");
		sbuffer.append(text.getLineSpacingFactor());
		sbuffer.append("\"");

		sbuffer.append(" leadingOffset=\"");
		sbuffer.append(text.getLeadingOffset());
		sbuffer.append("\"");

		if (text.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			sbuffer.append(" hyperlinkType=\"");
			sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(text.getHyperlinkType())));
			sbuffer.append("\"");
		}

		if (text.getAnchorName() != null)
		{
			sbuffer.append(" anchorName=\"");
			sbuffer.append(text.getAnchorName());
			sbuffer.append("\"");
		}
				
		if (text.getHyperlinkReference() != null)
		{
			sbuffer.append(" hyperlinkReference=\"");
			sbuffer.append(text.getHyperlinkReference());
			sbuffer.append("\"");
		}

		if (text.getHyperlinkAnchor() != null)
		{
			sbuffer.append(" hyperlinkAnchor=\"");
			sbuffer.append(text.getHyperlinkAnchor());
			sbuffer.append("\"");
		}

		if (text.getHyperlinkPage() != null)
		{
			sbuffer.append(" hyperlinkPage=\"");
			sbuffer.append(text.getHyperlinkPage());
			sbuffer.append("\"");
		}

		sbuffer.append(">\n");

		exportReportElement(text);

		String font = exportFont(text.getFont());
		if (font != null)
		{
			sbuffer.append("\t\t\t" + font + "\n");
		}

		if (text.getText() != null)
		{
			sbuffer.append("\t\t\t<textContent><![CDATA[");
			sbuffer.append(text.getText());
			sbuffer.append("]]></textContent>\n");
		}

		sbuffer.append("\t\t</text>\n");
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


}
