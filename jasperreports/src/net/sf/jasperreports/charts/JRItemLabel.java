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

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRFont;


/**
 * Represents the formatting option for the textual
 * representation of item labels in a category plot
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public interface JRItemLabel extends JRCloneable
{
	/**
	 * 
	 */
	public JRChart getChart();
	
	/**
	 * Returns the item label font.
	 *
	 * @return the item label font
	 */
	public JRFont getFont();

	/**
	 * Returns the item label text color
	 *
	 * @return the item label text color
	 */
	public Color getColor();

	/**
	 * Returns the item label background color
	 *
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

}
