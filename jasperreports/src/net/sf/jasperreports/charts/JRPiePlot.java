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

import net.sf.jasperreports.engine.JRChartPlot;


/**
 * Type of plot used for rendering Pie charts. 
 * <br/>
 * Special settings that the Pie plot exposes are: 
 * <ul>
 * <li>the <code>circular</code> flag</li>
 * <li>the label format</li>
 * <li>the legend label format</li>
 * <li>the item label</li>
 * <li>the <code>showLabels</code> flag</li>
 * </ul>
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRPiePlot extends JRChartPlot
{
	/**
	 * @return a flag that specifies a circular form for the pie
	 */
	public Boolean getCircular();
	
	/**
	 * @return the format pattern for labels
	 */
	public String getLabelFormat();
	
	/**
	 * @return the format pattern for legend labels
	 */
	public String getLegendLabelFormat();

	/**
	 * @return a {@link net.sf.jasperreports.charts.JRItemLabel JRItemLabel} object 
	 * representing the item label
	 */
	public JRItemLabel getItemLabel();
	
	/**
	 * @return a flag that specifies whether labels are to be shown or not
	 */
	public Boolean getShowLabels();
	
}
