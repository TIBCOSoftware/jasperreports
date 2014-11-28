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
package jcharts;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStringUtil;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillAxisDataset extends JRFillElementDataset
{

	private final AxisDataset dataset;
	
	private String label;
	private Double value;
	
	private List<String> labels;
	private List<Double> values;
	
	public FillAxisDataset(AxisDataset dataset,	JRFillObjectFactory factory)
	{
		super(dataset, factory);
		
		this.dataset = dataset;
	}

	protected void customEvaluate(JRCalculator calculator)
			throws JRExpressionEvalException
	{
		label = JRStringUtil.getString(calculator.evaluate(dataset.getLabelExpression()));
		value = (Double) calculator.evaluate(dataset.getValueExpression());
	}

	protected void customIncrement()
	{
		labels.add(label);
		values.add(value);
	}

	protected void customInitialize()
	{
		labels = new ArrayList<String>();
		values = new ArrayList<Double>();
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		AxisChartCompiler.collectExpressions(dataset, collector);
	}

	public List<String> getLabels()
	{
		return labels;
	}

	public List<Double> getValues()
	{
		return values;
	}

	protected void finishDataset()
	{
		//one last increment is required in certain cases
		increment();
	}

}
