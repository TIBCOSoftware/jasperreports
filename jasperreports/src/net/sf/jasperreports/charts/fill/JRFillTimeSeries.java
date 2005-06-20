/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

import java.util.Date;
import java.util.TimeZone;

import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillTimeSeries extends JRFillChartDataset implements JRTimeSeries
{

	/**
	 *
	 */
	private TimeSeries dataset = null;
	
	private Comparable series = null;
	private Date timePeriod = null;
	private Number value = null;
	private String label = null;
	
	private boolean isIncremented = false;
	
	
	/**
	 *
	 */
	public JRFillTimeSeries(
		JRTimeSeries timeSeries, 
		JRFillObjectFactory factory
		)
	{
		super(timeSeries, factory);
	}


	/**
	 *
	 */
	public JRExpression getSeriesExpression()
	{
		return ((JRTimeSeries)parent).getSeriesExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getTimePeriodExpression()
	{
		return ((JRTimeSeries)parent).getTimePeriodExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return ((JRTimeSeries)parent).getValueExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return ((JRTimeSeries)parent).getLabelExpression();
	}
	
	
	/**
	 *
	 */
	protected void initialize()
	{
		dataset = 
			new TimeSeries(
				"FIXME NOW",//(String)calculator.evaluate(getSeriesExpression()),
				Year.class//(Class)calculator.evaluate(getTimePeriodExpression())
				);
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		series = (Comparable)calculator.evaluate(getSeriesExpression()); 
		timePeriod = (Date)calculator.evaluate(getTimePeriodExpression()); 
		value = (Number)calculator.evaluate(getValueExpression());
		label = (String)calculator.evaluate(getLabelExpression());
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void increment()
	{
		if (timePeriod != null) dataset.addOrUpdate(RegularTimePeriod.createInstance(Year.class, timePeriod, TimeZone.getDefault()), value);//FIXME NOW verify if condifion
		isIncremented = true;
	}

	/**
	 *
	 */
	public Dataset getDataset()
	{
		if (isIncremented == false)
		{
			increment();
		}
		TimeSeriesCollection col = new TimeSeriesCollection();
		col.addSeries(dataset);
		return col;
	}

	
}
