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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BaseDataLevelBucket implements DataLevelBucket, Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String EXCEPTION_MESSAGE_KEY_BUCKET_LOAD_ERROR = "engine.analytics.dataset.bucket.load.error";
	
	protected String valueClassName;
	protected String valueClassRealName;
	protected Class<?> valueClass;

	// only used for deserialization
	@Deprecated
	protected SortOrderEnum orderValue = null;
	
	protected BucketOrder order = BucketOrder.ASCENDING;
	protected JRExpression expression;
	protected JRExpression comparatorExpression;
	
	protected List<DataLevelBucketProperty> bucketProperties;

	protected BaseDataLevelBucket()
	{
		this.bucketProperties = new ArrayList<DataLevelBucketProperty>();
	}
	
	public BaseDataLevelBucket(DataLevelBucket bucket, JRBaseObjectFactory factory)
	{
		factory.put(bucket, this);
		
		this.valueClassName = bucket.getValueClassName();
		this.order = bucket.getOrder();
		this.expression = factory.getExpression(bucket.getExpression());
		this.comparatorExpression = factory.getExpression(bucket.getComparatorExpression());
		
		List<DataLevelBucketProperty> properties = bucket.getBucketProperties();
		this.bucketProperties = new ArrayList<DataLevelBucketProperty>(properties.size());
		for (DataLevelBucketProperty property : properties)
		{
			this.bucketProperties.add(factory.getDataLevelBucketProperty(property));
		}
	}

	public String getValueClassName()
	{
		return valueClassName;
	}

	@Deprecated
	public SortOrderEnum getOrderValue()
	{
		return BucketOrder.toSortOrderEnum(order);
	}
	
	@Override
	public BucketOrder getOrder()
	{
		return order;
	}

	public JRExpression getExpression()
	{
		return expression;
	}

	public JRExpression getComparatorExpression()
	{
		return comparatorExpression;
	}
	
	public Class<?> getValueClass()
	{
		if (valueClass == null)
		{
			String className = getValueClassRealName();
			if (className != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(className);
				}
				catch (ClassNotFoundException e)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_BUCKET_LOAD_ERROR,
							(Object[])null,
							e);
				}
			}
		}
		
		return valueClass;
	}

	private String getValueClassRealName()
	{
		if (valueClassRealName == null)
		{
			valueClassRealName = JRClassLoader.getClassRealName(valueClassName);
		}
		
		return valueClassRealName;
	}

	@Override
	public List<DataLevelBucketProperty> getBucketProperties()
	{
		// TODO lucianc unmodifiable?
		return bucketProperties;
	}

	public Object clone()
	{
		BaseDataLevelBucket clone = null;
		try
		{
			clone = (BaseDataLevelBucket) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.expression = JRCloneUtils.nullSafeClone(expression);
		clone.comparatorExpression = JRCloneUtils.nullSafeClone(comparatorExpression);
		clone.bucketProperties = JRCloneUtils.cloneList(bucketProperties);
		return clone;
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (orderValue != null && order == null)
		{
			// deserializing old version object
			order = BucketOrder.fromSortOrderEnum(orderValue);
			orderValue = null;
		}
	}

}
