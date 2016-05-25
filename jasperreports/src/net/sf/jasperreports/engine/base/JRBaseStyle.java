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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.engine.Deduplicable;
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
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.ObjectUtils;
import net.sf.jasperreports.engine.util.StyleResolver;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 */
public class JRBaseStyle implements JRStyle, Serializable, JRChangeEventsSupport, Deduplicable
{

	public static final String EXCEPTION_MESSAGE_KEY_CIRCULAR_DEPENDENCY = "engine.style.circular.dependency";
	
	/**
	 *
	 */
	private static final long serialVersionUID = 10001;// too late to replace this now
	
	public static final String PROPERTY_BACKCOLOR = "backcolor";
	
	public static final String PROPERTY_BLANK_WHEN_NULL = "isBlankWhenNull";
	
	public static final String PROPERTY_BOLD = "isBold";
	
	public static final String PROPERTY_FILL = "fill";
	
	public static final String PROPERTY_FONT_NAME = "fontName";
	
	public static final String PROPERTY_FONT_SIZE = "fontSize";
	
	public static final String PROPERTY_FORECOLOR = "forecolor";
	
	/**
	 * @deprecated Replaced by {@link #PROPERTY_HORIZONTAL_TEXT_ALIGNMENT} and {@link #PROPERTY_HORIZONTAL_IMAGE_ALIGNMENT}.
	 */
	public static final String PROPERTY_HORIZONTAL_ALIGNMENT = "horizontalAlignment";
	
	public static final String PROPERTY_HORIZONTAL_TEXT_ALIGNMENT = "horizontalTextAlignment";

	public static final String PROPERTY_HORIZONTAL_IMAGE_ALIGNMENT = "horizontalImageAlignment";

	public static final String PROPERTY_ITALIC = "isItalic";
	
	/**
	 * @deprecated Replaced by {@link JRBaseParagraph#PROPERTY_LINE_SPACING}
	 */
	public static final String PROPERTY_LINE_SPACING = "lineSpacing";
	
	public static final String PROPERTY_MODE = "mode";
	
	public static final String PROPERTY_PATTERN = "pattern";
	
	public static final String PROPERTY_PDF_EMBEDDED = "isPdfEmbedded";
	
	public static final String PROPERTY_PDF_ENCODING = "pdfEncoding";
	
	public static final String PROPERTY_PDF_FONT_NAME = "pdfFontName";
	
	public static final String PROPERTY_RADIUS = "radius";
	
	public static final String PROPERTY_ROTATION = "rotation";
	
	public static final String PROPERTY_SCALE_IMAGE = "scaleImage";
	
	public static final String PROPERTY_STRIKE_THROUGH = "isStrikeThrough";
	
	public static final String PROPERTY_MARKUP = "markup";
	
	public static final String PROPERTY_UNDERLINE = "isUnderline";
	
	/**
	 * @deprecated Replaced by {@link #PROPERTY_VERTICAL_TEXT_ALIGNMENT} and {@value #PROPERTY_VERTICAL_IMAGE_ALIGNMENT}.
	 */
	public static final String PROPERTY_VERTICAL_ALIGNMENT = "verticalAlignment";
	
	public static final String PROPERTY_VERTICAL_TEXT_ALIGNMENT = "verticalTextAlignment";
	
	public static final String PROPERTY_VERTICAL_IMAGE_ALIGNMENT = "verticalImageAlignment";


	/**
	 *
	 */
	protected final JRDefaultStyleProvider defaultStyleProvider;
	protected JRStyle parentStyle;
	protected String parentStyleNameReference;

	/**
	 *
	 */
	protected String name;
	protected boolean isDefault;

	protected Byte positionType;//FIXME not used
	protected Byte stretchType;//FIXME not used
	protected ModeEnum modeValue;
	protected Color forecolor;
	protected Color backcolor;

	protected JRPen linePen;
	protected FillEnum fillValue;

	protected Integer radius;

	protected ScaleImageEnum scaleImageValue;
	protected HorizontalTextAlignEnum horizontalTextAlign;
	protected VerticalTextAlignEnum verticalTextAlign;
	protected HorizontalImageAlignEnum horizontalImageAlign;
	protected VerticalImageAlignEnum verticalImageAlign;

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
		this((JRDefaultStyleProvider)null);
	}

	/**
	 *
	 */
	public JRBaseStyle(JRDefaultStyleProvider defaultStyleProvider)
	{
		this.defaultStyleProvider = defaultStyleProvider;
		
		linePen = new JRBasePen(this);
		lineBox = new JRBaseLineBox(this);
		paragraph = new JRBaseParagraph(this);
	}

	/**
	 *
	 */
	public JRBaseStyle(String name)
	{
		this((JRDefaultStyleProvider)null);
		
		this.name = name;
	}

	/**
	 *
	 */
	public JRBaseStyle(JRDefaultStyleProvider defaultStyleProvider, String name)
	{
		this(defaultStyleProvider);
		
		this.name = name;
	}

	/**
	 *
	 */
	public JRBaseStyle(JRStyle style, JRAbstractObjectFactory factory)
	{
		name = style.getName();
		
		defaultStyleProvider = factory.getDefaultStyleProvider();
		
		factory.setStyle(new JRStyleSetter()
		{
			@Override
			public void setStyle(JRStyle aStyle)
			{
				setParentStyle(aStyle);
			}

			@Override
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
		horizontalTextAlign = style.getOwnHorizontalTextAlign();
		verticalTextAlign = style.getOwnVerticalTextAlign();
		horizontalImageAlign = style.getOwnHorizontalImageAlign();
		verticalImageAlign = style.getOwnVerticalImageAlign();

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
		fontsize = style.getOwnFontsize();
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
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_CIRCULAR_DEPENDENCY,
						new Object[]{getName()});
			}
		}
	}


	@Override
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	/**
	 *
	 */
	protected StyleResolver getStyleResolver() 
	{
		if (getDefaultStyleProvider() != null)
		{
			return getDefaultStyleProvider().getStyleResolver();
		}
		
		return StyleResolver.getInstance();
	}

	@Override
	public JRStyle getStyle()
	{
		return parentStyle;
	}

	@Override
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
	
	@Override
	public boolean isDefault()
	{
		return isDefault;
	}

	@Override
	public Color getForecolor()
	{
		return getStyleResolver().getForecolor(this);
	}

	@Override
	public Color getOwnForecolor()
	{
		return forecolor;
	}

	@Override
	public Color getBackcolor()
	{
		return getStyleResolver().getBackcolor(this);
	}

	@Override
	public Color getOwnBackcolor()
	{
		return backcolor;
	}

	@Override
	public JRPen getLinePen()
	{
		return linePen;
	}

	@Override
	public FillEnum getFillValue()
	{
		return getStyleResolver().getFillValue(this);
	}

	@Override
	public FillEnum getOwnFillValue()
	{
		return fillValue;
	}

	@Override
	public Integer getRadius()
	{
		return getStyleResolver().getRadius(this);
	}

	@Override
	public Integer getOwnRadius()
	{
		return radius;
	}

	@Override
	public ScaleImageEnum getScaleImageValue()
	{
		return getStyleResolver().getScaleImageValue(this);
	}

	@Override
	public ScaleImageEnum getOwnScaleImageValue()
	{
		return this.scaleImageValue;
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalTextAlign()} and {@link #getHorizontalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getHorizontalTextAlign());
	}

	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalTextAlign()} and {@link #getOwnHorizontalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalAlignEnum(getOwnHorizontalTextAlign());
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalTextAlign()} and {@link #getVerticalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getVerticalTextAlign());
	}

	/**
	 * @deprecated Replaced by {@link #getOwnVerticalTextAlign()} and {@link #getOwnVerticalImageAlign()}.
	 */
	@Override
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue()
	{
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalAlignEnum(getOwnVerticalTextAlign());
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
	public HorizontalImageAlignEnum getHorizontalImageAlign()
	{
		return getStyleResolver().getHorizontalImageAlign(this);
	}
		
	@Override
	public HorizontalImageAlignEnum getOwnHorizontalImageAlign()
	{
		return horizontalImageAlign;
	}
		
	@Override
	public VerticalImageAlignEnum getVerticalImageAlign()
	{
		return getStyleResolver().getVerticalImageAlign(this);
	}
		
	@Override
	public VerticalImageAlignEnum getOwnVerticalImageAlign()
	{
		return verticalImageAlign;
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
		getEventSupport().firePropertyChange(PROPERTY_ROTATION, old, this.rotationValue);
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
	public Boolean isBlankWhenNull()
	{
		return getStyleResolver().isBlankWhenNull(this);
	}

	@Override
	public Boolean isOwnBlankWhenNull()
	{
		return isBlankWhenNull;
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
	public Boolean isBold()
	{
		return getStyleResolver().isBold(this);
	}

	@Override
	public Boolean isOwnBold()
	{
		return isBold;
	}

	@Override
	public Boolean isItalic()
	{
		return getStyleResolver().isItalic(this);
	}

	@Override
	public Boolean isOwnItalic()
	{
		return isItalic;
	}

	@Override
	public Boolean isUnderline()
	{
		return getStyleResolver().isUnderline(this);
	}

	@Override
	public Boolean isOwnUnderline()
	{
		return isUnderline;
	}

	@Override
	public Boolean isStrikeThrough()
	{
		return getStyleResolver().isStrikeThrough(this);
	}

	@Override
	public Boolean isOwnStrikeThrough()
	{
		return isStrikeThrough;
	}

	@Override
	public Float getFontsize()
	{
		return getStyleResolver().getFontsize(this);
	}

	@Override
	public Float getOwnFontsize()
	{
		return fontsize;
	}

	/**
	 * @deprecated Replaced by {@link #getFontsize()}.
	 */
	@Override
	public Integer getFontSize()
	{
		Float fontSize = getFontsize();
		return fontSize == null ? null : fontSize.intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getOwnFontsize()}.
	 */
	@Override
	public Integer getOwnFontSize()
	{
		return fontsize == null ? null : fontsize.intValue();
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
	public Boolean isPdfEmbedded()
	{
		return getStyleResolver().isPdfEmbedded(this);
	}

	@Override
	public Boolean isOwnPdfEmbedded()
	{
		return isPdfEmbedded;
	}

	@Override
	public String getPattern()
	{
		return getStyleResolver().getPattern(this);
	}

	@Override
	public String getOwnPattern()
	{
		return pattern;
	}

	@Override
	public ModeEnum getModeValue()
	{
		return getStyleResolver().getModeValue(this);
	}

	@Override
	public ModeEnum getOwnModeValue()
	{
		return modeValue;
	}

	@Override
	public void setForecolor(Color forecolor)
	{
		Object old = this.forecolor;
		this.forecolor = forecolor;
		getEventSupport().firePropertyChange(PROPERTY_FORECOLOR, old, this.forecolor);
	}

	@Override
	public void setBackcolor(Color backcolor)
	{
		Object old = this.backcolor;
		this.backcolor = backcolor;
		getEventSupport().firePropertyChange(PROPERTY_BACKCOLOR, old, this.backcolor);
	}

	@Override
	public void setMode(ModeEnum modeValue)
	{
		Object old = this.modeValue;
		this.modeValue = modeValue;
		getEventSupport().firePropertyChange(JRBaseStyle.PROPERTY_MODE, old, this.modeValue);
	}

	@Override
	public void setFill(FillEnum fillValue)
	{
		Object old = this.fillValue;
		this.fillValue = fillValue;
		getEventSupport().firePropertyChange(PROPERTY_FILL, old, this.fillValue);
	}

	@Override
	public void setRadius(int radius)
	{
		setRadius(Integer.valueOf(radius));
	}

	@Override
	public void setRadius(Integer radius)
	{
		Object old = this.radius;
		this.radius = radius;
		getEventSupport().firePropertyChange(PROPERTY_RADIUS, old, this.radius);
	}

	@Override
	public void setScaleImage(ScaleImageEnum scaleImageValue)
	{
		Object old = this.scaleImageValue;
		this.scaleImageValue = scaleImageValue;
		getEventSupport().firePropertyChange(PROPERTY_SCALE_IMAGE, old, this.scaleImageValue);
	}

	/**
	 * @deprecated Replaced by {@link #setHorizontalTextAlign(HorizontalTextAlignEnum)} and {@link #setHorizontalImageAlign(HorizontalImageAlignEnum)}.
	 */
	@Override
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignmentValue)
	{
		setHorizontalTextAlign(net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalTextAlignEnum(horizontalAlignmentValue));
		setHorizontalImageAlign(net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalImageAlignEnum(horizontalAlignmentValue));
	}

	/**
	 * @deprecated Replaced by {@link #setVerticalTextAlign(VerticalTextAlignEnum)} and {@link #setVerticalImageAlign(VerticalImageAlignEnum)}.
	 */
	@Override
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignmentValue)
	{
		setVerticalTextAlign(net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalTextAlignEnum(verticalAlignmentValue));
		setVerticalImageAlign(net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalImageAlignEnum(verticalAlignmentValue));
	}

	@Override
	public void setHorizontalImageAlign(HorizontalImageAlignEnum horizontalImageAlign)
	{
		HorizontalImageAlignEnum old = this.horizontalImageAlign;
		this.horizontalImageAlign = horizontalImageAlign;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_IMAGE_ALIGNMENT, old, this.horizontalImageAlign);
	}

	@Override
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalImageAlign)
	{
		VerticalImageAlignEnum old = this.verticalImageAlign;
		this.verticalImageAlign = verticalImageAlign;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_IMAGE_ALIGNMENT, old, this.verticalImageAlign);
	}

	@Override
	public void setHorizontalTextAlign(HorizontalTextAlignEnum horizontalTextAlign)
	{
		HorizontalTextAlignEnum old = this.horizontalTextAlign;
		this.horizontalTextAlign = horizontalTextAlign;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_TEXT_ALIGNMENT, old, this.horizontalTextAlign);
	}

	@Override
	public void setVerticalTextAlign(VerticalTextAlignEnum verticalTextAlign)
	{
		VerticalTextAlignEnum old = this.verticalTextAlign;
		this.verticalTextAlign = verticalTextAlign;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_TEXT_ALIGNMENT, old, this.verticalTextAlign);
	}

	@Override
	public void setFontName(String fontName)
	{
		Object old = this.fontName;
		this.fontName = fontName;
		getEventSupport().firePropertyChange(PROPERTY_FONT_NAME, old, this.fontName);
	}

	@Override
	public void setBold(boolean bold)
	{
		setBold(bold ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setBold(Boolean bold)
	{
		Object old = this.isBold;
		this.isBold = bold;
		getEventSupport().firePropertyChange(PROPERTY_BOLD, old, this.isBold);
	}

	@Override
	public void setItalic(boolean italic)
	{
		setItalic(italic ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setItalic(Boolean italic)
	{
		Object old = this.isItalic;
		this.isItalic = italic;
		getEventSupport().firePropertyChange(PROPERTY_ITALIC, old, this.isItalic);
	}

	@Override
	public void setPdfEmbedded(boolean pdfEmbedded)
	{
		setPdfEmbedded(pdfEmbedded ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setPdfEmbedded(Boolean pdfEmbedded)
	{
		Object old = this.isPdfEmbedded;
		this.isPdfEmbedded = pdfEmbedded;
		getEventSupport().firePropertyChange(PROPERTY_PDF_EMBEDDED, old, this.isPdfEmbedded);
	}

	@Override
	public void setStrikeThrough(boolean strikeThrough)
	{
		setStrikeThrough(strikeThrough ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setStrikeThrough(Boolean strikeThrough)
	{
		Object old = this.isStrikeThrough;
		this.isStrikeThrough = strikeThrough;
		getEventSupport().firePropertyChange(PROPERTY_STRIKE_THROUGH, old, this.isStrikeThrough);
	}

	@Override
	public void setMarkup(String markup)
	{
		Object old = this.markup;
		this.markup = markup;
		getEventSupport().firePropertyChange(PROPERTY_MARKUP, old, this.markup);
	}

	@Override
	public void setBlankWhenNull(boolean isBlankWhenNull)
	{
		setBlankWhenNull(isBlankWhenNull ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setBlankWhenNull(Boolean isBlankWhenNull)
	{
		Object old = this.isBlankWhenNull;
		this.isBlankWhenNull = isBlankWhenNull;
		getEventSupport().firePropertyChange(PROPERTY_BLANK_WHEN_NULL, old, this.isBlankWhenNull);
	}

	@Override
	public void setUnderline(boolean underline)
	{
		setUnderline(underline ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setUnderline(Boolean underline)
	{
		Object old = this.isUnderline;
		this.isUnderline = underline;
		getEventSupport().firePropertyChange(PROPERTY_UNDERLINE, old, this.isUnderline);
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
	public void setPattern(String pattern)
	{
		Object old = this.pattern;
		this.pattern = pattern;
		getEventSupport().firePropertyChange(PROPERTY_PATTERN, old, this.pattern);
	}

	@Override
	public void setPdfEncoding(String pdfEncoding)
	{
		Object old = this.pdfEncoding;
		this.pdfEncoding = pdfEncoding;
		getEventSupport().firePropertyChange(PROPERTY_PDF_ENCODING, old, this.pdfEncoding);
	}

	@Override
	public void setPdfFontName(String pdfFontName)
	{
		Object old = this.pdfFontName;
		this.pdfFontName = pdfFontName;
		getEventSupport().firePropertyChange(PROPERTY_PDF_FONT_NAME, old, this.pdfFontName);
	}

	@Override
	public void setFontSize(Float fontSize)
	{
		Object old = this.fontsize;
		this.fontsize = fontSize;
		getEventSupport().firePropertyChange(PROPERTY_FONT_SIZE, old, this.fontsize);
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
	public JRConditionalStyle[] getConditionalStyles()
	{
		return conditionalStyles;
	}

	@Override
	public String getStyleNameReference()
	{
		return parentStyleNameReference;
	}
	
	@Override
	public Float getDefaultLineWidth()
	{
		return null;
	}
	
	@Override
	public Color getDefaultLineColor()
	{
		return getForecolor();
	}

	
	private transient JRPropertyChangeSupport eventSupport;
	
	@Override
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
	private Byte scaleImage;
	/**
	 * @deprecated
	 */
	private Byte fill;
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
			modeValue = ModeEnum.getByValue(mode);
			horizontalAlignmentValue = net.sf.jasperreports.engine.type.HorizontalAlignEnum.getByValue(horizontalAlignment);
			verticalAlignmentValue = net.sf.jasperreports.engine.type.VerticalAlignEnum.getByValue(verticalAlignment);
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

			horizontalImageAlign = net.sf.jasperreports.engine.type.HorizontalAlignEnum.getHorizontalImageAlignEnum(horizontalAlignmentValue);
			verticalImageAlign = net.sf.jasperreports.engine.type.VerticalAlignEnum.getVerticalImageAlignEnum(verticalAlignmentValue);

			horizontalAlignmentValue = null;
			verticalAlignmentValue = null;
		}
	}
	
	@Override
	public Object clone()
	{
		JRBaseStyle clone = null;
		try
		{
			clone = (JRBaseStyle) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.lineBox = lineBox == null ? null : lineBox.clone(clone);
		clone.linePen = linePen == null ? null : linePen.clone(clone);
		clone.paragraph = paragraph == null ? null : paragraph.clone(clone);
		clone.conditionalStyles = JRCloneUtils.cloneArray(conditionalStyles);
		clone.eventSupport = null;

		return clone;
	}

	@Override
	public int getHashCode()
	{
		ObjectUtils.HashCode hash = ObjectUtils.hash();
		hash.addIdentity(parentStyle);
		addStyleHash(hash);
		
		// maybe adding conditional style to the hash is not worth it
		// as the remaining attributes provide good enough hash information
		hash.addIdentical(conditionalStyles);
		
		return hash.getHashCode();
	}

	protected void addStyleHash(ObjectUtils.HashCode hash)
	{
		hash.add(name);
		hash.add(isDefault);
		hash.add(modeValue);
		hash.add(forecolor);
		hash.add(backcolor);
		hash.addIdentical(linePen); 
		hash.add(fillValue);
		hash.add(radius);
		hash.add(scaleImageValue);
		hash.add(horizontalTextAlign);
		hash.add(verticalTextAlign);
		hash.add(horizontalImageAlign);
		hash.add(verticalImageAlign);
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
		hash.add(rotationValue);
		hash.add(markup);
		hash.add(pattern);
		hash.add(isBlankWhenNull);
	}

	@Override
	public boolean isIdentical(Object object)
	{
		if (this == object)
		{
			return true;
		}
		
		if (!(object instanceof JRBaseStyle))
		{
			return false;
		}
		
		JRBaseStyle style = (JRBaseStyle) object;

		return
				ObjectUtils.equalsIdentity(parentStyle, style.parentStyle)
				&& identicalStyle(style)
				&& ObjectUtils.identical(conditionalStyles, style.conditionalStyles);
	}

	protected boolean identicalStyle(JRBaseStyle style)
	{
		return 
				ObjectUtils.equals(name, style.name)
				&& ObjectUtils.equals(isDefault, style.isDefault)
				&& ObjectUtils.equals(modeValue, style.modeValue)
				&& ObjectUtils.equals(forecolor, style.forecolor)
				&& ObjectUtils.equals(backcolor, style.backcolor)
				&& ObjectUtils.identical(linePen, style.linePen)
				&& ObjectUtils.equals(fillValue, style.fillValue)
				&& ObjectUtils.equals(radius, style.radius)
				&& ObjectUtils.equals(scaleImageValue, style.scaleImageValue)
				&& ObjectUtils.equals(horizontalTextAlign, style.horizontalTextAlign)
				&& ObjectUtils.equals(verticalTextAlign, style.verticalTextAlign)
				&& ObjectUtils.equals(horizontalImageAlign, style.horizontalImageAlign)
				&& ObjectUtils.equals(verticalImageAlign, style.verticalImageAlign)
				&& ObjectUtils.identical(lineBox, style.lineBox)
				&& ObjectUtils.identical(paragraph, style.paragraph)
				&& ObjectUtils.equals(fontName, style.fontName)
				&& ObjectUtils.equals(isBold, style.isBold)
				&& ObjectUtils.equals(isItalic, style.isItalic)
				&& ObjectUtils.equals(isUnderline, style.isUnderline)
				&& ObjectUtils.equals(isStrikeThrough, style.isStrikeThrough)
				&& ObjectUtils.equals(fontsize, style.fontsize)
				&& ObjectUtils.equals(pdfFontName, style.pdfFontName)
				&& ObjectUtils.equals(pdfEncoding, style.pdfEncoding)
				&& ObjectUtils.equals(isPdfEmbedded, style.isPdfEmbedded)
				&& ObjectUtils.equals(rotationValue, style.rotationValue)
				&& ObjectUtils.equals(markup, style.markup)
				&& ObjectUtils.equals(pattern, style.pattern)
				&& ObjectUtils.equals(isBlankWhenNull, style.isBlankWhenNull);
	}
}
