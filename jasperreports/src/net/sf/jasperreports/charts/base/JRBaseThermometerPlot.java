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
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.base.JRBaseDataRange;
import net.sf.jasperreports.charts.base.JRBaseValueDisplay;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

import java.awt.Color;

/**
 * An immutable representation of the layout of a thermometer plot.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseThermometerPlot extends JRBaseChartPlot implements JRThermometerPlot
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * The range of values that can be displayed by this thermometer.  Specifies
	 * the upper and lower bounds of the meter itself.
	 */
	protected JRDataRange dataRange = null;

	/**
	 * Formatting information for the textual display of the value, including
	 * font, color and a mask.
	 */
	protected JRValueDisplay valueDisplay = null;

	/**
	 * Indicates if the boundaries of each range should be shown.
	 */
	protected boolean showValueLines = false;

	/**
	 * Specifies where the textual display of the value should be shown.
	 */
	protected byte valueLocation = JRThermometerPlot.LOCATION_BULB;

	/**
	 * The default color to use for the mercury in the thermometer.
	 */
	protected Color mercuryColor = null;

	/**
	 * The boundaries of the low range.
	 */
	protected JRDataRange lowRange = null;

	/**
	 * The boundaries of the medium range.
	 */
	protected JRDataRange mediumRange = null;

	/**
	 * The boundaries of the high range.
	 */
	protected JRDataRange highRange = null;

	/**
	 * Constructs a new thermometer plot that is a copy of an existing one.
	 *
	 * @param thermoPlot the plot to copy
	 */
	public JRBaseThermometerPlot(JRChartPlot thermoPlot, JRChart chart)
	{
		super(thermoPlot, chart);
	}

	/**
	 * Constructs a new plot that is a copy of an existing one, and registers
	 * all expression used by the plot with the specified factory.
	 *
	 * @param thermoPlot the plot to copy
	 * @param factory the factory to register any expressions with
	 */
	public JRBaseThermometerPlot(JRThermometerPlot thermoPlot, JRBaseObjectFactory factory)
	{
		super(thermoPlot, factory);

		dataRange = new JRBaseDataRange(thermoPlot.getDataRange(), factory);

		valueDisplay = new JRBaseValueDisplay(thermoPlot.getValueDisplay(), factory);

		showValueLines = thermoPlot.isShowValueLines();

		valueLocation = thermoPlot.getValueLocation();

		mercuryColor = thermoPlot.getMercuryColor();

		if (thermoPlot.getLowRange() != null)
			lowRange = new JRBaseDataRange(thermoPlot.getLowRange(), factory);
		if (thermoPlot.getMediumRange() != null)
			mediumRange = new JRBaseDataRange(thermoPlot.getMediumRange(), factory);
		if (thermoPlot.getHighRange() != null)
			highRange = new JRBaseDataRange(thermoPlot.getHighRange(), factory);
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
	public boolean isShowValueLines()
	{
		return showValueLines;
	}

	/**
	 *
	 */
	public byte getValueLocation()
	{
		return valueLocation;
	}

	/**
	 *
	 */
	public Color getMercuryColor()
	{
		return mercuryColor;
	}

	/**
	 *
	 */
	public JRDataRange getLowRange()
	{
		return lowRange;
	}

	/**
	 *
	 */
	public JRDataRange getMediumRange()
	{
		return mediumRange;
	}

	/**
	 *
	 */
	public JRDataRange getHighRange()
	{
		return highRange;
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
