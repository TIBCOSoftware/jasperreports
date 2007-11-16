/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
import java.text.BreakIterator;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.MaxFontSizeFinder;


/**
 * Default text measurer implementation.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TextMeasurer implements JRTextMeasurer
{

	/**
	 *
	 */
	private static final FontRenderContext FONT_RENDER_CONTEXT = TextRenderer.LINE_BREAK_FONT_RENDER_CONTEXT;

	private JRText textElement;

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
	private TextMeasuredState measuredState;
	
	protected static class TextMeasuredState implements JRMeasuredText, Cloneable
	{
		protected int textOffset = 0;
		protected int lines = 0;
		protected int fontSizeSum = 0;
		protected int firstLineMaxFontSize = 0;
		protected float textHeight = 0;
		protected float firstLineLeading = 0;
		protected boolean isLeftToRight = true;
		
		public boolean isLeftToRight()
		{
			return isLeftToRight;
		}
		
		public int getTextOffset()
		{
			return textOffset;
		}
		
		public float getTextHeight()
		{
			return textHeight;
		}
		
		public float getLineSpacingFactor()
		{
			if (lines > 0)
			{
				return textHeight / fontSizeSum;
			}
			return 0;
		}
		
		public float getLeadingOffset()
		{
			return firstLineLeading - firstLineMaxFontSize * getLineSpacingFactor();
		}
		
		public TextMeasuredState cloneState()
		{
			try
			{
				return (TextMeasuredState) super.clone();
			}
			catch (CloneNotSupportedException e)
			{
				//never
				throw new JRRuntimeException(e);
			}
		}
	}
	
	/**
	 * 
	 */
	public TextMeasurer(JRText textElement)
	{
		this.textElement = textElement;
	}
	
	/**
	 * 
	 */
	protected void initialize(int availableStretchHeight)
	{
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
			case JRTextElement.ROTATION_UPSIDE_DOWN :
			{
				int tmpPadding = topPadding;
				topPadding = bottomPadding;
				bottomPadding = tmpPadding;
				tmpPadding = leftPadding;
				leftPadding = rightPadding;
				rightPadding = tmpPadding;
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

		formatWidth = width - leftPadding - rightPadding;
		formatWidth = formatWidth < 0 ? 0 : formatWidth;
		maxHeight = height + availableStretchHeight - topPadding - bottomPadding;
		maxHeight = maxHeight < 0 ? 0 : maxHeight;
		measuredState = new TextMeasuredState();
	}

	/**
	 * 
	 */
	public JRMeasuredText measure(
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

		boolean rendered = true;
		while(tkzer.hasMoreTokens() && rendered) 
		{
			String token = tkzer.nextToken();

			if ("\n".equals(token))
			{
				rendered = renderParagraph(allParagraphs, lastParagraphStart, lastParagraphText);

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

		if (rendered && lastParagraphStart < remainingTextStart + remainingText.length())
		{
			renderParagraph(allParagraphs, lastParagraphStart, lastParagraphText);
		}
		
		return measuredState;
	}

	/**
	 * 
	 */
	protected boolean renderParagraph(
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

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, getBreakIterator(), FONT_RENDER_CONTEXT);
		
		boolean rendered = true;
		while (lineMeasurer.getPosition() < paragraph.getEndIndex() && rendered)
		{
			rendered = renderNextLine(lineMeasurer, paragraph);
			if (rendered)
			{
				positionWithinParagraph = lineMeasurer.getPosition();
			}
		}
		
		measuredState.textOffset = lastParagraphStart + positionWithinParagraph;//(lastParagraphText == null ? 0 : positionWithinParagraph);
		
		return rendered;
	}
	
	protected BreakIterator getBreakIterator()
	{
		return BreakIterator.getLineInstance();
	}
	
	protected boolean renderNextLine(LineBreakMeasurer lineMeasurer, AttributedCharacterIterator paragraph)
	{
		int lineStartPosition = lineMeasurer.getPosition();

		TextLayout layout = lineMeasurer.nextLayout(formatWidth);

		measuredState.isLeftToRight = measuredState.isLeftToRight && layout.isLeftToRight();
		
		float newTextHeight = measuredState.textHeight + layout.getLeading() + lineSpacing * layout.getAscent();
		boolean fits = newTextHeight + layout.getDescent() <= maxHeight;
		if (fits)
		{
			measuredState.textHeight = newTextHeight;
			measuredState.lines++;

			measuredState.fontSizeSum += 
				maxFontSizeFinder.findMaxFontSize(
					new AttributedString(
						paragraph, 
						lineStartPosition, 
						lineStartPosition + layout.getCharacterCount()
						).getIterator(),
					textElement.getFontSize()
					);

			if (measuredState.lines == 1)
			{
				measuredState.firstLineLeading = measuredState.textHeight;
				measuredState.firstLineMaxFontSize = measuredState.fontSizeSum;
			}

			// here is the Y offset where we would draw the line
			//lastDrawPosY = drawPosY;
			//
			measuredState.textHeight += layout.getDescent();
		}
		return fits;
	}
	
}
