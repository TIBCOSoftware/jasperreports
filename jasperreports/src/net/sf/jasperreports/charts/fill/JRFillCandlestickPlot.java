/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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

		timeAxisLabelFont = new JRBaseFont(null, null, candlestickPlot.getChart(), candlestickPlot.getTimeAxisLabelFont());
		timeAxisLabelColor = candlestickPlot.getOwnTimeAxisLabelColor();
		timeAxisTickLabelFont = new JRBaseFont(null, null, candlestickPlot.getChart(), candlestickPlot.getTimeAxisTickLabelFont());
		timeAxisTickLabelColor = candlestickPlot.getOwnTimeAxisTickLabelColor();
		timeAxisLineColor = candlestickPlot.getTimeAxisLineColor();
		
		valueAxisLabelFont = new JRBaseFont(null, null, candlestickPlot.getChart(), candlestickPlot.getValueAxisLabelFont());
		valueAxisLabelColor = candlestickPlot.getOwnValueAxisLabelColor();
		valueAxisTickLabelFont = new JRBaseFont(null, null, candlestickPlot.getChart(), candlestickPlot.getValueAxisTickLabelFont());
		valueAxisTickLabelColor = candlestickPlot.getOwnValueAxisTickLabelColor();
		valueAxisLineColor = candlestickPlot.getValueAxisTickLabelColor();
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
	public void setTimeAxisLabelFont(JRFont font)
	{
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
	public void setTimeAxisTickLabelFont(JRFont font)
	{
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
	public void setTimeAxisTickLabelMask(String mask)
	{
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
	public JRFont getValueAxisLabelFont()
	{
		return valueAxisLabelFont;
	}

	/**
	 *
	 */
	public void setValueAxisLabelFont(JRFont font)
	{
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
	public void setValueAxisTickLabelFont(JRFont font)
	{
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
	public void setValueAxisTickLabelMask(String mask)
	{
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
	
	public boolean isShowVolume()
	{
		return ((JRCandlestickPlot)parent).isShowVolume();
	}
}
