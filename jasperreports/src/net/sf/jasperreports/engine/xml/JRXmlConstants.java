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
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGraphicElement;
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
	 *
	 */
	private static final String POSITION_TYPE_FLOAT = "Float";
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_TOP = "FixRelativeToTop";
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM = "FixRelativeToBottom";

	private static Map positionTypeMap = null;

	public static Map getPositionTypeMap()
	{
		if (positionTypeMap == null)
		{
			positionTypeMap = new HashMap(6);
			positionTypeMap.put(POSITION_TYPE_FLOAT,                  new Byte(JRElement.POSITION_TYPE_FLOAT));
			positionTypeMap.put(POSITION_TYPE_FIX_RELATIVE_TO_TOP,    new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP));
			positionTypeMap.put(POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM, new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM));
			positionTypeMap.put(new Byte(JRElement.POSITION_TYPE_FLOAT),                  POSITION_TYPE_FLOAT);
			positionTypeMap.put(new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP),    POSITION_TYPE_FIX_RELATIVE_TO_TOP);
			positionTypeMap.put(new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM), POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM);
		}

		return positionTypeMap;
	}

	/**
	 *
	 */
	private static final String MODE_OPAQUE = "Opaque";
	private static final String MODE_TRANSPARENT = "Transparent";

	private static Map modeMap = null;

	public static Map getModeMap()
	{
		if (modeMap == null)
		{
			modeMap = new HashMap(4);
			modeMap.put(MODE_OPAQUE,      new Byte(JRElement.MODE_OPAQUE));
			modeMap.put(MODE_TRANSPARENT, new Byte(JRElement.MODE_TRANSPARENT));
			modeMap.put(new Byte(JRElement.MODE_OPAQUE),      MODE_OPAQUE);
			modeMap.put(new Byte(JRElement.MODE_TRANSPARENT), MODE_TRANSPARENT);
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
			colorMap = new HashMap(26);
			colorMap.put(COLOR_BLACK,      Color.black);
			colorMap.put(COLOR_BLUE,       Color.blue);
			colorMap.put(COLOR_CYAN,       Color.cyan);
			colorMap.put(COLOR_DARK_GRAY,  Color.darkGray);
			colorMap.put(COLOR_GRAY,       Color.gray);
			colorMap.put(COLOR_GREEN,      Color.green);
			colorMap.put(COLOR_LIGHT_GRAY, Color.lightGray);
			colorMap.put(COLOR_MAGENTA,    Color.magenta);
			colorMap.put(COLOR_ORANGE,     Color.orange);
			colorMap.put(COLOR_PINK,       Color.pink);
			colorMap.put(COLOR_RED,        Color.red);
			colorMap.put(COLOR_YELLOW,     Color.yellow);
			colorMap.put(COLOR_WHITE,      Color.white);
			colorMap.put(Color.black,      COLOR_BLACK);
			colorMap.put(Color.blue,       COLOR_BLUE);
			colorMap.put(Color.cyan,       COLOR_CYAN);
			colorMap.put(Color.darkGray,  COLOR_DARK_GRAY);
			colorMap.put(Color.gray,       COLOR_GRAY);
			colorMap.put(Color.green,      COLOR_GREEN);
			colorMap.put(Color.lightGray, COLOR_LIGHT_GRAY);
			colorMap.put(Color.magenta,    COLOR_MAGENTA);
			colorMap.put(Color.orange,     COLOR_ORANGE);
			colorMap.put(Color.pink,       COLOR_PINK);
			colorMap.put(Color.red,        COLOR_RED);
			colorMap.put(Color.yellow,     COLOR_YELLOW);
			colorMap.put(Color.white,      COLOR_WHITE);
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
			horizontalAlignMap = new HashMap(8);
			horizontalAlignMap.put(HORIZONTAL_ALIGN_LEFT,      new Byte(JRAlignment.HORIZONTAL_ALIGN_LEFT));
			horizontalAlignMap.put(HORIZONTAL_ALIGN_CENTER,    new Byte(JRAlignment.HORIZONTAL_ALIGN_CENTER));
			horizontalAlignMap.put(HORIZONTAL_ALIGN_RIGHT,     new Byte(JRAlignment.HORIZONTAL_ALIGN_RIGHT));
			horizontalAlignMap.put(HORIZONTAL_ALIGN_JUSTIFIED, new Byte(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED));
			horizontalAlignMap.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_LEFT),      HORIZONTAL_ALIGN_LEFT);
			horizontalAlignMap.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_CENTER),    HORIZONTAL_ALIGN_CENTER);
			horizontalAlignMap.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_RIGHT),     HORIZONTAL_ALIGN_RIGHT);
			horizontalAlignMap.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED), HORIZONTAL_ALIGN_JUSTIFIED);
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
			verticalAlignMap = new HashMap(6);
			verticalAlignMap.put(VERTICAL_ALIGN_TOP,    new Byte(JRAlignment.VERTICAL_ALIGN_TOP));
			verticalAlignMap.put(VERTICAL_ALIGN_MIDDLE, new Byte(JRAlignment.VERTICAL_ALIGN_MIDDLE));
			verticalAlignMap.put(VERTICAL_ALIGN_BOTTOM, new Byte(JRAlignment.VERTICAL_ALIGN_BOTTOM));
			verticalAlignMap.put(new Byte(JRAlignment.VERTICAL_ALIGN_TOP),    VERTICAL_ALIGN_TOP);
			verticalAlignMap.put(new Byte(JRAlignment.VERTICAL_ALIGN_MIDDLE), VERTICAL_ALIGN_MIDDLE);
			verticalAlignMap.put(new Byte(JRAlignment.VERTICAL_ALIGN_BOTTOM), VERTICAL_ALIGN_BOTTOM);
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
			rotationMap = new HashMap(6);
			rotationMap.put(ROTATION_NONE,  		new Byte(JRTextElement.ROTATION_NONE));
			rotationMap.put(ROTATION_LEFT,  		new Byte(JRTextElement.ROTATION_LEFT));
			rotationMap.put(ROTATION_RIGHT, 		new Byte(JRTextElement.ROTATION_RIGHT));
			rotationMap.put(ROTATION_UPSIDE_DOWN, 	new Byte(JRTextElement.ROTATION_UPSIDE_DOWN));
			rotationMap.put(new Byte(JRTextElement.ROTATION_NONE),  		ROTATION_NONE);
			rotationMap.put(new Byte(JRTextElement.ROTATION_LEFT),  		ROTATION_LEFT);
			rotationMap.put(new Byte(JRTextElement.ROTATION_RIGHT), 		ROTATION_RIGHT);
			rotationMap.put(new Byte(JRTextElement.ROTATION_UPSIDE_DOWN), 	ROTATION_UPSIDE_DOWN);
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
			breakTypeMap = new HashMap(4);
			breakTypeMap.put(BREAK_TYPE_PAGE,   new Byte(JRBreak.TYPE_PAGE));
			breakTypeMap.put(BREAK_TYPE_COLUMN, new Byte(JRBreak.TYPE_COLUMN));
			breakTypeMap.put(new Byte(JRBreak.TYPE_PAGE),   BREAK_TYPE_PAGE);
			breakTypeMap.put(new Byte(JRBreak.TYPE_COLUMN), BREAK_TYPE_COLUMN);
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
			runDirectionMap = new HashMap(4);
			runDirectionMap.put(RUN_DIRECTION_LTR, new Byte(JRPrintText.RUN_DIRECTION_LTR));
			runDirectionMap.put(RUN_DIRECTION_RTL, new Byte(JRPrintText.RUN_DIRECTION_RTL));
			runDirectionMap.put(new Byte(JRPrintText.RUN_DIRECTION_LTR), RUN_DIRECTION_LTR);
			runDirectionMap.put(new Byte(JRPrintText.RUN_DIRECTION_RTL), RUN_DIRECTION_RTL);
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
			lineSpacingMap = new HashMap(6);
			lineSpacingMap.put(LINE_SPACING_SINGLE, new Byte(JRTextElement.LINE_SPACING_SINGLE));
			lineSpacingMap.put(LINE_SPACING_1_1_2,  new Byte(JRTextElement.LINE_SPACING_1_1_2));
			lineSpacingMap.put(LINE_SPACING_DOUBLE, new Byte(JRTextElement.LINE_SPACING_DOUBLE));
			lineSpacingMap.put(new Byte(JRTextElement.LINE_SPACING_SINGLE), LINE_SPACING_SINGLE);
			lineSpacingMap.put(new Byte(JRTextElement.LINE_SPACING_1_1_2),  LINE_SPACING_1_1_2);
			lineSpacingMap.put(new Byte(JRTextElement.LINE_SPACING_DOUBLE), LINE_SPACING_DOUBLE);
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
			directionMap = new HashMap(4);
			directionMap.put(DIRECTION_TOP_DOWN,  new Byte(JRLine.DIRECTION_TOP_DOWN));
			directionMap.put(DIRECTION_BOTTOM_UP, new Byte(JRLine.DIRECTION_BOTTOM_UP));
			directionMap.put(new Byte(JRLine.DIRECTION_TOP_DOWN),  DIRECTION_TOP_DOWN);
			directionMap.put(new Byte(JRLine.DIRECTION_BOTTOM_UP), DIRECTION_BOTTOM_UP);
		}

		return directionMap;
	}

	/**
	 *
	 */
	private static final String SCALE_IMAGE_CLIP = "Clip";
	private static final String SCALE_IMAGE_FILL_FRAME = "FillFrame";
	private static final String SCALE_IMAGE_RETAIN_SHAPE = "RetainShape";

	private static Map scaleImageMap = null;

	public static Map getScaleImageMap()
	{
		if (scaleImageMap == null)
		{
			scaleImageMap = new HashMap(6);
			scaleImageMap.put(SCALE_IMAGE_CLIP,         new Byte(JRImage.SCALE_IMAGE_CLIP));
			scaleImageMap.put(SCALE_IMAGE_FILL_FRAME,   new Byte(JRImage.SCALE_IMAGE_FILL_FRAME));
			scaleImageMap.put(SCALE_IMAGE_RETAIN_SHAPE, new Byte(JRImage.SCALE_IMAGE_RETAIN_SHAPE));
			scaleImageMap.put(new Byte(JRImage.SCALE_IMAGE_CLIP),         SCALE_IMAGE_CLIP);
			scaleImageMap.put(new Byte(JRImage.SCALE_IMAGE_FILL_FRAME),   SCALE_IMAGE_FILL_FRAME);
			scaleImageMap.put(new Byte(JRImage.SCALE_IMAGE_RETAIN_SHAPE), SCALE_IMAGE_RETAIN_SHAPE);
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
			onErrorTypeMap = new HashMap(6);
			onErrorTypeMap.put(ON_ERROR_TYPE_ERROR, new Byte(JRImage.ON_ERROR_TYPE_ERROR));
			onErrorTypeMap.put(ON_ERROR_TYPE_BLANK, new Byte(JRImage.ON_ERROR_TYPE_BLANK));
			onErrorTypeMap.put(ON_ERROR_TYPE_ICON,  new Byte(JRImage.ON_ERROR_TYPE_ICON));
			onErrorTypeMap.put(new Byte(JRImage.ON_ERROR_TYPE_ERROR), ON_ERROR_TYPE_ERROR);
			onErrorTypeMap.put(new Byte(JRImage.ON_ERROR_TYPE_BLANK), ON_ERROR_TYPE_BLANK);
			onErrorTypeMap.put(new Byte(JRImage.ON_ERROR_TYPE_ICON),  ON_ERROR_TYPE_ICON);
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
			stretchTypeMap = new HashMap(6);
			stretchTypeMap.put(STRETCH_TYPE_NO_STRETCH,                 new Byte(JRElement.STRETCH_TYPE_NO_STRETCH));
			stretchTypeMap.put(STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT, new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT));
			stretchTypeMap.put(STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT,    new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT));
			stretchTypeMap.put(new Byte(JRElement.STRETCH_TYPE_NO_STRETCH),                 STRETCH_TYPE_NO_STRETCH);
			stretchTypeMap.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT), STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT);
			stretchTypeMap.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT),    STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT);
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
			lineStyleMap = new HashMap(4);
			lineStyleMap.put(LINE_STYLE_SOLID,  new Byte(JRPen.LINE_STYLE_SOLID));
			lineStyleMap.put(LINE_STYLE_DASHED, new Byte(JRPen.LINE_STYLE_DASHED));
			lineStyleMap.put(LINE_STYLE_DOTTED, new Byte(JRPen.LINE_STYLE_DOTTED));
			lineStyleMap.put(lINE_STYLE_DOUBLE, new Byte(JRPen.LINE_STYLE_DOUBLE));
			lineStyleMap.put(new Byte(JRPen.LINE_STYLE_SOLID),  LINE_STYLE_SOLID);
			lineStyleMap.put(new Byte(JRPen.LINE_STYLE_DASHED), LINE_STYLE_DASHED);
			lineStyleMap.put(new Byte(JRPen.LINE_STYLE_DOTTED), LINE_STYLE_DOTTED);
			lineStyleMap.put(new Byte(JRPen.LINE_STYLE_DOUBLE), lINE_STYLE_DOUBLE);
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
			penMap = new HashMap(10);
			penMap.put(PEN_NONE,     new Byte(JRGraphicElement.PEN_NONE));
			penMap.put(PEN_THIN,     new Byte(JRGraphicElement.PEN_THIN));
			penMap.put(PEN_1_POINT,  new Byte(JRGraphicElement.PEN_1_POINT));
			penMap.put(PEN_2_POINT,  new Byte(JRGraphicElement.PEN_2_POINT));
			penMap.put(PEN_4_POINT,  new Byte(JRGraphicElement.PEN_4_POINT));
			penMap.put(PEN_DOTTED,   new Byte(JRGraphicElement.PEN_DOTTED));
			penMap.put(new Byte(JRGraphicElement.PEN_NONE),     PEN_NONE);
			penMap.put(new Byte(JRGraphicElement.PEN_THIN),     PEN_THIN);
			penMap.put(new Byte(JRGraphicElement.PEN_1_POINT),  PEN_1_POINT);
			penMap.put(new Byte(JRGraphicElement.PEN_2_POINT),  PEN_2_POINT);
			penMap.put(new Byte(JRGraphicElement.PEN_4_POINT),  PEN_4_POINT);
			penMap.put(new Byte(JRGraphicElement.PEN_DOTTED),   PEN_DOTTED);
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
			fillMap = new HashMap(2);
			fillMap.put(FILL_SOLID, new Byte(JRGraphicElement.FILL_SOLID));
			fillMap.put(new Byte(JRGraphicElement.FILL_SOLID), FILL_SOLID);
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
			resetTypeMap = new HashMap(10);
			resetTypeMap.put(RESET_TYPE_NONE,   new Byte(JRVariable.RESET_TYPE_NONE));
			resetTypeMap.put(RESET_TYPE_REPORT, new Byte(JRVariable.RESET_TYPE_REPORT));
			resetTypeMap.put(RESET_TYPE_PAGE,   new Byte(JRVariable.RESET_TYPE_PAGE));
			resetTypeMap.put(RESET_TYPE_COLUMN, new Byte(JRVariable.RESET_TYPE_COLUMN));
			resetTypeMap.put(RESET_TYPE_GROUP,  new Byte(JRVariable.RESET_TYPE_GROUP));
			resetTypeMap.put(new Byte(JRVariable.RESET_TYPE_NONE),   RESET_TYPE_NONE);
			resetTypeMap.put(new Byte(JRVariable.RESET_TYPE_REPORT), RESET_TYPE_REPORT);
			resetTypeMap.put(new Byte(JRVariable.RESET_TYPE_PAGE),   RESET_TYPE_PAGE);
			resetTypeMap.put(new Byte(JRVariable.RESET_TYPE_COLUMN), RESET_TYPE_COLUMN);
			resetTypeMap.put(new Byte(JRVariable.RESET_TYPE_GROUP),  RESET_TYPE_GROUP);
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
			calculationMap = new HashMap(18);
			calculationMap.put(CALCULATION_NOTHING,            new Byte(JRVariable.CALCULATION_NOTHING));
			calculationMap.put(CALCULATION_COUNT,              new Byte(JRVariable.CALCULATION_COUNT));
			calculationMap.put(CALCULATION_SUM,                new Byte(JRVariable.CALCULATION_SUM));
			calculationMap.put(CALCULATION_AVERAGE,            new Byte(JRVariable.CALCULATION_AVERAGE));
			calculationMap.put(CALCULATION_LOWEST,             new Byte(JRVariable.CALCULATION_LOWEST));
			calculationMap.put(CALCULATION_HIGHEST,            new Byte(JRVariable.CALCULATION_HIGHEST));
			calculationMap.put(CALCULATION_STANDARD_DEVIATION, new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION));
			calculationMap.put(CALCULATION_VARIANCE,           new Byte(JRVariable.CALCULATION_VARIANCE));
			calculationMap.put(CALCULATION_SYSTEM,             new Byte(JRVariable.CALCULATION_SYSTEM));
			calculationMap.put(CALCULATION_FIRST,              new Byte(JRVariable.CALCULATION_FIRST));
			calculationMap.put(CALCULATION_DISTINCT_COUNT,     new Byte(JRVariable.CALCULATION_DISTINCT_COUNT));
			calculationMap.put(new Byte(JRVariable.CALCULATION_NOTHING),            CALCULATION_NOTHING);
			calculationMap.put(new Byte(JRVariable.CALCULATION_COUNT),              CALCULATION_COUNT);
			calculationMap.put(new Byte(JRVariable.CALCULATION_SUM),                CALCULATION_SUM);
			calculationMap.put(new Byte(JRVariable.CALCULATION_AVERAGE),            CALCULATION_AVERAGE);
			calculationMap.put(new Byte(JRVariable.CALCULATION_LOWEST),             CALCULATION_LOWEST);
			calculationMap.put(new Byte(JRVariable.CALCULATION_HIGHEST),            CALCULATION_HIGHEST);
			calculationMap.put(new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION), CALCULATION_STANDARD_DEVIATION);
			calculationMap.put(new Byte(JRVariable.CALCULATION_VARIANCE),           CALCULATION_VARIANCE);
			calculationMap.put(new Byte(JRVariable.CALCULATION_SYSTEM),             CALCULATION_SYSTEM);
			calculationMap.put(new Byte(JRVariable.CALCULATION_FIRST),              CALCULATION_FIRST);
			calculationMap.put(new Byte(JRVariable.CALCULATION_DISTINCT_COUNT),     CALCULATION_DISTINCT_COUNT);
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
			printOrderMap = new HashMap(4);
			printOrderMap.put(PRINT_ORDER_VERTICAL,   new Byte(JRReport.PRINT_ORDER_VERTICAL));
			printOrderMap.put(PRINT_ORDER_HORIZONTAL, new Byte(JRReport.PRINT_ORDER_HORIZONTAL));
			printOrderMap.put(new Byte(JRReport.PRINT_ORDER_VERTICAL),   PRINT_ORDER_VERTICAL);
			printOrderMap.put(new Byte(JRReport.PRINT_ORDER_HORIZONTAL), PRINT_ORDER_HORIZONTAL);
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
			orientationMap = new HashMap(4);
			orientationMap.put(ORIENTATION_PORTRAIT,  new Byte(JRReport.ORIENTATION_PORTRAIT));
			orientationMap.put(ORIENTATION_LANDSCAPE, new Byte(JRReport.ORIENTATION_LANDSCAPE));
			orientationMap.put(new Byte(JRReport.ORIENTATION_PORTRAIT),  ORIENTATION_PORTRAIT);
			orientationMap.put(new Byte(JRReport.ORIENTATION_LANDSCAPE), ORIENTATION_LANDSCAPE);
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
			whenNoDataTypeMap = new HashMap(6);
			whenNoDataTypeMap.put(WHEN_NO_DATA_TYPE_NO_PAGES,               new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES));
			whenNoDataTypeMap.put(WHEN_NO_DATA_TYPE_BLANK_PAGE,             new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE));
			whenNoDataTypeMap.put(WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL, new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL));
			whenNoDataTypeMap.put(WHEN_NO_DATA_TYPE_NO_DATA_SECTION,        new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION));
			whenNoDataTypeMap.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES),               WHEN_NO_DATA_TYPE_NO_PAGES);
			whenNoDataTypeMap.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE),             WHEN_NO_DATA_TYPE_BLANK_PAGE);
			whenNoDataTypeMap.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL), WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			whenNoDataTypeMap.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION),        WHEN_NO_DATA_TYPE_NO_DATA_SECTION);
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
			evaluationTimeMap = new HashMap(10);
			evaluationTimeMap.put(EVALUATION_TIME_NOW,    new Byte(JRExpression.EVALUATION_TIME_NOW));
			evaluationTimeMap.put(EVALUATION_TIME_REPORT, new Byte(JRExpression.EVALUATION_TIME_REPORT));
			evaluationTimeMap.put(EVALUATION_TIME_PAGE,   new Byte(JRExpression.EVALUATION_TIME_PAGE));
			evaluationTimeMap.put(EVALUATION_TIME_COLUMN, new Byte(JRExpression.EVALUATION_TIME_COLUMN));
			evaluationTimeMap.put(EVALUATION_TIME_GROUP,  new Byte(JRExpression.EVALUATION_TIME_GROUP));
			evaluationTimeMap.put(EVALUATION_TIME_BAND,  new Byte(JRExpression.EVALUATION_TIME_BAND));
			evaluationTimeMap.put(EVALUATION_TIME_AUTO,  new Byte(JRExpression.EVALUATION_TIME_AUTO));
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_NOW),    EVALUATION_TIME_NOW);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_REPORT), EVALUATION_TIME_REPORT);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_PAGE),   EVALUATION_TIME_PAGE);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_COLUMN), EVALUATION_TIME_COLUMN);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_GROUP),  EVALUATION_TIME_GROUP);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_BAND),  EVALUATION_TIME_BAND);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_AUTO),  EVALUATION_TIME_AUTO);
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
	 * @deprecated {@link JRHyperlinkHelper JRHyperlinkHelper} should be used instead.
	 */
	public static Map getHyperlinkTypeMap()
	{
		if (hyperlinkTypeMap == null)
		{
			hyperlinkTypeMap = new HashMap(12);
			hyperlinkTypeMap.put(HYPERLINK_TYPE_NONE,          new Byte(JRHyperlink.HYPERLINK_TYPE_NONE));
			hyperlinkTypeMap.put(HYPERLINK_TYPE_REFERENCE,     new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE));
			hyperlinkTypeMap.put(HYPERLINK_TYPE_LOCAL_ANCHOR,  new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR));
			hyperlinkTypeMap.put(HYPERLINK_TYPE_LOCAL_PAGE,    new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE));
			hyperlinkTypeMap.put(HYPERLINK_TYPE_REMOTE_ANCHOR, new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR));
			hyperlinkTypeMap.put(HYPERLINK_TYPE_REMOTE_PAGE,   new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE));
			hyperlinkTypeMap.put(new Byte(JRHyperlink.HYPERLINK_TYPE_NONE),          HYPERLINK_TYPE_NONE);
			hyperlinkTypeMap.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE),     HYPERLINK_TYPE_REFERENCE);
			hyperlinkTypeMap.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR),  HYPERLINK_TYPE_LOCAL_ANCHOR);
			hyperlinkTypeMap.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE),    HYPERLINK_TYPE_LOCAL_PAGE);
			hyperlinkTypeMap.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR), HYPERLINK_TYPE_REMOTE_ANCHOR);
			hyperlinkTypeMap.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE),   HYPERLINK_TYPE_REMOTE_PAGE);
		}

		return hyperlinkTypeMap;
	}

	/**
	 *
	 */
	private static final String HYPERLINK_TARGET_SELF = "Self";
	private static final String HYPERLINK_TARGET_BLANK = "Blank";

	private static Map hyperlinkTargetMap = null;

	public static Map getHyperlinkTargetMap()
	{
		if (hyperlinkTargetMap == null)
		{
			hyperlinkTargetMap = new HashMap(4);
			hyperlinkTargetMap.put(HYPERLINK_TARGET_SELF,  new Byte(JRHyperlink.HYPERLINK_TARGET_SELF));
			hyperlinkTargetMap.put(HYPERLINK_TARGET_BLANK, new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK));
			hyperlinkTargetMap.put(new Byte(JRHyperlink.HYPERLINK_TARGET_SELF),  HYPERLINK_TARGET_SELF);
			hyperlinkTargetMap.put(new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK), HYPERLINK_TARGET_BLANK);
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
			chartEdgeMap = new HashMap(4);
			chartEdgeMap.put(EDGE_TOP,    new Byte(JRChart.EDGE_TOP));
			chartEdgeMap.put(EDGE_BOTTOM, new Byte(JRChart.EDGE_BOTTOM));
			chartEdgeMap.put(EDGE_LEFT,   new Byte(JRChart.EDGE_LEFT));
			chartEdgeMap.put(EDGE_RIGHT,  new Byte(JRChart.EDGE_RIGHT));
			chartEdgeMap.put(new Byte(JRChart.EDGE_TOP),    EDGE_TOP);
			chartEdgeMap.put(new Byte(JRChart.EDGE_BOTTOM), EDGE_BOTTOM);
			chartEdgeMap.put(new Byte(JRChart.EDGE_LEFT),   EDGE_LEFT);
			chartEdgeMap.put(new Byte(JRChart.EDGE_RIGHT),  EDGE_RIGHT);
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
			plotOrientationMap = new HashMap(4);
			plotOrientationMap.put(ORIENTATION_HORIZONTAL, PlotOrientation.HORIZONTAL);
			plotOrientationMap.put(ORIENTATION_VERTICAL,   PlotOrientation.VERTICAL);
			plotOrientationMap.put(PlotOrientation.HORIZONTAL, ORIENTATION_HORIZONTAL);
			plotOrientationMap.put(PlotOrientation.VERTICAL,   ORIENTATION_VERTICAL);
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
			sortOrderMap = new HashMap(4);
			sortOrderMap.put(SORT_ORDER_ASCENDING,  new Byte(JRSortField.SORT_ORDER_ASCENDING));
			sortOrderMap.put(SORT_ORDER_DESCENDING, new Byte(JRSortField.SORT_ORDER_DESCENDING));
			sortOrderMap.put(new Byte(JRSortField.SORT_ORDER_ASCENDING),  SORT_ORDER_ASCENDING);
			sortOrderMap.put(new Byte(JRSortField.SORT_ORDER_DESCENDING), SORT_ORDER_DESCENDING);
		}

		return sortOrderMap;
	}


	private static final String SCALE_ON_BOTH_AXES = "BothAxes";
	private static final String SCALE_ON_DOMAIN_AXIS = "DomainAxis";
	private static final String SCALE_ON_RANGE_AXIS = "RangeAxis";

	private static Map scaleTypeMap = null;

	public static Map  getScaleTypeMap(){
		if( scaleTypeMap == null ){
			scaleTypeMap = new HashMap( 6 );
			scaleTypeMap.put( SCALE_ON_BOTH_AXES, new Integer( XYBubbleRenderer.SCALE_ON_BOTH_AXES ));
			scaleTypeMap.put( SCALE_ON_DOMAIN_AXIS, new Integer( XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS ));
			scaleTypeMap.put( SCALE_ON_RANGE_AXIS, new Integer( XYBubbleRenderer.SCALE_ON_RANGE_AXIS ));
			scaleTypeMap.put( new Integer( XYBubbleRenderer.SCALE_ON_BOTH_AXES ), SCALE_ON_BOTH_AXES );
			scaleTypeMap.put( new Integer( XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS ), SCALE_ON_DOMAIN_AXIS );
			scaleTypeMap.put( new Integer( XYBubbleRenderer.SCALE_ON_RANGE_AXIS ), SCALE_ON_RANGE_AXIS );
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
			whenResourceMissingTypeMap = new HashMap();
			whenResourceMissingTypeMap.put(WHEN_RESOURCE_MISSING_TYPE_NULL, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL));
			whenResourceMissingTypeMap.put(WHEN_RESOURCE_MISSING_TYPE_EMPTY, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY));
			whenResourceMissingTypeMap.put(WHEN_RESOURCE_MISSING_TYPE_KEY, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY));
			whenResourceMissingTypeMap.put(WHEN_RESOURCE_MISSING_TYPE_ERROR, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR));
			whenResourceMissingTypeMap.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL), WHEN_RESOURCE_MISSING_TYPE_NULL);
			whenResourceMissingTypeMap.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY), WHEN_RESOURCE_MISSING_TYPE_EMPTY);
			whenResourceMissingTypeMap.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY), WHEN_RESOURCE_MISSING_TYPE_KEY);
			whenResourceMissingTypeMap.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR), WHEN_RESOURCE_MISSING_TYPE_ERROR);
		}

		return whenResourceMissingTypeMap;
	}


	/**
	 *
	 */
	private static final String METER_SHAPE_CHORD = "chord";
	private static final String METER_SHAPE_CIRCLE = "circle";
	private static final String METER_SHAPE_PIE = "pie";

	private static Map meterShapeMap = null;

	public static Map getMeterShapeMap()
	{
		if (meterShapeMap == null)
		{
			meterShapeMap = new HashMap();
			meterShapeMap.put(METER_SHAPE_CHORD, new Byte(JRMeterPlot.SHAPE_CHORD));
			meterShapeMap.put(METER_SHAPE_CIRCLE, new Byte(JRMeterPlot.SHAPE_CIRCLE));
			meterShapeMap.put(METER_SHAPE_PIE, new Byte(JRMeterPlot.SHAPE_PIE));
			meterShapeMap.put(new Byte(JRMeterPlot.SHAPE_CHORD), METER_SHAPE_CHORD);
			meterShapeMap.put(new Byte(JRMeterPlot.SHAPE_CIRCLE), METER_SHAPE_CIRCLE);
			meterShapeMap.put(new Byte(JRMeterPlot.SHAPE_PIE), METER_SHAPE_PIE);
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
			thermometerValueLocationMap = new HashMap();
			thermometerValueLocationMap.put(THERMOMETER_VALUE_LOCATION_NONE, new Byte(JRThermometerPlot.LOCATION_NONE));
			thermometerValueLocationMap.put(THERMOMETER_VALUE_LOCATION_LEFT, new Byte(JRThermometerPlot.LOCATION_LEFT));
			thermometerValueLocationMap.put(THERMOMETER_VALUE_LOCATION_RIGHT, new Byte(JRThermometerPlot.LOCATION_RIGHT));
			thermometerValueLocationMap.put(THERMOMETER_VALUE_LOCATION_BULB, new Byte(JRThermometerPlot.LOCATION_BULB));
			thermometerValueLocationMap.put(new Byte(JRThermometerPlot.LOCATION_NONE), THERMOMETER_VALUE_LOCATION_NONE);
			thermometerValueLocationMap.put(new Byte(JRThermometerPlot.LOCATION_LEFT), THERMOMETER_VALUE_LOCATION_LEFT);
			thermometerValueLocationMap.put(new Byte(JRThermometerPlot.LOCATION_RIGHT), THERMOMETER_VALUE_LOCATION_RIGHT);
			thermometerValueLocationMap.put(new Byte(JRThermometerPlot.LOCATION_BULB), THERMOMETER_VALUE_LOCATION_BULB);
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
			axisPositionMap = new HashMap();
			axisPositionMap.put(AXIS_POSITION_LEFT_OR_TOP, new Byte(JRChartAxis.POSITION_LEFT_OR_TOP));
			axisPositionMap.put(AXIS_POSITION_RIGHT_OR_BOTTOM, new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM));
			axisPositionMap.put(new Byte(JRChartAxis.POSITION_LEFT_OR_TOP), AXIS_POSITION_LEFT_OR_TOP);
			axisPositionMap.put(new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM), AXIS_POSITION_RIGHT_OR_BOTTOM);
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
			crosstabBucketOrderMap = new HashMap();
			crosstabBucketOrderMap.put(CROSSTAB_BUCKET_ORDER_ASCENDING, new Byte(BucketDefinition.ORDER_ASCENDING));
			crosstabBucketOrderMap.put(CROSSTAB_BUCKET_ORDER_DESCENDING, new Byte(BucketDefinition.ORDER_DESCENDING));
			crosstabBucketOrderMap.put(new Byte(BucketDefinition.ORDER_ASCENDING), CROSSTAB_BUCKET_ORDER_ASCENDING);
			crosstabBucketOrderMap.put(new Byte(BucketDefinition.ORDER_DESCENDING), CROSSTAB_BUCKET_ORDER_DESCENDING);
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
			crosstabPercentageMap = new HashMap();
			crosstabPercentageMap.put(CROSSTAB_PERCENTAGE_NONE, new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_NONE));
			crosstabPercentageMap.put(CROSSTAB_PERCENTAGE_GRAND_TOTAL, new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL));
			crosstabPercentageMap.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_NONE), CROSSTAB_PERCENTAGE_NONE);
			crosstabPercentageMap.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL), CROSSTAB_PERCENTAGE_GRAND_TOTAL);
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
			crosstabTotalPositionMap = new HashMap();
			crosstabTotalPositionMap.put(CROSSTAB_TOTAL_POSITION_NONE, new Byte(BucketDefinition.TOTAL_POSITION_NONE));
			crosstabTotalPositionMap.put(CROSSTAB_TOTAL_POSITION_START, new Byte(BucketDefinition.TOTAL_POSITION_START));
			crosstabTotalPositionMap.put(CROSSTAB_TOTAL_POSITION_END, new Byte(BucketDefinition.TOTAL_POSITION_END));
			crosstabTotalPositionMap.put(new Byte(BucketDefinition.TOTAL_POSITION_NONE), CROSSTAB_TOTAL_POSITION_NONE);
			crosstabTotalPositionMap.put(new Byte(BucketDefinition.TOTAL_POSITION_START), CROSSTAB_TOTAL_POSITION_START);
			crosstabTotalPositionMap.put(new Byte(BucketDefinition.TOTAL_POSITION_END), CROSSTAB_TOTAL_POSITION_END);
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
			crosstabRowPositionMap = new HashMap();
			crosstabRowPositionMap.put(CROSSTAB_ROW_POSITION_TOP, new Byte(JRCellContents.POSITION_Y_TOP));
			crosstabRowPositionMap.put(CROSSTAB_ROW_POSITION_MIDDLE, new Byte(JRCellContents.POSITION_Y_MIDDLE));
			crosstabRowPositionMap.put(CROSSTAB_ROW_POSITION_BOTTOM, new Byte(JRCellContents.POSITION_Y_BOTTOM));
			crosstabRowPositionMap.put(CROSSTAB_ROW_POSITION_STRETCH, new Byte(JRCellContents.POSITION_Y_STRETCH));
			crosstabRowPositionMap.put(new Byte(JRCellContents.POSITION_Y_TOP), CROSSTAB_ROW_POSITION_TOP);
			crosstabRowPositionMap.put(new Byte(JRCellContents.POSITION_Y_MIDDLE), CROSSTAB_ROW_POSITION_MIDDLE);
			crosstabRowPositionMap.put(new Byte(JRCellContents.POSITION_Y_BOTTOM), CROSSTAB_ROW_POSITION_BOTTOM);
			crosstabRowPositionMap.put(new Byte(JRCellContents.POSITION_Y_STRETCH), CROSSTAB_ROW_POSITION_STRETCH);
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
			crosstabColumnPositionMap = new HashMap();
			crosstabColumnPositionMap.put(CROSSTAB_COLUMN_POSITION_LEFT, new Byte(JRCellContents.POSITION_X_LEFT));
			crosstabColumnPositionMap.put(CROSSTAB_COLUMN_POSITION_CENTER, new Byte(JRCellContents.POSITION_X_CENTER));
			crosstabColumnPositionMap.put(CROSSTAB_COLUMN_POSITION_RIGHT, new Byte(JRCellContents.POSITION_X_RIGHT));
			crosstabColumnPositionMap.put(CROSSTAB_COLUMN_POSITION_STRETCH, new Byte(JRCellContents.POSITION_X_STRETCH));
			crosstabColumnPositionMap.put(new Byte(JRCellContents.POSITION_X_LEFT), CROSSTAB_COLUMN_POSITION_LEFT);
			crosstabColumnPositionMap.put(new Byte(JRCellContents.POSITION_X_CENTER), CROSSTAB_COLUMN_POSITION_CENTER);
			crosstabColumnPositionMap.put(new Byte(JRCellContents.POSITION_X_RIGHT), CROSSTAB_COLUMN_POSITION_RIGHT);
			crosstabColumnPositionMap.put(new Byte(JRCellContents.POSITION_X_STRETCH), CROSSTAB_COLUMN_POSITION_STRETCH);
		}

		return crosstabColumnPositionMap;
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
			bandTypeMap = new HashMap(24);
			bandTypeMap.put(UNKNOWN,          new Byte(JROrigin.UNKNOWN));
			bandTypeMap.put(BACKGROUND,       new Byte(JROrigin.BACKGROUND));
			bandTypeMap.put(TITLE,            new Byte(JROrigin.TITLE));
			bandTypeMap.put(PAGE_HEADER,      new Byte(JROrigin.PAGE_HEADER));
			bandTypeMap.put(COLUMN_HEADER,    new Byte(JROrigin.COLUMN_HEADER));
			bandTypeMap.put(GROUP_HEADER,     new Byte(JROrigin.GROUP_HEADER));
			bandTypeMap.put(DETAIL,           new Byte(JROrigin.DETAIL));
			bandTypeMap.put(GROUP_FOOTER,     new Byte(JROrigin.GROUP_FOOTER));
			bandTypeMap.put(COLUMN_FOOTER,    new Byte(JROrigin.COLUMN_FOOTER));
			bandTypeMap.put(PAGE_FOOTER,      new Byte(JROrigin.PAGE_FOOTER));
			bandTypeMap.put(LAST_PAGE_FOOTER, new Byte(JROrigin.LAST_PAGE_FOOTER));
			bandTypeMap.put(SUMMARY,          new Byte(JROrigin.SUMMARY));
			bandTypeMap.put(NO_DATA,          new Byte(JROrigin.NO_DATA));
			bandTypeMap.put(new Byte(JROrigin.UNKNOWN),          UNKNOWN);
			bandTypeMap.put(new Byte(JROrigin.BACKGROUND),       BACKGROUND);
			bandTypeMap.put(new Byte(JROrigin.TITLE),            TITLE);
			bandTypeMap.put(new Byte(JROrigin.PAGE_HEADER),      PAGE_HEADER);
			bandTypeMap.put(new Byte(JROrigin.COLUMN_HEADER),    COLUMN_HEADER);
			bandTypeMap.put(new Byte(JROrigin.GROUP_HEADER),     GROUP_HEADER);
			bandTypeMap.put(new Byte(JROrigin.DETAIL),           DETAIL);
			bandTypeMap.put(new Byte(JROrigin.GROUP_FOOTER),     GROUP_FOOTER);
			bandTypeMap.put(new Byte(JROrigin.COLUMN_FOOTER),    COLUMN_FOOTER);
			bandTypeMap.put(new Byte(JROrigin.PAGE_FOOTER),      PAGE_FOOTER);
			bandTypeMap.put(new Byte(JROrigin.LAST_PAGE_FOOTER), LAST_PAGE_FOOTER);
			bandTypeMap.put(new Byte(JROrigin.SUMMARY),          SUMMARY);
			bandTypeMap.put(new Byte(JROrigin.NO_DATA),          NO_DATA);
		}

		return bandTypeMap;
	}

	
	/**
	 *
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
	public static final String JASPERREPORT_PUBLIC_ID = "-//JasperReports//DTD JasperReport//EN";
	public static final String JASPERREPORT_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jasperreport.dtd";
	public static final String JASPERREPORT_DTD = "net/sf/jasperreports/engine/dtds/jasperreport.dtd";
	public static final String JASPERPRINT_PUBLIC_ID = "-//JasperReports//DTD JasperPrint//EN";
	public static final String JASPERPRINT_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jasperprint.dtd";
	public static final String JASPERPRINT_DTD = "net/sf/jasperreports/engine/dtds/jasperprint.dtd";

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

	public static final String ATTRIBUTE_isShowLegend = "isShowLegend";
	public static final String ATTRIBUTE_evaluationTime = "evaluationTime";
	public static final String ATTRIBUTE_evaluationGroup = "evaluationGroup";
	public static final String ATTRIBUTE_bookmarkLevel = "bookmarkLevel";
	public static final String ATTRIBUTE_customizerClass = "customizerClass";
	public static final String ATTRIBUTE_renderType = "renderType";

	/**
	 * JRChartAxisFormatFactory associated constants
	 */
	public static final String ELEMENT_axisFormat = "axisFormat";
	public static final String ELEMENT_labelFont = "labelFont";
	public static final String ELEMENT_tickLabelFont = "tickLabelFont";
	public static final String ATTRIBUTE_labelColor = "labelColor";
	public static final String ATTRIBUTE_tickLabelColor = "tickLabelColor";
	public static final String ATTRIBUTE_tickLabelMask = "tickLabelMask";
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
	public static final String ELEMENT_xAxisLabelExpression = "xAxisLabelExpression";
	public static final String ELEMENT_yAxisLabelExpression = "yAxisLabelExpression";
	public static final String ELEMENT_timeAxisLabelExpression = "timeAxisLabelExpression";

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

	public static final String ELEMENT_categorySeries = "categorySeries";
	public static final String ELEMENT_xyzSeries = "xyzSeries";
	public static final String ELEMENT_xySeries = "xySeries";
	public static final String ELEMENT_timeSeries = "timeSeries";
	public static final String ELEMENT_timePeriodSeries = "timePeriodSeries";

	public static final String ELEMENT_incrementWhenExpression = "incrementWhenExpression";
	public static final String ELEMENT_keyExpression = "keyExpression";
	public static final String ELEMENT_valueExpression = "valueExpression";
	public static final String ELEMENT_labelExpression = "labelExpression";
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

	/**
	 * JRHyperlinkFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkTooltipExpression = "hyperlinkTooltipExpression";
	public static final String ELEMENT_sectionHyperlink = "sectionHyperlink";
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

	/**
	 * JRLineFactory associated constants
	 */
	public static final String ELEMENT_line = "line";

	public static final String ATTRIBUTE_direction = "direction";

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

}
