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
 * @deprecated Replaced by {@link StyleResolver} and {@link StyleUtil}.
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
		return styleResolver.getBaseStyle(styleContainer);
	}


	/**
	 *
	 */
	public static ModeEnum getMode(JRCommonElement element, ModeEnum defaultMode)
	{
		return styleResolver.getMode(element, defaultMode);
	}

	/**
	 *
	 */
	public static ModeEnum getModeValue(JRStyle style)
	{
		return styleResolver.getModeValue(style);
	}

	/**
	 *
	 */
	public static Color getForecolor(JRCommonElement element)
	{
		return styleResolver.getForecolor(element);
	}

	/**
	 *
	 */
	public static Color getForecolor(JRChartPlot plot)
	{
		return styleResolver.getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getForecolor(JRStyle style)
	{
		return styleResolver.getForecolor(style);
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRCommonElement element)
	{
		return styleResolver.getBackcolor(element);
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRChartPlot plot)
	{
		return styleResolver.getBackcolor(plot);
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRStyle style)
	{
		return styleResolver.getBackcolor(style);
	}

	/**
	 *
	 */
	public static Float getLineWidth(JRPen pen, Float defaultLineWidth)
	{
		return styleResolver.getLineWidth(pen, defaultLineWidth);
	}

	/**
	 *
	 */
	public static Float getLineWidth(JRBoxPen boxPen, Float defaultLineWidth)
	{
		return styleResolver.getLineWidth(boxPen, defaultLineWidth);
	}

	/**
	 *
	 */
	public static LineStyleEnum getLineStyleValue(JRPen pen)
	{
		return styleResolver.getLineStyleValue(pen);
	}

	/**
	 *
	 */
	public static LineStyleEnum getLineStyleValue(JRBoxPen boxPen)
	{
		return styleResolver.getLineStyleValue(boxPen);
	}

	/**
	 *
	 */
	public static Color getLineColor(JRPen pen, Color defaultColor)
	{
		return styleResolver.getLineColor(pen, defaultColor);
	}

	/**
	 *
	 */
	public static Color getLineColor(JRBoxPen boxPen, Color defaultColor)
	{
		return styleResolver.getLineColor(boxPen, defaultColor);
	}

	/**
	 *
	 */
	public static FillEnum getFillValue(JRCommonGraphicElement element)
	{
		return styleResolver.getFillValue(element);
	}

	/**
	 *
	 */
	public static FillEnum getFillValue(JRStyle style)
	{
		return styleResolver.getFillValue(style);
	}

	/**
	 *
	 */
	public static int getRadius(JRCommonRectangle rectangle)
	{
		return styleResolver.getRadius(rectangle);
	}

	/**
	 *
	 */
	public static Integer getRadius(JRStyle style)
	{
		return styleResolver.getRadius(style);
	}

	/**
	 *
	 */
	public static ScaleImageEnum getScaleImageValue(JRCommonImage image)
	{
		return styleResolver.getScaleImageValue(image);
	}

	/**
	 *
	 */
	public static ScaleImageEnum getScaleImageValue(JRStyle style)
	{
		return styleResolver.getScaleImageValue(style);
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
		return styleResolver.getHorizontalTextAlign(alignment);
	}

	/**
	 *
	 */
	public static HorizontalImageAlignEnum getHorizontalImageAlign(JRImageAlignment alignment)
	{
		return styleResolver.getHorizontalImageAlign(alignment);
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
		return styleResolver.getHorizontalTextAlign(style);
	}

	/**
	 *
	 */
	public static HorizontalImageAlignEnum getHorizontalImageAlign(JRStyle style)
	{
		return styleResolver.getHorizontalImageAlign(style);
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
		return styleResolver.getVerticalTextAlign(alignment);
	}

	/**
	 *
	 */
	public static VerticalImageAlignEnum getVerticalImageAlign(JRImageAlignment alignment)
	{
		return styleResolver.getVerticalImageAlign(alignment);
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
		return styleResolver.getVerticalTextAlign(style);
	}

	/**
	 *
	 */
	public static VerticalImageAlignEnum getVerticalImageAlign(JRStyle style)
	{
		return styleResolver.getVerticalImageAlign(style);
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
		return styleResolver.getRotationValue(element);
	}

	/**
	 *
	 */
	public static RotationEnum getRotationValue(JRStyle style)
	{
		return styleResolver.getRotationValue(style);
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
		return styleResolver.getMarkup(element);
	}

	/**
	 *
	 */
	public static String getMarkup(JRStyle style)
	{
		return styleResolver.getMarkup(style);
	}

	/**
	 *
	 */
	public static String getPattern(JRTextField element)
	{
		return styleResolver.getPattern(element);
	}

	/**
	 *
	 */
	public static String getPattern(JRStyle style)
	{
		return styleResolver.getPattern(style);
	}

	/**
	 *
	 */
	public static boolean isBlankWhenNull(JRTextField element)
	{
		return styleResolver.isBlankWhenNull(element);
	}

	/**
	 *
	 */
	public static Boolean isBlankWhenNull(JRStyle style)
	{
		return styleResolver.isBlankWhenNull(style);
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
		return styleResolver.isBold(font);
	}
	
	/**
	 *
	 */
	public static Boolean isBold(JRStyle style)
	{
		return styleResolver.isBold(style);
	}

	/**
	 *
	 */
	public static boolean isItalic(JRFont font)
	{
		return styleResolver.isItalic(font);
	}
	
	/**
	 *
	 */
	public static Boolean isItalic(JRStyle style)
	{
		return styleResolver.isItalic(style);
	}

	/**
	 *
	 */
	public static boolean isUnderline(JRFont font)
	{
		return styleResolver.isUnderline(font);
	}
	
	/**
	 *
	 */
	public static Boolean isUnderline(JRStyle style)
	{
		return styleResolver.isUnderline(style);
	}

	/**
	 *
	 */
	public static boolean isStrikeThrough(JRFont font)
	{
		return styleResolver.isStrikeThrough(font);
	}
	
	/**
	 *
	 */
	public static Boolean isStrikeThrough(JRStyle style)
	{
		return styleResolver.isStrikeThrough(style);
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
		return styleResolver.getFontsize(style);
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
		return styleResolver.isPdfEmbedded(style);
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
		return styleResolver.getTitleColor(chart);
	}

	/**
	 *
	 */
	public static Color getSubtitleColor(JRChart chart)
	{
		return styleResolver.getSubtitleColor(chart);
	}

	/**
	 *
	 */
	public static Color getLegendColor(JRChart chart)
	{
		return styleResolver.getLegendColor(chart);
	}

	/**
	 *
	 */
	public static Color getLegendBackgroundColor(JRChart chart)
	{
		return styleResolver.getLegendBackgroundColor(chart);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getCategoryAxisLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisTickLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getCategoryAxisTickLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisLineColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getCategoryAxisLineColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getValueAxisLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisTickLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getValueAxisTickLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisLineColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getValueAxisLineColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getXAxisLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getXAxisLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getXAxisTickLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getXAxisTickLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getXAxisLineColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getXAxisLineColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getYAxisLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getYAxisLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getYAxisTickLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getYAxisTickLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getYAxisLineColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getYAxisLineColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getTimeAxisLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisTickLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getTimeAxisTickLabelColor(axisFormat, plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisLineColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		return styleResolver.getTimeAxisLineColor(axisFormat, plot);
	}

	
	private JRStyleResolver()
	{
	}
}
