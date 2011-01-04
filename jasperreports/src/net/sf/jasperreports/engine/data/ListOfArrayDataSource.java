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
package net.sf.jasperreports.engine.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBeanCollectionDataSource.java 3939 2010-08-20 09:52:00Z teodord $
 */
public class ListOfArrayDataSource implements JRRewindableDataSource
{


	/**
	 *
	 */
	private List records = new ArrayList();
	private Iterator iterator;
	private Object[] currentRecord;
	private Map columnNamesMap = new HashMap();


	/**
	 *
	 */
	public ListOfArrayDataSource(List records, String[] columnNames)
	{
		this.records = records;
		
		if (columnNames != null)
		{
			for(int i = 0; i < columnNames.length; i++)
			{
				columnNamesMap.put(columnNames[i], Integer.valueOf(i));
			}
		}

		iterator = records.iterator();
	}


	/**
	 *
	 */
	public boolean next()
	{
		boolean hasNext = false;

		if (iterator != null)
		{
			hasNext = iterator.hasNext();

			if (hasNext)
			{
				currentRecord = (Object[])iterator.next();
			}
		}

		return hasNext;
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField jrField)
	{
		Integer fieldIndex = (Integer)columnNamesMap.get(jrField.getName());

		if (fieldIndex == null)
		{
			throw new JRRuntimeException("Field \"" + jrField.getName() + "\" not found in data source.");
		}

		return currentRecord[fieldIndex.intValue()];
	}


	/**
	 *
	 */
	public void moveFirst()
	{
		iterator = records.iterator();
	}


}
