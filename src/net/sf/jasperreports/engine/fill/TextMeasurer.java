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
package net.sf.jasperreports.engine.fill;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TextMeasurer
{

	/**
	 *
	 */
	private static FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(null, true, true);

	/**
	 * 
	 */
	private JRFillTextElement fillTextElement = null;


	/**
	 * 
	 */
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
	
	/**
	 * 
	 */
	public TextMeasurer(JRFillTextElement fillTextElement)
	{
		this.fillTextElement = fillTextElement;

		/*   */
		width = fillTextElement.getWidth();
		height = fillTextElement.getHeight();

		switch (fillTextElement.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				width = fillTextElement.getHeight();
				height = fillTextElement.getWidth();
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				width = fillTextElement.getHeight();
				height = fillTextElement.getWidth();
				break;
			}
			case JRTextElement.ROTATION_NONE :
			default :
			{
			}
		}
		
		/*   */
		switch (fillTextElement.getLineSpacing())
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

		/*   */
		if (fillTextElement.isStyledText())
		{
			maxFontSizeFinder = new StyledTextMaxFontFinder();
		}
		else
		{
			maxFontSizeFinder = new DefaultMaxFontFinder();
		}
	}
	
	/**
	 * 
	 */
	private void initialize(int availableStretchHeight)
	{
		maxHeight = (int)height + availableStretchHeight;
		textOffset = 0;
		lines = 0;
		fontSizeSum = 0;
		firstLineMaxFontSize = 0;
		textHeight = 0;
		firstLineLeading = 0;
		isLeftToRight = true;
		isMaxHeightReached = false;
	}

	/**
	 * 
	 */
	public void measure(
		JRStyledText styledText,
		String allText,
		int availableStretchHeight 
		)
	{
		/*   */
		initialize(availableStretchHeight);

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

				lastParagraphStart = tokenPosition + (tkzer.hasMoreTokens() || tokenPosition == 0 ? 1 : 0);
				lastParagraphText = null;
			}
			else
			{
				lastParagraphStart = tokenPosition;
				lastParagraphText = token;
			}

			tokenPosition += token.length();
		}

		if (!isMaxHeightReached)
		{
			renderParagraph(allParagraphs, lastParagraphStart, lastParagraphText);
		}
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

		int positionWithinParagraph = 0;

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, FONT_RENDER_CONTEXT);
		
		while (lineMeasurer.getPosition() < paragraph.getEndIndex() && !isMaxHeightReached)
		{
			int lineStartPosition = lineMeasurer.getPosition();

			TextLayout layout = lineMeasurer.nextLayout(width);

			isLeftToRight = isLeftToRight && layout.isLeftToRight();
			
			textHeight += layout.getLeading() + lineSpacing * layout.getAscent();

			if (textHeight + layout.getDescent() <= maxHeight)
			{
			    lines++;

				fontSizeSum += 
					maxFontSizeFinder.findMaxFontSize(
						new AttributedString(
							paragraph, 
							lineStartPosition, 
							lineStartPosition + layout.getCharacterCount()
							).getIterator(),
						fillTextElement.getFont().getSize()
						);
						
				if (lines == 1)
				{
					firstLineLeading = textHeight;
					firstLineMaxFontSize = fontSizeSum;
				}

			    positionWithinParagraph = lineMeasurer.getPosition();
				// here is the Y offset where we would draw the line
				//lastDrawPosY = drawPosY;
				//
				textHeight += layout.getDescent();
			}
			else
			{
			    textHeight -= layout.getLeading() + lineSpacing * layout.getAscent();
	    	    isMaxHeightReached = true;
			}
		}
		
		textOffset = lastParagraphStart + positionWithinParagraph;//(lastParagraphText == null ? 0 : positionWithinParagraph);
	}
	
	
	/**
	 * 
	 */
	protected boolean isLeftToRight()
	{
		return isLeftToRight;
	}
	
	/**
	 * 
	 */
	protected int getTextOffset()
	{
		return textOffset;
	}
	
	/**
	 * 
	 */
	protected float getTextHeight()
	{
		return textHeight + 1;
	}
	
	/**
	 * 
	 */
	protected float getLineSpacingFactor()
	{
		if (lines > 0)
		{
			return getTextHeight() / fontSizeSum;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * 
	 */
	protected float getLeadingOffset()
	{
		return firstLineLeading - firstLineMaxFontSize * getLineSpacingFactor();
	}
	
}


/**
 * 
 */
interface MaxFontSizeFinder
{
	/**
	 * 
	 */
	public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize);
}


/**
 * 
 */
class StyledTextMaxFontFinder implements MaxFontSizeFinder
{
	
	private static final Float ZERO = new Float(0);
	
	/**
	 * 
	 */
	public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize)
	{
		line.setIndex(0);
		Float maxFontSize = ZERO;
		int runLimit = 0;

		while(runLimit < line.getEndIndex() && (runLimit = line.getRunLimit(TextAttribute.SIZE)) <= line.getEndIndex())
		{
			Float size = (Float)line.getAttribute(TextAttribute.SIZE);
			if (maxFontSize.compareTo(size) < 0)
			{
				maxFontSize = size;
			}
			line.setIndex(runLimit);
		}

		return maxFontSize.intValue();
	}
}


/**
 * 
 */
class DefaultMaxFontFinder implements MaxFontSizeFinder
{
	/**
	 * 
	 */
	public int findMaxFontSize(AttributedCharacterIterator line, int defaultFontSize)
	{
		return defaultFontSize;
	}
}
