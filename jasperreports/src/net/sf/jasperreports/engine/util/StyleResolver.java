/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.charts.JRCategoryAxisFormat;
import net.sf.jasperreports.charts.JRTimeAxisFormat;
import net.sf.jasperreports.charts.JRValueAxisFormat;
import net.sf.jasperreports.charts.JRXAxisFormat;
import net.sf.jasperreports.charts.JRYAxisFormat;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRBoxContainer;
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
import net.sf.jasperreports.engine.JRParagraphContainer;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPenContainer;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.JRTextAlignment;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JasperReportsContext;
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
import net.sf.jasperreports.properties.PropertyConstants;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class StyleResolver
{
	@Property(
			category = PropertyConstants.CATEGORY_DESIGN,
			defaultValue = PropertyConstants.BOOLEAN_TRUE,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_2_1,
			valueType = Boolean.class
			)
	public static final String PROPERTY_STYLES_INHERIT_FROM_DEFAULT = JRPropertiesUtil.PROPERTY_PREFIX + "styles.inherit.from.default";
	
	private static final StyleResolver INSTANCE = new StyleResolver(DefaultJasperReportsContext.getInstance());

	private static final Integer INTEGER_ZERO = Integer.valueOf(0);
	
	private final JRPropertiesUtil propertiesUtil;
	private final Boolean stylesInheritFromDefault;


	/**
	 *
	 */
	public StyleResolver(JasperReportsContext jasperReportsContext)
	{
		propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
		stylesInheritFromDefault = propertiesUtil.getBooleanProperty(PROPERTY_STYLES_INHERIT_FROM_DEFAULT);
	}

	/**
	 *
	 */
	public static StyleResolver getInstance()
	{
		return INSTANCE;
	}

	/**
	 *
	 */
	public JRStyle getBaseStyle(JRStyle styleContainer)
	{
		if (styleContainer != null)
		{
			JRStyle style = styleContainer.getStyle();
			if (style != null)
			{
				return style;
			}
			if (stylesInheritFromDefault && !styleContainer.isDefault())
			{
				JRDefaultStyleProvider defaultStyleProvider = styleContainer.getDefaultStyleProvider();
				if (defaultStyleProvider != null)
				{
					return defaultStyleProvider.getDefaultStyle();
				}
			}
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getBaseStyle(JRLineBox box)
	{
		if (box != null)
		{
			JRBoxContainer boxContainer = box.getBoxContainer();

			if (boxContainer instanceof JRStyle)
			{
				return getBaseStyle((JRStyle)boxContainer);
			}
			
			return getBaseStyle(boxContainer);
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getBaseStyle(JRPen pen)
	{
		if (pen != null)
		{
			JRPenContainer penContainer = pen.getPenContainer();

			if (penContainer instanceof JRStyle)
			{
				return getBaseStyle((JRStyle)penContainer);
			}

			if (penContainer instanceof JRLineBox)
			{
				return getBaseStyle((JRLineBox)penContainer);
			}
			
			return getBaseStyle(penContainer);
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getBaseStyle(JRParagraph paragraph)
	{
		if (paragraph != null)
		{
			JRParagraphContainer paragraphContainer = paragraph.getParagraphContainer();

			if (paragraphContainer instanceof JRStyle)
			{
				return getBaseStyle((JRStyle)paragraphContainer);
			}
			
			return getBaseStyle(paragraphContainer);
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getBaseStyle(JRStyleContainer styleContainer)
	{
		if (styleContainer != null)
		{
			if (styleContainer instanceof JRStyle)
			{
				return getBaseStyle((JRStyle)styleContainer);
			}
			return getBaseStyleFromStyleContainer(styleContainer);
		}
		return null;
	}

	/**
	 *
	 */
	protected static JRStyle getBaseStyleFromStyleContainer(JRStyleContainer styleContainer)
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
	public ModeEnum getMode(JRCommonElement element, ModeEnum defaultMode)
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
	public ModeEnum getModeValue(JRStyle style)
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
	public Color getForecolor(JRCommonElement element)
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
	public Color getForecolor(JRChartPlot plot)
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
	public Color getForecolor(JRStyle style)
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
	public Color getBackcolor(JRCommonElement element)
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
	public Color getBackcolor(JRChartPlot plot)
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
	public Color getBackcolor(JRStyle style)
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
	public Float getLineWidth(JRPen pen, Float defaultLineWidth)
	{
		Float ownLineWidth = pen.getOwnLineWidth();
		if (ownLineWidth != null)
		{
			return ownLineWidth;
		}
		JRStyle baseStyle = getBaseStyle(pen);
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
	public Float getLineWidth(JRBoxPen boxPen, Float defaultLineWidth)
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
		JRStyle baseStyle = getBaseStyle(boxPen);
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
	public LineStyleEnum getLineStyleValue(JRPen pen)
	{
		LineStyleEnum ownLineStyle = pen.getOwnLineStyleValue();
		if (ownLineStyle != null)
		{
			return ownLineStyle;
		}
		JRStyle baseStyle = getBaseStyle(pen);
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
	public LineStyleEnum getLineStyleValue(JRBoxPen boxPen)
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
		JRStyle baseStyle = getBaseStyle(boxPen);
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
	public Color getLineColor(JRPen pen, Color defaultColor)
	{
		Color ownLineColor = pen.getOwnLineColor();
		if (ownLineColor != null)
		{
			return ownLineColor;
		}
		JRStyle baseStyle = getBaseStyle(pen);
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
	public Color getLineColor(JRBoxPen boxPen, Color defaultColor)
	{
		//FIXMENOW line color is resolved to base style forecolor before current pen container forecolor; 
		// for example, rectangle with blue forecolor would have border drawn in red, if it uses style with red forecolor
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
		JRStyle baseStyle = getBaseStyle(boxPen);
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
	public FillEnum getFillValue(JRCommonGraphicElement element)
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
	public FillEnum getFillValue(JRStyle style)
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
	public int getRadius(JRCommonRectangle rectangle)
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
	public Integer getRadius(JRStyle style)
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
	public ScaleImageEnum getScaleImageValue(JRCommonImage image)
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
	public ScaleImageEnum getScaleImageValue(JRStyle style)
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
	public HorizontalTextAlignEnum getHorizontalTextAlign(JRTextAlignment alignment)
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
	public HorizontalImageAlignEnum getHorizontalImageAlign(JRImageAlignment alignment)
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
	 *
	 */
	public HorizontalTextAlignEnum getHorizontalTextAlign(JRStyle style)
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
	public HorizontalImageAlignEnum getHorizontalImageAlign(JRStyle style)
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
	 *
	 */
	public VerticalTextAlignEnum getVerticalTextAlign(JRTextAlignment alignment)
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
	public VerticalImageAlignEnum getVerticalImageAlign(JRImageAlignment alignment)
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
	 *
	 */
	public VerticalTextAlignEnum getVerticalTextAlign(JRStyle style)
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
	public VerticalImageAlignEnum getVerticalImageAlign(JRStyle style)
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
	public Float getLineSpacingSize(JRParagraph paragraph)
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
		return propertiesUtil.getFloatProperty(JRParagraph.DEFAULT_LINE_SPACING_SIZE);
	}

	/**
	 *
	 */
	public Integer getFirstLineIndent(JRParagraph paragraph)
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
		return propertiesUtil.getIntegerProperty(JRParagraph.DEFAULT_FIRST_LINE_INDENT);
	}

	/**
	 *
	 */
	public Integer getLeftIndent(JRParagraph paragraph)
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
		return propertiesUtil.getIntegerProperty(JRParagraph.DEFAULT_LEFT_INDENT);
	}

	/**
	 *
	 */
	public Integer getRightIndent(JRParagraph paragraph)
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
		return propertiesUtil.getIntegerProperty(JRParagraph.DEFAULT_RIGHT_INDENT);
	}

	/**
	 *
	 */
	public Integer getSpacingBefore(JRParagraph paragraph)
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
		return propertiesUtil.getIntegerProperty(JRParagraph.DEFAULT_SPACING_BEFORE);
	}

	/**
	 *
	 */
	public Integer getSpacingAfter(JRParagraph paragraph)
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
		return propertiesUtil.getIntegerProperty(JRParagraph.DEFAULT_SPACING_AFTER);
	}

	/**
	 *
	 */
	public Integer getTabStopWidth(JRParagraph paragraph)
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
		return propertiesUtil.getIntegerProperty(JRParagraph.DEFAULT_TAB_STOP_WIDTH);
	}

	/**
	 *
	 */
	public TabStop[] getTabStops(JRParagraph paragraph)
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
	public RotationEnum getRotationValue(JRCommonText element)
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
	public RotationEnum getRotationValue(JRStyle style)
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
	 * 
	 */
	public LineSpacingEnum getLineSpacing(JRParagraph paragraph)
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
	public String getMarkup(JRCommonText element)
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
	public String getMarkup(JRStyle style)
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
	public String getPattern(JRTextField element)
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
	public String getPattern(JRStyle style)
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
	public boolean isBlankWhenNull(JRTextField element)
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
	public Boolean isBlankWhenNull(JRStyle style)
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
	public String getFontName(JRFont font)
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
		return propertiesUtil.getProperty(JRFont.DEFAULT_FONT_NAME);
	}
	
	/**
	 *
	 */
	public String getFontName(JRStyle style)
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
		return propertiesUtil.getProperty(JRFont.DEFAULT_FONT_NAME);
	}

	/**
	 *
	 */
	public boolean isBold(JRFont font)
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
	public Boolean isBold(JRStyle style)
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
	public boolean isItalic(JRFont font)
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
	public Boolean isItalic(JRStyle style)
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
	public boolean isUnderline(JRFont font)
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
	public Boolean isUnderline(JRStyle style)
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
	public boolean isStrikeThrough(JRFont font)
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
	public Boolean isStrikeThrough(JRStyle style)
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
	public float getFontsize(JRFont font)
	{
		Float ownFontSize = font.getOwnFontsize();
		if (ownFontSize != null)
		{
			return ownFontSize.floatValue();
		}
		JRStyle baseStyle = getBaseStyle(font);
		if (baseStyle != null)
		{
			Float fontSize = baseStyle.getFontsize();
			if (fontSize != null)
			{
				return fontSize.floatValue();
			}
		}
		return propertiesUtil.getFloatProperty(JRFont.DEFAULT_FONT_SIZE);
	}
	
	/**
	 *
	 */
	public Float getFontsize(JRStyle style)
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
	 *
	 */
	public String getPdfFontName(JRFont font)
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
		return propertiesUtil.getProperty(JRFont.DEFAULT_PDF_FONT_NAME);
	}
	
	/**
	 *
	 */
	public String getPdfFontName(JRStyle style)
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
		return propertiesUtil.getProperty(JRFont.DEFAULT_PDF_FONT_NAME);
	}

	/**
	 *
	 */
	public String getPdfEncoding(JRFont font)
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
		return propertiesUtil.getProperty(JRFont.DEFAULT_PDF_ENCODING);
	}
	
	/**
	 *
	 */
	public String getPdfEncoding(JRStyle style)
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
		return propertiesUtil.getProperty(JRFont.DEFAULT_PDF_ENCODING);
	}

	/**
	 *
	 */
	public boolean isPdfEmbedded(JRFont font)
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
		return propertiesUtil.getBooleanProperty(JRFont.DEFAULT_PDF_EMBEDDED);
	}
	
	/**
	 *
	 */
	public Boolean isPdfEmbedded(JRStyle style)
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
	public Integer getPadding(JRLineBox box)
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
	public Integer getTopPadding(JRLineBox box)
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
	public Integer getLeftPadding(JRLineBox box)
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
	public Integer getBottomPadding(JRLineBox box)
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
	public Integer getRightPadding(JRLineBox box)
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
	 *
	 */
	public Color getTitleColor(JRChart chart)
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
	public Color getSubtitleColor(JRChart chart)
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
	public Color getLegendColor(JRChart chart)
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
	public Color getLegendBackgroundColor(JRChart chart)
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
	public Color getCategoryAxisLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getCategoryAxisTickLabelColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getCategoryAxisLineColor(JRCategoryAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getValueAxisLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getValueAxisTickLabelColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getValueAxisLineColor(JRValueAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getXAxisLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getXAxisTickLabelColor(JRXAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getXAxisLineColor(JRXAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getYAxisLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getYAxisTickLabelColor(JRYAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getYAxisLineColor(JRYAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getTimeAxisLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getTimeAxisTickLabelColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
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
	public Color getTimeAxisLineColor(JRTimeAxisFormat axisFormat, JRChartPlot plot)
	{
		Color ownTimeAxisLineColor = axisFormat.getOwnTimeAxisLineColor();
		if (ownTimeAxisLineColor != null) 
		{
			return ownTimeAxisLineColor;
		}
		return getForecolor(plot);
	}
}
