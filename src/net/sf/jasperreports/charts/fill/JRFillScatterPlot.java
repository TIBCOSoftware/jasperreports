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

import java.awt.Color;

import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillScatterPlot extends JRFillChartPlot implements JRScatterPlot {
	
	/**
	 *
	 */
	protected JRFont xAxisLabelFont = null;
	protected Color xAxisLabelColor = null;
	protected JRFont xAxisTickLabelFont = null;
	protected Color xAxisTickLabelColor = null;
	protected Color xAxisLineColor = null;

	protected JRFont yAxisLabelFont = null;
	protected Color yAxisLabelColor = null;
	protected JRFont yAxisTickLabelFont = null;
	protected Color yAxisTickLabelColor = null;
	protected Color yAxisLineColor = null;

	
	/**
	 *
	 */
	public JRFillScatterPlot( JRScatterPlot plot, JRFillObjectFactory factory ){
		super( plot, factory );
		
		xAxisLabelFont = new JRBaseFont(null, null, plot.getChart(), plot.getXAxisLabelFont());
		xAxisLabelColor = plot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = new JRBaseFont(null, null, plot.getChart(), plot.getXAxisTickLabelFont());
		xAxisTickLabelColor = plot.getOwnXAxisTickLabelColor();
		xAxisLineColor = plot.getOwnXAxisLineColor();
		
		yAxisLabelFont = new JRBaseFont(null, null, plot.getChart(), plot.getYAxisLabelFont());
		yAxisLabelColor = plot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = new JRBaseFont(null, null, plot.getChart(), plot.getYAxisTickLabelFont());
		yAxisTickLabelColor = plot.getOwnYAxisTickLabelColor();
		yAxisLineColor = plot.getOwnYAxisLineColor();
	}
	
	/**
	 *
	 */
	public JRExpression getXAxisLabelExpression()
	{
		return ((JRScatterPlot)parent).getXAxisLabelExpression();
	}

	/**
	 * 
	 */
	public JRFont getXAxisLabelFont()
	{
		return xAxisLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getXAxisLabelColor()
	{
		return JRStyleResolver.getXAxisLabelColor(this, this);
	}
		
	/**
	 * 
	 */
	public Color getOwnXAxisLabelColor()
	{
		return xAxisLabelColor;
	}
		
	/**
	 * 
	 */
	public JRFont getXAxisTickLabelFont()
	{
		return xAxisTickLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getXAxisTickLabelColor()
	{
		return JRStyleResolver.getXAxisTickLabelColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnXAxisTickLabelColor()
	{
		return xAxisTickLabelColor;
	}

	/**
	 *
	 */
	public String getXAxisTickLabelMask()
	{
		return ((JRScatterPlot)parent).getXAxisTickLabelMask();
	}

	/**
	 *
	 */
	public void setXAxisTickLabelMask(String mask)
	{
	}

	/**
	 * 
	 */
	public Color getXAxisLineColor()
	{
		return JRStyleResolver.getXAxisLineColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnXAxisLineColor()
	{
		return xAxisLineColor;
	}

	/**
	 *
	 */
	public JRExpression getYAxisLabelExpression()
	{
		return ((JRScatterPlot)parent).getYAxisLabelExpression();
	}

	/**
	 * 
	 */
	public JRFont getYAxisLabelFont()
	{
		return yAxisLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getYAxisLabelColor()
	{
		return JRStyleResolver.getYAxisLabelColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnYAxisLabelColor()
	{
		return yAxisLabelColor;
	}
	
	/**
	 * 
	 */
	public JRFont getYAxisTickLabelFont()
	{
		return yAxisTickLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getYAxisTickLabelColor()
	{
		return JRStyleResolver.getYAxisTickLabelColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnYAxisTickLabelColor()
	{
		return yAxisTickLabelColor;
	}
	
	/**
	 *
	 */
	public String getYAxisTickLabelMask()
	{
		return ((JRScatterPlot)parent).getYAxisTickLabelMask();
	}

	/**
	 *
	 */
	public void setYAxisTickLabelMask(String mask)
	{
	}

	/**
	 * 
	 */
	public Color getYAxisLineColor()
	{
		return JRStyleResolver.getYAxisLineColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnYAxisLineColor()
	{
		return yAxisLineColor;
	}
	
	/**
	 * @deprecated Replaced by {@link #getShowShapes()}
	 */
	public boolean isShowShapes(){
		return ((JRScatterPlot)parent).isShowShapes();
	}
	
	/**
	 *
	 */
	public Boolean getShowShapes(){
		return ((JRScatterPlot)parent).getShowShapes();
	}
	
	/**
	 * @deprecated Replaced by {@link #setShowShapes(Boolean)}
	 */
	public void setShowShapes( boolean value ){
	}
	
	/**
	 *
	 */
	public void setShowShapes( Boolean value ){
	}
	
	/**
	 * @deprecated Replaced by {@link #getShowLines()}
	 */
	public boolean isShowLines(){
		return ((JRScatterPlot)parent).isShowLines();
	}
	
	/**
	 *
	 */
	public Boolean getShowLines(){
		return ((JRScatterPlot)parent).getShowLines();
	}
	
	/**
	 * @deprecated Replaced by {@link #setShowLines(Boolean)}
	 */
	public void setShowLines( boolean value ){
	}
	
	/**
	 *
	 */
	public void setShowLines( Boolean value ){
	}
	
}
