/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs;

/**
 * Crosstab column group interface.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCrosstabColumnGroup extends JRCrosstabGroup
{
	/**
	 * Returns the height of the group headers.
	 * 
	 * @return the height of the group headers
	 * @see JRCrosstabGroup#getHeader()
	 * @see JRCrosstabGroup#getTotalHeader()
	 */
	public int getHeight();
	
	
	/**
	 * Returns the position of the header contents for header stretching.
	 * <p>
	 * The column group headers stretch horizontally when there are multiple sub group entries.
	 * The header contents will be adjusted to the new width depending on this attribute:
	 * <ul>
	 * 	<li>{@link JRCellContents#POSITION_X_LEFT JRCellContents.POSITION_X_LEFT} - the contents will be rendered on the left side of the header</li>
	 * 	<li>{@link JRCellContents#POSITION_X_CENTER JRCellContents.POSITION_X_CENTER} - the contents will be rendered on the center of the header</li>
	 * 	<li>{@link JRCellContents#POSITION_X_RIGHT JRCellContents.POSITION_X_RIGHT} - the contents will be rendered on the right side of the header</li>
	 * 	<li>{@link JRCellContents#POSITION_X_STRETCH JRCellContents.POSITION_X_STRETCH} - the contents will be proportionally stretched to the new header size</li>
	 * </ul>
	 * 
	 * @return the position of the header contents for header stretching
	 */
	public byte getPosition();
}
