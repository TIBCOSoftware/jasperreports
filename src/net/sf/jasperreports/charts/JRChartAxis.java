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
package net.sf.jasperreports.charts;

import net.sf.jasperreports.engine.JRChart;

/**
 * Describes an axis that can be added to a multiple axis chart.  The name
 * "axis" is a bit of a misnomer, as it really contains information about
 * a new dataset to plot, the axis to plot it against, and how to render that
 * dataset.
 *
 * @author Barry Klawans (barry@users.sourceforge.net)
 * @version $Id$
 */


public interface JRChartAxis
{
	/**
	 * Position the axis to the left of a VERTICAL chart or on the top
	 * of a HORIZONTAL chart.
	 */
	public static final byte POSITION_LEFT_OR_TOP = 1;

	/**
	 * Position the axis to the right of a VERTICAL chart or on the bottom
	 * of a HORIZONTAL chart.
	 */
	public static final byte POSITION_RIGHT_OR_BOTTOM = 2;


	/**
	 * Returns the position of this axis.
	 *
	 * @return the position of this axis
	 */
	public byte getPosition();


	/**
	 * Returns the chart that contains the dataset and plot to use for this
	 * axis.  The plot is used to figure out how to render the dataset when
	 * adding to the multiple axis chart.
	 *
	 * @return the chart that contains the dataset and plot for this axis
	 */
	public JRChart getChart();
	
	
	/**
	 * 
	 */
	public Object clone(JRChart parentChart) throws CloneNotSupportedException;
}
