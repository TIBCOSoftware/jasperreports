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
package net.sf.jasperreports.engine.util;

import java.awt.Color;

import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.JRTimeAxisFormat;
import net.sf.jasperreports.charts.JRValueAxisFormat;
import net.sf.jasperreports.charts.JRXAxisFormat;
import net.sf.jasperreports.charts.JRYAxisFormat;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRCommonImage;
import net.sf.jasperreports.engine.JRCommonRectangle;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRImageAlignment;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRTextAlignment;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;


/**
 * @deprecated Replaced by {@link StyleResolver}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class JRStyleResolver
{
	private static final StyleResolver styleResolver = StyleResolver.getInstance();


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
	 * @deprecated Replaced by {@link #getHorizontalTextAlign(JRTextAlignment)} and {@link #getHorizontalImageAlign(JRImageAlignment)}.
	 */
	public static net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue(net.sf.jasperreports.engine.JRAlignment alignment)
	{
		net.sf.jasperreports.engine.type.HorizontalAlignEnum ownHorizontalAlignment = alignment.getOwnHorizontalAlignmentValue();
		if (ownHorizontalAlignment != null)
		{
			return ownHorizontalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			net.sf.jasperreports.engine.type.HorizontalAlignEnum horizontalAlignment = baseStyle.getHorizontalAlignmentValue();
			if (horizontalAlignment != null)
			{
				return horizontalAlignment;
			}
		}
		return net.sf.jasperreports.engine.type.HorizontalAlignEnum.LEFT;
	}

	/**
	 *
	 */
	public static HorizontalTextAlignEnum getHorizontalTextAlign(JRTextAlignment alignment)
	{
		HorizontalTextAlignEnum ownHorizontalAlignment = alignment.getOwnHorizontalTextAlign();
		if (ownHorizontalAlignment != null)
		{
			return ownHorizontalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			HorizontalTextAlignEnum horizontalAlignment = baseStyle.getHorizontalTextAlign();
			if (horizontalAlignment != null)
			{
				return horizontalAlignment;
			}
		}
		return HorizontalTextAlignEnum.LEFT;
	}

	/**
	 *
	 */
	public static HorizontalImageAlignEnum getHorizontalImageAlign(JRImageAlignment alignment)
	{
		HorizontalImageAlignEnum ownHorizontalAlignment = alignment.getOwnHorizontalImageAlign();
		if (ownHorizontalAlignment != null)
		{
			return ownHorizontalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			HorizontalImageAlignEnum horizontalAlignment = baseStyle.getHorizontalImageAlign();
			if (horizontalAlignment != null)
			{
				return horizontalAlignment;
			}
		}
		return HorizontalImageAlignEnum.LEFT;
	}

	/**
	 * @deprecated Replaced by {@link #getHorizontalTextAlign(JRStyle)} and {@link #getHorizontalImageAlign(JRStyle)}.
	 */
	public static net.sf.jasperreports.engine.type.HorizontalAlignEnum getHorizontalAlignmentValue(JRStyle style)
	{
		net.sf.jasperreports.engine.type.HorizontalAlignEnum ownHorizontalAlignment = style.getOwnHorizontalAlignmentValue();
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
	public static HorizontalTextAlignEnum getHorizontalTextAlign(JRStyle style)
	{
		HorizontalTextAlignEnum ownHorizontalAlignment = style.getOwnHorizontalTextAlign();
		if (ownHorizontalAlignment != null)
		{
			return ownHorizontalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getHorizontalTextAlign();
		}
		return null;
	}

	/**
	 *
	 */
	public static HorizontalImageAlignEnum getHorizontalImageAlign(JRStyle style)
	{
		HorizontalImageAlignEnum ownHorizontalAlignment = style.getOwnHorizontalImageAlign();
		if (ownHorizontalAlignment != null)
		{
			return ownHorizontalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getHorizontalImageAlign();
		}
		return null;
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalTextAlign(JRTextAlignment)} and {@link #getVerticalImageAlign(JRImageAlignment)}.
	 */
	public static net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue(net.sf.jasperreports.engine.JRAlignment alignment)
	{
		net.sf.jasperreports.engine.type.VerticalAlignEnum ownVerticalAlignment = alignment.getOwnVerticalAlignmentValue();
		if (ownVerticalAlignment != null)
		{
			return ownVerticalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignment = baseStyle.getVerticalAlignmentValue();
			if (verticalAlignment != null)
			{
				return verticalAlignment;
			}
		}
		return net.sf.jasperreports.engine.type.VerticalAlignEnum.TOP;
	}

	/**
	 *
	 */
	public static VerticalTextAlignEnum getVerticalTextAlign(JRTextAlignment alignment)
	{
		VerticalTextAlignEnum ownVerticalAlignment = alignment.getOwnVerticalTextAlign();
		if (ownVerticalAlignment != null)
		{
			return ownVerticalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			VerticalTextAlignEnum verticalAlignment = baseStyle.getVerticalTextAlign();
			if (verticalAlignment != null)
			{
				return verticalAlignment;
			}
		}
		return VerticalTextAlignEnum.TOP;
	}

	/**
	 *
	 */
	public static VerticalImageAlignEnum getVerticalImageAlign(JRImageAlignment alignment)
	{
		VerticalImageAlignEnum ownVerticalAlignment = alignment.getOwnVerticalImageAlign();
		if (ownVerticalAlignment != null)
		{
			return ownVerticalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null)
		{
			VerticalImageAlignEnum verticalAlignment = baseStyle.getVerticalImageAlign();
			if (verticalAlignment != null)
			{
				return verticalAlignment;
			}
		}
		return VerticalImageAlignEnum.TOP;
	}

	/**
	 * @deprecated To be removed.
	 */
	public static Byte getVerticalAlignment(JRStyle style)
	{
		net.sf.jasperreports.engine.type.VerticalAlignEnum verticalAlignment = getVerticalAlignmentValue(style);
		return verticalAlignment == null ? null : verticalAlignment.getValueByte();
	}

	/**
	 * @deprecated Replaced by {@link #getVerticalTextAlign(JRStyle)} and {@link #getVerticalImageAlign(JRStyle)}.
	 */
	public static net.sf.jasperreports.engine.type.VerticalAlignEnum getVerticalAlignmentValue(JRStyle style)
	{
		net.sf.jasperreports.engine.type.VerticalAlignEnum ownVerticalAlignment = style.getOwnVerticalAlignmentValue();
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
	public static VerticalTextAlignEnum getVerticalTextAlign(JRStyle style)
	{
		VerticalTextAlignEnum ownVerticalAlignment = style.getOwnVerticalTextAlign();
		if (ownVerticalAlignment != null)
		{
			return ownVerticalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getVerticalTextAlign();
		}
		return null;
	}

	/**
	 *
	 */
	public static VerticalImageAlignEnum getVerticalImageAlign(JRStyle style)
	{
		VerticalImageAlignEnum ownVerticalAlignment = style.getOwnVerticalImageAlign();
		if (ownVerticalAlignment != null)
		{
			return ownVerticalAlignment;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getVerticalImageAlign();
		}
		return null;
	}

	/**
	 *
	 */
	public static Float getLineSpacingSize(JRParagraph paragraph)
	{
		return styleResolver.getLineSpacingSize(paragraph);
	}

	/**
	 *
	 */
	public static Integer getFirstLineIndent(JRParagraph paragraph)
	{
		return styleResolver.getFirstLineIndent(paragraph);
	}

	/**
	 *
	 */
	public static Integer getLeftIndent(JRParagraph paragraph)
	{
		return styleResolver.getLeftIndent(paragraph);
	}

	/**
	 *
	 */
	public static Integer getRightIndent(JRParagraph paragraph)
	{
		return styleResolver.getRightIndent(paragraph);
	}

	/**
	 *
	 */
	public static Integer getSpacingBefore(JRParagraph paragraph)
	{
		return styleResolver.getSpacingBefore(paragraph);
	}

	/**
	 *
	 */
	public static Integer getSpacingAfter(JRParagraph paragraph)
	{
		return styleResolver.getSpacingAfter(paragraph);
	}

	/**
	 *
	 */
	public static Integer getTabStopWidth(JRParagraph paragraph)
	{
		return styleResolver.getTabStopWidth(paragraph);
	}

	/**
	 *
	 */
	public static TabStop[] getTabStops(JRParagraph paragraph)
	{
		return styleResolver.getTabStops(paragraph);
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
		return styleResolver.getLineSpacing(paragraph);
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
		return styleResolver.getFontName(font);
	}
	
	/**
	 *
	 */
	public static String getFontName(JRStyle style)
	{
		return styleResolver.getFontName(style);
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
	public static float getFontsize(JRFont font)
	{
		return styleResolver.getFontsize(font);
	}
	
	/**
	 *
	 */
	public static Float getFontsize(JRStyle style)
	{
		Float ownFontSize = style.getOwnFontsize();
		if (ownFontSize != null)
		{
			return ownFontSize;
		}
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
		{
			return baseStyle.getFontsize();
		}
		return null;
	}

	/**
	 * @deprecated Replaced by {@link #getFontsize(JRFont)}.
	 */
	public static int getFontSize(JRFont font)
	{
		return (int)getFontsize(font);
	}
	
	/**
	 * @deprecated Replaced by {@link #getFontsize(JRStyle)}.
	 */
	public static Integer getFontSize(JRStyle style)
	{
		Float fontSize = getFontsize(style);
		return fontSize == null ? null : fontSize.intValue();
	}

	/**
	 *
	 */
	public static String getPdfFontName(JRFont font)
	{
		return styleResolver.getPdfFontName(font);
	}
	
	/**
	 *
	 */
	public static String getPdfFontName(JRStyle style)
	{
		return styleResolver.getPdfFontName(style);
	}

	/**
	 *
	 */
	public static String getPdfEncoding(JRFont font)
	{
		return styleResolver.getPdfEncoding(font);
	}
	
	/**
	 *
	 */
	public static String getPdfEncoding(JRStyle style)
	{
		return styleResolver.getPdfEncoding(style);
	}

	/**
	 *
	 */
	public static boolean isPdfEmbedded(JRFont font)
	{
		return styleResolver.isPdfEmbedded(font);
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
		return styleResolver.getPadding(box);
	}

	/**
	 *
	 */
	public static Integer getTopPadding(JRLineBox box)
	{
		return styleResolver.getTopPadding(box);
	}

	/**
	 *
	 */
	public static Integer getLeftPadding(JRLineBox box)
	{
		return styleResolver.getLeftPadding(box);
	}

	/**
	 *
	 */
	public static Integer getBottomPadding(JRLineBox box)
	{
		return styleResolver.getBottomPadding(box);
	}

	/**
	 *
	 */
	public static Integer getRightPadding(JRLineBox box)
	{
		return styleResolver.getRightPadding(box);
	}


	/**
	 * @deprecated Replaced by {@link StyleUtil#appendStyle(JRStyle, JRStyle)}.
	 */
	public static void appendStyle(JRStyle destStyle, JRStyle srcStyle)
	{
		StyleUtil.appendStyle(destStyle, srcStyle);
	}

	/**
	 * @deprecated Replaced by {@link StyleUtil#appendPen(JRPen, JRPen)}.
	 */
	public static void appendPen(JRPen destPen, JRPen srcPen)
	{
		StyleUtil.appendPen(destPen, srcPen);
	}

	/**
	 * @deprecated Replaced by {@link StyleUtil#appendBox(JRLineBox, JRLineBox)}.
	 */
	public static void appendBox(JRLineBox destBox, JRLineBox srcBox)
	{
		StyleUtil.appendBox(destBox, srcBox);
	}

	/**
	 * @deprecated Replaced by {@link StyleUtil#appendParagraph(JRParagraph, JRParagraph)}.
	 */
	public static void appendParagraph(JRParagraph destParagraph, JRParagraph srcParagraph)
	{
		StyleUtil.appendParagraph(destParagraph, srcParagraph);
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
