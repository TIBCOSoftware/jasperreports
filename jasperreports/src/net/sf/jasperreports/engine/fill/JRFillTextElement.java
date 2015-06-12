/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRSingletonCache;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextMeasurerUtil;
import net.sf.jasperreports.engine.util.MarkupProcessor;
import net.sf.jasperreports.engine.util.MarkupProcessorFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRFillTextElement extends JRFillElement implements JRTextElement
{
	
	public static final String PROPERTY_CONSUME_SPACE_ON_OVERFLOW = 
			JRPropertiesUtil.PROPERTY_PREFIX + "consume.space.on.overflow";
	public static final String EXCEPTION_MESSAGE_KEY_MISSING_MARKUP_PROCESSOR_FACTORY = "fill.text.element.missing.markup.processor.factory";
	public static final String EXCEPTION_MESSAGE_KEY_INVALID_START_INDEX = "fill.text.element.invalid.start.index";

	/**
	 *
	 */
	private static final JRSingletonCache<MarkupProcessorFactory> markupProcessorFactoryCache = 
			new JRSingletonCache<MarkupProcessorFactory>(MarkupProcessorFactory.class);
	private static final Map<String,MarkupProcessor> markupProcessors = new HashMap<String,MarkupProcessor>();

	/**
	 *
	 */
	private boolean isLeftToRight = true;
	private JRTextMeasurer textMeasurer;
	private float lineSpacingFactor;
	private float leadingOffset;
	private float textWidth;
	private float textHeight;
	private int elementStretchHeightDelta;
	private int textStart;
	private int textEnd;
	private short[] lineBreakOffsets;
	private String textTruncateSuffix;
	private String rawText;
	private JRStyledText styledText;
	private Map<JRStyle,Map<Attribute,Object>> styledTextAttributesMap = new HashMap<JRStyle,Map<Attribute,Object>>();
	
	protected final JRLineBox initLineBox;
	protected final JRParagraph initParagraph;
	private final boolean consumeSpaceOnOverflow;
	protected JRLineBox lineBox;
	protected JRParagraph paragraph;

	private JRStyle currentFillStyle;
	private FillStyleObjects fillStyleObjects;
	private Map<JRStyle, FillStyleObjects> fillStyleObjectsMap;
	
	private boolean defaultKeepFullText;
	private boolean dynamicKeepFullText;

	/**
	 *
	 */
	protected JRFillTextElement(
		JRBaseFiller filler,
		JRTextElement textElement, 
		JRFillObjectFactory factory
		)
	{
		super(filler, textElement, factory);

		initLineBox = textElement.getLineBox().clone(this);
		initParagraph = textElement.getParagraph().clone(this);

		// not supporting property expressions for this
		this.consumeSpaceOnOverflow = JRPropertiesUtil.getInstance(filler.getJasperReportsContext()).getBooleanProperty(
				textElement, PROPERTY_CONSUME_SPACE_ON_OVERFLOW, true);
		
		this.defaultKeepFullText = filler.getPropertiesUtil().getBooleanProperty( 
				JRTextElement.PROPERTY_PRINT_KEEP_FULL_TEXT, false,
				// manually falling back to report properties as getParentProperties() is null for textElement
				textElement, filler.getJasperReport());
		this.dynamicKeepFullText = hasDynamicProperty(JRTextElement.PROPERTY_PRINT_KEEP_FULL_TEXT);
		
		this.fillStyleObjectsMap = new HashMap<JRStyle, JRFillTextElement.FillStyleObjects>();
	}
	

	protected JRFillTextElement(JRFillTextElement textElement, JRFillCloneFactory factory)
	{
		super(textElement, factory);
		
		initLineBox = textElement.getLineBox().clone(this);
		initParagraph = textElement.getParagraph().clone(this);
		this.consumeSpaceOnOverflow = textElement.consumeSpaceOnOverflow;
		
		this.defaultKeepFullText = textElement.defaultKeepFullText;
		this.dynamicKeepFullText = textElement.dynamicKeepFullText;
		this.fillStyleObjectsMap = textElement.fillStyleObjectsMap;
	}


	private void createTextMeasurer()
	{
		textMeasurer = JRTextMeasurerUtil.getInstance(filler.getJasperReportsContext()).createTextMeasurer(this);
	}

	protected void ensureTextMeasurer()
	{
		if (textMeasurer == null)
		{
			createTextMeasurer();
		}
	}


	/**
	 *
	 */
	protected void evaluateStyle(
		byte evaluation
		) throws JRException
	{
		super.evaluateStyle(evaluation);

		if (providerStyle == null)
		{
			lineBox = null;
			paragraph = null;
			
			setFillStyleObjects();
		}
		else
		{
			lineBox = initLineBox.clone(this);
			paragraph = initParagraph.clone(this);
			JRStyleResolver.appendBox(lineBox, providerStyle.getLineBox());
			JRStyleResolver.appendParagraph(paragraph, providerStyle.getParagraph());
			
			fillStyleObjects = null;
		}
	}

	private void setFillStyleObjects()
	{
		JRStyle evaluatedStyle = getStyle();
		// quick check for fast exit (avoid map lookup) when the style has not changed
		//FIXME keep two previous styles to catch common alternating row styles?
		if (fillStyleObjects != null && currentFillStyle == evaluatedStyle)
		{
			return;
		}
		
		// update current style
		currentFillStyle = evaluatedStyle;
		
		// search cached per style
		fillStyleObjects = fillStyleObjectsMap.get(evaluatedStyle);
		if (fillStyleObjects == null)
		{
			// create fill style objects
			CachingLineBox cachedLineBox = new CachingLineBox(initLineBox);
			CachingParagraph cachedParagraph = new CachingParagraph(initParagraph);
			fillStyleObjects = new FillStyleObjects(cachedLineBox, cachedParagraph);
			
			fillStyleObjectsMap.put(evaluatedStyle, fillStyleObjects);
		}
	}


	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getHorizontalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getOwnHorizontalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalTextAlign(HorizontalTextAlignEnum)}.
	 */
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue)
	{
		setHorizontalTextAlign(net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalTextAlignEnum(horizontalAlignmentValue));
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getVerticalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getOwnVerticalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalTextAlign(VerticalTextAlignEnum)}.
	 */
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue)
	{
		setVerticalTextAlign(net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalTextAlignEnum(verticalAlignmentValue));
	}
		
	/**
	 *
	 */
	public HorizontalTextAlignEnum getHorizontalTextAlign()
	{
		return JRStyleResolver.getHorizontalTextAlign(this);
	}
		
	public HorizontalTextAlignEnum getOwnHorizontalTextAlign()
	{
		return providerStyle == null || providerStyle.getOwnHorizontalTextAlign() == null ? ((JRTextElement)this.parent).getOwnHorizontalTextAlign() : providerStyle.getOwnHorizontalTextAlign();
	}

	/**
	 *
	 */
	public void setHorizontalTextAlign(HorizontalTextAlignEnum horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public VerticalTextAlignEnum getVerticalTextAlign()
	{
		return JRStyleResolver.getVerticalTextAlign(this);
	}
		
	public VerticalTextAlignEnum getOwnVerticalTextAlign()
	{
		return providerStyle == null || providerStyle.getOwnVerticalTextAlign() == null ? ((JRTextElement)this.parent).getOwnVerticalTextAlign() : providerStyle.getOwnVerticalTextAlign();
	}

	/**
	 *
	 */
	public void setVerticalTextAlign(VerticalTextAlignEnum verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public RotationEnum getRotationValue()
	{
		return JRStyleResolver.getRotationValue(this);
	}
		
	public RotationEnum getOwnRotationValue()
	{
		return providerStyle == null || providerStyle.getOwnRotationValue() == null ? ((JRTextElement)this.parent).getOwnRotationValue() : providerStyle.getOwnRotationValue();
	}

	/**
	 *
	 */
	public void setRotation(RotationEnum rotation)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#getLineSpacing()}.
	 */
	public LineSpacingEnum getLineSpacingValue()
	{
		return getParagraph().getLineSpacing();
	}
		
	/**
	 * @deprecated Replaced by {@link JRParagraph#getOwnLineSpacing()}.
	 */
	public LineSpacingEnum getOwnLineSpacingValue()
	{
		return getParagraph().getOwnLineSpacing();
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#setLineSpacing(LineSpacingEnum)}.
	 */
	public void setLineSpacing(LineSpacingEnum lineSpacing)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public String getMarkup()
	{
		return JRStyleResolver.getMarkup(this);
	}
		
	/**
	 *
	 */
	public String getOwnMarkup()
	{
		return providerStyle == null || providerStyle.getOwnMarkup() == null ? ((JRTextElement)parent).getOwnMarkup() : providerStyle.getOwnMarkup();
	}

	/**
	 *
	 */
	public void setMarkup(String markup)
	{
		throw new UnsupportedOperationException();
	}

	protected JRLineBox getPrintLineBox()
	{
		return lineBox == null ? initLineBox : lineBox;
	}
	
	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox == null 
				? (fillStyleObjects == null ? initLineBox : fillStyleObjects.lineBox) 
				: lineBox;
	}

	protected JRParagraph getPrintParagraph()
	{
		return paragraph == null ? initParagraph : paragraph;
	}

	/**
	 *
	 */
	public JRParagraph getParagraph()
	{
		return paragraph == null 
				? (fillStyleObjects == null ? initParagraph : fillStyleObjects.paragraph) 
				: paragraph;
	}

	/**
	 * @deprecated
	 */
	public JRFont getFont()
	{
		return this;
	}

	
	/**
	 *
	 */
	protected Map<Attribute,Object> getStyledTextAttributes()
	{
		JRStyle style = getStyle();
		Map<Attribute,Object> styledTextAttributes = styledTextAttributesMap.get(style);
		if (styledTextAttributes == null)
		{
			styledTextAttributes = new HashMap<Attribute,Object>(); 
			//JRFontUtil.getAttributes(styledTextAttributes, this, filler.getLocale());
			FontUtil.getInstance(filler.getJasperReportsContext()).getAttributesWithoutAwtFont(styledTextAttributes, this);
			styledTextAttributes.put(TextAttribute.FOREGROUND, getForecolor());
			if (getModeValue() == ModeEnum.OPAQUE)
			{
				styledTextAttributes.put(TextAttribute.BACKGROUND, getBackcolor());
			}
			styledTextAttributesMap.put(style, styledTextAttributes);
		}
		
		return styledTextAttributes;
	}

	/**
	 *
	 */
	protected float getLineSpacingFactor()
	{
		return lineSpacingFactor;
	}
		
	/**
	 *
	 */
	protected void setLineSpacingFactor(float lineSpacingFactor)
	{
		this.lineSpacingFactor = lineSpacingFactor;
	}

	/**
	 *
	 */
	protected float getLeadingOffset()
	{
		return leadingOffset;
	}
		
	/**
	 *
	 */
	protected void setLeadingOffset(float leadingOffset)
	{
		this.leadingOffset = leadingOffset;
	}

	/**
	 *
	 */
	public RunDirectionEnum getRunDirectionValue()
	{
		return isLeftToRight ? RunDirectionEnum.LTR : RunDirectionEnum.RTL;
	}
		
	/**
	 *
	 */
	public float getTextWidth()
	{
		return textWidth;
	}
		
	/**
	 *
	 */
	protected void setTextWidth(float textWidth)
	{
		this.textWidth = textWidth;
	}

	/**
	 *
	 */
	protected float getTextHeight()
	{
		return textHeight;
	}
		
	/**
	 *
	 */
	protected void setTextHeight(float textHeight)
	{
		this.textHeight = textHeight;
	}

	/**
	 *
	 */
	protected int getTextStart()
	{
		return textStart;
	}
		
	/**
	 *
	 */
	protected void setTextStart(int textStart)
	{
		this.textStart = textStart;
	}

	/**
	 *
	 */
	protected int getTextEnd()
	{
		return textEnd;
	}
		
	/**
	 *
	 */
	protected void setTextEnd(int textEnd)
	{
		this.textEnd = textEnd;
	}
	
	protected short[] getLineBreakOffsets()
	{
		return lineBreakOffsets;
	}

	protected void setLineBreakOffsets(short[] lineBreakOffsets)
	{
		this.lineBreakOffsets = lineBreakOffsets;
	}

	protected void resetTextChunk()
	{
		textStart = 0;
		textEnd = 0;
		textTruncateSuffix = null;
		lineBreakOffsets = null;
		elementStretchHeightDelta = 0;
	}
	
	/**
	 *
	 */
	protected String getRawText()
	{
		return rawText;
	}

	/**
	 *
	 */
	protected void setRawText(String rawText)
	{
		this.rawText = rawText;
		styledText = null;
	}


	/**
	 *
	 */
	public void reset()
	{
		super.reset();
		
		isLeftToRight = true;
		lineSpacingFactor = 0;
		leadingOffset = 0;
		textHeight = 0;
		elementStretchHeightDelta = 0;
	}


	/**
	 *
	 */
	public void rewind()
	{
		resetTextChunk();
	}


	/**
	 *
	 */
	protected JRStyledText getStyledText()
	{
		if (styledText == null)
		{
			String text = getRawText();
			if (text != null)
			{
				styledText = 
					filler.getStyledTextParser().getStyledText(
						getStyledTextAttributes(), 
						text, 
						!JRCommonText.MARKUP_NONE.equals(getMarkup()),
						filler.getLocale()
						);
			}
		}
		
		return styledText;
	}

	/**
	 *
	 */
	public String getText()
	{
		JRStyledText tmpStyledText = getStyledText();

		if (tmpStyledText == null)
		{
			return null;
		}

		return tmpStyledText.getText();
	}
	

	/**
	 *
	 */
	protected void chopTextElement(
		int availableStretchHeight
		)
	{
		ensureTextMeasurer();
		
		JRStyledText tmpStyledText = getStyledText();

		if (tmpStyledText == null)
		{
			return;
		}

		if (getTextEnd() == tmpStyledText.getText().length())
		{
			return;
		}

		boolean canOverflow = canOverflow();
		JRMeasuredText measuredText = textMeasurer.measure(
			tmpStyledText,
			getTextEnd(),
			availableStretchHeight,
			canOverflow
			);
		
		isLeftToRight = measuredText.isLeftToRight();
		setTextWidth(measuredText.getTextWidth());
		setTextHeight(measuredText.getTextHeight());
		
		elementStretchHeightDelta = 0;
		if (getRotationValue().equals(RotationEnum.NONE))
		{
			//FIXME truncating to int here seems wrong as the text measurer compares 
			// the exact text height against the available height
			int elementTextHeight = (int) getTextHeight() + getLineBox().getTopPadding() + getLineBox().getBottomPadding();
			boolean textEnded = measuredText.getTextOffset() >= tmpStyledText.getText().length();
			if (textEnded || !canOverflow || !consumeSpaceOnOverflow)
			{
				setStretchHeight(elementTextHeight);
			}
			else
			{
				// occupy all remaining space so that no other element renders there
				// but do not change the print element height
				int stretchHeight = getHeight() + availableStretchHeight;
				setStretchHeight(stretchHeight);
				
				// store the difference between the consumed stretch height and the text stretch height.
				// this will be used in fill() to set the print element height, 
				// which doesn't take into account the consumed empty space
				int textStretchHeight = elementTextHeight > getHeight() ? elementTextHeight : getHeight();
				elementStretchHeightDelta = getStretchHeight() - textStretchHeight;
			}
		}
		else
		{
			setStretchHeight(getHeight());
		}
		
		setTextStart(getTextEnd());
		setTextEnd(measuredText.getTextOffset());
		setLineBreakOffsets(measuredText.getLineBreakOffsets());
		setTextTruncateSuffix(measuredText.getTextSuffix());
		setLineSpacingFactor(measuredText.getLineSpacingFactor());
		setLeadingOffset(measuredText.getLeadingOffset());
	}
	
	public int getPrintElementHeight()
	{
		return getStretchHeight() - elementStretchHeightDelta;
	}
	
	protected abstract boolean canOverflow();


	/**
	 *
	 */
	public String getFontName()
	{
		return JRStyleResolver.getFontName(this);
	}

	/**
	 *
	 */
	public String getOwnFontName()
	{
		return providerStyle == null || providerStyle.getOwnFontName() == null ? ((JRFont)parent).getOwnFontName() : providerStyle.getOwnFontName();
	}

	/**
	 *
	 */
	public void setFontName(String fontName)
	{
		throw new UnsupportedOperationException();
	}


	/**
	 *
	 */
	public boolean isBold()
	{
		return JRStyleResolver.isBold(this);
	}

	/**
	 *
	 */
	public Boolean isOwnBold()
	{
		return providerStyle == null || providerStyle.isOwnBold() == null ? ((JRFont)parent).isOwnBold() : providerStyle.isOwnBold();
	}

	/**
	 *
	 */
	public void setBold(boolean isBold)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Alternative setBold method which allows also to reset
	 * the "own" isBold property.
	 */
	public void setBold(Boolean isBold)
	{
		throw new UnsupportedOperationException();
	}


	/**
	 *
	 */
	public boolean isItalic()
	{
		return JRStyleResolver.isItalic(this);
	}

	/**
	 *
	 */
	public Boolean isOwnItalic()
	{
		return providerStyle == null || providerStyle.isOwnItalic() == null ? ((JRFont)parent).isOwnItalic() : providerStyle.isOwnItalic();
	}

	/**
	 *
	 */
	public void setItalic(boolean isItalic)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Alternative setItalic method which allows also to reset
	 * the "own" isItalic property.
	 */
	public void setItalic(Boolean isItalic)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public boolean isUnderline()
	{
		return JRStyleResolver.isUnderline(this);
	}

	/**
	 *
	 */
	public Boolean isOwnUnderline()
	{
		return providerStyle == null || providerStyle.isOwnUnderline() == null ? ((JRFont)parent).isOwnUnderline() : providerStyle.isOwnUnderline();
	}

	/**
	 *
	 */
	public void setUnderline(boolean isUnderline)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Alternative setUnderline method which allows also to reset
	 * the "own" isUnderline property.
	 */
	public void setUnderline(Boolean isUnderline)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public boolean isStrikeThrough()
	{
		return JRStyleResolver.isStrikeThrough(this);
	}

	/**
	 *
	 */
	public Boolean isOwnStrikeThrough()
	{
		return providerStyle == null || providerStyle.isOwnStrikeThrough() == null ? ((JRFont)parent).isOwnStrikeThrough() : providerStyle.isOwnStrikeThrough();
	}

	/**
	 *
	 */
	public void setStrikeThrough(boolean isStrikeThrough)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Alternative setStrikeThrough method which allows also to reset
	 * the "own" isStrikeThrough property.
	 */
	public void setStrikeThrough(Boolean isStrikeThrough)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public float getFontsize()
	{
		return JRStyleResolver.getFontsize(this);
	}

	/**
	 *
	 */
	public Float getOwnFontsize()
	{
		return providerStyle == null || providerStyle.getOwnFontsize() == null ? ((JRFont)parent).getOwnFontsize() : providerStyle.getOwnFontsize();
	}

	/**
	 * 
	 */
	public void setFontSize(Float size)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated Replaced by {@link #getFontsize()}.
	 */
	public int getFontSize()
	{
		return (int)getFontsize();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnFontsize()}.
	 */
	public Integer getOwnFontSize()
	{
		Float fontSize = getOwnFontsize();
		return fontSize == null ? null : fontSize.intValue();
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	public void setFontSize(int size)
	{
		setFontSize((float)size);
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	public void setFontSize(Integer size)
	{
		setFontSize(size == null ? null : size.floatValue());
	}

	/**
	 *
	 */
	public String getPdfFontName()
	{
		return JRStyleResolver.getPdfFontName(this);
	}

	/**
	 *
	 */
	public String getOwnPdfFontName()
	{
		return providerStyle == null || providerStyle.getOwnPdfFontName() == null ? ((JRFont)parent).getOwnPdfFontName() : providerStyle.getOwnPdfFontName();
	}

	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName)
	{
		throw new UnsupportedOperationException();
	}


	/**
	 *
	 */
	public String getPdfEncoding()
	{
		return JRStyleResolver.getPdfEncoding(this);
	}

	/**
	 *
	 */
	public String getOwnPdfEncoding()
	{
		return providerStyle == null || providerStyle.getOwnPdfEncoding() == null ? ((JRFont)parent).getOwnPdfEncoding() : providerStyle.getOwnPdfEncoding();
	}

	/**
	 *
	 */
	public void setPdfEncoding(String pdfEncoding)
	{
		throw new UnsupportedOperationException();
	}


	/**
	 *
	 */
	public boolean isPdfEmbedded()
	{
		return JRStyleResolver.isPdfEmbedded(this);
	}

	/**
	 *
	 */
	public Boolean isOwnPdfEmbedded()
	{
		return providerStyle == null || providerStyle.isOwnPdfEmbedded() == null ? ((JRFont)parent).isOwnPdfEmbedded() : providerStyle.isOwnPdfEmbedded();
	}

	/**
	 *
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Alternative setPdfEmbedded method which allows also to reset
	 * the "own" isPdfEmbedded property.
	 */
	public void setPdfEmbedded(Boolean isPdfEmbedded)
	{
		throw new UnsupportedOperationException();
	}

	
	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}

	
	/**
	 *
	 */
	public void setHeight(int height)
	{
		super.setHeight(height);
		
		createTextMeasurer();
	}


	public void setWidth(int width)
	{
		super.setWidth(width);
		
		createTextMeasurer();
	}

	protected String processMarkupText(String text)
	{
		text = JRStringUtil.replaceCRwithLF(text);
		
		if (text != null)
		{
			String markup = getMarkup();
			if (
				!JRCommonText.MARKUP_NONE.equals(markup)
				&& !JRCommonText.MARKUP_STYLED_TEXT.equals(markup)
				)
			{
				text = getMarkupProcessor(markup).convert(text);
			}
		}
		
		return text;
	}

	protected MarkupProcessor getMarkupProcessor(String markup)
	{
		MarkupProcessor markupProcessor = markupProcessors.get(markup);
		
		if (markupProcessor == null)
		{
			String factoryClass = filler.getPropertiesUtil().getProperty(MarkupProcessorFactory.PROPERTY_MARKUP_PROCESSOR_FACTORY_PREFIX + markup);
			if (factoryClass == null)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_MISSING_MARKUP_PROCESSOR_FACTORY,  
						new Object[]{markup} 
						);
			}

			MarkupProcessorFactory factory = null;
			try
			{
				factory = markupProcessorFactoryCache.getCachedInstance(factoryClass);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
			
			markupProcessor = factory.createMarkupProcessor();
			markupProcessors.put(markup, markupProcessor);
		}
		
		return markupProcessor;
	}

	protected void setPrintText(JRPrintText printText)
	{
		int startIndex = getTextStart();
		int endIndex = getTextEnd();
		JRStyledText fullStyledText = getStyledText();
		String fullText = fullStyledText.getText();
		
		boolean keepAllText = !canOverflow() && keepFullText();
		if (keepAllText)
		{
			//assert getTextStart() == 0
			if (startIndex != 0)
			{
				throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_INVALID_START_INDEX,  
					(Object[])null 
					);
			}
			
			if (!JRCommonText.MARKUP_NONE.equals(getMarkup()))
			{
				//rewrite as styled text
				String styledText = filler.getStyledTextParser().write(
						fullStyledText);
				setPrintText(printText, styledText);
			}
			else
			{
				setPrintText(printText, fullText);
			}
			
			if (endIndex < fullText.length())
			{
				printText.setTextTruncateIndex(Integer.valueOf(endIndex));
			}
		}
		else
		{
			String printedText;
			if (!JRCommonText.MARKUP_NONE.equals(getMarkup()))
			{
				printedText = filler.getStyledTextParser().write(
						fullStyledText, 
						startIndex, endIndex);
			}
			else
			{
				// relying on substring to return the same String object when whole substring
				printedText = fullText.substring(startIndex, endIndex);
			}
			
			setPrintText(printText, printedText);
		}
		
		printText.setTextTruncateSuffix(getTextTruncateSuffix());
		printText.setLineBreakOffsets(getLineBreakOffsets());
	}
	
	protected boolean keepFullText()
	{
		boolean keepFullText = defaultKeepFullText;
		if (dynamicKeepFullText)
		{
			String keepFullTextProp = getDynamicProperties().getProperty(
					JRTextElement.PROPERTY_PRINT_KEEP_FULL_TEXT);
			if (keepFullTextProp != null)
			{
				keepFullText = JRPropertiesUtil.asBoolean(keepFullTextProp);
			}
		}
		return keepFullText;
	}
	
	protected void setPrintText(JRPrintText printText, String text)
	{
		printText.setText(text);
	}

	protected String getTextTruncateSuffix()
	{
		return textTruncateSuffix;
	}

	protected void setTextTruncateSuffix(String textTruncateSuffix)
	{
		this.textTruncateSuffix = textTruncateSuffix;
	}

	private static class FillStyleObjects
	{
		private final JRLineBox lineBox;
		private final JRParagraph paragraph;
		
		public FillStyleObjects(JRLineBox lineBox, JRParagraph paragraph)
		{
			this.lineBox = lineBox;
			this.paragraph = paragraph;
		}
	}
}
