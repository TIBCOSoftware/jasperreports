/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseScatterPlot extends JRBaseChartPlot implements JRScatterPlot {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SHOW_LINES = "isShowLines";
	
	public static final String PROPERTY_SHOW_SHAPES = "isShowShapes";
	
	protected JRExpression xAxisLabelExpression;
	protected JRFont xAxisLabelFont;
	protected Color xAxisLabelColor;
	protected JRFont xAxisTickLabelFont;
	protected Color xAxisTickLabelColor;
	protected String xAxisTickLabelMask;
	protected Boolean xAxisVerticalTickLabels;
	protected Color xAxisLineColor;

	protected JRExpression yAxisLabelExpression;
	protected JRFont yAxisLabelFont;
	protected Color yAxisLabelColor;
	protected JRFont yAxisTickLabelFont;
	protected Color yAxisTickLabelColor;
	protected String yAxisTickLabelMask;
	protected Boolean yAxisVerticalTickLabels;
	protected Color yAxisLineColor;
	
	protected JRExpression domainAxisMinValueExpression;
	protected JRExpression domainAxisMaxValueExpression;
	protected JRExpression rangeAxisMinValueExpression;
	protected JRExpression rangeAxisMaxValueExpression;
	
	Boolean showShapes;
	Boolean showLines;
	
	
	/**
	 * 
	 */
	public JRBaseScatterPlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRScatterPlot scatterPlot = plot instanceof JRScatterPlot ? (JRScatterPlot)plot : null;
		if (scatterPlot != null)
		{
			xAxisLabelFont = scatterPlot.getXAxisLabelFont();
			xAxisTickLabelFont = scatterPlot.getXAxisTickLabelFont();
			yAxisLabelFont = scatterPlot.getYAxisLabelFont();
			yAxisTickLabelFont = scatterPlot.getYAxisTickLabelFont();
		}
	}

	/**
	 * 
	 */
	public JRBaseScatterPlot(JRScatterPlot scatterPlot, JRBaseObjectFactory factory )
	{
		super(scatterPlot, factory );
		
		showShapes = scatterPlot.getShowShapes();
		showLines = scatterPlot.getShowLines();
		
		xAxisLabelExpression = factory.getExpression( scatterPlot.getXAxisLabelExpression() );
		xAxisLabelFont = factory.getFont(chart, scatterPlot.getXAxisLabelFont());
		xAxisLabelColor = scatterPlot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = factory.getFont(chart, scatterPlot.getXAxisTickLabelFont());
		xAxisTickLabelColor = scatterPlot.getOwnXAxisTickLabelColor();
		xAxisTickLabelMask = scatterPlot.getXAxisTickLabelMask();
		xAxisVerticalTickLabels = scatterPlot.getXAxisVerticalTickLabels();
		xAxisLineColor = scatterPlot.getOwnXAxisLineColor();
		
		yAxisLabelExpression = factory.getExpression( scatterPlot.getYAxisLabelExpression() );
		yAxisLabelFont = factory.getFont(chart, scatterPlot.getYAxisLabelFont());
		yAxisLabelColor = scatterPlot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = factory.getFont(chart, scatterPlot.getYAxisTickLabelFont());
		yAxisTickLabelColor = scatterPlot.getOwnYAxisTickLabelColor();
		yAxisTickLabelMask = scatterPlot.getYAxisTickLabelMask();
		yAxisVerticalTickLabels = scatterPlot.getYAxisVerticalTickLabels();
		yAxisLineColor = scatterPlot.getOwnYAxisLineColor();
		
		domainAxisMinValueExpression = factory.getExpression( scatterPlot.getDomainAxisMinValueExpression() );
		domainAxisMaxValueExpression = factory.getExpression( scatterPlot.getDomainAxisMaxValueExpression() );
		rangeAxisMinValueExpression = factory.getExpression( scatterPlot.getRangeAxisMinValueExpression() );
		rangeAxisMaxValueExpression = factory.getExpression( scatterPlot.getRangeAxisMaxValueExpression() );
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
	public Boolean getXAxisVerticalTickLabels()
	{
		return xAxisVerticalTickLabels;
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
	public Boolean getYAxisVerticalTickLabels()
	{
		return yAxisVerticalTickLabels;
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
		return showShapes;
	}
	
	/**
	 * 
	 */
	public Boolean getShowLines(){
		return showLines;
	}
	
	/**
	 * 
	 */
	public JRExpression getDomainAxisMinValueExpression(){
		return domainAxisMinValueExpression;
	}

	/**
	 * 
	 */
	public JRExpression getDomainAxisMaxValueExpression(){
		return domainAxisMaxValueExpression;
	}

	/**
	 * 
	 */
	public JRExpression getRangeAxisMinValueExpression(){
		return rangeAxisMinValueExpression;
	}

	/**
	 * 
	 */
	public JRExpression getRangeAxisMaxValueExpression(){
		return rangeAxisMaxValueExpression;
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
		clone.xAxisLabelExpression = JRCloneUtils.nullSafeClone(xAxisLabelExpression);
		clone.yAxisLabelExpression = JRCloneUtils.nullSafeClone(yAxisLabelExpression);
		clone.domainAxisMinValueExpression = JRCloneUtils.nullSafeClone(domainAxisMinValueExpression);
		clone.domainAxisMaxValueExpression = JRCloneUtils.nullSafeClone(domainAxisMaxValueExpression);
		clone.rangeAxisMinValueExpression = JRCloneUtils.nullSafeClone(rangeAxisMinValueExpression);
		clone.rangeAxisMaxValueExpression = JRCloneUtils.nullSafeClone(rangeAxisMaxValueExpression);
		return clone;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private boolean isShowShapes = true;
	/**
	 * @deprecated
	 */
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
