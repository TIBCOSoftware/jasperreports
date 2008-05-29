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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintHyperlink;

import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.general.Dataset;

/**
 * A chart item hyperlink provider used for multiple axis charts.
 * 
 * A separate hyperlink provider can be set for each axis/dataset in the
 * multi chart.  This provider then resolves the provider for each
 * chart entity and delegates the hyperlink retrieval to it.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class MultiAxisChartHyperlinkProvider implements ChartHyperlinkProvider
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private Map datasetProviders = new HashMap();
	
	/**
	 * Creates a multiple axis chart hyperlink provider.
	 */
	public MultiAxisChartHyperlinkProvider()
	{
	}
	
	/**
	 * Registers a hyperlink provider for a specific dataset.
	 * 
	 * The dataset will be used to determine a provider for a chart entity.
	 * 
	 * @param dataset the dataset
	 * @param provider the hyperlink provider
	 */
	public void addHyperlinkProvider(Dataset dataset, ChartHyperlinkProvider provider)
	{
		datasetProviders.put(dataset, provider);
	}

	/**
	 * Returns <code>true</code> if any of the registered providers has hyperlinks.
	 */
	public boolean hasHyperlinks()
	{
		boolean hasHyperlinks = false;
		if (!datasetProviders.isEmpty())
		{
			for (Iterator it = datasetProviders.values().iterator(); it.hasNext();)
			{
				ChartHyperlinkProvider provider = (ChartHyperlinkProvider) it.next();
				if (provider.hasHyperlinks())
				{
					hasHyperlinks = true;
					break;
				}
			}
		}
		return hasHyperlinks;
	}
	
	/**
	 * Determines the provider for the chart entity based on its dataset,
	 * and delegates the call to it.
	 */
	public JRPrintHyperlink getEntityHyperlink(ChartEntity entity)
	{
		JRPrintHyperlink hyperlink = null;
		ChartHyperlinkProvider provider = resolveEntityProvider(entity);
		if (provider != null && provider.hasHyperlinks())
		{
			hyperlink = provider.getEntityHyperlink(entity);
		}
		return hyperlink;
	}
	
	protected ChartHyperlinkProvider resolveEntityProvider(ChartEntity entity)
	{
		ChartHyperlinkProvider provider = null;
		Dataset dataset = getEntityDataset(entity);
		if (dataset != null)
		{
			provider = (ChartHyperlinkProvider) datasetProviders.get(dataset);
		}
		return provider;
	}
	
	protected Dataset getEntityDataset(ChartEntity entity)
	{
		Dataset dataset = null;
		if (entity instanceof CategoryItemEntity)
		{
			dataset = ((CategoryItemEntity) entity).getDataset();
		}
		else if (entity instanceof XYItemEntity)
		{
			dataset = ((XYItemEntity) entity).getDataset();
		}
		return dataset;
	}

}
