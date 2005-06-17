/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.*;


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
			verticalAlignMap.put(VERTICAL_ALIGN_TOP,    new Byte(JRTextElement.VERTICAL_ALIGN_TOP));
			verticalAlignMap.put(VERTICAL_ALIGN_MIDDLE, new Byte(JRTextElement.VERTICAL_ALIGN_MIDDLE));
			verticalAlignMap.put(VERTICAL_ALIGN_BOTTOM, new Byte(JRTextElement.VERTICAL_ALIGN_BOTTOM));
			verticalAlignMap.put(new Byte(JRTextElement.VERTICAL_ALIGN_TOP),    VERTICAL_ALIGN_TOP);
			verticalAlignMap.put(new Byte(JRTextElement.VERTICAL_ALIGN_MIDDLE), VERTICAL_ALIGN_MIDDLE);
			verticalAlignMap.put(new Byte(JRTextElement.VERTICAL_ALIGN_BOTTOM), VERTICAL_ALIGN_BOTTOM);
		}
		
		return verticalAlignMap;
	}

	/**
	 *
	 */
	private static final String ROTATION_NONE = "None";
	private static final String ROTATION_LEFT = "Left";
	private static final String ROTATION_RIGHT = "Right";

	private static Map rotationMap = null;

	public static Map getRotationMap()
	{
		if (rotationMap == null)
		{
			rotationMap = new HashMap(6);
			rotationMap.put(ROTATION_NONE,  new Byte(JRTextElement.ROTATION_NONE));
			rotationMap.put(ROTATION_LEFT,  new Byte(JRTextElement.ROTATION_LEFT));
			rotationMap.put(ROTATION_RIGHT, new Byte(JRTextElement.ROTATION_RIGHT));
			rotationMap.put(new Byte(JRTextElement.ROTATION_NONE),  ROTATION_NONE);
			rotationMap.put(new Byte(JRTextElement.ROTATION_LEFT),  ROTATION_LEFT);
			rotationMap.put(new Byte(JRTextElement.ROTATION_RIGHT), ROTATION_RIGHT);
		}
		
		return rotationMap;
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
			calculationMap.put(new Byte(JRVariable.CALCULATION_NOTHING),            CALCULATION_NOTHING);
			calculationMap.put(new Byte(JRVariable.CALCULATION_COUNT),              CALCULATION_COUNT);
			calculationMap.put(new Byte(JRVariable.CALCULATION_SUM),                CALCULATION_SUM);
			calculationMap.put(new Byte(JRVariable.CALCULATION_AVERAGE),            CALCULATION_AVERAGE);
			calculationMap.put(new Byte(JRVariable.CALCULATION_LOWEST),             CALCULATION_LOWEST);
			calculationMap.put(new Byte(JRVariable.CALCULATION_HIGHEST),            CALCULATION_HIGHEST);
			calculationMap.put(new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION), CALCULATION_STANDARD_DEVIATION);
			calculationMap.put(new Byte(JRVariable.CALCULATION_VARIANCE),           CALCULATION_VARIANCE);
			calculationMap.put(new Byte(JRVariable.CALCULATION_SYSTEM),             CALCULATION_SYSTEM);
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

	private static Map whenNoDataTypeMap = null;

	public static Map getWhenNoDataTypeMap()
	{
		if (whenNoDataTypeMap == null)
		{
			whenNoDataTypeMap = new HashMap(6);
			whenNoDataTypeMap.put(WHEN_NO_DATA_TYPE_NO_PAGES,               new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES));
			whenNoDataTypeMap.put(WHEN_NO_DATA_TYPE_BLANK_PAGE,             new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE));
			whenNoDataTypeMap.put(WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL, new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL));
			whenNoDataTypeMap.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES),               WHEN_NO_DATA_TYPE_NO_PAGES);
			whenNoDataTypeMap.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE),             WHEN_NO_DATA_TYPE_BLANK_PAGE);
			whenNoDataTypeMap.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL), WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
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
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_NOW),    EVALUATION_TIME_NOW);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_REPORT), EVALUATION_TIME_REPORT);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_PAGE),   EVALUATION_TIME_PAGE);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_COLUMN), EVALUATION_TIME_COLUMN);
			evaluationTimeMap.put(new Byte(JRExpression.EVALUATION_TIME_GROUP),  EVALUATION_TIME_GROUP);
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
    public static final String TITLE_POSITION_TOP = "top";
    public static final String TITLE_POSITION_BOTTOM = "bottom";
    public static final String TITLE_POSITION_LEFT = "left";
    public static final String TITLE_POSITION_RIGHT = "right";

    public static Map chartTitlePositionMap = null;

    public static Map getChartTitlePositionMap()
    {
        if (chartTitlePositionMap == null)
        {
            chartTitlePositionMap = new HashMap(4);
            chartTitlePositionMap.put(TITLE_POSITION_TOP, new Byte(JRChart.TITLE_POSITION_TOP));
            chartTitlePositionMap.put(TITLE_POSITION_BOTTOM, new Byte(JRChart.TITLE_POSITION_BOTTOM));
            chartTitlePositionMap.put(TITLE_POSITION_LEFT, new Byte(JRChart.TITLE_POSITION_LEFT));
            chartTitlePositionMap.put(TITLE_POSITION_RIGHT, new Byte(JRChart.TITLE_POSITION_RIGHT));
            chartTitlePositionMap.put(new Byte(JRChart.TITLE_POSITION_TOP), TITLE_POSITION_TOP);
            chartTitlePositionMap.put(new Byte(JRChart.TITLE_POSITION_BOTTOM), TITLE_POSITION_BOTTOM);
            chartTitlePositionMap.put(new Byte(JRChart.TITLE_POSITION_LEFT), TITLE_POSITION_LEFT);
            chartTitlePositionMap.put(new Byte(JRChart.TITLE_POSITION_RIGHT), TITLE_POSITION_RIGHT);
        }

        return chartTitlePositionMap;
    }
}