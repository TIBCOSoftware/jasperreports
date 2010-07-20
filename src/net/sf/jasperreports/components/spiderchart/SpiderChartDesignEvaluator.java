/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.components.spiderchart;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.charts.util.ChartUtil;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRProperties;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 * Spider Chart design evaluator.
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: SpiderChartDesignConverter.java 3898 2010-07-19 11:30:26Z shertage $
 */
public class SpiderChartDesignEvaluator
{

	/**
	 *
	 */
	private static DefaultCategoryDataset sampleDataset;
	
	/**
	 *
	 */
	private static final Double MAX_VALUE = Double.valueOf(10);
	
	/**
	 * 
	 */
	public static JRRenderable evaluateRenderer(ReportConverter reportConverter, JRComponentElement element)
	{
		SpiderChartComponent chartComponent = (SpiderChartComponent) element.getComponent();
		ChartSettings chartSettings = chartComponent.getChartSettings();
		SpiderPlot plot = chartComponent.getPlot();
		
		SpiderWebPlot spiderWebPlot = new SpiderWebPlot(getSampleDataset());

        if(plot.getAxisLineColor() != null)
        {
        	spiderWebPlot.setAxisLinePaint(plot.getAxisLineColor());
        }
        if(plot.getAxisLineWidth() != null)
        {
        	spiderWebPlot.setAxisLineStroke(new BasicStroke(plot.getAxisLineWidth()));
        }
        if(plot.getBackcolor() != null)
        {
        	spiderWebPlot.setBackgroundPaint(plot.getBackcolor());
        }
        if(plot.getBackgroundAlpha() != null)
        {
        	spiderWebPlot.setBackgroundAlpha(plot.getBackgroundAlpha());
        }
        if(plot.getForegroundAlpha() != null)
        {
        	spiderWebPlot.setForegroundAlpha(plot.getForegroundAlpha());
        }
        if(plot.getHeadPercent() != null)
        {
        	spiderWebPlot.setHeadPercent(plot.getHeadPercent());
        }
        if(plot.getInteriorGap() != null)
        {
        	spiderWebPlot.setInteriorGap(plot.getInteriorGap());
        }
        if(plot.getLabelColor() != null)
        {
        	spiderWebPlot.setLabelPaint(plot.getLabelColor());
        }
        if(plot.getLabelFont() != null)
        {
        	spiderWebPlot.setLabelFont(JRFontUtil.getAwtFont(plot.getLabelFont(), Locale.getDefault()));
        }
        if(plot.getLabelGap() != null)
        {
        	spiderWebPlot.setAxisLabelGap(plot.getLabelGap());
        }
        
        spiderWebPlot.setMaxValue(MAX_VALUE);
        
        if(plot.getRotation() != null)
        {
        	spiderWebPlot.setDirection(plot.getRotation().getRotation());
        }
        if(plot.getStartAngle() != null)
        {
        	spiderWebPlot.setStartAngle(plot.getStartAngle());
        }
        if(plot.getTableOrder() != null)
        {
        	spiderWebPlot.setDataExtractOrder(plot.getTableOrder().getOrder());
        }
        if(plot.getWebFilled() != null)
        {
        	spiderWebPlot.setWebFilled(plot.getWebFilled());
        }

        spiderWebPlot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        spiderWebPlot.setLabelGenerator(new StandardCategoryItemLabelGenerator());
        
        String titleText = JRExpressionUtil.getExpressionText(chartSettings.getTitleExpression());
        
        Font titleFont = chartSettings.getTitleFont() != null 
        	? JRFontUtil.getAwtFont(chartSettings.getTitleFont(), Locale.getDefault())
        	: TextTitle.DEFAULT_FONT;
        	
        JFreeChart jfreechart = new JFreeChart(titleText, titleFont, spiderWebPlot, true);

		if(chartSettings.getBackcolor() != null)
		{
			jfreechart.setBackgroundPaint(chartSettings.getBackcolor());
		}
		
        RectangleEdge titleEdge = getEdge(chartSettings.getTitlePosition(), RectangleEdge.TOP);
		
		if (titleText != null)
		{
			TextTitle title = jfreechart.getTitle();
			title.setText(titleText);
			if(chartSettings.getTitleColor() != null)
			{
				title.setPaint(chartSettings.getTitleColor());
			}
			
			title.setFont(titleFont);
			title.setPosition(titleEdge);
			jfreechart.setTitle(title);
		}

        String subtitleText = JRExpressionUtil.getExpressionText(chartSettings.getSubtitleExpression());
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setText(subtitleText);
			if(chartSettings.getSubtitleColor() != null)
			{
				subtitle.setPaint(chartSettings.getSubtitleColor());
			}

			if(chartSettings.getSubtitleColor() != null)
			{
		        Font subtitleFont = chartSettings.getSubtitleFont() != null 
	        	? JRFontUtil.getAwtFont(chartSettings.getSubtitleFont(), Locale.getDefault())
	        	: TextTitle.DEFAULT_FONT;
				subtitle.setFont(subtitleFont);
			}
			
			subtitle.setPosition(titleEdge);

			jfreechart.addSubtitle(subtitle);
		}

		// Apply all of the legend formatting options
		LegendTitle legend = jfreechart.getLegend();
		if (legend != null)
		{
			legend.setVisible((chartSettings.getShowLegend() == null || chartSettings.getShowLegend()));
			if (legend.isVisible())
			{
				if(chartSettings.getLegendColor() != null)
				{
					legend.setItemPaint(chartSettings.getLegendColor());
				}
				if (chartSettings.getLegendBackgroundColor() != null)
				{
					legend.setBackgroundPaint(chartSettings.getLegendBackgroundColor());
				}

				if(chartSettings.getLegendFont() != null)
				{
					legend.setItemFont(JRFontUtil.getAwtFont(chartSettings.getLegendFont(), Locale.getDefault()));
				}
				legend.setPosition(getEdge(chartSettings.getLegendPosition(), RectangleEdge.BOTTOM));
			}
		}
			
		Rectangle2D rectangle = new Rectangle2D.Double(0, 0, element.getWidth(), element.getHeight());

		String renderType = chartSettings.getRenderType();
		if(renderType == null)
		{
			renderType = JRProperties.getProperty(reportConverter.getReport(), JRChart.PROPERTY_CHART_RENDER_TYPE);
		}

		return 
			ChartUtil.getChartRendererFactory(renderType).getRenderer(
				jfreechart, 
				null,
				rectangle
				);
	}
	
	public static CategoryDataset getSampleDataset()
	{
		if (sampleDataset == null)
		{
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			dataset.addValue(1.0, "Series 1", "Category 1");
			dataset.addValue(5.0, "Series 1", "Category 2");
			dataset.addValue(4.0, "Series 1", "Category 3");
			dataset.addValue(3.0, "Series 1", "Category 4");
			dataset.addValue(6.0, "Series 1", "Category 5");
			dataset.addValue(4.0, "Series 1", "Category 6");
			dataset.addValue(3.0, "Series 1", "Category 7");
			dataset.addValue(5.0, "Series 2", "Category 1");
			dataset.addValue(7.0, "Series 2", "Category 2");
			dataset.addValue(8.0, "Series 2", "Category 3");
			dataset.addValue(6.0, "Series 2", "Category 4");
			dataset.addValue(9.0, "Series 2", "Category 5");
			dataset.addValue(8.0, "Series 2", "Category 6");
			dataset.addValue(7.0, "Series 2", "Category 7");
			dataset.addValue(5.0, "Series 3", "Category 1");
			dataset.addValue(4.0, "Series 3", "Category 2");
			dataset.addValue(6.0, "Series 3", "Category 3");
			dataset.addValue(3.0, "Series 3", "Category 4");
			dataset.addValue(2.0, "Series 3", "Category 5");
			dataset.addValue(7.0, "Series 3", "Category 6");
			dataset.addValue(5.0, "Series 3", "Category 7");
			
			sampleDataset = dataset;
		}
		
		return sampleDataset;
	}

	public Object getLabelGenerator() {
		return null;
	}

	public Locale getLocale() {
		return null;//FIXMETHEME
	}

	public TimeZone getTimeZone() {
		return null;
	}

	/**
	 *
	 */
	private static RectangleEdge getEdge(EdgeEnum position, RectangleEdge defaultPosition)
	{
		RectangleEdge edge = defaultPosition;
		if(position != null)
		{
			switch (position)
			{
				case TOP :
				{
					edge = RectangleEdge.TOP;
					break;
				}
				case BOTTOM :
				{
					edge = RectangleEdge.BOTTOM;
					break;
				}
				case LEFT :
				{
					edge = RectangleEdge.LEFT;
					break;
				}
				case RIGHT :
				{
					edge = RectangleEdge.RIGHT;
					break;
				}
			}
		}
		return edge;
	}

}
