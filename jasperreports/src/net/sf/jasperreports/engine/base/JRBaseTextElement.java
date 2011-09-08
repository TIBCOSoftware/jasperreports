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

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This class provides functionality common to text elements. It provides implementation for the methods described
 * in <tt>JRTextElement</tt>.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseTextElement extends JRBaseElement implements JRTextElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	/**
	 *
	 */
	protected HorizontalAlignEnum horizontalAlignmentValue;
	protected VerticalAlignEnum verticalAlignmentValue;
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
	protected Integer fontSize;
	protected String pdfFontName;
	protected String pdfEncoding;
	protected Boolean isPdfEmbedded;

	
	/**
	 * Initializes properties that are specific to text elements. Common properties are initialized by its
	 * parent constructor.
	 * @param textElement an element whose properties are copied to this element. Usually it is a
	 * {@link net.sf.jasperreports.engine.design.JRDesignTextElement} that must be transformed into an
	 * <tt>JRBaseTextElement</tt> at compile time.
	 * @param factory a factory used in the compile process
	 */
	protected JRBaseTextElement(JRTextElement textElement, JRBaseObjectFactory factory)
	{
		super(textElement, factory);

		horizontalAlignmentValue = textElement.getOwnHorizontalAlignmentValue();
		verticalAlignmentValue = textElement.getOwnVerticalAlignmentValue();
		rotationValue = textElement.getOwnRotationValue();
		markup = textElement.getOwnMarkup();

		lineBox = textElement.getLineBox().clone(this);
		paragraph = textElement.getParagraph().clone(this);

		fontName = textElement.getOwnFontName();
		isBold = textElement.isOwnBold();
		isItalic = textElement.isOwnItalic();
		isUnderline = textElement.isOwnUnderline();
		isStrikeThrough = textElement.isOwnStrikeThrough();
		fontSize = textElement.getOwnFontSize();
		pdfFontName = textElement.getOwnPdfFontName();
		pdfEncoding = textElement.getOwnPdfEncoding();
		isPdfEmbedded = textElement.isOwnPdfEmbedded();
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
		Object old = this.horizontalAlignmentValue;
		this.horizontalAlignmentValue = horizontalAlignmentValue;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT, old, this.horizontalAlignmentValue);
	}

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
		Object old = this.verticalAlignmentValue;
		this.verticalAlignmentValue = verticalAlignmentValue;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT, old, this.verticalAlignmentValue);
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
	 *
	 */
	public void setRotation(RotationEnum rotationValue)
	{
		Object old = this.rotationValue;
		this.rotationValue = rotationValue;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_ROTATION, old, this.rotationValue);
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
		Object old = this.markup;
		this.markup = markup;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_MARKUP, old, this.markup);
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
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
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
		Object old = this.fontName;
		this.fontName = fontName;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_FONT_NAME, old, this.fontName);
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
		Object old = this.isBold;
		this.isBold = isBold;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_BOLD, old, this.isBold);
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
		Object old = this.isItalic;
		this.isItalic = isItalic;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_ITALIC, old, this.isItalic);
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
		Object old = this.isUnderline;
		this.isUnderline = isUnderline;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_UNDERLINE, old, this.isUnderline);
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
		Object old = this.isStrikeThrough;
		this.isStrikeThrough = isStrikeThrough;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_STRIKE_THROUGH, old, this.isStrikeThrough);
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
		Object old = this.fontSize;
		this.fontSize = fontSize;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_FONT_SIZE, old, this.fontSize);
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
		Object old = this.pdfFontName;
		this.pdfFontName = pdfFontName;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_PDF_FONT_NAME, old, this.pdfFontName);
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
		Object old = this.pdfEncoding;
		this.pdfEncoding = pdfEncoding;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_PDF_ENCODING, old, this.pdfEncoding);
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
		Object old = this.isPdfEmbedded;
		this.isPdfEmbedded = isPdfEmbedded;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_PDF_EMBEDDED, old, this.isPdfEmbedded);
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
	public Object clone() 
	{
		JRBaseTextElement clone = (JRBaseTextElement)super.clone();
		
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
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			horizontalAlignmentValue = HorizontalAlignEnum.getByValue(horizontalAlignment);
			verticalAlignmentValue = VerticalAlignEnum.getByValue(verticalAlignment);
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

		if (paragraph == null)
		{
			paragraph = new JRBaseParagraph(this);
			paragraph.setLineSpacing(lineSpacingValue);
			lineSpacingValue = null;
		}
	}
}
