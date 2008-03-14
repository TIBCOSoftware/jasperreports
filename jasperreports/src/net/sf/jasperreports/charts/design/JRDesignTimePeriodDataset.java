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

import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignTimePeriodDataset extends JRDesignChartDataset implements JRTimePeriodDataset {
	
	/**
	 * 
	 */
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_TIME_PERIODS_SERIES = "timePeriodSeries";
	
	private List timePeriodSeriesList = new ArrayList();
	

	/**
	 * 
	 */
	public JRDesignTimePeriodDataset(JRChartDataset dataset)
	{
		super( dataset );
	}
	
	/**
	 * 
	 */
	public JRTimePeriodSeries[] getSeries()
	{
		JRTimePeriodSeries[] timePeriodSeriesArray = new JRTimePeriodSeries[timePeriodSeriesList.size()];
		timePeriodSeriesList.toArray(timePeriodSeriesArray);
		
		return timePeriodSeriesArray;
	}
	
	/**
	 * 
	 */
	public List getSeriesList()
	{
		return timePeriodSeriesList;
	}

	/**
	 * 
	 */
	public void addTimePeriodSeries( JRTimePeriodSeries timePeriodSeries ) 
	{
		timePeriodSeriesList.add(timePeriodSeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_TIME_PERIODS_SERIES, 
				timePeriodSeries, timePeriodSeriesList.size() - 1);
	}
	
	/**
	 * 
	 */
	public JRTimePeriodSeries removeTimePeriodSeries(JRTimePeriodSeries timePeriodSeries)
	{
		if( timePeriodSeries != null)
		{
			int idx = timePeriodSeriesList.indexOf(timePeriodSeries);
			if (idx >= 0)
			{
				timePeriodSeriesList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_TIME_PERIODS_SERIES, 
						timePeriodSeries, idx);
			}
		}
		
		return timePeriodSeries;
	}
	
	/** 
	 * 
	 */
	public byte getDatasetType() 
	{
		return JRChartDataset.TIMEPERIOD_DATASET;
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
		JRDesignTimePeriodDataset clone = (JRDesignTimePeriodDataset)super.clone();
		
		if (timePeriodSeriesList != null)
		{
			clone.timePeriodSeriesList = new ArrayList(timePeriodSeriesList.size());
			for(int i = 0; i < timePeriodSeriesList.size(); i++)
			{
				clone.timePeriodSeriesList.add(((JRTimePeriodSeries)timePeriodSeriesList.get(i)).clone());
			}
		}

		return clone;
	}
}
