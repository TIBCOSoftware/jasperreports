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

import java.awt.Color;

import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.jfree.chart.renderer.xy.XYBubbleRenderer;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseBubblePlot extends JRBaseChartPlot implements JRBubblePlot {

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected JRExpression xAxisLabelExpression = null;
	protected JRFont xAxisLabelFont = null;
	protected Color xAxisLabelColor = null;
	protected JRFont xAxisTickLabelFont = null;
	protected Color xAxisTickLabelColor = null;
	protected String xAxisTickLabelMask = null;
	protected Color xAxisLineColor = null;

	protected JRExpression yAxisLabelExpression = null;
	protected JRFont yAxisLabelFont = null;
	protected Color yAxisLabelColor = null;
	protected JRFont yAxisTickLabelFont = null;
	protected Color yAxisTickLabelColor = null;
	protected String yAxisTickLabelMask = null;
	protected Color yAxisLineColor = null;
	
	protected int scaleType = XYBubbleRenderer.SCALE_ON_RANGE_AXIS;
	
	
	/**
	 * 
	 */
	public JRBaseBubblePlot(JRChartPlot bubblePlot, JRChart chart){
		super(bubblePlot, chart);
	}


	/**
	 * 
	 */
	public JRBaseBubblePlot( JRBubblePlot bubblePlot, JRBaseObjectFactory factory ){
		super( bubblePlot, factory );
		
		scaleType = bubblePlot.getScaleType();
		
		xAxisLabelExpression = factory.getExpression( bubblePlot.getXAxisLabelExpression() );
		xAxisLabelFont = new JRBaseFont(null, null, bubblePlot.getChart(), bubblePlot.getXAxisLabelFont());
		xAxisLabelColor = bubblePlot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = new JRBaseFont(null, null, bubblePlot.getChart(), bubblePlot.getXAxisTickLabelFont());
		xAxisTickLabelColor = bubblePlot.getOwnXAxisTickLabelColor();
		xAxisTickLabelMask = bubblePlot.getXAxisTickLabelMask();
		xAxisLineColor = bubblePlot.getXAxisLineColor();
		
		yAxisLabelExpression = factory.getExpression( bubblePlot.getYAxisLabelExpression() );
		yAxisLabelFont = new JRBaseFont(null, null, bubblePlot.getChart(), bubblePlot.getYAxisLabelFont());
		yAxisLabelColor = bubblePlot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = new JRBaseFont(null, null, bubblePlot.getChart(), bubblePlot.getYAxisTickLabelFont());
		yAxisTickLabelColor = bubblePlot.getOwnYAxisTickLabelColor();
		yAxisTickLabelMask = bubblePlot.getYAxisTickLabelMask();
		yAxisLineColor = bubblePlot.getYAxisLineColor();
	}
	
	/**
	 * 
	 */
	public JRExpression getXAxisLabelExpression(){
		return xAxisLabelExpression;
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
		return xAxisTickLabelMask;
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
	public JRExpression getYAxisLabelExpression(){
		return yAxisLabelExpression;
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
		return yAxisTickLabelMask;
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
	 * 
	 */
	public int getScaleType(){
		return scaleType;
	}
	
	/**
	 * 
	 */
	public void setScaleType( int scaleType ){
		this.scaleType = scaleType;
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

}
