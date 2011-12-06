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
package net.sf.jasperreports.charts.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.type.MeterShapeEnum;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;

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
	protected JRDataRange dataRange;

	/**
	 * Formatting information for the textual display of the value.
	 */
	protected JRValueDisplay valueDisplay;

	/**
	 * The shape to use when drawing the Meter. Only applied if the meter is
	 * over 180 degrees wide and less than a full circle.
	 */
	protected MeterShapeEnum shapeValue;


	/**
	 * The defined intervals for the Meter.  Each interval indicates a
	 * subsection of the meter and a color to use for that section.
	 */
	protected List<JRMeterInterval> intervals = new java.util.ArrayList<JRMeterInterval>();

	/**
	 * The extend of the meter face in degrees.  It will always be centered
	 * around the straight up position.
	 */
	protected Integer meterAngleInteger;

	/**
	 * Optional description of what the meter is displaying.  It will be
	 * appended to the textual representation of the value.
	 */
	protected String units;

	/**
	 * How often to draw ticks around the face of the meter.  The interval
	 * is relative to the meter range - if the meter displays 100 to 200 and
	 * the tickInterval is 20, there will be 4 ticks at 120, 140, 160 and 180.
	 */
	protected Double tickIntervalDouble;

	/**
	 * The color to use for the face of the meter.
	 */
	protected Color meterBackgroundColor;

	/**
	 * The color to use for the pointer on the meter.
	 */
	protected Color needleColor;

	/**
	 * The color to use for each tick on the face of the meter.
	 */
	protected Color tickColor;

	/**
	 * The font to use when writing tick labels.
	 */
	protected JRFont tickLabelFont;


	/**
	 * Constructs a copy of an existing meter.
	 *
	 * @param plot the plot to copy
	 */
	public JRBaseMeterPlot(JRChartPlot plot, JRChart chart)
	{
		super(plot, chart);
		
		JRMeterPlot meterPlot = plot instanceof JRMeterPlot ? (JRMeterPlot)plot : null;
		if (meterPlot == null)
		{
			valueDisplay = new JRBaseValueDisplay(null, chart);
		}
		else
		{
			valueDisplay = new JRBaseValueDisplay(meterPlot.getValueDisplay(), chart);
			tickLabelFont = meterPlot.getTickLabelFont();
		}
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
		shapeValue = meterPlot.getShapeValue();
		List<JRMeterInterval> origIntervals = meterPlot.getIntervals();
		intervals.clear();
		if (origIntervals != null)
		{
			Iterator<JRMeterInterval> iter = origIntervals.iterator();
			while (iter.hasNext())
			{
				JRMeterInterval interval = iter.next();
				intervals.add(new JRMeterInterval(interval, factory));
			}
		}

		meterAngleInteger = meterPlot.getMeterAngleInteger();
		units = meterPlot.getUnits();
		tickIntervalDouble = meterPlot.getTickIntervalDouble();

		meterBackgroundColor = meterPlot.getMeterBackgroundColor();
		needleColor = meterPlot.getNeedleColor();
		tickColor = meterPlot.getTickColor();
		tickLabelFont = factory.getFont(this.getChart(), meterPlot.getTickLabelFont());
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
	public MeterShapeEnum getShapeValue()
	{
		return shapeValue;
	}

	/**
	 *
	 */
	public List<JRMeterInterval> getIntervals(){
		return intervals;
	}

	/**
	 *
	 */
	public Integer getMeterAngleInteger()
	{
		return meterAngleInteger;
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
	public Double getTickIntervalDouble()
	{
		return tickIntervalDouble;
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
	 *
	 */
	public JRFont getTickLabelFont()
	{
		return tickLabelFont;
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

	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		JRBaseMeterPlot clone = (JRBaseMeterPlot)super.clone(parentChart);
		clone.dataRange = JRCloneUtils.nullSafeClone(dataRange);
		clone.valueDisplay = JRCloneUtils.nullSafeClone(valueDisplay);
		clone.intervals = JRCloneUtils.cloneList(intervals);
		return clone;
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte shape = MeterShapeEnum.PIE.getValue();
	/**
	 * @deprecated
	 */
	private int meterAngle = 180;
	/**
	 * @deprecated
	 */
	private double tickInterval = 10.0;
	/**
	 * @deprecated
	 */
	private Byte shapeByte;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3)
			{
				shapeValue = MeterShapeEnum.getByValue(shape);
				meterAngleInteger = Integer.valueOf(meterAngle);
				tickIntervalDouble = Double.valueOf(tickInterval);
			}
			else
			{
				shapeValue = MeterShapeEnum.getByValue(shapeByte);
				shapeByte = null;
			}
		}
	}
}
