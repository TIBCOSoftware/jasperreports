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
package net.sf.jasperreports.charts.fill;

import java.awt.Color;

import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRFillBar3DPlot extends JRFillChartPlot implements JRBar3DPlot 
{
	
	/**
	 *
	 */
	protected JRFont categoryAxisLabelFont;
	protected Color categoryAxisLabelColor;
	protected JRFont categoryAxisTickLabelFont;
	protected Color categoryAxisTickLabelColor;
	protected Color categoryAxisLineColor;

	protected JRFont valueAxisLabelFont;
	protected Color valueAxisLabelColor;
	protected JRFont valueAxisTickLabelFont;
	protected Color valueAxisTickLabelColor;
	protected Color valueAxisLineColor;

	
	/**
	 *
	 */
	public JRFillBar3DPlot( JRBar3DPlot barPlot, JRFillObjectFactory factory ){
		super( barPlot, factory );

		categoryAxisLabelFont = factory.getFont(barPlot.getChart(), barPlot.getCategoryAxisLabelFont()); 
		categoryAxisLabelColor = barPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = factory.getFont(barPlot.getChart(), barPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = barPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisLineColor = barPlot.getOwnCategoryAxisLineColor();
		
		valueAxisLabelFont = factory.getFont(barPlot.getChart(), barPlot.getValueAxisLabelFont());
		valueAxisLabelColor = barPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = factory.getFont(barPlot.getChart(), barPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = barPlot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = barPlot.getOwnValueAxisLineColor();
	}
	
	@Override
	public JRExpression getCategoryAxisLabelExpression()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisLabelExpression();
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

	/**
	 *
	 */
	public void setCategoryAxisLabelColor(Color color)
	{
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

	/**
	 *
	 */
	public void setCategoryAxisTickLabelColor(Color color)
	{
	}

	@Override
	public String getCategoryAxisTickLabelMask()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisTickLabelMask();
	}

	@Override
	public Boolean getCategoryAxisVerticalTickLabels()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisVerticalTickLabels();
	}

	@Override
	public Double getCategoryAxisTickLabelRotation()
	{
		return ((JRBar3DPlot)parent).getCategoryAxisTickLabelRotation();
	}

	@Override
	public void setCategoryAxisTickLabelRotation(Double labelRotation)
	{
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

	/**
	 *
	 */
	public void setCategoryAxisLineColor(Color color)
	{
	}

	@Override
	public JRExpression getValueAxisLabelExpression()
	{
		return ((JRBar3DPlot)parent).getValueAxisLabelExpression();
	}

	@Override
	public JRExpression getDomainAxisMinValueExpression()
	{
		return ((JRBar3DPlot)parent).getDomainAxisMinValueExpression();
	}

	@Override
	public JRExpression getDomainAxisMaxValueExpression()
	{
		return ((JRBar3DPlot)parent).getDomainAxisMaxValueExpression();
	}

	@Override
	public JRExpression getRangeAxisMinValueExpression()
	{
		return ((JRBar3DPlot)parent).getRangeAxisMinValueExpression();
	}

	@Override
	public JRExpression getRangeAxisMaxValueExpression()
	{
		return ((JRBar3DPlot)parent).getRangeAxisMaxValueExpression();
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

	/**
	 *
	 */
	public void setValueAxisLabelColor(Color color)
	{
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

	/**
	 *
	 */
	public void setValueAxisTickLabelColor(Color color)
	{
	}

	@Override
	public String getValueAxisTickLabelMask()
	{
		return ((JRBar3DPlot)parent).getValueAxisTickLabelMask();
	}

	@Override
	public Boolean getValueAxisVerticalTickLabels()
	{
		return ((JRBar3DPlot)parent).getValueAxisVerticalTickLabels();
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

	/**
	 *
	 */
	public void setValueAxisLineColor(Color color)
	{
	}
	
	@Override
	public Double getXOffsetDouble(){
		return ((JRBar3DPlot)parent).getXOffsetDouble();
	}
	
	@Override
	public void setXOffset( Double xOffset ){
	}
	
	@Override
	public Double getYOffsetDouble(){
		return ((JRBar3DPlot)parent).getYOffsetDouble();
	}
	
	@Override
	public void setYOffset( Double yOffset ){
	}
	
	@Override
	public Boolean getShowLabels(){
		return ((JRBar3DPlot)parent).getShowLabels();
	}
	
	@Override
	public JRItemLabel getItemLabel()
	{
		return ((JRBar3DPlot)parent).getItemLabel();
	}

	/**
	 *
	 */
	public void setItemLabel( JRItemLabel itemLabel ){
	}
	
	@Override
	public void setShowLabels( Boolean isShowLabels ){
	}
}
