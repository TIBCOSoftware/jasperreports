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


import java.awt.Color;
import java.awt.Paint;
import java.awt.image.BufferedImage;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AreaChartProperties;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.types.ChartType;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JChartsScriptlet extends JRDefaultScriptlet 
{


	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException 
	{
		try 
		{
			AreaChartProperties areaChartProperties = new AreaChartProperties();

			double[][] data = {{10, 15, 30, 53}, {6, 30, 10, 21}, {20, 25, 20, 8}};
			Paint[] paints = {new Color( 0, 255, 0, 100 ), new Color( 255, 0, 0, 100 ), new Color( 0, 0, 255, 100 )};
			String[] legendLabels = {"Games", "Events", "Players" };
			AxisChartDataSet axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.AREA, areaChartProperties);

			String[] axisLabels = {"January", "March", "May", "June"};
			DataSeries dataSeries = new DataSeries(axisLabels, "Months", "People", "Popular Events");
			dataSeries.addIAxisPlotDataSet(axisChartDataSet);

			ChartProperties chartProperties = new ChartProperties();
			AxisProperties axisProperties = new AxisProperties();
			axisProperties.setYAxisRoundValuesToNearest(0);
			LegendProperties legendProperties = new LegendProperties();

			AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 500, 350);

			BufferedImage bufferedImage = new BufferedImage(500, 350, BufferedImage.TYPE_INT_RGB);

			axisChart.setGraphics2D(bufferedImage.createGraphics());
			axisChart.render();

			super.setVariableValue("ChartImage", bufferedImage);
		}
		catch(ChartDataException chartDataException) 
		{
			throw new JRScriptletException(chartDataException);
		}
	}


}
