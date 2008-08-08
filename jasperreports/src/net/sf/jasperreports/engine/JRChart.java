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
package net.sf.jasperreports.engine;

import java.awt.Color;

import net.sf.jasperreports.engine.util.JRProperties;


/**
 * Implementations of this interface can be used for rendering chart components. Data obtained from the report
 * datasource can be also displayed in a chart, embedded in the report. There are a lot of chart types, each with
 * its own dataset and characteristics. This interface only defines the common properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface JRChart extends JRElement, JRAnchor, JRHyperlink, JRBox, JRBoxContainer
{

	/**
	 * Specifies the type of chart rendering. Possible values are <code>draw</code>, <code>image</code> and <code>svg</code>.
	 * If the rendering type is <code>draw</code> a vector image is constructed, using the <code>JFreeChart</code> dedicated APIs.
	 * For type image, a PNG encoded image will be generated, while for type svg, an SVG snippet will be generated.
	 * <p>
	 * Defaults to <code>draw</code>.
	 */
	public static final String PROPERTY_CHART_RENDER_TYPE = JRProperties.PROPERTY_PREFIX + "chart.render.type";//FIXMECHART javadoc comment

	/**
	 *
	 */
	public static final byte EDGE_TOP = 1;
	public static final byte EDGE_BOTTOM = 2;
	public static final byte EDGE_LEFT = 3;
	public static final byte EDGE_RIGHT = 4;

	/**
	 * @deprecated Replaced by {@link #EDGE_TOP}.
	 */
	public static final byte TITLE_POSITION_TOP = EDGE_TOP;
	/**
	 * @deprecated Replaced by {@link #EDGE_BOTTOM}.
	 */
	public static final byte TITLE_POSITION_BOTTOM = EDGE_BOTTOM;
	/**
	 * @deprecated Replaced by {@link #EDGE_LEFT}.
	 */
	public static final byte TITLE_POSITION_LEFT = EDGE_LEFT;
	/**
	 * @deprecated Replaced by {@link #EDGE_RIGHT}.
	 */
	public static final byte TITLE_POSITION_RIGHT = EDGE_RIGHT;


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
	public boolean isShowLegend();

	/**
	 *
	 */
	public void setShowLegend(boolean isShowLegend);

	/**
	 * Returns the evaluation time for this chart.
	 */
	public byte getEvaluationTime();
		
	/**
	 * Gets the evaluation group for this chart (only applies when evaluation time is group).
	 */
	public JRGroup getEvaluationGroup();

	/**
	 * Gets the box around the element.
	 * @deprecated Replaced by {@link JRBoxContainer#getLineBox()}
	 */
	public JRBox getBox();

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
	public byte getTitlePosition();


	/**
	 * Sets the title position relative to the chart.
	 */
	public void setTitlePosition(byte titlePosition);
	

	/**
	 *
	 */
	public Color getTitleColor();


	/**
	 *
	 */
	public Color getOwnTitleColor();


	/**
	 *
	 */
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
	public Color getSubtitleColor();

	/**
	 *
	 */
	public Color getOwnSubtitleColor();

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor);

	
	/**
	 * 
	 */
	public Color getLegendColor();
	
	/**
	 * 
	 */
	public Color getOwnLegendColor();
	
	/**
	 * 
	 */
	public void setLegendColor(Color legendColor);
	
	/**
	 * 
	 */
	public Color getOwnLegendBackgroundColor();
	
	/**
	 * 
	 */
	public Color getLegendBackgroundColor();
	
	/**
	 * 
	 */
	public void setLegendBackgroundColor(Color legendBackgroundColor);
	
	/**
	 * 
	 */
	public JRFont getLegendFont();
	
	/**
	 *
	 */
	public byte getLegendPosition();


	/**
	 *
	 */
	public void setLegendPosition(byte legendPosition);
	

	/**
	 * Gets the chart dataset. Most chart types have different dataset structures, depending on the chart type.
	 */
	public JRChartDataset getDataset();


	/**
	 * Gets the chart plot. Plots are used to define various chart visual properties, such as colors and transparency.
	 */
	public JRChartPlot getPlot();


	/**
	 * Gets the chart type. It must be one of the chart type constants in this class.
	 */ 
	public byte getChartType();
	
	/**
	 * Gets a user specified chart customizer class name.
	 * @see JRChartCustomizer
 	 */
	public String getCustomizerClass();


	/**
	 *
	 */
	public String getRenderType();


	/**
	 *
	 */
	public void setRenderType(String renderType);
	
}
