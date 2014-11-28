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
package net.sf.jasperreports.charts.base;

import java.io.Serializable;

import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public class JRBaseTimePeriodSeries implements JRTimePeriodSeries, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 608;// too late to replace this now
	
	protected JRExpression seriesExpression;
	
	protected JRExpression startDateExpression;
	
	protected JRExpression endDateExpression;
	
	protected JRExpression valueExpression;
	
	protected JRExpression labelExpression;
	
	protected JRHyperlink itemHyperlink;
	
	
	protected JRBaseTimePeriodSeries(){
	}
	
	public JRBaseTimePeriodSeries( JRTimePeriodSeries timePeriodSeries, JRBaseObjectFactory factory ){
		factory.put( timePeriodSeries, factory );
		
		seriesExpression = factory.getExpression( timePeriodSeries.getSeriesExpression() );
		startDateExpression = factory.getExpression( timePeriodSeries.getStartDateExpression() );
		endDateExpression = factory.getExpression( timePeriodSeries.getEndDateExpression() );
		valueExpression = factory.getExpression( timePeriodSeries.getValueExpression() );
		labelExpression = factory.getExpression( timePeriodSeries.getLabelExpression() );
		itemHyperlink = factory.getHyperlink(timePeriodSeries.getItemHyperlink());
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
	
	public JRHyperlink getItemHyperlink()
	{
		return itemHyperlink;
	}
	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseTimePeriodSeries clone = null;
		
		try
		{
			clone = (JRBaseTimePeriodSeries)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}

		clone.seriesExpression = JRCloneUtils.nullSafeClone(seriesExpression);
		clone.startDateExpression = JRCloneUtils.nullSafeClone(startDateExpression);
		clone.endDateExpression = JRCloneUtils.nullSafeClone(endDateExpression);
		clone.valueExpression = JRCloneUtils.nullSafeClone(valueExpression);
		clone.labelExpression = JRCloneUtils.nullSafeClone(labelExpression);
		clone.itemHyperlink = JRCloneUtils.nullSafeClone(itemHyperlink);
		
		return clone;
	}
}
