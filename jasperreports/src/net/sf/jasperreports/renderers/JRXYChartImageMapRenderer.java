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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import net.sf.jasperreports.charts.util.XYChartHyperlinkProvider;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintHyperlink;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;


/**
 * Image map renderer used for charts with XY datasets.
 * 
 * @deprecated
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXYChartImageMapRenderer extends JRAbstractChartImageMapRenderer
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private XYChartHyperlinkProvider xyChartHyperlinkProvider = null;
	
	public JRXYChartImageMapRenderer(JFreeChart chart, Map itemHyperlinks)
	{
		super(chart);
		
		xyChartHyperlinkProvider = new XYChartHyperlinkProvider(itemHyperlinks);
	}


	public JRPrintHyperlink getEntityHyperlink(ChartEntity entity)
	{
		return xyChartHyperlinkProvider.getEntityHyperlink(entity);
	}

	public boolean hasHyperlinks()
	{
		return xyChartHyperlinkProvider.hasHyperlinks();
	}

	/**
	 * These fields are only for serialization backward compatibility.
	 */
	private Map itemHyperlinks;

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (xyChartHyperlinkProvider == null)
		{
			this.xyChartHyperlinkProvider = new XYChartHyperlinkProvider(itemHyperlinks);
			itemHyperlinks = null;
		}
	}
}
