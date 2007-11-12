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

import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
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
public class JRFillXyzSeries implements JRXyzSeries {
	
	JRXyzSeries parent = null;
	
	private Comparable series = null;
	private Number xValue = null;
	private Number yValue = null;
	private Number zValue = null;
	private JRPrintHyperlink itemHyperlink;
	
	public JRFillXyzSeries( JRXyzSeries xyzSeries, JRFillObjectFactory factory ){
		factory.put( xyzSeries, this );
		parent = xyzSeries;
	}
	
	public JRExpression getSeriesExpression(){
		return parent.getSeriesExpression();
	}
	
	public JRExpression getXValueExpression(){
		return parent.getXValueExpression();
	}
	
	public JRExpression getYValueExpression(){
		return parent.getYValueExpression();
	}
	
	public JRExpression getZValueExpression(){
		return parent.getZValueExpression();
	}
	
	
	protected Comparable getSeries(){
		return series;
	}
	
	protected Number getXValue(){
		return xValue;
	}
	
	protected Number getYValue(){
		return yValue;
	}
	
	protected Number getZValue(){
		return zValue;
	}
	
	protected JRPrintHyperlink getPrintItemHyperlink()
	{
		return itemHyperlink;
	}
	
	protected void evaluate( JRCalculator calculator ) throws JRExpressionEvalException {
		series = (Comparable)calculator.evaluate( getSeriesExpression() );
		xValue = (Number)calculator.evaluate( getXValueExpression() );
		yValue = (Number)calculator.evaluate( getYValueExpression() );
		zValue = (Number)calculator.evaluate( getZValueExpression() );
		
		if (hasItemHyperlinks())
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
	
	public boolean hasItemHyperlinks()
	{
		return !JRHyperlinkHelper.isEmpty(getItemHyperlink()); 
	}

	/**
	 *
	 */
	public Object clone() throws CloneNotSupportedException 
	{
		throw new CloneNotSupportedException();
	}
}
