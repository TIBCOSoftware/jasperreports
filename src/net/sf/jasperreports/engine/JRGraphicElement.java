/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
package net.sf.jasperreports.engine;


/**
 * An abstract representation of a report graphic element. It provides basic functionality for images, lines, rectangles
 * and ellipses.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRGraphicElement extends JRElement
{


	/**
	 * Contant useful for specifying that the element border will not be drawn.
	 */
	public static final byte PEN_NONE = 0;

	/**
	 * Contant useful for specifying that an element border of 1 pixel.
	 */
	public static final byte PEN_1_POINT = 1;

	/**
	 * Contant useful for specifying that an element border of 2 pixels.
	 */
	public static final byte PEN_2_POINT = 2;

	/**
	 * Contant useful for specifying that an element border of 4 pixels.
	 */
	public static final byte PEN_4_POINT = 3;

	/**
	 * Contant useful for specifying that an element has a dashed border.
	 */
	public static final byte PEN_DOTTED = 4;

	/**
	 * Contant useful for specifying that an element has a thin border (0.5 pixels)
	 */
	public static final byte PEN_THIN = 5;


	/**
	 * Constant useful for specifying that the inside of an element should be drawn using the background color. It
	 * is ignored if the element draw mode is transparent.
	 */
	public static final byte FILL_SOLID = 1;


	/**
	 * Indicates the pen type used for this element.
	 * @return one of the pen constants in this class
	 */
	public byte getPen();
		
	/**
	 * Sets the pen type that will used for this element.
	 * @param pen one of the pen constants in this class
	 */
	public void setPen(byte pen);

	/**
	 * Indicates the fill type used for this element.
	 * @return one of the pen constants in this class
	 */
	public byte getFill();

	/**
	 * Sets the fill type used for this element.
	 * @param fill one of the pen constants in this class
	 */
	public void setFill(byte fill);
	

}
