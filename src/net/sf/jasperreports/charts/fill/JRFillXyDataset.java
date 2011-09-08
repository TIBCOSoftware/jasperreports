/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.util.XYDatasetLabelGenerator;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.Pair;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillXyDataset extends JRFillChartDataset implements JRXyDataset
{

	/**
	 *
	 */
	protected JRFillXySeries[] xySeries;

	private List<Comparable<?>> seriesNames;
	private Map<Comparable<?>, XYSeries> seriesMap;
	private Map<Comparable<?>, Map<Number, String>> labelsMap;
	
	private Map<Comparable<?>, Map<Pair, JRPrintHyperlink>> itemHyperlinks;
	
	
	/**
	 *
	 */
	public JRFillXyDataset(
		JRXyDataset xyDataset, 
		JRFillObjectFactory factory
		)
	{
		super(xyDataset, factory);

		/*   */
		JRXySeries[] srcXySeries = xyDataset.getSeries();
		if (srcXySeries != null && srcXySeries.length > 0)
		{
			xySeries = new JRFillXySeries[srcXySeries.length];
			for(int i = 0; i < xySeries.length; i++)
			{
				xySeries[i] = (JRFillXySeries)factory.getXySeries(srcXySeries[i]);
			}
		}
	}
	
	
	/**
	 *
	 */
	public JRXySeries[] getSeries()
	{
		return xySeries;
	}


	/**
	 *
	 */
	protected void customInitialize()
	{
		seriesNames = null;
		seriesMap = null;
		labelsMap = null;
		itemHyperlinks = null;
	}

	
	/**
	 *
	 */
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		if (xySeries != null && xySeries.length > 0)
		{
			for(int i = 0; i < xySeries.length; i++)
			{
				xySeries[i].evaluate(calculator);
			}
		}
	}

	
	/**
	 *
	 */
	protected void customIncrement()
	{
		if (xySeries != null && xySeries.length > 0)
		{
			if (seriesNames == null)
			{
				seriesNames = new ArrayList<Comparable<?>>();
				seriesMap = new HashMap<Comparable<?>, XYSeries>();
				labelsMap = new HashMap<Comparable<?>, Map<Number, String>>();
				itemHyperlinks = new HashMap<Comparable<?>, Map<Pair, JRPrintHyperlink>>();
			}

			for(int i = 0; i < xySeries.length; i++)
			{
				JRFillXySeries crtXySeries = xySeries[i];

				Comparable<?> seriesName = crtXySeries.getSeries();
				XYSeries xySrs = seriesMap.get(seriesName);
				if (xySrs == null)
				{
					xySrs =  new XYSeries(seriesName);
					seriesNames.add(seriesName);
					seriesMap.put(seriesName, xySrs);
				}
				
				xySrs.addOrUpdate(
					crtXySeries.getXValue(), 
					crtXySeries.getYValue()
					);
				
				if (crtXySeries.getLabelExpression() != null)
				{
					Map<Number, String> seriesLabels = labelsMap.get(seriesName);
					if (seriesLabels == null)
					{
						seriesLabels = new HashMap<Number, String>();
						labelsMap.put(seriesName, seriesLabels);
					}
					
					seriesLabels.put(crtXySeries.getXValue(), crtXySeries.getLabel());
				}
				
				if (crtXySeries.hasItemHyperlinks())
				{
					Map<Pair, JRPrintHyperlink> seriesLinks = itemHyperlinks.get(seriesName);
					if (seriesLinks == null)
					{
						seriesLinks = new HashMap<Pair, JRPrintHyperlink>();
						itemHyperlinks.put(seriesName, seriesLinks);
					}
					Pair xyKey = new Pair(crtXySeries.getXValue(), crtXySeries.getYValue());
					seriesLinks.put(xyKey, crtXySeries.getPrintItemHyperlink());
				}
			}
		}
	}

	
	/**
	 *
	 */
	public Dataset getCustomDataset()
	{
		XYSeriesCollection dataset = new XYSeriesCollection();
		if (seriesNames != null)
		{
			for(int i = 0; i < seriesNames.size(); i++)
			{
				Comparable<?> seriesName = seriesNames.get(i);
				dataset.addSeries(seriesMap.get(seriesName));
			}
		}
		return dataset;
	}

	
	/**
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.XY_DATASET;
	}
	
	
	/**
	 * 
	 */
	public Object getLabelGenerator(){
		return new XYDatasetLabelGenerator(labelsMap);	
	}
	
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	
	public Map<Comparable<?>, Map<Pair, JRPrintHyperlink>> getItemHyperlinks()
	{
		return itemHyperlinks;
	}
	
	
	public boolean hasItemHyperlinks()
	{
		boolean foundLinks = false;
		if (xySeries != null && xySeries.length > 0)
		{
			for (int i = 0; i < xySeries.length && !foundLinks; i++)
			{
				JRFillXySeries serie = xySeries[i];
				foundLinks = serie.hasItemHyperlinks();
			}
		}
		return foundLinks;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}


}
