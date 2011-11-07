/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.fill;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.text.Bidi;
import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.export.AbstractTextRenderer;
import net.sf.jasperreports.engine.export.AwtTextRenderer;
import net.sf.jasperreports.engine.util.DelegatePropertiesHolder;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledText.Run;
import net.sf.jasperreports.engine.util.ParagraphUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Default text measurer implementation.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TextMeasurer implements JRTextMeasurer
{
	private static final Log log = LogFactory.getLog(TextMeasurer.class);
	
	//FIXME remove this after measureSimpleText() is proven to be stable
	public static final String PROPERTY_MEASURE_SIMPLE_TEXTS = JRProperties.PROPERTY_PREFIX + "measure.simple.text";

	private JRCommonText textElement;
	private JRPropertiesHolder propertiesHolder;
	
	private boolean measureSimpleTexts;
	private final Map<FontKey, FontInfo> fontInfos = new HashMap<FontKey, FontInfo>();

	/**
	 * 
	 */
	//private MaxFontSizeFinder maxFontSizeFinder;
	
	private int width;
	private int height;
	private int topPadding;
	private int leftPadding;
	private int bottomPadding;
	private int rightPadding;
	private JRParagraph jrParagraph;

	//private float formatWidth;
	private int maxHeight;
	private boolean canOverflow;
	private Map<Attribute,Object> globalAttributes;
	private TextMeasuredState measuredState;
	private TextMeasuredState prevMeasuredState;
	
	protected static class TextMeasuredState implements JRMeasuredText, Cloneable
	{
		private final boolean saveLineBreakOffsets;
		
		protected int textOffset;
		protected int lines;
		protected int paragraphStartLine;
		protected float textHeight;
		protected boolean isLeftToRight = true;
		protected String textSuffix;
		
		protected int lastOffset;
		protected ArrayList<Integer> lineBreakOffsets;
		
		public TextMeasuredState(boolean saveLineBreakOffsets)
		{
			this.saveLineBreakOffsets = saveLineBreakOffsets;
		}
		
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
		
		public String getTextSuffix()
		{
			return textSuffix;
		}
		
		public TextMeasuredState cloneState()
		{
			try
			{
				TextMeasuredState clone = (TextMeasuredState) super.clone();
				
				//clone the list of offsets
				//might be a performance problem on very large texts
				if (lineBreakOffsets != null)
				{
					clone.lineBreakOffsets = (ArrayList<Integer>) lineBreakOffsets.clone();
				}
				
				return clone;
			}
			catch (CloneNotSupportedException e)
			{
				//never
				throw new JRRuntimeException(e);
			}
		}

		protected void addLineBreak()
		{
			if (saveLineBreakOffsets)
			{
				if (lineBreakOffsets == null)
				{
					lineBreakOffsets = new ArrayList<Integer>();
				}

				int breakOffset = textOffset - lastOffset;
				lineBreakOffsets.add(Integer.valueOf(breakOffset));
				lastOffset = textOffset;
			}
		}
		
		public short[] getLineBreakOffsets()
		{
			if (!saveLineBreakOffsets)
			{
				//if no line breaks are to be saved, return null
				return null;
			}
			
			//if the last line break occurred at the truncation position
			//exclude the last break offset
			int exclude = lastOffset == textOffset ? 1 : 0;
			if (lineBreakOffsets == null 
					|| lineBreakOffsets.size() <= exclude)
			{
				//use the zero length array singleton
				return JRPrintText.ZERO_LINE_BREAK_OFFSETS;
			}
			
			short[] offsets = new short[lineBreakOffsets.size() - exclude];
			boolean overflow = false;
			for (int i = 0; i < offsets.length; i++)
			{
				int offset = lineBreakOffsets.get(i).intValue();
				if (offset > Short.MAX_VALUE)
				{
					if (log.isWarnEnabled())
					{
						log.warn("Line break offset value " + offset 
								+ " is bigger than the maximum supported value of"
								+ Short.MAX_VALUE 
								+ ". Line break offsets will not be saved for this text.");
					}
					
					overflow = true;
					break;
				}
				offsets[i] = (short) offset;
			}
			
			if (overflow)
			{
				//if a line break offset overflow occurred, do not return any 
				//line break offsets
				return null;
			}
			
			return offsets;
		}
	}
	
	/**
	 * 
	 */
	public TextMeasurer(JRCommonText textElement)
	{
		this.textElement = textElement;
		this.propertiesHolder = textElement instanceof JRPropertiesHolder ? (JRPropertiesHolder) textElement : null;//FIXMENOW all elements are now properties holders, so interfaces might be rearranged
		if (textElement.getDefaultStyleProvider() instanceof JRPropertiesHolder)
		{
			this.propertiesHolder = 
				new DelegatePropertiesHolder(
					propertiesHolder, 
					(JRPropertiesHolder)textElement.getDefaultStyleProvider()
					);
		}
		
		measureSimpleTexts = JRProperties.getBooleanProperty(this.propertiesHolder, PROPERTY_MEASURE_SIMPLE_TEXTS, true);
	}
	
	/**
	 * 
	 */
	protected void initialize(
		JRStyledText styledText,
		int remainingTextStart,
		int availableStretchHeight, 
		boolean canOverflow
		)
	{
		width = textElement.getWidth();
		height = textElement.getHeight();
		
		topPadding = textElement.getLineBox().getTopPadding().intValue();
		leftPadding = textElement.getLineBox().getLeftPadding().intValue();
		bottomPadding = textElement.getLineBox().getBottomPadding().intValue();
		rightPadding = textElement.getLineBox().getRightPadding().intValue();
		
		jrParagraph = textElement.getParagraph();

		switch (textElement.getRotationValue())
		{
			case LEFT :
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
			case RIGHT :
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
			case UPSIDE_DOWN :
			{
				int tmpPadding = topPadding;
				topPadding = bottomPadding;
				bottomPadding = tmpPadding;
				tmpPadding = leftPadding;
				leftPadding = rightPadding;
				rightPadding = tmpPadding;
				break;
			}
			case NONE :
			default :
			{
			}
		}
		
		//maxFontSizeFinder = MaxFontSizeFinder.getInstance(!JRCommonText.MARKUP_NONE.equals(textElement.getMarkup()));

//		formatWidth = width - leftPadding - rightPadding;
//		formatWidth = formatWidth < 0 ? 0 : formatWidth;
		maxHeight = height + availableStretchHeight - topPadding - bottomPadding;
		maxHeight = maxHeight < 0 ? 0 : maxHeight;
		this.canOverflow = canOverflow;
		this.globalAttributes = styledText.getGlobalAttributes();
		
		boolean saveLineBreakOffsets = JRProperties.getBooleanProperty(propertiesHolder, 
				JRTextElement.PROPERTY_SAVE_LINE_BREAKS, false);
		
		measuredState = new TextMeasuredState(saveLineBreakOffsets);
		measuredState.lastOffset = remainingTextStart;
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
		initialize(styledText, remainingTextStart, availableStretchHeight, canOverflow);

		if (measureSimpleTexts && measureSimpleText(styledText, remainingTextStart))
		{
			// simple text measured
			return measuredState;
		}

		AttributedCharacterIterator allParagraphs = 
			styledText.getAwtAttributedString(
				JRProperties.getBooleanProperty(propertiesHolder, JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT, false)
				).getIterator();

		int tokenPosition = remainingTextStart;
		int lastParagraphStart = remainingTextStart;
		String lastParagraphText = null;

		String remainingText = styledText.getText().substring(remainingTextStart);
		StringTokenizer tkzer = new StringTokenizer(remainingText, "\n", true);

		boolean rendered = true;
		// text is split into paragraphs, using the newline character as delimiter
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
	
	protected static class FontKey
	{
		String family;
		int size;
		int style;
		Number weight;
		public FontKey(String family, int size, int style)
		{
			super();
			this.family = family;
			this.size = size;
			this.style = style;
		}
		
		@Override
		public int hashCode()
		{
			int hash = 43;
			hash = hash*29 + family.hashCode();
			hash = hash*29 + size;
			hash = hash*29 + style;
			return hash;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			FontKey info = (FontKey) obj;
			return family.equals(info.family) && size == info.size && style == info.style;
		}
		
		public String toString()
		{
			return "{family: " + family
					+ ", size: " + size
					+ ", style: " + style
					+ "}";
		}
	}
	
	protected static class FontInfo
	{
		private static final int MIN_COUNT = 10;
		private static final double FONT_SIZE_MIN_FACTOR = 0.1;
		private static final double WIDTH_CHECK_FACTOR = 1.2;
		
		final Font font;
		int measurementsCount;
		double characterWidthSum;
		
		public FontInfo(Font font)
		{
			this.font = font;
		}
	}

	protected boolean measureSimpleText(JRStyledText styledText, int remainingTextStart)
	{
		if (remainingTextStart != 0)
		{
			// not measuring text fragments for now
			return false;
		}
		
		List<Run> runs = styledText.getRuns();
		if (runs.size() != 1)
		{
			// multiple styles
			return false;
		}
		
		String text = styledText.getText();
		if (text.length() == 0 //this should not happen but still checking
				|| text.indexOf('\n') >= 0 || text.indexOf('\b') >= 0)
		{
			// we don't handle newlines and tabs here
			return false;
		}
		
		if (hasParagraphIndents())
		{
			// not handling this case for now
			return false;
		}
		
		Run run = styledText.getRuns().get(0);
		
		if (run.attributes.get(TextAttribute.SUPERSCRIPT) != null)
		{
			// not handling this case, see JRStyledText.getAwtAttributedString
			return false;
		}
		
		String family = (String) run.attributes.get(TextAttribute.FAMILY);
		Number size = (Number) run.attributes.get(TextAttribute.SIZE);
		
		if (family == null || size == null)
		{
			// this should not happen, but still checking
			return false;
		}
		
		int availableWidth = width - leftPadding - rightPadding;
		
		// a test to exclude cases of very large texts
		if (text.length() * size.intValue() * FontInfo.FONT_SIZE_MIN_FACTOR > availableWidth)
		{
			return false;
		}
		
		int style = 0;
		Number posture = (Number) run.attributes.get(TextAttribute.POSTURE);
		if (posture != null && !TextAttribute.POSTURE_REGULAR.equals(posture))
		{
			if (TextAttribute.POSTURE_OBLIQUE.equals(posture))
			{
				style |= Font.ITALIC;
			}
			else
			{
				// non standard posture
				return false;
			}
		}
		
		Number weight = (Number) run.attributes.get(TextAttribute.WEIGHT);
		if (weight != null && !TextAttribute.WEIGHT_REGULAR.equals(weight))
		{
			if (TextAttribute.WEIGHT_BOLD.equals(weight))
			{
				style |= Font.BOLD;
			}
			else
			{
				// non standard weight
				return false;
			}
		}
		
		FontKey fontKey = new FontKey(family, size.intValue(), style);
		FontInfo fontInfo = fontInfos.get(fontKey);
		if (fontInfo == null)
		{
			// check bundled fonts
			Font font = JRFontUtil.getAwtFontFromBundles(family, style, size.intValue(), styledText.getLocale(), false);
			if (font == null)
			{
				// checking AWT font
				JRFontUtil.checkAwtFont(family, false);
				// creating AWT font
				font = Font.getFont(run.attributes);
			}
			
			fontInfo = new FontInfo(font);
			fontInfos.put(fontKey, fontInfo);
		}
		
		// FIXME implement more sophisticated heuristics; keep the measurements globally?
		if (fontInfo.measurementsCount > FontInfo.MIN_COUNT)
		{
			// checking the current text against the avg character width
			double avgCharWidth = fontInfo.characterWidthSum / fontInfo.measurementsCount;
			if (avgCharWidth * text.length() > availableWidth * FontInfo.WIDTH_CHECK_FACTOR)
			{
				// not measuring based on the character width statistics
				return false;
			}
		}
		
		Rectangle2D bounds = fontInfo.font.getStringBounds(text, getFontRenderContext());
		
		// adding the measurement to the font info statistics
		++fontInfo.measurementsCount;
		fontInfo.characterWidthSum += bounds.getWidth() / text.length();
		
		boolean fitsWidth = bounds.getWidth() <= availableWidth;
		boolean fitsHeight = bounds.getHeight() <= maxHeight;
		if (log.isTraceEnabled())
		{
			log.trace("simple text of length " + text.length() 
					+ " measured to width " + bounds.getWidth()
					+ " with font " + fontInfo
					+ ", fits width" + fitsWidth
					+ ", fits height" + fitsHeight);
		}
		
		if (!fitsWidth)
		{
			// the text does not fit on one line
			return false;
		}
		
		// the whole text fits in one line
		measuredState.isLeftToRight = isLeftToRight(text);
		if (fitsHeight)
		{
			measuredState.textOffset = text.length();
			measuredState.textHeight = (float) bounds.getHeight();
		}
		else
		{
			measuredState.textOffset = 0;
			measuredState.textHeight = 0;
		}
		
		return true;
	}

	protected boolean isLeftToRight(String text)
	{
		boolean leftToRight = true;
		if (Bidi.requiresBidi(text.toCharArray(), 0, text.length()))
		{
			// determining the text direction
			Bidi bidi = new Bidi(text, 0);
			leftToRight = bidi.isLeftToRight();
		}
		return leftToRight;
	}
	
	protected boolean hasParagraphIndents()
	{
		return (jrParagraph.getFirstLineIndent() != null && jrParagraph.getFirstLineIndent().intValue() > 0)
				|| (jrParagraph.getLeftIndent() != null && jrParagraph.getLeftIndent().intValue() > 0)
				|| (jrParagraph.getRightIndent() != null && jrParagraph.getRightIndent().intValue() > 0);
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

		List<Integer> tabIndexes = JRStringUtil.getTabIndexes(lastParagraphText);
		
		int[] currentTabHolder = new int[]{0};
		TabStop[] nextTabStopHolder = new TabStop[]{null};
		boolean[] requireNextWordHolder = new boolean[]{false};

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, getFontRenderContext());
		
		measuredState.paragraphStartLine = measuredState.lines;
		measuredState.textOffset = lastParagraphStart;
		
		boolean rendered = true;
		boolean renderedLine = false;

		// the paragraph is measured one line at a time
		while (lineMeasurer.getPosition() < paragraph.getEndIndex() && rendered)
		{
			rendered = renderNextLine(lineMeasurer, paragraph, tabIndexes, currentTabHolder, nextTabStopHolder, requireNextWordHolder);
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
	
	protected void processLastTruncatedRow(
		AttributedCharacterIterator allParagraphs,
		String paragraphText, 
		int paragraphOffset,
		boolean lineTruncated
		)
	{
		if (lineTruncated && isToTruncateAtChar())
		{
			truncateLastLineAtChar(allParagraphs, paragraphText, paragraphOffset);
		}
		
		appendTruncateSuffix(allParagraphs);
	}

	protected void truncateLastLineAtChar(
		AttributedCharacterIterator allParagraphs, 
		String paragraphText, 
		int paragraphOffset
		)
	{
		//truncate the original line at char
		measuredState = prevMeasuredState.cloneState();
		AttributedCharacterIterator lineParagraph = 
			new AttributedString(
				allParagraphs, 
				measuredState.textOffset,
				paragraphOffset + paragraphText.length()
				).getIterator();
		LineBreakMeasurer lineMeasurer = 
			new LineBreakMeasurer(
				lineParagraph, 
				BreakIterator.getCharacterInstance(), 
				getFontRenderContext()
				);
		//render again the last line
		//if the line does not fit now, it will remain empty
		renderNextLine(lineMeasurer, lineParagraph, null, new int[]{0}, new TabStop[]{null}, new boolean[]{false});
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
			AttributedCharacterIterator lineAttributes = 
				new AttributedString(
					allParagraphs, 
					measuredState.textOffset,
					measuredState.textOffset + linePosition
					).getIterator();
			setAttributes(attributedText, lineAttributes, 0);
			
			//set global attributes for the suffix part
			setAttributes(
				attributedText, 
				globalAttributes, 
				text.length() - truncateSuffx.length(), 
				text.length()
				);
			
			AttributedCharacterIterator lineParagraph = attributedText.getIterator();
			
			BreakIterator breakIterator = 
				isToTruncateAtChar() 
				? BreakIterator.getCharacterInstance() 
				: BreakIterator.getLineInstance();
			LineBreakMeasurer lineMeasurer = 
				new LineBreakMeasurer(
					lineParagraph,
					breakIterator,
					getFontRenderContext()
					);

			if (renderNextLine(lineMeasurer, lineParagraph, null, new int[]{0}, new TabStop[]{null}, new boolean[]{false}))
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
			if (truncateSuffx.length() == 0)
			{
				truncateSuffx = null;
			}
		}
		return truncateSuffx;
	}
	
	protected boolean renderNextLine(LineBreakMeasurer lineMeasurer, AttributedCharacterIterator paragraph, List<Integer> tabIndexes, int[] currentTabHolder, TabStop[] nextTabStopHolder, boolean[] requireNextWordHolder)
	{
		boolean lineComplete = false;

		int lineStartPosition = lineMeasurer.getPosition();
		
		float maxAscent = 0;
		float maxDescent = 0;
		float maxLeading = 0;
		int characterCount = 0;
		boolean isLeftToRight = true;
		
		// each line is split into segments, using the tab character as delimiter
		List<TabSegment> segments = new ArrayList<TabSegment>(1);

		TabSegment oldSegment = null;
		TabSegment crtSegment = null;

		// splitting the current line into tab segments
		while (!lineComplete)
		{
			// the current segment limit is either the next tab character or the paragraph end 
			int tabIndexOrEndIndex = (tabIndexes == null || currentTabHolder[0] >= tabIndexes.size() ? paragraph.getEndIndex() : tabIndexes.get(currentTabHolder[0]) + 1);
			
			float startX = (lineMeasurer.getPosition() == 0 ? textElement.getParagraph().getFirstLineIndent() : 0) + leftPadding;
			float endX = width - textElement.getParagraph().getRightIndent() - rightPadding;
			endX = endX < startX ? startX : endX;
			//formatWidth = endX - startX;
			//formatWidth = endX;

			int startIndex = lineMeasurer.getPosition();

			float rightX = 0;

			if (segments.size() == 0)
			{
				rightX = startX;
				//nextTabStop = nextTabStop;
			}
			else
			{
				rightX = oldSegment.rightX;
				nextTabStopHolder[0] = ParagraphUtil.getNextTabStop(jrParagraph, endX, rightX);
			}

			//float availableWidth = formatWidth - ParagraphUtil.getSegmentOffset(nextTabStopHolder[0], rightX); // nextTabStop can be null here; and that's OK
			float availableWidth = endX - textElement.getParagraph().getLeftIndent() - ParagraphUtil.getSegmentOffset(nextTabStopHolder[0], rightX); // nextTabStop can be null here; and that's OK
			
			// creating a text layout object for each tab segment 
			TextLayout layout = 
				lineMeasurer.nextLayout(
					availableWidth,
					tabIndexOrEndIndex,
					requireNextWordHolder[0]
					);
			
			if (layout != null)
			{
				maxAscent = Math.max(maxAscent, layout.getAscent());
				maxDescent = Math.max(maxDescent, layout.getDescent());
				maxLeading = Math.max(maxLeading, layout.getLeading());
				characterCount += layout.getCharacterCount();
				isLeftToRight = isLeftToRight && layout.isLeftToRight();

				//creating the current segment
				crtSegment = new TabSegment();
				crtSegment.layout = layout;

				float leftX = ParagraphUtil.getLeftX(nextTabStopHolder[0], layout.getAdvance()); // nextTabStop can be null here; and that's OK
				if (rightX > leftX)
				{
					crtSegment.leftX = rightX;
					crtSegment.rightX = rightX + layout.getAdvance();
				}
				else
				{
					crtSegment.leftX = leftX;
					// we need this special tab stop based utility call because adding the advance to leftX causes rounding issues
					crtSegment.rightX = ParagraphUtil.getRightX(nextTabStopHolder[0], layout.getAdvance()); // nextTabStop can be null here; and that's OK
				}

				segments.add(crtSegment);
			}
			
			requireNextWordHolder[0] = true;

			if (lineMeasurer.getPosition() == tabIndexOrEndIndex)
			{
				// the segment limit was a tab; going to the next tab
				currentTabHolder[0] = currentTabHolder[0] + 1;
			}
			
			if (lineMeasurer.getPosition() == paragraph.getEndIndex())
			{
				// the segment limit was the paragraph end; line completed and next line should start at normal zero x offset
				lineComplete = true;
				nextTabStopHolder[0] = null;
			}
			else
			{
				// there is paragraph text remaining 
				if (lineMeasurer.getPosition() == tabIndexOrEndIndex)
				{
					// the segment limit was a tab
					if (crtSegment.rightX >= ParagraphUtil.getLastTabStop(jrParagraph, endX).getPosition())
					{
						// current segment stretches out beyond the last tab stop; line complete
						lineComplete = true;
						// next line should should start at first tab stop indent
						nextTabStopHolder[0] = ParagraphUtil.getFirstTabStop(jrParagraph, endX);
					}
//					else
//					{
//						//nothing; this leaves lineComplete=false
//					}
				}
				else
				{
					// the segment did not fit entirely
					lineComplete = true;
					if (layout == null)
					{
						// nothing fitted; next line should start at first tab stop indent
						if (nextTabStopHolder[0].getPosition() == ParagraphUtil.getFirstTabStop(jrParagraph, endX).getPosition())//FIXMETAB check based on segments.size()
						{
							// at second attempt we give up to avoid infinite loop
							nextTabStopHolder[0] = null;
							requireNextWordHolder[0] = false;
							
							//provide dummy maxFontSize because it is used for the line height of this empty line when attempting drawing below
				 			AttributedString tmpText = 
								new AttributedString(
									paragraph, 
									startIndex, 
									startIndex + 1
									);
				 			LineBreakMeasurer lbm = new LineBreakMeasurer(tmpText.getIterator(), getFontRenderContext());
				 			TextLayout tlyt = lbm.nextLayout(100);//FIXME what is this? why 100?
							maxAscent = tlyt.getAscent();
							maxDescent = tlyt.getDescent();
							maxLeading = tlyt.getLeading();
						}
						else
						{
							nextTabStopHolder[0] = ParagraphUtil.getFirstTabStop(jrParagraph, endX);
						}
					}
					else
					{
						// something fitted
						nextTabStopHolder[0] = null;
						requireNextWordHolder[0] = false;
					}
				}
			}

			oldSegment = crtSegment;
		}
		
		float lineHeight = AbstractTextRenderer.getLineHeight(measuredState.lines == 0, jrParagraph, maxLeading, maxAscent);
		
		if (measuredState.lines == 0) //FIXMEPARA
		//if (measuredState.paragraphStartLine == measuredState.lines)
		{
			lineHeight += jrParagraph.getSpacingBefore().intValue();
		}
		
		float newTextHeight = measuredState.textHeight + lineHeight;
		boolean fits = newTextHeight + maxDescent <= maxHeight;
		if (fits)
		{
			prevMeasuredState = measuredState.cloneState();
			
			measuredState.isLeftToRight = isLeftToRight;//run direction is per layout; but this is the best we can do for now
			measuredState.textHeight = newTextHeight;
			measuredState.lines++;

//			measuredState.fontSizeSum += 
//				maxFontSizeFinder.findMaxFontSize(
//					new AttributedString(
//						paragraph, 
//						lineStartPosition, 
//						lineStartPosition + characterCount
//						).getIterator(),
//					textElement.getFontSize()
//					);

//			if (measuredState.lines == 1)
//			{
//				measuredState.firstLineLeading = measuredState.textHeight;
//				measuredState.firstLineMaxFontSize = measuredState.fontSizeSum;
//			}

			// here is the Y offset where we would draw the line
			//lastDrawPosY = drawPosY;
			//
			measuredState.textHeight += maxDescent;
			
			measuredState.textOffset += lineMeasurer.getPosition() - lineStartPosition;
			
			if (lineMeasurer.getPosition() < paragraph.getEndIndex())
			{
				//if not the last line in a paragraph, save the line break position
				measuredState.addLineBreak();
			}
//			else //FIXMEPARA
//			{
//				measuredState.textHeight += jrParagraph.getSpacingAfter().intValue();
//			}
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
		int stringOffset
		)
	{
		for (char c = attributes.first(); c != CharacterIterator.DONE; c = attributes.next())
		{
			for (Iterator<Map.Entry<Attribute,Object>> it = attributes.getAttributes().entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<Attribute,Object> attributeEntry = it.next();
				AttributedCharacterIterator.Attribute attribute = attributeEntry.getKey();
				if (attributes.getRunStart(attribute) == attributes.getIndex())
				{
					Object attributeValue = attributeEntry.getValue();
					string.addAttribute(
						attribute, 
						attributeValue, 
						attributes.getIndex() + stringOffset,
						attributes.getRunLimit(attribute) + stringOffset
						);
				}
			}
		}
	}
	
	protected void setAttributes(
		AttributedString string,
		Map<Attribute,Object> attributes, 
		int startIndex, 
		int endIndex
		)
	{
		for (Iterator<Map.Entry<Attribute,Object>> it = attributes.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<Attribute,Object> entry = it.next();
			Attribute attribute = entry.getKey();
			Object attributeValue = entry.getValue();
			string.addAttribute(attribute, attributeValue, startIndex, endIndex);
		}
	}

	

	/**
	 * 
	 */
	public FontRenderContext getFontRenderContext()
	{
		return AwtTextRenderer.LINE_BREAK_FONT_RENDER_CONTEXT;
	}

	
}

class TabSegment
{
	public TextLayout layout;
	public float leftX;
	public float rightX;
}
