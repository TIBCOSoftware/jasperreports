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
import java.text.CharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.util.JRProperties;
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

	private JRCommonText textElement;
	private JRPropertiesHolder propertiesHolder;

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
	private boolean canOverflow;
	private Map globalAttributes;
	private TextMeasuredState measuredState;
	private TextMeasuredState prevMeasuredState;
	
	protected static class TextMeasuredState implements JRMeasuredText, Cloneable
	{
		protected int textOffset = 0;
		protected int lines = 0;
		protected int fontSizeSum = 0;
		protected int firstLineMaxFontSize = 0;
		protected float textHeight = 0;
		protected float firstLineLeading = 0;
		protected boolean isLeftToRight = true;
		protected String textSuffix = null;
		
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

		public String getTextSuffix()
		{
			return textSuffix;
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
	public TextMeasurer(JRCommonText textElement)
	{
		this.textElement = textElement;
		this.propertiesHolder = textElement instanceof JRPropertiesHolder ? (JRPropertiesHolder) textElement : null;
	}
	
	/**
	 * 
	 */
	protected void initialize(JRStyledText styledText, int availableStretchHeight, boolean canOverflow)
	{
		width = textElement.getWidth();
		height = textElement.getHeight();
		
		topPadding = textElement.getLineBox().getTopPadding().intValue();
		leftPadding = textElement.getLineBox().getLeftPadding().intValue();
		bottomPadding = textElement.getLineBox().getBottomPadding().intValue();
		rightPadding = textElement.getLineBox().getRightPadding().intValue();

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

		maxFontSizeFinder = MaxFontSizeFinder.getInstance(!JRCommonText.MARKUP_NONE.equals(textElement.getMarkup()));

		formatWidth = width - leftPadding - rightPadding;
		formatWidth = formatWidth < 0 ? 0 : formatWidth;
		maxHeight = height + availableStretchHeight - topPadding - bottomPadding;
		maxHeight = maxHeight < 0 ? 0 : maxHeight;
		this.canOverflow = canOverflow;
		this.globalAttributes = styledText.getGlobalAttributes();
		measuredState = new TextMeasuredState();
		prevMeasuredState = null;
	}

	/**
	 * 
	 */
	public JRMeasuredText measure(
		JRStyledText styledText,
		int remainingTextStart,
		int availableStretchHeight,
		boolean canOverflow
		)
	{
		/*   */
		initialize(styledText, availableStretchHeight, canOverflow);

		AttributedCharacterIterator allParagraphs = styledText.getAttributedString().getIterator();

		int tokenPosition = remainingTextStart;
		int lastParagraphStart = remainingTextStart;
		String lastParagraphText = null;

		String remainingText = styledText.getText().substring(remainingTextStart);
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

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, FONT_RENDER_CONTEXT);
		
		measuredState.textOffset = lastParagraphStart;
		
		boolean rendered = true;
		boolean renderedLine = false;
		while (lineMeasurer.getPosition() < paragraph.getEndIndex() && rendered)
		{
			rendered = renderNextLine(lineMeasurer, paragraph);
			renderedLine = renderedLine || rendered;
		}
		
		//if we rendered at least one line, and the last line didn't fit 
		//and the text does not overflow
		if (!rendered && prevMeasuredState != null && !canOverflow)
		{
			//handle last rendered row
			processLastTruncatedRow(allParagraphs, lastParagraphText, lastParagraphStart, renderedLine);
		}
		
		return rendered;
	}
	
	protected void processLastTruncatedRow(AttributedCharacterIterator allParagraphs,
			String paragraphText, int paragraphOffset,
			boolean lineTruncated)
	{
		if (lineTruncated && isToTruncateAtChar())
		{
			truncateLastLineAtChar(allParagraphs, paragraphText, paragraphOffset);
		}
		
		appendTruncateSuffix(allParagraphs);
	}

	protected void truncateLastLineAtChar(AttributedCharacterIterator allParagraphs, String paragraphText, int paragraphOffset)
	{
		//truncate the original line at char
		measuredState = prevMeasuredState.cloneState();
		AttributedCharacterIterator lineParagraph = new AttributedString(
				allParagraphs, 
				measuredState.textOffset,
				paragraphOffset + paragraphText.length()).getIterator();
		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(
				lineParagraph, 
				BreakIterator.getCharacterInstance(), 
				FONT_RENDER_CONTEXT);
		//render again the last line
		//if the line does not fit now, it will remain empty
		renderNextLine(lineMeasurer, lineParagraph);
	}

	protected void appendTruncateSuffix(AttributedCharacterIterator allParagraphs)
	{
		String truncateSuffx = getTruncateSuffix();
		if (truncateSuffx == null)
		{
			return;
		}
		
		int lineStart = prevMeasuredState.textOffset;

		//advance from the line start until the next line start or the first newline
		StringBuffer lineText = new StringBuffer();
		allParagraphs.setIndex(lineStart);
		while (allParagraphs.getIndex() < measuredState.textOffset 
				&& allParagraphs.current() != '\n')
		{
			lineText.append(allParagraphs.current());
			allParagraphs.next();
		}
		int linePosition = allParagraphs.getIndex() - lineStart;
		
		//iterate to the beginning of the line
		boolean done = false;
		do
		{
			measuredState = prevMeasuredState.cloneState();

			String text = lineText.substring(0, linePosition) + truncateSuffx;
			AttributedString attributedText = new AttributedString(text);
			
			//set original attributes for the text part
			AttributedCharacterIterator lineAttributes = new AttributedString(
					allParagraphs, 
					measuredState.textOffset,
					measuredState.textOffset + linePosition).getIterator();
			setAttributes(attributedText, lineAttributes, 0);
			
			//set global attributes for the suffix part
			setAttributes(attributedText, globalAttributes, 
					text.length() - truncateSuffx.length(), text.length());
			
			AttributedCharacterIterator lineParagraph = attributedText.getIterator();
			
			BreakIterator breakIterator = 
				isToTruncateAtChar() 
				? BreakIterator.getCharacterInstance() 
				: BreakIterator.getLineInstance();
			LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(
					lineParagraph,
					breakIterator,
					FONT_RENDER_CONTEXT);

			if (renderNextLine(lineMeasurer, lineParagraph))
			{
				int lastPos = lineMeasurer.getPosition();
				//test if the entire suffix fit
				if (lastPos == linePosition + truncateSuffx.length())
				{
					//subtract the suffix from the offset
					measuredState.textOffset -= truncateSuffx.length();
					measuredState.textSuffix = truncateSuffx;
					done = true;
				}
				else
				{
					linePosition = breakIterator.preceding(linePosition);
					if (linePosition == BreakIterator.DONE)
					{
						//if the text suffix did not fit the line, only the part of it that fits will show

						//truncate the suffix
						String actualSuffix = truncateSuffx.substring(0, 
								measuredState.textOffset - prevMeasuredState.textOffset);
						//if the last text char is not a new line
						if (prevMeasuredState.textOffset > 0
								&& allParagraphs.setIndex(prevMeasuredState.textOffset - 1) != '\n')
						{
							//force a new line so that the suffix is displayed on the last line
							actualSuffix = '\n' + actualSuffix;
						}
						measuredState.textSuffix = actualSuffix;
						
						//restore the next to last line offset
						measuredState.textOffset = prevMeasuredState.textOffset;

						done = true;
					}
				}
			}
			else
			{
				//if the line did not fit, leave it empty
				done = true;
			}
		}
		while (!done);
	}

	protected boolean isToTruncateAtChar()
	{
		return JRProperties.getBooleanProperty(propertiesHolder, 
				JRTextElement.PROPERTY_TRUNCATE_AT_CHAR, false);
	}

	protected String getTruncateSuffix()
	{
		String truncateSuffx = JRProperties.getProperty(propertiesHolder,
				JRTextElement.PROPERTY_TRUNCATE_SUFFIX);
		if (truncateSuffx != null)
		{
			truncateSuffx = truncateSuffx.trim();
		}
		if (truncateSuffx.length() == 0)
		{
			truncateSuffx = null;
		}
		return truncateSuffx;
	}
	
	protected boolean renderNextLine(LineBreakMeasurer lineMeasurer, AttributedCharacterIterator paragraph)
	{
		int lineStartPosition = lineMeasurer.getPosition();

		TextLayout layout = lineMeasurer.nextLayout(formatWidth);

		float newTextHeight = measuredState.textHeight + layout.getLeading() + lineSpacing * layout.getAscent();
		boolean fits = newTextHeight + layout.getDescent() <= maxHeight;
		if (fits)
		{
			prevMeasuredState = measuredState.cloneState();
			
			measuredState.isLeftToRight = measuredState.isLeftToRight && layout.isLeftToRight();
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
			
			measuredState.textOffset += lineMeasurer.getPosition() - lineStartPosition;
		}
		
		return fits;
	}
	
	protected JRPropertiesHolder getTextPropertiesHolder()
	{
		return propertiesHolder;
	}
	
	protected void setAttributes(
			AttributedString string,
			AttributedCharacterIterator attributes, 
			int stringOffset)
	{
		for (char c = attributes.first(); c != CharacterIterator.DONE; c = attributes.next())
		{
			for (Iterator it = attributes.getAttributes().entrySet().iterator(); it.hasNext();)
			{
				Map.Entry attributeEntry = (Map.Entry) it.next();
				AttributedCharacterIterator.Attribute attribute = (Attribute) attributeEntry.getKey();
				if (attributes.getRunStart(attribute) == attributes.getIndex())
				{
					Object attributeValue = attributeEntry.getValue();
					string.addAttribute(attribute, attributeValue, 
							attributes.getIndex() + stringOffset,
							attributes.getRunLimit(attribute) + stringOffset);
				}
			};
		}
	}
	
	protected void setAttributes(
			AttributedString string,
			Map attributes, 
			int startIndex, int endIndex)
	{
		for (Iterator it = attributes.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			AttributedCharacterIterator.Attribute attribute = (Attribute) entry.getKey();
			Object attributeValue = entry.getValue();
			string.addAttribute(attribute, attributeValue, startIndex, endIndex);
		}
	}
}
