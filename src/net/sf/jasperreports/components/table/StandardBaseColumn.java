/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class StandardBaseColumn implements BaseColumn, Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private JRExpression printWhenExpression;
	private Cell tableHeader;
	private Cell tableFooter;
	private Cell columnHeader;
	private Cell columnFooter;
	private Integer width;
	private Integer rowSpan;

	public StandardBaseColumn()
	{
	}

	public StandardBaseColumn(BaseColumn column, ColumnFactory factory)
	{
		this.printWhenExpression = factory.getBaseObjectFactory().getExpression(
				column.getPrintWhenExpression());
		
		this.tableHeader = factory.createCell(column.getTableHeader());
		this.tableFooter = factory.createCell(column.getTableFooter());
		this.columnHeader = factory.createCell(column.getColumnHeader());
		this.columnFooter = factory.createCell(column.getColumnFooter());

		this.width = column.getWidth();
		this.rowSpan = column.getRowSpan();
	}
	
	public Cell getColumnHeader()
	{
		return columnHeader;
	}
	
	public Cell getColumnFooter()
	{
		return columnFooter;
	}

	public JRExpression getPrintWhenExpression()
	{
		return printWhenExpression;
	}

	public Integer getRowSpan()
	{
		return rowSpan;
	}

	public void setPrintWhenExpression(JRExpression printWhenExpression)
	{
		this.printWhenExpression = printWhenExpression;
	}

	public void setColumnHeader(Cell header)
	{
		this.columnHeader = header;
	}

	public void setColumnFooter(Cell header)
	{
		this.columnFooter = header;
	}

	public void setRowSpan(Integer rowSpan)
	{
		this.rowSpan = rowSpan;
	}
	
	public Object clone()
	{
		try
		{
			StandardBaseColumn clone = (StandardBaseColumn) super.clone();
			clone.columnHeader = (Cell) JRCloneUtils.nullSafeClone(columnHeader);
			clone.columnFooter = (Cell) JRCloneUtils.nullSafeClone(columnFooter);
			clone.printWhenExpression = (JRExpression) JRCloneUtils.nullSafeClone(
					printWhenExpression);
			return clone;
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}

	public Cell getTableHeader()
	{
		return tableHeader;
	}

	public void setTableHeader(Cell tableHeader)
	{
		this.tableHeader = tableHeader;
	}

	public Cell getTableFooter()
	{
		return tableFooter;
	}

	public void setTableFooter(Cell tableFooter)
	{
		this.tableFooter = tableFooter;
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

}
