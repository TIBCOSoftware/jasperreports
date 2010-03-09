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

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;



/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRApiConstants
{
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
