/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.charts.fill;

import java.awt.Color;

import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
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
	protected JRFont xAxisLabelFont;
	protected Color xAxisLabelColor;
	protected JRFont xAxisTickLabelFont;
	protected Color xAxisTickLabelColor;
	protected Color xAxisLineColor;

	protected JRFont yAxisLabelFont;
	protected Color yAxisLabelColor;
	protected JRFont yAxisTickLabelFont;
	protected Color yAxisTickLabelColor;
	protected Color yAxisLineColor;

	
	/**
	 *
	 */
	public JRFillScatterPlot( JRScatterPlot plot, JRFillObjectFactory factory ){
		super( plot, factory );
		
		xAxisLabelFont = factory.getFont(plot.getChart(), plot.getXAxisLabelFont());
		xAxisLabelColor = plot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = factory.getFont(plot.getChart(), plot.getXAxisTickLabelFont());
		xAxisTickLabelColor = plot.getOwnXAxisTickLabelColor();
		xAxisLineColor = plot.getOwnXAxisLineColor();
		
		yAxisLabelFont = factory.getFont(plot.getChart(), plot.getYAxisLabelFont());
		yAxisLabelColor = plot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = factory.getFont(plot.getChart(), plot.getYAxisTickLabelFont());
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
	public Boolean getXAxisVerticalTickLabels()
	{
		return ((JRScatterPlot)parent).getXAxisVerticalTickLabels();
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
	public Boolean getYAxisVerticalTickLabels()
	{
		return ((JRScatterPlot)parent).getYAxisVerticalTickLabels();
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
	public Boolean getShowShapes(){
		return ((JRScatterPlot)parent).getShowShapes();
	}
	
	/**
	 *
	 */
	public void setShowShapes( Boolean value ){
	}
	
	/**
	 *
	 */
	public Boolean getShowLines(){
		return ((JRScatterPlot)parent).getShowLines();
	}
	
	/**
	 *
	 */
	public void setShowLines( Boolean value ){
	}
	
	/**
	 *
	 */
	public JRExpression getDomainAxisMinValueExpression()
	{
		return ((JRScatterPlot)parent).getDomainAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMaxValueExpression()
	{
		return ((JRScatterPlot)parent).getDomainAxisMaxValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMinValueExpression()
	{
		return ((JRScatterPlot)parent).getRangeAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMaxValueExpression()
	{
		return ((JRScatterPlot)parent).getRangeAxisMaxValueExpression();
	}

}
