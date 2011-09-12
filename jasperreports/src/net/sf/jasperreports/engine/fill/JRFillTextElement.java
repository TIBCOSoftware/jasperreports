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
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSingletonCache;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRTextMeasurerUtil;
import net.sf.jasperreports.engine.util.MarkupProcessor;
import net.sf.jasperreports.engine.util.MarkupProcessorFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRFillTextElement extends JRFillElement implements JRTextElement
{

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
	/**
	 * @deprecated No longer used.
	 */
	private float lineSpacingFactor;
	/**
	 * @deprecated No longer used.
	 */
	private float leadingOffset;
	private float textHeight;
	private int textStart;
	private int textEnd;
	private short[] lineBreakOffsets;
	private String textTruncateSuffix;
	private String rawText;
	private JRStyledText styledText;
	private Map<JRStyle,Map<Attribute,Object>> styledTextAttributesMap = new HashMap<JRStyle,Map<Attribute,Object>>();
	
	protected final JRLineBox lineBox;
	protected final JRParagraph paragraph;

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

		lineBox = textElement.getLineBox().clone(this);
		paragraph = textElement.getParagraph().clone(this);
	}
	

	protected JRFillTextElement(JRFillTextElement textElement, JRFillCloneFactory factory)
	{
		super(textElement, factory);
		
		lineBox = textElement.getLineBox().clone(this);
		paragraph = textElement.getParagraph().clone(this);
	}


	private void createTextMeasurer()
	{
		textMeasurer = JRTextMeasurerUtil.createTextMeasurer(this);
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
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
	}

	/**
	 *
	 */
	public HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return JRStyleResolver.getHorizontalAlignmentValue(this);
	}
		
	public HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return ((JRTextElement)this.parent).getOwnHorizontalAlignmentValue();
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(HorizontalAlignEnum horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public VerticalAlignEnum getVerticalAlignmentValue()
	{
		return JRStyleResolver.getVerticalAlignmentValue(this);
	}
		
	public VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return ((JRTextElement)this.parent).getOwnVerticalAlignmentValue();
	}

	/**
	 *
	 */
	public void setVerticalAlignment(VerticalAlignEnum verticalAlignment)
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
		return ((JRTextElement)this.parent).getOwnRotationValue();
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
		return ((JRTextElement)parent).getOwnMarkup();
	}

	/**
	 *
	 */
	public void setMarkup(String markup)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	/**
	 *
	 */
	public JRParagraph getParagraph()
	{
		return paragraph;
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
			JRFontUtil.getAttributesWithoutAwtFont(styledTextAttributes, this);
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
	 * @deprecated No longer used.
	 */
	protected float getLineSpacingFactor()
	{
		return lineSpacingFactor;
	}
		
	/**
	 * @deprecated No longer used.
	 */
	protected void setLineSpacingFactor(float lineSpacingFactor)
	{
		this.lineSpacingFactor = lineSpacingFactor;
	}

	/**
	 * @deprecated No longer used.
	 */
	protected float getLeadingOffset()
	{
		return leadingOffset;
	}
		
	/**
	 * @deprecated No longer used.
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
	protected void reset()
	{
		super.reset();
		
		isLeftToRight = true;
		lineSpacingFactor = 0;
		leadingOffset = 0;
		textHeight = 0;
	}


	/**
	 *
	 */
	protected void rewind()
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

		/*   */
		JRMeasuredText measuredText = textMeasurer.measure(
			tmpStyledText,
			getTextEnd(),
			availableStretchHeight,
			canOverflow()
			);
		
		isLeftToRight = measuredText.isLeftToRight();
		setTextHeight(measuredText.getTextHeight());
		if (getRotationValue().equals(RotationEnum.NONE))
		{
			setStretchHeight((int)getTextHeight() + getLineBox().getTopPadding().intValue() + getLineBox().getBottomPadding().intValue());
		}
		else
		{
			setStretchHeight(getHeight());
		}
		setTextStart(getTextEnd());
		setTextEnd(measuredText.getTextOffset());
		setLineBreakOffsets(measuredText.getLineBreakOffsets());
		setTextTruncateSuffix(measuredText.getTextSuffix());
		//setLineSpacingFactor(measuredText.getLineSpacingFactor());
		//setLeadingOffset(measuredText.getLeadingOffset());
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
		return ((JRFont)parent).getOwnFontName();
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
		return ((JRFont)parent).isOwnBold();
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
		return ((JRFont)parent).isOwnItalic();
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
		return ((JRFont)parent).isOwnUnderline();
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
		return ((JRFont)parent).isOwnStrikeThrough();
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
	public int getFontSize()
	{
		return JRStyleResolver.getFontSize(this);
	}

	/**
	 *
	 */
	public Integer getOwnFontSize()
	{
		return ((JRFont)parent).getOwnFontSize();
	}

	/**
	 *
	 */
	public void setFontSize(int size)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Alternative setSize method which allows also to reset
	 * the "own" size property.
	 */
	public void setFontSize(Integer size)
	{
		throw new UnsupportedOperationException();
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
		return ((JRFont)parent).getOwnPdfFontName();
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
		return ((JRFont)parent).getOwnPdfEncoding();
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
		return ((JRFont)parent).isOwnPdfEmbedded();
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

	protected static MarkupProcessor getMarkupProcessor(String markup)
	{
		MarkupProcessor markupProcessor = markupProcessors.get(markup);
		
		if (markupProcessor == null)
		{
			String factoryClass = JRProperties.getProperty(MarkupProcessorFactory.PROPERTY_MARKUP_PROCESSOR_FACTORY_PREFIX + markup);
			if (factoryClass == null)
			{
				throw new JRRuntimeException("No markup processor factory specifyed for '" + markup + "' markup.");
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
		
		boolean keepAllText = !canOverflow() 
				&& JRProperties.getBooleanProperty(this, JRTextElement.PROPERTY_PRINT_KEEP_FULL_TEXT, false);
		if (keepAllText)
		{
			//assert getTextStart() == 0
			if (startIndex != 0)
			{
				throw new JRRuntimeException("Text start index != 0 on keep all text.");
			}
			
			if (!JRCommonText.MARKUP_NONE.equals(getMarkup()))
			{
				//rewrite as styled text
				String styledText = filler.getStyledTextParser().write(
						fullStyledText);
				printText.setText(styledText);
			}
			else
			{
				printText.setText(fullText);
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
				printedText = fullText.substring(startIndex, endIndex);
			}
			printText.setText(printedText);
		}
		
		printText.setTextTruncateSuffix(getTextTruncateSuffix());
		printText.setLineBreakOffsets(getLineBreakOffsets());
	}

	protected String getTextTruncateSuffix()
	{
		return textTruncateSuffix;
	}

	protected void setTextTruncateSuffix(String textTruncateSuffix)
	{
		this.textTruncateSuffix = textTruncateSuffix;
	}

}
