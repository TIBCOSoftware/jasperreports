package net.sf.jasperreports.chartthemes.simple;
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
import java.awt.Color;

import net.sf.jasperreports.engine.JRChart;

import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.UnitType;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ChartThemesApp.java 2548 2009-01-27 12:44:51Z teodord $
 */
public class SimpleSettingsFactory
{


	/**
	 *
	 */
	public static final ChartThemeSettings createChartThemeSettings()
	{
		ChartThemeSettings settings = new ChartThemeSettings();

		ChartSettings chartSettings = settings.getChartSettings();
		chartSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		//chartSettings.setBackgroundPaint(new ColorProvider(Color.red));
		chartSettings.getFont().setBold(Boolean.TRUE);
		chartSettings.setBorderVisible(Boolean.TRUE);
		chartSettings.setAntiAlias(Boolean.TRUE);
		chartSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));

		TitleSettings titleSettings = settings.getTitleSettings();
		titleSettings.setShowTitle(Boolean.TRUE);
		titleSettings.setPosition(new Byte(JRChart.EDGE_TOP));
		titleSettings.setForegroundPaint(new ColorProvider(Color.black));
		titleSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		titleSettings.getFont().setBold(Boolean.TRUE);
		titleSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		titleSettings.setVerticalAlignment(VerticalAlignment.TOP);
		titleSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));
		
		TitleSettings subtitleSettings = settings.getSubtitleSettings();
		subtitleSettings.setShowTitle(Boolean.TRUE);
		subtitleSettings.setPosition(new Byte(JRChart.EDGE_TOP));
		subtitleSettings.setForegroundPaint(new ColorProvider(Color.black));
		subtitleSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		subtitleSettings.getFont().setBold(Boolean.TRUE);
		subtitleSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		subtitleSettings.setVerticalAlignment(VerticalAlignment.TOP);
		subtitleSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));

		LegendSettings legendSettings = settings.getLegendSettings();
		legendSettings.setShowLegend(Boolean.TRUE);
		legendSettings.setPosition(new Byte(JRChart.EDGE_BOTTOM));
		legendSettings.setForegroundPaint(new ColorProvider(Color.black));
		legendSettings.setBackgroundPaint(new GradientPaintProvider(10, 20, Color.green, 30, 40, Color.blue));
		legendSettings.getFont().setBold(Boolean.TRUE);
		legendSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		
		PlotSettings plotSettings = settings.getPlotSettings();
		plotSettings.setOutlineVisible(Boolean.TRUE);
		plotSettings.setOutlinePaint(new ColorProvider(Color.red));
		
		AxisSettings domainAxisSettings = settings.getDomainAxisSettings();
		domainAxisSettings.setAxisVisible(Boolean.TRUE);
		
		AxisSettings rangeAxisSettings = settings.getRangeAxisSettings();
		rangeAxisSettings.setAxisVisible(Boolean.TRUE);
		
		return settings;
	}
}
