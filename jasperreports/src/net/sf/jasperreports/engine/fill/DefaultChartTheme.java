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
import java.awt.Paint;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.fill.JRFillAreaPlot;
import net.sf.jasperreports.charts.fill.JRFillBar3DPlot;
import net.sf.jasperreports.charts.fill.JRFillBarPlot;
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillChartAxis;
import net.sf.jasperreports.charts.fill.JRFillGanttDataset;
import net.sf.jasperreports.charts.fill.JRFillLinePlot;
import net.sf.jasperreports.charts.fill.JRFillMeterPlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.charts.fill.JRFillThermometerPlot;
import net.sf.jasperreports.charts.fill.JRFillTimePeriodDataset;
import net.sf.jasperreports.charts.fill.JRFillTimeSeriesDataset;
import net.sf.jasperreports.charts.fill.JRFillXyDataset;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRFontUtil;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
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
import org.jfree.data.gantt.GanttCategoryDataset;
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
 * @version $Id: JRFillChart.java 2278 2008-08-14 16:14:54Z teodord $
 */
public class DefaultChartTheme implements ChartTheme
{


	/**
	 *
	 */
	protected static final Color TRANSPARENT_PAINT = new Color(0, 0, 0, 0);
	
	/**
	 *
	 */
	protected static final Color DEFAULT_AXIS_LINE_PAINT = new Color(0, 0, 0, 0);
	
	/**
	 * Specifies whether a chart legend should be visible or no by default.
	 * It has to be overriden for child themes which don't show chart legends
	 */
	protected final boolean DEFAULT_SHOW_lEGEND = true;
	
	/**
	 * Specifies the default axis location.
	 * It has to be overriden for child themes with another default axis location
	 */
	protected final AxisLocation DEFAULT_AXIS_LOCATION = AxisLocation.TOP_OR_LEFT;
	
	/**
	 *
	 */
	private JRFillChart chart = null;
	

	/**
	 *
	 */
	protected DefaultChartTheme()
	{
	}
	
	
	/**
	 *
	 */
	protected JRFillChart getChart()
	{
		return chart;
	}
	
	
	/**
	 *
	 */
	protected JRChartPlot getPlot()
	{
		return chart.getPlot();
	}
	
	
	/**
	 *
	 */
	protected JRFillChartDataset getDataset()
	{
		return (JRFillChartDataset)chart.getDataset();
	}
	
	
	/**
	 *
	 */
	protected final Object evaluateExpression(JRExpression expression, byte evaluation) throws JRException
	{
		return chart.evaluateExpression(expression, evaluation);
	}

	
	/**
	 *
	 */
	public JFreeChart createChart(JRFillChart chart, byte evaluation) throws JRException
	{
		this.chart = chart;
		
		JFreeChart jfreeChart = null;
		
		switch(chart.getChartType()) {
			case JRChart.CHART_TYPE_AREA:
				jfreeChart = createAreaChart(evaluation);
				break;
			case JRChart.CHART_TYPE_BAR:
				jfreeChart = createBarChart(evaluation);
				break;
			case JRChart.CHART_TYPE_BAR3D:
				jfreeChart = createBar3DChart(evaluation);
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				jfreeChart = createBubbleChart(evaluation);
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
				jfreeChart = createCandlestickChart(evaluation);
				break;
			case JRChart.CHART_TYPE_HIGHLOW:
				jfreeChart = createHighLowChart(evaluation);
				break;
			case JRChart.CHART_TYPE_LINE:
				jfreeChart = createLineChart(evaluation);
				break;
			case JRChart.CHART_TYPE_METER:
				jfreeChart = createMeterChart(evaluation);
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				//multi-axis charts are dealt with in JRFillChart
				break;
			case JRChart.CHART_TYPE_PIE:
				jfreeChart = createPieChart(evaluation);
				break;
			case JRChart.CHART_TYPE_PIE3D:
				jfreeChart = createPie3DChart(evaluation);
				break;
			case JRChart.CHART_TYPE_SCATTER:
				jfreeChart = createScatterChart(evaluation);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR:
				jfreeChart = createStackedBarChart(evaluation);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR3D:
				jfreeChart = createStackedBar3DChart(evaluation);
				break;
			case JRChart.CHART_TYPE_THERMOMETER:
				jfreeChart = createThermometerChart(evaluation);
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				jfreeChart = createTimeSeriesChart(evaluation);
				break;
			case JRChart.CHART_TYPE_XYAREA:
				jfreeChart = createXyAreaChart(evaluation);
				break;
			case JRChart.CHART_TYPE_XYBAR:
				jfreeChart = createXYBarChart(evaluation);
				break;
			case JRChart.CHART_TYPE_XYLINE:
				jfreeChart = createXyLineChart(evaluation);
				break;
			case JRChart.CHART_TYPE_STACKEDAREA:
				jfreeChart = createStackedAreaChart(evaluation);
				break;
			case JRChart.CHART_TYPE_GANTT:
				jfreeChart = createGanttChart(evaluation);
				break;
			default:
				throw new JRRuntimeException("Chart type " + chart.getChartType() + " not supported.");
		}

		return jfreeChart;
	}


	/**
	 *
	 */
	protected void configureChart(JFreeChart jfreeChart, JRChartPlot jrPlot, byte evaluation) throws JRException
	{
		configureChart(jfreeChart, 
				jrPlot, 
				evaluation,
				null,
				RectangleEdge.TOP, 
				null, 
				RectangleEdge.BOTTOM,
				1f,
				1f,
				0.0,
				PlotOrientation.VERTICAL,
				null);
	}

	/**
	 *
	 */
	protected void configureChart(
			JFreeChart jfreeChart, 
			JRChartPlot jrPlot, 
			byte evaluation,
			Paint defaultBackgroundPaint,
			RectangleEdge defaultTitlePosition,
			RectangleEdge defaultSubtitlePosition,
			RectangleEdge defaultLegendPosition,
			float defaultBackgroundAlpha,
			float defaultForegroundAlpha,
			double defaultLabelRotation,
			PlotOrientation defaultPlotOrientation,
			Paint[] defaultSeriesColors) throws JRException
	{
		JRFillChart jrChart = getChart();
		if (jrChart.getOwnMode() != null)
		{
			if(jrChart.getOwnMode().byteValue() == JRElement.MODE_OPAQUE)
			{
				if(jrChart.getOwnBackcolor() == null && defaultBackgroundPaint != null)
				{
					jfreeChart.setBackgroundPaint(defaultBackgroundPaint);
				}
				else
				{
					jfreeChart.setBackgroundPaint(jrChart.getBackcolor());
				}
			}
			else
			{
				jfreeChart.setBackgroundPaint(TRANSPARENT_PAINT);
			}
		}
		else
		{
			jfreeChart.setBackgroundPaint(defaultBackgroundPaint != null ? defaultBackgroundPaint : TRANSPARENT_PAINT);
		}
		
		RectangleEdge titleEdge = getEdge(jrChart.getTitlePositionByte(), defaultTitlePosition);
		
		if (jfreeChart.getTitle() != null)
		{
			TextTitle title = jfreeChart.getTitle();
			title.setPaint(jrChart.getTitleColor());

			title.setFont(JRFontUtil.getAwtFont(jrChart.getTitleFont()));
			title.setPosition(titleEdge);
		}

		String subtitleText = (String)evaluateExpression(jrChart.getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(jrChart.getSubtitleColor());

			subtitle.setFont(JRFontUtil.getAwtFont(jrChart.getSubtitleFont()));
			
			//Subtitle has not its own position set, and by default this will be set the same as title position
			RectangleEdge subtitleEdge = null;
			if(defaultSubtitlePosition == null)
			{	
				subtitleEdge = titleEdge;
			}
			else
			{
				subtitleEdge = getEdge(null, defaultSubtitlePosition);
			}
			
			subtitle.setPosition(subtitleEdge);
			jfreeChart.addSubtitle(subtitle);
		}

		// Apply all of the legend formatting options
		LegendTitle legend = jfreeChart.getLegend();
		if (legend != null)
		{
			legend.setItemPaint(jrChart.getLegendColor());

			if (jrChart.getOwnLegendBackgroundColor() == null)// in a way, legend backcolor inheritence from chart is useless
			{
				legend.setBackgroundPaint(TRANSPARENT_PAINT);
			}
			else
			{
				legend.setBackgroundPaint(jrChart.getLegendBackgroundColor());
			}

			legend.setItemFont(JRFontUtil.getAwtFont(jrChart.getLegendFont()));
			legend.setPosition(getEdge(jrChart.getLegendPositionByte(), defaultLegendPosition));
		}
		
		configurePlot(jfreeChart.getPlot(), 
				jrPlot,
				defaultBackgroundAlpha,
				defaultForegroundAlpha,
				defaultLabelRotation,
				defaultPlotOrientation,
				defaultSeriesColors);
	}


	/**
	 *
	 */
	protected void configurePlot(Plot p, JRChartPlot jrPlot)
	{
		configurePlot(p,
				jrPlot,
				1f,
				1f,
				0.0,
				PlotOrientation.VERTICAL,
				null);
	}

	/**
	 *
	 */
	protected void configurePlot(Plot p, 
			JRChartPlot jrPlot,
			float defaultBackgroundAlpha,
			float defaultForegroundAlpha,
			double defaultLabelRotation,
			PlotOrientation defaultPlotOrientation,
			Paint[] defaultSeriesColors)
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

		float backgroundAlpha = getPlot().getBackgroundAlphaFloat() == null ? defaultBackgroundAlpha : getPlot().getBackgroundAlphaFloat().floatValue();
		float foregroundAlpha = getPlot().getForegroundAlphaFloat() == null ? defaultForegroundAlpha : getPlot().getForegroundAlphaFloat().floatValue();
		p.setBackgroundAlpha(backgroundAlpha);
		p.setForegroundAlpha(foregroundAlpha);

		if (p instanceof CategoryPlot)
		{
			// Handle rotation of the category labels.
			CategoryAxis axis = ((CategoryPlot)p).getDomainAxis();
			double labelRotation = getPlot().getLabelRotationDouble() == null ? defaultLabelRotation : getPlot().getLabelRotationDouble().doubleValue();
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
		SortedSet seriesColors = getPlot().getSeriesColors();
		if (seriesColors != null && seriesColors.size() > 0)
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
		else if(defaultSeriesColors != null)
		{
			p.setDrawingSupplier(new DefaultDrawingSupplier(
				defaultSeriesColors,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
				)
			);
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
	protected void configureAxis(
			Axis axis,
			JRFont labelFont,
			Color labelColor,
			JRFont tickLabelFont,
			Color tickLabelColor,
			String tickLabelMask,
			Color lineColor
			)
	{
		configureAxis(
				axis,
				labelFont,
				labelColor,
				tickLabelFont,
				tickLabelColor,
				tickLabelMask,
				lineColor,
				null
		);
	}

	protected void configureAxis(
			Axis axis,
			JRFont labelFont,
			Color labelColor,
			JRFont tickLabelFont,
			Color tickLabelColor,
			String tickLabelMask,
			Paint linePaint,
			Paint defaultLinePaint
			)
		{
			axis.setLabelFont(JRFontUtil.getAwtFont(labelFont));
			axis.setTickLabelFont(JRFontUtil.getAwtFont(tickLabelFont));
			if (labelColor != null)
			{
				axis.setLabelPaint(labelColor);
			}

			if (tickLabelColor != null)
			{
				axis.setTickLabelPaint(tickLabelColor);
			}

			if (linePaint != null)
			{
				axis.setAxisLinePaint(linePaint);
				axis.setTickMarkPaint(linePaint);
			}
			else if(defaultLinePaint != null)
			{
				axis.setAxisLinePaint(defaultLinePaint);
				axis.setTickMarkPaint(defaultLinePaint);
			}
			else
			{
				axis.setAxisLinePaint(DEFAULT_AXIS_LINE_PAINT);
				axis.setTickMarkPaint(DEFAULT_AXIS_LINE_PAINT);
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
	protected JFreeChart createAreaChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = 
			ChartFactory.createAreaChart( 
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)getDataset().getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(jfreeChart, getPlot(), evaluation);
		JRFillAreaPlot areaPlot = (JRFillAreaPlot)getPlot();
		// Handle the axis formating for the category axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnValueAxisLineColor());
		return jfreeChart;
	}


	protected JFreeChart createBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart =
			ChartFactory.createBarChart3D(
					(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
					(CategoryDataset)getDataset().getDataset(),
					getPlot().getOrientation(),
					isShowLegend(),
					true,
					false );

		configureChart(jfreeChart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		JRFillBar3DPlot bar3DPlot = (JRFillBar3DPlot)getPlot();

		BarRenderer3D barRenderer3D =
			new BarRenderer3D(
				bar3DPlot.getXOffsetDouble() == null ? BarRenderer3D.DEFAULT_X_OFFSET : bar3DPlot.getXOffsetDouble().doubleValue(),
				bar3DPlot.getYOffsetDouble() == null ? BarRenderer3D.DEFAULT_Y_OFFSET : bar3DPlot.getYOffsetDouble().doubleValue()
				);

		barRenderer3D.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		barRenderer3D.setBaseItemLabelsVisible(bar3DPlot.getShowLabels() == null ? false : bar3DPlot.getShowLabels().booleanValue());

		categoryPlot.setRenderer(barRenderer3D);
		
		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), bar3DPlot.getCategoryAxisLabelFont(),
				bar3DPlot.getCategoryAxisLabelColor(), bar3DPlot.getCategoryAxisTickLabelFont(),
				bar3DPlot.getCategoryAxisTickLabelColor(), bar3DPlot.getCategoryAxisTickLabelMask(),
				bar3DPlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), bar3DPlot.getValueAxisLabelFont(),
				bar3DPlot.getValueAxisLabelColor(), bar3DPlot.getValueAxisTickLabelFont(),
				bar3DPlot.getValueAxisTickLabelColor(), bar3DPlot.getValueAxisTickLabelMask(),
				bar3DPlot.getOwnValueAxisLineColor());
		
		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createBarChart(byte evaluation) throws JRException
	{
		CategoryDataset categoryDataset = (CategoryDataset)getDataset().getDataset();
		JFreeChart jfreeChart =
			ChartFactory.createBarChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				categoryDataset,
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		//plot.setNoDataMessage("No data to display");
		
		JRFillBarPlot barPlot = (JRFillBarPlot)getPlot();
		boolean isShowTickMarks = barPlot.getShowTickMarks() == null ? true : barPlot.getShowTickMarks().booleanValue();
		boolean isShowTickLabels = barPlot.getShowTickLabels() == null ? true : barPlot.getShowTickLabels().booleanValue();

		categoryPlot.getDomainAxis().setTickMarksVisible(isShowTickMarks);
		categoryPlot.getDomainAxis().setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getOwnCategoryAxisLineColor());

		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(isShowTickMarks);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getOwnValueAxisLineColor());


		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		categoryRenderer.setBaseItemLabelsVisible( barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue());
		
		return jfreeChart;
	}


	protected JFreeChart createBubbleChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = ChartFactory.createBubbleChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getXAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getYAxisLabelExpression(), evaluation),
				 (XYZDataset)getDataset().getDataset(),
				 getPlot().getOrientation(),
				 isShowLegend(),
				 true,
				 false);

		configureChart(jfreeChart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		JRBubblePlot bubblePlot = (JRBubblePlot)getPlot();
		int scaleType = bubblePlot.getScaleTypeInteger() == null ? XYBubbleRenderer.SCALE_ON_RANGE_AXIS : bubblePlot.getScaleTypeInteger().intValue();
		XYBubbleRenderer bubbleRenderer = new XYBubbleRenderer( scaleType );
		xyPlot.setRenderer( bubbleRenderer );

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), bubblePlot.getXAxisLabelFont(),
				bubblePlot.getXAxisLabelColor(), bubblePlot.getXAxisTickLabelFont(),
				bubblePlot.getXAxisTickLabelColor(), bubblePlot.getXAxisTickLabelMask(),
				bubblePlot.getOwnXAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), bubblePlot.getYAxisLabelFont(),
				bubblePlot.getYAxisLabelColor(), bubblePlot.getYAxisTickLabelFont(),
				bubblePlot.getYAxisTickLabelColor(), bubblePlot.getYAxisTickLabelMask(),
				bubblePlot.getOwnYAxisLineColor());
		
		return jfreeChart;
	}


	/**
	 *
	 * @param evaluation
	 * @throws net.sf.jasperreports.engine.JRException
	 */
	protected JFreeChart createCandlestickChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart =
			ChartFactory.createCandlestickChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)getDataset().getDataset(),
				isShowLegend()
				);

		configureChart(jfreeChart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		JRCandlestickPlot candlestickPlot = (JRCandlestickPlot)getPlot();
		CandlestickRenderer candlestickRenderer = (CandlestickRenderer) xyPlot.getRenderer();
		boolean isShowVolume = candlestickPlot.getShowVolume() == null ? true : candlestickPlot.getShowVolume().booleanValue();
		candlestickRenderer.setDrawVolume(isShowVolume);

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), candlestickPlot.getTimeAxisLabelFont(),
				candlestickPlot.getTimeAxisLabelColor(), candlestickPlot.getTimeAxisTickLabelFont(),
				candlestickPlot.getTimeAxisTickLabelColor(), candlestickPlot.getTimeAxisTickLabelMask(),
				candlestickPlot.getOwnTimeAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), candlestickPlot.getValueAxisLabelFont(),
				candlestickPlot.getValueAxisLabelColor(), candlestickPlot.getValueAxisTickLabelFont(),
				candlestickPlot.getValueAxisTickLabelColor(), candlestickPlot.getValueAxisTickLabelMask(),
				candlestickPlot.getOwnValueAxisLineColor());

		return jfreeChart;
	}


	/**
	 *
	 * @param evaluation
	 * @throws JRException
	 */
	protected JFreeChart createHighLowChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart =
			ChartFactory.createHighLowChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)getDataset().getDataset(),
				isShowLegend()
				);

		configureChart(jfreeChart, getPlot(), evaluation);

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
				highLowPlot.getOwnTimeAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), highLowPlot.getValueAxisLabelFont(),
				highLowPlot.getValueAxisLabelColor(), highLowPlot.getValueAxisTickLabelFont(),
				highLowPlot.getValueAxisTickLabelColor(), highLowPlot.getValueAxisTickLabelMask(),
				highLowPlot.getOwnValueAxisLineColor());
		
		return jfreeChart;
	}


	protected JFreeChart createLineChart(byte evaluation) throws JRException 
	{
		JFreeChart chart = ChartFactory.createLineChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRLinePlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRLinePlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)getDataset().getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		JRFillLinePlot linePlot = (JRFillLinePlot)getPlot();

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
				linePlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), linePlot.getValueAxisLabelFont(),
				linePlot.getValueAxisLabelColor(), linePlot.getValueAxisTickLabelFont(),
				linePlot.getValueAxisTickLabelColor(), linePlot.getValueAxisTickLabelMask(),
				linePlot.getOwnValueAxisLineColor());

		return chart;
	}


	/**
	 *
	 */
	protected JFreeChart createPie3DChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart =
			ChartFactory.createPieChart3D(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(PieDataset)getDataset().getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);

		PiePlot3D piePlot3D = (PiePlot3D) jfreeChart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		JRPie3DPlot jrPlot = (JRFillPie3DPlot)getPlot();
		double depthFactor = jrPlot.getDepthFactorDouble() == null ? JRPie3DPlot.DEPTH_FACTOR_DEFAULT : jrPlot.getDepthFactorDouble().doubleValue();
		boolean isCircular =  jrPlot.getCircular() == null ? false : jrPlot.getCircular().booleanValue();
		piePlot3D.setDepthFactor(depthFactor);
		piePlot3D.setCircular(isCircular);

		PieSectionLabelGenerator labelGenerator = ((JRFillPieDataset)getDataset()).getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot3D.setLabelGenerator(labelGenerator);
		}
		else if (((JRFillPie3DPlot)getPlot()).getLabelFormat() != null)
		{
			piePlot3D.setLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRFillPie3DPlot)getPlot()).getLabelFormat())
				);
		}

		if (((JRFillPie3DPlot)getPlot()).getLegendLabelFormat() != null)
		{
			piePlot3D.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRFillPie3DPlot)getPlot()).getLegendLabelFormat())
				);
		}
		
		//FIXMECHART at this moment, there are no label font, label backcolor
		// and label forecolor properties defined for the PieChart3D

		piePlot3D.setLabelFont(JRFontUtil.getAwtFont(new JRBaseFont(null, null, getChart(), null)));

		piePlot3D.setLabelPaint(getChart().getForecolor());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createPieChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart =
			ChartFactory.createPieChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(PieDataset)getDataset().getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);
		PiePlot piePlot = (PiePlot)jfreeChart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		JRFillPiePlot jrPlot = (JRFillPiePlot)getPlot();
		boolean isCircular = jrPlot.getCircular() == null ? true : jrPlot.getCircular().booleanValue();
		piePlot.setCircular(isCircular);

		PieSectionLabelGenerator labelGenerator = ((JRFillPieDataset)getDataset()).getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot.setLabelGenerator(labelGenerator);
		}
		else if (((JRFillPiePlot)getPlot()).getLabelFormat() != null)
		{
			piePlot.setLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRFillPiePlot)getPlot()).getLabelFormat())
				);
		}

		if (((JRFillPiePlot)getPlot()).getLegendLabelFormat() != null)
		{
			piePlot.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator(((JRFillPiePlot)getPlot()).getLegendLabelFormat())
				);
		}
		
		//FIXMECHART at this moment, there are no label font, label backcolor
		// and label forecolor properties defined for the PieChart

		piePlot.setLabelFont(JRFontUtil.getAwtFont(new JRBaseFont(null, null, getChart(), null)));

		piePlot.setLabelPaint(getChart().getForecolor());
		
		return jfreeChart;
	}


	protected JFreeChart createScatterChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = ChartFactory.createScatterPlot(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRScatterPlot)getPlot()).getXAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRScatterPlot)getPlot()).getYAxisLabelExpression(), evaluation ),
				(XYDataset)getDataset().getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(jfreeChart, getPlot(), evaluation);
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
				scatterPlot.getOwnXAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(jfreeChart.getXYPlot().getRangeAxis(), scatterPlot.getYAxisLabelFont(),
				scatterPlot.getYAxisLabelColor(), scatterPlot.getYAxisTickLabelFont(),
				scatterPlot.getYAxisTickLabelColor(), scatterPlot.getYAxisTickLabelMask(),
				scatterPlot.getOwnYAxisLineColor());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createStackedBar3DChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart =
			ChartFactory.createStackedBarChart3D(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)getDataset().getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		JRFillBar3DPlot bar3DPlot = (JRFillBar3DPlot)getPlot();

		StackedBarRenderer3D stackedBarRenderer3D =
			new StackedBarRenderer3D(
				bar3DPlot.getXOffsetDouble() == null ? StackedBarRenderer3D.DEFAULT_X_OFFSET : bar3DPlot.getXOffsetDouble().doubleValue(),
				bar3DPlot.getYOffsetDouble() == null ? StackedBarRenderer3D.DEFAULT_Y_OFFSET : bar3DPlot.getYOffsetDouble().doubleValue()
				);

		stackedBarRenderer3D.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		stackedBarRenderer3D.setBaseItemLabelsVisible(bar3DPlot.getShowLabels() == null ? false : bar3DPlot.getShowLabels().booleanValue());

		categoryPlot.setRenderer(stackedBarRenderer3D);

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), bar3DPlot.getCategoryAxisLabelFont(),
				bar3DPlot.getCategoryAxisLabelColor(), bar3DPlot.getCategoryAxisTickLabelFont(),
				bar3DPlot.getCategoryAxisTickLabelColor(), bar3DPlot.getCategoryAxisTickLabelMask(),
				bar3DPlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), bar3DPlot.getValueAxisLabelFont(),
				bar3DPlot.getValueAxisLabelColor(), bar3DPlot.getValueAxisTickLabelFont(),
				bar3DPlot.getValueAxisTickLabelColor(), bar3DPlot.getValueAxisTickLabelMask(),
				bar3DPlot.getOwnValueAxisLineColor());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createStackedBarChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart =
			ChartFactory.createStackedBarChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)getDataset().getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		JRFillBarPlot barPlot = (JRFillBarPlot)getPlot();
		//plot.setNoDataMessage("No data to display");
		boolean isShowTickMarks = barPlot.getShowTickMarks() == null ? true : barPlot.getShowTickMarks().booleanValue();
		boolean isShowTickLabels = barPlot.getShowTickLabels() == null ? true : barPlot.getShowTickLabels().booleanValue();
		boolean isShowLabels = barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue();
		
		categoryPlot.getDomainAxis().setTickMarksVisible(isShowTickMarks);
		categoryPlot.getDomainAxis().setTickLabelsVisible(isShowTickLabels);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(isShowTickMarks);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(isShowTickLabels);

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		categoryRenderer.setBaseItemLabelsVisible(isShowLabels);

		// Handle the axis formating for the catagory axis
		configureAxis(categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getOwnValueAxisLineColor());

		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createStackedAreaChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart =
			ChartFactory.createStackedAreaChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)getDataset().getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);
		JRFillAreaPlot areaPlot = (JRFillAreaPlot)getPlot();

		// Handle the axis formating for the catagory axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(((CategoryPlot)jfreeChart.getPlot()).getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnValueAxisLineColor());

		((CategoryPlot)jfreeChart.getPlot()).getDomainAxis().setCategoryMargin(0);
		
		return jfreeChart;
	}

	protected JFreeChart createXyAreaChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = 
			ChartFactory.createXYAreaChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(XYDataset)getDataset().getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);
		JRAreaPlot areaPlot = (JRAreaPlot)getPlot();

		// Handle the axis formating for the catagory axis
		configureAxis(jfreeChart.getXYPlot().getDomainAxis(), areaPlot.getCategoryAxisLabelFont(),
				areaPlot.getCategoryAxisLabelColor(), areaPlot.getCategoryAxisTickLabelFont(),
				areaPlot.getCategoryAxisTickLabelColor(), areaPlot.getCategoryAxisTickLabelMask(),
				areaPlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(jfreeChart.getXYPlot().getRangeAxis(), areaPlot.getValueAxisLabelFont(),
				areaPlot.getValueAxisLabelColor(), areaPlot.getValueAxisTickLabelFont(),
				areaPlot.getValueAxisTickLabelColor(), areaPlot.getValueAxisTickLabelMask(),
				areaPlot.getOwnValueAxisLineColor());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createXYBarChart(byte evaluation) throws JRException
	{
		IntervalXYDataset tmpDataset = (IntervalXYDataset)getDataset().getDataset();

		boolean isDate = true;
		if( getDataset().getDatasetType() == JRChartDataset.XY_DATASET ){
			isDate = false;
		}

		JFreeChart jfreeChart =
			ChartFactory.createXYBarChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				isDate,
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				tmpDataset,
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
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
		boolean isShowLabels = barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue();
		
		itemRenderer.setBaseItemLabelsVisible( isShowLabels );

		// Handle the axis formating for the catagory axis
		configureAxis(xyPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
				barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
				barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
				barPlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
				barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
				barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
				barPlot.getOwnValueAxisLineColor());

		return jfreeChart;
	}


	protected JFreeChart createXyLineChart(byte evaluation) throws JRException 
	{
		JRLinePlot linePlot = (JRLinePlot) getPlot();

		JFreeChart jfreeChart = 
			ChartFactory.createXYLineChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(linePlot.getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(linePlot.getValueAxisLabelExpression(), evaluation ),
				(XYDataset)getDataset().getDataset(),
				linePlot.getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(jfreeChart, getPlot(), evaluation);

		// Handle the axis formating for the catagory axis
		configureAxis(jfreeChart.getXYPlot().getDomainAxis(), linePlot.getCategoryAxisLabelFont(),
				linePlot.getCategoryAxisLabelColor(), linePlot.getCategoryAxisTickLabelFont(),
				linePlot.getCategoryAxisTickLabelColor(), linePlot.getCategoryAxisTickLabelMask(),
				linePlot.getOwnCategoryAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(jfreeChart.getXYPlot().getRangeAxis(), linePlot.getValueAxisLabelFont(),
				linePlot.getValueAxisLabelColor(), linePlot.getValueAxisTickLabelFont(),
				linePlot.getValueAxisTickLabelColor(), linePlot.getValueAxisTickLabelMask(),
				linePlot.getOwnValueAxisLineColor());

		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer) jfreeChart.getXYPlot().getRenderer();
		boolean isShowShapes = linePlot.getShowShapes() == null ? true : linePlot.getShowShapes().booleanValue();
		boolean isShowLines = linePlot.getShowLines() == null ? true : linePlot.getShowLines().booleanValue();
		lineRenderer.setBaseShapesVisible(isShowShapes);
		lineRenderer.setBaseLinesVisible(isShowLines);

		return jfreeChart;
	}

	protected JFreeChart createTimeSeriesChart(byte evaluation) throws JRException 
	{
		String timeAxisLabel = (String)evaluateExpression(((JRTimeSeriesPlot)getPlot()).getTimeAxisLabelExpression(), evaluation);
		String valueAxisLabel = (String)evaluateExpression(((JRTimeSeriesPlot)getPlot()).getValueAxisLabelExpression(), evaluation);

		JFreeChart jfreeChart = ChartFactory.createTimeSeriesChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				timeAxisLabel,
				valueAxisLabel,
				(TimeSeriesCollection)getDataset().getDataset(),
				isShowLegend(),
				true,
				false );

		configureChart(jfreeChart, getPlot(), evaluation);

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
				timeSeriesPlot.getOwnTimeAxisLineColor());

		// Handle the axis formating for the value axis
		configureAxis(xyPlot.getRangeAxis(), timeSeriesPlot.getValueAxisLabelFont(),
				timeSeriesPlot.getValueAxisLabelColor(), timeSeriesPlot.getValueAxisTickLabelFont(),
				timeSeriesPlot.getValueAxisTickLabelColor(), timeSeriesPlot.getValueAxisTickLabelMask(),
				timeSeriesPlot.getOwnValueAxisLineColor());

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createGanttChart(byte evaluation) throws JRException
	{
		GanttCategoryDataset ganttCategoryDataset = (GanttCategoryDataset)getDataset().getDataset();
		//FIXMECHART legend/tooltip/url should come from plot?
		
		JFreeChart jfreeChart =
			ChartFactory.createGanttChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				ganttCategoryDataset,
				isShowLegend(),
				true,  //FIXMECHART tooltip: I guess BarPlot is not the best for gantt
				false
				);

		configureChart(jfreeChart, getPlot(), evaluation);
		
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		//plot.setNoDataMessage("No data to display");
		
		JRFillBarPlot barPlot = (JRFillBarPlot)getPlot();
		boolean isShowTickMarks = barPlot.getShowTickMarks() == null ? true : barPlot.getShowTickMarks().booleanValue();
		boolean isShowTickLabels = barPlot.getShowTickLabels() == null ? true : barPlot.getShowTickLabels().booleanValue();
		boolean isShowLabels = barPlot.getShowLabels() == null ? false : barPlot.getShowLabels().booleanValue();
		
		categoryPlot.getDomainAxis().setTickMarksVisible(isShowTickMarks);
		categoryPlot.getDomainAxis().setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the catagory axis
		configureAxis(
			categoryPlot.getDomainAxis(), barPlot.getCategoryAxisLabelFont(),
			barPlot.getCategoryAxisLabelColor(), barPlot.getCategoryAxisTickLabelFont(),
			barPlot.getCategoryAxisTickLabelColor(), barPlot.getCategoryAxisTickLabelMask(),
			barPlot.getOwnCategoryAxisLineColor()
			);
		((DateAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(isShowTickMarks);
		((DateAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(isShowTickLabels);
		// Handle the axis formating for the value axis
		configureAxis(
			categoryPlot.getRangeAxis(), barPlot.getValueAxisLabelFont(),
			barPlot.getValueAxisLabelColor(), barPlot.getValueAxisTickLabelFont(),
			barPlot.getValueAxisTickLabelColor(), barPlot.getValueAxisTickLabelMask(),
			barPlot.getOwnValueAxisLineColor()
			);

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator(((JRFillGanttDataset)getDataset()).getLabelGenerator());
		categoryRenderer.setBaseItemLabelsVisible(isShowLabels);

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

	/**
	 * Build and configure a meter chart.
	 *
	 * @param evaluation current expression evaluation phase
	 * @throws JRException
	*/
	protected JFreeChart createMeterChart( byte evaluation ) throws JRException 
	{
		JRFillMeterPlot jrPlot = (JRFillMeterPlot)getPlot();

		// Start by creating the plot that wil hold the meter
		MeterPlot chartPlot = new MeterPlot((ValueDataset)getDataset().getDataset());

		// Set the shape
		int shape = jrPlot.getShapeByte() == null ? JRMeterPlot.SHAPE_PIE : jrPlot.getShapeByte().intValue();
		if (shape == JRMeterPlot.SHAPE_CHORD)
			chartPlot.setDialShape(DialShape.CHORD);
		else if (shape == JRMeterPlot.SHAPE_CIRCLE)
			chartPlot.setDialShape(DialShape.CIRCLE);
		else
			chartPlot.setDialShape(DialShape.PIE);

		// Set the meter's range
		chartPlot.setRange(convertRange(jrPlot.getDataRange(), evaluation));

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
				chartPlot.setValueFont(JRFontUtil.getAwtFont(display.getFont()));
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
		JFreeChart jfreeChart = 
			new JFreeChart(
				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
				null, 
				chartPlot, 
				isShowLegend()
				);

		// Set all the generic options
		configureChart(jfreeChart, getPlot(), evaluation);

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
	protected JFreeChart createThermometerChart( byte evaluation ) throws JRException 
	{
		JRFillThermometerPlot jrPlot = (JRFillThermometerPlot)getPlot();

		// Create the plot that will hold the thermometer.
		ThermometerPlot chartPlot = new ThermometerPlot((ValueDataset)getDataset().getDataset());

		Range range = convertRange(jrPlot.getDataRange(), evaluation);

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
				chartPlot.setValueFont(JRFontUtil.getAwtFont(display.getFont()));
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
		JFreeChart jfreeChart = new JFreeChart(chartPlot);

		// Set the generic options
		configureChart(jfreeChart, getPlot(), evaluation);
		
		return jfreeChart;
	}

	/**
	 * Specifies the axis location.
	 * It has to be overriden for child themes with another default axis location
	 */
	protected AxisLocation getChartAxisLocation(JRFillChartAxis chartAxis)
	{
		return chartAxis.getPositionByte() != null && chartAxis.getPositionByte().byteValue() == JRChartAxis.POSITION_RIGHT_OR_BOTTOM
				? AxisLocation.BOTTOM_OR_RIGHT 
				: DEFAULT_AXIS_LOCATION;
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

	
	public static final ChartThemeBundle BUNDLE = 
		new ChartThemeBundle()
		{
			private static final String NAME = "default";

			public String[] getChartThemeNames() 
			{
				return new String[]{NAME};
			}
		
			public ChartTheme getChartTheme(String themeName) 
			{
				if (NAME.equals(themeName))
				{
					return new DefaultChartTheme(); 
				}
				return null;
			}
		};
		
	/**
	 * Specifies whether a chart legend should be visible or no by default.
	 * It has to be overriden for child themes which don't show chart legends
	 */
	protected boolean isShowLegend()
	{
		return getChart().getShowLegend() == null ? DEFAULT_SHOW_lEGEND : getChart().getShowLegend().booleanValue();
	}
}
