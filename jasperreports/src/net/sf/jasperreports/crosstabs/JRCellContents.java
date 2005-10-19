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

import java.awt.Color;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElementGroup;

/**
 * Crosstab cell contents interface.
 * <p>
 * This interface is used for both crosstab row/column headers and data cells.
 * <p>
 * There are some restrictions/rules regarding crosstab cells:
 * <ul>
 * 	<li>subreports, crosstabs and charts are not allowed</li>
 * 	<li>delayed evaluation for text fields and images is not allowed</li>
 * 	<li>cells have fixed size and cannot stretch/overflow</li>
 * </ul>
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCellContents extends JRElementGroup
{
	/**
	 * Horizontal stretch position indicating that the contents will be rendered on the left side.
	 */
	public static final byte POSITION_X_LEFT = 1;
	
	/**
	 * Horizontal stretch position indicating that the contents will be rendered on the center.
	 */
	public static final byte POSITION_X_CENTER = 2;
	
	/**
	 * Horizontal stretch position indicating that the contents will be rendered on the right side.
	 */
	public static final byte POSITION_X_RIGHT = 3;
	
	/**
	 * Horizontal stretch position indicating that the contents will be horizontally stretched.
	 */
	public static final byte POSITION_X_STRETCH = 4;
	
	/**
	 * Vertical stretch position indicating that the contents will be rendered on the top.
	 */
	public static final byte POSITION_Y_TOP = 1;
	
	/**
	 * Vertical stretch position indicating that the contents will be rendered on the middle.
	 */
	public static final byte POSITION_Y_MIDDLE = 2;
	
	/**
	 * Vertical stretch position indicating that the contents will be rendered on the bottom.
	 */
	public static final byte POSITION_Y_BOTTOM = 3;
	
	/**
	 * Vertical stretch position indicating that the contents will be rendered vertically stretched.
	 */
	public static final byte POSITION_Y_STRETCH = 4;

	/**
	 * Width or height value indicating that the value has not been computed.
	 */
	public static final int NOT_CALCULATED = Integer.MIN_VALUE;
	
	
	/**
	 * Returns the cell background color.
	 * <p>
	 * The cell is filled with the background color only if the crosstab has opaque mode.
	 * The default cell background color is the crosstab element background color.
	 * 
	 * @return the cell backcolor
	 */
	public Color getBackcolor();
	
	
	/**
	 * Returns the cell border.
	 * 
	 * @return the cell border
	 */
	public JRBox getBox();
	
	
	/**
	 * Returns the computed cell width.
	 * 
	 * @return the computed cell width
	 * @see #NOT_CALCULATED
	 */
	public int getWidth();
	
		
	/**
	 * Returns the computed cell height.
	 * 
	 * @return the computed cell height
	 * @see #NOT_CALCULATED
	 */
	public int getHeight();
}
