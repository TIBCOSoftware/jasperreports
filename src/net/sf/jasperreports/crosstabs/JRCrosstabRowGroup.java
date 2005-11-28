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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs;


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
	 * 	<li>{@link JRCellContents#POSITION_Y_TOP JRCellContents.POSITION_Y_TOP} - the contents will be rendered at the top of of the header</li>
	 * 	<li>{@link JRCellContents#POSITION_Y_MIDDLE JRCellContents.POSITION_Y_MIDDLE} - the contents will be rendered on the center of the header</li>
	 * 	<li>{@link JRCellContents#POSITION_Y_BOTTOM JRCellContents.POSITION_Y_BOTTOM} - the contents will be rendered at the bottom of the header</li>
	 * 	<li>{@link JRCellContents#POSITION_Y_STRETCH JRCellContents.POSITION_Y_STRETCH} - the contents will be proportionally stretched to the new header size</li>
	 * </ul>
	 * 
	 * @return the position of the header contents for header stretching
	 */
	public byte getPosition();
}
