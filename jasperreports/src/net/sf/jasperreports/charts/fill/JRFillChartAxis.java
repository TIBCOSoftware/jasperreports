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
package net.sf.jasperreports.charts.fill;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.type.AxisPositionEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.fill.JRFillChart;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * Describes an axis that can be added to a multiple axis chart.  The name
 * "axis" is a bit of a misnomer, as it really contains information about
 * a new dataset to plot, the axis to plot it against, and how to render that
 * dataset.
 *
 * @author Barry Klawans (barry@users.sourceforge.net)
 */
public class JRFillChartAxis implements JRChartAxis
{

	protected JRChartAxis parent;

	/**
	 * The filled version of the <code>chart</code> field.  Contains evaluated
	 * expressions and data.
	 */
	protected JRFillChart fillChart;


	public JRFillChartAxis(JRChartAxis axis, JRFillObjectFactory factory)
	{
		factory.put(axis, this);

		this.parent = axis;
		this.fillChart = (JRFillChart)factory.getVisitResult(axis.getChart());
	}

	/**
	 * Return the filled version of the chart with the dataset and plot for
	 * this axis.
	 *
	 * @return the filled version of the chart with the dataset and plot for
	 * 		   this axis
	 */
	public JRFillChart getFillChart()
	{
		return fillChart;
	}

	public JRChart getChart()
	{
		return parent.getChart();
	}

	public AxisPositionEnum getPositionValue()
	{
		return parent.getPositionValue();
	}

	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public JRChartAxis clone(JRChart parentChart)
	{
		throw new UnsupportedOperationException();
	}
}
