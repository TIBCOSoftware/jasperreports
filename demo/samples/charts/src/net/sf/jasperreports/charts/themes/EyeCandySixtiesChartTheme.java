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
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.fill.JRFillMeterPlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.charts.fill.JRFillThermometerPlot;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.fill.DefaultChartTheme;
import net.sf.jasperreports.engine.fill.JRFillChart;
import net.sf.jasperreports.engine.util.JRFontUtil;

import org.jfree.chart.HashUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialLayerChangeEvent;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialScale;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLine3DRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.text.TextUtilities;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.ui.TextAnchor;
import org.jfree.util.UnitType;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class EyeCandySixtiesChartTheme extends DefaultChartTheme
{

	public static final Color[] COLORS = 
		new Color[]{
			new Color(250, 97, 18),
			new Color(237, 38, 42),
			new Color(0, 111, 60),
			new Color(250, 223, 18),
			new Color(47, 137, 187),
			new Color(231, 133, 35),
			new Color(229, 1, 140),
			new Color(234, 171, 53)
			};
	
    // gradient paints for series...
	public static final GradientPaint GRADIENT_PAINTS[] = new GradientPaint[]{
    		new GradientPaint(0.0f, 0.0f, COLORS[0], 0.0f, 0.0f, COLORS[0].darker()),
    		new GradientPaint(0.0f, 0.0f, COLORS[1], 0.0f, 0.0f, COLORS[1].darker()),
    		new GradientPaint(0.0f, 0.0f, COLORS[2], 0.0f, 0.0f, COLORS[2].darker()),
    		new GradientPaint(0.0f, 0.0f, COLORS[3], 0.0f, 0.0f, COLORS[3].darker()),
    		new GradientPaint(0.0f, 0.0f, COLORS[4], 0.0f, 0.0f, COLORS[4].darker()),
    		new GradientPaint(0.0f, 0.0f, COLORS[5], 0.0f, 0.0f, COLORS[5].darker()),
    		new GradientPaint(0.0f, 0.0f, COLORS[6], 0.0f, 0.0f, COLORS[6].darker())
    };
	
	public static final Color GRIDLINE_COLOR = new Color(134,134,134);
	public static final Color BORDER_COLOR = new Color(27,80,108);
	public static final Color THERMOMETER_COLOR = Color.BLACK;
//	public static final Color MARKER_COLOR = new Color(210,210,210);
	
	/**
	 *
	 */
	public EyeCandySixtiesChartTheme()
	{
	}
	
	
	/**
	 *
	 */
	protected void configureChart(JFreeChart jfreeChart, JRChartPlot jrPlot, byte evaluation) throws JRException
	{
		super.configureChart(jfreeChart, jrPlot, evaluation);
		JRFillChart chart = getChart();

		TextTitle title = jfreeChart.getTitle();
		float baseFontSize = chart.getLegendFont() != null ? chart.getLegendFont().getFontSize() : 8f;
		
		double chartPadding = 10d;
		
		if(title != null)
		{
			Font titleFont = title.getFont();
			
			if(chart.getTitleFont().isOwnBold() == null)
			{
				titleFont = titleFont.deriveFont(Font.BOLD);
			}

			if(chart.getTitleFont().getOwnFontSize() == null)
			{
				titleFont = titleFont.deriveFont(2.25f * baseFontSize);
			}

			title.setFont(titleFont);
			title.setHorizontalAlignment(HorizontalAlignment.CENTER);
			RectangleInsets padding = title.getPadding();
			title.setPadding(padding.getTop(), padding.getLeft(), padding.getBottom()+15, padding.getRight());
		}
		
		
		for(int i = 0; i < jfreeChart.getSubtitleCount(); i++)
		{
			Title subtitle = jfreeChart.getSubtitle(i);
			TextTitle textSubtitle = subtitle instanceof TextTitle ? (TextTitle)subtitle : null;
			if(textSubtitle != null)
			{
				Font subtitleFont = textSubtitle.getFont();
				
				if(chart.getSubtitleFont().isOwnBold() == null)
				{
					subtitleFont = subtitleFont.deriveFont(Font.PLAIN);
				}

				if(chart.getSubtitleFont().getOwnFontSize() == null)
				{
					subtitleFont = subtitleFont.deriveFont(baseFontSize);
				}

				textSubtitle.setFont(subtitleFont);
				textSubtitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
				subtitle.setPosition(RectangleEdge.BOTTOM);
				
			}
		}
		
		LegendTitle legend = jfreeChart.getLegend();
		if (legend != null)
		{
			Font legendFont = legend.getItemFont();
			
			if(chart.getLegendFont().isOwnBold() == null)
			{
				legendFont = legendFont.deriveFont(Font.PLAIN);
			}

			if(chart.getLegendFont().getOwnFontSize() == null)
			{
				legendFont = legendFont.deriveFont(baseFontSize);
			}
			
			legend.setItemFont(legendFont);
			legend.setFrame(BlockBorder.NONE);
			legend.setPosition(RectangleEdge.RIGHT);
			legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		}
		
		jfreeChart.setBackgroundPaint(
				new GradientPaint(0f, 0f, new Color(41, 120, 162), 0f, getChart().getHeight() * 0.7f, Color.WHITE, false)
				);
		jfreeChart.setAntiAlias(true);
		JRLineBox lineBox = chart.getLineBox();
		if(
			lineBox.getLeftPen().getLineWidth().floatValue() == 0	
			&& lineBox.getBottomPen().getLineWidth().floatValue() == 0
			&& lineBox.getRightPen().getLineWidth().floatValue() == 0
			&& lineBox.getTopPen().getLineWidth().floatValue() == 0
			)
		{
			jfreeChart.setBorderStroke(new BasicStroke(3f));
			jfreeChart.setBorderPaint(BORDER_COLOR);
			jfreeChart.setBorderVisible(true);
		}
		jfreeChart.setPadding(new RectangleInsets(UnitType.ABSOLUTE, chartPadding, chartPadding, chartPadding, chartPadding));	
		
	}


	/**
	 *
	 */
	protected void configurePlot(Plot plot, JRChartPlot jrPlot)
	{
		super.configurePlot(plot, jrPlot);

		// Set any color series
		SortedSet seriesColors = getPlot().getSeriesColors();//FIXMETHEME
		plot.setDrawingSupplier(
			new DefaultDrawingSupplier(
				COLORS,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
				)
			);
		
		if(plot instanceof CategoryPlot)
		{
			CategoryPlot categoryPlot = (CategoryPlot)plot;
			CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
			CategoryDataset categoryDataset = categoryPlot.getDataset();
			for(int i = 0; i < categoryDataset.getRowCount(); i++)
			{
				categoryRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
			}
			categoryPlot.setRangeGridlinePaint(GRIDLINE_COLOR);
			categoryPlot.setRangeGridlineStroke(new BasicStroke(1f));
			categoryPlot.setDomainGridlinesVisible(false);
			categoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
//			calculateTickUnits(categoryPlot.getRangeAxis());
		}
		else if(plot instanceof XYPlot)
		{
			XYPlot xyPlot = (XYPlot)plot;
			XYDataset xyDataset = xyPlot.getDataset();
			XYItemRenderer xyItemRenderer = xyPlot.getRenderer();
			for(int i = 0; i < xyDataset.getSeriesCount(); i++)
			{
				xyItemRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
			}
			xyPlot.setRangeGridlinePaint(GRIDLINE_COLOR);
			xyPlot.setRangeGridlineStroke(new BasicStroke(1f));
			xyPlot.setDomainGridlinesVisible(false);
//			calculateTickUnits(xyPlot.getRangeAxis());
//			calculateTickUnits(xyPlot.getDomainAxis());
		}
		
		plot.setForegroundAlpha(1f);
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
	 * @param GRIDLINE_COLOR color to use when drawing the axis line and any tick marks
	 */
	protected void configureAxis(
		Axis axis,
		JRFont labelFont,
		Color labelColor,
		JRFont tickLabelFont,
		Color tickLabelColor,
		String tickLabelMask,
		Color axisLineColor
		)
	{
		super.configureAxis(axis, labelFont, labelColor, tickLabelFont, tickLabelColor, tickLabelMask, axisLineColor);
		axis.setAxisLinePaint(GRIDLINE_COLOR);
		axis.setAxisLineStroke(new BasicStroke(1f));
		axis.setTickMarkPaint(GRIDLINE_COLOR);
		if (labelFont.isOwnBold() == null)
		{
			axis.setLabelFont(axis.getLabelFont().deriveFont(Font.BOLD));
		}
		
		if (labelFont.getOwnFontSize() == null)
		{
			axis.setLabelFont(axis.getLabelFont().deriveFont((float)tickLabelFont.getFontSize()));
		}
		
		if (tickLabelFont.isOwnBold() == null)
		{
			axis.setTickLabelFont(axis.getTickLabelFont().deriveFont(Font.PLAIN));
		}
		
	}
	
	protected JFreeChart createAreaChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createAreaChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		calculateTickUnits(categoryPlot.getRangeAxis());
		return jfreeChart;
	}
	
	protected JFreeChart createStackedAreaChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createStackedAreaChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		calculateTickUnits(categoryPlot.getRangeAxis());
		return jfreeChart;
	}
	
	protected JFreeChart createXyAreaChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createXyAreaChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
//		XYDataset xyDataset = xyPlot.getDataset();
//		XYAreaRenderer xyAreaRenderer = (XYAreaRenderer)xyPlot.getRenderer();

		SquareXYAreaRenderer squareXyAreaRenderer = new SquareXYAreaRenderer((XYAreaRenderer)xyPlot.getRenderer());
		xyPlot.setRenderer(squareXyAreaRenderer);
		
//		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
//		{
//			//xyAreaRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
//			//xyAreaRenderer.setSeriesPaint(i, gp[i]);
//			xyAreaRenderer.setSeriesShape(i, new Rectangle2D.Double(-3, -3, 6, 6));
//		}
		
		calculateTickUnits(xyPlot.getDomainAxis());
		calculateTickUnits(xyPlot.getRangeAxis());
		
		return jfreeChart;
	}
	
	/**
	 *
	 */
	protected JFreeChart createPieChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPieChart(evaluation);
		
		PiePlot piePlot = (PiePlot)jfreeChart.getPlot();
		//piePlot.setForegroundAlpha(1f);
		piePlot.setLabelBackgroundPaint(TRANSPARENT_PAINT);
		piePlot.setLabelShadowPaint(TRANSPARENT_PAINT);
		piePlot.setLabelOutlinePaint(TRANSPARENT_PAINT);
		piePlot.setShadowXOffset(5);
		piePlot.setShadowYOffset(10);
		piePlot.setShadowPaint(new GradientPaint(0, getChart().getHeight() / 2, new Color(41, 120, 162), 0, getChart().getHeight(), Color.white));
		PieDataset pieDataset = piePlot.getDataset();
		for(int i = 0; i < pieDataset.getItemCount(); i++)
		{
			piePlot.setSectionOutlinePaint(pieDataset.getKey(i), TRANSPARENT_PAINT);
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
//		piePlot.setLabelFont(new Font("Tahoma", Font.PLAIN, 4));
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
		piePlot3D.setLabelBackgroundPaint(TRANSPARENT_PAINT);
		piePlot3D.setLabelShadowPaint(TRANSPARENT_PAINT);
		piePlot3D.setLabelOutlinePaint(TRANSPARENT_PAINT);
		piePlot3D.setDarkerSides(true);
		piePlot3D.setDepthFactor(0.1);
// does not work for 3D
//		piePlot3D.setShadowXOffset(5);
//		piePlot3D.setShadowYOffset(10);
//		piePlot3D.setShadowPaint(new GradientPaint(
//				0, 
//				getChart().getHeight() / 2, 
//				new Color(41, 120, 162), 
//				0, 
//				getChart().getHeight(), 
//				Color.white)
//		);

		PieDataset pieDataset = piePlot3D.getDataset();
		
		for(int i = 0; i < pieDataset.getItemCount(); i++)
		{
			piePlot3D.setSectionOutlinePaint(pieDataset.getKey(i), TRANSPARENT_PAINT);
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


	/**
	 *
	 */
	protected JFreeChart createBarChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createBarChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		//categoryPlot.setOrientation(PlotOrientation.HORIZONTAL);
		BarRenderer barRenderer = (BarRenderer)categoryPlot.getRenderer();
		CategoryDataset categoryDataset = categoryPlot.getDataset();
		barRenderer.setItemMargin(0);
		
		barRenderer.setGradientPaintTransformer(
            new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL)
            );				
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
		}
		calculateTickUnits(categoryPlot.getRangeAxis());
		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createStackedBarChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createStackedBarChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		//categoryPlot.setOrientation(PlotOrientation.HORIZONTAL);
		BarRenderer barRenderer = (BarRenderer)categoryPlot.getRenderer();
		CategoryDataset categoryDataset = categoryPlot.getDataset();
		barRenderer.setItemMargin(0);
		barRenderer.setGradientPaintTransformer(
            new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL)
            );				
		
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
		}
		calculateTickUnits(categoryPlot.getRangeAxis());
		return jfreeChart;
	}

	protected JFreeChart createBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createBar3DChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		BarRenderer3D barRenderer3D = (BarRenderer3D)categoryPlot.getRenderer();
		
		barRenderer3D = new GradientBarRenderer3D(barRenderer3D);
		categoryPlot.setRenderer(barRenderer3D);
		
		barRenderer3D.setItemMargin(0);
		barRenderer3D.setWallPaint(TRANSPARENT_PAINT);
		//categoryPlot.setOrientation(PlotOrientation.HORIZONTAL);

		CategoryDataset categoryDataset = categoryPlot.getDataset();
		barRenderer3D.setItemMargin(0);
		
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer3D.setSeriesPaint(i, GRADIENT_PAINTS[i]);
		}
		calculateTickUnits(categoryPlot.getRangeAxis());
		return jfreeChart;
	}


	protected JFreeChart createStackedBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createStackedBar3DChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		BarRenderer3D barRenderer3D = (BarRenderer3D)categoryPlot.getRenderer();
		barRenderer3D.setWallPaint(TRANSPARENT_PAINT);

		//CategoryDataset categoryDataset = categoryPlot.getDataset();
		barRenderer3D.setItemMargin(0);
		calculateTickUnits(categoryPlot.getRangeAxis());
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
		bubbleRenderer = new GradientXYBubbleRenderer(bubbleRenderer.getScaleType());
		xyPlot.setRenderer(bubbleRenderer);
		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
		{
			bubbleRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
			bubbleRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
		}
		calculateTickUnits(xyPlot.getDomainAxis());
		calculateTickUnits(xyPlot.getRangeAxis());
		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createXYBarChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createXYBarChart(evaluation);
		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		XYDataset xyDataset = xyPlot.getDataset();
		XYBarRenderer renderer = (XYBarRenderer)xyPlot.getRenderer();
		renderer.setMargin(0.1);
		renderer.setGradientPaintTransformer(
	            new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL)
	            );				
		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
		{
			renderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
		}
		calculateTickUnits(xyPlot.getDomainAxis());
		calculateTickUnits(xyPlot.getRangeAxis());
		return jfreeChart;
	}

	protected JFreeChart createHighLowChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createHighLowChart(evaluation);
		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		//xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));
		calculateTickUnits(xyPlot.getDomainAxis());
		calculateTickUnits(xyPlot.getRangeAxis());
		return jfreeChart;
	}

	protected JFreeChart createScatterChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createScatterChart(evaluation);
		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		Color gridColor = new Color(196, 196, 196);
		xyPlot.setRangeGridlinePaint(gridColor);
		xyPlot.setRangeGridlineStroke(new BasicStroke(0.75f));
		xyPlot.setDomainGridlinesVisible(true);
		xyPlot.setDomainGridlinePaint(gridColor);
		xyPlot.setDomainGridlineStroke(new BasicStroke(0.75f));
		xyPlot.setRangeZeroBaselineVisible(true);

		XYDataset xyDataset = xyPlot.getDataset();
		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer)xyPlot.getRenderer();
		lineRenderer.setUseFillPaint(true);
		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
		{
			lineRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
			lineRenderer.setSeriesFillPaint(i, GRADIENT_PAINTS[i]);
			lineRenderer.setSeriesPaint(i, COLORS[i]);
			//lineRenderer.setSeriesShape(i, new Ellipse2D.Double(-3, -3, 6, 6));
		}
		
		calculateTickUnits(xyPlot.getDomainAxis());
		calculateTickUnits(xyPlot.getRangeAxis());
		return jfreeChart;
	}

	protected JFreeChart createXyLineChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createXyLineChart(evaluation);
		
		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer) jfreeChart.getXYPlot().getRenderer();
		lineRenderer.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		XYLine3DRenderer line3DRenderer = new XYLine3DRenderer();
		line3DRenderer.setLinesVisible(lineRenderer.getLinesVisible());
		line3DRenderer.setShapesVisible(lineRenderer.getShapesVisible());
		line3DRenderer.setBaseToolTipGenerator(lineRenderer.getBaseToolTipGenerator());
		line3DRenderer.setURLGenerator(lineRenderer.getURLGenerator());
		line3DRenderer.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		line3DRenderer.setXOffset(2);
		line3DRenderer.setYOffset(2);
		line3DRenderer.setWallPaint(GRIDLINE_COLOR);
		
		XYPlot xyPlot = jfreeChart.getXYPlot();
		xyPlot.setRenderer(line3DRenderer);
		calculateTickUnits(xyPlot.getDomainAxis());
		calculateTickUnits(xyPlot.getRangeAxis());
		
		return jfreeChart;
	}

	/**
	 *
	 */
	protected JFreeChart createLineChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createLineChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)categoryPlot.getRenderer();
		lineRenderer.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		for(int i = 0; i < lineRenderer.getRowCount(); i++)
		{
			lineRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
			lineRenderer.setSeriesFillPaint(i, GRADIENT_PAINTS[i]);
			lineRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
			lineRenderer.setSeriesShapesVisible(i,true);
			//line3DRenderer.setSeriesLinesVisible(i,lineRenderer.getSeriesVisible(i));
		}
//		configureChart(jfreeChart, getPlot(), evaluation);
		calculateTickUnits(categoryPlot.getRangeAxis());
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
		
		categoryPlot.setDomainGridlinePaint(GRIDLINE_COLOR);
		
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
		
		categoryPlot.setRangeGridlinePaint(GRIDLINE_COLOR);
		categoryPlot.getDomainAxis().setTickLabelsVisible(
				//barPlot.isShowTickLabels()
				true
				);
		CategoryDataset categoryDataset = categoryPlot.getDataset();
		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelsVisible(true);
		BarRenderer barRenderer = (BarRenderer)categoryRenderer;
		barRenderer.setSeriesPaint(0, COLORS[3]);
		barRenderer.setSeriesPaint(1, COLORS[0]);
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
//				(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
//				null, 
//				chartPlot, 
//				getChart().isShowLegend()
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
		jfreeChart.setBackgroundPaint(TRANSPARENT_PAINT);
		jfreeChart.setBorderVisible(false);
		
		Range range = convertRange(jrPlot.getDataRange(), evaluation);

		// Set the boundary of the thermomoter
		chartPlot.setLowerBound(range.getLowerBound());
		chartPlot.setUpperBound(range.getUpperBound());
		chartPlot.setGap(0);
		
		
		chartPlot.setShowValueLines(jrPlot.isShowValueLines());

		// Units can only be Fahrenheit, Celsius or none, so turn off for now.
		chartPlot.setUnits(ThermometerPlot.UNITS_NONE);

		// Set the color of the mercury.  Only used when the value is outside of
		// any defined ranges.
		Paint paint = (jrPlot.getMercuryColor() != null ? (Paint)jrPlot.getMercuryColor() : (Paint)GRADIENT_PAINTS[0]);
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
				chartPlot.setValueFont(new Font(JRFontUtil.getAttributes(display.getFont())).deriveFont(Font.BOLD));
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

        GradientPaint gp = 
        	new GradientPaint(
        		new Point(), Color.LIGHT_GRAY, 
        		new Point(), Color.BLACK,
        		false
        		);
        
        GradientPaint gp2 = 
        	new GradientPaint(
        		new Point(), Color.GRAY, 
        		new Point(), Color.BLACK
        		);
        
        // get data for diagrams
        DialPlot dialPlot = new DialPlot();
        //dialPlot.setView(0.0, 0.0, 1.0, 1.0);
        dialPlot.setDataset((ValueDataset)getDataset().getDataset());
        StandardDialFrame dialFrame = new StandardDialFrame();
        //dialFrame.setRadius(0.60);
        //dialFrame.setBackgroundPaint(gp2);
        dialFrame.setForegroundPaint(gp2);
        dialPlot.setDialFrame(dialFrame);
        
        DialBackground db = new DialBackground(gp);
        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.VERTICAL));
        dialPlot.setBackground(db);
		JRValueDisplay display = jrPlot.getValueDisplay();
		JRFont jrFont = display != null  && display.getFont() != null ? 
				display.getFont() : 
				new JRBaseFont(null, null, getChart(), null);

        Range range = convertRange(jrPlot.getDataRange(), evaluation);
        double bound = Math.max(Math.abs(range.getUpperBound()), Math.abs(range.getLowerBound()));
        int dialUnitScale = EyeCandySixtiesUtilities.getScale(bound);
        
        double lowerBound = EyeCandySixtiesUtilities.getTruncatedValue(range.getLowerBound(), dialUnitScale);
        double upperBound = EyeCandySixtiesUtilities.getTruncatedValue(range.getUpperBound(), dialUnitScale);
        
        StandardDialScale scale = 
        	new StandardDialScale(
        		lowerBound,
        		upperBound,
        		225, 
        		-270,
        		(upperBound - lowerBound)/6, 
        		15
        		);
        scale.setTickRadius(0.9);
        scale.setTickLabelOffset(0.16);
        scale.setTickLabelFont(new Font(JRFontUtil.getAttributes(jrFont)).deriveFont(8f).deriveFont(Font.BOLD));
        scale.setMajorTickStroke(new BasicStroke(1f));
        scale.setMinorTickStroke(new BasicStroke(0.3f));
        scale.setMajorTickPaint(Color.WHITE);
        scale.setMinorTickPaint(Color.WHITE);
        
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
			int colorStep = 255 / intervals.size();
			
			for(int i = 0; i < intervals.size(); i++)
			{
				JRMeterInterval interval = (JRMeterInterval)intervals.get(i);
				Range intervalRange = convertRange(interval.getDataRange(), evaluation);
		        double intervalLowerBound = EyeCandySixtiesUtilities.getTruncatedValue(intervalRange.getLowerBound(), dialUnitScale);
		        double intervalUpperBound = EyeCandySixtiesUtilities.getTruncatedValue(intervalRange.getUpperBound(), dialUnitScale);
				
		        StandardDialRange dialRange = 
		        	new StandardDialRange(
		        		intervalLowerBound, 
		        		intervalUpperBound, 
		        		interval.getBackgroundColor() == null 
		        			? new Color(255 - colorStep * i, 0 + colorStep * i, 0)
		        			: interval.getBackgroundColor()
		        		);
		        dialRange.setInnerRadius(0.41);
		        dialRange.setOuterRadius(0.42);
		        dialPlot.addLayer(dialRange);
			}
		}

        String displayVisibility = display != null && getChart().hasProperties() ? 
        		getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.dial.value.display.visible") : "false";
        
        if(Boolean.parseBoolean(displayVisibility))
        {
        	ScaledDialValueIndicator dvi = new ScaledDialValueIndicator(0, dialUnitScale);
	        dvi.setBackgroundPaint(TRANSPARENT_PAINT);
	        dvi.setFont(new Font(JRFontUtil.getAttributes(jrFont)).deriveFont(10f).deriveFont(Font.BOLD));
	        dvi.setOutlinePaint(TRANSPARENT_PAINT);
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
		
        String label = getChart().hasProperties() ? 
        		getChart().getPropertiesMap().getProperty("net.sf.jasperreports.chart.dial.label") : null;

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
		        dialAnnotation.setFont(new Font(JRFontUtil.getAttributes(jrFont)).deriveFont(Font.BOLD));
		        dialAnnotation.setPaint(Color.WHITE);
		        dialAnnotation.setRadius(Math.sin(Math.PI/4.0) + i/10.0);
		        dialAnnotation.setAnchor(TextAnchor.CENTER);
		        dialPlot.addLayer(dialAnnotation);
        	}
       }
		
		
		//	        DialPointer needle = new DialPointer.Pointer();
        DialPointer needle = new GradientPaintDialPointer(dialUnitScale);
        
        needle.setVisible(true);
        needle.setRadius(0.91);
        dialPlot.addLayer(needle);
        
        DialCap cap = new DialCap();
        cap.setRadius(0.05);
        cap.setFillPaint(Color.DARK_GRAY);
        cap.setOutlinePaint(Color.GRAY);
        cap.setOutlineStroke(new BasicStroke(0.5f));
        dialPlot.setCap(cap);
        
		JFreeChart jfreeChart = 
		new JFreeChart(
			(String)evaluateExpression(getChart().getTitleExpression(), evaluation),
			null, 
			dialPlot, 
			getChart().isShowLegend()
			);

		// Set all the generic options
		configureChart(jfreeChart, getPlot(), evaluation);

		jfreeChart.setBackgroundPaint(TRANSPARENT_PAINT);
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
		
		for(int i = 0; i < dataset.getSeriesCount(); i++)
		{
			renderer.setSeriesFillPaint(i, COLORS[i]);
			renderer.setSeriesPaint(i, Color.DARK_GRAY);
		}
		calculateTickUnits(xyPlot.getDomainAxis());
		calculateTickUnits(xyPlot.getRangeAxis());
		return jfreeChart;
	}
	
	private void calculateTickUnits(Axis axis)
	{
		if(axis instanceof NumberAxis)
		{
			NumberAxis numberAxis = (NumberAxis)axis;
			int maxNumberOfTicks = 5;
			int axisRange = (int)numberAxis.getRange().getLength();
			int newTickUnitSize = axisRange/maxNumberOfTicks;
			int tickUnitSize = newTickUnitSize;
			
			//preferably multiple of 5 values should be used as tick units lengths:
			int i = 1;
			while(tickUnitSize > 9)
			{
				tickUnitSize /= 10;
				i *= 10;
			}
			tickUnitSize *= i;
			newTickUnitSize = tickUnitSize + i/2;
			
			if(axisRange/newTickUnitSize > maxNumberOfTicks)
			{
				newTickUnitSize += i/2;
			}
			if(numberAxis.getNumberFormatOverride() != null)
			{
				numberAxis.setTickUnit(new NumberTickUnit(newTickUnitSize, numberAxis.getNumberFormatOverride()));
			}
			else
			{
				numberAxis.setTickUnit(new NumberTickUnit(newTickUnitSize));
			}
			
		}
	}
	
}

class SquareXYAreaRenderer extends XYAreaRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public SquareXYAreaRenderer(XYAreaRenderer parent)
	{
		super(XYAreaRenderer.AREA, parent.getToolTipGenerator(), parent.getURLGenerator());
	}
	
	public LegendItem getLegendItem(int datasetIndex, int series) 
	{
		setLegendArea(new Rectangle2D.Double(-3, -3, 6, 6));
		return super.getLegendItem(datasetIndex, series);
	}
}

	
class GradientXYBubbleRenderer extends XYBubbleRenderer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public GradientXYBubbleRenderer(int scaleType)
	{
		super(scaleType);
	}

	public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) 
	{
        // return straight away if the item is not visible
        if (!getItemVisible(series, item)) {
            return;   
        }
        
        PlotOrientation orientation = plot.getOrientation();
        
        // get the data point...
        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double z = Double.NaN;
        if (dataset instanceof XYZDataset) {
            XYZDataset xyzData = (XYZDataset) dataset;
            z = xyzData.getZValue(series, item);
        }
        if (!Double.isNaN(z)) {
            RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
            double transX = domainAxis.valueToJava2D(x, dataArea, 
                    domainAxisLocation);
            double transY = rangeAxis.valueToJava2D(y, dataArea, 
                    rangeAxisLocation);

            double transDomain = 0.0;
            double transRange = 0.0;
            double zero;

            switch(getScaleType()) {
                case SCALE_ON_DOMAIN_AXIS:
                    zero = domainAxis.valueToJava2D(0.0, dataArea, 
                            domainAxisLocation);
                    transDomain = domainAxis.valueToJava2D(z, dataArea, 
                            domainAxisLocation) - zero;
                    transRange = transDomain;
                    break;
                case SCALE_ON_RANGE_AXIS:
                    zero = rangeAxis.valueToJava2D(0.0, dataArea, 
                            rangeAxisLocation);
                    transRange = zero - rangeAxis.valueToJava2D(z, dataArea, 
                            rangeAxisLocation);
                    transDomain = transRange;
                    break;
                default:
                    double zero1 = domainAxis.valueToJava2D(0.0, dataArea, 
                            domainAxisLocation);
                    double zero2 = rangeAxis.valueToJava2D(0.0, dataArea, 
                            rangeAxisLocation);
                    transDomain = domainAxis.valueToJava2D(z, dataArea, 
                            domainAxisLocation) - zero1;
                    transRange = zero2 - rangeAxis.valueToJava2D(z, dataArea, 
                            rangeAxisLocation);
            }
            transDomain = Math.abs(transDomain);
            transRange = Math.abs(transRange);
            Ellipse2D circle = null;
            if (orientation == PlotOrientation.VERTICAL) {
                circle = new Ellipse2D.Double(transX - transDomain / 2.0, 
                        transY - transRange / 2.0, transDomain, transRange);
            }
            else if (orientation == PlotOrientation.HORIZONTAL) {
                circle = new Ellipse2D.Double(transY - transRange / 2.0, 
                        transX - transDomain / 2.0, transRange, transDomain);
            }
            
            Paint paint = getItemPaint(series, item);
            if (paint instanceof GradientPaint)
            {
            	paint = new StandardGradientPaintTransformer().transform((GradientPaint)paint, circle);
            }
            g2.setPaint(paint);
            g2.fill(circle);
            g2.setStroke(getItemOutlineStroke(series, item));
            g2.setPaint(getItemOutlinePaint(series, item));
            g2.draw(circle);

            if (isItemLabelVisible(series, item)) {
                if (orientation == PlotOrientation.VERTICAL) {
                    drawItemLabel(g2, orientation, dataset, series, item, 
                            transX, transY, false);
                }
                else if (orientation == PlotOrientation.HORIZONTAL) {
                    drawItemLabel(g2, orientation, dataset, series, item, 
                            transY, transX, false);                
                }
            }
            
            // add an entity if this info is being collected
            EntityCollection entities = null;
            if (info != null) {
                entities = info.getOwner().getEntityCollection();
                if (entities != null && circle.intersects(dataArea)) {
                    addEntity(entities, circle, dataset, series, item, 
                            circle.getCenterX(), circle.getCenterY());
                }
            }

            int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
            int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
            updateCrosshairValues(crosshairState, x, y, domainAxisIndex, 
                    rangeAxisIndex, transX, transY, orientation);
        }
	}
};

class GradientBarRenderer3D extends BarRenderer3D
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public GradientBarRenderer3D(BarRenderer3D barRenderer3D) 
	{
		super(barRenderer3D.getXOffset(), barRenderer3D.getYOffset());
		setBaseItemLabelGenerator(barRenderer3D.getBaseItemLabelGenerator());
		setBaseItemLabelsVisible(barRenderer3D.getBaseItemLabelsVisible());
	}

	public void drawItem(Graphics2D g2,
        CategoryItemRendererState state,
        Rectangle2D dataArea,
        CategoryPlot plot,
        CategoryAxis domainAxis,
        ValueAxis rangeAxis,
        CategoryDataset dataset,
        int row,
        int column,
        int pass) 
    {

		// check the value we are plotting...
		Number dataValue = dataset.getValue(row, column);
		if (dataValue == null) {
			return;
		}
	
		double value = dataValue.doubleValue();
	
		Rectangle2D adjusted = new Rectangle2D.Double(dataArea.getX(),
		   dataArea.getY() + getYOffset(),
		   dataArea.getWidth() - getXOffset(),
		   dataArea.getHeight() - getYOffset());
	
		PlotOrientation orientation = plot.getOrientation();
	
		double barW0 = calculateBarW0(plot, orientation, adjusted, domainAxis,
		   state, row, column);
		double[] barL0L1 = calculateBarL0L1(value);
		if (barL0L1 == null) {
			return;  // the bar is not visible
		}
	
		RectangleEdge edge = plot.getRangeAxisEdge();
		double transL0 = rangeAxis.valueToJava2D(barL0L1[0], adjusted, edge);
		double transL1 = rangeAxis.valueToJava2D(barL0L1[1], adjusted, edge);
		double barL0 = Math.min(transL0, transL1);
		double barLength = Math.abs(transL1 - transL0);
	
		// draw the bar...
		Rectangle2D bar = null;
		if (orientation == PlotOrientation.HORIZONTAL) {
		bar = new Rectangle2D.Double(barL0, barW0, barLength,
		       state.getBarWidth());
		}
		else {
		bar = new Rectangle2D.Double(barW0, barL0, state.getBarWidth(),
		       barLength);
		}
		Paint itemPaint = getItemPaint(row, column);
        if (itemPaint instanceof GradientPaint)
        {
        	itemPaint = getGradientPaintTransformer().transform((GradientPaint)itemPaint, bar);
        }
        g2.setPaint(itemPaint);
		g2.fill(bar);
	
		double x0 = bar.getMinX();
		double x1 = x0 + getXOffset();
		double x2 = bar.getMaxX();
		double x3 = x2 + getXOffset();
		
		double y0 = bar.getMinY() - getYOffset();
		double y1 = bar.getMinY();
		double y2 = bar.getMaxY() - getYOffset();
		double y3 = bar.getMaxY();
		
		GeneralPath bar3dRight = null;
		GeneralPath bar3dTop = null;
		if (barLength > 0.0) {
			bar3dRight = new GeneralPath();
			bar3dRight.moveTo((float) x2, (float) y3);
			bar3dRight.lineTo((float) x2, (float) y1);
			bar3dRight.lineTo((float) x3, (float) y0);
			bar3dRight.lineTo((float) x3, (float) y2);
			bar3dRight.closePath();
			
			if (itemPaint instanceof Color) {
			   g2.setPaint(((Color) itemPaint).darker());
			}
			else if (itemPaint instanceof GradientPaint)
			{
				GradientPaint gp = (GradientPaint)itemPaint;
				g2.setPaint(
					new StandardGradientPaintTransformer().transform(
						new GradientPaint(gp.getPoint1(), gp.getColor1().darker(), gp.getPoint2(), gp.getColor2().darker(), gp.isCyclic()), 
						bar3dRight
						)
					);
			}
			g2.fill(bar3dRight);
		}
	
		bar3dTop = new GeneralPath();
		bar3dTop.moveTo((float) x0, (float) y1);
		bar3dTop.lineTo((float) x1, (float) y0);
		bar3dTop.lineTo((float) x3, (float) y0);
		bar3dTop.lineTo((float) x2, (float) y1);
		bar3dTop.closePath();
		g2.fill(bar3dTop);
	
		if (isDrawBarOutline()
		   && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
			g2.setStroke(getItemOutlineStroke(row, column));
			g2.setPaint(getItemOutlinePaint(row, column));
			g2.draw(bar);
			if (bar3dRight != null) {
			   g2.draw(bar3dRight);
			}
			if (bar3dTop != null) {
			   g2.draw(bar3dTop);
			}
		}
	
		CategoryItemLabelGenerator generator
		= getItemLabelGenerator(row, column);
		if (generator != null && isItemLabelVisible(row, column)) {
		drawItemLabel(g2, dataset, row, column, plot, generator, bar,
		       (value < 0.0));
		}
	
		// add an item entity, if this information is being collected
		EntityCollection entities = state.getEntityCollection();
		if (entities != null) {
			GeneralPath barOutline = new GeneralPath();
			barOutline.moveTo((float) x0, (float) y3);
			barOutline.lineTo((float) x0, (float) y1);
			barOutline.lineTo((float) x1, (float) y0);
			barOutline.lineTo((float) x3, (float) y0);
			barOutline.lineTo((float) x3, (float) y2);
			barOutline.lineTo((float) x2, (float) y3);
			barOutline.closePath();
			addItemEntity(entities, dataset, row, column, barOutline);
		}
    }
	
};

class GradientPaintDialPointer extends DialPointer.Pointer 
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
    
    private Paint gradientFillPaint;

    private int scale;
    
    /**
     * Creates a new instance.
     */
    public GradientPaintDialPointer() {
        this(0, 0.03, new Color(191, 48, 0), new Color(191, 48, 0), 1);
    }
 
    /**
     * Creates a new instance.
     */
    public GradientPaintDialPointer(int scale) {
        this(0, 0.03, new Color(191, 48, 0), new Color(191, 48, 0), scale);
    }
 
//    /**
//     * Creates a new instance.
//     * 
//     * @param datasetIndex  the dataset index.
//     */
//    private GradientPaintDialPointer(int datasetIndex, double widthRadius, Paint gradientFillPaint, Paint outlinePaint) 
//    {
//        this(datasetIndex, widthRadius,gradientFillPaint, outlinePaint, 1);
//    }
    
    
    /**
     * Creates a new instance.
     * 
     * @param datasetIndex  the dataset index.
     */
    private GradientPaintDialPointer(int datasetIndex, double widthRadius, Paint gradientFillPaint, Paint outlinePaint, int scale) 
    {
        super(datasetIndex);
        setWidthRadius(widthRadius);
        this.gradientFillPaint = gradientFillPaint;
        setOutlinePaint(outlinePaint);
        this.scale = scale;
    }
    
    /**
     * Returns the fill paint.
     * 
     * @return The paint (never <code>null</code>).
     * 
     * @see #setGradientFillPaint(Paint)
     * 
     * @since 1.0.8
     */
    public Paint getGradientFillPaint() {
        return this.gradientFillPaint;
    }
    
    /**
     * Sets the fill paint and sends a {@link DialLayerChangeEvent} to all 
     * registered listeners.
     * 
     * @param paint  the paint (<code>null</code> not permitted).
     * 
     * @see #getFillPaint()
     * 
     * @since 1.0.8
     */
    public void setGradientFillPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.gradientFillPaint = paint;
        notifyListeners(new DialLayerChangeEvent(this));
    }
    
    /**
     * Draws the pointer.
     * 
     * @param g2  the graphics target.
     * @param plot  the plot.
     * @param frame  the dial's reference frame.
     * @param view  the dial's view.
     */
    public void draw(Graphics2D g2, DialPlot plot, Rectangle2D frame, 
            Rectangle2D view) {
    
        g2.setStroke(new BasicStroke(1.0f));
        Rectangle2D lengthRect = DialPlot.rectangleByRadius(frame, 
                this.getRadius(), this.getRadius());
        Rectangle2D widthRect = DialPlot.rectangleByRadius(frame, 
                this.getWidthRadius(), this.getWidthRadius());
        double value = EyeCandySixtiesUtilities.getScaledValue(plot.getValue(this.getDatasetIndex()), scale);
        DialScale scale = plot.getScaleForDataset(this.getDatasetIndex());
        double angle = scale.valueToAngle(value);
    
        Arc2D arc1 = new Arc2D.Double(lengthRect, angle, 0, Arc2D.OPEN);
        Point2D pt1 = arc1.getEndPoint();
        Arc2D arc2 = new Arc2D.Double(widthRect, angle - 90.0, 180.0, 
                Arc2D.OPEN);
        Point2D pt2 = arc2.getStartPoint();
        Point2D pt3 = arc2.getEndPoint();
        Arc2D arc3 = new Arc2D.Double(widthRect, angle - 180.0, 0.0, 
                Arc2D.OPEN);
        Point2D pt4 = arc3.getStartPoint();
    
        GeneralPath gp = new GeneralPath();
        gp.moveTo((float) pt1.getX(), (float) pt1.getY());
        gp.lineTo((float) pt2.getX(), (float) pt2.getY());
        gp.lineTo((float) pt4.getX(), (float) pt4.getY());
        gp.lineTo((float) pt3.getX(), (float) pt3.getY());
        gp.closePath();
        g2.setPaint(this.gradientFillPaint);
        g2.fill(gp);
    
        g2.setPaint(this.getOutlinePaint());
        Line2D line = new Line2D.Double(frame.getCenterX(), 
                frame.getCenterY(), pt1.getX(), pt1.getY());
//        g2.draw(line);
    
        line.setLine(pt2, pt3);
        g2.draw(line);
    
        line.setLine(pt3, pt1);
        g2.draw(line);
    
        line.setLine(pt2, pt1);
        g2.draw(line);
    
        line.setLine(pt2, pt4);
        g2.draw(line);

        line.setLine(pt3, pt4);
        g2.draw(line);
    }
    
    /**
     * Tests this pointer for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GradientPaintDialPointer)) {
            return false;
        }
        GradientPaintDialPointer that = (GradientPaintDialPointer) obj;
        
        if (!this.gradientFillPaint.equals(that.gradientFillPaint)) 
        {
            return false;
        }
        return super.equals(obj);
    }
    
    /**
     * Returns a hash code for this instance.
     * 
     * @return A hash code.
     */
    public int hashCode() {
        int result = super.hashCode();
        result = HashUtilities.hashCode(result, this.gradientFillPaint);
        return result;
    }
    
    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.gradientFillPaint, stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream) 
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.gradientFillPaint = SerialUtilities.readPaint(stream);
    }
   
};

class ScaledDialValueIndicator extends DialValueIndicator
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private int scale;
	/**
     * Creates a new instance of <code>DialValueIndicator</code>.
     */
    public ScaledDialValueIndicator() {
        this(0,1);
    }
    
    /** 
     * Creates a new instance of <code>DialValueIndicator</code>.
     * 
     * @param datasetIndex  the dataset index.
     */
    public ScaledDialValueIndicator(int datasetIndex) {
    	this(datasetIndex, 1);
    }
    
    /** 
     * Creates a new instance of <code>DialValueIndicator</code>.
     * 
     * @param datasetIndex  the dataset index.
     * @param scale  the scale.
     */
    public ScaledDialValueIndicator(int datasetIndex, int scale) {
    	super(datasetIndex);
    	setScale(scale);
    }
    
    /**
     * Draws the background to the specified graphics device.  If the dial
     * frame specifies a window, the clipping region will already have been 
     * set to this window before this method is called.
     *
     * @param g2  the graphics device (<code>null</code> not permitted).
     * @param plot  the plot (ignored here).
     * @param frame  the dial frame (ignored here).
     * @param view  the view rectangle (<code>null</code> not permitted). 
     */
    public void draw(Graphics2D g2, DialPlot plot, Rectangle2D frame, 
            Rectangle2D view) {

        // work out the anchor point
        Rectangle2D f = DialPlot.rectangleByRadius(frame, getRadius(), 
                this.getRadius());
        Arc2D arc = new Arc2D.Double(f, this.getAngle(), 0.0, Arc2D.OPEN);
        Point2D pt = arc.getStartPoint();
        
        // calculate the bounds of the template value
        FontMetrics fm = g2.getFontMetrics(this.getFont());
        String s = this.getNumberFormat().format(this.getTemplateValue());
        Rectangle2D tb = TextUtilities.getTextBounds(s, g2, fm);

        // align this rectangle to the frameAnchor
        Rectangle2D bounds = RectangleAnchor.createRectangle(new Size2D(
                tb.getWidth(), tb.getHeight()), pt.getX(), pt.getY(), 
                this.getFrameAnchor());
        
        // add the insets
        Rectangle2D fb = this.getInsets().createOutsetRectangle(bounds);

        // draw the background
        g2.setPaint(this.getBackgroundPaint());
        g2.fill(fb);

        // draw the border
        g2.setStroke(this.getOutlineStroke());
        g2.setPaint(this.getOutlinePaint());
        g2.draw(fb);
        
        
        // now find the text anchor point
        String valueStr = this.getNumberFormat().format(EyeCandySixtiesUtilities.getScaledValue(plot.getValue(this.getDatasetIndex()), scale));
        Point2D pt2 = RectangleAnchor.coordinates(bounds, this.getValueAnchor());
        g2.setPaint(this.getPaint());
        g2.setFont(this.getFont());
        TextUtilities.drawAlignedString(valueStr, g2, (float) pt2.getX(), 
                (float) pt2.getY(), this.getTextAnchor());
        
    }

	/**
     * @return the scale
     */
    public int getScale()
    {
    	return this.scale;
    }

	/**
     * @param scale the scale to set
     */
    public void setScale(int scale)
    {
    	this.scale = scale;
    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ScaledDialValueIndicator)) {
            return false;
        }
        ScaledDialValueIndicator that = (ScaledDialValueIndicator) obj;
        if (this.scale != that.scale) {
            return false;
        }
        return super.equals(obj);
    }
    
    /**
     * Returns a hash code for this instance.
     * 
     * @return The hash code.
     */
    public int hashCode() {
        return 37 * super.hashCode() + scale;
    }
    
    /**
     * Returns a clone of this instance.
     *
     * @return The clone.
     *
     * @throws CloneNotSupportedException if some attribute of this instance
     *     cannot be cloned.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
};

class EyeCandySixtiesUtilities
{
	public static int getScale(double value)
	{
		return BigDecimal.valueOf(value).precision() - BigDecimal.valueOf(value).scale() - 1;
	}
	
	public static double getTruncatedValue(double value, int scale)
	{
		String newValue;
		String sign = value < 0 ? "-" : ""; 
		value = Math.abs(value);
		
		if(scale < 0)
		{
			value *= Math.pow(10.0, -scale);
			newValue = (String.valueOf(value) + "0000").substring(0,4);
		}
		else if(scale > 2)
		{
			newValue =  (String.valueOf(value / Math.pow(10.0, scale - 2)) + "000").substring(0,3);
		}
		else
		{
			newValue = String.valueOf(value);
			if(newValue.length() > 4)
				newValue = newValue.substring(0,4);
		}
		return Double.valueOf(sign + newValue).doubleValue();
	}
	
	public static double getScaledValue(double value, int scale)
	{
		if(scale < 0)
		{
			return value * Math.pow(10.0, -scale);
		}
		else if(scale > 2)
		{
			return value / Math.pow(10.0, scale-2);
		}
		
		return value;
	}
	
};
