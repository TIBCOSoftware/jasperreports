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

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.charts.util.CategoryChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartUtil;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRTemplateImage;
import net.sf.jasperreports.engine.fill.JRTemplatePrintImage;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRFontUtil;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;


/**
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class FillSpiderChart extends BaseFillComponent implements JRFillCloneable
{

	private final SpiderChartComponent chartComponent;
	private final FillChartSettings chartSettings;
	private final FillSpiderDataset dataset;
	private final FillSpiderPlot plot;
	
	private Double maxValue;
	private String titleText;
	private String subtitleText;
	private String anchorName;
	private String hyperlinkReference;
	private String hyperlinkAnchor;
	private Integer hyperlinkPage;
	private String hyperlinkTooltip;
	private Integer bookmarkLevel;
	
	private JRFillExpressionEvaluator expressionEvaluator;
	private ChartHyperlinkProvider chartHyperlinkProvider;
	private JRRenderable renderer;

	public FillSpiderChart(SpiderChartComponent chartComponent, JRFillObjectFactory factory)
	{
		this.chartComponent = chartComponent;
		this.chartSettings = new FillChartSettings(chartComponent.getChart(), factory);
		this.dataset = new FillSpiderDataset(chartComponent.getDataset(), factory);
		factory.registerElementDataset(this.dataset);
		this.plot = new FillSpiderPlot(chartComponent.getPlot(), factory);
		this.expressionEvaluator = factory.getExpressionEvaluator();
	}

	protected boolean isEvaluateNow()
	{
		return chartComponent.getEvaluationTime() == EvaluationTimeEnum.NOW;
	}
	
	public void evaluate(byte evaluation) throws JRException
	{
		if (isEvaluateNow())
		{
			evaluateRenderer(evaluation);
		}
	}

	/**
	 *
	 */
	protected void evaluateRenderer(byte evaluation) throws JRException
	{
		JFreeChart chart = evaluateChart(evaluation);
		JRComponentElement element = fillContext.getComponentElement();		
		Rectangle2D rectangle = new Rectangle2D.Double(0,0,element.getWidth(),element.getHeight());

		renderer = 
			ChartUtil.getChartRendererFactory(chartSettings.getRenderType()).getRenderer(
				chart, 
				chartHyperlinkProvider,
				rectangle
				);
	}

	protected JFreeChart evaluateChart(byte evaluation) throws JRException
	{
		maxValue = (Double) fillContext.evaluate(plot.getMaxValueExpression(), evaluation);
        titleText = (String) fillContext.evaluate(chartSettings.getTitleExpression(), evaluation);
		subtitleText = (String)fillContext.evaluate(chartSettings.getSubtitleExpression(), evaluation);
		anchorName = (String) fillContext.evaluate(chartSettings.getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) fillContext.evaluate(chartSettings.getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String) fillContext.evaluate(chartSettings.getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) fillContext.evaluate(chartSettings.getHyperlinkPageExpression(), evaluation);
		hyperlinkTooltip = (String) fillContext.evaluate(chartSettings.getHyperlinkTooltipExpression(), evaluation);
		
		dataset.evaluateDatasetRun(evaluation);
		chartHyperlinkProvider = new CategoryChartHyperlinkProvider(dataset.getItemHyperlinks());

		bookmarkLevel = chartSettings.getBookmarkLevel();
		
		SpiderWebPlot spiderWebPlot = new SpiderWebPlot((DefaultCategoryDataset)dataset.getCustomDataset());

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
        if(maxValue != null)
        {
        	spiderWebPlot.setMaxValue(maxValue);
        }
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

        spiderWebPlot.setLabelGenerator(new StandardCategoryItemLabelGenerator());
        spiderWebPlot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        
        Font titleFont = chartSettings.getTitleFont() != null 
        	? JRFontUtil.getAwtFont(chartSettings.getTitleFont(), Locale.getDefault())
        	: TextTitle.DEFAULT_FONT;
        JFreeChart jfreechart = new JFreeChart(titleText, titleFont, spiderWebPlot, true);

		RectangleEdge titleEdge = getEdge(chartSettings.getTitlePositionValue(), RectangleEdge.TOP);
		
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
		if (Boolean.TRUE.equals(chartSettings.getShowLegend()) && legend != null)
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
			legend.setPosition(getEdge(chartSettings.getLegendPositionValue(), RectangleEdge.BOTTOM));
		}
		
		return jfreechart;
	
	}


	public JRPrintElement fill()
	{
		JRComponentElement element = fillContext.getComponentElement();
		JRTemplateImage templateImage = new JRTemplateImage(fillContext.getElementOrigin(), 
				fillContext.getDefaultStyleProvider());
		templateImage.setStyle(fillContext.getElementStyle());
		
		JRTemplatePrintImage image = new JRTemplatePrintImage(templateImage);
		image.setX(element.getX());
		image.setY(fillContext.getElementPrintY());
		image.setWidth(element.getWidth());
		image.setHeight(element.getHeight());

		if (isEvaluateNow())
		{
			copy(image);
		}
		else
		{
			fillContext.registerDelayedEvaluation(image, 
					chartComponent.getEvaluationTime(), chartComponent.getEvaluationGroup());
		}
		
		return image;
	}

	public FillPrepareResult prepare(int availableHeight)
	{
		return FillPrepareResult.PRINT_NO_STRETCH;
	}

	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		throw new UnsupportedOperationException();
	}

	public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateRenderer(evaluation);
		copy((JRPrintImage) element);
	}

	protected void copy(JRPrintImage printImage)
	{
		printImage.setRenderer(getRenderer());
		printImage.setAnchorName(getAnchorName());
		printImage.setHyperlinkReference(getHyperlinkReference());
		printImage.setHyperlinkAnchor(getHyperlinkAnchor());
		printImage.setHyperlinkPage(getHyperlinkPage());
		printImage.setHyperlinkTooltip(getHyperlinkTooltip());
		printImage.setBookmarkLevel(getBookmarkLevel());
//		printImage.setHyperlinkParameters(hyperlinkParameters);
//		transferProperties(printImage);
			
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

	protected ChartHyperlinkProvider getHyperlinkProvider()
	{
		return chartHyperlinkProvider;
	}

	/**
	 * @return the dataset
	 */
	public FillSpiderDataset getDataset() {
		return dataset;
	}

	/**
	 * @return the plot
	 */
	public FillSpiderPlot getPlot() {
		return plot;
	}

	/**
	 * @return the maxValue
	 */
	public Double getMaxValue() {
		return maxValue;
	}

	/**
	 * @return the titleText
	 */
	public String getTitleText() {
		return titleText;
	}

	/**
	 * @return the subtitleText
	 */
	public String getSubtitleText() {
		return subtitleText;
	}

	/**
	 * @return the anchorName
	 */
	public String getAnchorName() {
		return anchorName;
	}

	/**
	 * @return the hyperlinkReference
	 */
	public String getHyperlinkReference() {
		return hyperlinkReference;
	}

	/**
	 * @return the hyperlinkAnchor
	 */
	public String getHyperlinkAnchor() {
		return hyperlinkAnchor;
	}

	/**
	 * @return the hyperlinkPage
	 */
	public Integer getHyperlinkPage() {
		return hyperlinkPage;
	}

	/**
	 * @return the hyperlinkTooltip
	 */
	public String getHyperlinkTooltip() {
		return hyperlinkTooltip;
	}
	
	/**
	 * @return the hyperlinkTooltip
	 */
	public Integer getBookmarkLevel() {
		return bookmarkLevel;
	}

	/**
	 * @return the expressionEvaluator
	 */
	public JRFillExpressionEvaluator getExpressionEvaluator() {
		return expressionEvaluator;
	}

	/**
	 * @return the renderer
	 */
	public JRRenderable getRenderer() {
		return renderer;
	}


}
