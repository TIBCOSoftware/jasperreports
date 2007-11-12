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

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.fill.JRFillChart;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * Describes an axis that can be added to a multiple axis chart.  The name
 * "axis" is a bit of a misnomer, as it really contains information about
 * a new dataset to plot, the axis to plot it against, and how to render that
 * dataset.
 *
 * @author Barry Klawans (barry@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillChartAxis implements JRChartAxis
{

	protected JRChartAxis parent;

	/**
	 * The filled version of the <code>chart</code> field.  Contains evaluated
	 * expressions and data.
	 */
	protected JRFillChart fillChart = null;

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


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

	public byte getPosition()
	{
		return parent.getPosition();
	}

	/**
	 *
	 */
	public Object clone(JRChart chart) throws CloneNotSupportedException 
	{
		throw new CloneNotSupportedException();
	}
}
