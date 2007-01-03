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

import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$ 
 */
public class JRBaseScatterPlot extends JRBaseChartPlot implements JRScatterPlot {
	
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
	
	boolean isShowShapes = true;
	boolean isShowLines = true;
	
	
	/**
	 * 
	 */
	public JRBaseScatterPlot( JRChartPlot scattedPlot){
		super( scattedPlot);
	}

	/**
	 * 
	 */
	public JRBaseScatterPlot( JRScatterPlot scattedPlot, JRBaseObjectFactory factory ){
		super( scattedPlot, factory );
		
		isShowShapes = scattedPlot.isShowShapes();
		isShowLines = scattedPlot.isShowLines();
		
		xAxisLabelExpression = factory.getExpression( scattedPlot.getXAxisLabelExpression() );
		xAxisLabelFont = factory.getFont(scattedPlot.getXAxisLabelFont());
		xAxisLabelColor = scattedPlot.getXAxisLabelColor();
		xAxisTickLabelFont = factory.getFont(scattedPlot.getXAxisTickLabelFont());
		xAxisTickLabelColor = scattedPlot.getXAxisTickLabelColor();
		xAxisTickLabelMask = scattedPlot.getXAxisTickLabelMask();
		xAxisLineColor = scattedPlot.getXAxisLineColor();
		
		yAxisLabelExpression = factory.getExpression( scattedPlot.getYAxisLabelExpression() );
		yAxisLabelFont = factory.getFont(scattedPlot.getYAxisLabelFont());
		yAxisLabelColor = scattedPlot.getYAxisLabelColor();
		yAxisTickLabelFont = factory.getFont(scattedPlot.getYAxisTickLabelFont());
		yAxisTickLabelColor = scattedPlot.getYAxisTickLabelColor();
		yAxisTickLabelMask = scattedPlot.getYAxisTickLabelMask();
		yAxisLineColor = scattedPlot.getYAxisLineColor();
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
		return xAxisLineColor;
	}
	
	/**
	 * 
	 */
	public JRExpression getYAxisLabelExpression() {
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
		return yAxisLineColor;
	}
	
	/**
	 * 
	 */
	public boolean isShowShapes(){
		return isShowShapes;
	}
	
	/**
	 * 
	 */
	public boolean isShowLines(){
		return isShowLines;
	}
	
	/**
	 * 
	 */
	public void setShowShapes( boolean value ){
		this.isShowShapes = value;
	}
	
	/**
	 * 
	 */
	public void setShowLines( boolean value ){
		this.isShowLines = value;
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

}
