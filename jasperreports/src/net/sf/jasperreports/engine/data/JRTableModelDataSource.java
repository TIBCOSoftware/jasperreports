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

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.data;

import java.util.HashMap;

import javax.swing.table.TableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTableModelDataSource implements JRRewindableDataSource
{
	

	/**
	 *
	 */
	private TableModel tableModel = null;
	private int index = -1;
	private HashMap columnNames = new HashMap();
	

	/**
	 *
	 */
	public JRTableModelDataSource(TableModel model)
	{
		this.tableModel = model;
		
		if (this.tableModel != null)
		{
			for(int i = 0; i < tableModel.getColumnCount(); i++)
			{
				this.columnNames.put(tableModel.getColumnName(i), new Integer(i));
			}
		}
	}
	

	/**
	 *
	 */
	public boolean next()
	{
		this.index++;

		if (this.tableModel != null)
		{
			return (this.index < this.tableModel.getRowCount());
		}

		return false;
	}
	
	
	/**
	 *
	 */
	public Object getFieldValue(JRField jrField) throws JRException
	{
		String fieldName = jrField.getName();
		
		Integer columnIndex = (Integer)this.columnNames.get(fieldName);
		
		if (columnIndex != null)
		{
			return this.tableModel.getValueAt(index, columnIndex.intValue());
		}
		else if (fieldName.startsWith("COLUMN_"))
		{
			return this.tableModel.getValueAt(index, Integer.parseInt(fieldName.substring(7)));
		}
		else
		{
			throw new JRException("Unknown column name : " + fieldName);
		}
	}

	
	/**
	 *
	 */
	public void moveFirst()
	{
		this.index = -1;
	}


}
