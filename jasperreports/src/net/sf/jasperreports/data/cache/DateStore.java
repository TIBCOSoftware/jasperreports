/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DateStore implements BufferColumnStore
{

	private static final Log log = LogFactory.getLog(DateStore.class);

	private final ValueTransformer valueTransformer;
	private final LongArrayStore timeStore;

	public DateStore(int size, ValueTransformer valueTransformer)
	{
		this.valueTransformer = valueTransformer;
		timeStore = new LongArrayStore(size, true);
		
		if (log.isDebugEnabled())
		{
			log.debug(this + ": created time store " + timeStore);
		}
	}

	@Override
	public Class<?> getBaseValuesType()
	{
		return valueTransformer.getResultType();
	}
	
	public void addValue(Object object)
	{
		if (!(object instanceof Date))
		{
			throw new IllegalArgumentException();
		}

		Date value = (Date) object;
		long time = value.getTime();
		timeStore.add(time);
	}

	public boolean full()
	{
		return timeStore.full();
	}

	public void resetValues()
	{
		timeStore.resetValues();
	}

	public ColumnValues createValues()
	{
		ColumnValues timeValues = timeStore.createValues();
		return new TransformedColumnValues(timeValues, valueTransformer);
	}

	public String toString()
	{
		return "DateStore@" + hashCode();
	}

}
