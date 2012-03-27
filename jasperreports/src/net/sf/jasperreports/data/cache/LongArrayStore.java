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
public class LongArrayStore implements BufferColumnStore
{

	private static final Log log = LogFactory.getLog(LongArrayStore.class);
	
	protected static enum ValueLength
	{
		BYTE(1), SHORT(2), INT(4), LONG(8);
		
		private final int byteLength;
		
		private ValueLength(int byteLength)
		{
			this.byteLength = byteLength;
		}
		
		public int byteLength()
		{
			return byteLength;
		}
	}
	
	private static final int MAX_RUN_LENGTH = Short.MAX_VALUE - Short.MIN_VALUE;
	private static final int RUN_LENGTH_PENALTY = 32;// 32 bytes

	private final boolean useGCD;
	private final ValueTransformer valueTransformer;
	
	private final long[] values;
	
	private int count;
	
	private long min;
	private long max;
	
	private int runLengthStart;
	private int runLengthCount;
	private int runLengthMax;
	
	public LongArrayStore(int size)
	{
		this(size, null, false);
	}
	
	public LongArrayStore(int size, ValueTransformer valueTransformer)
	{
		this(size, valueTransformer, false);
	}
	
	public LongArrayStore(int size, boolean useGCD)
	{
		this(size, null, useGCD);
	}
	
	public LongArrayStore(int size, ValueTransformer valueTransformer, boolean useGCD)
	{
		this.useGCD = useGCD;
		this.valueTransformer = valueTransformer;
		
		this.values = new long[size];
		reset();
	}
	
	private void reset()
	{
		this.count = 0;
		
		this.min = Long.MAX_VALUE;
		this.max = Long.MIN_VALUE;
		
		this.runLengthStart = -1;
		this.runLengthCount = 0;
		this.runLengthMax = 1;
	}
	
	public void add(long value)
	{
		values[count] = value;
		++count;
		
		if (value < min)
		{
			min = value;
		}
		if (value > max)
		{
			max = value;
		}
		
		if (runLengthStart >= 0 && value == values[runLengthStart] 
				&& (count - runLengthStart) < MAX_RUN_LENGTH)
		{
			if (count - runLengthStart > runLengthMax)
			{
				runLengthMax = count - runLengthStart;
			}
		}
		else
		{
			runLengthStart = count - 1;
			++runLengthCount;
		}
	}

	public void addValue(Object value)
	{
		if (value instanceof Integer
				|| value instanceof Long
				|| value instanceof Byte
				|| value instanceof Short)
		{
			add(((Number) value).longValue());
		}
		else if (value instanceof Character)
		{
			add(((Character) value).charValue());
		}
		else
		{
			throw new IllegalArgumentException();
		}
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
				log.debug(this + ": no values");
			}
			
			return EmptyColumnValues.instance();
		}
		
		if (min == max)
		{
			// constant value
			if (log.isDebugEnabled())
			{
				log.debug(this + ": constant value of size " + count);
			}
			
			//FIXME keep value as primitive?
			// transform the value so that we don't need to store the transformer
			Object value = valueTransformer == null ? min : valueTransformer.get(min);
			return new ConstantColumnValue(count, value);
		}
		
		long linearOffset = 0;
		long linearFactor = 1;
		
		if (min != 0)
		{
			linearOffset = min;
			
			if (log.isDebugEnabled())
			{
				log.debug(this + ": using offset " + linearOffset);
			}
			
			// apply min
			for (int i = 0; i < count; i++)
			{
				values[i] -= linearOffset;
			}
			
			min = 0;
			max -= linearOffset;
		}
		
		if (useGCD)
		{
			long gcd = computeGCD();
			if (gcd > 1)
			{
				if (log.isDebugEnabled())
				{
					log.debug(this + ": using factor " + gcd);
				}
				
				for (int i = 0; i < count; i++)
				{
					values[i] /= gcd;
				}

				max /= gcd;
				linearFactor = gcd;
			}
		}
		
		ValueLength valueLength = getValueLength(max);
		ValueLength runLength = getValueLength(runLengthMax - 1);
		
		ColumnValues runLengthValues = null;
		int originalCount = count;
		if (useRunLength(valueLength, runLength))
		{
			if (log.isDebugEnabled())
			{
				log.debug(this + ": using run lengths count " + runLengthCount + ", original count " + count);
			}
			
			// go with run lengths
			long[] runLengths = new long[runLengthCount];
			int runIdx = 0;
			runLengths[runIdx] = 0;// we start from 0 because we use 1 as offset
			for (int i = 1; i < count; ++i)
			{
				if (values[i] == values[runIdx] && runLengths[runIdx] < MAX_RUN_LENGTH)
				{
					++runLengths[runIdx];
				}
				else
				{
					++runIdx;
					runLengths[runIdx] = 0;
					values[runIdx] = values[i];
				}
			}
			
			// update the values count
			count = runLengthCount;
			
			if (log.isDebugEnabled())
			{
				log.debug(this + ": creating run lengths of count " + runLengthCount 
						+ ", value length " + runLength);
			}
			
			runLengthValues = toValues(runLengthCount, runLengths, runLength, 1, 1);
		}
		
		if (log.isDebugEnabled())
		{
			log.debug(this + ": creating values of count " + count + ", value length " + valueLength);
		}
		
		ColumnValues colValues = toValues(count, values, valueLength, linearFactor, linearOffset);
		if (valueTransformer != null)
		{
			colValues = new TransformedColumnValues(colValues, valueTransformer);
		}
		
		ColumnValues finalValues;
		if (runLengthValues == null)
		{
			finalValues = colValues;
		}
		else
		{
			finalValues = new RunLengthColumnValues(originalCount, colValues, runLengthValues);
		}
		return finalValues;
	}

	protected long computeGCD()
	{
		long gcd = values[0] - min;
		for (int i = 1; i < count; i++)
		{
			gcd = gcd(gcd, values[i] - min);
			if (gcd == 1)
			{
				return gcd;
			}
		}
		return gcd;
	}

	private static long gcd(long a, long b)
	{
		if (b == 0)
		{
			return a;
		}
		
		while (a > 0)
		{
			long t = a;
			a = b % a;
			b = t;
		}
		return b;
	}

	protected ValueLength getValueLength(long value)
	{
		ValueLength valueLength;
		if ((value & 0xffffffffffffff00L) == 0)
		{
			// byte values
			valueLength = ValueLength.BYTE;
		}
		else if ((value & 0xffffffffffff0000L) == 0)
		{
			// short values
			valueLength = ValueLength.SHORT;
		}
		else if ((value & 0xffffffff00000000L) == 0)
		{
			// int values
			valueLength = ValueLength.INT;
		}
		else
		{
			valueLength = ValueLength.LONG;
		}
		return valueLength;
	}

	protected boolean useRunLength(ValueLength valueLength, ValueLength runLength)
	{
		return count * valueLength.byteLength() > 
				runLengthCount * (valueLength.byteLength() + runLength.byteLength()) + RUN_LENGTH_PENALTY;
	}
	
	protected ColumnValues toValues(int count, long[] values, ValueLength valueLength, 
			long linearFactor, long linearOffset)
	{
		ColumnValues colValues;
		switch (valueLength) {
		case BYTE:
			// byte values
			colValues = toByteValues(count, values, linearFactor, linearOffset);
			break;
		case SHORT:
			// short values
			colValues = toShortValues(count, values, linearFactor, linearOffset);
			break;
		case INT:
			// int values
			colValues = toIntValues(count, values, linearFactor, linearOffset);
			break;
		default:
			colValues = toLongValues(count, values, linearFactor, linearOffset);
			break;
		}
		return colValues;
	}

	protected ColumnValues toByteValues(int count, long[] values, long linearFactor, long linearOffset)
	{
		byte[] byteValues = new byte[count];
		for (int i = 0; i < count; i++)
		{
			byteValues[i] = (byte) (values[i] & 0xFF);
		}
		return new ByteArrayValues(byteValues, linearFactor, linearOffset);
	}

	protected ColumnValues toShortValues(int count, long[] values, long linearFactor, long linearOffset)
	{
		short[] shortValues = new short[count];
		for (int i = 0; i < count; i++)
		{
			shortValues[i] = (short) (values[i] & 0xFFFF);
		}
		return new ShortArrayValues(shortValues, linearFactor, linearOffset);
	}

	protected ColumnValues toIntValues(int count, long[] values, long linearFactor, long linearOffset)
	{
		int[] intValues = new int[count];
		for (int i = 0; i < count; i++)
		{
			intValues[i] = (int) (values[i] & 0xFFFFFFFFL);
		}
		return new IntArrayValues(intValues, linearFactor, linearOffset);
	}

	protected ColumnValues toLongValues(int count, long[] values, long linearFactor, long linearOffset)
	{
		// always create a copy
		long[] longValues = new long[count];
		System.arraycopy(values, 0, longValues, 0, count);
		return new LongArrayValues(longValues, linearFactor, linearOffset);
	}

	public String toString()
	{
		return "LongArrayStore@" + hashCode();
	}

}
