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



/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRLineBox extends JRPenContainer, JRCloneable
{

	/**
	 * 
	 */
	public JRBoxContainer getBoxContainer();

	/**
	 * 
	 */
	public JRLineBox clone(JRBoxContainer boxContainer);

	
	/**
	 * Gets the pen properties for the border.
	 */
	public JRPen getPen();

	/**
	 *
	 */
	public void copyPen(JRPen pen);

	/**
	 * Gets the pen properties for the top border.
	 */
	public JRPen getTopPen();

	/**
	 *
	 */
	public void copyTopPen(JRPen topPen);

	/**
	 * Gets the pen properties for the left border.
	 */
	public JRPen getLeftPen();

	/**
	 *
	 */
	public void copyLeftPen(JRPen leftPen);

	/**
	 * Gets the pen properties for the bottom border.
	 */
	public JRPen getBottomPen();

	/**
	 *
	 */
	public void copyBottomPen(JRPen bottomPen);

	/**
	 * Gets the pen properties for the right border.
	 */
	public JRPen getRightPen();

	/**
	 *
	 */
	public void copyRightPen(JRPen rightPen);

	
	/**
	 * Gets the default padding in pixels (can be overwritten by individual settings).
	 */
	public Integer getPadding();

	/**
	 *
	 */
	public Integer getOwnPadding();

	/**
	 * Sets the default padding in pixels (can be overwritten by individual settings).
	 */
	public void setPadding(int padding);

	/**
	 *
	 */
	public void setPadding(Integer padding);

	/**
	 *
	 */
	public Integer getTopPadding();

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
	public void setTopPadding(Integer padding);

	/**
	 *
	 */
	public Integer getLeftPadding();

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
	public void setLeftPadding(Integer padding);

	/**
	 *
	 */
	public Integer getBottomPadding();

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
	public void setBottomPadding(Integer padding);

	/**
	 *
	 */
	public Integer getRightPadding();

	/**
	 *
	 */
	public Integer getOwnRightPadding();

	/**
	 *
	 */
	public void setRightPadding(int padding);

	/**
	 *
	 */
	public void setRightPadding(Integer padding);
	
}
