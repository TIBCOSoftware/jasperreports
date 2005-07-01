/*
 * ============================================================================
 * GNU Lesser General Public License
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

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRIntervalXyDataset;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;


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
	public void collect(JRChart chart)
	{
		collectElement(chart);
		collectAnchor(chart);
		collectHyperlink(chart);
		
		addExpression(chart.getTitleExpression());
		addExpression(chart.getSubtitleExpression());

		switch(chart.getChartType()) {
			case JRChart.CHART_TYPE_AREA:
				collect( (JRCategoryDataset)chart.getDataset() );
				collect( (JRAreaPlot)chart.getPlot() ) ;
				break;
			case JRChart.CHART_TYPE_BAR:
				collect((JRCategoryDataset)chart.getDataset());
				collect((JRBarPlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_BAR3D:
				collect((JRCategoryDataset)chart.getDataset() );
				collect((JRBar3DPlot)chart.getPlot() );
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				collect((JRXyzDataset)chart.getDataset());
				collect((JRBubblePlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
				collect((JRHighLowDataset)chart.getDataset());
				collect((JRCandlestickPlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_HIGHLOW:
				collect((JRHighLowDataset)chart.getDataset());
				collect((JRHighLowPlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_LINE:
				collect( (JRCategoryDataset)chart.getDataset() );
				collect( (JRLinePlot)chart.getPlot()  );
				break;
			case JRChart.CHART_TYPE_PIE:
				collect((JRPieDataset)chart.getDataset());
				break;
			case JRChart.CHART_TYPE_PIE3D:
				collect((JRPieDataset)chart.getDataset());
				break;
			case JRChart.CHART_TYPE_SCATTER:
				collect( (JRXyDataset)chart.getDataset() );
				collect( (JRScatterPlot)chart.getPlot()  );
				break;
			case JRChart.CHART_TYPE_STACKEDBAR:
				collect((JRCategoryDataset)chart.getDataset());
				collect((JRBarPlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_STACKEDBAR3D:
				collect((JRCategoryDataset)chart.getDataset());
				collect((JRBar3DPlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				collect((JRTimeSeriesDataset)chart.getDataset() );
				collect((JRTimeSeriesPlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_XYAREA:
				collect( (JRXyDataset)chart.getDataset() );
				collect( (JRAreaPlot)chart.getPlot() ) ;
				break;
			case JRChart.CHART_TYPE_XYBAR:
				if( chart.getDataset() instanceof JRTimePeriodDataset ){	
					collect((JRTimePeriodDataset)chart.getDataset());
				}
				else {
					collect((JRTimeSeriesDataset)chart.getDataset());
				}
				collect((JRBarPlot)chart.getPlot());
				break;
			case JRChart.CHART_TYPE_XYLINE:
				collect( (JRXyDataset)chart.getDataset() );
				collect( (JRLinePlot)chart.getPlot()  );
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}
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
	private void collect(JRXyDataset xyDataset)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		JRXySeries[] xySeries = xyDataset.getSeries();
		if (xySeries != null && xySeries.length > 0)
		{
			for(int j = 0; j < xySeries.length; j++)
			{
				collect(xySeries[j]);
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
	private void collect( JRTimeSeriesDataset timeSeriesDataset ){
		JRTimeSeries[] timeSeries = timeSeriesDataset.getSeries();
		if( timeSeries != null && timeSeries.length > 0 ){
			for( int i = 0; i <  timeSeries.length; i++ ){
				collect( timeSeries[i] );
			}
		}
	}
	
	
	private void collect( JRTimePeriodDataset timePeriodDataset ){
		JRTimePeriodSeries[] timePeriodSeries = timePeriodDataset.getSeries();
		if( timePeriodSeries != null && timePeriodSeries.length > 0 ){
			for( int i = 0; i < timePeriodSeries.length; i++ ){
				collect( timePeriodSeries[i] );
			}
		}
	}

	
	/**
	 *
	 */
	private void collect(JRXySeries xySeries)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(xySeries.getSeriesExpression());
		addExpression(xySeries.getXValueExpression());
		addExpression(xySeries.getYValueExpression());
		addExpression(xySeries.getLabelExpression());
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
	
	private void collect( JRTimeSeriesPlot timeSeriesPlot ){
		addExpression( timeSeriesPlot.getTimeAxisLabelExpression() );
		addExpression( timeSeriesPlot.getValueAxisLabelExpression() );
	}
	
	private void collect( JRScatterPlot scatterPlot ){
		addExpression( scatterPlot.getXAxisLabelExpression() );
		addExpression( scatterPlot.getYAxisLabelExpression() );
	}
	
	private void collect( JRAreaPlot areaPlot ){
		addExpression( areaPlot.getCategoryAxisLabelExpression() );
		addExpression( areaPlot.getValueAxisLabelExpression() );
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
	 * @param timePeriodSeries
	 */
	private void collect( JRTimePeriodSeries timePeriodSeries ){
		addExpression( timePeriodSeries.getSeriesExpression() );
		addExpression( timePeriodSeries.getStartDateExpression() );
		addExpression( timePeriodSeries.getEndDateExpression() );
		addExpression( timePeriodSeries.getValueExpression() );
	}



	/**
	 *
	 */
	private void collect(JRXyzDataset xyzDataset) {
		JRXyzSeries[] xyzSeries = xyzDataset.getSeries();
		if (xyzSeries != null && xyzSeries.length > 0)
		{
			for(int j = 0; j < xyzSeries.length; j++)
			{
				collect(xyzSeries[j]);
			}
		}
		
	}

	/**
	 *
	 */
	private void collect(JRXyzSeries xyzSeries) {
		addExpression(xyzSeries.getSeriesExpression());
		addExpression(xyzSeries.getXValueExpression());
		addExpression(xyzSeries.getYValueExpression());
		addExpression(xyzSeries.getZValueExpression());
		
	}

	/**
	 *
	 */
	private void collect(JRBubblePlot bubblePlot) {
		addExpression(bubblePlot.getXAxisLabelExpression());
		addExpression(bubblePlot.getYAxisLabelExpression());
		
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
		addExpression(highLowDataset.getVolumeExpression());
	}


	/**
	 *
	 */
	private void collect(JRCandlestickPlot candlestickPlot)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(candlestickPlot.getTimeAxisLabelExpression());
		addExpression(candlestickPlot.getValueAxisLabelExpression());
	}

}
