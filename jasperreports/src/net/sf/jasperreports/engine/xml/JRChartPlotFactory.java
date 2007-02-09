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

import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;

import org.jfree.chart.plot.PlotOrientation;
import org.xml.sax.Attributes;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRChartPlotFactory extends JRBaseFactory
{

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
	
	public static final String ATTRIBUTE_backcolor = "backcolor";
	public static final String ATTRIBUTE_orientation = "orientation";
	public static final String ATTRIBUTE_backgroundAlpha = "backgroundAlpha";
	public static final String ATTRIBUTE_foregroundAlpha = "foregroundAlpha";
	public static final String ATTRIBUTE_labelRotation = "labelRotation";
	
	public static final String ATTRIBUTE_color = "color";
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


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRChartPlot plot = (JRChartPlot) digester.peek();

		Color color = JRXmlConstants.getColor(atts.getValue(ATTRIBUTE_backcolor), Color.black);
		if (color != null)
		{
			plot.setBackcolor(color);
		}

		String orientation = atts.getValue(ATTRIBUTE_orientation);
		if (orientation != null && orientation.length() > 0)
			plot.setOrientation((PlotOrientation)JRXmlConstants.getPlotOrientationMap().get(orientation));

		String foregroundAlpha = atts.getValue(ATTRIBUTE_foregroundAlpha);
		if (foregroundAlpha != null && foregroundAlpha.length() > 0)
			plot.setForegroundAlpha(Float.valueOf(foregroundAlpha).floatValue());

		String backgroundAlpha = atts.getValue(ATTRIBUTE_backgroundAlpha);
		if (backgroundAlpha != null && backgroundAlpha.length() > 0)
			plot.setBackgroundAlpha(Float.valueOf(backgroundAlpha).floatValue());

		String labelRotation = atts.getValue(ATTRIBUTE_labelRotation);
		if (labelRotation != null && labelRotation.length() > 0)
			plot.setLabelRotation(Double.valueOf(labelRotation).doubleValue());

		return plot;
	}
	
	public static class JRSeriesColorFactory extends JRBaseFactory
	{
		public static final String ELEMENT_seriesColor = "seriesColor";
		
		public static final String ATTRIBUTE_seriesOrder = "seriesOrder";
		public static final String ATTRIBUTE_color = "color";
		
		public Object createObject(Attributes atts)
		{
			int seriesIndex = -1;
			Color color = null;
			
			String seriesNumber = atts.getValue(ATTRIBUTE_seriesOrder);
			if (seriesNumber != null && seriesNumber.length() > 0)
				seriesIndex = Integer.valueOf(seriesNumber).intValue();

			String colorName = atts.getValue(ATTRIBUTE_color);
			if (colorName != null && colorName.length() > 0)
				color = JRXmlConstants.getColor(colorName, null);
			
			return new JRBaseChartPlot.JRBaseSeriesColor(seriesIndex, color);
		}
	}
}
