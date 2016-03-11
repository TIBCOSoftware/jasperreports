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
package net.sf.jasperreports.charts.fill;

import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultValueDataset;

/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRFillValueDataset extends JRFillChartDataset implements JRValueDataset
{

	private Number value;

	/**
	 *
	 */
	private DefaultValueDataset valueDataset = new DefaultValueDataset();


	/**
	 *
	 */
	public JRFillValueDataset(JRValueDataset valueDataset,
							  JRFillObjectFactory factory)
	{
		super(valueDataset, factory);
	}

	@Override
	public JRExpression getValueExpression()
	{
		return ((JRValueDataset)parent).getValueExpression();
	}


	@Override
	protected void customInitialize()
	{
		valueDataset = new DefaultValueDataset();
	}

	@Override
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		value = (Number)calculator.evaluate(getValueExpression());
	}

	@Override
	protected void customIncrement()
	{
		valueDataset.setValue(value);
	}

	@Override
	public Dataset getCustomDataset()
	{
		return valueDataset;
	}

	@Override
	public Object getLabelGenerator()
	{
		return null;
	}

	@Override
	public byte getDatasetType() {
		return JRChartDataset.VALUE_DATASET;
	}

	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	@Override
	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
