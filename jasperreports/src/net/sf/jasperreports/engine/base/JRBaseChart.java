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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseChart extends JRBaseElement implements JRChart
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/*
	 * Box properties
	 */
	
	public static final String PROPERTY_BORDER = JRBaseStyle.PROPERTY_BORDER;
	
	public static final String PROPERTY_BORDER_COLOR = JRBaseStyle.PROPERTY_BORDER_COLOR;
	
	public static final String PROPERTY_BOTTOM_BORDER = JRBaseStyle.PROPERTY_BOTTOM_BORDER;
	
	public static final String PROPERTY_BOTTOM_BORDER_COLOR = JRBaseStyle.PROPERTY_BOTTOM_BORDER_COLOR;
	
	public static final String PROPERTY_BOTTOM_PADDING = JRBaseStyle.PROPERTY_BOTTOM_PADDING;
	
	public static final String PROPERTY_LEFT_BORDER = JRBaseStyle.PROPERTY_LEFT_BORDER;
	
	public static final String PROPERTY_LEFT_BORDER_COLOR = JRBaseStyle.PROPERTY_LEFT_BORDER_COLOR;
	
	public static final String PROPERTY_LEFT_PADDING = JRBaseStyle.PROPERTY_LEFT_PADDING;
	
	public static final String PROPERTY_PADDING = JRBaseStyle.PROPERTY_PADDING;
	
	public static final String PROPERTY_RIGHT_BORDER = JRBaseStyle.PROPERTY_RIGHT_BORDER;
	
	public static final String PROPERTY_RIGHT_BORDER_COLOR = JRBaseStyle.PROPERTY_RIGHT_BORDER_COLOR;
	
	public static final String PROPERTY_RIGHT_PADDING = JRBaseStyle.PROPERTY_RIGHT_PADDING;
	
	public static final String PROPERTY_TOP_BORDER = JRBaseStyle.PROPERTY_TOP_BORDER;
	
	public static final String PROPERTY_TOP_BORDER_COLOR = JRBaseStyle.PROPERTY_TOP_BORDER_COLOR;
	
	public static final String PROPERTY_TOP_PADDING = JRBaseStyle.PROPERTY_TOP_PADDING;

	/*
	 * Chart properties
	 */
	
	public static final String PROPERTY_LEGEND_BACKGROUND_COLOR = "legendBackgroundColor";
	
	public static final String PROPERTY_LEGEND_COLOR = "legendColor";
	
	public static final String PROPERTY_LEGEND_POSITION = "legendPosition";
	
	public static final String PROPERTY_SHOW_LEGEND = "showLegend";
	
	public static final String PROPERTY_SUBTITLE_COLOR = "subtitleColor";
	
	public static final String PROPERTY_TITLE_COLOR = "titleColor";
	
	public static final String PROPERTY_TITLE_POSITION = "titlePosition";
	
	/**
	 *
	 */
	protected byte chartType = 0;

	/**
	 *
	 */
	protected boolean isShowLegend = false;
	protected byte evaluationTime = JRExpression.EVALUATION_TIME_NOW;
	protected byte hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NULL;
	protected String linkType;
	protected byte hyperlinkTarget = JRHyperlink.HYPERLINK_TARGET_SELF;
	private JRHyperlinkParameter[] hyperlinkParameters;
	
	protected byte titlePosition = JRChart.EDGE_TOP;
	protected Color titleColor = null;
	protected Color subtitleColor = null;
	protected Color legendColor = null;
	protected Color legendBackgroundColor = null;
	protected byte legendPosition = JRChart.EDGE_BOTTOM;

	/**
	 *
	 */
	protected JRBox box = null;
	protected JRFont titleFont = null;
	protected JRFont subtitleFont = null;
	protected JRFont legendFont = null;

	protected String customizerClass;

	/**
	 *
	 */
	protected JRGroup evaluationGroup = null;
	protected JRExpression titleExpression = null;
	protected JRExpression subtitleExpression = null;
	protected JRExpression anchorNameExpression = null;
	protected JRExpression hyperlinkReferenceExpression = null;
	protected JRExpression hyperlinkAnchorExpression = null;
	protected JRExpression hyperlinkPageExpression = null;
	private JRExpression hyperlinkTooltipExpression;

	protected JRChartDataset dataset = null;
	protected JRChartPlot plot = null;


	/**
	 *
	 */
	protected Byte border;
	protected Byte topBorder = null;
	protected Byte leftBorder = null;
	protected Byte bottomBorder = null;
	protected Byte rightBorder = null;
	protected Color borderColor = null;
	protected Color topBorderColor = null;
	protected Color leftBorderColor = null;
	protected Color bottomBorderColor = null;
	protected Color rightBorderColor = null;
	protected Integer padding;
	protected Integer topPadding = null;
	protected Integer leftPadding = null;
	protected Integer bottomPadding = null;
	protected Integer rightPadding = null;


	/**
	 * The bookmark level for the anchor associated with this chart.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;
	
	/**
	 *
	 */
	protected JRBaseChart(JRChart chart, JRBaseObjectFactory factory)
	{
		super(chart, factory);

		chartType = chart.getChartType();

		switch(chartType) {
			case CHART_TYPE_AREA:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_BAR:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_BAR3D:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_BUBBLE:
				dataset = factory.getXyzDataset((JRXyzDataset) chart.getDataset());
				plot = factory.getBubblePlot((JRBubblePlot) chart.getPlot());
				break;
			case CHART_TYPE_CANDLESTICK:
				dataset = factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getCandlestickPlot((JRCandlestickPlot) chart.getPlot());
				break;
			case CHART_TYPE_HIGHLOW:
				dataset = factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getHighLowPlot((JRHighLowPlot) chart.getPlot());
				break;
			case CHART_TYPE_LINE:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			case CHART_TYPE_METER:
				dataset = factory.getValueDataset((JRValueDataset) chart.getDataset());
				plot = factory.getMeterPlot((JRMeterPlot) chart.getPlot());
				break;
			case CHART_TYPE_MULTI_AXIS:
				dataset = null;
				plot = factory.getMultiAxisPlot((JRMultiAxisPlot) chart.getPlot());
				break;
			case CHART_TYPE_PIE:
				dataset = factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPiePlot((JRPiePlot) chart.getPlot());
				break;
			case CHART_TYPE_PIE3D:
				dataset = factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPie3DPlot((JRPie3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_SCATTER:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getScatterPlot((JRScatterPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR3D:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_THERMOMETER:
				dataset = factory.getValueDataset((JRValueDataset) chart.getDataset());
				plot = factory.getThermometerPlot((JRThermometerPlot) chart.getPlot());
				break;
			case CHART_TYPE_TIMESERIES:
				dataset = factory.getTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
				plot = factory.getTimeSeriesPlot( (JRTimeSeriesPlot)chart.getPlot() );
				break;
			case CHART_TYPE_XYAREA:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYBAR:
				
				switch (chart.getDataset().getDatasetType()){
					case JRChartDataset.TIMESERIES_DATASET:
						dataset = factory.getTimeSeriesDataset((JRTimeSeriesDataset) chart.getDataset());
						break;
					case JRChartDataset.TIMEPERIOD_DATASET:
						dataset = factory.getTimePeriodDataset((JRTimePeriodDataset) chart.getDataset() );
						break;
					case JRChartDataset.XY_DATASET:
						dataset = factory.getXyDataset( (JRXyDataset)chart.getDataset() );
						break;
				}
				plot = factory.getBarPlot((JRBarPlot)chart.getPlot());
				break;
			case CHART_TYPE_XYLINE:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDAREA:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}

		isShowLegend = chart.isShowLegend();
		evaluationTime = chart.getEvaluationTime();
		linkType = chart.getLinkType();
		hyperlinkTarget = chart.getHyperlinkTarget();
		titlePosition = chart.getTitlePosition();
		titleColor = chart.getOwnTitleColor();
		subtitleColor = chart.getOwnSubtitleColor();
		legendColor = chart.getOwnLegendColor();
		legendBackgroundColor = chart.getOwnLegendBackgroundColor();
		legendPosition = chart.getLegendPosition();

		titleFont = new JRBaseFont(null, null, this, chart.getTitleFont());
		subtitleFont = new JRBaseFont(null, null, this, chart.getSubtitleFont());
		legendFont = new JRBaseFont(null, null, this, chart.getLegendFont());

		evaluationGroup = factory.getGroup(chart.getEvaluationGroup());
		titleExpression = factory.getExpression(chart.getTitleExpression());
		subtitleExpression = factory.getExpression(chart.getSubtitleExpression());
		anchorNameExpression = factory.getExpression(chart.getAnchorNameExpression());
		hyperlinkReferenceExpression = factory.getExpression(chart.getHyperlinkReferenceExpression());
		hyperlinkAnchorExpression = factory.getExpression(chart.getHyperlinkAnchorExpression());
		hyperlinkPageExpression = factory.getExpression(chart.getHyperlinkPageExpression());
		hyperlinkTooltipExpression = factory.getExpression(chart.getHyperlinkTooltipExpression());
		bookmarkLevel = chart.getBookmarkLevel();
		hyperlinkParameters = JRBaseHyperlink.copyHyperlinkParameters(chart, factory);

		customizerClass = chart.getCustomizerClass();

		border = chart.getOwnBorder();
		topBorder = chart.getOwnTopBorder();
		leftBorder = chart.getOwnLeftBorder();
		bottomBorder = chart.getOwnBottomBorder();
		rightBorder = chart.getOwnRightBorder();
		borderColor = chart.getOwnBorderColor();
		topBorderColor = chart.getOwnTopBorderColor();
		leftBorderColor = chart.getOwnLeftBorderColor();
		bottomBorderColor = chart.getOwnBottomBorderColor();
		rightBorderColor = chart.getOwnRightBorderColor();
		padding = chart.getOwnPadding();
		topPadding = chart.getOwnTopPadding();
		leftPadding = chart.getOwnLeftPadding();
		bottomPadding = chart.getOwnBottomPadding();
		rightPadding = chart.getOwnRightPadding();

	}
		

	/**
	 *
	 */
	public boolean isShowLegend()
	{
		return this.isShowLegend;
	}

	/**
	 *
	 */
	public void setShowLegend(boolean isShowLegend)
	{
		boolean old = this.isShowLegend;
		this.isShowLegend = isShowLegend;
		getEventSupport().firePropertyChange(PROPERTY_SHOW_LEGEND, old, this.isShowLegend);
	}

	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return evaluationTime;
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	/**
	 * @deprecated
	 */
	public JRBox getBox()
	{
		return this;
	}

	/**
	 *
	 */
	public JRFont getTitleFont()
	{
		return titleFont;
	}

	/**
	 *
	 */
	public byte getTitlePosition()
	{
		return titlePosition;
	}

	/**
	 *
	 */
	public void setTitlePosition(byte titlePosition)
	{
		byte old = this.titlePosition;
		this.titlePosition = titlePosition;
		getEventSupport().firePropertyChange(PROPERTY_TITLE_POSITION, old, this.titlePosition);
	}

	/**
	 *
	 */
	public Color getTitleColor()
	{
		return JRStyleResolver.getTitleColor(this);
	}

	/**
	 *
	 */
	public Color getOwnTitleColor()
	{
		return titleColor;
	}

	/**
	 *
	 */
	public void setTitleColor(Color titleColor)
	{
		Object old = this.titleColor;
		this.titleColor = titleColor;
		getEventSupport().firePropertyChange(PROPERTY_TITLE_COLOR, old, this.titleColor);
	}

	/**
	 *
	 */
	public JRFont getSubtitleFont()
	{
		return subtitleFont;
	}

	/**
	 *
	 */
	public Color getOwnSubtitleColor()
	{
		return subtitleColor;
	}

	/**
	 *
	 */
	public Color getSubtitleColor()
	{
		return JRStyleResolver.getSubtitleColor(this);
	}

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor)
	{
		Object old = this.subtitleColor;
		this.subtitleColor = subtitleColor;
		getEventSupport().firePropertyChange(PROPERTY_SUBTITLE_COLOR, old, this.subtitleColor);
	}

	public Color getLegendBackgroundColor() {
		return JRStyleResolver.getLegendBackgroundColor(this);
	}

	public Color getOwnLegendBackgroundColor() {
		return legendBackgroundColor;
	}


	public Color getOwnLegendColor() {
		return legendColor;
	}

	public Color getLegendColor() {
		return JRStyleResolver.getLegendColor(this);
	}

	public JRFont getLegendFont() {
		return legendFont;
	}


	public void setLegendBackgroundColor(Color legendBackgroundColor) {
		Object old = this.legendBackgroundColor;
		this.legendBackgroundColor = legendBackgroundColor;
		getEventSupport().firePropertyChange(PROPERTY_LEGEND_BACKGROUND_COLOR, old, this.legendBackgroundColor);
	}


	public void setLegendColor(Color legendColor) {
		Object old = this.legendColor;
		this.legendColor = legendColor;
		getEventSupport().firePropertyChange(PROPERTY_LEGEND_COLOR, old, this.legendColor);
	}
	
	/**
	 *
	 */
	public byte getLegendPosition()
	{
		return legendPosition;
	}

	/**
	 *
	 */
	public void setLegendPosition(byte legendPosition)
	{
		byte old = this.legendPosition;
		this.legendPosition = legendPosition;
		getEventSupport().firePropertyChange(PROPERTY_LEGEND_POSITION, old, this.legendPosition);
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return JRHyperlinkHelper.getHyperlinkType(this);
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return hyperlinkTarget;
	}
		
	/**
	 *
	 */
	public JRExpression getTitleExpression()
	{
		return titleExpression;
	}

	/**
	 *
	 */
	public JRExpression getSubtitleExpression()
	{
		return subtitleExpression;
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return anchorNameExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return hyperlinkReferenceExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return hyperlinkAnchorExpression;
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return hyperlinkPageExpression;
	}
	
	/**
	 *
	 */
	public JRChartDataset getDataset()
	{
		return dataset;
	}

	/**
	 *
	 */
	public JRChartPlot getPlot()
	{
		return plot;
	}

	public byte getChartType()
	{
		return chartType;
	}


	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public void visit(JRVisitor visitor)
	{
		visitor.visitChart(this);
	}


	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	/**
	 *
	 */
	public String getCustomizerClass()
	{
		return customizerClass;
	}

	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}

	/**
	 *
	 */
	public byte getBorder()
	{
		return JRStyleResolver.getBorder(this);
	}

	public Byte getOwnBorder()
	{
		return border;
	}

	/**
	 *
	 */
	public void setBorder(byte border)
	{
		setBorder(new Byte(border));
	}

	/**
	 *
	 */
	public void setBorder(Byte border)
	{
		Object old = this.border;
		this.border = border;
		getEventSupport().firePropertyChange(PROPERTY_BORDER, old, this.border);
	}

	/**
	 *
	 */
	public Color getBorderColor()
	{
		return JRStyleResolver.getBorderColor(this, getForecolor());
	}

	public Color getOwnBorderColor()
	{
		return borderColor;
	}

	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
		Object old = this.borderColor;
		this.borderColor = borderColor;
		getEventSupport().firePropertyChange(PROPERTY_BORDER_COLOR, old, this.borderColor);
	}

	/**
	 *
	 */
	public int getPadding()
	{
		return JRStyleResolver.getPadding(this);
	}

	public Integer getOwnPadding()
	{
		return padding;
	}

	/**
	 *
	 */
	public void setPadding(int padding)
	{
		setPadding(new Integer(padding));
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
		Object old = this.padding;
		this.padding = padding;
		getEventSupport().firePropertyChange(PROPERTY_PADDING, old, this.padding);
	}

	/**
	 *
	 */
	public byte getTopBorder()
	{
		return JRStyleResolver.getTopBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnTopBorder()
	{
		return topBorder;
	}

	/**
	 *
	 */
	public void setTopBorder(byte topBorder)
	{
		setTopBorder(new Byte(topBorder));
	}

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder)
	{
		Object old = this.topBorder;
		this.topBorder = topBorder;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER, old, this.topBorder);
	}

	/**
	 *
	 */
	public Color getTopBorderColor()
	{
		return JRStyleResolver.getTopBorderColor(this, getForecolor());
	}

	/**
	 *
	 */
	public Color getOwnTopBorderColor()
	{
		return topBorderColor;
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
		Object old = this.topBorderColor;
		this.topBorderColor = topBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_TOP_BORDER_COLOR, old, this.topBorderColor);
	}

	/**
	 *
	 */
	public int getTopPadding()
	{
		return JRStyleResolver.getTopPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return topPadding;
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
		setTopPadding(new Integer(topPadding));
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
		Object old = this.topPadding;
		this.topPadding = topPadding;
		getEventSupport().firePropertyChange(PROPERTY_TOP_PADDING, old, this.topPadding);
	}

	/**
	 *
	 */
	public byte getLeftBorder()
	{
		return JRStyleResolver.getLeftBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnLeftBorder()
	{
		return leftBorder;
	}

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder)
	{
		setLeftBorder(new Byte(leftBorder));
	}

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder)
	{
		Object old = this.leftBorder;
		this.leftBorder = leftBorder;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER, old, this.leftBorder);
	}

	/**
	 *
	 */
	public Color getLeftBorderColor()
	{
		return JRStyleResolver.getLeftBorderColor(this, getForecolor());
	}

	/**
	 *
	 */
	public Color getOwnLeftBorderColor()
	{
		return leftBorderColor;
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
		Object old = this.leftBorderColor;
		this.leftBorderColor = leftBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_BORDER_COLOR, old, this.leftBorderColor);
	}

	/**
	 *
	 */
	public int getLeftPadding()
	{
		return JRStyleResolver.getLeftPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return leftPadding;
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
		setLeftPadding(new Integer(leftPadding));
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		Object old = this.leftPadding;
		this.leftPadding = leftPadding;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_PADDING, old, this.leftPadding);
	}

	/**
	 *
	 */
	public byte getBottomBorder()
	{
		return JRStyleResolver.getBottomBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnBottomBorder()
	{
		return bottomBorder;
	}

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder)
	{
		setBottomBorder(new Byte(bottomBorder));
	}

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
		Object old = this.bottomBorder;
		this.bottomBorder = bottomBorder;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_BORDER, old, this.bottomBorder);
	}

	/**
	 *
	 */
	public Color getBottomBorderColor()
	{
		return JRStyleResolver.getBottomBorderColor(this, getForecolor());
	}

	/**
	 *
	 */
	public Color getOwnBottomBorderColor()
	{
		return bottomBorderColor;
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
		Object old = this.bottomBorderColor;
		this.bottomBorderColor = bottomBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_BORDER_COLOR, old, this.bottomBorderColor);
	}

	/**
	 *
	 */
	public int getBottomPadding()
	{
		return JRStyleResolver.getBottomPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return bottomPadding;
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
		setBottomPadding(new Integer(bottomPadding));
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		Object old = this.bottomPadding;
		this.bottomPadding = bottomPadding;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_PADDING, old, this.bottomPadding);
	}

	/**
	 *
	 */
	public byte getRightBorder()
	{
		return JRStyleResolver.getRightBorder(this);
	}

	/**
	 *
	 */
	public Byte getOwnRightBorder()
	{
		return rightBorder;
	}

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder)
	{
		setRightBorder(new Byte(rightBorder));
	}

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder)
	{
		Object old = this.rightBorder;
		this.rightBorder = rightBorder;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER, old, this.rightBorder);
	}

	/**
	 *
	 */
	public Color getRightBorderColor()
	{
		return JRStyleResolver.getRightBorderColor(this, getForecolor());
	}

	/**
	 *
	 */
	public Color getOwnRightBorderColor()
	{
		return rightBorderColor;
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
		Object old = this.rightBorderColor;
		this.rightBorderColor = rightBorderColor;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_BORDER_COLOR, old, this.rightBorderColor);
	}

	/**
	 *
	 */
	public int getRightPadding()
	{
		return JRStyleResolver.getRightPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return rightPadding;
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
		setRightPadding(new Integer(rightPadding));
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
		Object old = this.rightPadding;
		this.rightPadding = rightPadding;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_PADDING, old, this.rightPadding);
	}


	public String getLinkType()
	{
		return linkType;
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return hyperlinkParameters;
	}
	
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();		
		normalizeLinkType();
	}


	protected void normalizeLinkType()
	{
		if (linkType == null)
		{
			 linkType = JRHyperlinkHelper.getLinkType(hyperlinkType);
		}
		hyperlinkType = JRHyperlink.HYPERLINK_TYPE_NULL;
	}

	
	public JRExpression getHyperlinkTooltipExpression()
	{
		return hyperlinkTooltipExpression;
	}


	/**
	 * 
	 */
	public Object clone() throws CloneNotSupportedException 
	{
		JRBaseChart clone = (JRBaseChart)super.clone();
		
		if (hyperlinkParameters != null)
		{
			clone.hyperlinkParameters = new JRHyperlinkParameter[hyperlinkParameters.length];
			for(int i = 0; i < hyperlinkParameters.length; i++)
			{
				clone.hyperlinkParameters[i] = (JRHyperlinkParameter)hyperlinkParameters[i].clone();
			}
		}

		if (titleExpression != null)
		{
			clone.titleExpression = (JRExpression)titleExpression.clone();
		}
		if (subtitleExpression != null)
		{
			clone.subtitleExpression = (JRExpression)subtitleExpression.clone();
		}
		if (anchorNameExpression != null)
		{
			clone.anchorNameExpression = (JRExpression)anchorNameExpression.clone();
		}
		if (hyperlinkReferenceExpression != null)
		{
			clone.hyperlinkReferenceExpression = (JRExpression)hyperlinkReferenceExpression.clone();
		}
		if (hyperlinkAnchorExpression != null)
		{
			clone.hyperlinkAnchorExpression = (JRExpression)hyperlinkAnchorExpression.clone();
		}
		if (hyperlinkPageExpression != null)
		{
			clone.hyperlinkPageExpression = (JRExpression)hyperlinkPageExpression.clone();
		}
		if (hyperlinkTooltipExpression != null)
		{
			clone.hyperlinkTooltipExpression = (JRExpression)hyperlinkTooltipExpression.clone();
		}

		if (dataset != null)
		{
			clone.dataset = (JRChartDataset)dataset.clone();
		}
		if (plot != null)
		{
			clone.plot = (JRChartPlot)plot.clone(clone);
		}

		return clone;
	}
}
