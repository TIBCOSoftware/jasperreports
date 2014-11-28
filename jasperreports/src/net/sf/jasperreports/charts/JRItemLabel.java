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

import java.awt.Color;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRFont;


/**
 * Contains the formatting option for the textual 
 * representation of item labels in a category plot
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public interface JRItemLabel extends JRCloneable
{
	/**
	 * @return the chart the current item label belongs to. 
	 */
	public JRChart getChart();
	
	/**
	 * @return the item label font. Allows full font customization for the item label.
	 */
	public JRFont getFont();

	/**
	 * @return the item label text color
	 */
	public Color getColor();

	/**
	 * @return the item label background color
	 */
	public Color getBackgroundColor();

	/**
	 * Returns the item label formatting mask.
	 * The mask will be specified using the patterns defined
	 * in <code>java.text.DecimalFormat</code>.
	 *
	 * @return the item label formatting mask
	 */
//	public String getMask();

	public JRItemLabel clone(JRChart parentChart);

}
