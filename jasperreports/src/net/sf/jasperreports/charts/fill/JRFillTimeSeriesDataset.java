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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.fill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.util.TimeSeriesLabelGenerator;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillTimeSeriesDataset extends JRFillChartDataset implements JRTimeSeriesDataset 
{

	/**
	 * 
	 */
	protected JRFillTimeSeries[] timeSeries = null;
	
	private List seriesNames = null;
	private Map seriesMap = null;
	private Map labelsMap = null;
	
	
	public JRFillTimeSeriesDataset(
		JRTimeSeriesDataset timeSeriesDataset, 
		JRFillObjectFactory factory 
		)
	{
		super( timeSeriesDataset, factory );
		
		JRTimeSeries[] srcTimeSeries = timeSeriesDataset.getSeries();
		if( srcTimeSeries != null && srcTimeSeries.length > 0)
		{
			timeSeries = new JRFillTimeSeries[srcTimeSeries.length];
			for (int i = 0; i < timeSeries.length; i++)
			{
				timeSeries[i] = (JRFillTimeSeries)factory.getTimeSeries(srcTimeSeries[i]);
			}
		}
	}
	
	public JRTimeSeries[] getSeries()
	{
		return timeSeries;
	}
	
	protected void customInitialize()
	{
		seriesNames = null;
		seriesMap = null;
		labelsMap = null;
	}
	
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException 
	{
		if(timeSeries != null && timeSeries.length > 0)
		{
			for (int i = 0; i < timeSeries.length; i++)
			{
				timeSeries[i].evaluate( calculator );
			}
		}
	}
	
	
	protected void customIncrement()
	{
		if (timeSeries != null && timeSeries.length > 0)
		{
			if (seriesNames == null)
			{
				seriesNames = new ArrayList();
				seriesMap = new HashMap();
				labelsMap = new HashMap();
			}

			for (int i = 0; i < timeSeries.length; i++)
			{
				JRFillTimeSeries crtTimeSeries = timeSeries[i];
				
				Comparable seriesName = crtTimeSeries.getSeries();
				TimeSeries series = (TimeSeries)seriesMap.get(seriesName);
				if(series == null)
				{
					series = new TimeSeries(seriesName.toString(), getTimePeriod());
					seriesNames.add(seriesName);
					seriesMap.put(seriesName, series);
				}
				
				RegularTimePeriod tp = 
					RegularTimePeriod.createInstance(
						getTimePeriod(), 
						crtTimeSeries.getTimePeriod(), 
						TimeZone.getDefault()
						);

				series.addOrUpdate(tp, crtTimeSeries.getValue());

				if (crtTimeSeries.getLabelExpression() != null)
				{
					Map seriesLabels = (Map)labelsMap.get(seriesName);
					if (seriesLabels == null)
					{
						seriesLabels = new HashMap();
						labelsMap.put(seriesName, seriesLabels);
					}
					
					seriesLabels.put(tp, crtTimeSeries.getLabel());
				}
			}
		}
	}
	
	public Dataset getCustomDataset()
	{
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		if (seriesNames != null)
		{
			for(int i = 0; i < seriesNames.size(); i++)
			{
				Comparable seriesName = (Comparable)seriesNames.get(i);
				dataset.addSeries((TimeSeries)seriesMap.get(seriesName));
			}
		}
		return dataset;
	}


	public Class getTimePeriod() {
		return ((JRTimeSeriesDataset)parent).getTimePeriod();
	}

	public void setTimePeriod(Class timePeriod) {	
	}

	/** 
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.TIMESERIES_DATASET;
	}
	
	
	public TimeSeriesLabelGenerator getLabelGenerator(){
		return new TimeSeriesLabelGenerator(labelsMap);
	}
	
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


}
