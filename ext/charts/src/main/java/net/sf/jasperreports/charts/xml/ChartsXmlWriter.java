/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.charts.xml;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.charts.ChartVisitor;
import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRChart;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRChartDataset;
import net.sf.jasperreports.charts.JRChartPlot;
import net.sf.jasperreports.charts.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRPieSeries;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimePeriodDataset;
import net.sf.jasperreports.charts.JRTimePeriodSeries;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRTimeSeriesDataset;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.charts.JRValueDataset;
import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.charts.type.TimePeriodEnum;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.type.DatasetResetTypeEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.engine.xml.XmlWriterVisitor;


/**
 * A writer that produces the JRXML representation of an in-memory report.
 * <p>
 * Sometimes report designs are generated automatically using the JasperReports 
 * API. Report design objects obtained this way can be serialized for disk storage or 
 * transferred over the network, but they also can be stored in JRXML format.
 * </p><p>
 * The JRXML representation of a given report design object can be obtained by using one 
 * of the <code>public static writeReport()</code> methods exposed by this class.
 * </p>
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @author Minor enhancements by Barry Klawans (bklawans@users.sourceforge.net)
 */
public class ChartsXmlWriter implements ChartVisitor// extends JRXmlWriter //FIXME7 duplicated class name
{
	private final JRXmlWriter parent;
	private final JRXmlWriteHelper writer;

	/**
	 *
	 */
	public ChartsXmlWriter(XmlWriterVisitor visitor)
	{
		this.parent = visitor.getXmlWriter();
		this.writer = parent.getXmlWriteHelper();
	}
	
	
	@Override
	public void visitChart(JRChart chart) 
	{
		try
		{
			writeChartTag(chart);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	/**
	 *
	 */
	
	private void writeChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_chart);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLegend, chart.getShowLegend());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, chart.getEvaluationTimeValue(), EvaluationTimeEnum.NOW);

		if (chart.getEvaluationTimeValue() == EvaluationTimeEnum.GROUP)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, chart.getEvaluationGroup().getName());
		}

		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkType, chart.getLinkType(), HyperlinkTypeEnum.NONE.getName());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_hyperlinkTarget, chart.getLinkTarget(), HyperlinkTargetEnum.SELF.getName());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_bookmarkLevel, chart.getBookmarkLevel(), JRAnchor.NO_BOOKMARK);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_customizerClass, chart.getCustomizerClass());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_renderType, chart.getRenderType());
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_theme, chart.getTheme());

		parent.writeReportElement(chart);
		parent.writeBox(chart.getLineBox());

		// write title
		writer.startElement(JRXmlConstants.ELEMENT_chartTitle);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_position, chart.getTitlePositionValue());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, chart.getOwnTitleColor());
		parent.writeFont(chart.getTitleFont());
		if (chart.getTitleExpression() != null)
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_titleExpression, chart.getTitleExpression());
		}
		writer.closeElement();

		// write subtitle
		writer.startElement(JRXmlConstants.ELEMENT_chartSubtitle);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, chart.getOwnSubtitleColor());
		parent.writeFont(chart.getSubtitleFont());
		if (chart.getSubtitleExpression() != null)
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_subtitleExpression, chart.getSubtitleExpression());
		}
		writer.closeElement();

		// write chartLegend
		writer.startElement(JRXmlConstants.ELEMENT_chartLegend);
		if (chart.getOwnLegendColor() != null)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_textColor, chart.getOwnLegendColor());
		}
		if (chart.getOwnLegendBackgroundColor() != null)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_backgroundColor, chart.getOwnLegendBackgroundColor());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_position, chart.getLegendPositionValue());
		parent.writeFont(chart.getLegendFont());
		writer.closeElement();

		writer.writeExpression(JRXmlConstants.ELEMENT_anchorNameExpression, chart.getAnchorNameExpression());
		if (parent.isNewerVersionOrEqual(JRConstants.VERSION_6_13_0))
		{
			writer.writeExpression(JRXmlConstants.ELEMENT_bookmarkLevelExpression, chart.getBookmarkLevelExpression());
		}
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkReferenceExpression, chart.getHyperlinkReferenceExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkWhenExpression, chart.getHyperlinkWhenExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkAnchorExpression, chart.getHyperlinkAnchorExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkPageExpression, chart.getHyperlinkPageExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_hyperlinkTooltipExpression, chart.getHyperlinkTooltipExpression());
		parent.writeHyperlinkParameters(chart.getHyperlinkParameters());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeCategoryDataSet(JRCategoryDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_categoryDataset);

		parent.writeElementDataset(dataset);

		/*   */
		JRCategorySeries[] categorySeries = dataset.getSeries();
		if (categorySeries != null && categorySeries.length > 0)
		{
			for(int i = 0; i < categorySeries.length; i++)
			{
				writeCategorySeries(categorySeries[i]);
			}
		}

		writer.closeElement();
	}


	private void writeTimeSeriesDataset(JRTimeSeriesDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timeSeriesDataset);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_timePeriod, dataset.getTimePeriodValue(), TimePeriodEnum.DAY);

		parent.writeElementDataset( dataset );

		JRTimeSeries[] timeSeries = dataset.getSeries();
		if( timeSeries != null && timeSeries.length > 0 )
		{
			for( int i = 0; i < timeSeries.length; i++ )
			{
				writeTimeSeries( timeSeries[i] );
			}
		}

		writer.closeElement();
	}


	/**
	 * 
	 */
	private void writeGanttDataset(JRGanttDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_ganttDataset);
		
		parent.writeElementDataset(dataset);
		
		/*   */
		JRGanttSeries[] ganttSeries = dataset.getSeries();
		if (ganttSeries != null && ganttSeries.length > 0)
		{
			for(int i = 0; i < ganttSeries.length; i++)
			{
				writeGanttSeries(ganttSeries[i]);
			}
		}
		
		writer.closeElement();
	}


	private void writeTimePeriodDataset(JRTimePeriodDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timePeriodDataset);
		parent.writeElementDataset(dataset);

		JRTimePeriodSeries[] timePeriodSeries = dataset.getSeries();
		if( timePeriodSeries != null && timePeriodSeries.length > 0 )
		{
			for( int i = 0; i < timePeriodSeries.length; i++ )
			{
				writeTimePeriodSeries(timePeriodSeries[i]);
			}
		}
		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writePieSeries(JRPieSeries pieSeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_pieSeries);

		writer.writeExpression(JRXmlConstants.ELEMENT_keyExpression, pieSeries.getKeyExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, pieSeries.getValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, pieSeries.getLabelExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_sectionHyperlink, pieSeries.getSectionHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	
	private void writeCategorySeries(JRCategorySeries categorySeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_categorySeries);

		writer.writeExpression(JRXmlConstants.ELEMENT_seriesExpression, categorySeries.getSeriesExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_categoryExpression, categorySeries.getCategoryExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, categorySeries.getValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, categorySeries.getLabelExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, categorySeries.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	private void writeXyzDataset(JRXyzDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyzDataset);
		parent.writeElementDataset(dataset);

		JRXyzSeries[] series = dataset.getSeries();
		if( series != null && series.length > 0 )
		{
			for( int i = 0; i < series.length; i++ )
			{
				writeXyzSeries(series[i]);
			}
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeXyzSeries(JRXyzSeries series) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyzSeries);

		writer.writeExpression(JRXmlConstants.ELEMENT_seriesExpression, series.getSeriesExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_xValueExpression, series.getXValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_yValueExpression, series.getYValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_zValueExpression, series.getZValueExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, series.getItemHyperlink());

		writer.closeElement();
	}

	/**
	 *
	 */
	
	private void writeXySeries(JRXySeries xySeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xySeries);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_autoSort, xySeries.getAutoSort());
		writer.writeExpression(JRXmlConstants.ELEMENT_seriesExpression, xySeries.getSeriesExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_xValueExpression, xySeries.getXValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_yValueExpression, xySeries.getYValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, xySeries.getLabelExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, xySeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	private void writeXyDataset(JRXyDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyDataset);

		parent.writeElementDataset(dataset);

		/*   */
		JRXySeries[] xySeries = dataset.getSeries();
		if (xySeries != null && xySeries.length > 0)
		{
			for(int i = 0; i < xySeries.length; i++)
			{
				writeXySeries(xySeries[i]);
			}
		}

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeTimeSeries(JRTimeSeries timeSeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timeSeries);

		writer.writeExpression(JRXmlConstants.ELEMENT_seriesExpression, timeSeries.getSeriesExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_timePeriodExpression, timeSeries.getTimePeriodExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, timeSeries.getValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, timeSeries.getLabelExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, timeSeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 * 
	 */
	
	private void writeGanttSeries(JRGanttSeries ganttSeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_ganttSeries);
		
		writer.writeExpression(JRXmlConstants.ELEMENT_seriesExpression, ganttSeries.getSeriesExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_taskExpression, ganttSeries.getTaskExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_subtaskExpression, ganttSeries.getSubtaskExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_startDateExpression, ganttSeries.getStartDateExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_endDateExpression, ganttSeries.getEndDateExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_percentExpression, ganttSeries.getPercentExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, ganttSeries.getLabelExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, ganttSeries.getItemHyperlink());
		
		writer.closeElement();
	}


	
	private void writeTimePeriodSeries(JRTimePeriodSeries timePeriodSeries) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timePeriodSeries);

		writer.writeExpression(JRXmlConstants.ELEMENT_seriesExpression, timePeriodSeries.getSeriesExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_startDateExpression, timePeriodSeries.getStartDateExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_endDateExpression, timePeriodSeries.getEndDateExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, timePeriodSeries.getValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, timePeriodSeries.getLabelExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, timePeriodSeries.getItemHyperlink());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writePieDataset(JRPieDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_pieDataset, parent.getNamespace());

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_maxCount, dataset.getMaxCount());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_minPercentage, dataset.getMinPercentage());

		parent.writeElementDataset(dataset);

		/*   */
		JRPieSeries[] pieSeries = dataset.getSeries();
		if (pieSeries != null)
		{
			if (pieSeries.length > 1)
			{
				for(int i = 0; i < pieSeries.length; i++)
				{
					writePieSeries(pieSeries[i]);
				}
			}
			else
			{
				//preserve old syntax of single series pie datasets
				JRPieSeries ps = pieSeries[0];
				writer.writeExpression(JRXmlConstants.ELEMENT_keyExpression, ps.getKeyExpression());
				writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, ps.getValueExpression());
				writer.writeExpression(JRXmlConstants.ELEMENT_labelExpression, ps.getLabelExpression());
				parent.writeHyperlink(JRXmlConstants.ELEMENT_sectionHyperlink, ps.getSectionHyperlink());
			}
		}

		writer.writeExpression(JRXmlConstants.ELEMENT_otherKeyExpression, dataset.getOtherKeyExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_otherLabelExpression, dataset.getOtherLabelExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_otherSectionHyperlink, dataset.getOtherSectionHyperlink());

		writer.closeElement();
	}

	/**
	 * Writes the description of a value dataset to the output stream.
	 * @param dataset the value dataset to persist
	 */
	
	public void writeValueDataset(JRValueDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_valueDataset, parent.getNamespace());

		// default reset type of value datasets is None
		parent.writeElementDataset(dataset, DatasetResetTypeEnum.NONE, true);

		writer.writeExpression(JRXmlConstants.ELEMENT_valueExpression, dataset.getValueExpression());
		writer.closeElement();
	}


	/**
	 * Writes the description of how to display a value in a valueDataset.
	 *
	 * @param valueDisplay the description to save
	 */
	public void writeValueDisplay(JRValueDisplay valueDisplay) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_valueDisplay, parent.getNamespace());

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, valueDisplay.getColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mask, valueDisplay.getMask());

		parent.writeFont(valueDisplay.getFont());

		writer.closeElement();
	}

	/**
	 * Writes the description of how to display item labels in a category plot.
	 *
	 * @param itemLabel the description to save
	 */
	public void writeItemLabel(JRItemLabel itemLabel) throws IOException
	{
		if(itemLabel != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_itemLabel, parent.getNamespace());
	
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, itemLabel.getColor());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_backgroundColor, itemLabel.getBackgroundColor());
	//		writer.addAttribute(JRXmlConstants.ATTRIBUTE_mask, itemLabel.getMask());
	
			parent.writeFont(itemLabel.getFont());
	
			writer.closeElement();
		}
	}

	/**
	 * Writes a data range block to the output stream.
	 *
	 * @param dataRange the range to write
	 */
	
	public void writeDataRange(JRDataRange dataRange) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_dataRange, parent.getNamespace());

		writer.writeExpression(JRXmlConstants.ELEMENT_lowExpression, dataRange.getLowExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_highExpression, dataRange.getHighExpression());
		writer.closeElement();
	}


	/**
	 * Writes a meter interval description to the output stream.
	 *
	 * @param interval the interval to write
	 */
	private void writeMeterInterval(JRMeterInterval interval) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_meterInterval);

		writer.addAttribute(JRXmlConstants.ATTRIBUTE_label, interval.getLabel());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, interval.getBackgroundColor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_alpha, interval.getAlphaDouble());

		writeDataRange(interval.getDataRange());

		writer.closeElement();
	}

	/**
	 * Writes out the contents of a series colors block for a chart.  Assumes the caller
	 * has already written the <code>&lt;seriesColors&gt;</code> tag.
	 *
	 * @param seriesColors the colors to write
	 */
	private void writeSeriesColors(SortedSet<JRSeriesColor> seriesColors) throws IOException
	{
		if (seriesColors == null || seriesColors.size() == 0)
		{
			return;
		}
		//FIXME why do we need an array?
		JRSeriesColor[] colors = seriesColors.toArray(new JRSeriesColor[seriesColors.size()]);
		for (int i = 0; i < colors.length; i++)
		{
			writer.startElement(JRXmlConstants.ELEMENT_seriesColor);
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_seriesOrder, colors[i].getSeriesOrder());
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_color, colors[i].getColor());
			writer.closeElement();
		}
	}

	/**
	 * Write the information about a the data and layout that make up one range axis in
	 * a multiple axis chart.
	 *
	 * @param chartAxis the axis being written
	 */
	private void writeChartAxis(JRChartAxis chartAxis) throws IOException
	{
		writer.startElement(JRChartAxisFactory.ELEMENT_axis);
		writer.addAttribute(JRChartAxisFactory.ATTRIBUTE_position, chartAxis.getPositionValue());

		// Let the nested chart describe itself
		writeChartTag(chartAxis.getChart());
		writer.closeElement();
	}

	/**
	 *
	 *
	 */
	@SuppressWarnings("deprecation")
	private void writePlot(JRChartPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_plot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backcolor, plot.getOwnBackcolor());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_orientation, plot.getOrientationValue(), PlotOrientationEnum.VERTICAL);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_backgroundAlpha, plot.getBackgroundAlphaFloat());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_foregroundAlpha, plot.getForegroundAlphaFloat());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelRotation, plot.getLabelRotationDouble());
		writeSeriesColors(plot.getSeriesColors());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePieChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_pieChart, parent.getNamespace());
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPiePlot plot = (JRPiePlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_piePlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isCircular, plot.getCircular());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelFormat, plot.getLabelFormat());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_legendLabelFormat, plot.getLegendLabelFormat());
		writePlot(chart.getPlot());
		writeItemLabel(plot.getItemLabel());
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writePie3DChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_pie3DChart, parent.getNamespace());
		writeChart(chart);
		writePieDataset((JRPieDataset) chart.getDataset());

		// write plot
		JRPie3DPlot plot = (JRPie3DPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_pie3DPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_depthFactor, plot.getDepthFactorDouble());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isCircular, plot.getCircular());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelFormat, plot.getLabelFormat());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_legendLabelFormat, plot.getLegendLabelFormat());
		writePlot(chart.getPlot());
		writeItemLabel(plot.getItemLabel());
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 * Writes out the category axis format block.
	 *
	 * @param axisLabelFont font to use for the axis label
	 * @param axisLabelColor color to use for the axis label
	 * @param axisTickLabelFont font to use for the label of each tick mark
	 * @param axisTickLabelColor color to use for the label of each tick mark
	 * @param axisTickLabelMask formatting mask to use for the label of each tick mark
	 * @param axisVerticalTickLabels flag to render tick labels at 90 degrees
	 * @param labelRotation label rotation angle
	 * @param axisLineColor the color to use for the axis line and any tick marks
	 *
	 */
	public void writeCategoryAxisFormat(
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Double labelRotation, 
		Color axisLineColor
		)  throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_categoryAxisFormat, parent.getNamespace());
		
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelRotation, labelRotation);

		writeAxisFormat(
			axisLabelFont, 
			axisLabelColor,
			axisTickLabelFont, 
			axisTickLabelColor,
			axisTickLabelMask, 
			axisVerticalTickLabels, 
			axisLineColor
			);
		
		writer.closeElement();
	}
	
	
	/**
	 * Writes out the axis format block for a chart axis.
	 *
	 * @param axisFormatElementName the name of the axis format element being written
	 * @param axisLabelFont font to use for the axis label
	 * @param axisLabelColor color to use for the axis label
	 * @param axisTickLabelFont font to use for the label of each tick mark
	 * @param axisTickLabelColor color to use for the label of each tick mark
	 * @param axisTickLabelMask formatting mask to use for the label of each tick mark
	 * @param axisVerticalTickLabels flag to render tick labels at 90 degrees
	 * @param axisLineColor the color to use for the axis line and any tick marks
	 *
	 */
	public void writeAxisFormat(
		String axisFormatElementName,
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Color axisLineColor
		)  throws IOException
	{
		writer.startElement(axisFormatElementName, parent.getNamespace());

		writeAxisFormat(
			axisLabelFont, 
			axisLabelColor,
			axisTickLabelFont, 
			axisTickLabelColor,
			axisTickLabelMask, 
			axisVerticalTickLabels, 
			axisLineColor
			);
		
		writer.closeElement();
	}
	
	
	/**
	 * Writes out the axis format block for a chart axis.
	 *
	 * @param axisLabelFont font to use for the axis label
	 * @param axisLabelColor color to use for the axis label
	 * @param axisTickLabelFont font to use for the label of each tick mark
	 * @param axisTickLabelColor color to use for the label of each tick mark
	 * @param axisTickLabelMask formatting mask to use for the label of each tick mark
	 * @param axisVerticalTickLabels flag to render tick labels at 90 degrees
	 * @param axisLineColor the color to use for the axis line and any tick marks
	 *
	 */
	public void writeAxisFormat(
		JRFont axisLabelFont, 
		Color axisLabelColor,
		JRFont axisTickLabelFont, 
		Color axisTickLabelColor,
		String axisTickLabelMask, 
		Boolean axisVerticalTickLabels, 
		Color axisLineColor
		)  throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_axisFormat);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_labelColor, axisLabelColor);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_tickLabelColor, axisTickLabelColor);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_tickLabelMask, axisTickLabelMask);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_verticalTickLabels, axisVerticalTickLabels);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_axisLineColor, axisLineColor);

		if (axisLabelFont != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_labelFont);
			parent.writeFont(axisLabelFont);
			writer.closeElement();
		}

		if (axisTickLabelFont != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_tickLabelFont);
			parent.writeFont(axisTickLabelFont);
			writer.closeElement();
		}

		writer.closeElement();
	}
	/**
	 *
	 */
	
	private void writeBarPlot(JRBarPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_barPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowTickLabels, plot.getShowTickLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowTickMarks, plot.getShowTickMarks());

		writePlot(plot);
		writeItemLabel(plot.getItemLabel());
		
		writer.writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression());
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
						plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
						plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
						plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeBubblePlot(JRBubblePlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_bubblePlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_scaleType, plot.getScaleTypeValue());
		writePlot(plot);

		writer.writeExpression(JRXmlConstants.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor());
		
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeLinePlot(JRLinePlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_linePlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot(plot);

		writer.writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression());
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());
		
		writer.closeElement();
	}


	
	private void writeTimeSeriesPlot(JRTimeSeriesPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timeSeriesPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot( plot );

		writer.writeExpression(JRXmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	public void writeBar3DPlot(JRBar3DPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_bar3DPlot, parent.getNamespace());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLabels, plot.getShowLabels());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_xOffset, plot.getXOffsetDouble());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_yOffset, plot.getYOffsetDouble());
		writePlot(plot);
		writeItemLabel(plot.getItemLabel());
		
		writer.writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression());
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBarChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_barChart, parent.getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBar3DChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_bar3DChart, parent.getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBar3DPlot((JRBar3DPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeBubbleChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_bubbleChart, parent.getNamespace());
		writeChart(chart);
		writeXyzDataset((JRXyzDataset) chart.getDataset());
		writeBubblePlot((JRBubblePlot) chart.getPlot());
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeStackedBarChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_stackedBarChart, parent.getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeStackedBar3DChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_stackedBar3DChart, parent.getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeBar3DPlot((JRBar3DPlot) chart.getPlot());
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeLineChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_lineChart, parent.getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());
		writer.closeElement();
	}


	public void writeTimeSeriesChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_timeSeriesChart, parent.getNamespace());
		writeChart(chart);
		writeTimeSeriesDataset((JRTimeSeriesDataset)chart.getDataset());
		writeTimeSeriesPlot((JRTimeSeriesPlot)chart.getPlot());
		writer.closeElement();
	}

	
	public void writeHighLowDataset(JRHighLowDataset dataset) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_highLowDataset, parent.getNamespace());

		parent.writeElementDataset(dataset);

		writer.writeExpression(JRXmlConstants.ELEMENT_seriesExpression, dataset.getSeriesExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_dateExpression, dataset.getDateExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_highExpression, dataset.getHighExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_lowExpression, dataset.getLowExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_openExpression, dataset.getOpenExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_closeExpression, dataset.getCloseExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_volumeExpression, dataset.getVolumeExpression());
		parent.writeHyperlink(JRXmlConstants.ELEMENT_itemHyperlink, dataset.getItemHyperlink());

		writer.closeElement();
	}


	
	public void writeHighLowChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_highLowChart, parent.getNamespace());

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRHighLowPlot plot = (JRHighLowPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_highLowPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowOpenTicks, plot.getShowOpenTicks());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowCloseTicks, plot.getShowCloseTicks());

		writePlot(plot);

		writer.writeExpression(JRXmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());

		writer.closeElement();
		writer.closeElement();
	}


	/**
	 * 
	 */
	public void writeGanttChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_ganttChart, parent.getNamespace());
		
		writeChart(chart);
		writeGanttDataset((JRGanttDataset) chart.getDataset());
		writeBarPlot((JRBarPlot) chart.getPlot());
		
		writer.closeElement();
	}


	
	public void writeCandlestickChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_candlestickChart, parent.getNamespace());

		writeChart(chart);
		writeHighLowDataset((JRHighLowDataset) chart.getDataset());

		JRCandlestickPlot plot = (JRCandlestickPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_candlestickPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowVolume, plot.getShowVolume());

		writePlot(plot);

		writer.writeExpression(JRXmlConstants.ELEMENT_timeAxisLabelExpression, plot.getTimeAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_timeAxisFormat, plot.getTimeAxisLabelFont(), plot.getOwnTimeAxisLabelColor(),
				plot.getTimeAxisTickLabelFont(), plot.getOwnTimeAxisTickLabelColor(),
				plot.getTimeAxisTickLabelMask(), plot.getTimeAxisVerticalTickLabels(), plot.getOwnTimeAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());

		writer.closeElement();
		writer.closeElement();
	}

	/**
	 *
	 */
	
	private void writeAreaPlot(JRAreaPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_areaPlot);
		writePlot(plot);

		writer.writeExpression(JRXmlConstants.ELEMENT_categoryAxisLabelExpression, plot.getCategoryAxisLabelExpression());
		writeCategoryAxisFormat(plot.getCategoryAxisLabelFont(), plot.getOwnCategoryAxisLabelColor(),
				plot.getCategoryAxisTickLabelFont(), plot.getOwnCategoryAxisTickLabelColor(),
				plot.getCategoryAxisTickLabelMask(), plot.getCategoryAxisVerticalTickLabels(), 
				plot.getCategoryAxisTickLabelRotation(), plot.getOwnCategoryAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_valueAxisLabelExpression, plot.getValueAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_valueAxisFormat, plot.getValueAxisLabelFont(), plot.getOwnValueAxisLabelColor(),
				plot.getValueAxisTickLabelFont(), plot.getOwnValueAxisTickLabelColor(),
				plot.getValueAxisTickLabelMask(), plot.getValueAxisVerticalTickLabels(), plot.getOwnValueAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_areaChart, parent.getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	
	private void writeScatterPlot(JRScatterPlot plot) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_scatterPlot);
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowLines, plot.getShowLines());
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_isShowShapes, plot.getShowShapes());

		writePlot(plot);

		writer.writeExpression(JRXmlConstants.ELEMENT_xAxisLabelExpression, plot.getXAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_xAxisFormat, plot.getXAxisLabelFont(), plot.getOwnXAxisLabelColor(),
				plot.getXAxisTickLabelFont(), plot.getOwnXAxisTickLabelColor(),
				plot.getXAxisTickLabelMask(), plot.getXAxisVerticalTickLabels(), plot.getOwnXAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_yAxisLabelExpression, plot.getYAxisLabelExpression());
		writeAxisFormat(JRXmlConstants.ELEMENT_yAxisFormat, plot.getYAxisLabelFont(), plot.getOwnYAxisLabelColor(),
				plot.getYAxisTickLabelFont(), plot.getOwnYAxisTickLabelColor(),
				plot.getYAxisTickLabelMask(), plot.getYAxisVerticalTickLabels(), plot.getOwnYAxisLineColor());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMinValueExpression, plot.getDomainAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_domainAxisMaxValueExpression, plot.getDomainAxisMaxValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMinValueExpression, plot.getRangeAxisMinValueExpression());
		writer.writeExpression(JRXmlConstants.ELEMENT_rangeAxisMaxValueExpression, plot.getRangeAxisMaxValueExpression());
		
		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeScatterChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_scatterChart, parent.getNamespace());

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeScatterPlot((JRScatterPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeXyAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyAreaChart, parent.getNamespace());

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeXyBarChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyBarChart, parent.getNamespace()); //FIXME7

		writeChart(chart);
		JRChartDataset dataset = chart.getDataset();

		if( dataset.getDatasetType() == JRChartDataset.TIMESERIES_DATASET ){
			writeTimeSeriesDataset( (JRTimeSeriesDataset)dataset );
		}
		else if( dataset.getDatasetType() == JRChartDataset.TIMEPERIOD_DATASET ){
			writeTimePeriodDataset( (JRTimePeriodDataset)dataset );
		}
		else if( dataset.getDatasetType() == JRChartDataset.XY_DATASET ){
			writeXyDataset( (JRXyDataset)dataset );
		}

		writeBarPlot((JRBarPlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 *
	 */
	public void writeXyLineChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_xyLineChart, parent.getNamespace());

		writeChart(chart);
		writeXyDataset((JRXyDataset) chart.getDataset());
		writeLinePlot((JRLinePlot) chart.getPlot());

		writer.closeElement();
	}


	/**
	 * Writes the definition of a meter chart to the output stream.
	 *
	 * @param chart the meter chart to write
	 */
	public void writeMeterChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_meterChart, parent.getNamespace());

		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRMeterPlot plot = (JRMeterPlot) chart.getPlot();
		writer.startElement(JRMeterPlotFactory.ELEMENT_meterPlot);
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_shape, plot.getShapeValue());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_angle, plot.getMeterAngleInteger());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_units, plot.getUnits());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickInterval, plot.getTickIntervalDouble());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_meterColor, plot.getMeterBackgroundColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_needleColor, plot.getNeedleColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickColor, plot.getTickColor());
		writer.addAttribute(JRMeterPlotFactory.ATTRIBUTE_tickCount, plot.getTickCount());
		writePlot(chart.getPlot());
		if (plot.getTickLabelFont() != null)
		{
			writer.startElement(JRXmlConstants.ELEMENT_tickLabelFont);
			parent.writeFont(plot.getTickLabelFont());
			writer.closeElement();
		}
		writeValueDisplay(plot.getValueDisplay());
		writeDataRange(plot.getDataRange());

		List<JRMeterInterval> intervals = plot.getIntervals();
		if (intervals != null)
		{
			Iterator<JRMeterInterval> iter = intervals.iterator();
			while (iter.hasNext())
			{
				JRMeterInterval meterInterval = iter.next();
				writeMeterInterval(meterInterval);
			}
		}
		writer.closeElement();

		writer.closeElement();
	}


	/**
	 * Writes the description of a thermometer chart to the output stream.
	 *
	 * @param chart the thermometer chart to write
	 */
	public void writeThermometerChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_thermometerChart, parent.getNamespace());

		writeChart(chart);
		writeValueDataset((JRValueDataset) chart.getDataset());

		// write plot
		JRThermometerPlot plot = (JRThermometerPlot) chart.getPlot();

		writer.startElement(JRThermometerPlotFactory.ELEMENT_thermometerPlot, parent.getNamespace());

		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_valueLocation, plot.getValueLocationValue());
		writer.addAttribute(JRThermometerPlotFactory.ATTRIBUTE_mercuryColor, plot.getMercuryColor());

		writePlot(chart.getPlot());

		writeValueDisplay(plot.getValueDisplay());
		writeDataRange(plot.getDataRange());

		if (plot.getLowRange() != null)
		{
			writer.startElement(JRThermometerPlotFactory.ELEMENT_lowRange);
			writeDataRange(plot.getLowRange());
			writer.closeElement();
		}

		if (plot.getMediumRange() != null)
		{
			writer.startElement(JRThermometerPlotFactory.ELEMENT_mediumRange);
			writeDataRange(plot.getMediumRange());
			writer.closeElement();
		}

		if (plot.getHighRange() != null)
		{
			writer.startElement(JRThermometerPlotFactory.ELEMENT_highRange);
			writeDataRange(plot.getHighRange());
			writer.closeElement();
		}


		writer.closeElement();

		writer.closeElement();
	}


	/**
	 * Writes the definition of a multiple axis chart to the output stream.
	 *
	 * @param chart the multiple axis chart to write
	 */
	public void writeMultiAxisChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_multiAxisChart, parent.getNamespace());

		writeChart(chart);

		// write plot
		JRMultiAxisPlot plot = (JRMultiAxisPlot) chart.getPlot();
		writer.startElement(JRXmlConstants.ELEMENT_multiAxisPlot);

		writePlot(chart.getPlot());

		List<JRChartAxis> axes = plot.getAxes();
		if (axes != null)
		{
			Iterator<JRChartAxis> iter = axes.iterator();
			while (iter.hasNext())
			{
				JRChartAxis chartAxis = iter.next();
				writeChartAxis(chartAxis);
			}
		}
		writer.closeElement();

		writer.closeElement();
	}

	/**
	 *
	 */
	public void writeStackedAreaChart(JRChart chart) throws IOException
	{
		writer.startElement(JRXmlConstants.ELEMENT_stackedAreaChart, parent.getNamespace());

		writeChart(chart);
		writeCategoryDataSet((JRCategoryDataset) chart.getDataset());
		writeAreaPlot((JRAreaPlot) chart.getPlot());

		writer.closeElement();
	}


	public void writeChartTag(JRChart chart) throws IOException
	{
		switch(chart.getChartType()) {
			case JRChart.CHART_TYPE_AREA:
				writeAreaChart(chart);
				break;
			case JRChart.CHART_TYPE_BAR:
				writeBarChart(chart);
				break;
			case JRChart.CHART_TYPE_BAR3D:
				writeBar3DChart(chart);
				break;
			case JRChart.CHART_TYPE_BUBBLE:
				writeBubbleChart(chart);
				break;
			case JRChart.CHART_TYPE_CANDLESTICK:
				writeCandlestickChart(chart);
				break;
			case JRChart.CHART_TYPE_HIGHLOW:
				writeHighLowChart(chart);
				break;
			case JRChart.CHART_TYPE_LINE:
				writeLineChart(chart);
				break;
			case JRChart.CHART_TYPE_METER:
				writeMeterChart(chart);
				break;
			case JRChart.CHART_TYPE_MULTI_AXIS:
				writeMultiAxisChart(chart);
				break;
			case JRChart.CHART_TYPE_PIE:
				writePieChart(chart);
				break;
			case JRChart.CHART_TYPE_PIE3D:
				writePie3DChart(chart);
				break;
			case JRChart.CHART_TYPE_SCATTER:
				writeScatterChart(chart);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR:
				writeStackedBarChart(chart);
				break;
			case JRChart.CHART_TYPE_STACKEDBAR3D:
				writeStackedBar3DChart(chart);
				break;
			case JRChart.CHART_TYPE_THERMOMETER:
				writeThermometerChart(chart);
				break;
			case JRChart.CHART_TYPE_TIMESERIES:
				writeTimeSeriesChart( chart );
				break;
			case JRChart.CHART_TYPE_XYAREA:
				writeXyAreaChart(chart);
				break;
			case JRChart.CHART_TYPE_XYBAR:
				writeXyBarChart(chart);
				break;
			case JRChart.CHART_TYPE_XYLINE:
				writeXyLineChart(chart);
				break;
			case JRChart.CHART_TYPE_STACKEDAREA:
				writeStackedAreaChart(chart);
				break;
			case JRChart.CHART_TYPE_GANTT:
				writeGanttChart(chart);
				break;
			default:
				throw 
				new JRRuntimeException(
						parent.EXCEPTION_MESSAGE_KEY_UNSUPPORTED_CHART_TYPE, //FIXME7
						(Object[])null);
		}
	}


}
