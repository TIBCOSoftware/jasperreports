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

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.base.JRBaseDataRange;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.charts.base.JRBaseValueDisplay;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

/**
 * An immutable representation of the layout of a Meter chart.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseMeterPlot extends JRBaseChartPlot implements JRMeterPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * The range displayed by the Meter.
	 */
	protected JRDataRange dataRange = null;

	/**
	 * Formatting information for the textual display of the value.
	 */
	protected JRValueDisplay valueDisplay = null;

	/**
	 * The shape to use when drawing the Meter. Only applied if the meter is
	 * over 180 degrees wide and less than a full circle.
	 */
	protected byte shape = JRMeterPlot.SHAPE_PIE;

	/**
	 * The defined intervals for the Meter.  Each interval indicates a
	 * subsection of the meter and a color to use for that section.
	 */
	protected List intervals = new java.util.ArrayList();

	/**
	 * The extend of the meter face in degrees.  It will always be centered
	 * around the straight up position.
	 */
	protected int meterAngle = 180;

	/**
	 * Optional description of what the meter is displaying.  It will be
	 * appended to the textual representation of the value.
	 */
	protected String units = null;

	/**
	 * How often to draw ticks around the face of the meter.  The interval
	 * is relative to the meter range - if the meter displays 100 to 200 and
	 * the tickInterval is 20, there will be 4 ticks at 120, 140, 160 and 180.
	 */
	protected double tickInterval = 10.0;

	/**
	 * The color to use for the face of the meter.
	 */
	protected Color meterBackgroundColor = null;

	/**
	 * The color to use for the pointer on the meter.
	 */
	protected Color needleColor = null;

	/**
	 * The color to use for each tick on the face of the meter.
	 */
	protected Color tickColor = null;



	/**
	 * Constructs a copy of an existing meter.
	 *
	 * @param meterPlot the meter to copy
	 */
	public JRBaseMeterPlot(JRChartPlot meterPlot, JRChart chart)
	{
		super(meterPlot, chart);
	}

	/**
	 * Constructs a copy of an existing meter and registers all expressions
	 * maintained by the meter plot with a factory.
	 *
	 * @param meterPlot the meter to copy
	 * @param factory the factory to register expressions with
	 */
	public JRBaseMeterPlot(JRMeterPlot meterPlot, JRBaseObjectFactory factory)
	{
		super(meterPlot, factory);

		dataRange = new JRBaseDataRange(meterPlot.getDataRange(), factory);
		valueDisplay = new JRBaseValueDisplay(meterPlot.getValueDisplay(), factory);
		shape = meterPlot.getShape();
		List origIntervals = meterPlot.getIntervals();
		intervals.clear();
		if (origIntervals != null)
		{
			Iterator iter = origIntervals.iterator();
			while (iter.hasNext())
			{
				JRMeterInterval interval = (JRMeterInterval)iter.next();
				intervals.add(new JRMeterInterval(interval, factory));
			}
		}

		meterAngle = meterPlot.getMeterAngle();
		units = meterPlot.getUnits();
		tickInterval = meterPlot.getTickInterval();

		meterBackgroundColor = meterPlot.getMeterBackgroundColor();
		needleColor = meterPlot.getNeedleColor();
		tickColor = meterPlot.getTickColor();
	}



	/**
	 *
	 */
	public JRDataRange getDataRange()
	{
		return dataRange;
	}

	/**
	 *
	 */
	public JRValueDisplay getValueDisplay()
	{
		return valueDisplay;
	}

	/**
	 *
	 */
	public byte getShape()
	{
		return shape;
	}

	/**
	 *
	 */
	public List getIntervals(){
		return intervals;
	}

	/**
	 *
	 */
	public int getMeterAngle()
	{
		return meterAngle;
	}

	/**
	 *
	 */
	public String getUnits()
	{
		return units;
	}

	/**
	 *
	 */
	public double getTickInterval()
	{
		return tickInterval;
	}

	/**
	 *
	 */
	public Color getMeterBackgroundColor()
	{
		return meterBackgroundColor;
	}

	/**
	 *
	 */
	public Color getNeedleColor()
	{
		return needleColor;
	}

	/**
	 *
	 */
	public Color getTickColor()
	{
		return tickColor;
	}

	/**
	 * Adds all the expression used by this plot with the specified collector.
	 * All collected expression that are also registered with a factory will
	 * be included with the report is compiled.
	 *
	 * @param collector the expression collector to use
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

}
