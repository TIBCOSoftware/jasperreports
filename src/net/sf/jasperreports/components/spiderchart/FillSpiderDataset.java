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
package net.sf.jasperreports.components.spiderchart;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.fill.JRFillCategorySeries;
import net.sf.jasperreports.charts.util.CategoryLabelGenerator;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: FillSpiderDataset.java 3877 2010-07-14 15:25:08Z shertage $
 */
public class FillSpiderDataset extends JRFillElementDataset implements SpiderDataset
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/**
	 *
	 */
	protected JRFillCategorySeries[] categorySeries;

	private DefaultCategoryDataset dataset;
	private Map labelsMap;
	
	private Map itemHyperlinks;

	
	/**
	 *
	 */
	public FillSpiderDataset(
		SpiderDataset spiderDataset, 
		JRFillObjectFactory factory
		)
	{
		super(spiderDataset, factory);

		/*   */
		JRCategorySeries[] srcCategorySeries = spiderDataset.getSeries();
		if (srcCategorySeries != null && srcCategorySeries.length > 0)
		{
			categorySeries = new JRFillCategorySeries[srcCategorySeries.length];
			for(int i = 0; i < srcCategorySeries.length; i++)
			{
				categorySeries[i] = (JRFillCategorySeries)factory.getCategorySeries(srcCategorySeries[i]);
			}
		}
	}
	
	
	/**
	 *
	 */
	public JRCategorySeries[] getSeries()
	{
		return categorySeries;
	}


	/**
	 *
	 */
	protected void customInitialize()
	{
		dataset = null;
		labelsMap = null;
		itemHyperlinks = null;
	}

	/**
	 *
	 */
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		if (categorySeries != null && categorySeries.length > 0)
		{
			for(int i = 0; i < categorySeries.length; i++)
			{
				categorySeries[i].evaluate(calculator);
			}
		}
	}

	/**
	 *
	 */
	protected void customIncrement()
	{
		if (categorySeries != null && categorySeries.length > 0)
		{
			if (dataset == null)
			{
				dataset = new DefaultCategoryDataset();
				labelsMap = new HashMap();
				itemHyperlinks = new HashMap();
			}
			
			for(int i = 0; i < categorySeries.length; i++)
			{
				JRFillCategorySeries crtCategorySeries = categorySeries[i];
				
				Comparable seriesName = crtCategorySeries.getSeries();
				if (seriesName == null)
				{
					throw new JRRuntimeException("Category series name is null.");
				}

				dataset.addValue(
					crtCategorySeries.getValue(), 
					crtCategorySeries.getSeries(), 
					crtCategorySeries.getCategory()
					);

				if (crtCategorySeries.getLabelExpression() != null)
				{
					Map seriesLabels = (Map)labelsMap.get(seriesName);
					if (seriesLabels == null)
					{
						seriesLabels = new HashMap();
						labelsMap.put(seriesName, seriesLabels);
					}
					
					seriesLabels.put(crtCategorySeries.getCategory(), crtCategorySeries.getLabel());
				}
				
				if (crtCategorySeries.hasItemHyperlinks())
				{
					Map seriesLinks = (Map) itemHyperlinks.get(seriesName);
					if (seriesLinks == null)
					{
						seriesLinks = new HashMap();
						itemHyperlinks.put(seriesName, seriesLinks);
					}
					seriesLinks.put(crtCategorySeries.getCategory(), crtCategorySeries.getPrintItemHyperlink());
				}
			}
		}
	}

	/**
	 *
	 */
	public DefaultCategoryDataset getCustomDataset()
	{
		return dataset;
	}

	/**
	 *
	 */
	public StandardCategoryItemLabelGenerator getLabelGenerator()
	{
		return labelsMap != null ? new CategoryLabelGenerator(labelsMap) : new StandardCategoryItemLabelGenerator();
	}


	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		SpiderChartCompiler.collectExpressions(this, collector);
	}

	
	public Map getItemHyperlinks()
	{
		return itemHyperlinks;
	}
	
	
	public boolean hasItemHyperlinks()
	{
		boolean foundLinks = false;
		if (categorySeries != null && categorySeries.length > 0)
		{
			for (int i = 0; i < categorySeries.length && !foundLinks; i++)
			{
				JRFillCategorySeries serie = categorySeries[i];
				foundLinks = serie.hasItemHyperlinks();
			}
		}
		return foundLinks;
	}

	public void finishDataset()
	{
		//one last increment is required in certain cases
		increment();
	}

	
}
