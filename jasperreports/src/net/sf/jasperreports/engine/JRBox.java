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

import java.awt.Color;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBox
{


	/**
	 *
	 */
	public byte getBorder();


	/**
	 *
	 */
	public void setBorder(byte border);


	/**
	 *
	 */
	public Color getBorderColor();


	/**
	 *
	 */
	public void setBorderColor(Color color);


	/**
	 *
	 */
	public int getPadding();


	/**
	 *
	 */
	public void setPadding(int padding);

	
	/**
	 *
	 */
	public byte getTopBorder();


	/**
	 *
	 */
	public Byte getOwnTopBorder();
	

	/**
	 *
	 */
	public void setTopBorder(byte border);


	/**
	 *
	 */
	public Color getTopBorderColor();


	/**
	 *
	 */
	public Color getOwnTopBorderColor();


	/**
	 *
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
