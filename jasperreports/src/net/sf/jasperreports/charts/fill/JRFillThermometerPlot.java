/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.type.ValueLocationEnum;
import net.sf.jasperreports.engine.fill.JRFillChartPlot;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRFillThermometerPlot extends JRFillChartPlot implements JRThermometerPlot
{



	/**
	 *
	 */
	public JRFillThermometerPlot(JRThermometerPlot thermoPlot, JRFillObjectFactory factory)
	{
		super(thermoPlot, factory);
	}


	/**
	 *
	 */
	public JRDataRange getDataRange()
	{
		return ((JRThermometerPlot)parent).getDataRange();
	}

	/**
	 *
	 */
	public JRValueDisplay getValueDisplay()
	{
		return ((JRThermometerPlot)parent).getValueDisplay();
	}

	/**
	 * @deprecated No longer used.
	 */
	public boolean isShowValueLines()
	{
		return ((JRThermometerPlot)parent).isShowValueLines();
	}

	/**
	 *
	 */
	public ValueLocationEnum getValueLocationValue()
	{
		return ((JRThermometerPlot)parent).getValueLocationValue();
	}

	/**
	 *
	 */
	public Color getMercuryColor()
	{
		return ((JRThermometerPlot)parent).getMercuryColor();
	}

	/**
	 *
	 */
	public JRDataRange getLowRange()
	{
		return ((JRThermometerPlot)parent).getLowRange();
	}

	/**
	 *
	 */
	public JRDataRange getMediumRange()
	{
		return ((JRThermometerPlot)parent).getMediumRange();
	}

	/**
	 *
	 */
	public JRDataRange getHighRange()
	{
		return ((JRThermometerPlot)parent).getHighRange();
	}
}
