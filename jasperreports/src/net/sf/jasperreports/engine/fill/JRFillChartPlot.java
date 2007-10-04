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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.util.SortedSet;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.jfree.chart.plot.PlotOrientation;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillChartPlot implements JRChartPlot
{


	/**
	 *
	 */
	protected JRChartPlot parent = null;

	/**
	 *
	 */
	protected JRChart chart = null;


	/**
	 *
	 */
	protected JRFillChartPlot(
		JRChartPlot plot, 
		JRFillObjectFactory factory
		)
	{
		factory.put(plot, this);

		parent = plot;
		
		chart = (JRChart)factory.getVisitResult(plot.getChart());
	}


	/**
	 *
	 */
	public JRChart getChart()
	{
		return chart;
	}
	
	/**
	 *
	 */
	public Color getBackcolor()
	{
		return JRStyleResolver.getBackcolor(this);
	}
	
	/**
	 *
	 */
	public Color getOwnBackcolor()
	{
		return parent.getOwnBackcolor();
	}
	
	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
	}

	/**
	 *
	 */
	public PlotOrientation getOrientation()
	{
		return parent.getOrientation();
	}
	
	/**
	 *
	 */
	public void setOrientation(PlotOrientation orientation)
	{
	}
		
	/**
	 *
	 */
	public float getBackgroundAlpha()
	{
		return parent.getBackgroundAlpha();
	}
	
	/**
	 *
	 */
	public void setBackgroundAlpha(float BackgroundAlpha)
	{
	}
	
	/**
	 *
	 */
	public float getForegroundAlpha()
	{
		return parent.getForegroundAlpha();
	}
	
	/**
	 *
	 */
	public void setForegroundAlpha(float foregroundAlpha)
	{
	}
	
	/**
	 *
	 */
	public double getLabelRotation()
	{
		return parent.getLabelRotation();
	}
	
	/**
	 *
	 */
	public void setLabelRotation(double labelRotation)
	{
	}
	
	/**
	 * Returns a list of all the defined series colors.  Every entry in the list is of type JRChartPlot.JRSeriesColor.
	 * If there are no defined series colors this method will return an empty list, not null. 
	 */
	public SortedSet getSeriesColors()
	{
		return parent.getSeriesColors();
	}
	
	/**
	 * Removes all defined series colors.
	 */
	public void clearSeriesColors()
	{
	}
	
	/**
	 * Adds the specified series color to the plot.
	 */
	public void addSeriesColor(JRSeriesColor seriesColor)
	{
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
	}

}
