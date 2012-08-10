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
package net.sf.jasperreports.charts.util;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImageArea;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.fill.DefaultChartTheme;
import net.sf.jasperreports.engine.util.JRSingletonCache;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class ChartUtil
{
	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	private static final JRSingletonCache<ChartRendererFactory> CHART_RENDERER_FACTORY_CACHE = 
			new JRSingletonCache<ChartRendererFactory>(ChartRendererFactory.class);

	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private ChartUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	private static ChartUtil getDefaultInstance()//FIXMECONTEXT check this use of this
	{
		return new ChartUtil(DefaultJasperReportsContext.getInstance());
	}
	
	
	/**
	 *
	 */
	public static ChartUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new ChartUtil(jasperReportsContext);
	}
	
	
	/**
	 * 
	 */
	public static List<JRPrintImageAreaHyperlink> getImageAreaHyperlinks(
		JFreeChart chart,
		ChartHyperlinkProvider chartHyperlinkProvider,
		Graphics2D grx,
		Rectangle2D renderingArea
		)// throws JRException
	{
		List<JRPrintImageAreaHyperlink> areaHyperlinks = null;
		
		if (chartHyperlinkProvider != null && chartHyperlinkProvider.hasHyperlinks())
		{
			ChartRenderingInfo renderingInfo = new ChartRenderingInfo();

			if (grx == null)
			{
				chart.createBufferedImage((int) renderingArea.getWidth(), (int)  renderingArea.getHeight(), renderingInfo);
			}
			else
			{
				chart.draw(grx, renderingArea, renderingInfo);
			}
			
			EntityCollection entityCollection = renderingInfo.getEntityCollection();
			if (entityCollection != null && entityCollection.getEntityCount() > 0)
			{
				areaHyperlinks = new ArrayList<JRPrintImageAreaHyperlink>(entityCollection.getEntityCount());
				
				for (@SuppressWarnings("unchecked")
				Iterator<ChartEntity> it = entityCollection.iterator(); it.hasNext();)
				{
					ChartEntity entity = it.next();
					JRPrintHyperlink printHyperlink = chartHyperlinkProvider.getEntityHyperlink(entity);
					if (printHyperlink != null)
					{
						JRPrintImageArea area = getImageArea(entity);

						JRPrintImageAreaHyperlink areaHyperlink = new JRPrintImageAreaHyperlink();
						areaHyperlink.setArea(area);
						areaHyperlink.setHyperlink(printHyperlink);
						areaHyperlinks.add(areaHyperlink);
					}
				}
			}
		}
		
		return areaHyperlinks;
	}

	private static JRPrintImageArea getImageArea(ChartEntity entity)
	{
		JRPrintImageArea area = new JRPrintImageArea();
		area.setShape(JRPrintImageArea.getShape(entity.getShapeType()));
		
		int[] coordinates = getCoordinates(entity);
		if (coordinates != null)
		{
			area.setCoordinates(coordinates);
		}
		return area;
	}
	
	private static int[] getCoordinates(ChartEntity entity)
	{
		int[] coordinates = null;
		String shapeCoords = entity.getShapeCoords();
		if (shapeCoords != null && shapeCoords.length() > 0)
		{
			StringTokenizer tokens = new StringTokenizer(shapeCoords, ",");
			coordinates = new int[tokens.countTokens()];
			int idx = 0;
			while (tokens.hasMoreTokens())
			{
				String coord = tokens.nextToken();
				coordinates[idx] = Integer.parseInt(coord);
				++idx;
			}
		}
		return coordinates;
	}

	/**
	 * 
	 */
	public ChartTheme getTheme(String themeName)
	{
		if (themeName == null)
		{
			return new DefaultChartTheme();
		}

		List<ChartThemeBundle> themeBundles = jasperReportsContext.getExtensions(ChartThemeBundle.class);
		for (Iterator<ChartThemeBundle> it = themeBundles.iterator(); it.hasNext();)
		{
			ChartThemeBundle bundle = it.next();
			ChartTheme chartTheme = bundle.getChartTheme(themeName);
			if (chartTheme != null)
			{
				return chartTheme;
			}
		}
		throw new JRRuntimeException("Chart theme '" + themeName + "' not found.");
	}

	/**
	 * @deprecated Replaced by {@link #getTheme(String)}.
	 */
	public static ChartTheme getChartTheme(String themeName)
	{
		return getDefaultInstance().getTheme(themeName);
	}

	/**
	 * 
	 */
	public ChartRenderableFactory getChartRenderableFactory(String renderType)
	{
		String factoryClass = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(ChartRenderableFactory.PROPERTY_CHART_RENDERER_FACTORY_PREFIX + renderType);
		if (factoryClass == null)
		{
			throw new JRRuntimeException("No chart renderer factory specifyed for '" + renderType + "' render type.");
		}

		try
		{
			@SuppressWarnings("deprecation")
			ChartRendererFactory factory = CHART_RENDERER_FACTORY_CACHE.getCachedInstance(factoryClass);
			if (factory instanceof ChartRenderableFactory)
			{
				return (ChartRenderableFactory)factory;
			}
			
			return new WrappingChartRenderableFactory(factory); 
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 * @deprecated Replaced by {@link #getChartRenderableFactory(String)}.
	 */
	public static ChartRendererFactory getChartRendererFactory(String renderType)
	{
		return getDefaultInstance().getChartRenderableFactory(renderType);
	}

	/**
	 * @deprecated To be removed.
	 */
	public static class WrappingChartRenderableFactory implements ChartRenderableFactory
	{
		private ChartRendererFactory factory;
		
		public WrappingChartRenderableFactory(ChartRendererFactory factory)
		{
			this.factory = factory;
		}

		public net.sf.jasperreports.engine.JRRenderable getRenderer(
			JFreeChart chart,
			ChartHyperlinkProvider chartHyperlinkProvider,
			Rectangle2D rectangle
			) 
		{
			return factory.getRenderer(chart, chartHyperlinkProvider, rectangle);
		}

		public Renderable getRenderable(
			JasperReportsContext jasperReportsContext,
			JFreeChart chart,
			ChartHyperlinkProvider chartHyperlinkProvider,
			Rectangle2D rectangle) 
		{
			net.sf.jasperreports.engine.JRRenderable deprecatedRenderer 
				= getRenderer(chart, chartHyperlinkProvider, rectangle);
			return RenderableUtil.getWrappingRenderable(deprecatedRenderer);
		}
	}
}
