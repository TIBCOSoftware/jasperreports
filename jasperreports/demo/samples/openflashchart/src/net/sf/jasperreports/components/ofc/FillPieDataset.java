/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.ofc;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: FillPieDataset.java 3031 2009-08-27 11:14:57Z teodord $
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
