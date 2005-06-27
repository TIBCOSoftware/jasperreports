/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.Map;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRIntervalXyDataset;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.fill.JRFillBar3DPlot;
import net.sf.jasperreports.charts.fill.JRFillBarPlot;
import net.sf.jasperreports.charts.fill.JRFillBubblePlot;
import net.sf.jasperreports.charts.fill.JRFillLinePlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.renderers.JCommonDrawableRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillChart extends JRFillElement implements JRChart
{


	/**
	 *
	 */
	protected byte chartType = 0;

	/**
	 *
	 */
	protected JRFont titleFont = null;
	protected JRFont subtitleFont = null;

	/**
	 *
	 */
	protected JRGroup evaluationGroup = null;

	protected JRChartDataset dataset = null;
	protected JRChartPlot plot = null;

	protected JRRenderable renderer = null;
	private String anchorName = null;
	private String hyperlinkReference = null;
	private String hyperlinkAnchor = null;
	private Integer hyperlinkPage = null;
	

	/**
	 *
	 */
	protected JRFillChart(
		JRBaseFiller filler,
		JRChart chart, 
		JRFillObjectFactory factory
		)
	{
		super(filler, chart, factory);

		/*   */
		chartType = chart.getChartType();

		switch(chartType) {
			case CHART_TYPE_AREA:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
			    break;
			case CHART_TYPE_BAR:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
			    break;
			case CHART_TYPE_BAR3D:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
			    break;
			case CHART_TYPE_BUBBLE:
				dataset = factory.getXyzDataset((JRXyzDataset) chart.getDataset());
				plot = factory.getBubblePlot((JRBubblePlot) chart.getPlot());
			    break;
			case CHART_TYPE_CANDLESTICK:
				dataset = factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getCandlestickPlot((JRCandlestickPlot) chart.getPlot());
			    break;
			case CHART_TYPE_HIGHLOW:
				dataset = factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getHighLowPlot((JRHighLowPlot) chart.getPlot());
			    break;
			case CHART_TYPE_LINE:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
			    break;
			case CHART_TYPE_PIE:
				dataset = factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPiePlot((JRPiePlot) chart.getPlot());
				break;
			case CHART_TYPE_PIE3D:
				dataset = factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPie3DPlot((JRPie3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_SCATTER:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getScatterPlot((JRScatterPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR3D:
				dataset = factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_TIMESERIES:
				// TODO after time series charts are completed
				break;
			case CHART_TYPE_XYAREA:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYBAR:
				dataset = factory.getIntervalXyDataset((JRIntervalXyDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYLINE:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}

		titleFont = factory.getFont(chart.getTitleFont());
		subtitleFont = factory.getFont(chart.getSubtitleFont());

		evaluationGroup = (JRGroup)factory.getGroup(chart.getEvaluationGroup());
	}


	/**
	 *
	 */
	public boolean isShowLegend()
	{
		return ((JRChart)parent).isShowLegend();
	}
		
	/**
	 *
	 */
	public void setShowLegend(boolean isShowLegend)
	{
	}
		
	/**
	 *
	 */
	public byte getEvaluationTime()
	{
		return ((JRChart)parent).getEvaluationTime();
	}
		
	/**
	 *
	 */
	public JRGroup getEvaluationGroup()
	{
		return evaluationGroup;
	}
		
	/**
	 *
	 */
	public JRBox getBox()
	{
		return ((JRChart)parent).getBox();
	}

	/**
	 *
	 */
	public JRFont getTitleFont()
	{
		return titleFont;
	}

	/**
	 *
	 */
	public byte getTitlePosition()
	{
		return ((JRChart)parent).getTitlePosition();
	}

	/**
	 *
	 */
	public void setTitlePosition(byte titlePosition)
	{
	}

	/**
	 *
	 */
	public Color getTitleColor()
	{
		return ((JRChart)parent).getTitleColor();
	}

	/**
	 *
	 */
	public void setTitleColor(Color titleColor)
	{
	}

	/**
	 *
	 */
	public JRFont getSubtitleFont()
	{
		return subtitleFont;
	}

	/**
	 *
	 */
	public Color getSubtitleColor()
	{
		return ((JRChart)parent).getSubtitleColor();
	}

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor)
	{
	}

	/**
	 *
	 */
	public JRExpression getTitleExpression()
	{
		return ((JRChart)parent).getTitleExpression();
	}

	/**
	 *
	 */
	public JRExpression getSubtitleExpression()
	{
		return ((JRChart)parent).getSubtitleExpression();
	}

	/**
	 *
	 */
	public byte getHyperlinkType()
	{
		return ((JRChart)parent).getHyperlinkType();
	}
		
	/**
	 *
	 */
	public byte getHyperlinkTarget()
	{
		return ((JRChart)parent).getHyperlinkTarget();
	}
		
	/**
	 *
	 */
	public JRExpression getAnchorNameExpression()
	{
		return ((JRChart)parent).getAnchorNameExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkReferenceExpression()
	{
		return ((JRChart)parent).getHyperlinkReferenceExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkAnchorExpression()
	{
		return ((JRChart)parent).getHyperlinkAnchorExpression();
	}

	/**
	 *
	 */
	public JRExpression getHyperlinkPageExpression()
	{
		return ((JRChart)parent).getHyperlinkPageExpression();
	}

	
	/**
	 *
	 */
	public JRChartDataset getDataset()
	{
		return dataset;
	}

	/**
	 *
	 */
	public JRChartPlot getPlot()
	{
		return plot;
	}

	/**
	 *
	 */
	protected JRRenderable getRenderer()
	{
		return renderer;
	}
		
	/**
	 *
	 */
	protected String getAnchorName()
	{
		return anchorName;
	}

	/**
	 *
	 */
	protected String getHyperlinkReference()
	{
		return hyperlinkReference;
	}

	/**
	 *
	 */
	protected String getHyperlinkAnchor()
	{
		return hyperlinkAnchor;
	}

	/**
	 *
	 */
	protected Integer getHyperlinkPage()
	{
		return hyperlinkPage;
	}
		

	/**
	 *
	 */
	protected JRTemplateImage getJRTemplateImage()
	{
		if (template == null)
		{
			template = new JRTemplateImage((JRChart)parent);
		}
		
		return (JRTemplateImage)template;
	}


	/**
	 *
	 */
	protected Object evaluateExpression(JRExpression expression, byte evaluation) throws JRException
	{
		return filler.calculator.evaluate(expression, evaluation);
	}


	/**
	 *
	 */
	protected void rewind() throws JRException
	{
	}


	/**
	 *
	 */
	protected void evaluate(byte evaluation) throws JRException
	{
		reset();
		
		evaluatePrintWhenExpression(evaluation);

		if (
			(isPrintWhenExpressionNull() ||
			(!isPrintWhenExpressionNull() && 
			isPrintWhenTrue()))
			)
		{
			if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
			{
				evaluateImage(evaluation);
			}
		}
	}


	/**
	 *
	 */
	protected void evaluateImage(byte evaluation) throws JRException
	{
		switch(chartType) {
			case CHART_TYPE_AREA:
				evaluateAreaImage(evaluation);
			    break;
			case CHART_TYPE_BAR:
				evaluateBarImage(evaluation);
			    break;
			case CHART_TYPE_BAR3D:
				evaluateBar3DImage(evaluation);
			    break;
			case CHART_TYPE_BUBBLE:
				evaluateBubbleImage(evaluation);
			    break;
			case CHART_TYPE_CANDLESTICK:
				evaluateCandlestickImage(evaluation);
			    break;
			case CHART_TYPE_HIGHLOW:
				evaluateHighLowImage(evaluation);
			    break;
			case CHART_TYPE_LINE:
				evaluateLineImage(evaluation);
			    break;
			case CHART_TYPE_PIE:
				evaluatePieImage(evaluation);
				break;
			case CHART_TYPE_PIE3D:
				evaluatePie3DImage(evaluation);
				break;
			case CHART_TYPE_SCATTER:
				evaluateScatterImage(evaluation);
				break;
			case CHART_TYPE_STACKEDBAR:
				evaluateStackedBarImage(evaluation);
				break;
			case CHART_TYPE_STACKEDBAR3D:
				evaluateStackedBar3DImage(evaluation);
				break;
			case CHART_TYPE_TIMESERIES:
				// TODO after time series charts are completed
				break;
			case CHART_TYPE_XYAREA:
				evaluateXyAreaImage(evaluation);
				break;
			case CHART_TYPE_XYBAR:
				evaluateXYBarImage(evaluation);
				break;
			case CHART_TYPE_XYLINE:
				evaluateXyLineImage(evaluation);
				break;
			default:
				throw new JRRuntimeException("Chart type " + getChartType() + " not supported.");
		}

		anchorName = (String)filler.calculator.evaluate(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String)filler.calculator.evaluate(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String)filler.calculator.evaluate(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer)filler.calculator.evaluate(getHyperlinkPageExpression(), evaluation);
	}


	/**
	 *
	 */
	protected boolean prepare(
		int availableStretchHeight,
		boolean isOverflow
		) throws JRException
	{
		boolean willOverflow = false;

		if (
			isPrintWhenExpressionNull() ||
			( !isPrintWhenExpressionNull() && 
			isPrintWhenTrue() )
			)
		{
			setToPrint(true);
		}
		else
		{
			setToPrint(false);
		}

		if (!isToPrint())
		{
			return willOverflow;
		}
		
		boolean isToPrint = true;
		boolean isReprinted = false;

		if (getEvaluationTime() == JRExpression.EVALUATION_TIME_NOW)
		{
			if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				availableStretchHeight < getRelativeY() - getY() - getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(isPrintWhenDetailOverflows() && (isAlreadyPrinted() || (!isAlreadyPrinted() && !isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}

			if (
				isToPrint && 
				isRemoveLineWhenBlank() &&
				getRenderer() == null
				)
			{
				isToPrint = false;
			}
		}
		else
		{
			if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}
	
			if (
				isToPrint && 
				availableStretchHeight < getRelativeY() - getY() - getBandBottomY()
				)
			{
				isToPrint = false;
				willOverflow = true;
			}
			
			if (
				isToPrint && 
				isOverflow && 
				//(this.isAlreadyPrinted() || !this.isPrintRepeatedValues())
				(isPrintWhenDetailOverflows() && (isAlreadyPrinted() || (!isAlreadyPrinted() && !isPrintRepeatedValues())))
				)
			{
				isReprinted = true;
			}
		}

		setToPrint(isToPrint);
		setReprinted(isReprinted);
		
		return willOverflow;
	}


	/**
	 *
	 */
	protected JRPrintElement fill() throws JRException
	{
		JRPrintImage printImage = new JRTemplatePrintImage(getJRTemplateImage());
		
		printImage.setX(getX());
		printImage.setY(getRelativeY());
		printImage.setHeight(getStretchHeight());

		switch (getEvaluationTime())
		{
			case JRExpression.EVALUATION_TIME_REPORT :
			{
				filler.reportBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_PAGE :
			{
				filler.pageBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_COLUMN :
			{
				filler.columnBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_GROUP :
			{
				Map specificGroupBoundCharts = (Map)filler.groupBoundCharts.get(getEvaluationGroup().getName());
				specificGroupBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_NOW :
			default :
			{
				copy(printImage);
			}
		}
		
		return printImage;
	}


	/**
	 *
	 */
	protected void copy(JRPrintImage printImage) throws JRException//FIXME NOW do hyperlinks work for evaluationTime != now?
	{
		printImage.setRenderer(getRenderer());
		printImage.setAnchorName(getAnchorName());
		printImage.setHyperlinkReference(getHyperlinkReference());
		printImage.setHyperlinkAnchor(getHyperlinkAnchor());
		printImage.setHyperlinkPage(getHyperlinkPage());
	}

	public byte getChartType()
	{
		return chartType;
	}


	public JRElement getCopy(JRAbstractObjectFactory factory)
	{
		return factory.getChart(this);
	}


	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	/**
	 * The method is never called for fill charts
	 * @param writer
	 */
	public void writeXml(JRXmlWriter writer)
	{
	}

	protected void evaluateAreaImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createAreaChart( (String)evaluateExpression(getTitleExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		plot.setBackgroundPaint( getPlot().getBackcolor() );
		plot.setBackgroundAlpha( getPlot().getBackgroundAlpha() );
		plot.setForegroundAlpha( getPlot().getForegroundAlpha() );

		renderer = new JCommonDrawableRenderer( chart );
	}


	protected void evaluateBar3DImage( byte evaluation ) throws JRException {
		JFreeChart chart =
			ChartFactory.createBarChart3D(
					(String)evaluateExpression( getTitleExpression(), evaluation ),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
					(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression(), evaluation ),
					(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
					getPlot().getOrientation(),
					isShowLegend(),
					true,
					false );

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		plot.setBackgroundPaint( getPlot().getBackcolor() );
		plot.setBackgroundAlpha( getPlot().getBackgroundAlpha() );
		plot.setForegroundAlpha( getPlot().getForegroundAlpha() );
		BarRenderer3D barRenderer3D =
			new BarRenderer3D(
				((JRFillBar3DPlot)getPlot()).getXOffset(),
				((JRFillBar3DPlot)getPlot()).getYOffset()
				);
		plot.setRenderer(barRenderer3D);

		renderer = new JCommonDrawableRenderer( chart );
	}


	/**
	 *
	 */
	protected void evaluateBarImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");
		plot.setBackgroundPaint(getPlot().getBackcolor());
		plot.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		plot.setForegroundAlpha(getPlot().getForegroundAlpha());
		((CategoryAxis)plot.getDomainAxis()).setTickMarksVisible(
			((JRFillBarPlot)getPlot()).isShowTickMarks()
			);
		((CategoryAxis)plot.getDomainAxis()).setTickLabelsVisible(
				((JRFillBarPlot)getPlot()).isShowTickLabels()
				);
		((NumberAxis)plot.getRangeAxis()).setTickMarksVisible(
				((JRFillBarPlot)getPlot()).isShowTickMarks()
				);
		((NumberAxis)plot.getRangeAxis()).setTickLabelsVisible(
				((JRFillBarPlot)getPlot()).isShowTickLabels()
				);

		renderer = new JCommonDrawableRenderer(chart);

	}


	protected void evaluateBubbleImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createBubbleChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getXAxisLabelExpression(), evaluation ),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getYAxisLabelExpression(), evaluation ),
				 (XYZDataset)((JRFillChartDataset)dataset).getDataset(),
				 getPlot().getOrientation(),
				 isShowLegend(),
				 true,
				 false);



		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint( getPlot().getBackcolor() );
		plot.setBackgroundAlpha( getPlot().getBackgroundAlpha() );
		plot.setForegroundAlpha( getPlot().getForegroundAlpha() );

		XYBubbleRenderer bubbleRenderer = new XYBubbleRenderer( ((JRFillBubblePlot)getPlot()).getScaleType() );
		plot.setRenderer( bubbleRenderer );

		renderer = new JCommonDrawableRenderer( chart );
	}


	/**
	 *
	 * @param evaluation
	 * @throws net.sf.jasperreports.engine.JRException
	 */
	protected void evaluateCandlestickImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createCandlestickChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend()
				);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		XYPlot plot = (XYPlot) chart.getPlot();
		CandlestickRenderer candlestickRenderer = (CandlestickRenderer) plot.getRenderer();
		candlestickRenderer.setDrawVolume(((JRCandlestickPlot)getPlot()).isShowVolume());

		renderer = new JCommonDrawableRenderer(chart);
	}


	/**
	 *
	 * @param evaluation
	 * @throws JRException
	 */
	protected void evaluateHighLowImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createHighLowChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend()
				);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		XYPlot plot = (XYPlot) chart.getPlot();
		HighLowRenderer hlRenderer = (HighLowRenderer) plot.getRenderer();
		hlRenderer.setDrawOpenTicks(((JRHighLowPlot)getPlot()).isShowOpenTicks());
		hlRenderer.setDrawCloseTicks(((JRHighLowPlot)getPlot()).isShowCloseTicks());

		renderer = new JCommonDrawableRenderer(chart);
	}


	protected void evaluateLineImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createLineChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRLinePlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRLinePlot)getPlot()).getValueAxisLabelExpression(), evaluation ),
				(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		plot.setBackgroundPaint( getPlot().getBackcolor() );
		plot.setBackgroundAlpha( getPlot().getBackgroundAlpha() );
		plot.setForegroundAlpha( getPlot().getForegroundAlpha() );

		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)plot.getRenderer();
		lineRenderer.setShapesVisible( ((JRFillLinePlot)getPlot()).isShowShapes() );
		lineRenderer.setLinesVisible( ((JRFillLinePlot)getPlot()).isShowLines() );

		renderer = new JCommonDrawableRenderer( chart );
	}


	/**
	 *
	 */
	protected void evaluatePie3DImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createPieChart3D(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(PieDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend(),
				true,
				false
				);

		if (chart.getTitle() != null)
		{
			chart.getTitle().setPaint(getTitleColor());
		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle();
			subtitle.setPaint(getSubtitleColor());
			chart.addSubtitle(subtitle);
		}

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setOutlinePaint(getPlot().getBackcolor());
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		plot.setBackgroundPaint(getPlot().getBackcolor());
		plot.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		plot.setForegroundAlpha(getPlot().getForegroundAlpha());
		plot.setDepthFactor(((JRFillPie3DPlot)getPlot()).getDepthFactor());

		renderer = new JCommonDrawableRenderer(chart);
	}


	/**
	 *
	 */
	protected void evaluatePieImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createPieChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(PieDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend(),
				true,
				false
				);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		PiePlot plot = (PiePlot)chart.getPlot();
		plot.setOutlinePaint(getPlot().getBackcolor());
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		plot.setBackgroundPaint(getPlot().getBackcolor());
		plot.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		plot.setForegroundAlpha(getPlot().getForegroundAlpha());

		plot.setLabelGenerator(new CustomLabelGenerator());

		renderer = new JCommonDrawableRenderer(chart);
	}

	private static class CustomLabelGenerator implements PieSectionLabelGenerator, Serializable
	{
		public String generateSectionLabel(PieDataset arg0, Comparable arg1)
		{
			return "FIXME NOW";
		}
	}


	protected void evaluateScatterImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createScatterPlot(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRScatterPlot)getPlot()).getXAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRScatterPlot)getPlot()).getYAxisLabelExpression(), evaluation ),
				(XYDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint( getPlot().getBackcolor() );
		plot.setBackgroundAlpha( getPlot().getBackgroundAlpha() );
		plot.setForegroundAlpha( getPlot().getForegroundAlpha() );

//		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)plot.getRenderer();
//		lineRenderer.setShapesVisible( ((JRFillLinePlot)getPlot()).isShowShapes() );
//		lineRenderer.setLinesVisible( ((JRFillLinePlot)getPlot()).isShowLines() );

		renderer = new JCommonDrawableRenderer( chart );
	}


	/**
	 *
	 */
	protected void evaluateStackedBar3DImage(byte evaluation) throws JRException
	{
		// TODO: a special plot implementation should be made for stacked bar charts
		JFreeChart chart =
			ChartFactory.createStackedBarChart3D(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBar3DPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");
		plot.setBackgroundPaint(getPlot().getBackcolor());
		plot.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		plot.setForegroundAlpha(getPlot().getForegroundAlpha());

		renderer = new JCommonDrawableRenderer(chart);
	}


	/**
	 *
	 */
	protected void evaluateStackedBarImage(byte evaluation) throws JRException
	{
		// TODO: a special plot implementation should be made for stacked bar charts
		JFreeChart chart =
			ChartFactory.createStackedBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");
		plot.setBackgroundPaint(getPlot().getBackcolor());
		plot.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		plot.setForegroundAlpha(getPlot().getForegroundAlpha());

		renderer = new JCommonDrawableRenderer(chart);
	}


	protected void evaluateXyAreaImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createXYAreaChart(
			(String)evaluateExpression(getTitleExpression(), evaluation ),
			(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
			(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
			(XYDataset)((JRFillChartDataset)dataset).getDataset(),
			getPlot().getOrientation(),
			isShowLegend(),
			true,
			false
			);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint( getPlot().getBackcolor() );
		plot.setBackgroundAlpha( getPlot().getBackgroundAlpha() );
		plot.setForegroundAlpha( getPlot().getForegroundAlpha() );

		renderer = new JCommonDrawableRenderer( chart );
	}


	/**
	 *
	 */
	protected void evaluateXYBarImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createXYBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				true,
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(IntervalXYDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		XYPlot plot = (XYPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");
		plot.setBackgroundPaint(getPlot().getBackcolor());
		plot.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		plot.setForegroundAlpha(getPlot().getForegroundAlpha());
//		((XYPlot)plot.getDomainAxis()).setTickMarksVisible(
//			((JRFillBarPlot)getPlot()).isShowTickMarks()
//			);
//		((CategoryAxis)plot.getDomainAxis()).setTickLabelsVisible(
//				((JRFillBarPlot)getPlot()).isShowTickLabels()
//				);
//		((NumberAxis)plot.getRangeAxis()).setTickMarksVisible(
//				((JRFillBarPlot)getPlot()).isShowTickMarks()
//				);
//		((NumberAxis)plot.getRangeAxis()).setTickLabelsVisible(
//				((JRFillBarPlot)getPlot()).isShowTickLabels()
//				);

		renderer = new JCommonDrawableRenderer(chart);
	}


	protected void evaluateXyLineImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createXYLineChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRLinePlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRLinePlot)getPlot()).getValueAxisLabelExpression(), evaluation ),
				(XYDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());
			Map attributes = getTitleFont().getNonPdfAttributes();
			title.setFont(new Font(attributes));

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());
			Map attributes = getSubtitleFont().getNonPdfAttributes();
			subtitle.setFont(new Font(attributes));
			chart.addSubtitle(subtitle);
		}

		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint( getPlot().getBackcolor() );
		plot.setBackgroundAlpha( getPlot().getBackgroundAlpha() );
		plot.setForegroundAlpha( getPlot().getForegroundAlpha() );

//		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)plot.getRenderer();
//		lineRenderer.setShapesVisible( ((JRFillLinePlot)getPlot()).isShowShapes() );
//		lineRenderer.setLinesVisible( ((JRFillLinePlot)getPlot()).isShowLines() );

		renderer = new JCommonDrawableRenderer( chart );
	}

}
