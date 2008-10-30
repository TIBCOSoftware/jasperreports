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

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.SortedSet;

import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.DefaultChartTheme;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillChart.java 2278 2008-08-14 16:14:54Z teodord $
 */
public class SimpleChartTheme extends DefaultChartTheme
{


	/**
	 *
	 */
	public SimpleChartTheme()
	{
	}
	
	
	/**
	 *
	 */
	protected void configureChart(JFreeChart jfreeChart, JRChartPlot jrPlot, byte evaluation) throws JRException
	{
		super.configureChart(jfreeChart, jrPlot, evaluation);

		LegendTitle legend = jfreeChart.getLegend();
		if (legend != null)
		{
			jfreeChart.getLegend().setBorder(BlockBorder.NONE);
		}
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
			Color[] colors = new Color[]{
				new Color( 66, 138, 247, 180),
				new Color(206,  77,  24, 180),
				new Color(123, 207,  24, 180),
				new Color(247, 207,  57, 180),
				new Color( 90, 186, 206, 180)
				};
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

		CategoryPlot categoryPlot = plot instanceof CategoryPlot ? (CategoryPlot)plot:null;
		if (categoryPlot != null)
		{
			if (getPlot().getOwnBackcolor() == null)
			{
				categoryPlot.setBackgroundPaint(
					new GradientPaint(0, 0, new Color(222, 231, 247), 0, getChart().getHeight() / 2, Color.white, true)
					);
				categoryPlot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			}

			CategoryDataset categoryDataset = (CategoryDataset)categoryPlot.getDataset();

			CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
			for(int i = 0; i < categoryDataset.getRowCount(); i++)
			{
				categoryRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);//FIXME better stroke zero?
			}
		}

		XYPlot xyPlot = plot instanceof XYPlot ? (XYPlot)plot:null;
		if (xyPlot != null)
		{
			xyPlot.setBackgroundPaint(new Color(222, 231, 247));
			xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));
			xyPlot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		}
	}


	/**
	 *
	 */
	protected JFreeChart createPieChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPieChart(evaluation);

		PiePlot piePlot = (PiePlot)jfreeChart.getPlot();

		if (getPlot().getOwnBackcolor() == null)
		{
			piePlot.setBackgroundPaint(
				new GradientPaint(0, 0, new Color(222, 231, 247), 0, getChart().getHeight() / 2, Color.white, true)
				);
		}

		piePlot.setLabelBackgroundPaint(TRANSPARENT_PAINT);
		piePlot.setLabelShadowPaint(TRANSPARENT_PAINT);
		piePlot.setLabelOutlinePaint(TRANSPARENT_PAINT);
		
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

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createPie3DChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPie3DChart(evaluation);

		PiePlot3D piePlot3D = (PiePlot3D) jfreeChart.getPlot();

		if (getPlot().getOwnBackcolor() == null)
		{
			piePlot3D.setBackgroundPaint(
				new GradientPaint(0, 0, new Color(222, 231, 247), 0, getChart().getHeight() / 2, Color.white, true)
				);
		}

		piePlot3D.setLabelBackgroundPaint(TRANSPARENT_PAINT);
		piePlot3D.setLabelShadowPaint(TRANSPARENT_PAINT);
		piePlot3D.setLabelOutlinePaint(TRANSPARENT_PAINT);

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

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createBarChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createBarChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setOutlinePaint(TRANSPARENT_PAINT);
		
		return jfreeChart;
	}

	protected JFreeChart createBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createBar3DChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		if (getPlot().getOwnBackcolor() == null)
		{
			categoryPlot.setBackgroundPaint(
				new GradientPaint(0, 0, new Color(222, 231, 247), 0, getChart().getHeight(), Color.white, true)
				);
		}

		CategoryDataset categoryDataset = (CategoryDataset)categoryPlot.getDataset();

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			categoryRenderer.setSeriesOutlinePaint(i, TRANSPARENT_PAINT);
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
		
		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		XYItemRenderer itemRenderer = xyPlot.getRenderer();
		itemRenderer.setOutlinePaint(TRANSPARENT_PAINT);
		
		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createXYBarChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createXYBarChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();

		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		XYBarRenderer renderer = (XYBarRenderer)xyPlot.getRenderer();
		renderer.setOutlinePaint(TRANSPARENT_PAINT);
		renderer.setMargin(0.1);

		return jfreeChart;
	}


	protected JFreeChart createXyAreaChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createXyAreaChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();

		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		return jfreeChart;
	}


	protected JFreeChart createScatterChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createScatterChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();

		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		return jfreeChart;
	}

	protected JFreeChart createXyLineChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createXyLineChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();

		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		return jfreeChart;
	}

	protected JFreeChart createTimeSeriesChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createTimeSeriesChart(evaluation);

		XYPlot xyPlot = (XYPlot)jfreeChart.getPlot();
		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		return jfreeChart;
	}

	protected JFreeChart createHighLowChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createHighLowChart(evaluation);

		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();

		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));

		return jfreeChart;
	}

	protected JFreeChart createCandlestickChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createCandlestickChart(evaluation);

		XYPlot xyPlot = (XYPlot) jfreeChart.getPlot();

		xyPlot.setBackgroundPaint(new Color(222, 231, 247));
		xyPlot.setRangeTickBandPaint(new Color(231, 243, 255));
		
		return jfreeChart;
	}

	protected JFreeChart createStackedBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createStackedBar3DChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		if (getPlot().getOwnBackcolor() == null)
		{
			categoryPlot.setBackgroundPaint(
				new GradientPaint(0, 0, new Color(222, 231, 247), 0, getChart().getHeight(), Color.white, true)
				);
		}

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
