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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.MaxFontSizeFinder;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TextMeasurer
{

	/**
	 *
	 */
	private static final FontRenderContext FONT_RENDER_CONTEXT = TextRenderer.LINE_BREAK_FONT_RENDER_CONTEXT;

	/**
	 * 
	 */
	private JRTextElement textElement = null;


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
	public TextMeasurer(JRTextElement textElement)
	{
		this.textElement = textElement;

		/*   */
		width = textElement.getWidth();
		height = textElement.getHeight();
		
		topPadding = textElement.getTopPadding();
		leftPadding = textElement.getLeftPadding();
		bottomPadding = textElement.getBottomPadding();
		rightPadding = textElement.getRightPadding();

		switch (textElement.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				width = textElement.getHeight();
				height = textElement.getWidth();
				int tmpPadding = topPadding;
				topPadding = leftPadding;
				leftPadding = bottomPadding;
				bottomPadding = rightPadding;
				rightPadding = tmpPadding;
				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				width = textElement.getHeight();
				height = textElement.getWidth();
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
		switch (textElement.getLineSpacing())
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

		maxFontSizeFinder = MaxFontSizeFinder.getInstance(textElement.isStyledText());
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
		String remainingText,
		int remainingTextStart,
		int availableStretchHeight 
		)
	{
		/*   */
		initialize(availableStretchHeight);

		AttributedCharacterIterator allParagraphs = styledText.getAttributedString().getIterator();

		int tokenPosition = remainingTextStart;
		int lastParagraphStart = remainingTextStart;
		String lastParagraphText = null;

		StringTokenizer tkzer = new StringTokenizer(remainingText, "\n", true);

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

		if (!isMaxHeightReached && lastParagraphStart < remainingTextStart + remainingText.length())
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

			// we consider the extra pixel that is going to be added 
			// in the end to the overall text height. see getTextHeight() below
			if (textHeight + layout.getDescent() + 1 <= maxHeight)   
			{
				lines++;

				fontSizeSum += 
					maxFontSizeFinder.findMaxFontSize(
						new AttributedString(
							paragraph, 
							lineStartPosition, 
							lineStartPosition + layout.getCharacterCount()
							).getIterator(),
						textElement.getFontSize()
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
	public float getTextHeight()
	{
		// we add an extra pixel to make sure the text field 
		// is tall enough and letters do not get cut at the bottom.
		return textHeight + 1;
	}
	
	/**
	 * 
	 */
	public float getLineSpacingFactor()
	{
		if (lines > 0)
		{
			return textHeight / fontSizeSum;
		}
		return 0;
	}
	
	/**
	 * 
	 */
	public float getLeadingOffset()
	{
		return firstLineLeading - firstLineMaxFontSize * getLineSpacingFactor();
	}
	
}
