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
package net.sf.jasperreports.engine.export;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.MaxFontSizeFinder;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TextRenderer
{
	public static final FontRenderContext LINE_BREAK_FONT_RENDER_CONTEXT = new FontRenderContext(null, true, true);

	private Graphics2D grx = null;
	private int x = 0;
	private int y = 0;
	private int topPadding = 0;
	private int leftPadding = 0;
	private float formatWidth = 0;
	private float verticalOffset = 0;
	private float lineSpacingFactor = 0;
	private float leadingOffset = 0;
	private int maxHeight = 0;
	private float drawPosY = 0;
	private float drawPosX = 0;
	private boolean isMaxHeightReached = false;
	private byte horizontalAlignment = 0;
	private int fontSize = 0;
	
	/**
	 * 
	 */
	private MaxFontSizeFinder maxFontSizeFinder = null;

	
	/**
	 * 
	 */
	public void render(
		Graphics2D initGrx,
		int initX,
		int initY,
		int initWidth,
		int initHeight,
		int initTopPadding,
		int initLeftPadding,
		int initBottomPadding,
		int initRightPadding,
		float initTextHeight,
		byte initHorizontalAlignment,
		byte initVerticalAlignment,
		float initLineSpacingFactor,
		float initLeadingOffset,
		int initFontSize,
		boolean isStyledText,
		JRStyledText styledText,
		String allText
		)
	{
		/*   */
		initialize(
			initGrx, 
			initX, 
			initY, 
			initWidth, 
			initHeight, 
			initTopPadding,
			initLeftPadding,
			initBottomPadding,
			initRightPadding,
			initTextHeight, 
			initHorizontalAlignment, 
			initVerticalAlignment, 
			initLineSpacingFactor,
			initLeadingOffset,
			initFontSize,
			isStyledText
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
		Graphics2D initGrx,
		int initX,
		int initY,
		int initWidth,
		int initHeight,
		int initTopPadding,
		int initLeftPadding,
		int initBottomPadding,
		int initRightPadding,
		float initTextHeight,
		byte initHorizontalAlignment,
		byte initVerticalAlignment,
		float initLineSpacingFactor,
		float initLeadingOffset,
		int initFontSize,
		boolean isStyledText
		)
	{
		this.grx = initGrx;
		
		this.horizontalAlignment = initHorizontalAlignment;

		verticalOffset = 0f;
		switch (initVerticalAlignment)
		{
			case JRAlignment.VERTICAL_ALIGN_TOP :
			{
				verticalOffset = 0f;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_MIDDLE :
			{
				verticalOffset = (initHeight - initTopPadding - initBottomPadding - initTextHeight) / 2f;
				break;
			}
			case JRAlignment.VERTICAL_ALIGN_BOTTOM :
			{
				verticalOffset = initHeight - initTopPadding - initBottomPadding - initTextHeight;
				break;
			}
			default :
			{
				verticalOffset = 0f;
			}
		}

		this.lineSpacingFactor = initLineSpacingFactor;
		this.leadingOffset = initLeadingOffset;

		this.x = initX;
		this.y = initY;
		this.topPadding = initTopPadding;
		this.leftPadding = initLeftPadding;
		formatWidth = initWidth - initLeftPadding - initRightPadding;
		formatWidth = formatWidth < 0 ? 0 : formatWidth;
		maxHeight = initHeight - initTopPadding - initBottomPadding;
		maxHeight = maxHeight < 0 ? 0 : maxHeight;

		drawPosY = 0;
		drawPosX = 0;
	
		isMaxHeightReached = false;
		
		this.fontSize = initFontSize;
		maxFontSizeFinder = MaxFontSizeFinder.getInstance(isStyledText);
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

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, LINE_BREAK_FONT_RENDER_CONTEXT);//grx.getFontRenderContext()
	
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
			layout = new TextLayout(tmpText.getIterator(), LINE_BREAK_FONT_RENDER_CONTEXT);//grx.getFontRenderContext()
			//eugene fix - end

			float lineHeight = lineSpacingFactor * 
				maxFontSizeFinder.findMaxFontSize(
					new AttributedString(
						paragraph, 
						startIndex, 
						startIndex + layout.getCharacterCount()
						).getIterator(),
					fontSize
					);

			if (drawPosY + lineHeight <= maxHeight)
			{
				drawPosY += lineHeight;
				
				switch (horizontalAlignment)
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
						drawPosX = formatWidth - layout.getAdvance();
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
						drawPosX = 0;
					}
				}

				draw(layout);
			}
			else
			{
				isMaxHeightReached = true;
			}
		}
	}
	
	/**
	 * 
	 */
	public void draw(TextLayout layout)
	{
		layout.draw(
			grx,
			drawPosX + x + leftPadding,
			drawPosY + y + topPadding + verticalOffset + leadingOffset
			);
	}
	
}
