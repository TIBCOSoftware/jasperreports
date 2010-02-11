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
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
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
public class JRXmlConstants
{


	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#OPAQUE#getName()}.
	 */
	private static final String POSITION_TYPE_FLOAT = PositionTypeEnum.POSITION_TYPE_FLOAT.getName();
	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#POSITION_TYPE_FIX_RELATIVE_TO_TOP#getName()}.
	 */
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_TOP = PositionTypeEnum.POSITION_TYPE_FIX_RELATIVE_TO_TOP.getName();
	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM#getName()}.
	 */
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM = PositionTypeEnum.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM.getName();

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
	private static final String MODE_OPAQUE = "Opaque";
	/**
	 * @deprecated Replaced by {@link ModeEnum#TRANSPARENT#getName()}.
	 */
	private static final String MODE_TRANSPARENT = "Transparent";

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
	 *
	 */
	private static final String HORIZONTAL_ALIGN_LEFT = "Left";
	private static final String HORIZONTAL_ALIGN_CENTER = "Center";
	private static final String HORIZONTAL_ALIGN_RIGHT = "Right";
	private static final String HORIZONTAL_ALIGN_JUSTIFIED = "Justified";

	private static Map horizontalAlignMap = null;

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
	 *
	 */
	public static Map getTextAlignMap()
	{
		return getHorizontalAlignMap();
	}

	/**
	 *
	 */
	private static final String VERTICAL_ALIGN_TOP = "Top";
	private static final String VERTICAL_ALIGN_MIDDLE = "Middle";
	private static final String VERTICAL_ALIGN_BOTTOM = "Bottom";

	private static Map verticalAlignMap = null;

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
	 *
	 */
	private static final String ROTATION_NONE = "None";
	private static final String ROTATION_LEFT = "Left";
	private static final String ROTATION_RIGHT = "Right";
	private static final String ROTATION_UPSIDE_DOWN = "UpsideDown";

	private static Map rotationMap = null;

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
	 *
	 */
	private static final String BREAK_TYPE_PAGE = "Page";
	private static final String BREAK_TYPE_COLUMN = "Column";

	private static Map breakTypeMap = null;

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
	 *
	 */
	private static final String RUN_DIRECTION_LTR = "LTR";
	private static final String RUN_DIRECTION_RTL = "RTL";

	private static Map runDirectionMap = null;

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
	 *
	 */
	private static final String LINE_SPACING_SINGLE = "Single";
	private static final String LINE_SPACING_1_1_2 = "1_1_2";
	private static final String LINE_SPACING_DOUBLE = "Double";

	private static Map lineSpacingMap = null;

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
	 *
	 */
	private static final String DIRECTION_TOP_DOWN = "TopDown";
	private static final String DIRECTION_BOTTOM_UP = "BottomUp";

	private static Map directionMap = null;

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
	 *
	 */
	private static final String SCALE_IMAGE_CLIP = "Clip";
	private static final String SCALE_IMAGE_FILL_FRAME = "FillFrame";
	private static final String SCALE_IMAGE_RETAIN_SHAPE = "RetainShape";
	private static final String SCALE_IMAGE_REAL_HEIGT = "RealHeight";
	private static final String SCALE_IMAGE_REAL_SIZE = "RealSize";

	private static Map scaleImageMap = null;

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
	 *
	 */
	private static final String ON_ERROR_TYPE_ERROR = "Error";
	private static final String ON_ERROR_TYPE_BLANK = "Blank";
	private static final String ON_ERROR_TYPE_ICON = "Icon";

	private static Map onErrorTypeMap = null;

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
	 *
	 */
	private static final String STRETCH_TYPE_NO_STRETCH = "NoStretch";
	private static final String STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT = "RelativeToTallestObject";
	private static final String STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT = "RelativeToBandHeight";

	private static Map stretchTypeMap = null;

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
	 *
	 */
	private static final String FOOTER_POSITION_NORMAL = "Normal";
	private static final String FOOTER_POSITION_STACK_AT_BOTTOM = "StackAtBottom";
	private static final String FOOTER_POSITION_FORCE_AT_BOTTOM = "ForceAtBottom";
	private static final String FOOTER_POSITION_COLLATE_AT_BOTTOM = "CollateAtBottom";

	private static Map footerPositionMap = null;

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

	/**
	 *
	 */
	public static final String JASPERREPORT_PUBLIC_ID = "-//JasperReports//DTD JasperReport//EN";//FIXME align with samples
	public static final String JASPERREPORT_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jasperreport.dtd";
	public static final String JASPERREPORT_DTD = "net/sf/jasperreports/engine/dtds/jasperreport.dtd";
	public static final String JASPERPRINT_PUBLIC_ID = "-//JasperReports//DTD JasperPrint//EN";
	public static final String JASPERPRINT_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jasperprint.dtd";
	public static final String JASPERPRINT_DTD = "net/sf/jasperreports/engine/dtds/jasperprint.dtd";
	
	/**
	 * The namespace used by the JRXML XML schema.
	 */
	public static final String JASPERREPORTS_NAMESPACE = 
		"http://jasperreports.sourceforge.net/jasperreports";
	
	/**
	 * The system location of the JRXML XML schema.
	 */
	public static final String JASPERREPORT_XSD_SYSTEM_ID = 
		"http://jasperreports.sourceforge.net/xsd/jasperreport.xsd";
	
	/**
	 * The internal location/resource name of the JRXML XML schema.
	 */
	public static final String JASPERREPORT_XSD_RESOURCE = 
		"net/sf/jasperreports/engine/dtds/jasperreport.xsd";
	
	/**
	 * The internal location/resource name of the JRXML DTD compatibility XML schema.
	 */
	public static final String JASPERREPORT_XSD_DTD_COMPAT_RESOURCE = 
		"net/sf/jasperreports/engine/dtds/jasperreport-dtd-compat.xsd";

	/**
	 * Template XML public ID.
	 */
	public static final String JASPERTEMPLATE_PUBLIC_ID = "-//JasperReports//DTD Template//EN";

	/**
	 * Template XML system ID.
	 */
	public static final String JASPERTEMPLATE_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jaspertemplate.dtd";

	/**
	 * DTD location for template XMLs.
	 */
	public static final String JASPERTEMPLATE_DTD = "net/sf/jasperreports/engine/dtds/jaspertemplate.dtd";

	/**
	 *	JasperDesignFactory associated constants
	 */
	public static final String ELEMENT_jasperReport = "jasperReport";
	public static final String ATTRIBUTE_name = "name";
	public static final String ATTRIBUTE_language = "language";
	public static final String ATTRIBUTE_columnCount = "columnCount";
	public static final String ATTRIBUTE_printOrder = "printOrder";
	public static final String ATTRIBUTE_pageWidth = "pageWidth";
	public static final String ATTRIBUTE_pageHeight = "pageHeight";
	public static final String ATTRIBUTE_orientation = "orientation";
	public static final String ATTRIBUTE_whenNoDataType = "whenNoDataType";
	public static final String ATTRIBUTE_columnWidth = "columnWidth";
	public static final String ATTRIBUTE_columnSpacing = "columnSpacing";
	public static final String ATTRIBUTE_leftMargin = "leftMargin";
	public static final String ATTRIBUTE_rightMargin = "rightMargin";
	public static final String ATTRIBUTE_topMargin = "topMargin";
	public static final String ATTRIBUTE_bottomMargin = "bottomMargin";
	public static final String ATTRIBUTE_isTitleNewPage = "isTitleNewPage";
	public static final String ATTRIBUTE_isSummaryNewPage = "isSummaryNewPage";
	public static final String ATTRIBUTE_isSummaryWithPageHeaderAndFooter = "isSummaryWithPageHeaderAndFooter";
	public static final String ATTRIBUTE_isFloatColumnFooter = "isFloatColumnFooter";
	public static final String ATTRIBUTE_scriptletClass = "scriptletClass";
	public static final String ATTRIBUTE_formatFactoryClass = "formatFactoryClass";
	public static final String ATTRIBUTE_resourceBundle = "resourceBundle";
	public static final String ATTRIBUTE_whenResourceMissingType = "whenResourceMissingType";
	public static final String ATTRIBUTE_isIgnorePagination = "isIgnorePagination";

	public static final String ATTRIBUTE_value = "value";

	public static final String ELEMENT_import = "import";
	public static final String ELEMENT_background = "background";
	public static final String ELEMENT_title = "title";
	public static final String ELEMENT_pageHeader = "pageHeader";
	public static final String ELEMENT_columnHeader = "columnHeader";
	public static final String ELEMENT_detail = "detail";
	public static final String ELEMENT_columnFooter = "columnFooter";
	public static final String ELEMENT_pageFooter = "pageFooter";
	public static final String ELEMENT_lastPageFooter = "lastPageFooter";
	public static final String ELEMENT_summary = "summary";
	public static final String ELEMENT_noData = "noData";
	public static final String ELEMENT_property = "property";
	public static final String ELEMENT_propertyExpression = "propertyExpression";

	public static final String ELEMENT_page = "page";

	/**
	 * JasperPrintFactory associated constants
	 */
	public static final String ELEMENT_jasperPrint = "jasperPrint";
	public static final String ATTRIBUTE_locale = "locale";
	public static final String ATTRIBUTE_timezone = "timezone";

	/**
	 * JROriginFactory associated constants
	 */
	public static final String ELEMENT_origin = "origin";
	public static final String ATTRIBUTE_report = "report";
	public static final String ATTRIBUTE_group = "group";
	public static final String ATTRIBUTE_band = "band";

	/**
	 * JRBandFactory associated constants
	 */
	public static final String ELEMENT_band = "band";
	public static final String ELEMENT_printWhenExpression = "printWhenExpression";

	public static final String ATTRIBUTE_height = "height";
	public static final String ATTRIBUTE_isSplitAllowed = "isSplitAllowed";
	public static final String ATTRIBUTE_splitType = "splitType";

	/**
	 * JRPenFactory associated constants
	 */
	public static final String ELEMENT_pen = "pen";

	public static final String ATTRIBUTE_lineWidth = "lineWidth";
	public static final String ATTRIBUTE_lineStyle = "lineStyle";
	public static final String ATTRIBUTE_lineColor = "lineColor";

	/**
	 * JRBoxFactory associated constants
	 */
	public static final String ELEMENT_box = "box";
	public static final String ELEMENT_topPen = "topPen";
	public static final String ELEMENT_leftPen = "leftPen";
	public static final String ELEMENT_bottomPen = "bottomPen";
	public static final String ELEMENT_rightPen = "rightPen";

	public static final String ATTRIBUTE_border = "border";
	public static final String ATTRIBUTE_borderColor = "borderColor";
	public static final String ATTRIBUTE_padding = "padding";
	public static final String ATTRIBUTE_topBorder = "topBorder";
	public static final String ATTRIBUTE_topBorderColor = "topBorderColor";
	public static final String ATTRIBUTE_topPadding = "topPadding";
	public static final String ATTRIBUTE_leftBorder = "leftBorder";
	public static final String ATTRIBUTE_leftBorderColor = "leftBorderColor";
	public static final String ATTRIBUTE_leftPadding = "leftPadding";
	public static final String ATTRIBUTE_bottomBorder = "bottomBorder";
	public static final String ATTRIBUTE_bottomBorderColor = "bottomBorderColor";
	public static final String ATTRIBUTE_bottomPadding = "bottomPadding";
	public static final String ATTRIBUTE_rightBorder = "rightBorder";
	public static final String ATTRIBUTE_rightBorderColor = "rightBorderColor";
	public static final String ATTRIBUTE_rightPadding = "rightPadding";

	/**
	 * JRBreakFactory associated constants
	 */
	public static final String ELEMENT_break = "break";

	public static final String ATTRIBUTE_type = "type";

	/**
	 * JRChartFactory associated constants
	 */
	public static final String ELEMENT_chart = "chart";
	public static final String ELEMENT_chartTitle = "chartTitle";
	public static final String ELEMENT_titleExpression = "titleExpression";
	public static final String ELEMENT_chartSubtitle = "chartSubtitle";
	public static final String ELEMENT_subtitleExpression = "subtitleExpression";
	public static final String ELEMENT_chartLegend = "chartLegend";

	public static final String ELEMENT_pieChart = "pieChart";
	public static final String ELEMENT_pie3DChart = "pie3DChart";
	public static final String ELEMENT_barChart = "barChart";
	public static final String ELEMENT_bar3DChart = "bar3DChart";
	public static final String ELEMENT_bubbleChart = "bubbleChart";
	public static final String ELEMENT_stackedBarChart = "stackedBarChart";
	public static final String ELEMENT_stackedBar3DChart = "stackedBar3DChart";
	public static final String ELEMENT_lineChart = "lineChart";
	public static final String ELEMENT_highLowChart = "highLowChart";
	public static final String ELEMENT_candlestickChart = "candlestickChart";
	public static final String ELEMENT_areaChart = "areaChart";
	public static final String ELEMENT_scatterChart = "scatterChart";
	public static final String ELEMENT_timeSeriesChart = "timeSeriesChart";
	public static final String ELEMENT_xyAreaChart = "xyAreaChart";
	public static final String ELEMENT_xyBarChart = "xyBarChart";
	public static final String ELEMENT_xyLineChart = "xyLineChart";
	public static final String ELEMENT_meterChart = "meterChart";
	public static final String ELEMENT_thermometerChart = "thermometerChart";
	public static final String ELEMENT_multiAxisChart = "multiAxisChart";
	public static final String ELEMENT_stackedAreaChart = "stackedAreaChart";
	public static final String ELEMENT_ganttChart = "ganttChart";

	public static final String ATTRIBUTE_isShowLegend = "isShowLegend";
	public static final String ATTRIBUTE_evaluationTime = "evaluationTime";
	public static final String ATTRIBUTE_evaluationGroup = "evaluationGroup";
	public static final String ATTRIBUTE_bookmarkLevel = "bookmarkLevel";
	public static final String ATTRIBUTE_customizerClass = "customizerClass";
	public static final String ATTRIBUTE_renderType = "renderType";
	public static final String ATTRIBUTE_theme = "theme";

	/**
	 * JRChartAxisFormatFactory associated constants
	 */
	public static final String ELEMENT_axisFormat = "axisFormat";
	public static final String ELEMENT_labelFont = "labelFont";
	public static final String ELEMENT_tickLabelFont = "tickLabelFont";
	public static final String ATTRIBUTE_labelColor = "labelColor";
	public static final String ATTRIBUTE_tickLabelColor = "tickLabelColor";
	public static final String ATTRIBUTE_tickLabelMask = "tickLabelMask";
	public static final String ATTRIBUTE_verticalTickLabels = "verticalTickLabels";
	public static final String ATTRIBUTE_axisLineColor = "axisLineColor";

	/**
	 * JRChartLegendFactory associated constants
	 */
	public static final String ATTRIBUTE_textColor = "textColor";
	public static final String ATTRIBUTE_backgroundColor = "backgroundColor";

	/**
	 * JRChartTitleFactory associated constants
	 */
	public static final String ATTRIBUTE_position = "position";
	public static final String ATTRIBUTE_color = "color";

	/**
	 * JRChartPlotFactory associated constants
	 */
	public static final String ELEMENT_plot = "plot";
	public static final String ELEMENT_piePlot = "piePlot";
	public static final String ELEMENT_pie3DPlot = "pie3DPlot";
	public static final String ELEMENT_barPlot = "barPlot";
	public static final String ELEMENT_bubblePlot = "bubblePlot";
	public static final String ELEMENT_linePlot = "linePlot";
	public static final String ELEMENT_timeSeriesPlot = "timeSeriesPlot";
	public static final String ELEMENT_bar3DPlot = "bar3DPlot";
	public static final String ELEMENT_highLowPlot = "highLowPlot";
	public static final String ELEMENT_candlestickPlot = "candlestickPlot";
	public static final String ELEMENT_areaPlot = "areaPlot";
	public static final String ELEMENT_scatterPlot = "scatterPlot";
	public static final String ELEMENT_multiAxisPlot = "multiAxisPlot";

	public static final String ELEMENT_valueDisplay = "valueDisplay";
	public static final String ELEMENT_itemLabel = "itemLabel";
	public static final String ELEMENT_dataRange = "dataRange";
	public static final String ELEMENT_meterInterval = "meterInterval";
	public static final String ELEMENT_categoryAxisFormat = "categoryAxisFormat";
	public static final String ELEMENT_valueAxisFormat = "valueAxisFormat";
	public static final String ELEMENT_xAxisFormat = "xAxisFormat";
	public static final String ELEMENT_yAxisFormat = "yAxisFormat";
	public static final String ELEMENT_timeAxisFormat = "timeAxisFormat";

	public static final String ELEMENT_lowExpression = "lowExpression";
	public static final String ELEMENT_highExpression = "highExpression";
	public static final String ELEMENT_categoryAxisLabelExpression = "categoryAxisLabelExpression";
	public static final String ELEMENT_valueAxisLabelExpression = "valueAxisLabelExpression";
	public static final String ELEMENT_domainAxisMinValueExpression = "domainAxisMinValueExpression";
	public static final String ELEMENT_domainAxisMaxValueExpression = "domainAxisMaxValueExpression";
	public static final String ELEMENT_rangeAxisMinValueExpression = "rangeAxisMinValueExpression";
	public static final String ELEMENT_rangeAxisMaxValueExpression = "rangeAxisMaxValueExpression";
	public static final String ELEMENT_xAxisLabelExpression = "xAxisLabelExpression";
	public static final String ELEMENT_yAxisLabelExpression = "yAxisLabelExpression";
	public static final String ELEMENT_timeAxisLabelExpression = "timeAxisLabelExpression";
	public static final String ELEMENT_taskExpression = "taskExpression";
	public static final String ELEMENT_subtaskExpression = "subtaskExpression";
	public static final String ELEMENT_percentExpression = "percentExpression";

	public static final String ATTRIBUTE_backgroundAlpha = "backgroundAlpha";
	public static final String ATTRIBUTE_foregroundAlpha = "foregroundAlpha";
	public static final String ATTRIBUTE_labelRotation = "labelRotation";

	public static final String ATTRIBUTE_mask = "mask";
	public static final String ATTRIBUTE_label = "label";
	public static final String ATTRIBUTE_alpha = "alpha";
	public static final String ATTRIBUTE_depthFactor = "depthFactor";
	public static final String ATTRIBUTE_isShowLabels = "isShowLabels";
	public static final String ATTRIBUTE_isShowTickLabels = "isShowTickLabels";
	public static final String ATTRIBUTE_scaleType = "scaleType";
	public static final String ATTRIBUTE_isShowTickMarks = "isShowTickMarks";
	public static final String ATTRIBUTE_isShowLines = "isShowLines";
	public static final String ATTRIBUTE_isShowShapes = "isShowShapes";
	public static final String ATTRIBUTE_xOffset = "xOffset";
	public static final String ATTRIBUTE_yOffset = "yOffset";
	public static final String ATTRIBUTE_isShowOpenTicks = "isShowOpenTicks";
	public static final String ATTRIBUTE_isShowCloseTicks = "isShowCloseTicks";
	public static final String ATTRIBUTE_isShowVolume = "isShowVolume";
	public static final String ATTRIBUTE_isCircular = "isCircular";
	public static final String ATTRIBUTE_labelFormat = "labelFormat";
	public static final String ATTRIBUTE_legendLabelFormat = "legendLabelFormat";

	/**
	 * JRSeriesColorFactory associated constants
	 */
	public static final String ELEMENT_seriesColor = "seriesColor";

	public static final String ATTRIBUTE_seriesOrder = "seriesOrder";

	/**
	 * JRConditionalStyleFactory associated constants
	 */
	public static final String ELEMENT_conditionalStyle = "conditionalStyle";
	public static final String ELEMENT_conditionExpression = "conditionExpression";

	/**
	 * JRStyleFactory associated constants
	 */
	public static final String ELEMENT_style = "style";

	public static final String ATTRIBUTE_isDefault = "isDefault";
	public static final String ATTRIBUTE_mode = "mode";
	public static final String ATTRIBUTE_forecolor = "forecolor";
	public static final String ATTRIBUTE_backcolor = "backcolor";
	public static final String ATTRIBUTE_style = "style";
	public static final String ATTRIBUTE_origin = "origin";

	public static final String ATTRIBUTE_radius = "radius";

	// these are inherited by both images and texts.

	public static final String ATTRIBUTE_rotation = "rotation";
	public static final String ATTRIBUTE_lineSpacing = "lineSpacing";
	public static final String ATTRIBUTE_isStyledText = "isStyledText";
	public static final String ATTRIBUTE_markup = "markup";
	public static final String ATTRIBUTE_pattern = "pattern";
	public static final String ATTRIBUTE_isBlankWhenNull = "isBlankWhenNull";

	public static final String ATTRIBUTE_fontSize = "fontSize";

	/**
	 * JRDatasetFactory associated constants
	 */
	public static final String ELEMENT_subDataset = "subDataset";
	public static final String ELEMENT_filterExpression = "filterExpression";

	/**
	 * JRDatasetRunFactory associated constants
	 */
	public static final String ELEMENT_datasetRun = "datasetRun";

	public static final String ELEMENT_parametersMapExpression = "parametersMapExpression";
	public static final String ELEMENT_connectionExpression = "connectionExpression";
	public static final String ELEMENT_dataSourceExpression = "dataSourceExpression";

	public static final String ATTRIBUTE_subDataset = "subDataset";

	/**
	 * JRDatasetRunParameterExpressionFactory associated constants
	 */
	public static final String ELEMENT_datasetParameterExpression = "datasetParameterExpression";

	/**
	 * JRDatasetRunParameterFactory associated constants
	 */
	public static final String ELEMENT_datasetParameter = "datasetParameter";

	/**
	 * JRElementDatasetFactory associated constants
	 */
	public static final String ELEMENT_dataset = "dataset";
	public static final String ELEMENT_categoryDataset = "categoryDataset";
	public static final String ELEMENT_timeSeriesDataset = "timeSeriesDataset";
	public static final String ELEMENT_timePeriodDataset = "timePeriodDataset";
	public static final String ELEMENT_xyzDataset = "xyzDataset";
	public static final String ELEMENT_xyDataset = "xyDataset";
	public static final String ELEMENT_pieDataset = "pieDataset";
	public static final String ELEMENT_valueDataset = "valueDataset";
	public static final String ELEMENT_highLowDataset = "highLowDataset";
	public static final String ELEMENT_ganttDataset = "ganttDataset";

	public static final String ELEMENT_pieSeries = "pieSeries";
	public static final String ELEMENT_categorySeries = "categorySeries";
	public static final String ELEMENT_xyzSeries = "xyzSeries";
	public static final String ELEMENT_xySeries = "xySeries";
	public static final String ELEMENT_timeSeries = "timeSeries";
	public static final String ELEMENT_timePeriodSeries = "timePeriodSeries";
	public static final String ELEMENT_ganttSeries = "ganttSeries";

	public static final String ELEMENT_incrementWhenExpression = "incrementWhenExpression";
	public static final String ELEMENT_keyExpression = "keyExpression";
	public static final String ELEMENT_valueExpression = "valueExpression";
	public static final String ELEMENT_labelExpression = "labelExpression";
	public static final String ELEMENT_otherKeyExpression = "otherKeyExpression";
	public static final String ELEMENT_otherLabelExpression = "otherLabelExpression";
	public static final String ELEMENT_seriesExpression = "seriesExpression";
	public static final String ELEMENT_categoryExpression = "categoryExpression";
	public static final String ELEMENT_xValueExpression = "xValueExpression";
	public static final String ELEMENT_yValueExpression = "yValueExpression";
	public static final String ELEMENT_zValueExpression = "zValueExpression";
	public static final String ELEMENT_timePeriodExpression = "timePeriodExpression";
	public static final String ELEMENT_startDateExpression = "startDateExpression";
	public static final String ELEMENT_endDateExpression = "endDateExpression";
	public static final String ELEMENT_dateExpression = "dateExpression";
	public static final String ELEMENT_openExpression = "openExpression";
	public static final String ELEMENT_closeExpression = "closeExpression";
	public static final String ELEMENT_volumeExpression = "volumeExpression";

	public static final String ATTRIBUTE_maxCount = "maxCount";
	public static final String ATTRIBUTE_minPercentage = "minPercentage";
	public static final String ATTRIBUTE_timePeriod = "timePeriod";

	/**
	 * JRElementFactory associated constants
	 */
	public static final String ELEMENT_reportElement = "reportElement";

	public static final String ATTRIBUTE_key = "key";
	public static final String ATTRIBUTE_positionType = "positionType";
	public static final String ATTRIBUTE_stretchType = "stretchType";
	public static final String ATTRIBUTE_isPrintRepeatedValues = "isPrintRepeatedValues";
	public static final String ATTRIBUTE_x = "x";
	public static final String ATTRIBUTE_y = "y";
	public static final String ATTRIBUTE_width = "width";
	public static final String ATTRIBUTE_isRemoveLineWhenBlank = "isRemoveLineWhenBlank";
	public static final String ATTRIBUTE_isPrintInFirstWholeBand = "isPrintInFirstWholeBand";
	public static final String ATTRIBUTE_isPrintWhenDetailOverflows = "isPrintWhenDetailOverflows";
	public static final String ATTRIBUTE_printWhenGroupChanges = "printWhenGroupChanges";

	/**
	 * JRElementGroupFactory associated constants
	 */
	public static final String ELEMENT_elementGroup = "elementGroup";

	/**
	 * JREllipseFactory associated constants
	 */
	public static final String ELEMENT_ellipse = "ellipse";

	/**
	 * JRFieldFactory associated constants
	 */
	public static final String ELEMENT_field = "field";
	public static final String ELEMENT_fieldDescription = "fieldDescription";

	public static final String ATTRIBUTE_class = "class";

	public static final String ATTRIBUTE_nestedType = "nestedType";

	/**
	 * JRFieldFactory associated constants
	 */
	public static final String ELEMENT_font = "font";

	public static final String ATTRIBUTE_reportFont = "reportFont";
	public static final String ATTRIBUTE_fontName = "fontName";
	public static final String ATTRIBUTE_isBold = "isBold";
	public static final String ATTRIBUTE_isItalic = "isItalic";
	public static final String ATTRIBUTE_isUnderline = "isUnderline";
	public static final String ATTRIBUTE_isStrikeThrough = "isStrikeThrough";
	public static final String ATTRIBUTE_size = "size";
	public static final String ATTRIBUTE_pdfFontName = "pdfFontName";
	public static final String ATTRIBUTE_pdfEncoding = "pdfEncoding";
	public static final String ATTRIBUTE_isPdfEmbedded = "isPdfEmbedded";

	/**
	 * JRFrameFactory associated constants
	 */
	public static final String ELEMENT_frame = "frame";

	/**
	 * JRGraphicElementFactory associated constants
	 */
	public static final String ELEMENT_graphicElement = "graphicElement";

	public static final String ATTRIBUTE_pen = "pen";
	public static final String ATTRIBUTE_fill = "fill";

	/**
	 * JRGroupFactory associated constants
	 */
	public static final String ELEMENT_group = "group";
	public static final String ELEMENT_groupExpression = "groupExpression";
	public static final String ELEMENT_groupHeader = "groupHeader";
	public static final String ELEMENT_groupFooter = "groupFooter";

	public static final String ATTRIBUTE_isStartNewColumn = "isStartNewColumn";
	public static final String ATTRIBUTE_isStartNewPage = "isStartNewPage";
	public static final String ATTRIBUTE_isResetPageNumber = "isResetPageNumber";
	public static final String ATTRIBUTE_isReprintHeaderOnEachPage = "isReprintHeaderOnEachPage";
	public static final String ATTRIBUTE_minHeightToStartNewPage = "minHeightToStartNewPage";
	public static final String ATTRIBUTE_footerPosition = "footerPosition";
	public static final String ATTRIBUTE_keepTogether = "keepTogether";

	/**
	 * JRHyperlinkFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkTooltipExpression = "hyperlinkTooltipExpression";
	public static final String ELEMENT_sectionHyperlink = "sectionHyperlink";
	public static final String ELEMENT_otherSectionHyperlink = "otherSectionHyperlink";
	public static final String ELEMENT_itemHyperlink = "itemHyperlink";
	public static final String ELEMENT_anchorNameExpression = "anchorNameExpression";
	public static final String ELEMENT_hyperlinkReferenceExpression = "hyperlinkReferenceExpression";
	public static final String ELEMENT_hyperlinkAnchorExpression = "hyperlinkAnchorExpression";
	public static final String ELEMENT_hyperlinkPageExpression = "hyperlinkPageExpression";

	public static final String ATTRIBUTE_hyperlinkType = "hyperlinkType";
	public static final String ATTRIBUTE_hyperlinkTarget = "hyperlinkTarget";

	/**
	 * JRHyperlinkFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkParameterExpression = "hyperlinkParameterExpression";

	/**
	 * JRHyperlinkParameterFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkParameter = "hyperlinkParameter";

	/**
	 * JRImageFactory associated constants
	 */
	public static final String ELEMENT_image = "image";
	public static final String ELEMENT_imageSource = "imageSource";
	public static final String ELEMENT_imageExpression = "imageExpression";

	public static final String ATTRIBUTE_scaleImage = "scaleImage";
	public static final String ATTRIBUTE_hAlign = "hAlign";
	public static final String ATTRIBUTE_vAlign = "vAlign";
	public static final String ATTRIBUTE_isUsingCache = "isUsingCache";
	public static final String ATTRIBUTE_isLazy = "isLazy";
	public static final String ATTRIBUTE_onErrorType = "onErrorType";
	
	public static final String ATTRIBUTE_runToBottom = "runToBottom";

	/**
	 * JRLineFactory associated constants
	 */
	public static final String ELEMENT_line = "line";

	public static final String ATTRIBUTE_direction = "direction";

	/**
	 * JRScriptletFactory associated constants
	 */
	public static final String ELEMENT_scriptlet = "scriptlet";
	public static final String ELEMENT_scriptletDescription = "scriptletDescription";

	/**
	 * JRParameterFactory associated constants
	 */
	public static final String ELEMENT_parameter = "parameter";
	public static final String ELEMENT_parameterDescription = "parameterDescription";
	public static final String ELEMENT_defaultValueExpression = "defaultValueExpression";

	public static final String ATTRIBUTE_isForPrompting = "isForPrompting";

	/**
	 * JRPrintHyperlinkParameterValueFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkParameterValue = "hyperlinkParameterValue";

	/**
	 * JRPrintImageFactory associated constants
	 */
	public static final String ATTRIBUTE_anchorName = "anchorName";
	public static final String ATTRIBUTE_hyperlinkReference = "hyperlinkReference";
	public static final String ATTRIBUTE_hyperlinkAnchor = "hyperlinkAnchor";
	public static final String ATTRIBUTE_hyperlinkPage = "hyperlinkPage";
	public static final String ATTRIBUTE_hyperlinkTooltip = "hyperlinkTooltip";

	/**
	 * JRPrintImageSourceFactory associated constants
	 */
	public static final String ATTRIBUTE_isEmbedded = "isEmbedded";

	/**
	 * JRPrintTextFactory associated constants
	 */
	public static final String ATTRIBUTE_textAlignment = "textAlignment";
	public static final String ATTRIBUTE_verticalAlignment = "verticalAlignment";
	public static final String ATTRIBUTE_runDirection = "runDirection";
	public static final String ATTRIBUTE_textHeight = "textHeight";
	public static final String ATTRIBUTE_lineSpacingFactor = "lineSpacingFactor";
	public static final String ATTRIBUTE_leadingOffset = "leadingOffset";
	public static final String ATTRIBUTE_valueClass = "valueClass";

	/**
	 * JRQueryFactory associated constants
	 */
	public static final String ELEMENT_queryString = "queryString";

	/**
	 * JRRectangleFactory associated constants
	 */
	public static final String ELEMENT_rectangle = "rectangle";

	/**
	 * JRSortFieldFactory associated constants
	 */
	public static final String ELEMENT_sortField = "sortField";

	public static final String ATTRIBUTE_order = "order";

	/**
	 * JRStaticTextFactory associated constants
	 */
	public static final String ELEMENT_staticText = "staticText";
	public static final String ELEMENT_text = "text";
	public static final String ELEMENT_textContent = "textContent";
	public static final String ATTRIBUTE_truncateIndex = "truncateIndex";
	public static final String ELEMENT_textTruncateSuffix = "textTruncateSuffix";
	public static final String ELEMENT_lineBreakOffsets = "lineBreakOffsets";
	public static final String LINE_BREAK_OFFSET_SEPARATOR = ",";

	/**
	 * JRSubreportExpressionFactory associated constants
	 */
	public static final String ELEMENT_subreportExpression = "subreportExpression";

	/**
	 * JRSubreportFactory associated constants
	 */
	public static final String ELEMENT_subreport = "subreport";

	/**
	 * JRSubreportParameterFactory associated constants
	 */
	public static final String ELEMENT_subreportParameter = "subreportParameter";
	public static final String ELEMENT_subreportParameterExpression = "subreportParameterExpression";

	/**
	 * JRSubreportReturnValueFactory associated constants
	 */
	public static final String ELEMENT_returnValue = "returnValue";

	public static final String ATTRIBUTE_subreportVariable = "subreportVariable";
	public static final String ATTRIBUTE_toVariable = "toVariable";

	/**
	 * JRTextElementFactory associated constants
	 */
	public static final String ELEMENT_textElement = "textElement";

	/**
	 * JRTextFieldExpressionFactory associated constants
	 */
	public static final String ELEMENT_textFieldExpression = "textFieldExpression";

	/**
	 * JRTextFieldFactory  associated constants
	 */
	public static final String ELEMENT_textField = "textField";

	public static final String ATTRIBUTE_isStretchWithOverflow = "isStretchWithOverflow";

	/**
	 * JRVariableFactory  associated constants
	 */
	public static final String ELEMENT_variable = "variable";
	public static final String ELEMENT_variableExpression = "variableExpression";
	public static final String ELEMENT_initialValueExpression = "initialValueExpression";

	public static final String ATTRIBUTE_resetType = "resetType";
	public static final String ATTRIBUTE_resetGroup = "resetGroup";
	public static final String ATTRIBUTE_incrementType = "incrementType";
	public static final String ATTRIBUTE_incrementGroup = "incrementGroup";
	public static final String ATTRIBUTE_calculation = "calculation";
	public static final String ATTRIBUTE_incrementerFactoryClass = "incrementerFactoryClass";

	/**
	 * JRReportFontFactory  associated constants
	 */
	public static final String ELEMENT_reportFont = "reportFont";

	public static final String ELEMENT_template = "template";


	public static final String TEMPLATE_ELEMENT_ROOT = "jasperTemplate";
	public static final String TEMPLATE_ELEMENT_INCLUDED_TEMPLATE = "template";
	
	public static final String ELEMENT_componentElement = "componentElement";
	
	public static final String ELEMENT_genericElement = "genericElement";
	public static final String ELEMENT_genericElementType = "genericElementType";
	public static final String ELEMENT_genericElementParameter = 
		"genericElementParameter";
	public static final String ELEMENT_genericElementParameter_valueExpression = 
		"valueExpression";
	public static final String ATTRIBUTE_namespace = "namespace";
	public static final String ATTRIBUTE_skipWhenNull = "skipWhenNull";

	public static final String ELEMENT_genericElementParameterValue = 
		"genericElementParameterValue";
}
