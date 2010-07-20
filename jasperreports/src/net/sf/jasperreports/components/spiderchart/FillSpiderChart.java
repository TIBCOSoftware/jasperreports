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
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillCloneFactory;
import net.sf.jasperreports.engine.fill.JRFillCloneable;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillHyperlinkHelper;
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
 * @version $Id: FillSpiderChart.java 3892 2010-07-16 13:43:20Z shertage $
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
	private JRPrintHyperlinkParameters hyperlinkParameters;
	
	private JRFillExpressionEvaluator expressionEvaluator;
	private ChartHyperlinkProvider chartHyperlinkProvider;
	private JRRenderable renderer;

	public FillSpiderChart(SpiderChartComponent chartComponent, JRFillObjectFactory factory)
	{
		this.chartComponent = chartComponent;
		this.chartSettings = new FillChartSettings(chartComponent.getChartSettings(), factory);
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
			ChartUtil.getChartRendererFactory(getChartSettings().getRenderType()).getRenderer(
				chart, 
				chartHyperlinkProvider,
				rectangle
				);
	}

	protected JFreeChart evaluateChart(byte evaluation) throws JRException
	{
		maxValue = (Double) fillContext.evaluate(getPlot().getMaxValueExpression(), evaluation);
        titleText = (String) fillContext.evaluate(getChartSettings().getTitleExpression(), evaluation);
		subtitleText = (String)fillContext.evaluate(getChartSettings().getSubtitleExpression(), evaluation);
		anchorName = (String) fillContext.evaluate(getChartSettings().getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) fillContext.evaluate(getChartSettings().getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String) fillContext.evaluate(getChartSettings().getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) fillContext.evaluate(getChartSettings().getHyperlinkPageExpression(), evaluation);
		hyperlinkTooltip = (String) fillContext.evaluate(getChartSettings().getHyperlinkTooltipExpression(), evaluation);
		hyperlinkParameters = JRFillHyperlinkHelper.evaluateHyperlinkParameters(getChartSettings(), expressionEvaluator, evaluation);

		dataset.evaluateDatasetRun(evaluation);
		dataset.finishDataset();

		chartHyperlinkProvider = new CategoryChartHyperlinkProvider(dataset.getItemHyperlinks());

		bookmarkLevel = getChartSettings().getBookmarkLevel();
		
		SpiderWebPlot spiderWebPlot = new SpiderWebPlot((DefaultCategoryDataset)dataset.getCustomDataset());

        if(getPlot().getAxisLineColor() != null)
        {
        	spiderWebPlot.setAxisLinePaint(getPlot().getAxisLineColor());
        }
        if(getPlot().getAxisLineWidth() != null)
        {
        	spiderWebPlot.setAxisLineStroke(new BasicStroke(getPlot().getAxisLineWidth()));
        }
        if(getPlot().getBackcolor() != null)
        {
        	spiderWebPlot.setBackgroundPaint(getPlot().getBackcolor());
        }
        if(getPlot().getBackgroundAlpha() != null)
        {
        	spiderWebPlot.setBackgroundAlpha(getPlot().getBackgroundAlpha());
        }
        if(getPlot().getForegroundAlpha() != null)
        {
        	spiderWebPlot.setForegroundAlpha(getPlot().getForegroundAlpha());
        }
        if(getPlot().getHeadPercent() != null)
        {
        	spiderWebPlot.setHeadPercent(getPlot().getHeadPercent());
        }
        if(getPlot().getInteriorGap() != null)
        {
        	spiderWebPlot.setInteriorGap(getPlot().getInteriorGap());
        }
        if(getPlot().getLabelColor() != null)
        {
        	spiderWebPlot.setLabelPaint(getPlot().getLabelColor());
        }
        if(getPlot().getLabelFont() != null)
        {
        	spiderWebPlot.setLabelFont(JRFontUtil.getAwtFont(getPlot().getLabelFont(), Locale.getDefault()));
        }
        if(getPlot().getLabelGap() != null)
        {
        	spiderWebPlot.setAxisLabelGap(getPlot().getLabelGap());
        }
        if(maxValue != null)
        {
        	spiderWebPlot.setMaxValue(maxValue);
        }
        if(getPlot().getRotation() != null)
        {
        	spiderWebPlot.setDirection(getPlot().getRotation().getRotation());
        }
        if(getPlot().getStartAngle() != null)
        {
        	spiderWebPlot.setStartAngle(getPlot().getStartAngle());
        }
        if(getPlot().getTableOrder() != null)
        {
        	spiderWebPlot.setDataExtractOrder(getPlot().getTableOrder().getOrder());
        }
        if(getPlot().getWebFilled() != null)
        {
        	spiderWebPlot.setWebFilled(getPlot().getWebFilled());
        }

        spiderWebPlot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        spiderWebPlot.setLabelGenerator(new StandardCategoryItemLabelGenerator());
        
        Font titleFont = getChartSettings().getTitleFont() != null 
        	? JRFontUtil.getAwtFont(getChartSettings().getTitleFont(), Locale.getDefault())
        	: TextTitle.DEFAULT_FONT;
        	
        JFreeChart jfreechart = new JFreeChart(titleText, titleFont, spiderWebPlot, true);

		if(chartSettings.getBackcolor() != null)
		{
			jfreechart.setBackgroundPaint(chartSettings.getBackcolor());
		}
		
		RectangleEdge titleEdge = getEdge(getChartSettings().getTitlePosition(), RectangleEdge.TOP);
		
		if (titleText != null)
		{
			TextTitle title = jfreechart.getTitle();
			title.setText(titleText);
			if(getChartSettings().getTitleColor() != null)
			{
				title.setPaint(getChartSettings().getTitleColor());
			}
			
			title.setFont(titleFont);
			title.setPosition(titleEdge);
			jfreechart.setTitle(title);
		}

		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setText(subtitleText);
			if(getChartSettings().getSubtitleColor() != null)
			{
				subtitle.setPaint(getChartSettings().getSubtitleColor());
			}

			if(getChartSettings().getSubtitleColor() != null)
			{
		        Font subtitleFont = getChartSettings().getSubtitleFont() != null 
	        	? JRFontUtil.getAwtFont(getChartSettings().getSubtitleFont(), Locale.getDefault())
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
				if(getChartSettings().getLegendColor() != null)
				{
					legend.setItemPaint(getChartSettings().getLegendColor());
				}
				if (getChartSettings().getLegendBackgroundColor() != null)
				{
					legend.setBackgroundPaint(getChartSettings().getLegendBackgroundColor());
				}
	
				if(getChartSettings().getLegendFont() != null)
				{
					legend.setItemFont(JRFontUtil.getAwtFont(getChartSettings().getLegendFont(), Locale.getDefault()));
				}
				legend.setPosition(getEdge(getChartSettings().getLegendPosition(), RectangleEdge.BOTTOM));
			}
		}
		return jfreechart;
	
	}


	public JRPrintElement fill()
	{
		JRComponentElement element = fillContext.getComponentElement();
		JRTemplateImage templateImage = new JRTemplateImage(fillContext.getElementOrigin(), 
				fillContext.getDefaultStyleProvider());
		templateImage.setStyle(fillContext.getElementStyle());
		templateImage.setLinkType(getLinkType());
		templateImage.setLinkTarget(getLinkTarget());
		
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
		printImage.setHyperlinkParameters(hyperlinkParameters);
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
	 * @return the chartSettings
	 */
	public FillChartSettings getChartSettings() {
		return chartSettings;
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

	public String getLinkType()
	{
		return getChartSettings().getLinkType();
	}

	public String getLinkTarget()
	{
		return getChartSettings().getLinkTarget();
	}

	/**
	 * @return the hyperlinkParameters
	 */
	public JRPrintHyperlinkParameters getHyperlinkParameters() {
		return hyperlinkParameters;
	}

}
