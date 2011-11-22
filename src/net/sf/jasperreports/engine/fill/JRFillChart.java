/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TimeZone;

import net.sf.jasperreports.charts.ChartContext;
import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillChartAxis;
import net.sf.jasperreports.charts.fill.JRFillGanttDataset;
import net.sf.jasperreports.charts.fill.JRFillHighLowDataset;
import net.sf.jasperreports.charts.fill.JRFillMultiAxisPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillTimePeriodDataset;
import net.sf.jasperreports.charts.fill.JRFillTimeSeriesDataset;
import net.sf.jasperreports.charts.fill.JRFillXyDataset;
import net.sf.jasperreports.charts.fill.JRFillXyzDataset;
import net.sf.jasperreports.charts.type.AxisPositionEnum;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.charts.util.CategoryChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.ChartUtil;
import net.sf.jasperreports.charts.util.HighLowChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.charts.util.MultiAxisChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.PieChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.TimePeriodChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.TimeSeriesChartHyperlinkProvider;
import net.sf.jasperreports.charts.util.XYChartHyperlinkProvider;
import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @author Some enhancements by Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillChart extends JRFillElement implements JRChart
{


	/**
	 *
	 */
	protected byte chartType;

	/**
	 *
	 */
	protected JRFont titleFont;
	protected JRFont subtitleFont;
	protected JRFont legendFont;
	protected final JRLineBox lineBox;

	/**
	 *
	 */
	protected JRGroup evaluationGroup;

	protected JRFillChartDataset dataset;
	protected JRChartPlot plot;

	protected JRRenderable renderer;
	private String anchorName;
	private String hyperlinkReference;
	private String hyperlinkAnchor;
	private Integer hyperlinkPage;
	private String hyperlinkTooltip;
	private JRPrintHyperlinkParameters hyperlinkParameters;

	protected String customizerClass;
	protected JRChartCustomizer chartCustomizer;
	
	protected String renderType;
	protected String themeName;

	protected JFreeChart jfreeChart;
	protected ChartHyperlinkProvider chartHyperlinkProvider;

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
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_BAR:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_BAR3D:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_BUBBLE:
				dataset = (JRFillChartDataset) factory.getXyzDataset((JRXyzDataset) chart.getDataset());
				plot = factory.getBubblePlot((JRBubblePlot) chart.getPlot());
				break;
			case CHART_TYPE_CANDLESTICK:
				dataset = (JRFillChartDataset) factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getCandlestickPlot((JRCandlestickPlot) chart.getPlot());
				break;
			case CHART_TYPE_HIGHLOW:
				dataset = (JRFillChartDataset) factory.getHighLowDataset((JRHighLowDataset) chart.getDataset());
				plot = factory.getHighLowPlot((JRHighLowPlot) chart.getPlot());
				break;
			case CHART_TYPE_LINE:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			case CHART_TYPE_METER:
				dataset = (JRFillChartDataset) factory.getValueDataset((JRValueDataset) chart.getDataset());
				plot = factory.getMeterPlot((JRMeterPlot) chart.getPlot());
				break;
			case CHART_TYPE_MULTI_AXIS:
				plot = factory.getMultiAxisPlot((JRMultiAxisPlot) chart.getPlot());
				dataset = ((JRFillMultiAxisPlot)plot).getMainDataset();
				break;
			case CHART_TYPE_PIE:
				dataset = (JRFillChartDataset) factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPiePlot((JRPiePlot) chart.getPlot());
				break;
			case CHART_TYPE_PIE3D:
				dataset = (JRFillChartDataset) factory.getPieDataset((JRPieDataset) chart.getDataset());
				plot = factory.getPie3DPlot((JRPie3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_SCATTER:
				dataset = (JRFillChartDataset) factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getScatterPlot((JRScatterPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDBAR3D:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getBar3DPlot((JRBar3DPlot) chart.getPlot());
				break;
			case CHART_TYPE_THERMOMETER:
				dataset = (JRFillChartDataset) factory.getValueDataset((JRValueDataset) chart.getDataset());
				plot = factory.getThermometerPlot((JRThermometerPlot) chart.getPlot());
				break;
			case CHART_TYPE_TIMESERIES:
				dataset = (JRFillChartDataset) factory.getTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
				plot = factory.getTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
				break;
			case CHART_TYPE_XYAREA:
				dataset = (JRFillChartDataset) factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYBAR:
				switch (chart.getDataset().getDatasetType()){
					case JRChartDataset.TIMESERIES_DATASET:
						dataset = (JRFillChartDataset) factory.getTimeSeriesDataset( (JRTimeSeriesDataset)chart.getDataset() );
						break;
					case JRChartDataset.TIMEPERIOD_DATASET:
						dataset = (JRFillChartDataset) factory.getTimePeriodDataset((JRTimePeriodDataset) chart.getDataset() );
						break;
					case JRChartDataset.XY_DATASET:
						dataset = (JRFillChartDataset) factory.getXyDataset( (JRXyDataset)chart.getDataset() );
						break;
				}

				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			case CHART_TYPE_XYLINE:
				dataset = (JRFillChartDataset) factory.getXyDataset((JRXyDataset) chart.getDataset());
				plot = factory.getLinePlot((JRLinePlot) chart.getPlot());
				break;
			case CHART_TYPE_STACKEDAREA:
				dataset = (JRFillChartDataset) factory.getCategoryDataset((JRCategoryDataset) chart.getDataset());
				plot = factory.getAreaPlot((JRAreaPlot) chart.getPlot());
				break;
			case CHART_TYPE_GANTT:
				dataset = (JRFillChartDataset) factory.getGanttDataset((JRGanttDataset) chart.getDataset());
				plot = factory.getBarPlot((JRBarPlot) chart.getPlot());
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
		}

		titleFont = factory.getFont(chart, chart.getTitleFont());
		subtitleFont = factory.getFont(chart, chart.getSubtitleFont());
		legendFont =  factory.getFont(this, chart.getLegendFont());
		
		lineBox = chart.getLineBox().clone(this);

		evaluationGroup = factory.getGroup(chart.getEvaluationGroup());

		customizerClass = chart.getCustomizerClass();
		if (customizerClass != null && customizerClass.length() > 0) {
			try {
				Class<?> myClass = JRClassLoader.loadClassForName(customizerClass);
				chartCustomizer = (JRChartCustomizer) myClass.newInstance();
			} catch (Exception e) {
				throw new JRRuntimeException("Could not create chart customizer instance.", e);
			}

			if (chartCustomizer instanceof JRAbstractChartCustomizer)
			{
				((JRAbstractChartCustomizer) chartCustomizer).init(filler, this);
			}
		}
		
		renderType = chart.getRenderType();
		if(renderType == null)
		{
			renderType = JRProperties.getProperty(getParentProperties(), JRChart.PROPERTY_CHART_RENDER_TYPE);
		}
		
		themeName = chart.getTheme();
		if(themeName == null)
		{
			themeName = JRProperties.getProperty(getParentProperties(), JRChart.PROPERTY_CHART_THEME);
		}
	}


	/**
	 *
	 */
	public ModeEnum getModeValue()
	{
		return JRStyleResolver.getMode(this, ModeEnum.TRANSPARENT);
	}

	/**
	 * 
	 */
	public Boolean getShowLegend()
	{
		return ((JRChart)parent).getShowLegend();
	}

	/**
	 *
	 */
	public void setShowLegend(Boolean isShowLegend)
	{
	}

	/**
	 *
	 */
	public String getRenderType()
	{
		return renderType;
	}

	/**
	 *
	 */
	public void setRenderType(String renderType)
	{
	}

	/**
	 *
	 */
	public String getTheme()
	{
		return themeName;
	}

	/**
	 *
	 */
	public void setTheme(String theme)
	{
	}

	/**
	 *
	 */
	public EvaluationTimeEnum getEvaluationTimeValue()
	{
		return ((JRChart)parent).getEvaluationTimeValue();
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
	public JRLineBox getLineBox()
	{
		return lineBox;
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
	public EdgeEnum getTitlePositionValue()
	{
		return ((JRChart)parent).getTitlePositionValue();
	}

	/**
	 *
	 */
	public void setTitlePosition(EdgeEnum titlePosition)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Color getTitleColor()
	{
		return JRStyleResolver.getTitleColor(this);
	}

	/**
	 *
	 */
	public Color getOwnTitleColor()
	{
		return ((JRChart)parent).getOwnTitleColor();
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
	public Color getOwnSubtitleColor()
	{
		return ((JRChart)parent).getOwnSubtitleColor();
	}

	/**
	 *
	 */
	public Color getSubtitleColor()
	{
		return JRStyleResolver.getSubtitleColor(this);
	}

	/**
	 *
	 */
	public void setSubtitleColor(Color subtitleColor)
	{
	}

	/**
	 * Returns the color to use for text in the legend.
	 *
	 * @return the color to use for text in the legend
	 */
	public Color getOwnLegendColor()
	{
		return ((JRChart)parent).getOwnLegendColor();
	}

	/**
	 * Returns the inherited color to use for text in the legend.
	 *
	 * @return the color to use for text in the legend
	 */
	public Color getLegendColor()
	{
		return JRStyleResolver.getLegendColor(this);
	}

	/**
	 * Sets the color to use for text in the legend.
	 *
	 * @param legendColor the color to use for text in the legend
	 */
	public void setLegendColor(Color legendColor)
	{
	}

	/**
	 * Returns the color to use as the background of the legend.
	 *
	 * @return the color to use as the background of the legend
	 */
	public Color getOwnLegendBackgroundColor()
	{
		return ((JRChart)parent).getOwnLegendBackgroundColor();
	}

	/**
	 * Returns the color to use as the background of the legend.
	 *
	 * @return the color to use as the background of the legend
	 */
	public Color getLegendBackgroundColor()
	{
		return JRStyleResolver.getLegendBackgroundColor(this);
	}

	/**
	 * Sets the color to use for the background of the legend.
	 *
	 * @param legendBackgroundColor the color to use for the background of the legend
	 */
	public void setLegendBackgroundColor(Color legendBackgroundColor)
	{
	}

	/**
	 * Returns the font to use in the legend.
	 *
	 * @return the font to use in the legend
	 */
	public JRFont getLegendFont()
	{
		return legendFont;
	}

	/**
	 *
	 */
	public EdgeEnum getLegendPositionValue()
	{
		return ((JRChart)parent).getLegendPositionValue();
	}

	/**
	 *
	 */
	public void setLegendPosition(EdgeEnum legendPosition)
	{
		throw new UnsupportedOperationException();
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
	 * @deprecated Replaced by {@link #getHyperlinkTypeValue()}.
	 */
	public byte getHyperlinkType()
	{
		return getHyperlinkTypeValue().getValue();
	}

	/**
	 *
	 */
	public HyperlinkTypeEnum getHyperlinkTypeValue()
	{
		return ((JRChart)parent).getHyperlinkTypeValue();
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
	public Locale getLocale()
	{
		return filler.getLocale();
	}

	public TimeZone getTimeZone()
	{
		return filler.getTimeZone();
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
	public void setDataset(JRFillChartDataset dataset)
	{
		this.dataset = dataset;
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

	protected String getHyperlinkTooltip()
	{
		return hyperlinkTooltip;
	}

	/**
	 * 
	 */
	public Color getDefaultLineColor() 
	{
		return getForecolor();
	}


	/**
	 *
	 */
	protected JRTemplateImage getJRTemplateImage()
	{
		return (JRTemplateImage) getElementTemplate();
	}

	protected JRTemplateElement createElementTemplate()
	{
		return new JRTemplateImage(getElementOrigin(), 
				filler.getJasperPrint().getDefaultStyleProvider(), this);
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

		if (isPrintWhenExpressionNull() || isPrintWhenTrue())
		{
			if (getEvaluationTimeValue() == EvaluationTimeEnum.NOW)
			{
				evaluateRenderer(evaluation);
			}
		}
	}


	/**
	 *
	 */
	protected void evaluateRenderer(byte evaluation) throws JRException
	{
		JFreeChart chart = evaluateChart(evaluation);
		
		Rectangle2D rectangle = new Rectangle2D.Double(0,0,getWidth(),getHeight());

		renderer = 
			ChartUtil.getChartRendererFactory(getRenderType()).getRenderer(
				chart, 
				chartHyperlinkProvider,
				rectangle
				);
	}
	
	protected ChartHyperlinkProvider getHyperlinkProvider()
	{
		return chartHyperlinkProvider;
	}
	
	/**
	 *
	 */
	protected JFreeChart evaluateChart(byte evaluation) throws JRException
	{
		evaluateProperties(evaluation);
		evaluateDatasetRun(evaluation);

		ChartTheme theme = ChartUtil.getChartTheme(themeName);
		
		if (getChartType() == JRChart.CHART_TYPE_MULTI_AXIS)
		{
			createMultiAxisChart(evaluation);
		}
		else
		{
			jfreeChart = theme.createChart(new FillChartContext(evaluation));

			chartHyperlinkProvider = createChartHyperlinkProvider();
		}

		if (chartCustomizer != null)
		{
			chartCustomizer.customize(jfreeChart, this);
		}

		anchorName = (String) evaluateExpression(getAnchorNameExpression(), evaluation);
		hyperlinkReference = (String) evaluateExpression(getHyperlinkReferenceExpression(), evaluation);
		hyperlinkAnchor = (String) evaluateExpression(getHyperlinkAnchorExpression(), evaluation);
		hyperlinkPage = (Integer) evaluateExpression(getHyperlinkPageExpression(), evaluation);
		hyperlinkTooltip = (String) evaluateExpression(getHyperlinkTooltipExpression(), evaluation);
		hyperlinkParameters = JRFillHyperlinkHelper.evaluateHyperlinkParameters(this, expressionEvaluator, evaluation);

		return jfreeChart;
	}


	/**
	 *
	 */
	protected boolean prepare(
		int availableHeight,
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

		if (getEvaluationTimeValue() == EvaluationTimeEnum.NOW)
		{
			if (isOverflow && isAlreadyPrinted() && !isPrintWhenDetailOverflows())
			{
				isToPrint = false;
			}

			if (
				isToPrint &&
				availableHeight < getRelativeY() + getHeight()
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
				availableHeight < getRelativeY() + getHeight()
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
		JRTemplatePrintImage printImage = new JRTemplatePrintImage(getJRTemplateImage(), elementId);

		printImage.setX(getX());
		printImage.setY(getRelativeY());
		printImage.setWidth(getWidth());
		printImage.setHeight(getStretchHeight());

		EvaluationTimeEnum evaluationTime = getEvaluationTimeValue();
		if (evaluationTime == EvaluationTimeEnum.NOW)
		{
			copy(printImage);
		}
		else
		{
			filler.addBoundElement(this, printImage, evaluationTime, getEvaluationGroup(), band);
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
		printImage.setHyperlinkTooltip(getHyperlinkTooltip());
		printImage.setBookmarkLevel(getBookmarkLevel());
		printImage.setHyperlinkParameters(hyperlinkParameters);
		transferProperties(printImage);
	}

	public byte getChartType()
	{
		return chartType;
	}


	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitChart(this);
	}

	
	/**
	 *
	 */
	protected ChartHyperlinkProvider createChartHyperlinkProvider()
	{
		ChartHyperlinkProvider chartHyperlinkProvider = null;
		
		switch(getChartType()) 
		{
			case JRChart.CHART_TYPE_AREA:
			case JRChart.CHART_TYPE_BAR:
			case JRChart.CHART_TYPE_BAR3D:
			case JRChart.CHART_TYPE_LINE:
			case JRChart.CHART_TYPE_STACKEDBAR3D:
			case JRChart.CHART_TYPE_STACKEDBAR:
			case JRChart.CHART_TYPE_STACKEDAREA:
				chartHyperlinkProvider = new CategoryChartHyperlinkProvider(((JRFillCategoryDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				chartHyperlinkProvider = new XYChartHyperlinkProvider(((JRFillXyzDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_SCATTER:
			case JRChart.CHART_TYPE_XYAREA:
			case JRChart.CHART_TYPE_XYLINE:
				chartHyperlinkProvider = new XYChartHyperlinkProvider(((JRFillXyDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
			case JRChart.CHART_TYPE_HIGHLOW:
				chartHyperlinkProvider = new HighLowChartHyperlinkProvider(((JRFillHighLowDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				//multi-axis charts are dealt with in createMultiAxisChart
				break;
			case JRChart.CHART_TYPE_PIE:
			case JRChart.CHART_TYPE_PIE3D:
				chartHyperlinkProvider = new PieChartHyperlinkProvider(((JRFillPieDataset)getDataset()).getSectionHyperlinks());
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				chartHyperlinkProvider = new TimeSeriesChartHyperlinkProvider(((JRFillTimeSeriesDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_XYBAR:
				switch(getDataset().getDatasetType())
				{
					case JRChartDataset.TIMESERIES_DATASET :
						chartHyperlinkProvider = new TimeSeriesChartHyperlinkProvider(((JRFillTimeSeriesDataset)getDataset()).getItemHyperlinks());
						break;
					case JRChartDataset.TIMEPERIOD_DATASET :
						chartHyperlinkProvider = new TimePeriodChartHyperlinkProvider(((JRFillTimePeriodDataset)getDataset()).getItemHyperlinks());
						break;
					case JRChartDataset.XY_DATASET :
						chartHyperlinkProvider = new XYChartHyperlinkProvider(((JRFillXyDataset)getDataset()).getItemHyperlinks());
						break;
					default:
						break;
				}
				break;
			case JRChart.CHART_TYPE_GANTT:
				chartHyperlinkProvider = new XYChartHyperlinkProvider(((JRFillGanttDataset)getDataset()).getItemHyperlinks());
				break;
			case JRChart.CHART_TYPE_METER:
			case JRChart.CHART_TYPE_THERMOMETER:
				//no item hyperlinks
				break;
			default:
				throw new JRRuntimeException("Chart type " + getChartType() + " not supported.");
		}

		return chartHyperlinkProvider;
	}



	/**
	 * Build and configure a multiple axis chart.  A multiple axis chart support more than
	 * one range axis.  Multiple datasets using different ranges can be displayed as long as
	 * they share a common domain axis.  Each dataset can be rendered differently, so one chart
	 * can contain (for example) two line charts, a bar chart and an area chart.
	 * <br><br>
	 * Multiple axis charts are handled differently than the other chart types.  They do not
	 * have a dataset, as each chart that is added to the multiple axis chart has its own
	 * dataset.  For simplicity, each dataset is treated as its own chart, and in fact we
	 * reuse the design of all the chart types and let JFreeChart actually run them.  Then
	 * we pull out the bits we need and add it to the common chart.  All the plot and chart
	 * options on the nested charts is ignored, and all formatting is controlled by the plot
	 * attached to the multiAxisChart.  The one exception is seriesColor, which can be used in
	 * a nested report to specify a color for a specific series in that report.
	 *
	 * @param evaluation current expression evaluation phase
	 * @throws JRException
	 */
	protected void createMultiAxisChart(byte evaluation) throws JRException
	{
		// A multi axis chart has to have at least one axis and chart specified.
		// Create the first axis as the base plot, and then go ahead and create the
		// charts for any additional axes.  Just take the renderer and data series
		// from those charts and add them to the first one.
		Plot mainPlot = null;

		JRFillMultiAxisPlot jrPlot = (JRFillMultiAxisPlot)getPlot();
		
		// create a multi axis hyperlink provider
		MultiAxisChartHyperlinkProvider multiHyperlinkProvider = new MultiAxisChartHyperlinkProvider();
		
		// Generate the main plot from the first axes specified.
		Iterator<JRChartAxis> iter = jrPlot.getAxes().iterator();
		if (iter.hasNext())
		{
			JRFillChartAxis axis = (JRFillChartAxis)iter.next();
			JRFillChart fillChart = axis.getFillChart();
			
			//a JFreeChart object should be obtained first; the rendering type should be always "vector"
			

			jfreeChart = fillChart.evaluateChart(evaluation);
			//FIXME honor printWhenExpression
			// Override the plot from the first axis with the plot for the multi-axis
			// chart.
			//FIXME is the above comment true? 
			//configureChart(jfreeChart, getPlot(), evaluation);
			mainPlot = jfreeChart.getPlot();
			ChartHyperlinkProvider axisHyperlinkProvider = fillChart.getHyperlinkProvider();

			if (mainPlot instanceof CategoryPlot)
			{
				CategoryPlot categoryPlot = (CategoryPlot) mainPlot;
				categoryPlot.setRangeAxisLocation(0, getChartAxisLocation(axis));
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(categoryPlot.getDataset(), 
							axisHyperlinkProvider);
				}
			}
			else if (mainPlot instanceof XYPlot)
			{
				XYPlot xyPlot = (XYPlot) mainPlot;
				xyPlot.setRangeAxisLocation(0, getChartAxisLocation(axis));
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(xyPlot.getDataset(), axisHyperlinkProvider);
				}
			}
		 }

		// Now handle all the extra axes, if any.
		int axisNumber = 0;
		while (iter.hasNext())
		{			
			JRFillChartAxis chartAxis = (JRFillChartAxis)iter.next();
			JRFillChart fillChart = chartAxis.getFillChart();

			fillChart.evaluatePrintWhenExpression(evaluation);
			if (!(fillChart.isPrintWhenExpressionNull() || fillChart.isPrintWhenTrue()))
			{
				continue;
			}

			axisNumber++;
			JFreeChart axisChart = fillChart.evaluateChart(evaluation);
			ChartHyperlinkProvider axisHyperlinkProvider = fillChart.getHyperlinkProvider();

			// In JFreeChart to add a second chart type to an existing chart
			// you need to add an axis, a data series and a renderer.  To
			// leverage existing code we simply create a new chart for the
			// axis and then pull out the bits we need and add them to the multi
			// chart.  Currently JFree only supports category plots and xy plots
			// in a multi-axis chart, and you can not mix the two.
			if (mainPlot instanceof CategoryPlot)
			{
				CategoryPlot mainCatPlot = (CategoryPlot)mainPlot;
				if (!(axisChart.getPlot() instanceof CategoryPlot))
				{
					throw new JRException("You can not mix plot types in a MultiAxisChart");
				}

				// Get the axis and add it to the multi axis chart plot
				CategoryPlot axisPlot = (CategoryPlot)axisChart.getPlot();
				mainCatPlot.setRangeAxis(axisNumber, axisPlot.getRangeAxis());
				mainCatPlot.setRangeAxisLocation(axisNumber, getChartAxisLocation(chartAxis));

				// Add the data set and map it to the recently added axis
				mainCatPlot.setDataset(axisNumber, axisPlot.getDataset());
				mainCatPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

				// Set the renderer to use to draw the dataset.
				mainCatPlot.setRenderer(axisNumber, axisPlot.getRenderer());

				// Handle any color series for this chart
				configureAxisSeriesColors(axisPlot.getRenderer(), fillChart.getPlot());
				
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(axisPlot.getDataset(), 
							axisHyperlinkProvider);
				}
			}
			else if (mainPlot instanceof XYPlot)
			{
				XYPlot mainXyPlot = (XYPlot)mainPlot;
				if (!(axisChart.getPlot() instanceof XYPlot))
				{
					throw new JRException("You can not mix plot types in a MultiAxisChart");
				}

				// Get the axis and add it to the multi axis chart plot
				XYPlot axisPlot = (XYPlot)axisChart.getPlot();
				mainXyPlot.setRangeAxis(axisNumber, axisPlot.getRangeAxis());
				mainXyPlot.setRangeAxisLocation(axisNumber, getChartAxisLocation(chartAxis));

				// Add the data set and map it to the recently added axis
				mainXyPlot.setDataset(axisNumber, axisPlot.getDataset());
				mainXyPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

				// Set the renderer to use to draw the dataset.
				mainXyPlot.setRenderer(axisNumber, axisPlot.getRenderer());

				// Handle any color series for this chart
				configureAxisSeriesColors(axisPlot.getRenderer(), fillChart.getPlot());
				
				if (axisHyperlinkProvider != null)
				{
					multiHyperlinkProvider.addHyperlinkProvider(axisPlot.getDataset(), 
							axisHyperlinkProvider);
				}
			}
			else
			{
				throw new JRException("MultiAxis charts only support Category and XY plots.");
			}
		}

		//set the multi hyperlink provider
		chartHyperlinkProvider = multiHyperlinkProvider;
	}


	/**
	 * The series colors set in the main plot of a multiple axis chart are used for
	 * all the rendered charts in the plot.  This is a problem with multiple line
	 * charts, using different scales and thus different axis.  All the lines will
	 * be drawn using the first series color (since they are the first series for that
	 * rendered) and it will be impossible to tell them apart.
	 * <br><br>
	 * For this reason we interpret series colors for charts included in a multiple
	 * axis chart as specify absolute series colors for that renderer.
	 *
	 * @param renderer the renderer of the chart being created
	 * @param jrPlot the Jasper view of that plot
	 */
	private void configureAxisSeriesColors(CategoryItemRenderer renderer, JRChartPlot jrPlot)
	{
		SortedSet<JRSeriesColor> seriesColors = jrPlot.getSeriesColors();

		if (seriesColors != null)
		{
			Iterator<JRSeriesColor> iter = seriesColors.iterator();
			while (iter.hasNext())
			{
				JRSeriesColor seriesColor = iter.next();
				renderer.setSeriesPaint(seriesColor.getSeriesOrder(), seriesColor.getColor());
			}
		}
	}

	/**
	 * The series colors set in the main plot of a multiple axis chart are used for
	 * all the rendered charts in the plot.  This is a problem with multiple line
	 * charts, using different scales and thus different axis.  All the lines will
	 * be drawn using the first series color (since they are the first series for that
	 * rendered) and it will be impossible to tell them apart.
	 * <br>
	 * For this reason we interpret series colors for charts included in a multiple
	 * axis chart as specify absolute series colors for that renderer.
	 *
	 * @param renderer the renderer of the chart being created
	 * @param jrPlot the Jasper view of that plot
	 */
	private void configureAxisSeriesColors(XYItemRenderer renderer, JRChartPlot jrPlot)
	{
		SortedSet<JRSeriesColor> seriesColors = jrPlot.getSeriesColors();

		if (seriesColors != null)
		{
			Iterator<JRSeriesColor> iter = seriesColors.iterator();
			while (iter.hasNext())
			{
				JRSeriesColor seriesColor = iter.next();
				renderer.setSeriesPaint(seriesColor.getSeriesOrder(), seriesColor.getColor());
			}
		}
	}

	/**
	 * Converts a JasperReport data range into one understood by JFreeChart.
	 *
	 * @param dataRange the JasperReport version of the range
	 * @param evaluation current expression evaluation phase
	 * @return the JFreeChart version of the range
	 * @throws JRException thrown when the low value of the range is greater than the
	 * 						high value
	 */
	protected Range convertRange(JRDataRange dataRange, byte evaluation) throws JRException
	{
		if (dataRange == null)
		{
			return null;
		}
		Number low = (Number)evaluateExpression(dataRange.getLowExpression(), evaluation);
		Number high = (Number)evaluateExpression(dataRange.getHighExpression(), evaluation);
		return new Range( low != null ? low.doubleValue() : 0.0,
								 high != null ? high.doubleValue() : 100.0);
	}

	/**
	 * Converts a JasperReports meter interval to one that JFreeChart understands.
	 *
	 * @param interval the JasperReports definition of an interval
	 * @param evaluation current evaluation time
	 * @return the JFreeChart version of the same interval
	 * @throws JRException thrown when the interval contains an invalid range
	 */
	protected MeterInterval convertInterval(JRMeterInterval interval, byte evaluation) throws JRException
	{
		String label = interval.getLabel();
		if (label == null)
		{
			label = "";
		}

		Range range = convertRange(interval.getDataRange(), evaluation);

		Color color = interval.getBackgroundColor();
		float[] components = color.getRGBColorComponents(null);
		float alpha = interval.getAlphaDouble() == null ? (float)JRMeterInterval.DEFAULT_TRANSPARENCY : interval.getAlphaDouble().floatValue();

		Color alphaColor = new Color(components[0], components[1], components[2], alpha);

		return new MeterInterval(label, range, alphaColor, null, alphaColor);
	}

	protected AxisLocation getChartAxisLocation(JRFillChartAxis chartAxis)
	{
		return chartAxis.getPositionValue() != null && chartAxis.getPositionValue() == AxisPositionEnum.RIGHT_OR_BOTTOM
				? AxisLocation.BOTTOM_OR_RIGHT 
				: AxisLocation.TOP_OR_LEFT;
	}
	
	protected void resolveElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateRenderer(evaluation);

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


	private void evaluateDatasetRun(byte evaluation) throws JRException
	{
		dataset.evaluateDatasetRun(evaluation);
	}


	public JRFillCloneable createClone(JRFillCloneFactory factory)
	{
		//not needed
		return null;
	}


	public JRHyperlinkParameter[] getHyperlinkParameters()
	{
		return ((JRChart) parent).getHyperlinkParameters();
	}


	public String getLinkType()
	{
		return ((JRChart) parent).getLinkType();
	}

	public String getLinkTarget()
	{
		return ((JRChart) parent).getLinkTarget();
	}


	public JRExpression getHyperlinkTooltipExpression()
	{
		return ((JRChart) parent).getHyperlinkTooltipExpression();
	}

	class FillChartContext implements ChartContext
	{
		private final byte evaluation; 
		
		protected FillChartContext(byte evaluation)
		{
			this.evaluation = evaluation;
		}
		
		public String evaluateTextExpression(JRExpression expression) throws JRException {
			return JRStringUtil.getString(JRFillChart.this.evaluateExpression(expression, evaluation));
		}

		public Object evaluateExpression(JRExpression expression) throws JRException {
			return JRFillChart.this.evaluateExpression(expression, evaluation);
		}

		public JRChart getChart() {
			return JRFillChart.this;
		}

		public Dataset getDataset() {
			return ((JRFillChartDataset)JRFillChart.this.getDataset()).getDataset();
		}

		public Object getLabelGenerator() {
			return ((JRFillChartDataset)JRFillChart.this.getDataset()).getLabelGenerator();
		}

		public Locale getLocale() {
			return JRFillChart.this.getLocale();
		}

		public TimeZone getTimeZone() {
			return JRFillChart.this.getTimeZone();
		}
		public byte getEvaluation() 
		{
			return JRFillChart.this.getEvaluationTimeValue().getValue();
		}
	}
}
