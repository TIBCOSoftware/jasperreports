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
package net.sf.jasperreports.components.ofc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.Pair;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillBarDataset extends JRFillElementDataset
{
	
	private final BarDataset dataset;

	private List<FillBarSeries> seriesList;
	
	private Set<String> seriesKeys = new LinkedHashSet<String>();
	private Set<String> categories = new LinkedHashSet<String>();
	private Map<Pair<String, String>, Number> values = new LinkedHashMap<Pair<String, String>, Number>();
	
	public FillBarDataset(BarDataset dataset, JRFillObjectFactory factory)
	{
		super(dataset, factory);
		
		this.dataset = dataset;
		
		List<BarSeries> datasetSeries = dataset.getSeries();
		seriesList = new ArrayList<FillBarSeries>(datasetSeries.size());
		for (Iterator<BarSeries> it = datasetSeries.iterator(); it.hasNext();)
		{
			BarSeries series = it.next();
			FillBarSeries fillSeries = new FillBarSeries(series);
			seriesList.add(fillSeries);
		}
	}

	protected void customEvaluate(JRCalculator calculator)
			throws JRExpressionEvalException
	{
		for (Iterator<FillBarSeries> it = seriesList.iterator(); it.hasNext();)
		{
			FillBarSeries series = it.next();
			series.evaluate(calculator);
		}
	}

	protected void customIncrement()
	{
		for (Iterator<FillBarSeries> it = seriesList.iterator(); it.hasNext();)
		{
			FillBarSeries series = it.next();
			String seriesKey = series.getSeriesKey();
			String category = series.getCategory();
			Number value = series.getValue();
			
			seriesKeys.add(seriesKey);
			categories.add(category);
			values.put(new Pair<String, String>(seriesKey, category), value);
		}
	}

	protected void customInitialize()
	{
		seriesKeys.clear();
		categories.clear();
		values.clear();
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		BarChartCompiler.collectExpressions(dataset, collector);
	}

	public void finishDataset()
	{
		super.increment();
	}
	
	public Set<String> getSeriesKeys()
	{
		return seriesKeys;
	}
	
	public Set<String> getCategories()
	{
		return categories;
	}
	
	public Number getValue(String seriesKey, String category)
	{
		return values.get(new Pair<String, String>(seriesKey, category));
	}

}
