/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$ 
 */
public class JRFillAreaPlot extends JRFillChartPlot implements JRAreaPlot 
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
	public JRFillAreaPlot( JRAreaPlot areaPlot, JRFillObjectFactory factory )
	{
		super( areaPlot, factory ); 

		categoryAxisLabelFont = factory.getFont(areaPlot.getChart(), areaPlot.getCategoryAxisLabelFont()); 
		categoryAxisLabelColor = areaPlot.getOwnCategoryAxisLabelColor();
		categoryAxisTickLabelFont = factory.getFont(areaPlot.getChart(), areaPlot.getCategoryAxisTickLabelFont());
		categoryAxisTickLabelColor = areaPlot.getOwnCategoryAxisTickLabelColor();
		categoryAxisLineColor = areaPlot.getOwnCategoryAxisLineColor();
		
		valueAxisLabelFont = factory.getFont(areaPlot.getChart(), areaPlot.getValueAxisLabelFont());
		valueAxisLabelColor = areaPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = factory.getFont(areaPlot.getChart(), areaPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = areaPlot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = areaPlot.getOwnValueAxisLineColor();
	}
	
	/**
	 *
	 */
	public JRExpression getCategoryAxisLabelExpression()
	{
		return ((JRAreaPlot)parent).getCategoryAxisLabelExpression();
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
	public void setCategoryAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getCategoryAxisTickLabelMask()
	{
		return ((JRAreaPlot)parent).getCategoryAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getCategoryAxisVerticalTickLabels()
	{
		return ((JRAreaPlot)parent).getCategoryAxisVerticalTickLabels();
	}

	/**
	 * 
	 */
	public Double getCategoryAxisTickLabelRotation()
	{
		return ((JRAreaPlot)parent).getCategoryAxisTickLabelRotation();
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
		return ((JRAreaPlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMinValueExpression()
	{
		return ((JRAreaPlot)parent).getDomainAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMaxValueExpression()
	{
		return ((JRAreaPlot)parent).getDomainAxisMaxValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMinValueExpression()
	{
		return ((JRAreaPlot)parent).getRangeAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMaxValueExpression()
	{
		return ((JRAreaPlot)parent).getRangeAxisMaxValueExpression();
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
		return ((JRAreaPlot)parent).getValueAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getValueAxisVerticalTickLabels()
	{
		return ((JRAreaPlot)parent).getValueAxisVerticalTickLabels();
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
}
