/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.fill.BucketOrderer;
import net.sf.jasperreports.crosstabs.fill.BucketOrdererProvider;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.analytics.dataset.BucketOrder;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class OrderByColumnProvider implements BucketOrdererProvider
{
	
	private static final Log log = LogFactory.getLog(OrderByColumnProvider.class);
	
	public static final String PROPERTY_ORDER_BY_COLUMN = JRPropertiesUtil.PROPERTY_PREFIX + "crosstab.order.by.column";

	@Override
	public BucketOrderer createOrderer(JasperReportsContext jasperReportsContext, 
			JRCrosstab crosstab, JRCrosstabGroup group)
	{
		if (!(group instanceof JRCrosstabRowGroup))
		{
			return null;
		}
		
		String orderByProperty = crosstab.getPropertiesMap().getProperty(PROPERTY_ORDER_BY_COLUMN);
		if (orderByProperty == null || orderByProperty.isEmpty())
		{
			return null;
		}
		
		boolean isLast = isLastGroup(crosstab, (JRCrosstabRowGroup) group);
		if (!isLast && group.getBucket().getOrder() != BucketOrder.NONE)
		{
			// ordering by column only applies to nesting groups is they are not already ordered
			return null;
		}
		
		OrderByColumnInfo orderInfo = JacksonUtil.getInstance(jasperReportsContext).loadObject(
				orderByProperty, OrderByColumnInfo.class);
		if (orderInfo.getOrder() == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("order or measure name not present in order by column information");
			}
			return null;
		}
		
		return new OrderByColumnOrderer(orderInfo);
	}

	protected boolean isLastGroup(JRCrosstab crosstab, JRCrosstabRowGroup group)
	{
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		JRCrosstabRowGroup lastGroup = rowGroups[rowGroups.length - 1];
		// comparing group names because object might differ (base/fill/etc)
		return group.getName().equals(lastGroup.getName());
	}

}
