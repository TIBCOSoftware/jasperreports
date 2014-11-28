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

import java.util.List;

import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.type.SortOrderEnum;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface DataLevelBucket extends JRCloneable
{

	/**
	 * Returns the class of the bucket value. Any class is allowed as long as it is in the classpath at compile and run time.
	 * @return a <tt>Class</tt> instance representing the bucket value class
	 */
	public Class<?> getValueClass();
		
	/**
	 * Returns the string name of the bucket value class.
	 */
	public String getValueClassName();
		
	/**
	 * Returns the bucket sorting type.
	 * <p>
	 * The possible values are:
	 * <ul>
	 * 	<li>{@link SortOrderEnum#ASCENDING SortOrderEnum.ASCENDING}</li>
	 * 	<li>{@link SortOrderEnum#DESCENDING SortOrderEnum.DESCENDING}</li>
	 * </ul>
	 * 
	 * @return the bucket sorting type
	 * 
	 * @deprecated replaced by {@link #getOrder()}
	 */
	@Deprecated
	public SortOrderEnum getOrderValue();

	/**
	 * Returns the bucket sorting type.
	 * <p>
	 * The possible values are:
	 * <ul>
	 * 	<li>{@link BucketOrder#ASCENDING BucketOrder.ASCENDING}</li>
	 * 	<li>{@link BucketOrder#DESCENDING BucketOrder.DESCENDING}</li>
	 * 	<li>{@link BucketOrder#NONE BucketOrder.NONE}</li>
	 * </ul>
	 * 
	 * @return the bucket sorting type
	 * @see #getComparatorExpression()
	 */
	public BucketOrder getOrder();
	
	/**
	 * Returns the grouping expression.
	 * 
	 * @return the grouping expression
	 */
	public JRExpression getExpression();
	
	
	/**
	 * Returns the comparator expression.
	 * <p>
	 * The result of this expression is used to sort the buckets, in ascending or
	 * descending order (given by {@link #getOrder() getOrder()}.
	 * If the order type is {@link BucketOrder#NONE}, no sorting will be performed
	 * and the comparator expression will be ignored.
	 * </p>
	 * 
	 * <p>
	 * If no comparator expression is specified, the natural order will be used.
	 * </p>
	 * 
	 * <p>
	 * If the bucket has an order by expression, the comparator will be used to
	 * compare values as produced by that expression.
	 * </p>
	 * 
	 * @return the comparator expression
	 */
	public JRExpression getComparatorExpression();

	public List<DataLevelBucketProperty> getBucketProperties();
	
}
