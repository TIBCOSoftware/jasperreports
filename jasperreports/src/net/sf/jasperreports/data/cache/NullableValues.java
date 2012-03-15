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

import java.io.Serializable;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class NullableValues implements ColumnValues, Serializable
{

	private final ColumnValues nullValues;
	private final ColumnValues values;

	public NullableValues(ColumnValues nullValues, ColumnValues values)
	{
		this.nullValues = nullValues;
		this.values = values;
	}

	public int size()
	{
		return nullValues.size();
	}

	public ColumnValuesIterator iterator()
	{
		ColumnValuesIterator nullsIterator = nullValues.iterator();
		ColumnValuesIterator valuesIterator = values.iterator();
		return new NullableIterator(nullsIterator, valuesIterator);
	}

}

class NullableIterator implements ColumnValuesIterator
{

	private final ColumnValuesIterator nulls;
	private final ColumnValuesIterator values;
	private Object value;

	public NullableIterator(ColumnValuesIterator nulls,
			ColumnValuesIterator values)
	{
		this.nulls = nulls;
		this.values = values;
	}

	public void moveFirst()
	{
		nulls.moveFirst();
		values.moveFirst();
		value = null;
	}

	public boolean next()
	{
		if (!nulls.next())
		{
			return false;
		}
		
		boolean isNull = (Boolean) nulls.get();
		if (isNull)
		{
			value = null;
		}
		else
		{
			if (!values.next())
			{
				throw new IllegalStateException();
			}
			
			value = values.get();
		}
		return true;
	}

	public Object get()
	{
		return value;
	}
	
}