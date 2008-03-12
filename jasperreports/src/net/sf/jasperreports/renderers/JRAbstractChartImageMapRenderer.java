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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import net.sf.jasperreports.charts.util.ChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartUtil;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImageMapRenderer;

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

	public List renderWithHyperlinks(Graphics2D grx, Rectangle2D renderingArea) throws JRException
	{
		render(grx, renderingArea);
		return ChartUtil.getImageAreaHyperlinks(getChart(), this, grx, renderingArea);
	}

	/**
	 * @deprecated Replaced by {@link #renderWithHyperlinks(Graphics2D, Rectangle2D)}
	 */
	public List getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException
	{
		return ChartUtil.getImageAreaHyperlinks(getChart(), this, null, renderingArea);
	}

}
