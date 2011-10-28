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

import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */

public class JRFillTimeSeriesPlot extends JRFillChartPlot implements JRTimeSeriesPlot {

	/**
	 *
	 */
	protected JRFont timeAxisLabelFont;
	protected Color timeAxisLabelColor;
	protected JRFont timeAxisTickLabelFont;
	protected Color timeAxisTickLabelColor;
	protected Color timeAxisLineColor;

	protected JRFont valueAxisLabelFont;
	protected Color valueAxisLabelColor;
	protected JRFont valueAxisTickLabelFont;
	protected Color valueAxisTickLabelColor;
	protected Color valueAxisLineColor;

	
	/**
	 *
	 */
	public JRFillTimeSeriesPlot( JRTimeSeriesPlot plot, JRFillObjectFactory factory )
	{
		super(plot, factory);

		timeAxisLabelFont = factory.getFont(plot.getChart(), plot.getTimeAxisLabelFont());
		timeAxisLabelColor = plot.getOwnTimeAxisLabelColor();
		timeAxisTickLabelFont = factory.getFont(plot.getChart(), plot.getTimeAxisTickLabelFont());
		timeAxisTickLabelColor = plot.getOwnTimeAxisTickLabelColor();
		timeAxisLineColor = plot.getOwnTimeAxisLineColor();
		
		valueAxisLabelFont = factory.getFont(plot.getChart(), plot.getValueAxisLabelFont());
		valueAxisLabelColor = plot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = factory.getFont(plot.getChart(), plot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = plot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = plot.getOwnValueAxisTickLabelColor();
	}
	
	/**
	 *
	 */
	public JRExpression getTimeAxisLabelExpression()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRFont getTimeAxisLabelFont()
	{
		return timeAxisLabelFont;
	}

	/**
	 *
	 */
	public Color getTimeAxisLabelColor()
	{
		return JRStyleResolver.getTimeAxisLabelColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnTimeAxisLabelColor()
	{
		return timeAxisLabelColor;
	}

	/**
	 *
	 */
	public void setTimeAxisLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRFont getTimeAxisTickLabelFont()
	{
		return timeAxisTickLabelFont;
	}

	/**
	 *
	 */
	public Color getTimeAxisTickLabelColor()
	{
		return JRStyleResolver.getTimeAxisTickLabelColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnTimeAxisTickLabelColor()
	{
		return timeAxisTickLabelColor;
	}

	/**
	 *
	 */
	public void setTimeAxisTickLabelColor(Color color)
	{
	}

	/**
	 *
	 */
	public String getTimeAxisTickLabelMask()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getTimeAxisVerticalTickLabels()
	{
		return ((JRTimeSeriesPlot)parent).getTimeAxisVerticalTickLabels();
	}

	/**
	 *
	 */
	public Color getTimeAxisLineColor()
	{
		return JRStyleResolver.getTimeAxisLineColor(this, this);
	}

	/**
	 *
	 */
	public Color getOwnTimeAxisLineColor()
	{
		return timeAxisLineColor;
	}

	/**
	 *
	 */
	public void setTimeAxisLineColor(Color color)
	{
	}

	/**
	 *
	 */
	public JRExpression getValueAxisLabelExpression()
	{
		return ((JRTimeSeriesPlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMinValueExpression()
	{
		return ((JRTimeSeriesPlot)parent).getDomainAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMaxValueExpression()
	{
		return ((JRTimeSeriesPlot)parent).getDomainAxisMaxValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMinValueExpression()
	{
		return ((JRTimeSeriesPlot)parent).getRangeAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMaxValueExpression()
	{
		return ((JRTimeSeriesPlot)parent).getRangeAxisMaxValueExpression();
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
		return ((JRTimeSeriesPlot)parent).getValueAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getValueAxisVerticalTickLabels()
	{
		return ((JRTimeSeriesPlot)parent).getValueAxisVerticalTickLabels();
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
	public Boolean getShowLines(){
		return ((JRTimeSeriesPlot)parent).getShowLines();
	}
	
	/**
	 * 
	 */
	public void setShowLines(Boolean showLines){
	}
	
	/**
	 * 
	 */
	public Boolean getShowShapes(){
		return ((JRTimeSeriesPlot)parent).getShowShapes();
	}
	
	/**
	 * 
	 */
	public void setShowShapes(Boolean showLines){
	}
}
