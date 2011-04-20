/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.crosstabs.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * Base read-only implementation of {@link net.sf.jasperreports.crosstabs.JRCrosstabBucket JRCrosstabBucket}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseCrosstabBucket implements JRCrosstabBucket, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected SortOrderEnum orderValue = SortOrderEnum.ASCENDING;
	protected JRExpression expression;
	protected JRExpression orderByExpression;
	protected JRExpression comparatorExpression;

	protected JRBaseCrosstabBucket()
	{
	}
	
	public JRBaseCrosstabBucket(JRCrosstabBucket bucket, JRBaseObjectFactory factory)
	{
		factory.put(bucket, this);
		
		this.orderValue = bucket.getOrderValue();
		this.expression = factory.getExpression(bucket.getExpression());
		this.orderByExpression = factory.getExpression(bucket.getOrderByExpression());
		this.comparatorExpression = factory.getExpression(bucket.getComparatorExpression());
	}

	public SortOrderEnum getOrderValue()
	{
		return orderValue;
	}

	public JRExpression getExpression()
	{
		return expression;
	}

	public JRExpression getOrderByExpression()
	{
		return orderByExpression;
	}

	public JRExpression getComparatorExpression()
	{
		return comparatorExpression;
	}
	
	public Object clone()
	{
		try
		{
			JRBaseCrosstabBucket clone = (JRBaseCrosstabBucket) super.clone();
			clone.expression = (JRExpression) JRCloneUtils.nullSafeClone(expression);
			clone.orderByExpression = (JRExpression) JRCloneUtils.nullSafeClone(orderByExpression);
			clone.comparatorExpression = (JRExpression) JRCloneUtils.nullSafeClone(comparatorExpression);
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
	}


	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private byte order;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			orderValue = SortOrderEnum.getByValue(order);
		}
	}
	
}
