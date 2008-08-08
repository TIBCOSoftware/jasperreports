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
package net.sf.jasperreports.charts.util;

import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintHyperlink;

import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRCategoryChartImageMapRenderer.java 1364 2006-08-31 15:13:20Z lucianc $
 */
public class CategoryChartHyperlinkProvider implements ChartHyperlinkProvider
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private Map itemHyperlinks;
	
	public CategoryChartHyperlinkProvider(Map itemHyperlinks)
	{
		this.itemHyperlinks = itemHyperlinks;
	}


	public JRPrintHyperlink getEntityHyperlink(ChartEntity entity)
	{
		JRPrintHyperlink printHyperlink = null;
		if (hasHyperlinks() && entity instanceof CategoryItemEntity)
		{
			CategoryItemEntity itemEntity = (CategoryItemEntity) entity;
			Comparable serie = itemEntity.getDataset().getRowKey(itemEntity.getSeries());
			Map serieHyperlinks = (Map) itemHyperlinks.get(serie);
			if (serieHyperlinks != null)
			{
				Object category = itemEntity.getCategory();
				printHyperlink = (JRPrintHyperlink) serieHyperlinks.get(category);
			}
		}
		return printHyperlink;
	}

	public boolean hasHyperlinks()
	{
		return itemHyperlinks != null && itemHyperlinks.size() > 0;
	}

}
