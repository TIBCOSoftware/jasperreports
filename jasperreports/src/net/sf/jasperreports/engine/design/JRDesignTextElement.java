/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.engine.design;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;


/**
 * This class provides functionality common to design text elements. It provides implementation for the methods described
 * in <tt>JRTextElement</tt> and setters for text element attributes that can only be modified at design time.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class JRDesignTextElement extends JRDesignElement implements JRTextElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	
	/**
	 *
	 */
	protected HorizontalTextAlignEnum horizontalTextAlign;
	protected VerticalTextAlignEnum verticalTextAlign;
	protected RotationEnum rotationValue;
	protected String markup;

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


	/**
	 * Creates a new text element.
	 */
	protected JRDesignTextElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
		
		lineBox = new JRBaseLineBox(this);
		paragraph = new JRBaseParagraph(this);
	}


	@Override
	public ModeEnum getModeValue()
	{
		return getStyleResolver().getMode(this, ModeEnum.TRANSPARENT);
	}


	/**
	 * @deprecated Replaced by {@link #getHorizontalTextAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getHorizontalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalTextAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getOwnHorizontalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setHorizontalTextAlign(HorizontalTextAlignEnum)}.
	 */
	@Override
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue)
	{
		setHorizontalTextAlign(net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalTextAlignEnum(horizontalAlignmentValue));
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalTextAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getVerticalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #getOwnVerticalTextAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getOwnVerticalTextAlign());
	}
		
	/**
	 * @deprecated Replaced by {@link #setVerticalTextAlign(VerticalTextAlignEnum)}.
	 */
	@Override
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue)
	{
		setVerticalTextAlign(net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalTextAlignEnum(verticalAlignmentValue));
	}

	@Override
	public HorizontalTextAlignEnum getHorizontalTextAlign()
	{
		return getStyleResolver().getHorizontalTextAlign(this);
	}
		
	@Override
	public HorizontalTextAlignEnum getOwnHorizontalTextAlign()
	{
		return horizontalTextAlign;
	}
		
	@Override
	public void setHorizontalTextAlign(HorizontalTextAlignEnum horizontalTextAlign)
	{
		Object old = this.horizontalTextAlign;
		this.horizontalTextAlign = horizontalTextAlign;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_HORIZONTAL_TEXT_ALIGNMENT, old, this.horizontalTextAlign);
	}

	@Override
	public VerticalTextAlignEnum getVerticalTextAlign()
	{
		return getStyleResolver().getVerticalTextAlign(this);
	}
		
	@Override
	public VerticalTextAlignEnum getOwnVerticalTextAlign()
	{
		return verticalTextAlign;
	}
		
	@Override
	public void setVerticalTextAlign(VerticalTextAlignEnum verticalTextAlign)
	{
		Object old = this.verticalTextAlign;
		this.verticalTextAlign = verticalTextAlign;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_VERTICAL_TEXT_ALIGNMENT, old, this.verticalTextAlign);
	}

	@Override
	public RotationEnum getRotationValue()
	{
		return getStyleResolver().getRotationValue(this);
	}

	@Override
	public RotationEnum getOwnRotationValue()
	{
		return this.rotationValue;
	}

	@Override
	public void setRotation(RotationEnum rotationValue)
	{
		Object old = this.rotationValue;
		this.rotationValue = rotationValue;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_ROTATION, old, this.rotationValue);
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#getLineSpacing()}.
	 */
	@Override
	public LineSpacingEnum getLineSpacingValue()
	{
		return getParagraph().getLineSpacing();
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#getOwnLineSpacing()}.
	 */
	@Override
	public LineSpacingEnum getOwnLineSpacingValue()
	{
		return getParagraph().getOwnLineSpacing();
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#setLineSpacing(LineSpacingEnum)}.
	 */
	@Override
	public void setLineSpacing(LineSpacingEnum lineSpacing)
	{
		getParagraph().setLineSpacing(lineSpacing);
	}

	@Override
	public String getMarkup()
	{
		return getStyleResolver().getMarkup(this);
	}
		
	@Override
	public String getOwnMarkup()
	{
		return markup;
	}

	@Override
	public void setMarkup(String markup)
	{
		Object old = this.markup;
		this.markup = markup;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_MARKUP, old, this.markup);
	}

	@Override
	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	@Override
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
	 * Sets a <tt>JRFont</tt> object containing all font properties for this text element
	 * @deprecated
	 */
	public void setFont(JRFont font)
	{
		setFontName(font.getOwnFontName());
		setBold(font.isOwnBold());
		setItalic(font.isOwnItalic());
		setUnderline(font.isOwnUnderline());
		setStrikeThrough(font.isOwnStrikeThrough());
		setFontSize(font.getOwnFontsize());
		setPdfFontName(font.getOwnPdfFontName());
		setPdfEncoding(font.getOwnPdfEncoding());
		setPdfEmbedded(font.isOwnPdfEmbedded());
	}

	@Override
	public String getFontName()
	{
		return getStyleResolver().getFontName(this);
	}

	@Override
	public String getOwnFontName()
	{
		return fontName;
	}

	@Override
	public void setFontName(String fontName)
	{
		Object old = this.fontName;
		this.fontName = fontName;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_FONT_NAME, old, this.fontName);
	}


	@Override
	public boolean isBold()
	{
		return getStyleResolver().isBold(this);
	}

	@Override
	public Boolean isOwnBold()
	{
		return isBold;
	}

	@Override
	public void setBold(boolean isBold)
	{
		setBold(isBold ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setBold method which allows also to reset
	 * the "own" isBold property.
	 */
	@Override
	public void setBold(Boolean isBold)
	{
		Object old = this.isBold;
		this.isBold = isBold;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_BOLD, old, this.isBold);
	}


	@Override
	public boolean isItalic()
	{
		return getStyleResolver().isItalic(this);
	}

	@Override
	public Boolean isOwnItalic()
	{
		return isItalic;
	}

	@Override
	public void setItalic(boolean isItalic)
	{
		setItalic(isItalic ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setItalic method which allows also to reset
	 * the "own" isItalic property.
	 */
	@Override
	public void setItalic(Boolean isItalic)
	{
		Object old = this.isItalic;
		this.isItalic = isItalic;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_ITALIC, old, this.isItalic);
	}

	@Override
	public boolean isUnderline()
	{
		return getStyleResolver().isUnderline(this);
	}

	@Override
	public Boolean isOwnUnderline()
	{
		return isUnderline;
	}

	@Override
	public void setUnderline(boolean isUnderline)
	{
		setUnderline(isUnderline ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setUnderline method which allows also to reset
	 * the "own" isUnderline property.
	 */
	@Override
	public void setUnderline(Boolean isUnderline)
	{
		Object old = this.isUnderline;
		this.isUnderline = isUnderline;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_UNDERLINE, old, this.isUnderline);
	}

	@Override
	public boolean isStrikeThrough()
	{
		return getStyleResolver().isStrikeThrough(this);
	}

	@Override
	public Boolean isOwnStrikeThrough()
	{
		return isStrikeThrough;
	}

	@Override
	public void setStrikeThrough(boolean isStrikeThrough)
	{
		setStrikeThrough(isStrikeThrough ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setStrikeThrough method which allows also to reset
	 * the "own" isStrikeThrough property.
	 */
	@Override
	public void setStrikeThrough(Boolean isStrikeThrough)
	{
		Object old = this.isStrikeThrough;
		this.isStrikeThrough = isStrikeThrough;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_STRIKE_THROUGH, old, this.isStrikeThrough);
	}

	@Override
	public float getFontsize()
	{
		return getStyleResolver().getFontsize(this);
	}

	@Override
	public Float getOwnFontsize()
	{
		return fontsize;
	}

	/**
	 * Method which allows also to reset the "own" size property.
	 */
	@Override
	public void setFontSize(Float fontSize)
	{
		Object old = this.fontsize;
		this.fontsize = fontSize;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_FONT_SIZE, old, this.fontsize);
	}

	/**
	 * @deprecated Replaced by {@link #getFontsize()}.
	 */
	@Override
	public int getFontSize()
	{
		return (int)getFontsize();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnFontsize()}.
	 */
	@Override
	public Integer getOwnFontSize()
	{
		return fontsize == null ? null : fontsize.intValue();
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	@Override
	public void setFontSize(int fontSize)
	{
		setFontSize((float)fontSize);
	}

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	@Override
	public void setFontSize(Integer fontSize)
	{
		setFontSize(fontSize == null ? null : fontSize.floatValue());
	}

	@Override
	public String getPdfFontName()
	{
		return getStyleResolver().getPdfFontName(this);
	}

	@Override
	public String getOwnPdfFontName()
	{
		return pdfFontName;
	}

	@Override
	public void setPdfFontName(String pdfFontName)
	{
		Object old = this.pdfFontName;
		this.pdfFontName = pdfFontName;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_PDF_FONT_NAME, old, this.pdfFontName);
	}


	@Override
	public String getPdfEncoding()
	{
		return getStyleResolver().getPdfEncoding(this);
	}

	@Override
	public String getOwnPdfEncoding()
	{
		return pdfEncoding;
	}

	@Override
	public void setPdfEncoding(String pdfEncoding)
	{
		Object old = this.pdfEncoding;
		this.pdfEncoding = pdfEncoding;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_PDF_ENCODING, old, this.pdfEncoding);
	}


	@Override
	public boolean isPdfEmbedded()
	{
		return getStyleResolver().isPdfEmbedded(this);
	}

	@Override
	public Boolean isOwnPdfEmbedded()
	{
		return isPdfEmbedded;
	}

	@Override
	public void setPdfEmbedded(boolean isPdfEmbedded)
	{
		setPdfEmbedded(isPdfEmbedded ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Alternative setPdfEmbedded method which allows also to reset
	 * the "own" isPdfEmbedded property.
	 */
	@Override
	public void setPdfEmbedded(Boolean isPdfEmbedded)
	{
		Object old = this.isPdfEmbedded;
		this.isPdfEmbedded = isPdfEmbedded;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_PDF_EMBEDDED, old, this.isPdfEmbedded);
	}

	@Override
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}

	@Override
	public Object clone() 
	{
		JRDesignTextElement clone = (JRDesignTextElement)super.clone();
		
		clone.lineBox = lineBox.clone(clone);
		clone.paragraph = paragraph.clone(clone);
		
		return clone;
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
	private Boolean isStyledText;
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
		
		if (isStyledText != null)
		{
			markup = isStyledText.booleanValue() ? JRCommonText.MARKUP_STYLED_TEXT : JRCommonText.MARKUP_NONE;
			isStyledText = null;
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
}
