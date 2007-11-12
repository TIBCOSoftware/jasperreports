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
	
	private List timeSeriesList = new ArrayList();
	private Class timePeriod = null;
	

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
	public List getSeriesList()
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
	public Class getTimePeriod() 
	{
		return timePeriod;
	}
	
	/**
	 * 
	 */
	public void setTimePeriod( Class timePeriod )
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
	public Object clone() throws CloneNotSupportedException 
	{
		JRDesignTimeSeriesDataset clone = (JRDesignTimeSeriesDataset)super.clone();
		
		if (timeSeriesList != null)
		{
			clone.timeSeriesList = new ArrayList(timeSeriesList.size());
			for(int i = 0; i < timeSeriesList.size(); i++)
			{
				clone.timeSeriesList.add(((JRTimeSeries)timeSeriesList.get(i)).clone());
			}
		}

		return clone;
	}
}
