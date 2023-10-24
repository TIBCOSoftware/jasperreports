/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.charts.base;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.type.TimePeriodEnum;
import net.sf.jasperreports.charts.util.ChartUtil;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public class JRBaseTimeSeriesDataset extends JRBaseChartDataset implements JRTimeSeriesDataset, JRChangeEventsSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_TIME_PERIOD = "timePeriod";
	
	private JRTimeSeries[] timeSeries;
	private TimePeriodEnum timePeriodValue;
	
	protected JRBaseTimeSeriesDataset( JRTimeSeriesDataset dataset ){
		super( dataset );
	}
	
	public JRBaseTimeSeriesDataset( JRTimeSeriesDataset dataset, JRBaseObjectFactory factory ){
		super( dataset, factory );
		
		timePeriodValue = dataset.getTimePeriodValue();
		JRTimeSeries[] srcTimeSeries = dataset.getSeries();
		
		if( srcTimeSeries != null && srcTimeSeries.length > 0 ){
			timeSeries = new JRTimeSeries[ srcTimeSeries.length ];
			for( int i = 0; i< timeSeries.length; i++ ){
				timeSeries[i] = factory.getTimeSeries( srcTimeSeries[i]);
			}
		}
	}
	
	@Override
	public JRTimeSeries[] getSeries(){
		return timeSeries;
	}
	
	/**
	 * @deprecated Replaced by {@link #getTimePeriod()}.
	 */
	@Override
	public Class<?> getTimePeriod(){
		return ChartUtil.getTimePeriod(timePeriodValue);
	}
	
	/**
	 * @deprecated Replaced by {@link #setTimePeriod(TimePeriodEnum)}.
	 */
	@Override
	public void setTimePeriod( Class<?> timePeriod ){
		setTimePeriod(ChartUtil.getTimePeriod(timePeriod));
	}

	
	@Override
	public TimePeriodEnum getTimePeriodValue(){
		return timePeriodValue;
	}
	
	@Override
	public void setTimePeriod( TimePeriodEnum timePeriodValue ){
		Object old = this.timePeriodValue;
		this.timePeriodValue = timePeriodValue;
		getEventSupport().firePropertyChange(PROPERTY_TIME_PERIOD, old, this.timePeriodValue);
	}


	@Override
	public byte getDatasetType() {
		return JRChartDataset.TIMESERIES_DATASET;
	}


	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	@Override
	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

	@Override
	public Object clone() 
	{
		JRBaseTimeSeriesDataset clone = (JRBaseTimeSeriesDataset)super.clone();
		clone.timeSeries = JRCloneUtils.cloneArray(timeSeries);
		clone.eventSupport = null;
		return clone;
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
	@Override
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
	
	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Class<?> timePeriod;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_6_21_0)
		{
			timePeriodValue = ChartUtil.getTimePeriod(timePeriod);
		}
	}
	
}
