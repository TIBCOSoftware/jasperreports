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
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPieDataset;
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

		chart.getDataset().collectExpressions(this);
		chart.getPlot().collectExpressions(this);
	}

	/**
	 *
	 */
	public void collect(JRPieDataset pieDataset)
	{
		addExpression(pieDataset.getKeyExpression());
		addExpression(pieDataset.getValueExpression());
		addExpression(pieDataset.getLabelExpression());
	}

	/**
	 *
	 */
	public void collect(JRCategoryDataset categoryDataset)
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
	public void collect(JRXyDataset xyDataset)
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
	public void collect( JRTimeSeriesDataset timeSeriesDataset ){
		JRTimeSeries[] timeSeries = timeSeriesDataset.getSeries();
		if( timeSeries != null && timeSeries.length > 0 ){
			for( int i = 0; i <  timeSeries.length; i++ ){
				collect( timeSeries[i] );
			}
		}
	}
	
	/**
	 * 
	 */
	public void collect( JRTimePeriodDataset timePeriodDataset ){
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
	private void collect(JRXySeries xySeries)
	{
		addExpression(xySeries.getSeriesExpression());
		addExpression(xySeries.getXValueExpression());
		addExpression(xySeries.getYValueExpression());
		addExpression(xySeries.getLabelExpression());
	}

	/**
	 *
	 */
	private void collect(JRCategorySeries categorySeries)
	{
		addExpression(categorySeries.getSeriesExpression());
		addExpression(categorySeries.getCategoryExpression());
		addExpression(categorySeries.getValueExpression());
		addExpression(categorySeries.getLabelExpression());
	}

	/**
	 *
	 */
	public void collect(JRBarPlot barPlot)
	{
		addExpression(barPlot.getCategoryAxisLabelExpression());
		addExpression(barPlot.getValueAxisLabelExpression());
	}
	
	/**
	 *
	 */
	public void collect(JRBar3DPlot barPlot)
	{
		addExpression(barPlot.getCategoryAxisLabelExpression());
		addExpression(barPlot.getValueAxisLabelExpression());
	}
	
	/**
	 *
	 */
	public void collect( JRLinePlot linePlot ){
		addExpression( linePlot.getCategoryAxisLabelExpression() );
		addExpression( linePlot.getValueAxisLabelExpression() );
	}
	
	/**
	 *
	 */
	public void collect( JRTimeSeriesPlot timeSeriesPlot ){
		addExpression( timeSeriesPlot.getTimeAxisLabelExpression() );
		addExpression( timeSeriesPlot.getValueAxisLabelExpression() );
	}
	
	/**
	 *
	 */
	public void collect( JRScatterPlot scatterPlot ){
		addExpression( scatterPlot.getXAxisLabelExpression() );
		addExpression( scatterPlot.getYAxisLabelExpression() );
	}
	
	/**
	 *
	 */
	public void collect( JRAreaPlot areaPlot ){
		addExpression( areaPlot.getCategoryAxisLabelExpression() );
		addExpression( areaPlot.getValueAxisLabelExpression() );
	}

	/**
	 *
	 */
	private void collect(JRTimeSeries timeSeries)
	{
		addExpression(timeSeries.getSeriesExpression());
		addExpression(timeSeries.getTimePeriodExpression());
		addExpression(timeSeries.getValueExpression());
		addExpression(timeSeries.getLabelExpression());
	}
	
	/**
	 *
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
	public void collect(JRXyzDataset xyzDataset) {
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
	public void collect(JRBubblePlot bubblePlot) {
		addExpression(bubblePlot.getXAxisLabelExpression());
		addExpression(bubblePlot.getYAxisLabelExpression());
		
	}

	/**
	 *
	 */
	public void collect(JRHighLowPlot highLowPlot)
	{
		addExpression(highLowPlot.getTimeAxisLabelExpression());
		addExpression(highLowPlot.getValueAxisLabelExpression());
	}

	/**
	 *
	 */
	public void collect(JRHighLowDataset highLowDataset)
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
	public void collect(JRCandlestickPlot candlestickPlot)
	{
		addExpression(candlestickPlot.getTimeAxisLabelExpression());
		addExpression(candlestickPlot.getValueAxisLabelExpression());
	}

}
