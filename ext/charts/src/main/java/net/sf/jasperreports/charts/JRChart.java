/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.charts;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.charts.design.JRDesignChart;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREvaluation;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.properties.PropertyConstants;


/**
 * Implementations of this interface can be used for rendering chart components. Data obtained from the report
 * datasource can be also displayed in a chart, embedded in the report. There are a lot of chart types, each with
 * its own dataset and characteristics. This interface only defines the common properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
@JsonTypeName("chart")
@JsonDeserialize(as = JRDesignChart.class)
public interface JRChart extends JRElement, JREvaluation, JRAnchor, JRHyperlink, JRBoxContainer
{

	/**
	 * Specifies the type of chart rendering. Possible values are <code>draw</code>, <code>image</code> and <code>svg</code>.
	 * If the rendering type is <code>draw</code> a vector image is constructed, using the <code>JFreeChart</code> dedicated APIs.
	 * For type image, a PNG encoded image will be generated, while for type svg, an SVG snippet will be generated.
	 * <p>
	 * Defaults to <code>draw</code>.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_FILL,
			defaultValue = "draw",
			scopes = {PropertyScope.CONTEXT, PropertyScope.REPORT},
			sinceVersion = PropertyConstants.VERSION_2_0_5
			)
	public static final String PROPERTY_CHART_RENDER_TYPE = JRPropertiesUtil.PROPERTY_PREFIX + "chart.render.type";//FIXMECHART javadoc comment

	/**
	 * Property used to specify the chart theme name.
	 */
	@Property(
			category = PropertyConstants.CATEGORY_FILL,
			defaultValue = "default",
			scopes = {PropertyScope.CONTEXT, PropertyScope.REPORT},
			sinceVersion = PropertyConstants.VERSION_3_1_0
			)
	public static final String PROPERTY_CHART_THEME = JRPropertiesUtil.PROPERTY_PREFIX + "chart.theme";


	/**
	 *
	 */
	public static final byte CHART_TYPE_AREA = 1;
	public static final byte CHART_TYPE_BAR3D = 2;
	public static final byte CHART_TYPE_BAR = 3;
	public static final byte CHART_TYPE_BUBBLE = 4;
	public static final byte CHART_TYPE_CANDLESTICK = 5;
	public static final byte CHART_TYPE_HIGHLOW = 6;
	public static final byte CHART_TYPE_LINE = 7;
	public static final byte CHART_TYPE_PIE3D = 8;
	public static final byte CHART_TYPE_PIE = 9;
	public static final byte CHART_TYPE_SCATTER = 10;
	public static final byte CHART_TYPE_STACKEDBAR3D = 11;
	public static final byte CHART_TYPE_STACKEDBAR = 12;
	public static final byte CHART_TYPE_XYAREA = 13;
	public static final byte CHART_TYPE_XYBAR = 14;
	public static final byte CHART_TYPE_XYLINE = 15;
	public static final byte CHART_TYPE_TIMESERIES = 16;
	public static final byte CHART_TYPE_METER = 17;
	public static final byte CHART_TYPE_THERMOMETER = 18;
	public static final byte CHART_TYPE_MULTI_AXIS = 19;
	public static final byte CHART_TYPE_STACKEDAREA = 20;
	public static final byte CHART_TYPE_GANTT = 21;

	/**
	 * rendering type
	 */
	public static final String RENDER_TYPE_DRAW = "draw";
	public static final String RENDER_TYPE_IMAGE = "image";
	public static final String RENDER_TYPE_SVG = "svg";

	/**
	 * 
	 */
	@JsonInclude(Include.NON_NULL)
	@JacksonXmlProperty(isAttribute = true)
	public Boolean getShowLegend();

	/**
	 *
	 */
	public void setShowLegend(Boolean isShowLegend);

	/**
	 * Gets the expression whose evaluation will form the title.
	 */
	public JRExpression getTitleExpression();


	/**
	 *
	 */
	public JRFont getTitleFont();


	/**
	 * Gets the title position relative to the chart.
	 */
	@JacksonXmlProperty(isAttribute = true)
	public EdgeEnum getTitlePosition();


	/**
	 * Sets the title position relative to the chart.
	 */
	public void setTitlePosition(EdgeEnum titlePosition);
	

	/**
	 *
	 */
	@JsonIgnore
	public Color getTitleColor();


	/**
	 *
	 */
	@JsonGetter("titleColor")
	@JacksonXmlProperty(localName = "titleColor", isAttribute = true)
	public Color getOwnTitleColor();


	/**
	 *
	 */
	@JsonSetter
	public void setTitleColor(Color titleColor);

	
	/**
	 * Gets the expression whose evaluation will form the subtitle.
	 */
	public JRExpression getSubtitleExpression();


	/**
	 *
	 */
	public JRFont getSubtitleFont();


	/**
	 *
	 */
	@JsonIgnore
	public Color getSubtitleColor();

	/**
	 *
	 */
	@JsonGetter("subtitleColor")
	@JacksonXmlProperty(localName = "subtitleColor", isAttribute = true)
	public Color getOwnSubtitleColor();

	/**
	 *
	 */
	@JsonSetter
	public void setSubtitleColor(Color subtitleColor);

	
	/**
	 * 
	 */
	@JsonIgnore
	public Color getLegendColor();
	
	/**
	 * 
	 */
	@JsonGetter("legendColor")
	@JacksonXmlProperty(localName = "legendColor", isAttribute = true)
	public Color getOwnLegendColor();
	
	/**
	 * 
	 */
	@JsonSetter
	public void setLegendColor(Color legendColor);
	
	/**
	 * 
	 */
	@JsonGetter("legendBackgroundColor")
	@JacksonXmlProperty(localName = "legendBackgroundColor", isAttribute = true)
	public Color getOwnLegendBackgroundColor();
	
	/**
	 * 
	 */
	@JsonIgnore
	public Color getLegendBackgroundColor();
	
	/**
	 * 
	 */
	@JsonSetter
	public void setLegendBackgroundColor(Color legendBackgroundColor);
	
	/**
	 * 
	 */
	public JRFont getLegendFont();
	

	/**
	 *
	 */
	@JacksonXmlProperty(isAttribute = true)
	public EdgeEnum getLegendPosition();


	/**
	 *
	 */
	public void setLegendPosition(EdgeEnum legendPosition);
	

	/**
	 * Gets the chart dataset. Most chart types have different dataset structures, depending on the chart type.
	 */
	public JRChartDataset getDataset();


	/**
	 * Gets the chart plot. Plots are used to define various chart visual properties, such as colors and transparency.
	 */
	@JsonGetter
	public JRChartPlot getPlot();


	/**
	 * Gets the chart type. It must be one of the chart type constants in this class.
	 */ 
	@JacksonXmlProperty(isAttribute = true)
	public byte getChartType(); //FIXMEJACK maybe introduce enum
	
	/**
	 * Gets a user specified chart customizer class name.
	 * @see JRChartCustomizer
 	 */
	@JacksonXmlProperty(isAttribute = true)
	public String getCustomizerClass();


	/**
	 *
	 */
	@JacksonXmlProperty(isAttribute = true)
	public String getRenderType();


	/**
	 *
	 */
	public void setRenderType(String renderType);
	
	/**
	 *
	 */
	@JacksonXmlProperty(isAttribute = true)
	public String getTheme();


	/**
	 *
	 */
	public void setTheme(String theme);
	
}
