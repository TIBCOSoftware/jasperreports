/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import org.jfree.chart.renderer.category.BarRenderer3D;

import net.sf.jasperreports.charts.ChartCopyObjectFactory;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRItemLabel;
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
public class JRBaseBar3DPlot extends JRBaseChartPlot implements JRBar3DPlot 
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_SHOW_LABELS = "isShowLabels";
	
	public static final String PROPERTY_X_OFFSET = "xOffset";
	
	public static final String PROPERTY_Y_OFFSET = "yOffset";
	
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

	protected JRItemLabel itemLabel;
	
	private Double xOffsetDouble;
	private Double yOffsetDouble;
	private Boolean showLabels;
	
	/**
	 * 
	 */
	public JRBaseBar3DPlot(JRChartPlot plot, JRChart chart)
	{
		this(plot, chart, ChartCopyBaseObjectFactory.instance());
	}
	
	protected JRBaseBar3DPlot(JRChartPlot plot, JRChart chart, ChartCopyObjectFactory copyObjectFactory)
	{
		super(plot, chart);
		
		JRBar3DPlot barPlot = plot instanceof JRBar3DPlot ? (JRBar3DPlot)plot : null;
		if (barPlot == null)
		{
			itemLabel = copyObjectFactory.copyItemLabel(null, chart);
		}
		else
		{
			categoryAxisLabelFont = barPlot.getCategoryAxisLabelFont();
			categoryAxisTickLabelFont = getCategoryAxisTickLabelFont();
			valueAxisLabelFont = barPlot.getValueAxisLabelFont();
			valueAxisTickLabelFont = barPlot.getValueAxisTickLabelFont();
			itemLabel = copyObjectFactory.copyItemLabel(barPlot.getItemLabel(), chart);
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
		categoryAxisLabelFont = factory.getFont(chart, barPlot.getCategoryAxisLabelFont());
		categoryAxisLabelColor = barPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = factory.getFont(chart, barPlot.getCategoryAxisTickLabelFont());
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
		valueAxisLabelFont = factory.getFont(chart, barPlot.getValueAxisLabelFont());
		valueAxisLabelColor = barPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = factory.getFont(chart, barPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = barPlot.getOwnValueAxisTickLabelColor();
		valueAxisTickLabelMask = barPlot.getValueAxisTickLabelMask();
		valueAxisVerticalTickLabels = barPlot.getValueAxisVerticalTickLabels();
		valueAxisLineColor = barPlot.getOwnValueAxisLineColor();
		itemLabel = new JRBaseItemLabel(barPlot.getItemLabel(), factory);
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
	public Double getXOffsetDouble(){
		return xOffsetDouble;
	}
	
	@Override
	public JRItemLabel getItemLabel()
	{
		return itemLabel;
	}
	
	@Override
	public void setXOffset( Double xOffset ){
		Double old = this.xOffsetDouble;
		this.xOffsetDouble = xOffset;
		getEventSupport().firePropertyChange(PROPERTY_X_OFFSET, old, this.xOffsetDouble);
	}
	
	@Override
	public Double getYOffsetDouble(){
		return yOffsetDouble;
	}
	
	@Override
	public void setYOffset( Double yOffset ){
		Double old = this.yOffsetDouble;
		this.yOffsetDouble = yOffset;
		getEventSupport().firePropertyChange(PROPERTY_Y_OFFSET, old, this.yOffsetDouble);
	}
	
	@Override
	public Boolean getShowLabels(){
		return showLabels;
	}
	
	@Override
	public void setShowLabels( Boolean showLabels ){
		Boolean old = this.showLabels;
		this.showLabels = showLabels;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LABELS, old, this.showLabels);
	}

	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	@Override
	public Object clone(JRChart parentChart) 
	{
		JRBaseBar3DPlot clone = (JRBaseBar3DPlot)super.clone(parentChart);
		clone.categoryAxisLabelExpression = JRCloneUtils.nullSafeClone(categoryAxisLabelExpression);
		clone.valueAxisLabelExpression = JRCloneUtils.nullSafeClone(valueAxisLabelExpression);
		clone.domainAxisMinValueExpression = JRCloneUtils.nullSafeClone(domainAxisMinValueExpression);
		clone.domainAxisMaxValueExpression = JRCloneUtils.nullSafeClone(domainAxisMaxValueExpression);
		clone.rangeAxisMinValueExpression = JRCloneUtils.nullSafeClone(rangeAxisMinValueExpression);
		clone.rangeAxisMaxValueExpression = JRCloneUtils.nullSafeClone(rangeAxisMaxValueExpression);
		clone.itemLabel = itemLabel == null ? null : itemLabel.clone(parentChart);
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
	private boolean isShowLabels;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0)
		{
			xOffsetDouble = xOffset;
			yOffsetDouble = yOffset;
			showLabels = isShowLabels;
		}
	}
}
