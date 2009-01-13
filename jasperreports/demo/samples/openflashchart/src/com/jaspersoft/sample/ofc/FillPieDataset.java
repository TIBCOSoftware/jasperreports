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
package com.jaspersoft.sample.ofc;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillPieDataset extends JRFillElementDataset
{

	private final PieDataset dataset;
	
	private String key;
	private Number value;
	
	private List keys;
	private List values;
	
	public FillPieDataset(PieDataset dataset,	JRFillObjectFactory factory)
	{
		super(dataset, factory);
		
		this.dataset = dataset;
	}

	protected void customEvaluate(JRCalculator calculator)
			throws JRExpressionEvalException
	{
		key = (String) calculator.evaluate(dataset.getKeyExpression());
		value = (Number) calculator.evaluate(dataset.getValueExpression());
	}

	protected void customIncrement()
	{
		keys.add(key);
		values.add(value);
	}

	protected void customInitialize()
	{
		keys = new ArrayList();
		values = new ArrayList();
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		PieChartCompiler.collectExpressions(dataset, collector);
	}

	public void finishDataset()
	{
		super.increment();
	}
	
	public List getKeys()
	{
		return keys;
	}

	public List getValues()
	{
		return values;
	}

}
