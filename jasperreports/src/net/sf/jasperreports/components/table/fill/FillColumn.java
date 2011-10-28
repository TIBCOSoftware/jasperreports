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
package net.sf.jasperreports.components.table.fill;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FillColumn
{

	private BaseColumn tableColumn;
	private int width;
	private List<FillColumn> subcolumns;
	
	public FillColumn(BaseColumn tableColumn)
	{
		this(tableColumn,  
				tableColumn.getWidth(), 
				null);
	}
	
	public FillColumn(BaseColumn tableColumn, int width,
			List<FillColumn> subcolumns)
	{
		super();
		
		this.tableColumn = tableColumn;
		this.width = width;
		this.subcolumns = subcolumns;
	}

	public BaseColumn getTableColumn()
	{
		return tableColumn;
	}

	public int getWidth()
	{
		return width;
	}

	public List<FillColumn> getSubcolumns()
	{
		return subcolumns;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			//quick exit
			return true;
		}
		
		if (!(obj instanceof FillColumn))
		{
			return false;
		}
		
		FillColumn col = (FillColumn) obj;
		
		return tableColumn == col.tableColumn // instance comparison
			&& width == col.width
			&& (subcolumns == null ? col.subcolumns == null : 
				col.subcolumns != null && subcolumns.equals(col.subcolumns));
	}

	@Override
	public int hashCode()
	{
		int hash = tableColumn.hashCode();
		hash = hash * 37 + width;
		hash = hash * 37 + (subcolumns == null ? 0 : subcolumns.hashCode());
		return hash;
	}
	
}
