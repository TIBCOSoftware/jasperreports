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
package com.jaspersoft.sample.ofc;

import java.util.Iterator;

import com.jaspersoft.sample.ofc.BarDataset;
import com.jaspersoft.sample.ofc.BarSeries;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BarChartCompiler implements ComponentCompiler
{

	public void collectExpressions(Component component, JRExpressionCollector collector)
	{
		BarChartComponent chart = (BarChartComponent) component;
		collector.addExpression(chart.getTitleExpression());
		collectExpressions(chart.getDataset(), collector);
	}

	public static void collectExpressions(BarDataset dataset, JRExpressionCollector collector)
	{
		collector.collect(dataset);
		
		JRExpressionCollector datasetCollector = collector.getCollector(dataset);
		for (Iterator it = dataset.getSeries().iterator(); it.hasNext();)
		{
			BarSeries series = (BarSeries) it.next();
			datasetCollector.addExpression(series.getSeriesExpression());
			datasetCollector.addExpression(series.getCategoryExpression());
			datasetCollector.addExpression(series.getValueExpression());
		}
	}

	public void verify(Component component, JRVerifier verifier)
	{
		//TODO
	}

	public Component toCompiledComponent(Component component,
			JRBaseObjectFactory baseFactory)
	{
		BarChartComponent chart = (BarChartComponent) component;
		BarChartComponent compiledChart = new BarChartComponent(chart, baseFactory);
		return compiledChart;
	}
	
}
