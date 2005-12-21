/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.engine;

import java.awt.Color;
import java.util.List;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id
 */
public interface JRStyle extends JRStyleContainer
{
	/**
	 * Gets the style unique name.
	 */
	public String getName();

	/**
	 * Gets a flag that specifies if this is the default report style.
	 */
	public boolean isDefault();

	/**
	 * Returns the element transparency mode.
	 * The default value depends on the type of the report element. Graphic elements like rectangles and lines are
	 * opaque by default, but the images are transparent. Both static texts and text fields are transparent
	 * by default, and so are the subreport elements.
	 * @return MODE_OPAQUE or MODE_TRANSPARENT
	 */
	public Byte getMode();

	public Byte getOwnMode();

	public Color getForecolor();

	public Color getOwnForecolor();

	public Color getBackcolor();

	public Color getOwnBackcolor();

	/**
	 * Indicates the pen type used for this element.
	 * @return one of the pen constants in this class
	 */
	public Byte getPen();

	public Byte getOwnPen();

	/**
	 * Indicates the fill type used for this element.
	 * @return one of the pen constants in this class
	 */
	public Byte getFill();

	public Byte getOwnFill();

	/**
	 * Indicates the corner radius for rectangles with round corners. The default is 0.
	 */
	public Integer getRadius();

	public Integer getOwnRadius();

	/**
	 * Gets the image scale type.
	 * @return one of the scale constants in this class
	 */
	public Byte getScaleImage();

	public Byte getOwnScaleImage();

	/**
	 * Gets the horizontal alignment of the element.
	 * @return one of the alignment values defined in {@link JRAlignment}
	 */
	public Byte getHorizontalAlignment();

	public Byte getOwnHorizontalAlignment();

	/**
	 * Gets the vertical alignment of the element.
	 * @return one of the alignment values defined in {@link JRAlignment}
	 */
	public Byte getVerticalAlignment();

	public Byte getOwnVerticalAlignment();

	/**
	 * Gets the default border pen size (can be overwritten by individual settings).
	 */
	public Byte getBorder();

	public Byte getOwnBorder();

	/**
	 * Gets the default border color (can be overwritten by individual settings).
	 */
	public Color getBorderColor();

	public Color getOwnBorderColor();

	/**
	 * Gets the default padding in pixels (can be overwritten by individual settings).
	 */
	public Integer getPadding();

	public Integer getOwnPadding();

	/**
	 * Gets the top border pen size.
	 */
	public Byte getTopBorder();


	/**
	 * Gets the top border pen size (if the default value was overwritten).
	 */
	public Byte getOwnTopBorder();


	/**
	 * Gets the top border color.
	 */
	public Color getTopBorderColor();


	/**
	 * Gets the top border color (if the default value was overwritten).
	 */
	public Color getOwnTopBorderColor();


	/**
	 *
	 */
	public Integer getTopPadding();


	/**
	 *
	 */
	public Integer getOwnTopPadding();


	/**
	 *
	 */
	public Byte getLeftBorder();


	/**
	 *
	 */
	public Byte getOwnLeftBorder();


	/**
	 *
	 */
	public Color getLeftBorderColor();


	/**
	 *
	 */
	public Color getOwnLeftBorderColor();


	/**
	 *
	 */
	public Integer getLeftPadding();


	/**
	 *
	 */
	public Integer getOwnLeftPadding();


	/**
	 *
	 */
	public Byte getBottomBorder();


	/**
	 *
	 */
	public Byte getOwnBottomBorder();


	/**
	 *
	 */
	public Color getBottomBorderColor();


	/**
	 *
	 */
	public Color getOwnBottomBorderColor();


	/**
	 *
	 */
	public Integer getBottomPadding();


	/**
	 *
	 */
	public Integer getOwnBottomPadding();


	/**
	 *
	 */
	public Byte getRightBorder();


	/**
	 *
	 */
	public Byte getOwnRightBorder();


	/**
	 *
	 */
	public Color getRightBorderColor();


	/**
	 *
	 */
	public Color getOwnRightBorderColor();


	/**
	 *
	 */
	public Integer getRightPadding();


	/**
	 *
	 */
	public Integer getOwnRightPadding();


	/**
	 * Gets the text rotation.
	 * @return a value representing one of the rotation constants in this class
	 */
	public Byte getRotation();

	public Byte getOwnRotation();

	/**
	 * Gets the line spacing.
	 * @return a value representing one of the line spacing constants in this class
	 */
	public Byte getLineSpacing();

	public Byte getOwnLineSpacing();

	/**
	 * Returns true if the text can contain style tags.
	 */
	public Boolean isStyledText();

	public Boolean isOwnStyledText();

	/**
	 *
	 */
	public String getFontName();

	/**
	 *
	 */
	public String getOwnFontName();

	/**
	 *
	 */
	public Boolean isBold();

	/**
	 *
	 */
	public Boolean isOwnBold();

	/**
	 *
	 */
	public Boolean isItalic();

	/**
	 *
	 */
	public Boolean isOwnItalic();

	/**
	 *
	 */
	public Boolean isUnderline();

	/**
	 *
	 */
	public Boolean isOwnUnderline();

	/**
	 *
	 */
	public Boolean isStrikeThrough();

	/**
	 *
	 */
	public Boolean isOwnStrikeThrough();

	/**
	 *
	 */
	public Integer getFontSize();

	/**
	 *
	 */
	public Integer getOwnFontSize();

	/**
	 *
	 */
	public String getPdfFontName();

	/**
	 *
	 */
	public String getOwnPdfFontName();

	/**
	 *
	 */
	public String getPdfEncoding();

	/**
	 *
	 */
	public String getOwnPdfEncoding();

	/**
	 *
	 */
	public Boolean isPdfEmbedded();

	/**
	 *
	 */
	public Boolean isOwnPdfEmbedded();

	/**
	 * Gets the pattern used for this text field. The pattern will be used in a <tt>SimpleDateFormat</tt> for dates
	 * and a <tt>DecimalFormat</tt> for numeric text fields. The pattern format must follow one of these two classes
	 * formatting rules, as specified in the JDK API docs.
	 * @return a string containing the pattern.
	 */
	public String getPattern();

	public String getOwnPattern();

	/**
	 *
	 */
	public Boolean isBlankWhenNull();

	/**
	 *
	 */
	public Boolean isOwnBlankWhenNull();

	/**
	 *
	 */
	public void setForecolor(Color forecolor);

	/**
	 *
	 */
	public void setBackcolor(Color backcolor);

	/**
	 *
	 */
	public void setMode(byte mode);

	/**
	 *
	 */
	public void setMode(Byte mode);

	/**
	 *
	 */
	public void setPen(byte pen);

	/**
	 *
	 */
	public void setPen(Byte pen);

	/**
	 *
	 */
	public void setFill(byte fill);

	/**
	 *
	 */
	public void setFill(Byte fill);

	/**
	 *
	 */
	public void setRadius(int radius);

	/**
	 *
	 */
	public void setRadius(Integer radius);

	/**
	 *
	 */
	public void setScaleImage(byte scaleImage);

	/**
	 *
	 */
	public void setScaleImage(Byte scaleImage);

	/**
	 *
	 */
	public void setHorizontalAlignment(byte horizontalAlignment);

	/**
	 *
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment);

	/**
	 *
	 */
	public void setVerticalAlignment(byte verticalAlignment);

	/**
	 *
	 */
	public void setVerticalAlignment(Byte verticalAlignment);

	/**
	 *
	 */
	public void setBorder(byte border);

	/**
	 *
	 */
	public void setBorder(Byte border);

	/**
	 *
	 */
	public void setBorderColor(Color borderColor);

	/**
	 *
	 */
	public void setPadding(int padding);

	/**
	 *
	 */
	public void setPadding(Integer padding);

	/**
	 *
	 */
	public void setTopBorder(byte topBorder);

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder);

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor);

	/**
	 *
	 */
	public void setTopPadding(int topPadding);

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding);

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder);

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder);

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor);

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding);

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding);

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder);

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder);

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor);

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding);

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding);

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder);

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder);

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor);

	/**
	 *
	 */
	public void setRightPadding(int rightPadding);

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding);

	/**
	 *
	 */
	public void setRotation(byte rotation);

	/**
	 *
	 */
	public void setRotation(Byte rotation);

	/**
	 *
	 */
	public void setFontName(String fontName);

	/**
	 *
	 */
	public void setBold(boolean bold);

	/**
	 *
	 */
	public void setBold(Boolean bold);

	/**
	 *
	 */
	public void setItalic(boolean italic);

	/**
	 *
	 */
	public void setItalic(Boolean italic);

	/**
	 *
	 */
	public void setPdfEmbedded(boolean pdfEmbedded);

	/**
	 *
	 */
	public void setPdfEmbedded(Boolean pdfEmbedded);

	/**
	 *
	 */
	public void setStrikeThrough(boolean strikeThrough);

	/**
	 *
	 */
	public void setStrikeThrough(Boolean strikeThrough);

	/**
	 *
	 */
	public void setStyledText(boolean styledText);

	/**
	 *
	 */
	public void setStyledText(Boolean styledText);

	/**
	 *
	 */
	public void setUnderline(boolean underline);

	/**
	 *
	 */
	public void setUnderline(Boolean underline);

	/**
	 *
	 */
	public void setLineSpacing(byte lineSpacing);

	/**
	 *
	 */
	public void setLineSpacing(Byte lineSpacing);

	/**
	 *
	 */
	public void setPattern(String pattern);

	/**
	 *
	 */
	public void setBlankWhenNull(boolean isBlankWhenNull);

	/**
	 *
	 */
	public void setBlankWhenNull(Boolean isBlankWhenNull);

	/**
	 *
	 */
	public void setPdfEncoding(String pdfEncoding);

	/**
	 *
	 */
	public void setPdfFontName(String pdfFontName);

	/**
	 *
	 */
	public void setFontSize(int fontSize);

	/**
	 *
	 */
	public void setFontSize(Integer fontSize);

	/**
	 *
	 */
	public JRConditionalStyle[] getConditionalStyles();
}
