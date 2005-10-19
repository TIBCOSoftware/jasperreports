/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs.fill.calculation;

import java.util.Comparator;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * Bucket definition.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BucketDefinition
{	
	/**
	 * Ascending order constant.
	 */
	public static final byte ORDER_ASCENDING = 1;
	
	/**
	 * Descending order constant.
	 */
	public static final byte ORDER_DESCENDING = 2;
	
	/**
	 * Constant indicating that total are not required for this bucket.
	 */
	public static final byte TOTAL_POSITION_NONE = 0;
	
	/**
	 * Constants indicating that totals are to be positioned before the other buckets.
	 */
	public static final byte TOTAL_POSITION_START = 1;
	
	/**
	 * Constants indicating that totals are to be positioned at the end of the other buckets.
	 */
	public static final byte TOTAL_POSITION_END = 2;
	
	/**
	 * Value type used for non-null values.
	 */
	protected static final byte VALUE_TYPE_VALUE = 0;
	
	/**
	 * Value type used for null bucket values.
	 */
	protected static final byte VALUE_TYPE_NULL = 1;
	
	/**
	 * Value type used for total buckets.
	 */
	protected static final byte VALUE_TYPE_TOTAL = 2;

	/**
	 * The total value for this bucket.
	 */
	protected final Bucket VALUE_TOTAL = new Bucket(VALUE_TYPE_TOTAL);
	
	/**
	 * The null bucket.
	 */
	protected final Bucket VALUE_NULL = new Bucket(VALUE_TYPE_NULL);
	
	protected final Comparator comparator;
	private final byte totalPosition;
	
	private boolean computeTotal;

	
	/**
	 * Creates a bucket.
	 * 
	 * @param valueClass the class of the bucket values
	 * @param comparator the comparator to use for bucket sorting
	 * @param order the order type, {@link #ORDER_ASCENDING ORDER_ASCENDING} or {@link #ORDER_DESCENDING ORDER_DESCENDING} descending
	 * @param totalPosition the position of the total bucket
	 * @throws JRException
	 */
	public BucketDefinition(Class valueClass, Comparator comparator, byte order, byte totalPosition) throws JRException
	{
		if (comparator == null && !Comparable.class.isAssignableFrom(valueClass))
		{
			throw new JRException("The bucket expression values are not comparable and no comparator specified.");
		}
		
		switch (order)
		{
			case ORDER_DESCENDING:
			{
				if (comparator == null)
				{
					this.comparator = new ReverseComparator();
				}
				else
				{
					this.comparator = new ReverseComparator(comparator);
				}
				break;
			}
			case ORDER_ASCENDING:				
			default:
			{
				this.comparator = comparator;
				break;
			}
		}
		
		this.totalPosition = totalPosition;
		computeTotal = totalPosition != TOTAL_POSITION_NONE;
	}

	
	/**
	 * Whether this bucket needs total calculation.
	 * 
	 * @return this bucket needs total calculation
	 */
	public boolean computeTotal()
	{
		return computeTotal;
	}

	
	/**
	 * Instructs that the bucket will need total bucket calculation.
	 * 
	 * @see #computeTotal()
	 */
	public void setComputeTotal()
	{
		computeTotal = true;
	}

	
	/**
	 * Returns the total bucket position.
	 * 
	 * @return the total bucket position
	 */
	public byte getTotalPosition()
	{
		return totalPosition;
	}

	
	/**
	 * Returns the comparator used for bucket ordering.
	 * 
	 * @return the comparator used for bucket ordering
	 */
	public Comparator getComparator()
	{
		return comparator;
	}
	
	
	/**
	 * Creates a {@link Bucket BucketValue} object for a given value.
	 * 
	 * @param value the value
	 * @return the corresponding {@link Bucket BucketValue} object
	 */
	public Bucket create(Object value)
	{
		if (value == null)
		{
			return VALUE_NULL;
		}
		
		return new Bucket(value);
	}
	
	
	/**
	 * Bucket value class.
	 * 
	 * @author Lucian Chirita (lucianc@users.sourceforge.net)
	 */
	public class Bucket implements Comparable
	{
		private final Object value;
		private final byte type;
		
		
		/**
		 * Creates a bucket for a value type.
		 * 
		 * @param type the value type
		 */
		protected Bucket(byte type)
		{
			this.value = null;
			this.type = type;
		}
		
		
		/**
		 * Creates a bucket for a value.
		 * 
		 * @param value the value
		 */
		protected Bucket(Object value)
		{
			this.value = value;
			this.type = VALUE_TYPE_VALUE;
		}
		
		
		/**
		 * Returns the bucket value.
		 * 
		 * @return the bucket value
		 */
		public Object getValue()
		{
			return value;
		}
		
		public boolean equals (Object o)
		{
			if (o == null || !(o instanceof Bucket))
			{
				return false;
			}
			
			if (o == this)
			{
				return true;
			}
			
			Bucket v = (Bucket) o;

			if (type != VALUE_TYPE_VALUE)
			{
				return type == v.type;
			}
			
			return v.type == VALUE_TYPE_VALUE && value.equals(v.value);
		}
		
		public int hashCode()
		{
			int hash = type;
			
			if (type == VALUE_TYPE_VALUE)
			{
				hash = 37*hash + value.hashCode();
			}
			
			return hash;
		}
		
		public String toString()
		{
			switch(type)
			{
				case VALUE_TYPE_NULL:
					return "NULL";
				case VALUE_TYPE_TOTAL:
					return "TOTAL";
				case VALUE_TYPE_VALUE:
				default:
					return String.valueOf(value);
			}
		}

		public int compareTo(Object o)
		{
			Bucket val = (Bucket) o;
			if (type != val.type)
			{
				return type - val.type;
			}
			
			if (type != VALUE_TYPE_VALUE)
			{
				return 0;
			}

			if (comparator != null)
			{
				return comparator.compare(value, val.value);
			}
			
			return ((Comparable) value).compareTo(val.value);
		}
		
		
		/**
		 * Decides whether this is a total bucket.
		 * 
		 * @return whether this is a total bucket
		 */
		public boolean isTotal()
		{
			return type == VALUE_TYPE_TOTAL;
		}
	}
}
