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
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRScatterPlot;
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

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$ 
 */
public class JRBaseScatterPlot extends JRBaseChartPlot implements JRScatterPlot {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SHOW_LINES = "showLines";
	
	public static final String PROPERTY_SHOW_SHAPES = "showShapes";
	
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
	
	Boolean showShapes = null;
	Boolean showLines = null;
	
	
	/**
	 * 
	 */
	public JRBaseScatterPlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRScatterPlot scattedPlot = plot instanceof JRScatterPlot ? (JRScatterPlot)plot : null;
		if (scattedPlot == null)
		{
			xAxisLabelFont = new JRBaseFont(chart, null);
			xAxisTickLabelFont = new JRBaseFont(chart, null);
			yAxisLabelFont = new JRBaseFont(chart, null);
			yAxisTickLabelFont = new JRBaseFont(chart, null);
		}
		else
		{
			xAxisLabelFont = new JRBaseFont(chart, scattedPlot.getXAxisLabelFont());
			xAxisTickLabelFont = new JRBaseFont(chart, scattedPlot.getXAxisTickLabelFont());
			yAxisLabelFont = new JRBaseFont(chart, scattedPlot.getYAxisLabelFont());
			yAxisTickLabelFont = new JRBaseFont(chart, scattedPlot.getYAxisTickLabelFont());
		}
	}

	/**
	 * 
	 */
	public JRBaseScatterPlot(JRScatterPlot scattedPlot, JRBaseObjectFactory factory )
	{
		super(scattedPlot, factory );
		
		showShapes = scattedPlot.getShowShapes();
		showLines = scattedPlot.getShowLines();
		
		xAxisLabelExpression = factory.getExpression( scattedPlot.getXAxisLabelExpression() );
		xAxisLabelFont = new JRBaseFont(scattedPlot.getChart(), scattedPlot.getXAxisLabelFont());
		xAxisLabelColor = scattedPlot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = new JRBaseFont(scattedPlot.getChart(), scattedPlot.getXAxisTickLabelFont());
		xAxisTickLabelColor = scattedPlot.getOwnXAxisTickLabelColor();
		xAxisTickLabelMask = scattedPlot.getXAxisTickLabelMask();
		xAxisLineColor = scattedPlot.getOwnXAxisLineColor();
		
		yAxisLabelExpression = factory.getExpression( scattedPlot.getYAxisLabelExpression() );
		yAxisLabelFont = new JRBaseFont(scattedPlot.getChart(), scattedPlot.getYAxisLabelFont());
		yAxisLabelColor = scattedPlot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = new JRBaseFont(scattedPlot.getChart(), scattedPlot.getYAxisTickLabelFont());
		yAxisTickLabelColor = scattedPlot.getOwnYAxisTickLabelColor();
		yAxisTickLabelMask = scattedPlot.getYAxisTickLabelMask();
		yAxisLineColor = scattedPlot.getOwnYAxisLineColor();
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
	 * @deprecated Replaced by {@link #getShowShapes()}
	 */
	public boolean isShowShapes(){
		return showShapes == null ? true : showShapes.booleanValue();
	}
	
	/**
	 * 
	 */
	public Boolean getShowShapes(){
		return showShapes;
	}
	
	/**
	 * @deprecated Replaced by {@link #getShowLines()}
	 */
	public boolean isShowLines(){
		return showLines == null ? true : showLines.booleanValue();
	}
	
	/**
	 * 
	 */
	public Boolean getShowLines(){
		return showLines;
	}
	
	/**
	 * @deprecated Replaced by {@link #setShowShapes(Boolean)}
	 */
	public void setShowShapes( boolean value ){
		setShowShapes(Boolean.valueOf(value));
	}
	
	/**
	 * 
	 */
	public void setShowShapes( Boolean value ){
		Boolean old = this.showShapes;
		this.showShapes = value;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_SHAPES, old, this.showShapes);
	}
	
	/**
	 * @deprecated Replaced by {@link #setShowLines(Boolean)}
	 */
	public void setShowLines( boolean value ){
		setShowLines(Boolean.valueOf(value));
	}

	/**
	 * 
	 */
	public void setShowLines( Boolean value ){
		Boolean old = this.showLines;
		this.showLines = value;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LINES, old, this.showLines);
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		JRBaseScatterPlot clone = (JRBaseScatterPlot)super.clone(parentChart);
		if (xAxisLabelExpression != null)
		{
			clone.xAxisLabelExpression = (JRExpression)xAxisLabelExpression.clone();
		}
		if (yAxisLabelExpression != null)
		{
			clone.yAxisLabelExpression = (JRExpression)yAxisLabelExpression.clone();
		}
		return clone;
	}

	/**
	 * This field is only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3;
	private boolean isShowShapes = true;
	private boolean isShowLines = true;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3)
		{
			showShapes = Boolean.valueOf(isShowShapes);
			showLines = Boolean.valueOf(isShowLines);
		}
	}
	
}
