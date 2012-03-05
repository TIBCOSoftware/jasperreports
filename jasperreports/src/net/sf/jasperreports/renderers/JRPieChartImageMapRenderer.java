/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.renderers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import net.sf.jasperreports.charts.util.PieChartHyperlinkProvider;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintHyperlink;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;


/**
 * Image map renderer used for pie charts.
 * 
 * @deprecated
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRPieChartImageMapRenderer extends JRAbstractChartImageMapRenderer
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private PieChartHyperlinkProvider pieChartHyperlinkProvider;
	
	public JRPieChartImageMapRenderer(JFreeChart chart, Map<Comparable<?>, JRPrintHyperlink> sectionHyperlinks)
	{
		super(chart);
		
		pieChartHyperlinkProvider = new PieChartHyperlinkProvider(sectionHyperlinks);
	}

	
	public JRPrintHyperlink getEntityHyperlink(ChartEntity entity)
	{
		return new PieChartHyperlinkProvider(sectionHyperlinks).getEntityHyperlink(entity);
	}


	public boolean hasHyperlinks()
	{
		return pieChartHyperlinkProvider.hasHyperlinks();
	}

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	/**
	 * @deprecated
	 */
	private Map<Comparable<?>, JRPrintHyperlink> sectionHyperlinks;

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		
		if (pieChartHyperlinkProvider == null)
		{
			this.pieChartHyperlinkProvider = new PieChartHyperlinkProvider(sectionHyperlinks);
			sectionHyperlinks = null;
		}
	}
}
