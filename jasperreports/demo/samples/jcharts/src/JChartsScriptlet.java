/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
