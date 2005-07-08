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
package net.sf.jasperreports.charts.fill;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillTimeSeriesDataset extends JRFillChartDataset implements JRTimeSeriesDataset {

	private TimeSeriesCollection dataset = new TimeSeriesCollection();
	
	protected JRFillTimeSeries[] timeSeries = null;
	protected Map[] labels = null;
	protected Class timeSeriesClass = null;
	
	
	private boolean isIncremented = false;
	
	public JRFillTimeSeriesDataset( JRTimeSeriesDataset  timeSeriesDataset, 
									JRFillObjectFactory factory ){
		
		super( timeSeriesDataset, factory );
		
		JRTimeSeries[] srcTimeSeries = timeSeriesDataset.getSeries();
		if( srcTimeSeries != null && srcTimeSeries.length > 0 ){
			timeSeries = new JRFillTimeSeries[ srcTimeSeries.length ];
			for( int i = 0; i < timeSeries.length; i++ ){
				timeSeries[i] = (JRFillTimeSeries)factory.getTimeSeries( srcTimeSeries[i] );
			}
		}
		timeSeriesClass = getTimePeriod();
	}
	
	public JRTimeSeries[] getSeries(){
		return timeSeries;
	}
	
	protected void initialize(){
		dataset = new TimeSeriesCollection();
		labels = new Map[ getSeries().length ]; //FIXME NOW test for nulls
		for( int i=0; i<labels.length; i++ ){
			if( getSeries()[i].getLabelExpression() != null ){
				labels[i] = new HashMap();
			}
		}
		isIncremented = false;
	}
	
	protected void evaluate( JRCalculator calculator ) throws JRExpressionEvalException {
		if( timeSeries != null && timeSeries.length > 0 ){
			for( int i = 0; i < timeSeries.length; i++ ){
				timeSeries[i].evaluate( calculator );
			}
		}
		isIncremented = false;
	}
	
	
	protected void increment(){
		if( timeSeries != null && timeSeries.length > 0 ){
			for( int i = 0; i < timeSeries.length; i++ ){
				JRFillTimeSeries crtTimeSeries = timeSeries[i];
				String seriesName = (String)crtTimeSeries.getSeries();
				TimeSeries timeSeries = dataset.getSeries(seriesName);
				if( timeSeries == null ){
					timeSeries = new TimeSeries(seriesName, getTimePeriod());
					dataset.addSeries( timeSeries );
				}
				
				if( crtTimeSeries.getTimePeriod() != null ){
					timeSeries.addOrUpdate( 
						RegularTimePeriod.createInstance( 
								getTimePeriod(),
								crtTimeSeries.getTimePeriod(),
								TimeZone.getDefault()
							),
						crtTimeSeries.getValue()
					);
					if( labels[i] != null ){
						RegularTimePeriod tp = RegularTimePeriod.createInstance( getTimePeriod(), crtTimeSeries.getTimePeriod(), TimeZone.getDefault() );
						labels[i].put( tp , crtTimeSeries.getLabel() );
					}
				}
			}
		}
		isIncremented = true;
	}
	
	public Dataset getDataset(){
		if( isIncremented == false ){
			increment();
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
	
	
	public StandardXYItemLabelGenerator getLabelGenerator(){
		return new TimeSeriesLabelGenerator( labels );
	}
	
	
	static class TimeSeriesLabelGenerator extends StandardXYItemLabelGenerator {
		private Map[] labels = null;
		
		public TimeSeriesLabelGenerator( Map[] labels ){
			this.labels = labels;
		}
		
		public String generateLabel( XYDataset dataset, int series, int item ){
			if( labels[series] != null ){
				return (String)labels[series].get(((TimeSeriesCollection)dataset).getSeries( series ).getTimePeriod( item ));
			}
			else {
				return super.generateLabel( dataset, series, item );
			}
			
			
		}
	}
}
