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

import java.io.Serializable;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * Base read-only implementation of {@link JRChartAxis JRChartAxis}.
 *
 * @author Barry Klawans (barry@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseChartAxis implements JRChartAxis, Serializable
{

	/**
	 * Where to position the axis.
	 */
	protected byte position = POSITION_LEFT_OR_TOP;

	/**
	 * The Chart object containing the dataset and plot to use with this axis.
	 */
	protected JRChart chart = null;

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Construct a new axis that will be added to the specified chart.
	 */
	public JRBaseChartAxis()
	{
	}

	public JRBaseChartAxis(JRChartAxis axis, JRBaseObjectFactory factory)
	{
		factory.put(axis, this);

		this.position = axis.getPosition();
		this.chart = factory.getChart(axis.getChart());
	}

	/**
	 * Returns the position of this axis.
	 *
	 * @return the position of this axis
	 */
	public byte getPosition()
	{
		return position;
	}

	/**
	 * Returns the chart that contains the dataset and plot to use for this
	 * axis.  The plot is used to figure out how to render the dataset when
	 * adding to the multiple axis chart.
	 *
	 * @return the chart that contains the dataset and plot for this axis
	 */
	public JRChart getChart()
	{
		return chart;
	}
}
