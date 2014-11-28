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
package net.sf.jasperreports.crosstabs.interactive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.fill.calculation.OrderByColumnInfo;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.analytics.dataset.BucketOrder;
import net.sf.jasperreports.engine.fill.JRFillCrosstab;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.util.JacksonUtil;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SortByColumnCommand implements Command
{
	
	private static final Log log = LogFactory.getLog(SortByColumnCommand.class);

	private final JasperReportsContext jasperReportsContext;
	private final JRDesignCrosstab crosstab;
	private final SortByColumnData sortData;
	private final JRCrosstabRowGroup lastRowGroup;
	
	private CrosstabOrderAttributes oldOrderAttributes;
	private String newOrderBy;

	public SortByColumnCommand(JasperReportsContext jasperReportsContext, JRDesignCrosstab crosstab, SortByColumnData sortData)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.crosstab = crosstab;
		this.sortData = sortData;
		
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		lastRowGroup = rowGroups[rowGroups.length - 1];
	}
	
	@Override
	public void execute() throws CommandException
	{
		oldOrderAttributes = new CrosstabOrderAttributes(crosstab);
		
		BucketOrder order = sortData.getOrder();
		if (order == BucketOrder.NONE)
		{
			newOrderBy = null;
		}
		else
		{
			OrderByColumnInfo orderByInfo = new OrderByColumnInfo();
			orderByInfo.setMeasureIndex(sortData.getMeasureIndex()); 
			orderByInfo.setOrder(BucketOrder.toSortOrderEnum(order));
			orderByInfo.setColumnValues(sortData.getColumnValues());
			newOrderBy = JacksonUtil.getInstance(jasperReportsContext).getJsonString(orderByInfo);
		}

		setOrder();
	}

	protected void setOrder()
	{
		oldOrderAttributes.prepareSorting();
		
		if (log.isDebugEnabled())
		{
			log.debug("setting crosstab " + sortData.getCrosstabId() + " order by to " + newOrderBy);
		}
		
		if (newOrderBy == null)
		{
			crosstab.getPropertiesMap().removeProperty(JRFillCrosstab.PROPERTY_ORDER_BY_COLUMN);
		}
		else
		{
			crosstab.getPropertiesMap().setProperty(JRFillCrosstab.PROPERTY_ORDER_BY_COLUMN, newOrderBy);
			
			// clearing the order of the last row group so that the ordering does not reappear when unsorting by the column
			((JRDesignCrosstabBucket) lastRowGroup.getBucket()).setOrder(BucketOrder.NONE);
		}
	}

	@Override
	public void undo()
	{
		oldOrderAttributes.restore();
	}

	@Override
	public void redo()
	{
		setOrder();
	}

}
