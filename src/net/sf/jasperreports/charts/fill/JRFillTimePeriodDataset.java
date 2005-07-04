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

import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillTimePeriodDataset extends JRFillChartDataset implements JRTimePeriodDataset {
	
	/**
	 * 
	 */
	TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
	
	protected JRFillTimePeriodSeries[] timePeriodSeries = null;
	
	private boolean isIncremented = false;
	
	
	public JRFillTimePeriodDataset( JRTimePeriodDataset timePeriodDataset,
									JRFillObjectFactory factory ){
	
		super( timePeriodDataset, factory );
		
		JRTimePeriodSeries[] srcTimePeriodSeries = timePeriodDataset.getSeries();
		if( srcTimePeriodSeries != null && srcTimePeriodSeries.length > 0 ){
			timePeriodSeries = new JRFillTimePeriodSeries[ srcTimePeriodSeries.length ];
			for( int i = 0; i< timePeriodSeries.length; i++ ){
				timePeriodSeries[i] = (JRFillTimePeriodSeries)factory.getTimePeriodSeries( srcTimePeriodSeries[i] );
			}
		}
	}
	
	
	public JRTimePeriodSeries[] getSeries(){
		return timePeriodSeries;
	}
	
	protected void initialize(){
		dataset = new TimePeriodValuesCollection();
		isIncremented = false;
	}
	
	protected void evaluate( JRCalculator calculator ) throws JRExpressionEvalException {
		if( timePeriodSeries != null && timePeriodSeries.length > 0 ){
			for( int i = 0; i< timePeriodSeries.length; i++ ){
				timePeriodSeries[i].evaluate( calculator );
			}
		}
		isIncremented = false;
	}
	
	
	protected void increment(){
		if( timePeriodSeries != null && timePeriodSeries.length > 0 ){
			for( int i = 0; i < timePeriodSeries.length; i++ ){
				JRFillTimePeriodSeries crtTimePeriodSeries = timePeriodSeries[i];
				String seriesName = (String)crtTimePeriodSeries.getSeries();
				
				TimePeriodValues timePeriodValues = null;
				for( int j = 0; j<dataset.getSeriesCount(); j++ ){
					TimePeriodValues tmp = dataset.getSeries( j );
					if( tmp.getKey().equals( seriesName )){
						timePeriodValues = tmp;
					}
				}
				
				if( timePeriodValues == null ){
					timePeriodValues = new TimePeriodValues( seriesName );
					dataset.addSeries( timePeriodValues );
				}
				
				if( crtTimePeriodSeries.getStartDate() != null && crtTimePeriodSeries.getEndDate() != null ){
					timePeriodValues.add( new SimpleTimePeriod( crtTimePeriodSeries.getStartDate(), crtTimePeriodSeries.getEndDate() ),
										  crtTimePeriodSeries.getValue());
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

	/** 
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.TIMEPERIOD_DATASET;
	}	
	
}
