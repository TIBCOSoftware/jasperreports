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
package net.sf.jasperreports.crosstabs.design;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.engine.JRConstants;

/**
 * Crosstab column group implementation to be used for report designing. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabColumnGroup extends JRDesignCrosstabGroup implements JRCrosstabColumnGroup
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected int height;
	protected byte position = JRCellContents.POSITION_X_LEFT;

	
	/**
	 * Creates a column group.
	 */
	public JRDesignCrosstabColumnGroup()
	{
		super();
	}

	public byte getPosition()
	{
		return position;
	}
	
	
	/**
	 * Sets the header contents stretch position.
	 * 
	 * @param position the header contents stretch position
	 * @see JRCrosstabColumnGroup#getPosition()
	 */
	public void setPosition(byte position)
	{
		this.position = position;
	}

	public int getHeight()
	{
		return height;
	}

	
	/**
	 * Sets the header cell height.
	 * 
	 * @param height the height
	 * @see JRCrosstabColumnGroup#getHeight()
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
}
