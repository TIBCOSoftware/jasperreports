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
 *(at your option) any later version.
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
 * An abstract representation of a graphic element representing a straight line.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRLine extends JRGraphicElement
{


	/**
	 * Constant used for specifying that the line starts from the top and goes towards the bottom.
	 */
	public static final byte DIRECTION_TOP_DOWN = 1;

	/**
	 * Constant used for specifying that the line starts from the top and goes towards the bottom.
	 */
	public static final byte DIRECTION_BOTTOM_UP = 2;


	/**
	 * Gets the line direction.
	 * @return one of the direction constants
	 */
	public byte getDirection();

	/**
	 * Sets the line direction.
	 * @param direction one of the direction constants
	 */
	public void setDirection(byte direction);


}
