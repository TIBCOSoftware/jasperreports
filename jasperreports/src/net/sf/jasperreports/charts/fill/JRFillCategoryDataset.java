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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.util.CategoryLabelGenerator;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCategoryDataset extends JRFillChartDataset implements JRCategoryDataset
{

	/**
	 *
	 */
	protected JRFillCategorySeries[] categorySeries;

	private DefaultCategoryDataset dataset;
	private Map<Comparable<?>, Map<Comparable<?>, String>> labelsMap;
	
	private Map<Comparable<?>, Map<Comparable<?>, JRPrintHyperlink>> itemHyperlinks;

	
	/**
	 *
	 */
	public JRFillCategoryDataset(
		JRCategoryDataset categoryDataset, 
		JRFillObjectFactory factory
		)
	{
		super(categoryDataset, factory);

		/*   */
		JRCategorySeries[] srcCategorySeries = categoryDataset.getSeries();
		if (srcCategorySeries != null && srcCategorySeries.length > 0)
		{
			categorySeries = new JRFillCategorySeries[srcCategorySeries.length];
			for(int i = 0; i < categorySeries.length; i++)
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
				labelsMap = new HashMap<Comparable<?>, Map<Comparable<?>, String>>();
				itemHyperlinks = new HashMap<Comparable<?>, Map<Comparable<?>, JRPrintHyperlink>>();
			}
			
			for(int i = 0; i < categorySeries.length; i++)
			{
				JRFillCategorySeries crtCategorySeries = categorySeries[i];
				
				Comparable<?> seriesName = crtCategorySeries.getSeries();
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
					Map<Comparable<?>, String> seriesLabels = labelsMap.get(seriesName);
					if (seriesLabels == null)
					{
						seriesLabels = new HashMap<Comparable<?>, String>();
						labelsMap.put(seriesName, seriesLabels);
					}
					
					seriesLabels.put(crtCategorySeries.getCategory(), crtCategorySeries.getLabel());
				}
				
				if (crtCategorySeries.hasItemHyperlinks())
				{
					Map<Comparable<?>, JRPrintHyperlink> seriesLinks = itemHyperlinks.get(seriesName);
					if (seriesLinks == null)
					{
						seriesLinks = new HashMap<Comparable<?>, JRPrintHyperlink>();
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
	public Dataset getCustomDataset()
	{
		return dataset;
	}


	/**
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.CATEGORY_DATASET;
	}

	
	/**
	 *
	 */
	public Object getLabelGenerator()
	{
		return new CategoryLabelGenerator(labelsMap);
	}


	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	
	public Map<Comparable<?>, Map<Comparable<?>, JRPrintHyperlink>> getItemHyperlinks()
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


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
