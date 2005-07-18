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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.util;

import java.util.Map;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CategoryLabelGenerator extends StandardCategoryItemLabelGenerator 
{
	private static final long serialVersionUID = 10000;
	
	private Map labelsMap = null;
	
	public CategoryLabelGenerator(Map labelsMap)
	{
		this.labelsMap = labelsMap;
	}

	public String generateLabel(CategoryDataset dataset, int series, int category) 
	{
		Comparable seriesName = dataset.getRowKey(series);
		Map labels = (Map)labelsMap.get(seriesName);
		if(labels != null)
		{
			return (String)labels.get(dataset.getColumnKey(category));
		}
		return super.generateLabel(dataset, series, category);
	}
}