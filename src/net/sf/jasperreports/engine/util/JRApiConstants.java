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
package net.sf.jasperreports.engine.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRVariable;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;



/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRApiConstants
{
	private static Map rotationMap = null;

	public static Map getRotationMap()
	{
		if (rotationMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRTextElement.ROTATION_NONE),  		"JRTextElement.ROTATION_NONE");
			map.put(new Byte(JRTextElement.ROTATION_LEFT),  		"JRTextElement.ROTATION_LEFT");
			map.put(new Byte(JRTextElement.ROTATION_RIGHT), 		"JRTextElement.ROTATION_RIGHT");
			map.put(new Byte(JRTextElement.ROTATION_UPSIDE_DOWN), 	"JRTextElement.ROTATION_UPSIDE_DOWN");
			rotationMap = Collections.unmodifiableMap(map);
		}

		return rotationMap;
	}

	public static String getRotation(Byte key)
	{
		return (String)getRotationMap().get(key);
	}
	
	private static Map breakTypeMap = null;

	public static Map getBreakTypeMap()
	{
		if (breakTypeMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRBreak.TYPE_PAGE),   "JRBreak.TYPE_PAGE");
			map.put(new Byte(JRBreak.TYPE_COLUMN), "JRBreak.TYPE_COLUMN");
			breakTypeMap = Collections.unmodifiableMap(map);
		}

		return breakTypeMap;
	}

	public static String getBreakType(Byte key)
	{
		return (String)getBreakTypeMap().get(key);
	}
	
	private static Map runDirectionMap = null;

	public static Map getRunDirectionMap()
	{
		if (runDirectionMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRPrintText.RUN_DIRECTION_LTR), "JRPrintText.RUN_DIRECTION_LTR");
			map.put(new Byte(JRPrintText.RUN_DIRECTION_RTL), "JRPrintText.RUN_DIRECTION_RTL");
			runDirectionMap = Collections.unmodifiableMap(map);
		}

		return runDirectionMap;
	}

	public static String getRunDirection(Byte key)
	{
		return (String)getRunDirectionMap().get(key);
	}
	
	private static Map crosstabRunDirectionMap = null;

	public static Map getCrosstabRunDirectionMap()
	{
		if (crosstabRunDirectionMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRCrosstab.RUN_DIRECTION_LTR), "JRCrosstab.RUN_DIRECTION_LTR");
			map.put(new Byte(JRCrosstab.RUN_DIRECTION_RTL), "JRCrosstab.RUN_DIRECTION_RTL");
			crosstabRunDirectionMap = Collections.unmodifiableMap(map);
		}

		return crosstabRunDirectionMap;
	}

	public static String getCrosstabRunDirection(Byte key)
	{
		return (String)getCrosstabRunDirectionMap().get(key);
	}
	
	private static Map lineSpacingMap = null;

	public static Map getLineSpacingMap()
	{
		if (lineSpacingMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRTextElement.LINE_SPACING_SINGLE), "JRTextElement.LINE_SPACING_SINGLE");
			map.put(new Byte(JRTextElement.LINE_SPACING_1_1_2),  "JRTextElement.LINE_SPACING_1_1_2");
			map.put(new Byte(JRTextElement.LINE_SPACING_DOUBLE), "JRTextElement.LINE_SPACING_DOUBLE");
			lineSpacingMap = Collections.unmodifiableMap(map);
		}

		return lineSpacingMap;
	}

	public static String getLineSpacing(Byte key)
	{
		return (String)getLineSpacingMap().get(key);
	}
	
	private static Map directionMap = null;

	public static Map getDirectionMap()
	{
		if (directionMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRLine.DIRECTION_TOP_DOWN),  "JRLine.DIRECTION_TOP_DOWN");
			map.put(new Byte(JRLine.DIRECTION_BOTTOM_UP), "JRLine.DIRECTION_BOTTOM_UP");
			directionMap = Collections.unmodifiableMap(map);
		}

		return directionMap;
	}

	public static String getDirection(Byte key)
	{
		return (String)getDirectionMap().get(key);
	}
	
	private static Map scaleImageMap = null;

	public static Map getScaleImageMap()
	{
		if (scaleImageMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRImage.SCALE_IMAGE_CLIP),         "JRImage.SCALE_IMAGE_CLIP");
			map.put(new Byte(JRImage.SCALE_IMAGE_FILL_FRAME),   "JRImage.SCALE_IMAGE_FILL_FRAME");
			map.put(new Byte(JRImage.SCALE_IMAGE_RETAIN_SHAPE), "JRImage.SCALE_IMAGE_RETAIN_SHAPE");
			map.put(new Byte(JRImage.SCALE_IMAGE_REAL_HEIGHT), "JRImage.SCALE_IMAGE_REAL_HEIGHT");
			map.put(new Byte(JRImage.SCALE_IMAGE_REAL_SIZE), "JRImage.SCALE_IMAGE_REAL_SIZE");
			scaleImageMap = Collections.unmodifiableMap(map);
		}

		return scaleImageMap;
	}

	public static String getScaleImage(Byte key)
	{
		return (String)getScaleImageMap().get(key);
	}
	
	private static Map onErrorTypeMap = null;

	public static Map getOnErrorTypeMap()
	{
		if (onErrorTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRImage.ON_ERROR_TYPE_ERROR), "JRImage.ON_ERROR_TYPE_ERROR");
			map.put(new Byte(JRImage.ON_ERROR_TYPE_BLANK), "JRImage.ON_ERROR_TYPE_BLANK");
			map.put(new Byte(JRImage.ON_ERROR_TYPE_ICON),  "JRImage.ON_ERROR_TYPE_ICON");
			onErrorTypeMap = Collections.unmodifiableMap(map);
		}

		return onErrorTypeMap;
	}

	public static String getOnErrorType(Byte key)
	{
		return (String)getOnErrorTypeMap().get(key);
	}
	
	private static Map stretchTypeMap = null;

	public static Map getStretchTypeMap()
	{
		if (stretchTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRElement.STRETCH_TYPE_NO_STRETCH),                 "JRElement.STRETCH_TYPE_NO_STRETCH");
			map.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT), "JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT");
			map.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT),    "JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT");
			stretchTypeMap = Collections.unmodifiableMap(map);
		}

		return stretchTypeMap;
	}

	public static String getStretchType(Byte key)
	{
		return (String)getStretchTypeMap().get(key);
	}
	
	private static Map lineStyleMap = null;

	public static Map getLineStyleMap()
	{
		if (lineStyleMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRPen.LINE_STYLE_SOLID),  "JRPen.LINE_STYLE_SOLID");
			map.put(new Byte(JRPen.LINE_STYLE_DASHED), "JRPen.LINE_STYLE_DASHED");
			map.put(new Byte(JRPen.LINE_STYLE_DOTTED), "JRPen.LINE_STYLE_DOTTED");
			map.put(new Byte(JRPen.LINE_STYLE_DOUBLE), "JRPen.LINE_STYLE_DOUBLE");
			lineStyleMap = Collections.unmodifiableMap(map);
		}

		return lineStyleMap;
	}

	public static String getLineStyle(Byte key)
	{
		return (String)getLineStyleMap().get(key);
	}
	
	private static Map penMap = null;

	public static Map getPenMap()
	{
		if (penMap == null)
		{
			Map map = new HashMap(10);
			map.put(new Byte(JRGraphicElement.PEN_NONE),     "JRGraphicElement.PEN_NONE");
			map.put(new Byte(JRGraphicElement.PEN_THIN),     "JRGraphicElement.PEN_THIN");
			map.put(new Byte(JRGraphicElement.PEN_1_POINT),  "JRGraphicElement.PEN_1_POINT");
			map.put(new Byte(JRGraphicElement.PEN_2_POINT),  "JRGraphicElement.PEN_2_POINT");
			map.put(new Byte(JRGraphicElement.PEN_4_POINT),  "JRGraphicElement.PEN_4_POINT");
			map.put(new Byte(JRGraphicElement.PEN_DOTTED),   "JRGraphicElement.PEN_DOTTED");
			penMap = Collections.unmodifiableMap(map);
		}

		return penMap;
	}

	public static String getPen(Byte key)
	{
		return (String)getPenMap().get(key);
	}
	
	private static Map fillMap = null;

	public static Map getFillMap()
	{
		if (fillMap == null)
		{
			Map map = new HashMap(3);
			map.put(new Byte(JRGraphicElement.FILL_SOLID), "JRGraphicElement.FILL_SOLID");
			fillMap = Collections.unmodifiableMap(map);
		}

		return fillMap;
	}

	public static String getFill(Byte key)
	{
		return (String)getFillMap().get(key);
	}
	
	private static Map resetTypeMap = null;

	public static Map getResetTypeMap()
	{
		if (resetTypeMap == null)
		{
			Map map = new HashMap(10);
			map.put(new Byte(JRVariable.RESET_TYPE_NONE),   "JRVariable.RESET_TYPE_NONE");
			map.put(new Byte(JRVariable.RESET_TYPE_REPORT), "JRVariable.RESET_TYPE_REPORT");
			map.put(new Byte(JRVariable.RESET_TYPE_PAGE),   "JRVariable.RESET_TYPE_PAGE");
			map.put(new Byte(JRVariable.RESET_TYPE_COLUMN), "JRVariable.RESET_TYPE_COLUMN");
			map.put(new Byte(JRVariable.RESET_TYPE_GROUP),  "JRVariable.RESET_TYPE_GROUP");
			resetTypeMap = Collections.unmodifiableMap(map);
		}

		return resetTypeMap;
	}

	public static String getResetType(Byte key)
	{
		return (String)getResetTypeMap().get(key);
	}
	
	private static Map calculationMap = null;

	public static Map getCalculationMap()
	{
		if (calculationMap == null)
		{
			Map map = new HashMap(16);
			map.put(new Byte(JRVariable.CALCULATION_NOTHING),            "JRVariable.CALCULATION_NOTHING");
			map.put(new Byte(JRVariable.CALCULATION_COUNT),              "JRVariable.CALCULATION_COUNT");
			map.put(new Byte(JRVariable.CALCULATION_SUM),                "JRVariable.CALCULATION_SUM");
			map.put(new Byte(JRVariable.CALCULATION_AVERAGE),            "JRVariable.CALCULATION_AVERAGE");
			map.put(new Byte(JRVariable.CALCULATION_LOWEST),             "JRVariable.CALCULATION_LOWEST");
			map.put(new Byte(JRVariable.CALCULATION_HIGHEST),            "JRVariable.CALCULATION_HIGHEST");
			map.put(new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION), "JRVariable.CALCULATION_STANDARD_DEVIATION");
			map.put(new Byte(JRVariable.CALCULATION_VARIANCE),           "JRVariable.CALCULATION_VARIANCE");
			map.put(new Byte(JRVariable.CALCULATION_SYSTEM),             "JRVariable.CALCULATION_SYSTEM");
			map.put(new Byte(JRVariable.CALCULATION_FIRST),              "JRVariable.CALCULATION_FIRST");
			map.put(new Byte(JRVariable.CALCULATION_DISTINCT_COUNT),     "JRVariable.CALCULATION_DISTINCT_COUNT");
			calculationMap = Collections.unmodifiableMap(map);
		}

		return calculationMap;
	}

	public static String getCalculation(Byte key)
	{
		return (String)getCalculationMap().get(key);
	}
	
	private static Map printOrderMap = null;

	public static Map getPrintOrderMap()
	{
		if (printOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRReport.PRINT_ORDER_VERTICAL),   "JRReport.PRINT_ORDER_VERTICAL");
			map.put(new Byte(JRReport.PRINT_ORDER_HORIZONTAL), "JRReport.PRINT_ORDER_HORIZONTAL");
			printOrderMap = Collections.unmodifiableMap(map);
		}

		return printOrderMap;
	}

	public static String getPrintOrder(Byte key)
	{
		return (String)getPrintOrderMap().get(key);
	}
	
	private static Map orientationMap = null;

	public static Map getOrientationMap()
	{
		if (orientationMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRReport.ORIENTATION_PORTRAIT),  "JRReport.ORIENTATION_PORTRAIT");
			map.put(new Byte(JRReport.ORIENTATION_LANDSCAPE), "JRReport.ORIENTATION_LANDSCAPE");
			orientationMap = Collections.unmodifiableMap(map);
		}

		return orientationMap;
	}

	public static String getOrientation(Byte key)
	{
		return (String)getOrientationMap().get(key);
	}
	
	private static Map whenNoDataTypeMap = null;

	public static Map getWhenNoDataTypeMap()
	{
		if (whenNoDataTypeMap == null)
		{
			Map map = new HashMap(11);
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES),               "JRReport.WHEN_NO_DATA_TYPE_NO_PAGES");
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE),             "JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE");
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL), "JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL");
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION),        "JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION");
			whenNoDataTypeMap = Collections.unmodifiableMap(map);
		}

		return whenNoDataTypeMap;
	}

	public static String getWhenNoDataType(Byte key)
	{
		return (String)getWhenNoDataTypeMap().get(key);
	}
	
	private static Map evaluationTimeMap = null;

	public static Map getEvaluationTimeMap()
	{
		if (evaluationTimeMap == null)
		{
			Map map = new HashMap(19);
			map.put(new Byte(JRExpression.EVALUATION_TIME_NOW),    "JRExpression.EVALUATION_TIME_NOW");
			map.put(new Byte(JRExpression.EVALUATION_TIME_REPORT), "JRExpression.EVALUATION_TIME_REPORT");
			map.put(new Byte(JRExpression.EVALUATION_TIME_PAGE),   "JRExpression.EVALUATION_TIME_PAGE");
			map.put(new Byte(JRExpression.EVALUATION_TIME_COLUMN), "JRExpression.EVALUATION_TIME_COLUMN");
			map.put(new Byte(JRExpression.EVALUATION_TIME_GROUP),  "JRExpression.EVALUATION_TIME_GROUP");
			map.put(new Byte(JRExpression.EVALUATION_TIME_BAND),   "JRExpression.EVALUATION_TIME_BAND");
			map.put(new Byte(JRExpression.EVALUATION_TIME_AUTO),   "JRExpression.EVALUATION_TIME_AUTO");
			evaluationTimeMap = Collections.unmodifiableMap(map);
		}

		return evaluationTimeMap;
	}

	public static String getEvaluationTime(Byte key)
	{
		return (String)getEvaluationTimeMap().get(key);
	}
	
	private static Map hyperlinkTypeMap = null;


	/**
	 * @deprecated Replaced by {@link JRHyperlinkHelper#getHyperlinkType(String)}.
	 */
	public static Map getHyperlinkTypeMap()
	{
		if (hyperlinkTypeMap == null)
		{
			Map map = new HashMap(16);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_NONE),          "JRHyperlink.HYPERLINK_TYPE_NONE");
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE),     "JRHyperlink.HYPERLINK_TYPE_REFERENCE");
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR),  "JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR");
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE),    "JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE");
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR), "JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR");
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE),   "JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE");
			hyperlinkTypeMap = Collections.unmodifiableMap(map);
		}

		return hyperlinkTypeMap;
	}


	private static Map hyperlinkTargetMap = null;

	/**
	 * @deprecated Replaced by {@link JRHyperlinkHelper#getHyperlinkTarget(String)}.
	 */
	public static Map getHyperlinkTargetMap()
	{
		if (hyperlinkTargetMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_SELF),  "JRHyperlink.HYPERLINK_TARGET_SELF");
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK), "JRHyperlink.HYPERLINK_TARGET_BLANK");
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_PARENT), "JRHyperlink.HYPERLINK_TARGET_PARENT");
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_TOP), "JRHyperlink.HYPERLINK_TARGET_TOP");
			hyperlinkTargetMap = Collections.unmodifiableMap(map);
		}

		return hyperlinkTargetMap;
	}


	private static Map chartEdgeMap = null;

	public static Map getChartEdgeMap()
	{
		if (chartEdgeMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRChart.EDGE_TOP),    "JRChart.EDGE_TOP");
			map.put(new Byte(JRChart.EDGE_BOTTOM), "JRChart.EDGE_BOTTOM");
			map.put(new Byte(JRChart.EDGE_LEFT),   "JRChart.EDGE_LEFT");
			map.put(new Byte(JRChart.EDGE_RIGHT),  "JRChart.EDGE_RIGHT");
			chartEdgeMap = Collections.unmodifiableMap(map);
		}

		return chartEdgeMap;
	}

	public static String getChartEdge(Byte key)
	{
		return (String)getChartEdgeMap().get(key);
	}
	
	/**
	 * @deprecated Replaced by {@link #getChartEdgeMap()}.
	 */
	public static Map getChartTitlePositionMap()
	{
		return getChartEdgeMap();
	}
	
	private static Map plotOrientationMap = null;

	public static Map getPlotOrientationMap()
	{
		if (plotOrientationMap == null)
		{
			Map map = new HashMap(6);
			map.put(PlotOrientation.HORIZONTAL, "PlotOrientation.HORIZONTAL");
			map.put(PlotOrientation.VERTICAL,   "PlotOrientation.VERTICAL");
			plotOrientationMap = Collections.unmodifiableMap(map);
		}

		return plotOrientationMap;
	}

	public static String getPlotOrientation(Byte key)
	{
		return (String)getPlotOrientationMap().get(key);
	}
	
	private static Map sortOrderMap = null;

	public static Map getSortOrderMap()
	{
		if (sortOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRSortField.SORT_ORDER_ASCENDING),  "JRSortField.SORT_ORDER_ASCENDING");
			map.put(new Byte(JRSortField.SORT_ORDER_DESCENDING), "JRSortField.SORT_ORDER_DESCENDING");
			sortOrderMap = Collections.unmodifiableMap(map);
		}

		return sortOrderMap;
	}

	public static String getSortOrder(Byte key)
	{
		return (String)getSortOrderMap().get(key);
	}
	
	private static Map scaleTypeMap = null;

	public static Map  getScaleTypeMap(){
		if( scaleTypeMap == null ){
			Map map = new HashMap( 8 );
			map.put( new Integer( XYBubbleRenderer.SCALE_ON_BOTH_AXES ), 	"XYBubbleRenderer.SCALE_ON_BOTH_AXES" );
			map.put( new Integer( XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS ), 	"XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS" );
			map.put( new Integer( XYBubbleRenderer.SCALE_ON_RANGE_AXIS ), 	"XYBubbleRenderer.SCALE_ON_RANGE_AXI" );
			scaleTypeMap = Collections.unmodifiableMap(map);
		}

		return scaleTypeMap;
	}

	public static String getScaleType(Byte key)
	{
		return (String)getScaleTypeMap().get(key);
	}
	

/*
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
*/
	
	private static Map whenResourceMissingTypeMap = null;

	public static Map getWhenResourceMissingTypeMap()
	{
		if (whenResourceMissingTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL), 	"JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL");
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY), 	"JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY");
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY), 		"JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY");
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR), 	"JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR");
			whenResourceMissingTypeMap = Collections.unmodifiableMap(map);
		}

		return whenResourceMissingTypeMap;
	}
	
	public static String getWhenResourceMissingType(Byte key)
	{
		return (String)getWhenResourceMissingTypeMap().get(key);
	}
	
	private static Map meterShapeMap = null;

	public static Map getMeterShapeMap()
	{
		if (meterShapeMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRMeterPlot.SHAPE_CHORD), 	"JRMeterPlot.SHAPE_CHORD");
			map.put(new Byte(JRMeterPlot.SHAPE_CIRCLE), "JRMeterPlot.SHAPE_CIRCLE");
			map.put(new Byte(JRMeterPlot.SHAPE_PIE), 	"JRMeterPlot.SHAPE_PIE");
			map.put(new Byte(JRMeterPlot.SHAPE_DIAL), 	"JRMeterPlot.SHAPE_DIAL");
			meterShapeMap = Collections.unmodifiableMap(map);
		}

		return meterShapeMap;
	}

	public static String getMeterShape(Byte key)
	{
		return (String)getMeterShapeMap().get(key);
	}
	
	private static Map thermometerValueLocationMap = null;

	public static Map getThermometerValueLocationMap()
	{
		if (thermometerValueLocationMap == null)
		{
			Map map = new HashMap(11);
			map.put(new Byte(JRThermometerPlot.LOCATION_NONE), 	"JRThermometerPlot.LOCATION_NONE");
			map.put(new Byte(JRThermometerPlot.LOCATION_LEFT), 	"JRThermometerPlot.LOCATION_LEFT");
			map.put(new Byte(JRThermometerPlot.LOCATION_RIGHT), "JRThermometerPlot.LOCATION_RIGHT");
			map.put(new Byte(JRThermometerPlot.LOCATION_BULB), 	"JRThermometerPlot.LOCATION_BULB");
			thermometerValueLocationMap = Collections.unmodifiableMap(map);
		}

		return thermometerValueLocationMap;
	}

	public static String getThermometerValueLocation(Byte key)
	{
		return (String)getThermometerValueLocationMap().get(key);
	}
	
	private static Map axisPositionMap = null;

	public static Map getAxisPositionMap()
	{
		 if (axisPositionMap == null)
		 {
			Map map = new HashMap(6);
			map.put(new Byte(JRChartAxis.POSITION_LEFT_OR_TOP), 	"JRChartAxis.POSITION_LEFT_OR_TOP");
			map.put(new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM), "JRChartAxis.POSITION_RIGHT_OR_BOTTOM");
			axisPositionMap = Collections.unmodifiableMap(map);
		 }

		 return axisPositionMap;
	}

	public static String getAxisPosition(Byte key)
	{
		return (String)getAxisPositionMap().get(key);
	}
	
	private static Map crosstabBucketOrderMap = null;

	public static Map getCrosstabBucketOrderMap()
	{
		if (crosstabBucketOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(BucketDefinition.ORDER_ASCENDING), 	"BucketDefinition.ORDER_ASCENDING");
			map.put(new Byte(BucketDefinition.ORDER_DESCENDING), 	"BucketDefinition.ORDER_DESCENDING");
			crosstabBucketOrderMap = Collections.unmodifiableMap(map);
		}

		return crosstabBucketOrderMap;
	}

	public static String getCrosstabBucketOrder(Byte key)
	{
		return (String)getCrosstabBucketOrderMap().get(key);
	}
	
	private static Map crosstabPercentageMap = null;

	public static Map getCrosstabPercentageMap()
	{
		if (crosstabPercentageMap == null)
		{
			Map map = new HashMap(6);
			map.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_NONE), 		"JRCrosstabMeasure.CROSSTAB_PERCENTAGE_NONE");
			map.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL),"JRCrosstabMeasure.CROSSTAB_PERCENTAGE_GRAND_TOTAL");
			crosstabPercentageMap = Collections.unmodifiableMap(map);
		}

		return crosstabPercentageMap;
	}

	public static String getCrosstabPercentage(Byte key)
	{
		return (String)getCrosstabPercentageMap().get(key);
	}
	
	private static Map crosstabTotalPositionMap = null;

	public static Map getCrosstabTotalPositionMap()
	{
		if (crosstabTotalPositionMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_NONE), 	"BucketDefinition.TOTAL_POSITION_NONE");
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_START), 	"BucketDefinition.TOTAL_POSITION_START");
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_END), 		"BucketDefinition.TOTAL_POSITION_END");
			crosstabTotalPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabTotalPositionMap;
	}

	public static String getCrosstabTotalPosition(Byte key)
	{
		return (String)getCrosstabTotalPositionMap().get(key);
	}
	
	private static Map crosstabRowPositionMap = null;

	public static Map getCrosstabRowPositionMap()
	{
		if (crosstabRowPositionMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRCellContents.POSITION_Y_TOP), 	"JRCellContents.POSITION_Y_TOP");
			map.put(new Byte(JRCellContents.POSITION_Y_MIDDLE), "JRCellContents.POSITION_Y_MIDDLE");
			map.put(new Byte(JRCellContents.POSITION_Y_BOTTOM), "JRCellContents.POSITION_Y_BOTTOM");
			map.put(new Byte(JRCellContents.POSITION_Y_STRETCH),"JRCellContents.POSITION_Y_STRETCH");
			crosstabRowPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabRowPositionMap;
	}

	public static String getCrosstabRowPosition(Byte key)
	{
		return (String)getCrosstabRowPositionMap().get(key);
	}
	
	private static Map crosstabColumnPositionMap = null;


	public static Map getCrosstabColumnPositionMap()
	{
		if (crosstabColumnPositionMap == null)
		{
			Map map = new HashMap(11);
			map.put(new Byte(JRCellContents.POSITION_X_LEFT), 	"JRCellContents.POSITION_X_LEFT");
			map.put(new Byte(JRCellContents.POSITION_X_CENTER), "JRCellContents.POSITION_X_CENTER");
			map.put(new Byte(JRCellContents.POSITION_X_RIGHT), 	"JRCellContents.POSITION_X_RIGHT");
			map.put(new Byte(JRCellContents.POSITION_X_STRETCH),"JRCellContents.POSITION_X_STRETCH");
			crosstabColumnPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabColumnPositionMap;
	}

	public static String getCrosstabColumnPosition(Byte key)
	{
		return (String)getCrosstabColumnPositionMap().get(key);
	}
	
	private static Map splitTypeMap = null;

	public static Map getSplitTypeMap()
	{
		if (splitTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(JRBand.SPLIT_TYPE_STRETCH,   "JRBand.SPLIT_TYPE_STRETCH");
			map.put(JRBand.SPLIT_TYPE_PREVENT,   "JRBand.SPLIT_TYPE_PREVENT");
			map.put(JRBand.SPLIT_TYPE_IMMEDIATE, "JRBand.SPLIT_TYPE_IMMEDIATE");
			splitTypeMap = Collections.unmodifiableMap(map);
		}

		return splitTypeMap;
	}
	
	public static String getSplitType(Byte key)
	{
		return (String)getSplitTypeMap().get(key);
	}
	
	private static Map chunkTypeMap = null;
	
	public static Map getChunkTypeMap()
	{
		if (chunkTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(new Byte(JRExpressionChunk.TYPE_FIELD), 			"JRExpressionChunk.TYPE_FIELD");
			map.put(new Byte(JRExpressionChunk.TYPE_PARAMETER), 		"JRExpressionChunk.TYPE_PARAMETER");
			map.put(new Byte(JRExpressionChunk.TYPE_RESOURCE), 			"JRExpressionChunk.JRExpressionChunk.TYPE_RESOURCE");
			map.put(new Byte(JRExpressionChunk.TYPE_TEXT), 				"JRExpressionChunk.TYPE_TEXT");
			map.put(new Byte(JRExpressionChunk.TYPE_VARIABLE), 			"JRExpressionChunk.TYPE_VARIABLE");
			chunkTypeMap = Collections.unmodifiableMap(map);
		}

		return chunkTypeMap;
	}

	public static String getChunkType(Byte key)
	{
		return (String)getChunkTypeMap().get(key);
	}
	
	public static String getBooleanText(Boolean key)
	{
		return key == null 
			? null 
			: (key.booleanValue() ? "Boolean.TRUE" : "Boolean.FALSE");
	}
	
	
	/**
	 * @deprecated Replaced by {@link JRColorUtil#getColor(String, Color)}.
	 *
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
				if (JRApiConstants.getColorMap().containsKey(strColor))
				{
					color = (Color)JRApiConstants.getColorMap().get(strColor);
				}
				else
				{
					color = defaultColor;
				}
			}
		}

		return color;
	}
	*/
}
