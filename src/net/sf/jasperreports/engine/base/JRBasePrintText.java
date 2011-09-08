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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.PrintElementVisitor;
import net.sf.jasperreports.engine.fill.TextFormat;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePrintText extends JRBasePrintElement implements JRPrintText
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String text = "";
	protected Integer textTruncateIndex;
	protected short[] lineBreakOffsets;
	protected String textTruncateSuffix;
	protected transient String truncatedText;
	protected Object value;
	/**
	 * @deprecated No longer used.
	 */
	protected float lineSpacingFactor;
	/**
	 * @deprecated No longer used.
	 */
	protected float leadingOffset;
	protected HorizontalAlignEnum horizontalAlignmentValue;
	protected VerticalAlignEnum verticalAlignmentValue;
	protected RotationEnum rotationValue;
	protected RunDirectionEnum runDirectionValue = RunDirectionEnum.LTR;
	protected float textHeight;
	protected String markup;
	protected TextFormat textFormat;
	protected String anchorName;
	protected String linkType;
	protected String linkTarget;
	protected String hyperlinkReference;
	protected String hyperlinkAnchor;
	protected Integer hyperlinkPage;
	protected String hyperlinkTooltip;
	protected JRPrintHyperlinkParameters hyperlinkParameters;

	/**
	 * The bookmark level for the anchor associated with this field.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;

	/**
	 *
	 */
	protected JRLineBox lineBox;
	protected JRParagraph paragraph;

	protected String fontName;
	protected Boolean isBold;
	protected Boolean isItalic;
	protected Boolean isUnderline;
	protected Boolean isStrikeThrough;
	protected Integer fontSize;
	protected String pdfFontName;
	protected String pdfEncoding;
	protected Boolean isPdfEmbedded;

	protected String valueClassName;
	protected String pattern;
	protected String formatFactoryClass;
	protected String localeCode;
	protected String timeZoneId;
	
	/**
	 *
	 */
	public JRBasePrintText(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
		
		lineBox = new JRBaseLineBox(this);
		paragraph = new JRBaseParagraph(this);
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
		return JRStyleResolver.getHorizontalAlignmentValue(this);
	}
		
	public HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return horizontalAlignmentValue;
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(HorizontalAlignEnum horizontalAlignmentValue)
	{
		this. horizontalAlignmentValue =  horizontalAlignmentValue;
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
		return verticalAlignmentValue;
	}

	/**
	 *
	 */
	public void setVerticalAlignment(VerticalAlignEnum verticalAlignmentValue)
	{
		this.verticalAlignmentValue = verticalAlignmentValue;
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
		return rotationValue;
	}

	/**
	 *
	 */
	public void setRotation(RotationEnum rotationValue)
	{
		this.rotationValue = rotationValue;
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
		getParagraph().setLineSpacing(lineSpacing);
	}

	/**
	 *
	 */
	public String getMarkup()
	{
		return JRStyleResolver.getMarkup(this);
	}
		
	public String getOwnMarkup()
	{
		return markup;
	}

	/**
	 *
	 */
	public void setMarkup(String markup)
	{
		this.markup = markup;
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
	 *
	 */
	public void copyBox(JRLineBox lineBox)
	{
		this.lineBox = lineBox.clone(this);
	}

	/**
	 *
	 */
	public void copyParagraph(JRParagraph paragraph)
	{
		this.paragraph = paragraph.clone(this);
	}

	/**
	 * @deprecated
	 */
	public JRFont getFont()
	{
		return this;
	}

	/**
	 * @deprecated
	 */
	public void setFont(JRFont font)
	{
		fontName = font.getOwnFontName();
		isBold = font.isOwnBold();
		isItalic = font.isOwnItalic();
		isUnderline = font.isOwnUnderline();
		isStrikeThrough = font.isOwnStrikeThrough();
		fontSize = font.getOwnFontSize();
		pdfFontName = font.getOwnPdfFontName();
		pdfEncoding = font.getOwnPdfEncoding();
		isPdfEmbedded = font.isOwnPdfEmbedded();
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
		return JRHyperlinkHelper.getHyperlinkTypeValue(getLinkType());
	}
		
	/**
	 *
	 */
	public void setHyperlinkType(HyperlinkTypeEnum hyperlinkType)
	{
		setLinkType(JRHyperlinkHelper.getLinkType(hyperlinkType));
	}

	/**
	 *
	 */
	public HyperlinkTargetEnum getHyperlinkTargetValue()
	{
		return JRHyperlinkHelper.getHyperlinkTargetValue(getLinkTarget());
	}
		
	/**
	 *
	 */
	public void setHyperlinkTarget(HyperlinkTargetEnum hyperlinkTarget)
	{
		setLinkTarget(JRHyperlinkHelper.getLinkTarget(hyperlinkTarget));
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
		
	/**
	 *
	 */
	public void setHyperlinkPage(String hyperlinkPage)
	{
		this.hyperlinkPage = Integer.valueOf(hyperlinkPage);
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
		return JRStyleResolver.getFontName(this);
	}

	/**
	 *
	 */
	public String getOwnFontName()
	{
		return fontName;
	}

	/**
	 *
	 */
	public void setFontName(String fontName)
	{
		this.fontName = fontName;
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
		return isBold;
	}

	/**
	 *
	 */
	public void setBold(boolean isBold)
	{
		setBold(isBold ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setBold method which allows also to reset
	 * the "own" isBold property.
	 */
	public void setBold(Boolean isBold)
	{
		this.isBold = isBold;
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
		return isItalic;
	}

	/**
	 *
	 */
	public void setItalic(boolean isItalic)
	{
		setItalic(isItalic ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setItalic method which allows also to reset
	 * the "own" isItalic property.
	 */
	public void setItalic(Boolean isItalic)
	{
		this.isItalic = isItalic;
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
		return isUnderline;
	}

	/**
	 *
	 */
	public void setUnderline(boolean isUnderline)
	{
		setUnderline(isUnderline ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setUnderline method which allows also to reset
	 * the "own" isUnderline property.
	 */
	public void setUnderline(Boolean isUnderline)
	{
		this.isUnderline = isUnderline;
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
		return isStrikeThrough;
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
		this.isStrikeThrough = isStrikeThrough;
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
		return fontSize;
	}

	/**
	 *
	 */
	public void setFontSize(int fontSize)
	{
		setFontSize(Integer.valueOf(fontSize));
	}

	/**
	 * Alternative setSize method which allows also to reset
	 * the "own" size property.
	 */
	public void setFontSize(Integer fontSize)
	{
		this.fontSize = fontSize;
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
		return pdfFontName;
	}

	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName)
	{
		this.pdfFontName = pdfFontName;
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
		return pdfEncoding;
	}

	/**
	 *
	 */
	public void setPdfEncoding(String pdfEncoding)
	{
		this.pdfEncoding = pdfEncoding;
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
		return isPdfEmbedded;
	}

	/**
	 *
	 */
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
		setPdfEmbedded(isPdfEmbedded ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setPdfEmbedded method which allows also to reset
	 * the "own" isPdfEmbedded property.
	 */
	public void setPdfEmbedded(Boolean isPdfEmbedded)
	{
		this.isPdfEmbedded = isPdfEmbedded;
	}

	
	public String getPattern()
	{
		return pattern;
	}

	
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	
	public String getValueClassName()
	{
		return valueClassName;
	}

	
	public void setValueClassName(String valueClassName)
	{
		this.valueClassName = valueClassName;
	}

	
	public String getFormatFactoryClass()
	{
		return formatFactoryClass;
	}

	
	public void setFormatFactoryClass(String formatFactoryClass)
	{
		this.formatFactoryClass = formatFactoryClass;
	}

	
	public String getLocaleCode()
	{
		return localeCode;
	}

	
	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	
	public String getTimeZoneId()
	{
		return timeZoneId;
	}

	
	public void setTimeZoneId(String timeZoneId)
	{
		this.timeZoneId = timeZoneId;
	}

	
	public JRPrintHyperlinkParameters getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}

	
	public void setHyperlinkParameters(JRPrintHyperlinkParameters hyperlinkParameters)
	{
		this.hyperlinkParameters = hyperlinkParameters;
	}

	
	/**
	 * Adds a custom hyperlink parameter.
	 * 
	 * @param parameter the parameter to add
	 * @see #getHyperlinkParameters()
	 * @see JRPrintHyperlinkParameters#addParameter(JRPrintHyperlinkParameter)
	 */
	public void addHyperlinkParameter(JRPrintHyperlinkParameter parameter)
	{
		if (hyperlinkParameters == null)
		{
			hyperlinkParameters = new JRPrintHyperlinkParameters();
		}
		hyperlinkParameters.addParameter(parameter);
	}
	
	
	public String getLinkType()
	{
		return linkType;
	}


	
	public void setLinkType(String linkType)
	{
		this.linkType = linkType;
	}
	
	public String getLinkTarget()
	{
		return linkTarget;
	}


	
	public void setLinkTarget(String linkTarget)
	{
		this.linkTarget = linkTarget;
	}
	
	
	public String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
	}


	
	public void setHyperlinkTooltip(String hyperlinkTooltip)
	{
		this.hyperlinkTooltip = hyperlinkTooltip;
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}


	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte horizontalAlignment;
	/**
	 * @deprecated
	 */
	private Byte verticalAlignment;
	/**
	 * @deprecated
	 */
	private Byte rotation;
	/**
	 * @deprecated
	 */
	private Byte lineSpacing;
	/**
	 * @deprecated
	 */
	private LineSpacingEnum lineSpacingValue;
	/**
	 * @deprecated
	 */
	private Byte border;
	/**
	 * @deprecated
	 */
	private Byte topBorder;
	/**
	 * @deprecated
	 */
	private Byte leftBorder;
	/**
	 * @deprecated
	 */
	private Byte bottomBorder;
	/**
	 * @deprecated
	 */
	private Byte rightBorder;
	/**
	 * @deprecated
	 */
	private Color borderColor;
	/**
	 * @deprecated
	 */
	private Color topBorderColor;
	/**
	 * @deprecated
	 */
	private Color leftBorderColor;
	/**
	 * @deprecated
	 */
	private Color bottomBorderColor;
	/**
	 * @deprecated
	 */
	private Color rightBorderColor;
	/**
	 * @deprecated
	 */
	private Integer padding;
	/**
	 * @deprecated
	 */
	private Integer topPadding;
	/**
	 * @deprecated
	 */
	private Integer leftPadding;
	/**
	 * @deprecated
	 */
	private Integer bottomPadding;
	/**
	 * @deprecated
	 */
	private Integer rightPadding;
	/**
	 * @deprecated
	 */
	private Boolean isStyledText;
	/**
	 * @deprecated
	 */
	private byte hyperlinkType;
	/**
	 * @deprecated
	 */
	private byte hyperlinkTarget;
	/**
	 * @deprecated
	 */
	private byte runDirection;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			horizontalAlignmentValue = HorizontalAlignEnum.getByValue(horizontalAlignment);
			verticalAlignmentValue = VerticalAlignEnum.getByValue(verticalAlignment);
			rotationValue = RotationEnum.getByValue(rotation);
			runDirectionValue = RunDirectionEnum.getByValue(runDirection);
			lineSpacingValue = LineSpacingEnum.getByValue(lineSpacing);

			horizontalAlignment = null;
			verticalAlignment = null;
			rotation = null;
			lineSpacing = null;
		}
		
		if (lineBox == null)
		{
			lineBox = new JRBaseLineBox(this);
			JRBoxUtil.setToBox(
				border,
				topBorder,
				leftBorder,
				bottomBorder,
				rightBorder,
				borderColor,
				topBorderColor,
				leftBorderColor,
				bottomBorderColor,
				rightBorderColor,
				padding,
				topPadding,
				leftPadding,
				bottomPadding,
				rightPadding,
				lineBox
				);
			border = null;
			topBorder = null;
			leftBorder = null;
			bottomBorder = null;
			rightBorder = null;
			borderColor = null;
			topBorderColor = null;
			leftBorderColor = null;
			bottomBorderColor = null;
			rightBorderColor = null;
			padding = null;
			topPadding = null;
			leftPadding = null;
			bottomPadding = null;
			rightPadding = null;
		}

		if (isStyledText != null)
		{
			markup = isStyledText.booleanValue() ? JRCommonText.MARKUP_STYLED_TEXT : JRCommonText.MARKUP_NONE;
			isStyledText = null;
		}

		if (linkType == null)
		{
			 linkType = JRHyperlinkHelper.getLinkType(HyperlinkTypeEnum.getByValue(hyperlinkType));
		}

		if (linkTarget == null)
		{
			 linkTarget = JRHyperlinkHelper.getLinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
		}

		if (paragraph == null)
		{
			paragraph = new JRBaseParagraph(this);
			paragraph.setLineSpacing(lineSpacingValue);
			lineSpacingValue = null;
		}
	}

	public <T> void accept(PrintElementVisitor<T> visitor, T arg)
	{
		visitor.visit(this, arg);
	}
}
