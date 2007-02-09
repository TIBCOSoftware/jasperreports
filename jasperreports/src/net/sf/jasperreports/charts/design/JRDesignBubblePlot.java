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

import net.sf.jasperreports.charts.base.JRBaseBubblePlot;
import net.sf.jasperreports.charts.util.JRAxisFormat;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$ 
 */
public class JRDesignBubblePlot extends JRBaseBubblePlot {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	public JRDesignBubblePlot(JRChartPlot bubblePlot, JRChart chart)
	{
		super(bubblePlot, chart);
	}


	/**
	 *
	 */
	public void setXAxisLabelExpression( JRExpression xAxisLabelExpression ){
		this.xAxisLabelExpression = xAxisLabelExpression;
	}

	/**
	 *
	 */
	public void setXAxisLabelFont(JRFont xAxisLabelFont)
	{
		this.xAxisLabelFont = xAxisLabelFont;
	}

	/**
	 *
	 */
	public void setXAxisLabelColor(Color xAxisLabelColor)
	{
		this.xAxisLabelColor = xAxisLabelColor;
	}
	
	/**
	 *
	 */
	public void setXAxisTickLabelFont(JRFont xAxisTickLabelFont)
	{
		this.xAxisTickLabelFont = xAxisTickLabelFont;
	}

	/**
	 *
	 */
	public void setXAxisTickLabelColor(Color xAxisTickLabelColor)
	{
		this.xAxisTickLabelColor = xAxisTickLabelColor;
	}

	/**
	 *
	 */
	public void setXAxisTickLabelMask(String xAxisTickLabelMask)
	{
		this.xAxisTickLabelMask = xAxisTickLabelMask;
	}

	/**
	 *
	 */
	public void setXAxisLineColor(Color xAxisLineColor)
	{
		this.xAxisLineColor = xAxisLineColor;
	}

	/**
	 *
	 */
	public void setYAxisLabelExpression( JRExpression yAxisLabelExpression ){
		this.yAxisLabelExpression = yAxisLabelExpression;
	}

	/**
	 *
	 */
	public void setYAxisLabelFont(JRFont yAxisLabelFont)
	{
		this.yAxisLabelFont = yAxisLabelFont;
	}

	/**
	 *
	 */
	public void setYAxisLabelColor(Color yAxisLabelColor)
	{
		this.yAxisLabelColor = yAxisLabelColor;
	}
	
	/**
	 *
	 */
	public void setYAxisTickLabelFont(JRFont yAxisTickLabelFont)
	{
		this.yAxisTickLabelFont = yAxisTickLabelFont;
	}

	/**
	 *
	 */
	public void setYAxisTickLabelColor(Color yAxisTickLabelColor)
	{
		this.yAxisTickLabelColor = yAxisTickLabelColor;
	}

	/**
	 *
	 */
	public void setYAxisTickLabelMask(String yAxisTickLabelMask)
	{
		this.yAxisTickLabelMask = yAxisTickLabelMask;
	}

	/**
	 *
	 */
	public void setYAxisLineColor(Color yAxisLineColor)
	{
		this.yAxisLineColor = yAxisLineColor;
	}
	
	/**
	 * 
	 */
	public void setXAxisFormat(JRAxisFormat axisFormat)
	{
		setXAxisLabelColor(axisFormat.getLabelColor());
		setXAxisLabelFont(axisFormat.getLabelFont());
		setXAxisTickLabelFont(axisFormat.getTickLabelFont());
		setXAxisTickLabelColor(axisFormat.getTickLabelColor());
		setXAxisTickLabelMask(axisFormat.getTickLabelMask());
		setXAxisLineColor(axisFormat.getLineColor());
	}

	/**
	 * 
	 */
	public void setYAxisFormat(JRAxisFormat axisFormat)
	{
		setYAxisLabelColor(axisFormat.getLabelColor());
		setYAxisLabelFont(axisFormat.getLabelFont());
		setYAxisTickLabelFont(axisFormat.getTickLabelFont());
		setYAxisTickLabelColor(axisFormat.getTickLabelColor());
		setYAxisTickLabelMask(axisFormat.getTickLabelMask());
		setYAxisLineColor(axisFormat.getLineColor());
	}
}
