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

import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBubblePlot extends JRFillChartPlot implements JRBubblePlot 
{

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
	public JRFillBubblePlot( JRBubblePlot bubblePlot, JRFillObjectFactory factory ){
		super( bubblePlot, factory );
		
		xAxisLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getXAxisLabelFont());
		xAxisLabelColor = bubblePlot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getXAxisTickLabelFont());
		xAxisTickLabelColor = bubblePlot.getOwnXAxisTickLabelColor();
		xAxisLineColor = bubblePlot.getOwnXAxisLineColor();
		
		yAxisLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getYAxisLabelFont());
		yAxisLabelColor = bubblePlot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = new JRBaseFont(bubblePlot.getChart(), bubblePlot.getYAxisTickLabelFont());
		yAxisTickLabelColor = bubblePlot.getOwnYAxisTickLabelColor();
		yAxisLineColor = bubblePlot.getOwnYAxisLineColor();
	}
	
	/**
	 *
	 */
	public JRExpression getXAxisLabelExpression()
	{
		return ((JRBubblePlot)parent).getXAxisLabelExpression();
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
		return ((JRBubblePlot)parent).getXAxisTickLabelMask();
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
		return ((JRBubblePlot)parent).getYAxisLabelExpression();
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
		return ((JRBubblePlot)parent).getYAxisTickLabelMask();
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
	 * @deprecated Replaced by {@link #getScaleTypeInteger()}
	 */
	public int getScaleType(){
		return ((JRBubblePlot)parent).getScaleType();
	}
	
	/**
	 * 
	 */
	public Integer getScaleTypeInteger(){
		return ((JRBubblePlot)parent).getScaleTypeInteger();
	}
	
	/**
	 *
	 */
	public void setScaleType( int scaleType ){
	}
	
	/**
	 *
	 */
	public void setScaleType( Integer scaleType ){
	}
	
}
