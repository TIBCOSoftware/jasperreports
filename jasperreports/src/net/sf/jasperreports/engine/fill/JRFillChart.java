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
import java.util.Map;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.fill.JRFillBar3DPlot;
import net.sf.jasperreports.charts.fill.JRFillBarPlot;
import net.sf.jasperreports.charts.fill.JRFillBubblePlot;
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillLinePlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillTimePeriodDataset;
import net.sf.jasperreports.charts.fill.JRFillTimeSeriesDataset;
import net.sf.jasperreports.charts.fill.JRFillXyDataset;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChild;
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
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.renderers.JCommonDrawableRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.TimeSeriesCollection;
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
	private static final Color TRANSPARENT_PAINT = new Color(0, 0, 0, 0);

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

	protected String customizerClass;
	protected JRChartCustomizer chartCustomizer;

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
				dataset = factory.getTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
				plot = factory.getTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
				break;
			case CHART_TYPE_XYAREA:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYBAR:
				switch (chart.getDataset().getDatasetType()){
					case JRChartDataset.TIMESERIES_DATASET:
						dataset = factory.getTimeSeriesDataset( (JRTimeSeriesDataset)chart.getDataset() );
						break;
					case JRChartDataset.TIMEPERIOD_DATASET:
						dataset = factory.getTimePeriodDataset((JRTimePeriodDataset) chart.getDataset() );
						break;
					case JRChartDataset.XY_DATASET:
						dataset = factory.getXyDataset( (JRXyDataset)chart.getDataset() );
						break;
				}
				
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYLINE:
				dataset = factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}

		if (chart.getTitleFont() != null)
			titleFont = factory.getFont(chart.getTitleFont());

		if (chart.getSubtitleFont() != null)
			subtitleFont = factory.getFont(chart.getSubtitleFont());

		evaluationGroup = factory.getGroup(chart.getEvaluationGroup());

		customizerClass = chart.getCustomizerClass();
		if (customizerClass != null && customizerClass.length() > 0) {
			try {
				Class myClass = Class.forName(customizerClass);
				chartCustomizer = (JRChartCustomizer) myClass.newInstance();
			} catch (Exception e) {
				throw new JRRuntimeException(e.getMessage());
			}
		}
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
	 * @deprecated
	 */
	public JRBox getBox()
	{
		return (JRBox)parent;
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
			template = new JRTemplateImage(filler.getJasperPrint().getDefaultStyleProvider(), (JRChart)parent);
		}
		
		return (JRTemplateImage)template;
	}


	/**
	 *
	 */
	protected void rewind()
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
		evaluateDatasetRun(evaluation);
		
		JFreeChart chart;
		switch(chartType) {
			case CHART_TYPE_AREA:
				chart = evaluateAreaImage(evaluation);
				break;
			case CHART_TYPE_BAR:
				chart = evaluateBarImage(evaluation);
				break;
			case CHART_TYPE_BAR3D:
				chart = evaluateBar3DImage(evaluation);
				break;
			case CHART_TYPE_BUBBLE:
				chart = evaluateBubbleImage(evaluation);
				break;
			case CHART_TYPE_CANDLESTICK:
				chart = evaluateCandlestickImage(evaluation);
				break;
			case CHART_TYPE_HIGHLOW:
				chart = evaluateHighLowImage(evaluation);
				break;
			case CHART_TYPE_LINE:
				chart = evaluateLineImage(evaluation);
				break;
			case CHART_TYPE_PIE:
				chart = evaluatePieImage(evaluation);
				break;
			case CHART_TYPE_PIE3D:
				chart = evaluatePie3DImage(evaluation);
				break;
			case CHART_TYPE_SCATTER:
				chart = evaluateScatterImage(evaluation);
				break;
			case CHART_TYPE_STACKEDBAR:
				chart = evaluateStackedBarImage(evaluation);
				break;
			case CHART_TYPE_STACKEDBAR3D:
				chart = evaluateStackedBar3DImage(evaluation);
				break;
			case CHART_TYPE_TIMESERIES:
				chart = evaluateTimeSeriesImage( evaluation );
				break;
			case CHART_TYPE_XYAREA:
				chart = evaluateXyAreaImage(evaluation);
				break;
			case CHART_TYPE_XYBAR:
				chart = evaluateXYBarImage(evaluation);
				break;
			case CHART_TYPE_XYLINE:
				chart = evaluateXyLineImage(evaluation);
				break;
			default:
				throw new JRRuntimeException("Chart type " + getChartType() + " not supported.");
		}

		if (chartCustomizer != null)
			chartCustomizer.customize(chart, this);

		renderer = new JCommonDrawableRenderer( chart );

		anchorName = (String) evaluateExpression(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) evaluateExpression(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String) evaluateExpression(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) evaluateExpression(getHyperlinkPageExpression(), evaluation);
	}


	/**
	 *
	 */
	protected boolean prepare(
		int availableStretchHeight,
		boolean isOverflow
		)
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
	protected JRPrintElement fill()
	{
		JRPrintImage printImage = new JRTemplatePrintImage(getJRTemplateImage());
		
		printImage.setX(getX());
		printImage.setY(getRelativeY());
		printImage.setWidth(getWidth());
		printImage.setHeight(getStretchHeight());

		switch (getEvaluationTime())
		{
			case JRExpression.EVALUATION_TIME_REPORT :
			{
				filler.reportBoundElements.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_PAGE :
			{
				filler.pageBoundElements.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_COLUMN :
			{
				filler.columnBoundElements.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_GROUP :
			{
				Map specificGroupBoundCharts = (Map)filler.groupBoundElements.get(getEvaluationGroup().getName());
				specificGroupBoundCharts.put(printImage, this);
				break;
			}
			case JRExpression.EVALUATION_TIME_BAND :
			{
				band.boundElements.put(printImage, this);
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
	protected void copy(JRPrintImage printImage)
	{
		printImage.setRenderer(getRenderer());
		printImage.setAnchorName(getAnchorName());
		printImage.setHyperlinkReference(getHyperlinkReference());
		printImage.setHyperlinkAnchor(getHyperlinkAnchor());
		printImage.setHyperlinkPage(getHyperlinkPage());
		printImage.setBookmarkLevel(getBookmarkLevel());
	}

	public byte getChartType()
	{
		return chartType;
	}


	public JRChild getCopy(JRAbstractObjectFactory factory)
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


	/**
	 *
	 */
	private void configureChart(JFreeChart chart, byte evaluation) throws JRException
	{
		if (getMode() == JRElement.MODE_OPAQUE)
		{
			chart.setBackgroundPaint(getBackcolor());
		}
		else
		{
			chart.setBackgroundPaint(TRANSPARENT_PAINT);
		}

		if (chart.getTitle() != null)
		{
			TextTitle title = chart.getTitle();
			title.setPaint(getTitleColor());

			JRFont font = getTitleFont();
			if (font != null) {
				Map attributes = JRFontUtil.getNonPdfAttributes(font);
				title.setFont(new Font(attributes));
			}

		}

		String subtitleText = (String)evaluateExpression(getSubtitleExpression(), evaluation);
		if (subtitleText != null)
		{
			TextTitle subtitle = new TextTitle(subtitleText);
			subtitle.setPaint(getSubtitleColor());

			JRFont font = getSubtitleFont();
			if (font != null) {
				Map attributes = JRFontUtil.getNonPdfAttributes(font);
				subtitle.setFont(new Font(attributes));
			}

			chart.addSubtitle(subtitle);
		}
		
		configurePlot(chart.getPlot());
	}

	
	/**
	 *
	 */
	private void configurePlot(Plot p)
	{
		p.setOutlinePaint(TRANSPARENT_PAINT);

		if (getPlot().getBackcolor() == null)
		{
			p.setBackgroundPaint(TRANSPARENT_PAINT);
		}
		else
		{
			p.setBackgroundPaint(getPlot().getBackcolor());
		}
		
		p.setBackgroundAlpha(getPlot().getBackgroundAlpha());
		p.setForegroundAlpha(getPlot().getForegroundAlpha());
	}

	/**
	 *
	 */
	protected JFreeChart evaluateAreaImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createAreaChart( (String)evaluateExpression(getTitleExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation ),
				(String)evaluateExpression(((JRAreaPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, evaluation);

		return chart;
	}


	protected JFreeChart evaluateBar3DImage( byte evaluation ) throws JRException {
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

		configureChart(chart, evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();

		BarRenderer3D barRenderer3D =
			new BarRenderer3D(
				((JRFillBar3DPlot)getPlot()).getXOffset(),
				((JRFillBar3DPlot)getPlot()).getYOffset()
				);
		categoryPlot.setRenderer(barRenderer3D);

		barRenderer3D.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());//FIXME CHARTS what to do when missing labelExpression
		barRenderer3D.setItemLabelsVisible( ((JRFillBar3DPlot)getPlot()).isShowLabels() );

		return chart;
	}


	/**
	 *
	 */
	protected JFreeChart evaluateBarImage(byte evaluation) throws JRException
	{
		CategoryDataset categoryDataset = (CategoryDataset)((JRFillChartDataset) dataset).getDataset();
		JFreeChart chart =
			ChartFactory.createBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				categoryDataset,
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");

		categoryPlot.getDomainAxis().setTickMarksVisible(
			((JRFillBarPlot)getPlot()).isShowTickMarks()
			);
		categoryPlot.getDomainAxis().setTickLabelsVisible(
				((JRFillBarPlot)getPlot()).isShowTickLabels()
				);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(
				((JRFillBarPlot)getPlot()).isShowTickMarks()
				);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(
				((JRFillBarPlot)getPlot()).isShowTickLabels()
				);
		
		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		categoryRenderer.setItemLabelsVisible( ((JRFillBarPlot)getPlot()).isShowLabels() );
		
		return chart;

	}


	protected JFreeChart evaluateBubbleImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createBubbleChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getXAxisLabelExpression(), evaluation ),
				(String)evaluateExpression(((JRBubblePlot)getPlot()).getYAxisLabelExpression(), evaluation ),
				 (XYZDataset)((JRFillChartDataset)dataset).getDataset(),
				 getPlot().getOrientation(),
				 isShowLegend(),
				 true,
				 false);

		configureChart(chart, evaluation);

		XYPlot xyPlot = (XYPlot)chart.getPlot();

		XYBubbleRenderer bubbleRenderer = new XYBubbleRenderer( ((JRFillBubblePlot)getPlot()).getScaleType() );
		xyPlot.setRenderer( bubbleRenderer );

		return chart;
	}


	/**
	 *
	 * @param evaluation
	 * @throws net.sf.jasperreports.engine.JRException
	 */
	protected JFreeChart evaluateCandlestickImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createCandlestickChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRCandlestickPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend()
				);

		configureChart(chart, evaluation);

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		CandlestickRenderer candlestickRenderer = (CandlestickRenderer) xyPlot.getRenderer();
		candlestickRenderer.setDrawVolume(((JRCandlestickPlot)getPlot()).isShowVolume());

		return chart;
	}


	/**
	 *
	 * @param evaluation
	 * @throws JRException
	 */
	protected JFreeChart evaluateHighLowImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createHighLowChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getTimeAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRHighLowPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				(DefaultHighLowDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend()
				);

		configureChart(chart, evaluation);

		XYPlot xyPlot = (XYPlot) chart.getPlot();
		HighLowRenderer hlRenderer = (HighLowRenderer) xyPlot.getRenderer();
		hlRenderer.setDrawOpenTicks(((JRHighLowPlot)getPlot()).isShowOpenTicks());
		hlRenderer.setDrawCloseTicks(((JRHighLowPlot)getPlot()).isShowCloseTicks());

		return chart;
	}


	protected JFreeChart evaluateLineImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createLineChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRLinePlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRLinePlot)getPlot()).getValueAxisLabelExpression(), evaluation ),
				(CategoryDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();

		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)categoryPlot.getRenderer();
		lineRenderer.setShapesVisible( ((JRFillLinePlot)getPlot()).isShowShapes() );
		lineRenderer.setLinesVisible( ((JRFillLinePlot)getPlot()).isShowLines() );

		return chart;
	}


	/**
	 *
	 */
	protected JFreeChart evaluatePie3DImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createPieChart3D(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(PieDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, evaluation);

		PiePlot3D piePlot3D = (PiePlot3D) chart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");
		piePlot3D.setDepthFactor(((JRFillPie3DPlot)getPlot()).getDepthFactor());
		
		PieSectionLabelGenerator labelGenerator = ((JRFillPieDataset)getDataset()).getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot3D.setLabelGenerator(labelGenerator);
		}

		return chart;
	}


	/**
	 *
	 */
	protected JFreeChart evaluatePieImage(byte evaluation) throws JRException
	{
		JFreeChart chart =
			ChartFactory.createPieChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(PieDataset)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, evaluation);

		PiePlot piePlot = (PiePlot)chart.getPlot();
		//plot.setStartAngle(290);
		//plot.setDirection(Rotation.CLOCKWISE);
		//plot.setNoDataMessage("No data to display");

		PieSectionLabelGenerator labelGenerator = ((JRFillPieDataset)getDataset()).getLabelGenerator();
		if (labelGenerator != null)
		{
			piePlot.setLabelGenerator(labelGenerator);
		}

		return chart;
	}



	protected JFreeChart evaluateScatterImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createScatterPlot(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRScatterPlot)getPlot()).getXAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRScatterPlot)getPlot()).getYAxisLabelExpression(), evaluation ),
				(XYDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, evaluation);
		XYLineAndShapeRenderer plotRenderer = (XYLineAndShapeRenderer) ((XYPlot)chart.getPlot()).getRenderer();

		JRScatterPlot scatterPlot = (JRScatterPlot) getPlot();
		plotRenderer.setLinesVisible(scatterPlot.isShowLines());
		plotRenderer.setShapesVisible(scatterPlot.isShowShapes());

		return chart;
	}


	/**
	 *
	 */
	protected JFreeChart evaluateStackedBar3DImage(byte evaluation) throws JRException
	{
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

		configureChart(chart, evaluation);
		
		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();

		StackedBarRenderer3D stackedBarRenderer3D =
			new StackedBarRenderer3D(
				((JRFillBar3DPlot)getPlot()).getXOffset(),
				((JRFillBar3DPlot)getPlot()).getYOffset()
				);
		categoryPlot.setRenderer(stackedBarRenderer3D);

		stackedBarRenderer3D.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());//FIXME CHARTS what to do when missing labelExpression
		stackedBarRenderer3D.setItemLabelsVisible( ((JRFillBar3DPlot)getPlot()).isShowLabels() );

		return chart;
	}


	/**
	 *
	 */
	protected JFreeChart evaluateStackedBarImage(byte evaluation) throws JRException
	{
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

		configureChart(chart, evaluation);

		CategoryPlot categoryPlot = (CategoryPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");

		categoryPlot.getDomainAxis().setTickMarksVisible(
			((JRFillBarPlot)getPlot()).isShowTickMarks()
			);
		categoryPlot.getDomainAxis().setTickLabelsVisible(
				((JRFillBarPlot)getPlot()).isShowTickLabels()
				);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickMarksVisible(
				((JRFillBarPlot)getPlot()).isShowTickMarks()
				);
		((NumberAxis)categoryPlot.getRangeAxis()).setTickLabelsVisible(
				((JRFillBarPlot)getPlot()).isShowTickLabels()
				);
		
		CategoryItemRenderer categoryRenderer = categoryPlot.getRenderer();
		categoryRenderer.setBaseItemLabelGenerator(((JRFillCategoryDataset)getDataset()).getLabelGenerator());
		categoryRenderer.setItemLabelsVisible( ((JRFillBarPlot)getPlot()).isShowLabels() );
		
		return chart;
	}


	protected JFreeChart evaluateXyAreaImage( byte evaluation ) throws JRException {
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

		configureChart(chart, evaluation);

		return chart;
	}


	/**
	 *
	 */
	protected JFreeChart evaluateXYBarImage(byte evaluation) throws JRException
	{
		IntervalXYDataset tmpDataset = (IntervalXYDataset)((JRFillChartDataset)dataset).getDataset();
		
		boolean isDate = true;
		if( dataset.getDatasetType() == JRChartDataset.XY_DATASET ){
			isDate = false;
		}
		
		JFreeChart chart =
			ChartFactory.createXYBarChart(
				(String)evaluateExpression(getTitleExpression(), evaluation),
				(String)evaluateExpression(((JRBarPlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				isDate,
				(String)evaluateExpression(((JRBarPlot)getPlot()).getValueAxisLabelExpression(), evaluation),
				tmpDataset,
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false
				);

		configureChart(chart, evaluation);

		XYPlot xyPlot = (XYPlot)chart.getPlot();
		//plot.setNoDataMessage("No data to display");
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
		
		
		XYItemRenderer itemRenderer = xyPlot.getRenderer();
		if( getDataset().getDatasetType() == JRChartDataset.TIMESERIES_DATASET ) {
			itemRenderer.setBaseItemLabelGenerator( ((JRFillTimeSeriesDataset)getDataset()).getLabelGenerator() );
		}
		else if( getDataset().getDatasetType() == JRChartDataset.TIMEPERIOD_DATASET  ){
			itemRenderer.setBaseItemLabelGenerator( ((JRFillTimePeriodDataset)getDataset()).getLabelGenerator() );
		}
		else if( getDataset().getDatasetType() == JRChartDataset.XY_DATASET ) {
			itemRenderer.setBaseItemLabelGenerator( ((JRFillXyDataset)getDataset()).getLabelGenerator() );
		}

		itemRenderer.setBaseItemLabelsVisible( ((JRFillBarPlot)getPlot()).isShowLabels() );

		return chart;
	}


	protected JFreeChart evaluateXyLineImage( byte evaluation ) throws JRException {
		JFreeChart chart = ChartFactory.createXYLineChart(
				(String)evaluateExpression( getTitleExpression(), evaluation),
				(String)evaluateExpression( ((JRLinePlot)getPlot()).getCategoryAxisLabelExpression(), evaluation),
				(String)evaluateExpression(((JRLinePlot)getPlot()).getValueAxisLabelExpression(), evaluation ),
				(XYDataset)((JRFillChartDataset)dataset).getDataset(),
				getPlot().getOrientation(),
				isShowLegend(),
				true,
				false);

		configureChart(chart, evaluation);

		return chart;
	}

	protected JFreeChart evaluateTimeSeriesImage( byte evaluation ) throws JRException {
		
		
		String timeAxisLabel = (String)evaluateExpression( ((JRTimeSeriesPlot)getPlot()).getTimeAxisLabelExpression(), evaluation );
		String valueAxisLabel = (String)evaluateExpression( ((JRTimeSeriesPlot)getPlot()).getValueAxisLabelExpression(), evaluation );
				
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				(String)evaluateExpression( getTitleExpression(), evaluation ),
				timeAxisLabel,
				valueAxisLabel,
				(TimeSeriesCollection)((JRFillChartDataset)dataset).getDataset(),
				isShowLegend(),
				true,
				false );
		
		configureChart(chart, evaluation);
		
		XYPlot xyPlot = (XYPlot)chart.getPlot();
		
		XYLineAndShapeRenderer lineRenderer = (XYLineAndShapeRenderer)xyPlot.getRenderer();
		lineRenderer.setDefaultLinesVisible(((JRTimeSeriesPlot)getPlot()).isShowLines() );
		lineRenderer.setDefaultShapesVisible(((JRTimeSeriesPlot)getPlot()).isShowShapes() );
		
		return chart;
		
	}


	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateImage(evaluation);

		copy((JRPrintImage) element);
	}


	public int getBookmarkLevel()
	{
		return ((JRChart)parent).getBookmarkLevel();
	}

	/**
	 *
	 */
	public String getCustomizerClass()
	{
		return customizerClass;
	}

	/**
	 *
	 */
	public byte getBorder()
	{
		return ((JRBox)parent).getBorder();
	}

	public Byte getOwnBorder()
	{
		return ((JRBox)parent).getOwnBorder();
	}

	/**
	 *
	 */
	public void setBorder(byte border)
	{
	}

	/**
	 *
	 */
	public void setBorder(Byte border)
	{
	}

	/**
	 *
	 */
	public Color getBorderColor()
	{
		return ((JRBox)parent).getBorderColor();
	}

	public Color getOwnBorderColor()
	{
		return ((JRBox)parent).getOwnBorderColor();
	}

	/**
	 *
	 */
	public void setBorderColor(Color borderColor)
	{
	}

	/**
	 *
	 */
	public int getPadding()
	{
		return ((JRBox)parent).getPadding();
	}

	public Integer getOwnPadding()
	{
		return ((JRBox)parent).getOwnPadding();
	}

	/**
	 *
	 */
	public void setPadding(int padding)
	{
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
	}

	/**
	 *
	 */
	public byte getTopBorder()
	{
		return ((JRBox)parent).getTopBorder();
	}

	/**
	 *
	 */
	public Byte getOwnTopBorder()
	{
		return ((JRBox)parent).getOwnTopBorder();
	}

	/**
	 *
	 */
	public void setTopBorder(byte topBorder)
	{
	}

	/**
	 *
	 */
	public void setTopBorder(Byte topBorder)
	{
	}

	/**
	 *
	 */
	public Color getTopBorderColor()
	{
		return ((JRBox)parent).getTopBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnTopBorderColor()
	{
		return ((JRBox)parent).getOwnTopBorderColor();
	}

	/**
	 *
	 */
	public void setTopBorderColor(Color topBorderColor)
	{
	}

	/**
	 *
	 */
	public int getTopPadding()
	{
		return ((JRBox)parent).getTopPadding();
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return ((JRBox)parent).getOwnTopPadding();
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
	}

	/**
	 *
	 */
	public byte getLeftBorder()
	{
		return ((JRBox)parent).getLeftBorder();
	}

	/**
	 *
	 */
	public Byte getOwnLeftBorder()
	{
		return ((JRBox)parent).getOwnLeftBorder();
	}

	/**
	 *
	 */
	public void setLeftBorder(byte leftBorder)
	{
	}

	/**
	 *
	 */
	public void setLeftBorder(Byte leftBorder)
	{
	}

	/**
	 *
	 */
	public Color getLeftBorderColor()
	{
		return ((JRBox)parent).getLeftBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnLeftBorderColor()
	{
		return ((JRBox)parent).getOwnLeftBorderColor();
	}

	/**
	 *
	 */
	public void setLeftBorderColor(Color leftBorderColor)
	{
	}

	/**
	 *
	 */
	public int getLeftPadding()
	{
		return ((JRBox)parent).getLeftPadding();
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return ((JRBox)parent).getOwnLeftPadding();
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
	}

	/**
	 *
	 */
	public byte getBottomBorder()
	{
		return ((JRBox)parent).getBottomBorder();
	}

	/**
	 *
	 */
	public Byte getOwnBottomBorder()
	{
		return ((JRBox)parent).getOwnBottomBorder();
	}

	/**
	 *
	 */
	public void setBottomBorder(byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public void setBottomBorder(Byte bottomBorder)
	{
	}

	/**
	 *
	 */
	public Color getBottomBorderColor()
	{
		return ((JRBox)parent).getBottomBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnBottomBorderColor()
	{
		return ((JRBox)parent).getOwnBottomBorderColor();
	}

	/**
	 *
	 */
	public void setBottomBorderColor(Color bottomBorderColor)
	{
	}

	/**
	 *
	 */
	public int getBottomPadding()
	{
		return ((JRBox)parent).getBottomPadding();
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return ((JRBox)parent).getOwnBottomPadding();
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
	}

	/**
	 *
	 */
	public byte getRightBorder()
	{
		return ((JRBox)parent).getRightBorder();
	}

	/**
	 *
	 */
	public Byte getOwnRightBorder()
	{
		return ((JRBox)parent).getOwnRightBorder();
	}

	/**
	 *
	 */
	public void setRightBorder(byte rightBorder)
	{
	}

	/**
	 *
	 */
	public void setRightBorder(Byte rightBorder)
	{
	}

	/**
	 *
	 */
	public Color getRightBorderColor()
	{
		return ((JRBox)parent).getRightBorderColor();
	}

	/**
	 *
	 */
	public Color getOwnRightBorderColor()
	{
		return ((JRBox)parent).getOwnRightBorderColor();
	}

	/**
	 *
	 */
	public void setRightBorderColor(Color rightBorderColor)
	{
	}

	/**
	 *
	 */
	public int getRightPadding()
	{
		return ((JRBox)parent).getRightPadding();
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return ((JRBox)parent).getOwnRightPadding();
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
	}



	private void evaluateDatasetRun(byte evaluation) throws JRException
	{
		((JRFillChartDataset) dataset).evaluateDatasetRun(evaluation);
	}
}
