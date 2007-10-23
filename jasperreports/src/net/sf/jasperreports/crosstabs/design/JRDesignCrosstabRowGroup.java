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
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.engine.JRConstants;

/**
 * Crosstab row group implementation to be used for report designing.
 *  
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabRowGroup extends JRDesignCrosstabGroup implements JRCrosstabRowGroup
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_POSITION = "position";

	public static final String PROPERTY_WIDTH = "width";

	protected int width;
	protected byte position = JRCellContents.POSITION_Y_TOP;

	public JRDesignCrosstabRowGroup()
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
	 * @see JRCrosstabRowGroup#getPosition()
	 */
	public void setPosition(byte position)
	{
		byte old = this.position;
		this.position = position;
		getEventSupport().firePropertyChange(PROPERTY_POSITION, old, this.position);
	}

	public int getWidth()
	{
		return width;
	}

	
	/**
	 * Sets the header cell width.
	 * 
	 * @param width the width
	 * @see JRCrosstabRowGroup#getWidth()
	 */
	public void setWidth(int width)
	{
		int old = this.width;
		this.width = width;
		getEventSupport().firePropertyChange(PROPERTY_WIDTH, old, this.width);
	}

	public void setHeader(JRDesignCellContents header)
	{
		super.setHeader(header);
		
		setCellOrigin(this.header, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER,
						getName(), null));
	}

	public void setTotalHeader(JRDesignCellContents totalHeader)
	{
		super.setTotalHeader(totalHeader);
		
		setCellOrigin(this.totalHeader, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER,
						getName(), null));
	}

	void setParent(JRDesignCrosstab parent)
	{
		super.setParent(parent);
		
		setCellOrigin(this.header, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER,
						getName(), null));
		setCellOrigin(this.totalHeader, 
				new JRCrosstabOrigin(getParent(), JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER,
						getName(), null));
	}
}
