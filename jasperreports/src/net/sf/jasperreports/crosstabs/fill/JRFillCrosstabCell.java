/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.crosstabs.fill;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.engine.fill.JRFillCellContents;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCrosstabCell implements JRCrosstabCell
{
	private JRCrosstabCell parentCell;
	protected JRFillCellContents contents;
	
	public JRFillCrosstabCell(JRCrosstabCell cell, JRFillCrosstabObjectFactory factory)
	{
		factory.put(cell, this);
		
		parentCell = cell;
		
		contents = factory.getCell(cell.getContents(), JRCellContents.TYPE_DATA);
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
		throw new UnsupportedOperationException();
	}
}
