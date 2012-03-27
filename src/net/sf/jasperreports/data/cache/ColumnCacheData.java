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
package net.sf.jasperreports.data.cache;

import java.io.IOException;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.data.IndexedDataSource;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ColumnCacheData implements Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private String[] fieldNames;
	private int size;
	private ColumnValues[] values;
	
	public ColumnCacheData(String[] fieldNames, int size, ColumnValues[] values)
	{
		if (fieldNames == null || values == null || fieldNames.length != values.length)
		{
			throw new IllegalArgumentException();
		}
		
		this.fieldNames = fieldNames;
		this.size = size;
		this.values = values;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(size);
		out.writeInt(fieldNames.length);
		for (int i = 0; i < fieldNames.length; i++)
		{
			out.writeObject(fieldNames[i]);
			out.writeObject(values[i]);
		}
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		size = in.readInt();
		int fieldCount = in.readInt();
		fieldNames = new String[fieldCount];
		values = new ColumnValues[fieldCount];
		for (int i = 0; i < fieldCount; i++)
		{
			fieldNames[i] = (String) in.readObject();
			values[i] = (ColumnValues) in.readObject();
		}
	}
	
	public IndexedDataSource createDataSource()
	{
		return new ColumnValuesDataSource(fieldNames, size, values);
	}

	public int size()
	{
		return size;
	}
}