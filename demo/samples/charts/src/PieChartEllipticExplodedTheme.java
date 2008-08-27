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
public class PieChartEllipticExplodedTheme extends SimpleChartTheme
{

	/**
	 *
	 */
	public PieChartEllipticExplodedTheme(JRFillChart chart)
	{
		super(chart);
	}
	
	/**
	 *
	 */
	protected JFreeChart createPieChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPieChart(evaluation);

		PiePlot piePlot = (PiePlot)jfreeChart.getPlot();

		piePlot.setExplodePercent(1, 0.2);
		piePlot.setExplodePercent(2, 0.2);
		piePlot.setExplodePercent(3, 0.2);
		piePlot.setExplodePercent(4, 0.2);
		piePlot.setExplodePercent(5, 0.2);
		piePlot.setCircular(false);
		piePlot.setShadowXOffset(1d);
		piePlot.setShadowYOffset(1d);
		piePlot.setInteriorGap(0.1);

		return jfreeChart;
	}


	/**
	 *
	 */
	protected JFreeChart createPie3DChart(byte evaluation) throws JRException
	{
		JFreeChart jfreeChart = super.createPie3DChart(evaluation);

		PiePlot3D piePlot3D = (PiePlot3D) jfreeChart.getPlot();

		piePlot3D.setExplodePercent(1, 0.2);
		piePlot3D.setExplodePercent(2, 0.2);
		piePlot3D.setExplodePercent(3, 0.2);
		piePlot3D.setExplodePercent(4, 0.2);
		piePlot3D.setExplodePercent(5, 0.2);
		piePlot3D.setCircular(false);
		piePlot3D.setShadowXOffset(1d);
		piePlot3D.setShadowYOffset(1d);
		piePlot3D.setInteriorGap(0.1);
		return jfreeChart;
	}

}
