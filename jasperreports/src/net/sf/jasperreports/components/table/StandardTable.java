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

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class StandardTable implements TableComponent, Serializable, JRChangeEventsSupport
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_DATASET_RUN = "datasetRun";
	public static final String PROPERTY_COLUMNS = "columns";
	public static final String PROPERTY_WHEN_NO_DATA_TYPE = "whenNoDataType";

	private JRDatasetRun datasetRun;
	private List<BaseColumn> columns;
	
	private WhenNoDataTypeTableEnum whenNoDataType;

	public StandardTable()
	{
		columns = new ArrayList<BaseColumn>();
	}

	public StandardTable(TableComponent table, JRBaseObjectFactory factory)
	{
		whenNoDataType = table.getWhenNoDataType();

		datasetRun = factory.getDatasetRun(table.getDatasetRun());
		
		ColumnFactory columnFactory = new ColumnFactory(factory);
		columns = columnFactory.createColumns(table.getColumns());
	}
	
	public List<BaseColumn> getColumns()
	{
		return columns;
	}

	public void setColumns(List<BaseColumn> columns)
	{
		Object old = this.columns;
		this.columns = columns;
		getEventSupport().firePropertyChange(PROPERTY_COLUMNS, 
				old, this.columns);
	}

	public void addColumn(BaseColumn column)
	{
		columns.add(column);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_COLUMNS, 
				column, columns.size() - 1);
	}
	
	public void addColumn(int index, BaseColumn column)
	{
		columns.add(index, column);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_COLUMNS, 
				column, index);
	}

	public boolean removeColumn(BaseColumn column)
	{
		int idx = columns.indexOf(column);
		if (idx >= 0)
		{
			columns.remove(idx);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_COLUMNS, 
					column, idx);
		}
		return idx >= 0;
	}

	public JRDatasetRun getDatasetRun()
	{
		return datasetRun;
	}

	public void setDatasetRun(JRDatasetRun datasetRun)
	{
		Object old = this.datasetRun;
		this.datasetRun = datasetRun;
		getEventSupport().firePropertyChange(PROPERTY_DATASET_RUN, 
				old, this.datasetRun);
	}
	
	public WhenNoDataTypeTableEnum getWhenNoDataType()
	{
		return whenNoDataType;
	}
	
	public void setWhenNoDataType(WhenNoDataTypeTableEnum whenNoDataType)
	{
		Object old = this.whenNoDataType;
		this.whenNoDataType = whenNoDataType;
		getEventSupport().firePropertyChange(PROPERTY_WHEN_NO_DATA_TYPE, old, this.whenNoDataType);
	}
	
	public Object clone()
	{
		StandardTable clone = null;
		try
		{
			clone = (StandardTable) super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.datasetRun = JRCloneUtils.nullSafeClone(datasetRun);
		clone.columns = JRCloneUtils.cloneList(columns);
		clone.eventSupport = null;
		return clone;
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
	
}
