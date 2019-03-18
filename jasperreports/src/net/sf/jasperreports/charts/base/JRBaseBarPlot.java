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

import net.sf.jasperreports.charts.ChartCopyObjectFactory;
import net.sf.jasperreports.charts.JRBarPlot;
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
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRBaseBarPlot extends JRBaseChartPlot implements JRBarPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_SHOW_LABELS = "isShowLabels";

	public static final String PROPERTY_SHOW_TICK_LABELS = "isShowTickLabels";

	public static final String PROPERTY_SHOW_TICK_MARKS = "isShowTickMarks";

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

	protected Boolean showTickMarks;
	protected Boolean showTickLabels;
	protected Boolean showLabels;
	
	protected JRItemLabel itemLabel;


	/**
	 *
	 */
	public JRBaseBarPlot(JRChartPlot plot, JRChart chart)
	{
		this(plot, chart, ChartCopyBaseObjectFactory.instance());
	}

	protected JRBaseBarPlot(JRChartPlot plot, JRChart chart, ChartCopyObjectFactory copyObjectFactory)
	{
		super(plot, chart);
		
		JRBarPlot barPlot = plot instanceof JRBarPlot ? (JRBarPlot)plot : null;
		if (barPlot == null)
		{
			itemLabel = copyObjectFactory.copyItemLabel(null, chart);
		}
		else
		{
			categoryAxisLabelFont = barPlot.getCategoryAxisLabelFont();
			categoryAxisTickLabelFont = barPlot.getCategoryAxisTickLabelFont();
			valueAxisLabelFont = barPlot.getValueAxisLabelFont();
			valueAxisTickLabelFont = barPlot.getValueAxisTickLabelFont();
			itemLabel = copyObjectFactory.copyItemLabel(barPlot.getItemLabel(), chart);
		}
	}

	/**
	 *
	 */
	public JRBaseBarPlot(JRBarPlot barPlot, JRBaseObjectFactory factory)
	{
		super(barPlot, factory);

		showTickMarks = barPlot.getShowTickMarks();
		showTickLabels = barPlot.getShowTickLabels();
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
	public Boolean getShowLabels(){
		return showLabels;
	}

	@Override
	public JRItemLabel getItemLabel()
	{
		return itemLabel;
	}
	
	@Override
	public void setShowLabels( Boolean showLabels ){
		Boolean old = this.showLabels;
		this.showLabels = showLabels;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LABELS, old, this.showLabels);
	}


	@Override
	public Boolean getShowTickMarks()
	{
		return showTickMarks;
	}

	@Override
	public void setShowTickMarks(Boolean isShowTickMarks)
	{
		Boolean old = this.showTickMarks;
		this.showTickMarks = isShowTickMarks;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_TICK_MARKS, old, this.showTickMarks);
	}

	@Override
	public Boolean getShowTickLabels()
	{
		return showTickLabels;
	}

	@Override
	public void setShowTickLabels(Boolean showTickLabels)
	{
		Boolean old = this.showTickLabels;
		this.showTickLabels = showTickLabels;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_TICK_LABELS, old, this.showTickLabels);
	}

	@Override
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	@Override
	public Object clone(JRChart parentChart)
	{
		JRBaseBarPlot clone = (JRBaseBarPlot)super.clone(parentChart);
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
	private boolean isShowTickMarks;
	/**
	 * @deprecated
	 */
	private boolean isShowTickLabels = true;
	/**
	 * @deprecated
	 */
	private boolean isShowLabels = true;


	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_0)
		{
			showTickMarks = isShowTickMarks;
			showTickLabels = isShowTickLabels;
			showLabels = isShowLabels;
		}
	}

}
