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
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.BreakTypeEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Second;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlConstants extends XmlConstants
{


	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#OPAQUE#getName()}.
	 */
	private static final String POSITION_TYPE_FLOAT = PositionTypeEnum.FLOAT.getName();
	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#POSITION_TYPE_FIX_RELATIVE_TO_TOP#getName()}.
	 */
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_TOP = PositionTypeEnum.FIX_RELATIVE_TO_TOP.getName();
	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM#getName()}.
	 */
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM = PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM.getName();

	/**
	 * @deprecated Replaced by {@link PositionTypeEnum}.
	 */
	private static Map positionTypeMap = null;

	/**
	 * @deprecated Replaced by {@link PositionTypeEnum}.
	 */
	public static Map getPositionTypeMap()
	{
		if (positionTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(POSITION_TYPE_FLOAT,                  new Byte(JRElement.POSITION_TYPE_FLOAT));
			map.put(POSITION_TYPE_FIX_RELATIVE_TO_TOP,    new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP));
			map.put(POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM, new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM));
			map.put(new Byte(JRElement.POSITION_TYPE_FLOAT),                  POSITION_TYPE_FLOAT);
			map.put(new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP),    POSITION_TYPE_FIX_RELATIVE_TO_TOP);
			map.put(new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM), POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM);
			positionTypeMap = Collections.unmodifiableMap(map);
		}

		return positionTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link ModeEnum#OPAQUE#getName()}.
	 */
	private static final String MODE_OPAQUE = ModeEnum.OPAQUE.getName();
	/**
	 * @deprecated Replaced by {@link ModeEnum#TRANSPARENT#getName()}.
	 */
	private static final String MODE_TRANSPARENT = ModeEnum.TRANSPARENT.getName();

	/**
	 * @deprecated Replaced by {@link ModeEnum}.
	 */
	private static Map modeMap = null;

	/**
	 * @deprecated Replaced by {@link ModeEnum}.
	 */
	public static Map getModeMap()
	{
		if (modeMap == null)
		{
			Map map = new HashMap(6);
			map.put(MODE_OPAQUE,      new Byte(JRElement.MODE_OPAQUE));
			map.put(MODE_TRANSPARENT, new Byte(JRElement.MODE_TRANSPARENT));
			map.put(new Byte(JRElement.MODE_OPAQUE),      MODE_OPAQUE);
			map.put(new Byte(JRElement.MODE_TRANSPARENT), MODE_TRANSPARENT);
			modeMap = Collections.unmodifiableMap(map);
		}

		return modeMap;
	}

	/**
	 *
	 */
	private static final String COLOR_BLACK = "black";
	private static final String COLOR_BLUE = "blue";
	private static final String COLOR_CYAN = "cyan";
	private static final String COLOR_DARK_GRAY = "darkGray";
	private static final String COLOR_GRAY = "gray";
	private static final String COLOR_GREEN = "green";
	private static final String COLOR_LIGHT_GRAY = "lightGray";
	private static final String COLOR_MAGENTA = "magenta";
	private static final String COLOR_ORANGE = "orange";
	private static final String COLOR_PINK = "pink";
	private static final String COLOR_RED = "red";
	private static final String COLOR_YELLOW = "yellow";
	private static final String COLOR_WHITE = "white";

	private static Map colorMap = null;

	public static Map getColorMap()
	{
		if (colorMap == null)
		{
			Map map = new HashMap(35);
			map.put(COLOR_BLACK,      Color.black);
			map.put(COLOR_BLUE,       Color.blue);
			map.put(COLOR_CYAN,       Color.cyan);
			map.put(COLOR_DARK_GRAY,  Color.darkGray);
			map.put(COLOR_GRAY,       Color.gray);
			map.put(COLOR_GREEN,      Color.green);
			map.put(COLOR_LIGHT_GRAY, Color.lightGray);
			map.put(COLOR_MAGENTA,    Color.magenta);
			map.put(COLOR_ORANGE,     Color.orange);
			map.put(COLOR_PINK,       Color.pink);
			map.put(COLOR_RED,        Color.red);
			map.put(COLOR_YELLOW,     Color.yellow);
			map.put(COLOR_WHITE,      Color.white);
			map.put(Color.black,      COLOR_BLACK);
			map.put(Color.blue,       COLOR_BLUE);
			map.put(Color.cyan,       COLOR_CYAN);
			map.put(Color.darkGray,   COLOR_DARK_GRAY);
			map.put(Color.gray,       COLOR_GRAY);
			map.put(Color.green,      COLOR_GREEN);
			map.put(Color.lightGray,  COLOR_LIGHT_GRAY);
			map.put(Color.magenta,    COLOR_MAGENTA);
			map.put(Color.orange,     COLOR_ORANGE);
			map.put(Color.pink,       COLOR_PINK);
			map.put(Color.red,        COLOR_RED);
			map.put(Color.yellow,     COLOR_YELLOW);
			map.put(Color.white,      COLOR_WHITE);
			colorMap = Collections.unmodifiableMap(map);
		}

		return colorMap;
	}

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_LEFT#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_LEFT = HorizontalAlignEnum.LEFT.getName();
	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_CENTER#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_CENTER = HorizontalAlignEnum.CENTER.getName();
	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_RIGHT#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_RIGHT = HorizontalAlignEnum.RIGHT.getName();
	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_JUSTIFIED#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_JUSTIFIED = HorizontalAlignEnum.JUSTIFIED.getName();

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum}.
	 */
	private static Map horizontalAlignMap = null;

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum}.
	 */
	public static Map getHorizontalAlignMap()
	{
		if (horizontalAlignMap == null)
		{
			Map map = new HashMap(11);
			map.put(HORIZONTAL_ALIGN_LEFT,      new Byte(JRAlignment.HORIZONTAL_ALIGN_LEFT));
			map.put(HORIZONTAL_ALIGN_CENTER,    new Byte(JRAlignment.HORIZONTAL_ALIGN_CENTER));
			map.put(HORIZONTAL_ALIGN_RIGHT,     new Byte(JRAlignment.HORIZONTAL_ALIGN_RIGHT));
			map.put(HORIZONTAL_ALIGN_JUSTIFIED, new Byte(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED));
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_LEFT),      HORIZONTAL_ALIGN_LEFT);
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_CENTER),    HORIZONTAL_ALIGN_CENTER);
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_RIGHT),     HORIZONTAL_ALIGN_RIGHT);
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED), HORIZONTAL_ALIGN_JUSTIFIED);
			horizontalAlignMap = Collections.unmodifiableMap(map);
		}

		return horizontalAlignMap;
	}

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum}.
	 */
	public static Map getTextAlignMap()
	{
		return getHorizontalAlignMap();
	}

	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum#TOP#getName()}.
	 */
	private static final String VERTICAL_ALIGN_TOP = VerticalAlignEnum.TOP.getName();
	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum#MIDDLE#getName()}.
	 */
	private static final String VERTICAL_ALIGN_MIDDLE = VerticalAlignEnum.MIDDLE.getName();
	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum#BOTTOM#getName()}.
	 */
	private static final String VERTICAL_ALIGN_BOTTOM = VerticalAlignEnum.BOTTOM.getName();

	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum}.
	 */
	private static Map verticalAlignMap = null;

	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum}.
	 */
	public static Map getVerticalAlignMap()
	{
		if (verticalAlignMap == null)
		{
			Map map = new HashMap(8);
			map.put(VERTICAL_ALIGN_TOP,    new Byte(JRAlignment.VERTICAL_ALIGN_TOP));
			map.put(VERTICAL_ALIGN_MIDDLE, new Byte(JRAlignment.VERTICAL_ALIGN_MIDDLE));
			map.put(VERTICAL_ALIGN_BOTTOM, new Byte(JRAlignment.VERTICAL_ALIGN_BOTTOM));
			map.put(new Byte(JRAlignment.VERTICAL_ALIGN_TOP),    VERTICAL_ALIGN_TOP);
			map.put(new Byte(JRAlignment.VERTICAL_ALIGN_MIDDLE), VERTICAL_ALIGN_MIDDLE);
			map.put(new Byte(JRAlignment.VERTICAL_ALIGN_BOTTOM), VERTICAL_ALIGN_BOTTOM);
			verticalAlignMap = Collections.unmodifiableMap(map);
		}

		return verticalAlignMap;
	}

	/**
	 * @deprecated Replaced by {@link RotationEnum#NONE#getName()}.
	 */
	private static final String ROTATION_NONE = "None";
	/**
	 * @deprecated Replaced by {@link RotationEnum#LEFT#getName()}.
	 */
	private static final String ROTATION_LEFT = "Left";
	/**
	 * @deprecated Replaced by {@link RotationEnum#RIGHT#getName()}.
	 */
	private static final String ROTATION_RIGHT = "Right";
	/**
	 * @deprecated Replaced by {@link RotationEnum#UPSIDE_DOWN#getName()}.
	 */
	private static final String ROTATION_UPSIDE_DOWN = "UpsideDown";

	/**
	 * @deprecated Replaced by {@link RotationEnum}.
	 */
	private static Map rotationMap = null;

	/**
	 * @deprecated Replaced by {@link RotationEnum}.
	 */
	public static Map getRotationMap()
	{
		if (rotationMap == null)
		{
			Map map = new HashMap(11);
			map.put(ROTATION_NONE,  		new Byte(JRTextElement.ROTATION_NONE));
			map.put(ROTATION_LEFT,  		new Byte(JRTextElement.ROTATION_LEFT));
			map.put(ROTATION_RIGHT, 		new Byte(JRTextElement.ROTATION_RIGHT));
			map.put(ROTATION_UPSIDE_DOWN, 	new Byte(JRTextElement.ROTATION_UPSIDE_DOWN));
			map.put(new Byte(JRTextElement.ROTATION_NONE),  		ROTATION_NONE);
			map.put(new Byte(JRTextElement.ROTATION_LEFT),  		ROTATION_LEFT);
			map.put(new Byte(JRTextElement.ROTATION_RIGHT), 		ROTATION_RIGHT);
			map.put(new Byte(JRTextElement.ROTATION_UPSIDE_DOWN), 	ROTATION_UPSIDE_DOWN);
			rotationMap = Collections.unmodifiableMap(map);
		}

		return rotationMap;
	}

	/**
	 * @deprecated Replaced by {@link BreakTypeEnum#PAGE#getName()}.
	 */
	private static final String BREAK_TYPE_PAGE = "Page";
	/**
	 * @deprecated Replaced by {@link BreakTypeEnum#COLUMN#getName()}.
	 */
	private static final String BREAK_TYPE_COLUMN = "Column";

	/**
	 * @deprecated Replaced by {@link BreakTypeEnum}.
	 */
	private static Map breakTypeMap = null;

	/**
	 * @deprecated Replaced by {@link BreakTypeEnum}.
	 */
	public static Map getBreakTypeMap()
	{
		if (breakTypeMap == null)
		{
			Map map = new HashMap(6);
			map.put(BREAK_TYPE_PAGE,   new Byte(JRBreak.TYPE_PAGE));
			map.put(BREAK_TYPE_COLUMN, new Byte(JRBreak.TYPE_COLUMN));
			map.put(new Byte(JRBreak.TYPE_PAGE),   BREAK_TYPE_PAGE);
			map.put(new Byte(JRBreak.TYPE_COLUMN), BREAK_TYPE_COLUMN);
			breakTypeMap = Collections.unmodifiableMap(map);
		}

		return breakTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link RunDirectionEnum#LTR#getName()}.
	 */
	private static final String RUN_DIRECTION_LTR = "LTR";
	/**
	 * @deprecated Replaced by {@link RunDirectionEnum#RTL#getName()}.
	 */
	private static final String RUN_DIRECTION_RTL = "RTL";

	/**
	 * @deprecated Replaced by {@link RunDirectionEnum}.
	 */
	private static Map runDirectionMap = null;

	/**
	 * @deprecated Replaced by {@link RunDirectionEnum}.
	 */
	public static Map getRunDirectionMap()
	{
		if (runDirectionMap == null)
		{
			Map map = new HashMap(6);
			map.put(RUN_DIRECTION_LTR, new Byte(JRPrintText.RUN_DIRECTION_LTR));
			map.put(RUN_DIRECTION_RTL, new Byte(JRPrintText.RUN_DIRECTION_RTL));
			map.put(new Byte(JRPrintText.RUN_DIRECTION_LTR), RUN_DIRECTION_LTR);
			map.put(new Byte(JRPrintText.RUN_DIRECTION_RTL), RUN_DIRECTION_RTL);
			runDirectionMap = Collections.unmodifiableMap(map);
		}

		return runDirectionMap;
	}

	/**
	 * @deprecated Replaced by {@link LineSpacingEnum#SINGLE#getName()}.
	 */
	private static final String LINE_SPACING_SINGLE = "Single";
	/**
	 * @deprecated Replaced by {@link LineSpacingEnum#ONE_AND_HALF#getName()}.
	 */
	private static final String LINE_SPACING_1_1_2 = "1_1_2";
	/**
	 * @deprecated Replaced by {@link LineSpacingEnum#DOUBLE#getName()}.
	 */
	private static final String LINE_SPACING_DOUBLE = "Double";

	/**
	 * @deprecated Replaced by {@link LineSpacingEnum}.
	 */
	private static Map lineSpacingMap = null;

	/**
	 * @deprecated Replaced by {@link LineSpacingEnum}.
	 */
	public static Map getLineSpacingMap()
	{
		if (lineSpacingMap == null)
		{
			Map map = new HashMap(8);
			map.put(LINE_SPACING_SINGLE, new Byte(JRTextElement.LINE_SPACING_SINGLE));
			map.put(LINE_SPACING_1_1_2,  new Byte(JRTextElement.LINE_SPACING_1_1_2));
			map.put(LINE_SPACING_DOUBLE, new Byte(JRTextElement.LINE_SPACING_DOUBLE));
			map.put(new Byte(JRTextElement.LINE_SPACING_SINGLE), LINE_SPACING_SINGLE);
			map.put(new Byte(JRTextElement.LINE_SPACING_1_1_2),  LINE_SPACING_1_1_2);
			map.put(new Byte(JRTextElement.LINE_SPACING_DOUBLE), LINE_SPACING_DOUBLE);
			lineSpacingMap = Collections.unmodifiableMap(map);
		}

		return lineSpacingMap;
	}

	/**
	 * @deprecated Replaced by {@link LineDirectionEnum#TOP_DOWN#getName()}.
	 */
	private static final String DIRECTION_TOP_DOWN = "TopDown";
	/**
	 * @deprecated Replaced by {@link LineDirectionEnum#BOTTOM_UP#getName()}.
	 */
	private static final String DIRECTION_BOTTOM_UP = "BottomUp";

	/**
	 * @deprecated Replaced by {@link LineDirectionEnum}.
	 */
	private static Map directionMap = null;

	/**
	 * @deprecated Replaced by {@link LineDirectionEnum}.
	 */
	public static Map getDirectionMap()
	{
		if (directionMap == null)
		{
			Map map = new HashMap(6);
			map.put(DIRECTION_TOP_DOWN,  new Byte(JRLine.DIRECTION_TOP_DOWN));
			map.put(DIRECTION_BOTTOM_UP, new Byte(JRLine.DIRECTION_BOTTOM_UP));
			map.put(new Byte(JRLine.DIRECTION_TOP_DOWN),  DIRECTION_TOP_DOWN);
			map.put(new Byte(JRLine.DIRECTION_BOTTOM_UP), DIRECTION_BOTTOM_UP);
			directionMap = Collections.unmodifiableMap(map);
		}

		return directionMap;
	}

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#CLIP#getName()}.
	 */
	private static final String SCALE_IMAGE_CLIP = "Clip";
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#FILL_FRAME#getName()}.
	 */
	private static final String SCALE_IMAGE_FILL_FRAME = "FillFrame";
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#RETAIN_SHAPE#getName()}.
	 */
	private static final String SCALE_IMAGE_RETAIN_SHAPE = "RetainShape";
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#REAL_HEIGHT#getName()}.
	 */
	private static final String SCALE_IMAGE_REAL_HEIGT = "RealHeight";
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#REAL_SIZE#getName()}.
	 */
	private static final String SCALE_IMAGE_REAL_SIZE = "RealSize";

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum}.
	 */
	private static Map scaleImageMap = null;

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum}.
	 */
	public static Map getScaleImageMap()
	{
		if (scaleImageMap == null)
		{
			Map map = new HashMap(14);
			map.put(SCALE_IMAGE_CLIP,         new Byte(JRImage.SCALE_IMAGE_CLIP));
			map.put(SCALE_IMAGE_FILL_FRAME,   new Byte(JRImage.SCALE_IMAGE_FILL_FRAME));
			map.put(SCALE_IMAGE_RETAIN_SHAPE, new Byte(JRImage.SCALE_IMAGE_RETAIN_SHAPE));
			map.put(SCALE_IMAGE_REAL_HEIGT, new Byte(JRImage.SCALE_IMAGE_REAL_HEIGHT));
			map.put(SCALE_IMAGE_REAL_SIZE, new Byte(JRImage.SCALE_IMAGE_REAL_SIZE));
			map.put(new Byte(JRImage.SCALE_IMAGE_CLIP),         SCALE_IMAGE_CLIP);
			map.put(new Byte(JRImage.SCALE_IMAGE_FILL_FRAME),   SCALE_IMAGE_FILL_FRAME);
			map.put(new Byte(JRImage.SCALE_IMAGE_RETAIN_SHAPE), SCALE_IMAGE_RETAIN_SHAPE);
			map.put(new Byte(JRImage.SCALE_IMAGE_REAL_HEIGHT), SCALE_IMAGE_REAL_HEIGT);
			map.put(new Byte(JRImage.SCALE_IMAGE_REAL_SIZE), SCALE_IMAGE_REAL_SIZE);
			scaleImageMap = Collections.unmodifiableMap(map);
		}

		return scaleImageMap;
	}

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#ERROR#getName()}.
	 */
	private static final String ON_ERROR_TYPE_ERROR = "Error";
	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#BLANK#getName()}.
	 */
	private static final String ON_ERROR_TYPE_BLANK = "Blank";
	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#ICON#getName()}.
	 */
	private static final String ON_ERROR_TYPE_ICON = "Icon";

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum}.
	 */
	private static Map onErrorTypeMap = null;

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum}.
	 */
	public static Map getOnErrorTypeMap()
	{
		if (onErrorTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(ON_ERROR_TYPE_ERROR, new Byte(JRImage.ON_ERROR_TYPE_ERROR));
			map.put(ON_ERROR_TYPE_BLANK, new Byte(JRImage.ON_ERROR_TYPE_BLANK));
			map.put(ON_ERROR_TYPE_ICON,  new Byte(JRImage.ON_ERROR_TYPE_ICON));
			map.put(new Byte(JRImage.ON_ERROR_TYPE_ERROR), ON_ERROR_TYPE_ERROR);
			map.put(new Byte(JRImage.ON_ERROR_TYPE_BLANK), ON_ERROR_TYPE_BLANK);
			map.put(new Byte(JRImage.ON_ERROR_TYPE_ICON),  ON_ERROR_TYPE_ICON);
			onErrorTypeMap = Collections.unmodifiableMap(map);
		}

		return onErrorTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link StretchTypeEnum#NO_STRETCH#getName()}.
	 */
	private static final String STRETCH_TYPE_NO_STRETCH = "NoStretch";
	/**
	 * @deprecated Replaced by {@link StretchTypeEnum#RELATIVE_TO_TALLEST_OBJECT#getName()}.
	 */
	private static final String STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT = "RelativeToTallestObject";
	/**
	 * @deprecated Replaced by {@link StretchTypeEnum#RELATIVE_TO_BAND_HEIGHT#getName()}.
	 */
	private static final String STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT = "RelativeToBandHeight";

	/**
	 * @deprecated Replaced by {@link StretchTypeEnum}.
	 */
	private static Map stretchTypeMap = null;

	/**
	 * @deprecated Replaced by {@link StretchTypeEnum}.
	 */
	public static Map getStretchTypeMap()
	{
		if (stretchTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(STRETCH_TYPE_NO_STRETCH,                 new Byte(JRElement.STRETCH_TYPE_NO_STRETCH));
			map.put(STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT, new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT));
			map.put(STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT,    new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT));
			map.put(new Byte(JRElement.STRETCH_TYPE_NO_STRETCH),                 STRETCH_TYPE_NO_STRETCH);
			map.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT), STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT);
			map.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT),    STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT);
			stretchTypeMap = Collections.unmodifiableMap(map);
		}

		return stretchTypeMap;
	}

	/**
	 *
	 */
	private static final String LINE_STYLE_SOLID = "Solid";
	private static final String LINE_STYLE_DASHED = "Dashed";
	private static final String LINE_STYLE_DOTTED = "Dotted";
	private static final String lINE_STYLE_DOUBLE = "Double";

	private static Map lineStyleMap = null;

	public static Map getLineStyleMap()
	{
		if (lineStyleMap == null)
		{
			Map map = new HashMap(11);
			map.put(LINE_STYLE_SOLID,  new Byte(JRPen.LINE_STYLE_SOLID));
			map.put(LINE_STYLE_DASHED, new Byte(JRPen.LINE_STYLE_DASHED));
			map.put(LINE_STYLE_DOTTED, new Byte(JRPen.LINE_STYLE_DOTTED));
			map.put(lINE_STYLE_DOUBLE, new Byte(JRPen.LINE_STYLE_DOUBLE));
			map.put(new Byte(JRPen.LINE_STYLE_SOLID),  LINE_STYLE_SOLID);
			map.put(new Byte(JRPen.LINE_STYLE_DASHED), LINE_STYLE_DASHED);
			map.put(new Byte(JRPen.LINE_STYLE_DOTTED), LINE_STYLE_DOTTED);
			map.put(new Byte(JRPen.LINE_STYLE_DOUBLE), lINE_STYLE_DOUBLE);
			lineStyleMap = Collections.unmodifiableMap(map);
		}

		return lineStyleMap;
	}

	/**
	 *
	 */
	private static final String PEN_NONE = "None";
	private static final String PEN_THIN = "Thin";
	private static final String PEN_1_POINT = "1Point";
	private static final String PEN_2_POINT = "2Point";
	private static final String PEN_4_POINT = "4Point";
	private static final String PEN_DOTTED = "Dotted";

	private static Map penMap = null;

	public static Map getPenMap()
	{
		if (penMap == null)
		{
			Map map = new HashMap(16);
			map.put(PEN_NONE,     new Byte(JRGraphicElement.PEN_NONE));
			map.put(PEN_THIN,     new Byte(JRGraphicElement.PEN_THIN));
			map.put(PEN_1_POINT,  new Byte(JRGraphicElement.PEN_1_POINT));
			map.put(PEN_2_POINT,  new Byte(JRGraphicElement.PEN_2_POINT));
			map.put(PEN_4_POINT,  new Byte(JRGraphicElement.PEN_4_POINT));
			map.put(PEN_DOTTED,   new Byte(JRGraphicElement.PEN_DOTTED));
			map.put(new Byte(JRGraphicElement.PEN_NONE),     PEN_NONE);
			map.put(new Byte(JRGraphicElement.PEN_THIN),     PEN_THIN);
			map.put(new Byte(JRGraphicElement.PEN_1_POINT),  PEN_1_POINT);
			map.put(new Byte(JRGraphicElement.PEN_2_POINT),  PEN_2_POINT);
			map.put(new Byte(JRGraphicElement.PEN_4_POINT),  PEN_4_POINT);
			map.put(new Byte(JRGraphicElement.PEN_DOTTED),   PEN_DOTTED);
			penMap = Collections.unmodifiableMap(map);
		}

		return penMap;
	}

	/**
	 *
	 */
	private static final String FILL_SOLID = "Solid";

	private static Map fillMap = null;

	public static Map getFillMap()
	{
		if (fillMap == null)
		{
			Map map = new HashMap(3);
			map.put(FILL_SOLID, new Byte(JRGraphicElement.FILL_SOLID));
			map.put(new Byte(JRGraphicElement.FILL_SOLID), FILL_SOLID);
			fillMap = Collections.unmodifiableMap(map);
		}

		return fillMap;
	}

	/**
	 *
	 */
	private static final String RESET_TYPE_NONE = "None";
	private static final String RESET_TYPE_REPORT = "Report";
	private static final String RESET_TYPE_PAGE = "Page";
	private static final String RESET_TYPE_COLUMN = "Column";
	private static final String RESET_TYPE_GROUP = "Group";

	private static Map resetTypeMap = null;

	public static Map getResetTypeMap()
	{
		if (resetTypeMap == null)
		{
			Map map = new HashMap(14);
			map.put(RESET_TYPE_NONE,   new Byte(JRVariable.RESET_TYPE_NONE));
			map.put(RESET_TYPE_REPORT, new Byte(JRVariable.RESET_TYPE_REPORT));
			map.put(RESET_TYPE_PAGE,   new Byte(JRVariable.RESET_TYPE_PAGE));
			map.put(RESET_TYPE_COLUMN, new Byte(JRVariable.RESET_TYPE_COLUMN));
			map.put(RESET_TYPE_GROUP,  new Byte(JRVariable.RESET_TYPE_GROUP));
			map.put(new Byte(JRVariable.RESET_TYPE_NONE),   RESET_TYPE_NONE);
			map.put(new Byte(JRVariable.RESET_TYPE_REPORT), RESET_TYPE_REPORT);
			map.put(new Byte(JRVariable.RESET_TYPE_PAGE),   RESET_TYPE_PAGE);
			map.put(new Byte(JRVariable.RESET_TYPE_COLUMN), RESET_TYPE_COLUMN);
			map.put(new Byte(JRVariable.RESET_TYPE_GROUP),  RESET_TYPE_GROUP);
			resetTypeMap = Collections.unmodifiableMap(map);
		}

		return resetTypeMap;
	}

	/**
	 *
	 */
	private static final String CALCULATION_NOTHING = "Nothing";
	private static final String CALCULATION_COUNT = "Count";
	private static final String CALCULATION_SUM = "Sum";
	private static final String CALCULATION_AVERAGE = "Average";
	private static final String CALCULATION_LOWEST = "Lowest";
	private static final String CALCULATION_HIGHEST = "Highest";
	private static final String CALCULATION_STANDARD_DEVIATION = "StandardDeviation";
	private static final String CALCULATION_VARIANCE = "Variance";
	private static final String CALCULATION_SYSTEM = "System";
	private static final String CALCULATION_FIRST = "First";
	private static final String CALCULATION_DISTINCT_COUNT = "DistinctCount";

	private static Map calculationMap = null;

	public static Map getCalculationMap()
	{
		if (calculationMap == null)
		{
			Map map = new HashMap(30);
			map.put(CALCULATION_NOTHING,            new Byte(JRVariable.CALCULATION_NOTHING));
			map.put(CALCULATION_COUNT,              new Byte(JRVariable.CALCULATION_COUNT));
			map.put(CALCULATION_SUM,                new Byte(JRVariable.CALCULATION_SUM));
			map.put(CALCULATION_AVERAGE,            new Byte(JRVariable.CALCULATION_AVERAGE));
			map.put(CALCULATION_LOWEST,             new Byte(JRVariable.CALCULATION_LOWEST));
			map.put(CALCULATION_HIGHEST,            new Byte(JRVariable.CALCULATION_HIGHEST));
			map.put(CALCULATION_STANDARD_DEVIATION, new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION));
			map.put(CALCULATION_VARIANCE,           new Byte(JRVariable.CALCULATION_VARIANCE));
			map.put(CALCULATION_SYSTEM,             new Byte(JRVariable.CALCULATION_SYSTEM));
			map.put(CALCULATION_FIRST,              new Byte(JRVariable.CALCULATION_FIRST));
			map.put(CALCULATION_DISTINCT_COUNT,     new Byte(JRVariable.CALCULATION_DISTINCT_COUNT));
			map.put(new Byte(JRVariable.CALCULATION_NOTHING),            CALCULATION_NOTHING);
			map.put(new Byte(JRVariable.CALCULATION_COUNT),              CALCULATION_COUNT);
			map.put(new Byte(JRVariable.CALCULATION_SUM),                CALCULATION_SUM);
			map.put(new Byte(JRVariable.CALCULATION_AVERAGE),            CALCULATION_AVERAGE);
			map.put(new Byte(JRVariable.CALCULATION_LOWEST),             CALCULATION_LOWEST);
			map.put(new Byte(JRVariable.CALCULATION_HIGHEST),            CALCULATION_HIGHEST);
			map.put(new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION), CALCULATION_STANDARD_DEVIATION);
			map.put(new Byte(JRVariable.CALCULATION_VARIANCE),           CALCULATION_VARIANCE);
			map.put(new Byte(JRVariable.CALCULATION_SYSTEM),             CALCULATION_SYSTEM);
			map.put(new Byte(JRVariable.CALCULATION_FIRST),              CALCULATION_FIRST);
			map.put(new Byte(JRVariable.CALCULATION_DISTINCT_COUNT),     CALCULATION_DISTINCT_COUNT);
			calculationMap = Collections.unmodifiableMap(map);
		}

		return calculationMap;
	}

	/**
	 *
	 */
	private static final String PRINT_ORDER_VERTICAL = "Vertical";
	private static final String PRINT_ORDER_HORIZONTAL = "Horizontal";

	private static Map printOrderMap = null;

	public static Map getPrintOrderMap()
	{
		if (printOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(PRINT_ORDER_VERTICAL,   new Byte(JRReport.PRINT_ORDER_VERTICAL));
			map.put(PRINT_ORDER_HORIZONTAL, new Byte(JRReport.PRINT_ORDER_HORIZONTAL));
			map.put(new Byte(JRReport.PRINT_ORDER_VERTICAL),   PRINT_ORDER_VERTICAL);
			map.put(new Byte(JRReport.PRINT_ORDER_HORIZONTAL), PRINT_ORDER_HORIZONTAL);
			printOrderMap = Collections.unmodifiableMap(map);
		}

		return printOrderMap;
	}

	/**
	 *
	 */
	private static final String ORIENTATION_PORTRAIT = "Portrait";
	private static final String ORIENTATION_LANDSCAPE = "Landscape";

	private static Map orientationMap = null;

	public static Map getOrientationMap()
	{
		if (orientationMap == null)
		{
			Map map = new HashMap(6);
			map.put(ORIENTATION_PORTRAIT,  new Byte(JRReport.ORIENTATION_PORTRAIT));
			map.put(ORIENTATION_LANDSCAPE, new Byte(JRReport.ORIENTATION_LANDSCAPE));
			map.put(new Byte(JRReport.ORIENTATION_PORTRAIT),  ORIENTATION_PORTRAIT);
			map.put(new Byte(JRReport.ORIENTATION_LANDSCAPE), ORIENTATION_LANDSCAPE);
			orientationMap = Collections.unmodifiableMap(map);
		}

		return orientationMap;
	}

	/**
	 *
	 */
	private static final String WHEN_NO_DATA_TYPE_NO_PAGES = "NoPages";
	private static final String WHEN_NO_DATA_TYPE_BLANK_PAGE = "BlankPage";
	private static final String WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL = "AllSectionsNoDetail";
	private static final String WHEN_NO_DATA_TYPE_NO_DATA_SECTION = "NoDataSection";

	private static Map whenNoDataTypeMap = null;

	public static Map getWhenNoDataTypeMap()
	{
		if (whenNoDataTypeMap == null)
		{
			Map map = new HashMap(11);
			map.put(WHEN_NO_DATA_TYPE_NO_PAGES,               new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES));
			map.put(WHEN_NO_DATA_TYPE_BLANK_PAGE,             new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE));
			map.put(WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL, new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL));
			map.put(WHEN_NO_DATA_TYPE_NO_DATA_SECTION,        new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION));
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES),               WHEN_NO_DATA_TYPE_NO_PAGES);
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE),             WHEN_NO_DATA_TYPE_BLANK_PAGE);
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL), WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION),        WHEN_NO_DATA_TYPE_NO_DATA_SECTION);
			whenNoDataTypeMap = Collections.unmodifiableMap(map);
		}

		return whenNoDataTypeMap;
	}

	/**
	 *
	 */
	private static final String EVALUATION_TIME_NOW = "Now";
	private static final String EVALUATION_TIME_REPORT = "Report";
	private static final String EVALUATION_TIME_PAGE = "Page";
	private static final String EVALUATION_TIME_COLUMN = "Column";
	private static final String EVALUATION_TIME_GROUP = "Group";
	private static final String EVALUATION_TIME_BAND = "Band";
	private static final String EVALUATION_TIME_AUTO = "Auto";

	private static Map evaluationTimeMap = null;

	public static Map getEvaluationTimeMap()
	{
		if (evaluationTimeMap == null)
		{
			Map map = new HashMap(19);
			map.put(EVALUATION_TIME_NOW,    new Byte(JRExpression.EVALUATION_TIME_NOW));
			map.put(EVALUATION_TIME_REPORT, new Byte(JRExpression.EVALUATION_TIME_REPORT));
			map.put(EVALUATION_TIME_PAGE,   new Byte(JRExpression.EVALUATION_TIME_PAGE));
			map.put(EVALUATION_TIME_COLUMN, new Byte(JRExpression.EVALUATION_TIME_COLUMN));
			map.put(EVALUATION_TIME_GROUP,  new Byte(JRExpression.EVALUATION_TIME_GROUP));
			map.put(EVALUATION_TIME_BAND,  new Byte(JRExpression.EVALUATION_TIME_BAND));
			map.put(EVALUATION_TIME_AUTO,  new Byte(JRExpression.EVALUATION_TIME_AUTO));
			map.put(new Byte(JRExpression.EVALUATION_TIME_NOW),    EVALUATION_TIME_NOW);
			map.put(new Byte(JRExpression.EVALUATION_TIME_REPORT), EVALUATION_TIME_REPORT);
			map.put(new Byte(JRExpression.EVALUATION_TIME_PAGE),   EVALUATION_TIME_PAGE);
			map.put(new Byte(JRExpression.EVALUATION_TIME_COLUMN), EVALUATION_TIME_COLUMN);
			map.put(new Byte(JRExpression.EVALUATION_TIME_GROUP),  EVALUATION_TIME_GROUP);
			map.put(new Byte(JRExpression.EVALUATION_TIME_BAND),  EVALUATION_TIME_BAND);
			map.put(new Byte(JRExpression.EVALUATION_TIME_AUTO),  EVALUATION_TIME_AUTO);
			evaluationTimeMap = Collections.unmodifiableMap(map);
		}

		return evaluationTimeMap;
	}

	/**
	 *
	 */
	private static final String HYPERLINK_TYPE_NONE = "None";
	private static final String HYPERLINK_TYPE_REFERENCE = "Reference";
	private static final String HYPERLINK_TYPE_LOCAL_ANCHOR = "LocalAnchor";
	private static final String HYPERLINK_TYPE_LOCAL_PAGE = "LocalPage";
	private static final String HYPERLINK_TYPE_REMOTE_ANCHOR = "RemoteAnchor";
	private static final String HYPERLINK_TYPE_REMOTE_PAGE = "RemotePage";

	private static Map hyperlinkTypeMap = null;


	/**
	 * @deprecated Replaced by {@link JRHyperlinkHelper#getHyperlinkType(String)}.
	 */
	public static Map getHyperlinkTypeMap()
	{
		if (hyperlinkTypeMap == null)
		{
			Map map = new HashMap(16);
			map.put(HYPERLINK_TYPE_NONE,          new Byte(JRHyperlink.HYPERLINK_TYPE_NONE));
			map.put(HYPERLINK_TYPE_REFERENCE,     new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE));
			map.put(HYPERLINK_TYPE_LOCAL_ANCHOR,  new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR));
			map.put(HYPERLINK_TYPE_LOCAL_PAGE,    new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE));
			map.put(HYPERLINK_TYPE_REMOTE_ANCHOR, new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR));
			map.put(HYPERLINK_TYPE_REMOTE_PAGE,   new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE));
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_NONE),          HYPERLINK_TYPE_NONE);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE),     HYPERLINK_TYPE_REFERENCE);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR),  HYPERLINK_TYPE_LOCAL_ANCHOR);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE),    HYPERLINK_TYPE_LOCAL_PAGE);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR), HYPERLINK_TYPE_REMOTE_ANCHOR);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE),   HYPERLINK_TYPE_REMOTE_PAGE);
			hyperlinkTypeMap = Collections.unmodifiableMap(map);
		}

		return hyperlinkTypeMap;
	}

	/**
	 *
	 */
	private static final String HYPERLINK_TARGET_SELF = "Self";
	private static final String HYPERLINK_TARGET_BLANK = "Blank";
	private static final String HYPERLINK_TARGET_PARENT = "Parent";
	private static final String HYPERLINK_TARGET_TOP = "Top";

	private static Map hyperlinkTargetMap = null;

	/**
	 * @deprecated Replaced by {@link JRHyperlinkHelper#getHyperlinkTarget(String)}.
	 */
	public static Map getHyperlinkTargetMap()
	{
		if (hyperlinkTargetMap == null)
		{
			Map map = new HashMap(11);
			map.put(HYPERLINK_TARGET_SELF,  new Byte(JRHyperlink.HYPERLINK_TARGET_SELF));
			map.put(HYPERLINK_TARGET_BLANK, new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK));
			map.put(HYPERLINK_TARGET_PARENT, new Byte(JRHyperlink.HYPERLINK_TARGET_PARENT));
			map.put(HYPERLINK_TARGET_TOP, new Byte(JRHyperlink.HYPERLINK_TARGET_TOP));
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_SELF),  HYPERLINK_TARGET_SELF);
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK), HYPERLINK_TARGET_BLANK);
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_PARENT), HYPERLINK_TARGET_PARENT);
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_TOP), HYPERLINK_TARGET_TOP);
			hyperlinkTargetMap = Collections.unmodifiableMap(map);
		}

		return hyperlinkTargetMap;
	}


	/**
	 *
	 */
	private static final String EDGE_TOP = "Top";
	private static final String EDGE_BOTTOM = "Bottom";
	private static final String EDGE_LEFT = "Left";
	private static final String EDGE_RIGHT = "Right";

	private static Map chartEdgeMap = null;

	public static Map getChartEdgeMap()
	{
		if (chartEdgeMap == null)
		{
			Map map = new HashMap(11);
			map.put(EDGE_TOP,    new Byte(JRChart.EDGE_TOP));
			map.put(EDGE_BOTTOM, new Byte(JRChart.EDGE_BOTTOM));
			map.put(EDGE_LEFT,   new Byte(JRChart.EDGE_LEFT));
			map.put(EDGE_RIGHT,  new Byte(JRChart.EDGE_RIGHT));
			map.put(new Byte(JRChart.EDGE_TOP),    EDGE_TOP);
			map.put(new Byte(JRChart.EDGE_BOTTOM), EDGE_BOTTOM);
			map.put(new Byte(JRChart.EDGE_LEFT),   EDGE_LEFT);
			map.put(new Byte(JRChart.EDGE_RIGHT),  EDGE_RIGHT);
			chartEdgeMap = Collections.unmodifiableMap(map);
		}

		return chartEdgeMap;
	}

	/**
	 * @deprecated Replaced by {@link #getChartEdgeMap()}.
	 */
	public static Map getChartTitlePositionMap()
	{
		return getChartEdgeMap();
	}
	
	/**
	 *
	 */
	private static final String ORIENTATION_HORIZONTAL = "Horizontal";
	private static final String ORIENTATION_VERTICAL = "Vertical";

	private static Map plotOrientationMap = null;

	public static Map getPlotOrientationMap()
	{
		if (plotOrientationMap == null)
		{
			Map map = new HashMap(6);
			map.put(ORIENTATION_HORIZONTAL, PlotOrientation.HORIZONTAL);
			map.put(ORIENTATION_VERTICAL,   PlotOrientation.VERTICAL);
			map.put(PlotOrientation.HORIZONTAL, ORIENTATION_HORIZONTAL);
			map.put(PlotOrientation.VERTICAL,   ORIENTATION_VERTICAL);
			plotOrientationMap = Collections.unmodifiableMap(map);
		}

		return plotOrientationMap;
	}

	/**
	 *
	 */
	private static final String SORT_ORDER_ASCENDING = "Ascending";
	private static final String SORT_ORDER_DESCENDING = "Descending";

	private static Map sortOrderMap = null;

	public static Map getSortOrderMap()
	{
		if (sortOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(SORT_ORDER_ASCENDING,  new Byte(JRSortField.SORT_ORDER_ASCENDING));
			map.put(SORT_ORDER_DESCENDING, new Byte(JRSortField.SORT_ORDER_DESCENDING));
			map.put(new Byte(JRSortField.SORT_ORDER_ASCENDING),  SORT_ORDER_ASCENDING);
			map.put(new Byte(JRSortField.SORT_ORDER_DESCENDING), SORT_ORDER_DESCENDING);
			sortOrderMap = Collections.unmodifiableMap(map);
		}

		return sortOrderMap;
	}


	private static final String SCALE_ON_BOTH_AXES = "BothAxes";
	private static final String SCALE_ON_DOMAIN_AXIS = "DomainAxis";
	private static final String SCALE_ON_RANGE_AXIS = "RangeAxis";

	private static Map scaleTypeMap = null;

	public static Map  getScaleTypeMap(){
		if( scaleTypeMap == null ){
			Map map = new HashMap( 8 );
			map.put( SCALE_ON_BOTH_AXES, new Integer( XYBubbleRenderer.SCALE_ON_BOTH_AXES ));
			map.put( SCALE_ON_DOMAIN_AXIS, new Integer( XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS ));
			map.put( SCALE_ON_RANGE_AXIS, new Integer( XYBubbleRenderer.SCALE_ON_RANGE_AXIS ));
			map.put( new Integer( XYBubbleRenderer.SCALE_ON_BOTH_AXES ), SCALE_ON_BOTH_AXES );
			map.put( new Integer( XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS ), SCALE_ON_DOMAIN_AXIS );
			map.put( new Integer( XYBubbleRenderer.SCALE_ON_RANGE_AXIS ), SCALE_ON_RANGE_AXIS );
			scaleTypeMap = Collections.unmodifiableMap(map);
		}

		return scaleTypeMap;
	}



	private static final String TIME_PERIOD_YEAR = "Year";
	private static final String TIME_PERIOD_QUARTER = "Quarter";
	private static final String TIME_PERIOD_MONTH = "Month";
	private static final String TIME_PERIOD_WEEK = "Week";
	private static final String TIME_PERIOD_DAY = "Day";
	private static final String TIME_PERIOD_HOUR = "Hour";
	private static final String TIME_PERIOD_MINUTE = "Minute";
	private static final String TIME_PERIOD_SECOND = "Second";
	private static final String TIME_PERIOD_MILISECOND = "Milisecond";



	public static Class getTimePeriod( String timePeriod ) {
		if( timePeriod.equals( TIME_PERIOD_YEAR ) ){
			return Year.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_QUARTER )){
			return Quarter.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_MONTH )){
			return Month.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_WEEK )){
			return Week.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_DAY )) {
			return Day.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_HOUR )){
			return Hour.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_MINUTE )){
			return Minute.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_SECOND )){
			return Second.class;
		}
		else {
			return Millisecond.class;
		}

	}


	public static String getTimePeriodName( Class clazz  ){
		if( clazz.equals( Year.class )){
			return TIME_PERIOD_YEAR;
		}
		else if ( clazz.equals( Quarter.class )){
			return TIME_PERIOD_QUARTER;
		}
		else if( clazz.equals( Month.class )){
			return TIME_PERIOD_MONTH;
		}
		else if( clazz.equals( Week.class )){
			return TIME_PERIOD_WEEK;
		}
		else if( clazz.equals( Day.class )){
			return TIME_PERIOD_DAY;
		}
		else if( clazz.equals( Hour.class )){
			return TIME_PERIOD_HOUR;
		}
		else if( clazz.equals( Minute.class )){
			return TIME_PERIOD_MINUTE;
		}
		else if( clazz.equals( Second.class )){
			return TIME_PERIOD_SECOND;
		}
		else {
			return TIME_PERIOD_MILISECOND;
		}
	}



	/**
	 *
	 */
	private static final String WHEN_RESOURCE_MISSING_TYPE_NULL = "Null";
	private static final String WHEN_RESOURCE_MISSING_TYPE_EMPTY = "Empty";
	private static final String WHEN_RESOURCE_MISSING_TYPE_KEY = "Key";
	private static final String WHEN_RESOURCE_MISSING_TYPE_ERROR = "Error";

	private static Map whenResourceMissingTypeMap = null;

	public static Map getWhenResourceMissingTypeMap()
	{
		if (whenResourceMissingTypeMap == null)
		{
			Map map = new HashMap(11);
			map.put(WHEN_RESOURCE_MISSING_TYPE_NULL, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL));
			map.put(WHEN_RESOURCE_MISSING_TYPE_EMPTY, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY));
			map.put(WHEN_RESOURCE_MISSING_TYPE_KEY, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY));
			map.put(WHEN_RESOURCE_MISSING_TYPE_ERROR, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR));
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL), WHEN_RESOURCE_MISSING_TYPE_NULL);
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY), WHEN_RESOURCE_MISSING_TYPE_EMPTY);
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY), WHEN_RESOURCE_MISSING_TYPE_KEY);
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR), WHEN_RESOURCE_MISSING_TYPE_ERROR);
			whenResourceMissingTypeMap = Collections.unmodifiableMap(map);
		}

		return whenResourceMissingTypeMap;
	}


	/**
	 *
	 */
	private static final String METER_SHAPE_CHORD = "chord";
	private static final String METER_SHAPE_CIRCLE = "circle";
	private static final String METER_SHAPE_PIE = "pie";
	private static final String METER_SHAPE_DIAL = "dial";

	private static Map meterShapeMap = null;

	public static Map getMeterShapeMap()
	{
		if (meterShapeMap == null)
		{
			Map map = new HashMap(11);
			map.put(METER_SHAPE_CHORD, new Byte(JRMeterPlot.SHAPE_CHORD));
			map.put(METER_SHAPE_CIRCLE, new Byte(JRMeterPlot.SHAPE_CIRCLE));
			map.put(METER_SHAPE_PIE, new Byte(JRMeterPlot.SHAPE_PIE));
			map.put(METER_SHAPE_DIAL, new Byte(JRMeterPlot.SHAPE_DIAL));
			map.put(new Byte(JRMeterPlot.SHAPE_CHORD), METER_SHAPE_CHORD);
			map.put(new Byte(JRMeterPlot.SHAPE_CIRCLE), METER_SHAPE_CIRCLE);
			map.put(new Byte(JRMeterPlot.SHAPE_PIE), METER_SHAPE_PIE);
			map.put(new Byte(JRMeterPlot.SHAPE_DIAL), METER_SHAPE_DIAL);
			meterShapeMap = Collections.unmodifiableMap(map);
		}

		return meterShapeMap;
	}


	/**
	 *
	 */
	private static final String THERMOMETER_VALUE_LOCATION_NONE = "none";
	private static final String THERMOMETER_VALUE_LOCATION_LEFT = "left";
	private static final String THERMOMETER_VALUE_LOCATION_RIGHT = "right";
	private static final String THERMOMETER_VALUE_LOCATION_BULB = "bulb";

	private static Map thermometerValueLocationMap = null;

	public static Map getThermometerValueLocationMap()
	{
		if (thermometerValueLocationMap == null)
		{
			Map map = new HashMap(11);
			map.put(THERMOMETER_VALUE_LOCATION_NONE, new Byte(JRThermometerPlot.LOCATION_NONE));
			map.put(THERMOMETER_VALUE_LOCATION_LEFT, new Byte(JRThermometerPlot.LOCATION_LEFT));
			map.put(THERMOMETER_VALUE_LOCATION_RIGHT, new Byte(JRThermometerPlot.LOCATION_RIGHT));
			map.put(THERMOMETER_VALUE_LOCATION_BULB, new Byte(JRThermometerPlot.LOCATION_BULB));
			map.put(new Byte(JRThermometerPlot.LOCATION_NONE), THERMOMETER_VALUE_LOCATION_NONE);
			map.put(new Byte(JRThermometerPlot.LOCATION_LEFT), THERMOMETER_VALUE_LOCATION_LEFT);
			map.put(new Byte(JRThermometerPlot.LOCATION_RIGHT), THERMOMETER_VALUE_LOCATION_RIGHT);
			map.put(new Byte(JRThermometerPlot.LOCATION_BULB), THERMOMETER_VALUE_LOCATION_BULB);
			thermometerValueLocationMap = Collections.unmodifiableMap(map);
		}

		return thermometerValueLocationMap;
	}


	/**
	 *
	 */
	private static final String AXIS_POSITION_LEFT_OR_TOP = "leftOrTop";
	private static final String AXIS_POSITION_RIGHT_OR_BOTTOM = "rightOrBottom";

	private static Map axisPositionMap = null;

	public static Map getAxisPositionMap()
	{
		 if (axisPositionMap == null)
		 {
			Map map = new HashMap(6);
			map.put(AXIS_POSITION_LEFT_OR_TOP, new Byte(JRChartAxis.POSITION_LEFT_OR_TOP));
			map.put(AXIS_POSITION_RIGHT_OR_BOTTOM, new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM));
			map.put(new Byte(JRChartAxis.POSITION_LEFT_OR_TOP), AXIS_POSITION_LEFT_OR_TOP);
			map.put(new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM), AXIS_POSITION_RIGHT_OR_BOTTOM);
			axisPositionMap = Collections.unmodifiableMap(map);
		 }

		 return axisPositionMap;
	}


	/**
	 *
	 */
	private static final String CROSSTAB_BUCKET_ORDER_ASCENDING = "Ascending";
	private static final String CROSSTAB_BUCKET_ORDER_DESCENDING = "Descending";

	private static Map crosstabBucketOrderMap = null;

	public static Map getCrosstabBucketOrderMap()
	{
		if (crosstabBucketOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(CROSSTAB_BUCKET_ORDER_ASCENDING, new Byte(BucketDefinition.ORDER_ASCENDING));
			map.put(CROSSTAB_BUCKET_ORDER_DESCENDING, new Byte(BucketDefinition.ORDER_DESCENDING));
			map.put(new Byte(BucketDefinition.ORDER_ASCENDING), CROSSTAB_BUCKET_ORDER_ASCENDING);
			map.put(new Byte(BucketDefinition.ORDER_DESCENDING), CROSSTAB_BUCKET_ORDER_DESCENDING);
			crosstabBucketOrderMap = Collections.unmodifiableMap(map);
		}

		return crosstabBucketOrderMap;
	}


	private static final String CROSSTAB_PERCENTAGE_NONE = "None";
	private static final String CROSSTAB_PERCENTAGE_GRAND_TOTAL = "GrandTotal";

	private static Map crosstabPercentageMap = null;


	public static Map getCrosstabPercentageMap()
	{
		if (crosstabPercentageMap == null)
		{
			Map map = new HashMap(6);
			map.put(CROSSTAB_PERCENTAGE_NONE, new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_NONE));
			map.put(CROSSTAB_PERCENTAGE_GRAND_TOTAL, new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL));
			map.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_NONE), CROSSTAB_PERCENTAGE_NONE);
			map.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL), CROSSTAB_PERCENTAGE_GRAND_TOTAL);
			crosstabPercentageMap = Collections.unmodifiableMap(map);
		}

		return crosstabPercentageMap;
	}


	private static final String CROSSTAB_TOTAL_POSITION_NONE = "None";
	private static final String CROSSTAB_TOTAL_POSITION_START = "Start";
	private static final String CROSSTAB_TOTAL_POSITION_END = "End";

	private static Map crosstabTotalPositionMap = null;


	public static Map getCrosstabTotalPositionMap()
	{
		if (crosstabTotalPositionMap == null)
		{
			Map map = new HashMap(8);
			map.put(CROSSTAB_TOTAL_POSITION_NONE, new Byte(BucketDefinition.TOTAL_POSITION_NONE));
			map.put(CROSSTAB_TOTAL_POSITION_START, new Byte(BucketDefinition.TOTAL_POSITION_START));
			map.put(CROSSTAB_TOTAL_POSITION_END, new Byte(BucketDefinition.TOTAL_POSITION_END));
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_NONE), CROSSTAB_TOTAL_POSITION_NONE);
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_START), CROSSTAB_TOTAL_POSITION_START);
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_END), CROSSTAB_TOTAL_POSITION_END);
			crosstabTotalPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabTotalPositionMap;
	}


	private static final String CROSSTAB_ROW_POSITION_TOP = "Top";
	private static final String CROSSTAB_ROW_POSITION_MIDDLE = "Middle";
	private static final String CROSSTAB_ROW_POSITION_BOTTOM = "Bottom";
	private static final String CROSSTAB_ROW_POSITION_STRETCH = "Stretch";

	private static Map crosstabRowPositionMap = null;


	public static Map getCrosstabRowPositionMap()
	{
		if (crosstabRowPositionMap == null)
		{
			Map map = new HashMap(11);
			map.put(CROSSTAB_ROW_POSITION_TOP, new Byte(JRCellContents.POSITION_Y_TOP));
			map.put(CROSSTAB_ROW_POSITION_MIDDLE, new Byte(JRCellContents.POSITION_Y_MIDDLE));
			map.put(CROSSTAB_ROW_POSITION_BOTTOM, new Byte(JRCellContents.POSITION_Y_BOTTOM));
			map.put(CROSSTAB_ROW_POSITION_STRETCH, new Byte(JRCellContents.POSITION_Y_STRETCH));
			map.put(new Byte(JRCellContents.POSITION_Y_TOP), CROSSTAB_ROW_POSITION_TOP);
			map.put(new Byte(JRCellContents.POSITION_Y_MIDDLE), CROSSTAB_ROW_POSITION_MIDDLE);
			map.put(new Byte(JRCellContents.POSITION_Y_BOTTOM), CROSSTAB_ROW_POSITION_BOTTOM);
			map.put(new Byte(JRCellContents.POSITION_Y_STRETCH), CROSSTAB_ROW_POSITION_STRETCH);
			crosstabRowPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabRowPositionMap;
	}


	private static final String CROSSTAB_COLUMN_POSITION_LEFT = "Left";
	private static final String CROSSTAB_COLUMN_POSITION_CENTER = "Center";
	private static final String CROSSTAB_COLUMN_POSITION_RIGHT = "Right";
	private static final String CROSSTAB_COLUMN_POSITION_STRETCH = "Stretch";

	private static Map crosstabColumnPositionMap = null;


	public static Map getCrosstabColumnPositionMap()
	{
		if (crosstabColumnPositionMap == null)
		{
			Map map = new HashMap(11);
			map.put(CROSSTAB_COLUMN_POSITION_LEFT, new Byte(JRCellContents.POSITION_X_LEFT));
			map.put(CROSSTAB_COLUMN_POSITION_CENTER, new Byte(JRCellContents.POSITION_X_CENTER));
			map.put(CROSSTAB_COLUMN_POSITION_RIGHT, new Byte(JRCellContents.POSITION_X_RIGHT));
			map.put(CROSSTAB_COLUMN_POSITION_STRETCH, new Byte(JRCellContents.POSITION_X_STRETCH));
			map.put(new Byte(JRCellContents.POSITION_X_LEFT), CROSSTAB_COLUMN_POSITION_LEFT);
			map.put(new Byte(JRCellContents.POSITION_X_CENTER), CROSSTAB_COLUMN_POSITION_CENTER);
			map.put(new Byte(JRCellContents.POSITION_X_RIGHT), CROSSTAB_COLUMN_POSITION_RIGHT);
			map.put(new Byte(JRCellContents.POSITION_X_STRETCH), CROSSTAB_COLUMN_POSITION_STRETCH);
			crosstabColumnPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabColumnPositionMap;
	}

	
	/**
	 *
	 */
	private static final String SPLIT_TYPE_STRETCH = "Stretch";
	private static final String SPLIT_TYPE_PREVENT = "Prevent";
	private static final String SPLIT_TYPE_IMMEDIATE = "Immediate";

	
	private static Map splitTypeMap = null;

	public static Map getSplitTypeMap()
	{
		if (splitTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(SPLIT_TYPE_STRETCH,   JRBand.SPLIT_TYPE_STRETCH);
			map.put(SPLIT_TYPE_PREVENT,   JRBand.SPLIT_TYPE_PREVENT);
			map.put(SPLIT_TYPE_IMMEDIATE, JRBand.SPLIT_TYPE_IMMEDIATE);
			map.put(JRBand.SPLIT_TYPE_STRETCH,   SPLIT_TYPE_STRETCH);
			map.put(JRBand.SPLIT_TYPE_PREVENT,   SPLIT_TYPE_PREVENT);
			map.put(JRBand.SPLIT_TYPE_IMMEDIATE, SPLIT_TYPE_IMMEDIATE);
			splitTypeMap = Collections.unmodifiableMap(map);
		}

		return splitTypeMap;
	}

	
	/**
	 *
	 */
	private static final String UNKNOWN = "unknown";
	private static final String BACKGROUND = "background";
	private static final String TITLE = "title";
	private static final String PAGE_HEADER = "pageHeader";
	private static final String COLUMN_HEADER = "columnHeader";
	private static final String GROUP_HEADER = "groupHeader";
	private static final String DETAIL = "detail";
	private static final String GROUP_FOOTER = "groupFooter";
	private static final String COLUMN_FOOTER = "columnFooter";
	private static final String PAGE_FOOTER = "pageFooter";
	private static final String LAST_PAGE_FOOTER = "lastPageFooter";
	private static final String SUMMARY = "summary";
	private static final String NO_DATA = "noData";

	
	private static Map bandTypeMap = null;

	public static Map getBandTypeMap()
	{
		if (bandTypeMap == null)
		{
			Map map = new HashMap(35);
			map.put(UNKNOWN,          new Byte(JROrigin.UNKNOWN));
			map.put(BACKGROUND,       new Byte(JROrigin.BACKGROUND));
			map.put(TITLE,            new Byte(JROrigin.TITLE));
			map.put(PAGE_HEADER,      new Byte(JROrigin.PAGE_HEADER));
			map.put(COLUMN_HEADER,    new Byte(JROrigin.COLUMN_HEADER));
			map.put(GROUP_HEADER,     new Byte(JROrigin.GROUP_HEADER));
			map.put(DETAIL,           new Byte(JROrigin.DETAIL));
			map.put(GROUP_FOOTER,     new Byte(JROrigin.GROUP_FOOTER));
			map.put(COLUMN_FOOTER,    new Byte(JROrigin.COLUMN_FOOTER));
			map.put(PAGE_FOOTER,      new Byte(JROrigin.PAGE_FOOTER));
			map.put(LAST_PAGE_FOOTER, new Byte(JROrigin.LAST_PAGE_FOOTER));
			map.put(SUMMARY,          new Byte(JROrigin.SUMMARY));
			map.put(NO_DATA,          new Byte(JROrigin.NO_DATA));
			map.put(new Byte(JROrigin.UNKNOWN),          UNKNOWN);
			map.put(new Byte(JROrigin.BACKGROUND),       BACKGROUND);
			map.put(new Byte(JROrigin.TITLE),            TITLE);
			map.put(new Byte(JROrigin.PAGE_HEADER),      PAGE_HEADER);
			map.put(new Byte(JROrigin.COLUMN_HEADER),    COLUMN_HEADER);
			map.put(new Byte(JROrigin.GROUP_HEADER),     GROUP_HEADER);
			map.put(new Byte(JROrigin.DETAIL),           DETAIL);
			map.put(new Byte(JROrigin.GROUP_FOOTER),     GROUP_FOOTER);
			map.put(new Byte(JROrigin.COLUMN_FOOTER),    COLUMN_FOOTER);
			map.put(new Byte(JROrigin.PAGE_FOOTER),      PAGE_FOOTER);
			map.put(new Byte(JROrigin.LAST_PAGE_FOOTER), LAST_PAGE_FOOTER);
			map.put(new Byte(JROrigin.SUMMARY),          SUMMARY);
			map.put(new Byte(JROrigin.NO_DATA),          NO_DATA);
			bandTypeMap = Collections.unmodifiableMap(map);
		}

		return bandTypeMap;
	}

	
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#NORMAL#getName()}.
	 */
	private static final String FOOTER_POSITION_NORMAL = "Normal";
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#STACK_AT_BOTTOM#getName()}.
	 */
	private static final String FOOTER_POSITION_STACK_AT_BOTTOM = "StackAtBottom";
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#FORCE_AT_BOTTOM#getName()}.
	 */
	private static final String FOOTER_POSITION_FORCE_AT_BOTTOM = "ForceAtBottom";
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#COLLATE_AT_BOTTOM#getName()}.
	 */
	private static final String FOOTER_POSITION_COLLATE_AT_BOTTOM = "CollateAtBottom";

	/**
	 * @deprecated Replaced by {@link FooterPositionEnum}.
	 */
	private static Map footerPositionMap = null;

	/**
	 * @deprecated Replaced by {@link FooterPositionEnum}.
	 */
	public static Map getFooterPositionMap()
	{
		if (footerPositionMap == null)
		{
			Map map = new HashMap(11);
			map.put(FOOTER_POSITION_NORMAL, new Byte(JRGroup.FOOTER_POSITION_NORMAL));
			map.put(FOOTER_POSITION_STACK_AT_BOTTOM, new Byte(JRGroup.FOOTER_POSITION_STACK_AT_BOTTOM));
			map.put(FOOTER_POSITION_FORCE_AT_BOTTOM, new Byte(JRGroup.FOOTER_POSITION_FORCE_AT_BOTTOM));
			map.put(FOOTER_POSITION_COLLATE_AT_BOTTOM, new Byte(JRGroup.FOOTER_POSITION_COLLATE_AT_BOTTOM));
			map.put(new Byte(JRGroup.FOOTER_POSITION_NORMAL), FOOTER_POSITION_NORMAL);
			map.put(new Byte(JRGroup.FOOTER_POSITION_STACK_AT_BOTTOM), FOOTER_POSITION_STACK_AT_BOTTOM);
			map.put(new Byte(JRGroup.FOOTER_POSITION_FORCE_AT_BOTTOM), FOOTER_POSITION_FORCE_AT_BOTTOM);
			map.put(new Byte(JRGroup.FOOTER_POSITION_COLLATE_AT_BOTTOM), FOOTER_POSITION_COLLATE_AT_BOTTOM);
			footerPositionMap = Collections.unmodifiableMap(map);
		}

		return footerPositionMap;
	}

	
	/**
	 * @deprecated Replaced by {@link JRColorUtil#getColor(String, Color)}.
	 */
	public static Color getColor(String strColor, Color defaultColor)
	{
		Color color = null;

		if (strColor != null && strColor.length() > 0)
		{
			char firstChar = strColor.charAt(0);
			if (firstChar == '#')
			{
				color = new Color(Integer.parseInt(strColor.substring(1), 16));
			}
			else if ('0' <= firstChar && firstChar <= '9')
			{
				color = new Color(Integer.parseInt(strColor));
			}
			else
			{
				if (JRXmlConstants.getColorMap().containsKey(strColor))
				{
					color = (Color)JRXmlConstants.getColorMap().get(strColor);
				}
				else
				{
					color = defaultColor;
				}
			}
		}

		return color;
	}

}
