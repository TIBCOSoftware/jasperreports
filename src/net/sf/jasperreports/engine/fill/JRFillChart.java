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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRGanttDataset;
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
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillChartAxis;
import net.sf.jasperreports.charts.fill.JRFillGanttDataset;
import net.sf.jasperreports.charts.fill.JRFillHighLowDataset;
import net.sf.jasperreports.charts.fill.JRFillMultiAxisPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillTimePeriodDataset;
import net.sf.jasperreports.charts.fill.JRFillTimeSeriesDataset;
import net.sf.jasperreports.charts.fill.JRFillXyDataset;
import net.sf.jasperreports.charts.fill.JRFillXyzDataset;
import net.sf.jasperreports.charts.util.CategoryChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartRendererFactory;
import net.sf.jasperreports.charts.util.HighLowChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.charts.util.MultiAxisChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.PieChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.TimePeriodChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.TimeSeriesChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.XYChartHyperlinkProvider;
import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRPenUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSingletonCache;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.LineBoxWrapper;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @author Some enhancements by Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillChart extends JRFillElement implements JRChart
{


	/**
	 *
	 */
	private static final JRSingletonCache chartRendererFactoryCache = new JRSingletonCache(ChartRendererFactory.class);

	/**
	 *
	 */
	protected byte chartType = 0;

	/**
	 *
	 */
	protected JRFont titleFont = null;
	protected JRFont subtitleFont = null;
	protected JRFont legendFont = null;
	protected final JRLineBox lineBox;

	/**
	 *
	 */
	protected JRGroup evaluationGroup = null;

	protected JRFillChartDataset dataset = null;
	protected JRChartPlot plot = null;

	protected JRRenderable renderer = null;
	private String anchorName = null;
	private String hyperlinkReference = null;
	private String hyperlinkAnchor = null;
	private Integer hyperlinkPage = null;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;

	protected String customizerClass;
	protected JRChartCustomizer chartCustomizer;
	
	protected String renderType;
	protected String themeName;

	protected JFreeChart jfreeChart;
	protected ChartHyperlinkProvider chartHyperlinkProvider;

	/**
	 *
	 */
	protected JRFillChart(
		JRBaseFiller filler,
		JRChart chart,
		JRFillObjectFactory factory
		)
	{
		super(filler, chart, factory);

		/*   */
		chartType = chart.getChartType();

		switch(chartType) {
			case CHART_TYPE_AREA:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_BAR:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_BAR3D:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_BUBBLE:
				dataset = (JRFillChartDataset) factory.getXyzDataset((JRXyzDataset) chart.getDataset());
				plot = factory.getBubblePlot((JRBubblePlot) chart.getPlot());
				break;
			case CHART_TYPE_CANDLESTICK:
				dataset = (JRFillChartDataset) factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getCandlestickPlot((JRCandlestickPlot) chart.getPlot());
				break;
			case CHART_TYPE_HIGHLOW:
				dataset = (JRFillChartDataset) factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getHighLowPlot((JRHighLowPlot) chart.getPlot());
				break;
			case CHART_TYPE_LINE:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			case CHART_TYPE_METER:
				dataset = (JRFillChartDataset) factory.getValueDataset((JRValueDataset) chart.getDataset());
				plot = factory.getMeterPlot((JRMeterPlot) chart.getPlot());
				break;
			case CHART_TYPE_MULTI_AXIS:
				plot = factory.getMultiAxisPlot((JRMultiAxisPlot) chart.getPlot());
				dataset = ((JRFillMultiAxisPlot)plot).getMainDataset();
				break;
			case CHART_TYPE_PIE:
				dataset = (JRFillChartDataset) factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPiePlot((JRPiePlot) chart.getPlot());
				break;
			case CHART_TYPE_PIE3D:
				dataset = (JRFillChartDataset) factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPie3DPlot((JRPie3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_SCATTER:
				dataset = (JRFillChartDataset) factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getScatterPlot((JRScatterPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR3D:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_THERMOMETER:
				dataset = (JRFillChartDataset) factory.getValueDataset((JRValueDataset) chart.getDataset());
				plot = factory.getThermometerPlot((JRThermometerPlot) chart.getPlot());
				break;
			case CHART_TYPE_TIMESERIES:
				dataset = (JRFillChartDataset) factory.getTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
				plot = factory.getTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
				break;
			case CHART_TYPE_XYAREA:
				dataset = (JRFillChartDataset) factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYBAR:
				switch (chart.getDataset().getDatasetType()){
					case JRChartDataset.TIMESERIES_DATASET:
						dataset = (JRFillChartDataset) factory.getTimeSeriesDataset( (JRTimeSeriesDataset)chart.getDataset() );
						break;
					case JRChartDataset.TIMEPERIOD_DATASET:
						dataset = (JRFillChartDataset) factory.getTimePeriodDataset((JRTimePeriodDataset) chart.getDataset() );
						break;
					case JRChartDataset.XY_DATASET:
						dataset = (JRFillChartDataset) factory.getXyDataset( (JRXyDataset)chart.getDataset() );
						break;
				}

				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYLINE:
				dataset = (JRFillChartDataset) factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDAREA:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_GANTT:
				dataset = (JRFillChartDataset) factory.getGanttDataset((JRGanttDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}

		titleFont = new JRBaseFont(null, null, chart, chart.getTitleFont());
		subtitleFont = new JRBaseFont(null, null, chart, chart.getSubtitleFont());
		legendFont = new JRBaseFont(null, null, chart, chart.getLegendFont());
		
		lineBox = chart.getLineBox().clone(this);

		evaluationGroup = factory.getGroup(chart.getEvaluationGroup());

		customizerClass = chart.getCustomizerClass();
		if (customizerClass != null && customizerClass.length() > 0) {
			try {
				Class myClass = JRClassLoader.loadClassForName(customizerClass);
				chartCustomizer = (JRChartCustomizer) myClass.newInstance();
			} catch (Exception e) {
				throw new JRRuntimeException("Could not create chart customizer instance.", e);
			}

			if (chartCustomizer instanceof JRAbstractChartCustomizer)
			{
				((JRAbstractChartCustomizer) chartCustomizer).init(filler, this);
			}
		}
		
		renderType = chart.getRenderType();
		if(renderType == null)
		{
			renderType = JRProperties.getProperty(getParentProperties(), JRChart.PROPERTY_CHART_RENDER_TYPE);
		}
		
		themeName = chart.getTheme();
		if(themeName == null)
		{
			themeName = JRProperties.getProperty(getParentProperties(), JRChart.PROPERTY_CHART_THEME);
		}
	}


	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}

	/**
	 * @deprecated Replaced by {@link #getShowLegend()}
	 */
	public boolean isShowLegend()
	{
		return ((JRChart)parent).isShowLegend();
	}

	/**
	 * @deprecated Replaced by {@link #isShowLegend(Boolean)}
	 */
	public void setShowLegend(boolean isShowLegend)
	{
	}

	/**
	 * 
	 */
	public Boolean getShowLegend()
	{
		return ((JRChart)parent).getShowLegend();
	}

	/**
	 *
	 */
	public void setShowLegend(Boolean isShowLegend)
	{
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
	}

	/**
	 *
	 */
	public String getTheme()
	{
		return themeName;
	}

	/**
	 *
	 */
	public void setTheme(String theme)
	{
	}

	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return ((JRChart)parent).getEvaluationTime();
	}

	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()} 
	 */
	public JRBox getBox()
	{
		return new LineBoxWrapper(getLineBox());
	}

	/**
	 *
	 */
	public JRLineBox getLineBox()
	{
		return lineBox;
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBorder(byte border)
	{
		JRPenUtil.setLinePenFromPen(border, getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBorder(Byte border)
	{
		JRPenUtil.setLinePenFromPen(border, getLineBox().getPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getBorderColor()
	{
		return getLineBox().getPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnBorderColor()
	{
		return getLineBox().getPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBorderColor(Color borderColor)
	{
		getLineBox().getPen().setLineColor(borderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getPadding()
	{
		return getLineBox().getPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnPadding()
	{
		return getLineBox().getOwnPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setPadding(int padding)
	{
		getLineBox().setPadding(padding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setPadding(Integer padding)
	{
		getLineBox().setPadding(padding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getTopBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnTopBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopBorder(byte topBorder)
	{
		JRPenUtil.setLinePenFromPen(topBorder, getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopBorder(Byte topBorder)
	{
		JRPenUtil.setLinePenFromPen(topBorder, getLineBox().getTopPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getTopBorderColor()
	{
		return getLineBox().getTopPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnTopBorderColor()
	{
		return getLineBox().getTopPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
		getLineBox().getTopPen().setLineColor(topBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getTopPadding()
	{
		return getLineBox().getTopPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnTopPadding()
	{
		return getLineBox().getOwnTopPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopPadding(int topPadding)
	{
		getLineBox().setTopPadding(topPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setTopPadding(Integer topPadding)
	{
		getLineBox().setTopPadding(topPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getLeftBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnLeftBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftBorder(byte leftBorder)
	{
		JRPenUtil.setLinePenFromPen(leftBorder, getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftBorder(Byte leftBorder)
	{
		JRPenUtil.setLinePenFromPen(leftBorder, getLineBox().getLeftPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getLeftBorderColor()
	{
		return getLineBox().getLeftPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnLeftBorderColor()
	{
		return getLineBox().getLeftPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
		getLineBox().getLeftPen().setLineColor(leftBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getLeftPadding()
	{
		return getLineBox().getLeftPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnLeftPadding()
	{
		return getLineBox().getOwnLeftPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftPadding(int leftPadding)
	{
		getLineBox().setLeftPadding(leftPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		getLineBox().setLeftPadding(leftPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getBottomBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnBottomBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomBorder(byte bottomBorder)
	{
		JRPenUtil.setLinePenFromPen(bottomBorder, getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
		JRPenUtil.setLinePenFromPen(bottomBorder, getLineBox().getBottomPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getBottomBorderColor()
	{
		return getLineBox().getBottomPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnBottomBorderColor()
	{
		return getLineBox().getBottomPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
		getLineBox().getBottomPen().setLineColor(bottomBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getBottomPadding()
	{
		return getLineBox().getBottomPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnBottomPadding()
	{
		return getLineBox().getOwnBottomPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomPadding(int bottomPadding)
	{
		getLineBox().setBottomPadding(bottomPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		getLineBox().setBottomPadding(bottomPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public byte getRightBorder()
	{
		return JRPenUtil.getPenFromLinePen(getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Byte getOwnRightBorder()
	{
		return JRPenUtil.getOwnPenFromLinePen(getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightBorder(byte rightBorder)
	{
		JRPenUtil.setLinePenFromPen(rightBorder, getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightBorder(Byte rightBorder)
	{
		JRPenUtil.setLinePenFromPen(rightBorder, getLineBox().getRightPen());
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getRightBorderColor()
	{
		return getLineBox().getRightPen().getLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Color getOwnRightBorderColor()
	{
		return getLineBox().getRightPen().getOwnLineColor();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
		getLineBox().getRightPen().setLineColor(rightBorderColor);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public int getRightPadding()
	{
		return getLineBox().getRightPadding().intValue();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public Integer getOwnRightPadding()
	{
		return getLineBox().getOwnRightPadding();
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightPadding(int rightPadding)
	{
		getLineBox().setRightPadding(rightPadding);
	}

	/**
	 * @deprecated Replaced by {@link #getLineBox()}
	 */
	public void setRightPadding(Integer rightPadding)
	{
		getLineBox().setRightPadding(rightPadding);
	}

	/**
	 *
	 */
	public JRFont getTitleFont()
	{
		return titleFont;
	}

	/**
	 * @deprecated Replaced by {@link #getTitlePositionByte()}
	 */
	public byte getTitlePosition()
	{
		return ((JRChart)parent).getTitlePosition();
	}

	/**
	 * @deprecated Replaced by {@link #setTitlePosition(Byte)}
	 */
	public void setTitlePosition(byte titlePosition)
	{
	}

	/**
	 *
	 */
	public Byte getTitlePositionByte()
	{
		return ((JRChart)parent).getTitlePositionByte();
	}

	/**
	 *
	 */
	public void setTitlePosition(Byte titlePosition)
	{
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
		return ((JRChart)parent).getOwnTitleColor();
	}

	/**
	 *
	 */
	public void setTitleColor(Color titleColor)
	{
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
		return ((JRChart)parent).getOwnSubtitleColor();
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
	}

	/**
	 * Returns the color to use for text in the legend.
	 *
	 * @return the color to use for text in the legend
	 */
	public Color getOwnLegendColor()
	{
		return ((JRChart)parent).getOwnLegendColor();
	}

	/**
	 * Returns the inherited color to use for text in the legend.
	 *
	 * @return the color to use for text in the legend
	 */
	public Color getLegendColor()
	{
		return JRStyleResolver.getLegendColor(this);
	}

	/**
	 * Sets the color to use for text in the legend.
	 *
	 * @param legendColor the color to use for text in the legend
	 */
	public void setLegendColor(Color legendColor)
	{
	}

	/**
	 * Returns the color to use as the background of the legend.
	 *
	 * @return the color to use as the background of the legend
	 */
	public Color getOwnLegendBackgroundColor()
	{
		return ((JRChart)parent).getOwnLegendBackgroundColor();
	}

	/**
	 * Returns the color to use as the background of the legend.
	 *
	 * @return the color to use as the background of the legend
	 */
	public Color getLegendBackgroundColor()
	{
		return JRStyleResolver.getLegendBackgroundColor(this);
	}

	/**
	 * Sets the color to use for the background of the legend.
	 *
	 * @param legendBackgroundColor the color to use for the background of the legend
	 */
	public void setLegendBackgroundColor(Color legendBackgroundColor)
	{
	}

	/**
	 * Returns the font to use in the legend.
	 *
	 * @return the font to use in the legend
	 */
	public JRFont getLegendFont()
	{
		return legendFont;
	}

	/**
	 * @deprecated Replaced by {@link #getLegendPositionByte()}
	 */
	public byte getLegendPosition()
	{
		return ((JRChart)parent).getLegendPosition();
	}

	/**
	 * @deprecated Replaced by {@link #setLegendPosition(Byte)}
	 */
	public void setLegendPosition(byte legendPosition)
	{
	}

	/**
	 *
	 */
	public Byte getLegendPositionByte()
	{
		return ((JRChart)parent).getLegendPositionByte();
	}

	/**
	 *
	 */
	public void setLegendPosition(Byte legendPosition)
	{
	}

	/**
	 *
	 */
	public JRExpression getTitleExpression()
	{
		return ((JRChart)parent).getTitleExpression();
	}

	/**
	 *
	 */
	public JRExpression getSubtitleExpression()
	{
		return ((JRChart)parent).getSubtitleExpression();
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return ((JRChart)parent).getHyperlinkType();
	}

	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return ((JRChart)parent).getHyperlinkTarget();
	}

	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return ((JRChart)parent).getAnchorNameExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRChart)parent).getHyperlinkReferenceExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRChart)parent).getHyperlinkAnchorExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return ((JRChart)parent).getHyperlinkPageExpression();
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
	public void setDataset(JRFillChartDataset dataset)
	{
		this.dataset = dataset;
	}

	/**
	 *
	 */
	public JRChartPlot getPlot()
	{
		return plot;
	}

	/**
	 *
	 */
	protected JRRenderable getRenderer()
	{
		return renderer;
	}

	/**
	 *
	 */
	protected String getAnchorName()
	{
		return anchorName;
	}

	/**
	 *
	 */
	protected String getHyperlinkReference()
	{
		return hyperlinkReference;
	}

	/**
	 *
	 */
	protected String getHyperlinkAnchor()
	{
		return hyperlinkAnchor;
	}

	/**
	 *
	 */
	protected Integer getHyperlinkPage()
	{
		return hyperlinkPage;
	}

	protected String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
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
	protected JRTemplateImage getJRTemplateImage()
	{
		JRStyle style = getStyle();
		JRTemplateImage template = (JRTemplateImage) getTemplate(style);
		if (template == null)
		{
			template = new JRTemplateImage(getElementOrigin(), 
					filler.getJasperPrint().getDefaultStyleProvider(), this);
			transferProperties(template);
			registerTemplate(style, template);
		}
		return template;
	}


	/**
	 *
	 */
	protected void rewind()
	{
	}


	/**
	 *
	 */
	protected void evaluate(byte evaluation) throws JRException
	{
		reset();

		evaluatePrintWhenExpression(evaluation);

		if (
			(isPrintWhenExpressionNull() ||
			(!isPrintWhenExpressionNull() &&
			isPrintWhenTrue()))
			)
		{
			if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
			{
				evaluateRenderer(evaluation);
			}
		}
	}


	/**
	 *
	 */
	protected void evaluateRenderer(byte evaluation) throws JRException
	{
		JFreeChart chart = evaluateChart(evaluation);
		
		Rectangle2D rectangle = new Rectangle2D.Double(0,0,getWidth(),getHeight());

		renderer = 
			getChartRendererFactory(getRenderType()).getRenderer(
				chart, 
				chartHyperlinkProvider,
				rectangle
				);
	}
	
	protected ChartHyperlinkProvider getHyperlinkProvider()
	{
		return chartHyperlinkProvider;
	}
	
	protected static ChartRendererFactory getChartRendererFactory(String renderType)
	{
		ChartRendererFactory chartRendererFactory = null;

		String factoryClass = JRProperties.getProperty(ChartRendererFactory.PROPERTY_CHART_RENDERER_FACTORY_PREFIX + renderType);
		if (factoryClass == null)
		{
			throw new JRRuntimeException("No chart renderer factory specifyed for '" + renderType + "' render type.");
		}

		try
		{
			chartRendererFactory = (ChartRendererFactory) chartRendererFactoryCache.getCachedInstance(factoryClass);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return chartRendererFactory;
	}

	protected static ChartTheme getChartTheme(String themeName)
	{
		List themeBundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(ChartThemeBundle.class);
		for (Iterator it = themeBundles.iterator(); it.hasNext();)
		{
			ChartThemeBundle bundle = (ChartThemeBundle) it.next();
			ChartTheme chartTheme = bundle.getChartTheme(themeName);
			if (chartTheme != null)
			{
				return chartTheme;
			}
		}
		throw new JRRuntimeException("Chart theme '" + themeName + "' not found.");
	}

	/**
	 *
	 */
	protected JFreeChart evaluateChart(byte evaluation) throws JRException
	{
		evaluateProperties(evaluation);
		evaluateDatasetRun(evaluation);

		ChartTheme theme = null;
		if (themeName == null)
		{
			theme = new DefaultChartTheme();
		}
		else
		{
			theme = getChartTheme(themeName);
		}
		
		if (getChartType() == JRChart.CHART_TYPE_MULTI_AXIS)
		{
			createMultiAxisChart(evaluation);
		}
		else
		{
			jfreeChart = theme.createChart(this, evaluation);

			chartHyperlinkProvider = createChartHyperlinkProvider();
		}

		if (chartCustomizer != null)
		{
			chartCustomizer.customize(jfreeChart, this);
		}

		anchorName = (String) evaluateExpression(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) evaluateExpression(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String) evaluateExpression(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) evaluateExpression(getHyperlinkPageExpression(), evaluation);
		hyperlinkTooltip = (String) evaluateExpression(getHyperlinkTooltipExpression(), evaluation);
		hyperlinkParameters = JRFillHyperlinkHelper.evaluateHyperlinkParameters(this, expressionEvaluator, evaluation);

		return jfreeChart;
	}


	/**
	 *
	 */
	protected boolean prepare(
		int availableStretchHeight,
		boolean isOverflow
		)
	{
		boolean willOverflow = false;

		if (
			isPrintWhenExpressionNull() ||
			( !isPrintWhenExpressionNull() &&
			isPrintWhenTrue() )
			)
		{
			setToPrint(true);
		}
		else
		{
			setToPrint(false);
		}

		if (!isToPrint())
		{
			return willOverflow;
		}

		boolean isToPrint = true;
		boolean isReprinted = false;

		if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
		{
			if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}

			if (
				isToPrint &&
				availableStretchHeight < getRelativeY() - getY() - getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}

			if (
				isToPrint &&
				isOverflow &&
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(isPrintWhenDetailOverflows() && (isAlreadyPrinted() || (!isAlreadyPrinted() && !isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}

			if (
				isToPrint &&
				isRemoveLineWhenBlank() &&
				getRenderer() == null
				)
			{
				isToPrint = false;
			}
		}
		else
		{
			if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}

			if (
				isToPrint &&
				availableStretchHeight < getRelativeY() - getY() - getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}

			if (
				isToPrint &&
				isOverflow &&
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(isPrintWhenDetailOverflows() && (isAlreadyPrinted() || (!isAlreadyPrinted() && !isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}
		}

		setToPrint(isToPrint);
		setReprinted(isReprinted);

		return willOverflow;
	}


	/**
	 *
	 */
	protected JRPrintElement fill()
	{
		JRTemplatePrintImage printImage = new JRTemplatePrintImage(getJRTemplateImage());

		printImage.setX(getX());
		printImage.setY(getRelativeY());
		printImage.setWidth(getWidth());
		printImage.setHeight(getStretchHeight());

		byte evaluationType = getEvaluationTime();
		if (evaluationType == JRExpression.EVALUATION_TIME_NOW)
		{
			copy(printImage);
		}
		else
		{
			filler.addBoundElement(this, printImage, evaluationType, getEvaluationGroup(), band);
		}

		return printImage;
	}


	/**
	 *
	 */
	protected void copy(JRPrintImage printImage)
	{
		printImage.setRenderer(getRenderer());
		printImage.setAnchorName(getAnchorName());
		printImage.setHyperlinkReference(getHyperlinkReference());
		printImage.setHyperlinkAnchor(getHyperlinkAnchor());
		printImage.setHyperlinkPage(getHyperlinkPage());
		printImage.setHyperlinkTooltip(getHyperlinkTooltip());
		printImage.setBookmarkLevel(getBookmarkLevel());
		printImage.setHyperlinkParameters(hyperlinkParameters);
		transferProperties(printImage);
	}

	public byte getChartType()
	{
		return chartType;
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

	
	/**
	 *
	 */
	protected ChartHyperlinkProvider createChartHyperlinkProvider()
	{
		ChartHyperlinkProvider chartHyperlinkProvider = null;
		
		switch(getChartType()) 
		{
			case JRChart.CHART_TYPE_AREA:
			case JRChart.CHART_TYPE_BAR:
			case JRChart.CHART_TYPE_BAR3D:
			case JRChart.CHART_TYPE_LINE:
			case JRChart.CHART_TYPE_STACKEDBAR3D:
			case JRChart.CHART_TYPE_STACKEDBAR:
			case JRChart.CHART_TYPE_STACKEDAREA:
				chartHyperlinkProvider = new CategoryChartHyperlinkProvider(((JRFillCategoryDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				chartHyperlinkProvider = new XYChartHyperlinkProvider(((JRFillXyzDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_SCATTER:
			case JRChart.CHART_TYPE_XYAREA:
			case JRChart.CHART_TYPE_XYLINE:
				chartHyperlinkProvider = new XYChartHyperlinkProvider(((JRFillXyDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
			case JRChart.CHART_TYPE_HIGHLOW:
				chartHyperlinkProvider = new HighLowChartHyperlinkProvider(((JRFillHighLowDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				//multi-axis charts are dealt with in createMultiAxisChart
				break;
			case JRChart.CHART_TYPE_PIE:
			case JRChart.CHART_TYPE_PIE3D:
				chartHyperlinkProvider = new PieChartHyperlinkProvider(((JRFillPieDataset)getDataset()).getSectionHyperlinks());
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				chartHyperlinkProvider = new TimeSeriesChartHyperlinkProvider(((JRFillTimeSeriesDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_XYBAR:
				switch(getDataset().getDatasetType())
				{
					case JRChartDataset.TIMESERIES_DATASET :
						chartHyperlinkProvider = new TimeSeriesChartHyperlinkProvider(((JRFillTimeSeriesDataset)getDataset()).getItemHyperlinks());
						break;
					case JRChartDataset.TIMEPERIOD_DATASET :
						chartHyperlinkProvider = new TimePeriodChartHyperlinkProvider(((JRFillTimePeriodDataset)getDataset()).getItemHyperlinks());
						break;
					case JRChartDataset.XY_DATASET :
						chartHyperlinkProvider = new XYChartHyperlinkProvider(((JRFillXyDataset)getDataset()).getItemHyperlinks());
						break;
					default:
						break;
				}
				break;
			case JRChart.CHART_TYPE_GANTT:
				chartHyperlinkProvider = new CategoryChartHyperlinkProvider(((JRFillGanttDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_METER:
			case JRChart.CHART_TYPE_THERMOMETER:
				//no item hyperlinks
				break;
			default:
				throw new JRRuntimeException("Chart type " + getChartType() + " not supported.");
		}

		return chartHyperlinkProvider;
	}



	/**
	 * Build and configure a multiple axis chart.  A multiple axis chart support more than
	 * one range axis.  Multiple datasets using different ranges can be displayed as long as
	 * they share a common domain axis.  Each dataset can be rendered differently, so one chart
	 * can contain (for example) two line charts, a bar chart and an area chart.
	 * <br><br>
	 * Multiple axis charts are handled differently than the other chart types.  They do not
	 * have a dataset, as each chart that is added to the multiple axis chart has its own
	 * dataset.  For simplicity, each dataset is treated as its own chart, and in fact we
	 * reuse the design of all the chart types and let JFreeChart actually run them.  Then
	 * we pull out the bits we need and add it to the common chart.  All the plot and chart
	 * options on the nested charts is ignored, and all formatting is controlled by the plot
	 * attached to the multiAxisChart.  The one exception is seriesColor, which can be used in
	 * a nested report to specify a color for a specific series in that report.
	 *
	 * @param evaluation current expression evaluation phase
	 * @throws JRException
	 */
	protected void createMultiAxisChart(byte evaluation) throws JRException
	{
		// A multi axis chart has to have at least one axis and chart specified.
		// Create the first axis as the base plot, and then go ahead and create the
		// charts for any additional axes.  Just take the renderer and data series
		// from those charts and add them to the first one.
		Plot mainPlot = null;

		JRFillMultiAxisPlot jrPlot = (JRFillMultiAxisPlot)getPlot();
		
		// create a multi axis hyperlink provider
		MultiAxisChartHyperlinkProvider multiHyperlinkProvider = new MultiAxisChartHyperlinkProvider();
		
		// Generate the main plot from the first axes specified.
		Iterator iter = jrPlot.getAxes().iterator();
		if (iter.hasNext())
		{
			JRFillChartAxis axis = (JRFillChartAxis)iter.next();
			JRFillChart fillChart = axis.getFillChart();
			
			//a JFreeChart object should be obtained first; the rendering type should be always "vector"
			

			jfreeChart = fillChart.evaluateChart(evaluation);
			// Override the plot from the first axis with the plot for the multi-axis
			// chart.
			//FIXME is the above comment true? 
			//configureChart(jfreeChart, getPlot(), evaluation);
			mainPlot = jfreeChart.getPlot();
			ChartHyperlinkProvider axisHyperlinkProvider = fillChart.getHyperlinkProvider();

			if (mainPlot instanceof CategoryPlot)
			{
				CategoryPlot categoryPlot = (CategoryPlot) mainPlot;
//				categoryPlot.setRangeAxisLocation(0, getChartAxisLocation(axis));
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(categoryPlot.getDataset(), 
							axisHyperlinkProvider);
				}
			}
			else if (mainPlot instanceof XYPlot)
			{
				XYPlot xyPlot = (XYPlot) mainPlot;
//				xyPlot.setRangeAxisLocation(0, getChartAxisLocation(axis));
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(xyPlot.getDataset(), axisHyperlinkProvider);
				}
			}
		 }

		// Now handle all the extra axes, if any.
		int axisNumber = 0;
		while (iter.hasNext())
		{
			axisNumber++;
			JRFillChartAxis chartAxis = (JRFillChartAxis)iter.next();
			JRFillChart fillChart = chartAxis.getFillChart();
			JFreeChart axisChart = fillChart.evaluateChart(evaluation);
			ChartHyperlinkProvider axisHyperlinkProvider = fillChart.getHyperlinkProvider();

			// In JFreeChart to add a second chart type to an existing chart
			// you need to add an axis, a data series and a renderer.  To
			// leverage existing code we simply create a new chart for the
			// axis and then pull out the bits we need and add them to the multi
			// chart.  Currently JFree only supports category plots and xy plots
			// in a multi-axis chart, and you can not mix the two.
			if (mainPlot instanceof CategoryPlot)
			{
				CategoryPlot mainCatPlot = (CategoryPlot)mainPlot;
				if (!(axisChart.getPlot() instanceof CategoryPlot))
				{
					throw new JRException("You can not mix plot types in a MultiAxisChart");
				}

				// Get the axis and add it to the multi axis chart plot
				CategoryPlot axisPlot = (CategoryPlot)axisChart.getPlot();
				mainCatPlot.setRangeAxis(axisNumber, axisPlot.getRangeAxis());
//				mainCatPlot.setRangeAxisLocation(axisNumber, getChartAxisLocation(chartAxis));

				// Add the data set and map it to the recently added axis
				mainCatPlot.setDataset(axisNumber, axisPlot.getDataset());
				mainCatPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

				// Set the renderer to use to draw the dataset.
				mainCatPlot.setRenderer(axisNumber, axisPlot.getRenderer());

				// Handle any color series for this chart
				configureAxisSeriesColors(axisPlot.getRenderer(), fillChart.getPlot());
				
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(axisPlot.getDataset(), 
							axisHyperlinkProvider);
				}
			}
			else if (mainPlot instanceof XYPlot)
			{
				XYPlot mainXyPlot = (XYPlot)mainPlot;
				if (!(axisChart.getPlot() instanceof XYPlot))
				{
					throw new JRException("You can not mix plot types in a MultiAxisChart");
				}

				// Get the axis and add it to the multi axis chart plot
				XYPlot axisPlot = (XYPlot)axisChart.getPlot();
				mainXyPlot.setRangeAxis(axisNumber, axisPlot.getRangeAxis());
//				mainXyPlot.setRangeAxisLocation(axisNumber, getChartAxisLocation(chartAxis));

				// Add the data set and map it to the recently added axis
				mainXyPlot.setDataset(axisNumber, axisPlot.getDataset());
				mainXyPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

				// Set the renderer to use to draw the dataset.
				mainXyPlot.setRenderer(axisNumber, axisPlot.getRenderer());

				// Handle any color series for this chart
				configureAxisSeriesColors(axisPlot.getRenderer(), fillChart.getPlot());
				
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(axisPlot.getDataset(), 
							axisHyperlinkProvider);
				}
			}
			else
			{
				throw new JRException("MultiAxis charts only support Category and XY plots.");
			}
		}

		//set the multi hyperlink provider
		chartHyperlinkProvider = multiHyperlinkProvider;
	}


	/**
	 * The series colors set in the main plot of a multiple axis chart are used for
	 * all the rendered charts in the plot.  This is a problem with multiple line
	 * charts, using different scales and thus different axis.  All the lines will
	 * be drawn using the first series color (since they are the first series for that
	 * rendered) and it will be impossible to tell them apart.
	 * <br><br>
	 * For this reason we interpret series colors for charts included in a multiple
	 * axis chart as specify absolute series colors for that renderer.
	 *
	 * @param renderer the renderer of the chart being created
	 * @param jrPlot the Jasper view of that plot
	 */
	private void configureAxisSeriesColors(CategoryItemRenderer renderer, JRChartPlot jrPlot)
	{
		SortedSet seriesColors = jrPlot.getSeriesColors();

		if (seriesColors != null)
		{
			Iterator iter = seriesColors.iterator();
			while (iter.hasNext())
			{
				JRSeriesColor seriesColor = (JRSeriesColor)iter.next();
				renderer.setSeriesPaint(seriesColor.getSeriesOrder(), seriesColor.getColor());
			}
		}
	}

	/**
	 * The series colors set in the main plot of a multiple axis chart are used for
	 * all the rendered charts in the plot.  This is a problem with multiple line
	 * charts, using different scales and thus different axis.  All the lines will
	 * be drawn using the first series color (since they are the first series for that
	 * rendered) and it will be impossible to tell them apart.
	 * <br>
	 * For this reason we interpret series colors for charts included in a multiple
	 * axis chart as specify absolute series colors for that renderer.
	 *
	 * @param renderer the renderer of the chart being created
	 * @param jrPlot the Jasper view of that plot
	 */
	private void configureAxisSeriesColors(XYItemRenderer renderer, JRChartPlot jrPlot)
	{
		SortedSet seriesColors = jrPlot.getSeriesColors();

		if (seriesColors != null)
		{
			Iterator iter = seriesColors.iterator();
			while (iter.hasNext())
			{
				JRSeriesColor seriesColor = (JRSeriesColor)iter.next();
				renderer.setSeriesPaint(seriesColor.getSeriesOrder(), seriesColor.getColor());
			}
		}
	}

	/**
	 * Converts a JasperReport data range into one understood by JFreeChart.
	 *
	 * @param dataRange the JasperReport version of the range
	 * @param evaluation current expression evaluation phase
	 * @return the JFreeChart version of the range
	 * @throws JRException thrown when the low value of the range is greater than the
	 * 						high value
	 */
	protected Range convertRange(JRDataRange dataRange, byte evaluation) throws JRException
	{
		if (dataRange == null)
			return null;

		Number low = (Number)evaluateExpression(dataRange.getLowExpression(), evaluation);
		Number high = (Number)evaluateExpression(dataRange.getHighExpression(), evaluation);
		return new Range( low != null ? low.doubleValue() : 0.0,
								 high != null ? high.doubleValue() : 100.0);
	}

	/**
	 * Converts a JasperReports meter interval to one that JFreeChart understands.
	 *
	 * @param interval the JasperReports definition of an interval
	 * @param evaluation current evaluation time
	 * @return the JFreeChart version of the same interval
	 * @throws JRException thrown when the interval contains an invalid range
	 */
	protected MeterInterval convertInterval(JRMeterInterval interval, byte evaluation) throws JRException
	{
		String label = interval.getLabel();
		if (label == null)
			label = "";

		Range range = convertRange(interval.getDataRange(), evaluation);

		Color color = interval.getBackgroundColor();
		float[] components = color.getRGBColorComponents(null);
		float alpha = interval.getAlphaDouble() == null ? (float)JRMeterInterval.DEFAULT_TRANSPARENCY : interval.getAlphaDouble().floatValue();

		Color alphaColor = new Color(components[0], components[1], components[2], alpha);

		return new MeterInterval(label, range, alphaColor, null, alphaColor);
	}

	protected AxisLocation getChartAxisLocation(JRFillChartAxis chartAxis)
	{
		return chartAxis.getPositionByte() != null && chartAxis.getPositionByte().byteValue() == JRChartAxis.POSITION_RIGHT_OR_BOTTOM
				? AxisLocation.BOTTOM_OR_RIGHT 
				: AxisLocation.TOP_OR_LEFT;
	}
	
	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateRenderer(evaluation);

		copy((JRPrintImage) element);
	}


	public int getBookmarkLevel()
	{
		return ((JRChart)parent).getBookmarkLevel();
	}

	/**
	 *
	 */
	public String getCustomizerClass()
	{
		return customizerClass;
	}


	private void evaluateDatasetRun(byte evaluation) throws JRException
	{
		dataset.evaluateDatasetRun(evaluation);
	}


	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		//not needed
		return null;
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return ((JRChart) parent).getHyperlinkParameters();
	}


	public String getLinkType()
	{
		return ((JRChart) parent).getLinkType();
	}


	public JRExpression getHyperlinkTooltipExpression()
	{
		return ((JRChart) parent).getHyperlinkTooltipExpression();
	}

	
}
