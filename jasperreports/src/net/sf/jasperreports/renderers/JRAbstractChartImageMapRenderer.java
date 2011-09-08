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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import net.sf.jasperreports.charts.util.ChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartUtil;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageMapRenderer;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;

import org.jfree.chart.JFreeChart;


/**
 * Abstract image map renderer for charts.
 * 
 * @deprecated
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractChartImageMapRenderer extends JFreeChartRenderer implements JRImageMapRenderer, ChartHyperlinkProvider
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public JRAbstractChartImageMapRenderer(JFreeChart chart)
	{
		super(chart);
	}

	public List<JRPrintImageAreaHyperlink> renderWithHyperlinks(Graphics2D grx, Rectangle2D renderingArea) throws JRException
	{
		render(grx, renderingArea);
		return ChartUtil.getImageAreaHyperlinks(getChart(), this, grx, renderingArea);
	}

	/**
	 * @deprecated Replaced by {@link #renderWithHyperlinks(Graphics2D, Rectangle2D)}
	 */
	public List<JRPrintImageAreaHyperlink> getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		return ChartUtil.getImageAreaHyperlinks(getChart(), this, null, renderingArea);
	}

	public boolean hasImageAreaHyperlinks()
	{
		return hasHyperlinks();
	}

}
