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
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseCandlestickPlot extends JRBaseChartPlot implements JRCandlestickPlot
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRExpression timeAxisLabelExpression = null;
	protected JRExpression valueAxisLabelExpression = null;

	protected boolean isShowVolume = true;


	/**
	 *
	 */
	protected JRBaseCandlestickPlot(JRChartPlot candlestickPlot)
	{
		super(candlestickPlot);
	}


	/**
	 *
	 */
	public JRBaseCandlestickPlot(JRCandlestickPlot candlestickPlot, JRBaseObjectFactory factory)
	{
		super(candlestickPlot, factory);

		isShowVolume = candlestickPlot.isShowVolume();

		timeAxisLabelExpression = factory.getExpression(candlestickPlot.getTimeAxisLabelExpression());
		valueAxisLabelExpression = factory.getExpression(candlestickPlot.getValueAxisLabelExpression());
	}


	public JRExpression getTimeAxisLabelExpression()
	{
		return timeAxisLabelExpression;
	}


	public JRExpression getValueAxisLabelExpression()
	{
		return valueAxisLabelExpression;
	}


	public boolean isShowVolume()
	{
		return isShowVolume;
	}


	public void setShowVolume(boolean ShowVolume)
	{
		isShowVolume = ShowVolume;
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

}
