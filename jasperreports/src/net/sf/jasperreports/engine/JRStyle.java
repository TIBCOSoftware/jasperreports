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
package net.sf.jasperreports.engine;

import java.awt.Color;

import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;

/**
 * Provides a collection of style settings declared at the report level.
 * <h3>Report Styles</h3>
 * A report style is a collection of style settings declared at the report level. These settings
 * can be reused throughout the entire report template when setting the style properties of
 * report elements.
 * <p>
 * The <code>name</code> attribute of a <code>&lt;style&gt;</code> element is mandatory. It must be unique because it 
 * references the corresponding report style throughout the report.
 * </p><p>
 * One can use <code>isDefault="true"</code> for one of your report style declarations to mark the 
 * default for elements that do not or cannot have another style specified.
 * </p><p>
 * Each report style definition can reference another style definition from which it will
 * inherit some or all of its properties. The <code>style</code> attribute inside a <code>&lt;style&gt;</code> element 
 * specifies the name of the parent report style.
 * </p><p>
 * Below is a list with the main style attributes:</p>
 * <ul>
 * <li><code>name</code> - the name of the style. Required.</li>
 * <li><code>isDefault</code> - flag that specifies if the style represents the default report style.</li>
 * <li><code>style</code> - the name of the parent style</li>
 * <li><code>mode</code> - specifies whether report elements are opaque or transparent. Possible values are:
 * <ul>
 * <li><code>Opaque</code></li>
 * <li><code>Transparent</code></li>
 * </ul>
 * </li>
 * <li><code>forecolor</code> - the font color for text elements or the line color for graphic elements</li>
 * <li><code>backcolor</code> - the element background color</li>
 * <li><code>fill</code> - the fill pattern. Possible value is <code>Solid</code></li>
 * <li><code>radius</code> - the radius for some graphic elements</li>
 * <li><code>hTextAlign</code> - the horizontal alignment of the text within a text element. Possible values are:
 * <ul>
 * <li><code>Left</code> - default setting</li>
 * <li><code>Center</code></li>
 * <li><code>Right</code></li>
 * <li><code>Justified</code></li>
 * </ul>
 * </li>
 * <li><code>vTextAlign</code> - the vertical alignment of the text within a text element. Possible values are:
 * <ul>
 * <li><code>Top</code> - default setting</li>
 * <li><code>Middle</code></li>
 * <li><code>Bottom</code></li>
 * </ul>
 * </li>
 * <li><code>hImageAlign</code> - the horizontal alignment of the image within an image element. Possible values are:
 * <ul>
 * <li><code>Left</code> - default setting</li>
 * <li><code>Center</code></li>
 * <li><code>Right</code></li>
 * </ul>
 * </li>
 * <li><code>vImageAlign</code> - the vertical alignment of the image within an image element. Possible values are:
 * <ul>
 * <li><code>Top</code> - default setting</li>
 * <li><code>Middle</code></li>
 * <li><code>Bottom</code></li>
 * </ul>
 * </li>
 * <li><code>rotation</code> - the text rotation within a text element. Possible values are:
 * <ul>
 * <li><code>None</code> - default setting</li>
 * <li><code>Left</code></li>
 * <li><code>Right</code></li>
 * <li><code>UpsideDown</code></li>
 * </ul>
 * </li>
 * <li><code>fontName</code> - the name of the font face</li>
 * <li><code>fontSize</code> - the font size</li>
 * <li><code>isBold</code> - flag that specifies whether the font is bold</li>
 * <li><code>isItalic</code> - flag that specifies whether the font is italic</li>
 * <li><code>isUnderline</code> - flag that specifies whether the font is underlined</li>
 * <li><code>isStrikeThrough</code> - flag that specifies whether the font is strike through</li>
 * <li><code>pattern</code> - specifies the format pattern for text elements</li>
 * <li><code>isBlankWhenNull</code> - flag that specifies whether null values should be represented as blanks</li>
 * <li><code>markup</code> - the markup style for the text elements. Possible values are:
 * <ul>
 * <li><code>none</code> - default setting</li>
 * <li><code>styled</code></li>
 * <li><code>html</code></li>
 * <li><code>rtf</code></li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * A style also may contain:</p>
 * <ul>
 * <li>a {@link net.sf.jasperreports.engine.JRPen} element that can be retrieved 
 * using the {@link #getLinePen()} method.</li>
 * <li>a {@link net.sf.jasperreports.engine.JRLineBox} element that can be retrieved 
 * using the {@link #getLineBox()} method.</li>
 * <li>a {@link net.sf.jasperreports.engine.JRParagraph} element that can be retrieved 
 * using the {@link #getParagraph()} method inherited from the 
 *  {@link net.sf.jasperreports.engine.JRParagraphContainer} interface.</li>
 * </ul>
 * <p>
 * All report elements can reference a report style to inherit all or part of the style
 * properties. A report style declaration groups all the style-related properties supported
 * throughout the library, but an individual element inherits only those style properties that
 * apply to it. The others will be ignored.
 * </p>
 * <h3>Conditional Styles</h3>
 * Sometimes users need to change a report element style at runtime based on certain
 * conditions (for example, to alternate adjacent row colors in a report detail section). To
 * achieve this goal, one can set some style properties to be enabled only if a specified
 * condition is true. This is done using conditional styles.
 * </p><p>
 * A conditional style has two elements: a Boolean condition expression and a <code>style</code>. The
 * style is used only if the condition evaluates to true.
 * </p><p>
 * An important aspect is the priority of styles. When applied, a conditional style will
 * override the properties of its parent style.
 * </p><p>
 * A style can contain more than one conditional style. In this case, all conditionals that
 * evaluate to true will be appended to the existing style (the second style will be
 * appended to the first, and so on).
 * </p><p>
 * By default, the style condition expressions are evaluated during the report filling process
 * at the time that the style reference is used. The conditional expression evaluation will use
 * the current values of referenced variables and fields, regardless of the <code>evaluationTime</code>
 * attribute of the element that makes use of the style.
 * </p><p>
 * If the evaluation of the condition expression of the style needs to be delayed, just like the
 * value of the text field or the image element that uses the conditional style, the
 * {@link #PROPERTY_EVALUATION_TIME_ENABLED net.sf.jasperreports.style.evaluation.time.enabled} configuration property
 * should be set to true.
 * </p>
 * <h3>Style Templates</h3>
 * Report styles can also be defined in external style template files that are referenced by
 * report templates. This allows report designers to define in a single place a common look
 * for a set of reports.
 * </p><p>
 * A style template is an XML file that contains one or more style definitions. A template
 * can include references to other style template files, hence one can organize a style library
 * as a hierarchical set of style template files.
 * </p><p>
 * Style template files use by convention the <code>*.jrtx</code> extension, but this is not mandatory.
 * </p><p>
 * A report can use style templates by explicitly referring them in its definition. References
 * to a style templates are included in JRXML reports as <code>&lt;template&gt;</code> elements. Such an
 * element contains an expression that is resolved at fill time to a style template instance.
 * </p>
 * @see net.sf.jasperreports.engine.JRLineBox
 * @see net.sf.jasperreports.engine.JRParagraph
 * @see net.sf.jasperreports.engine.JRPen
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
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
		JRPropertiesUtil.PROPERTY_PREFIX + "style.evaluation.time.enabled";
	
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
	 * @return one of the alignment values defined in {@link net.sf.jasperreports.engine.type.HorizontalAlignEnum}
	 * @deprecated Replaced by {@link #getHorizontalTextAlign()} and {@link #getHorizontalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue();

	/**
	 * @deprecated Replaced by {@link #getOwnHorizontalTextAlign()} and {@link #getOwnHorizontalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.HorizontalAlignEnum getOwnHorizontalAlignmentValue();

	/**
	 * Gets the vertical alignment of the element.
	 * @return one of the alignment values defined in {@link net.sf.jasperreports.engine.type.VerticalAlignEnum}
	 * @deprecated Replaced by {@link #getVerticalTextAlign()} and {@link #getVerticalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue();

	/**
	 * @deprecated Replaced by {@link #getOwnVerticalTextAlign()} and {@link #getOwnVerticalImageAlign()}.
	 */
	public net.sf.jasperreports.engine.type.VerticalAlignEnum getOwnVerticalAlignmentValue();

	/**
	 * Gets the horizontal text alignment of the element.
	 * @return one of the alignment values defined in {@link HorizontalTextAlignEnum}
	 */
	public HorizontalTextAlignEnum getHorizontalTextAlign();

	public HorizontalTextAlignEnum getOwnHorizontalTextAlign();

	/**
	 * Gets the vertical text alignment of the element.
	 * @return one of the alignment values defined in {@link VerticalTextAlignEnum}
	 */
	public VerticalTextAlignEnum getVerticalTextAlign();

	public VerticalTextAlignEnum getOwnVerticalTextAlign();

	/**
	 * Gets the horizontal image alignment of the element.
	 * @return one of the alignment values defined in {@link HorizontalImageAlignEnum}
	 */
	public HorizontalImageAlignEnum getHorizontalImageAlign();

	public HorizontalImageAlignEnum getOwnHorizontalImageAlign();

	/**
	 * Gets the vertical image alignment of the element.
	 * @return one of the alignment values defined in {@link VerticalImageAlignEnum}
	 */
	public VerticalImageAlignEnum getVerticalImageAlign();

	public VerticalImageAlignEnum getOwnVerticalImageAlign();

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
	public Float getFontsize();

	/**
	 *
	 */
	public Float getOwnFontsize();

	/**
	 * @deprecated Replaced by {@link #getFontsize()}.
	 */
	public Integer getFontSize();

	/**
	 * @deprecated Replaced by {@link #getOwnFontsize()}.
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
	 * @deprecated Replaced by {@link #setHorizontalTextAlign(HorizontalTextAlignEnum)} and {@link #setHorizontalImageAlign(HorizontalImageAlignEnum)}.
	 */
	public void setHorizontalAlignment(net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignment);

	/**
	 * @deprecated Replaced by {@link #setVerticalTextAlign(VerticalTextAlignEnum)} and {@link #setVerticalImageAlign(VerticalImageAlignEnum)}.
	 */
	public void setVerticalAlignment(net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignment);

	/**
	 *
	 */
	public void setHorizontalTextAlign(HorizontalTextAlignEnum horizontalAlignment);

	/**
	 *
	 */
	public void setVerticalTextAlign(VerticalTextAlignEnum verticalAlignment);

	/**
	 *
	 */
	public void setHorizontalImageAlign(HorizontalImageAlignEnum horizontalAlignment);

	/**
	 *
	 */
	public void setVerticalImageAlign(VerticalImageAlignEnum verticalAlignment);

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
	public void setFontSize(Float fontSize);

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	public void setFontSize(int fontSize);

	/**
	 * @deprecated Replaced by {@link #setFontSize(Float)}.
	 */
	public void setFontSize(Integer fontSize);

	/**
	 *
	 */
	public JRConditionalStyle[] getConditionalStyles();
}
