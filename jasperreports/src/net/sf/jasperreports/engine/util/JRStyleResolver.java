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
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.base.JRBoxPen;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyleResolver
{

	private static final Integer INTEGER_ZERO = new Integer(0);

	/**
	 *
	 */
	private static JRFont getBaseFont(JRFont font)
	{
		if (font.getReportFont() != null)
			return font.getReportFont();
		if (font.getDefaultStyleProvider() != null)
			return font.getDefaultStyleProvider().getDefaultFont();
		return null;
	}
	
	/**
	 *
	 */
	private static JRStyle getBaseStyle(JRStyleContainer styleContainer)
	{
		if (styleContainer != null)
		{
			if (styleContainer.getStyle() != null)
				return styleContainer.getStyle();
			if (styleContainer.getDefaultStyleProvider() != null)
				return styleContainer.getDefaultStyleProvider().getDefaultStyle();
		}
		return null;
	}


	/**
	 *
	 */
	public static byte getMode(JRCommonElement element, byte defaultMode)
	{
		if (element.getOwnMode() != null) 
			return element.getOwnMode().byteValue();
		JRStyle style = getBaseStyle(element);
		if (style != null && style.getMode() != null)
			return style.getMode().byteValue();
		return defaultMode;
	}

	/**
	 *
	 */
	public static Byte getMode(JRStyle style)
	{
		if (style.getOwnMode() != null)
			return style.getOwnMode();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getMode();
		return null;
	}

	/**
	 *
	 */
	public static Color getForecolor(JRCommonElement element)
	{
		if (element.getOwnForecolor() != null) 
			return element.getOwnForecolor();
		JRStyle style = getBaseStyle(element);
		if (style != null && style.getForecolor() != null)
			return style.getForecolor();
		return Color.black;
	}

	/**
	 *
	 */
	public static Color getForecolor(JRChartPlot plot)
	{
		JRChart chart = plot.getChart();
		if (chart != null)
			return getForecolor(chart);
		return Color.black;
	}

	/**
	 *
	 */
	public static Color getForecolor(JRStyle style)
	{
		if (style.getOwnForecolor() != null)
			return style.getOwnForecolor();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getForecolor();
		return null;
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRCommonElement element)
	{
		if (element.getOwnBackcolor() != null) 
			return element.getOwnBackcolor();
		JRStyle style = getBaseStyle(element);
		if (style != null && style.getBackcolor() != null)
			return style.getBackcolor();
		return Color.white;
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRChartPlot plot)
	{
		if (plot.getOwnBackcolor() != null) 
			return plot.getOwnBackcolor();
		JRChart chart = plot.getChart();
		if (chart != null)
			return getBackcolor(chart);
		return Color.white;
	}

	/**
	 *
	 */
	public static Color getBackcolor(JRStyle style)
	{
		if (style.getOwnBackcolor() != null)
			return style.getOwnBackcolor();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getBackcolor();
		return null;
	}

	/**
	 *
	 */
	public static Float getLineWidth(JRPen pen, Float defaultLineWidth)
	{
		if (pen.getOwnLineWidth() != null)
			return pen.getOwnLineWidth();
		JRStyle baseStyle = getBaseStyle(pen.getStyleContainer());
		if (baseStyle != null && baseStyle.getLinePen().getLineWidth() != null)
			return baseStyle.getLinePen().getLineWidth();
		return defaultLineWidth;
	}

	/**
	 *
	 */
	public static Float getLineWidth(JRBoxPen boxPen, Float defaultLineWidth)
	{
		if (boxPen.getOwnLineWidth() != null)
			return boxPen.getOwnLineWidth();
		if (boxPen.getBox().getPen().getOwnLineWidth() != null) 
			return boxPen.getBox().getPen().getOwnLineWidth();
		JRStyle baseStyle = getBaseStyle(boxPen.getStyleContainer());
		if (baseStyle != null && boxPen.getPen(baseStyle.getLineBox()).getLineWidth() != null)
			return boxPen.getPen(baseStyle.getLineBox()).getLineWidth();
		return defaultLineWidth;
	}

	/**
	 *
	 */
	public static Byte getLineStyle(JRPen pen)
	{
		if (pen.getOwnLineStyle() != null)
			return pen.getOwnLineStyle();
		JRStyle baseStyle = getBaseStyle(pen.getStyleContainer());
		if (baseStyle != null && baseStyle.getLinePen().getLineStyle() != null)
			return baseStyle.getLinePen().getLineStyle();
		return new Byte(JRPen.LINE_STYLE_SOLID);
	}

	/**
	 *
	 */
	public static Byte getLineStyle(JRBoxPen boxPen)
	{
		if (boxPen.getOwnLineStyle() != null)
			return boxPen.getOwnLineStyle();
		if (boxPen.getBox().getPen().getOwnLineStyle() != null)
			return boxPen.getBox().getPen().getOwnLineStyle();
		JRStyle baseStyle = getBaseStyle(boxPen.getStyleContainer());
		if (baseStyle != null && boxPen.getPen(baseStyle.getLineBox()).getLineStyle() != null)
			return boxPen.getPen(baseStyle.getLineBox()).getLineStyle();
		return new Byte(JRPen.LINE_STYLE_SOLID);
	}

	/**
	 *
	 */
	public static Color getLineColor(JRPen pen, Color defaultColor)
	{
		if (pen.getOwnLineColor() != null)
			return pen.getOwnLineColor();
		JRStyle baseStyle = getBaseStyle(pen.getStyleContainer());
		if (baseStyle != null && baseStyle.getLinePen().getLineColor() != null)
			return baseStyle.getLinePen().getLineColor();
		return defaultColor;
	}

	/**
	 *
	 */
	public static Color getLineColor(JRBoxPen boxPen, Color defaultColor)
	{
		if (boxPen.getOwnLineColor() != null)
			return boxPen.getOwnLineColor();
		if (boxPen.getBox().getPen().getOwnLineColor() != null)
			return boxPen.getBox().getPen().getOwnLineColor();
		JRStyle baseStyle = getBaseStyle(boxPen.getStyleContainer());
		if (baseStyle != null && boxPen.getPen(baseStyle.getLineBox()).getLineColor() != null)
			return boxPen.getPen(baseStyle.getLineBox()).getLineColor();
		return defaultColor;
	}

	/**
	 *
	 */
	public static byte getFill(JRCommonGraphicElement element)
	{
		if (element.getOwnFill() != null)
			return element.getOwnFill().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getFill() != null)
			return baseStyle.getFill().byteValue();
		return JRGraphicElement.FILL_SOLID;
	}

	/**
	 *
	 */
	public static Byte getFill(JRStyle style)
	{
		if (style.getOwnFill() != null)
			return style.getOwnFill();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getFill();
		return null;
	}

	/**
	 *
	 */
	public static int getRadius(JRCommonRectangle rectangle)
	{
		if (rectangle.getOwnRadius() != null)
			return rectangle.getOwnRadius().intValue();
		JRStyle baseStyle = getBaseStyle(rectangle);
		if (baseStyle != null && baseStyle.getRadius() != null)
			return baseStyle.getRadius().intValue();
		return 0;
	}

	/**
	 *
	 */
	public static Integer getRadius(JRStyle style)
	{
		if (style.getOwnRadius() != null)
			return style.getOwnRadius();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getRadius();
		return null;
	}

	/**
	 *
	 */
	public static byte getScaleImage(JRCommonImage image)
	{
		if (image.getOwnScaleImage() != null)
			return image.getOwnScaleImage().byteValue();
		JRStyle baseStyle = getBaseStyle(image);
		if (baseStyle != null && baseStyle.getScaleImage() != null)
			return baseStyle.getScaleImage().byteValue();
		return JRImage.SCALE_IMAGE_RETAIN_SHAPE;
	}

	/**
	 *
	 */
	public static Byte getScaleImage(JRStyle style)
	{
		if (style.getOwnScaleImage() != null)
			return style.getOwnScaleImage();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null )
			return baseStyle.getScaleImage();
		return null;
	}

	/**
	 *
	 */
	public static byte getHorizontalAlignment(JRAlignment alignment)
	{
		if (alignment.getOwnHorizontalAlignment() != null)
			return alignment.getOwnHorizontalAlignment().byteValue();
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null && baseStyle.getHorizontalAlignment() != null)
			return baseStyle.getHorizontalAlignment().byteValue();
		return JRAlignment.HORIZONTAL_ALIGN_LEFT;
	}

	/**
	 *
	 */
	public static Byte getHorizontalAlignment(JRStyle style)
	{
		if (style.getOwnHorizontalAlignment() != null)
			return style.getOwnHorizontalAlignment();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null && baseStyle.getHorizontalAlignment() != null)
			return baseStyle.getHorizontalAlignment();
		return null;
	}

	/**
	 *
	 */
	public static byte getVerticalAlignment(JRAlignment alignment)
	{
		if (alignment.getOwnVerticalAlignment() != null)
			return alignment.getOwnVerticalAlignment().byteValue();
		JRStyle baseStyle = getBaseStyle(alignment);
		if (baseStyle != null && baseStyle.getVerticalAlignment() != null)
			return baseStyle.getVerticalAlignment().byteValue();
		return JRAlignment.VERTICAL_ALIGN_TOP;
	}

	/**
	 *
	 */
	public static Byte getVerticalAlignment(JRStyle style)
	{
		if (style.getOwnVerticalAlignment() != null)
			return style.getOwnVerticalAlignment();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null && baseStyle.getVerticalAlignment() != null)
			return baseStyle.getVerticalAlignment();
		return null;
	}

	/**
	 *
	 */
	public static byte getRotation(JRCommonText element)
	{
		if (element.getOwnRotation() != null)
			return element.getOwnRotation().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getRotation() != null)
			return baseStyle.getRotation().byteValue();
		return JRTextElement.ROTATION_NONE;
	}

	/**
	 *
	 */
	public static Byte getRotation(JRStyle style)
	{
		if (style.getOwnRotation() != null)
			return style.getOwnRotation();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getRotation();
		return null;
	}

	/**
	 *
	 */
	public static byte getLineSpacing(JRCommonText element)
	{
		if (element.getOwnLineSpacing() != null)
			return element.getOwnLineSpacing().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getLineSpacing() != null)
			return baseStyle.getLineSpacing().byteValue();
		return JRTextElement.LINE_SPACING_SINGLE;
	}

	/**
	 *
	 */
	public static Byte getLineSpacing(JRStyle style)
	{
		if (style.getOwnLineSpacing() != null)
			return style.getOwnLineSpacing();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getLineSpacing();
		return null;
	}

	/**
	 *
	 */
	public static boolean isStyledText(JRCommonText element)
	{
		if (element.isOwnStyledText() != null)
			return element.isOwnStyledText().booleanValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.isStyledText() != null)
			return baseStyle.isStyledText().booleanValue();
		return false;
	}

	/**
	 *
	 */
	public static Boolean isStyledText(JRStyle style)
	{
		if (style.isOwnStyledText() != null)
			return style.isOwnStyledText();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.isStyledText();
		return null;
	}

	/**
	 *
	 */
	public static String getPattern(JRTextField element)
	{
		if (element.getOwnPattern() != null)
			return element.getOwnPattern();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null)
			return baseStyle.getPattern();
		return null;
	}

	/**
	 *
	 */
	public static String getPattern(JRStyle style)
	{
		if (style.getOwnPattern() != null)
			return style.getOwnPattern();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getPattern();
		return null;
	}

	/**
	 *
	 */
	public static boolean isBlankWhenNull(JRTextField element)
	{
		if (element.isOwnBlankWhenNull() != null)
			return element.isOwnBlankWhenNull().booleanValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.isBlankWhenNull() != null)
			return baseStyle.isBlankWhenNull().booleanValue();
		return false;
	}

	/**
	 *
	 */
	public static Boolean isBlankWhenNull(JRStyle style)
	{
		if (style.isOwnBlankWhenNull() != null)
			return style.isOwnBlankWhenNull();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.isBlankWhenNull();
		return null;
	}

	/**
	 *
	 */
	public static String getFontName(JRFont font)
	{
		if (font.getOwnFontName() != null)
			return font.getOwnFontName();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null && baseFont.getFontName() != null)
			return baseFont.getFontName();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.getFontName() != null)
			return baseStyle.getFontName();
		return JRProperties.getProperty(JRFont.DEFAULT_FONT_NAME);
	}
	
	/**
	 *
	 */
	public static String getFontName(JRStyle style)
	{
		if (style.getOwnFontName() != null)
			return style.getOwnFontName();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null && baseStyle.getFontName() != null)
			return baseStyle.getFontName();
		return JRProperties.getProperty(JRFont.DEFAULT_FONT_NAME);
	}

	/**
	 *
	 */
	public static boolean isBold(JRFont font)
	{
		if (font.isOwnBold() != null)
			return font.isOwnBold().booleanValue();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null)
			return baseFont.isBold();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.isBold() != null)
			return baseStyle.isBold().booleanValue();
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isBold(JRStyle style)
	{
		if (style.isOwnBold() != null)
			return style.isOwnBold();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.isBold();
		return null;
	}

	/**
	 *
	 */
	public static boolean isItalic(JRFont font)
	{
		if (font.isOwnItalic() != null)
			return font.isOwnItalic().booleanValue();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null)
			return baseFont.isItalic();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.isItalic() != null)
			return baseStyle.isItalic().booleanValue();
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isItalic(JRStyle style)
	{
		if (style.isOwnItalic() != null)
			return style.isOwnItalic();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.isItalic();
		return null;
	}

	/**
	 *
	 */
	public static boolean isUnderline(JRFont font)
	{
		if (font.isOwnUnderline() != null)
			return font.isOwnUnderline().booleanValue();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null)
			return baseFont.isUnderline();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.isUnderline() != null)
			return baseStyle.isUnderline().booleanValue();
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isUnderline(JRStyle style)
	{
		if (style.isOwnUnderline() != null)
			return style.isOwnUnderline();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.isUnderline();
		return null;
	}

	/**
	 *
	 */
	public static boolean isStrikeThrough(JRFont font)
	{
		if (font.isOwnStrikeThrough() != null)
			return font.isOwnStrikeThrough().booleanValue();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null)
			return baseFont.isStrikeThrough();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.isStrikeThrough() != null)
			return baseStyle.isStrikeThrough().booleanValue();
		return false;
	}
	
	/**
	 *
	 */
	public static Boolean isStrikeThrough(JRStyle style)
	{
		if (style.isOwnStrikeThrough() != null)
			return style.isOwnStrikeThrough();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.isStrikeThrough();
		return null;
	}

	/**
	 *
	 */
	public static int getFontSize(JRFont font)
	{
		if (font.getOwnFontSize() != null)
			return font.getOwnFontSize().intValue();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null)
			return baseFont.getFontSize();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.getFontSize() != null)
			return baseStyle.getFontSize().intValue();
		return JRProperties.getIntegerProperty(JRFont.DEFAULT_FONT_SIZE);
	}
	
	/**
	 *
	 */
	public static Integer getFontSize(JRStyle style)
	{
		if (style.getOwnFontSize() != null)
			return style.getOwnFontSize();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getFontSize();
		return null;
	}

	/**
	 *
	 */
	public static String getPdfFontName(JRFont font)
	{
		if (font.getOwnPdfFontName() != null)
			return font.getOwnPdfFontName();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null && baseFont.getPdfFontName() != null)
			return baseFont.getPdfFontName();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.getPdfFontName() != null)
			return baseStyle.getPdfFontName();
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_FONT_NAME);
	}
	
	/**
	 *
	 */
	public static String getPdfFontName(JRStyle style)
	{
		if (style.getOwnPdfFontName() != null)
			return style.getOwnPdfFontName();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null && baseStyle.getPdfFontName() != null)
			return baseStyle.getPdfFontName();
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_FONT_NAME);
	}

	/**
	 *
	 */
	public static String getPdfEncoding(JRFont font)
	{
		if (font.getOwnPdfEncoding() != null)
			return font.getOwnPdfEncoding();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null && baseFont.getPdfEncoding() != null)
			return baseFont.getPdfEncoding();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.getPdfEncoding() != null)
			return baseStyle.getPdfEncoding();
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_ENCODING);
	}
	
	/**
	 *
	 */
	public static String getPdfEncoding(JRStyle style)
	{
		if (style.getOwnPdfEncoding() != null)
			return style.getOwnPdfEncoding();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null && baseStyle.getPdfEncoding() != null)
			return baseStyle.getPdfEncoding();
		return JRProperties.getProperty(JRFont.DEFAULT_PDF_ENCODING);
	}

	/**
	 *
	 */
	public static boolean isPdfEmbedded(JRFont font)
	{
		if (font.isOwnPdfEmbedded() != null)
			return font.isOwnPdfEmbedded().booleanValue();
		JRFont baseFont = getBaseFont(font);
		if (baseFont != null)
			return baseFont.isPdfEmbedded();
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null && baseStyle.isPdfEmbedded() != null)
			return baseStyle.isPdfEmbedded().booleanValue();
		return JRProperties.getBooleanProperty(JRFont.DEFAULT_PDF_EMBEDDED);
	}
	
	/**
	 *
	 */
	public static Boolean isPdfEmbedded(JRStyle style)
	{
		if (style.isOwnPdfEmbedded() != null)
			return style.isOwnPdfEmbedded();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.isPdfEmbedded();
		return null;
	}

	/**
	 *
	 */
	public static Integer getPadding(JRLineBox box)
	{
		if (box.getOwnPadding() != null)
			return box.getOwnPadding();
		JRStyle baseStyle = getBaseStyle(box);
		if (baseStyle != null && baseStyle.getLineBox().getPadding() != null)
			return baseStyle.getLineBox().getPadding();
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getTopPadding(JRLineBox box)
	{
		if (box.getOwnTopPadding() != null)
			return box.getOwnTopPadding();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getLineBox().getTopPadding() != null)
			return style.getLineBox().getTopPadding();
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getLeftPadding(JRLineBox box)
	{
		if (box.getOwnLeftPadding() != null)
			return box.getOwnLeftPadding();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getLineBox().getLeftPadding() != null)
			return style.getLineBox().getLeftPadding();
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getBottomPadding(JRLineBox box)
	{
		if (box.getOwnBottomPadding() != null)
			return box.getOwnBottomPadding();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getLineBox().getBottomPadding() != null)
			return style.getLineBox().getBottomPadding();
		return INTEGER_ZERO;
	}

	/**
	 *
	 */
	public static Integer getRightPadding(JRLineBox box)
	{
		if (box.getOwnRightPadding() != null)
			return box.getOwnRightPadding();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getLineBox().getRightPadding() != null)
			return style.getLineBox().getRightPadding();
		return INTEGER_ZERO;
	}


	/**
	 * Merges two styles, by appending the properties of the source style to the ones of the destination style.
	 */
	public static void appendStyle(JRStyle destStyle, JRStyle srcStyle)
	{
		if (srcStyle.getOwnMode() != null)
			destStyle.setMode(srcStyle.getOwnMode());
		if (srcStyle.getOwnForecolor() != null)
			destStyle.setForecolor(srcStyle.getOwnForecolor());
		if (srcStyle.getOwnBackcolor() != null)
			destStyle.setBackcolor(srcStyle.getOwnBackcolor());

		appendPen(destStyle.getLinePen(), srcStyle.getLinePen());
		
		if (srcStyle.getOwnFill() != null)
			destStyle.setFill(srcStyle.getOwnFill());

		if (srcStyle.getOwnRadius() != null)
			destStyle.setRadius(srcStyle.getOwnRadius());

		if (srcStyle.getOwnScaleImage() != null)
			destStyle.setScaleImage(srcStyle.getOwnScaleImage());
		if (srcStyle.getOwnHorizontalAlignment() != null)
			destStyle.setHorizontalAlignment(srcStyle.getOwnHorizontalAlignment());
		if (srcStyle.getOwnVerticalAlignment() != null)
			destStyle.setVerticalAlignment(srcStyle.getOwnVerticalAlignment());

		appendBox(destStyle.getLineBox(), srcStyle.getLineBox());

		if (srcStyle.getOwnRotation() != null)
			destStyle.setRotation(srcStyle.getOwnRotation());
		if (srcStyle.getOwnLineSpacing() != null)
			destStyle.setLineSpacing(srcStyle.getOwnLineSpacing());
		if (srcStyle.isOwnStyledText() != null)
			destStyle.setStyledText(srcStyle.isOwnStyledText());

		if (srcStyle.getOwnPattern() != null)
			destStyle.setPattern(srcStyle.getOwnPattern());

		if (srcStyle.getOwnFontName() != null)
			destStyle.setFontName(srcStyle.getOwnFontName());
		if (srcStyle.isOwnBold() != null)
			destStyle.setBold(srcStyle.isOwnBold());
		if (srcStyle.isOwnItalic() != null)
			destStyle.setItalic(srcStyle.isOwnItalic());
		if (srcStyle.isOwnUnderline() != null)
			destStyle.setUnderline(srcStyle.isOwnUnderline());
		if (srcStyle.isOwnStrikeThrough() != null)
			destStyle.setStrikeThrough(srcStyle.isOwnStrikeThrough());
		if (srcStyle.getOwnFontSize() != null)
			destStyle.setFontSize(srcStyle.getOwnFontSize());
		if (srcStyle.getOwnPdfFontName() != null)
			destStyle.setPdfFontName(srcStyle.getOwnPdfFontName());
		if (srcStyle.getOwnPdfEncoding() != null)
			destStyle.setPdfEncoding(srcStyle.getOwnPdfEncoding());
		if (srcStyle.isOwnPdfEmbedded() != null)
			destStyle.setPdfEmbedded(srcStyle.isOwnPdfEmbedded());
	}

	/**
	 * Merges two pens, by appending the properties of the source pen to the ones of the destination pen.
	 */
	private static void appendPen(JRPen destPen, JRPen srcPen)
	{
		if (srcPen.getOwnLineWidth() != null)
			destPen.setLineWidth(srcPen.getOwnLineWidth());
		if (srcPen.getOwnLineStyle() != null)
			destPen.setLineStyle(srcPen.getOwnLineStyle());
		if (srcPen.getOwnLineColor() != null)
			destPen.setLineColor(srcPen.getOwnLineColor());
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
			destBox.setPadding(srcBox.getOwnPadding());
		if (srcBox.getOwnTopPadding() != null)
			destBox.setTopPadding(srcBox.getOwnTopPadding());
		if (srcBox.getOwnLeftPadding() != null)
			destBox.setLeftPadding(srcBox.getOwnLeftPadding());
		if (srcBox.getOwnBottomPadding() != null)
			destBox.setBottomPadding(srcBox.getOwnBottomPadding());
		if (srcBox.getOwnRightPadding() != null)
			destBox.setRightPadding(srcBox.getOwnRightPadding());
	}

	/**
	 *
	 */
	public static Color getTitleColor(JRChart chart)
	{
		if (chart.getOwnTitleColor() != null) 
			return chart.getOwnTitleColor();
		return getForecolor(chart);
	}

	/**
	 *
	 */
	public static Color getSubtitleColor(JRChart chart)
	{
		if (chart.getOwnSubtitleColor() != null) 
			return chart.getOwnSubtitleColor();
		return getForecolor(chart);
	}

	/**
	 *
	 */
	public static Color getLegendColor(JRChart chart)
	{
		if (chart.getOwnLegendColor() != null) 
			return chart.getOwnLegendColor();
		return getForecolor(chart);
	}

	/**
	 *
	 */
	public static Color getLegendBackgroundColor(JRChart chart)
	{
		if (chart.getOwnLegendBackgroundColor() != null) 
			return chart.getOwnLegendBackgroundColor();
		return getBackcolor(chart);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnCategoryAxisLabelColor() != null) 
			return axisFormat.getOwnCategoryAxisLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisTickLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnCategoryAxisTickLabelColor() != null) 
			return axisFormat.getOwnCategoryAxisTickLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getCategoryAxisLineColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnCategoryAxisLineColor() != null) 
			return axisFormat.getOwnCategoryAxisLineColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnValueAxisLabelColor() != null) 
			return axisFormat.getOwnValueAxisLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisTickLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnValueAxisTickLabelColor() != null) 
			return axisFormat.getOwnValueAxisTickLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getValueAxisLineColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnValueAxisLineColor() != null) 
			return axisFormat.getOwnValueAxisLineColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getXAxisLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnXAxisLabelColor() != null) 
			return axisFormat.getOwnXAxisLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getXAxisTickLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnXAxisTickLabelColor() != null) 
			return axisFormat.getOwnXAxisTickLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getXAxisLineColor(JRXAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnXAxisLineColor() != null) 
			return axisFormat.getOwnXAxisLineColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getYAxisLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnYAxisLabelColor() != null) 
			return axisFormat.getOwnYAxisLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getYAxisTickLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnYAxisTickLabelColor() != null) 
			return axisFormat.getOwnYAxisTickLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getYAxisLineColor(JRYAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnYAxisLineColor() != null) 
			return axisFormat.getOwnYAxisLineColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnTimeAxisLabelColor() != null) 
			return axisFormat.getOwnTimeAxisLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisTickLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnTimeAxisTickLabelColor() != null) 
			return axisFormat.getOwnTimeAxisTickLabelColor();
		return getForecolor(plot);
	}

	/**
	 *
	 */
	public static Color getTimeAxisLineColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		if (axisFormat.getOwnTimeAxisLineColor() != null) 
			return axisFormat.getOwnTimeAxisLineColor();
		return getForecolor(plot);
	}

}
