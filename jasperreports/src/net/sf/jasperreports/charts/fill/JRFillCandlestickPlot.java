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

import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCandlestickPlot extends JRFillChartPlot implements JRCandlestickPlot
{

	/**
	 *
	 */
	protected JRFont timeAxisLabelFont = null;
	protected Color timeAxisLabelColor = null;
	protected JRFont timeAxisTickLabelFont = null;
	protected Color timeAxisTickLabelColor = null;
	protected Color timeAxisLineColor = null;

	protected JRFont valueAxisLabelFont = null;
	protected Color valueAxisLabelColor = null;
	protected JRFont valueAxisTickLabelFont = null;
	protected Color valueAxisTickLabelColor = null;
	protected Color valueAxisLineColor = null;

	
	/**
	 *
	 */
	public JRFillCandlestickPlot(
		JRCandlestickPlot candlestickPlot,
		JRFillObjectFactory factory
		)
	{
		super(candlestickPlot, factory);

		timeAxisLabelFont = new JRBaseFont(candlestickPlot.getChart(), candlestickPlot.getTimeAxisLabelFont());
		timeAxisLabelColor = candlestickPlot.getOwnTimeAxisLabelColor();
		timeAxisTickLabelFont = new JRBaseFont(candlestickPlot.getChart(), candlestickPlot.getTimeAxisTickLabelFont());
		timeAxisTickLabelColor = candlestickPlot.getOwnTimeAxisTickLabelColor();
		timeAxisLineColor = candlestickPlot.getOwnTimeAxisLineColor();
		
		valueAxisLabelFont = new JRBaseFont(candlestickPlot.getChart(), candlestickPlot.getValueAxisLabelFont());
		valueAxisLabelColor = candlestickPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(candlestickPlot.getChart(), candlestickPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = candlestickPlot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = candlestickPlot.getOwnValueAxisTickLabelColor();
	}

	/**
	 *
	 */
	public JRExpression getTimeAxisLabelExpression()
	{
		return ((JRCandlestickPlot)parent).getTimeAxisLabelExpression();
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
		return ((JRCandlestickPlot)parent).getTimeAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getTimeAxisVerticalTickLabels()
	{
		return ((JRCandlestickPlot)parent).getTimeAxisVerticalTickLabels();
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
		return ((JRCandlestickPlot)parent).getValueAxisLabelExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMinValueExpression()
	{
		return ((JRCandlestickPlot)parent).getDomainAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getDomainAxisMaxValueExpression()
	{
		return ((JRCandlestickPlot)parent).getDomainAxisMaxValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMinValueExpression()
	{
		return ((JRCandlestickPlot)parent).getRangeAxisMinValueExpression();
	}

	/**
	 *
	 */
	public JRExpression getRangeAxisMaxValueExpression()
	{
		return ((JRCandlestickPlot)parent).getRangeAxisMaxValueExpression();
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
		return ((JRCandlestickPlot)parent).getValueAxisTickLabelMask();
	}

	/**
	 * 
	 */
	public Boolean getValueAxisVerticalTickLabels()
	{
		return ((JRCandlestickPlot)parent).getValueAxisVerticalTickLabels();
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
	 * @deprecated Replaced by {@link #getShowVolume()}
	 */
	public boolean isShowVolume()
	{
		return ((JRCandlestickPlot)parent).isShowVolume();
	}
	
	/**
	 *
	 */
	public Boolean getShowVolume()
	{
		return ((JRCandlestickPlot)parent).getShowVolume();
	}
	
}
