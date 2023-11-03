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

import org.apache.commons.digester.Digester;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRChartPlot;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.charts.design.JRDesignItemLabel;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodSeries;
import net.sf.jasperreports.charts.design.JRDesignTimeSeries;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;
import net.sf.jasperreports.charts.design.JRDesignXySeries;
import net.sf.jasperreports.charts.design.JRDesignXyzSeries;
import net.sf.jasperreports.charts.util.JRAxisFormat;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.charts.xml.JRChartFactory.JRCategoryAxisFormatFactory;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRHyperlinkFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class ChartsXmlDigesterConfigurer implements XmlDigesterConfigurer
{
	
	@Override
	public void configureDigester(Digester digester)
	{
		addChartRules(digester);
	}


	/**
	 *
	 */
	private static void addChartRules(Digester digester)
	{
		digester.addFactoryCreate("*/plot", JRChartPlotFactory.class.getName());
		digester.addFactoryCreate("*/plot/seriesColor", JRChartPlotFactory.JRSeriesColorFactory.class.getName());
		digester.addSetNext("*/plot/seriesColor", "addSeriesColor", JRChartPlot.JRSeriesColor.class.getName());

		digester.addFactoryCreate("*/chart", JRChartFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle", JRChartFactory.JRChartTitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/chart/chartTitle/font", "setTitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/titleExpression", JRExpressionFactory.class);
		digester.addSetNext("*/chart/chartTitle/titleExpression", "setTitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartTitle/titleExpression", "setText", 0);
		digester.addFactoryCreate("*/chart/chartSubtitle", JRChartFactory.JRChartSubtitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/chart/chartSubtitle/font", "setSubtitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/subtitleExpression", JRExpressionFactory.class);
		digester.addSetNext("*/chart/chartSubtitle/subtitleExpression", "setSubtitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartSubtitle/subtitleExpression", "setText", 0);
		digester.addFactoryCreate("*/chart/chartLegend", JRChartFactory.JRChartLegendFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartLegend/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/chart/chartLegend/font", "setLegendFont", JRFont.class.getName());

		// axis labels

		digester.addFactoryCreate("*/categoryAxisFormat", JRCategoryAxisFormatFactory.class.getName());

		digester.addFactoryCreate("*/categoryAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/categoryAxisFormat/axisFormat", "setCategoryAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/categoryAxisFormat/axisFormat/labelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/categoryAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/categoryAxisFormat/axisFormat/tickLabelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/categoryAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/valueAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/valueAxisFormat/axisFormat", "setValueAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/valueAxisFormat/axisFormat/labelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/valueAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/valueAxisFormat/axisFormat/tickLabelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/valueAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/timeAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/timeAxisFormat/axisFormat", "setTimeAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/timeAxisFormat/axisFormat/labelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/timeAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/timeAxisFormat/axisFormat/tickLabelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/timeAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/xAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/xAxisFormat/axisFormat", "setXAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/xAxisFormat/axisFormat/labelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/xAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/xAxisFormat/axisFormat/tickLabelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/xAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/yAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/yAxisFormat/axisFormat", "setYAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/yAxisFormat/axisFormat/labelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/yAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/yAxisFormat/axisFormat/tickLabelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/yAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		// item labels
		digester.addFactoryCreate("*/itemLabel", JRItemLabelFactory.class.getName());
		digester.addSetNext("*/itemLabel", "setItemLabel", JRDesignItemLabel.class.getName());
		digester.addFactoryCreate("*/itemLabel/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/itemLabel/font", "setFont", JRFont.class.getName());

		// pie charts
		digester.addFactoryCreate("*/pieChart", JRPieChartFactory.class.getName());
		digester.addSetNext("*/pieChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/pieChart/piePlot", JRPiePlotFactory.class.getName());

		digester.addFactoryCreate("*/pieDataset", JRPieDatasetFactory.class.getName());
		digester.addFactoryCreate("*/pieDataset/pieSeries", JRPieSeriesFactory.class.getName());
		digester.addSetNext("*/pieDataset/pieSeries", "addPieSeries", JRDesignPieSeries.class.getName());

		digester.addFactoryCreate("*/pieSeries/keyExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieSeries/keyExpression", "setKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieSeries/keyExpression", "setText", 0);
		digester.addFactoryCreate("*/pieSeries/labelExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieSeries/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/pieSeries/valueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieSeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieSeries/valueExpression", "setText", 0);
		digester.addFactoryCreate("*/pieSeries/sectionHyperlink", JRHyperlinkFactory.class);
		digester.addSetNext("*/pieSeries/sectionHyperlink", "setSectionHyperlink", JRHyperlink.class.getName());

		digester.addFactoryCreate("*/pieDataset/keyExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieDataset/keyExpression", "setKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/keyExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/labelExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieDataset/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/valueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieDataset/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/valueExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/sectionHyperlink", JRHyperlinkFactory.class);
		digester.addSetNext("*/pieDataset/sectionHyperlink", "setSectionHyperlink", JRHyperlink.class.getName());

		digester.addFactoryCreate("*/pieDataset/otherKeyExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieDataset/otherKeyExpression", "setOtherKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/otherKeyExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/otherLabelExpression", JRExpressionFactory.class);
		digester.addSetNext("*/pieDataset/otherLabelExpression", "setOtherLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/otherLabelExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/otherSectionHyperlink", JRHyperlinkFactory.class);
		digester.addSetNext("*/pieDataset/otherSectionHyperlink", "setOtherSectionHyperlink", JRHyperlink.class.getName());

		// pie 3D charts
		digester.addFactoryCreate("*/pie3DChart", JRPie3DChartFactory.class.getName());
		digester.addSetNext("*/pie3DChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/pie3DPlot", JRPie3DPlotFactory.class.getName());

		// bar charts
		digester.addFactoryCreate("*/barChart", JRBarChartFactory.class.getName());
		digester.addSetNext("*/barChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/barChart/barPlot", JRBarPlotFactory.class.getName());

		digester.addFactoryCreate( "*/barPlot/categoryAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/barPlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/categoryAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/valueAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/barPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/barPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/barPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/barPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/barPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/rangeAxisMaxValueExpression", "setText", 0 );


		// area charts
		digester.addFactoryCreate( "*/areaChart", JRAreaChartFactory.class.getName() );
		digester.addSetNext( "*/areaChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/areaChart/areaPlot", JRAreaPlotFactory.class.getName() );

		digester.addFactoryCreate( "*/xyAreaChart", JRXyAreaChartFactory.class.getName() );
		digester.addSetNext( "*/xyAreaChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/xyAreaChart/areaPlot", JRAreaPlotFactory.class.getName() );

		digester.addFactoryCreate( "*/areaPlot/categoryAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/categoryAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/valueAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/rangeAxisMaxValueExpression", "setText", 0 );

		// bar3d charts
		digester.addFactoryCreate( "*/bar3DChart", JRBar3DChartFactory.class.getName() );
		digester.addSetNext( "*/bar3DChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate("*/bar3DChart/bar3DPlot", JRBar3DPlotFactory.class.getName());

		digester.addFactoryCreate( "*/bar3DPlot/categoryAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/categoryAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/valueAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/rangeAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate("*/categoryDataset", JRCategoryDatasetFactory.class.getName());
		digester.addFactoryCreate("*/categoryDataset/categorySeries", JRCategorySeriesFactory.class.getName());
		digester.addSetNext("*/categoryDataset/categorySeries", "addCategorySeries", JRDesignCategorySeries.class.getName());

		//digester.addFactoryCreate("*/categorySeries", JRCategoryDatasetFactory.class.getName());
		digester.addFactoryCreate("*/categorySeries/seriesExpression", JRExpressionFactory.class);
		digester.addSetNext("*/categorySeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/categorySeries/categoryExpression", JRExpressionFactory.class);
		digester.addSetNext("*/categorySeries/categoryExpression", "setCategoryExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/categoryExpression", "setText", 0);
		digester.addFactoryCreate("*/categorySeries/labelExpression", JRExpressionFactory.class);
		digester.addSetNext("*/categorySeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/categorySeries/valueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/categorySeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/valueExpression", "setText", 0);

		String itemHyperlinkPattern = "*/" + JRXmlConstants.ELEMENT_itemHyperlink;
		digester.addFactoryCreate(itemHyperlinkPattern, JRHyperlinkFactory.class);
		digester.addSetNext(itemHyperlinkPattern, "setItemHyperlink", JRHyperlink.class.getName());

		digester.addFactoryCreate( "*/xyzDataset", JRXyzDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/xyzDataset/xyzSeries", JRXyzSeriesFactory.class.getName() );
		digester.addSetNext( "*/xyzDataset/xyzSeries", "addXyzSeries", JRDesignXyzSeries.class.getName() );

		digester.addFactoryCreate( "*/xyzSeries/seriesExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/seriesExpression", "setText", 0 );
		digester.addFactoryCreate( "*/xyzSeries", JRXyzDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/xyzSeries/xValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/xValueExpression", "setXValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/xValueExpression", "setText", 0 );
		digester.addFactoryCreate( "*/xyzSeries/yValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/yValueExpression", "setYValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/yValueExpression", "setText", 0 );
		digester.addFactoryCreate( "*/xyzSeries/zValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/zValueExpression", "setZValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/zValueExpression", "setText", 0 );


		// time period dataset
		digester.addFactoryCreate( "*/timePeriodDataset", JRTimePeriodDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/timePeriodDataset/timePeriodSeries", JRTimePeriodSeriesFactory.class.getName() );
		digester.addSetNext( "*/timePeriodDataset/timePeriodSeries", "addTimePeriodSeries", JRDesignTimePeriodSeries.class.getName() );

		digester.addFactoryCreate("*/timePeriodSeries/seriesExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/timePeriodSeries/startDateExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/startDateExpression", "setStartDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/startDateExpression", "setText", 0);
		digester.addFactoryCreate("*/timePeriodSeries/endDateExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/endDateExpression", "setEndDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/endDateExpression", "setText", 0);
		digester.addFactoryCreate("*/timePeriodSeries/valueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/valueExpression", "setText", 0);
		digester.addFactoryCreate( "*/timePeriodSeries/labelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/timePeriodSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timePeriodSeries/labelExpression", "setText", 0);

		digester.addFactoryCreate("*/timeSeriesChart", JRTimeSeriesChartFactory.class.getName());
		digester.addFactoryCreate("*/timeSeriesChart/timeSeriesPlot", JRTimeSeriesPlotFactory.class.getName());
		digester.addSetNext("*/timeSeriesChart", "addElement", JRDesignElement.class.getName());

		// add plot labels
		digester.addFactoryCreate( "*/timeSeriesPlot/timeAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext("*/timeSeriesPlot/timeAxisLabelExpression", "setTimeAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/timeSeriesPlot/timeAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/valueAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/timeSeriesPlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timeSeriesPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timeSeriesPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timeSeriesPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timeSeriesPlot/rangeAxisMaxValueExpression", "setText", 0 );

		// XY bar charts
		digester.addFactoryCreate("*/xyBarChart", JRXyBarChartFactory.class.getName());
		digester.addSetNext("*/xyBarChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/xyBarChart/barPlot", JRBarPlotFactory.class.getName());


//		 time series dataset
		digester.addFactoryCreate( "*/timeSeriesDataset", JRTimeSeriesDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/timeSeriesDataset/timeSeries", JRTimeSeriesFactory.class.getName());
		digester.addSetNext( "*/timeSeriesDataset/timeSeries", "addTimeSeries", JRDesignTimeSeries.class.getName() );

		digester.addFactoryCreate("*/timeSeries/seriesExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timeSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/timeSeries/timePeriodExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timeSeries/timePeriodExpression", "setTimePeriodExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/timePeriodExpression", "setText", 0);
		digester.addFactoryCreate("*/timeSeries/labelExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timeSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/timeSeries/valueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/timeSeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/valueExpression", "setText", 0);

		digester.addFactoryCreate("*/stackedBarChart", JRStackedBarChartFactory.class.getName());
		digester.addSetNext("*/stackedBarChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/stackedBarChart/barPlot", JRBarPlotFactory.class.getName());

		digester.addFactoryCreate( "*/lineChart", JRLineChartFactory.class.getName() );
		digester.addSetNext( "*/lineChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/linePlot", JRLinePlotFactory.class.getName() );


		//add plot labels
		digester.addFactoryCreate( "*/linePlot/categoryAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext("*/linePlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/linePlot/categoryAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/valueAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext("*/linePlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/linePlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/linePlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/linePlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/linePlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/linePlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/rangeAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/xyLineChart", JRXyLineChartFactory.class.getName() );
		digester.addSetNext( "*/xyLineChart", "addElement", JRDesignElement.class.getName() );

		digester.addFactoryCreate( "*/scatterChart", JRScatterChartFactory.class.getName() );
		digester.addSetNext( "*/scatterChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/scatterPlot", JRScatterPlotFactory.class.getName() );

		// add plot labels
		digester.addFactoryCreate( "*/scatterPlot/xAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/xAxisLabelExpression", "setXAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/xAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/yAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/yAxisLabelExpression", "setYAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/yAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/rangeAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate("*/xyDataset", JRXyDatasetFactory.class.getName());
		digester.addFactoryCreate("*/xyDataset/xySeries", JRXySeriesFactory.class.getName());
		digester.addSetNext("*/xyDataset/xySeries", "addXySeries", JRDesignXySeries.class.getName());

		digester.addFactoryCreate("*/xySeries", JRXyDatasetFactory.class.getName());
		digester.addFactoryCreate("*/xySeries/seriesExpression", JRExpressionFactory.class);
		digester.addSetNext("*/xySeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/xySeries/xValueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/xySeries/xValueExpression", "setXValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/xValueExpression", "setText", 0);
		digester.addFactoryCreate("*/xySeries/yValueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/xySeries/yValueExpression", "setYValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/yValueExpression", "setText", 0);
		digester.addFactoryCreate("*/xySeries/labelExpression", JRExpressionFactory.class);
		digester.addSetNext("*/xySeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/labelExpression", "setText", 0);

		digester.addFactoryCreate("*/stackedBar3DChart", JRStackedBar3DChartFactory.class.getName());
		digester.addSetNext("*/stackedBar3DChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/stackedBar3DChart/bar3DPlot", JRBar3DPlotFactory.class.getName());

		digester.addFactoryCreate( "*/bubbleChart", JRBubbleChartFactory.class.getName() );
		digester.addSetNext( "*/bubbleChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/bubblePlot", JRBubblePlotFactory.class.getName() );

		// add plot labels
		digester.addFactoryCreate( "*/bubblePlot/xAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/xAxisLabelExpression", "setXAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/xAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/yAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/yAxisLabelExpression", "setYAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/yAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/rangeAxisMaxValueExpression", "setText", 0 );

		// high-low charts
		digester.addFactoryCreate("*/highLowChart", JRHighLowChartFactory.class.getName());
		digester.addSetNext("*/highLowChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/highLowChart/highLowPlot", JRHighLowPlotFactory.class.getName());

		digester.addFactoryCreate( "*/highLowPlot/timeAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/timeAxisLabelExpression", "setTimeAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/timeAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/valueAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/rangeAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate("*/highLowDataset", JRHighLowDatasetFactory.class.getName());
		digester.addFactoryCreate("*/highLowDataset/seriesExpression", JRExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/dateExpression", JRExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/dateExpression", "setDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/dateExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/highExpression", JRExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/highExpression", "setHighExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/highExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/lowExpression", JRExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/lowExpression", "setLowExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/lowExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/openExpression", JRExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/openExpression", "setOpenExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/openExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/closeExpression", JRExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/closeExpression", "setCloseExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/closeExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/volumeExpression", JRExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/volumeExpression", "setVolumeExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/volumeExpression", "setText", 0);

		// candlestick charts
		digester.addFactoryCreate("*/candlestickChart", JRCandlestickChartFactory.class);
		digester.addSetNext("*/candlestickChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/candlestickChart/candlestickPlot", JRCandlestickPlotFactory.class);

		digester.addFactoryCreate( "*/candlestickPlot/timeAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/timeAxisLabelExpression", "setTimeAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/timeAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/valueAxisLabelExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/domainAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/domainAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/rangeAxisMinValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/rangeAxisMaxValueExpression", JRExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/rangeAxisMaxValueExpression", "setText", 0 );

		// value datasets
		digester.addFactoryCreate("*/valueDataset", JRValueDatasetFactory.class.getName());
		digester.addFactoryCreate("*/valueDataset/valueExpression", JRExpressionFactory.class);
		digester.addSetNext("*/valueDataset/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/valueDataset/valueExpression", "setText", 0);

		// data ranges - anything that contains a data range must have a "setDataRange" method.
		digester.addFactoryCreate("*/dataRange", JRDataRangeFactory.class.getName());
		digester.addSetNext("*/dataRange", "setDataRange", JRDesignDataRange.class.getName());
		digester.addFactoryCreate("*/dataRange/lowExpression", JRExpressionFactory.class);
		digester.addSetNext("*/dataRange/lowExpression", "setLowExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/dataRange/lowExpression", "setText", 0);
		digester.addFactoryCreate("*/dataRange/highExpression", JRExpressionFactory.class);
		digester.addSetNext("*/dataRange/highExpression", "setHighExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/dataRange/highExpression", "setText", 0);

		// value displays - anything that contains a display value must have a "setValueDisplay" method.
		digester.addFactoryCreate("*/valueDisplay", JRValueDisplayFactory.class.getName());
		digester.addSetNext("*/valueDisplay", "setValueDisplay", JRDesignValueDisplay.class.getName());
		digester.addFactoryCreate("*/valueDisplay/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/valueDisplay/font", "setFont", JRFont.class.getName());

		// meter charts
		digester.addFactoryCreate("*/meterChart", JRMeterChartFactory.class.getName());
		digester.addSetNext("*/meterChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/meterChart/meterPlot", JRMeterPlotFactory.class.getName());
		digester.addFactoryCreate("*/meterPlot/tickLabelFont/font", ChartFontFactory.class.getName());
		digester.addSetNext("*/meterPlot/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/meterPlot/lowExpression", JRExpressionFactory.class);
		digester.addSetNext("*/meterPlot/lowExpression", "setLowExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/meterPlot/lowExpression", "setText", 0);
		digester.addFactoryCreate("*/meterPlot/highExpression", JRExpressionFactory.class);
		digester.addSetNext("*/meterPlot/highExpression", "setHighExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/meterPlot/highExpression", "setText", 0);

		digester.addFactoryCreate("*/meterPlot/meterInterval", JRMeterIntervalFactory.class.getName());
		digester.addSetNext("*/meterPlot/meterInterval", "addInterval", JRMeterInterval.class.getName());

		// thermometer charts
		digester.addFactoryCreate("*/thermometerChart", JRThermometerChartFactory.class.getName());
		digester.addSetNext("*/thermometerChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/thermometerChart/thermometerPlot", JRThermometerPlotFactory.class.getName());

		digester.addFactoryCreate("*/thermometerPlot/lowRange/dataRange", JRDataRangeFactory.class.getName());
		digester.addSetNext("*/thermometerPlot/lowRange/dataRange", "setLowRange", JRDesignDataRange.class.getName());
		digester.addFactoryCreate("*/thermometerPlot/mediumRange/dataRange", JRDataRangeFactory.class.getName());
		digester.addSetNext("*/thermometerPlot/mediumRange/dataRange", "setMediumRange", JRDesignDataRange.class.getName());
		digester.addFactoryCreate("*/thermometerPlot/highRange/dataRange", JRDataRangeFactory.class.getName());
		digester.addSetNext("*/thermometerPlot/highRange/dataRange", "setHighRange", JRDesignDataRange.class.getName());

		//multi axis charts
		digester.addFactoryCreate("*/multiAxisChart", JRMultiAxisChartFactory.class.getName());
		digester.addSetNext("*/multiAxisChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/multiAxisChart/multiAxisPlot", JRMultiAxisPlotFactory.class.getName());
		digester.addFactoryCreate("*/axis", JRChartAxisFactory.class.getName());
		digester.addSetNext("*/axis", "addAxis", JRChartAxis.class.getName());

		digester.addFactoryCreate("*/stackedAreaChart", JRStackedAreaChartFactory.class.getName());
		digester.addSetNext("*/stackedAreaChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/stackedAreaChart/areaPlot", JRAreaPlotFactory.class.getName());

		// gantt charts 
		digester.addFactoryCreate("*/ganttChart", JRGanttChartFactory.class.getName());
		digester.addSetNext("*/ganttChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/ganttChart/barPlot", JRBarPlotFactory.class.getName());

		digester.addFactoryCreate("*/ganttDataset", JRGanttDatasetFactory.class.getName());
		digester.addFactoryCreate("*/ganttDataset/ganttSeries", JRGanttSeriesFactory.class.getName());
		digester.addSetNext("*/ganttDataset/ganttSeries", "addGanttSeries", JRDesignGanttSeries.class.getName());
		digester.addFactoryCreate("*/ganttSeries", JRGanttDatasetFactory.class.getName());
		digester.addFactoryCreate("*/ganttSeries/seriesExpression", JRExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/taskExpression", JRExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/taskExpression", "setTaskExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/taskExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/subtaskExpression", JRExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/subtaskExpression", "setSubtaskExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/subtaskExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/startDateExpression", JRExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/startDateExpression", "setStartDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/startDateExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/endDateExpression", JRExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/endDateExpression", "setEndDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/endDateExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/percentExpression", JRExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/percentExpression", "setPercentExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/percentExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/labelExpression", JRExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/labelExpression", "setText", 0);
	}

}
