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
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public class JRDesignTimePeriodDataset extends JRDesignChartDataset implements JRTimePeriodDataset {
	
	/**
	 * 
	 */
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_TIME_PERIODS_SERIES = "timePeriodSeries";
	
	private List<JRTimePeriodSeries> timePeriodSeriesList = new ArrayList<JRTimePeriodSeries>();
	

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
	public List<JRTimePeriodSeries> getSeriesList()
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
	public void addTimePeriodSeries(int index, JRTimePeriodSeries timePeriodSeries ) 
	{
		timePeriodSeriesList.add(index, timePeriodSeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_TIME_PERIODS_SERIES, 
				timePeriodSeries, index);
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
		clone.timePeriodSeriesList = JRCloneUtils.cloneList(timePeriodSeriesList);
		return clone;
	}
}
