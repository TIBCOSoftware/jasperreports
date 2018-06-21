/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
	
	@Override
	public JRExpression getXAxisLabelExpression(){
		return xAxisLabelExpression;
	}
	
	@Override
	public JRFont getXAxisLabelFont()
	{
		return xAxisLabelFont;
	}
	
	@Override
	public Color getXAxisLabelColor()
	{
		return getStyleResolver().getXAxisLabelColor(this, this);
	}
		
	@Override
	public Color getOwnXAxisLabelColor()
	{
		return xAxisLabelColor;
	}
		
	@Override
	public JRFont getXAxisTickLabelFont()
	{
		return xAxisTickLabelFont;
	}
	
	@Override
	public Color getXAxisTickLabelColor()
	{
		return getStyleResolver().getXAxisTickLabelColor(this, this);
	}

	@Override
	public Color getOwnXAxisTickLabelColor()
	{
		return xAxisTickLabelColor;
	}

	@Override
	public String getXAxisTickLabelMask()
	{
		return xAxisTickLabelMask;
	}
	
	@Override
	public Boolean getXAxisVerticalTickLabels()
	{
		return xAxisVerticalTickLabels;
	}
	
	@Override
	public Color getXAxisLineColor()
	{
		return getStyleResolver().getXAxisLineColor(this, this);
	}

	@Override
	public Color getOwnXAxisLineColor()
	{
		return xAxisLineColor;
	}

	@Override
	public JRExpression getYAxisLabelExpression() {
		return yAxisLabelExpression;
	}
	
	@Override
	public JRFont getYAxisLabelFont()
	{
		return yAxisLabelFont;
	}
	
	@Override
	public Color getYAxisLabelColor()
	{
		return getStyleResolver().getYAxisLabelColor(this, this);
	}
	
	@Override
	public Color getOwnYAxisLabelColor()
	{
		return yAxisLabelColor;
	}
	
	@Override
	public JRFont getYAxisTickLabelFont()
	{
		return yAxisTickLabelFont;
	}
	
	@Override
	public Color getYAxisTickLabelColor()
	{
		return getStyleResolver().getYAxisTickLabelColor(this, this);
	}
	
	@Override
	public Color getOwnYAxisTickLabelColor()
	{
		return yAxisTickLabelColor;
	}
	
	@Override
	public String getYAxisTickLabelMask()
	{
		return yAxisTickLabelMask;
	}

	@Override
	public Boolean getYAxisVerticalTickLabels()
	{
		return yAxisVerticalTickLabels;
	}

	@Override
	public Color getYAxisLineColor()
	{
		return getStyleResolver().getYAxisLineColor(this, this);
	}
	
	@Override
	public Color getOwnYAxisLineColor()
	{
		return yAxisLineColor;
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
	public JRExpression getDomainAxisMinValueExpression(){
		return domainAxisMinValueExpression;
	}

	@Override
	public JRExpression getDomainAxisMaxValueExpression(){
		return domainAxisMaxValueExpression;
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
	public void setShowShapes( Boolean value ){
		Boolean old = this.showShapes;
		this.showShapes = value;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_SHAPES, old, this.showShapes);
	}
	
	@Override
	public void setShowLines( Boolean value ){
		Boolean old = this.showLines;
		this.showLines = value;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LINES, old, this.showLines);
	}

	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	@Override
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
