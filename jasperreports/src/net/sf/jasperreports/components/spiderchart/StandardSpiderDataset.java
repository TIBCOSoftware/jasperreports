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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;

/**
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: StandardSpiderDataset.java 3889 2010-07-16 10:52:00Z shertage $
 */
public class StandardSpiderDataset extends JRDesignElementDataset implements SpiderDataset
{
	
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_CATEGORY_SERIES = "categorySeries";
	
	private List categorySeriesList = new ArrayList();
	
	public StandardSpiderDataset()
	{
		super();
	}

	public StandardSpiderDataset(SpiderDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);
		JRCategorySeries[] srcCategorySeries = dataset.getSeries();
		if (srcCategorySeries != null && srcCategorySeries.length > 0)
		{
			for(int i = 0; i < srcCategorySeries.length; i++)
			{
				addCategorySeries(factory.getCategorySeries(srcCategorySeries[i]));
			}
		}
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		SpiderChartCompiler.collectExpressions(this, collector);
	}

	public JRCategorySeries[] getSeries()
	{
		JRCategorySeries[] categorySeriesArray = new JRCategorySeries[categorySeriesList.size()];
		categorySeriesList.toArray(categorySeriesArray);
		return categorySeriesArray;
	}

	/**
	 * 
	 */
	public List getSeriesList()
	{
		return categorySeriesList;
	}

	/**
	 *
	 */
	public void addCategorySeries(JRCategorySeries categorySeries)
	{
		categorySeriesList.add(categorySeries);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_CATEGORY_SERIES, 
				categorySeries, categorySeriesList.size() - 1);
	}

	/**
	 *
	 */
	public JRCategorySeries removeCategorySeries(JRCategorySeries categorySeries)
	{
		if (categorySeries != null)
		{
			int idx = categorySeriesList.indexOf(categorySeries);
			if (idx >= 0)
			{
				categorySeriesList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CATEGORY_SERIES, 
						categorySeries, idx);
			}
		}
		
		return categorySeries;
	}

}
