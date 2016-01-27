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
package net.sf.jasperreports.charts.util;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.chart.JFreeChart;

import net.sf.jasperreports.engine.ImageMapRenderable;
import net.sf.jasperreports.engine.JRAbstractSvgRenderer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class DrawChartRenderer extends JRAbstractSvgRenderer implements ImageMapRenderable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private JFreeChart chart;
	private ChartHyperlinkProvider chartHyperlinkProvider;
	
	public DrawChartRenderer(JFreeChart chart, ChartHyperlinkProvider chartHyperlinkProvider)
	{
		this.chart = chart;
		this.chartHyperlinkProvider = chartHyperlinkProvider;
	}

	public void render(JasperReportsContext jasperReportsContext, Graphics2D grx, Rectangle2D rectangle) 
	{
		if (chart != null)
		{
			chart.draw(grx, rectangle);
		}
	}
	
	/**
	 * @deprecated To be removed.
	 */
	public List<JRPrintImageAreaHyperlink> renderWithHyperlinks(Graphics2D grx, Rectangle2D rectangle) 
	{
		try
		{
			render(grx, rectangle);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return ChartUtil.getImageAreaHyperlinks(chart, chartHyperlinkProvider, grx, rectangle);
	}
	
	/**
	 *
	 */
	public List<JRPrintImageAreaHyperlink> getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		return ChartUtil.getImageAreaHyperlinks(chart, chartHyperlinkProvider, null, renderingArea);
	}

	public boolean hasImageAreaHyperlinks()
	{
		return chartHyperlinkProvider != null && chartHyperlinkProvider.hasHyperlinks();
	}
}
