/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import java.awt.font.FontRenderContext;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.export.AbstractTextRenderer;
import net.sf.jasperreports.engine.export.AwtTextRenderer;
import net.sf.jasperreports.engine.util.DelegatePropertiesHolder;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
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
	public static final String PROPERTY_MEASURE_SIMPLE_TEXTS = JRPropertiesUtil.PROPERTY_PREFIX + "measure.simple.text";

	protected JasperReportsContext jasperReportsContext;
	protected JRCommonText textElement;
	private JRPropertiesHolder propertiesHolder;
	private DynamicPropertiesHolder dynamicPropertiesHolder;
	
	private SimpleTextLineWrapper simpleLineWrapper;
	private ComplexTextLineWrapper complextLineWrapper;
	
	protected int width;
	private int height;
	private int topPadding;
	protected int leftPadding;
	private int bottomPadding;
	protected int rightPadding;
	private JRParagraph jrParagraph;

	private float formatWidth;
	protected int maxHeight;
	private boolean canOverflow;
	
	private boolean hasDynamicIgnoreMissingFontProp;
	private boolean defaultIgnoreMissingFont;
	private boolean ignoreMissingFont;
	
	private boolean hasDynamicSaveLineBreakOffsetsProp;
	private boolean defaultSaveLineBreakOffsets;
	
	protected TextMeasuredState measuredState;
	protected TextMeasuredState prevMeasuredState;
	
	protected static class TextMeasuredState implements JRMeasuredText, Cloneable
	{
		private final boolean saveLineBreakOffsets;
		
		protected int textOffset;
		protected int lines;
		protected int fontSizeSum;
		protected int firstLineMaxFontSize;
		protected int paragraphStartLine;
		protected float textWidth;
		protected float textHeight;
		protected float firstLineLeading;
		protected boolean isLeftToRight = true;
		protected String textSuffix;
		protected boolean isMeasured = true;
		
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
		
		public float getTextWidth()
		{
			return textWidth;
		}
		
		public float getTextHeight()
		{
			return textHeight;
		}
		
		public float getLineSpacingFactor()
		{
			if (isMeasured && lines > 0 && fontSizeSum > 0)
			{
				return textHeight / fontSizeSum;
			}
			return 0;
		}
		
		public float getLeadingOffset()
		{
			if (isMeasured && lines > 0 && fontSizeSum > 0)
			{
				return firstLineLeading - firstLineMaxFontSize * getLineSpacingFactor();
			}
			return 0;
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
	public TextMeasurer(JasperReportsContext jasperReportsContext, JRCommonText textElement)
	{
		this.jasperReportsContext = jasperReportsContext;
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
		
		if (textElement instanceof DynamicPropertiesHolder)
		{
			this.dynamicPropertiesHolder = (DynamicPropertiesHolder) textElement;
			
			// we can check this from the beginning
			this.hasDynamicIgnoreMissingFontProp = this.dynamicPropertiesHolder.hasDynamicProperty(
					JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT);
			this.hasDynamicSaveLineBreakOffsetsProp = this.dynamicPropertiesHolder.hasDynamicProperty(
					JRTextElement.PROPERTY_SAVE_LINE_BREAKS);
		}

		// read static property values
		JRPropertiesUtil propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
		defaultIgnoreMissingFont = propertiesUtil.getBooleanProperty(propertiesHolder, 
				JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT, false);
		defaultSaveLineBreakOffsets = propertiesUtil.getBooleanProperty(propertiesHolder, 
				JRTextElement.PROPERTY_SAVE_LINE_BREAKS, false);
		
		Context measureContext = new Context();
		simpleLineWrapper = new SimpleTextLineWrapper();
		simpleLineWrapper.init(measureContext);
		
		complextLineWrapper = new ComplexTextLineWrapper();
		complextLineWrapper.init(measureContext);
	}

	/**
	 * @deprecated Replaced by {@link #TextMeasurer(JasperReportsContext, JRCommonText)}.
	 */
	public TextMeasurer(JRCommonText textElement)
	{
		this(DefaultJasperReportsContext.getInstance(), textElement);
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

		formatWidth = width - leftPadding - rightPadding;
		formatWidth = formatWidth < 0 ? 0 : formatWidth;
		maxHeight = height + availableStretchHeight - topPadding - bottomPadding;
		maxHeight = maxHeight < 0 ? 0 : maxHeight;
		this.canOverflow = canOverflow;
		
		// refresh properties if required
		ignoreMissingFont = defaultIgnoreMissingFont;
		if (hasDynamicIgnoreMissingFontProp)
		{
			String dynamicIgnoreMissingFontProp = dynamicPropertiesHolder.getDynamicProperties().getProperty(
					JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT);
			if (dynamicIgnoreMissingFontProp != null)
			{
				ignoreMissingFont = JRPropertiesUtil.asBoolean(dynamicIgnoreMissingFontProp);
			}
		}
		
		boolean saveLineBreakOffsets = defaultSaveLineBreakOffsets;
		if (hasDynamicSaveLineBreakOffsetsProp)
		{
			String dynamicSaveLineBreakOffsetsProp = dynamicPropertiesHolder.getDynamicProperties().getProperty(
					JRTextElement.PROPERTY_SAVE_LINE_BREAKS);
			if (dynamicSaveLineBreakOffsetsProp != null)
			{
				saveLineBreakOffsets = JRPropertiesUtil.asBoolean(dynamicSaveLineBreakOffsetsProp);
			}
		}
		
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

		TextLineWrapper lineWrapper = simpleLineWrapper;
		// check if the simple wrapper would handle the text
		if (!lineWrapper.start(styledText))
		{
			lineWrapper = complextLineWrapper;
			lineWrapper.start(styledText);
		}
		
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
				rendered = renderParagraph(lineWrapper, lastParagraphStart, lastParagraphText);

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
			renderParagraph(lineWrapper, lastParagraphStart, lastParagraphText);
		}
		
		return measuredState;
	}
	
	protected boolean hasParagraphIndents()
	{
		Integer firstLineIndent = jrParagraph.getFirstLineIndent();
		if (firstLineIndent != null && firstLineIndent.intValue() > 0)
		{
			return true;
		}
		
		Integer leftIndent = jrParagraph.getLeftIndent();
		if (leftIndent != null && leftIndent.intValue() > 0)
		{
			return true;
		}
		
		Integer rightIndent = jrParagraph.getRightIndent();
		return rightIndent != null && rightIndent.intValue() > 0;
	}

	/**
	 * 
	 */
	protected boolean renderParagraph(
		TextLineWrapper lineWrapper,
		int lastParagraphStart,
		String lastParagraphText
		)
	{
		if (lastParagraphText == null)
		{
			lineWrapper.startEmptyParagraph(lastParagraphStart);
		}
		else
		{
			lineWrapper.startParagraph(lastParagraphStart, 
					lastParagraphStart + lastParagraphText.length(),
					false);
		}

		List<Integer> tabIndexes = JRStringUtil.getTabIndexes(lastParagraphText);
		
		int[] currentTabHolder = new int[]{0};
		TabStop[] nextTabStopHolder = new TabStop[]{null};
		boolean[] requireNextWordHolder = new boolean[]{false};
		
		measuredState.paragraphStartLine = measuredState.lines;
		measuredState.textOffset = lastParagraphStart;
		
		boolean rendered = true;
		boolean renderedLine = false;

		// the paragraph is measured one line at a time
		while (lineWrapper.paragraphPosition() < lineWrapper.paragraphEnd() && rendered)
		{
			rendered = renderNextLine(lineWrapper, tabIndexes, currentTabHolder, nextTabStopHolder, requireNextWordHolder);
			renderedLine = renderedLine || rendered;
		}
		
		//if we rendered at least one line, and the last line didn't fit 
		//and the text does not overflow
		if (!rendered && prevMeasuredState != null && !canOverflow)
		{
			//handle last rendered row
			processLastTruncatedRow(lineWrapper, lastParagraphText, lastParagraphStart, renderedLine);
		}
		
		return rendered;
	}
	
	protected void processLastTruncatedRow(
		TextLineWrapper lineWrapper,
		String paragraphText, 
		int paragraphOffset,
		boolean lineTruncated
		)
	{
		//FIXME move all this to TextLineWrapper?
		if (lineTruncated && isToTruncateAtChar())
		{
			truncateLastLineAtChar(lineWrapper, paragraphText, paragraphOffset);
		}
		
		appendTruncateSuffix(lineWrapper);
	}

	protected void truncateLastLineAtChar(
		TextLineWrapper lineWrapper, 
		String paragraphText, 
		int paragraphOffset
		)
	{
		//truncate the original line at char
		measuredState = prevMeasuredState.cloneState();
		lineWrapper.startParagraph(measuredState.textOffset, 
				paragraphOffset + paragraphText.length(), 
				true);
		//render again the last line
		//if the line does not fit now, it will remain empty
		renderNextLine(lineWrapper, null, new int[]{0}, new TabStop[]{null}, new boolean[]{false});
	}

	protected void appendTruncateSuffix(TextLineWrapper lineWrapper)
	{
		String truncateSuffx = getTruncateSuffix();
		if (truncateSuffx == null)
		{
			return;
		}
		
		int lineStart = prevMeasuredState.textOffset;

		//advance from the line start until the next line start or the first newline
		String lineText = lineWrapper.getLineText(lineStart, measuredState.textOffset);
		int linePosition = lineText.length();
		
		//iterate to the beginning of the line
		boolean done = false;
		do
		{
			measuredState = prevMeasuredState.cloneState();

			String text = lineText.substring(0, linePosition) + truncateSuffx;
			boolean truncateAtChar = isToTruncateAtChar();
			TextLineWrapper lastLineWrapper = lineWrapper.lastLineWrapper(text, 
					measuredState.textOffset, linePosition, truncateAtChar);
			
			BreakIterator breakIterator = 
				truncateAtChar 
				? BreakIterator.getCharacterInstance() 
				: BreakIterator.getLineInstance();
			breakIterator.setText(text);

			if (renderNextLine(lastLineWrapper, null, new int[]{0}, new TabStop[]{null}, new boolean[]{false}))
			{
				int lastPos = lastLineWrapper.paragraphPosition();
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
								&& lineWrapper.charAt(prevMeasuredState.textOffset - 1) != '\n')
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
		//FIXME do not read each time
		return JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(propertiesHolder, 
				JRTextElement.PROPERTY_TRUNCATE_AT_CHAR, false);
	}

	protected String getTruncateSuffix()
	{
		//FIXME do not read each time
		String truncateSuffx = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(propertiesHolder,
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
	

	protected boolean renderNextLine(TextLineWrapper lineWrapper, List<Integer> tabIndexes, int[] currentTabHolder, TabStop[] nextTabStopHolder, boolean[] requireNextWordHolder)
	{
		boolean lineComplete = false;

		int lineStartPosition = lineWrapper.paragraphPosition();
		
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
			int tabIndexOrEndIndex = (tabIndexes == null || currentTabHolder[0] >= tabIndexes.size() ? lineWrapper.paragraphEnd() : tabIndexes.get(currentTabHolder[0]) + 1);
			
			float startX = (lineWrapper.paragraphPosition() == 0 ? jrParagraph.getFirstLineIndent() : 0) + leftPadding;
			float endX = width - jrParagraph.getRightIndent() - rightPadding;
			endX = endX < startX ? startX : endX;
			//formatWidth = endX - startX;
			//formatWidth = endX;

			int startIndex = lineWrapper.paragraphPosition();

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
			float availableWidth = endX - jrParagraph.getLeftIndent() - ParagraphUtil.getSegmentOffset(nextTabStopHolder[0], rightX); // nextTabStop can be null here; and that's OK
			
			// creating a text layout object for each tab segment 
			TextLine textLine = 
				lineWrapper.nextLine(
					availableWidth,
					tabIndexOrEndIndex,
					requireNextWordHolder[0]
					);
			
			if (textLine != null)
			{
				maxAscent = Math.max(maxAscent, textLine.getAscent());
				maxDescent = Math.max(maxDescent, textLine.getDescent());
				maxLeading = Math.max(maxLeading, textLine.getLeading());
				characterCount += textLine.getCharacterCount();
				isLeftToRight = isLeftToRight && textLine.isLeftToRight();

				//creating the current segment
				crtSegment = new TabSegment();
				crtSegment.textLine = textLine;

				float leftX = ParagraphUtil.getLeftX(nextTabStopHolder[0], textLine.getAdvance()); // nextTabStop can be null here; and that's OK
				if (rightX > leftX)
				{
					crtSegment.leftX = rightX;
					crtSegment.rightX = rightX + textLine.getAdvance();
				}
				else
				{
					crtSegment.leftX = leftX;
					// we need this special tab stop based utility call because adding the advance to leftX causes rounding issues
					crtSegment.rightX = ParagraphUtil.getRightX(nextTabStopHolder[0], textLine.getAdvance()); // nextTabStop can be null here; and that's OK
				}

				segments.add(crtSegment);
			}
			
			requireNextWordHolder[0] = true;

			if (lineWrapper.paragraphPosition() == tabIndexOrEndIndex)
			{
				// the segment limit was a tab; going to the next tab
				currentTabHolder[0] = currentTabHolder[0] + 1;
			}
			
			if (lineWrapper.paragraphPosition() == lineWrapper.paragraphEnd())
			{
				// the segment limit was the paragraph end; line completed and next line should start at normal zero x offset
				lineComplete = true;
				nextTabStopHolder[0] = null;
			}
			else
			{
				// there is paragraph text remaining 
				if (lineWrapper.paragraphPosition() == tabIndexOrEndIndex)
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
					if (textLine == null)
					{
						// nothing fitted; next line should start at first tab stop indent
						if (nextTabStopHolder[0].getPosition() == ParagraphUtil.getFirstTabStop(jrParagraph, endX).getPosition())//FIXMETAB check based on segments.size()
						{
							// at second attempt we give up to avoid infinite loop
							nextTabStopHolder[0] = null;
							requireNextWordHolder[0] = false;
							
							//provide dummy maxFontSize because it is used for the line height of this empty line when attempting drawing below
							TextLine baseLine = lineWrapper.baseTextLine(startIndex);
							maxAscent = baseLine.getAscent();
							maxDescent = baseLine.getDescent();
							maxLeading = baseLine.getLeading();
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
			measuredState.textWidth = Math.max(measuredState.textWidth, crtSegment == null ? 0 : crtSegment.rightX);
			measuredState.textHeight = newTextHeight;
			measuredState.lines++;

			if (
				measuredState.isMeasured
				&& (tabIndexes == null || tabIndexes.size() == 0)
				&& !hasParagraphIndents() 
				)
			{
				measuredState.fontSizeSum += 
					lineWrapper.maxFontSize(lineStartPosition, lineStartPosition + characterCount);

				if (measuredState.lines == 1)
				{
					measuredState.firstLineLeading = measuredState.textHeight;
					measuredState.firstLineMaxFontSize = measuredState.fontSizeSum;
				}
			}
			else
			{
				measuredState.isMeasured = false;
			}
			
			// here is the Y offset where we would draw the line
			//lastDrawPosY = drawPosY;
			//
			measuredState.textHeight += maxDescent;
			
			measuredState.textOffset += lineWrapper.paragraphPosition() - lineStartPosition;
			
			if (lineWrapper.paragraphPosition() < lineWrapper.paragraphEnd())
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

	

	/**
	 * 
	 */
	public FontRenderContext getFontRenderContext()
	{
		return AwtTextRenderer.LINE_BREAK_FONT_RENDER_CONTEXT;
	}

	class Context implements TextMeasureContext
	{
		@Override
		public JasperReportsContext getJasperReportsContext()
		{
			return jasperReportsContext;
		}

		@Override
		public JRCommonText getElement()
		{
			return textElement;
		}

		@Override
		public JRPropertiesHolder getPropertiesHolder()
		{
			return propertiesHolder;
		}

		@Override
		public boolean isIgnoreMissingFont()
		{
			return ignoreMissingFont;
		}

		@Override
		public FontRenderContext getFontRenderContext()
		{
			return TextMeasurer.this.getFontRenderContext();
		}
	}
}

class TabSegment
{
	public TextLine textLine;
	public float leftX;
	public float rightX;
}
