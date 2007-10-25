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
package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.base.JRBaseChartAxis;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * {@link JRChartAxis JRChartAxis} implementation to be used for report design.
 *
 * @author Barry Klawans (barry@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignChartAxis extends JRBaseChartAxis implements JRChangeEventsSupport
{
	
	public static final String PROPERTY_CHART = "chart";
	
	public static final String PROPERTY_POSITION = "position";

	/**
	 * The multiple axis chart that this axis belongs to.
	 */
	protected JRDesignChart parentChart = null;

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Construct a new axis that will be added to the specified chart.
	 *
	 * @param parentChart the chart that the axis will be added to
	 */
	public JRDesignChartAxis(JRDesignChart parentChart)
	{
		this.parentChart = parentChart;
	}

	/**
	 * Sets the position of this axis' value line relative to the multiple
	 * axis chart.
	 *
	 * @param position the position of this axis
	 */
	public void setPosition(byte position)
	{
		byte old = this.position;
		this.position = position;
		getEventSupport().firePropertyChange(PROPERTY_POSITION, old, this.position);
	}

	/**
	 * Set the chart that contains the dataset and plot to use for this
	 * axis.  The plot is used to figure out how to render the dataset (ie
	 * as a line or bar chart) when adding it to the multiple axis chart.
	 *
	 * @param chart the chart that contains the dataset and plot for this axis
	 */
	public void setChart(JRDesignChart chart)
	{
		// Override the chart elements that we are going to ignore, as they
		// are supposed to be controlled by the multi chart's settings.
		chart.setBackcolor(parentChart.getBackcolor());
		chart.setShowLegend(parentChart.isShowLegend());
		chart.setTitleExpression(parentChart.getTitleExpression());
		chart.setTitleFont(parentChart.getTitleFont());
		chart.setTitlePosition(parentChart.getTitlePosition());
		chart.setTitleColor(parentChart.getTitleColor());
		chart.setSubtitleExpression(parentChart.getSubtitleExpression());
		chart.setSubtitleFont(parentChart.getSubtitleFont());
		chart.setSubtitleColor(parentChart.getSubtitleColor());
		chart.setLegendColor(parentChart.getLegendColor());
		chart.setLegendBackgroundColor(parentChart.getLegendBackgroundColor());
		chart.setLegendFont(parentChart.getLegendFont());
		chart.setLegendPosition(parentChart.getLegendPosition());
		
		Object old = this.chart;
		this.chart = chart;
		getEventSupport().firePropertyChange(PROPERTY_CHART, old, this.chart);
	}

	/**
	 * Sets the chart that contains the dataset and plot for this axis.
	 * Identical to {@link #setChart} but is called by the XML digester
	 * when parsing the report source.
	 *
	 * @param element
	 */
	public void addElement(JRElement element)
	{
		setChart((JRDesignChart)element);
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}
}
