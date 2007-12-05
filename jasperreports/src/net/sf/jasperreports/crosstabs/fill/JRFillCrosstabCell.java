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
package net.sf.jasperreports.crosstabs.fill;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.engine.fill.JRFillCellContents;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCrosstabCell implements JRCrosstabCell
{
	private JRCrosstabCell parentCell;
	protected JRFillCellContents contents;
	
	public JRFillCrosstabCell(JRCrosstabCell cell, JRFillObjectFactory factory)
	{
		factory.put(cell, this);
		
		parentCell = cell;
		
		contents = factory.getCell(cell.getContents());
	}

	public String getRowTotalGroup()
	{
		return parentCell.getRowTotalGroup();
	}

	public String getColumnTotalGroup()
	{
		return parentCell.getColumnTotalGroup();
	}

	public JRCellContents getContents()
	{
		return contents;
	}

	public JRFillCellContents getFillContents()
	{
		return contents;
	}

	public Integer getWidth()
	{
		return parentCell.getWidth();
	}

	public Integer getHeight()
	{
		return parentCell.getHeight();
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		return null;
	}
}
