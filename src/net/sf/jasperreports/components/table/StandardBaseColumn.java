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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class StandardBaseColumn implements BaseColumn, Serializable, JRChangeEventsSupport
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_PRINT_WHEN_EXPRESSION = "printWhenExpression";
	public static final String PROPERTY_TABLE_HEADER = "tableHeader";
	public static final String PROPERTY_TABLE_FOOTER = "tableFooter";
	public static final String PROPERTY_COLUMN_HEADER = "columnHeader";
	public static final String PROPERTY_COLUMN_FOOTER = "columnFooter";
	public static final String PROPERTY_GROUP_HEADERS = "groupHeaders";
	public static final String PROPERTY_GROUP_FOOTERS = "groupFooters";
	public static final String PROPERTY_WIDTH = "width";
	
	private JRExpression printWhenExpression;
	private Cell tableHeader;
	private Cell tableFooter;
	private List<GroupCell> groupHeaders; 
	private List<GroupCell> groupFooters; 
	private Cell columnHeader;
	private Cell columnFooter;
	private Integer width;

	public StandardBaseColumn()
	{
		groupHeaders = new ArrayList<GroupCell>();
		groupFooters = new ArrayList<GroupCell>();
	}

	public StandardBaseColumn(BaseColumn column, ColumnFactory factory)
	{
		this.printWhenExpression = factory.getBaseObjectFactory().getExpression(
				column.getPrintWhenExpression());
		
		this.tableHeader = factory.createCell(column.getTableHeader());
		this.tableFooter = factory.createCell(column.getTableFooter());
		this.groupHeaders = factory.createGroupCells(column.getGroupHeaders());
		this.groupFooters = factory.createGroupCells(column.getGroupFooters());
		this.columnHeader = factory.createCell(column.getColumnHeader());
		this.columnFooter = factory.createCell(column.getColumnFooter());

		this.width = column.getWidth();
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

	public void setPrintWhenExpression(JRExpression printWhenExpression)
	{
		Object old = this.printWhenExpression;
		this.printWhenExpression = printWhenExpression;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_EXPRESSION, 
				old, this.printWhenExpression);
	}

	public void setColumnHeader(Cell header)
	{
		Object old = this.columnHeader;
		this.columnHeader = header;
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_HEADER, 
				old, this.columnHeader);
	}

	public void setColumnFooter(Cell header)
	{
		Object old = this.columnFooter;
		this.columnFooter = header;
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_FOOTER, 
				old, this.columnFooter);
	}
	
	public Object clone()
	{
		StandardBaseColumn clone = null;
		try
		{
			clone = (StandardBaseColumn) super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.tableHeader = JRCloneUtils.nullSafeClone(tableHeader);
		clone.tableFooter = JRCloneUtils.nullSafeClone(tableFooter);
		clone.groupHeaders = JRCloneUtils.cloneList(groupHeaders);
		clone.groupFooters = JRCloneUtils.cloneList(groupFooters);
		clone.columnHeader = JRCloneUtils.nullSafeClone(columnHeader);
		clone.columnFooter = JRCloneUtils.nullSafeClone(columnFooter);
		clone.printWhenExpression = JRCloneUtils.nullSafeClone(printWhenExpression);
		clone.eventSupport = null;
		return clone;
	}

	public Cell getTableHeader()
	{
		return tableHeader;
	}

	public void setTableHeader(Cell tableHeader)
	{
		Object old = this.tableHeader;
		this.tableHeader = tableHeader;
		getEventSupport().firePropertyChange(PROPERTY_TABLE_HEADER, 
				old, this.tableHeader);
	}

	public Cell getTableFooter()
	{
		return tableFooter;
	}

	public void setTableFooter(Cell tableFooter)
	{
		Object old = this.tableFooter;
		this.tableFooter = tableFooter;
		getEventSupport().firePropertyChange(PROPERTY_TABLE_FOOTER, 
				old, this.tableFooter);
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		Object old = this.width;
		this.width = width;
		getEventSupport().firePropertyChange(PROPERTY_WIDTH, 
				old, this.width);
	}

	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}

	public List<GroupCell> getGroupHeaders()
	{
		return groupHeaders;
	}

	public List<GroupCell> getGroupFooters()
	{
		return groupFooters;
	}

	protected int findGroupCellIndex(List<GroupCell> groupCells, String groupName)
	{
		int idx = -1;
		for (ListIterator<GroupCell> it = groupCells.listIterator(); it.hasNext();)
		{
			GroupCell groupCell = it.next();
			if (groupName.equals(groupCell.getGroupName()))
			{
				idx = it.previousIndex();
			}
		}
		return idx;
	}
	
	public Cell getGroupFooter(String groupName)
	{
		int idx = findGroupCellIndex(groupFooters, groupName);
		return idx < 0 ? null : groupFooters.get(idx).getCell();
	}

	public Cell getGroupHeader(String groupName)
	{
		int idx = findGroupCellIndex(groupHeaders, groupName);
		return idx < 0 ? null : groupHeaders.get(idx).getCell();
	}

	public void setGroupHeaders(List<GroupCell> groupHeaders)
	{
		Object old = this.groupHeaders;
		this.groupHeaders = groupHeaders;
		getEventSupport().firePropertyChange(PROPERTY_GROUP_HEADERS, 
				old, this.groupHeaders);
	}

	public void setGroupFooters(List<GroupCell> groupFooters)
	{
		Object old = this.groupFooters;
		this.groupFooters = groupFooters;
		getEventSupport().firePropertyChange(PROPERTY_GROUP_FOOTERS, 
				old, this.groupFooters);
	}

	public void addGroupHeader(GroupCell groupCell)
	{
		groupHeaders.add(groupCell);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_GROUP_HEADERS, 
				groupCell, groupHeaders.size() - 1);
	}

	public void addGroupFooter(GroupCell groupCell)
	{
		groupFooters.add(groupCell);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_GROUP_FOOTERS, 
				groupCell, groupFooters.size() - 1);
	}

	public boolean removeGroupFooter(GroupCell groupCell)
	{
		int idx = groupFooters.indexOf(groupCell);
		if (idx >= 0)
		{
			groupFooters.remove(idx);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_GROUP_FOOTERS, 
					groupCell, idx);
		}
		return idx >= 0;
	}

	public boolean removeGroupHeader(GroupCell groupCell)
	{
		int idx = groupHeaders.indexOf(groupCell);
		if (idx >= 0)
		{
			groupHeaders.remove(idx);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_GROUP_HEADERS, 
					groupCell, idx);
		}
		return idx >= 0;
	}
	
	public void setGroupFooter(String groupName, Cell cell)
	{
		int idx = findGroupCellIndex(groupFooters, groupName);
		if (idx >= 0)
		{
			GroupCell old = groupFooters.get(idx);
			if (cell == null)
			{
				// removing group footer
				groupFooters.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_GROUP_FOOTERS, 
						old, idx);
			}
			else
			{
				// replacing group footer
				StandardGroupCell groupCell = new StandardGroupCell(groupName, cell);
				groupFooters.set(idx, groupCell);
				getEventSupport().fireIndexedPropertyChange(PROPERTY_GROUP_FOOTERS, idx, 
						old, groupCell);
			}
		}
		else if (cell != null)
		{
				// adding group footer
				StandardGroupCell groupCell = new StandardGroupCell(groupName, cell);
				addGroupFooter(groupCell);
		}
	}
	
	public void setGroupHeader(String groupName, Cell cell)
	{
		int idx = findGroupCellIndex(groupHeaders, groupName);
		if (idx >= 0)
		{
			GroupCell old = groupHeaders.get(idx);
			if (cell == null)
			{
				// removing group header
				groupHeaders.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_GROUP_HEADERS, 
						old, idx);
			}
			else
			{
				// replacing group header
				StandardGroupCell groupCell = new StandardGroupCell(groupName, cell);
				groupHeaders.set(idx, groupCell);
				getEventSupport().fireIndexedPropertyChange(PROPERTY_GROUP_HEADERS, idx, 
						old, groupCell);
			}
		}
		else if (cell != null)
		{
			// adding group header
			StandardGroupCell groupCell = new StandardGroupCell(groupName, cell);
			addGroupHeader(groupCell);
		}
	}
}
