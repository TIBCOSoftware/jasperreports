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
package net.sf.jasperreports.crosstabs.fill.calculation;

import java.util.Comparator;

import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bucket definition.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BucketDefinition
{
	
	private static final Log log = LogFactory.getLog(BucketDefinition.class);
	
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
	
	protected final Comparator<Object> bucketValueComparator;

	protected final JRExpression orderByExpression;
	protected final Comparator<Object> orderValueComparator;
	private final CrosstabTotalPositionEnum totalPosition;
	
	private boolean computeTotal;

	
	/**
	 * Creates a bucket.
	 * 
	 * @param valueClass the class of the bucket values
	 * @param orderByExpression expression that provides order by values
	 * @param comparator the comparator to use for bucket sorting
	 * @param order the order type, {@link SortOrderEnum#ASCENDING} or {@link SortOrderEnum#DESCENDING} descending
	 * @param totalPosition the position of the total bucket
	 * @throws JRException
	 */
	public BucketDefinition(Class<?> valueClass, 
			JRExpression orderByExpression, Comparator<Object> comparator, SortOrderEnum order, 
			CrosstabTotalPositionEnum totalPosition) throws JRException
	{
		if (comparator == null && orderByExpression == null 
				&& !Comparable.class.isAssignableFrom(valueClass))
		{
			throw new JRException("The bucket expression values are not comparable and no comparator specified.");
		}
		
		this.orderByExpression = orderByExpression;
		if (orderByExpression == null)
		{
			// we don't have an order by expression
			// the buckets are ordered using the bucket values
			this.bucketValueComparator = createOrderComparator(comparator, order);
			this.orderValueComparator = null;
		}
		else
		{
			// we have an order by expression
			// we only need an internal ordering for bucket values
			if (Comparable.class.isAssignableFrom(valueClass))
			{
				// using natural order
				this.bucketValueComparator = ComparableComparator.getInstance();
			}
			else
			{
				// using an arbitrary rank comparator
				if (log.isDebugEnabled())
				{
					log.debug("Using arbitrary rank comparator for bucket");
				}
				
				this.bucketValueComparator = new ArbitraryRankComparator();
			}
			
			// the comparator is used for order by values
			this.orderValueComparator = createOrderComparator(comparator, order);
		}
		
		this.totalPosition = totalPosition;
		computeTotal = totalPosition != CrosstabTotalPositionEnum.NONE;
	}

	
	protected static Comparator<Object> createOrderComparator(Comparator<Object> comparator, SortOrderEnum order)
	{
		Comparator<Object> orderComparator;
		switch (order)
		{
			case DESCENDING:
			{
				if (comparator == null)
				{
					orderComparator = new ReverseComparator();
				}
				else
				{
					orderComparator = new ReverseComparator(comparator);
				}
				break;
			}
			case ASCENDING:				
			default:
			{
				if (comparator == null)
				{
					orderComparator = ComparableComparator.getInstance();
				}
				else
				{
					orderComparator = comparator;
				}
				break;
			}
		}
		return orderComparator;
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
	public CrosstabTotalPositionEnum getTotalPosition()
	{
		return totalPosition;
	}

	
	public JRExpression getOrderByExpression()
	{
		return orderByExpression;
	}
	
	
	public boolean hasOrderValues()
	{
		return orderByExpression != null;
	}
	
	
	public int compareOrderValues(Object v1, Object v2)
	{
		return orderValueComparator.compare(v1, v2);
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
	public class Bucket implements Comparable<Object>
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
			
			return bucketValueComparator.compare(value, val.value);
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
