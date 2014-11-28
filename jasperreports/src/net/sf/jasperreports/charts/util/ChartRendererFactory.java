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

import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRenderable;

import org.jfree.chart.JFreeChart;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link ChartRenderableFactory}.
 */
public interface ChartRendererFactory
{
	/**
	 * @deprecated Replaced by {@link ChartRenderableFactory#PROPERTY_CHART_RENDERER_FACTORY_PREFIX}.
	 */
	public static final String PROPERTY_CHART_RENDERER_FACTORY_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "chart.renderer.factory.";

	/**
	 * @deprecated Replaced by {@link ChartRenderableFactory#getRenderer(JFreeChart, ChartHyperlinkProvider, Rectangle2D)}. 
	 */
	public JRRenderable getRenderer(
		JFreeChart chart, 
		ChartHyperlinkProvider chartHyperlinkProvider,
		Rectangle2D rectangle
		);
}
