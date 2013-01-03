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

import net.sf.jasperreports.engine.JRChartPlot;

import java.util.List;


/**
 * Represents the display options and nested charts of a multiple axis chart.
 * The display options set at the root of a multiple axis chart override the
 * options set in the nested charts.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public interface JRMultiAxisPlot extends JRChartPlot
{
	/**
	 * Returns a List of all the children axis.  Each element is of type
	 * <code>net.sf.jasperreports.charts.util.JRChartAxis</code>.
	 */
	public List<JRChartAxis> getAxes();
}
