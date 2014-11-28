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

import net.sf.jasperreports.charts.JRLinePlot;
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
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public class JRBaseLinePlot extends JRBaseChartPlot implements JRLinePlot {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SHOW_LINES = "isShowLines";
	
	public static final String PROPERTY_SHOW_SHAPES = "isShowShapes";
	
	protected JRExpression categoryAxisLabelExpression;
	protected JRFont categoryAxisLabelFont;
	protected Color categoryAxisLabelColor;
	protected JRFont categoryAxisTickLabelFont;
	protected Color categoryAxisTickLabelColor;
	protected String categoryAxisTickLabelMask;
	protected Boolean categoryAxisVerticalTickLabels;
	protected Color categoryAxisLineColor;

	protected JRExpression valueAxisLabelExpression;
	protected JRExpression rangeAxisMinValueExpression;
	protected JRExpression rangeAxisMaxValueExpression;
	protected JRExpression domainAxisMinValueExpression;
	protected JRExpression domainAxisMaxValueExpression;
	protected JRFont valueAxisLabelFont;
	protected Color valueAxisLabelColor;
	protected JRFont valueAxisTickLabelFont;
	protected Color valueAxisTickLabelColor;
	protected String valueAxisTickLabelMask;
	protected Boolean valueAxisVerticalTickLabels;
	protected Color valueAxisLineColor;
	
	Boolean showShapes;
	Boolean showLines;
	
	
	/**
	 * 
	 */
	public JRBaseLinePlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRLinePlot linePlot = plot instanceof JRLinePlot ? (JRLinePlot)plot : null;
		if (linePlot != null)
		{
			categoryAxisLabelFont = linePlot.getCategoryAxisLabelFont(); 
			categoryAxisTickLabelFont = linePlot.getCategoryAxisTickLabelFont();
			valueAxisLabelFont = linePlot.getValueAxisLabelFont();
			valueAxisTickLabelFont = linePlot.getValueAxisTickLabelFont();
		}
	}
	
	/**
	 * 
	 */
	public JRBaseLinePlot(JRLinePlot linePlot, JRBaseObjectFactory factory )
	{
		super(linePlot, factory);
		
		showShapes = linePlot.getShowShapes();
		showLines = linePlot.getShowLines();
		
		categoryAxisLabelExpression = factory.getExpression( linePlot.getCategoryAxisLabelExpression() );
		categoryAxisLabelFont = factory.getFont(chart, linePlot.getCategoryAxisLabelFont());
		categoryAxisLabelColor = linePlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = factory.getFont(chart, linePlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = linePlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisTickLabelMask = linePlot.getCategoryAxisTickLabelMask();
		categoryAxisVerticalTickLabels = linePlot.getCategoryAxisVerticalTickLabels();
		categoryAxisLineColor = linePlot.getOwnCategoryAxisLineColor();
		labelRotationDouble = linePlot.getCategoryAxisTickLabelRotation();
		
		valueAxisLabelExpression = factory.getExpression( linePlot.getValueAxisLabelExpression() );
		domainAxisMinValueExpression = factory.getExpression( linePlot.getDomainAxisMinValueExpression() );
		domainAxisMaxValueExpression = factory.getExpression( linePlot.getDomainAxisMaxValueExpression() );
		rangeAxisMinValueExpression = factory.getExpression( linePlot.getRangeAxisMinValueExpression() );
		rangeAxisMaxValueExpression = factory.getExpression( linePlot.getRangeAxisMaxValueExpression() );
		valueAxisLabelFont = factory.getFont(chart, linePlot.getValueAxisLabelFont());
		valueAxisLabelColor = linePlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = factory.getFont(chart, linePlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = linePlot.getOwnValueAxisTickLabelColor();
		valueAxisTickLabelMask = linePlot.getValueAxisTickLabelMask();
		valueAxisVerticalTickLabels = linePlot.getValueAxisVerticalTickLabels();
		valueAxisLineColor = linePlot.getOwnValueAxisLineColor();
	}
	
	/**
	 * 
	 */
	public JRExpression getCategoryAxisLabelExpression(){
		return categoryAxisLabelExpression;
	}
	
	/**
	 * 
	 */
	public JRFont getCategoryAxisLabelFont()
	{
		return categoryAxisLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getCategoryAxisLabelColor()
	{
		return JRStyleResolver.getCategoryAxisLabelColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnCategoryAxisLabelColor()
	{
		return categoryAxisLabelColor;
	}
	
	/**
	 * 
	 */
	public JRFont getCategoryAxisTickLabelFont()
	{
		return categoryAxisTickLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getCategoryAxisTickLabelColor()
	{
		return JRStyleResolver.getCategoryAxisTickLabelColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnCategoryAxisTickLabelColor()
	{
		return categoryAxisTickLabelColor;
	}

	/**
	 * 
	 */
	public String getCategoryAxisTickLabelMask()
	{
		return categoryAxisTickLabelMask;
	}

	/**
	 * 
	 */
	public Boolean getCategoryAxisVerticalTickLabels()
	{
		return categoryAxisVerticalTickLabels;
	}

	/**
	 * 
	 */
	public Double getCategoryAxisTickLabelRotation()
	{
		return labelRotationDouble;
	}

	/**
	 * 
	 */
	public void setCategoryAxisTickLabelRotation(Double labelRotationDouble)
	{
		Object old = this.labelRotationDouble;
		this.labelRotationDouble = labelRotationDouble;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_TICK_LABEL_ROTATION, old, this.labelRotationDouble);
	}

	/**
	 * 
	 */
	public Color getCategoryAxisLineColor()
	{
		return JRStyleResolver.getCategoryAxisLineColor(this, this);
	}
		
	/**
	 * 
	 */
	public Color getOwnCategoryAxisLineColor()
	{
		return categoryAxisLineColor;
	}
		
	/**
	 * 
	 */
	public JRExpression getValueAxisLabelExpression(){
		return valueAxisLabelExpression;
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
	public JRFont getValueAxisLabelFont()
	{
		return valueAxisLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getValueAxisLabelColor()
	{
		return JRStyleResolver.getValueAxisLabelColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnValueAxisLabelColor()
	{
		return valueAxisLabelColor;
	}
	
	/**
	 * 
	 */
	public JRFont getValueAxisTickLabelFont()
	{
		return valueAxisTickLabelFont;
	}
	
	/**
	 * 
	 */
	public Color getValueAxisTickLabelColor()
	{
		return JRStyleResolver.getValueAxisTickLabelColor(this, this);
	}

	/**
	 * 
	 */
	public Color getOwnValueAxisTickLabelColor()
	{
		return valueAxisTickLabelColor;
	}

	/**
	 * 
	 */
	public String getValueAxisTickLabelMask()
	{
		return valueAxisTickLabelMask;
	}

	/**
	 * 
	 */
	public Boolean getValueAxisVerticalTickLabels()
	{
		return valueAxisVerticalTickLabels;
	}

	/**
	 * 
	 */
	public Color getValueAxisLineColor()
	{
		return JRStyleResolver.getValueAxisLineColor(this, this);
	}
	
	/**
	 * 
	 */
	public Color getOwnValueAxisLineColor()
	{
		return valueAxisLineColor;
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
		getEventSupport().firePropertyChange(PROPERTY_SHOW_SHAPES, old, this.showLines);
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
		JRBaseLinePlot clone = (JRBaseLinePlot)super.clone(parentChart);
		clone.categoryAxisLabelExpression = JRCloneUtils.nullSafeClone(categoryAxisLabelExpression);
		clone.valueAxisLabelExpression = JRCloneUtils.nullSafeClone(valueAxisLabelExpression);
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
