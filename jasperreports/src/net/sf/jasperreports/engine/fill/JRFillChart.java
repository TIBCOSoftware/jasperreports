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
import java.awt.Font;
import java.awt.Paint;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRDataRange;
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
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.fill.JRFillAreaPlot;
import net.sf.jasperreports.charts.fill.JRFillBar3DPlot;
import net.sf.jasperreports.charts.fill.JRFillBarPlot;
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillChartAxis;
import net.sf.jasperreports.charts.fill.JRFillHighLowDataset;
import net.sf.jasperreports.charts.fill.JRFillLinePlot;
import net.sf.jasperreports.charts.fill.JRFillMeterPlot;
import net.sf.jasperreports.charts.fill.JRFillMultiAxisPlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.charts.fill.JRFillThermometerPlot;
import net.sf.jasperreports.charts.fill.JRFillTimePeriodDataset;
import net.sf.jasperreports.charts.fill.JRFillTimeSeriesDataset;
import net.sf.jasperreports.charts.fill.JRFillXyDataset;
import net.sf.jasperreports.charts.fill.JRFillXyzDataset;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRElement;
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
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRPenUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.LineBoxWrapper;
import net.sf.jasperreports.renderers.JFreeChartRenderer;
import net.sf.jasperreports.renderers.JRCategoryChartImageMapRenderer;
import net.sf.jasperreports.renderers.JRHighLowChartImageMapRenderer;
import net.sf.jasperreports.renderers.JRPieChartImageMapRenderer;
import net.sf.jasperreports.renderers.JRTimePeriodChartImageMapRenderer;
import net.sf.jasperreports.renderers.JRTimeSeriesChartImageMapRenderer;
import net.sf.jasperreports.renderers.JRXYChartImageMapRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;


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
	private static final Color TRANSPARENT_PAINT = new Color(0, 0, 0, 0);

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
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}

		titleFont = new JRBaseFont(null, null, chart, chart.getTitleFont());
		subtitleFont = new JRBaseFont(null, null, chart, chart.getSubtitleFont());
		legendFont = new JRBaseFont(null, null, chart, chart.getLegendFont());

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
	public boolean isShowLegend()
	{
		return ((JRChart)parent).isShowLegend();
	}

	/**
	 *
	 */
	public void setShowLegend(boolean isShowLegend)
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
		return ((JRBoxContainer)parent).getLineBox();
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
	 *
	 */
	public byte getTitlePosition()
	{
		return ((JRChart)parent).getTitlePosition();
	}

	/**
	 *
	 */
	public void setTitlePosition(byte titlePosition)
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
	 *
	 */
	public byte getLegendPosition()
	{
		return ((JRChart)parent).getLegendPosition();
	}

	/**
	 *
	 */
	public void setLegendPosition(byte legendPosition)
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
			template = new JRTemplateImage(band.getOrigin(), filler.getJasperPrint().getDefaultStyleProvider(), this);
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
				evaluateImage(evaluation);
			}
		}
	}


	/**
	 *
	 */
	protected JFreeChartRenderer evaluateImage(byte evaluation) throws JRException
	{
		evaluateDatasetRun(evaluation);

		JFreeChartRenderer chartRenderer;
		switch(chartType) {
			case CHART_TYPE_AREA:
				chartRenderer = evaluateAreaImage(evaluation);
				break;
			case CHART_TYPE_BAR:
				chartRenderer = evaluateBarImage(evaluation);
				break;
			case CHART_TYPE_BAR3D:
				chartRenderer = evaluateBar3DImage(evaluation);
				break;
			case CHART_TYPE_BUBBLE:
				chartRenderer = evaluateBubbleImage(evaluation);
				break;
			case CHART_TYPE_CANDLESTICK:
				chartRenderer = evaluateCandlestickImage(evaluation);
				break;
			case CHART_TYPE_HIGHLOW:
				chartRenderer = evaluateHighLowImage(evaluation);
				break;
			case CHART_TYPE_LINE:
				chartRenderer = evaluateLineImage(evaluation);
				break;
			case CHART_TYPE_METER:
				chartRenderer = evaluateMeterImage(evaluation);
				break;
			case CHART_TYPE_MULTI_AXIS:
				chartRenderer = evaluateMultiAxisImage(evaluation);
				break;
			case CHART_TYPE_PIE:
				chartRenderer = evaluatePieImage(evaluation);
				break;
			case CHART_TYPE_PIE3D:
				chartRenderer = evaluatePie3DImage(evaluation);
				break;
			case CHART_TYPE_SCATTER:
				chartRenderer = evaluateScatterImage(evaluation);
				break;
			case CHART_TYPE_STACKEDBAR:
				chartRenderer = evaluateStackedBarImage(evaluation);
				break;
			case CHART_TYPE_STACKEDBAR3D:
				chartRenderer = evaluateStackedBar3DImage(evaluation);
				break;
			case CHART_TYPE_THERMOMETER:
				chartRenderer = evaluateThermometerImage(evaluation);
				break;
			case CHART_TYPE_TIMESERIES:
				chartRenderer = evaluateTimeSeriesImage( evaluation );
				break;
			case CHART_TYPE_XYAREA:
				chartRenderer = evaluateXyAreaImage(evaluation);
				break;
			case CHART_TYPE_XYBAR:
				chartRenderer = evaluateXYBarImage(evaluation);
				break;
			case CHART_TYPE_XYLINE:
				chartRenderer = evaluateXyLineImage(evaluation);
				break;
			case CHART_TYPE_STACKEDAREA:
				chartRenderer = evaluateStackedAreaImage(evaluation);
				break;
			default:
				throw new JRRuntimeException("Chart type " + getChartType() + " not supported.");
		}

		if (chartCustomizer != null)
		{
			chartCustomizer.customize(chartRenderer.getChart(), this);
		}

		renderer = chartRenderer;

		anchorName = (String) evaluateExpression(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) evaluateExpression(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String) evaluateExpression(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) evaluateExpression(getHyperlinkPageExpression(), evaluation);
		hyperlinkTooltip = (String) evaluateExpression(getHyperlinkTooltipExpression(), evaluation);
		hyperlinkParameters = JRFillHyperlinkHelper.evaluateHyperlinkParameters(this, expressionEvaluator, evaluation);
		return chartRenderer;
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
	private void configureChart(JFreeChart chart, JRChartPlot jrPlot, byte evaluation) throws JRException
	{
		if (getMode() == JRElement.MODE_OPAQUE)
		{
			chart.setBackgroundPaint(getBackcolor());
		}
		else
		{
			chart.setBackgroundPaint(TRANSPARENT_PAINT);
		}

		RectangleEdge titleEdge = getEdge(getTitlePosition());
		
		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());

			title.setFont(new Font(JRFontUtil.getAttributes(getTitleFont())));
			title.setPosition(titleEdge);
		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());

			subtitle.setFont(new Font(JRFontUtil.getAttributes(getSubtitleFont())));
			subtitle.setPosition(titleEdge);

			chart.addSubtitle(subtitle);
		}

		// Apply all of the legend formatting options
		LegendTitle legend = chart.getLegend();
		if (legend != null)
		{
			legend.setItemPaint(getLegendColor());

			if (getOwnLegendBackgroundColor() == null)// in a way, legend backcolor inheritence from chart is useless
			{
				legend.setBackgroundPaint(TRANSPARENT_PAINT);
			}
			else
			{
				legend.setBackgroundPaint(getLegendBackgroundColor());
			}

			chart.getLegend().setItemFont(new Font(JRFontUtil.getAttributes(getLegendFont())));//FIXME put these in JRFontUtil
			chart.getLegend().setPosition(getEdge(getLegendPosition()));
		}

		configurePlot(chart.getPlot(), jrPlot);
	}


	/**
	 *
	 */
	private void configurePlot(Plot p, JRChartPlot jrPlot)
	{
		p.setOutlinePaint(TRANSPARENT_PAINT);

		if (getPlot().getOwnBackcolor() == null)// in a way, plot backcolor inheritence from chart is useless
		{
			p.setBackgroundPaint(TRANSPARENT_PAINT);
		}
		else
		{
			p.setBackgroundPaint(getPlot().getBackcolor());
		}

		p.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		p.setForegroundAlpha(getPlot().getForegroundAlpha());

		SortedSet seriesColors = getPlot().getSeriesColors();
		if (p instanceof CategoryPlot)
		{
			// Handle rotation of the category labels.
			CategoryAxis axis = ((CategoryPlot)p).getDomainAxis();
			double labelRotation = getPlot().getLabelRotation();
			if (labelRotation == 90)
			{
				axis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
			}
			else if (labelRotation == -90) {
				axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
			}
			else if (labelRotation < 0)
			{
				axis.setCategoryLabelPositions(
						CategoryLabelPositions.createUpRotationLabelPositions( (-labelRotation / 180.0) * Math.PI));
			}
			else if (labelRotation > 0)
			{
				axis.setCategoryLabelPositions(
						CategoryLabelPositions.createDownRotationLabelPositions((labelRotation / 180.0) * Math.PI));
			}
		}


		// Set any color series
		if (seriesColors != null)
		{
			if (seriesColors.size() == 1)
			{
				// Add the single color to the beginning of the color cycle, using all the default
				// colors.  To replace the defaults you have to specify at least two colors.
				Paint[] colors = new Paint[DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length + 1];
				colors[0] = ((JRSeriesColor)seriesColors.first()).getColor();
				for (int i = 0; i < DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length; i++)
				{
					colors[i + 1] = DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[i];
				}

				p.setDrawingSupplier(new DefaultDrawingSupplier(colors,
						DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
						DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
						DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
						DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
			}
			else if (seriesColors.size() > 1)
			{
				// Set up a custom drawing supplier that cycles through the user's colors
				// instead of the default colors.
				Color[] colors = new Color[seriesColors.size()];
				JRSeriesColor[] colorSequence = new JRSeriesColor[seriesColors.size()];
				seriesColors.toArray(colorSequence);
				for (int i = 0; i < colorSequence.length; i++)
				{
					colors[i] = colorSequence[i].getColor();
				}

				p.setDrawingSupplier(new DefaultDrawingSupplier(colors,
											DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
											DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
											DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
											DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
			}
		}
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
	 * Sets all the axis formatting options.  This includes the colors and fonts to use on
	 * the axis as well as the color to use when drawing the axis line.
	 *
	 * @param axis the axis to format
	 * @param labelFont the font to use for the axis label
	 * @param labelColor the color of the axis label
	 * @param tickLabelFont the font to use for each tick mark value label
	 * @param tickLabelColor the color of each tick mark value label
	 * @param tickLabelMask formatting mask for the label.  If the axis is a NumberAxis then
	 * 					    the mask should be <code>java.text.DecimalFormat</code> mask, and
	 * 						if it is a DateAxis then the mask should be a
	 * 						<code>java.text.SimpleDateFormat</code> mask.
	 * @param lineColor color to use when drawing the axis line and any tick marks
	 */
	private void configureAxis(
		Axis axis,
		JRFont labelFont,
		Color labelColor,
		JRFont tickLabelFont,
		Color tickLabelColor,
		String tickLabelMask,
		Color lineColor
		)
	{
		axis.setLabelFont(new Font(JRFontUtil.getAttributes(labelFont)));
		axis.setTickLabelFont(new Font(JRFontUtil.getAttributes(tickLabelFont)));

		if (labelColor != null)
		{
			axis.setLabelPaint(labelColor);
		}

		if (tickLabelColor != null)
		{
			axis.setTickLabelPaint(tickLabelColor);
		}

		if (lineColor != null)
		{
			axis.setAxisLinePaint(lineColor);
			axis.setTickMarkPaint(lineColor);
		}

		if (tickLabelMask != null)
		{
			if (axis instanceof NumberAxis)
			{
				NumberFormat fmt = NumberFormat.getInstance();
				if (fmt instanceof DecimalFormat)
					((DecimalFormat) fmt).applyPattern(tickLabelMask);
				((NumberAxis)axis).setNumberFormatOverride(fmt);
			}
			else if (axis instanceof DateAxis)
			{
				DateFormat fmt = null;
				if (tickLabelMask.equals("SHORT") || tickLabelMask.equals("DateFormat.SHORT"))
					fmt = DateFormat.getDateInstance(DateFormat.SHORT);
				else if (tickLabelMask.equals("MEDIUM") || tickLabelMask.equals("DateFormat.MEDIUM"))
					fmt = DateFormat.getDateInstance(DateFormat.MEDIUM);
				else if (tickLabelMask.equals("LONG") || tickLabelMask.equals("DateFormat.LONG"))
					fmt = DateFormat.getDateInstance(DateFormat.LONG);
				else if (tickLabelMask.equals("FULL") || tickLabelMask.equals("DateFormat.FULL"))
					fmt = DateFormat.getDateInstance(DateFormat.FULL);
				else
					fmt = new SimpleDateFormat(tickLabelMask);

				((DateAxis)axis).setDateFormatOverride(fmt);
			}
			// ignore mask for other axis types.
		}
	}
	/**
	 *
	 */
	protected JFreeChartRenderer evaluateAreaImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createAreaChart( (String)evaluateExpression(getTitleExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)dataset.getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, getPlot(), evaluation);
		JRFillAreaPlot areaPlot = (JRFillAreaPlot)getPlot();

		// Handle the axis formating for the catagory axis
		configureAxis(((CategoryPlot)chart.getPlot()).getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(((CategoryPlot)chart.getPlot()).getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getValueAxisLineColor());

		return getCategoryRenderer(chart);
	}


	protected JFreeChartRenderer evaluateBar3DImage( byte evaluation ) throws JRException {
		JFreeChart chart =
			ChartFactory.createBarChart3D(
					(String)evaluateExpression( getTitleExpression(), evaluation ),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression(), evaluation ),
					(CategoryDataset)dataset.getDataset(),
					getPlot().getOrientation(),
					isShowLegend(),
					true,
					false );

		configureChart(chart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		JRFillBar3DPlot bar3DPlot = (JRFillBar3DPlot)getPlot();

		BarRenderer3D barRenderer3D =
			new BarRenderer3D(
				bar3DPlot.getXOffset(),
				bar3DPlot.getYOffset()
				);
		categoryPlot.setRenderer(barRenderer3D);

		barRenderer3D.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		barRenderer3D.setItemLabelsVisible( bar3DPlot.isShowLabels() );

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), bar3DPlot.getCategoryAxisLabelFont(),
				bar3DPlot.getCategoryAxisLabelColor(), bar3DPlot.getCategoryAxisTickLabelFont(),
				bar3DPlot.getCategoryAxisTickLabelColor(), bar3DPlot.getCategoryAxisTickLabelMask(),
				bar3DPlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), bar3DPlot.getValueAxisLabelFont(),
				bar3DPlot.getValueAxisLabelColor(), bar3DPlot.getValueAxisTickLabelFont(),
				bar3DPlot.getValueAxisTickLabelColor(), bar3DPlot.getValueAxisTickLabelMask(),
				bar3DPlot.getValueAxisLineColor());

		return getCategoryRenderer(chart);
	}


	/**
	 *
	 */
	protected JFreeChartRenderer evaluateBarImage(byte evaluation) throws JRException
	{
		CategoryDataset categoryDataset = (CategoryDataset)dataset.getDataset();
		JFreeChart chart =
			ChartFactory.createBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				categoryDataset,
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");

		JRFillBarPlot barPlot = (JRFillBarPlot)getPlot();
		categoryPlot.getDomainAxis().setTickMarksVisible(
			barPlot.isShowTickMarks()
			);
		categoryPlot.getDomainAxis().setTickLabelsVisible(
				barPlot.isShowTickLabels()
				);
		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getCategoryAxisLineColor());

		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(
				barPlot.isShowTickMarks()
				);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(
				barPlot.isShowTickLabels()
				);
		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getValueAxisLineColor());


		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		categoryRenderer.setItemLabelsVisible( barPlot.isShowLabels() );

		return getCategoryRenderer(chart);

	}


	protected JFreeChartRenderer evaluateBubbleImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createBubbleChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getXAxisLabelExpression(), evaluation ),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getYAxisLabelExpression(), evaluation ),
				 (XYZDataset)dataset.getDataset(),
				 getPlot().getOrientation(),
				 isShowLegend(),
				 true,
				 false);

		configureChart(chart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot)chart.getPlot();
		JRBubblePlot bubblePlot = (JRBubblePlot)getPlot();

		XYBubbleRenderer bubbleRenderer = new XYBubbleRenderer( bubblePlot.getScaleType() );
		xyPlot.setRenderer( bubbleRenderer );

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), bubblePlot.getXAxisLabelFont(),
				bubblePlot.getXAxisLabelColor(), bubblePlot.getXAxisTickLabelFont(),
				bubblePlot.getXAxisTickLabelColor(), bubblePlot.getXAxisTickLabelMask(),
				bubblePlot.getXAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), bubblePlot.getYAxisLabelFont(),
				bubblePlot.getYAxisLabelColor(), bubblePlot.getYAxisTickLabelFont(),
				bubblePlot.getYAxisTickLabelColor(), bubblePlot.getYAxisTickLabelMask(),
				bubblePlot.getYAxisLineColor());

		return getXYZRenderer(chart);
	}


	/**
	 *
	 * @param evaluation
	 * @throws net.sf.jasperreports.engine.JRException
	 */
	protected JFreeChartRenderer evaluateCandlestickImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createCandlestickChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)dataset.getDataset(),
				isShowLegend()
				);

		configureChart(chart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		JRCandlestickPlot candlestickPlot = (JRCandlestickPlot)getPlot();
		CandlestickRenderer candlestickRenderer = (CandlestickRenderer) xyPlot.getRenderer();
		candlestickRenderer.setDrawVolume(candlestickPlot.isShowVolume());

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), candlestickPlot.getTimeAxisLabelFont(),
				candlestickPlot.getTimeAxisLabelColor(), candlestickPlot.getTimeAxisTickLabelFont(),
				candlestickPlot.getTimeAxisTickLabelColor(), candlestickPlot.getTimeAxisTickLabelMask(),
				candlestickPlot.getTimeAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), candlestickPlot.getValueAxisLabelFont(),
				candlestickPlot.getValueAxisLabelColor(), candlestickPlot.getValueAxisTickLabelFont(),
				candlestickPlot.getValueAxisTickLabelColor(), candlestickPlot.getValueAxisTickLabelMask(),
				candlestickPlot.getValueAxisLineColor());

		return getHighLowRenderer(chart);
	}


	/**
	 *
	 * @param evaluation
	 * @throws JRException
	 */
	protected JFreeChartRenderer evaluateHighLowImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createHighLowChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)dataset.getDataset(),
				isShowLegend()
				);

		configureChart(chart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		JRHighLowPlot highLowPlot = (JRHighLowPlot)getPlot();
		HighLowRenderer hlRenderer = (HighLowRenderer) xyPlot.getRenderer();
		hlRenderer.setDrawOpenTicks(highLowPlot.isShowOpenTicks());
		hlRenderer.setDrawCloseTicks(highLowPlot.isShowCloseTicks());

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), highLowPlot.getTimeAxisLabelFont(),
				highLowPlot.getTimeAxisLabelColor(), highLowPlot.getTimeAxisTickLabelFont(),
				highLowPlot.getTimeAxisTickLabelColor(), highLowPlot.getTimeAxisTickLabelMask(),
				highLowPlot.getTimeAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), highLowPlot.getValueAxisLabelFont(),
				highLowPlot.getValueAxisLabelColor(), highLowPlot.getValueAxisTickLabelFont(),
				highLowPlot.getValueAxisTickLabelColor(), highLowPlot.getValueAxisTickLabelMask(),
				highLowPlot.getValueAxisLineColor());

		return getHighLowRenderer(chart);
	}


	protected JFreeChartRenderer evaluateLineImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createLineChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRLinePlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRLinePlot)getPlot()).getValueAxisLabelExpression(), evaluation ),
				(CategoryDataset)dataset.getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		JRFillLinePlot linePlot = (JRFillLinePlot)getPlot();

		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)categoryPlot.getRenderer();
		lineRenderer.setShapesVisible( linePlot.isShowShapes() );//FIXMECHART check this
		lineRenderer.setLinesVisible( linePlot.isShowLines() );

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), linePlot.getCategoryAxisLabelFont(),
				linePlot.getCategoryAxisLabelColor(), linePlot.getCategoryAxisTickLabelFont(),
				linePlot.getCategoryAxisTickLabelColor(), linePlot.getCategoryAxisTickLabelMask(),
				linePlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), linePlot.getValueAxisLabelFont(),
				linePlot.getValueAxisLabelColor(), linePlot.getValueAxisTickLabelFont(),
				linePlot.getValueAxisTickLabelColor(), linePlot.getValueAxisTickLabelMask(),
				linePlot.getValueAxisLineColor());

		return getCategoryRenderer(chart);
	}


	/**
	 *
	 */
	protected JFreeChartRenderer evaluatePie3DImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createPieChart3D(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(PieDataset)dataset.getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, getPlot(), evaluation);

		PiePlot3D piePlot3D = (PiePlot3D) chart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		piePlot3D.setDepthFactor(((JRFillPie3DPlot)getPlot()).getDepthFactor());
		piePlot3D.setCircular(((JRFillPie3DPlot)getPlot()).isCircular());

		PieSectionLabelGenerator labelGenerator = ((JRFillPieDataset)getDataset()).getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot3D.setLabelGenerator(labelGenerator);
		}

		//FIXMECHART at this moment, there are no label font, label backcolor
		// and label forecolor properties defined for the PieChart3D

		piePlot3D.setLabelFont(new Font(JRFontUtil.getAttributes(new JRBaseFont(null, null, this, null))));

		piePlot3D.setLabelPaint(getForecolor());

		return getPieRenderer(chart);
	}


	/**
	 *
	 */
	protected JFreeChartRenderer evaluatePieImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createPieChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(PieDataset)dataset.getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, getPlot(), evaluation);
		PiePlot piePlot = (PiePlot)chart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		piePlot.setCircular(((JRFillPiePlot)getPlot()).isCircular());

		PieSectionLabelGenerator labelGenerator = ((JRFillPieDataset)getDataset()).getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot.setLabelGenerator(labelGenerator);
		}

		//FIXMECHART at this moment, there are no label font, label backcolor
		// and label forecolor properties defined for the PieChart

		piePlot.setLabelFont(new Font(JRFontUtil.getAttributes(new JRBaseFont(null, null, this, null))));

		piePlot.setLabelPaint(getForecolor());

		return getPieRenderer(chart);
	}


	protected JFreeChartRenderer evaluateScatterImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createScatterPlot(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRScatterPlot)getPlot()).getXAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRScatterPlot)getPlot()).getYAxisLabelExpression(), evaluation ),
				(XYDataset)dataset.getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, getPlot(), evaluation);
		XYLineAndShapeRenderer plotRenderer = (XYLineAndShapeRenderer) ((XYPlot)chart.getPlot()).getRenderer();

		JRScatterPlot scatterPlot = (JRScatterPlot) getPlot();
		plotRenderer.setLinesVisible(scatterPlot.isShowLines());
		plotRenderer.setShapesVisible(scatterPlot.isShowShapes());

		// Handle the axis formating for the catagory axis
		configureAxis(chart.getXYPlot().getDomainAxis(), scatterPlot.getXAxisLabelFont(),
				scatterPlot.getXAxisLabelColor(), scatterPlot.getXAxisTickLabelFont(),
				scatterPlot.getXAxisTickLabelColor(), scatterPlot.getXAxisTickLabelMask(),
				scatterPlot.getXAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(chart.getXYPlot().getRangeAxis(), scatterPlot.getYAxisLabelFont(),
				scatterPlot.getYAxisLabelColor(), scatterPlot.getYAxisTickLabelFont(),
				scatterPlot.getYAxisTickLabelColor(), scatterPlot.getYAxisTickLabelMask(),
				scatterPlot.getYAxisLineColor());

		return getXYRenderer(chart);
	}


	/**
	 *
	 */
	protected JFreeChartRenderer evaluateStackedBar3DImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createStackedBarChart3D(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)dataset.getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		JRFillBar3DPlot bar3DPlot = (JRFillBar3DPlot)getPlot();

		StackedBarRenderer3D stackedBarRenderer3D =
			new StackedBarRenderer3D(
				bar3DPlot.getXOffset(),
				bar3DPlot.getYOffset()
				);
		categoryPlot.setRenderer(stackedBarRenderer3D);

		stackedBarRenderer3D.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		stackedBarRenderer3D.setItemLabelsVisible( bar3DPlot.isShowLabels() );

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), bar3DPlot.getCategoryAxisLabelFont(),
				bar3DPlot.getCategoryAxisLabelColor(), bar3DPlot.getCategoryAxisTickLabelFont(),
				bar3DPlot.getCategoryAxisTickLabelColor(), bar3DPlot.getCategoryAxisTickLabelMask(),
				bar3DPlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), bar3DPlot.getValueAxisLabelFont(),
				bar3DPlot.getValueAxisLabelColor(), bar3DPlot.getValueAxisTickLabelFont(),
				bar3DPlot.getValueAxisTickLabelColor(), bar3DPlot.getValueAxisTickLabelMask(),
				bar3DPlot.getValueAxisLineColor());

		return getCategoryRenderer(chart);
	}


	/**
	 *
	 */
	protected JFreeChartRenderer evaluateStackedBarImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createStackedBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)dataset.getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		JRFillBarPlot barPlot = (JRFillBarPlot)getPlot();
		//plot.setNoDataMessage("No data to display");

		categoryPlot.getDomainAxis().setTickMarksVisible(
				barPlot.isShowTickMarks()
				);
		categoryPlot.getDomainAxis().setTickLabelsVisible(
				barPlot.isShowTickLabels()
				);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(
				barPlot.isShowTickMarks()
				);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(
				barPlot.isShowTickLabels()
				);

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		categoryRenderer.setItemLabelsVisible( ((JRFillBarPlot)getPlot()).isShowLabels() );

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getValueAxisLineColor());

		return getCategoryRenderer(chart);
	}

	/**
	 *
	 */
	protected JFreeChartRenderer evaluateStackedAreaImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createStackedAreaChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)dataset.getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, getPlot(), evaluation);
		JRFillAreaPlot areaPlot = (JRFillAreaPlot)getPlot();

		// Handle the axis formating for the catagory axis
		configureAxis(((CategoryPlot)chart.getPlot()).getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(((CategoryPlot)chart.getPlot()).getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getValueAxisLineColor());

		return getCategoryRenderer(chart);
	}

	protected JFreeChartRenderer evaluateXyAreaImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createXYAreaChart(
			(String)evaluateExpression(getTitleExpression(), evaluation ),
			(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
			(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
			(XYDataset)dataset.getDataset(),
			getPlot().getOrientation(),
			isShowLegend(),
			true,
			false
			);

		configureChart(chart, getPlot(), evaluation);
		JRAreaPlot areaPlot = (JRAreaPlot)getPlot();

		// Handle the axis formating for the catagory axis
		configureAxis(chart.getXYPlot().getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(chart.getXYPlot().getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getValueAxisTickLabelMask(),
				areaPlot.getValueAxisLineColor());
		return getXYRenderer(chart);
	}


	/**
	 *
	 */
	protected JFreeChartRenderer evaluateXYBarImage(byte evaluation) throws JRException
	{
		IntervalXYDataset tmpDataset = (IntervalXYDataset)dataset.getDataset();

		boolean isDate = true;
		if( dataset.getDatasetType() == JRChartDataset.XY_DATASET ){
			isDate = false;
		}

		JFreeChart chart =
			ChartFactory.createXYBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				isDate,
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				tmpDataset,
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");
//		((XYPlot)plot.getDomainAxis()).setTickMarksVisible(
//			((JRFillBarPlot)getPlot()).isShowTickMarks()
//			);
//		((CategoryAxis)plot.getDomainAxis()).setTickLabelsVisible(
//				((JRFillBarPlot)getPlot()).isShowTickLabels()
//				);
//		((NumberAxis)plot.getRangeAxis()).setTickMarksVisible(
//				((JRFillBarPlot)getPlot()).isShowTickMarks()
//				);
//		((NumberAxis)plot.getRangeAxis()).setTickLabelsVisible(
//				((JRFillBarPlot)getPlot()).isShowTickLabels()
//				);


		XYItemRenderer itemRenderer = xyPlot.getRenderer();
		if( getDataset().getDatasetType() == JRChartDataset.TIMESERIES_DATASET ) {
			itemRenderer.setBaseItemLabelGenerator( ((JRFillTimeSeriesDataset)getDataset()).getLabelGenerator() );
		}
		else if( getDataset().getDatasetType() == JRChartDataset.TIMEPERIOD_DATASET  ){
			itemRenderer.setBaseItemLabelGenerator( ((JRFillTimePeriodDataset)getDataset()).getLabelGenerator() );
		}
		else if( getDataset().getDatasetType() == JRChartDataset.XY_DATASET ) {
			itemRenderer.setBaseItemLabelGenerator( ((JRFillXyDataset)getDataset()).getLabelGenerator() );
		}

		JRFillBarPlot barPlot = (JRFillBarPlot)getPlot();

		itemRenderer.setBaseItemLabelsVisible( barPlot.isShowLabels() );

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getValueAxisLineColor());

		return getXYBarRenderer(chart);
	}


	protected JFreeChartRenderer evaluateXyLineImage( byte evaluation ) throws JRException {
		JRLinePlot linePlot = (JRLinePlot) getPlot();

		JFreeChart chart = ChartFactory.createXYLineChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression(linePlot.getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(linePlot.getValueAxisLabelExpression(), evaluation ),
				(XYDataset)dataset.getDataset(),
				linePlot.getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, getPlot(), evaluation);

		// Handle the axis formating for the catagory axis
		configureAxis(chart.getXYPlot().getDomainAxis(), linePlot.getCategoryAxisLabelFont(),
				linePlot.getCategoryAxisLabelColor(), linePlot.getCategoryAxisTickLabelFont(),
				linePlot.getCategoryAxisTickLabelColor(), linePlot.getCategoryAxisTickLabelMask(),
				linePlot.getCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(chart.getXYPlot().getRangeAxis(), linePlot.getValueAxisLabelFont(),
				linePlot.getValueAxisLabelColor(), linePlot.getValueAxisTickLabelFont(),
				linePlot.getValueAxisTickLabelColor(), linePlot.getValueAxisTickLabelMask(),
				linePlot.getValueAxisLineColor());

		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
		lineRenderer.setShapesVisible(linePlot.isShowShapes());
		lineRenderer.setLinesVisible(linePlot.isShowLines());

		return getXYRenderer(chart);
	}

	protected JFreeChartRenderer evaluateTimeSeriesImage( byte evaluation ) throws JRException {


		String timeAxisLabel = (String)evaluateExpression( ((JRTimeSeriesPlot)getPlot()).getTimeAxisLabelExpression(), evaluation );
		String valueAxisLabel = (String)evaluateExpression( ((JRTimeSeriesPlot)getPlot()).getValueAxisLabelExpression(), evaluation );

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				(String)evaluateExpression( getTitleExpression(), evaluation ),
				timeAxisLabel,
				valueAxisLabel,
				(TimeSeriesCollection)dataset.getDataset(),
				isShowLegend(),
				true,
				false );

		configureChart(chart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot)chart.getPlot();
		JRTimeSeriesPlot timeSeriesPlot = (JRTimeSeriesPlot)getPlot();

		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer)xyPlot.getRenderer();
		lineRenderer.setLinesVisible(((JRTimeSeriesPlot)getPlot()).isShowLines() );
		lineRenderer.setShapesVisible(((JRTimeSeriesPlot)getPlot()).isShowShapes() );

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), timeSeriesPlot.getTimeAxisLabelFont(),
				timeSeriesPlot.getTimeAxisLabelColor(), timeSeriesPlot.getTimeAxisTickLabelFont(),
				timeSeriesPlot.getTimeAxisTickLabelColor(), timeSeriesPlot.getTimeAxisTickLabelMask(),
				timeSeriesPlot.getTimeAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), timeSeriesPlot.getValueAxisLabelFont(),
				timeSeriesPlot.getValueAxisLabelColor(), timeSeriesPlot.getValueAxisTickLabelFont(),
				timeSeriesPlot.getValueAxisTickLabelColor(), timeSeriesPlot.getValueAxisTickLabelMask(),
				timeSeriesPlot.getValueAxisLineColor());

		return getTimeSeriesRenderer(chart);

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

		Color alphaColor = new Color(components[0], components[1], components[2], (float)interval.getAlpha());

		return new MeterInterval(label, range, alphaColor, null, alphaColor);
	}

	/**
	 * Build and configure a meter chart.
	 *
	 * @param evaluation current expression evaluation phase
	 * @return the JFreeChart meter chart
	 * @throws JRException
	*/
	protected JFreeChartRenderer evaluateMeterImage( byte evaluation ) throws JRException {

		JRFillMeterPlot jrPlot = (JRFillMeterPlot)getPlot();

		// Start by creating the plot that wil hold the meter
		MeterPlot chartPlot = new MeterPlot((ValueDataset)dataset.getDataset());

		// Set the shape
		int shape = jrPlot.getShape();
		if (shape == JRMeterPlot.SHAPE_CHORD)
			chartPlot.setDialShape(DialShape.CHORD);
		else if (shape == JRMeterPlot.SHAPE_CIRCLE)
			chartPlot.setDialShape(DialShape.CIRCLE);
		else
			chartPlot.setDialShape(DialShape.PIE);

		// Set the meter's range
		chartPlot.setRange(convertRange(jrPlot.getDataRange(), evaluation));

		// Set the size of the meter
		chartPlot.setMeterAngle(jrPlot.getMeterAngle());

		// Set the units - this is just a string that will be shown next to the
		// value
		String units = jrPlot.getUnits();
		if (units != null && units.length() > 0)
			chartPlot.setUnits(units);

		// Set the spacing between ticks.  I hate the name "tickSize" since to me it
		// implies I am changing the size of the tick, not the spacing between them.
		chartPlot.setTickSize(jrPlot.getTickInterval());

		// Set all the colors we support
		Color color = jrPlot.getMeterBackgroundColor();
		if (color != null)
			chartPlot.setDialBackgroundPaint(color);

		color = jrPlot.getNeedleColor();
		if (color != null)
			chartPlot.setNeedlePaint(color);

		// Set how the value is displayed.
		JRValueDisplay display = jrPlot.getValueDisplay();
		if (display != null)
		{
			if (display.getColor() != null)
			{
				chartPlot.setValuePaint(display.getColor());
			}

			if (display.getMask() != null)
			{
				chartPlot.setTickLabelFormat(new DecimalFormat(display.getMask()));
			}
			if (display.getFont() != null)
			{
				chartPlot.setValueFont(new Font(JRFontUtil.getAttributes(display.getFont())));
			}

		}

		color = jrPlot.getTickColor();
		if (color != null)
			chartPlot.setTickPaint(color);

		// Now define all of the intervals, setting their range and color
		List intervals = jrPlot.getIntervals();
		if (intervals != null)
		{
			Iterator iter = intervals.iterator();
			while (iter.hasNext())
			{
				JRMeterInterval interval = (JRMeterInterval)iter.next();
				chartPlot.addInterval(convertInterval(interval, evaluation));
			}
		}

		// Actually create the chart around the plot
		JFreeChart chart = new JFreeChart((String)evaluateExpression( getTitleExpression(), evaluation ),
										  null, chartPlot, isShowLegend());

		// Set all the generic options
		configureChart(chart, getPlot(), evaluation);

		// Meters only display a single value, so no hyperlinks are supported
		return new JFreeChartRenderer(chart);

	}

	/**
	 * Build and run a thermometer chart.  JFreeChart thermometer charts have some
	 * limitations.  They always have a maximum of three ranges, and the colors of those
	 * ranges seems to be fixed.
	 *
	 * @param evaluation current expression evaluation phase
	 * @return the JFreeChart thermometer chart
	 * @throws JRException
	 */
	protected JFreeChartRenderer evaluateThermometerImage( byte evaluation ) throws JRException {

		JRFillThermometerPlot jrPlot = (JRFillThermometerPlot)getPlot();

		// Create the plot that will hold the thermometer.
		ThermometerPlot chartPlot = new ThermometerPlot((ValueDataset)dataset.getDataset());

		Range range = convertRange(jrPlot.getDataRange(), evaluation);

		// Set the boundary of the thermomoter
		chartPlot.setLowerBound(range.getLowerBound());
		chartPlot.setUpperBound(range.getUpperBound());

		chartPlot.setShowValueLines(jrPlot.isShowValueLines());

		// Units can only be Fahrenheit, Celsius or none, so turn off for now.
		chartPlot.setUnits(ThermometerPlot.UNITS_NONE);

		// Set the color of the mercury.  Only used when the value is outside of
		// any defined ranges.
		Color color = jrPlot.getMercuryColor();
		if (color != null)
		{
			chartPlot.setMercuryPaint(color);
		}

		// Set the formatting of the value display
		JRValueDisplay display = jrPlot.getValueDisplay();
		if (display != null)
		{
			if (display.getColor() != null)
			{
				chartPlot.setValuePaint(display.getColor());
			}
			if (display.getMask() != null)
			{
				chartPlot.setValueFormat(new DecimalFormat(display.getMask()));
			}
			if (display.getFont() != null)
			{
				chartPlot.setValueFont(new Font(JRFontUtil.getAttributes(display.getFont())));
			}
		}

		// Set the location of where the value is displayed
		switch (jrPlot.getValueLocation())
		{
		  case JRThermometerPlot.LOCATION_NONE:
			 chartPlot.setValueLocation(ThermometerPlot.NONE);
			 break;
		  case JRThermometerPlot.LOCATION_LEFT:
			 chartPlot.setValueLocation(ThermometerPlot.LEFT);
			 break;
		  case JRThermometerPlot.LOCATION_RIGHT:
			 chartPlot.setValueLocation(ThermometerPlot.RIGHT);
			 break;
		  case JRThermometerPlot.LOCATION_BULB:
		  default:
			 chartPlot.setValueLocation(ThermometerPlot.BULB);
			 break;
		}

		// Define the three ranges
		range = convertRange(jrPlot.getLowRange(), evaluation);
		if (range != null)
		{
			chartPlot.setSubrangeInfo(2, range.getLowerBound(), range.getUpperBound());
		}

		range = convertRange(jrPlot.getMediumRange(), evaluation);
		if (range != null)
		{
			chartPlot.setSubrangeInfo(1, range.getLowerBound(), range.getUpperBound());
		}

		range = convertRange(jrPlot.getHighRange(), evaluation);
		if (range != null)
		{
			chartPlot.setSubrangeInfo(0, range.getLowerBound(), range.getUpperBound());
		}

		// Build a chart around this plot
		JFreeChart chart = new JFreeChart(chartPlot);

		// Set the generic options
		configureChart(chart, getPlot(), evaluation);

		// Thermometer plots only show a single value, so no drilldown or
		// hyperlinking is supported.
		return new JFreeChartRenderer(chart);

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
	 * @return the JFreeChart chart
	 * @throws JRException
	 */
	protected JFreeChartRenderer evaluateMultiAxisImage(byte evaluation) throws JRException
	{
		// A multi axis chart has to have at least one axis and chart specified.
		// Create the first axis as the base plot, and then go ahead and create the
		// charts for any additional axes.  Just take the renderer and data series
		// from those charts and add them to the first one.
		JFreeChart mainChart = null;
		Plot mainPlot = null;

		JRFillMultiAxisPlot jrPlot = (JRFillMultiAxisPlot)getPlot();

		// Generate the main plot from the first axes specified.
		Iterator iter = jrPlot.getAxes().iterator();
		if (iter.hasNext())
		{
			JRFillChartAxis axis = (JRFillChartAxis)iter.next();
			JRFillChart fillChart = axis.getFillChart();
			mainChart = fillChart.evaluateImage(evaluation).getChart();
			// Override the plot from the first axis with the plot for the multi-axis
			// chart.
			configureChart(mainChart, getPlot(), evaluation);
			mainPlot = mainChart.getPlot();
			if (axis.getPosition() == JRChartAxis.POSITION_RIGHT_OR_BOTTOM)
			{
				if (mainPlot instanceof CategoryPlot)
					((CategoryPlot)mainPlot).setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
				if (mainPlot instanceof XYPlot)
					((XYPlot)mainPlot).setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
			}
			else
			{
				if (mainPlot instanceof CategoryPlot)
					((CategoryPlot)mainPlot).setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);
				if (mainPlot instanceof XYPlot)
					((XYPlot)mainPlot).setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);
			}
		 }

		// Now handle all the extra axes, if any.
		int axisNumber = 0;
		while (iter.hasNext())
		{
			axisNumber++;
			JRFillChartAxis chartAxis = (JRFillChartAxis)iter.next();
			JRFillChart fillChart = chartAxis.getFillChart();
			JFreeChart axisChart = fillChart.evaluateImage(evaluation).getChart();

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
				if (chartAxis.getPosition() == JRChartAxis.POSITION_RIGHT_OR_BOTTOM)
				{
					mainCatPlot.setRangeAxisLocation(axisNumber, AxisLocation.BOTTOM_OR_RIGHT);
				}
				else
				{
					mainCatPlot.setRangeAxisLocation(axisNumber, AxisLocation.TOP_OR_LEFT);
				}

				// Add the data set and map it to the recently added axis
				mainCatPlot.setDataset(axisNumber, axisPlot.getDataset());
				mainCatPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

				// Set the renderer to use to draw the dataset.
				mainCatPlot.setRenderer(axisNumber, axisPlot.getRenderer());

				// Handle any color series for this chart
				configureAxisSeriesColors(axisPlot.getRenderer(), fillChart.getPlot());
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
				if (chartAxis.getPosition() == JRChartAxis.POSITION_RIGHT_OR_BOTTOM)
				{
					mainXyPlot.setRangeAxisLocation(axisNumber, AxisLocation.BOTTOM_OR_RIGHT);
				}
				else
				{
					mainXyPlot.setRangeAxisLocation(axisNumber, AxisLocation.TOP_OR_LEFT);
				}

				// Add the data set and map it to the recently added axis
				mainXyPlot.setDataset(axisNumber, axisPlot.getDataset());
				mainXyPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

				// Set the renderer to use to draw the dataset.
				mainXyPlot.setRenderer(axisNumber, axisPlot.getRenderer());

				// Handle any color series for this chart
				configureAxisSeriesColors(axisPlot.getRenderer(), fillChart.getPlot());
			}
			else
			{
				throw new JRException("MultiAxis charts only support Category and XY plots.");
			}
		}
		return new JFreeChartRenderer(mainChart);
	}

	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateImage(evaluation);

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


	protected JFreeChartRenderer getCategoryRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		JRFillCategoryDataset categoryDataset = (JRFillCategoryDataset) getDataset();
		if (categoryDataset.hasItemHyperlinks())
		{
			chartRenderer = new JRCategoryChartImageMapRenderer(chart, categoryDataset.getItemHyperlinks());
		}
		else
		{
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}


	protected JFreeChartRenderer getPieRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		JRFillPieDataset pieDataset = (JRFillPieDataset) getDataset();
		if (pieDataset.hasSectionHyperlinks())
		{
			chartRenderer = new JRPieChartImageMapRenderer(chart, pieDataset.getSectionHyperlinks());
		}
		else
		{
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}


	protected JFreeChartRenderer getXYRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		JRFillXyDataset xyDataset = (JRFillXyDataset) getDataset();
		if (xyDataset.hasItemHyperlinks())
		{
			chartRenderer = new JRXYChartImageMapRenderer(chart, xyDataset.getItemHyperlinks());
		}
		else
		{
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}


	protected JFreeChartRenderer getXYBarRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		if( getDataset().getDatasetType() == JRChartDataset.TIMESERIES_DATASET ) {
			chartRenderer = getTimeSeriesRenderer(chart);
		}
		else if( getDataset().getDatasetType() == JRChartDataset.TIMEPERIOD_DATASET  ){
			chartRenderer = getTimePeriodRenderer(chart);
		}
		else if( getDataset().getDatasetType() == JRChartDataset.XY_DATASET ) {
			chartRenderer = getXYRenderer(chart);
		} else {
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}

	protected JFreeChartRenderer getXYZRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		JRFillXyzDataset xyDataset = (JRFillXyzDataset) getDataset();
		if (xyDataset.hasItemHyperlinks())
		{
			chartRenderer = new JRXYChartImageMapRenderer(chart, xyDataset.getItemHyperlinks());
		}
		else
		{
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}


	protected JFreeChartRenderer getHighLowRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		JRFillHighLowDataset hlDataset = (JRFillHighLowDataset) getDataset();
		if (hlDataset.hasItemHyperlink())
		{
			chartRenderer = new JRHighLowChartImageMapRenderer(chart, hlDataset.getItemHyperlinks());
		}
		else
		{
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}


	protected JFreeChartRenderer getTimeSeriesRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		JRFillTimeSeriesDataset tsDataset = (JRFillTimeSeriesDataset) getDataset();
		if (tsDataset.hasItemHyperlinks())
		{
			chartRenderer = new JRTimeSeriesChartImageMapRenderer(chart, tsDataset.getItemHyperlinks());
		}
		else
		{
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}


	protected JFreeChartRenderer getTimePeriodRenderer(JFreeChart chart)
	{
		JFreeChartRenderer chartRenderer;
		JRFillTimePeriodDataset tpDataset = (JRFillTimePeriodDataset) getDataset();
		if (tpDataset.hasItemHyperlinks())
		{
			chartRenderer = new JRTimePeriodChartImageMapRenderer(chart, tpDataset.getItemHyperlinks());
		}
		else
		{
			chartRenderer = new JFreeChartRenderer(chart);
		}
		return chartRenderer;
	}

	/**
	 *
	 */
	private static RectangleEdge getEdge(byte position)
	{
		RectangleEdge edge = RectangleEdge.TOP;
		switch (position)
		{
			case EDGE_TOP :
			{
				edge = RectangleEdge.TOP;
				break;
			}
			case EDGE_BOTTOM :
			{
				edge = RectangleEdge.BOTTOM;
				break;
			}
			case EDGE_LEFT :
			{
				edge = RectangleEdge.LEFT;
				break;
			}
			case EDGE_RIGHT :
			{
				edge = RectangleEdge.RIGHT;
				break;
			}
		}
		return edge;
	}

}
