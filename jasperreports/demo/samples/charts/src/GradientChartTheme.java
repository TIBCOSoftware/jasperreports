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
import net.sf.jasperreports.engine.fill.JRFillChart;

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
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class GradientChartTheme extends SimpleChartTheme
{

	protected GradientPaint[] colors = null;
	protected Color LIGHT_GREEN = new Color(239,255,147);
	
	/**
	 *
	 */
	public GradientChartTheme(JRFillChart chart)
	{
		super(chart);
		colors = new GradientPaint[]{
				new GradientPaint( 0f,0f, Color.GREEN, 0f,0f, Color.ORANGE),
				new GradientPaint( 0f,0f, Color.YELLOW, 0f,0f, Color.RED),
				new GradientPaint( 0f,0f, Color.CYAN, 0f,0f, Color.PINK),
				new GradientPaint( 0f,0f, Color.YELLOW, 0f,0f, Color.MAGENTA),
				new GradientPaint( 0f,0f, Color.CYAN, 0f,0f, Color.DARK_GRAY)
				};
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
			
			plot.setBackgroundPaint(
					new GradientPaint(0, 0, LIGHT_GREEN, 0, getChart().getHeight(), Color.WHITE, true)
					);
		}
		
		if(plot instanceof CategoryPlot)
		{
			CategoryItemRenderer categoryRenderer = ((CategoryPlot)plot).getRenderer();
			categoryRenderer.setSeriesPaint(0, colors[0]);
			categoryRenderer.setSeriesPaint(1, colors[1]);
			categoryRenderer.setSeriesPaint(2, colors[2]);
			categoryRenderer.setSeriesPaint(3, colors[3]);
			categoryRenderer.setSeriesPaint(4, colors[4]);
		}
	}


	protected JFreeChart createBar3DChart(byte evaluation) throws JRException 
	{
		JFreeChart jfreeChart = super.createBar3DChart(evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)jfreeChart.getPlot();
		categoryPlot.setBackgroundPaint(
				new GradientPaint(0, 0, LIGHT_GREEN, 0, getChart().getHeight(), Color.WHITE, true)
				);
		CategoryDataset categoryDataset = (CategoryDataset)categoryPlot.getDataset();

		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		for(int i = 0; i < categoryDataset.getRowCount(); i++)
		{
			categoryRenderer.setSeriesOutlinePaint(i, colors[i]);
		}
		
		return jfreeChart;
	}

	
}
