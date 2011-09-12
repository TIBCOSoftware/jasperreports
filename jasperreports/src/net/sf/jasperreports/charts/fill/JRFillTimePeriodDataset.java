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

import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.util.TimePeriodDatasetLabelGenerator;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;


/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillTimePeriodDataset extends JRFillChartDataset implements JRTimePeriodDataset
{

	/**
	 * 
	 */
	protected JRFillTimePeriodSeries[] timePeriodSeries;

	private List<Comparable<?>> seriesNames;
	private Map<Comparable<?>, TimePeriodValues> seriesMap;
	private Map<Comparable<?>, Map<TimePeriod, String>> labelsMap;
	private Map<Comparable<?>, Map<TimePeriod, JRPrintHyperlink>> itemHyperlinks;


	/**
	 * 
	 */
	public JRFillTimePeriodDataset(
		JRTimePeriodDataset timePeriodDataset,
		JRFillObjectFactory factory
		)
	{
		super(timePeriodDataset, factory);

		JRTimePeriodSeries[] srcTimePeriodSeries = timePeriodDataset.getSeries();
		if (srcTimePeriodSeries != null && srcTimePeriodSeries.length > 0)
		{
			timePeriodSeries = new JRFillTimePeriodSeries[srcTimePeriodSeries.length];
			for (int i = 0; i < timePeriodSeries.length; i++)
			{
				timePeriodSeries[i] = 
					(JRFillTimePeriodSeries)factory.getTimePeriodSeries(srcTimePeriodSeries[i]);
			}
		}
	}

	public JRTimePeriodSeries[] getSeries()
	{
		return timePeriodSeries;
	}

	protected void customInitialize()
	{
		seriesNames = null;
		seriesMap = null;
		labelsMap = null;
		itemHyperlinks = null;
	}

	protected void customEvaluate(JRCalculator calculator)
			throws JRExpressionEvalException
	{
		if (timePeriodSeries != null && timePeriodSeries.length > 0)
		{
			for (int i = 0; i < timePeriodSeries.length; i++)
			{
				timePeriodSeries[i].evaluate(calculator);
			}
		}
	}

	protected void customIncrement()
	{
		if (timePeriodSeries != null && timePeriodSeries.length > 0)
		{
			if (seriesNames == null)
			{
				seriesNames = new ArrayList<Comparable<?>>();
				seriesMap = new HashMap<Comparable<?>, TimePeriodValues>();
				labelsMap = new HashMap<Comparable<?>, Map<TimePeriod, String>>();
				itemHyperlinks = new HashMap<Comparable<?>, Map<TimePeriod, JRPrintHyperlink>>();
			}

			for (int i = 0; i < timePeriodSeries.length; i++)
			{
				JRFillTimePeriodSeries crtTimePeriodSeries = timePeriodSeries[i];

				Comparable<?> seriesName = crtTimePeriodSeries.getSeries();
				if (seriesName == null)
				{
					throw new JRRuntimeException("Time period series name is null.");
				}

				TimePeriodValues timePeriodValues = seriesMap.get(seriesName);
				if (timePeriodValues == null)
				{
					timePeriodValues = new TimePeriodValues(seriesName.toString());
					seriesNames.add(seriesName);
					seriesMap.put(seriesName, timePeriodValues);
				}

				SimpleTimePeriod stp = 
					new SimpleTimePeriod(
						crtTimePeriodSeries.getStartDate(), 
						crtTimePeriodSeries.getEndDate()
						);
				
				timePeriodValues.add(stp, crtTimePeriodSeries.getValue());
				
				if (crtTimePeriodSeries.getLabelExpression() != null)
				{
					Map<TimePeriod, String> seriesLabels = labelsMap.get(seriesName);
					if (seriesLabels == null)
					{
						seriesLabels = new HashMap<TimePeriod, String>();
						labelsMap.put(seriesName, seriesLabels);
					}
					
					seriesLabels.put(stp, crtTimePeriodSeries.getLabel());
				}
				
				if (crtTimePeriodSeries.hasItemHyperlink())
				{
					Map<TimePeriod, JRPrintHyperlink> seriesLinks = itemHyperlinks.get(seriesName);
					if (seriesLinks == null)
					{
						seriesLinks = new HashMap<TimePeriod, JRPrintHyperlink>();
						itemHyperlinks.put(seriesName, seriesLinks);
					}
					
					seriesLinks.put(stp, crtTimePeriodSeries.getPrintItemHyperlink());
				}
			}
		}
	}

	public Dataset getCustomDataset()
	{
		TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
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
	public byte getDatasetType()
	{
		return JRChartDataset.TIMEPERIOD_DATASET;
	}

	/**
	 * 
	 */
	public Object getLabelGenerator()
	{
		return new TimePeriodDatasetLabelGenerator(labelsMap);
	}

	/**
	 * 
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}
	
	
	public boolean hasItemHyperlinks()
	{
		boolean foundLinks = false;
		if (timePeriodSeries != null && timePeriodSeries.length > 0)
		{
			for (int i = 0; i < timePeriodSeries.length && !foundLinks; i++)
			{
				foundLinks = timePeriodSeries[i].hasItemHyperlink();
			}
		}
		return foundLinks;
	}
	
	
	public Map<Comparable<?>, Map<TimePeriod, JRPrintHyperlink>> getItemHyperlinks()
	{
		return itemHyperlinks;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
