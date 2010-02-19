/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.type.PenEnum;


/**
 * An abstract representation of a report graphic element. It provides basic functionality for images, lines, rectangles
 * and ellipses.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRGraphicElement extends JRElement, JRCommonGraphicElement
{
	/**
	 * Contant useful for specifying that the element border will not be drawn.
	 * @deprecated Replaced by {@link PenEnum#NONE}.
	 */
	public static final byte PEN_NONE = 0;

	/**
	 * Contant useful for specifying that an element border of 1 pixel.
	 * @deprecated Replaced by {@link PenEnum#ONE_POINT}.
	 */
	public static final byte PEN_1_POINT = 1;

	/**
	 * Contant useful for specifying that an element border of 2 pixels.
	 * @deprecated Replaced by {@link PenEnum#TWO_POINT}.
	 */
	public static final byte PEN_2_POINT = 2;

	/**
	 * Contant useful for specifying that an element border of 4 pixels.
	 * @deprecated Replaced by {@link PenEnum#FOUR_POINT}.
	 */
	public static final byte PEN_4_POINT = 3;

	/**
	 * Contant useful for specifying that an element has a dashed border.
	 * @deprecated Replaced by {@link PenEnum#DOTTED}.
	 */
	public static final byte PEN_DOTTED = 4;

	/**
	 * Contant useful for specifying that an element has a thin border (0.5 pixels)
	 * @deprecated Replaced by {@link PenEnum#THIN}.
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
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public byte getPen();

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public Byte getOwnPen();

	/**
	 * Sets the pen type that will used for this element.
	 * @param pen one of the pen constants in this class
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(byte pen);

	/**
	 * @deprecated Replaced by {@link #getLinePen()}
	 */
	public void setPen(Byte pen);
	

}
