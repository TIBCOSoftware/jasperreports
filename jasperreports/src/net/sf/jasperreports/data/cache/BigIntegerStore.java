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

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BigIntegerStore implements BufferColumnStore
{

	private static final Log log = LogFactory.getLog(BigIntegerStore.class);
	
	private final BigInteger[] values;
	private int count;
	
	private BigInteger min;
	private BigInteger max;
	
	private LongArrayStore primitiveStore;
	
	public BigIntegerStore(int size)
	{
		this.values = new BigInteger[size];
		reset();
	}
	
	private void reset()
	{
		this.count = 0;
		
		this.min = BigInteger.valueOf(Long.MAX_VALUE);
		this.max = BigInteger.valueOf(Long.MIN_VALUE);
	}

	public void addValue(Object object)
	{
		if (!(object instanceof BigInteger))
		{
			throw new IllegalArgumentException();
		}
		
		BigInteger value = (BigInteger) object;
		values[count] = value;
		++count;

		min = min.min(value);
		max = max.max(value);
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
		
		if (min.equals(max))
		{
			// constant value
			if (log.isDebugEnabled())
			{
				log.debug(this + ": constant values of size " + count);
			}
			
			return new ConstantColumnValue(count, min);
		}
		
		BigInteger delta = max.subtract(min);
		int deltaLength = delta.bitLength();
		boolean useOffset = false;
		ColumnValues primitiveValues = null;
		if (deltaLength < 64)// bitLength() excludes the sign
		{
			// if all values can be stored as long, do not use a BigInteger offset
			useOffset = !(min.signum() >= 0 && min.bitLength() < 64 
					&& max.signum() >=0 && max.bitLength() < 64);
			
			if (log.isDebugEnabled())
			{
				log.debug(this + ": creating privitive store of size " + count
						+ ", with offset " + useOffset);
			}
			
			primitiveValues = createPrimitiveValues(useOffset);
		}
		
		ColumnValues columnValues;
		if (primitiveValues == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug(this + ": creating array store of size " + count);
			}
			
			// TODO lucianc 
			BigInteger[] bigValues = new BigInteger[count];
			System.arraycopy(values, 0, bigValues, 0, count);
			columnValues = new ObjectArrayValues(bigValues);
		}
		else
		{
			ValueTransformer transformer = useOffset 
					? new NumberToBigIntegerOffsetTransformer(min)
					: NumberToBigIntegerTransformer.instance();
			columnValues = new TransformedColumnValues(primitiveValues, transformer);
		}
		return columnValues;
	}

	protected ColumnValues createPrimitiveValues(boolean useOffset)
	{
		if (primitiveStore == null)
		{
			primitiveStore = new LongArrayStore(values.length, true);
		}
		else
		{
			primitiveStore.resetValues();
		}
		
		for (int i = 0; i < count; i++)
		{
			BigInteger value = values[i];
			if (useOffset)
			{
				value = value.subtract(min);
			}
			primitiveStore.add(value.longValue());
		}
		
		ColumnValues primitiveValues = primitiveStore.createValues();
		return primitiveValues;
	}

	protected ColumnValues toByteValues(boolean useOffset)
	{
		byte[] byteValues = new byte[count];
		for (int i = 0; i < count; i++)
		{
			BigInteger value = values[i];
			if (useOffset)
			{
				value = value.subtract(min);
			}
			byteValues[i] = (byte) (value.byteValue() & 0xFF);
		}
		return new ByteArrayValues(byteValues, 1, 0);
	}

	protected ColumnValues toShortValues(boolean useOffset)
	{
		short[] shortValues = new short[count];
		for (int i = 0; i < count; i++)
		{
			BigInteger value = values[i];
			if (useOffset)
			{
				value = value.subtract(min);
			}
			shortValues[i] = (short) (value.shortValue() & 0xFFFF);
		}
		return new ShortArrayValues(shortValues, 0, 1);
	}

	protected ColumnValues toIntValues(boolean useOffset)
	{
		int[] intValues = new int[count];
		for (int i = 0; i < count; i++)
		{
			BigInteger value = values[i];
			if (useOffset)
			{
				value = value.subtract(min);
			}
			intValues[i] = (int) (value.intValue() & 0xFFFFFFFFL);
		}
		return new IntArrayValues(intValues, 0, 1);
	}

	protected ColumnValues toLongValues(boolean useOffset)
	{
		long[] longValues = new long[count];
		for (int i = 0; i < count; i++)
		{
			BigInteger value = values[i];
			if (useOffset)
			{
				value = value.subtract(min);
			}
			longValues[i] = value.longValue();
		}
		return new LongArrayValues(longValues, 1, 0);
	}

	public String toString()
	{
		return "BigIntegerStore@" + hashCode();
	}
}
