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
package net.sf.jasperreports.crosstabs;

import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;


/**
 * Crosstab row group interface.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCrosstabRowGroup extends JRCrosstabGroup
{
	/**
	 * Returns the width of the group headers.
	 * 
	 * @return the width of the group headers
	 * @see JRCrosstabGroup#getHeader()
	 * @see JRCrosstabGroup#getTotalHeader()
	 */
	public int getWidth();
	

	/**
	 * Returns the position of the header contents for header stretching.
	 * <p>
	 * The row group headers stretch vertically when there are multiple sub group entries.
	 * The header contents will be adjusted to the new height depending on this attribute:
	 * <ul>
	 * 	<li>{@link CrosstabRowPositionEnum#TOP CrosstabRowPositionEnum.TOP} - the contents will be rendered at the top of of the header</li>
	 * 	<li>{@link CrosstabRowPositionEnum#MIDDLE CrosstabRowPositionEnum.MIDDLE} - the contents will be rendered on the center of the header</li>
	 * 	<li>{@link CrosstabRowPositionEnum#BOTTOM JCrosstabRowPositionEnum.BOTTOM} - the contents will be rendered at the bottom of the header</li>
	 * 	<li>{@link CrosstabRowPositionEnum#STRETCH CrosstabRowPositionEnum.STRETCH} - the contents will be proportionally stretched to the new header size</li>
	 * </ul>
	 * 
	 * @return the position of the header contents for header stretching
	 */
	public CrosstabRowPositionEnum getPositionValue();
}
