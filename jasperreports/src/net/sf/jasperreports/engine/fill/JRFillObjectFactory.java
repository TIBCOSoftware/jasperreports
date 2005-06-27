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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRAreaChart;
import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DChart;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarChart;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubbleChart;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickChart;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRCategoryDataset;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.JRHighLowChart;
import net.sf.jasperreports.charts.JRHighLowDataset;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRIntervalXyDataset;
import net.sf.jasperreports.charts.JRLineChart;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRPie3DChart;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPieChart;
import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterChart;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRStackedBar3DChart;
import net.sf.jasperreports.charts.JRStackedBarChart;
import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.charts.JRXyAreaChart;
import net.sf.jasperreports.charts.JRXyBarChart;
import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXyLineChart;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.charts.fill.JRFillAreaPlot;
import net.sf.jasperreports.charts.fill.JRFillBar3DPlot;
import net.sf.jasperreports.charts.fill.JRFillBarPlot;
import net.sf.jasperreports.charts.fill.JRFillBubblePlot;
import net.sf.jasperreports.charts.fill.JRFillCandlestickPlot;
import net.sf.jasperreports.charts.fill.JRFillCategoryDataset;
import net.sf.jasperreports.charts.fill.JRFillCategorySeries;
import net.sf.jasperreports.charts.fill.JRFillHighLowDataset;
import net.sf.jasperreports.charts.fill.JRFillHighLowPlot;
import net.sf.jasperreports.charts.fill.JRFillIntervalXyDataset;
import net.sf.jasperreports.charts.fill.JRFillLinePlot;
import net.sf.jasperreports.charts.fill.JRFillPie3DPlot;
import net.sf.jasperreports.charts.fill.JRFillPieDataset;
import net.sf.jasperreports.charts.fill.JRFillPiePlot;
import net.sf.jasperreports.charts.fill.JRFillScatterPlot;
import net.sf.jasperreports.charts.fill.JRFillTimeSeries;
import net.sf.jasperreports.charts.fill.JRFillXyDataset;
import net.sf.jasperreports.charts.fill.JRFillXySeries;
import net.sf.jasperreports.charts.fill.JRFillXyzDataset;
import net.sf.jasperreports.charts.fill.JRFillXyzSeries;
import net.sf.jasperreports.engine.JRAbstractObjectFactory;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseReportFont;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillObjectFactory extends JRAbstractObjectFactory
{


	/**
	 *
	 */
	private JRBaseFiller filler = null;

	private JRFont defaultFont = null;

	private List datasets = new ArrayList();


	/**
	 *
	 */
	protected JRFillObjectFactory(JRBaseFiller filler)
	{
		this.filler = filler;
	}


	/**
	 *
	 */
	protected JRFillChartDataset[] getDatasets()
	{
		return (JRFillChartDataset[]) datasets.toArray(new JRFillChartDataset[datasets.size()]);
	}


	/**
	 *
	 */
	public JRReportFont getReportFont(JRReportFont font)
	{
		JRBaseReportFont fillFont = null;

		if (font != null)
		{
			fillFont = (JRBaseReportFont)get(font);
			if (fillFont == null)
			{
				fillFont = new JRBaseReportFont(font);
				fillFont.setCachingAttributes(true);
				put(font, fillFont);
			}
		}

		return fillFont;
	}


	/**
	 *
	 */
	protected JRBaseFont getFont(JRFont font)
	{
		JRBaseFont fillFont = null;

		if (font != null)
		{
			fillFont = (JRBaseFont)get(font);
			if (fillFont == null)
			{
				fillFont =
					new JRBaseFont(
						filler.getJasperPrint(),
						getReportFont(font.getReportFont()),
						font
						);
				fillFont.setCachingAttributes(true);
				put(font, fillFont);
			}
		}
		else
		{
			if (defaultFont == null)
			{
				defaultFont = new JRBaseFont();
			}
			fillFont = getFont(defaultFont);
		}

		return fillFont;
	}


	/**
	 *
	 */
	protected JRFillParameter getParameter(JRParameter parameter)
	{
		JRFillParameter fillParameter = null;

		if (parameter != null)
		{
			fillParameter = (JRFillParameter)get(parameter);
			if (fillParameter == null)
			{
				fillParameter = new JRFillParameter(parameter, this);
			}
		}

		return fillParameter;
	}


	/**
	 *
	 */
	protected JRFillField getField(JRField field)
	{
		JRFillField fillField = null;

		if (field != null)
		{
			fillField = (JRFillField)get(field);
			if (fillField == null)
			{
				fillField = new JRFillField(field, this);
			}
		}

		return fillField;
	}


	/**
	 *
	 */
	protected JRFillVariable getVariable(JRVariable variable)
	{
		JRFillVariable fillVariable = null;

		if (variable != null)
		{
			fillVariable = (JRFillVariable)get(variable);
			if (fillVariable == null)
			{
				fillVariable = new JRFillVariable(variable, this);
			}
		}

		return fillVariable;
	}


	/**
	 *
	 */
	protected JRFillGroup getGroup(JRGroup group)
	{
		JRFillGroup fillGroup = null;

		if (group != null)
		{
			fillGroup = (JRFillGroup)get(group);
			if (fillGroup == null)
			{
				fillGroup = new JRFillGroup(group, this);
			}
		}

		return fillGroup;
	}


	/**
	 *
	 */
	protected JRFillBand getBand(JRBand band)
	{
		JRFillBand fillBand = null;

		//if (band != null)
		//{
		// for null bands, the filler's missingFillBand will be returned
			fillBand = (JRFillBand)get(band);
			if (fillBand == null)
			{
				fillBand = new JRFillBand(filler, band, this);
			}
		//}

		return fillBand;
	}


	/**
	 *
	 */
	protected JRFillElementGroup getElementGroup(JRElementGroup elementGroup)
	{
		JRFillElementGroup fillElementGroup = null;

		if (elementGroup != null)
		{
			fillElementGroup = (JRFillElementGroup)get(elementGroup);
			if (fillElementGroup == null)
			{
				fillElementGroup = new JRFillElementGroup(elementGroup, this);
			}
		}

		return fillElementGroup;
	}


	/**
	 *
	 */
	public JRLine getLine(JRLine line)
	{
		JRFillLine fillLine = null;

		if (line != null)
		{
			fillLine = (JRFillLine)get(line);
			if (fillLine == null)
			{
				fillLine = new JRFillLine(filler, line, this);
			}
		}

		return fillLine;
	}


	/**
	 *
	 */
	public JRRectangle getRectangle(JRRectangle rectangle)
	{
		JRFillRectangle fillRectangle = null;

		if (rectangle != null)
		{
			fillRectangle = (JRFillRectangle)get(rectangle);
			if (fillRectangle == null)
			{
				fillRectangle = new JRFillRectangle(filler, rectangle, this);
			}
		}

		return fillRectangle;
	}


	/**
	 *
	 */
	public JREllipse getEllipse(JREllipse ellipse)
	{
		JRFillEllipse fillEllipse = null;

		if (ellipse != null)
		{
			fillEllipse = (JRFillEllipse)get(ellipse);
			if (fillEllipse == null)
			{
				fillEllipse = new JRFillEllipse(filler, ellipse, this);
			}
		}

		return fillEllipse;
	}


	/**
	 *
	 */
	public JRImage getImage(JRImage image)
	{
		JRFillImage fillImage = null;

		if (image != null)
		{
			fillImage = (JRFillImage)get(image);
			if (fillImage == null)
			{
				fillImage = new JRFillImage(filler, image, this);
			}
		}

		return fillImage;
	}


	/**
	 *
	 */
	public JRStaticText getStaticText(JRStaticText staticText)
	{
		JRFillStaticText fillStaticText = null;

		if (staticText != null)
		{
			fillStaticText = (JRFillStaticText)get(staticText);
			if (fillStaticText == null)
			{
				fillStaticText = new JRFillStaticText(filler, staticText, this);
			}
		}

		return fillStaticText;
	}


	/**
	 *
	 */
	public JRTextField getTextField(JRTextField textField)
	{
		JRFillTextField fillTextField = null;

		if (textField != null)
		{
			fillTextField = (JRFillTextField)get(textField);
			if (fillTextField == null)
			{
				fillTextField = new JRFillTextField(filler, textField, this);
			}
		}

		return fillTextField;
	}


	/**
	 *
	 */
	public JRSubreport getSubreport(JRSubreport subreport)
	{
		JRFillSubreport fillSubreport = null;

		if (subreport != null)
		{
			fillSubreport = (JRFillSubreport)get(subreport);
			if (fillSubreport == null)
			{
				fillSubreport = new JRFillSubreport(filler, subreport, this);
			}
		}

		return fillSubreport;
	}


	public JRChart getChart(JRChart chart)
	{
		JRFillChart fillChart = null;

		if (chart != null)
		{
			fillChart = (JRFillChart)get(chart);
			if (fillChart == null)
			{
				fillChart = new JRFillChart(filler, chart, this);
			}
		}

		return fillChart;
	}


	/**
	 *
	 *
	public JRPieChart getPieChart(JRPieChart pieChart)
	{
		JRFillPieChart fillPieChart = null;

		if (pieChart != null)
		{
			fillPieChart = (JRFillPieChart)get(pieChart);
			if (fillPieChart == null)
			{
				fillPieChart = new JRFillPieChart(filler, pieChart, this);
			}
		}

		return fillPieChart;
	}


	/**
	 *
	 */
	public JRPieDataset getPieDataset(JRPieDataset pieDataset)
	{
		JRFillPieDataset fillPieDataset = null;

		if (pieDataset != null)
		{
			fillPieDataset = (JRFillPieDataset)get(pieDataset);
			if (fillPieDataset == null)
			{
				fillPieDataset = new JRFillPieDataset(pieDataset, this);
				datasets.add(fillPieDataset);
			}
		}

		return fillPieDataset;
	}


	/**
	 *
	 */
	public JRPiePlot getPiePlot(JRPiePlot piePlot)
	{
		JRFillPiePlot fillPiePlot = null;

		if (piePlot != null)
		{
			fillPiePlot = (JRFillPiePlot)get(piePlot);
			if (fillPiePlot == null)
			{
				fillPiePlot = new JRFillPiePlot(piePlot, this);
			}
		}

		return fillPiePlot;
	}


	/**
	 *
	 *
	public JRPie3DChart getPie3DChart(JRPie3DChart pie3DChart)
	{
		JRFillPie3DChart fillPie3DChart = null;

		if (pie3DChart != null)
		{
			fillPie3DChart = (JRFillPie3DChart)get(pie3DChart);
			if (fillPie3DChart == null)
			{
				fillPie3DChart = new JRFillPie3DChart(filler, pie3DChart, this);
			}
		}

		return fillPie3DChart;
	}


	/**
	 *
	 */
	public JRPie3DPlot getPie3DPlot(JRPie3DPlot pie3DPlot)
	{
		JRFillPie3DPlot fillPie3DPlot = null;

		if (pie3DPlot != null)
		{
			fillPie3DPlot = (JRFillPie3DPlot)get(pie3DPlot);
			if (fillPie3DPlot == null)
			{
				fillPie3DPlot = new JRFillPie3DPlot(pie3DPlot, this);
			}
		}

		return fillPie3DPlot;
	}


	/**
	 *
	 *
	public JRBarChart getBarChart(JRBarChart barChart)
	{
		JRFillBarChart fillBarChart = null;

		if (barChart != null)
		{
			fillBarChart = (JRFillBarChart)get(barChart);
			if (fillBarChart == null)
			{
				fillBarChart = new JRFillBarChart(filler, barChart, this);
			}
		}

		return fillBarChart;
	}


	/**
	 *
	 *
	public JRStackedBarChart getStackedBarChart(JRStackedBarChart stackedBarChart)
	{
		JRFillStackedBarChart fillBarChart = null;

		if (stackedBarChart != null)
		{
			fillBarChart = (JRFillStackedBarChart)get(stackedBarChart);
			if (fillBarChart == null)
			{
				fillBarChart = new JRFillStackedBarChart(filler, stackedBarChart, this);
			}
		}

		return fillBarChart;
	}


	/**
	 *
	 *
	public JRStackedBar3DChart getStackedBar3DChart(JRStackedBar3DChart stackedBar3DChart)
	{
		JRFillStackedBar3DChart fillBar3DChart = null;

		if (stackedBar3DChart != null)
		{
			fillBar3DChart = (JRFillStackedBar3DChart)get(stackedBar3DChart);
			if (fillBar3DChart == null)
			{
				fillBar3DChart = new JRFillStackedBar3DChart(filler, stackedBar3DChart, this);
			}
		}

		return fillBar3DChart;
	}


	/**
	 *
	 */
	public JRCategoryDataset getCategoryDataset(JRCategoryDataset categoryDataset)
	{
		JRFillCategoryDataset fillCategoryDataset = null;

		if (categoryDataset != null)
		{
			fillCategoryDataset = (JRFillCategoryDataset)get(categoryDataset);
			if (fillCategoryDataset == null)
			{
				fillCategoryDataset = new JRFillCategoryDataset(categoryDataset, this);
				datasets.add(fillCategoryDataset);
			}
		}

		return fillCategoryDataset;
	}

	public JRXyzDataset getXyzDataset( JRXyzDataset xyzDataset ){
		JRFillXyzDataset fillXyzDataset = null;
		if( xyzDataset != null ){
			fillXyzDataset = (JRFillXyzDataset)get( xyzDataset );
			if( fillXyzDataset == null ){
				fillXyzDataset = new JRFillXyzDataset( xyzDataset, this );
				datasets.add( fillXyzDataset );
			}
		}

		return fillXyzDataset;

	}


	/**
	 *
	 */
	public JRXyDataset getXyDataset(JRXyDataset xyDataset)
	{
		JRFillXyDataset fillXyDataset = null;

		if (xyDataset != null)
		{
			fillXyDataset = (JRFillXyDataset)get(xyDataset);
			if (fillXyDataset == null)
			{
				fillXyDataset = new JRFillXyDataset(xyDataset, this);
				datasets.add(fillXyDataset);
			}
		}

		return fillXyDataset;
	}


	/**
	 *
	 */
	public JRIntervalXyDataset getIntervalXyDataset(JRIntervalXyDataset intervalXyDataset)
	{
		JRFillIntervalXyDataset fillIntervalXyDataset = null;

		if (intervalXyDataset != null)
		{
			fillIntervalXyDataset = (JRFillIntervalXyDataset)get(intervalXyDataset);
			if (fillIntervalXyDataset == null)
			{
				fillIntervalXyDataset = new JRFillIntervalXyDataset(intervalXyDataset, this);
				datasets.add(fillIntervalXyDataset);
			}
		}

		return fillIntervalXyDataset;
	}


	/**
	 *
	 */
	public JRCategorySeries getCategorySeries(JRCategorySeries categorySeries)
	{
		JRFillCategorySeries fillCategorySeries = null;

		if (categorySeries != null)
		{
			fillCategorySeries = (JRFillCategorySeries)get(categorySeries);
			if (fillCategorySeries == null)
			{
				fillCategorySeries = new JRFillCategorySeries(categorySeries, this);
			}
		}

		return fillCategorySeries;
	}


	public JRXyzSeries getXyzSeries( JRXyzSeries xyzSeries ){
		JRFillXyzSeries fillXyzSeries = null;

		if( xyzSeries != null ){
			fillXyzSeries = (JRFillXyzSeries)get( xyzSeries );

			if( fillXyzSeries == null ){
				fillXyzSeries = new JRFillXyzSeries( xyzSeries, this );
			}
		}

		return fillXyzSeries;
	}


	/**
	 *
	 */
	public JRXySeries getXySeries(JRXySeries xySeries)
	{
		JRFillXySeries fillXySeries = null;

		if (xySeries != null)
		{
			fillXySeries = (JRFillXySeries)get(xySeries);
			if (fillXySeries == null)
			{
				fillXySeries = new JRFillXySeries(xySeries, this);
			}
		}

		return fillXySeries;
	}


	/**
	 *
	 */
	public JRBarPlot getBarPlot(JRBarPlot barPlot)
	{
		JRFillBarPlot fillBarPlot = null;

		if (barPlot != null)
		{
			fillBarPlot = (JRFillBarPlot)get(barPlot);
			if (fillBarPlot == null)
			{
				fillBarPlot = new JRFillBarPlot(barPlot, this);
			}
		}

		return fillBarPlot;
	}


	/**
	 *
	 *
	public JRXyBarChart getXyBarChart(JRXyBarChart xyBarChart)
	{
		JRFillXyBarChart fillXyBarChart = null;

		if (xyBarChart != null)
		{
			fillXyBarChart = (JRFillXyBarChart)get(xyBarChart);
			if (fillXyBarChart == null)
			{
				fillXyBarChart = new JRFillXyBarChart(filler, xyBarChart, this);
			}
		}

		return fillXyBarChart;
	}


	/**
	 *
	 */
	public JRTimeSeries getTimeSeries(JRTimeSeries timeSeries)
	{
		JRFillTimeSeries fillTimeSeries = null;

		if (timeSeries != null)
		{
			fillTimeSeries = (JRFillTimeSeries)get(timeSeries);
			if (fillTimeSeries == null)
			{
				fillTimeSeries = new JRFillTimeSeries(timeSeries, this);
			}
		}

		return fillTimeSeries;
	}


	/**
	 *
	 *
	public JRBar3DChart getBar3DChart(JRBar3DChart barChart) {

		JRFillBar3DChart fillBarChart = null;

		if (barChart != null){
			fillBarChart = (JRFillBar3DChart)get(barChart);
			if (fillBarChart == null){
				fillBarChart = new JRFillBar3DChart(filler, barChart, this);
			}
		}

		return fillBarChart;
	}


	/**
	 *
	 */
	public JRBar3DPlot getBar3DPlot(JRBar3DPlot barPlot) {
		JRFillBar3DPlot fillBarPlot = null;

		if (barPlot != null){
			fillBarPlot = (JRFillBar3DPlot)get(barPlot);
			if (fillBarPlot == null){
				fillBarPlot = new JRFillBar3DPlot(barPlot, this);
			}
		}

		return fillBarPlot;
	}


	/**
	 *
	 *
	public JRLineChart getLineChart(JRLineChart lineChart) {
		JRFillLineChart fillLineChart = null;

		if (lineChart != null){
			fillLineChart = (JRFillLineChart)get(lineChart);
			if (fillLineChart == null){
				fillLineChart = new JRFillLineChart(filler, lineChart, this);
			}
		}

		return fillLineChart;
	}


	/**
	 *
	 *
	public JRScatterChart getScatterChart(JRScatterChart scatterChart) {
		JRFillScatterChart fillLineChart = null;

		if (scatterChart != null){
			fillLineChart = (JRFillScatterChart)get(scatterChart);
			if (fillLineChart == null){
				fillLineChart = new JRFillScatterChart(filler, scatterChart, this);
			}
		}

		return fillLineChart;
	}


	/**
	 *
	 *
	public JRXyLineChart getXyLineChart(JRXyLineChart xyLineChart) {
		JRFillXyLineChart fillXyLineChart = null;

		if (xyLineChart != null){
			fillXyLineChart = (JRFillXyLineChart)get(xyLineChart);
			if (fillXyLineChart == null){
				fillXyLineChart = new JRFillXyLineChart(filler, xyLineChart, this);
			}
		}

		return fillXyLineChart;
	}


	/**
	 *
	 */
	public JRLinePlot getLinePlot(JRLinePlot linePlot) {
		JRFillLinePlot fillLinePlot = null;

		if (linePlot != null){
			fillLinePlot = (JRFillLinePlot)get(linePlot);
			if (fillLinePlot == null){
				fillLinePlot = new JRFillLinePlot(linePlot, this);
			}
		}

		return fillLinePlot;
	}


	/**
	 *
	 */
	public JRScatterPlot getScatterPlot(JRScatterPlot scatterPlot) {
		JRFillScatterPlot fillScatterPlot = null;

		if (scatterPlot != null){
			fillScatterPlot = (JRFillScatterPlot)get(scatterPlot);
			if (fillScatterPlot == null){
				fillScatterPlot = new JRFillScatterPlot(scatterPlot, this);
			}
		}

		return fillScatterPlot;
	}


	/**
	 *
	 *
	public JRAreaChart getAreaChart(JRAreaChart areaChart) {
		JRFillAreaChart fillAreaChart = null;

		if (areaChart != null)
		{
			fillAreaChart = (JRFillAreaChart)get(areaChart);
			if (fillAreaChart == null)
			{
				fillAreaChart = new JRFillAreaChart(filler, areaChart, this);
			}
		}

		return fillAreaChart;
	}


	/**
	 *
	 *
	public JRXyAreaChart getXyAreaChart(JRXyAreaChart xyAreaChart) {
		JRFillXyAreaChart fillXyAreaChart = null;

		if (xyAreaChart != null)
		{
			fillXyAreaChart = (JRFillXyAreaChart)get(xyAreaChart);
			if (fillXyAreaChart == null)
			{
				fillXyAreaChart = new JRFillXyAreaChart(filler, xyAreaChart, this);
			}
		}

		return fillXyAreaChart;
	}


	/**
	 *
	 */
	public JRAreaPlot getAreaPlot(JRAreaPlot areaPlot) {
		JRFillAreaPlot fillAreaPlot = null;

		if (areaPlot != null)
		{
			fillAreaPlot = (JRFillAreaPlot)get(areaPlot);
			if (fillAreaPlot == null)
			{
				fillAreaPlot = new JRFillAreaPlot(areaPlot, this);
			}
		}

		return fillAreaPlot;
	}


	/**
	 *
	 *
	public JRBubbleChart getBubbleChart(JRBubbleChart bubbleChart) {
		JRFillBubbleChart fillBubbleChart = null;

		if (bubbleChart != null)
		{
			fillBubbleChart = (JRFillBubbleChart)get(bubbleChart);
			if (fillBubbleChart == null)
			{
				fillBubbleChart = new JRFillBubbleChart(filler, bubbleChart, this);
			}
		}

		return fillBubbleChart;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRAbstractObjectFactory#getBubblePlot(net.sf.jasperreports.charts.JRBubblePlot)
	 */
	public JRBubblePlot getBubblePlot(JRBubblePlot bubblePlot) {
		JRFillBubblePlot fillBubblePlot = null;

		if (bubblePlot != null)
		{
			fillBubblePlot = (JRFillBubblePlot)get(bubblePlot);
			if (fillBubblePlot == null)
			{
				fillBubblePlot = new JRFillBubblePlot(bubblePlot, this);
			}
		}

		return fillBubblePlot;
	}


	/**
	 *
	 *
	public JRHighLowChart getHighLowChart(JRHighLowChart highLowChart)
	{
		JRFillHighLowChart fillHighLowChart = null;

		if (highLowChart != null){
			fillHighLowChart = (JRFillHighLowChart)get(highLowChart);
			if (fillHighLowChart == null){
				fillHighLowChart = new JRFillHighLowChart(filler, highLowChart, this);
			}
		}

		return fillHighLowChart;
	}


	public JRCandlestickChart getCandlestickChart(JRCandlestickChart candlestickChart)
	{
		JRFillCandlestickChart fillCandlestickChart = null;

		if (candlestickChart != null){
			fillCandlestickChart = (JRFillCandlestickChart)get(candlestickChart);
			if (fillCandlestickChart == null){
				fillCandlestickChart = new JRFillCandlestickChart(filler, candlestickChart, this);
			}
		}

		return fillCandlestickChart;
	}


	/**
	 *
	 */
	public JRHighLowDataset getHighLowDataset(JRHighLowDataset highLowDataset)
	{
		JRFillHighLowDataset fillHighLowDataset = null;

		if (highLowDataset != null)
		{
			fillHighLowDataset = (JRFillHighLowDataset)get(highLowDataset);
			if (fillHighLowDataset == null)
			{
				fillHighLowDataset = new JRFillHighLowDataset(highLowDataset, this);
				datasets.add(fillHighLowDataset);
			}
		}

		return fillHighLowDataset;
	}


	/**
	 *
	 */
	public JRHighLowPlot getHighLowPlot(JRHighLowPlot highLowPlot) {
		JRFillHighLowPlot fillHighLowPlot = null;

		if (highLowPlot != null){
			fillHighLowPlot = (JRFillHighLowPlot)get(highLowPlot);
			if (fillHighLowPlot == null){
				fillHighLowPlot = new JRFillHighLowPlot(highLowPlot, this);
			}
		}

		return fillHighLowPlot;
	}


	public JRCandlestickPlot getCandlestickPlot(JRCandlestickPlot candlestickPlot)
	{
		JRFillCandlestickPlot fillCandlestickPlot = null;

		if (candlestickPlot != null){
			fillCandlestickPlot = (JRFillCandlestickPlot)get(candlestickPlot);
			if (fillCandlestickPlot == null){
				fillCandlestickPlot = new JRFillCandlestickPlot(candlestickPlot, this);
			}
		}

		return fillCandlestickPlot;
	}
}
