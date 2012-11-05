/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package jcharts;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRTemplateImage;
import net.sf.jasperreports.engine.fill.JRTemplatePrintImage;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

import org.jCharts.Chart;
import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AreaChartProperties;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.types.ChartType;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillAxisChart extends BaseFillComponent implements JRFillCloneable
{

	private final AxisChartComponent chart;
	private final FillAxisDataset dataset;
	
	private String legendLabel;
	
	public FillAxisChart(AxisChartComponent chart, JRFillObjectFactory factory)
	{
		this.chart = chart;
		this.dataset = new FillAxisDataset(chart.getDataset(), factory);
		factory.registerElementDataset(this.dataset);
	}

	protected boolean isEvaluateNow()
	{
		return chart.getEvaluationTime() == EvaluationTimeEnum.NOW;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		if (isEvaluateNow())
		{
			 evaluateChart(evaluation);
		}
	}

	protected void evaluateChart(byte evaluation) throws JRException
	{
		legendLabel = (String) fillContext.evaluate(chart.getLegendLabelExpression(), evaluation);
		
		dataset.evaluateDatasetRun(evaluation);
	}

	public JRPrintElement fill()
	{
		JRComponentElement element = fillContext.getComponentElement();
		JRTemplateImage templateImage = new JRTemplateImage(fillContext.getElementOrigin(), 
				fillContext.getDefaultStyleProvider());
		templateImage.setStyle(fillContext.getElementStyle());
		
		JRTemplatePrintImage image = new JRTemplatePrintImage(templateImage, elementId);
		image.setX(element.getX());
		image.setY(fillContext.getElementPrintY());
		image.setWidth(element.getWidth());
		image.setHeight(element.getHeight());

		if (isEvaluateNow())
		{
			copy(image);
		}
		else
		{
			fillContext.registerDelayedEvaluation(image, 
					chart.getEvaluationTime(), chart.getEvaluationGroup());
		}
		
		return image;
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		return FillPrepareResult.PRINT_NO_STRETCH;
	}

	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		throw new UnsupportedOperationException();
	}

	public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateChart(evaluation);
		copy((JRPrintImage) element);
	}

	protected void copy(JRPrintImage image)
	{
		dataset.finishDataset();
		
		List labelsList = dataset.getLabels();
		String[] labels = (String[]) labelsList.toArray(new String[labelsList.size()]);
		
		List valuesList = dataset.getValues();
		double[][] values = {new double[valuesList.size()]};
		for (int i = 0; i < values[0].length; i++)
		{
			values[0][i] = ((Double) valuesList.get(i)).doubleValue();
		}
		
		String[] legendLabels = {legendLabel};
		Paint[] paints = {chart.getAreaColor()};
		try
		{
			AreaChartProperties areaChartProperties = new AreaChartProperties();
			AxisChartDataSet axisChartDataSet = new AxisChartDataSet(values, legendLabels, 
					paints, ChartType.AREA, areaChartProperties);

			DataSeries dataSeries = new DataSeries(labels, null, null, null);
			dataSeries.addIAxisPlotDataSet(axisChartDataSet);

			ChartProperties chartProperties = new ChartProperties();
			AxisProperties axisProperties = new AxisProperties();
			LegendProperties legendProperties = new LegendProperties();

			JRComponentElement element = fillContext.getComponentElement();
			AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 
					element.getWidth(), element.getHeight());
			
			//creating an chart image because AxisChart objects fail on serialization
			BufferedImage img = getChartImage(axisChart);
			JRRenderable renderer = JRImageRenderer.getInstance(img, JRRenderable.IMAGE_TYPE_PNG, 
					OnErrorTypeEnum.ERROR);
			image.setRenderer(renderer);
		}
		catch (ChartDataException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected BufferedImage getChartImage(Chart chart) throws JRException
	{
		BufferedImage bufferedImage = new BufferedImage(
				chart.getImageWidth(), chart.getImageHeight(), 
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bufferedImage.createGraphics();
		chart.setGraphics2D(graphics);
		try
		{
			chart.render();
		}
		catch (ChartDataException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (PropertyException e)
		{
			throw new JRRuntimeException(e);
		}
		return bufferedImage;
	}

}
