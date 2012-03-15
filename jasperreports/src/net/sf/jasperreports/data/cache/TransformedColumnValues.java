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
public class TransformedColumnValues implements ColumnValues, Serializable
{

	private final ColumnValues rawValues;
	private final ValueTransformer transformer;
	
	public TransformedColumnValues(ColumnValues rawValues,
			ValueTransformer transformer)
	{
		this.rawValues = rawValues;
		this.transformer = transformer;
	}

	public int size()
	{
		return rawValues.size();
	}

	public ColumnValuesIterator iterator()
	{
		return new TransformedIterator();
	}

	protected class TransformedIterator implements ColumnValuesIterator
	{
		private final ColumnValuesIterator rawIterator;

		public TransformedIterator()
		{
			rawIterator = rawValues.iterator();
		}
		
		public void moveFirst()
		{
			rawIterator.moveFirst();
		}

		public boolean next()
		{
			return rawIterator.next();
		}

		public Object get()
		{
			Object rawValue = rawIterator.get();
			return transformer.get(rawValue);
		}
		
	}
	
}
