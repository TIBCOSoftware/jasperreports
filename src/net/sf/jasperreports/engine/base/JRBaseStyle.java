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
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseStyle implements JRStyle, Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 10001;


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
			public void setStyleDelayed(JRStyle aStyle)
			{
				setParentStyle(aStyle);
			}

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
		this.forecolor = forecolor;
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
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
		this.mode = mode;
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
		this.pen = pen;
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
		this.fill = fill;
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
		this.radius = radius;
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
		this.scaleImage = scaleImage;
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
		this.horizontalAlignment = horizontalAlignment;
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
		this.verticalAlignment = verticalAlignment;
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
		this.border = border;
	}

	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
		this.borderColor = borderColor;
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
		this.padding = padding;
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
		this.topBorder = topBorder;
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
		this.topBorderColor = topBorderColor;
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
		this.topPadding = topPadding;
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
		this.leftBorder = leftBorder;
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
		this.leftBorderColor = leftBorderColor;
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
		this.leftPadding = leftPadding;
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
		this.bottomBorder = bottomBorder;
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
		this.bottomBorderColor = bottomBorderColor;
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
		this.bottomPadding = bottomPadding;
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
		this.rightBorder = rightBorder;
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
		this.rightBorderColor = rightBorderColor;
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
		this.rightPadding = rightPadding;
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
		this.rotation = rotation;
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
	public void setBold(boolean bold)
	{
		setBold(bold ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 *
	 */
	public void setBold(Boolean bold)
	{
		this.isBold = bold;
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
		this.isItalic = italic;
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
		this.isPdfEmbedded = pdfEmbedded;
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
		this.isStrikeThrough = strikeThrough;
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
		this.isStyledText = styledText;
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
		this.isBlankWhenNull = isBlankWhenNull;
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
		this.isUnderline = underline;
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
		this.lineSpacing = lineSpacing;
	}

	/**
	 *
	 */
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
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
	public void setPdfFontName(String pdfFontName)
	{
		this.pdfFontName = pdfFontName;
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
		this.fontSize = fontSize;
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

}
