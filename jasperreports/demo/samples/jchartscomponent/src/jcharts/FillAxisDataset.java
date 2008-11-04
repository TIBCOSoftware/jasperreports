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
package jcharts;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillAxisDataset extends JRFillElementDataset
{

	private final AxisDataset dataset;
	
	private String label;
	private Double value;
	
	private List labels;
	private List values;
	
	public FillAxisDataset(AxisDataset dataset,	JRFillObjectFactory factory)
	{
		super(dataset, factory);
		
		this.dataset = dataset;
	}

	protected void customEvaluate(JRCalculator calculator)
			throws JRExpressionEvalException
	{
		label = (String) calculator.evaluate(dataset.getLabelExpression());
		value = (Double) calculator.evaluate(dataset.getValueExpression());
	}

	protected void customIncrement()
	{
		labels.add(label);
		values.add(value);
	}

	protected void customInitialize()
	{
		labels = new ArrayList();
		values = new ArrayList();
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		AxisChartCompiler.collectExpressions(dataset, collector);
	}

	public List getLabels()
	{
		return labels;
	}

	public List getValues()
	{
		return values;
	}

	protected void finishDataset()
	{
		//one last increment is required in certain cases
		increment();
	}

}
