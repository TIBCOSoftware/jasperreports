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
package net.sf.jasperreports.crosstabs.xml;

import org.xml.sax.Attributes;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.engine.xml.JRBaseFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRCrosstabBucketFactory extends JRBaseFactory
{
	public static final String ELEMENT_bucket = "bucket";
	public static final String ELEMENT_bucketExpression = "bucketExpression";
	public static final String ELEMENT_orderByExpression = "orderByExpression";
	public static final String ELEMENT_comparatorExpression = "comparatorExpression";
	
	public static final String ATTRIBUTE_order = "order";
	
	public Object createObject(Attributes attributes)
	{
		JRDesignCrosstabBucket bucket = new JRDesignCrosstabBucket();
		
		String orderAttr = attributes.getValue(ATTRIBUTE_order);
		if (orderAttr != null)
		{
			Byte order = (Byte) JRXmlConstants.getCrosstabBucketOrderMap().get(orderAttr);
			bucket.setOrder(order.byteValue());
		}
		
		return bucket;
	}

}
