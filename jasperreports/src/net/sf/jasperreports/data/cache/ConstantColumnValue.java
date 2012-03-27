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
public class ConstantColumnValue implements ColumnValues, Serializable
{

	private final int size;
	private final Object value;
	private final ValueTransformer valueTransformer;
	
	public ConstantColumnValue(int size, Object value)
	{
		this(size, value, null);
	}
	
	public ConstantColumnValue(int size, Object value, ValueTransformer valueTransformer)
	{
		this.size = size;
		this.value = value;
		this.valueTransformer = valueTransformer;
	}

	public int size()
	{
		return size;
	}

	public ColumnValuesIterator iterator()
	{
		return new IndexColumnValueIterator(size)
		{
			public Object get()
			{
				return valueTransformer == null ? value : valueTransformer.get(value);
			}
		};
	}

}
