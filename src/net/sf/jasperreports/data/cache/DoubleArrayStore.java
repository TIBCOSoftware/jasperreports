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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class DoubleArrayStore implements BufferColumnStore
{

	private static final Log log = LogFactory.getLog(DoubleArrayStore.class);

	private final double[] values;
	private int count;
	
	public DoubleArrayStore(int size)
	{
		this.values = new double[size];
		reset();
	}
	
	private void reset()
	{
		this.count = 0;
	}

	public void addValue(Object object)
	{
		if (!(object instanceof Double))
		{
			throw new IllegalArgumentException();
		}
		
		double value = (Double) object;
		values[count] = value;
		++count;
	}

	public boolean full()
	{
		return count >= values.length;
	}
	
	public void resetValues()
	{
		reset();
	}
	
	public ColumnValues createValues()
	{
		if (count == 0)
		{
			// no values
			if (log.isDebugEnabled())
			{
				log.debug(this + ": empty values");
			}
			
			return EmptyColumnValues.instance();
		}
		
		if (count == 1)
		{
			if (log.isDebugEnabled())
			{
				log.debug(this + ": single value");
			}
			
			return new SingleObjectValue(values[0]);
		}
		
		//TODO lucianc run length
		
		if (log.isDebugEnabled())
		{
			log.debug(this + ": creating values of size " + count);
		}
		
		double[] doubleValues = new double[count];
		System.arraycopy(values, 0, doubleValues, 0, count);
		return new DoubleArrayValues(doubleValues);
	}

	public String toString()
	{
		return "DoubleArrayStore@" + hashCode();
	}

}
