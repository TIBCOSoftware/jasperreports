/*
 * ============================================================================
 * GNU Lesser General Public License
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
package net.sf.jasperreports.engine;

import java.awt.Color;

import org.jfree.chart.plot.PlotOrientation;


/**
 * Chart plots define chart appearance and display details such as colors, legend or labels. Each plot may have different
 * characteristics, depending on the chart type it belongs to. This is the superinterface for all plots and contains common
 * properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRChartPlot
{


	/**
	 * Gets the chart background color.
	 */
	public Color getBackcolor();
	
	/**
	 * Sets the chart background color.
	 */
	public void setBackcolor(Color backcolor);


	/**
	 * Gets the plot orientation (horizontal or vertical).
	 */
	public PlotOrientation getOrientation();
	
	/**
	 * Sets the plot orientation (horizontal or vertical).
	 */
	public void setOrientation(PlotOrientation orientation);

	/**
	 * Gets the transparency factor for this plot background. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 * @return a float value between 0 and 1.
	 */
	public float getBackgroundAlpha();
	
	/**
	 * Sets the transparency factor for this plot background. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 */
	public void setBackgroundAlpha(float backgroundAlpha);

	/**
	 * Gets the transparency factor for this plot foreground. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 * @return a float value between 0 and 1.
	 */
	public float getForegroundAlpha();
	
	/**
	 * Sets the transparency factor for this plot foreground. The range is from 0 to 1, where 0 means transparent and 1
	 * opaque. The default is 1.
	 */
	public void setForegroundAlpha(float foregroundAlpha);

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector);

}
