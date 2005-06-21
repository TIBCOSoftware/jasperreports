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

import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCategorySeries implements JRCategorySeries
{

	/**
	 *
	 */
	protected JRCategorySeries parent = null;

	private Comparable series = null;
	private Comparable category = null;
	private Number value = null;
	private String label = null;
	
	
	/**
	 *
	 */
	public JRFillCategorySeries(
		JRCategorySeries series, 
		JRFillObjectFactory factory
		)
	{
		factory.put(series, this);

		parent = series;
	}


	/**
	 *
	 */
	public JRExpression getSeriesExpression()
	{
		return ((JRCategorySeries)parent).getSeriesExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getCategoryExpression()
	{
		return ((JRCategorySeries)parent).getCategoryExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return ((JRCategorySeries)parent).getValueExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return ((JRCategorySeries)parent).getLabelExpression();
	}
	
	
	/**
	 *
	 */
	protected Comparable getSeries()
	{
		return series;
	}
		
	/**
	 *
	 */
	protected Comparable getCategory()
	{
		return category;
	}
		
	/**
	 *
	 */
	protected Number getValue()
	{
		return value;
	}
		
	/**
	 *
	 */
	protected String getLabel()
	{
		return label;
	}
	
	
	/**
	 *
	 */
	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		series = (Comparable)calculator.evaluate(getSeriesExpression()); 
		category = (Comparable)calculator.evaluate(getCategoryExpression()); 
		value = (Number)calculator.evaluate(getValueExpression());
		label = (String)calculator.evaluate(getLabelExpression());
	}

}
