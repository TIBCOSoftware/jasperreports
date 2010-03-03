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
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;



/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRApiConstants
{
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
