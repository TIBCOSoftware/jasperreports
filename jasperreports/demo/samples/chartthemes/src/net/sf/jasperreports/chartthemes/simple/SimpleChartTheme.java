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
package net.sf.jasperreports.chartthemes.simple;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Stroke;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

import net.sf.jasperreports.charts.ChartContext;
import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.chartthemes.spring.ChartThemesConstants;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRFontUtil;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockFrame;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericChartTheme.java 2535 2009-01-16 14:06:40Z teodord $
 */
public class SimpleChartTheme implements ChartTheme
{
	/**
	 *
	 */
	protected ChartThemeSettings chartThemeSettings = null;
	
	/**
	 *
	 */
	protected ChartContext chartContext = null;
	

	/**
	 *
	 */
	protected SimpleChartTheme()
	{
	}
	
	
	/**
	 *
	 */
	public SimpleChartTheme(ChartThemeSettings chartThemeSettings)
	{
		this.chartThemeSettings = chartThemeSettings;
	}
	
	
	/**
	 *
	 */
	public ChartThemeSettings getChartThemeSettings()
	{
		return chartThemeSettings;
	}
	
	
	/**
	 *
	 */
	public void setChartThemeSettings(ChartThemeSettings chartThemeSettings)
	{
		this.chartThemeSettings = chartThemeSettings;
	}
	
	
	/**
	 *
	 */
	public ChartSettings getChartSettings()
	{
		return getChartThemeSettings().getChartSettings();
	}
	
	
	/**
	 *
	 */
	public TitleSettings getTitleSettings()
	{
		return getChartThemeSettings().getTitleSettings();
	}
	
	
	/**
	 *
	 */
	public TitleSettings getSubtitleSettings()
	{
		return getChartThemeSettings().getSubtitleSettings();
	}
	
	
	/**
	 *
	 */
	public LegendSettings getLegendSettings()
	{
		return getChartThemeSettings().getLegendSettings();
	}
	
	
	/**
	 *
	 */
	public PlotSettings getPlotSettings()
	{
		return getChartThemeSettings().getPlotSettings();
	}
	
	
	/**
	 *
	 */
	public AxisSettings getDomainAxisSettings()
	{
		return getChartThemeSettings().getDomainAxisSettings();
	}

	
	/**
	 *
	 */
	public AxisSettings getRangeAxisSettings()
	{
		return getChartThemeSettings().getRangeAxisSettings();
	}

	
	/**
	 *
	 */
	protected JRChart getChart()
	{
		return chartContext.getChart();
	}
	
	
	/**
	 *
	 */
	protected JRChartPlot getPlot()
	{
		return getChart().getPlot();
	}
	
	
	/**
	 *
	 */
	protected Dataset getDataset()
	{
		return chartContext.getDataset();
	}
	
	
	/**
	 *
	 */
	protected Object getLabelGenerator()
	{
		return chartContext.getLabelGenerator();
	}
	
	
	/**
	 *
	 */
	protected Locale getLocale()
	{
		return chartContext.getLocale();
	}
	
	
	/**
	 *
	 */
	protected final Object evaluateExpression(JRExpression expression) throws JRException
	{
		return chartContext.evaluateExpression(expression);
	}

	
	/**
	 *
	 */
	public JFreeChart createChart(ChartContext chartContext) throws JRException
	{
		this.chartContext = chartContext;
		
		JFreeChart jfreeChart = null;
		
		switch(getChart().getChartType()) {
			case JRChart.CHART_TYPE_AREA:
				jfreeChart = createAreaChart();
				break;
			case JRChart.CHART_TYPE_BAR:
				jfreeChart = createBarChart();
				break;
			case JRChart.CHART_TYPE_BAR3D:
				jfreeChart = createBar3DChart();
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				jfreeChart = createBubbleChart();
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
				jfreeChart = createCandlestickChart();
				break;
			case JRChart.CHART_TYPE_HIGHLOW:
				jfreeChart = createHighLowChart();
				break;
			case JRChart.CHART_TYPE_LINE:
				jfreeChart = createLineChart();
				break;
			case JRChart.CHART_TYPE_METER:
				jfreeChart = createMeterChart();
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				//multi-axis charts are dealt with in JRChart
				break;
			case JRChart.CHART_TYPE_PIE:
				jfreeChart = createPieChart();
				break;
			case JRChart.CHART_TYPE_PIE3D:
				jfreeChart = createPie3DChart();
				break;
			case JRChart.CHART_TYPE_SCATTER:
				jfreeChart = createScatterChart();
				break;
			case JRChart.CHART_TYPE_STACKEDBAR:
				jfreeChart = createStackedBarChart();
				break;
			case JRChart.CHART_TYPE_STACKEDBAR3D:
				jfreeChart = createStackedBar3DChart();
				break;
			case JRChart.CHART_TYPE_THERMOMETER:
				jfreeChart = createThermometerChart();
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				jfreeChart = createTimeSeriesChart();
				break;
			case JRChart.CHART_TYPE_XYAREA:
				jfreeChart = createXyAreaChart();
				break;
			case JRChart.CHART_TYPE_XYBAR:
				jfreeChart = createXYBarChart();
				break;
			case JRChart.CHART_TYPE_XYLINE:
				jfreeChart = createXyLineChart();
				break;
			case JRChart.CHART_TYPE_STACKEDAREA:
				jfreeChart = createStackedAreaChart();
				break;
			case JRChart.CHART_TYPE_GANTT:
				jfreeChart = createGanttChart();
				break;
			default:
				throw new JRRuntimeException("Chart type " + getChart().getChartType() + " not supported.");
		}

		return jfreeChart;
	}

	/**
	 *
	 */
	protected void configureChart(JFreeChart jfreeChart, JRChartPlot jrPlot) throws JRException
	{	
		ChartSettings chartSettings = getChartSettings();
		setChartBackground(jfreeChart);
		setChartTitle(jfreeChart);
		setChartSubtitles(jfreeChart);
		setChartLegend(jfreeChart);
		setChartBorder(jfreeChart);
		
		Boolean chartAntiAlias = chartSettings.getAntiAlias();
		if(chartAntiAlias != null)
			jfreeChart.setAntiAlias(chartAntiAlias.booleanValue());
		
		Boolean textAntiAlias = chartSettings.getTextAntiAlias();
		if(textAntiAlias != null)
			jfreeChart.setTextAntiAlias(textAntiAlias.booleanValue());
		
		RectangleInsets padding = chartSettings.getPadding();
		if(padding != null)
		{
			jfreeChart.setPadding(padding);//FIXMETHEME consider using linebox
		}
		configurePlot(jfreeChart.getPlot(), jrPlot);
	}


	/**
	 *
	 */
	protected void configurePlot(Plot plot, JRChartPlot jrPlot)
	{
		RectangleInsets padding = getPlotSettings().getPadding();
		if(padding != null) 
			plot.setInsets(padding);

		Boolean plotOutlineVisible = getPlotSettings().getOutlineVisible();
		if(plotOutlineVisible == null || plotOutlineVisible.booleanValue()) 
		{
			Paint outlinePaint = getPlotSettings().getOutlinePaint() == null ? null : getPlotSettings().getOutlinePaint().getPaint();
			if(outlinePaint != null)
				plot.setOutlinePaint(outlinePaint);
			
			Stroke plotOutlineStroke = getPlotSettings().getOutlineStroke();
			if(plotOutlineStroke != null)
				plot.setOutlineStroke(plotOutlineStroke);
			
			plot.setOutlineVisible(true);
		}
		else
		{
			plot.setOutlineVisible(false);
		}
		
		setPlotBackground(plot, jrPlot);
		setPlotDrawingDefaults(plot, jrPlot);
		
		if (plot instanceof CategoryPlot)
		{
			handleCategoryPlotSettings((CategoryPlot)plot, jrPlot);
		}

		if (plot instanceof XYPlot)
		{
			handleXYPlotSettings((XYPlot)plot, jrPlot);
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
	 * @param isRangeAxis used to distinguish between range and domain axis type
	 */
	protected void configureAxis(
			Axis axis,
			JRFont labelFont,
			Color labelColor,
			JRFont tickLabelFont,
			Color tickLabelColor,
			String tickLabelMask,
			Paint lineColor,
			AxisSettings axisSettings
			) throws JRException
	{
		configureAxis(axis, labelFont, labelColor, tickLabelFont, tickLabelColor, tickLabelMask, lineColor, axisSettings, DateTickUnit.YEAR);
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
	 * @param isRangeAxis used to distinguish between range and domain axis type
	 * @param timeUnit time unit used to create a DateAxis
	 */
	protected void configureAxis(
			Axis axis,
			JRFont labelFont,
			Color labelColor,
			JRFont tickLabelFont,
			Color tickLabelColor,
			String tickLabelMask,
			Paint lineColor,
			AxisSettings axisSettings,
			int timePeriod
			) throws JRException
	{
		Boolean axisVisible = axisSettings.getVisible();
		
		if(axisVisible == null || axisVisible.booleanValue())
		{
			setAxisLine(axis, lineColor, axisSettings);

//			Double defaultFixedDimension = getAxisSettings(isRangeAxis);
//			if(defaultFixedDimension != null)
//			{
//				axis.setFixedDimension(defaultFixedDimension.doubleValue());
//			}
			
			setAxisLabel(axis, labelFont, labelColor, axisSettings);
			setAxisTickLabels(axis, tickLabelFont, tickLabelColor, tickLabelMask, axisSettings);
			setAxisTickMarks(axis, lineColor, axisSettings);
			setAxisBounds(axis, axisSettings, timePeriod);
			
		}
		else
		{
			axis.setVisible(false);
		}
	}

	/**
	 *
	 */
	protected JFreeChart createAreaChart() throws JRException 
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart = 
			ChartFactory.createAreaChart( 
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression()),
				(CategoryDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(jfreeChart, getPlot());
		JRAreaPlot areaPlot = (JRAreaPlot)getPlot();
		// Handle the axis formating for the category axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());
		return jfreeChart;
	}


	protected JFreeChart createBar3DChart() throws JRException 
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createBarChart3D(
					(String)evaluateExpression(getChart().getTitleExpression()),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression()),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression()),
					(CategoryDataset)getDataset(),
					getPlot().getOrientation(),
					isShowLegend(),
					true,
					false );

		configureChart(jfreeChart, getPlot());

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		JRBar3DPlot bar3DPlot = (JRBar3DPlot)getPlot();

		BarRenderer3D barRenderer3D =
			new BarRenderer3D(
				bar3DPlot.getXOffsetDouble() == null ? BarRenderer3D.DEFAULT_X_OFFSET : bar3DPlot.getXOffsetDouble().doubleValue(),
				bar3DPlot.getYOffsetDouble() == null ? BarRenderer3D.DEFAULT_Y_OFFSET : bar3DPlot.getYOffsetDouble().doubleValue()
				);

		barRenderer3D.setBaseItemLabelGenerator((CategoryItemLabelGenerator)getLabelGenerator());
		barRenderer3D.setBaseItemLabelsVisible(bar3DPlot.getShowLabels());

		categoryPlot.setRenderer(barRenderer3D);
		
		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), bar3DPlot.getCategoryAxisLabelFont(),
				bar3DPlot.getCategoryAxisLabelColor(), bar3DPlot.getCategoryAxisTickLabelFont(),
				bar3DPlot.getCategoryAxisTickLabelColor(), bar3DPlot.getCategoryAxisTickLabelMask(),
				bar3DPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), bar3DPlot.getValueAxisLabelFont(),
				bar3DPlot.getValueAxisLabelColor(), bar3DPlot.getValueAxisTickLabelFont(),
				bar3DPlot.getValueAxisTickLabelColor(), bar3DPlot.getValueAxisTickLabelMask(),
				bar3DPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());
		
		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createBarChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createBarChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression()),
				(CategoryDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		//plot.setNoDataMessage("No data to display");
		
		JRBarPlot barPlot = (JRBarPlot)getPlot();
		boolean isShowTickMarks = barPlot.getShowTickMarks() == null ? true : barPlot.getShowTickMarks().booleanValue();
		boolean isShowTickLabels = barPlot.getShowTickLabels() == null ? true : barPlot.getShowTickLabels().booleanValue();

		categoryPlot.getDomainAxis().setTickMarksVisible(isShowTickMarks);
		categoryPlot.getDomainAxis().setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(isShowTickMarks);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());


		BarRenderer categoryRenderer = (BarRenderer)categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)getLabelGenerator());
		categoryRenderer.setBaseItemLabelsVisible( barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue());
		categoryRenderer.setShadowVisible(false);
		
		return jfreeChart;
	}


	protected JFreeChart createBubbleChart() throws JRException 
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart = 
			ChartFactory.createBubbleChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getXAxisLabelExpression()),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getYAxisLabelExpression()),
				 (XYZDataset)getDataset(),
				 getPlot().getOrientation(),
				 isShowLegend(),
				 true,
				 false);

		configureChart(jfreeChart, getPlot());

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		JRBubblePlot bubblePlot = (JRBubblePlot)getPlot();
		int scaleType = bubblePlot.getScaleTypeInteger() == null ? XYBubbleRenderer.SCALE_ON_RANGE_AXIS : bubblePlot.getScaleTypeInteger().intValue();
		XYBubbleRenderer bubbleRenderer = new XYBubbleRenderer( scaleType );
		xyPlot.setRenderer( bubbleRenderer );

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), bubblePlot.getXAxisLabelFont(),
				bubblePlot.getXAxisLabelColor(), bubblePlot.getXAxisTickLabelFont(),
				bubblePlot.getXAxisTickLabelColor(), bubblePlot.getXAxisTickLabelMask(),
				bubblePlot.getOwnXAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), bubblePlot.getYAxisLabelFont(),
				bubblePlot.getYAxisLabelColor(), bubblePlot.getYAxisTickLabelFont(),
				bubblePlot.getYAxisTickLabelColor(), bubblePlot.getYAxisTickLabelMask(),
				bubblePlot.getOwnYAxisLineColor(), getRangeAxisSettings());
		
		return jfreeChart;
	}


	/**
	 *
	 * @param evaluation
	 * @throws net.sf.jasperreports.engine.JRException
	 */
	protected JFreeChart createCandlestickChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createCandlestickChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getTimeAxisLabelExpression()),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getValueAxisLabelExpression()),
				(DefaultHighLowDataset)getDataset(),
				isShowLegend()
				);

		configureChart(jfreeChart, getPlot());

		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		JRCandlestickPlot candlestickPlot = (JRCandlestickPlot)getPlot();
		CandlestickRenderer candlestickRenderer = (CandlestickRenderer) xyPlot.getRenderer();
		boolean isShowVolume = candlestickPlot.getShowVolume() == null ? true : candlestickPlot.getShowVolume().booleanValue();
		candlestickRenderer.setDrawVolume(isShowVolume);

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), candlestickPlot.getTimeAxisLabelFont(),
				candlestickPlot.getTimeAxisLabelColor(), candlestickPlot.getTimeAxisTickLabelFont(),
				candlestickPlot.getTimeAxisTickLabelColor(), candlestickPlot.getTimeAxisTickLabelMask(),
				candlestickPlot.getOwnTimeAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), candlestickPlot.getValueAxisLabelFont(),
				candlestickPlot.getValueAxisLabelColor(), candlestickPlot.getValueAxisTickLabelFont(),
				candlestickPlot.getValueAxisTickLabelColor(), candlestickPlot.getValueAxisTickLabelMask(),
				candlestickPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());

		return jfreeChart;
	}


	/**
	 *
	 * @param evaluation
	 * @throws JRException
	 */
	protected JFreeChart createHighLowChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createHighLowChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getTimeAxisLabelExpression()),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getValueAxisLabelExpression()),
				(DefaultHighLowDataset)getDataset(),
				isShowLegend()
				);

		configureChart(jfreeChart, getPlot());
		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		JRHighLowPlot highLowPlot = (JRHighLowPlot)getPlot();
		HighLowRenderer hlRenderer = (HighLowRenderer) xyPlot.getRenderer();
		boolean isShowOpenTicks = highLowPlot.getShowOpenTicks() == null ? false : highLowPlot.getShowOpenTicks().booleanValue();
		boolean isShowCloseTicks = highLowPlot.getShowCloseTicks() == null ? false : highLowPlot.getShowCloseTicks().booleanValue();
		
		hlRenderer.setDrawOpenTicks(isShowOpenTicks);
		hlRenderer.setDrawCloseTicks(isShowCloseTicks);

		// Handle the axis formating for the category axis
		configureAxis(xyPlot.getDomainAxis(), highLowPlot.getTimeAxisLabelFont(),
				highLowPlot.getTimeAxisLabelColor(), highLowPlot.getTimeAxisTickLabelFont(),
				highLowPlot.getTimeAxisTickLabelColor(), highLowPlot.getTimeAxisTickLabelMask(),
				highLowPlot.getOwnTimeAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), highLowPlot.getValueAxisLabelFont(),
				highLowPlot.getValueAxisLabelColor(), highLowPlot.getValueAxisTickLabelFont(),
				highLowPlot.getValueAxisTickLabelColor(), highLowPlot.getValueAxisTickLabelMask(),
				highLowPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());
		
		return jfreeChart;
	}


	protected JFreeChart createLineChart() throws JRException 
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart freeChart = 
			ChartFactory.createLineChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression( ((JRLinePlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRLinePlot)getPlot()).getValueAxisLabelExpression()),
				(CategoryDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(freeChart, getPlot());

		CategoryPlot categoryPlot = (CategoryPlot)freeChart.getPlot();
		JRLinePlot linePlot = (JRLinePlot)getPlot();

		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)categoryPlot.getRenderer();
		boolean isShowShapes = linePlot.getShowShapes() == null ? true : linePlot.getShowShapes().booleanValue();
		boolean isShowLines = linePlot.getShowLines() == null ? true : linePlot.getShowLines().booleanValue();
		
		lineRenderer.setBaseShapesVisible( isShowShapes );//FIXMECHART check this
		lineRenderer.setBaseLinesVisible( isShowLines );
		
		//FIXME labels?

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), linePlot.getCategoryAxisLabelFont(),
				linePlot.getCategoryAxisLabelColor(), linePlot.getCategoryAxisTickLabelFont(),
				linePlot.getCategoryAxisTickLabelColor(), linePlot.getCategoryAxisTickLabelMask(),
				linePlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), linePlot.getValueAxisLabelFont(),
				linePlot.getValueAxisLabelColor(), linePlot.getValueAxisTickLabelFont(),
				linePlot.getValueAxisTickLabelColor(), linePlot.getValueAxisTickLabelMask(),
				linePlot.getOwnValueAxisLineColor(), getRangeAxisSettings());

		return freeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createPie3DChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createPieChart3D(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(PieDataset)getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());

		PiePlot3D piePlot3D = (PiePlot3D) jfreeChart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		JRPie3DPlot jrPlot = (JRPie3DPlot)getPlot();
		double depthFactor = jrPlot.getDepthFactorDouble() == null ? JRPie3DPlot.DEPTH_FACTOR_DEFAULT : jrPlot.getDepthFactorDouble().doubleValue();
		boolean isCircular =  jrPlot.getCircular() == null ? false : jrPlot.getCircular().booleanValue();
		piePlot3D.setDepthFactor(depthFactor);
		piePlot3D.setCircular(isCircular);

		PieSectionLabelGenerator labelGenerator = (PieSectionLabelGenerator)getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot3D.setLabelGenerator(labelGenerator);
		}
		else if (((JRPie3DPlot)getPlot()).getLabelFormat() != null)
		{
			piePlot3D.setLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRPie3DPlot)getPlot()).getLabelFormat())
				);
		}

		if (((JRPie3DPlot)getPlot()).getLegendLabelFormat() != null)
		{
			piePlot3D.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRPie3DPlot)getPlot()).getLegendLabelFormat())
				);
		}
		
		//FIXMECHART at this moment, there are no label font, label backcolor
		// and label forecolor properties defined for the PieChart3D

//		piePlot3D.setLabelFont(JRFontUtil.getAwtFont(new JRBaseFont(null, null, chart, null)));

		piePlot3D.setLabelPaint(getChart().getForecolor());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createPieChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createPieChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(PieDataset)getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());
		PiePlot piePlot = (PiePlot)jfreeChart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		JRPiePlot jrPlot = (JRPiePlot)getPlot();
		boolean isCircular = jrPlot.getCircular() == null ? true : jrPlot.getCircular().booleanValue();
		piePlot.setCircular(isCircular);

		PieSectionLabelGenerator labelGenerator = (PieSectionLabelGenerator)getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot.setLabelGenerator(labelGenerator);
		}
		else if (((JRPiePlot)getPlot()).getLabelFormat() != null)
		{
			piePlot.setLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRPiePlot)getPlot()).getLabelFormat())
				);
		}

		if (((JRPiePlot)getPlot()).getLegendLabelFormat() != null)
		{
			piePlot.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRPiePlot)getPlot()).getLegendLabelFormat())
				);
		}
		
		//FIXMECHART at this moment, there are no label font, label backcolor
		// and label forecolor properties defined for the PieChart

//		piePlot.setLabelFont(JRFontUtil.getAwtFont(new JRBaseFont(null, null, chart, null)));

		piePlot.setLabelPaint(getChart().getForecolor());
		
		return jfreeChart;
	}


	protected JFreeChart createScatterChart() throws JRException 
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart = 
			ChartFactory.createScatterPlot(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRScatterPlot)getPlot()).getXAxisLabelExpression()),
				(String)evaluateExpression(((JRScatterPlot)getPlot()).getYAxisLabelExpression()),
				(XYDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(jfreeChart, getPlot());
		XYLineAndShapeRenderer plotRenderer = (XYLineAndShapeRenderer) ((XYPlot)jfreeChart.getPlot()).getRenderer();

		JRScatterPlot scatterPlot = (JRScatterPlot) getPlot();
		boolean isShowLines = scatterPlot.getShowLines() == null ? true : scatterPlot.getShowLines().booleanValue();
		boolean isShowShapes = scatterPlot.getShowShapes() == null ? true : scatterPlot.getShowShapes().booleanValue();
		
		plotRenderer.setBaseLinesVisible(isShowLines);
		plotRenderer.setBaseShapesVisible(isShowShapes);

		// Handle the axis formating for the catagory axis
		configureAxis(jfreeChart.getXYPlot().getDomainAxis(), scatterPlot.getXAxisLabelFont(),
				scatterPlot.getXAxisLabelColor(), scatterPlot.getXAxisTickLabelFont(),
				scatterPlot.getXAxisTickLabelColor(), scatterPlot.getXAxisTickLabelMask(),
				scatterPlot.getOwnXAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(jfreeChart.getXYPlot().getRangeAxis(), scatterPlot.getYAxisLabelFont(),
				scatterPlot.getYAxisLabelColor(), scatterPlot.getYAxisTickLabelFont(),
				scatterPlot.getYAxisTickLabelColor(), scatterPlot.getYAxisTickLabelMask(),
				scatterPlot.getOwnYAxisLineColor(), getRangeAxisSettings());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createStackedBar3DChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createStackedBarChart3D(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression()),
				(CategoryDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		JRBar3DPlot bar3DPlot = (JRBar3DPlot)getPlot();

		StackedBarRenderer3D stackedBarRenderer3D =
			new StackedBarRenderer3D(
				bar3DPlot.getXOffsetDouble() == null ? StackedBarRenderer3D.DEFAULT_X_OFFSET : bar3DPlot.getXOffsetDouble().doubleValue(),
				bar3DPlot.getYOffsetDouble() == null ? StackedBarRenderer3D.DEFAULT_Y_OFFSET : bar3DPlot.getYOffsetDouble().doubleValue()
				);

		stackedBarRenderer3D.setBaseItemLabelGenerator((CategoryItemLabelGenerator)getLabelGenerator());
		stackedBarRenderer3D.setBaseItemLabelsVisible( bar3DPlot.getShowLabels() );

		categoryPlot.setRenderer(stackedBarRenderer3D);

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), bar3DPlot.getCategoryAxisLabelFont(),
				bar3DPlot.getCategoryAxisLabelColor(), bar3DPlot.getCategoryAxisTickLabelFont(),
				bar3DPlot.getCategoryAxisTickLabelColor(), bar3DPlot.getCategoryAxisTickLabelMask(),
				bar3DPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), bar3DPlot.getValueAxisLabelFont(),
				bar3DPlot.getValueAxisLabelColor(), bar3DPlot.getValueAxisTickLabelFont(),
				bar3DPlot.getValueAxisTickLabelColor(), bar3DPlot.getValueAxisTickLabelMask(),
				bar3DPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createStackedBarChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createStackedBarChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression()),
				(CategoryDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		JRBarPlot barPlot = (JRBarPlot)getPlot();
		//plot.setNoDataMessage("No data to display");
		boolean isShowTickMarks = barPlot.getShowTickMarks() == null ? true : barPlot.getShowTickMarks().booleanValue();
		boolean isShowTickLabels = barPlot.getShowTickLabels() == null ? true : barPlot.getShowTickLabels().booleanValue();
		boolean isShowLabels = barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue();
		
		categoryPlot.getDomainAxis().setTickMarksVisible(isShowTickMarks);
		categoryPlot.getDomainAxis().setTickLabelsVisible(isShowTickLabels);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(isShowTickMarks);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(isShowTickLabels);

		BarRenderer categoryRenderer = (BarRenderer)categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)getLabelGenerator());
		categoryRenderer.setBaseItemLabelsVisible(isShowLabels);
		categoryRenderer.setShadowVisible(false);

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());

		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createStackedAreaChart() throws JRException
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createStackedAreaChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression()),
				(CategoryDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());
		JRAreaPlot areaPlot = (JRAreaPlot)getPlot();

		// Handle the axis formating for the catagory axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());

		((CategoryPlot)jfreeChart.getPlot()).getDomainAxis().setCategoryMargin(0);
		
		return jfreeChart;
	}

	protected JFreeChart createXyAreaChart() throws JRException 
	{
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart = 
			ChartFactory.createXYAreaChart(
				(String)evaluateExpression(getChart().getTitleExpression() ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression()),
				(XYDataset)getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());
		JRAreaPlot areaPlot = (JRAreaPlot)getPlot();

		// Handle the axis formating for the catagory axis
		configureAxis(jfreeChart.getXYPlot().getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(jfreeChart.getXYPlot().getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getValueAxisTickLabelMask(),
				areaPlot.getOwnValueAxisLineColor(), getRangeAxisSettings());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createXYBarChart() throws JRException
	{
		IntervalXYDataset tmpDataset = (IntervalXYDataset)getDataset();

		boolean isDate = true;
		if( getChart().getDataset().getDatasetType() == JRChartDataset.XY_DATASET ){
			isDate = false;
		}

		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createXYBarChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression()),
				isDate,
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression()),
				tmpDataset,
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot());

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		//plot.setNoDataMessage("No data to display");
//		((XYPlot)plot.getDomainAxis()).setTickMarksVisible(
//			((JRBarPlot)getPlot()).isShowTickMarks()
//			);
//		((CategoryAxis)plot.getDomainAxis()).setTickLabelsVisible(
//				((JRBarPlot)getPlot()).isShowTickLabels()
//				);
//		((NumberAxis)plot.getRangeAxis()).setTickMarksVisible(
//				((JRBarPlot)getPlot()).isShowTickMarks()
//				);
//		((NumberAxis)plot.getRangeAxis()).setTickLabelsVisible(
//				((JRBarPlot)getPlot()).isShowTickLabels()
//				);


		XYBarRenderer itemRenderer = (XYBarRenderer)xyPlot.getRenderer();
		itemRenderer.setBaseItemLabelGenerator((XYItemLabelGenerator)getLabelGenerator());
		itemRenderer.setShadowVisible(false);

		JRBarPlot barPlot = (JRBarPlot)getPlot();
		boolean isShowLabels = barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue();
		
		itemRenderer.setBaseItemLabelsVisible( isShowLabels );

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings(), DateTickUnit.DAY);

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getOwnValueAxisLineColor(), getRangeAxisSettings(), DateTickUnit.DAY);

		return jfreeChart;
	}


	protected JFreeChart createXyLineChart() throws JRException 
	{
		JRLinePlot linePlot = (JRLinePlot) getPlot();

		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart = 
			ChartFactory.createXYLineChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(linePlot.getCategoryAxisLabelExpression()),
				(String)evaluateExpression(linePlot.getValueAxisLabelExpression()),
				(XYDataset)getDataset(),
				linePlot.getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(jfreeChart, getPlot());

		// Handle the axis formating for the catagory axis
		configureAxis(jfreeChart.getXYPlot().getDomainAxis(), linePlot.getCategoryAxisLabelFont(),
				linePlot.getCategoryAxisLabelColor(), linePlot.getCategoryAxisTickLabelFont(),
				linePlot.getCategoryAxisTickLabelColor(), linePlot.getCategoryAxisTickLabelMask(),
				linePlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings());

		// Handle the axis formating for the value axis
		configureAxis(jfreeChart.getXYPlot().getRangeAxis(), linePlot.getValueAxisLabelFont(),
				linePlot.getValueAxisLabelColor(), linePlot.getValueAxisTickLabelFont(),
				linePlot.getValueAxisTickLabelColor(), linePlot.getValueAxisTickLabelMask(),
				linePlot.getOwnValueAxisLineColor(), getRangeAxisSettings());

		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer) jfreeChart.getXYPlot().getRenderer();
		boolean isShowShapes = linePlot.getShowShapes() == null ? true : linePlot.getShowShapes().booleanValue();
		boolean isShowLines = linePlot.getShowLines() == null ? true : linePlot.getShowLines().booleanValue();
		lineRenderer.setBaseShapesVisible(isShowShapes);
		lineRenderer.setBaseLinesVisible(isShowLines);

		return jfreeChart;
	}

	protected JFreeChart createTimeSeriesChart() throws JRException 
	{
		String timeAxisLabel = (String)evaluateExpression(((JRTimeSeriesPlot)getPlot()).getTimeAxisLabelExpression());
		String valueAxisLabel = (String)evaluateExpression(((JRTimeSeriesPlot)getPlot()).getValueAxisLabelExpression());

		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart = 
			ChartFactory.createTimeSeriesChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				timeAxisLabel,
				valueAxisLabel,
				(TimeSeriesCollection)getDataset(),
				isShowLegend(),
				true,
				false );

		configureChart(jfreeChart, getPlot());

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		JRTimeSeriesPlot timeSeriesPlot = (JRTimeSeriesPlot)getPlot();
		
		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer)xyPlot.getRenderer();
		
		boolean isShowShapes = timeSeriesPlot.getShowShapes() == null ? true : timeSeriesPlot.getShowShapes().booleanValue();
		boolean isShowLines = timeSeriesPlot.getShowLines() == null ? true : timeSeriesPlot.getShowLines().booleanValue();
		lineRenderer.setBaseLinesVisible(isShowLines);
		lineRenderer.setBaseShapesVisible(isShowShapes);
		
		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), timeSeriesPlot.getTimeAxisLabelFont(),
				timeSeriesPlot.getTimeAxisLabelColor(), timeSeriesPlot.getTimeAxisTickLabelFont(),
				timeSeriesPlot.getTimeAxisTickLabelColor(), timeSeriesPlot.getTimeAxisTickLabelMask(),
				timeSeriesPlot.getOwnTimeAxisLineColor(), getDomainAxisSettings(), DateTickUnit.DAY);

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), timeSeriesPlot.getValueAxisLabelFont(),
				timeSeriesPlot.getValueAxisLabelColor(), timeSeriesPlot.getValueAxisTickLabelFont(),
				timeSeriesPlot.getValueAxisTickLabelColor(), timeSeriesPlot.getValueAxisTickLabelMask(),
				timeSeriesPlot.getOwnValueAxisLineColor(), getRangeAxisSettings(), DateTickUnit.DAY);

		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createGanttChart() throws JRException
	{
		//FIXMECHART legend/tooltip/url should come from plot?
		
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart jfreeChart =
			ChartFactory.createGanttChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression()),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression()),
				(GanttCategoryDataset)getDataset(),
				isShowLegend(),
				true,  //FIXMECHART tooltip: I guess BarPlot is not the best for gantt
				false
				);

		configureChart(jfreeChart, getPlot());
		
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		//plot.setNoDataMessage("No data to display");
		
		JRBarPlot barPlot = (JRBarPlot)getPlot();
		boolean isShowTickMarks = barPlot.getShowTickMarks() == null ? true : barPlot.getShowTickMarks().booleanValue();
		boolean isShowTickLabels = barPlot.getShowTickLabels() == null ? true : barPlot.getShowTickLabels().booleanValue();
		boolean isShowLabels = barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue();
		
		//FIXMETHEME these are useless if the theme settings apply after regardless of these; check all
		categoryPlot.getDomainAxis().setTickMarksVisible(isShowTickMarks);
		categoryPlot.getDomainAxis().setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the catagory axis
		configureAxis(
			categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
			barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
			barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
			barPlot.getOwnCategoryAxisLineColor(), getDomainAxisSettings()
			);
		((DateAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(isShowTickMarks);
		((DateAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the value axis
		configureAxis(
			categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
			barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
			barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
			barPlot.getOwnValueAxisLineColor(), getRangeAxisSettings()
			);

		BarRenderer categoryRenderer = (BarRenderer)categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)getLabelGenerator());
		categoryRenderer.setBaseItemLabelsVisible(isShowLabels);
		categoryRenderer.setShadowVisible(false);

		return jfreeChart;
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
	protected Range convertRange(JRDataRange dataRange) throws JRException
	{
		if (dataRange == null)
			return null;

		Number low = (Number)evaluateExpression(dataRange.getLowExpression());
		Number high = (Number)evaluateExpression(dataRange.getHighExpression());
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
	protected MeterInterval convertInterval(JRMeterInterval interval) throws JRException
	{
		String label = interval.getLabel();
		if (label == null)
			label = "";

		Range range = convertRange(interval.getDataRange());

		Color color = interval.getBackgroundColor() != null ? interval.getBackgroundColor() : (Color)ChartThemesConstants.TRANSPARENT_PAINT;
		float[] components = color.getRGBColorComponents(null);

		float alpha = interval.getAlphaDouble() == null ? (float)JRMeterInterval.DEFAULT_TRANSPARENCY : interval.getAlphaDouble().floatValue();
		Color alphaColor = new Color(components[0], components[1], components[2], alpha);

		return new MeterInterval(label, range, alphaColor, null, alphaColor);
	}

	/**
	 * Build and configure a meter chart.
	 *
	 * @param evaluation current expression evaluation phase
	 * @throws JRException
	*/
	protected JFreeChart createMeterChart() throws JRException 
	{
		JRMeterPlot jrPlot = (JRMeterPlot)getPlot();

		// Start by creating the plot that wil hold the meter
		MeterPlot chartPlot = new MeterPlot((ValueDataset)getDataset());

		// Set the shape
		int shape = jrPlot.getShapeByte() == null ? JRMeterPlot.SHAPE_PIE : jrPlot.getShapeByte().intValue();
		if (shape == JRMeterPlot.SHAPE_CHORD)
			chartPlot.setDialShape(DialShape.CHORD);
		else if (shape == JRMeterPlot.SHAPE_CIRCLE)
			chartPlot.setDialShape(DialShape.CIRCLE);
		else
			chartPlot.setDialShape(DialShape.PIE);

		// Set the meter's range
		chartPlot.setRange(convertRange(jrPlot.getDataRange()));

		// Set the size of the meter
		int meterAngle = jrPlot.getMeterAngleInteger() == null ? 180 : jrPlot.getMeterAngleInteger().intValue();
		chartPlot.setMeterAngle(meterAngle);

		// Set the units - this is just a string that will be shown next to the
		// value
		String units = jrPlot.getUnits();
		if (units != null && units.length() > 0)
			chartPlot.setUnits(units);

		// Set the spacing between ticks.  I hate the name "tickSize" since to me it
		// implies I am changing the size of the tick, not the spacing between them.
		double tickInterval = jrPlot.getTickIntervalDouble() == null ? 10.0 : jrPlot.getTickIntervalDouble().doubleValue();
		chartPlot.setTickSize(tickInterval);

		// Set all the colors we support
		Color color = jrPlot.getMeterBackgroundColor();
		if (color != null)
			chartPlot.setDialBackgroundPaint(color);

		color = jrPlot.getNeedleColor();
		if (color != null)
			chartPlot.setNeedlePaint(color);

		JRBaseFont font = new JRBaseFont();
		JRFontUtil.copyNonNullOwnProperties(getPlotSettings().getTickLabelFont(), font);
		JRFontUtil.copyNonNullOwnProperties(jrPlot.getTickLabelFont(), font);
		font = new JRBaseFont(getChart(), font);
		chartPlot.setTickLabelFont(JRFontUtil.getAwtFont(font, getLocale()));

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
//				chartPlot.setValueFont(JRFontUtil.getAwtFont(display.getFont()));
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
				if(interval != null)
					chartPlot.addInterval(convertInterval(interval));
			}
		}

		// Actually create the chart around the plot
		JFreeChart jfreeChart = 
			new JFreeChart(
				(String)evaluateExpression(getChart().getTitleExpression()),
				null, 
				chartPlot, 
				isShowLegend()
				);

		// Set all the generic options
		configureChart(jfreeChart, getPlot());

		return jfreeChart;
	}

	/**
	 * Build and run a thermometer chart.  JFreeChart thermometer charts have some
	 * limitations.  They always have a maximum of three ranges, and the colors of those
	 * ranges seems to be fixed.
	 *
	 * @param evaluation current expression evaluation phase
	 * @throws JRException
	 */
	protected JFreeChart createThermometerChart() throws JRException 
	{
		JRThermometerPlot jrPlot = (JRThermometerPlot)getPlot();

		// Create the plot that will hold the thermometer.
		ThermometerPlot chartPlot = new ThermometerPlot((ValueDataset)getDataset());

		Range range = convertRange(jrPlot.getDataRange());

		// Set the boundary of the thermomoter
		chartPlot.setLowerBound(range.getLowerBound());
		chartPlot.setUpperBound(range.getUpperBound());

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
//				chartPlot.setValueFont(JRFontUtil.getAwtFont(display.getFont()));
			}
		}

		// Set the location of where the value is displayed
		byte valueLocation = jrPlot.getValueLocationByte() == null ? ThermometerPlot.BULB : jrPlot.getValueLocationByte().byteValue();
		switch (valueLocation)
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
		range = convertRange(jrPlot.getLowRange());
		if (range != null)
		{
			chartPlot.setSubrangeInfo(2, range.getLowerBound(), range.getUpperBound());
		}

		range = convertRange(jrPlot.getMediumRange());
		if (range != null)
		{
			chartPlot.setSubrangeInfo(1, range.getLowerBound(), range.getUpperBound());
		}

		range = convertRange(jrPlot.getHighRange());
		if (range != null)
		{
			chartPlot.setSubrangeInfo(0, range.getLowerBound(), range.getUpperBound());
		}

		// Build a chart around this plot
		JFreeChart jfreeChart = new JFreeChart(chartPlot);

		// Set the generic options
		configureChart(jfreeChart, getPlot());
		
		return jfreeChart;
	}

	/**
	 * Specifies the axis location.
	 * It has to be overriden for child themes with another default axis location
	 *
	protected AxisLocation getChartAxisLocation(JRChartAxis chartAxis)
	{
		if(chartAxis.getPositionByte() != null)
		{
			switch (chartAxis.getPositionByte().byteValue())
			{
				case JRChartAxis.POSITION_RIGHT_OR_BOTTOM :
					return AxisLocation.BOTTOM_OR_RIGHT;
				default:
					return AxisLocation.TOP_OR_LEFT;
			}
		}
		else
		{
			return (AxisLocation)getDefaultValue(defaultAxisPropertiesMap, ChartThemesConstants.AXIS_LOCATION);
		}
	}
	
	/**
	 *
	 */
	private static RectangleEdge getEdge(Byte position, RectangleEdge defaultPosition)
	{
		RectangleEdge edge = defaultPosition;
		if(position != null)
		{
			switch (position.byteValue())
			{
				case JRChart.EDGE_TOP :
				{
					edge = RectangleEdge.TOP;
					break;
				}
				case JRChart.EDGE_BOTTOM :
				{
					edge = RectangleEdge.BOTTOM;
					break;
				}
				case JRChart.EDGE_LEFT :
				{
					edge = RectangleEdge.LEFT;
					break;
				}
				case JRChart.EDGE_RIGHT :
				{
					edge = RectangleEdge.RIGHT;
					break;
				}
			}
		}
		return edge;
	}

	protected void populateSeriesColors(Paint[] colors, Paint[] colorSequence)
	{
		if(colors != null)
		{
			int size = colorSequence != null ? colorSequence.length : 0;
			for (int i = 0; i < size; i++)
			{
				colors[i] = colorSequence[i];
			}
			for (int i = size; i < colors.length; i++)
			{
				colors[i] = DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[i - size];
			}
		}
	}

	protected void setChartBackground(JFreeChart jfreeChart)
	{
		Paint backgroundPaint = getChartSettings().getBackgroundPaint() == null ? null : getChartSettings().getBackgroundPaint().getPaint();

		if (getChart().getOwnMode() != null)
		{
			if(getChart().getOwnMode().byteValue() == JRElement.MODE_OPAQUE)
			{
				if(getChart().getOwnBackcolor() != null || backgroundPaint == null)
				{
					backgroundPaint = getChart().getBackcolor();
				}
			}
			else
			{
				backgroundPaint = ChartThemesConstants.TRANSPARENT_PAINT;
			}
		}
		
		if (backgroundPaint != null)
		{
			GradientPaint gp = backgroundPaint instanceof GradientPaint ? (GradientPaint)backgroundPaint : null;
			if (gp != null)
			{
				backgroundPaint = new GradientPaint(0f, 0f, gp.getColor1(), 0f, getChart().getHeight() * 0.7f, gp.getColor2(), false);
			}
			jfreeChart.setBackgroundPaint(backgroundPaint);
		}

		setChartBackgroundImage(jfreeChart);
	}
	
	protected void setChartBackgroundImage(JFreeChart jfreeChart)
	{
		ChartSettings chartSettings = getChartSettings();
		Image backgroundImage = chartSettings.getBackgroundImage() == null ? null : chartSettings.getBackgroundImage().getImage();
		if(backgroundImage != null)
		{
			jfreeChart.setBackgroundImage(backgroundImage);

			Integer backgroundImageAlignment = chartSettings.getBackgroundImageAlignment();
			if(backgroundImageAlignment != null)
			{
				jfreeChart.setBackgroundImageAlignment(backgroundImageAlignment.intValue());
			}
			Float backgroundImageAlpha = chartSettings.getBackgroundImageAlpha();
//			if(getChart().getOwnMode() != null && getChart().getOwnMode().byteValue() == JRElement.MODE_TRANSPARENT)
//			{
//				backgroundImageAlpha = new Float(0);
//			}
			if(backgroundImageAlpha != null)
			{
				jfreeChart.setBackgroundImageAlpha(backgroundImageAlpha.floatValue());
			}
		}
	}

	protected void setChartTitle(JFreeChart jfreeChart)
	{
		TitleSettings titleSettings =  getTitleSettings();
		Boolean showTitle = titleSettings.getShowTitle();
		if(showTitle == null || showTitle.booleanValue())
		{
			TextTitle title = jfreeChart.getTitle();
					
			if(title != null)
			{
				Paint forePaint = getChart().getOwnTitleColor();
				if (forePaint == null && titleSettings.getForegroundPaint() != null)
				{
					forePaint = titleSettings.getForegroundPaint().getPaint();
				}
				if (forePaint == null)
				{
					forePaint = getChart().getTitleColor();
				}
				RectangleEdge titleEdge = getEdge(
						getChart().getTitlePositionByte(), 
						getEdge(
							titleSettings.getPosition(), 
							RectangleEdge.TOP
							)
						);
				
				handleTitleSettings(title, titleSettings, getChart().getTitleFont(), forePaint, titleEdge);
			}
		}
		else
		{
			jfreeChart.setTitle((TextTitle)null);
		}
	}

	protected void setChartSubtitles(JFreeChart jfreeChart) throws JRException
	{	
		TitleSettings subtitleSettings = getSubtitleSettings();
		
		Boolean subtitleVisibility = subtitleSettings.getShowTitle();

		if(subtitleVisibility == null || subtitleVisibility.booleanValue())
		{
			String subtitleText = (String)evaluateExpression(getChart().getSubtitleExpression());
			if (subtitleText != null)
			{
				TextTitle subtitle = new TextTitle(subtitleText);
				Paint subtitleForecolor = getChart().getOwnSubtitleColor() != null  
					? getChart().getOwnSubtitleColor() 
					: subtitleSettings.getForegroundPaint() != null  
					? subtitleSettings.getForegroundPaint().getPaint()
					: getChart().getSubtitleColor();
				//Subtitle has not its own position set, and by default this will be set the same as title position
				RectangleEdge subtitleEdge = getEdge(subtitleSettings.getPosition(), jfreeChart.getTitle() == null ? null : jfreeChart.getTitle().getPosition());
				handleTitleSettings(subtitle, subtitleSettings, getChart().getSubtitleFont(), subtitleForecolor, subtitleEdge);
	
				jfreeChart.addSubtitle(subtitle);
			}
		}
	}
	
	protected void setChartLegend(JFreeChart jfreeChart)
	{
		//The legend visibility is already taken into account in the jfreeChart object's constructor
		LegendTitle legend = jfreeChart.getLegend();
		if (legend != null)
		{
			LegendSettings legendSettings = getLegendSettings();
			JRBaseFont font = new JRBaseFont();
			JRFontUtil.copyNonNullOwnProperties(legendSettings.getFont(), font);
			JRFontUtil.copyNonNullOwnProperties(getChart().getLegendFont(), font);
			font = new JRBaseFont(getChart(), font);
			legend.setItemFont(JRFontUtil.getAwtFont(font, getLocale()));

			Paint forePaint = getChart().getOwnLegendColor();
			if (forePaint == null && legendSettings.getForegroundPaint() != null)
			{
				forePaint = legendSettings.getForegroundPaint().getPaint();
			}
			if (forePaint == null)
			{
				forePaint = getChart().getLegendColor();
			}
			if (forePaint != null)
				legend.setItemPaint(forePaint);

			Paint backPaint = getChart().getOwnLegendBackgroundColor();
			if (backPaint == null && legendSettings.getBackgroundPaint() != null)
			{
				backPaint = legendSettings.getBackgroundPaint().getPaint();
			}
			if (backPaint == null)
			{
				backPaint = getChart().getLegendBackgroundColor();
			}
			if (backPaint != null)
				legend.setBackgroundPaint(backPaint);

			BlockFrame blockFrame = legendSettings.getBlockFrame();
			if(blockFrame != null)
				legend.setFrame(blockFrame);
			
			HorizontalAlignment hAlign = legendSettings.getHorizontalAlignment();
			if(hAlign != null)
				legend.setHorizontalAlignment(hAlign);
			
			VerticalAlignment vAlign = legendSettings.getVerticalAlignment();
			if(vAlign != null)
				legend.setVerticalAlignment(vAlign);
			
			RectangleInsets padding = legendSettings.getPadding();
			if(padding != null)
				legend.setPadding(padding);

			legend.setPosition(
				getEdge(
					getChart().getLegendPositionByte(), 
					getEdge(
						legendSettings.getPosition(), 
						RectangleEdge.BOTTOM
						)
					)
				);
		}
	}
	
	protected void setChartBorder(JFreeChart jfreeChart)
	{
		ChartSettings chartSettings = getChartSettings();
		JRLineBox lineBox = getChart().getLineBox();
		if(
			lineBox.getLeftPen().getLineWidth().floatValue() == 0
			&& lineBox.getBottomPen().getLineWidth().floatValue() == 0
			&& lineBox.getRightPen().getLineWidth().floatValue() == 0
			&& lineBox.getTopPen().getLineWidth().floatValue() == 0
			)
		{
			boolean isVisible = chartSettings.getBorderVisible() == null 
				? true 
				: chartSettings.getBorderVisible().booleanValue();
			if (isVisible)
			{
				Stroke stroke = chartSettings.getBorderStroke();
				if(stroke != null)
					jfreeChart.setBorderStroke(stroke);
				Paint paint = chartSettings.getBorderPaint() == null
						? null
						: chartSettings.getBorderPaint().getPaint();
				if(paint != null)
					jfreeChart.setBorderPaint(paint);
			}
				
			jfreeChart.setBorderVisible(isVisible);
		}
	}

	protected void setPlotBackground(Plot plot, JRChartPlot jrPlot)
	{
		PlotSettings plotSettings = getPlotSettings();
		Paint backgroundPaint = jrPlot.getOwnBackcolor();
		if(backgroundPaint == null && plotSettings.getBackgroundPaint() != null)
		{
			backgroundPaint = plotSettings.getBackgroundPaint().getPaint();
		}
		if(backgroundPaint == null)
		{
			backgroundPaint = ChartThemesConstants.TRANSPARENT_PAINT;
		}
		plot.setBackgroundPaint(backgroundPaint);
		
		Float backgroundAlpha = jrPlot.getBackgroundAlphaFloat();
		if (backgroundAlpha == null)
		{
			backgroundAlpha = plotSettings.getBackgroundAlpha();
		}
		if(backgroundAlpha != null)
			plot.setBackgroundAlpha(backgroundAlpha.floatValue());
		
		Float foregroundAlpha = jrPlot.getForegroundAlphaFloat();
		if (foregroundAlpha == null)
		{
			foregroundAlpha = plotSettings.getForegroundAlpha();
		}
		if(foregroundAlpha != null)
			plot.setForegroundAlpha(foregroundAlpha.floatValue());
		
		Image backgroundImage = plotSettings.getBackgroundImage() == null ? null : plotSettings.getBackgroundImage().getImage();
		if(backgroundImage != null)
		{
			plot.setBackgroundImage(backgroundImage);
			Integer backgroundImageAlignment = plotSettings.getBackgroundImageAlignment();
			if(backgroundImageAlignment != null)
			{
				plot.setBackgroundImageAlignment(backgroundImageAlignment.intValue());
			}
			Float backgroundImageAlpha = plotSettings.getBackgroundImageAlpha();
			if(backgroundImageAlpha != null)
			{
				plot.setBackgroundImageAlpha(backgroundImageAlpha.floatValue());
			}
		}
	}
	
	protected void handleCategoryPlotSettings(CategoryPlot p, JRChartPlot jrPlot)
	{
		PlotSettings plotSettings = getPlotSettings();
		Double themeLabelRotation = plotSettings.getLabelRotation();
		// Handle rotation of the category labels.
		CategoryAxis axis = p.getDomainAxis();
		boolean hasRotation = jrPlot.getLabelRotationDouble() != null || themeLabelRotation != null;
		if(hasRotation)
		{
			double labelRotation = jrPlot.getLabelRotationDouble() != null  
					? jrPlot.getLabelRotationDouble().doubleValue() 
					: themeLabelRotation.doubleValue();
			
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
		PlotOrientation plotOrientation = plotSettings.getOrientation();
		if(plotOrientation != null)
		{
			p.setOrientation(plotOrientation);
		}
		
		CategoryItemRenderer categoryRenderer = p.getRenderer();
		Paint[] paintSequence = getPaintSequence(plotSettings, jrPlot);	
		if(paintSequence != null)
		{
			for(int i = 0; i < paintSequence.length; i++)
			{
				categoryRenderer.setSeriesPaint(i, paintSequence[i]);
			}
		}
		Paint[] outlinePaintSequence = getOutlinePaintSequence(plotSettings);	
		if(outlinePaintSequence != null)
		{
			for(int i = 0; i < outlinePaintSequence.length; i++)
			{
				categoryRenderer.setSeriesOutlinePaint(i, outlinePaintSequence[i]);
			}
		}
		Stroke[] strokeSequence = getStrokeSequence(plotSettings);
		if(strokeSequence != null)
		{
			for(int i = 0; i < strokeSequence.length; i++)
			{
				categoryRenderer.setSeriesStroke(i, strokeSequence[i]);
			}
		}
		Stroke[] outlineStrokeSequence = getOutlineStrokeSequence(plotSettings);
		if(outlineStrokeSequence != null)
		{
			for(int i = 0; i < outlineStrokeSequence.length; i++)
			{
				categoryRenderer.setSeriesOutlineStroke(i, outlineStrokeSequence[i]);
			}
		}
		
		Boolean domainGridlineVisible = plotSettings.getDomainGridlineVisible();
		if(domainGridlineVisible == null || domainGridlineVisible.booleanValue())
		{
			PaintProvider domainGridlinePaint = plotSettings.getDomainGridlinePaint();
			if(domainGridlinePaint != null)
			{
				p.setDomainGridlinePaint(domainGridlinePaint.getPaint());
			}
			Stroke domainGridlineStroke = plotSettings.getDomainGridlineStroke();
			if(domainGridlineStroke != null)
			{
				p.setDomainGridlineStroke(domainGridlineStroke);
			}
			
		}
		Boolean rangeGridlineVisible = plotSettings.getRangeGridlineVisible();
		if(rangeGridlineVisible == null || rangeGridlineVisible.booleanValue())
		{
			PaintProvider rangeGridlinePaint = plotSettings.getRangeGridlinePaint();
			if(rangeGridlinePaint != null)
			{
				p.setRangeGridlinePaint(rangeGridlinePaint.getPaint());
			}
			Stroke rangeGridlineStroke = plotSettings.getRangeGridlineStroke();
			if(rangeGridlineStroke != null)
			{
				p.setRangeGridlineStroke(rangeGridlineStroke);
			}
		}
	}

	protected void handleXYPlotSettings(XYPlot p, JRChartPlot jrPlot)
	{
		PlotSettings plotSettings = getPlotSettings();
		XYItemRenderer xyItemRenderer = p.getRenderer();
		Paint[] paintSequence = getPaintSequence(plotSettings, jrPlot);	
		if (paintSequence != null)
		{
			for(int i = 0; i < paintSequence.length; i++)
			{
				xyItemRenderer.setSeriesPaint(i, paintSequence[i]);
			}
		}
		Paint[] outlinePaintSequence = getOutlinePaintSequence(plotSettings);	
		if (outlinePaintSequence != null)
		{
			for(int i = 0; i < outlinePaintSequence.length; i++)
			{
				xyItemRenderer.setSeriesOutlinePaint(i, outlinePaintSequence[i]);
			}
		}
		Stroke[] strokeSequence = getStrokeSequence(plotSettings);
		if (strokeSequence != null)
		{
			for(int i = 0; i < strokeSequence.length; i++)
			{
				xyItemRenderer.setSeriesStroke(i, strokeSequence[i]);
			}
		}
		Stroke[] outlineStrokeSequence = getOutlineStrokeSequence(plotSettings);
		if (outlineStrokeSequence != null)
		{
			for(int i = 0; i < outlineStrokeSequence.length; i++)
			{
				xyItemRenderer.setSeriesOutlineStroke(i, outlineStrokeSequence[i]);
			}
		}
		
		Boolean domainGridlineVisible = plotSettings.getDomainGridlineVisible();
		if(domainGridlineVisible == null || domainGridlineVisible.booleanValue())
		{
			PaintProvider domainGridlinePaint = plotSettings.getDomainGridlinePaint();
			if(domainGridlinePaint != null)
			{
				p.setDomainGridlinePaint(domainGridlinePaint.getPaint());
			}
			Stroke domainGridlineStroke = plotSettings.getDomainGridlineStroke();
			if(domainGridlineStroke != null)
			{
				p.setDomainGridlineStroke(domainGridlineStroke);
			}
			
		}
		Boolean rangeGridlineVisible = plotSettings.getRangeGridlineVisible();
		if(rangeGridlineVisible == null || rangeGridlineVisible.booleanValue())
		{
			PaintProvider rangeGridlinePaint = plotSettings.getRangeGridlinePaint();
			if(rangeGridlinePaint != null)
			{
				p.setRangeGridlinePaint(rangeGridlinePaint.getPaint());
			}
			Stroke rangeGridlineStroke = plotSettings.getRangeGridlineStroke();
			if(rangeGridlineStroke != null)
			{
				p.setRangeGridlineStroke(rangeGridlineStroke);
			}
		}
		
//		p.setRangeZeroBaselineVisible(true);
		
	}

	protected void setPlotDrawingDefaults(Plot p, JRChartPlot jrPlot)
	{
		PlotSettings plotSettings = getPlotSettings();
		Paint[] paintSequence = getPaintSequence(plotSettings, jrPlot);	
		Paint[] outlinePaintSequence = getOutlinePaintSequence(plotSettings);	
		Stroke[] strokeSequence = getStrokeSequence(plotSettings);
		Stroke[] outlineStrokeSequence = getOutlineStrokeSequence(plotSettings);
			
//		Shape[] defaultPlotShapeSequence = 
//			getDefaultValue(defaultPlotPropertiesMap, ChartThemesConstants.PLOT_SHAPE_SEQUENCE) != null ?
//			(Shape[])getDefaultValue(defaultPlotPropertiesMap, ChartThemesConstants.PLOT_SHAPE_SEQUENCE) :
//			DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE;

		p.setDrawingSupplier(new DefaultDrawingSupplier(
				paintSequence,
				outlinePaintSequence,
				strokeSequence,
				outlineStrokeSequence,
				DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
				)
			);
	}
	
	protected void setAxisLine(Axis axis, Paint lineColor, AxisSettings axisSettings)
	{
		Boolean axisLineVisible = axisSettings.getLineVisible();
		if(axisLineVisible == null || axisLineVisible.booleanValue())
		{
			Paint linePaint = lineColor;
			if (linePaint == null && axisSettings.getLinePaint() != null)
			{
				linePaint = axisSettings.getLinePaint().getPaint();
			}
			if (linePaint != null)
			{
				axis.setAxisLinePaint(linePaint);
			}
			Stroke axisLineStroke = axisSettings.getLineStroke();
			if(axisLineStroke != null)
				axis.setAxisLineStroke(axisLineStroke);
		}
		else
		{
			axis.setAxisLineVisible(false);
		}
	}
	
	protected void setAxisLabel(Axis axis, JRFont labelFont, Paint labelColor, AxisSettings axisSettings)
	{
		Boolean axisLabelVisible = axisSettings.getLabelVisible();
		
		if(axisLabelVisible == null || axisLabelVisible.booleanValue())
		{
			if(axis.getLabel() == null)
				axis.setLabel(axisSettings.getLabel());

			Double labelAngle = axisSettings.getLabelAngle();
			if(labelAngle != null)
				axis.setLabelAngle(labelAngle.doubleValue());

			JRBaseFont font = new JRBaseFont();
			JRFontUtil.copyNonNullOwnProperties(axisSettings.getLabelFont(), font);
			JRFontUtil.copyNonNullOwnProperties(labelFont, font);
			font = new JRBaseFont(getChart(), font);
			axis.setLabelFont(JRFontUtil.getAwtFont(font, getLocale()));
			
			RectangleInsets labelInsets = axisSettings.getLabelInsets();
			if(labelInsets != null)
			{
				axis.setLabelInsets(labelInsets);
			}
			Paint labelPaint = labelColor != null 
					? labelColor 
					: axisSettings.getLabelPaint() != null 
					? axisSettings.getLabelPaint().getPaint()
					: null;
			if (labelPaint != null)
			{
				axis.setLabelPaint(labelPaint);
			}
		}
	}

	protected void setAxisTickLabels(Axis axis, JRFont tickLabelFont, Paint tickLabelColor, String tickLabelMask, AxisSettings axisSettings)
	{
		boolean axisTickLabelsVisible = axisSettings.getTickLabelsVisible() == null || axisSettings.getTickLabelsVisible().booleanValue();//FIXMETHEME axis visibility should be dealt with above;
		
		axis.setTickLabelsVisible(axisTickLabelsVisible);

		if(axisTickLabelsVisible)
		{
			JRBaseFont font = new JRBaseFont();
			JRFontUtil.copyNonNullOwnProperties(axisSettings.getTickLabelFont(), font);
			JRFontUtil.copyNonNullOwnProperties(tickLabelFont, font);
			font = new JRBaseFont(getChart(), font);
			axis.setTickLabelFont(JRFontUtil.getAwtFont(font, getLocale()));
			
			RectangleInsets tickLabelInsets = axisSettings.getTickLabelInsets();
			if(tickLabelInsets != null)
			{
				axis.setTickLabelInsets(tickLabelInsets);
			}
			
			Paint tickLabelPaint = tickLabelColor != null 
				? tickLabelColor 
				: axisSettings.getTickLabelPaint() != null 
				? axisSettings.getTickLabelPaint().getPaint()
				: null;
				
			if (tickLabelPaint != null)
			{
				axis.setTickLabelPaint(tickLabelPaint);
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
					
					if (fmt != null)
						((DateAxis)axis).setDateFormatOverride(fmt);
					else
						((DateAxis)axis).setDateFormatOverride(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
				}
				// ignore mask for other axis types.
			}
		}
	}
	
	protected void setAxisTickMarks(Axis axis, Paint lineColor, AxisSettings axisSettings)
	{
		boolean axisTickMarksVisible = axisSettings.getTickMarksVisible() == null || axisSettings.getTickMarksVisible().booleanValue();
		
		axis.setTickMarksVisible(axisTickMarksVisible);
		
		if(axisTickMarksVisible)
		{
			Float axisTickMarksInsideLength = axisSettings.getTickMarksInsideLength();
			if(axisTickMarksInsideLength != null)
				axis.setTickMarkInsideLength(axisTickMarksInsideLength.floatValue());
			
			Float axisTickMarksOutsideLength = axisSettings.getTickMarksOutsideLength();
			if(axisTickMarksOutsideLength != null)
				axis.setTickMarkInsideLength(axisTickMarksOutsideLength.floatValue());
			
			Paint tickMarkPaint = axisSettings.getTickMarksPaint() != null && axisSettings.getTickMarksPaint().getPaint() != null
				? axisSettings.getTickMarksPaint().getPaint()
				: lineColor;
			
			if (tickMarkPaint != null)
			{
				axis.setTickMarkPaint(tickMarkPaint);
			}
			Stroke tickMarkStroke = axisSettings.getTickMarksStroke();
			if(tickMarkStroke != null)
				axis.setTickMarkStroke(tickMarkStroke);
		}
	}
	
	protected void setAxisBounds(Axis axis, AxisSettings axisSettings, int timeUnit) throws JRException
	{
		if (axis instanceof ValueAxis)
		{
			String axisMinValue = null;
			String axisMaxValue = null;
			int tickCount = -1;
			
			
			if(getChart().hasProperties())
			{
				String tickCountProperty = null;
				if(axisSettings == getChartThemeSettings().getRangeAxisSettings())
				{
					axisMinValue = getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.range.axis.minvalue");
					axisMaxValue = getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.range.axis.maxvalue");
					tickCountProperty = getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.range.axis.tickcount");
				}
				else
				{
					axisMinValue = getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.domain.axis.minvalue");
					axisMaxValue = getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.domain.axis.maxvalue");
					tickCountProperty = getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.domain.axis.tickcount");
				}
				if(tickCountProperty != null)
				{
					tickCount = Integer.valueOf(tickCountProperty).intValue();
				}
			}
			else
			{
				Integer tickCountInteger = null;
				axisMinValue = String.valueOf(axisSettings.getMinValue());
				axisMaxValue = String.valueOf(axisSettings.getMaxValue());
				tickCountInteger = axisSettings.getTickCount();
				if(tickCountInteger != null)
				{
					tickCount = tickCountInteger.intValue();
				}
			}
			
			if(axis instanceof DateAxis)
			{
				DateAxis dateAxis = (DateAxis)axis;
				DateFormat df  = dateAxis.getDateFormatOverride();

				if(df != null)
				{
					if(axisMinValue != null)
					{
						try
                        {
	                        dateAxis.setMinimumDate(df.parse(axisMinValue));
                        }
                        catch (ParseException e)
                        {
	                        throw new JRException(e);
                        }
					}
					if(axisMaxValue != null)
					{
						try
                        {
	                        dateAxis.setMaximumDate(df.parse(axisMaxValue));
                        }
                        catch (ParseException e)
                        {
	                        throw new JRException(e);
                        }
					}
				}
			}
			else
			{
				if(axisMinValue != null && !axisMinValue.equals("null"))
				{
					((ValueAxis)axis).setLowerBound(Double.valueOf(axisMinValue).doubleValue());
				}
				if(axisMaxValue != null && !axisMaxValue.equals("null"))
				{
					((ValueAxis)axis).setUpperBound(Double.valueOf(axisMaxValue).doubleValue());
				}
			}
			
			calculateTickUnits(axis, tickCount, timeUnit);
		}
	}

	/**
	 * For a given axis, adjust the tick unit size, in order to 
	 * have a customizable number of ticks on that axis
	 * @param axis the axis 
	 * @param tickCount the user defined number of ticks for the axis
	 * @param timePeriodUnit the time period used as measure unit on a org.jfree.chart.axis.NumberAxis
	 */
	protected void calculateTickUnits(Axis axis, int tickCount, int timePeriodUnit)
	{
		if(tickCount < 0)
			return;
		if(axis instanceof NumberAxis)
		{
			NumberAxis numberAxis = (NumberAxis)axis;
			int axisRange = (int)numberAxis.getRange().getLength();
			if(numberAxis.getNumberFormatOverride() != null)
			{
				numberAxis.setTickUnit(new NumberTickUnit(axisRange/tickCount, numberAxis.getNumberFormatOverride()));
			}
			else
			{
				numberAxis.setTickUnit(new NumberTickUnit(axisRange/tickCount));
			}
		}
		else if(axis instanceof DateAxis)
		{
//			DateAxis dateAxis = (DateAxis)axis;
//			int axisRange = (int)dateAxis.getRange().getLength();
//			if(dateAxis.getDateFormatOverride() != null)
//			{
//				dateAxis.setTickUnit(new DateTickUnit(timePeriodUnit, axisRange/tickCount, dateAxis.getDateFormatOverride()));
//			}
//			else
//			{
//				dateAxis.setTickUnit(new DateTickUnit(timePeriodUnit, axisRange/tickCount));
//			}
		}
	}
	
	/**
	 * Specifies whether a chart legend should be visible or no by default.
	 */
	protected boolean isShowLegend()
	{
		Boolean showLegend = 
			getChart().getShowLegend() != null
				? getChart().getShowLegend()
				: getLegendSettings().getShowLegend() == null
					? Boolean.TRUE
					: getLegendSettings().getShowLegend();

		return showLegend.booleanValue();
	}

	protected Paint[] getPaintSequence(PlotSettings plotSettings, JRChartPlot jrPlot)
	{
		Paint[] colors = null;
		SortedSet seriesColors = jrPlot.getSeriesColors();
		Paint[] colorSequence = null;
		
		//The series gradient paint setting is considered first
		List themeSeriesPaintProvider = getChartThemeSettings().getPlotSettings().getSeriesGradientPaintSequence() != null 
				? getChartThemeSettings().getPlotSettings().getSeriesGradientPaintSequence()
				: getChartThemeSettings().getPlotSettings().getSeriesColorSequence();
				
		if (seriesColors != null && seriesColors.size() > 0)
		{
			int seriesColorsSize = seriesColors.size();
			
			colors = new Paint[DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length + seriesColorsSize];

			JRSeriesColor[] jrColorSequence = new JRSeriesColor[seriesColorsSize];
			seriesColors.toArray(jrColorSequence);
			colorSequence = new Paint[seriesColorsSize];
			
			for (int i = 0; i < seriesColorsSize; i++)
			{
				colorSequence[i] = jrColorSequence[i].getColor();
			}
			populateSeriesColors(colors, colorSequence);
		}
		else if(themeSeriesPaintProvider != null && !themeSeriesPaintProvider.isEmpty())
		{
			colors = new Paint[DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE.length + themeSeriesPaintProvider.size()];
			colorSequence = new Paint[themeSeriesPaintProvider.size()];
			List themeSeriesColors = new ArrayList();
			for(int i=0; i< themeSeriesPaintProvider.size(); i++)
			{
				themeSeriesColors.add(((PaintProvider)themeSeriesPaintProvider.get(i)).getPaint());
			}
			themeSeriesColors.toArray(colorSequence);
			populateSeriesColors(colors, colorSequence);
		}
		else
		{
			colors = DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE;
		}
		return colors;
	}
	
	protected Paint[] getOutlinePaintSequence(PlotSettings plotSettings)
	{
		List outlinePaintSequenceProvider = plotSettings.getSeriesOutlinePaintSequence();
		Paint[] outlinePaintSequence = null;
		if(outlinePaintSequenceProvider != null && !outlinePaintSequenceProvider.isEmpty())
		{
			outlinePaintSequence = new Paint[outlinePaintSequenceProvider.size()];
			for (int i=0; i< outlinePaintSequenceProvider.size(); i++)
			{
				outlinePaintSequence[i] = ((PaintProvider)outlinePaintSequenceProvider.get(i)).getPaint();
			}
		}
		else
		{
			outlinePaintSequence = DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE;
		}
		return outlinePaintSequence;
	}
	
	protected Stroke[] getStrokeSequence(PlotSettings plotSettings)
	{
		List strokeSequenceList = plotSettings.getSeriesStrokeSequence();
		Stroke[] strokeSequence = null;
		if(strokeSequenceList != null && !strokeSequenceList.isEmpty())
		{
			strokeSequence = (Stroke[])strokeSequenceList.toArray(new Stroke[strokeSequenceList.size()]);
		}
		else
		{
			strokeSequence = DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE;
		}
		return strokeSequence;
	}
	
	protected Stroke[] getOutlineStrokeSequence(PlotSettings plotSettings)
	{
		List outlineStrokeSequenceList = getChartThemeSettings().getPlotSettings().getSeriesOutlineStrokeSequence();
		Stroke[] outlineStrokeSequence = null;
		if(outlineStrokeSequenceList != null && !outlineStrokeSequenceList.isEmpty())
		{
			outlineStrokeSequence = (Stroke[])outlineStrokeSequenceList.toArray(new Stroke[outlineStrokeSequenceList.size()]);
		}
		else
		{
			outlineStrokeSequence = DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE;
		}
		return outlineStrokeSequence;
	}

	protected void handleTitleSettings(
			TextTitle title, 
			TitleSettings titleSettings, 
			JRFont titleFont,
			Paint titleForegroundPaint,
			RectangleEdge titleEdge)
	{
		JRBaseFont font = new JRBaseFont();
		JRFontUtil.copyNonNullOwnProperties(titleSettings.getFont(), font);
		JRFontUtil.copyNonNullOwnProperties(titleFont, font);
		font = new JRBaseFont(getChart(), font);
		title.setFont(JRFontUtil.getAwtFont(font, getLocale()));
		
		HorizontalAlignment hAlign = (HorizontalAlignment)titleSettings.getHorizontalAlignment();
		if(hAlign != null)
			title.setHorizontalAlignment(hAlign);
		
		VerticalAlignment vAlign = (VerticalAlignment)titleSettings.getVerticalAlignment();
		if(vAlign != null)
			title.setVerticalAlignment(vAlign);
		
		RectangleInsets padding = titleSettings.getPadding();
		if(padding != null)
			title.setPadding(padding);
		
		if (titleForegroundPaint != null)
			title.setPaint(titleForegroundPaint);

		Paint backPaint = titleSettings.getBackgroundPaint() != null ? titleSettings.getBackgroundPaint().getPaint() : null;
		if(backPaint != null)
			title.setBackgroundPaint(backPaint);
		if(titleEdge != null)
			title.setPosition(titleEdge);
	}

}
