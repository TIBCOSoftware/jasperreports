/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

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
	protected JRFillXySeries[] xySeries = null;

	private List seriesNames = null;
	private Map seriesMap = null;
	private Map labelsMap = null;
	
	
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
				seriesNames = new ArrayList();
				seriesMap = new HashMap();
				labelsMap = new HashMap();
			}

			for(int i = 0; i < xySeries.length; i++)
			{
				JRFillXySeries crtXySeries = xySeries[i];

				Comparable seriesName = crtXySeries.getSeries();
				XYSeries xySrs = (XYSeries)seriesMap.get(seriesName);
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
					Map seriesLabels = (Map)labelsMap.get(seriesName);
					if (seriesLabels == null)
					{
						seriesLabels = new HashMap();
						labelsMap.put(seriesName, seriesLabels);
					}
					
					seriesLabels.put(crtXySeries.getXValue(), crtXySeries.getLabel());
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
				Comparable seriesName = (Comparable)seriesNames.get(i);
				dataset.addSeries((XYSeries)seriesMap.get(seriesName));
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
	public XYDatasetLabelGenerator getLabelGenerator(){
		return new XYDatasetLabelGenerator(labelsMap);	
	}
	
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


}
