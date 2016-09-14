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
import java.util.Map;

import net.sf.jasperreports.chartcustomizers.utils.ChartCustomizerUtils;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;

/**
 * This customizer plots a line chart as spline.
 * 
 * @author Giulio Toffoli (gt78@users.sourceforge.net)
 */
public abstract class AbstractValueMarkerCustomizer implements JRChartCustomizer, ConfigurableChartCustomizer {

	public static String STROKE_SOLID = "solid";
	public static String STROKE_DOTTED = "dotted";
	public static String STROKE_DASHED = "dashed";

	protected Map<String, String> configuration = null;

	@Override
	public abstract void customize(JFreeChart jfc, JRChart jrc);

	@Override
	public void setConfiguration(Map<String, String> properties) {
		this.configuration = properties;
	}

	protected ValueMarker createMarker() {
		Double value = ChartCustomizerUtils.asDouble(configuration.get("value"));

		if (value == null)
			return null;

		ValueMarker marker = new ValueMarker(value);

		if (configuration.containsKey("label")) {
			String text = configuration.get("label");
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

		if (configuration.containsKey("label-position-offset")) {
			String offsets = configuration.get("label-position-offset");

			String[] vals = offsets.split(",");
			System.out.println("Offsets: " + Float.parseFloat(vals[0]) + " " + Float.parseFloat(vals[1]) + " "
					+ Float.parseFloat(vals[2]) + " " + Float.parseFloat(vals[3]));
			marker.setLabelOffset(new RectangleInsets(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]),
					Float.parseFloat(vals[2]), Float.parseFloat(vals[3])));
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
			String strokeStyle = (String) configuration.get("stroke-style");

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

		return marker;
	}

}
