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
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTextAlignment;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.ObjectUtils;


/**
 * Text element information shared by multiple print text objects.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @see JRTemplatePrintText
 */
public class JRTemplateText extends JRTemplateElement implements JRAlignment, JRTextAlignment, JRFont, JRCommonText, TextFormat
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private HorizontalTextAlignEnum horizontalTextAlign;
	private VerticalTextAlignEnum verticalTextAlign;
	private RotationEnum rotationValue;
	private String markup;
	private String linkType;
	private String linkTarget;

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
	protected Float fontsize;
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
	protected JRTemplateText(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JRStaticText staticText)
	{
		super(origin, defaultStyleProvider);
		
		setStaticText(staticText);
	}

	/**
	 *
	 */
	protected JRTemplateText(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider, JRTextField textField)
	{
		super(origin, defaultStyleProvider);
		
		setTextField(textField);
	}


	/**
	 * Creates a template text.
	 * 
	 * @param origin the origin of the elements that will use this template
	 * @param defaultStyleProvider the default style provider to use for
	 * this template
	 */
	public JRTemplateText(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		super(origin, defaultStyleProvider);
		
		lineBox = new JRBaseLineBox(this);
		paragraph = new JRBaseParagraph(this);
	}
	
	/**
	 *
	 */
	protected void setStaticText(JRStaticText staticText)
	{
		setTextElement(staticText);
	}

	/**
	 *
	 */
	protected void setTextField(JRTextField textField)
	{
		setTextElement(textField);

		setLinkType(textField.getLinkType());
		setLinkTarget(textField.getLinkTarget());
	}

	/**
	 *
	 */
	protected void setTextElement(JRTextElement textElement)
	{
		super.setElement(textElement);
		
		fontName = textElement.getOwnFontName();
		isBold = textElement.isOwnBold();
		isItalic = textElement.isOwnItalic();
		isUnderline = textElement.isOwnUnderline();
		isStrikeThrough = textElement.isOwnStrikeThrough();
		fontsize = textElement.getOwnFontsize();
		pdfFontName = textElement.getOwnPdfFontName();
		pdfEncoding = textElement.getOwnPdfEncoding();
		isPdfEmbedded = textElement.isOwnPdfEmbedded();

		horizontalTextAlign = textElement.getOwnHorizontalTextAlign();
		verticalTextAlign = textElement.getOwnVerticalTextAlign();
		rotationValue = textElement.getOwnRotationValue();
		markup = textElement.getOwnMarkup();
	}

	public void setTextFormat(TextFormat textFormat)
	{
		if (textFormat != null)
		{
			setValueClassName(textFormat.getValueClassName());
			setPattern(textFormat.getPattern());
			setFormatFactoryClass(textFormat.getFormatFactoryClass());
			setLocaleCode(textFormat.getLocaleCode());
			setTimeZoneId(textFormat.getTimeZoneId());
		}
	}

	/**
	 * Copies box attributes.
	 * 
	 * @param box the object to copy attributes from
	 */
	public void copyLineBox(JRLineBox box)
	{
		lineBox = box.clone(this);
	}

	/**
	 * Copies paragraph attributes.
	 * 
	 * @param prg the object to copy attributes from
	 */
	public void copyParagraph(JRParagraph prg)
	{
		paragraph = prg.clone(this);
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
		
	/**
	 *
	 */
	public HorizontalTextAlignEnum getOwnHorizontalTextAlign()
	{
		return horizontalTextAlign;
	}
		
	/**
	 *
	 */
	public void setHorizontalTextAlign(HorizontalTextAlignEnum horizontalTextAlign)
	{
		this.horizontalTextAlign = horizontalTextAlign;
	}

	/**
	 *
	 */
	public VerticalTextAlignEnum getVerticalTextAlign()
	{
		return JRStyleResolver.getVerticalTextAlign(this);
	}
		
	/**
	 *
	 */
	public VerticalTextAlignEnum getOwnVerticalTextAlign()
	{
		return verticalTextAlign;
	}
		
	/**
	 *
	 */
	public void setVerticalTextAlign(VerticalTextAlignEnum verticalTextAlign)
	{
		this.verticalTextAlign = verticalTextAlign;
	}

	/**
	 *
	 */
	public RotationEnum getRotationValue()
	{
		return JRStyleResolver.getRotationValue(this);
	}

	/**
	 *
	 */
	public RotationEnum getOwnRotationValue()
	{
		return this.rotationValue;
	}

	/**
	 * Sets the text rotation.
	 * 
	 * @param rotationValue one of
	 * 	<ul>
	 * 		<li>{@link RotationEnum#NONE}</li>
	 * 		<li>{@link RotationEnum#LEFT}</li>
	 * 		<li>{@link RotationEnum#RIGHT}</li>
	 * 		<li>{@link RotationEnum#UPSIDE_DOWN}</li>
	 * 	</ul>
	 * values, or <code>null</code> if this template
	 * should not specify a rotation attribute of its own
	 */
	public void setRotation(RotationEnum rotationValue)
	{
		this.rotationValue = rotationValue;
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
	 * Sets the text markup attribute.
	 * 
	 * @param markup the markup attribute
	 * @see #getMarkup()
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
	 * @deprecated
	 */
	public JRFont getFont()
	{
		return this;
	}

	
	/**
	 * Retrieves the hyperlink type for the element.
	 * <p>
	 * The actual hyperlink type is determined by {@link #getLinkType() getLinkType()}.
	 * This method can is used to determine whether the hyperlink type is one of the
	 * built-in types or a custom type. 
O	 * When hyperlink is of custom type, {@link HyperlinkTypeEnum#CUSTOM CUSTOM} is returned.
	 * </p>
	 * @return one of the hyperlink type constants
	 * @see #getLinkType()
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return JRHyperlinkHelper.getHyperlinkTypeValue(getLinkType());
	}

	/**
	 * @deprecated
	 */
	public byte getHyperlinkTarget()
	{
		return getHyperlinkTargetValue().getValue();
	}
	
	/**
	 * Retrieves the hyperlink target name for the element.
	 * <p>
	 * The actual hyperlink target name is determined by {@link #getLinkTarget() getLinkTarget()}.
	 * This method is used to determine whether the hyperlink target name is one of the
	 * built-in names or a custom one. 
	 * When hyperlink target has a custom name, {@link HyperlinkTargetEnum#CUSTOM CUSTOM} is returned.
	 * </p>
	 * @return one of the hyperlink target name constants
	 * @see #getLinkTarget()
	 */
	public HyperlinkTargetEnum getHyperlinkTargetValue()
	{
		return JRHyperlinkHelper.getHyperlinkTargetValue(getLinkTarget());
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
	public float getFontsize()
	{
		return JRStyleResolver.getFontsize(this);
	}

	/**
	 *
	 */
	public Float getOwnFontsize()
	{
		return fontsize;
	}

	/**
	 * Method which allows also to reset the "own" size property.
	 */
	public void setFontSize(Float fontSize)
	{
		this.fontsize = fontSize;
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
		return fontsize == null ? null : fontsize.intValue();
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

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return parentStyle;
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

	
	/**
	 * Returns the hyperlink type.
	 * <p>
	 * The type can be one of the built-in types
	 * (Reference, LocalAnchor, LocalPage, RemoteAnchor, RemotePage),
	 * or can be an arbitrary type.
	 * </p>
	 * @return the hyperlink type
	 */
	public String getLinkType()
	{
		return linkType;
	}


	/**
	 * Sets the hyperlink type.
	 * <p>
	 * The type can be one of the built-in types
	 * (Reference, LocalAnchor, LocalPage, RemoteAnchor, RemotePage),
	 * or can be an arbitrary type.
	 * </p>
	 * @param linkType the hyperlink type
	 */
	public void setLinkType(String linkType)
	{
		this.linkType = linkType;
	}
	
	/**
	 *
	 */
	protected void setLinkTarget(String linkTarget)
	{
		this.linkTarget = linkTarget;
	}

	
	/**
	 * Returns the hyperlink target name.
	 * <p>
	 * The target name can be one of the built-in names
	 * (Self, Blank, Top, Parent),
	 * or can be an arbitrary name.
	 * </p>
	 * @return the hyperlink type
	 */
	public String getLinkTarget()
	{
		return linkTarget;
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
	private net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue;
	/**
	 * @deprecated
	 */
	private net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue;
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
	private Integer fontSize;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			horizontalAlignmentValue = net.sf.jasperreports.engine.type.HorizontalAlignEnum.getByValue(horizontalAlignment);
			verticalAlignmentValue = net.sf.jasperreports.engine.type.VerticalAlignEnum.getByValue(verticalAlignment);
			rotationValue = RotationEnum.getByValue(rotation);
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

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_5_5_2)
		{
			fontsize = fontSize == null ? null : fontSize.floatValue();

			fontSize = null;
		}

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_6_0_2)
		{
			horizontalTextAlign = net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalTextAlignEnum(horizontalAlignmentValue);
			verticalTextAlign = net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalTextAlignEnum(verticalAlignmentValue);

			horizontalAlignmentValue = null;
			verticalAlignmentValue = null;
		}
	}

	public int getHashCode()
	{
		ObjectUtils.HashCode hash = ObjectUtils.hash();
		addTemplateHash(hash);
		hash.add(horizontalTextAlign);
		hash.add(verticalTextAlign);
		hash.add(rotationValue);
		hash.add(markup);
		hash.add(linkType);
		hash.add(linkTarget);
		hash.addIdentical(lineBox);
		hash.addIdentical(paragraph);
		hash.add(fontName);
		hash.add(isBold);
		hash.add(isItalic);
		hash.add(isUnderline);
		hash.add(isStrikeThrough);
		hash.add(fontsize);
		hash.add(pdfFontName);
		hash.add(pdfEncoding);
		hash.add(isPdfEmbedded);
		hash.add(valueClassName);
		hash.add(pattern);
		hash.add(formatFactoryClass);
		hash.add(localeCode);
		hash.add(timeZoneId);
		return hash.getHashCode();
	}

	public boolean isIdentical(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof JRTemplateText))
		{
			return false;
		}
		
		JRTemplateText template = (JRTemplateText) object;
		return templateIdentical(template)
				&& ObjectUtils.equals(horizontalTextAlign, template.horizontalTextAlign)
				&& ObjectUtils.equals(verticalTextAlign, template.verticalTextAlign)
				&& ObjectUtils.equals(rotationValue, template.rotationValue)
				&& ObjectUtils.equals(markup, template.markup)
				&& ObjectUtils.equals(linkType, template.linkType)
				&& ObjectUtils.equals(linkTarget, template.linkTarget)
				&& ObjectUtils.identical(lineBox, template.lineBox)
				&& ObjectUtils.identical(paragraph, template.paragraph)
				&& ObjectUtils.equals(fontName, template.fontName)
				&& ObjectUtils.equals(isBold, template.isBold)
				&& ObjectUtils.equals(isItalic, template.isItalic)
				&& ObjectUtils.equals(isUnderline, template.isUnderline)
				&& ObjectUtils.equals(isStrikeThrough, template.isStrikeThrough)
				&& ObjectUtils.equals(fontsize, template.fontsize)
				&& ObjectUtils.equals(pdfFontName, template.pdfFontName)
				&& ObjectUtils.equals(pdfEncoding, template.pdfEncoding)
				&& ObjectUtils.equals(isPdfEmbedded, template.isPdfEmbedded)
				&& ObjectUtils.equals(valueClassName, template.valueClassName)
				&& ObjectUtils.equals(pattern, template.pattern)
				&& ObjectUtils.equals(formatFactoryClass, template.formatFactoryClass)
				&& ObjectUtils.equals(localeCode, template.localeCode)
				&& ObjectUtils.equals(timeZoneId, template.timeZoneId);
	}
}
