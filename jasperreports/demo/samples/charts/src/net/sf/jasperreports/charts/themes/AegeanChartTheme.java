package net.sf.jasperreports.charts.themes;
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;

import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.fill.JRFillMeterPlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.charts.fill.JRFillThermometerPlot;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.util.JRFontUtil;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class AegeanChartTheme extends GenericChartTheme
{
	public static final Color THERMOMETER_COLOR = Color.BLACK;
//	public static final Color MARKER_COLOR = new Color(210,210,210);

	/**
	 *
	 */
	public AegeanChartTheme()
	{
	}

	/**
	 *
	 */
	protected void configureChart(JFreeChart jfreeChart, JRChartPlot jrPlot, byte evaluation) throws JRException
	{

		super.configureChart(jfreeChart, jrPlot, evaluation);
		TextTitle title = jfreeChart.getTitle();

		if(title != null)
		{
			
			RectangleInsets padding = title.getPadding();
			double bottomPadding = Math.max(padding.getBottom(), 15d);
			title.setPadding(padding.getTop(), padding.getLeft(), bottomPadding, padding.getRight());
		}
	}

	/**
	 *
	 */
	protected void configurePlot(Plot plot, JRChartPlot jrPlot)
	{

		super.configurePlot(plot, jrPlot);

		if(plot instanceof CategoryPlot)
		{
			CategoryPlot categoryPlot = (CategoryPlot)plot;
			CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
			CategoryDataset categoryDataset = categoryPlot.getDataset();
			for(int i = 0; i < categoryDataset.getRowCount(); i++)
			{
				categoryRenderer.setSeriesOutlinePaint(i, ChartThemesConstants.TRANSPARENT_PAINT);
			}
			categoryPlot.setRangeGridlinePaint(ChartThemesConstants.GRAY_PAINT_217);
			categoryPlot.setRangeGridlineStroke(new BasicStroke(0.5f));
			categoryPlot.setDomainGridlinesVisible(false);
			categoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		}
		else if(plot instanceof XYPlot)
		{
			XYPlot xyPlot = (XYPlot)plot;
			XYDataset xyDataset = xyPlot.getDataset();
			XYItemRenderer xyItemRenderer = xyPlot.getRenderer();
			for(int i = 0; i < xyDataset.getSeriesCount(); i++)
			{
				xyItemRenderer.setSeriesOutlinePaint(i, ChartThemesConstants.TRANSPARENT_PAINT);
			}
			xyPlot.setRangeGridlinePaint(ChartThemesConstants.GRAY_PAINT_217);
			xyPlot.setRangeGridlineStroke(new BasicStroke(0.5f));
			xyPlot.setDomainGridlinesVisible(false);
			xyPlot.setRangeZeroBaselineVisible(true);
		}
	}

	protected JFreeChart createScatterChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createScatterChart(evaluation);
		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		xyPlot.setDomainGridlinesVisible(false);
		XYLineAndShapeRenderer plotRenderer = (XYLineAndShapeRenderer) ((XYPlot)jfreeChart.getPlot()).getRenderer();
		plotRenderer.setBaseShapesFilled(false);
		plotRenderer.setBaseStroke(new BasicStroke(1f));
		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createPieChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPieChart(evaluation);

		PiePlot piePlot = (PiePlot)jfreeChart.getPlot();
		piePlot.setLabelBackgroundPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		piePlot.setLabelShadowPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		piePlot.setLabelOutlinePaint(ChartThemesConstants.TRANSPARENT_PAINT);
		piePlot.setShadowXOffset(0);
		piePlot.setShadowYOffset(0);
		PieDataset pieDataset = piePlot.getDataset();
		for(int i = 0; i < pieDataset.getItemCount(); i++)
		{
//			piePlot.setSectionOutlinePaint(pieDataset.getKey(i), ChartThemesConstants.TRANSPARENT_PAINT);
			piePlot.setSectionOutlinePaint(pieDataset.getKey(i), Color.BLACK);
			//makes pie colors darker
			//piePlot.setSectionPaint(pieDataset.getKey(i), GRADIENT_PAINTS[i]);
		}

		if (
			((JRFillPieDataset)getDataset()).getLabelGenerator() == null
			&& ((JRFillPiePlot)getPlot()).getLabelFormat() == null
			)
		{
			piePlot.setLabelGenerator(null);
		}

		if (((JRFillPiePlot)getPlot()).getLegendLabelFormat() == null)
		{
			piePlot.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator("{0}")
				);
		}
		piePlot.setCircular(true);
		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createPie3DChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPie3DChart(evaluation);

		PiePlot3D piePlot3D = (PiePlot3D) jfreeChart.getPlot();
		piePlot3D.setLabelBackgroundPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		piePlot3D.setLabelShadowPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		piePlot3D.setLabelOutlinePaint(ChartThemesConstants.TRANSPARENT_PAINT);
		piePlot3D.setDarkerSides(true);
		piePlot3D.setDepthFactor(0.07);
//does not work for 3D
//		piePlot3D.setShadowXOffset(5);
//		piePlot3D.setShadowYOffset(10);
//		piePlot3D.setShadowPaint(new GradientPaint(
//				0,
//				chart.getHeight() / 2,
//				new Color(41, 120, 162),
//				0,
//				chart.getHeight(),
//				Color.white)
//		);

		PieDataset pieDataset = piePlot3D.getDataset();

		for(int i = 0; i < pieDataset.getItemCount(); i++)
		{
			piePlot3D.setSectionOutlinePaint(pieDataset.getKey(i), ChartThemesConstants.TRANSPARENT_PAINT);
		}

		if (
			((JRFillPieDataset)getDataset()).getLabelGenerator() == null
			&& ((JRFillPie3DPlot)getPlot()).getLabelFormat() == null
			)
		{
			piePlot3D.setLabelGenerator(null);
		}

		if (((JRFillPie3DPlot)getPlot()).getLegendLabelFormat() == null)
		{
			piePlot3D.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator("{0}")
				);
		}
//		piePlot3D.setLabelFont(new Font("Tahoma", Font.PLAIN, 4));
		piePlot3D.setCircular(true);
		return jfreeChart;
	}

	protected JFreeChart createBar3DChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createBar3DChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		BarRenderer3D barRenderer3D = (BarRenderer3D)categoryPlot.getRenderer();
		barRenderer3D.setItemMargin(0);
		barRenderer3D.setWallPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		//categoryPlot.setOrientation(PlotOrientation.HORIZONTAL);
		CategoryDataset categoryDataset = categoryPlot.getDataset();
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer3D.setSeriesOutlinePaint(i, ChartThemesConstants.TRANSPARENT_PAINT);
		}
		
		return jfreeChart;
	}


	protected JFreeChart createStackedBar3DChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createStackedBar3DChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		BarRenderer3D barRenderer3D = (BarRenderer3D)categoryPlot.getRenderer();
		barRenderer3D.setWallPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		barRenderer3D.setItemMargin(0);
		CategoryDataset categoryDataset = categoryPlot.getDataset();
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer3D.setSeriesOutlinePaint(i, ChartThemesConstants.TRANSPARENT_PAINT);
		}
		
		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createBubbleChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createBubbleChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		XYDataset xyDataset = xyPlot.getDataset();
		XYBubbleRenderer bubbleRenderer = (XYBubbleRenderer)xyPlot.getRenderer();
		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
		{
			bubbleRenderer.setSeriesOutlinePaint(i, ChartThemesConstants.TRANSPARENT_PAINT);
		}
		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createGanttChart(byte evaluation) throws JRException
	{

		JFreeChart jfreeChart = super.createGanttChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		categoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		categoryPlot.setDomainGridlinesVisible(true);
		categoryPlot.setDomainGridlinePosition(CategoryAnchor.END);
		categoryPlot.setDomainGridlineStroke(new BasicStroke(
				0.5f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				50,
				new float[] {1},
				0
				)
		);

		categoryPlot.setDomainGridlinePaint(ChartThemesConstants.GRAY_PAINT_217);

		categoryPlot.setRangeGridlinesVisible(true);
		categoryPlot.setRangeGridlineStroke(new BasicStroke(
				0.5f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				50,
				new float[] {1},
				0
				)
		);

		categoryPlot.setRangeGridlinePaint(ChartThemesConstants.GRAY_PAINT_217);
//		JRBarPlot barPlot = (BarPlot)categoryPlot;
//		categoryPlot.getDomainAxis().setTickLabelsVisible(
//				categoryPlot.getShowTickLabels() == null ? true : barPlot.getShowTickLabels().
//				true
//				);
		CategoryDataset categoryDataset = categoryPlot.getDataset();
		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelsVisible(true);
		BarRenderer barRenderer = (BarRenderer)categoryRenderer;
		List seriesPaints = (List)getDefaultValue(defaultChartPropertiesMap, ChartThemesConstants.DEFAULT_SERIES_COLORS);
		barRenderer.setSeriesPaint(0, (Paint)seriesPaints.get(3));
		barRenderer.setSeriesPaint(1, (Paint)seriesPaints.get(0));
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer.setSeriesItemLabelFont(i, categoryPlot.getDomainAxis().getTickLabelFont());
			barRenderer.setSeriesItemLabelsVisible(i, true);
//			barRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
//			CategoryMarker categoryMarker = new CategoryMarker(categoryDataset.getColumnKey(i),MARKER_COLOR, new BasicStroke(1f));
//			categoryMarker.setAlpha(0.5f);
//			categoryPlot.addDomainMarker(categoryMarker, Layer.BACKGROUND);
		}
		categoryPlot.setOutlinePaint(Color.DARK_GRAY);
		categoryPlot.setOutlineStroke(new BasicStroke(1.5f));
		categoryPlot.setOutlineVisible(true);
		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createMeterChart(byte evaluation) throws JRException
	{
		return createDialChart(evaluation);
//		JRFillMeterPlot jrPlot = (JRFillMeterPlot)getPlot();
//
//		// Start by creating the plot that will hold the meter
//		MeterPlot chartPlot = new MeterPlot((ValueDataset)getDataset().getDataset());
//
//		// Set the shape
//		int shape = jrPlot.getShape();
//		if (shape == JRMeterPlot.SHAPE_CHORD)
//			chartPlot.setDialShape(DialShape.CHORD);
//		else if (shape == JRMeterPlot.SHAPE_CIRCLE)
//		chartPlot.setDialShape(DialShape.CIRCLE);
//		else
//			chartPlot.setDialShape(DialShape.PIE);
//
//		// Set the meter's range
//		chartPlot.setRange(convertRange(jrPlot.getDataRange(), evaluation));
//
//		// Set the size of the meter
//		chartPlot.setMeterAngle(jrPlot.getMeterAngle());
//
//		// Set the units - this is just a string that will be shown next to the
//		// value
//		String units = jrPlot.getUnits();
//		if (units != null && units.length() > 0)
//			chartPlot.setUnits(units);
//
//		// Set the spacing between ticks.  I hate the name "tickSize" since to me it
//		// implies I am changing the size of the tick, not the spacing between them.
//		chartPlot.setTickSize(jrPlot.getTickInterval());
//
//		JRValueDisplay display = jrPlot.getValueDisplay();
//		JRFont jrFont = display.getFont();
//
//		if (jrFont != null)
//		{
//			chartPlot.setTickLabelFont(new Font(JRFontUtil.getAttributes(jrFont)).deriveFont(Font.BOLD));
//		}
//
//
//		// Set all the colors we support
//		//Color color = jrPlot.getMeterBackgroundColor();
//		//if (color != null)
//
//		chartPlot.setDialBackgroundPaint(GRIDLINE_COLOR);
//
//		//chartPlot.setForegroundAlpha(1f);
//		chartPlot.setNeedlePaint(Color.DARK_GRAY);
//
//		// Set how the value is displayed.
//		if (display != null)
//		{
//			if (display.getColor() != null)
//			{
//				chartPlot.setValuePaint(display.getColor());
//			}
//
//			if (display.getMask() != null)
//			{
//				chartPlot.setTickLabelFormat(new DecimalFormat(display.getMask()));
//			}
//
//			if (jrFont != null)
//			{
//				Font font = new Font(JRFontUtil.getAttributes(jrFont));
//				if(jrFont.isOwnBold() == null)
//				{
//					font = font.deriveFont(Font.BOLD);
//				}
//				chartPlot.setValueFont(font);
//			}
//
//		}
//
//		chartPlot.setTickPaint(Color.BLACK);
//
//		// Now define all of the intervals, setting their range and color
//		List intervals = jrPlot.getIntervals();
//		if (intervals != null)
//		{
//			Iterator iter = intervals.iterator();
//			int i = 0;
//			while (iter.hasNext())
//			{
//				JRMeterInterval interval = (JRMeterInterval)iter.next();
//				interval.setBackgroundColor(COLORS[i]);
//				i++;
//				interval.setAlpha(1f);
//				chartPlot.addInterval(convertInterval(interval, evaluation));
//			}
//		}
//
//		// Actually create the chart around the plot
//		JFreeChart jfreeChart =
//			new JFreeChart(
//				(String)evaluateExpression(chart.getTitleExpression(), evaluation),
//				null,
//				chartPlot,
//				chart.isShowLegend()
//				);
//
//		// Set all the generic options
//		configureChart(jfreeChart, getPlot(), evaluation);
//
//		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createThermometerChart(byte evaluation) throws JRException
	{
		JRFillThermometerPlot jrPlot = (JRFillThermometerPlot)getPlot();

		// Create the plot that will hold the thermometer.
		ThermometerPlot chartPlot = new ThermometerPlot((ValueDataset)getDataset().getDataset());
		// Build a chart around this plot
		JFreeChart jfreeChart = new JFreeChart(chartPlot);

		// Set the generic options
		configureChart(jfreeChart, getPlot(), evaluation);
		jfreeChart.setBackgroundPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		jfreeChart.setBorderVisible(false);

		Range range = convertRange(jrPlot.getDataRange(), evaluation);

		// Set the boundary of the thermomoter
		chartPlot.setLowerBound(range.getLowerBound());
		chartPlot.setUpperBound(range.getUpperBound());
		chartPlot.setGap(0);

		// Units can only be Fahrenheit, Celsius or none, so turn off for now.
		chartPlot.setUnits(ThermometerPlot.UNITS_NONE);

		// Set the color of the mercury.  Only used when the value is outside of
		// any defined ranges.
		List seriesPaints = (List)getDefaultValue(defaultChartPropertiesMap, ChartThemesConstants.DEFAULT_SERIES_COLORS);
		
		Paint paint = (jrPlot.getMercuryColor() != null ? (Paint)jrPlot.getMercuryColor() : (Paint)seriesPaints.get(0));
		chartPlot.setMercuryPaint(paint);

		chartPlot.setThermometerPaint(THERMOMETER_COLOR);
		chartPlot.setThermometerStroke(new BasicStroke(2f));
		chartPlot.setOutlineVisible(false);
		chartPlot.setValueFont(chartPlot.getValueFont().deriveFont(Font.BOLD));



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
//				chartPlot.setValueFont(JRFontUtil.getAwtFont(display.getFont()).deriveFont(Font.BOLD));//FIXMETHEME check lite font everywhere
			}
		}

		// Set the location of where the value is displayed
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

		return jfreeChart;

//
//
//
//
//
//		JFreeChart jfreeChart = super.createThermometerChart(evaluation);
//		ThermometerPlot thermometerPlot = (ThermometerPlot)jfreeChart.getPlot();
//		thermometerPlot.setMercuryPaint(GRADIENT_PAINTS[0]);
//		thermometerPlot.setThermometerPaint(THERMOMETER_COLOR);
//		thermometerPlot.setThermometerStroke(new BasicStroke(2f));
//		thermometerPlot.setGap(2);
//		thermometerPlot.setForegroundAlpha(1f);
//		thermometerPlot.setValueFont(thermometerPlot.getValueFont().deriveFont(Font.BOLD));
//		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createDialChart(byte evaluation) throws JRException
	{

		JRFillMeterPlot jrPlot = (JRFillMeterPlot)getPlot();

		// get data for diagrams
		DialPlot dialPlot = new DialPlot();
		dialPlot.setDataset((ValueDataset)getDataset().getDataset());
		StandardDialFrame dialFrame = new StandardDialFrame();
		dialFrame.setForegroundPaint(Color.BLACK);
		dialFrame.setBackgroundPaint(Color.BLACK);
		dialFrame.setStroke(new BasicStroke(1f));
		dialPlot.setDialFrame(dialFrame);

		DialBackground db = new DialBackground(ChartThemesConstants.TRANSPARENT_PAINT);
		dialPlot.setBackground(db);
		JRValueDisplay display = jrPlot.getValueDisplay();
		JRFont jrFont = display != null  && display.getFont() != null ?
				display.getFont() :
				new JRBaseFont(null, null, chart, chart.getLegendFont());

		Range range = convertRange(jrPlot.getDataRange(), evaluation);
		double bound = Math.max(Math.abs(range.getUpperBound()), Math.abs(range.getLowerBound()));
		int dialUnitScale = ChartThemesUtilities.getScale(bound);

		double lowerBound = ChartThemesUtilities.getTruncatedValue(range.getLowerBound(), dialUnitScale);
		double upperBound = ChartThemesUtilities.getTruncatedValue(range.getUpperBound(), dialUnitScale);

		ScaledDialScale scale =
			new ScaledDialScale(
				lowerBound,
				upperBound,
				210,
				-240,
				(upperBound - lowerBound)/6,
				1
				);
		scale.setTickRadius(0.9);
		scale.setTickLabelOffset(0.16);
		scale.setTickLabelFont(JRFontUtil.getAwtFont(jrFont).deriveFont(16f).deriveFont(Font.BOLD));
		scale.setMajorTickStroke(new BasicStroke(1f));
		scale.setMinorTickStroke(new BasicStroke(0.7f));
		scale.setMajorTickPaint(Color.BLACK);
		scale.setMinorTickPaint(Color.BLACK);
		scale.setTickLabelsVisible(true);
		scale.setFirstTickLabelVisible(true);
		if((lowerBound == (int)lowerBound &&
				upperBound == (int)upperBound &&
				scale.getMajorTickIncrement() == (int)scale.getMajorTickIncrement()) ||
				dialUnitScale > 1
				)
		{
			scale.setTickLabelFormatter(new DecimalFormat("#,##0"));
		}
		else if(dialUnitScale == 1)
		{

			scale.setTickLabelFormatter(new DecimalFormat("#,##0.0"));
		}
		else if(dialUnitScale <= 0)
		{
			scale.setTickLabelFormatter(new DecimalFormat("#,##0.00"));
		}
		dialPlot.addScale(0, scale);
		
		List intervals = jrPlot.getIntervals();
		if (intervals != null && intervals.size() > 0)
		{
			int size = Math.min(3, intervals.size());
			for(int i = 0; i < size; i++)
			{
				JRMeterInterval interval = (JRMeterInterval)intervals.get(i);
				Range intervalRange = convertRange(interval.getDataRange(), evaluation);
				double intervalLowerBound = ChartThemesUtilities.getTruncatedValue(intervalRange.getLowerBound(), dialUnitScale);
				double intervalUpperBound = ChartThemesUtilities.getTruncatedValue(intervalRange.getUpperBound(), dialUnitScale);

				ScaledDialRange dialRange =
					new ScaledDialRange(
						intervalLowerBound,
						intervalUpperBound,
						interval.getBackgroundColor() == null
							? (Color)ChartThemesConstants.AEGEAN_INTERVAL_COLORS.get(i)
							: interval.getBackgroundColor(),
						15f
						);
				dialRange.setInnerRadius(0.5);
				dialRange.setOuterRadius(0.5);
				dialPlot.addLayer(dialRange);
			}
			
			if(intervals.size() > 3)
			{
				int colorStep = 255 / (intervals.size() - 3);
				for(int i = 3; i < intervals.size(); i++)
				{
					JRMeterInterval interval = (JRMeterInterval)intervals.get(i);
					Range intervalRange = convertRange(interval.getDataRange(), evaluation);
					double intervalLowerBound = ChartThemesUtilities.getTruncatedValue(intervalRange.getLowerBound(), dialUnitScale);
					double intervalUpperBound = ChartThemesUtilities.getTruncatedValue(intervalRange.getUpperBound(), dialUnitScale);
	
					ScaledDialRange dialRange =
						new ScaledDialRange(
							intervalLowerBound,
							intervalUpperBound,
							interval.getBackgroundColor() == null
								? new Color(255 - colorStep * (i - 3), 0 + colorStep * (i - 3), 0)
								: interval.getBackgroundColor(),
							15f
							);
					dialRange.setInnerRadius(0.5);
					dialRange.setOuterRadius(0.5);
					dialPlot.addLayer(dialRange);
				}
			}
		}

       String displayVisibility = display != null && chart.hasProperties() ? 
       		chart.getPropertiesMap().getProperty("net.sf.jasperreports.chart.dial.value.display.visible") : "false";
       
       if(Boolean.valueOf(displayVisibility).booleanValue())
       {
       	ScaledDialValueIndicator dvi = new ScaledDialValueIndicator(0, dialUnitScale);
	        dvi.setBackgroundPaint(ChartThemesConstants.TRANSPARENT_PAINT);
//	        dvi.setFont(JRFontUtil.getAwtFont(jrFont).deriveFont(10f).deriveFont(Font.BOLD));
	        dvi.setOutlinePaint(ChartThemesConstants.TRANSPARENT_PAINT);
	        dvi.setPaint(Color.WHITE);
	        
	        String pattern = display.getMask() != null ? display.getMask() : "#,##0.####";
	        if(pattern != null)
	        	dvi.setNumberFormat( new DecimalFormat(pattern));
	        dvi.setRadius(0.15);
	        dvi.setValueAnchor(RectangleAnchor.CENTER);
	        dvi.setTextAnchor(TextAnchor.CENTER);
	        //dvi.setTemplateValue(Double.valueOf(getDialTickValue(dialPlot.getValue(0),dialUnitScale)));
	        dialPlot.addLayer(dvi);
       }
		
		String label = chart.hasProperties() ?
				chart.getPropertiesMap().getProperty("net.sf.jasperreports.chart.dial.label") : null;

		if(label != null)
		{
			if(dialUnitScale < 0)
				label = new MessageFormat(label).format(new Object[]{String.valueOf(Math.pow(10, dialUnitScale))});
			else if(dialUnitScale < 3)
				label = new MessageFormat(label).format(new Object[]{"1"});
			else
				label = new MessageFormat(label).format(new Object[]{String.valueOf((int)Math.pow(10, dialUnitScale-2))});

			String[] textLines = label.split("\\n");
			for(int i = 0; i < textLines.length; i++)
			{
				DialTextAnnotation dialAnnotation = new DialTextAnnotation(textLines[i]);
//				dialAnnotation.setFont(JRFontUtil.getAwtFont(jrFont).deriveFont(Font.BOLD));
				dialAnnotation.setPaint(Color.BLACK);
				dialAnnotation.setRadius(Math.sin(Math.PI/6.0) + i/10.0);
				dialAnnotation.setAnchor(TextAnchor.CENTER);
				dialPlot.addLayer(dialAnnotation);
			}
		}

		DialPointer needle = new ScaledDialPointer(dialUnitScale, 0.047);

		needle.setVisible(true);
		needle.setRadius(0.7);
		dialPlot.addLayer(needle);

		DialCap cap = new DialCap();
		cap.setRadius(0.05);
		cap.setFillPaint(Color.BLACK);
		cap.setOutlinePaint(ChartThemesConstants.TRANSPARENT_PAINT);
		dialPlot.setCap(cap);

		JFreeChart jfreeChart =
		new JFreeChart(
			(String)evaluateExpression(chart.getTitleExpression(), evaluation),
			null,
			dialPlot,
			chart.getShowLegend() == null ? false : chart.getShowLegend().booleanValue()
			);

		// Set all the generic options
		configureChart(jfreeChart, getPlot(), evaluation);

		jfreeChart.setBackgroundPaint(ChartThemesConstants.TRANSPARENT_PAINT);
		jfreeChart.setBorderVisible(false);

		return jfreeChart;

	}

	/**
	 *
	 */
	protected JFreeChart createCandlestickChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createCandlestickChart(evaluation);
		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		CandlestickRenderer renderer = (CandlestickRenderer)xyPlot.getRenderer();
		DefaultHighLowDataset dataset = (DefaultHighLowDataset)xyPlot.getDataset();
		List seriesPaints = (List)getDefaultValue(defaultChartPropertiesMap, ChartThemesConstants.DEFAULT_SERIES_COLORS);

		for(int i = 0; i < dataset.getSeriesCount(); i++)
		{
			
			renderer.setSeriesFillPaint(i, (Paint)seriesPaints.get(i));
			renderer.setSeriesPaint(i, Color.DARK_GRAY);
		}
		return jfreeChart;
	}
}