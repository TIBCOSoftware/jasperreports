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
package net.sf.jasperreports.charts.design;

import java.awt.Color;

import net.sf.jasperreports.charts.base.JRBaseTimeSeriesPlot;
import net.sf.jasperreports.charts.util.JRAxisFormat;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */

public class JRDesignTimeSeriesPlot extends JRBaseTimeSeriesPlot {

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public JRDesignTimeSeriesPlot(JRChartPlot plot){
		super( plot );
	}
	
	/**
	 *
	 */
	public void setTimeAxisLabelExpression( JRExpression timeAxisLabelExpression ){
		this.timeAxisLabelExpression = timeAxisLabelExpression;
		
	}

	/**
	 *
	 */
	public void setTimeAxisLabelFont(JRFont timeAxisLabelFont)
	{
		this.timeAxisLabelFont = timeAxisLabelFont;
	}

	/**
	 *
	 */
	public void setTimeAxisLabelColor(Color timeAxisLabelColor)
	{
		this.timeAxisLabelColor = timeAxisLabelColor;
	}

	/**
	 *
	 */
	public void setTimeAxisTickLabelFont(JRFont timeAxisTickLabelFont)
	{
		this.timeAxisTickLabelFont = timeAxisTickLabelFont;
	}

	/**
	 *
	 */
	public void setTimeAxisTickLabelColor(Color timeAxisTickLabelColor)
	{
		this.timeAxisTickLabelColor = timeAxisTickLabelColor;
	}

	/**
	 *
	 */
	public void setTimeAxisTickLabelMask(String timeAxisTickLabelMask)
	{
		this.timeAxisTickLabelMask = timeAxisTickLabelMask;
	}

	/**
	 *
	 */
	public void setTimeAxisLineColor(Color timeAxisLineColor)
	{
		this.timeAxisLineColor = timeAxisLineColor;
	}

	/**
	 *
	 */
	public void setValueAxisLabelExpression(JRExpression valueAxisLabelExpression)
	{
		this.valueAxisLabelExpression = valueAxisLabelExpression;
	}

	/**
	 *
	 */
	public void setValueAxisLabelFont(JRFont valueAxisLabelFont)
	{
		this.valueAxisLabelFont = valueAxisLabelFont;
	}

	/**
	 *
	 */
	public void setValueAxisLabelColor(Color valueAxisLabelColor)
	{
		this.valueAxisLabelColor = valueAxisLabelColor;
	}
	
	/**
	 *
	 */
	public void setValueAxisTickLabelFont(JRFont valueAxisTickLabelFont)
	{
		this.valueAxisTickLabelFont = valueAxisTickLabelFont;
	}

	/**
	 *
	 */
	public void setValueAxisTickLabelColor(Color valueAxisTickLabelColor)
	{
		this.valueAxisTickLabelColor = valueAxisTickLabelColor;
	}

	/**
	 *
	 */
	public void setValueAxisTickLabelMask(String valueAxisTickLabelMask)
	{
		this.valueAxisTickLabelMask = valueAxisTickLabelMask;
	}

	/**
	 *
	 */
	public void setValueAxisLineColor(Color valueAxisLineColor)
	{
		this.valueAxisLineColor = valueAxisLineColor;
	}

	/**
	 * 
	 */
	public void setTimeAxisFormat(JRAxisFormat axisFormat)
	{
		setTimeAxisLabelFont(axisFormat.getLabelFont());
		setTimeAxisLabelColor(axisFormat.getLabelColor());
		setTimeAxisTickLabelFont(axisFormat.getTickLabelFont());
		setTimeAxisTickLabelColor(axisFormat.getTickLabelColor());
		setTimeAxisTickLabelMask(axisFormat.getTickLabelMask());
		setTimeAxisLineColor(axisFormat.getLineColor());
	}

	/**
	 * 
	 */
	public void setValueAxisFormat(JRAxisFormat axisFormat)
	{
		setValueAxisLabelFont(axisFormat.getLabelFont());
		setValueAxisLabelColor(axisFormat.getLabelColor());
		setValueAxisTickLabelFont(axisFormat.getTickLabelFont());
		setValueAxisTickLabelColor(axisFormat.getTickLabelColor());
		setValueAxisTickLabelMask(axisFormat.getTickLabelMask());
		setValueAxisLineColor(axisFormat.getLineColor());
	}
}
