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
 * @version $Id$
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
	 * <code>{@link JRChartAxis}</code>
	 */
	protected List axes = new java.util.ArrayList();



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

		List origAxes = multiAxisPlot.getAxes();
		axes.clear();
		if (origAxes != null)
		{
			Iterator iter = origAxes.iterator();
			while (iter.hasNext())
			{
				JRChartAxis axis = (JRChartAxis)iter.next();
				axes.add(factory.getChartAxis(axis));
			}
		}
	}



	/**
	 *
	 */
	public List getAxes()
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
		Iterator iter = axes.iterator();
		while (iter.hasNext())
		{
			JRChartAxis axis = (JRChartAxis)iter.next();
			collector.collect(axis.getChart());
		}
	}

	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		JRBaseMultiAxisPlot clone = (JRBaseMultiAxisPlot)super.clone(parentChart);
		
		if (axes != null)
		{
			clone.axes = new ArrayList(axes.size());
			for(int i = 0; i < axes.size(); i++)
			{
				clone.axes.add(((JRChartAxis)axes.get(i)).clone(parentChart));
			}
		}

		return clone;
	}
}
