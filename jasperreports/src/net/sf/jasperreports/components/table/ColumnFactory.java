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
package net.sf.jasperreports.components.table;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ColumnFactory implements ColumnVisitor<BaseColumn>
{

	private final JRBaseObjectFactory factory;
	
	public ColumnFactory(JRBaseObjectFactory factory)
	{
		this.factory = factory;
	}

	public List<BaseColumn> createColumns(List<BaseColumn> columns)
	{
		List<BaseColumn> createdCols = new ArrayList<BaseColumn>(columns.size());
		for (BaseColumn tableColumn : columns)
		{
			BaseColumn column = tableColumn.visitColumn(this);
			createdCols.add(column);
		}
		return createdCols;
	}
	
	public Cell createCell(Cell cell)
	{
		Cell newCell;
		if (cell == null)
		{
			newCell = null;
		}
		else
		{
			newCell = new CompiledCell(cell, factory);
		}
		return newCell;
	}
	
	public List<GroupCell> createGroupCells(List<GroupCell> cells)
	{
		List<GroupCell> newCells;
		if (cells == null)
		{
			newCells = null;
		}
		else
		{
			newCells = new ArrayList<GroupCell>(cells.size());
			for (GroupCell groupCell : cells)
			{
				GroupCell newCell = new StandardGroupCell(groupCell, this);
				newCells.add(newCell);
			}
		}
		return newCells;
	}
	
	public BaseColumn visitColumn(Column column)
	{
		return new StandardColumn(column, this);
	}

	public BaseColumn visitColumnGroup(ColumnGroup columnGroup)
	{
		return new StandardColumnGroup(columnGroup, this);
	}

	public JRBaseObjectFactory getBaseObjectFactory()
	{
		return factory;
	}

}
