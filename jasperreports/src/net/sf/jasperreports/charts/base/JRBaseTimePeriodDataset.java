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
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseTimePeriodDataset extends JRBaseChartDataset implements JRTimePeriodDataset {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private JRTimePeriodSeries[] timePeriodSeries = null;
	
	protected JRBaseTimePeriodDataset( JRTimePeriodDataset dataset ){
		super( dataset );
	}
	
	public JRBaseTimePeriodDataset( JRTimePeriodDataset dataset, JRBaseObjectFactory factory ){
		super( dataset, factory );
		
		JRTimePeriodSeries[] srcTimePeriodSeries = dataset.getSeries();
		
		if( srcTimePeriodSeries != null && srcTimePeriodSeries.length > 0 ){
			timePeriodSeries = new JRTimePeriodSeries[srcTimePeriodSeries.length];
			for( int i = 0; i < timePeriodSeries.length; i++ ){
				timePeriodSeries[i] = factory.getTimePeriodSeries( srcTimePeriodSeries[i] );
			}
		}
	}
	
	public JRTimePeriodSeries[] getSeries(){
		return timePeriodSeries;
	}

	/** 
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.TIMEPERIOD_DATASET;
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

}
