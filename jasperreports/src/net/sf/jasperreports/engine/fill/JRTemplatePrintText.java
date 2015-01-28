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

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.JRStyledTextUtil;
import net.sf.jasperreports.engine.virtualization.VirtualizationInput;
import net.sf.jasperreports.engine.virtualization.VirtualizationOutput;


/**
 * Implementation of {@link net.sf.jasperreports.engine.JRPrintText} that uses
 * a {@link net.sf.jasperreports.engine.fill.JRTemplateText} instance to
 * store common attributes. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRTemplatePrintText extends JRTemplatePrintElement implements JRPrintText
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private static final int SERIALIZATION_FLAG_ANCHOR = 1;
	private static final int SERIALIZATION_FLAG_HYPERLINK = 1 << 1;
	private static final int SERIALIZATION_FLAG_RTL = 1 << 2;
	private static final int SERIALIZATION_FLAG_TRUNCATION = 1 << 3;
	private static final int SERIALIZATION_FLAG_LINE_BREAK_OFFSETS = 1 << 4;
	private static final int SERIALIZATION_FLAG_ZERO_LINE_BREAK_OFFSETS = 1 << 5;
	private static final int SERIALIZATION_FLAG_HAS_VALUE = 1 << 6;

	/**
	 *
	 */
	private String text = "";
	private Integer textTruncateIndex;
	private String textTruncateSuffix;
	private short[] lineBreakOffsets;
	//private transient String truncatedText;
	private Object value;
	private float lineSpacingFactor;
	private float leadingOffset;
	private RunDirectionEnum runDirectionValue;
	private float textHeight;
	
	// we're no longer setting this at fill time, all format attributes are in the template.
	// keeping the field for old serialized JasperPrints.
	private TextFormat textFormat;
	
	private String anchorName;
	private String hyperlinkReference;
	private String hyperlinkAnchor;
	private Integer hyperlinkPage;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;

	/**
	 * The bookmark level for the anchor associated with this field.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;
	
	public JRTemplatePrintText()
	{
		
	}
	
	/**
	 * Creates a print text element.
	 * 
	 * @param text the template text that the element will use
	 * @deprecated provide a source Id via {@link #JRTemplatePrintText(JRTemplateText, int)}
	 */
	public JRTemplatePrintText(JRTemplateText text)
	{
		super(text);
	}
	
	/**
	 * Creates a print text element.
	 * 
	 * @param text the template text that the element will use
	 * @param sourceElementId the Id of the source element
	 * @deprecated replaced by {@link #JRTemplatePrintText(JRTemplateText, PrintElementOriginator)}
	 */
	public JRTemplatePrintText(JRTemplateText text, int sourceElementId)
	{
		super(text, sourceElementId);
	}
	
	/**
	 * Creates a print text element.
	 * 
	 * @param text the template text that the element will use
	 * @param originator
	 */
	public JRTemplatePrintText(JRTemplateText text, PrintElementOriginator originator)
	{
		super(text, originator);
	}

	/**
	 * @deprecated Replaced by {@link JRStyledTextUtil#getTruncatedText(JRPrintText)}.
	 */
	public String getText()
	{
		return JRStyledTextUtil.getInstance(DefaultJasperReportsContext.getInstance()).getTruncatedText(this);
	}
		
	/**
	 *
	 */
	public void setText(String text)
	{
		this.text = text;
		//this.truncatedText = null;
	}

	public Integer getTextTruncateIndex()
	{
		return textTruncateIndex;
	}

	public void setTextTruncateIndex(Integer textTruncateIndex)
	{
		this.textTruncateIndex = textTruncateIndex;
		//this.truncatedText = null;
	}

	public String getTextTruncateSuffix()
	{
		return textTruncateSuffix;
	}

	public void setTextTruncateSuffix(String textTruncateSuffix)
	{
		this.textTruncateSuffix = textTruncateSuffix;
		//this.truncatedText = null;
	}

	public short[] getLineBreakOffsets()
	{
		return lineBreakOffsets;
	}

	public void setLineBreakOffsets(short[] lineBreakOffsets)
	{
		this.lineBreakOffsets = lineBreakOffsets;
	}

	public String getFullText()
	{
		String fullText = this.text;
		if (textTruncateIndex == null && textTruncateSuffix != null)
		{
			fullText += textTruncateSuffix;
		}
		return fullText;
	}

	public String getOriginalText()
	{
		return text;
	}
	
	/**
	 * @deprecated Replaced by {@link JRStyledTextUtil#getStyledText(JRPrintText, JRStyledTextAttributeSelector)}.
	 */
	public JRStyledText getStyledText(JRStyledTextAttributeSelector attributeSelector)
	{
		return JRStyledTextUtil.getInstance(DefaultJasperReportsContext.getInstance()).getStyledText(this, attributeSelector);
	}

	public JRStyledText getFullStyledText(JRStyledTextAttributeSelector attributeSelector)
	{
		if (getFullText() == null)
		{
			return null;
		}

		return 
			JRStyledTextParser.getInstance().getStyledText(
				attributeSelector.getStyledTextAttributes(this), 
				getFullText(), 
				!JRCommonText.MARKUP_NONE.equals(getMarkup()),
				JRStyledTextAttributeSelector.getTextLocale(this)
				);
	}
	
	/**
	 *
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 *
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 *
	 */
	public float getLineSpacingFactor()
	{
		return lineSpacingFactor;
	}
		
	/**
	 *
	 */
	public void setLineSpacingFactor(float lineSpacingFactor)
	{
		this.lineSpacingFactor = lineSpacingFactor;
	}

	/**
	 *
	 */
	public float getLeadingOffset()
	{
		return leadingOffset;
	}
		
	/**
	 *
	 */
	public void setLeadingOffset(float leadingOffset)
	{
		this.leadingOffset = leadingOffset;
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return ((JRTemplateText)this.template).getHorizontalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return ((JRTemplateText)this.template).getOwnHorizontalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalTextAlign(HorizontalTextAlignEnum)}.
	 */
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 * @deprecated Replaced by {@link #getVerticalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return ((JRTemplateText)this.template).getVerticalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalTextAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return ((JRTemplateText)this.template).getOwnVerticalAlignmentValue();
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalTextAlign(VerticalTextAlignEnum)}.
	 */
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignment)
	{
		throw new UnsupportedOperationException();
	}
		
	/**
	 *
	 */
	public HorizontalTextAlignEnum getHorizontalTextAlign()
	{
		return ((JRTemplateText)this.template).getHorizontalTextAlign();
	}
		
	/**
	 *
	 */
	public HorizontalTextAlignEnum getOwnHorizontalTextAlign()
	{
		return ((JRTemplateText)this.template).getOwnHorizontalTextAlign();
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
		return ((JRTemplateText)this.template).getVerticalTextAlign();
	}
		
	/**
	 *
	 */
	public VerticalTextAlignEnum getOwnVerticalTextAlign()
	{
		return ((JRTemplateText)this.template).getOwnVerticalTextAlign();
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
		return ((JRTemplateText)this.template).getRotationValue();
	}
		
	/**
	 *
	 */
	public RotationEnum getOwnRotationValue()
	{
		return ((JRTemplateText)this.template).getOwnRotationValue();
	}
		
	/**
	 *
	 */
	public void setRotation(RotationEnum rotation)
	{
		throw new UnsupportedOperationException();
	}
		
	
	/**
	 *
	 */
	public RunDirectionEnum getRunDirectionValue()
	{
		return this.runDirectionValue;
	}

	/**
	 *
	 */
	public void setRunDirection(RunDirectionEnum runDirectionValue)
	{
		this.runDirectionValue = runDirectionValue;
	}
	/**
	 *
	 */
	public float getTextHeight()
	{
		return textHeight;
	}
		
	/**
	 *
	 */
	public void setTextHeight(float textHeight)
	{
		this.textHeight = textHeight;
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
		return ((JRTemplateText)template).getMarkup();
	}
		
	public String getOwnMarkup()
	{
		return ((JRTemplateText)template).getOwnMarkup();
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
		return ((JRTemplateText)template).getLineBox();
	}
		
	/**
	 *
	 */
	public JRParagraph getParagraph()
	{
		return ((JRTemplateText)template).getParagraph();
	}
		
	/**
	 * @deprecated
	 */
	public JRFont getFont()
	{
		return (JRTemplateText)template;
	}
		
	/**
	 * @deprecated
	 */
	public void setFont(JRFont font)
	{
	}

	/**
	 *
	 */
	public void setTextFormat(TextFormat textFormat)
	{
		this.textFormat = textFormat;
	}
		
	/**
	 *
	 */
	public String getAnchorName()
	{
		return anchorName;
	}
		
	/**
	 *
	 */
	public void setAnchorName(String anchorName)
	{
		this.anchorName = anchorName;
	}
		
	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return ((JRTemplateText)this.template).getHyperlinkTypeValue();
	}
		
	/**
	 *
	 */
	public void setHyperlinkType(HyperlinkTypeEnum hyperlinkType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public HyperlinkTargetEnum getHyperlinkTargetValue()
	{
		return ((JRTemplateText)this.template).getHyperlinkTargetValue();
	}
		
	/**
	 *
	 */
	public void setHyperlinkTarget(HyperlinkTargetEnum hyperlinkTarget)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public String getLinkTarget()
	{
		return ((JRTemplateText)template).getLinkTarget();
	}
		
	/**
	 *
	 */
	public void setLinkTarget(String linkTarget)
	{
	}
	/**
	 *
	 */
	public void setLinkTarget(byte hyperlinkTarget)
	{
	}

	/**
	 *
	 */
	public String getHyperlinkReference()
	{
		return hyperlinkReference;
	}
		
	/**
	 *
	 */
	public void setHyperlinkReference(String hyperlinkReference)
	{
		this.hyperlinkReference = hyperlinkReference;
	}
		
	/**
	 *
	 */
	public String getHyperlinkAnchor()
	{
		return hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public void setHyperlinkAnchor(String hyperlinkAnchor)
	{
		this.hyperlinkAnchor = hyperlinkAnchor;
	}
		
	/**
	 *
	 */
	public Integer getHyperlinkPage()
	{
		return hyperlinkPage;
	}
		
	/**
	 *
	 */
	public void setHyperlinkPage(Integer hyperlinkPage)
	{
		this.hyperlinkPage = hyperlinkPage;
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	public void setBookmarkLevel(int bookmarkLevel)
	{
		this.bookmarkLevel = bookmarkLevel;
	}

	/**
	 *
	 */
	public String getFontName()
	{
		return ((JRTemplateText)template).getFontName();
	}

	/**
	 *
	 */
	public String getOwnFontName()
	{
		return ((JRTemplateText)template).getOwnFontName();
	}

	/**
	 *
	 */
	public void setFontName(String fontName)
	{
	}


	/**
	 *
	 */
	public boolean isBold()
	{
		return ((JRTemplateText)template).isBold();
	}

	/**
	 *
	 */
	public Boolean isOwnBold()
	{
		return ((JRTemplateText)template).isOwnBold();
	}

	/**
	 *
	 */
	public void setBold(boolean isBold)
	{
	}

	/**
	 * Alternative setBold method which allows also to reset
	 * the "own" isBold property.
	 */
	public void setBold(Boolean isBold)
	{
	}


	/**
	 *
	 */
	public boolean isItalic()
	{
		return ((JRTemplateText)template).isItalic();
	}

	/**
	 *
	 */
	public Boolean isOwnItalic()
	{
		return ((JRTemplateText)template).isOwnItalic();
	}

	/**
	 *
	 */
	public void setItalic(boolean isItalic)
	{
	}

	/**
	 * Alternative setItalic method which allows also to reset
	 * the "own" isItalic property.
	 */
	public void setItalic(Boolean isItalic)
	{
	}

	/**
	 *
	 */
	public boolean isUnderline()
	{
		return ((JRTemplateText)template).isUnderline();
	}

	/**
	 *
	 */
	public Boolean isOwnUnderline()
	{
		return ((JRTemplateText)template).isOwnUnderline();
	}

	/**
	 *
	 */
	public void setUnderline(boolean isUnderline)
	{
	}

	/**
	 * Alternative setUnderline method which allows also to reset
	 * the "own" isUnderline property.
	 */
	public void setUnderline(Boolean isUnderline)
	{
	}

	/**
	 *
	 */
	public boolean isStrikeThrough()
	{
		return ((JRTemplateText)template).isStrikeThrough();
	}

	/**
	 *
	 */
	public Boolean isOwnStrikeThrough()
	{
		return ((JRTemplateText)template).isOwnStrikeThrough();
	}

	/**
	 *
	 */
	public void setStrikeThrough(boolean isStrikeThrough)
	{
		setStrikeThrough(isStrikeThrough ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setStrikeThrough method which allows also to reset
	 * the "own" isStrikeThrough property.
	 */
	public void setStrikeThrough(Boolean isStrikeThrough)
	{
	}

	/**
	 *
	 */
	public float getFontsize()
	{
		return ((JRTemplateText)template).getFontsize();
	}

	/**
	 *
	 */
	public Float getOwnFontsize()
	{
		return ((JRTemplateText)template).getOwnFontsize();
	}

	/**
	 * Method which allows also to reset the "own" size property.
	 */
	public void setFontSize(Float fontSize)
	{
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
	public void setFontSize(int fontSize)
	{
		setFontSize((float)fontSize);
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	public void setFontSize(Integer fontSize)
	{
		setFontSize(fontSize == null ? null : fontSize.floatValue());
	}

	/**
	 *
	 */
	public String getPdfFontName()
	{
		return ((JRTemplateText)template).getPdfFontName();
	}

	/**
	 *
	 */
	public String getOwnPdfFontName()
	{
		return ((JRTemplateText)template).getOwnPdfFontName();
	}

	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName)
	{
	}


	/**
	 *
	 */
	public String getPdfEncoding()
	{
		return ((JRTemplateText)template).getPdfEncoding();
	}

	/**
	 *
	 */
	public String getOwnPdfEncoding()
	{
		return ((JRTemplateText)template).getOwnPdfEncoding();
	}

	/**
	 *
	 */
	public void setPdfEncoding(String pdfEncoding)
	{
	}


	/**
	 *
	 */
	public boolean isPdfEmbedded()
	{
		return ((JRTemplateText)template).isPdfEmbedded();
	}

	/**
	 *
	 */
	public Boolean isOwnPdfEmbedded()
	{
		return ((JRTemplateText)template).isOwnPdfEmbedded();
	}

	/**
	 *
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
	}

	/**
	 * Alternative setPdfEmbedded method which allows also to reset
	 * the "own" isPdfEmbedded property.
	 */
	public void setPdfEmbedded(Boolean isPdfEmbedded)
	{
	}


	public String getValueClassName()
	{
		return  textFormat == null ? ((JRTemplateText) template).getValueClassName() : textFormat.getValueClassName();
	}

	public String getPattern()
	{
		return textFormat == null ? ((JRTemplateText) template).getPattern() : textFormat.getPattern();
	}

	public String getFormatFactoryClass()
	{
		return textFormat == null ? ((JRTemplateText) template).getFormatFactoryClass() : textFormat.getFormatFactoryClass();
	}

	public String getLocaleCode()
	{
		return textFormat == null ? ((JRTemplateText) template).getLocaleCode() : textFormat.getLocaleCode();
	}

	public String getTimeZoneId()
	{
		return textFormat == null ? ((JRTemplateText) template).getTimeZoneId() : textFormat.getTimeZoneId();
	}

	
	public JRPrintHyperlinkParameters getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}

	
	public void setHyperlinkParameters(JRPrintHyperlinkParameters hyperlinkParameters)
	{
		this.hyperlinkParameters = hyperlinkParameters;
	}

	public String getLinkType()
	{
		return ((JRTemplateText) template).getLinkType();
	}

	public void setLinkType(String type)
	{
	}

	
	public String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
	}

	
	public void setHyperlinkTooltip(String hyperlinkTooltip)
	{
		this.hyperlinkTooltip = hyperlinkTooltip;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte runDirection;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			runDirectionValue = RunDirectionEnum.getByValue(runDirection);
		}
	}

	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		visitor.visit(this, arg);
	}
	
	@Override
	public void writeVirtualized(VirtualizationOutput out) throws IOException
	{
		super.writeVirtualized(out);
		
		int flags = 0;
		boolean hasAnchor = anchorName != null || bookmarkLevel != JRAnchor.NO_BOOKMARK;
		boolean hasHyperlink = hyperlinkReference != null || hyperlinkAnchor != null
				|| hyperlinkPage != null || hyperlinkTooltip != null || hyperlinkParameters != null;
		boolean hasTrunc = textTruncateIndex != null || textTruncateSuffix != null;
		boolean hasLineBreakOffsets = lineBreakOffsets != null;
		boolean zeroLineBreakOffsets = lineBreakOffsets != null && lineBreakOffsets.length == 0;
		boolean hasValue = !(text == null ? value == null : (value instanceof String && text.equals(value)));
		
		if (hasAnchor)
		{
			flags |= SERIALIZATION_FLAG_ANCHOR;
		}
		if (hasHyperlink)
		{
			flags |= SERIALIZATION_FLAG_HYPERLINK;
		}
		if (hasTrunc)
		{
			flags |= SERIALIZATION_FLAG_TRUNCATION;
		}
		if (hasLineBreakOffsets)
		{
			flags |= SERIALIZATION_FLAG_LINE_BREAK_OFFSETS;
		}
		if (zeroLineBreakOffsets)
		{
			flags |= SERIALIZATION_FLAG_ZERO_LINE_BREAK_OFFSETS;
		}
		if (hasValue)
		{
			flags |= SERIALIZATION_FLAG_HAS_VALUE;
		}
		if (runDirectionValue == RunDirectionEnum.RTL)
		{
			flags |= SERIALIZATION_FLAG_RTL;
		}
		
		out.writeByte(flags);
		
		out.writeJRObject(text);
		if (hasValue)
		{
			out.writeJRObject(value);
		}
		
		//FIXME these usually repeat, keep in memory?
		out.writeFloat(lineSpacingFactor);
		out.writeFloat(leadingOffset);
		out.writeFloat(textHeight);

		if (hasTrunc)
		{
			out.writeJRObject(textTruncateIndex);
			out.writeJRObject(textTruncateSuffix);
		}
		
		if (hasLineBreakOffsets && !zeroLineBreakOffsets)
		{
			out.writeIntCompressed(lineBreakOffsets.length);
			for (short offset : lineBreakOffsets)
			{
				out.writeIntCompressed(offset);
			}
		}
		
		if (hasAnchor)
		{
			out.writeJRObject(anchorName);
			out.writeIntCompressed(bookmarkLevel);
		}

		if (hasHyperlink)
		{
			out.writeJRObject(hyperlinkReference);
			out.writeJRObject(hyperlinkAnchor);
			out.writeJRObject(hyperlinkPage);
			out.writeJRObject(hyperlinkTooltip);
			out.writeJRObject(hyperlinkParameters);
		}
	}

	@Override
	public void readVirtualized(VirtualizationInput in) throws IOException
	{
		super.readVirtualized(in);
		
		int flags = in.readUnsignedByte();
		text = (String) in.readJRObject();
		
		if ((flags & SERIALIZATION_FLAG_HAS_VALUE) != 0)
		{
			value = in.readJRObject();
		}
		else
		{
			value = text;
		}
		
		lineSpacingFactor = in.readFloat();
		leadingOffset = in.readFloat();
		textHeight = in.readFloat();
		
		if ((flags & SERIALIZATION_FLAG_TRUNCATION) != 0)
		{
			textTruncateIndex = (Integer) in.readJRObject();
			textTruncateSuffix = (String) in.readJRObject();
		}
		
		if ((flags & SERIALIZATION_FLAG_LINE_BREAK_OFFSETS) != 0)
		{
			if ((flags & SERIALIZATION_FLAG_ZERO_LINE_BREAK_OFFSETS) != 0)
			{
				lineBreakOffsets = JRPrintText.ZERO_LINE_BREAK_OFFSETS;
			}
			else
			{
				int offsetCount = in.readIntCompressed();
				lineBreakOffsets = new short[offsetCount];
				for (int i = 0; i < offsetCount; i++)
				{
					lineBreakOffsets[i] = (short) in.readIntCompressed();
				}
			}
		}
		
		if ((flags & SERIALIZATION_FLAG_ANCHOR) != 0)
		{
			anchorName = (String) in.readJRObject();
			bookmarkLevel = in.readIntCompressed();
		}
		else
		{
			bookmarkLevel = JRAnchor.NO_BOOKMARK;
		}

		if ((flags & SERIALIZATION_FLAG_HYPERLINK) != 0)
		{
			hyperlinkReference = (String) in.readJRObject();
			hyperlinkAnchor = (String) in.readJRObject();
			hyperlinkPage = (Integer) in.readJRObject();
			hyperlinkTooltip = (String) in.readJRObject();
			hyperlinkParameters = (JRPrintHyperlinkParameters) in.readJRObject();
		}
		
		runDirectionValue = (flags & SERIALIZATION_FLAG_RTL) != 0 ? RunDirectionEnum.RTL : RunDirectionEnum.LTR;
		
		PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID;
	}
	
}
