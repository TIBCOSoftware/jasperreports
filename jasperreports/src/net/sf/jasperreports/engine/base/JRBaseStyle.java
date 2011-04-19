/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
import java.io.Serializable;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRPenUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseStyle implements JRStyle, Serializable, JRChangeEventsSupport
{

	/**
	 *
	 */
	private static final long serialVersionUID = 10001;// too late to replace this now
	
	public static final String PROPERTY_BACKCOLOR = "backcolor";
	
	public static final String PROPERTY_BLANK_WHEN_NULL = "blankWhenNull";
	
	public static final String PROPERTY_BOLD = "bold";
	
	public static final String PROPERTY_FILL = "fill";
	
	public static final String PROPERTY_FONT_NAME = "fontName";
	
	public static final String PROPERTY_FONT_SIZE = "fontSize";
	
	public static final String PROPERTY_FORECOLOR = "forecolor";
	
	public static final String PROPERTY_HORIZONTAL_ALIGNMENT = "horizontalAlignment";
	
	public static final String PROPERTY_ITALIC = "italic";
	
	/**
	 * @deprecated Replaced by {@link JRBaseParagraph#PROPERTY_LINE_SPACING}
	 */
	public static final String PROPERTY_LINE_SPACING = "lineSpacing";
	
	public static final String PROPERTY_MODE = "mode";
	
	public static final String PROPERTY_PATTERN = "pattern";
	
	public static final String PROPERTY_PDF_EMBEDDED = "pdfEmbedded";
	
	public static final String PROPERTY_PDF_ENCODING = "pdfEncoding";
	
	public static final String PROPERTY_PDF_FONT_NAME = "pdfFontName";
	
	public static final String PROPERTY_RADIUS = "radius";
	
	public static final String PROPERTY_ROTATION = "rotation";
	
	public static final String PROPERTY_SCALE_IMAGE = "scaleImage";
	
	public static final String PROPERTY_STRIKE_THROUGH = "strikeThrough";
	
	/**
	 * @deprecated Replaced by {@link #PROPERTY_MARKUP}
	 */
	public static final String PROPERTY_IS_STYLED_TEXT = "isStyledText";
	
	public static final String PROPERTY_MARKUP = "markup";
	
	public static final String PROPERTY_UNDERLINE = "underline";
	
	public static final String PROPERTY_VERTICAL_ALIGNMENT = "verticalAlignment";


	/**
	 *
	 */
	protected JRDefaultStyleProvider defaultStyleProvider;
	protected JRStyle parentStyle;
	protected String parentStyleNameReference;

	/**
	 *
	 */
	protected String name;
	protected boolean isDefault;

	protected Byte positionType;
	protected Byte stretchType;
	protected ModeEnum modeValue;
	protected Color forecolor;
	protected Color backcolor;

	protected JRPen linePen;
	protected FillEnum fillValue;

	protected Integer radius;

	protected ScaleImageEnum scaleImageValue;
	protected HorizontalAlignEnum horizontalAlignmentValue;
	protected VerticalAlignEnum verticalAlignmentValue;

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

	protected RotationEnum rotationValue;
	protected String markup;

	protected String pattern;
	protected Boolean isBlankWhenNull;

	protected JRConditionalStyle[] conditionalStyles;


	/**
	 *
	 */
	public JRBaseStyle()
	{
		linePen = new JRBasePen(this);
		lineBox = new JRBaseLineBox(this);
		paragraph = new JRBaseParagraph(this);
	}

	/**
	 *
	 */
	public JRBaseStyle(String name)
	{
		this.name = name;

		linePen = new JRBasePen(this);
		lineBox = new JRBaseLineBox(this);
		paragraph = new JRBaseParagraph(this);
	}

	/**
	 *
	 */
	public JRBaseStyle(JRStyle style, JRAbstractObjectFactory factory)
	{
		name = style.getName();
		
		factory.setStyle(new JRStyleSetter()
		{
			public void setStyle(JRStyle aStyle)
			{
				setParentStyle(aStyle);
			}

			public void setStyleNameReference(String name)
			{
				parentStyleNameReference = name;
			}
		}, style);
		
		isDefault = style.isDefault();

		modeValue = style.getOwnModeValue();
		forecolor = style.getOwnForecolor();
		backcolor = style.getOwnBackcolor();

		linePen = style.getLinePen().clone(this);
		fillValue = style.getOwnFillValue();

		radius = style.getOwnRadius();

		scaleImageValue = style.getOwnScaleImageValue();
		horizontalAlignmentValue = style.getOwnHorizontalAlignmentValue();
		verticalAlignmentValue = style.getOwnVerticalAlignmentValue();

		lineBox = style.getLineBox().clone(this);
		paragraph = style.getParagraph().clone(this);

		rotationValue = style.getOwnRotationValue();
		markup = style.getOwnMarkup();

		pattern = style.getOwnPattern();

		fontName = style.getOwnFontName();
		isBold = style.isOwnBold();
		isItalic = style.isOwnItalic();
		isUnderline = style.isOwnUnderline();
		isStrikeThrough = style.isOwnStrikeThrough();
		fontSize = style.getOwnFontSize();
		pdfFontName = style.getOwnPdfFontName();
		pdfEncoding = style.getOwnPdfEncoding();
		isPdfEmbedded = style.isOwnPdfEmbedded();
		
		isBlankWhenNull = style.isOwnBlankWhenNull();

		JRConditionalStyle[] condStyles = style.getConditionalStyles();
		if (condStyles != null && condStyles.length > 0) {
			this.conditionalStyles = new JRConditionalStyle[condStyles.length];
			for (int i = 0; i < condStyles.length; i++) {
				this.conditionalStyles[i] = factory.getConditionalStyle(condStyles[i], this);
			}
		}
	}

	protected void setParentStyle(JRStyle parentStyle)
	{
		this.parentStyle = parentStyle;
		checkCircularParent();
	}
	
	protected void checkCircularParent()
	{
		for(JRStyle ancestor = parentStyle; ancestor != null; ancestor = ancestor.getStyle())
		{
			if (ancestor == this)
			{
				throw new JRRuntimeException("Circular dependency detected for style " + getName());
			}
		}
	}


	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return parentStyle;
	}

	/**
	 *
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Changes the name of this style.
	 * <p/>
	 * Note that this method is mostly meant to be used internally.
	 * Use cautiously as it might have unexpected consequences.
	 * 
	 * @param newName the new name
	 */
	public void rename(String newName)
	{
		this.name = newName;
	}
	
	/**
	 *
	 */
	public boolean isDefault()
	{
		return isDefault;
	}

	/**
	 *
	 */
	public Color getForecolor()
	{
		return JRStyleResolver.getForecolor(this);
	}

	/**
	 *
	 */
	public Color getOwnForecolor()
	{
		return forecolor;
	}

	public Color getBackcolor()
	{
		return JRStyleResolver.getBackcolor(this);
	}

	public Color getOwnBackcolor()
	{
		return backcolor;
	}

	public JRPen getLinePen()
	{
		return linePen;
	}

	/**
	 * @deprecated Replaced by {@link #getFillValue()}
	 */
	public Byte getFill()
	{
		return getFillValue() == null ? null : getFillValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnFillValue()}
	 */
	public Byte getOwnFill()
	{
		return getOwnFillValue() == null ? null : getOwnFillValue().getValueByte();
	}

	public FillEnum getFillValue()
	{
		return JRStyleResolver.getFillValue(this);
	}

	public FillEnum getOwnFillValue()
	{
		return fillValue;
	}

	public Integer getRadius()
	{
		return JRStyleResolver.getRadius(this);
	}

	public Integer getOwnRadius()
	{
		return radius;
	}

	/**
	 * @deprecated Replaced by {@link #getScaleImageValue()}.
	 */
	public Byte getScaleImage()
	{
		return getScaleImageValue() == null ? null : getScaleImageValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnScaleImageValue()}.
	 */
	public Byte getOwnScaleImage()
	{
		return getOwnScaleImageValue() == null ? null : getOwnScaleImageValue().getValueByte();
	}

	/**
	 * 
	 */
	public ScaleImageEnum getScaleImageValue()
	{
		return JRStyleResolver.getScaleImageValue(this);
	}

	/**
	 * 
	 */
	public ScaleImageEnum getOwnScaleImageValue()
	{
		return this.scaleImageValue;
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignmentValue()}.
	 */
	public Byte getHorizontalAlignment()
	{
		return getHorizontalAlignmentValue() == null ? null : getHorizontalAlignmentValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalAlignmentValue()}.
	 */
	public Byte getOwnHorizontalAlignment()
	{
		return getOwnHorizontalAlignmentValue() == null ? null : getOwnHorizontalAlignmentValue().getValueByte();
	}

	public HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return JRStyleResolver.getHorizontalAlignmentValue(this);
	}

	public HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return horizontalAlignmentValue;
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalAlignmentValue()}.
	 */
	public Byte getVerticalAlignment()
	{
		return getVerticalAlignmentValue() == null ? null : getVerticalAlignmentValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnVerticalAlignmentValue()}.
	 */
	public Byte getOwnVerticalAlignment()
	{
		return getOwnVerticalAlignmentValue() == null ? null : getOwnVerticalAlignmentValue().getValueByte();
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
	 * @deprecated Replaced by {@link #getRotationValue()}.
	 */
	public Byte getRotation()
	{
		return getRotationValue() == null ? null : getRotationValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnRotationValue()}.
	 */
	public Byte getOwnRotation()
	{
		return getOwnRotationValue() == null? null : getOwnRotationValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #setRotation(RotationEnum)}.
	 */
	public void setRotation(byte rotation)
	{
		setRotation(RotationEnum.getByValue(rotation));
	}

	/**
	 * @deprecated Replaced by {@link #setRotation(RotationEnum)}.
	 */
	public void setRotation(Byte rotation)
	{
		setRotation(RotationEnum.getByValue(rotation));
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
		getEventSupport().firePropertyChange(PROPERTY_ROTATION, old, this.rotationValue);
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#getLineSpacing()}.
	 */
	public Byte getLineSpacing()
	{
		return getParagraph().getLineSpacing() == null ? null : getParagraph().getLineSpacing().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#getOwnLineSpacing()}.
	 */
	public Byte getOwnLineSpacing()
	{
		return getParagraph().getOwnLineSpacing() == null ? null : getParagraph().getOwnLineSpacing().getValueByte();
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
	 * @deprecated Replaced by {@link #getMarkup()}
	 */
	public Boolean isStyledText()
	{
		String mkp = getMarkup();
		return JRCommonText.MARKUP_STYLED_TEXT.equals(mkp) ? Boolean.TRUE : (mkp == null ? null : Boolean.FALSE);
	}

	/**
	 * @deprecated Replaced by {@link #getOwnMarkup()}
	 */
	public Boolean isOwnStyledText()
	{
		String mkp = getOwnMarkup();
		return JRCommonText.MARKUP_STYLED_TEXT.equals(mkp) ? Boolean.TRUE : (mkp == null ? null : Boolean.FALSE);
	}

	public String getMarkup()
	{
		return JRStyleResolver.getMarkup(this);
	}

	public String getOwnMarkup()
	{
		return markup;
	}

	public Boolean isBlankWhenNull()
	{
		return JRStyleResolver.isBlankWhenNull(this);
	}

	public Boolean isOwnBlankWhenNull()
	{
		return isBlankWhenNull;
	}


	public String getFontName()
	{
		return JRStyleResolver.getFontName(this);
	}

	public String getOwnFontName()
	{
		return fontName;
	}

	public Boolean isBold()
	{
		return JRStyleResolver.isBold(this);
	}

	public Boolean isOwnBold()
	{
		return isBold;
	}

	public Boolean isItalic()
	{
		return JRStyleResolver.isItalic(this);
	}

	public Boolean isOwnItalic()
	{
		return isItalic;
	}

	public Boolean isUnderline()
	{
		return JRStyleResolver.isUnderline(this);
	}

	public Boolean isOwnUnderline()
	{
		return isUnderline;
	}

	public Boolean isStrikeThrough()
	{
		return JRStyleResolver.isStrikeThrough(this);
	}

	public Boolean isOwnStrikeThrough()
	{
		return isStrikeThrough;
	}

	public Integer getFontSize()
	{
		return JRStyleResolver.getFontSize(this);
	}

	public Integer getOwnFontSize()
	{
		return fontSize;
	}

	public String getPdfFontName()
	{
		return JRStyleResolver.getPdfFontName(this);
	}

	public String getOwnPdfFontName()
	{
		return pdfFontName;
	}

	public String getPdfEncoding()
	{
		return JRStyleResolver.getPdfEncoding(this);
	}

	public String getOwnPdfEncoding()
	{
		return pdfEncoding;
	}

	public Boolean isPdfEmbedded()
	{
		return JRStyleResolver.isPdfEmbedded(this);
	}

	public Boolean isOwnPdfEmbedded()
	{
		return isPdfEmbedded;
	}

	public String getPattern()
	{
		return JRStyleResolver.getPattern(this);
	}

	public String getOwnPattern()
	{
		return pattern;
	}

	/**
	 * @deprecated Replaced by {@link #getModeValue()}.
	 */
	public Byte getMode()
	{
		return getModeValue() == null ? null : getModeValue().getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnModeValue()}.
	 */
	public Byte getOwnMode()
	{
		return getOwnModeValue() == null ? null : getOwnModeValue().getValueByte();
	}

	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getModeValue(this);
	}

	/**
	 *
	 */
	public ModeEnum getOwnModeValue()
	{
		return modeValue;
	}

	/**
	 *
	 */
	public void setForecolor(Color forecolor)
	{
		Object old = this.forecolor;
		this.forecolor = forecolor;
		getEventSupport().firePropertyChange(PROPERTY_FORECOLOR, old, this.forecolor);
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		Object old = this.backcolor;
		this.backcolor = backcolor;
		getEventSupport().firePropertyChange(PROPERTY_BACKCOLOR, old, this.backcolor);
	}

	/**
	 * @deprecated Replaced by {@link #setMode(ModeEnum)}.
	 */
	public void setMode(byte mode)
	{
		setMode(ModeEnum.getByValue(mode));
	}

	/**
	 * @deprecated Replaced by {@link #setMode(ModeEnum)}.
	 */
	public void setMode(Byte mode)
	{
		setMode(ModeEnum.getByValue(mode));
	}

	/**
	 *
	 */
	public void setMode(ModeEnum modeValue)
	{
		Object old = this.modeValue;
		this.modeValue = modeValue;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_MODE, old, this.modeValue);
	}

	/**
	 * @deprecated Replaced by {@link #setFill(FillEnum)}.
	 */
	public void setFill(byte fill)
	{
		setFill(FillEnum.getByValue(fill));
	}

	/**
	 * @deprecated Replaced by {@link #setFill(FillEnum)}.
	 */
	public void setFill(Byte fill)
	{
		setFill(FillEnum.getByValue(fill));
	}

	/**
	 *
	 */
	public void setFill(FillEnum fillValue)
	{
		Object old = this.fillValue;
		this.fillValue = fillValue;
		getEventSupport().firePropertyChange(PROPERTY_FILL, old, this.fillValue);
	}

	/**
	 *
	 */
	public void setRadius(int radius)
	{
		setRadius(Integer.valueOf(radius));
	}

	/**
	 *
	 */
	public void setRadius(Integer radius)
	{
		Object old = this.radius;
		this.radius = radius;
		getEventSupport().firePropertyChange(PROPERTY_RADIUS, old, this.radius);
	}

	/**
	 * @deprecated Replaced by {@link #setScaleImage(ScaleImageEnum)}.
	 */
	public void setScaleImage(byte scaleImage)
	{
		setScaleImage(ScaleImageEnum.getByValue(scaleImage));
	}

	/**
	 * @deprecated Replaced by {@link #setScaleImage(ScaleImageEnum)}.
	 */
	public void setScaleImage(Byte scaleImage)
	{
		setScaleImage(ScaleImageEnum.getByValue(scaleImage));
	}

	/**
	 *
	 */
	public void setScaleImage(ScaleImageEnum scaleImageValue)
	{
		Object old = this.scaleImageValue;
		this.scaleImageValue = scaleImageValue;
		getEventSupport().firePropertyChange(PROPERTY_SCALE_IMAGE, old, this.scaleImageValue);
	}

	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(HorizontalAlignEnum)}.
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
		setHorizontalAlignment(HorizontalAlignEnum.getByValue(horizontalAlignment));
	}

	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(HorizontalAlignEnum)}.
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
		setHorizontalAlignment(HorizontalAlignEnum.getByValue(horizontalAlignment));
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(HorizontalAlignEnum horizontalAlignmentValue)
	{
		Object old = this.horizontalAlignmentValue;
		this.horizontalAlignmentValue = horizontalAlignmentValue;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_ALIGNMENT, old, this.horizontalAlignmentValue);
	}

	/**
	 * @deprecated Replaced by {@link #setVerticalAlignment(VerticalAlignEnum)}.
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
		setVerticalAlignment(VerticalAlignEnum.getByValue(verticalAlignment));
	}

	/**
	 * @deprecated Replaced by {@link #setVerticalAlignment(VerticalAlignEnum)}.
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
		setVerticalAlignment(VerticalAlignEnum.getByValue(verticalAlignment));
	}

	/**
	 *
	 */
	public void setVerticalAlignment(VerticalAlignEnum verticalAlignmentValue)
	{
		Object old = this.verticalAlignmentValue;
		this.verticalAlignmentValue = verticalAlignmentValue;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_ALIGNMENT, old, this.verticalAlignmentValue);
	}

	/**
	 *
	 */
	public void setFontName(String fontName)
	{
		Object old = this.fontName;
		this.fontName = fontName;
		getEventSupport().firePropertyChange(PROPERTY_FONT_NAME, old, this.fontName);
	}

	/**
	 *
	 */
	public void setBold(boolean bold)
	{
		setBold(bold ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setBold(Boolean bold)
	{
		Object old = this.isBold;
		this.isBold = bold;
		getEventSupport().firePropertyChange(PROPERTY_BOLD, old, this.isBold);
	}

	/**
	 *
	 */
	public void setItalic(boolean italic)
	{
		setItalic(italic ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setItalic(Boolean italic)
	{
		Object old = this.isItalic;
		this.isItalic = italic;
		getEventSupport().firePropertyChange(PROPERTY_ITALIC, old, this.isItalic);
	}

	/**
	 *
	 */
	public void setPdfEmbedded(boolean pdfEmbedded)
	{
		setPdfEmbedded(pdfEmbedded ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setPdfEmbedded(Boolean pdfEmbedded)
	{
		Object old = this.isPdfEmbedded;
		this.isPdfEmbedded = pdfEmbedded;
		getEventSupport().firePropertyChange(PROPERTY_PDF_EMBEDDED, old, this.isPdfEmbedded);
	}

	/**
	 *
	 */
	public void setStrikeThrough(boolean strikeThrough)
	{
		setStrikeThrough(strikeThrough ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setStrikeThrough(Boolean strikeThrough)
	{
		Object old = this.isStrikeThrough;
		this.isStrikeThrough = strikeThrough;
		getEventSupport().firePropertyChange(PROPERTY_STRIKE_THROUGH, old, this.isStrikeThrough);
	}

	/**
	 * @deprecated Replaced by {@link #setMarkup(String)}
	 */
	public void setStyledText(boolean styledText)
	{
		setStyledText(styledText ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * @deprecated Replaced by {@link #setMarkup(String)}
	 */
	public void setStyledText(Boolean styledText)
	{
		if (styledText == null)
		{
			setMarkup(null);
		}
		else
		{
			setMarkup(styledText.booleanValue() ? JRCommonText.MARKUP_STYLED_TEXT : JRCommonText.MARKUP_NONE);
		}
	}

	/**
	 *
	 */
	public void setMarkup(String markup)
	{
		Object old = this.markup;
		this.markup = markup;
		getEventSupport().firePropertyChange(PROPERTY_MARKUP, old, this.markup);
	}

	/**
	 *
	 */
	public void setBlankWhenNull(boolean isBlankWhenNull)
	{
		setBlankWhenNull(isBlankWhenNull ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setBlankWhenNull(Boolean isBlankWhenNull)
	{
		Object old = this.isBlankWhenNull;
		this.isBlankWhenNull = isBlankWhenNull;
		getEventSupport().firePropertyChange(PROPERTY_BLANK_WHEN_NULL, old, this.isBlankWhenNull);
	}

	/**
	 *
	 */
	public void setUnderline(boolean underline)
	{
		setUnderline(underline ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setUnderline(Boolean underline)
	{
		Object old = this.isUnderline;
		this.isUnderline = underline;
		getEventSupport().firePropertyChange(PROPERTY_UNDERLINE, old, this.isUnderline);
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#setLineSpacing(LineSpacingEnum)}.
	 */
	public void setLineSpacing(byte lineSpacing)
	{
		getParagraph().setLineSpacing(LineSpacingEnum.getByValue(lineSpacing));
	}

	/**
	 * @deprecated Replaced by {@link JRParagraph#setLineSpacing(LineSpacingEnum)}.
	 */
	public void setLineSpacing(Byte lineSpacing)
	{
		getParagraph().setLineSpacing(LineSpacingEnum.getByValue(lineSpacing));
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
	public void setPattern(String pattern)
	{
		Object old = this.pattern;
		this.pattern = pattern;
		getEventSupport().firePropertyChange(PROPERTY_PATTERN, old, this.pattern);
	}

	/**
	 *
	 */
	public void setPdfEncoding(String pdfEncoding)
	{
		Object old = this.pdfEncoding;
		this.pdfEncoding = pdfEncoding;
		getEventSupport().firePropertyChange(PROPERTY_PDF_ENCODING, old, this.pdfEncoding);
	}

	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName)
	{
		Object old = this.pdfFontName;
		this.pdfFontName = pdfFontName;
		getEventSupport().firePropertyChange(PROPERTY_PDF_FONT_NAME, old, this.pdfFontName);
	}

	/**
	 *
	 */
	public void setFontSize(int fontSize)
	{
		setFontSize(Integer.valueOf(fontSize));
	}

	/**
	 *
	 */
	public void setFontSize(Integer fontSize)
	{
		Object old = this.fontSize;
		this.fontSize = fontSize;
		getEventSupport().firePropertyChange(PROPERTY_FONT_SIZE, old, this.fontSize);
	}

	/**
	 *
	 */
	public JRConditionalStyle[] getConditionalStyles()
	{
		return conditionalStyles;
	}

	public String getStyleNameReference()
	{
		return parentStyleNameReference;
	}
	
	/**
	 *
	 */
	public Float getDefaultLineWidth()
	{
		return null;
	}
	
	/**
	 *
	 */
	public Color getDefaultLineColor()
	{
		return getForecolor();
	}

	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

		
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte mode;
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
	private Byte pen;
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
	private Byte scaleImage;
	/**
	 * @deprecated
	 */
	private Byte fill;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			modeValue = ModeEnum.getByValue(mode);
			horizontalAlignmentValue = HorizontalAlignEnum.getByValue(horizontalAlignment);
			verticalAlignmentValue = VerticalAlignEnum.getByValue(verticalAlignment);
			rotationValue = RotationEnum.getByValue(rotation);
			lineSpacingValue = LineSpacingEnum.getByValue(lineSpacing);
			scaleImageValue = ScaleImageEnum.getByValue(scaleImage);
			fillValue = FillEnum.getByValue(fill);
			
			mode = null;
			horizontalAlignment = null;
			verticalAlignment = null;
			rotation = null;
			lineSpacing = null;
			scaleImage = null;
			fill = null;
		}

		if (linePen == null)
		{
			linePen = new JRBasePen(this);
			JRPenUtil.setLinePenFromPen(pen, linePen);
			pen = null;
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
	
	public Object clone()
	{
		try
		{
			JRBaseStyle clone = (JRBaseStyle) super.clone();
			clone.lineBox = lineBox == null ? null : lineBox.clone(clone);
			clone.linePen = linePen == null ? null : linePen.clone(clone);
			clone.paragraph = paragraph == null ? null : paragraph.clone(clone);
			
			if (conditionalStyles != null)
			{
				clone.conditionalStyles = new JRConditionalStyle[conditionalStyles.length];
				for (int i = 0; i < conditionalStyles.length; i++)
				{
					JRConditionalStyle style = conditionalStyles[i];
					JRConditionalStyle styleClone = (JRConditionalStyle) style.clone();
					clone.conditionalStyles[i] = styleClone;
				}
			}
			
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
}
