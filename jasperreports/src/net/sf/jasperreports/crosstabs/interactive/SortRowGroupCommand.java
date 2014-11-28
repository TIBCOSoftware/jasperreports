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

import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.analytics.dataset.BucketOrder;
import net.sf.jasperreports.engine.fill.JRFillCrosstab;
import net.sf.jasperreports.web.commands.Command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class SortRowGroupCommand implements Command
{
	
	private static final Log log = LogFactory.getLog(SortRowGroupCommand.class);

	private final JRDesignCrosstab crosstab;
	private final SortRowGroupData sortData;
	
	private JRDesignCrosstabRowGroup rowGroup;
	private boolean lastRowGroup;
	
	private CrosstabOrderAttributes oldOrderAttributes;
	private BucketOrder newOrder;

	public SortRowGroupCommand(JRDesignCrosstab crosstab, SortRowGroupData sortData)
	{
		this.crosstab = crosstab;
		this.sortData = sortData;
	}
	
	@Override
	public void execute()
	{
		JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
		rowGroup = (JRDesignCrosstabRowGroup) rowGroups[sortData.getGroupIndex()];
		lastRowGroup = sortData.getGroupIndex() == rowGroups.length - 1;

		oldOrderAttributes = new CrosstabOrderAttributes(crosstab);
		
		newOrder = sortData.getOrder();
		
		setOrder();
	}

	protected void setOrder()
	{
		oldOrderAttributes.prepareSorting();

		if (log.isDebugEnabled())
		{
			log.debug("setting crosstab " + sortData.getCrosstabId() + " row group " + sortData.getGroupIndex() 
					+ " order to " + newOrder);
		}
		
		((JRDesignCrosstabBucket) rowGroup.getBucket()).setOrder(newOrder);
		
		if (lastRowGroup && newOrder != BucketOrder.NONE)
		{
			// sorting the last row group, we need to reset the column order by
			crosstab.getPropertiesMap().setProperty(JRFillCrosstab.PROPERTY_ORDER_BY_COLUMN, null);
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
