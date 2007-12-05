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
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseTimeSeriesDataset extends JRBaseChartDataset implements JRTimeSeriesDataset, JRChangeEventsSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_TIME_PERIOD = "timePeriod";
	
	private JRTimeSeries[] timeSeries = null;
	private Class timePeriod;
	
	protected JRBaseTimeSeriesDataset( JRTimeSeriesDataset dataset ){
		super( dataset );
	}
	
	public JRBaseTimeSeriesDataset( JRTimeSeriesDataset dataset, JRBaseObjectFactory factory ){
		super( dataset, factory );
		
		timePeriod = dataset.getTimePeriod();
		JRTimeSeries[] srcTimeSeries = dataset.getSeries();
		
		if( srcTimeSeries != null && srcTimeSeries.length > 0 ){
			timeSeries = new JRTimeSeries[ srcTimeSeries.length ];
			for( int i = 0; i< timeSeries.length; i++ ){
				timeSeries[i] = factory.getTimeSeries( srcTimeSeries[i]);
			}
		}
	}
	
	public JRTimeSeries[] getSeries(){
		return timeSeries;
	}
	
	public Class getTimePeriod(){
		return timePeriod;
	}
	
	public void setTimePeriod( Class timePeriod ){
		Object old = this.timePeriod;
		this.timePeriod = timePeriod;
		getEventSupport().firePropertyChange(PROPERTY_TIME_PERIOD, old, this.timePeriod);
	}


	/** 
	 * 
	 */
	public byte getDatasetType() {
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
		JRBaseTimeSeriesDataset clone = (JRBaseTimeSeriesDataset)super.clone();
		
		if (timeSeries != null)
		{
			clone.timeSeries = new JRTimeSeries[timeSeries.length];
			for(int i = 0; i < timeSeries.length; i++)
			{
				timeSeries[i] = (JRTimeSeries)timeSeries[i].clone();
			}
		}
		
		return clone;
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

}
