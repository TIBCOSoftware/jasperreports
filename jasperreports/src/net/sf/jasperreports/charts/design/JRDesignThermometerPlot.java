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

import java.awt.Color;

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.base.JRBaseThermometerPlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;

/**
 * The layout options of a thermometer chart.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignThermometerPlot extends JRBaseThermometerPlot
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_DATA_RANGE = "dataRange";
	
	public static final String PROPERTY_HIGH_RANGE = "highRange";
	
	public static final String PROPERTY_LOW_RANGE = "lowRange";
	
	public static final String PROPERTY_MEDIUM_RANGE = "mediumRange";
	
	public static final String PROPERTY_MERCURY_COLOR = "mercuryColor";
	
	/**
	 * @deprecated No longer used.
	 */
	public static final String PROPERTY_SHOW_VALUE_LINES = "showValueLines";
	
	public static final String PROPERTY_VALUE_DISPLAY = "valueDisplay";
	
	public static final String PROPERTY_VALUE_LOCATION = "valueLocation";


	/**
	 * Constructs a new plot that is a copy of an existing one.
	 *
	 * @param thermoPlot the plot to copy
	 */
	public JRDesignThermometerPlot(JRChartPlot thermoPlot, JRChart chart)
	{
		super(thermoPlot, chart);
	}

	/**
	 * Sets the range of values that can be displayed by this thermometer.
	 * Specifies the upper and lower bounds of the display area of the meter.
	 *
	 * @param dataRange the range of values to display
	 */
	public void setDataRange(JRDataRange dataRange)
	{
		Object old = this.dataRange;
		this.dataRange = dataRange;
		getEventSupport().firePropertyChange(PROPERTY_DATA_RANGE, old, this.dataRange);
	}

	/**
	 * Sets the formatting option for the textual display of the
	 * value.
	 *
	 *  @param valueDisplay the value display formatting options
	 */
	public void setValueDisplay(JRValueDisplay valueDisplay)
	{
		Object old = this.valueDisplay;
		this.valueDisplay = valueDisplay;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_DISPLAY, old, this.valueDisplay);
	}


	/**
	 * @deprecated No longer used.
	 */
	public void setShowValueLines(boolean showValueLines)
	{
		boolean old = this.showValueLines;
		this.showValueLines = showValueLines;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_VALUE_LINES, old, this.showValueLines);
	}

	/**
	 * @deprecated Replaced by {@link #setValueLocation(Byte)}
	 */
	public void setValueLocation(byte valueLocation)
	{
		setValueLocation(new Byte(valueLocation));
	}

	/**
	 * Sets where to show the textual display of the value.
	 *
	 * @param valueLocation where to show the textual display of the value
	 */
	public void setValueLocation(Byte valueLocation)
	{
		Byte old = this.valueLocationByte;
		this.valueLocationByte = valueLocation;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_LOCATION, old, this.valueLocationByte);
	}

	/**
	 * Sets the default color of the mercury in the thermometer.  This color
	 * will be used when the value is not in a specified range.
	 *
	 * @param mercuryColor the default color of the mercury
	 */
	public void setMercuryColor(Color mercuryColor)
	{
		Object old = this.mercuryColor;
		this.mercuryColor = mercuryColor;
		getEventSupport().firePropertyChange(PROPERTY_MERCURY_COLOR, old, this.mercuryColor);
	}

	/**
	 * Specifies the low range of the thermometer.
	 *
	 * @param lowRange the low range of the thermometer
	 */
	public void setLowRange(JRDataRange lowRange)
	{
		Object old = this.lowRange;
		this.lowRange = lowRange;
		getEventSupport().firePropertyChange(PROPERTY_LOW_RANGE, old, this.lowRange);
	}

	/**
	 * Specifies the medium range of the thermometer.
	 *
	 * @param mediumRange the medium range of the thermometer
	 */
	public void setMediumRange(JRDataRange mediumRange)
	{
		Object old = this.mediumRange;
		this.mediumRange = mediumRange;
		getEventSupport().firePropertyChange(PROPERTY_MEDIUM_RANGE, old, this.mediumRange);
	}

	/**
	 * Specifies the high range of the thermometer.
	 *
	 * @param highRange the high range of the thermometer
	 */
	public void setHighRange(JRDataRange highRange)
	{
		Object old = this.highRange;
		this.highRange = highRange;
		getEventSupport().firePropertyChange(PROPERTY_HIGH_RANGE, old, this.highRange);
	}
}
