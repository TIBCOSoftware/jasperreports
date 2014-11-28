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
package net.sf.jasperreports.engine.analytics.dataset;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.type.SortOrderEnum;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DesignDataLevelBucket extends BaseDataLevelBucket implements JRChangeEventsSupport
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_COMPARATOR_EXPRESSION = "comparatorExpression";

	public static final String PROPERTY_EXPRESSION = "expression";

	public static final String PROPERTY_ORDER = "order";

	public static final String PROPERTY_VALUE_CLASS = "valueClassName";

	public static final String PROPERTY_BUCKET_PROPERTIES = "bucketProperties";

	
	/**
	 * Creates a bucket.
	 */
	public DesignDataLevelBucket()
	{
		super();
	}

	
	/**
	 * Sets the comparator expression.
	 * <p>
	 * The expressions's type should be compatible with {@link java.util.Comparator java.util.Comparator}.
	 * 
	 * @param comparatorExpression the comparator expression
	 * @see DataLevelBucket#getComparatorExpression()
	 */
	public void setComparatorExpression(JRExpression comparatorExpression)
	{
		Object old = this.comparatorExpression;
		this.comparatorExpression = comparatorExpression;
		getEventSupport().firePropertyChange(PROPERTY_COMPARATOR_EXPRESSION, old, this.comparatorExpression);
	}

	
	/**
	 * Sets the grouping expression.
	 * 
	 * @param expression the grouping expression
	 * @see DataLevelBucket#getExpression()
	 */
	public void setExpression(JRExpression expression)
	{
		Object old = this.expression;
		this.expression = expression;
		getEventSupport().firePropertyChange(PROPERTY_EXPRESSION, old, this.expression);
	}

	
	/**
	 * Sets the sorting type.
	 * 
	 * @param orderValue one of
	 * <ul>
	 * 	<li>{@link SortOrderEnum#ASCENDING SortOrderEnum.ASCENDING}</li>
	 * 	<li>{@link SortOrderEnum#DESCENDING SortOrderEnum.DESCENDING}</li>
	 * </ul>
	 * @see DataLevelBucket#getOrderValue()
	 * 
	 * @deprecated replaced by {@link #setOrder(BucketOrder)}
	 */
	@Deprecated
	public void setOrder(SortOrderEnum orderValue)
	{
		BucketOrder order = BucketOrder.fromSortOrderEnum(orderValue);
		setOrder(order);
	}
	
	/**
	 * Sets the sorting type.
	 * 
	 * @param order one of
	 * <ul>
	 * 	<li>{@link BucketOrder#ASCENDING BucketOrder.ASCENDING}</li>
	 * 	<li>{@link BucketOrder#DESCENDING BucketOrder.DESCENDING}</li>
	 * 	<li>{@link BucketOrder#NONE BucketOrder.NONE}</li>
	 * </ul>
	 * @see DataLevelBucket#getOrder()
	 */
	public void setOrder(BucketOrder order)
	{
		Object old = this.order;
		this.order = order;
		getEventSupport().firePropertyChange(PROPERTY_ORDER, old, this.order);
	}
	

	/**
	 * Sets the bucket value class name.
	 * 
	 * @param valueClassName the bucket value class name
	 * @see DataLevelBucket#getValueClassName()
	 */
	public void setValueClassName(String valueClassName)
	{
		String old = this.valueClassName;
		
		this.valueClassName = valueClassName;
		this.valueClass = null;
		this.valueClassRealName = null;
		
		getEventSupport().firePropertyChange(PROPERTY_VALUE_CLASS, old,
				this.valueClassName);
	}

	public void addBucketProperty(DataLevelBucketProperty property)
	{
		bucketProperties.add(property);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_BUCKET_PROPERTIES, property, bucketProperties.size() - 1);
	}
	
	public void removeBucketProperty(DataLevelBucketProperty property)
	{
		int idx = bucketProperties.indexOf(property);
		if (idx >= 0)
		{
			bucketProperties.remove(idx);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_BUCKET_PROPERTIES, bucketProperties, idx);
		}
	}
	
	public Object clone()
	{
		DesignDataLevelBucket clone = (DesignDataLevelBucket) super.clone();
		clone.eventSupport = null;
		return clone;
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}
}
