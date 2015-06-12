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
package net.sf.jasperreports.engine.design;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sf.jasperreports.charts.design.JRDesignAreaPlot;
import net.sf.jasperreports.charts.design.JRDesignBar3DPlot;
import net.sf.jasperreports.charts.design.JRDesignBarPlot;
import net.sf.jasperreports.charts.design.JRDesignBubblePlot;
import net.sf.jasperreports.charts.design.JRDesignCandlestickPlot;
import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignGanttDataset;
import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;
import net.sf.jasperreports.charts.design.JRDesignHighLowPlot;
import net.sf.jasperreports.charts.design.JRDesignLinePlot;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.charts.design.JRDesignPie3DPlot;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignPiePlot;
import net.sf.jasperreports.charts.design.JRDesignScatterPlot;
import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.charts.design.JRDesignTimeSeriesDataset;
import net.sf.jasperreports.charts.design.JRDesignTimeSeriesPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDataset;
import net.sf.jasperreports.charts.design.JRDesignXyDataset;
import net.sf.jasperreports.charts.design.JRDesignXyzDataset;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseChart;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRCloneUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRDesignChart extends JRDesignElement implements JRChart
{
	public static final String EXCEPTION_MESSAGE_KEY_UNSUPPORTED_CHART_TYPE = "charts.chart.type.unsupported";

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/*
	 * Chart properties
	 */
	
	public static final String PROPERTY_ANCHOR_NAME_EXPRESSION = "anchorNameExpression";
	
	public static final String PROPERTY_BOOKMARK_LEVEL = "bookmarkLevel";
	
	public static final String PROPERTY_EVALUATION_GROUP = "evaluationGroup";
	
	public static final String PROPERTY_EVALUATION_TIME = "evaluationTime";
	
	public static final String PROPERTY_CHART_TYPE = "chartType";
	
	public static final String PROPERTY_CUSTOMIZER_CLASS = "customizerClass";
	
	public static final String PROPERTY_DATASET = "dataset";
	
	public static final String PROPERTY_LEGEND_FONT = "legendFont";
	
	public static final String PROPERTY_SUBTITLE_EXPRESSION = "subtitleExpression";
	
	public static final String PROPERTY_SUBTITLE_FONT = "subtitleFont";
	
	public static final String PROPERTY_TITLE_EXPRESSION = "titleExpression";
	
	public static final String PROPERTY_TITLE_FONT = "titleFont";
	
	/**
	 *
	 */
	protected byte chartType;

	/**
	 *
	 */
	protected Boolean showLegend;
	protected EvaluationTimeEnum evaluationTimeValue = EvaluationTimeEnum.NOW;
	protected String linkType;
	protected String linkTarget;
	protected Color titleColor;
	protected Color subtitleColor;
	protected Color legendColor;
	protected Color legendBackgroundColor;
	protected String renderType;
	protected String theme;

	/**
	 *
	 */
	protected JRFont titleFont;
	protected JRFont subtitleFont;
	protected JRFont legendFont;
	protected EdgeEnum legendPositionValue;
	protected EdgeEnum titlePositionValue;

	protected String customizerClass;

	/**
	 *
	 */
	protected JRGroup evaluationGroup;
	protected JRExpression titleExpression;
	protected JRExpression subtitleExpression;
	protected JRExpression anchorNameExpression;
	protected JRExpression hyperlinkReferenceExpression;
	protected JRExpression hyperlinkWhenExpression;
	protected JRExpression hyperlinkAnchorExpression;
	protected JRExpression hyperlinkPageExpression;
	private JRExpression hyperlinkTooltipExpression;
	private List<JRHyperlinkParameter> hyperlinkParameters;

	protected JRChartDataset dataset;
	protected JRChartPlot plot;

	/**
	 *
	 */
	protected JRLineBox lineBox;
	
	/**
	 * The bookmark level for the anchor associated with this chart.
	 * @see JRAnchor#getBookmarkLevel()
	 */
	protected int bookmarkLevel = JRAnchor.NO_BOOKMARK;


	/**
	 *
	 */
	public JRDesignChart(JRDefaultStyleProvider defaultStyleProvider, byte chartType)
	{
		super(defaultStyleProvider);
		
		setChartType(chartType);
		
		hyperlinkParameters = new ArrayList<JRHyperlinkParameter>();
		
		lineBox = new JRBaseLineBox(this);
	}

	/**
	 * 
	 */
	public Boolean getShowLegend()
	{
		return this.showLegend;
	}

	/**
	 *
	 */
	public void setShowLegend(Boolean isShowLegend)
	{
		Boolean old = this.showLegend;
		this.showLegend = isShowLegend;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_SHOW_LEGEND, old, this.showLegend);
	}

	
	/**
	 *
	 */
	public String getRenderType()
	{
		return renderType;
	}

	/**
	 *
	 */
	public void setRenderType(String renderType)
	{
		String old = this.renderType;
		this.renderType = renderType;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_RENDER_TYPE, old, this.renderType);
	}

	/**
	 *
	 */
	public String getTheme()
	{
		return theme;
	}

	/**
	 *
	 */
	public void setTheme(String theme)
	{
		String old = this.theme;
		this.theme = theme;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_THEME, old, this.theme);
	}

	/**
	 *
	 */
	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return evaluationTimeValue;
	}
		
	/**
	 *
	 */
	public void setEvaluationTime(EvaluationTimeEnum evaluationTimeValue)
	{
		Object old = this.evaluationTimeValue;
		this.evaluationTimeValue = evaluationTimeValue;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_TIME, old, this.evaluationTimeValue);
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	/**
	 *
	 */
	public void setEvaluationGroup(JRGroup group)
	{
		Object old = this.evaluationGroup;
		this.evaluationGroup = group;
		getEventSupport().firePropertyChange(PROPERTY_EVALUATION_GROUP, old, this.evaluationGroup);
	}
		
	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox;
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
	public void setTitleFont(JRFont font)//FIXMEFONT embedded fonts should never be null so these font setting methods should be deprecated; check iR impact
	{
		Object old = this.titleFont;
		this.titleFont = font;
		getEventSupport().firePropertyChange(PROPERTY_TITLE_FONT, old, this.titleFont);
	}
	
	/**
	 *
	 */
	public EdgeEnum getTitlePositionValue()
	{
		return titlePositionValue;
	}

	/**
	 *
	 */
	public void setTitlePosition(EdgeEnum titlePositionValue)
	{
		EdgeEnum old = this.titlePositionValue;
		this.titlePositionValue = titlePositionValue;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_TITLE_POSITION, old, this.titlePositionValue);
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
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_TITLE_COLOR, old, this.titleColor);
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
	public void setSubtitleFont(JRFont font)
	{
		Object old = this.subtitleFont;
		this.subtitleFont = font;
		getEventSupport().firePropertyChange(PROPERTY_SUBTITLE_FONT, old, this.subtitleFont);
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
	public Color getOwnSubtitleColor()
	{
		return subtitleColor;
	}

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor)
	{
		Object old = this.subtitleColor;
		this.subtitleColor = subtitleColor;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_SUBTITLE_COLOR, old, this.subtitleColor);
	}

	/**
	 *
	 */
	public Color getOwnLegendColor()
	{
		return legendColor;
	}

	/**
	 *
	 */
	public Color getLegendColor()
	{
		return JRStyleResolver.getLegendColor(this);
	}

	/**
	 *
	 */
	public void setLegendColor(Color legendColor)
	{
		Object old = this.legendColor;
		this.legendColor = legendColor;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_LEGEND_COLOR, old, this.legendColor);
	}
	
	/**
	 *
	 */
	public Color getOwnLegendBackgroundColor()
	{
		return legendBackgroundColor;
	}

	/**
	 *
	 */
	public Color getLegendBackgroundColor()
	{
		return JRStyleResolver.getLegendBackgroundColor(this);
	}

	/**
	 *
	 */
	public void setLegendBackgroundColor(Color legendBackgroundColor)
	{
		Object old = this.legendBackgroundColor;
		this.legendBackgroundColor = legendBackgroundColor;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR, old, this.legendBackgroundColor);
	}
	
	/**
	 *
	 */
	public JRFont getLegendFont()
	{
		return legendFont;
	}

	/**
	 *
	 */
	public void setLegendFont(JRFont legendFont)
	{
		Object old = this.legendFont;
		this.legendFont = legendFont;
		getEventSupport().firePropertyChange(PROPERTY_LEGEND_FONT, old, this.legendFont);
	}
	
	/**
	 *
	 */
	public EdgeEnum getLegendPositionValue()
	{
		return legendPositionValue;
	}

	/**
	 *
	 */
	public void setLegendPosition(EdgeEnum legendPositionValue)
	{
		EdgeEnum old = this.legendPositionValue;
		this.legendPositionValue = legendPositionValue;
		getEventSupport().firePropertyChange(JRBaseChart.PROPERTY_LEGEND_POSITION, old, this.legendPositionValue);
	}

	/**
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}
		
	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return JRHyperlinkHelper.getHyperlinkTypeValue(this);
	}
		
	/**
	 * @deprecated Replaced by {@link #setHyperlinkType(HyperlinkTypeEnum)}.
	 */
	public void setHyperlinkType(byte hyperlinkType)
	{
		setHyperlinkType(HyperlinkTypeEnum.getByValue(hyperlinkType));
	}
		
	/**
	 * Sets the link type as a built-in hyperlink type.
	 * 
	 * @param hyperlinkType the built-in hyperlink type
	 * @see #getLinkType()
	 */
	public void setHyperlinkType(HyperlinkTypeEnum hyperlinkType)
	{
		setLinkType(JRHyperlinkHelper.getLinkType(hyperlinkType));
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return JRHyperlinkHelper.getHyperlinkTarget(this);
	}
		
	/**
	 * @deprecated Replaced by {@link #setHyperlinkTarget(HyperlinkTargetEnum)}.
	 */
	public void setHyperlinkTarget(byte hyperlinkTarget)
	{
		setHyperlinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
	}
		
	/**
	 *
	 */
	public void setHyperlinkTarget(HyperlinkTargetEnum hyperlinkTarget)
	{
		setLinkTarget(JRHyperlinkHelper.getLinkTarget(hyperlinkTarget));
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
	public void setTitleExpression(JRExpression expression)
	{
		Object old = this.titleExpression;
		this.titleExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_TITLE_EXPRESSION, old, this.titleExpression);
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
	public void setSubtitleExpression(JRExpression expression)
	{
		Object old = this.subtitleExpression;
		this.subtitleExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_SUBTITLE_EXPRESSION, old, this.subtitleExpression);
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
	public void setAnchorNameExpression(JRExpression anchorNameExpression)
	{
		Object old = this.anchorNameExpression;
		this.anchorNameExpression = anchorNameExpression;
		getEventSupport().firePropertyChange(PROPERTY_ANCHOR_NAME_EXPRESSION, old, this.anchorNameExpression);
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
	public void setHyperlinkReferenceExpression(JRExpression hyperlinkReferenceExpression)
	{
		Object old = this.hyperlinkReferenceExpression;
		this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
		getEventSupport().firePropertyChange(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION, old, this.hyperlinkReferenceExpression);
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkWhenExpression()
	{
		return hyperlinkWhenExpression;
	}

	/**
	 *
	 */
	public void setHyperlinkWhenExpression(JRExpression hyperlinkWhenExpression)
	{
		Object old = this.hyperlinkWhenExpression;
		this.hyperlinkWhenExpression = hyperlinkWhenExpression;
		getEventSupport().firePropertyChange(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION, old, this.hyperlinkWhenExpression);
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
	public void setHyperlinkAnchorExpression(JRExpression hyperlinkAnchorExpression)
	{
		Object old = this.hyperlinkAnchorExpression;
		this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
		getEventSupport().firePropertyChange(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION, old, this.hyperlinkAnchorExpression);
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
	public void setHyperlinkPageExpression(JRExpression hyperlinkPageExpression)
	{
		Object old = this.hyperlinkPageExpression;
		this.hyperlinkPageExpression = hyperlinkPageExpression;
		getEventSupport().firePropertyChange(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION, old, this.hyperlinkPageExpression);
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


	/**
	 *
	 */
	public void setChartType(byte chartType)
	{
		byte old = this.chartType;
		this.chartType = chartType;
		
		switch(chartType) {
			case CHART_TYPE_AREA:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignAreaPlot(plot, this);
				break;
			case CHART_TYPE_BAR:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBarPlot(plot, this);
				break;
			case CHART_TYPE_BAR3D:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBar3DPlot(plot, this);
				break;
			case CHART_TYPE_BUBBLE:
				dataset = new JRDesignXyzDataset(dataset);
				plot = new JRDesignBubblePlot(plot, this);
				break;
			case CHART_TYPE_CANDLESTICK:
				dataset = new JRDesignHighLowDataset(dataset);
				plot = new JRDesignCandlestickPlot(plot, this);
				break;
			case CHART_TYPE_HIGHLOW:
				dataset = new JRDesignHighLowDataset(dataset);
				plot = new JRDesignHighLowPlot(plot, this);
				break;
			case CHART_TYPE_LINE:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignLinePlot(plot, this);
				break;
			case CHART_TYPE_METER:
				dataset = new JRDesignValueDataset(dataset);
				plot = new JRDesignMeterPlot(plot, this);
				break;
			case CHART_TYPE_MULTI_AXIS:
				dataset = null;
				plot = new JRDesignMultiAxisPlot(plot, this);
				break;
			case CHART_TYPE_PIE:
				dataset = new JRDesignPieDataset(dataset);
				plot = new JRDesignPiePlot(plot, this);
				break;
			case CHART_TYPE_PIE3D:
				dataset = new JRDesignPieDataset(dataset);
				plot = new JRDesignPie3DPlot(plot, this);
				break;
			case CHART_TYPE_SCATTER:
				dataset = new JRDesignXyDataset(dataset);
				plot = new JRDesignScatterPlot(plot, this);
				break;
			case CHART_TYPE_STACKEDBAR:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBarPlot(plot, this);
				break;
			case CHART_TYPE_STACKEDBAR3D:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignBar3DPlot(plot, this);
				break;
			case CHART_TYPE_THERMOMETER:
				dataset = new JRDesignValueDataset(dataset);
				plot = new JRDesignThermometerPlot(plot, this);
				break;
			case CHART_TYPE_TIMESERIES:
				dataset = new JRDesignTimeSeriesDataset(dataset);//other datasets could be supported
				plot = new JRDesignTimeSeriesPlot(plot, this);
				break;
			case CHART_TYPE_XYAREA:
				dataset = new JRDesignXyDataset(dataset);
				plot = new JRDesignAreaPlot(plot, this);
				break;
			case CHART_TYPE_XYBAR:
				plot = new JRDesignBarPlot(plot, this);
				break;
			case CHART_TYPE_XYLINE:
				dataset = new JRDesignXyDataset(dataset);
				plot = new JRDesignLinePlot(plot, this);
				break;
			case CHART_TYPE_STACKEDAREA:
				dataset = new JRDesignCategoryDataset(dataset);
				plot = new JRDesignAreaPlot(plot, this);
				break;
			case CHART_TYPE_GANTT:
				dataset = new JRDesignGanttDataset(dataset);
				plot = new JRDesignBarPlot(plot, this);
				break;
			default:
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_UNSUPPORTED_CHART_TYPE,
						(Object[])null);
		}

		getEventSupport().firePropertyChange(PROPERTY_CHART_TYPE, old, this.chartType);
	}


	public void setDataset(JRChartDataset ds)
	{
		Object old = this.dataset;
		dataset = ds;
		getEventSupport().firePropertyChange(PROPERTY_DATASET, old, this.dataset);		
	}


	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitChart(this);
	}

	
	public int getBookmarkLevel()
	{
		return bookmarkLevel;
	}


	/**
	 * Sets the boomark level for the anchor associated with this chart.
	 * 
	 * @param bookmarkLevel the bookmark level (starting from 1)
	 * or {@link JRAnchor#NO_BOOKMARK NO_BOOKMARK} if no bookmark should be created
	 */
	public void setBookmarkLevel(int bookmarkLevel)
	{
		int old = this.bookmarkLevel;
		this.bookmarkLevel = bookmarkLevel;
		getEventSupport().firePropertyChange(PROPERTY_BOOKMARK_LEVEL, old, this.bookmarkLevel);
	}

	/**
	 *
	 */
	public String getCustomizerClass()
	{
		return customizerClass;
	}

	/**
	 * Sets a user specified chart customizer class name.
	 * @see net.sf.jasperreports.engine.JRChartCustomizer
 	 */
	public void setCustomizerClass(String customizerClass)
	{
		Object old = this.customizerClass;
		this.customizerClass = customizerClass;
		getEventSupport().firePropertyChange(PROPERTY_CUSTOMIZER_CLASS, old, this.customizerClass);
	}


	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
	}


	public String getLinkType()
	{
		return linkType;
	}

	/**
	 * Sets the hyperlink type.
	 * <p>
	 * The type can be one of the built-in types
	 * (Reference, LocalAnchor, LocalPage, RemoteAnchor, RemotePage),
	 * or can be an arbitrary type.
	 * </p>
	 * @param type the hyperlink type
	 */
	public void setLinkType(String type)
	{
		Object old = this.linkType;
		this.linkType = type;
		getEventSupport().firePropertyChange(JRDesignHyperlink.PROPERTY_LINK_TYPE, old, this.linkType);
	}

	public String getLinkTarget()
	{
		return linkTarget;
	}


	/**
	 * Sets the hyperlink target.
	 * <p>
	 * The target can be one of the built-in target names
	 * (Self, Blank, Top, Parent),
	 * or can be an arbitrary target name.
	 * </p>
	 * @param target the hyperlink target
	 */
	public void setLinkTarget(String target)
	{
		Object old = this.linkTarget;
		this.linkTarget = target;
		getEventSupport().firePropertyChange(JRDesignHyperlink.PROPERTY_LINK_TARGET, old, this.linkTarget);
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		JRHyperlinkParameter[] parameters;
		if (hyperlinkParameters.isEmpty())
		{
			parameters = null;
		}
		else
		{
			parameters = new JRHyperlinkParameter[hyperlinkParameters.size()];
			hyperlinkParameters.toArray(parameters);
		}
		return parameters;
	}
	
	
	/**
	 * Returns the list of custom hyperlink parameters.
	 * 
	 * @return the list of custom hyperlink parameters
	 */
	public List<JRHyperlinkParameter> getHyperlinkParametersList()
	{
		return hyperlinkParameters;
	}
	
	
	/**
	 * Adds a custom hyperlink parameter.
	 * 
	 * @param parameter the parameter to add
	 */
	public void addHyperlinkParameter(JRHyperlinkParameter parameter)
	{
		hyperlinkParameters.add(parameter);
		getEventSupport().fireCollectionElementAddedEvent(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, 
				parameter, hyperlinkParameters.size() - 1);
	}
	

	/**
	 * Removes a custom hyperlink parameter.
	 * 
	 * @param parameter the parameter to remove
	 */
	public void removeHyperlinkParameter(JRHyperlinkParameter parameter)
	{
		int idx = hyperlinkParameters.indexOf(parameter);
		if (idx >= 0)
		{
			hyperlinkParameters.remove(idx);
			getEventSupport().fireCollectionElementRemovedEvent(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, 
					parameter, idx);
		}
	}
	
	
	/**
	 * Removes a custom hyperlink parameter.
	 * <p>
	 * If multiple parameters having the specified name exist, all of them
	 * will be removed
	 * </p>
	 * 
	 * @param parameterName the parameter name
	 */
	public void removeHyperlinkParameter(String parameterName)
	{
		for (ListIterator<JRHyperlinkParameter> it = hyperlinkParameters.listIterator(); it.hasNext();)
		{
			JRHyperlinkParameter parameter = it.next();
			if (parameter.getName() != null && parameter.getName().equals(parameterName))
			{
				it.remove();
				getEventSupport().fireCollectionElementRemovedEvent(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, 
						parameter, it.nextIndex());
			}
		}
	}
	
	
	public JRExpression getHyperlinkTooltipExpression()
	{
		return hyperlinkTooltipExpression;
	}

	
	/**
	 * Sets the expression which will be used to generate the hyperlink tooltip.
	 * 
	 * @param hyperlinkTooltipExpression the expression which will be used to generate the hyperlink tooltip
	 * @see #getHyperlinkTooltipExpression()
	 */
	public void setHyperlinkTooltipExpression(JRExpression hyperlinkTooltipExpression)
	{
		Object old = this.hyperlinkTooltipExpression;
		this.hyperlinkTooltipExpression = hyperlinkTooltipExpression;
		getEventSupport().firePropertyChange(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION, old, this.hyperlinkTooltipExpression);
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}

	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRDesignChart clone = (JRDesignChart)super.clone();
		clone.lineBox = lineBox.clone(clone);
		clone.hyperlinkParameters = JRCloneUtils.cloneList(hyperlinkParameters);
		clone.titleExpression = JRCloneUtils.nullSafeClone(titleExpression);
		clone.subtitleExpression = JRCloneUtils.nullSafeClone(subtitleExpression);
		clone.anchorNameExpression = JRCloneUtils.nullSafeClone(anchorNameExpression);
		clone.hyperlinkReferenceExpression = JRCloneUtils.nullSafeClone(hyperlinkReferenceExpression);
		clone.hyperlinkWhenExpression = JRCloneUtils.nullSafeClone(hyperlinkWhenExpression);
		clone.hyperlinkAnchorExpression = JRCloneUtils.nullSafeClone(hyperlinkAnchorExpression);
		clone.hyperlinkPageExpression = JRCloneUtils.nullSafeClone(hyperlinkPageExpression);
		clone.hyperlinkTooltipExpression = JRCloneUtils.nullSafeClone(hyperlinkTooltipExpression);
		clone.dataset = JRCloneUtils.nullSafeClone(dataset);
		clone.plot = plot == null ? null : (JRChartPlot) plot.clone(clone);
		return clone;
	}
	

	/*
	 * These fields are only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID; //NOPMD
	/**
	 * @deprecated
	 */
	private Byte border;
	/**
	 * @deprecated
	 */
	private Byte topBorder;
	/**
	 * @deprecated
	 */
	private Byte leftBorder;
	/**
	 * @deprecated
	 */
	private Byte bottomBorder;
	/**
	 * @deprecated
	 */
	private Byte rightBorder;
	/**
	 * @deprecated
	 */
	private Color borderColor;
	/**
	 * @deprecated
	 */
	private Color topBorderColor;
	/**
	 * @deprecated
	 */
	private Color leftBorderColor;
	/**
	 * @deprecated
	 */
	private Color bottomBorderColor;
	/**
	 * @deprecated
	 */
	private Color rightBorderColor;
	/**
	 * @deprecated
	 */
	private Integer padding;
	/**
	 * @deprecated
	 */
	private Integer topPadding;
	/**
	 * @deprecated
	 */
	private Integer leftPadding;
	/**
	 * @deprecated
	 */
	private Integer bottomPadding;
	/**
	 * @deprecated
	 */
	private Integer rightPadding;
	/**
	 * @deprecated
	 */
	private boolean isShowLegend;
	/**
	 * @deprecated
	 */
	private byte titlePosition;
	/**
	 * @deprecated
	 */
	private byte legendPosition;
	/**
	 * @deprecated
	 */
	private byte hyperlinkType;
	/**
	 * @deprecated
	 */
	private byte hyperlinkTarget;
	/**
	 * @deprecated
	 */
	private byte evaluationTime;
	/**
	 * @deprecated
	 */
	private Byte legendPositionByte;
	/**
	 * @deprecated
	 */
	private Byte titlePositionByte;
	
	@SuppressWarnings("deprecation")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		if (lineBox == null)
		{
			lineBox = new JRBaseLineBox(this);
			JRBoxUtil.setToBox(
				border,
				topBorder,
				leftBorder,
				bottomBorder,
				rightBorder,
				borderColor,
				topBorderColor,
				leftBorderColor,
				bottomBorderColor,
				rightBorderColor,
				padding,
				topPadding,
				leftPadding,
				bottomPadding,
				rightPadding,
				lineBox
				);
			border = null;
			topBorder = null;
			leftBorder = null;
			bottomBorder = null;
			rightBorder = null;
			borderColor = null;
			topBorderColor = null;
			leftBorderColor = null;
			bottomBorderColor = null;
			rightBorderColor = null;
			padding = null;
			topPadding = null;
			leftPadding = null;
			bottomPadding = null;
			rightPadding = null;
		}

		if (linkType == null)
		{
			 linkType = JRHyperlinkHelper.getLinkType(HyperlinkTypeEnum.getByValue(hyperlinkType));
		}

		if (linkTarget == null)
		{
			 linkTarget = JRHyperlinkHelper.getLinkTarget(HyperlinkTargetEnum.getByValue(hyperlinkTarget));
		}
		
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_7_2)
		{
			evaluationTimeValue = EvaluationTimeEnum.getByValue(evaluationTime);
			if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3)
			{
				legendPositionValue = EdgeEnum.getByValue(legendPosition);
				titlePositionValue = EdgeEnum.getByValue(titlePosition);
				showLegend = Boolean.valueOf(isShowLegend);
			}
			else
			{
				legendPositionValue = EdgeEnum.getByValue(legendPositionByte);
				titlePositionValue = EdgeEnum.getByValue(titlePositionByte);
				
				legendPositionByte = null;
				titlePositionByte = null;
			}
			
		}
	}
}
