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

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.crosstabs.fill.calculation.ColumnValueInfo;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.analytics.dataset.BucketOrder;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DataColumnInfo implements Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private Integer sortMeasureIndex;
	private BucketOrder order;
	private List<ColumnValueInfo> columnValues;

	public BucketOrder getOrder()
	{
		return order;
	}

	public void setOrder(BucketOrder order)
	{
		this.order = order;
	}

	public List<ColumnValueInfo> getColumnValues()
	{
		return columnValues;
	}

	public void setColumnValues(List<ColumnValueInfo> columnValues)
	{
		this.columnValues = columnValues;
	}

	public Integer getSortMeasureIndex()
	{
		return sortMeasureIndex;
	}

	public void setSortMeasureIndex(Integer sortMeasureIndex)
	{
		this.sortMeasureIndex = sortMeasureIndex;
	}
	
}
