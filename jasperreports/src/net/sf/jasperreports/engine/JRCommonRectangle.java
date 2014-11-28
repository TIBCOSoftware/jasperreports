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
package net.sf.jasperreports.engine;


/**
 * An abstract representation of a graphic element representing a rectangle.
 * <p/>
 * Rectangle elements are the simplest report elements. They share almost all their settings
 * with most other report elements.
 * <p/>
 * The <code>radius</code> attribute (see {@link #getRadius()}) specifies the radius for the 
 * arcs used to draw the corners of the rectangle. The default value is 0, meaning that the 
 * rectangle has normal, square corners.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface JRCommonRectangle extends JRStyleContainer
{

	
	/**
	 * Indicates the corner radius for rectangles with round corners. The default is 0.
	 */
	public int getRadius();

	public Integer getOwnRadius();

	/**
	 * Sets the corner radius for rectangles with round corners.
	 */
	public void setRadius(int radius);

	public void setRadius(Integer radius);

	
}
