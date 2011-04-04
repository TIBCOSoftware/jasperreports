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
package net.sf.jasperreports.engine;

import java.awt.Color;

import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public interface JRStyle extends JRStyleContainer, JRBoxContainer, JRPenContainer, JRCloneable
{
	
	/**
	 * A flag the determines whether the style of an element is evaluated at
	 * the element evaluation time, or at the time the band on which the element
	 * is placed is rendered.
	 * 
	 * <p>
	 * This applies to report elements that can have delayed evaluations times
	 * (such as text fields and images).  When this flag is set to
	 * <code>true</code>, conditional style expressions of the style that is
	 * associated with the element are evaluated at the moment the element is
	 * set to evaluate, and the resulting style to the generated print element.
	 * 
	 * <p>
	 * By default, this flag is set to <code>false</code>.  The property can be
	 * set globally, at report level and at element level.
	 */
	public static final String PROPERTY_EVALUATION_TIME_ENABLED = 
		JRProperties.PROPERTY_PREFIX + "style.evaluation.time.enabled";
	
	/**
	 * Gets the style unique name.
	 */
	public String getName();

	/**
	 * Gets a flag that specifies if this is the default report style.
	 */
	public boolean isDefault();

	/**
	 * @deprecated Replaced by {@link #getModeValue()}.
	 */
	public Byte getMode();

	/**
	 * @deprecated Replaced by {@link #getOwnModeValue()}.
	 */
	public Byte getOwnMode();

	/**
	 * Returns the element transparency mode.
	 * The default value depends on the type of the report element. Graphic elements like rectangles and lines are
	 * opaque by default, but the images are transparent. Both static texts and text fields are transparent
	 * by default, and so are the subreport elements.
	 */
	public ModeEnum getModeValue();

	public ModeEnum getOwnModeValue();

	public Color getForecolor();

	public Color getOwnForecolor();

	public Color getBackcolor();

	public Color getOwnBackcolor();

	/**
	 * 
	 */
	public JRPen getLinePen();

	/**
	 * Indicates the pen type used for this element.
	 * @return one of the pen constants in this class
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public Byte getPen();

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public Byte getOwnPen();

	/**
	 * @deprecated Replaced by {@link #getFillValue()}.
	 */
	public Byte getFill();

	/**
	 * @deprecated Replaced by {@link #getOwnFillValue()}.
	 */
	public Byte getOwnFill();

	/**
	 * Indicates the fill type used for this element.
	 * @return one of the fill constants in {@link FillEnum}.
	 */
	public FillEnum getFillValue();

	public FillEnum getOwnFillValue();

	/**
	 * Indicates the corner radius for rectangles with round corners. The default is 0.
	 */
	public Integer getRadius();

	public Integer getOwnRadius();

	/**
	 * @deprecated Replaced by {@link #getScaleImageValue()}.
	 */
	public Byte getScaleImage();

	/**
	 * @deprecated Replaced by {@link #getOwnScaleImageValue()}.
	 */
	public Byte getOwnScaleImage();

	/**
	 * Gets the image scale type.
	 * @return one of the scale types defined in {@link ScaleImageEnum}
	 */
	public ScaleImageEnum getScaleImageValue();

	/**
	 * Gets the image own scale type.
	 * @return one of the scale types defined in {@link ScaleImageEnum}
	 */
	public ScaleImageEnum getOwnScaleImageValue();

	/**
	 * @deprecated Replaced by {@link #getHorizontalAlignmentValue()}.
	 */
	public Byte getHorizontalAlignment();

	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalAlignmentValue()}.
	 */
	public Byte getOwnHorizontalAlignment();

	/**
	 * Gets the horizontal alignment of the element.
	 * @return one of the alignment values defined in {@link HorizontalAlignEnum}
	 */
	public HorizontalAlignEnum getHorizontalAlignmentValue();

	public HorizontalAlignEnum getOwnHorizontalAlignmentValue();

	/**
	 * @deprecated Replaced by {@link #getVerticalAlignmentValue()}.
	 */
	public Byte getVerticalAlignment();

	/**
	 * @deprecated Replaced by {@link #getOwnVerticalAlignmentValue()}.
	 */
	public Byte getOwnVerticalAlignment();

	/**
	 * Gets the vertical alignment of the element.
	 * @return one of the alignment values defined in {@link JRAlignment}
	 */
	public VerticalAlignEnum getVerticalAlignmentValue();

	public VerticalAlignEnum getOwnVerticalAlignmentValue();

	/**
	 * 
	 */
	public JRLineBox getLineBox();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getTopBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnTopBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getTopBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnTopBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getTopPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnTopPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getLeftBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnLeftBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getLeftBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnLeftBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getLeftPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnLeftPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getBottomBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnBottomBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getBottomBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnBottomBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getBottomPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnBottomPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getRightBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnRightBorder();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getRightBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnRightBorderColor();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getRightPadding();

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnRightPadding();


	/**
	 * Gets the text tab stop width.
	 */
	public Integer getTabStop();
	
	/**
	 * Gets the text own tab stop width.
	 */
	public Integer getOwnTabStop();
	
	/**
	 * @deprecated Replaced by {@link #getRotationValue()}.
	 */
	public Byte getRotation();

	/**
	 * @deprecated Replaced by {@link #getOwnRotationValue()}.
	 */
	public Byte getOwnRotation();

	/**
	 * Gets the text rotation.
	 * @return a value representing one of the rotation values in the {@link RotationEnum}.
	 */
	public RotationEnum getRotationValue();
	
	/**
	 * Gets the text own rotation.
	 * @return a value representing one of the rotation values in the {@link RotationEnum}.
	 */
	public RotationEnum getOwnRotationValue();
	
	/**
	 * @deprecated Replaced by {@link #getLineSpacingValue()}.
	 */
	public Byte getLineSpacing();

	/**
	 * @deprecated Replaced by {@link #getOwnLineSpacingValue()}.
	 */
	public Byte getOwnLineSpacing();

	/**
	 * Gets the line spacing.
	 * @return a value representing one of the line spacing constants in the {@link LineSpacingEnum}.
	 */
	public LineSpacingEnum getLineSpacingValue();

	public LineSpacingEnum getOwnLineSpacingValue();

	/**
	 * Returns true if the text can contain style tags.
	 * @deprecated Replaced by {@link #getMarkup()}
	 */
	public Boolean isStyledText();

	/**
	 * @deprecated Replaced by {@link #getOwnMarkup()}
	 */
	public Boolean isOwnStyledText();

	/**
	 * Returns the markup language used to format the text.
	 */
	public String getMarkup();

	public String getOwnMarkup();

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
	 * @deprecated Replaced by {@link #setMode(ModeEnum)}
	 */
	public void setMode(byte mode);

	/**
	 * @deprecated Replaced by {@link #setMode(ModeEnum)}
	 */
	public void setMode(Byte mode);

	/**
	 *
	 */
	public void setMode(ModeEnum mode);

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(byte pen);

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(Byte pen);

	/**
	 * @deprecated Replaced by {@link #setFill(FillEnum)}
	 */
	public void setFill(byte fill);

	/**
	 * @deprecated Replaced by {@link #setFill(FillEnum)}
	 */
	public void setFill(Byte fill);

	/**
	 * 
	 */
	public void setFill(FillEnum fill);

	/**
	 *
	 */
	public void setRadius(int radius);

	/**
	 *
	 */
	public void setRadius(Integer radius);

	/**
	 * @deprecated Replaced by {@link #setScaleImage(ScaleImageEnum)}.
	 */
	public void setScaleImage(byte scaleImage);

	/**
	 * @deprecated Replaced by {@link #setScaleImage(ScaleImageEnum)}.
	 */
	public void setScaleImage(Byte scaleImage);

	/**
	 *
	 */
	public void setScaleImage(ScaleImageEnum scaleImage);

	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(HorizontalAlignEnum)}.
	 */
	public void setHorizontalAlignment(byte horizontalAlignment);

	/**
	 * @deprecated Replaced by {@link #setHorizontalAlignment(HorizontalAlignEnum)}.
	 */
	public void setHorizontalAlignment(Byte horizontalAlignment);

	/**
	 *
	 */
	public void setHorizontalAlignment(HorizontalAlignEnum horizontalAlignment);

	/**
	 * @deprecated Replaced by {@link #setVerticalAlignment(VerticalAlignEnum)}.
	 */
	public void setVerticalAlignment(byte verticalAlignment);

	/**
	 * @deprecated Replaced by {@link #setVerticalAlignment(VerticalAlignEnum)}.
	 */
	public void setVerticalAlignment(Byte verticalAlignment);

	/**
	 *
	 */
	public void setVerticalAlignment(VerticalAlignEnum verticalAlignment);

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

	public void setTabStop(Integer tabStop);

	/**
	 * @deprecated Replaced by {@link #setRotation(RotationEnum)}.
	 */
	public void setRotation(byte rotation);

	/**
	 * @deprecated Replaced by {@link #setRotation(RotationEnum)}.
	 */
	public void setRotation(Byte rotation);

	public void setRotation(RotationEnum rotation);

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
	 * @deprecated Replaced by {@link #setMarkup(String)}
	 */
	public void setStyledText(boolean styledText);

	/**
	 * @deprecated Replaced by {@link #setMarkup(String)}
	 */
	public void setStyledText(Boolean styledText);

	/**
	 *
	 */
	public void setMarkup(String markup);

	/**
	 *
	 */
	public void setUnderline(boolean underline);

	/**
	 *
	 */
	public void setUnderline(Boolean underline);

	/**
	 * @deprecated Replaced by {@link #setLineSpacing(LineSpacingEnum)}
	 */
	public void setLineSpacing(byte lineSpacing);

	/**
	 * @deprecated Replaced by {@link #setLineSpacing(LineSpacingEnum)}
	 */
	public void setLineSpacing(Byte lineSpacing);

	/**
	 *
	 */
	public void setLineSpacing(LineSpacingEnum lineSpacing);

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
