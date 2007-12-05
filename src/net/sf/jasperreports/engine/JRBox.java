/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.engine;

import java.awt.Color;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRBox extends JRStyleContainer
{


	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public byte getBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public Byte getOwnBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public void setBorder(byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public void setBorder(Byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public Color getBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public Color getOwnBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPen()}
	 */
	public void setBorderColor(Color color);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getPadding()}
	 */
	public int getPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnPadding()}
	 */
	public Integer getOwnPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#setPadding(int)}
	 */
	public void setPadding(int padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#setPadding(Integer)}
	 */
	public void setPadding(Integer padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public byte getTopBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public Byte getOwnTopBorder();
	
	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public void setTopBorder(byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public void setTopBorder(Byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public Color getTopBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public Color getOwnTopBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPen()}
	 */
	public void setTopBorderColor(Color color);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getTopPadding()}
	 */
	public int getTopPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnTopPadding()}
	 */
	public Integer getOwnTopPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#setTopPadding(int)}
	 */
	public void setTopPadding(int padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#setTopPadding(Integer)}
	 */
	public void setTopPadding(Integer padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public byte getLeftBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public Byte getOwnLeftBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public void setLeftBorder(byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public void setLeftBorder(Byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public Color getLeftBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public Color getOwnLeftBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPen()}
	 */
	public void setLeftBorderColor(Color color);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getLeftPadding()}
	 */
	public int getLeftPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnLeftPadding()}
	 */
	public Integer getOwnLeftPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#setLeftPadding(int)}
	 */
	public void setLeftPadding(int padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#setLeftPadding(Integer)}
	 */
	public void setLeftPadding(Integer padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public byte getBottomBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public Byte getOwnBottomBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public void setBottomBorder(byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public void setBottomBorder(Byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public Color getBottomBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public Color getOwnBottomBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPen()}
	 */
	public void setBottomBorderColor(Color color);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getBottomPadding()}
	 */
	public int getBottomPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnBottomPadding()}
	 */
	public Integer getOwnBottomPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#setBottomPadding(int)}
	 */
	public void setBottomPadding(int padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#setBottomPadding(Integer)}
	 */
	public void setBottomPadding(Integer padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public byte getRightBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public Byte getOwnRightBorder();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public void setRightBorder(byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public void setRightBorder(Byte border);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public Color getRightBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public Color getOwnRightBorderColor();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPen()}
	 */
	public void setRightBorderColor(Color color);

	/**
	 * @deprecated Replaced by {@link JRLineBox#getRightPadding()}
	 */
	public int getRightPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#getOwnRightPadding()}
	 */
	public Integer getOwnRightPadding();

	/**
	 * @deprecated Replaced by {@link JRLineBox#setRightPadding(int)}
	 */
	public void setRightPadding(int padding);

	/**
	 * @deprecated Replaced by {@link JRLineBox#setRightPadding(Integer)}
	 */
	public void setRightPadding(Integer padding);

	
}
