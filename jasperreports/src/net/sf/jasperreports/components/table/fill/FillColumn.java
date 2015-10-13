/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class FillColumn implements JRPropertiesHolder
{

	private BaseColumn tableColumn;
	private int width;
	private List<FillColumn> subcolumns;
	private Integer colSpan;

	private JRPropertiesMap properties;
	
	public FillColumn(BaseColumn tableColumn, JRPropertiesMap properties)
	{
		this(tableColumn,  
				tableColumn.getWidth(), 
				null, properties);
	}
	
	public FillColumn(BaseColumn tableColumn, int width,
			List<FillColumn> subcolumns, JRPropertiesMap properties)
	{
		super();
		
		this.tableColumn = tableColumn;
		this.width = width;
		this.subcolumns = subcolumns;
		this.properties = properties;
	}

	public BaseColumn getTableColumn()
	{
		return tableColumn;
	}

	public int getWidth()
	{
		return width;
	}

	public int getColSpan()
	{
		if (colSpan == null)
		{
			colSpan = 1;
			if (subcolumns != null && subcolumns.size() > 0)
			{
				colSpan = 0;
				for (FillColumn subcolumn : subcolumns)
				{
					colSpan += subcolumn.getColSpan();
				}
			}
		}
		return colSpan;
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
	
	public boolean hasProperties()
	{
		return properties != null && properties.hasProperties();
	}

	public JRPropertiesMap getPropertiesMap()
	{
		return properties;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}

}
