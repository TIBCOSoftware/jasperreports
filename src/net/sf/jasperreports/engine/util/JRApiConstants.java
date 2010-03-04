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
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRVariable;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;



/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRApiConstants
{
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
	
	public static String getBooleanText(Boolean key)
	{
		return key == null 
			? null 
			: (key.booleanValue() ? "Boolean.TRUE" : "Boolean.FALSE");
	}
	
	
}
