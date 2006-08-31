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
package net.sf.jasperreports.renderers;

import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.util.Pair;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYDataset;


/**
 * Image map renderer used for charts with XY datasets.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXYChartImageMapRenderer extends JRAbstractChartImageMapRenderer
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private Map itemHyperlinks;
	
	public JRXYChartImageMapRenderer(JFreeChart chart, Map itemHyperlinks)
	{
		super(chart);
		
		this.itemHyperlinks = itemHyperlinks;
	}


	protected JRPrintHyperlink getEntityHyperlink(ChartEntity entity)
	{
		JRPrintHyperlink printHyperlink = null;
		if (entity instanceof XYItemEntity)
		{
			XYItemEntity itemEntity = (XYItemEntity) entity;
			XYDataset dataset = itemEntity.getDataset();
			Comparable serie = dataset.getSeriesKey(itemEntity.getSeriesIndex());
			Map serieHyperlinks = (Map) itemHyperlinks.get(serie);
			if (serieHyperlinks != null)
			{
				Number x = dataset.getX(itemEntity.getSeriesIndex(), itemEntity.getItem());
				Number y = dataset.getY(itemEntity.getSeriesIndex(), itemEntity.getItem());
				Object xyKey = new Pair(x, y);
				printHyperlink = (JRPrintHyperlink) serieHyperlinks.get(xyKey);
			}
		}
		return printHyperlink;
	}

}
