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
package net.sf.jasperreports.engine;

import java.awt.Color;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBox
{


	/**
	 * Gets the default border pen size (can be overwritten by individual settings).
	 */
	public byte getBorder();


	/**
	 * Sets the default border pen size (can be overwritten by individual settings).
	 */
	public void setBorder(byte border);


	/**
	 * Gets the default border color (can be overwritten by individual settings).
	 */
	public Color getBorderColor();


	/**
	 * Sets the default border color (can be overwritten by individual settings).
	 */
	public void setBorderColor(Color color);


	/**
	 * Gets the default padding in pixels (can be overwritten by individual settings).
	 */
	public int getPadding();


	/**
	 * Sets the default padding in pixels (can be overwritten by individual settings).
	 */
	public void setPadding(int padding);

	
	/**
	 * Gets the top border pen size.
	 */
	public byte getTopBorder();


	/**
	 * Gets the top border pen size (if the default value was overwritten).
	 */
	public Byte getOwnTopBorder();
	

	/**
	 * Sets the top border pen size.
	 */
	public void setTopBorder(byte border);


	/**
	 * Gets the top border color.
	 */
	public Color getTopBorderColor();


	/**
	 * Gets the top border color (if the default value was overwritten).
	 */
	public Color getOwnTopBorderColor();


	/**
	 * Sets the top border color.
	 */
	public void setTopBorderColor(Color color);


	/**
	 *
	 */
	public int getTopPadding();


	/**
	 *
	 */
	public Integer getOwnTopPadding();


	/**
	 *
	 */
	public void setTopPadding(int padding);

	
	/**
	 *
	 */
	public byte getLeftBorder();


	/**
	 *
	 */
	public Byte getOwnLeftBorder();


	/**
	 *
	 */
	public void setLeftBorder(byte border);


	/**
	 *
	 */
	public Color getLeftBorderColor();


	/**
	 *
	 */
	public Color getOwnLeftBorderColor();


	/**
	 *
	 */
	public void setLeftBorderColor(Color color);


	/**
	 *
	 */
	public int getLeftPadding();


	/**
	 *
	 */
	public Integer getOwnLeftPadding();


	/**
	 *
	 */
	public void setLeftPadding(int padding);

	
	/**
	 *
	 */
	public byte getBottomBorder();


	/**
	 *
	 */
	public Byte getOwnBottomBorder();


	/**
	 *
	 */
	public void setBottomBorder(byte border);


	/**
	 *
	 */
	public Color getBottomBorderColor();


	/**
	 *
	 */
	public Color getOwnBottomBorderColor();


	/**
	 *
	 */
	public void setBottomBorderColor(Color color);


	/**
	 *
	 */
	public int getBottomPadding();


	/**
	 *
	 */
	public Integer getOwnBottomPadding();


	/**
	 *
	 */
	public void setBottomPadding(int padding);

	
	/**
	 *
	 */
	public byte getRightBorder();


	/**
	 *
	 */
	public Byte getOwnRightBorder();


	/**
	 *
	 */
	public void setRightBorder(byte border);


	/**
	 *
	 */
	public Color getRightBorderColor();


	/**
	 *
	 */
	public Color getOwnRightBorderColor();


	/**
	 *
	 */
	public void setRightBorderColor(Color color);


	/**
	 *
	 */
	public int getRightPadding();


	/**
	 *
	 */
	public Integer getOwnRightPadding();


	/**
	 *
	 */
	public void setRightPadding(int padding);

	
}
