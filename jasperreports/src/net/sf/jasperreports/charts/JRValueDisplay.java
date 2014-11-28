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
package net.sf.jasperreports.charts;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRFont;

import java.awt.Color;


/**
 * Represents the formatting option for the textual
 * representation of a value displayed in a Meter or
 * Thermometer chart.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public interface JRValueDisplay extends JRCloneable
{
	/**
	 * 
	 */
	public JRChart getChart();
	
	/**
	 * Returns the color to use when writing the value.
	 *
	 * @return the color to use when writing the value
	 */
	public Color getColor();

	/**
	 * Returns the formatting mask to use when writing the value.
	 * The mask will be specified using the patterns defined
	 * in <code>java.text.DecimalFormat</code>.
	 *
	 * @return the formatting mask to use when writing the value
	 */
	public String getMask();

	/**
	 * Returns the font to use when writing the value.
	 *
	 * @return the font to use when writing the value
	 */
	public JRFont getFont();
	
	public JRValueDisplay clone(JRChart parentChart);

}
