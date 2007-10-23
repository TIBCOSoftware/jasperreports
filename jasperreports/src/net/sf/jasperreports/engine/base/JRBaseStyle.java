/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 *
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleSetter;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
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
	private static final long serialVersionUID = 10001;
	
	public static final String PROPERTY_BACKCOLOR = "backcolor";
	
	public static final String PROPERTY_BLANK_WHEN_NULL = "blankWhenNull";
	
	public static final String PROPERTY_BOLD = "bold";
	
	public static final String PROPERTY_BORDER = "border";
	
	public static final String PROPERTY_BORDER_COLOR = "borderColor";
	
	public static final String PROPERTY_BOTTOM_BORDER = "bottomBorder";
	
	public static final String PROPERTY_BOTTOM_BORDER_COLOR = "bottomBorderColor";
	
	public static final String PROPERTY_BOTTOM_PADDING = "bottomPadding";
	
	public static final String PROPERTY_FILL = "fill";
	
	public static final String PROPERTY_FONT_NAME = "fontName";
	
	public static final String PROPERTY_FONT_SIZE = "fontSize";
	
	public static final String PROPERTY_FORECOLOR = "forecolor";
	
	public static final String PROPERTY_HORIZONTAL_ALIGNMENT = "horizontalAlignment";
	
	public static final String PROPERTY_ITALIC = "italic";
	
	public static final String PROPERTY_LEFT_BORDER = "leftBorder";
	
	public static final String PROPERTY_LEFT_BORDER_COLOR = "leftBorderColor";
	
	public static final String PROPERTY_LEFT_PADDING = "leftPadding";
	
	public static final String PROPERTY_LINE_SPACING = "lineSpacing";
	
	public static final String PROPERTY_MODE = "mode";
	
	public static final String PROPERTY_PADDING = "padding";
	
	public static final String PROPERTY_PATTERN = "pattern";
	
	public static final String PROPERTY_PDF_EMBEDDED = "pdfEmbedded";
	
	public static final String PROPERTY_PDF_ENCODING = "pdfEncoding";
	
	public static final String PROPERTY_PDF_FONT_NAME = "pdfFontName";
	
	public static final String PROPERTY_PEN = "pen";
	
	public static final String PROPERTY_RADIUS = "radius";
	
	public static final String PROPERTY_RIGHT_BORDER = "rightBorder";
	
	public static final String PROPERTY_RIGHT_BORDER_COLOR = "rightBorderColor";
	
	public static final String PROPERTY_RIGHT_PADDING = "rightPadding";
	
	public static final String PROPERTY_ROTATION = "rotation";
	
	public static final String PROPERTY_SCALE_IMAGE = "scaleImage";
	
	public static final String PROPERTY_STRIKE_THROUGH = "strikeThrough";
	
	public static final String PROPERTY_IS_STYLED_TEXT = "isStyledText";
	
	public static final String PROPERTY_TOP_BORDER = "topBorder";
	
	public static final String PROPERTY_TOP_BORDER_COLOR = "topBorderColor";
	
	public static final String PROPERTY_TOP_PADDING = "topPadding";
	
	public static final String PROPERTY_UNDERLINE = "underline";
	
	public static final String PROPERTY_VERTICAL_ALIGNMENT = "verticalAlignment";


	/**
	 *
	 */
	protected JRDefaultStyleProvider defaultStyleProvider = null;
	protected JRStyle parentStyle = null;
	protected String parentStyleNameReference;

	/**
	 *
	 */
	protected String name = null;
	protected boolean isDefault = false;

	protected Byte positionType = null;
	protected Byte stretchType = null;
	protected Byte mode = null;
	protected Color forecolor = null;
	protected Color backcolor = null;

	protected Byte pen = null;
	protected Byte fill = null;

	protected Integer radius = null;

	protected Byte scaleImage = null;
	protected Byte horizontalAlignment = null;
	protected Byte verticalAlignment = null;

	protected Byte border = null;
	protected Byte topBorder = null;
	protected Byte leftBorder = null;
	protected Byte bottomBorder = null;
	protected Byte rightBorder = null;
	protected Color borderColor = null;
	protected Color topBorderColor = null;
	protected Color leftBorderColor = null;
	protected Color bottomBorderColor = null;
	protected Color rightBorderColor = null;
	protected Integer padding = null;
	protected Integer topPadding = null;
	protected Integer leftPadding = null;
	protected Integer bottomPadding = null;
	protected Integer rightPadding = null;

	protected String fontName = null;
	protected Boolean isBold = null;
	protected Boolean isItalic = null;
	protected Boolean isUnderline = null;
	protected Boolean isStrikeThrough = null;
	protected Integer fontSize = null;
	protected String pdfFontName = null;
	protected String pdfEncoding = null;
	protected Boolean isPdfEmbedded = null;

	protected Byte rotation = null;
	protected Byte lineSpacing = null;
	protected Boolean isStyledText = null;

	protected String pattern = null;
	protected Boolean isBlankWhenNull = null;

	protected JRConditionalStyle[] conditionalStyles;


	/**
	 *
	 */
	public JRBaseStyle()
	{
	}

	/**
	 *
	 */
	public JRBaseStyle(String name)
	{
		this.name = name;
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

		mode = style.getOwnMode();
		forecolor = style.getOwnForecolor();
		backcolor = style.getOwnBackcolor();

		pen = style.getOwnPen();
		fill = style.getOwnFill();

		radius = style.getOwnRadius();

		scaleImage = style.getOwnScaleImage();
		horizontalAlignment = style.getOwnHorizontalAlignment();
		verticalAlignment = style.getOwnVerticalAlignment();

		border = style.getOwnBorder();
		topBorder = style.getOwnTopBorder();
		leftBorder = style.getOwnLeftBorder();
		bottomBorder = style.getOwnBottomBorder();
		rightBorder = style.getOwnRightBorder();
		borderColor = style.getOwnBorderColor();
		topBorderColor = style.getOwnTopBorderColor();
		leftBorderColor = style.getOwnLeftBorderColor();
		bottomBorderColor = style.getOwnBottomBorderColor();
		rightBorderColor = style.getOwnRightBorderColor();
		padding = style.getOwnPadding();
		topPadding = style.getOwnTopPadding();
		leftPadding = style.getOwnLeftPadding();
		bottomPadding = style.getOwnBottomPadding();
		rightPadding = style.getOwnRightPadding();

		rotation = style.getOwnRotation();
		lineSpacing = style.getOwnLineSpacing();
		isStyledText = style.isOwnStyledText();

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

	public Byte getPen()
	{
		return JRStyleResolver.getPen(this);
	}

	public Byte getOwnPen()
	{
		return pen;
	}

	public Byte getFill()
	{
		return JRStyleResolver.getFill(this);
	}

	public Byte getOwnFill()
	{
		return fill;
	}

	public Integer getRadius()
	{
		return JRStyleResolver.getRadius(this);
	}

	public Integer getOwnRadius()
	{
		return radius;
	}

	public Byte getScaleImage()
	{
		return JRStyleResolver.getScaleImage(this);
	}

	public Byte getOwnScaleImage()
	{
		return scaleImage;
	}

	public Byte getHorizontalAlignment()
	{
		return JRStyleResolver.getHorizontalAlignment(this);
	}

	public Byte getOwnHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	public Byte getVerticalAlignment()
	{
		return JRStyleResolver.getVerticalAlignment(this);
	}

	public Byte getOwnVerticalAlignment()
	{
		return verticalAlignment;
	}

	public Byte getBorder()
	{
		return JRStyleResolver.getBorder(this);
	}

	public Byte getOwnBorder()
	{
		return border;
	}

	public Color getBorderColor()
	{
		return JRStyleResolver.getBorderColor(this);
	}

	public Color getOwnBorderColor()
	{
		return borderColor;
	}

	public Integer getPadding()
	{
		return JRStyleResolver.getPadding(this);
	}

	public Integer getOwnPadding()
	{
		return padding;
	}

	public Byte getTopBorder()
	{
		return JRStyleResolver.getTopBorder(this);
	}

	public Byte getOwnTopBorder()
	{
		return topBorder;
	}

	public Color getTopBorderColor()
	{
		return JRStyleResolver.getTopBorderColor(this);
	}

	public Color getOwnTopBorderColor()
	{
		return topBorderColor;
	}

	public Integer getTopPadding()
	{
		return JRStyleResolver.getTopPadding(this);
	}

	public Integer getOwnTopPadding()
	{
		return topPadding;
	}

	public Byte getLeftBorder()
	{
		return JRStyleResolver.getLeftBorder(this);
	}

	public Byte getOwnLeftBorder()
	{
		return leftBorder;
	}

	public Color getLeftBorderColor()
	{
		return JRStyleResolver.getLeftBorderColor(this);
	}

	public Color getOwnLeftBorderColor()
	{
		return leftBorderColor;
	}

	public Integer getLeftPadding()
	{
		return JRStyleResolver.getLeftPadding(this);
	}

	public Integer getOwnLeftPadding()
	{
		return leftPadding;
	}

	public Byte getBottomBorder()
	{
		return JRStyleResolver.getBottomBorder(this);
	}

	public Byte getOwnBottomBorder()
	{
		return bottomBorder;
	}

	public Color getBottomBorderColor()
	{
		return JRStyleResolver.getBottomBorderColor(this);
	}

	public Color getOwnBottomBorderColor()
	{
		return bottomBorderColor;
	}

	public Integer getBottomPadding()
	{
		return JRStyleResolver.getBottomPadding(this);
	}

	public Integer getOwnBottomPadding()
	{
		return bottomPadding;
	}

	public Byte getRightBorder()
	{
		return JRStyleResolver.getRightBorder(this);
	}

	public Byte getOwnRightBorder()
	{
		return rightBorder;
	}

	public Color getRightBorderColor()
	{
		return JRStyleResolver.getRightBorderColor(this);
	}

	public Color getOwnRightBorderColor()
	{
		return rightBorderColor;
	}

	public Integer getRightPadding()
	{
		return JRStyleResolver.getRightPadding(this);
	}

	public Integer getOwnRightPadding()
	{
		return rightPadding;
	}

	public Byte getRotation()
	{
		return JRStyleResolver.getRotation(this);
	}

	public Byte getOwnRotation()
	{
		return rotation;
	}

	public Byte getLineSpacing()
	{
		return JRStyleResolver.getLineSpacing(this);
	}

	public Byte getOwnLineSpacing()
	{
		return lineSpacing;
	}

	public Boolean isStyledText()
	{
		return JRStyleResolver.isStyledText(this);
	}

	public Boolean isOwnStyledText()
	{
		return isStyledText;
	}

	public Boolean isBlankWhenNull()
	{
		return JRStyleResolver.isBlankWhenNull(this);
	}

	public Boolean isOwnBlankWhenNull()
	{
		return isStyledText;
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

	public Byte getMode()
	{
		return JRStyleResolver.getMode(this);
	}

	public Byte getOwnMode()
	{
		return mode;
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
	 *
	 */
	public void setMode(byte mode)
	{
		setMode(new Byte(mode));
	}

	/**
	 *
	 */
	public void setMode(Byte mode)
	{
		Object old = this.mode;
		this.mode = mode;
		getEventSupport().firePropertyChange(PROPERTY_MODE, old, this.mode);
	}

	/**
	 *
	 */
	public void setPen(byte pen)
	{
		setPen(new Byte(pen));
	}

	/**
	 *
	 */
	public void setPen(Byte pen)
	{
		Object old = this.pen;
		this.pen = pen;
		getEventSupport().firePropertyChange(PROPERTY_PEN, old, this.pen);
	}

	/**
	 *
	 */
	public void setFill(byte fill)
	{
		setFill(new Byte(fill));
	}

	/**
	 *
	 */
	public void setFill(Byte fill)
	{
		Object old = this.fill;
		this.fill = fill;
		getEventSupport().firePropertyChange(PROPERTY_FILL, old, this.fill);
	}

	/**
	 *
	 */
	public void setRadius(int radius)
	{
		setRadius(new Integer(radius));
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
	 *
	 */
	public void setScaleImage(byte scaleImage)
	{
		setScaleImage(new Byte(scaleImage));
	}

	/**
	 *
	 */
	public void setScaleImage(Byte scaleImage)
	{
		Object old = this.scaleImage;
		this.scaleImage = scaleImage;
		getEventSupport().firePropertyChange(PROPERTY_SCALE_IMAGE, old, this.scaleImage);
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment)
	{
		setHorizontalAlignment(new Byte(horizontalAlignment));
	}

	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment)
	{
		Object old = this.horizontalAlignment;
		this.horizontalAlignment = horizontalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_HORIZONTAL_ALIGNMENT, old, this.horizontalAlignment);
	}

	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment)
	{
		setVerticalAlignment(new Byte(verticalAlignment));
	}

	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment)
	{
		Object old = this.verticalAlignment;
		this.verticalAlignment = verticalAlignment;
		getEventSupport().firePropertyChange(PROPERTY_VERTICAL_ALIGNMENT, old, this.verticalAlignment);
	}

	/**
	 *
	 */
	public void setBorder(byte border)
	{
		setBorder(new Byte(border));
	}

	/**
	 *
	 */
	public void setBorder(Byte border)
	{
		Object old = this.border;
		this.border = border;
		getEventSupport().firePropertyChange(PROPERTY_BORDER, old, this.border);
	}

	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
		Object old = this.borderColor;
		this.borderColor = borderColor;
		getEventSupport().firePropertyChange(PROPERTY_BORDER_COLOR, old, this.borderColor);
	}

	/**
	 *
	 */
	public void setPadding(int padding)
	{
		setPadding(new Integer(padding));
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
		Object old = this.padding;
		this.padding = padding;
		getEventSupport().firePropertyChange(PROPERTY_PADDING, old, this.padding);
	}

	/**
	 *
	 */
	public void setTopBorder(byte topBorder)
	{
		setTopBorder(new Byte(topBorder));
	}

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder)
	{
		Object old = this.topBorder;
		this.topBorder = topBorder;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER, old, this.topBorder);
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
		Object old = this.topBorderColor;
		this.topBorderColor = topBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER_COLOR, old, this.topBorderColor);
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
		setTopPadding(new Integer(topPadding));
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
		Object old = this.topPadding;
		this.topPadding = topPadding;
		getEventSupport().firePropertyChange(PROPERTY_TOP_PADDING, old, this.topPadding);
	}

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder)
	{
		setLeftBorder(new Byte(leftBorder));
	}

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder)
	{
		Object old = this.leftBorder;
		this.leftBorder = leftBorder;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER, old, this.leftBorder);
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
		Object old = this.leftBorderColor;
		this.leftBorderColor = leftBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER_COLOR, old, this.leftBorderColor);
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
		setLeftPadding(new Integer(leftPadding));
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		Object old = this.leftPadding;
		this.leftPadding = leftPadding;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_PADDING, old, this.leftPadding);
	}

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder)
	{
		setBottomBorder(new Byte(bottomBorder));
	}

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
		Object old = this.bottomBorder;
		this.bottomBorder = bottomBorder;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_BORDER, old, this.bottomBorder);
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
		Object old = this.bottomBorderColor;
		this.bottomBorderColor = bottomBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_BORDER_COLOR, old, this.bottomBorderColor);
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
		setBottomPadding(new Integer(bottomPadding));
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		Object old = this.bottomPadding;
		this.bottomPadding = bottomPadding;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_PADDING, old, this.bottomPadding);
	}

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder)
	{
		setRightBorder(new Byte(rightBorder));
	}

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder)
	{
		Object old = this.rightBorder;
		this.rightBorder = rightBorder;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER, old, this.rightBorder);
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
		Object old = this.rightBorderColor;
		this.rightBorderColor = rightBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER_COLOR, old, this.rightBorderColor);
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
		setRightPadding(new Integer(rightPadding));
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
		Object old = this.rightPadding;
		this.rightPadding = rightPadding;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_PADDING, old, this.rightPadding);
	}

	/**
	 *
	 */
	public void setRotation(byte rotation)
	{
		setRotation(new Byte(rotation));
	}

	/**
	 *
	 */
	public void setRotation(Byte rotation)
	{
		Object old = this.rotation;
		this.rotation = rotation;
		getEventSupport().firePropertyChange(PROPERTY_ROTATION, old, this.rotation);
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
	 *
	 */
	public void setStyledText(boolean styledText)
	{
		setStyledText(styledText ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setStyledText(Boolean styledText)
	{
		Object old = this.isStyledText;
		this.isStyledText = styledText;
		getEventSupport().firePropertyChange(PROPERTY_IS_STYLED_TEXT, old, this.isStyledText);
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
	 *
	 */
	public void setLineSpacing(byte lineSpacing)
	{
		setLineSpacing(new Byte(lineSpacing));
	}

	/**
	 *
	 */
	public void setLineSpacing(Byte lineSpacing)
	{
		Object old = this.lineSpacing;
		this.lineSpacing = lineSpacing;
		getEventSupport().firePropertyChange(PROPERTY_LINE_SPACING, old, this.lineSpacing);
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
		setFontSize(new Integer(fontSize));
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

}
