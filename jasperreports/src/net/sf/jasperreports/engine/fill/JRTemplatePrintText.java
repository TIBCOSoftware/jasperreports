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

import java.io.IOException;
import java.io.ObjectInputStream;

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
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;


/**
 * Implementation of {@link net.sf.jasperreports.engine.JRPrintText} that uses
 * a {@link net.sf.jasperreports.engine.fill.JRTemplateText} instance to
 * store common attributes. 
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplatePrintText extends JRTemplatePrintElement implements JRPrintText
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private String text = "";
	private Integer textTruncateIndex;
	private String textTruncateSuffix;
	private short[] lineBreakOffsets;
	private transient String truncatedText;
	private Object value;
	/**
	 * @deprecated No longer used.
	 */
	private float lineSpacingFactor;
	/**
	 * @deprecated No longer used.
	 */
	private float leadingOffset;
	private RunDirectionEnum runDirectionValue;
	private float textHeight;
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
	 */
	public JRTemplatePrintText(JRTemplateText text, int sourceElementId)
	{
		super(text, sourceElementId);
	}

	/**
	 *
	 */
	public String getText()
	{
		if (truncatedText == null && text != null)
		{
			if (getTextTruncateIndex() == null)
			{
				truncatedText = text;
			}
			else
			{
				if (!JRCommonText.MARKUP_NONE.equals(getMarkup()))
				{
					truncatedText = JRStyledTextParser.getInstance().write(
							getFullStyledText(JRStyledTextAttributeSelector.ALL), 
							0, getTextTruncateIndex().intValue());
				}
				else
				{
					truncatedText = text.substring(0, getTextTruncateIndex().intValue());
				}
			}
			
			if (textTruncateSuffix != null)
			{
				truncatedText += textTruncateSuffix;
			}
		}
		return truncatedText;
	}
		
	/**
	 *
	 */
	public void setText(String text)
	{
		this.text = text;
		this.truncatedText = null;
	}

	public Integer getTextTruncateIndex()
	{
		return textTruncateIndex;
	}

	public void setTextTruncateIndex(Integer textTruncateIndex)
	{
		this.textTruncateIndex = textTruncateIndex;
		this.truncatedText = null;
	}

	public String getTextTruncateSuffix()
	{
		return textTruncateSuffix;
	}

	public void setTextTruncateSuffix(String textTruncateSuffix)
	{
		this.textTruncateSuffix = textTruncateSuffix;
		this.truncatedText = null;
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
	
	public JRStyledText getStyledText(JRStyledTextAttributeSelector attributeSelector)
	{
		if (getText() == null)
		{
			return null;
		}
		
		return 
			JRStyledTextParser.getInstance().getStyledText(
				attributeSelector.getStyledTextAttributes(this), 
				getText(), 
				!JRCommonText.MARKUP_NONE.equals(getMarkup()),
				JRStyledTextAttributeSelector.getTextLocale(this)
				);
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
	 * @deprecated No longer used.
	 */
	public float getLineSpacingFactor()
	{
		return lineSpacingFactor;
	}
		
	/**
	 * @deprecated No longer used.
	 */
	public void setLineSpacingFactor(float lineSpacingFactor)
	{
		this.lineSpacingFactor = lineSpacingFactor;
	}

	/**
	 * @deprecated No longer used.
	 */
	public float getLeadingOffset()
	{
		return leadingOffset;
	}
		
	/**
	 * @deprecated No longer used.
	 */
	public void setLeadingOffset(float leadingOffset)
	{
		this.leadingOffset = leadingOffset;
	}

	/**
	 *
	 */
	public HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return ((JRTemplateText)this.template).getHorizontalAlignmentValue();
	}
		
	/**
	 *
	 */
	public HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return ((JRTemplateText)this.template).getOwnHorizontalAlignmentValue();
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
		return ((JRTemplateText)this.template).getVerticalAlignmentValue();
	}
		
	/**
	 *
	 */
	public VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return ((JRTemplateText)this.template).getOwnVerticalAlignmentValue();
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
	public int getFontSize()
	{
		return ((JRTemplateText)template).getFontSize();
	}

	/**
	 *
	 */
	public Integer getOwnFontSize()
	{
		return ((JRTemplateText)template).getOwnFontSize();
	}

	/**
	 *
	 */
	public void setFontSize(int fontSize)
	{
	}

	/**
	 * Alternative setSize method which allows also to reset
	 * the "own" size property.
	 */
	public void setFontSize(Integer fontSize)
	{
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
	
}
