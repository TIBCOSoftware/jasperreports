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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.SortedSet;

import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.fill.DefaultChartTheme;
import net.sf.jasperreports.engine.fill.JRFillChart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
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
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.util.UnitType;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class EyeCandySixtiesChartTheme extends DefaultChartTheme
{

	public static final Color[] COLORS = 
		new Color[]{
			new Color(250, 223,  18),
			new Color(250,  97,  18),
			new Color(237,  38,  42),
			new Color(  0, 111,  60),
			//new Color(228, 100,  37),
			new Color( 64, 157, 207),
			new Color(229,   1, 140),
			new Color(234, 171,  53)
			
			//new Color(220, 1, 83, 180),
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
		LegendTitle legend = jfreeChart.getLegend();
		
		if(title != null)
		{
			Font titleFont = title.getFont();
			
			if(chart.getTitleFont().isOwnBold() == null)
			{
				titleFont = titleFont.deriveFont(Font.BOLD);
			}

			if(chart.getTitleFont().getOwnFontSize() == null)
			{
				titleFont = titleFont.deriveFont(10f);
			}

			title.setFont(titleFont);
			title.setHorizontalAlignment(HorizontalAlignment.CENTER);
		}
		
		if (legend != null)
		{
			legend.setFrame(BlockBorder.NONE);
			legend.setPosition(RectangleEdge.RIGHT);
			legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		}
		
		jfreeChart.setBackgroundPaint(
				new GradientPaint(0f, 30f, new Color(41, 120, 162), 0f, getChart().getHeight() / 2, Color.WHITE, false)
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
			jfreeChart.setBorderStroke(new BasicStroke(2f));
			jfreeChart.setBorderPaint(BORDER_COLOR);
			jfreeChart.setBorderVisible(true);
		}
		jfreeChart.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 10, 10, 10, 10));
	}


	/**
	 *
	 */
	protected void configurePlot(Plot plot, JRChartPlot jrPlot)
	{
		super.configurePlot(plot, jrPlot);

		// Set any color series
		SortedSet seriesColors = getPlot().getSeriesColors();
		if (seriesColors == null || seriesColors.size() == 0)
		{
			plot.setDrawingSupplier(
				new DefaultDrawingSupplier(
					COLORS,
					DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
					DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
					DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
					DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
					)
				);
		}
		
		
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
			categoryPlot.setRangeGridlineStroke(new BasicStroke(0.75f));
			categoryPlot.setDomainGridlinesVisible(false);
			if(categoryPlot.getRenderer() instanceof BarRenderer || categoryPlot.getRenderer() instanceof BarRenderer3D)
			{
				((BarRenderer)categoryPlot.getRenderer()).setGradientPaintTransformer(
		                new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL)
		                );				
			}
			if(categoryPlot.getRenderer() instanceof BarRenderer3D)
			{
				((BarRenderer3D)categoryPlot.getRenderer()).setGradientPaintTransformer(
		                new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL)
		                );				
			}
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
			xyPlot.setRangeGridlineStroke(new BasicStroke(0.75f));
			xyPlot.setDomainGridlinesVisible(false);
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
		axis.setAxisLineStroke(new BasicStroke(1.5f));
		axis.setTickMarkPaint(GRIDLINE_COLOR);

		if(axis instanceof NumberAxis)
		{
			NumberAxis numberAxis = (NumberAxis)axis;
			
			// about 5 different values should be exposed on the numeric value axis
			double interval = (numberAxis.getUpperBound() - numberAxis.getLowerBound())/5d;
			
			// the default tick unit size:
			double oldTickUnitSize = numberAxis.getTickUnit().getSize();
			
			// the new unit tick size will be calculated as an integer multiple of 
			// the default tick unit size
			int j = (int)(interval/oldTickUnitSize);
			int count = 1;
			while(j > 9)
			{
				j /= 10;
				count *= 10;
			}
			count *= j;
			double newTickUnitSize = count * oldTickUnitSize;
			numberAxis.setTickUnit(new NumberTickUnit(newTickUnitSize));
		}
		else if(axis instanceof CategoryAxis)
		{
			CategoryAxis categoryAxis = (CategoryAxis)axis;
			categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		}
	}

	
//	protected JFreeChart createAreaChart(byte evaluation) throws JRException 
//	{
//		JFreeChart jfreeChart = super.createAreaChart(evaluation);
//		
//		makes series colors darker
//		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
//		AreaRenderer areaRenderer = (AreaRenderer)categoryPlot.getRenderer();

//		CategoryDataset categoryDataset = categoryPlot.getDataset();
//		
//		for(int i = 0; i < categoryDataset.getRowCount(); i++)
//		{
//			areaRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
//		}
//		
//		return jfreeChart;
//	}
	
//	protected JFreeChart createStackedAreaChart(byte evaluation) throws JRException 
//	{
//		JFreeChart jfreeChart = super.createStackedAreaChart(evaluation);
//		
//		makes series colors darker
//		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
//		StackedAreaRenderer stackedAreaRenderer = (StackedAreaRenderer)categoryPlot.getRenderer();

//		CategoryDataset categoryDataset = categoryPlot.getDataset();
//		
//		for(int i = 0; i < categoryDataset.getRowCount(); i++)
//		{
//			stackedAreaRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
//		}
//		
//		return jfreeChart;
//	}
	
//	protected JFreeChart createXYAreaChart(byte evaluation) throws JRException 
//	{
//		JFreeChart jfreeChart = super.createXyAreaChart(evaluation);
//
//		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
//		XYDataset xyDataset = xyPlot.getDataset();
//		XYAreaRenderer xyAreaRenderer = (XYAreaRenderer)xyPlot.getRenderer();
//		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
//		{
//			//xyAreaRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
//			xyAreaRenderer.setSeriesPaint(i, gp[i]);
//		}
//		
//		return jfreeChart;
//	}
	
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
			//makes pie colors darker piePlot.setSectionPaint(pieDataset.getKey(i), GRADIENT_PAINTS[i]);
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
		piePlot3D.setShadowXOffset(40);
		piePlot3D.setShadowYOffset(50);
		piePlot3D.setShadowPaint(new GradientPaint(
				0, 
				getChart().getHeight() / 2, 
				new Color(41, 120, 162), 
				0, 
				getChart().getHeight(), 
				Color.white)
		);

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
		
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
		}
		
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
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			barRenderer.setSeriesPaint(i, GRADIENT_PAINTS[i]);
		}
		
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

		return jfreeChart;
	}


	protected JFreeChart createStackedBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createStackedBar3DChart(evaluation);
		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		BarRenderer3D barRenderer3D = (BarRenderer3D)categoryPlot.getRenderer();
		barRenderer3D.setWallPaint(TRANSPARENT_PAINT);

		CategoryDataset categoryDataset = categoryPlot.getDataset();
		barRenderer3D.setItemMargin(0);

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
		
		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createXYBarChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createXYBarChart(evaluation);
		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		XYBarRenderer renderer = (XYBarRenderer)xyPlot.getRenderer();
		renderer.setMargin(0.1);
		//xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		//xyPlot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
		return jfreeChart;
	}


	protected JFreeChart createHighLowChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createHighLowChart(evaluation);
		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));
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

//		XYDataset xyDataset = xyPlot.getDataset();
//		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer)xyPlot.getRenderer();
//		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
//		{
//			lineRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
//			lineRenderer.setSeriesPaint(i, gp[i]);
//		}
		
		return jfreeChart;
	}

	protected JFreeChart createXyLineChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createXyLineChart(evaluation);
		
		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer) jfreeChart.getXYPlot().getRenderer();
		lineRenderer.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
//		XYLine3DRenderer line3DRenderer = new XYLine3DRenderer();
//		line3DRenderer.setLinesVisible(lineRenderer.getLinesVisible());
//		line3DRenderer.setShapesVisible(lineRenderer.getShapesVisible());
//		line3DRenderer.setBaseToolTipGenerator(lineRenderer.getBaseToolTipGenerator());
//		line3DRenderer.setURLGenerator(lineRenderer.getURLGenerator());
//		line3DRenderer.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//		line3DRenderer.setXOffset(2);
//		line3DRenderer.setYOffset(2);
//		jfreeChart.getXYPlot().setRenderer(line3DRenderer);
		
		return jfreeChart;
	}
	
}

class GradientXYBubbleRenderer extends XYBubbleRenderer
{
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
	public GradientBarRenderer3D(BarRenderer3D barRenderer3D) 
	{
		super(barRenderer3D.getXOffset(), barRenderer3D.getYOffset());
		setBaseItemLabelGenerator(barRenderer3D.getBaseItemLabelGenerator());
		setItemLabelsVisible(barRenderer3D.isItemLabelVisible(0, 0));//FIXMETHEME
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
        	itemPaint = new StandardGradientPaintTransformer().transform((GradientPaint)itemPaint, bar);
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
