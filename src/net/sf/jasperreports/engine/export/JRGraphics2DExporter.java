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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 */
package dori.jasper.engine.export;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import dori.jasper.engine.JRAbstractExporter;
import dori.jasper.engine.JRAlignment;
import dori.jasper.engine.JRElement;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRExporterParameter;
import dori.jasper.engine.JRFont;
import dori.jasper.engine.JRGraphicElement;
import dori.jasper.engine.JRImage;
import dori.jasper.engine.JRLine;
import dori.jasper.engine.JRPrintElement;
import dori.jasper.engine.JRPrintEllipse;
import dori.jasper.engine.JRPrintImage;
import dori.jasper.engine.JRPrintLine;
import dori.jasper.engine.JRPrintPage;
import dori.jasper.engine.JRPrintRectangle;
import dori.jasper.engine.JRPrintText;
import dori.jasper.engine.JRReportFont;
import dori.jasper.engine.JRTextElement;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.design.JRDesignReportFont;
import dori.jasper.engine.util.JRGraphEnvInitializer;
import dori.jasper.engine.util.JRImageLoader;
import dori.jasper.engine.util.JRLoader;
import dori.jasper.engine.util.JRStringUtil;


/**
 *
 */
public class JRGraphics2DExporter extends JRAbstractExporter
{


	/**
	 *
	 */
	private JasperPrint jasperPrint = null;
	private Graphics2D grx = null;
	private int pageIndex = 0;
	private float zoom = 1f;

	/**
	 *
	 */
	private JRReportFont defaultFont = null;


	/**
	 *
	 */
	public JRGraphics2DExporter() throws JRException
	{
		JRGraphEnvInitializer.initializeGraphEnv();
	}
	

	/**
	 *
	 */
	private JRReportFont getDefaultFont()
	{
		if (this.defaultFont == null)
		{
			this.defaultFont = jasperPrint.getDefaultFont();
			if (this.defaultFont == null)
			{
				this.defaultFont = new JRDesignReportFont();
			}
		}
		
		return this.defaultFont;
	}


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

		grx = (Graphics2D)this.parameters.get(JRGraphics2DExporterParameter.GRAPHICS_2D);
		if (grx == null)
		{
			throw new JRException("No output specified for the exporter. java.awt.Graphics2D object expected.");
		}
		
		int lastPageIndex = -1;
		if (this.jasperPrint.getPages() != null)
		{
			lastPageIndex = this.jasperPrint.getPages().size() - 1;
		}

		Integer index = (Integer)this.parameters.get(JRExporterParameter.PAGE_INDEX);
		if (index != null)
		{
			this.pageIndex = index.intValue();
			if (this.pageIndex < 0 || this.pageIndex > lastPageIndex)
			{
				throw new JRException("Page index out of range : " + this.pageIndex + " of " + lastPageIndex);
			}
		}

		Float zoomRatio = (Float)this.parameters.get(JRGraphics2DExporterParameter.ZOOM_RATIO);
		if (zoomRatio != null)
		{
			this.zoom = zoomRatio.floatValue();
			if (this.zoom <= 0)
			{
				throw new JRException("Invalid zoom ratio : " + this.zoom);
			}
		}

		this.exportReportToGraphics2D();
	}
		

	/**
	 *
	 */
	public void exportReportToGraphics2D()
	{
		grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//grx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		grx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		AffineTransform atrans = new AffineTransform();
		atrans.scale(zoom, zoom);
		grx.transform(atrans);

		java.util.List pages = jasperPrint.getPages();
		if (pages != null)
		{
			JRPrintPage page = (JRPrintPage)pages.get(this.pageIndex);
			this.exportPage(page);
		}
	}
	

	/**
	 *
	 */
	private void exportPage(JRPrintPage page)
	{
		grx.setColor(Color.white);
		grx.fillRect(
			0, 
			0, 
			jasperPrint.getPageWidth(), 
			jasperPrint.getPageHeight()
			);

		grx.setColor(Color.black);
		grx.setStroke(new BasicStroke(1));

		/*   */
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
	}
	
	
	/**
	 *
	 */
	private void exportLine(JRPrintLine line)
	{
		grx.setColor(line.getForecolor());
		
		Stroke stroke = null;
		switch (line.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				stroke = new BasicStroke(
					1f,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					0f,
					new float[]{5f, 3f},
					0f
					);
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				stroke = new BasicStroke(4f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				stroke = new BasicStroke(2f);
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				stroke = new BasicStroke(0.5f);
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				stroke = new BasicStroke(1f);
				break;
			}
		}

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				grx.drawLine(
					line.getX(), 
					line.getY(),
					line.getX() + line.getWidth() - 1,  
					line.getY() + line.getHeight() - 1
					);
			}
			else
			{
				grx.drawLine(
					line.getX(), 
					line.getY() + line.getHeight() - 1,
					line.getX() + line.getWidth() - 1,  
					line.getY()
					);
			}
		}
	}


	/**
	 *
	 */
	private void exportRectangle(JRPrintRectangle rectangle)
	{
		if (rectangle.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(rectangle.getBackcolor());
			grx.fillRoundRect(
				rectangle.getX(), 
				rectangle.getY(), 
				rectangle.getWidth(),
				rectangle.getHeight(),
				2 * rectangle.getRadius(),
				2 * rectangle.getRadius()
				);
		}

		grx.setColor(rectangle.getForecolor());

		Stroke stroke = null;
		switch (rectangle.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				stroke = new BasicStroke(
					1f,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					0f,
					new float[]{5f, 3f},
					0f
					);
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				stroke = new BasicStroke(4f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				stroke = new BasicStroke(2f);
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				stroke = new BasicStroke(0.5f);
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				stroke = new BasicStroke(1f);
				break;
			}
		}

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			grx.drawRoundRect(
				rectangle.getX(), 
				rectangle.getY(), 
				rectangle.getWidth() - 1,
				rectangle.getHeight() - 1,
				2 * rectangle.getRadius(),
				2 * rectangle.getRadius()
				);
		}
	}


	/**
	 *
	 */
	private void exportEllipse(JRPrintEllipse ellipse)
	{
		if (ellipse.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(ellipse.getBackcolor());
			grx.fillOval(
				ellipse.getX(), 
				ellipse.getY(), 
				ellipse.getWidth(),
				ellipse.getHeight()
				);
		}

		grx.setColor(ellipse.getForecolor());

		Stroke stroke = null;
		switch (ellipse.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				stroke = new BasicStroke(
					1f,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					0f,
					new float[]{5f, 3f},
					0f
					);
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				stroke = new BasicStroke(4f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				stroke = new BasicStroke(2f);
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				stroke = new BasicStroke(0.5f);
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				stroke = new BasicStroke(1f);
				break;
			}
		}

		if (stroke != null)
		{
			grx.setStroke(stroke);
			
			grx.drawOval(
				ellipse.getX(), 
				ellipse.getY(), 
				ellipse.getWidth() - 1,
				ellipse.getHeight() - 1
				);
		}
	}


	/**
	 *
	 */
	private void exportImage(JRPrintImage printImage)
	{
		if (printImage.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(printImage.getBackcolor());

			grx.fillRect(
				printImage.getX(), 
				printImage.getY(), 
				printImage.getWidth(),
				printImage.getHeight()
				);
		}

		
		int borderOffset = 0;
		Stroke stroke = null;
		switch (printImage.getPen())
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				borderOffset = 0;
				stroke = new BasicStroke(
					1f,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,
					0f,
					new float[]{5f, 3f},
					0f
					);
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				borderOffset = 2;
				stroke = new BasicStroke(4f);
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				borderOffset = 1;
				stroke = new BasicStroke(2f);
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				borderOffset = 0;
				stroke = new BasicStroke(0.5f);
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				borderOffset = 0;
				stroke = new BasicStroke(1f);
				break;
			}
		}


		int availableImageWidth = printImage.getWidth() - 2 * borderOffset;
		availableImageWidth = (availableImageWidth < 0)?0:availableImageWidth;

		int availableImageHeight = printImage.getHeight() - 2 * borderOffset;
		availableImageHeight = (availableImageHeight < 0)?0:availableImageHeight;
		
		byte[] imageData = printImage.getImageData();
		
		if (
			availableImageWidth > 0 && availableImageHeight > 0 &&
			imageData != null && imageData.length > 0
			)
		{
			Image awtImage = JRImageLoader.loadImage( imageData );
	
			int awtWidth = awtImage.getWidth(null);
			int awtHeight = awtImage.getHeight(null);

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

			switch (printImage.getScaleImage())// FIXME maybe put this in JRFiller
			{
				case JRImage.SCALE_IMAGE_CLIP :
				{
					int xoffset = (int)(xalignFactor * (availableImageWidth - awtWidth));
					int yoffset = (int)(yalignFactor * (availableImageHeight - awtHeight));

					grx.setClip(
						printImage.getX() + borderOffset, 
						printImage.getY() + borderOffset, 
						availableImageWidth, 
						availableImageHeight
						);
					grx.drawImage(
						awtImage, 
						printImage.getX() + xoffset + borderOffset, 
						printImage.getY() + yoffset + borderOffset, 
						awtWidth, 
						awtHeight, 
						//Color.red,
						null
						);
					grx.setClip(
						0, 
						0, 
						jasperPrint.getPageWidth(), 
						jasperPrint.getPageHeight()
						);
	
					break;
				}
				case JRImage.SCALE_IMAGE_FILL_FRAME :
				{
					grx.drawImage(
						awtImage, 
						printImage.getX() + borderOffset, 
						printImage.getY() + borderOffset, 
						availableImageWidth, 
						availableImageHeight,
						//Color.red,
						null
						);
	
					break;
				}
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE :
				default :
				{
					if (printImage.getHeight() > 0)
					{
						double ratio = (double)awtWidth / (double)awtHeight;
						
						if( ratio > (double)availableImageWidth / (double)availableImageHeight )
						{
							awtWidth = availableImageWidth; 
							awtHeight = (int)(availableImageWidth / ratio); 
						}
						else
						{
							awtWidth = (int)(availableImageHeight * ratio); 
							awtHeight = availableImageHeight; 
						}

						int xoffset = (int)(xalignFactor * (availableImageWidth - awtWidth));
						int yoffset = (int)(yalignFactor * (availableImageHeight - awtHeight));

						grx.drawImage(
							awtImage, 
							printImage.getX() + xoffset + borderOffset, 
							printImage.getY() + yoffset + borderOffset, 
							awtWidth, 
							awtHeight, 
							//Color.red,
							null
							);
					}
					
					break;
				}
			}
		}

		if (stroke != null)
		{
			grx.setColor(printImage.getForecolor());
			
			grx.setStroke(stroke);
	
			grx.drawRect(
				printImage.getX(), 
				printImage.getY(), 
				printImage.getWidth() - 1,
				printImage.getHeight() - 1
				);
		}
	}


	/**
	 *
	 */
	private void exportText(JRPrintText text)
	{
		String allText = text.getText();

		if (allText == null)
		{
			return;
		}
		
		if (text.getMode() == JRElement.MODE_OPAQUE)
		{
			grx.setColor(text.getBackcolor());
			grx.fillRect(
				text.getX(), 
				text.getY(), 
				text.getWidth(),
				text.getHeight()
				);
		}
		else
		{
			/*
			grx.setColor(text.getForecolor());
			grx.setStroke(new BasicStroke(1));
			grx.drawRect(
				text.getX(), 
				text.getY(), 
				text.getWidth(),
				text.getHeight()
				);
			*/
		}

		if (allText.length() == 0)
		{
			return;
		}
		
		grx.setColor(text.getForecolor());

		allText = JRStringUtil.treatNewLineChars(allText);

		float formatWidth = (float) text.getWidth();

		float verticalOffset = 0f;
		switch (text.getVerticalAlignment())
		{
			case JRTextElement.VERTICAL_ALIGN_TOP :
			{
				verticalOffset = 0f;
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_MIDDLE :
			{
				verticalOffset = ((float)text.getHeight() - text.getTextHeight()) / 2f;
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_BOTTOM :
			{
				verticalOffset = (float)text.getHeight() - text.getTextHeight();
				break;
			}
			default :
			{
				verticalOffset = 0f;
			}
		}

		float lineSpacing = 1f;
		switch (text.getLineSpacing())
		{
			case JRTextElement.LINE_SPACING_SINGLE :
			{
				lineSpacing = 1f;
				break;
			}
			case JRTextElement.LINE_SPACING_1_1_2 :
			{
				lineSpacing = 1.5f;
				break;
			}
			case JRTextElement.LINE_SPACING_DOUBLE :
			{
				lineSpacing = 2f;
				break;
			}
			default :
			{
				lineSpacing = 1f;
			}
		}

		int maxHeight = text.getHeight();
		//FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
		FontRenderContext fontRenderContext = grx.getFontRenderContext();
		JRFont font = text.getFont();
		if (font == null)
		{
			font = this.getDefaultFont();
		}
		Map fontAttributes = font.getAttributes();

		AttributedString atext;
		AttributedCharacterIterator paragraph;
		int paragraphStart;
		int paragraphEnd;
		LineBreakMeasurer lineMeasurer;
	
		float drawPosY = 0;
	    float drawPosX = 0;
	
		String paragr_text = "";
		boolean isMaxHeightReached = false;
		
		StringTokenizer tkzer = new StringTokenizer(allText, "\n");
		
		while(tkzer.hasMoreTokens() && !isMaxHeightReached) 
		{
			paragr_text = tkzer.nextToken();
			
			atext = new AttributedString(paragr_text, fontAttributes);
			paragraph = atext.getIterator();
			paragraphStart = paragraph.getBeginIndex();
			paragraphEnd = paragraph.getEndIndex();
			lineMeasurer = new LineBreakMeasurer(paragraph,fontRenderContext);
			lineMeasurer.setPosition(paragraphStart);
	
			TextLayout layout = null;
			while (lineMeasurer.getPosition() < paragraphEnd && !isMaxHeightReached)
			{
				//eugene fix - start
				int startIndex = lineMeasurer.getPosition();
				//eugene fix - end

				layout = lineMeasurer.nextLayout(formatWidth);

				//eugene fix - start
				AttributedString tmpText = 
					new AttributedString(
						paragraph, 
						startIndex, 
						startIndex + layout.getCharacterCount()
						);
				layout = new TextLayout(tmpText.getIterator(), fontRenderContext);
				//eugene fix - end

				drawPosY += layout.getLeading() + lineSpacing * layout.getAscent();
	
				if (drawPosY + layout.getDescent() <= maxHeight)
				{
				    switch (text.getTextAlignment())
				    {
						case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					    {
						    if (layout.isLeftToRight())
						    {
							    drawPosX = 0;
						    }
						    else
						    {
							    drawPosX = formatWidth - layout.getAdvance();
						    }
						    if (lineMeasurer.getPosition() < paragraphEnd)
						    {
							    layout = layout.getJustifiedLayout(formatWidth);
							}
	
						    break;
					    }
						case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					    {
						    if (layout.isLeftToRight())
						    {
							    drawPosX = formatWidth - layout.getAdvance();
						    }
						    else
						    {
							    drawPosX = formatWidth;
						    }
						    break;
					    }
						case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					    {
						    drawPosX = (formatWidth - layout.getAdvance()) / 2;
						    break;
					    }
						case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					    default :
					    {
						    if (layout.isLeftToRight())
						    {
							    drawPosX = 0;
						    }
						    else
						    {
							    drawPosX = formatWidth - layout.getAdvance();
						    }
					    }
				    }
	
				    layout.draw(
						grx,
						drawPosX + text.getX(),
						drawPosY + text.getY() + verticalOffset
						);
				    drawPosY += layout.getDescent();
				}
				else
				{
				    drawPosY -= layout.getLeading() + lineSpacing * layout.getAscent();
		    	    isMaxHeightReached = true;
				}
			}
		}
	}


}
