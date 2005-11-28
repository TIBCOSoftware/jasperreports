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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs.design;

import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabCell;
import net.sf.jasperreports.engine.JRConstants;

/**
 * Implementation of {@link net.sf.jasperreports.crosstabs.JRCrosstabCell JRCrosstabCell} to be used
 * for report design.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabCell extends JRBaseCrosstabCell
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	
	/**
	 * Creates a crosstab data cell.
	 */
	public JRDesignCrosstabCell()
	{
		contents = new JRDesignCellContents();
	}
	
	
	/**
	 * Indicates that the cell corresponds to a total column.
	 * 
	 * @param columnTotalGroup the corresponding column group
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabCell#getColumnTotalGroup()
	 */
	public void setColumnTotalGroup(String columnTotalGroup)
	{
		this.columnTotalGroup = columnTotalGroup;
	}
	
	
	/**
	 * Sets the cell contents.
	 * 
	 * @param contents the contents
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabCell#getContents()
	 */
	public void setContents(JRDesignCellContents contents)
	{
		if (contents == null)
		{
			contents = new JRDesignCellContents();
		}
		
		this.contents = contents;
	}

	
	
	/**
	 * Indicates that the cell corresponds to a total row.
	 * 
	 * @param rowTotalGroup the corresponding row group
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabCell#getRowTotalGroup()
	 */
	public void setRowTotalGroup(String rowTotalGroup)
	{
		this.rowTotalGroup = rowTotalGroup;
	}

	
	/**
	 * Sets the cell width.
	 * <p>
	 * This is compulsory for base cells only.
	 * 
	 * @param width the width
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabCell#getWidth()
	 */
	public void setWidth(Integer width)
	{
		this.width = width;
	}

	
	/**
	 * Sets the cell height.
	 * <p>
	 * This is compulsory for base cells only.
	 * 
	 * @param height the height
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabCell#getHeight()
	 */
	public void setHeight(Integer height)
	{
		this.height = height;
	}
}
