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
package net.sf.jasperreports.charts.fill;

import java.awt.Color;

import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillBarPlot extends JRFillChartPlot implements JRBarPlot
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
	public JRFillBarPlot(
		JRBarPlot barPlot, 
		JRFillObjectFactory factory
		)
	{
		super(barPlot, factory);

		categoryAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisLabelFont()); 
		categoryAxisLabelColor = barPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = barPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisLineColor = barPlot.getOwnCategoryAxisLineColor();
		
		valueAxisLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisLabelFont());
		valueAxisLabelColor = barPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(barPlot.getChart(), barPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = barPlot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = barPlot.getOwnValueAxisLineColor();
		
	}
		

	/**
	 *
	 */
	public JRExpression getCategoryAxisLabelExpression()
	{
		return ((JRBarPlot)parent).getCategoryAxisLabelExpression();
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
	public void setCategoryAxisLabelColor(Color color)
	{
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
	public JRItemLabel getItemLabel()
	{
		return ((JRBarPlot)parent).getItemLabel();
	}
	/**
	 *
	 */
	public void setItemLabel( JRItemLabel itemLabel ){
	}
	
	/**
	 *
	 */
	public void setCategoryAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getCategoryAxisTickLabelMask()
	{
		return ((JRBarPlot)parent).getCategoryAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getCategoryAxisVerticalTickLabels()
	{
		return ((JRBarPlot)parent).getCategoryAxisVerticalTickLabels();
	}

	/**
	 * 
	 */
	public Double getCategoryAxisTickLabelRotation()
	{
		return ((JRBarPlot)parent).getCategoryAxisTickLabelRotation();
	}

	/**
	 * 
	 */
	public void setCategoryAxisTickLabelRotation(Double labelRotation)
	{
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
	public void setCategoryAxisLineColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRExpression getValueAxisLabelExpression()
	{
		return ((JRBarPlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMinValueExpression()
	{
		return ((JRBarPlot)parent).getDomainAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMaxValueExpression()
	{
		return ((JRBarPlot)parent).getDomainAxisMaxValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMinValueExpression()
	{
		return ((JRBarPlot)parent).getRangeAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMaxValueExpression()
	{
		return ((JRBarPlot)parent).getRangeAxisMaxValueExpression();
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
	public void setValueAxisLabelColor(Color color)
	{
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
	public void setValueAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getValueAxisTickLabelMask()
	{
		return ((JRBarPlot)parent).getValueAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getValueAxisVerticalTickLabels()
	{
		return ((JRBarPlot)parent).getValueAxisVerticalTickLabels();
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
	public void setValueAxisLineColor(Color color)
	{
	}

	/**
	 * @deprecated Replaced by {@link #getShowTickMarks()} 
	 */
	public boolean isShowTickMarks()
	{
		return ((JRBarPlot)parent).isShowTickMarks();
	}
		
	/**
	 *
	 */
	public Boolean getShowTickMarks()
	{
		return ((JRBarPlot)parent).getShowTickMarks();
	}
		
	/**
	 * @deprecated Replaced by {@link #setShowTickMarks(Boolean)}.
	 */
	public void setShowTickMarks(boolean isShowTickMarks)
	{
	}
		
	/**
	 *
	 */
	public void setShowTickMarks(Boolean isShowTickMarks)
	{
	}
		
	/**
	 * @deprecated Replaced by {@link #getShowTickLabels()}
	 */
	public boolean isShowTickLabels()
	{
		return ((JRBarPlot)parent).isShowTickLabels();
	}
		
	/**
	 *
	 */
	public Boolean getShowTickLabels()
	{
		return ((JRBarPlot)parent).getShowTickLabels();
	}
		
	/**
	 * @deprecated Replaced by {@link #setShowTickLabels(Boolean)}.
	 */
	public void setShowTickLabels(boolean isShowTickLabels)
	{
	}

	/**
	 *
	 */
	public void setShowTickLabels(Boolean isShowTickLabels)
	{
	}

	/**
	 * @deprecated Replaced by {@link #getShowLabels()}
	 */
	public boolean isShowLabels(){
		return ((JRBarPlot)parent).isShowLabels();
	}
	
	/**
	 *
	 */
	public Boolean getShowLabels(){
		return ((JRBarPlot)parent).getShowLabels();
	}
	
	/**
	 * @deprecated Replaced by {@link #setShowLabels(Boolean)}.
	 */
	public void setShowLabels( boolean isShowLabels ){
	}
	/**
	 *
	 */
	public void setShowLabels( Boolean isShowLabels ){
	}
	
}
