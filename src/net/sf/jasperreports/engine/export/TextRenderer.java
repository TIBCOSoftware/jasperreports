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
package net.sf.jasperreports.engine.export;

import java.awt.Graphics2D;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TextRenderer
{

	/**
	 *
	 *
	private static FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(null, true, true);

	/**
	 * 
	 *
	private JRFillTextElement fillTextElement = null;


	/**
	 * 
	 *
	private MaxFontSizeFinder maxFontSizeFinder = null;

	private float width = 0;
	private float height = 0;
	private float lineSpacing = 0;

	private int maxHeight = 0;
	private int textOffset = 0;
	private int lines = 0;
	private int fontSizeSum = 0;
	private int firstLineMaxFontSize = 0;
	private float textHeight = 0;
	private float firstLineLeading = 0;
	private boolean isLeftToRight = true;
	private boolean isMaxHeightReached = false;
	*/
	
	private Graphics2D grx = null;
	private int x = 0;
	private int y = 0;
	private int topPadding = 0;
	private int leftPadding = 0;
	private float formatWidth = 0;
	private float verticalOffset = 0;
	private float floatLineSpacing = 0;
	private int maxHeight = 0;
	private float drawPosY = 0;
	private float drawPosX = 0;
	private boolean isMaxHeightReached = false;
	private byte textAlignment = 0;
	
	
	/**
	 * 
	 */
	public void render(
		Graphics2D grx,
		int x,
		int y,
		int width,
		int height,
		int topPadding,
		int leftPadding,
		int bottomPadding,
		int rightPadding,
		float textHeight,
		byte textAlignment,
		byte verticalAlignment,
		byte lineSpacing,
		JRStyledText styledText,
		String allText
		)
	{
		/*   */
		initialize(
			grx, 
			x, 
			y, 
			width, 
			height, 
			topPadding,
			leftPadding,
			bottomPadding,
			rightPadding,
			textHeight, 
			textAlignment, 
			verticalAlignment, 
			lineSpacing
			);
		
		AttributedCharacterIterator allParagraphs = styledText.getAttributedString().getIterator();

		int tokenPosition = 0;
		int lastParagraphStart = 0;
		String lastParagraphText = null;

		StringTokenizer tkzer = new StringTokenizer(allText, "\n", true);

		while(tkzer.hasMoreTokens() && !isMaxHeightReached) 
		{
			String token = tkzer.nextToken();

			if ("\n".equals(token))
			{
				renderParagraph(allParagraphs, lastParagraphStart, lastParagraphText);

				lastParagraphStart = tokenPosition;
				lastParagraphText = null;
			}
			else
			{
				lastParagraphStart = tokenPosition;
				lastParagraphText = token;
			}

			tokenPosition += token.length();
		}

		if (!isMaxHeightReached && lastParagraphStart < allText.length())
		{
			renderParagraph(allParagraphs, lastParagraphStart, lastParagraphText);
		}
	}


	/**
	 * 
	 */
	private void initialize(
		Graphics2D grx,
		int x,
		int y,
		int width,
		int height,
		int topPadding,
		int leftPadding,
		int bottomPadding,
		int rightPadding,
		float textHeight,
		byte textAlignment,
		byte verticalAlignment,
		byte lineSpacing
		)
	{
		this.grx = grx;
		
		this.textAlignment = textAlignment;

		verticalOffset = 0f;
		switch (verticalAlignment)
		{
			case JRTextElement.VERTICAL_ALIGN_TOP :
			{
				verticalOffset = 0f;
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_MIDDLE :
			{
				verticalOffset = ((float)height - textHeight) / 2f;
				break;
			}
			case JRTextElement.VERTICAL_ALIGN_BOTTOM :
			{
				verticalOffset = (float)height - textHeight;
				break;
			}
			default :
			{
				verticalOffset = 0f;
			}
		}

		floatLineSpacing = 1f;
		switch (lineSpacing)
		{
			case JRTextElement.LINE_SPACING_SINGLE :
			{
				floatLineSpacing = 1f;
				break;
			}
			case JRTextElement.LINE_SPACING_1_1_2 :
			{
				floatLineSpacing = 1.5f;
				break;
			}
			case JRTextElement.LINE_SPACING_DOUBLE :
			{
				floatLineSpacing = 2f;
				break;
			}
			default :
			{
				floatLineSpacing = 1f;
			}
		}

		this.x = x;
		this.y = y;
		this.topPadding = topPadding;
		this.leftPadding = leftPadding;
		formatWidth = width - leftPadding - rightPadding;
		formatWidth = formatWidth < 0 ? 0 : formatWidth;
		maxHeight = height - topPadding - bottomPadding;
		maxHeight = maxHeight < 0 ? 0 : maxHeight;

		drawPosY = 0;
	    drawPosX = 0;
	
		isMaxHeightReached = false;
	}
	
	/**
	 * 
	 */
	private void renderParagraph(
		AttributedCharacterIterator allParagraphs,
		int lastParagraphStart,
		String lastParagraphText
		)
	{
		AttributedCharacterIterator paragraph = null;
		
		if (lastParagraphText == null)
		{
			paragraph = 
				new AttributedString(
					" ",
					new AttributedString(
						allParagraphs, 
						lastParagraphStart, 
						lastParagraphStart + 1
						).getIterator().getAttributes()
					).getIterator();
		}
		else
		{
			paragraph = 
				new AttributedString(
					allParagraphs, 
					lastParagraphStart, 
					lastParagraphStart + lastParagraphText.length()
					).getIterator();
		}

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, grx.getFontRenderContext());
	
		while (lineMeasurer.getPosition() < paragraph.getEndIndex() && !isMaxHeightReached)
		{
			//eugene fix - start
			int startIndex = lineMeasurer.getPosition();
			//eugene fix - end

			TextLayout layout = lineMeasurer.nextLayout(formatWidth);

			//eugene fix - start
			AttributedString tmpText = 
				new AttributedString(
					paragraph, 
					startIndex, 
					startIndex + layout.getCharacterCount()
					);
			layout = new TextLayout(tmpText.getIterator(), grx.getFontRenderContext());
			//eugene fix - end

			drawPosY += layout.getLeading() + floatLineSpacing * layout.getAscent();

			if (drawPosY + layout.getDescent() <= maxHeight)
			{
			    switch (textAlignment)
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
					    if (lineMeasurer.getPosition() < paragraph.getEndIndex())
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

			    draw(layout);
			    drawPosY += layout.getDescent();
			}
			else
			{
			    drawPosY -= layout.getLeading() + floatLineSpacing * layout.getAscent();
	    	    isMaxHeightReached = true;
			}
		}
	}
	
	/**
	 * 
	 */
	public float getTextHeight()
	{
		return drawPosY + 1;
	}
	
	/**
	 * 
	 */
	public void draw(TextLayout layout)
	{
	    layout.draw(
			grx,
			drawPosX + x + leftPadding,
			drawPosY + y + topPadding + verticalOffset
			);
	}
	
}
