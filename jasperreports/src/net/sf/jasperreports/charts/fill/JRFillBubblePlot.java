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
package net.sf.jasperreports.charts.fill;

import java.awt.Color;

import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.type.ScaleTypeEnum;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 */
public class JRFillBubblePlot extends JRFillChartPlot implements JRBubblePlot 
{

	/**
	 *
	 */
	protected JRFont xAxisLabelFont;
	protected Color xAxisLabelColor;
	protected JRFont xAxisTickLabelFont;
	protected Color xAxisTickLabelColor;
	protected Color xAxisLineColor;

	protected JRFont yAxisLabelFont;
	protected Color yAxisLabelColor;
	protected JRFont yAxisTickLabelFont;
	protected Color yAxisTickLabelColor;
	protected Color yAxisLineColor;

	
	/**
	 *
	 */
	public JRFillBubblePlot( JRBubblePlot bubblePlot, JRFillObjectFactory factory ){
		super( bubblePlot, factory );
		
		xAxisLabelFont = factory.getFont(bubblePlot.getChart(), bubblePlot.getXAxisLabelFont());
		xAxisLabelColor = bubblePlot.getOwnXAxisLabelColor();
		xAxisTickLabelFont = factory.getFont(bubblePlot.getChart(), bubblePlot.getXAxisTickLabelFont());
		xAxisTickLabelColor = bubblePlot.getOwnXAxisTickLabelColor();
		xAxisLineColor = bubblePlot.getOwnXAxisLineColor();
		
		yAxisLabelFont = factory.getFont(bubblePlot.getChart(), bubblePlot.getYAxisLabelFont());
		yAxisLabelColor = bubblePlot.getOwnYAxisLabelColor();
		yAxisTickLabelFont = factory.getFont(bubblePlot.getChart(), bubblePlot.getYAxisTickLabelFont());
		yAxisTickLabelColor = bubblePlot.getOwnYAxisTickLabelColor();
		yAxisLineColor = bubblePlot.getOwnYAxisLineColor();
	}
	
	/**
	 *
	 */
	public JRExpression getXAxisLabelExpression()
	{
		return ((JRBubblePlot)parent).getXAxisLabelExpression();
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
		return ((JRBubblePlot)parent).getXAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getXAxisVerticalTickLabels()
	{
		return ((JRBubblePlot)parent).getXAxisVerticalTickLabels();
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
	public JRExpression getYAxisLabelExpression()
	{
		return ((JRBubblePlot)parent).getYAxisLabelExpression();
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
		return ((JRBubblePlot)parent).getYAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getYAxisVerticalTickLabels()
	{
		return ((JRBubblePlot)parent).getYAxisVerticalTickLabels();
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
	public ScaleTypeEnum getScaleTypeValue()
	{
		return ((JRBubblePlot)parent).getScaleTypeValue();
	}
	
	/**
	 *
	 */
	public void setScaleType( ScaleTypeEnum scaleType )
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 *
	 */
	public JRExpression getDomainAxisMinValueExpression()
	{
		return ((JRBubblePlot)parent).getDomainAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMaxValueExpression()
	{
		return ((JRBubblePlot)parent).getDomainAxisMaxValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMinValueExpression()
	{
		return ((JRBubblePlot)parent).getRangeAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMaxValueExpression()
	{
		return ((JRBubblePlot)parent).getRangeAxisMaxValueExpression();
	}

}
