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
public interface JRStyle extends JRStyleContainer, JRBoxContainer, JRPenContainer, JRParagraphContainer, JRCloneable
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
	 * Gets the horizontal alignment of the element.
	 * @return one of the alignment values defined in {@link HorizontalAlignEnum}
	 */
	public HorizontalAlignEnum getHorizontalAlignmentValue();

	public HorizontalAlignEnum getOwnHorizontalAlignmentValue();

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
	 * @deprecated Replaced by {@link JRParagraph#getLineSpacing()}.
	 */
	public LineSpacingEnum getLineSpacingValue();

	/**
	 * @deprecated Replaced by {@link JRParagraph#getOwnLineSpacing()}.
	 */
	public LineSpacingEnum getOwnLineSpacingValue();

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
	 *
	 */
	public void setMode(ModeEnum mode);

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
	 *
	 */
	public void setScaleImage(ScaleImageEnum scaleImage);

	/**
	 *
	 */
	public void setHorizontalAlignment(HorizontalAlignEnum horizontalAlignment);

	/**
	 *
	 */
	public void setVerticalAlignment(VerticalAlignEnum verticalAlignment);

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
	 * @deprecated Replaced by {@link JRParagraph#setLineSpacing(LineSpacingEnum)}
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
