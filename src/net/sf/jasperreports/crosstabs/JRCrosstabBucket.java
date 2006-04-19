/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs;

import net.sf.jasperreports.engine.JRExpression;

/**
 * Crosstab groups bucketing information interface.
 * <p>
 * The bucketing informartion consists of the grouping expression
 * and sorting information.
 * The buckets can be sorted according to the natural sorting (if the values
 * implement {@link java.lang.Comparable Comparable}) or using a comparator.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface JRCrosstabBucket
{
	/**
	 * Returns the bucket sorting type.
	 * <p>
	 * The possible values are:
	 * <ul>
	 * 	<li>{@link net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition#ORDER_ASCENDING Bucket.ORDER_ASCENDING}</li>
	 * 	<li>{@link net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition#ORDER_DESCENDING Bucket.ORDER_DESCENDING}</li>
	 * </ul>
	 * 
	 * @return the bucket sorting type
	 */
	public byte getOrder();
	
	
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
	 * 
	 * @return the comparator expression
	 */
	public JRExpression getComparatorExpression();
}
