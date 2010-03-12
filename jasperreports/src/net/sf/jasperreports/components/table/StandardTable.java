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
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class StandardTable implements TableComponent, Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private JRDatasetRun datasetRun;
	private List<BaseColumn> columns;

	public StandardTable()
	{
		columns = new ArrayList<BaseColumn>();
	}

	public StandardTable(TableComponent table, JRBaseObjectFactory factory)
	{
		datasetRun = factory.getDatasetRun(table.getDatasetRun());
		
		ColumnFactory columnFactory = new ColumnFactory(factory);
		columns = columnFactory.createColumns(table.getColumns());
	}
	
	public List<BaseColumn> getColumns()
	{
		return columns;
	}

	public void addColumn(BaseColumn column)
	{
		columns.add(column);
	}

	public JRDatasetRun getDatasetRun()
	{
		return datasetRun;
	}

	public void setDatasetRun(JRDatasetRun datasetRun)
	{
		this.datasetRun = datasetRun;
	}
	
	public Object clone()
	{
		try
		{
			StandardTable clone = (StandardTable) super.clone();
			clone.columns = JRCloneUtils.cloneList(columns);
			return clone;
		} 
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}
	
}
