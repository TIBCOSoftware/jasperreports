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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.crosstabs.design;

import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabBucket;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;

/**
 * Implementation of {@link net.sf.jasperreports.crosstabs.JRCrosstabBucket corsstab group bucket}
 * to be used for report designing.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabBucket extends JRBaseCrosstabBucket
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_EXPRESSION = "expression";

	
	/**
	 * Creates a crosstab group bucket.
	 */
	public JRDesignCrosstabBucket()
	{
		super();
	}

	
	/**
	 * Sets the comparator expression.
	 * <p>
	 * The expressions's type should be compatible with {@link java.util.Comparator java.util.Comparator}.
	 * 
	 * @param comparatorExpression the comparator expression
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabBucket#getComparatorExpression()
	 */
	public void setComparatorExpression(JRExpression comparatorExpression)
	{
		this.comparatorExpression = comparatorExpression;
	}

	
	/**
	 * Sets the grouping expression.
	 * 
	 * @param expression the grouping expression
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabBucket#getExpression()
	 */
	public void setExpression(JRDesignExpression expression)
	{
		this.expression = expression;
	}

	
	/**
	 * Sets the sorting type.
	 * 
	 * @param order one of
	 * <ul>
	 * 	<li>{@link net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition#ORDER_ASCENDING Bucket.ORDER_ASCENDING}</li>
	 * 	<li>{@link net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition#ORDER_DESCENDING Bucket.ORDER_DESCENDING}</li>
	 * </ul>
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabBucket#getOrder()
	 */
	public void setOrder(byte order)
	{
		this.order = order;
	}
}
