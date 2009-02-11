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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.renderers.JRSimpleImageMapRenderer;

import org.jfree.chart.JFreeChart;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRPieChartImageMapRenderer.java 1364 2006-08-31 15:13:20Z lucianc $
 */
public class ImageChartRendererFactory implements ChartRendererFactory
{
	
	public JRRenderable getRenderer(
		JFreeChart chart, 
		ChartHyperlinkProvider chartHyperlinkProvider,
		Rectangle2D rectangle
		)
	{
		BufferedImage bi = 
			new BufferedImage(
				(int)rectangle.getWidth(),
				(int)rectangle.getHeight(),
				BufferedImage.TYPE_INT_ARGB
				);

		List areaHyperlinks = null;

		if (chartHyperlinkProvider != null && chartHyperlinkProvider.hasHyperlinks())
		{
			areaHyperlinks = ChartUtil.getImageAreaHyperlinks(chart, chartHyperlinkProvider, (Graphics2D)bi.getGraphics(), rectangle);
		}
		else
		{
			chart.draw((Graphics2D)bi.getGraphics(), rectangle);
		}

		try
		{
			return new JRSimpleImageMapRenderer(JRImageLoader.loadImageDataFromAWTImage(bi, JRRenderable.IMAGE_TYPE_PNG), areaHyperlinks);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
