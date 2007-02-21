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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.jfree.chart.plot.PlotOrientation;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseChartPlot implements JRChartPlot, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected JRChart chart = null;
	protected Color backcolor = null;
	protected PlotOrientation orientation = PlotOrientation.VERTICAL;
	protected float backgroundAlpha = 1;
	protected float foregroundAlpha = 1;
	protected double labelRotation = 0.0;
	protected SortedSet  seriesColors = null;


	/**
	 *
	 */
	protected JRBaseChartPlot(JRChartPlot plot, JRChart chart)
	{
		this.chart = chart;

		if (plot != null) 
		{
			backcolor = plot.getOwnBackcolor();
			orientation = plot.getOrientation();
			backgroundAlpha = plot.getBackgroundAlpha();
			foregroundAlpha = plot.getForegroundAlpha();
			labelRotation = plot.getLabelRotation();
			seriesColors = new TreeSet(plot.getSeriesColors());
		}
		else
		{
			seriesColors = new TreeSet();
		}
	}


	/**
	 *
	 */
	protected JRBaseChartPlot(JRChartPlot plot, JRBaseObjectFactory factory)
	{
		factory.put(plot, this);

		chart = factory.getChart(plot.getChart());

		backcolor = plot.getOwnBackcolor();
		orientation = plot.getOrientation();
		backgroundAlpha = plot.getBackgroundAlpha();
		foregroundAlpha = plot.getForegroundAlpha();
		labelRotation = plot.getLabelRotation();
		seriesColors = new TreeSet(plot.getSeriesColors());
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
		return this.backcolor;
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
	}

	/**
	 *
	 */
	public PlotOrientation getOrientation()
	{
		return orientation;
	}

	/**
	 *
	 */
	public void setOrientation(PlotOrientation orientation)
	{
		this.orientation = orientation;
	}

	/**
	 *
	 */
	public float getBackgroundAlpha()
	{
		return backgroundAlpha;
	}

	/**
	 *
	 */
	public void setBackgroundAlpha(float backgroundAlpha)
	{
		this.backgroundAlpha = backgroundAlpha;
	}

	/**
	 *
	 */
	public float getForegroundAlpha()
	{
		return foregroundAlpha;
	}

	/**
	 *
	 */
	public void setForegroundAlpha(float foregroundAlpha)
	{
		this.foregroundAlpha = foregroundAlpha;
	}

	/**
	 * Gets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 */
	public double getLabelRotation()
	{
		return labelRotation;
	}
	
	/**
	 * Sets the angle in degrees to rotate the data axis labels.  The range is -360 to 360.  A positive value angles
	 * the label so it reads downwards wile a negative value angles the label so it reads upwards.  Only charts that
	 * use a category based axis (such as line or bar charts) support label rotation.
	 */
	public void setLabelRotation(double labelRotation)
	{
		this.labelRotation = labelRotation;
	}
	
	
	/**
	 * Returns a list of all the defined series colors.  Every entry in the list is of type JRChartPlot.JRSeriesColor.
	 * If there are no defined series colors this method will return an empty list, not null. 
	 */
	public SortedSet getSeriesColors()
	{
		return seriesColors;
	}
	
	/**
	 * Removes all defined series colors.
	 */
	public void clearSeriesColors()
	{
		seriesColors.clear();
	}
	
	/**
	 * Adds the specified series color to the plot.
	 */
	public void addSeriesColor(JRSeriesColor seriesColor)
	{
		seriesColors.add(seriesColor);
	}
	
	public static class JRBaseSeriesColor implements JRChartPlot.JRSeriesColor, Serializable, Comparable
	{
		/**
		 *
		 */
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
		
		protected int seriesOrder = -1;
		protected Color color = null;
		
		public JRBaseSeriesColor(int seriesOrder, Color color)
		{
			this.seriesOrder = seriesOrder;
			this.color = color;
		}
		
		/**
		 * Returns the series number (0 based) that this color applies to.
		 */
		public int getSeriesOrder()
		{
			return seriesOrder;
		}
		
		/**
		 * Returns the color to use for this series.
		 */
		public Color getColor()
		{
			return color;
		}

		public int compareTo(Object obj) {
			if (obj == null)
			{
				throw new NullPointerException();
			}
			
			return seriesOrder - ((JRBaseSeriesColor)obj).getSeriesOrder();
		}
	}
}
