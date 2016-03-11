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
	
	@Override
	public JRExpression getCategoryAxisLabelExpression(){
		return categoryAxisLabelExpression;
	}
	
	@Override
	public JRFont getCategoryAxisLabelFont()
	{
		return categoryAxisLabelFont;
	}
	
	@Override
	public Color getCategoryAxisLabelColor()
	{
		return getStyleResolver().getCategoryAxisLabelColor(this, this);
	}
	
	@Override
	public Color getOwnCategoryAxisLabelColor()
	{
		return categoryAxisLabelColor;
	}
	
	@Override
	public JRFont getCategoryAxisTickLabelFont()
	{
		return categoryAxisTickLabelFont;
	}
	
	@Override
	public Color getCategoryAxisTickLabelColor()
	{
		return getStyleResolver().getCategoryAxisTickLabelColor(this, this);
	}

	@Override
	public Color getOwnCategoryAxisTickLabelColor()
	{
		return categoryAxisTickLabelColor;
	}

	@Override
	public String getCategoryAxisTickLabelMask()
	{
		return categoryAxisTickLabelMask;
	}

	@Override
	public Boolean getCategoryAxisVerticalTickLabels()
	{
		return categoryAxisVerticalTickLabels;
	}

	@Override
	public Double getCategoryAxisTickLabelRotation()
	{
		return labelRotationDouble;
	}

	@Override
	public void setCategoryAxisTickLabelRotation(Double labelRotationDouble)
	{
		Object old = this.labelRotationDouble;
		this.labelRotationDouble = labelRotationDouble;
		getEventSupport().firePropertyChange(PROPERTY_CATEGORY_AXIS_TICK_LABEL_ROTATION, old, this.labelRotationDouble);
	}

	@Override
	public Color getCategoryAxisLineColor()
	{
		return getStyleResolver().getCategoryAxisLineColor(this, this);
	}
		
	@Override
	public Color getOwnCategoryAxisLineColor()
	{
		return categoryAxisLineColor;
	}
		
	@Override
	public JRExpression getValueAxisLabelExpression(){
		return valueAxisLabelExpression;
	}

	@Override
	public JRExpression getRangeAxisMinValueExpression(){
		return rangeAxisMinValueExpression;
	}

	@Override
	public JRExpression getRangeAxisMaxValueExpression(){
		return rangeAxisMaxValueExpression;
	}

	@Override
	public JRExpression getDomainAxisMinValueExpression(){
		return domainAxisMinValueExpression;
	}

	@Override
	public JRExpression getDomainAxisMaxValueExpression(){
		return domainAxisMaxValueExpression;
	}

	@Override
	public JRFont getValueAxisLabelFont()
	{
		return valueAxisLabelFont;
	}
	
	@Override
	public Color getValueAxisLabelColor()
	{
		return getStyleResolver().getValueAxisLabelColor(this, this);
	}
	
	@Override
	public Color getOwnValueAxisLabelColor()
	{
		return valueAxisLabelColor;
	}
	
	@Override
	public JRFont getValueAxisTickLabelFont()
	{
		return valueAxisTickLabelFont;
	}
	
	@Override
	public Color getValueAxisTickLabelColor()
	{
		return getStyleResolver().getValueAxisTickLabelColor(this, this);
	}

	@Override
	public Color getOwnValueAxisTickLabelColor()
	{
		return valueAxisTickLabelColor;
	}

	@Override
	public String getValueAxisTickLabelMask()
	{
		return valueAxisTickLabelMask;
	}

	@Override
	public Boolean getValueAxisVerticalTickLabels()
	{
		return valueAxisVerticalTickLabels;
	}

	@Override
	public Color getValueAxisLineColor()
	{
		return getStyleResolver().getValueAxisLineColor(this, this);
	}
	
	@Override
	public Color getOwnValueAxisLineColor()
	{
		return valueAxisLineColor;
	}
	
	@Override
	public Boolean getShowShapes(){
		return showShapes;
	}
	
	@Override
	public Boolean getShowLines(){
		return showLines;
	}
	
	@Override
	public void setShowShapes( Boolean value ){
		Boolean old = this.showShapes;
		this.showShapes = value;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_SHAPES, old, this.showShapes);
	}
	
	@Override
	public void setShowLines( Boolean value ){
		Boolean old = this.showLines;
		this.showLines = value;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_SHAPES, old, this.showLines);
	}

	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	@Override
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
