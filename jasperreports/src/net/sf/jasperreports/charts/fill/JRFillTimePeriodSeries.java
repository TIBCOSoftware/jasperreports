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
package net.sf.jasperreports.charts.fill;

import java.util.Date;

import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillHyperlinkHelper;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillTimePeriodSeries implements JRTimePeriodSeries {

	/**
	 * 
	 */
	protected JRTimePeriodSeries parent = null;
	
	private Comparable series = null;
	private Date startDate = null;
	private Date endDate = null;
	private Number value = null;
	private String label = null;
	private JRPrintHyperlink itemHyperlink;
	
	
	public JRFillTimePeriodSeries( JRTimePeriodSeries timePeriodSeries, JRFillObjectFactory factory  ){
		factory.put( timePeriodSeries, this );
		parent = timePeriodSeries;
	}
	
	
	public JRExpression getSeriesExpression(){
		return parent.getSeriesExpression(); 
	}
	
	public JRExpression getStartDateExpression(){
		return parent.getStartDateExpression();
	}
	
	public JRExpression getEndDateExpression(){
		return parent.getEndDateExpression();
	}
	
	public JRExpression getValueExpression(){
		return parent.getValueExpression();
	}
	
	public JRExpression getLabelExpression(){
		return parent.getLabelExpression();
	}
	
	protected Comparable getSeries(){
		return series;
	}
	
	protected Date getStartDate(){
		return startDate;
	}
	
	protected Date getEndDate(){
		return endDate;
	}
	
	protected Number getValue(){
		return value;
	}
	
	protected String getLabel(){
		return label;
	}
	
	protected void evaluate( JRCalculator calculator ) throws JRExpressionEvalException {
		series = (Comparable)calculator.evaluate( getSeriesExpression() );
		startDate = (Date)calculator.evaluate( getStartDateExpression() );
		endDate = (Date)calculator.evaluate( getEndDateExpression() );
		value= (Number)calculator.evaluate( getValueExpression() );
		label = (String)calculator.evaluate( getLabelExpression() );
		
		if (hasItemHyperlink())
		{
			evaluateItemHyperlink(calculator);
		}
	}


	protected void evaluateItemHyperlink(JRCalculator calculator) throws JRExpressionEvalException
	{
		try
		{
			itemHyperlink = JRFillHyperlinkHelper.evaluateHyperlink(getItemHyperlink(), calculator, JRExpression.EVALUATION_DEFAULT);
		}
		catch (JRExpressionEvalException e)
		{
			throw e;
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	public JRHyperlink getItemHyperlink()
	{
		return parent.getItemHyperlink();
	}


	public boolean hasItemHyperlink()
	{
		return getItemHyperlink() != null;
	}

	
	public JRPrintHyperlink getPrintItemHyperlink()
	{
		return itemHyperlink;
	}
	
	
}
