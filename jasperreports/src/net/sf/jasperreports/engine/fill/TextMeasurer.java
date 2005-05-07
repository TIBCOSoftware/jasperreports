/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

	private int width = 0;
	private int height = 0;
	private int topPadding = 0;
	private int leftPadding = 0;
	private int bottomPadding = 0;
	private int rightPadding = 0;
	private float lineSpacing = 0;

	private float formatWidth = 0;
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
		
		if (fillTextElement.getBox() != null)
		{
			topPadding = fillTextElement.getBox().getTopPadding();
			leftPadding = fillTextElement.getBox().getLeftPadding();
			bottomPadding = fillTextElement.getBox().getBottomPadding();
			rightPadding = fillTextElement.getBox().getRightPadding();
		}

		switch (fillTextElement.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				width = fillTextElement.getHeight();
				height = fillTextElement.getWidth();
				int tmpPadding = topPadding;
				topPadding = leftPadding;
				leftPadding = bottomPadding;
				bottomPadding = rightPadding;
				rightPadding = tmpPadding;
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				width = fillTextElement.getHeight();
				height = fillTextElement.getWidth();
				int tmpPadding = topPadding;
				topPadding = rightPadding;
				rightPadding = bottomPadding;
				bottomPadding = leftPadding;
				leftPadding = tmpPadding;
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
		formatWidth = width - leftPadding - rightPadding;
		formatWidth = formatWidth < 0 ? 0 : formatWidth;
		maxHeight = height + availableStretchHeight - topPadding - bottomPadding;
		maxHeight = maxHeight < 0 ? 0 : maxHeight;
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

		if (!isMaxHeightReached && lastParagraphStart < allText.length())
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

			TextLayout layout = lineMeasurer.nextLayout(formatWidth);

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
