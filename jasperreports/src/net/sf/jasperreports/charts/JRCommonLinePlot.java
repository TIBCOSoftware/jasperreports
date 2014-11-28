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


/**
 * Type of the plot used to render Line, XY Line and Scatter charts.
 * <br/>
 * It exposes the following specific settings:
 * <ul>
 * <li>the show lines flag</li>
 * <li>the show shapes flag</li>
 * </ul>
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRCommonLinePlot
{
	
	/**
	 * @return the show lines flag
	 */
	public Boolean getShowLines();
	
	/**
	 * Sets the show lines flag
	 * @param isShowLines the show lines flag
	 */
	public void setShowLines(Boolean isShowLines);
	
	/**
	 * @return the show shapes flag
	 */
	public Boolean getShowShapes();

	/**
	 * Sets the show shapes flag
	 * @param isShowShapes the show shapes flag
	 */
	public void setShowShapes(Boolean isShowShapes);
	
}
