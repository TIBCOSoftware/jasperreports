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

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.fill.JRTemplateElement;
import net.sf.jasperreports.engine.fill.JRTemplateGraphicElement;
import net.sf.jasperreports.engine.fill.JRTemplateImage;
import net.sf.jasperreports.engine.fill.JRTemplateRectangle;
import net.sf.jasperreports.engine.fill.JRTemplateText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRStyleResolver
{


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
		if (styleContainer.getStyle() != null)
			return styleContainer.getStyle();
		if (styleContainer.getDefaultStyleProvider() != null)
			return styleContainer.getDefaultStyleProvider().getDefaultStyle();
		return null;
	}


	/**
	 *
	 */
	public static byte getMode(JRElement element, byte defaultMode)
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
	public static byte getMode(JRPrintElement element, byte defaultMode)
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
	public static byte getMode(JRTemplateElement element, byte defaultMode)
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
	public static Color getForecolor(JRElement element)
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
	public static Color getForecolor(JRPrintElement element)
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
	public static Color getForecolor(JRTemplateElement element)
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
	public static Color getBackcolor(JRElement element)
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
	public static Color getBackcolor(JRPrintElement element)
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
	public static Color getBackcolor(JRTemplateElement element)
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
	public static byte getPen(JRGraphicElement element, byte defaultPen)
	{
		if (element.getOwnPen() != null)
			return element.getOwnPen().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getPen() != null)
			return baseStyle.getPen().byteValue();
		return defaultPen;
	}

	/**
	 *
	 */
	public static byte getPen(JRPrintGraphicElement element, byte defaultPen)
	{
		if (element.getOwnPen() != null)
			return element.getOwnPen().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getPen() != null)
			return baseStyle.getPen().byteValue();
		return defaultPen;
	}

	/**
	 *
	 */
	public static byte getPen(JRTemplateGraphicElement element, byte defaultPen)
	{
		if (element.getOwnPen() != null)
			return element.getOwnPen().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getPen() != null)
			return baseStyle.getPen().byteValue();
		return defaultPen;
	}

	/**
	 *
	 */
	public static Byte getPen(JRStyle style)
	{
		if (style.getOwnPen() != null)
			return style.getOwnPen();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getPen();
		return null;
	}

	/**
	 *
	 */
	public static byte getFill(JRGraphicElement element, byte defaultFill)
	{
		if (element.getOwnFill() != null)
			return element.getOwnFill().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getFill() != null)
			return baseStyle.getFill().byteValue();
		return defaultFill;
	}

	/**
	 *
	 */
	public static byte getFill(JRPrintGraphicElement element, byte defaultFill)
	{
		if (element.getOwnFill() != null)
			return element.getOwnFill().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getFill() != null)
			return baseStyle.getFill().byteValue();
		return defaultFill;
	}

	/**
	 *
	 */
	public static byte getFill(JRTemplateGraphicElement element, byte defaultFill)
	{
		if (element.getOwnFill() != null)
			return element.getOwnFill().byteValue();
		JRStyle baseStyle = getBaseStyle(element);
		if (baseStyle != null && baseStyle.getFill() != null)
			return baseStyle.getFill().byteValue();
		return defaultFill;
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
	public static int getRadius(JRRectangle rectangle)
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
	public static int getRadius(JRPrintRectangle rectangle)
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
	public static int getRadius(JRTemplateRectangle rectangle)
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
	public static byte getScaleImage(JRImage image)
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
	public static byte getScaleImage(JRPrintImage image)
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
	public static byte getScaleImage(JRTemplateImage image)
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
	public static byte getRotation(JRTextElement element)
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
	public static byte getRotation(JRPrintText element)
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
	public static byte getRotation(JRTemplateText element)
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
	public static byte getLineSpacing(JRTextElement element)
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
	public static byte getLineSpacing(JRPrintText element)
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
	public static byte getLineSpacing(JRTemplateText element)
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
	public static boolean isStyledText(JRTextElement element)
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
	public static boolean isStyledText(JRPrintText element)
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
	public static boolean isStyledText(JRTemplateText element)
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
		return JRProperties.getBooleanProperty(JRFont.DEFAULT_FONT_BOLD);
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
		return JRProperties.getBooleanProperty(JRFont.DEFAULT_FONT_ITALIC);
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
		return JRProperties.getBooleanProperty(JRFont.DEFAULT_FONT_UNDERLINE);
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
		return JRProperties.getBooleanProperty(JRFont.DEFAULT_FONT_STRIKETHROUGH);
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
		return JRFont.DEFAULT_PDF_FONT_NAME;
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
		return JRFont.DEFAULT_PDF_FONT_NAME;
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
		return JRFont.DEFAULT_PDF_ENCODING;
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
		return JRFont.DEFAULT_PDF_ENCODING;
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
	public static byte getBorder(JRBox box)
	{
		if (box.getOwnBorder() != null)
			return box.getOwnBorder().byteValue();
		JRStyle baseStyle = getBaseStyle(box);
		if (baseStyle != null && baseStyle.getBorder() != null)
			return baseStyle.getBorder().byteValue();
		return JRGraphicElement.PEN_NONE;
	}

	/**
	 *
	 */
	public static Byte getBorder(JRStyle style)
	{
		if (style.getOwnBorder() != null)
			return style.getOwnBorder();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getBorder();
		return null;
	}

	/**
	 *
	 */
	public static byte getTopBorder(JRBox box)
	{
		if (box.getOwnTopBorder() != null)
			return box.getOwnTopBorder().byteValue();
		if (box.getOwnBorder() != null)
			return box.getOwnBorder().byteValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getTopBorder() != null)
			return style.getTopBorder().byteValue();
		return JRGraphicElement.PEN_NONE;
	}

	/**
	 *
	 */
	public static Byte getTopBorder(JRStyle style)
	{
		if (style.getOwnTopBorder() != null)
			return style.getOwnTopBorder();
		if (style.getOwnBorder() != null)
			return style.getOwnBorder();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getTopBorder();
		return null;
	}

	/**
	 *
	 */
	public static byte getLeftBorder(JRBox box)
	{
		if (box.getOwnLeftBorder() != null)
			return box.getOwnLeftBorder().byteValue();
		if (box.getOwnBorder() != null)
			return box.getOwnBorder().byteValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getLeftBorder() != null)
			return style.getLeftBorder().byteValue();
		return JRGraphicElement.PEN_NONE;
	}

	/**
	 *
	 */
	public static Byte getLeftBorder(JRStyle style)
	{
		if (style.getOwnLeftBorder() != null)
			return style.getOwnLeftBorder();
		if (style.getOwnBorder() != null)
			return style.getOwnBorder();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getLeftBorder();
		return null;
	}

	/**
	 *
	 */
	public static byte getBottomBorder(JRBox box)
	{
		if (box.getOwnBottomBorder() != null)
			return box.getOwnBottomBorder().byteValue();
		if (box.getOwnBorder() != null)
			return box.getOwnBorder().byteValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getBottomBorder() != null)
			return style.getBottomBorder().byteValue();
		return JRGraphicElement.PEN_NONE;
	}

	/**
	 *
	 */
	public static Byte getBottomBorder(JRStyle style)
	{
		if (style.getOwnBottomBorder() != null)
			return style.getOwnBottomBorder();
		if (style.getOwnBorder() != null)
			return style.getOwnBorder();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getBottomBorder();
		return null;
	}

	/**
	 *
	 */
	public static byte getRightBorder(JRBox box)
	{
		if (box.getOwnRightBorder() != null)
			return box.getOwnRightBorder().byteValue();
		if (box.getOwnBorder() != null)
			return box.getOwnBorder().byteValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getRightBorder() != null)
			return style.getRightBorder().byteValue();
		return JRGraphicElement.PEN_NONE;
	}

	/**
	 *
	 */
	public static Byte getRightBorder(JRStyle style)
	{
		if (style.getOwnRightBorder() != null)
			return style.getOwnRightBorder();
		if (style.getOwnBorder() != null)
			return style.getOwnBorder();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getRightBorder();
		return null;
	}

	/**
	 *
	 */
	public static Color getBorderColor(JRBox box, Color defaultColor)
	{
		if (box.getOwnBorderColor() != null)
			return box.getOwnBorderColor();
		JRStyle baseStyle = getBaseStyle(box);
		if (baseStyle != null && baseStyle.getBorderColor() != null)
			return baseStyle.getBorderColor();
		return defaultColor;
	}

	/**
	 *
	 */
	public static Color getBorderColor(JRStyle style)
	{
		if (style.getOwnBorderColor() != null)
			return style.getOwnBorderColor();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getBorderColor();
		return null;
	}

	/**
	 *
	 */
	public static Color getTopBorderColor(JRBox box, Color defaultColor)
	{
		if (box.getOwnTopBorderColor() != null)
			return box.getOwnTopBorderColor();
		if (box.getOwnBorderColor() != null)
			return box.getOwnBorderColor();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getTopBorderColor() != null)
			return style.getTopBorderColor();
		return defaultColor;
	}

	/**
	 *
	 */
	public static Color getTopBorderColor(JRStyle style)
	{
		if (style.getOwnTopBorderColor() != null)
			return style.getOwnTopBorderColor();
		if (style.getOwnBorderColor() != null)
			return style.getOwnBorderColor();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getTopBorderColor();
		return null;
	}

	/**
	 *
	 */
	public static Color getLeftBorderColor(JRBox box, Color defaultColor)
	{
		if (box.getOwnLeftBorderColor() != null)
			return box.getOwnLeftBorderColor();
		if (box.getOwnBorderColor() != null)
			return box.getOwnBorderColor();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getLeftBorderColor() != null)
			return style.getLeftBorderColor();
		return defaultColor;
	}

	/**
	 *
	 */
	public static Color getLeftBorderColor(JRStyle style)
	{
		if (style.getOwnLeftBorderColor() != null)
			return style.getOwnLeftBorderColor();
		if (style.getOwnBorderColor() != null)
			return style.getOwnBorderColor();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getLeftBorderColor();
		return null;
	}

	/**
	 *
	 */
	public static Color getBottomBorderColor(JRBox box, Color defaultColor)
	{
		if (box.getOwnBottomBorderColor() != null)
			return box.getOwnBottomBorderColor();
		if (box.getOwnBorderColor() != null)
			return box.getOwnBorderColor();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getBottomBorderColor() != null)
			return style.getBottomBorderColor();
		return defaultColor;
	}

	/**
	 *
	 */
	public static Color getBottomBorderColor(JRStyle style)
	{
		if (style.getOwnBottomBorderColor() != null)
			return style.getOwnBottomBorderColor();
		if (style.getOwnBorderColor() != null)
			return style.getOwnBorderColor();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getBottomBorderColor();
		return null;
	}

	/**
	 *
	 */
	public static Color getRightBorderColor(JRBox box, Color defaultColor)
	{
		if (box.getOwnRightBorderColor() != null)
			return box.getOwnRightBorderColor();
		if (box.getOwnBorderColor() != null)
			return box.getOwnBorderColor();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getRightBorderColor() != null)
			return style.getRightBorderColor();
		return defaultColor;
	}

	/**
	 *
	 */
	public static Color getRightBorderColor(JRStyle style)
	{
		if (style.getOwnRightBorderColor() != null)
			return style.getOwnRightBorderColor();
		if (style.getOwnBorderColor() != null)
			return style.getOwnBorderColor();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getRightBorderColor();
		return null;
	}

	/**
	 *
	 */
	public static int getPadding(JRBox box)
	{
		if (box.getOwnPadding() != null)
			return box.getOwnPadding().intValue();
		JRStyle baseStyle = getBaseStyle(box);
		if (baseStyle != null && baseStyle.getPadding() != null)
			return baseStyle.getPadding().intValue();
		return 0;
	}

	/**
	 *
	 */
	public static Integer getPadding(JRStyle style)
	{
		if (style.getOwnPadding() != null)
			return style.getOwnPadding();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getPadding();
		return null;
	}

	/**
	 *
	 */
	public static int getTopPadding(JRBox box)
	{
		if (box.getOwnTopPadding() != null)
			return box.getOwnTopPadding().intValue();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding().intValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getTopPadding() != null)
			return style.getTopPadding().intValue();
		return 0;
	}

	/**
	 *
	 */
	public static Integer getTopPadding(JRStyle style)
	{
		if (style.getOwnTopPadding() != null)
			return style.getOwnTopPadding();
		if (style.getOwnPadding() != null)
			return style.getOwnPadding();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getTopPadding();
		return null;
	}

	/**
	 *
	 */
	public static int getLeftPadding(JRBox box)
	{
		if (box.getOwnLeftPadding() != null)
			return box.getOwnLeftPadding().intValue();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding().intValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getLeftPadding() != null)
			return style.getLeftPadding().intValue();
		return 0;
	}

	/**
	 *
	 */
	public static Integer getLeftPadding(JRStyle style)
	{
		if (style.getOwnLeftPadding() != null)
			return style.getOwnLeftPadding();
		if (style.getOwnPadding() != null)
			return style.getOwnPadding();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getLeftPadding();
		return null;
	}

	/**
	 *
	 */
	public static int getBottomPadding(JRBox box)
	{
		if (box.getOwnBottomPadding() != null)
			return box.getOwnBottomPadding().intValue();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding().intValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getBottomPadding() != null)
			return style.getBottomPadding().intValue();
		return 0;
	}

	/**
	 *
	 */
	public static Integer getBottomPadding(JRStyle style)
	{
		if (style.getOwnBottomPadding() != null)
			return style.getOwnBottomPadding();
		if (style.getOwnPadding() != null)
			return style.getOwnPadding();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getBottomPadding();
		return null;
	}

	/**
	 *
	 */
	public static int getRightPadding(JRBox box)
	{
		if (box.getOwnRightPadding() != null)
			return box.getOwnRightPadding().intValue();
		if (box.getOwnPadding() != null)
			return box.getOwnPadding().intValue();
		JRStyle style = getBaseStyle(box);
		if (style != null && style.getRightPadding() != null)
			return style.getRightPadding().intValue();
		return 0;
	}

	/**
	 *
	 */
	public static Integer getRightPadding(JRStyle style)
	{
		if (style.getOwnRightPadding() != null)
			return style.getOwnRightPadding();
		if (style.getOwnPadding() != null)
			return style.getOwnPadding();
		JRStyle baseStyle = getBaseStyle(style);
		if (baseStyle != null)
			return baseStyle.getRightPadding();
		return null;
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

		if (srcStyle.getOwnPen() != null)
			destStyle.setPen(srcStyle.getOwnPen());
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

		if (srcStyle.getOwnBorder() != null)
			destStyle.setBorder(srcStyle.getOwnBorder());
		if (srcStyle.getOwnTopBorder() != null)
			destStyle.setTopBorder(srcStyle.getOwnTopBorder());
		if (srcStyle.getOwnLeftBorder() != null)
			destStyle.setLeftBorder(srcStyle.getOwnLeftBorder());
		if (srcStyle.getOwnBottomBorder() != null)
			destStyle.setBottomBorder(srcStyle.getOwnBottomBorder());
		if (srcStyle.getOwnRightBorder() != null)
			destStyle.setRightBorder(srcStyle.getOwnRightBorder());
		if (srcStyle.getOwnBorderColor() != null)
			destStyle.setBorderColor(srcStyle.getOwnBorderColor());
		if (srcStyle.getOwnTopBorderColor() != null)
			destStyle.setTopBorderColor(srcStyle.getOwnTopBorderColor());
		if (srcStyle.getOwnLeftBorderColor() != null)
			destStyle.setLeftBorderColor(srcStyle.getOwnLeftBorderColor());
		if (srcStyle.getOwnBottomBorderColor() != null)
			destStyle.setBottomBorderColor(srcStyle.getOwnBottomBorderColor());
		if (srcStyle.getOwnRightBorderColor() != null)
			destStyle.setRightBorderColor(srcStyle.getOwnRightBorderColor());
		if (srcStyle.getOwnPadding() != null)
			destStyle.setPadding(srcStyle.getOwnPadding());
		if (srcStyle.getOwnTopPadding() != null)
			destStyle.setTopPadding(srcStyle.getOwnTopPadding());
		if (srcStyle.getOwnLeftPadding() != null)
			destStyle.setLeftPadding(srcStyle.getOwnLeftPadding());
		if (srcStyle.getOwnBottomPadding() != null)
			destStyle.setBottomPadding(srcStyle.getOwnBottomPadding());
		if (srcStyle.getOwnRightPadding() != null)
			destStyle.setRightPadding(srcStyle.getOwnRightPadding());

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
}
