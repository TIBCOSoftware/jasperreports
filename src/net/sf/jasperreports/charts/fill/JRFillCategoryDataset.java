/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.fill;

import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
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
	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	protected JRFillCategorySeries[] categorySeries = null;

	private boolean isIncremented = false;
	
	
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
	protected void initialize()
	{
		dataset = new DefaultCategoryDataset();
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		if (categorySeries != null && categorySeries.length > 0)
		{
			for(int i = 0; i < categorySeries.length; i++)
			{
				categorySeries[i].evaluate(calculator);
			}
		}
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void increment()
	{
		if (categorySeries != null && categorySeries.length > 0)
		{
			for(int i = 0; i < categorySeries.length; i++)
			{
				JRFillCategorySeries crtCategorySeries = categorySeries[i];
				if (crtCategorySeries.getCategory() != null) 
					dataset.addValue(
						crtCategorySeries.getValue(), 
						crtCategorySeries.getSeries(), 
						crtCategorySeries.getCategory()
						);//FIXME NOW verify if condifion
			}
		}
//		if (category != null) dataset.addValue(value, series, category);//FIXME NOW verify if condifion
//		if (category != null) dataset.addValue(new Double(value.doubleValue() / 2d), "FIXME NOW", category);//FIXME NOW verify if condifion
		isIncremented = true;
	}

	/**
	 *
	 */
	public Dataset getDataset()
	{
		if (isIncremented == false)
		{
			increment();
		}
		return dataset;
	}

	
}
