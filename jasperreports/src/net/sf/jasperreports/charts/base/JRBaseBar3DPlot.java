/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRItemLabel;
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

import org.jfree.chart.renderer.category.BarRenderer3D;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$ 
 */
public class JRBaseBar3DPlot extends JRBaseChartPlot implements JRBar3DPlot 
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SHOW_LABELS = "showLabels";
	
	public static final String PROPERTY_X_OFFSET = "xOffset";
	
	public static final String PROPERTY_Y_OFFSET = "yOffset";
	
	protected JRExpression categoryAxisLabelExpression = null;
	protected JRFont categoryAxisLabelFont = null;
	protected Color categoryAxisLabelColor = null;
	protected JRFont categoryAxisTickLabelFont = null;
	protected Color categoryAxisTickLabelColor = null;
	protected String categoryAxisTickLabelMask = null;
	protected Boolean categoryAxisVerticalTickLabels = null;
	protected Color categoryAxisLineColor = null;

	protected JRExpression valueAxisLabelExpression = null;
	protected JRExpression rangeAxisMinValueExpression = null;
	protected JRExpression rangeAxisMaxValueExpression = null;
	protected JRExpression domainAxisMinValueExpression = null;
	protected JRExpression domainAxisMaxValueExpression = null;
	protected JRFont valueAxisLabelFont = null;
	protected Color valueAxisLabelColor = null;
	protected JRFont valueAxisTickLabelFont = null;
	protected Color valueAxisTickLabelColor = null;
	protected String valueAxisTickLabelMask = null;
	protected Boolean valueAxisVerticalTickLabels = null;
	protected Color valueAxisLineColor = null;

	protected JRItemLabel itemLabel = null;
	
	private Double xOffsetDouble = null;
	private Double yOffsetDouble = null;
	private Boolean showLabels = null;
	
	/**
	 * 
	 */
	public JRBaseBar3DPlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRBar3DPlot barPlot = plot instanceof JRBar3DPlot ? (JRBar3DPlot)plot : null;
		if (barPlot == null)
		{
			categoryAxisLabelFont = new JRBaseFont(chart, null);
			categoryAxisTickLabelFont = new JRBaseFont(chart, null);
			valueAxisLabelFont = new JRBaseFont(chart, null);
			valueAxisTickLabelFont = new JRBaseFont(chart, null);
			itemLabel = new JRBaseItemLabel(null, chart);
		}
		else
		{
			categoryAxisLabelFont = new JRBaseFont(chart, barPlot.getCategoryAxisLabelFont());
			categoryAxisTickLabelFont = new JRBaseFont(chart, barPlot.getCategoryAxisTickLabelFont());
			valueAxisLabelFont = new JRBaseFont(chart, barPlot.getValueAxisLabelFont());
			valueAxisTickLabelFont = new JRBaseFont(chart, barPlot.getValueAxisTickLabelFont());
			itemLabel = new JRBaseItemLabel(barPlot.getItemLabel(), chart);
		}
	}


	/**
	 * 
	 */
	public JRBaseBar3DPlot(JRBar3DPlot barPlot, JRBaseObjectFactory factory )
	{
		super( barPlot, factory );
		
		xOffsetDouble = barPlot.getXOffsetDouble();
		yOffsetDouble = barPlot.getYOffsetDouble();
		showLabels = barPlot.getShowLabels();
		
		categoryAxisLabelExpression = factory.getExpression( barPlot.getCategoryAxisLabelExpression() );
		categoryAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisLabelFont());
		categoryAxisLabelColor = barPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = barPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisTickLabelMask = barPlot.getCategoryAxisTickLabelMask();
		categoryAxisVerticalTickLabels = barPlot.getCategoryAxisVerticalTickLabels();
		categoryAxisLineColor = barPlot.getOwnCategoryAxisLineColor();
		labelRotationDouble = barPlot.getCategoryAxisTickLabelRotation();
		
		valueAxisLabelExpression = factory.getExpression( barPlot.getValueAxisLabelExpression() );
		domainAxisMinValueExpression = factory.getExpression( barPlot.getDomainAxisMinValueExpression() );
		domainAxisMaxValueExpression = factory.getExpression( barPlot.getDomainAxisMaxValueExpression() );
		rangeAxisMinValueExpression = factory.getExpression( barPlot.getRangeAxisMinValueExpression() );
		rangeAxisMaxValueExpression = factory.getExpression( barPlot.getRangeAxisMaxValueExpression() );
		valueAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisLabelFont());
		valueAxisLabelColor = barPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = barPlot.getOwnValueAxisTickLabelColor();
		valueAxisTickLabelMask = barPlot.getValueAxisTickLabelMask();
		valueAxisVerticalTickLabels = barPlot.getValueAxisVerticalTickLabels();
		valueAxisLineColor = barPlot.getOwnValueAxisLineColor();
		itemLabel = new JRBaseItemLabel(barPlot.getItemLabel(), factory);
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
	 * @deprecated Replaced by {@link #getXOffsetDouble()}
	 */
	public double getXOffset(){
		return getXOffsetDouble() == null ? BarRenderer3D.DEFAULT_X_OFFSET : getXOffsetDouble().doubleValue();
	}
	
	/**
	 * 
	 */
	public Double getXOffsetDouble(){
		return xOffsetDouble;
	}
	
	/**
	 * @deprecated Replaced by {@link #setXOffset(Double)}
	 */
	public void setXOffset( double xOffset ){
		setXOffset(new Double(xOffset));
	}
	
	/**
	 *
	 */
	public JRItemLabel getItemLabel()
	{
		return itemLabel;
	}
	
	/**
	 * 
	 */
	public void setXOffset( Double xOffset ){
		Double old = this.xOffsetDouble;
		this.xOffsetDouble = xOffset;
		getEventSupport().firePropertyChange(PROPERTY_X_OFFSET, old, this.xOffsetDouble);
	}
	
	/**
	 * @deprecated Replaced by {@link #getYOffsetDouble()}
	 */
	public double getYOffset(){
		return getYOffsetDouble() == null ? BarRenderer3D.DEFAULT_Y_OFFSET : getYOffsetDouble().doubleValue();
	}
	
	/**
	 * 
	 */
	public Double getYOffsetDouble(){
		return yOffsetDouble;
	}
	
	/**
	 * @deprecated Replaced by {@link #setYOffset(Double)}
	 */
	public void setYOffset( double yOffset ){
		setYOffset(new Double(yOffset));
	}
	
	/**
	 * 
	 */
	public void setYOffset( Double yOffset ){
		Double old = this.yOffsetDouble;
		this.yOffsetDouble = yOffset;
		getEventSupport().firePropertyChange(PROPERTY_Y_OFFSET, old, this.yOffsetDouble);
	}
	
	/**
	 * @deprecated Replaced by {@link #getShowLabels()} 
	 */
	public boolean isShowLabels(){
		return showLabels == null ? false : showLabels.booleanValue();
	}
	
	/**
	 * 
	 */
	public Boolean getShowLabels(){
		return showLabels;
	}
	
	/**
	 * @deprecated Replaced by {@link #setShowLabels(Boolean)} 
	 */
	public void setShowLabels( boolean isShowLabels ){
		setShowLabels(Boolean.valueOf(isShowLabels));
	}

	/**
	 * 
	 */
	public void setShowLabels( Boolean showLabels ){
		Boolean old = this.showLabels;
		this.showLabels = showLabels;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LABELS, old, this.showLabels);
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
		JRBaseBar3DPlot clone = (JRBaseBar3DPlot)super.clone(parentChart);
		if (categoryAxisLabelExpression != null)
		{
			clone.categoryAxisLabelExpression = (JRExpression)categoryAxisLabelExpression.clone();
		}
		if (valueAxisLabelExpression != null)
		{
			clone.valueAxisLabelExpression = (JRExpression)valueAxisLabelExpression.clone();
		}
		if (domainAxisMinValueExpression != null)
		{
			clone.domainAxisMinValueExpression = (JRExpression)domainAxisMinValueExpression.clone();
		}
		if (domainAxisMaxValueExpression != null)
		{
			clone.domainAxisMaxValueExpression = (JRExpression)domainAxisMaxValueExpression.clone();
		}
		if (rangeAxisMinValueExpression != null)
		{
			clone.rangeAxisMinValueExpression = (JRExpression)rangeAxisMinValueExpression.clone();
		}
		if (rangeAxisMaxValueExpression != null)
		{
			clone.rangeAxisMaxValueExpression = (JRExpression)rangeAxisMaxValueExpression.clone();
		}
		if (itemLabel != null)
		{
			clone.itemLabel = (JRItemLabel)itemLabel.clone();
		}
		
		return clone;
	}


	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private double xOffset = BarRenderer3D.DEFAULT_X_OFFSET;
	/**
	 * @deprecated
	 */
	private double yOffset = BarRenderer3D.DEFAULT_Y_OFFSET;
	/**
	 * @deprecated
	 */
	private boolean isShowLabels = false;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0)
		{
			xOffsetDouble = new Double(xOffset);
			yOffsetDouble = new Double(yOffset);
			showLabels = Boolean.valueOf(isShowLabels);
		}
	}
}
