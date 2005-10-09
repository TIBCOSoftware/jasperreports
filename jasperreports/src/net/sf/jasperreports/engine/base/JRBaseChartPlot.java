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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRChartPlot;

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
	private static final long serialVersionUID = 10003;
	
	protected Color backcolor = null;
	protected PlotOrientation orientation = PlotOrientation.VERTICAL;
	protected float backgroundAlpha = 1;
	protected float foregroundAlpha = 1;


	/**
	 *
	 */
	protected JRBaseChartPlot(JRChartPlot plot)
	{
		if (plot != null) {
			backcolor = plot.getBackcolor();
			orientation = plot.getOrientation();
			backgroundAlpha = plot.getBackgroundAlpha();
			foregroundAlpha = plot.getForegroundAlpha();
		}
	}


	/**
	 *
	 */
	protected JRBaseChartPlot(JRChartPlot plot, JRBaseObjectFactory factory)
	{
		factory.put(plot, this);

		backcolor = plot.getBackcolor();
		orientation = plot.getOrientation();
		backgroundAlpha = plot.getBackgroundAlpha();
		foregroundAlpha = plot.getForegroundAlpha();
		
	}

	
	/**
	 *
	 */
	public Color getBackcolor()
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

}
