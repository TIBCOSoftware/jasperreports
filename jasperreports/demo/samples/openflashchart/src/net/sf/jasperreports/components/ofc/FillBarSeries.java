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

import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: FillBarSeries.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class FillBarSeries
{
	
	private final BarSeries barSeries;
	private String series;
	private String category;
	private Number value;

	public FillBarSeries(BarSeries series)
	{
		this.barSeries = series;
	}

	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		series = (String) calculator.evaluate(barSeries.getSeriesExpression());
		category = (String) calculator.evaluate(barSeries.getCategoryExpression());
		value = (Number) calculator.evaluate(barSeries.getValueExpression());
	}
	
	/**
	 * @return the series
	 */
	public String getSeriesKey()
	{
		return series;
	}

	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @return the value
	 */
	public Number getValue()
	{
		return value;
	}
}
