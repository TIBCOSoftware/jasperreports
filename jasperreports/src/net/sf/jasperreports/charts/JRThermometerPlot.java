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
package net.sf.jasperreports.charts;

import java.awt.Color;

import net.sf.jasperreports.charts.type.ValueLocationEnum;
import net.sf.jasperreports.engine.JRChartPlot;

/**
 * Represents the display options of a Thermometer chart.  A Thermometer chart
 * consists of the outline of a thermometer, a scale showing the reading of
 * the thermometer, three optional ranges corresponding to "good", "warning"
 * and "critical", and the textual display of the value.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public interface JRThermometerPlot extends JRChartPlot
{
	/**
	 * Returns the range of values displayed by this thermometer.  This range
	 * corresponds with the scale on the thermometer.
	 *
	 * @return the range of values displayed by this thermometer
	 */
	public JRDataRange getDataRange();

	/**
	 * Returns formatting information for the textual representation of the value.
	 *
	 * @return formatting information for the value
	 */
	public JRValueDisplay getValueDisplay();

	/**
	 * @deprecated No longer used.
	 */
	public boolean isShowValueLines();

	/**
	 * Returns the location where the value of the thermometer will be shown.  The
	 * return value will be one of <code>net.sf.jasperreports.charts.type.ValueLocationEnum.NONE</code>,
	 * <code>net.sf.jasperreports.charts.type.ValueLocationEnum.LEFT</code>, <code>net.sf.jasperreports.charts.type.ValueLocationEnum.RIGHT</code> or
	 * <code>net.sf.jasperreports.charts.type.ValueLocationEnum.BULB</code>.
	 *
	 * @return the location where the value of the thermometer will be shown
	 */
	public ValueLocationEnum getValueLocationValue();

	/**
	 * Returns the color of the "mercury" in the thermometer when the value is
	 * not in a defined range.
	 *
	 * @return the default color of the mercury in the thermometer
	 */
	public Color getMercuryColor();

	/**
	 * Returns the low range, or <code>null</code> if undefined.
	 *
	 * @return the low range, or <code>null</code> if undefined.
	 */
	public JRDataRange getLowRange();

	/**
	 * Returns the medium range, or <code>null</code> if undefined.
	 *
	 * @return the medium range, or <code>null</code> if undefined.
	 */
	public JRDataRange getMediumRange();

	/**
	 * Returns the high range, or <code>null</code> if undefined.
	 *
	 * @return the high range, or <code>null</code> if undefined.
	 */
	public JRDataRange getHighRange();
}
