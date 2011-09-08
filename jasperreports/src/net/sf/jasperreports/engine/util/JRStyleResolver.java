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
package net.sf.jasperreports.engine.util;

import java.awt.Color;

import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.JRTimeAxisFormat;
import net.sf.jasperreports.charts.JRValueAxisFormat;
import net.sf.jasperreports.charts.JRXAxisFormat;
import net.sf.jasperreports.charts.JRYAxisFormat;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRCommonImage;
import net.sf.jasperreports.engine.JRCommonRectangle;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class JRStyleResolver
{

	private static final Integer INTEGER_ZERO = Integer.valueOf(0);

	/**
	 *
	 */
	public static JRStyle getBaseStyle(JRStyleContainer styleContainer)
	{
		if (styleContainer != null)
		{
			JRStyle style = styleContainer.getStyle();
			if (style != null)
			{
				return style;
			}
			JRDefaultStyleProvider defaultStyleProvider = styleContainer.getDefaultStyleProvider();
			if (defaultStyleProvider != null)
			{
				return defaultStyleProvider.getDefaultStyle();
			}
		}
		return null;
	}


	/**
	 *
	 */
	public static ModeEnum getMode(JRCommonElement element, ModeEnum defaultMode)
	{
		ModeEnum ownMode = element.getOwnModeValue();
		if (ownMode != null) 
		{
			return ownMode;
		}
		JRStyle style = getBaseStyle(element);
		if (style != null)
		{
			ModeEnum mode = style.getModeValue();
			if (mode != null)
			{
				return mode;
			}
		}
		return defaultMode;
	}

	/**
	 *
	 */
	public static ModeEnum getModeValue(JRStyle style)
	{
		ModeEnum ownMode = style.getOwnModeValue();
		if (ownMode != null)
		{
			return ownMode;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getModeValue();
		}
		return null;
	}

	/**
	 *
	 */
	public static Color getForecolor(JRCommonElement element)
	{
		Color ownForecolor = element.getOwnForecolor();
		if (ownForecolor != null) 
		{
			return ownForecolor;
		}
		JRStyle style = getBaseStyle(element);
		if (style != null)
		{
			Color forecolor = style.getForecolor();
			if (forecolor != null)
			{
				return forecolor;
			}
		}
		return Color.black;
	}

	/**
	 *
	 */
	public static Color getForecolor(JRChartPlot plot)
	{
		JRChart chart = plot.getChart();
		if (chart != null)
		{
			return getForecolor(chart);
		}
		return Color.black;
	}

	/**
	 *
	 */
	public static Color getForecolor(JRStyle style)
	{
		Color ownForecolor = style.getOwnForecolor();
		if (ownForecolor != null)
		{
			return ownForecolor;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getForecolor();
		}
		return null;
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRCommonElement element)
	{
		Color ownBackcolor = element.getOwnBackcolor();
		if (ownBackcolor != null) 
		{
			return ownBackcolor;
		}
		JRStyle style = getBaseStyle(element);
		if (style != null)
		{
			Color backcolor = style.getBackcolor();
			if (backcolor != null)
			{
				return backcolor;
			}
		}
		return Color.white;
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRChartPlot plot)
	{
		Color ownBackcolor = plot.getOwnBackcolor();
		if (ownBackcolor != null)
		{
			return ownBackcolor;
		}
		JRChart chart = plot.getChart();
		if (chart != null)
		{
			return getBackcolor(chart);
		}
		return Color.white;
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRStyle style)
	{
		Color ownBackcolor = style.getOwnBackcolor();
		if (ownBackcolor != null)
		{
			return ownBackcolor;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getBackcolor();
		}
		return null;
	}

	/**
	 *
	 */
	public static Float getLineWidth(JRPen pen, Float defaultLineWidth)
	{
		Float ownLineWidth = pen.getOwnLineWidth();
		if (ownLineWidth != null)
		{
			return ownLineWidth;
		}
		JRStyle baseStyle = getBaseStyle(pen.getStyleContainer());
		if (baseStyle != null)
		{
			Float lineWidth = baseStyle.getLinePen().getLineWidth();
			if (lineWidth != null)
			{
				return lineWidth;
			}
		}
		return defaultLineWidth;
	}

	/**
	 *
	 */
	public static Float getLineWidth(JRBoxPen boxPen, Float defaultLineWidth)
	{
		Float ownLineWidth = boxPen.getOwnLineWidth();
		if (ownLineWidth != null)
		{
			return ownLineWidth;
		}
		Float penLineWidth = boxPen.getBox().getPen().getOwnLineWidth();
		if (penLineWidth != null) 
		{
			return penLineWidth;
		}
		JRStyle baseStyle = getBaseStyle(boxPen.getStyleContainer());
		if (baseStyle != null)
		{
			Float lineWidth = boxPen.getPen(baseStyle.getLineBox()).getLineWidth();
			if (lineWidth != null)
			{
				return lineWidth;
			}
		}
		return defaultLineWidth;
	}

	/**
	 *
	 */
	public static LineStyleEnum getLineStyleValue(JRPen pen)
	{
		LineStyleEnum ownLineStyle = pen.getOwnLineStyleValue();
		if (ownLineStyle != null)
		{
			return ownLineStyle;
		}
		JRStyle baseStyle = getBaseStyle(pen.getStyleContainer());
		if (baseStyle != null)
		{
			LineStyleEnum lineStyle = baseStyle.getLinePen().getLineStyleValue();
			if (lineStyle != null)
			{
				return lineStyle;
			}
		}
		return LineStyleEnum.SOLID;
	}

	/**
	 *
	 */
	public static LineStyleEnum getLineStyleValue(JRBoxPen boxPen)
	{
		LineStyleEnum ownLineStyle = boxPen.getOwnLineStyleValue();
		if (ownLineStyle != null)
		{
			return ownLineStyle;
		}
		LineStyleEnum penLineStyle = boxPen.getBox().getPen().getOwnLineStyleValue();
		if (penLineStyle != null)
		{
			return penLineStyle;
		}
		JRStyle baseStyle = getBaseStyle(boxPen.getStyleContainer());
		if (baseStyle != null)
		{
			LineStyleEnum lineStyle = boxPen.getPen(baseStyle.getLineBox()).getLineStyleValue();
			if (lineStyle != null)
			{
				return lineStyle;
			}
		}
		return LineStyleEnum.SOLID;
	}

	/**
	 *
	 */
	public static Color getLineColor(JRPen pen, Color defaultColor)
	{
		Color ownLineColor = pen.getOwnLineColor();
		if (ownLineColor != null)
		{
			return ownLineColor;
		}
		JRStyle baseStyle = getBaseStyle(pen.getStyleContainer());
		if (baseStyle != null)
		{
			Color lineColor = baseStyle.getLinePen().getLineColor();
			if (lineColor != null)
			{
				return lineColor;
			}
		}
		return defaultColor;
	}

	/**
	 *
	 */
	public static Color getLineColor(JRBoxPen boxPen, Color defaultColor)
	{
		Color ownLineColor = boxPen.getOwnLineColor();
		if (ownLineColor != null)
		{
			return ownLineColor;
		}
		Color penLineColor = boxPen.getBox().getPen().getOwnLineColor();
		if (penLineColor != null)
		{
			return penLineColor;
		}
		JRStyle baseStyle = getBaseStyle(boxPen.getStyleContainer());
		if (baseStyle != null)
		{
			Color lineColor = boxPen.getPen(baseStyle.getLineBox()).getLineColor();
			if (lineColor != null)
			{
				return lineColor;
			}
		}
		return defaultColor;
	}

	/**
	 *
	 */
	public static FillEnum getFillValue(JRCommonGraphicElement element)
	{
		FillEnum ownFill = element.getOwnFillValue();
		if (ownFill != null)
		{
			return ownFill;
		}
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null)
		{
			FillEnum fill = baseStyle.getFillValue();
			if (fill != null)
			{
				return fill;
			}
		}
		return FillEnum.SOLID;
	}

	/**
	 *
	 */
	public static FillEnum getFillValue(JRStyle style)
	{
		FillEnum ownFill = style.getOwnFillValue();
		if (ownFill != null)
		{
			return ownFill;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getFillValue();
		}
		return null;
	}

	/**
	 *
	 */
	public static int getRadius(JRCommonRectangle rectangle)
	{
		Integer ownRadius = rectangle.getOwnRadius();
		if (ownRadius != null)
		{
			return ownRadius.intValue();
		}
		JRStyle baseStyle = getBaseStyle(rectangle);
		if (baseStyle != null)
		{
			Integer radius = baseStyle.getRadius();
			if (radius != null)
			{
				return radius.intValue();
			}
		}
		return 0;
	}

	/**
	 *
	 */
	public static Integer getRadius(JRStyle style)
	{
		Integer ownRadius = style.getOwnRadius();
		if (ownRadius != null)
		{
			return ownRadius;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getRadius();
		}
		return null;
	}

	/**
	 *
	 */
	public static ScaleImageEnum getScaleImageValue(JRCommonImage image)
	{
		ScaleImageEnum ownScaleImage = image.getOwnScaleImageValue();
		if (ownScaleImage != null)
		{
			return ownScaleImage;
		}
		JRStyle baseStyle = getBaseStyle(image);
		if (baseStyle != null)
		{
			ScaleImageEnum scaleImage = baseStyle.getScaleImageValue();
			if (scaleImage != null)
			{
				return scaleImage;
			}
		}
		return ScaleImageEnum.RETAIN_SHAPE;
	}

	/**
	 *
	 */
	public static ScaleImageEnum getScaleImageValue(JRStyle style)
	{
		ScaleImageEnum ownScaleImage = style.getOwnScaleImageValue();
		if (ownScaleImage != null)
		{
			return ownScaleImage;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null )
		{
			return baseStyle.getScaleImageValue();
		}
		return null;
	}

	/**
	 *
	 */
	public static HorizontalAlignEnum getHorizontalAlignmentValue(JRAlignment alignment)
	{
		HorizontalAlignEnum ownHorizontalAlignment = alignment.getOwnHorizontalAlignmentValue();
		if (ownHorizontalAlignment != null)
		{
			return ownHorizontalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			HorizontalAlignEnum horizontalAlignment = baseStyle.getHorizontalAlignmentValue();
			if (horizontalAlignment != null)
			{
				return horizontalAlignment;
			}
		}
		return HorizontalAlignEnum.LEFT;
	}

	/**
	 *
	 */
	public static HorizontalAlignEnum getHorizontalAlignmentValue(JRStyle style)
	{
		HorizontalAlignEnum ownHorizontalAlignment = style.getOwnHorizontalAlignmentValue();
		if (ownHorizontalAlignment != null)
		{
			return ownHorizontalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getHorizontalAlignmentValue();
		}
		return null;
	}

	/**
	 *
	 */
	public static VerticalAlignEnum getVerticalAlignmentValue(JRAlignment alignment)
	{
		VerticalAlignEnum ownVerticalAlignment = alignment.getOwnVerticalAlignmentValue();
		if (ownVerticalAlignment != null)
		{
			return ownVerticalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			VerticalAlignEnum verticalAlignment = baseStyle.getVerticalAlignmentValue();
			if (verticalAlignment != null)
			{
				return verticalAlignment;
			}
		}
		return VerticalAlignEnum.TOP;
	}

	/**
	 *
	 */
	public static Byte getVerticalAlignment(JRStyle style)
	{
		VerticalAlignEnum verticalAlignment =getVerticalAlignmentValue(style);
		return verticalAlignment == null ? null : verticalAlignment.getValueByte();
	}

	/**
	 *
	 */
	public static VerticalAlignEnum getVerticalAlignmentValue(JRStyle style)
	{
		VerticalAlignEnum ownVerticalAlignment = style.getOwnVerticalAlignmentValue();
		if (ownVerticalAlignment != null)
		{
			return ownVerticalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getVerticalAlignmentValue();
		}
		return null;
	}

	/**
	 *
	 */
	public static Float getLineSpacingSize(JRParagraph paragraph)
	{
		Float ownLineSpacingSize = paragraph.getOwnLineSpacingSize();
		if (ownLineSpacingSize != null)
		{
			return ownLineSpacingSize;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			Float lineSpacingSize = style.getParagraph().getLineSpacingSize();
			if (lineSpacingSize != null)
			{
				return lineSpacingSize;
			}
		}
		return JRProperties.getFloatProperty(JRParagraph.DEFAULT_LINE_SPACING_SIZE);
	}

	/**
	 *
	 */
	public static Integer getFirstLineIndent(JRParagraph paragraph)
	{
		Integer ownFirstLineIndent = paragraph.getOwnFirstLineIndent();
		if (ownFirstLineIndent != null)
		{
			return ownFirstLineIndent;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			Integer firstLineIndent = style.getParagraph().getFirstLineIndent();
			if (firstLineIndent != null)
			{
				return firstLineIndent;
			}
		}
		return JRProperties.getIntegerProperty(JRParagraph.DEFAULT_FIRST_LINE_INDENT);
	}

	/**
	 *
	 */
	public static Integer getLeftIndent(JRParagraph paragraph)
	{
		Integer ownLeftIndent = paragraph.getOwnLeftIndent();
		if (ownLeftIndent != null)
		{
			return ownLeftIndent;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			Integer leftIndent = style.getParagraph().getLeftIndent();
			if (leftIndent != null)
			{
				return leftIndent;
			}
		}
		return JRProperties.getIntegerProperty(JRParagraph.DEFAULT_LEFT_INDENT);
	}

	/**
	 *
	 */
	public static Integer getRightIndent(JRParagraph paragraph)
	{
		Integer ownRightIndent = paragraph.getOwnRightIndent();
		if (ownRightIndent != null)
		{
			return ownRightIndent;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			Integer rightIndent = style.getParagraph().getRightIndent();
			if (rightIndent != null)
			{
				return rightIndent;
			}
		}
		return JRProperties.getIntegerProperty(JRParagraph.DEFAULT_RIGHT_INDENT);
	}

	/**
	 *
	 */
	public static Integer getSpacingBefore(JRParagraph paragraph)
	{
		Integer ownSpacingBefore = paragraph.getOwnSpacingBefore();
		if (ownSpacingBefore != null)
		{
			return ownSpacingBefore;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			Integer spacingBefore = style.getParagraph().getSpacingBefore();
			if (spacingBefore != null)
			{
				return spacingBefore;
			}
		}
		return JRProperties.getIntegerProperty(JRParagraph.DEFAULT_SPACING_BEFORE);
	}

	/**
	 *
	 */
	public static Integer getSpacingAfter(JRParagraph paragraph)
	{
		Integer ownSpacingAfter = paragraph.getOwnSpacingAfter();
		if (ownSpacingAfter != null)
		{
			return ownSpacingAfter;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			Integer spacingAfter = style.getParagraph().getSpacingAfter();
			if (spacingAfter != null)
			{
				return spacingAfter;
			}
		}
		return JRProperties.getIntegerProperty(JRParagraph.DEFAULT_SPACING_AFTER);
	}

	/**
	 *
	 */
	public static Integer getTabStopWidth(JRParagraph paragraph)
	{
		Integer ownTabStopWidth = paragraph.getOwnTabStopWidth();
		if (ownTabStopWidth != null)
		{
			return ownTabStopWidth;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			Integer tabStopWidth = style.getParagraph().getTabStopWidth();
			if (tabStopWidth != null)
			{
				return tabStopWidth;
			}
		}
		return JRProperties.getIntegerProperty(JRParagraph.DEFAULT_TAB_STOP_WIDTH);
	}

	/**
	 *
	 */
	public static TabStop[] getTabStops(JRParagraph paragraph)
	{
		TabStop[] ownTabStops = paragraph.getOwnTabStops();
		if (ownTabStops != null)
		{
			return ownTabStops;
		}
		JRStyle style = getBaseStyle(paragraph);
		if (style != null)
		{
			TabStop[] tabStops = style.getParagraph().getTabStops();
			if (tabStops != null)
			{
				return tabStops;
			}
		}
		return null;
	}

	/**
	 *
	 */
	public static RotationEnum getRotationValue(JRCommonText element)
	{
		RotationEnum ownRotation = element.getOwnRotationValue();
		if (ownRotation != null)
		{
			return ownRotation;
		}
		JRStyle style = getBaseStyle(element);
		if (style != null)
		{
			RotationEnum rotation = style.getRotationValue();
			if (rotation != null)
			{
				return rotation;
			}
		}
		return RotationEnum.NONE;
	}

	/**
	 *
	 */
	public static RotationEnum getRotationValue(JRStyle style)
	{
		RotationEnum ownRotation = style.getOwnRotationValue();
		if (ownRotation != null)
		{
			return ownRotation;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getRotationValue();
		}
		return null;
	}

	/**
	 * @deprecated Replaced by {@link #getLineSpacing(JRParagraph)}.
	 */
	public static LineSpacingEnum getLineSpacingValue(JRCommonText element)
	{
		return getLineSpacing(element.getParagraph());
	}

	/**
	 * @deprecated Replaced by {@link #getLineSpacing(JRParagraph)}.
	 */
	public static LineSpacingEnum getLineSpacingValue(JRStyle style)
	{
		return getLineSpacing(style.getParagraph());
	}

	/**
	 * 
	 */
	public static LineSpacingEnum getLineSpacing(JRParagraph paragraph)
	{
		LineSpacingEnum ownLineSpacing = paragraph.getOwnLineSpacing();
		if (ownLineSpacing != null)
		{
			return ownLineSpacing;
		}
		JRStyle baseStyle = getBaseStyle(paragraph);
		if (baseStyle != null)
		{
			LineSpacingEnum lineSpacing = baseStyle.getParagraph().getLineSpacing();
			if (lineSpacing != null)
			{
				return lineSpacing;
			}
		}
		return LineSpacingEnum.SINGLE;//FIXMENOW could we make all enums in default props?
	}

	/**
	 *
	 */
	public static String getMarkup(JRCommonText element)
	{
		String ownMarkup = element.getOwnMarkup();
		if (ownMarkup != null)
		{
			return ownMarkup;
		}
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null)
		{
			String markup = baseStyle.getMarkup();
			if (markup != null)
			{
				return markup;
			}
		}
		return JRCommonText.MARKUP_NONE;
	}

	/**
	 *
	 */
	public static String getMarkup(JRStyle style)
	{
		String ownMarkup = style.getOwnMarkup();
		if (ownMarkup != null)
		{
			return ownMarkup;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getMarkup();
		}
		return JRCommonText.MARKUP_NONE;
	}

	/**
	 *
	 */
	public static String getPattern(JRTextField element)
	{
		String ownPattern = element.getOwnPattern();
		if (ownPattern != null)
		{
			return ownPattern;
		}
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null)
		{
			return baseStyle.getPattern();
		}
		return null;
	}

	/**
	 *
	 */
	public static String getPattern(JRStyle style)
	{
		String ownPattern = style.getOwnPattern();
		if (ownPattern != null)
		{
			return ownPattern;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getPattern();
		}
		return null;
	}

	/**
	 *
	 */
	public static boolean isBlankWhenNull(JRTextField element)
	{
		Boolean ownBlankWhenNull = element.isOwnBlankWhenNull();
		if (ownBlankWhenNull != null)
		{
			return ownBlankWhenNull.booleanValue();
		}
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null)
		{
			Boolean blankWhenNull = baseStyle.isBlankWhenNull();
			if (blankWhenNull != null)
			{
				return blankWhenNull.booleanValue();
			}
		}
		return false;
	}

	/**
	 *
	 */
	public static Boolean isBlankWhenNull(JRStyle style)
	{
		Boolean ownBlankWhenNull = style.isOwnBlankWhenNull();
		if (ownBlankWhenNull != null)
		{
			return ownBlankWhenNull;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.isBlankWhenNull();
		}
		return null;
	}

	/**
	 *
	 */
	public static String getFontName(JRFont font)
	{
		String ownFontName = font.getOwnFontName();
		if (ownFontName != null)
		{
			return ownFontName;
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			String fontName = baseStyle.getFontName();
			if (fontName != null)
			{
				return fontName;
			}
		}
		return JRProperties.getProperty(JRFont.DEFAULT_FONT_NAME);
	}
	
	/**
	 *
	 */
	public static String getFontName(JRStyle style)
	{
		String ownFontName = style.getOwnFontName();
		if (ownFontName != null)
		{
			return ownFontName;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			String fontName = baseStyle.getFontName();
			if (fontName != null)
			{
				return fontName;
			}
		}
		return JRProperties.getProperty(JRFont.DEFAULT_FONT_NAME);
	}

	/**
	 *
	 */
	public static boolean isBold(JRFont font)
	{
		Boolean ownBold = font.isOwnBold();
		if (ownBold != null)
		{
			return ownBold.booleanValue();
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			Boolean bold = baseStyle.isBold();
			if (bold != null)
			{
				return bold.booleanValue();
			}
		}
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isBold(JRStyle style)
	{
		Boolean ownBold = style.isOwnBold();
		if (ownBold != null)
		{
			return ownBold;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.isBold();
		}
		return null;
	}

	/**
	 *
	 */
	public static boolean isItalic(JRFont font)
	{
		Boolean ownItalic = font.isOwnItalic();
		if (ownItalic != null)
		{
			return ownItalic.booleanValue();
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			Boolean italic = baseStyle.isItalic();
			if (italic != null)
			{
				return italic.booleanValue();
			}
		}
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isItalic(JRStyle style)
	{
		Boolean ownItalic = style.isOwnItalic();
		if (ownItalic != null)
		{
			return ownItalic;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.isItalic();
		}
		return null;
	}

	/**
	 *
	 */
	public static boolean isUnderline(JRFont font)
	{
		Boolean ownUnderline = font.isOwnUnderline();
		if (ownUnderline != null)
		{
			return ownUnderline.booleanValue();
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			Boolean underline = baseStyle.isUnderline();
			if (underline != null)
			{
				return underline.booleanValue();
			}
		}
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isUnderline(JRStyle style)
	{
		Boolean ownUnderline = style.isOwnUnderline();
		if (ownUnderline != null)
		{
			return ownUnderline;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.isUnderline();
		}
		return null;
	}

	/**
	 *
	 */
	public static boolean isStrikeThrough(JRFont font)
	{
		Boolean ownStrikeThrough = font.isOwnStrikeThrough();
		if (ownStrikeThrough != null)
		{
			return ownStrikeThrough.booleanValue();
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			Boolean strikeThrough = baseStyle.isStrikeThrough();
			if (strikeThrough != null)
			{
				return strikeThrough.booleanValue();
			}
		}
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isStrikeThrough(JRStyle style)
	{
		if (style.isOwnStrikeThrough() != null)
		{
			return style.isOwnStrikeThrough();
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.isStrikeThrough();
		}
		return null;
	}

	/**
	 *
	 */
	public static int getFontSize(JRFont font)
	{
		Integer ownFontSize = font.getOwnFontSize();
		if (ownFontSize != null)
		{
			return ownFontSize.intValue();
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			Integer fontSize = baseStyle.getFontSize();
			if (fontSize != null)
			{
				return fontSize.intValue();
			}
		}
		return JRProperties.getIntegerProperty(JRFont.DEFAULT_FONT_SIZE);
	}
	
	/**
	 *
	 */
	public static Integer getFontSize(JRStyle style)
	{
		Integer ownFontSize = style.getOwnFontSize();
		if (ownFontSize != null)
		{
			return ownFontSize;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getFontSize();
		}
		return null;
	}

	/**
	 *
	 */
	public static String getPdfFontName(JRFont font)
	{
		String ownPdfFontName = font.getOwnPdfFontName();
		if (ownPdfFontName != null)
		{
			return ownPdfFontName;
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			String pdfFontName = baseStyle.getPdfFontName();
			if (pdfFontName != null)
			{
				return pdfFontName;
			}
		}
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_FONT_NAME);
	}
	
	/**
	 *
	 */
	public static String getPdfFontName(JRStyle style)
	{
		String ownPdfFontName = style.getOwnPdfFontName();
		if (ownPdfFontName != null)
		{
			return ownPdfFontName;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			String pdfFontName = baseStyle.getPdfFontName();
			if (pdfFontName != null)
			{
				return pdfFontName;
			}
		}
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_FONT_NAME);
	}

	/**
	 *
	 */
	public static String getPdfEncoding(JRFont font)
	{
		String ownPdfEncoding = font.getOwnPdfEncoding();
		if (ownPdfEncoding != null)
		{
			return ownPdfEncoding;
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			String pdfEncoding = baseStyle.getPdfEncoding();
			if (pdfEncoding != null)
			{
				return pdfEncoding;
			}
		}
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_ENCODING);
	}
	
	/**
	 *
	 */
	public static String getPdfEncoding(JRStyle style)
	{
		String ownPdfEncoding = style.getOwnPdfEncoding();
		if (ownPdfEncoding != null)
		{
			return ownPdfEncoding;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			String pdfEncoding = baseStyle.getPdfEncoding();
			if (pdfEncoding != null)
			{
				return pdfEncoding;
			}
		}
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_ENCODING);
	}

	/**
	 *
	 */
	public static boolean isPdfEmbedded(JRFont font)
	{
		Boolean ownPdfEmbedded = font.isOwnPdfEmbedded();
		if (ownPdfEmbedded != null)
		{
			return ownPdfEmbedded.booleanValue();
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			Boolean pdfEmbedded = baseStyle.isPdfEmbedded();
			if (pdfEmbedded != null)
			{
				return pdfEmbedded.booleanValue();
			}
		}
		return JRProperties.getBooleanProperty(JRFont.DEFAULT_PDF_EMBEDDED);
	}
	
	/**
	 *
	 */
	public static Boolean isPdfEmbedded(JRStyle style)
	{
		Boolean ownPdfEmbedded = style.isOwnPdfEmbedded();
		if (ownPdfEmbedded != null)
		{
			return ownPdfEmbedded;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.isPdfEmbedded();
		}
		return null;
	}

	/**
	 *
	 */
	public static Integer getPadding(JRLineBox box)
	{
		Integer ownPadding = box.getOwnPadding();
		if (ownPadding != null)
		{
			return ownPadding;
		}
		JRStyle baseStyle = getBaseStyle(box);
		if (baseStyle != null)
		{
			Integer padding = baseStyle.getLineBox().getPadding();
			if (padding != null)
			{
				return padding;
			}
		}
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getTopPadding(JRLineBox box)
	{
		Integer ownTopPadding = box.getOwnTopPadding();
		if (ownTopPadding != null)
		{
			return ownTopPadding;
		}
		Integer ownPadding = box.getOwnPadding();
		if (ownPadding != null)
		{
			return ownPadding;
		}
		JRStyle style = getBaseStyle(box);
		if (style != null)
		{
			Integer topPadding = style.getLineBox().getTopPadding();
			if (topPadding != null)
			{
				return topPadding;
			}
		}
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getLeftPadding(JRLineBox box)
	{
		Integer ownLeftPadding = box.getOwnLeftPadding();
		if (ownLeftPadding != null)
		{
			return ownLeftPadding;
		}
		Integer ownPadding = box.getOwnPadding();
		if (ownPadding != null)
		{
			return ownPadding;
		}
		JRStyle style = getBaseStyle(box);
		if (style != null)
		{
			Integer leftPadding = style.getLineBox().getLeftPadding();
			if (leftPadding != null)
			{
				return leftPadding;
			}
		}
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getBottomPadding(JRLineBox box)
	{
		Integer ownBottomPadding = box.getOwnBottomPadding();
		if (ownBottomPadding != null)
		{
			return ownBottomPadding;
		}
		Integer ownPadding = box.getOwnPadding();
		if (ownPadding != null)
		{
			return ownPadding;
		}
		JRStyle style = getBaseStyle(box);
		if (style != null)
		{
			Integer bottomPadding = style.getLineBox().getBottomPadding();
			if (bottomPadding != null)
			{
				return bottomPadding;
			}
		}
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getRightPadding(JRLineBox box)
	{
		Integer ownRightPadding = box.getOwnRightPadding();
		if (ownRightPadding != null)
		{
			return ownRightPadding;
		}
		Integer ownPadding = box.getOwnPadding();
		if (ownPadding != null)
		{
			return ownPadding;
		}
		JRStyle style = getBaseStyle(box);
		if (style != null)
		{
			Integer rightPadding = style.getLineBox().getRightPadding();
			if (rightPadding != null)
			{
				return rightPadding;
			}
		}
		return INTEGER_ZERO;
	}


	/**
	 * Merges two styles, by appending the properties of the source style to the ones of the destination style.
	 */
	public static void appendStyle(JRStyle destStyle, JRStyle srcStyle)
	{
		if (srcStyle.getOwnModeValue() != null)
		{
			destStyle.setMode(srcStyle.getOwnModeValue());
		}
		if (srcStyle.getOwnForecolor() != null)
		{
			destStyle.setForecolor(srcStyle.getOwnForecolor());
		}
		if (srcStyle.getOwnBackcolor() != null)
		{
			destStyle.setBackcolor(srcStyle.getOwnBackcolor());
		}
		appendPen(destStyle.getLinePen(), srcStyle.getLinePen());
		
		if (srcStyle.getOwnFillValue() != null)
		{
			destStyle.setFill(srcStyle.getOwnFillValue());
		}
		if (srcStyle.getOwnRadius() != null)
		{
			destStyle.setRadius(srcStyle.getOwnRadius());
		}
		if (srcStyle.getOwnScaleImageValue() != null)
		{
			destStyle.setScaleImage(srcStyle.getOwnScaleImageValue());
		}
		if (srcStyle.getOwnHorizontalAlignmentValue() != null)
		{
			destStyle.setHorizontalAlignment(srcStyle.getOwnHorizontalAlignmentValue());
		}
		if (srcStyle.getOwnVerticalAlignmentValue() != null)
		{
			destStyle.setVerticalAlignment(srcStyle.getOwnVerticalAlignmentValue());
		}
		appendBox(destStyle.getLineBox(), srcStyle.getLineBox());
		appendParagraph(destStyle.getParagraph(), srcStyle.getParagraph());

		if (srcStyle.getOwnRotationValue() != null)
		{
			destStyle.setRotation(srcStyle.getOwnRotationValue());
		}
		if (srcStyle.getOwnMarkup() != null)
		{
			destStyle.setMarkup(srcStyle.getOwnMarkup());
		}
		if (srcStyle.getOwnPattern() != null)
		{
			destStyle.setPattern(srcStyle.getOwnPattern());
		}
		if (srcStyle.getOwnFontName() != null)
		{
			destStyle.setFontName(srcStyle.getOwnFontName());
		}
		if (srcStyle.isOwnBold() != null)
		{
			destStyle.setBold(srcStyle.isOwnBold());
		}
		if (srcStyle.isOwnItalic() != null)
		{
			destStyle.setItalic(srcStyle.isOwnItalic());
		}
		if (srcStyle.isOwnUnderline() != null)
		{
			destStyle.setUnderline(srcStyle.isOwnUnderline());
		}
		if (srcStyle.isOwnStrikeThrough() != null)
		{
			destStyle.setStrikeThrough(srcStyle.isOwnStrikeThrough());
		}
		if (srcStyle.getOwnFontSize() != null)
		{
			destStyle.setFontSize(srcStyle.getOwnFontSize());
		}
		if (srcStyle.getOwnPdfFontName() != null)
		{
			destStyle.setPdfFontName(srcStyle.getOwnPdfFontName());
		}
		if (srcStyle.getOwnPdfEncoding() != null)
		{
			destStyle.setPdfEncoding(srcStyle.getOwnPdfEncoding());
		}
		if (srcStyle.isOwnPdfEmbedded() != null)
		{
			destStyle.setPdfEmbedded(srcStyle.isOwnPdfEmbedded());
		}
	}

	/**
	 * Merges two pens, by appending the properties of the source pen to the ones of the destination pen.
	 */
	private static void appendPen(JRPen destPen, JRPen srcPen)
	{
		if (srcPen.getOwnLineWidth() != null)
		{
			destPen.setLineWidth(srcPen.getOwnLineWidth());
		}
		if (srcPen.getOwnLineStyleValue() != null)
		{
			destPen.setLineStyle(srcPen.getOwnLineStyleValue());
		}
		if (srcPen.getOwnLineColor() != null)
		{
			destPen.setLineColor(srcPen.getOwnLineColor());
		}
	}

	/**
	 * Merges two boxes, by appending the properties of the source box to the ones of the destination box.
	 */
	private static void appendBox(JRLineBox destBox, JRLineBox srcBox)
	{
		appendPen(destBox.getPen(), srcBox.getPen());
		appendPen(destBox.getTopPen(), srcBox.getTopPen());
		appendPen(destBox.getLeftPen(), srcBox.getLeftPen());
		appendPen(destBox.getBottomPen(), srcBox.getBottomPen());
		appendPen(destBox.getRightPen(), srcBox.getRightPen());

		if (srcBox.getOwnPadding() != null)
		{
			destBox.setPadding(srcBox.getOwnPadding());
		}
		if (srcBox.getOwnTopPadding() != null)
		{
			destBox.setTopPadding(srcBox.getOwnTopPadding());
		}
		if (srcBox.getOwnLeftPadding() != null)
		{
			destBox.setLeftPadding(srcBox.getOwnLeftPadding());
		}
		if (srcBox.getOwnBottomPadding() != null)
		{
			destBox.setBottomPadding(srcBox.getOwnBottomPadding());
		}
		if (srcBox.getOwnRightPadding() != null)
		{
			destBox.setRightPadding(srcBox.getOwnRightPadding());
		}
	}

	/**
	 * Merges two paragraphs, by appending the properties of the source paragraph to the ones of the destination paragraph.
	 */
	private static void appendParagraph(JRParagraph destParagraph, JRParagraph srcParagraph)
	{
		if (srcParagraph.getOwnLineSpacing() != null)
		{
			destParagraph.setLineSpacing(srcParagraph.getOwnLineSpacing());
		}
		if (srcParagraph.getOwnLeftIndent() != null)
		{
			destParagraph.setLeftIndent(srcParagraph.getOwnLeftIndent());
		}
		if (srcParagraph.getOwnRightIndent() != null)
		{
			destParagraph.setRightIndent(srcParagraph.getOwnRightIndent());
		}
		if (srcParagraph.getOwnSpacingBefore() != null)
		{
			destParagraph.setSpacingBefore(srcParagraph.getOwnSpacingBefore());
		}
		if (srcParagraph.getOwnSpacingAfter() != null)
		{
			destParagraph.setSpacingAfter(srcParagraph.getOwnSpacingAfter());
		}
		if (srcParagraph.getOwnTabStopWidth() != null)
		{
			destParagraph.setTabStopWidth(srcParagraph.getOwnTabStopWidth());
		}
	}

	/**
	 *
	 */
	public static Color getTitleColor(JRChart chart)
	{
		Color ownTitleColor = chart.getOwnTitleColor();
		if (ownTitleColor != null)
		{
			return ownTitleColor;
		}
		return getForecolor(chart);
	}

	/**
	 *
	 */
	public static Color getSubtitleColor(JRChart chart)
	{
		Color ownSubtitleColor = chart.getOwnSubtitleColor();
		if (ownSubtitleColor != null)
		{
			return ownSubtitleColor;
		}
		return getForecolor(chart);
	}

	/**
	 *
	 */
	public static Color getLegendColor(JRChart chart)
	{
		Color ownLegendColor = chart.getOwnLegendColor();
		if (ownLegendColor != null)
		{
			return ownLegendColor;
		}
		return getForecolor(chart);
	}

	/**
	 *
	 */
	public static Color getLegendBackgroundColor(JRChart chart)
	{
		Color ownLegendBackgroundColor = chart.getOwnLegendBackgroundColor();
		if (ownLegendBackgroundColor != null)
		{
			return ownLegendBackgroundColor;
		}
		return getBackcolor(chart);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownCategoryAxisLabelColor = axisFormat.getOwnCategoryAxisLabelColor();
		if (ownCategoryAxisLabelColor != null) 
		{
			return ownCategoryAxisLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisTickLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownCategoryAxisTickLabelColor = axisFormat.getOwnCategoryAxisTickLabelColor();
		if (ownCategoryAxisTickLabelColor != null)
		{
			return ownCategoryAxisTickLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisLineColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownCategoryAxisLineColor = axisFormat.getOwnCategoryAxisLineColor();
		if (ownCategoryAxisLineColor != null)
		{
			return ownCategoryAxisLineColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownValueAxisLabelColor = axisFormat.getOwnValueAxisLabelColor();
		if (ownValueAxisLabelColor != null)
		{
			return ownValueAxisLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisTickLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownValueAxisTickLabelColor = axisFormat.getOwnValueAxisTickLabelColor();
		if (ownValueAxisTickLabelColor != null) 
		{
			return ownValueAxisTickLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisLineColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownValueAxisLineColor = axisFormat.getOwnValueAxisLineColor();
		if (ownValueAxisLineColor != null) 
		{
			return ownValueAxisLineColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getXAxisLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownXAxisLabelColor = axisFormat.getOwnXAxisLabelColor();
		if (ownXAxisLabelColor != null) 
		{
			return ownXAxisLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getXAxisTickLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownXAxisTickLabelColor = axisFormat.getOwnXAxisTickLabelColor();
		if (ownXAxisTickLabelColor != null) 
		{
			return ownXAxisTickLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getXAxisLineColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownXAxisLineColor = axisFormat.getOwnXAxisLineColor();
		if (ownXAxisLineColor != null) 
		{
			return ownXAxisLineColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getYAxisLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownYAxisLabelColor = axisFormat.getOwnYAxisLabelColor();
		if (ownYAxisLabelColor != null)
		{
			return ownYAxisLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getYAxisTickLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownYAxisTickLabelColor = axisFormat.getOwnYAxisTickLabelColor();
		if (ownYAxisTickLabelColor != null) 
		{
			return ownYAxisTickLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getYAxisLineColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownYAxisLineColor = axisFormat.getOwnYAxisLineColor();
		if (ownYAxisLineColor != null) 
		{
			return ownYAxisLineColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownTimeAxisLabelColor = axisFormat.getOwnTimeAxisLabelColor();
		if (ownTimeAxisLabelColor != null) 
		{
			return ownTimeAxisLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisTickLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownTimeAxisTickLabelColor = axisFormat.getOwnTimeAxisTickLabelColor();
		if (ownTimeAxisTickLabelColor != null) 
		{
			return ownTimeAxisTickLabelColor;
		}
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisLineColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownTimeAxisLineColor = axisFormat.getOwnTimeAxisLineColor();
		if (ownTimeAxisLineColor != null) 
		{
			return ownTimeAxisLineColor;
		}
		return getForecolor(plot);
	}

	
	private JRStyleResolver()
	{
	}
}
