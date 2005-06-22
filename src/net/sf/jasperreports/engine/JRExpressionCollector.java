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
package net.sf.jasperreports.engine;

import java.util.Collection;
import java.util.HashSet;

import net.sf.jasperreports.charts.*;
import net.sf.jasperreports.charts.JRAreaChart;
import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DChart;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarChart;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRIntervalXyDataset;
import net.sf.jasperreports.charts.JRLineChart;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DChart;
import net.sf.jasperreports.charts.JRPieChart;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRStackedBar3DChart;
import net.sf.jasperreports.charts.JRStackedBarChart;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRXyBarChart;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExpressionCollector
{

	
	/**
	 *
	 */
	private Collection expressions = new HashSet();


	/**
	 *
	 */
	private void addExpression(JRExpression expression)
	{
		if (expression != null)
		{
			expressions.add(expression);
		}
	}

	/**
	 *
	 */
	public Collection collect(JRReport report)
	{
		collect(report.getParameters());
		collect(report.getVariables());
		collect(report.getGroups());

		collect(report.getBackground());
		collect(report.getTitle());
		collect(report.getPageHeader());
		collect(report.getColumnHeader());
		collect(report.getDetail());
		collect(report.getColumnFooter());
		collect(report.getPageFooter());
		collect(report.getLastPageFooter());
		collect(report.getSummary());
		
		return expressions;
	}
		

	/**
	 *
	 */
	private void collect(JRParameter[] parameters)
	{
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				addExpression(parameters[i].getDefaultValueExpression());
			}
		}
	}


	/**
	 *
	 */
	private void collect(JRVariable[] variables)
	{
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				JRVariable variable = variables[i];
				addExpression(variable.getExpression());
				addExpression(variable.getInitialValueExpression());
			}
		}
	}


	/**
	 *
	 */
	private void collect(JRGroup[] groups)
	{
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				JRGroup group = groups[i];
				addExpression(group.getExpression());

				collect(group.getGroupHeader());
				collect(group.getGroupFooter());
			}
		}
	}


	/**
	 *
	 */
	private void collect(JRBand band)
	{
		if (band != null)
		{
			addExpression(band.getPrintWhenExpression());
	
			JRElement[] elements = band.getElements();
			if (elements != null && elements.length > 0)
			{
				for(int i = 0; i < elements.length; i++)
				{
					elements[i].collectExpressions(this);
				}
			}
		}
	}

	/**
	 *
	 */
	private void collectElement(JRElement element)
	{
		addExpression(element.getPrintWhenExpression());
	}

	/**
	 *
	 */
	private void collectAnchor(JRAnchor anchor)
	{
		addExpression(anchor.getAnchorNameExpression());
	}

	/**
	 *
	 */
	private void collectHyperlink(JRHyperlink hyperlink)
	{
		addExpression(hyperlink.getHyperlinkReferenceExpression());
		addExpression(hyperlink.getHyperlinkAnchorExpression());
		addExpression(hyperlink.getHyperlinkPageExpression());
	}

	/**
	 *
	 */
	public void collect(JRLine line)
	{
		collectElement(line);
	}

	/**
	 *
	 */
	public void collect(JRRectangle rectangle)
	{
		collectElement(rectangle);
	}

	/**
	 *
	 */
	public void collect(JREllipse ellipse)
	{
		collectElement(ellipse);
	}

	/**
	 *
	 */
	public void collect(JRImage image)
	{
		collectElement(image);
		addExpression(image.getExpression());
		collectAnchor(image);
		collectHyperlink(image);
	}

	/**
	 *
	 */
	public void collect(JRStaticText staticText)
	{
		collectElement(staticText);
	}

	/**
	 *
	 */
	public void collect(JRTextField textField)
	{
		collectElement(textField);
		addExpression(textField.getExpression());
		collectAnchor(textField);
		collectHyperlink(textField);
	}

	/**
	 *
	 */
	public void collect(JRSubreport subreport)
	{
		collectElement(subreport);
		addExpression(subreport.getParametersMapExpression());

		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int j = 0; j < parameters.length; j++)
			{
				addExpression(parameters[j].getExpression());
			}
		}

		addExpression(subreport.getConnectionExpression());
		addExpression(subreport.getDataSourceExpression());
		addExpression(subreport.getExpression());
	}

	/**
	 *
	 */
	private void collectChart(JRChart chart)
	{
		collectElement(chart);
		collectAnchor(chart);
		collectHyperlink(chart);
		
		addExpression(chart.getTitleExpression());
		addExpression(chart.getSubtitleExpression());
	}

	/**
	 *
	 */
	public void collect(JRPieChart pieChart)
	{
		collectChart(pieChart);
		collect((JRPieDataset)pieChart.getDataset());
	}

	/**
	 *
	 */
	private void collect(JRPieDataset pieDataset)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(pieDataset.getKeyExpression());
		addExpression(pieDataset.getValueExpression());
		addExpression(pieDataset.getLabelExpression());
	}

	/**
	 *
	 */
	private void collect(JRPiePlot piePlot)
	{
	}

	/**
	 *
	 */
	public void collect(JRPie3DChart pie3DChart)
	{
		collectElement(pie3DChart);
		collectAnchor(pie3DChart);
		collectHyperlink(pie3DChart);
		collectChart(pie3DChart);
		collect((JRPieDataset)pie3DChart.getDataset());
	}

	/**
	 *
	 */
	public void collect(JRBarChart barChart)
	{
		collectChart(barChart);
		collect((JRCategoryDataset)barChart.getDataset());
		collect((JRBarPlot)barChart.getPlot());
	}
	
	
	public void collect(JRBar3DChart barChart ){
		collectChart( barChart );
		collect((JRCategoryDataset)barChart.getDataset() );
		collect((JRBar3DPlot)barChart.getPlot() );
	}
	
	public void collect( JRLineChart lineChart ){
	    collectChart( lineChart );
	    collect( (JRCategoryDataset)lineChart.getDataset() );
	    collect( (JRLinePlot)lineChart.getPlot()  );
	}
	
	public void collect( JRAreaChart areaChart ){
	    collectChart( areaChart );
	    collect( (JRCategoryDataset)areaChart.getDataset() );
	    collect( (JRAreaPlot)areaChart.getPlot() ) ;
	}

	/**
	 *
	 */
	public void collect(JRStackedBarChart stackedBarChart)
	{
		collectChart(stackedBarChart);
		collect((JRCategoryDataset)stackedBarChart.getDataset());
		collect((JRBarPlot)stackedBarChart.getPlot());
	}

	/**
	 *
	 */
	public void collect(JRStackedBar3DChart stackedBar3DChart)
	{
		collectChart(stackedBar3DChart);
		collect((JRCategoryDataset)stackedBar3DChart.getDataset());
		collect((JRBar3DPlot)stackedBar3DChart.getPlot());
	}

	/**
	 *
	 */
	private void collect(JRCategoryDataset categoryDataset)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		JRCategorySeries[] categorySeries = categoryDataset.getSeries();
		if (categorySeries != null && categorySeries.length > 0)
		{
			for(int j = 0; j < categorySeries.length; j++)
			{
				collect(categorySeries[j]);
			}
		}
	}

	/**
	 *
	 */
	private void collect(JRIntervalXyDataset intervalXyDataset)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		JRTimeSeries[] timeSeries = intervalXyDataset.getSeries();
		if (timeSeries != null && timeSeries.length > 0)
		{
			for(int j = 0; j < timeSeries.length; j++)
			{
				collect(timeSeries[j]);
			}
		}
	}

	/**
	 *
	 */
	private void collect(JRCategorySeries categorySeries)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(categorySeries.getSeriesExpression());
		addExpression(categorySeries.getCategoryExpression());
		addExpression(categorySeries.getValueExpression());
		addExpression(categorySeries.getLabelExpression());
	}

	/**
	 *
	 */
	private void collect(JRBarPlot barPlot)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(barPlot.getCategoryAxisLabelExpression());
		addExpression(barPlot.getValueAxisLabelExpression());
	}
	
	
	private void collect(JRBar3DPlot barPlot)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(barPlot.getCategoryAxisLabelExpression());
		addExpression(barPlot.getValueAxisLabelExpression());
	}
	
	
	private void collect( JRLinePlot linePlot ){
	    addExpression( linePlot.getCategoryAxisLabelExpression() );
	    addExpression( linePlot.getValueAxisLabelExpression() );
	}
	
	private void collect( JRAreaPlot areaPlot ){
	    addExpression( areaPlot.getCategoryAxisLabelExpression() );
	    addExpression( areaPlot.getValueAxisLabelExpression() );
	}

	/**
	 *
	 */
	public void collect(JRXyBarChart xyBarChart)
	{
		collectChart(xyBarChart);
		collect((JRIntervalXyDataset)xyBarChart.getDataset());
		collect((JRBarPlot)xyBarChart.getPlot());
	}

	/**
	 *
	 */
	private void collect(JRTimeSeries timeSeries)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(timeSeries.getSeriesExpression());
		addExpression(timeSeries.getTimePeriodExpression());
		addExpression(timeSeries.getValueExpression());
		addExpression(timeSeries.getLabelExpression());
	}

	/**
	 *
	 */
	public void collect(JRHighLowChart highLowChart)
	{
		collectChart(highLowChart);
		collect((JRHighLowDataset)highLowChart.getDataset());
		collect((JRHighLowPlot)highLowChart.getPlot());
	}

	/**
	 *
	 */
	private void collect(JRHighLowPlot highLowPlot)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(highLowPlot.getTimeAxisLabelExpression());
		addExpression(highLowPlot.getValueAxisLabelExpression());
	}

	/**
	 *
	 */
	private void collect(JRHighLowDataset highLowDataset)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(highLowDataset.getSeriesExpression());
		addExpression(highLowDataset.getDateExpression());
		addExpression(highLowDataset.getHighExpression());
		addExpression(highLowDataset.getLowExpression());
		addExpression(highLowDataset.getOpenExpression());
		addExpression(highLowDataset.getCloseExpression());
	}

}
