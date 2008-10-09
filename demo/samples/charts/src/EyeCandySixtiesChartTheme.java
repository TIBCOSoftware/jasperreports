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

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.SortedSet;

import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.fill.DefaultChartTheme;
import net.sf.jasperreports.engine.util.JRFontUtil;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class EyeCandySixtiesChartTheme extends DefaultChartTheme
{

	public static final Color[] colors = new Color[]{
			new Color(234, 171, 53, 180),
			new Color(250, 97, 18, 180),
			new Color(220, 1, 83, 180),
			new Color(0, 111, 60, 180),
			new Color(237,38,42, 180),
			new Color(250, 223, 18, 180),
			new Color(228, 100, 37, 180),
			new Color(64, 157, 207, 180),
			};
	
    // gradient paints for series...
	public static final GradientPaint gp[] = new GradientPaint[]{
    		new GradientPaint(0.0f, 0.0f, colors[0], 0.0f, 0.0f, Color.BLACK),
    		new GradientPaint(0.0f, 0.0f, colors[1], 0.0f, 0.0f, Color.BLACK),
    		new GradientPaint(0.0f, 0.0f, colors[2], 0.0f, 0.0f, Color.BLACK),
    		new GradientPaint(0.0f, 0.0f, colors[3], 0.0f, 0.0f, Color.BLACK),
    		new GradientPaint(0.0f, 0.0f, colors[4], 0.0f, 0.0f, Color.BLACK),
    		new GradientPaint(0.0f, 0.0f, colors[5], 0.0f, 0.0f, Color.BLACK),
    		new GradientPaint(0.0f, 0.0f, colors[6], 0.0f, 0.0f, Color.BLACK),
    		new GradientPaint(0.0f, 0.0f, colors[7], 0.0f, 0.0f, Color.BLACK)
    };
	
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

		TextTitle title = jfreeChart.getTitle();
		if(title != null)
		{
			title.setFont(new Font("Tahoma", Font.BOLD, 6));
		}
		LegendTitle legend = jfreeChart.getLegend();
		if (legend != null)
		{
			legend.setFrame(BlockBorder.NONE);
			legend.setItemFont(new Font("Tahoma", Font.PLAIN, 4));
			legend.setPosition(RectangleEdge.RIGHT);
			legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		}
		
		jfreeChart.setBackgroundPaint(
				new GradientPaint(0, 0, new Color(0, 64, 128), 0, getChart().getHeight() / 2, Color.white, true)
				);
		jfreeChart.setAntiAlias(true);
		jfreeChart.setBorderVisible(true);
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
					colors,
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
		super.configureAxis(axis, labelFont, labelColor, tickLabelFont, tickLabelColor, tickLabelMask, lineColor);
		axis.setLabelFont(new Font("Tahoma", Font.BOLD, 4));
		axis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 5));
	}

	/**
	 *
	 */
	protected JFreeChart createPieChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPieChart(evaluation);

		PiePlot piePlot = (PiePlot)jfreeChart.getPlot();
		piePlot.setForegroundAlpha(1f);
		piePlot.setLabelBackgroundPaint(TRANSPARENT_PAINT);
		piePlot.setLabelShadowPaint(TRANSPARENT_PAINT);
		piePlot.setLabelOutlinePaint(TRANSPARENT_PAINT);
		PieDataset pieDataset = piePlot.getDataset();
		
		for(int i = 0; i < pieDataset.getItemCount(); i++)
		{
			piePlot.setSectionPaint(pieDataset.getKey(i), gp[i]);
			//categoryRenderer.setSeriesOutlinePaint(i, gp[i]);
		}
		
		if (
			((JRFillPieDataset)getDataset()).getLabelGenerator() == null
			&& ((JRFillPiePlot)getPlot()).getLabelFormat() == null
			)
		{
			piePlot.setLabelGenerator(
				new StandardPieSectionLabelGenerator("{0} ({2})")
				);
		}

		if (((JRFillPiePlot)getPlot()).getLegendLabelFormat() == null)
		{
			piePlot.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator("{0}")
				);
		}
		piePlot.setLabelFont(new Font("Tahoma", Font.PLAIN, 4));
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
		piePlot3D.setForegroundAlpha(1f);
		piePlot3D.setLabelBackgroundPaint(TRANSPARENT_PAINT);
		piePlot3D.setLabelShadowPaint(TRANSPARENT_PAINT);
		piePlot3D.setLabelOutlinePaint(TRANSPARENT_PAINT);
		piePlot3D.setDarkerSides(true);
		piePlot3D.setDepthFactor(0.1);
		if (
			((JRFillPieDataset)getDataset()).getLabelGenerator() == null
			&& ((JRFillPie3DPlot)getPlot()).getLabelFormat() == null
			)
		{
			piePlot3D.setLabelGenerator(
				new StandardPieSectionLabelGenerator("{0} ({2})")
				);
		}

		if (((JRFillPie3DPlot)getPlot()).getLegendLabelFormat() == null)
		{
			piePlot3D.setLegendLabelGenerator(
				new StandardPieSectionLabelGenerator("{0}")
				);
		}
		piePlot3D.setLabelFont(new Font("Tahoma", Font.PLAIN, 4));
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
		categoryPlot.setOrientation(PlotOrientation.HORIZONTAL);
		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setOutlinePaint(TRANSPARENT_PAINT);

        categoryRenderer.setSeriesPaint(0, gp[0]);
        categoryRenderer.setSeriesPaint(1, gp[1]);
        categoryRenderer.setSeriesPaint(2, gp[2]);
        categoryRenderer.setSeriesPaint(3, gp[3]);
        categoryRenderer.setSeriesPaint(4, gp[4]);
        categoryRenderer.setSeriesPaint(5, gp[5]);
        categoryRenderer.setSeriesPaint(6, gp[6]);
        categoryRenderer.setSeriesPaint(7, gp[7]);
		
		return jfreeChart;
	}

	protected JFreeChart createBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createBar3DChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		categoryPlot.setOrientation(PlotOrientation.HORIZONTAL);
		categoryPlot.setForegroundAlpha(1f);
		CategoryDataset categoryDataset = (CategoryDataset)categoryPlot.getDataset();

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			categoryRenderer.setSeriesOutlinePaint(i, gp[i]);
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
		XYItemRenderer itemRenderer = xyPlot.getRenderer();
		itemRenderer.setOutlinePaint(TRANSPARENT_PAINT);
		for(int i = 0; i < xyDataset.getSeriesCount(); i++)
		{
			itemRenderer.setSeriesOutlinePaint(i, gp[i]);
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
		renderer.setOutlinePaint(TRANSPARENT_PAINT);
		renderer.setMargin(0.1);

		return jfreeChart;
	}


	protected JFreeChart createXyAreaChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createXyAreaChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();

		return jfreeChart;
	}


	protected JFreeChart createScatterChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createScatterChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();

		return jfreeChart;
	}

	protected JFreeChart createXyLineChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createXyLineChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();

		return jfreeChart;
	}

	protected JFreeChart createTimeSeriesChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createTimeSeriesChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		return jfreeChart;
	}

	protected JFreeChart createHighLowChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createHighLowChart(evaluation);

		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();

		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		return jfreeChart;
	}

	protected JFreeChart createCandlestickChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createCandlestickChart(evaluation);

		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();

		return jfreeChart;
	}

	protected JFreeChart createStackedBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createStackedBar3DChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		CategoryDataset categoryDataset = (CategoryDataset)categoryPlot.getDataset();

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			categoryRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
		}
		
		return jfreeChart;
	}



	/*
	protected JFreeChart createStackedAreaChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createStackedAreaChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setOutlinePaint(TRANSPARENT_PAINT);

		return jfreeChart;
	}
	*/

}
