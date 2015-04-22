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
package net.sf.jasperreports.charts.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * An immutable representation of the layout options of a multiple axis chart.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRBaseMultiAxisPlot extends JRBaseChartPlot implements JRMultiAxisPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * All the axes contained in this plot.  Each entry indicates a chart containing
	 * the dataset and layout of that entry and where to draw that chart's range
	 * axis.  All entries in the list are of the type
	 * {@link net.sf.jasperreports.charts.JRChartAxis JRChartAxis}
	 */
	protected List<JRChartAxis> axes = new ArrayList<JRChartAxis>();



	/**
	 * Constructs a copy of an existing multiple axis chart plot.
	 *
	 * @param multiAxisPlot the plot to copy
	 */
	public JRBaseMultiAxisPlot(JRChartPlot multiAxisPlot, JRChart chart)
	{
		super(multiAxisPlot, chart);
	}

	/**
	 * Creates a copy of an existing multiple axis chart plot and registers
	 * any expression contained in the plot with the specified factory.  Since
	 * the plot contains multiple other charts nested inside of it all of the
	 * expressions used by those charts is also registered with the factory.
	 *
	 * @param multiAxisPlot the plot to copy
	 * @param factory the factory to register expressions with
	 */
	public JRBaseMultiAxisPlot(JRMultiAxisPlot multiAxisPlot, JRBaseObjectFactory factory)
	{
		super(multiAxisPlot, factory);

		List<JRChartAxis> origAxes = multiAxisPlot.getAxes();
		axes.clear();
		if (origAxes != null)
		{
			Iterator<JRChartAxis> iter = origAxes.iterator();
			while (iter.hasNext())
			{
				JRChartAxis axis = iter.next();
				axes.add(factory.getChartAxis(axis));
			}
		}
	}



	/**
	 *
	 */
	public List<JRChartAxis> getAxes()
	{
		return axes;
	}

	/**
	 * Adds all the expression used by this plot with the specified collector.
	 * All collected expression that are also registered with a factory will
	 * be included with the report is compiled.
	 *
	 * @param collector the expression collector to use
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		Iterator<JRChartAxis> iter = axes.iterator();
		while (iter.hasNext())
		{
			JRChartAxis axis = iter.next();
			collector.collect(axis.getChart());
		}
	}

	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		JRBaseMultiAxisPlot clone = (JRBaseMultiAxisPlot)super.clone(parentChart);
		List<JRChartAxis> cloneAxes;
		if (axes == null)// should not be null but it's protected so let's be safe
		{
			cloneAxes = null;
		}
		else
		{
			cloneAxes = new ArrayList<JRChartAxis>(axes.size());
			for (JRChartAxis axis : axes)
			{
				JRChartAxis axisClone = axis == null ? null : axis.clone(parentChart);
				cloneAxes.add(axisClone);
			}
		}
		clone.axes = cloneAxes;
		return clone;
	}
}
