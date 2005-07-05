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
package net.sf.jasperreports.charts.base;

import java.io.Serializable;

import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseTimePeriodSeries implements JRTimePeriodSeries, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID=608; 
	
	protected JRExpression seriesExpression;
	
	protected JRExpression startDateExpression;
	
	protected JRExpression endDateExpression;
	
	protected JRExpression valueExpression;
	
	protected JRExpression labelExpression;
	
	
	protected JRBaseTimePeriodSeries(){
	}
	
	public JRBaseTimePeriodSeries( JRTimePeriodSeries timePeriodSeries, JRBaseObjectFactory factory ){
		factory.put( timePeriodSeries, factory );
		
		seriesExpression = factory.getExpression( timePeriodSeries.getSeriesExpression() );
		startDateExpression = factory.getExpression( timePeriodSeries.getStartDateExpression() );
		endDateExpression = factory.getExpression( timePeriodSeries.getEndDateExpression() );
		valueExpression = factory.getExpression( timePeriodSeries.getValueExpression() );
		labelExpression = factory.getExpression( timePeriodSeries.getLabelExpression() );
	}
	
	public JRExpression getSeriesExpression(){
		return seriesExpression;
	}
	
	public JRExpression getStartDateExpression(){
		return startDateExpression;
	}
	
	public JRExpression getEndDateExpression(){
		return endDateExpression;
	}
	
	public JRExpression getValueExpression(){
		return valueExpression;
	}
	
	public JRExpression getLabelExpression(){
		return labelExpression;
	}
}
