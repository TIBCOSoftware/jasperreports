/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.chartcustomizers.utils;

import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.jfree.chart.renderer.category.LevelRenderer;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import net.sf.jasperreports.chartcustomizers.CategoryMarkerCustomizer;
import net.sf.jasperreports.chartcustomizers.DomainAxisMinMaxCustomizer;
import net.sf.jasperreports.chartcustomizers.DomainAxisTickCustomizer;
import net.sf.jasperreports.chartcustomizers.DomainIntervalMarkerCustomizer;
import net.sf.jasperreports.chartcustomizers.DomainValueMarkerCustomizer;
import net.sf.jasperreports.chartcustomizers.LegendShapeCustomizer;
import net.sf.jasperreports.chartcustomizers.LevelRenderCustomizer;
import net.sf.jasperreports.chartcustomizers.LineDotShapeCustomizer;
import net.sf.jasperreports.chartcustomizers.ProxyChartCustomizer;
import net.sf.jasperreports.chartcustomizers.RangeAxisMinMaxCustomizer;
import net.sf.jasperreports.chartcustomizers.RangeAxisTickCustomizer;
import net.sf.jasperreports.chartcustomizers.RangeIntervalMarkerCustomizer;
import net.sf.jasperreports.chartcustomizers.RangeValueMarkerCustomizer;
import net.sf.jasperreports.chartcustomizers.SplineCustomizer;
import net.sf.jasperreports.chartcustomizers.StepCustomizer;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JRFillChart;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.util.JRColorUtil;

/**
 *
 * @author Giulio Toffoli (gt78@users.sourceforge.net)
 */
public class ChartCustomizerUtils {

	public static Double asDouble(Object value) {
		if (value == null)
			return null;
		if (value instanceof String) {
			return Double.parseDouble(((String) value).trim());
		} else if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}

		return null;
	}

	public static Float asFloat(Object value) {
		if (value == null)
			return null;
		if (value instanceof String) {
			return Float.parseFloat(((String) value).trim());
		} else if (value instanceof Number) {
			return ((Number) value).floatValue();
		}

		return null;
	}

	public static Boolean asBoolean(Object value) {
		if (value == null)
			return null;
		if (value instanceof String) {
			return Boolean.parseBoolean((String) value);
		}

		return null;
	}
	
	public static Integer asInteger(Object value) {
		if (value == null)
			return null;
		if (value instanceof String) {
			return Integer.parseInt((String) value);
		}

		return null;
	}

	public static Color asColor(Object value) {
		if (value == null)
			return null;
		if (value instanceof String) {
			return JRColorUtil.getColor((String) value, null);
		} else if (value instanceof Color) {
			return (Color) value;
		}

		return null;
	}

	/**
	 * Translate a string to a RectangleAnchor
	 * 
	 * @param str
	 * @return
	 */
	public static RectangleAnchor getLabelAnchorByName(String str) {
		if (str.equals("top"))
			return RectangleAnchor.TOP;
		if (str.equals("top-left"))
			return RectangleAnchor.TOP_LEFT;
		if (str.equals("top-right"))
			return RectangleAnchor.TOP_RIGHT;
		if (str.equals("bottom"))
			return RectangleAnchor.BOTTOM;
		if (str.equals("bottom-left"))
			return RectangleAnchor.BOTTOM_LEFT;
		if (str.equals("bottom-rigth"))
			return RectangleAnchor.BOTTOM_RIGHT;
		if (str.equals("center"))
			return RectangleAnchor.CENTER;
		if (str.equals("left"))
			return RectangleAnchor.LEFT;
		if (str.equals("right"))
			return RectangleAnchor.RIGHT;

		// default..
		return null;
	}

	/**
	 * Translate a string to a TextAnchor
	 * 
	 * @param str
	 * @return
	 */
	public static TextAnchor geTextAnchorByName(String str) {
		if (str.equals("baseline_center"))
			return TextAnchor.BASELINE_CENTER;
		if (str.equals("baseline_left"))
			return TextAnchor.BASELINE_LEFT;
		if (str.equals("baseline_right"))
			return TextAnchor.BASELINE_RIGHT;
		if (str.equals("bottom_center"))
			return TextAnchor.BOTTOM_CENTER;
		if (str.equals("bottom_left"))
			return TextAnchor.BOTTOM_LEFT;
		if (str.equals("bottom_right"))
			return TextAnchor.BOTTOM_RIGHT;
		if (str.equals("center"))
			return TextAnchor.CENTER;
		if (str.equals("center_left"))
			return TextAnchor.CENTER_LEFT;
		if (str.equals("center_right"))
			return TextAnchor.CENTER_RIGHT;
		if (str.equals("half_ascent_center"))
			return TextAnchor.HALF_ASCENT_CENTER;
		if (str.equals("half_ascent_left"))
			return TextAnchor.HALF_ASCENT_LEFT;
		if (str.equals("half_ascent_right"))
			return TextAnchor.HALF_ASCENT_RIGHT;
		if (str.equals("top_center"))
			return TextAnchor.TOP_CENTER;
		if (str.equals("top_left"))
			return TextAnchor.TOP_LEFT;
		if (str.equals("top_right"))
			return TextAnchor.TOP_RIGHT;
		
		// default..
		return null;
	}
	
	public static Layer getLayer(Map<String, String> configuration) {
		if (configuration.containsKey("layer")) {
			String layer = configuration.get("layer");

			if ("background".equals(layer))
				return Layer.BACKGROUND;
			if ("foreground".equals(layer))
				return Layer.FOREGROUND;

		}

		return Layer.BACKGROUND;
	}
	
	public static LevelRenderer getCategoryLevelRender(Map<String, String> configuration){
		LevelRenderer result = new LevelRenderer();
		if (configuration.containsKey(LevelRenderCustomizer.ITEM_MARIGN)){
			String value = configuration.get(LevelRenderCustomizer.ITEM_MARIGN);
			result.setItemMargin(asDouble(value));
		}
		if (configuration.containsKey(LevelRenderCustomizer.MAX_ITEM_WIDTH)){
			String value = configuration.get(LevelRenderCustomizer.MAX_ITEM_WIDTH);
			result.setMaximumItemWidth(asDouble(value));
		}
		return result;
	}
	
	public static Font getFont(String fontName, float fontSize, int attributes, JRChart jrc){
		JRFillChart fillChart = (JRFillChart)jrc;
		JasperReportsContext context = fillChart.getFiller().getJasperReportsContext();
		Locale locale = fillChart.getFiller().getMainDataset().getLocale();
		return FontUtil.getInstance(context).getAwtFontFromBundles(fontName, attributes, fontSize, locale, true);
	}
	
	/**
	 * Return a list of the customizers provided by this jar
	 */
	public static HashSet<Class<? extends JRChartCustomizer>> getEmbeddedCustomizers(){
		HashSet<Class<? extends JRChartCustomizer>> result = new HashSet<Class<? extends JRChartCustomizer>>();
		result.add(ProxyChartCustomizer.class);
		result.add(CategoryMarkerCustomizer.class);
		result.add(DomainAxisMinMaxCustomizer.class);
		result.add(DomainAxisTickCustomizer.class);
		result.add(DomainIntervalMarkerCustomizer.class);
		result.add(LegendShapeCustomizer.class);
		result.add(LevelRenderCustomizer.class);
		result.add(DomainValueMarkerCustomizer.class);
		result.add(LineDotShapeCustomizer.class);
		result.add(RangeAxisMinMaxCustomizer.class);
		result.add(RangeAxisTickCustomizer.class);
		result.add(RangeIntervalMarkerCustomizer.class);
		result.add(RangeValueMarkerCustomizer.class);
		result.add(SplineCustomizer.class);
		result.add(StepCustomizer.class);
		return result;
	}
}
