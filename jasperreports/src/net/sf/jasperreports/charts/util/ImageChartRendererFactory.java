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
import java.awt.image.BufferedImage;
import java.util.List;

import org.jfree.chart.JFreeChart;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.SimpleDataRenderer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ImageChartRendererFactory extends AbstractChartRenderableFactory
{
	
	@Override
	public Renderable getRenderable(
		JasperReportsContext jasperReportsContext,
		JFreeChart chart, 
		ChartHyperlinkProvider chartHyperlinkProvider,
		Rectangle2D rectangle
		)
	{
		int dpi = JRPropertiesUtil.getInstance(jasperReportsContext).getIntegerProperty(Renderable.PROPERTY_IMAGE_DPI, 72);
		double scale = dpi/72d;
		
		BufferedImage bi = 
			new BufferedImage(
				(int) (scale * (int)rectangle.getWidth()),
				(int) (scale * rectangle.getHeight()),
				BufferedImage.TYPE_INT_ARGB
				);

		List<JRPrintImageAreaHyperlink> areaHyperlinks = null;

		Graphics2D grx = bi.createGraphics();
		try
		{
			grx.scale(scale, scale);

			if (chartHyperlinkProvider != null && chartHyperlinkProvider.hasHyperlinks())
			{
				areaHyperlinks = ChartUtil.getImageAreaHyperlinks(chart, chartHyperlinkProvider, grx, rectangle);
			}
			else
			{
				chart.draw(grx, rectangle);
			}
		}
		finally
		{
			grx.dispose();
		}

		try
		{
			return new SimpleDataRenderer(JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(bi, ImageTypeEnum.PNG), areaHyperlinks);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
