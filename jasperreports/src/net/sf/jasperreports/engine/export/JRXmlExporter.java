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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import dori.jasper.engine.JRReport;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRRuntimeException;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.xml.JRXmlConstants;


/**
 *
 */
public class JRXmlExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	private JasperPrint jasperPrint = null;

	/**
	 *
	 */
	private StringBuffer sbuffer = null;
	private Map loadedImagesMap = null;
	private Map fontsMap = new HashMap();

	/**
	 *
	 */
	private boolean isEmbeddingImages = true;
	private File destFile = null;
	private File imagesDir = null;

	/**
	 *
	 */
	private static final int colorMask = Integer.parseInt("FFFFFF", 16);


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		this.jasperPrint = (JasperPrint)this.parameters.get(JRExporterParameter.JASPER_PRINT);
		if (jasperPrint == null)
		{
			InputStream is = (InputStream)this.parameters.get(JRExporterParameter.INPUT_STREAM);
			if (is != null)
			{
				this.jasperPrint = (JasperPrint)JRLoader.loadObject(is);
			}
			else
			{
				URL url = (URL)this.parameters.get(JRExporterParameter.INPUT_URL);
				if (url != null)
				{
					this.jasperPrint = (JasperPrint)JRLoader.loadObject(url);
				}
				else
				{
					File file = (File)this.parameters.get(JRExporterParameter.INPUT_FILE);
					if (file != null)
					{
						this.jasperPrint = (JasperPrint)JRLoader.loadObject(file);
					}
					else
					{
						String fileName = (String)this.parameters.get(JRExporterParameter.INPUT_FILE_NAME);
						if (fileName != null)
						{
							this.jasperPrint = (JasperPrint)JRLoader.loadObject(fileName);
						}
						else
						{
							throw new JRException("No input source supplied to the exporter.");
						}
					}
				}
			}
		}

		StringBuffer sb = (StringBuffer)this.parameters.get(JRXmlExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			sb.append(this.exportReportToBuffer().toString());
		}
		else
		{
			OutputStream os = (OutputStream)this.parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				String xmlString = this.exportReportToBuffer().toString();
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
				this.destFile = (File)this.parameters.get(JRExporterParameter.OUTPUT_FILE);
				if (this.destFile == null)
				{
					String fileName = (String)this.parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
					if (fileName != null)
					{
						this.destFile = new File(fileName);
					}
					else
					{
						throw new JRException("No output specified for the exporter.");
					}
				}
				
				this.imagesDir = new File(this.destFile.getParent(), this.destFile.getPath() + "_files");
				
				Boolean isEmbeddingImagesParameter = (Boolean)this.parameters.get(JRXmlExporterParameter.IS_EMBEDDING_IMAGES);
				if (isEmbeddingImagesParameter == null)
				{
					isEmbeddingImagesParameter = Boolean.TRUE;
				}
				this.isEmbeddingImages = isEmbeddingImagesParameter.booleanValue();
				
				this.exportReportToFile();
			}
		}
	}


	/**
	 *
	 */
	private void exportReportToFile() throws JRException
	{
		if (!isEmbeddingImages)
		{
			this.loadedImagesMap = new HashMap();
		}
		
		String xmlString = this.exportReportToBuffer().toString();
		
		FileOutputStream fos = null;

		try
		{
			byte[] bytes = xmlString.getBytes("UTF-8");
			fos = new FileOutputStream(this.destFile);
			fos.write(bytes, 0, bytes.length);
			fos.flush();
		}
		catch (Exception e)
		{
			throw new JRException("Error writing to file : " + this.destFile, e);
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
			Collection imageKeys = this.loadedImagesMap.keySet();
			if (imageKeys != null && imageKeys.size() > 0)
			{
				if (!imagesDir.exists())
				{
					imagesDir.mkdir();
				}
	
				byte[] imageData = null;
				File imageFile = null;
				for(Iterator it = imageKeys.iterator(); it.hasNext();)
				{
					imageData = (byte[])it.next();
					imageFile = new File(imagesDir, (String)this.loadedImagesMap.get(imageData));
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
	private StringBuffer exportReportToBuffer() throws JRException
	{
		this.sbuffer = new StringBuffer();
		
		this.sbuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		this.sbuffer.append("<!DOCTYPE jasperPrint PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperprint.dtd\">\n");
		this.sbuffer.append("\n");

		this.sbuffer.append("<jasperPrint name=\"");
		this.sbuffer.append(jasperPrint.getName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" pageWidth=\"");
		this.sbuffer.append(jasperPrint.getPageWidth());
		this.sbuffer.append("\"");

		this.sbuffer.append(" pageHeight=\"");
		this.sbuffer.append(jasperPrint.getPageHeight());
		this.sbuffer.append("\"");

		if (jasperPrint.getOrientation() != JRReport.ORIENTATION_PORTRAIT)
		{
			this.sbuffer.append(" orientation=\"");
			this.sbuffer.append((String)JRXmlConstants.getOrientationMap().get(new Byte(jasperPrint.getOrientation())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");
		
		JRReportFont[] fonts = jasperPrint.getFonts();
		if (fonts != null && fonts.length > 0)
		{
			for(int i = 0; i < fonts.length; i++)
			{
				this.fontsMap.put(fonts[i].getName(), fonts[i]);
				this.exportReportFont(fonts[i]);
			}
		}

		Collection pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{
			JRPrintPage page = null;
			for(Iterator it = pages.iterator(); it.hasNext();)
			{
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = (JRPrintPage)it.next();
	
				/*   */
				this.exportPage(page);
			}
		}

		this.sbuffer.append("</jasperPrint>\n");

		return sbuffer;
	}


	/**
	 *
	 */
	private void exportReportFont(JRReportFont font)
	{
		this.sbuffer.append("\t<reportFont");

		this.sbuffer.append(" name=\"");
		this.sbuffer.append(font.getName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isDefault=\"");
		this.sbuffer.append(font.isDefault());
		this.sbuffer.append("\"");

		this.sbuffer.append(" fontName=\"");
		this.sbuffer.append(font.getFontName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" size=\"");
		this.sbuffer.append(font.getSize());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isBold=\"");
		this.sbuffer.append(font.isBold());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isItalic=\"");
		this.sbuffer.append(font.isItalic());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isUnderline=\"");
		this.sbuffer.append(font.isUnderline());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isStrikeThrough=\"");
		this.sbuffer.append(font.isStrikeThrough());
		this.sbuffer.append("\"");

		this.sbuffer.append(" pdfFontName=\"");
		this.sbuffer.append(font.getPdfFontName());
		this.sbuffer.append("\"");

		this.sbuffer.append(" pdfEncoding=\"");
		this.sbuffer.append(font.getPdfEncoding());
		this.sbuffer.append("\"");

		this.sbuffer.append(" isPdfEmbedded=\"");
		this.sbuffer.append(font.isPdfEmbedded());
		this.sbuffer.append("\"");

		this.sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	private void exportPage(JRPrintPage page) throws JRException
	{
		this.sbuffer.append("\t<page>\n");

		JRPrintElement element = null;
		Collection elements = page.getElements();
		if (elements != null && elements.size() > 0)
		{
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				element = (JRPrintElement)it.next();
				
				if (element instanceof JRPrintLine)
				{
					this.exportLine((JRPrintLine)element);
				}
				else if (element instanceof JRPrintRectangle)
				{
					this.exportRectangle((JRPrintRectangle)element);
				}
				else if (element instanceof JRPrintEllipse)
				{
					this.exportEllipse((JRPrintEllipse)element);
				}
				else if (element instanceof JRPrintImage)
				{
					this.exportImage((JRPrintImage)element);
				}
				else if (element instanceof JRPrintText)
				{
					this.exportText((JRPrintText)element);
				}
			}
		}

		this.sbuffer.append("\t</page>\n");
	}
	
	
	/**
	 *
	 */
	private void exportLine(JRPrintLine line)
	{
		this.sbuffer.append("\t\t<line");

		if (line.getDirection() != JRLine.DIRECTION_TOP_DOWN)
		{
			this.sbuffer.append(" direction=\"");
			this.sbuffer.append((String)JRXmlConstants.getDirectionMap().get(new Byte(line.getDirection())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.exportReportElement(line);
		this.exportGraphicElement(line);
		
		this.sbuffer.append("\t\t</line>\n");
	}


	/**
	 *
	 */
	private void exportReportElement(JRPrintElement element)
	{
		this.sbuffer.append("\t\t\t<reportElement");

		if (
			(element instanceof JRPrintLine && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintRectangle && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintEllipse && element.getMode() != JRElement.MODE_OPAQUE) ||
			(element instanceof JRPrintImage && element.getMode() != JRElement.MODE_TRANSPARENT) ||
			(element instanceof JRPrintText && element.getMode() != JRElement.MODE_TRANSPARENT)
			)
		{
			this.sbuffer.append(" mode=\"");
			this.sbuffer.append((String)JRXmlConstants.getModeMap().get(new Byte(element.getMode())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(" x=\"");
		this.sbuffer.append(element.getX());
		this.sbuffer.append("\"");

		this.sbuffer.append(" y=\"");
		this.sbuffer.append(element.getY());
		this.sbuffer.append("\"");

		this.sbuffer.append(" width=\"");
		this.sbuffer.append(element.getWidth());
		this.sbuffer.append("\"");

		this.sbuffer.append(" height=\"");
		this.sbuffer.append(element.getHeight());
		this.sbuffer.append("\"");

		if (element.getForecolor().getRGB() != Color.black.getRGB())
		{
			this.sbuffer.append(" forecolor=\"#");
			String hexa = Integer.toHexString(element.getForecolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			this.sbuffer.append(hexa);
			this.sbuffer.append("\"");
		}

		if (element.getBackcolor().getRGB() != Color.white.getRGB())
		{
			this.sbuffer.append(" backcolor=\"#");
			String hexa = Integer.toHexString(element.getBackcolor().getRGB() & colorMask).toUpperCase();
			hexa = ("000000" + hexa).substring(hexa.length());
			this.sbuffer.append(hexa);
			this.sbuffer.append("\"");
		}

		this.sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	private void exportGraphicElement(JRPrintGraphicElement element)
	{
		this.sbuffer.append("\t\t\t<graphicElement");

		if (
			(element instanceof JRPrintLine && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintRectangle && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintEllipse && element.getPen() != JRGraphicElement.PEN_1_POINT) ||
			(element instanceof JRPrintImage && element.getPen() != JRGraphicElement.PEN_NONE)
			)
		{
			this.sbuffer.append(" pen=\"");
			this.sbuffer.append((String)JRXmlConstants.getPenMap().get(new Byte(element.getPen())));
			this.sbuffer.append("\"");
		}

		if (element.getFill() != JRGraphicElement.FILL_SOLID)
		{
			this.sbuffer.append(" fill=\"");
			this.sbuffer.append((String)JRXmlConstants.getFillMap().get(new Byte(element.getFill())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append("/>\n");
	}


	/**
	 *
	 */
	private void exportRectangle(JRPrintRectangle rectangle)
	{
		this.sbuffer.append("\t\t<rectangle");

		if (rectangle.getRadius() != 0)
		{
			this.sbuffer.append(" radius=\"");
			this.sbuffer.append(rectangle.getRadius());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.exportReportElement(rectangle);
		this.exportGraphicElement(rectangle);
		
		this.sbuffer.append("\t\t</rectangle>\n");
	}


	/**
	 *
	 */
	private void exportEllipse(JRPrintEllipse ellipse)
	{
		this.sbuffer.append("\t\t\t<ellipse>\n");

		this.exportReportElement(ellipse);
		this.exportGraphicElement(ellipse);
		
		this.sbuffer.append("\t\t</ellipse>\n");
	}


	/**
	 *
	 */
	private void exportImage(JRPrintImage image) throws JRException
	{
		this.sbuffer.append("\t\t<image");

		if (image.getScaleImage() != JRImage.SCALE_IMAGE_RETAIN_SHAPE)
		{
			this.sbuffer.append(" scaleImage=\"");
			this.sbuffer.append((String)JRXmlConstants.getScaleImageMap().get(new Byte(image.getScaleImage())));
			this.sbuffer.append("\"");
		}
		
		if (image.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			this.sbuffer.append(" hAlign=\"");
			this.sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(image.getHorizontalAlignment())));
			this.sbuffer.append("\"");
		}
		
		if (image.getVerticalAlignment() != JRAlignment.VERTICAL_ALIGN_TOP)
		{
			this.sbuffer.append(" vAlign=\"");
			this.sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(image.getVerticalAlignment())));
			this.sbuffer.append("\"");
		}
		
		if (image.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			this.sbuffer.append(" hyperlinkType=\"");
			this.sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(image.getHyperlinkType())));
			this.sbuffer.append("\"");
		}

		if (image.getAnchorName() != null)
		{
			this.sbuffer.append(" anchorName=\"");
			this.sbuffer.append(image.getAnchorName());
			this.sbuffer.append("\"");
		}
				
		if (image.getHyperlinkReference() != null)
		{
			this.sbuffer.append(" hyperlinkReference=\"");
			this.sbuffer.append(image.getHyperlinkReference());
			this.sbuffer.append("\"");
		}

		if (image.getHyperlinkAnchor() != null)
		{
			this.sbuffer.append(" hyperlinkAnchor=\"");
			this.sbuffer.append(image.getHyperlinkAnchor());
			this.sbuffer.append("\"");
		}

		if (image.getHyperlinkPage() != null)
		{
			this.sbuffer.append(" hyperlinkPage=\"");
			this.sbuffer.append(image.getHyperlinkPage());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");


		this.exportReportElement(image);
		this.exportGraphicElement(image);
		

		if (image.getImageData() != null)
		{
			this.sbuffer.append("\t\t\t<imageSource");
	
			if (isEmbeddingImages)
			{
				this.sbuffer.append(" isEmbedded=\"");
				this.sbuffer.append(isEmbeddingImages);
				this.sbuffer.append("\"");
			}
	
			this.sbuffer.append("><![CDATA[");
	
			String imageSource = "";
			
			if (isEmbeddingImages)
			{
				try
				{
					ByteArrayInputStream bais = new ByteArrayInputStream(image.getImageData());
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
				byte[] imageData = image.getImageData();
				if (this.loadedImagesMap.containsKey(imageData))
				{
					imageSource = 
						(
						new File(
							new File(imagesDir.getName()), 
							(String)this.loadedImagesMap.get(imageData)
							)
						).getPath();
				}
				else
				{
					imageSource = "img_" + String.valueOf(this.loadedImagesMap.size());
					this.loadedImagesMap.put(imageData, imageSource);
	
					imageSource = 
						(
						new File(
							new File(imagesDir.getName()), 
							imageSource
							)
						).getPath();
				}
			}
			
			this.sbuffer.append(imageSource);
	
			this.sbuffer.append("]]></imageSource>\n");
		}
		
		this.sbuffer.append("\t\t</image>\n");
	}


	/**
	 *
	 */
	private void exportText(JRPrintText text)
	{
		this.sbuffer.append("\t\t<text");

		if (text.getTextAlignment() != JRAlignment.HORIZONTAL_ALIGN_LEFT)
		{
			this.sbuffer.append(" textAlignment=\"");
			this.sbuffer.append((String)JRXmlConstants.getHorizontalAlignMap().get(new Byte(text.getTextAlignment())));
			this.sbuffer.append("\"");
		}

		if (text.getVerticalAlignment() != JRTextElement.VERTICAL_ALIGN_TOP)
		{
			this.sbuffer.append(" verticalAlignment=\"");
			this.sbuffer.append((String)JRXmlConstants.getVerticalAlignMap().get(new Byte(text.getVerticalAlignment())));
			this.sbuffer.append("\"");

			this.sbuffer.append(" textHeight=\"");
			this.sbuffer.append(text.getTextHeight());
			this.sbuffer.append("\"");
		}

		if (text.getLineSpacing() != JRTextElement.LINE_SPACING_SINGLE)
		{
			this.sbuffer.append(" lineSpacing=\"");
			this.sbuffer.append((String)JRXmlConstants.getLineSpacingMap().get(new Byte(text.getLineSpacing())));
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(" absoluteLineSpacing=\"");
		this.sbuffer.append(text.getAbsoluteLineSpacing());
		this.sbuffer.append("\"");

		this.sbuffer.append(" absoluteLeading=\"");
		this.sbuffer.append(text.getAbsoluteLeading());
		this.sbuffer.append("\"");

		if (text.getHyperlinkType() != JRHyperlink.HYPERLINK_TYPE_NONE)
		{
			this.sbuffer.append(" hyperlinkType=\"");
			this.sbuffer.append((String)JRXmlConstants.getHyperlinkTypeMap().get(new Byte(text.getHyperlinkType())));
			this.sbuffer.append("\"");
		}

		if (text.getAnchorName() != null)
		{
			this.sbuffer.append(" anchorName=\"");
			this.sbuffer.append(text.getAnchorName());
			this.sbuffer.append("\"");
		}
				
		if (text.getHyperlinkReference() != null)
		{
			this.sbuffer.append(" hyperlinkReference=\"");
			this.sbuffer.append(text.getHyperlinkReference());
			this.sbuffer.append("\"");
		}

		if (text.getHyperlinkAnchor() != null)
		{
			this.sbuffer.append(" hyperlinkAnchor=\"");
			this.sbuffer.append(text.getHyperlinkAnchor());
			this.sbuffer.append("\"");
		}

		if (text.getHyperlinkPage() != null)
		{
			this.sbuffer.append(" hyperlinkPage=\"");
			this.sbuffer.append(text.getHyperlinkPage());
			this.sbuffer.append("\"");
		}

		this.sbuffer.append(">\n");

		this.exportReportElement(text);

		String font = this.exportFont(text.getFont());
		if (font != null)
		{
			this.sbuffer.append("\t\t\t" + font + "\n");
		}

		if (text.getText() != null)
		{
			this.sbuffer.append("\t\t\t<textContent><![CDATA[");
			this.sbuffer.append(text.getText());
			this.sbuffer.append("]]></textContent>\n");
		}

		this.sbuffer.append("\t\t</text>\n");
	}


	/**
	 *
	 */
	private String exportFont(JRFont font)
	{
		String fontChunk = null;
		
		if (font != null)
		{
			StringBuffer tmpBuffer = new StringBuffer();
		
			if(font.getReportFont() != null)
			{
				JRFont baseFont = 
					(JRFont)this.fontsMap.get(
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
