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
package net.sf.jasperreports.chartcustomizers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRPropertiesUtil;

/**
 * This customizer plots a line chart as spline.
 * 
 * @author gtoffoli
 */
public class CategoryMarkerCustomizer implements JRChartCustomizer, ConfigurableChartCustomizer {

	public static String STROKE_SOLID = "solid";
	public static String STROKE_DOTTED = "dotted";
	public static String STROKE_DASHED = "dashed";
	
	public static final String FONT_NAME_KEY = "fontName";
	public static final String FONT_SIZE_KEY = "fontSize";
	public static final String FONT_BOLD_KEY = "isBold";
	public static final String FONT_ITALIC_KEY = "isItalic";
	
	public static final String OFFSET_TOP_KEY = "label-position-offset-top";
	public static final String OFFSET_LEFT_KEY = "label-position-offset-left";
	public static final String OFFSET_BOTTOM_KEY = "label-position-offset-bottom";
	public static final String OFFSET_RIGHT_KEY = "label-position-offset-right";

	public static String TYPE_LINE = "line";

	protected Map<String, String> configuration = null;

	@Override
	public void customize(JFreeChart jfc, JRChart jrc) {

		if (!(jfc.getPlot() instanceof CategoryPlot))
			return;

		CategoryPlot plot = (CategoryPlot) jfc.getPlot();
		CategoryMarker m = this.createMarker(jrc);
		
		if (m != null) {
			plot.addDomainMarker(m, ChartCustomizerUtils.getLayer(configuration));
		}
	}

	@Override
	public void setConfiguration(Map<String, String> properties) {
		this.configuration = properties;
	}

	protected CategoryMarker createMarker(JRChart jrc) {
		Comparable<?> value = (Comparable<?>) configuration.get("key");

		if (value == null)
			return null;

		CategoryMarker marker = new CategoryMarker(value);

		if (configuration.containsKey("type")) {
			String type = (String) configuration.get("type");
			if (TYPE_LINE.equals(type)) {
				marker.setDrawAsLine(true);
			}
		}

		if (configuration.containsKey("label")) {
			String text = (String) configuration.get("label");
			if (text != null && text.length() > 0) {
				marker.setLabel(text);
			}
		}

		// All cases..
		if (configuration.containsKey("label-position")) {
			RectangleAnchor position = ChartCustomizerUtils.getLabelAnchorByName(configuration.get("label-position"));

			if (position != null) {
				marker.setLabelAnchor(position);
			}
		}
		
		if (configuration.containsKey("label-text-position")) {
			TextAnchor position = ChartCustomizerUtils.geTextAnchorByName(configuration.get("label-text-position"));

			if (position != null) {
				marker.setLabelTextAnchor(position);
			}
		}


		if (configuration.containsKey(OFFSET_TOP_KEY) || 
				configuration.containsKey(OFFSET_BOTTOM_KEY) || 
					configuration.containsKey(OFFSET_LEFT_KEY) || 
						configuration.containsKey(OFFSET_RIGHT_KEY)) {
			
	
			RectangleInsets currentOffset = marker.getLabelOffset();
			
			Double offsetTop = ChartCustomizerUtils.asDouble(OFFSET_TOP_KEY);
			if (offsetTop != null){
				offsetTop = currentOffset.getTop();
			}
			
			Double offsetBottom = ChartCustomizerUtils.asDouble(OFFSET_BOTTOM_KEY);
			if (offsetBottom != null){
				offsetBottom = currentOffset.getBottom();
			}
			
			Double offsetLeft = ChartCustomizerUtils.asDouble(OFFSET_LEFT_KEY);
			if (offsetLeft != null){
				offsetLeft = currentOffset.getLeft();
			}
			
			Double offsetRight = ChartCustomizerUtils.asDouble(OFFSET_RIGHT_KEY);
			if (offsetRight != null){
				offsetRight = currentOffset.getRight();
			}

			marker.setLabelOffset(new RectangleInsets(offsetTop, offsetLeft, offsetBottom, offsetRight));
			
		}

		if (configuration.containsKey("color")) {
			Color color = ChartCustomizerUtils.asColor(configuration.get("color"));
			if (color != null) {
				marker.setPaint((Color) color);
			}
		}

		if (configuration.containsKey("alpha")) {
			Float alpha = ChartCustomizerUtils.asFloat(configuration.get("alpha"));
			if (alpha != null) {
				marker.setAlpha(alpha);
			}
		}

		BasicStroke basicStroke = new BasicStroke(1f);

		if (configuration.containsKey("stroke-width")) {
			float strokeWidth = JRPropertiesUtil.asFloat(configuration.get("stroke-width"));

			basicStroke = new BasicStroke(strokeWidth, basicStroke.getEndCap(), basicStroke.getLineJoin(),
					basicStroke.getMiterLimit(), basicStroke.getDashArray(), basicStroke.getDashPhase());
		}

		if (configuration.containsKey("stroke-style")) {
			String strokeStyle = configuration.get("stroke-style");

			if (STROKE_SOLID.equals(strokeStyle)) {
				basicStroke = new BasicStroke(basicStroke.getLineWidth(), basicStroke.getEndCap(),
						basicStroke.getLineJoin(), basicStroke.getMiterLimit(), new float[] { 25.0f, 25.0f },
						basicStroke.getDashPhase());
			} else if (STROKE_DOTTED.equals(strokeStyle)) {
				basicStroke = new BasicStroke(basicStroke.getLineWidth(), basicStroke.getEndCap(),
						basicStroke.getLineJoin(), basicStroke.getMiterLimit(), new float[] { 1.0f, 1.0f },
						basicStroke.getDashPhase());
			} else if (STROKE_DASHED.equals(strokeStyle)) {
				basicStroke = new BasicStroke(basicStroke.getLineWidth(), basicStroke.getEndCap(),
						basicStroke.getLineJoin(), basicStroke.getMiterLimit(), new float[] { 10.0f, 10.0f },
						basicStroke.getDashPhase());
			}
		}
		marker.setStroke(basicStroke);

		//Setup the font
		Font font = marker.getLabelFont();
		int fontStyle = Font.PLAIN;
		
		String isBold = configuration.get(FONT_BOLD_KEY);
		if (isBold != null && ChartCustomizerUtils.asBoolean(isBold)){
			fontStyle = fontStyle | Font.BOLD;
		}
		String isItalic = configuration.get(FONT_ITALIC_KEY);
		if (isItalic != null && ChartCustomizerUtils.asBoolean(isItalic)){
			fontStyle = fontStyle | Font.ITALIC;
		}
		
		String fontName = configuration.get(FONT_NAME_KEY);
		if(fontName == null){
			fontName = font.getFontName();
		}
		Float fontSize = ChartCustomizerUtils.asFloat(configuration.get(FONT_SIZE_KEY));
		if(fontSize == null){
			fontSize = Float.valueOf(font.getSize());
		}
		marker.setLabelFont(ChartCustomizerUtils.getFont(fontName, fontSize, fontStyle, jrc));

		return marker;
	}

}
