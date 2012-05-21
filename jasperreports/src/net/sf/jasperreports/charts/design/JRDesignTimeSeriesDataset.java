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
package net.sf.jasperreports.charts.design;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$  
 */
public class JRDesignTimeSeriesDataset extends JRDesignChartDataset implements JRTimeSeriesDataset {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_TIME_PERIOD = "timePeriod";
	
	public static final String PROPERTY_TIME_SERIES = "timeSeries";
	
	private List<JRTimeSeries> timeSeriesList = new ArrayList<JRTimeSeries>();
	private Class<?> timePeriod;
	

	/**
	 * 
	 */
	public JRDesignTimeSeriesDataset( JRChartDataset dataset )
	{
		super( dataset );
	}

	/**
	 * 
	 */
	public JRTimeSeries[] getSeries()
	{
		JRTimeSeries[] timeSeriesArray = new JRTimeSeries[ timeSeriesList.size() ];
		timeSeriesList.toArray( timeSeriesArray );
		
		return timeSeriesArray;
	}
	
	/**
	 * 
	 */
	public List<JRTimeSeries> getSeriesList()
	{
		return timeSeriesList;
	}

	/**
	 * 
	 */
	public void addTimeSeries( JRTimeSeries timeSeries ) 
	{
		timeSeriesList.add( timeSeries );
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_TIME_SERIES, 
				timeSeries, timeSeriesList.size() - 1);
	}
	
	/**
	 * 
	 */
	public void addTimeSeries(int index, JRTimeSeries timeSeries ) 
	{
		timeSeriesList.add(index, timeSeries );
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_TIME_SERIES, 
				timeSeries, index);
	}
	
	/**
	 * 
	 */
	public JRTimeSeries removeTimeSeries( JRTimeSeries timeSeries ) 
	{
		if( timeSeries != null)
		{
			int idx = timeSeriesList.indexOf(timeSeries);
			if (idx >= 0)
			{
				timeSeriesList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_TIME_SERIES, timeSeries, idx);
			}
		}
		
		return timeSeries;
	}

	/**
	 * 
	 */
	public Class<?> getTimePeriod() 
	{
		return timePeriod;
	}
	
	/**
	 * 
	 */
	public void setTimePeriod( Class<?> timePeriod )
	{
		Object old = this.timePeriod;
		this.timePeriod = timePeriod;
		getEventSupport().firePropertyChange(PROPERTY_TIME_PERIOD, old, this.timePeriod);
	}

	/** 
	 * 
	 */
	public byte getDatasetType() 
	{
		return JRChartDataset.TIMESERIES_DATASET;
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRDesignTimeSeriesDataset clone = (JRDesignTimeSeriesDataset)super.clone();
		clone.timeSeriesList = JRCloneUtils.cloneList(timeSeriesList);
		return clone;
	}
}
