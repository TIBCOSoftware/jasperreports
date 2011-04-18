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
package net.sf.jasperreports.engine.xml;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.type.AxisPositionEnum;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.charts.type.MeterShapeEnum;
import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.charts.type.ScaleTypeEnum;
import net.sf.jasperreports.charts.type.TimePeriodEnum;
import net.sf.jasperreports.charts.type.ValueLocationEnum;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabPercentageEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.BreakTypeEnum;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.ColorEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.FooterPositionEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.LineSpacingEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PenEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Second;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class JRXmlConstants
{
	/**
	 *
	 */
	public static final String JASPERREPORT_PUBLIC_ID = "-//JasperReports//DTD JasperReport//EN";//FIXME align with samples
	public static final String JASPERREPORT_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jasperreport.dtd";
	public static final String JASPERREPORT_DTD = "net/sf/jasperreports/engine/dtds/jasperreport.dtd";
	public static final String JASPERPRINT_PUBLIC_ID = "-//JasperReports//DTD JasperPrint//EN";
	public static final String JASPERPRINT_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jasperprint.dtd";
	public static final String JASPERPRINT_DTD = "net/sf/jasperreports/engine/dtds/jasperprint.dtd";
	
	/**
	 * The namespace used by the JRXML XML schema.
	 */
	public static final String JASPERREPORTS_NAMESPACE = 
		"http://jasperreports.sourceforge.net/jasperreports";
	
	/**
	 * The system location of the JRXML XML schema.
	 */
	public static final String JASPERREPORT_XSD_SYSTEM_ID = 
		"http://jasperreports.sourceforge.net/xsd/jasperreport.xsd";
	
	/**
	 * The internal location/resource name of the JRXML XML schema.
	 */
	public static final String JASPERREPORT_XSD_RESOURCE = 
		"net/sf/jasperreports/engine/dtds/jasperreport.xsd";
	
	/**
	 * The internal location/resource name of the JRXML DTD compatibility XML schema.
	 */
	public static final String JASPERREPORT_XSD_DTD_COMPAT_RESOURCE = 
		"net/sf/jasperreports/engine/dtds/jasperreport-dtd-compat.xsd";

	/**
	 * Template XML public ID.
	 */
	public static final String JASPERTEMPLATE_PUBLIC_ID = "-//JasperReports//DTD Template//EN";

	/**
	 * Template XML system ID.
	 */
	public static final String JASPERTEMPLATE_SYSTEM_ID = "http://jasperreports.sourceforge.net/dtds/jaspertemplate.dtd";

	/**
	 * DTD location for template XMLs.
	 */
	public static final String JASPERTEMPLATE_DTD = "net/sf/jasperreports/engine/dtds/jaspertemplate.dtd";

	/**
	 *	JasperDesignFactory associated constants
	 */
	public static final String ELEMENT_jasperReport = "jasperReport";
	public static final String ATTRIBUTE_name = "name";
	public static final String ATTRIBUTE_language = "language";
	public static final String ATTRIBUTE_columnCount = "columnCount";
	public static final String ATTRIBUTE_printOrder = "printOrder";
	public static final String ATTRIBUTE_columnDirection = "columnDirection";
	public static final String ATTRIBUTE_pageWidth = "pageWidth";
	public static final String ATTRIBUTE_pageHeight = "pageHeight";
	public static final String ATTRIBUTE_orientation = "orientation";
	public static final String ATTRIBUTE_whenNoDataType = "whenNoDataType";
	public static final String ATTRIBUTE_columnWidth = "columnWidth";
	public static final String ATTRIBUTE_columnSpacing = "columnSpacing";
	public static final String ATTRIBUTE_leftMargin = "leftMargin";
	public static final String ATTRIBUTE_rightMargin = "rightMargin";
	public static final String ATTRIBUTE_topMargin = "topMargin";
	public static final String ATTRIBUTE_bottomMargin = "bottomMargin";
	public static final String ATTRIBUTE_isTitleNewPage = "isTitleNewPage";
	public static final String ATTRIBUTE_isSummaryNewPage = "isSummaryNewPage";
	public static final String ATTRIBUTE_isSummaryWithPageHeaderAndFooter = "isSummaryWithPageHeaderAndFooter";
	public static final String ATTRIBUTE_isFloatColumnFooter = "isFloatColumnFooter";
	public static final String ATTRIBUTE_scriptletClass = "scriptletClass";
	public static final String ATTRIBUTE_formatFactoryClass = "formatFactoryClass";
	public static final String ATTRIBUTE_resourceBundle = "resourceBundle";
	public static final String ATTRIBUTE_whenResourceMissingType = "whenResourceMissingType";
	public static final String ATTRIBUTE_isIgnorePagination = "isIgnorePagination";

	public static final String ATTRIBUTE_value = "value";

	public static final String ELEMENT_import = "import";
	public static final String ELEMENT_background = "background";
	public static final String ELEMENT_title = "title";
	public static final String ELEMENT_pageHeader = "pageHeader";
	public static final String ELEMENT_columnHeader = "columnHeader";
	public static final String ELEMENT_detail = "detail";
	public static final String ELEMENT_columnFooter = "columnFooter";
	public static final String ELEMENT_pageFooter = "pageFooter";
	public static final String ELEMENT_lastPageFooter = "lastPageFooter";
	public static final String ELEMENT_summary = "summary";
	public static final String ELEMENT_noData = "noData";
	public static final String ELEMENT_property = "property";
	public static final String ELEMENT_propertyExpression = "propertyExpression";

	public static final String ELEMENT_page = "page";

	/**
	 * JasperPrintFactory associated constants
	 */
	public static final String ELEMENT_jasperPrint = "jasperPrint";
	public static final String ATTRIBUTE_locale = "locale";
	public static final String ATTRIBUTE_timezone = "timezone";

	/**
	 * JROriginFactory associated constants
	 */
	public static final String ELEMENT_origin = "origin";
	public static final String ATTRIBUTE_report = "report";
	public static final String ATTRIBUTE_group = "group";
	public static final String ATTRIBUTE_band = "band";

	/**
	 * JRBandFactory associated constants
	 */
	public static final String ELEMENT_band = "band";
	public static final String ELEMENT_printWhenExpression = "printWhenExpression";

	public static final String ATTRIBUTE_height = "height";
	public static final String ATTRIBUTE_isSplitAllowed = "isSplitAllowed";
	public static final String ATTRIBUTE_splitType = "splitType";

	/**
	 * JRPenFactory associated constants
	 */
	public static final String ELEMENT_pen = "pen";

	public static final String ATTRIBUTE_lineWidth = "lineWidth";
	public static final String ATTRIBUTE_lineStyle = "lineStyle";
	public static final String ATTRIBUTE_lineColor = "lineColor";

	/**
	 * JRBoxFactory associated constants
	 */
	public static final String ELEMENT_box = "box";
	public static final String ELEMENT_topPen = "topPen";
	public static final String ELEMENT_leftPen = "leftPen";
	public static final String ELEMENT_bottomPen = "bottomPen";
	public static final String ELEMENT_rightPen = "rightPen";

	public static final String ATTRIBUTE_border = "border";
	public static final String ATTRIBUTE_borderColor = "borderColor";
	public static final String ATTRIBUTE_padding = "padding";
	public static final String ATTRIBUTE_topBorder = "topBorder";
	public static final String ATTRIBUTE_topBorderColor = "topBorderColor";
	public static final String ATTRIBUTE_topPadding = "topPadding";
	public static final String ATTRIBUTE_leftBorder = "leftBorder";
	public static final String ATTRIBUTE_leftBorderColor = "leftBorderColor";
	public static final String ATTRIBUTE_leftPadding = "leftPadding";
	public static final String ATTRIBUTE_bottomBorder = "bottomBorder";
	public static final String ATTRIBUTE_bottomBorderColor = "bottomBorderColor";
	public static final String ATTRIBUTE_bottomPadding = "bottomPadding";
	public static final String ATTRIBUTE_rightBorder = "rightBorder";
	public static final String ATTRIBUTE_rightBorderColor = "rightBorderColor";
	public static final String ATTRIBUTE_rightPadding = "rightPadding";

	/**
	 * JRParagraphFactory associated constants
	 */
	public static final String ELEMENT_paragraph = "paragraph";
	public static final String ATTRIBUTE_lineSpacing = "lineSpacing";
	public static final String ATTRIBUTE_lineSpacingSize = "lineSpacingSize";
	public static final String ATTRIBUTE_firstLineIndent = "firstLineIndent";
	public static final String ATTRIBUTE_leftIndent = "leftIndent";
	public static final String ATTRIBUTE_rightIndent = "rightIndent";
	public static final String ATTRIBUTE_spacingBefore = "spacingBefore";
	public static final String ATTRIBUTE_spacingAfter = "spacingAfter";
	public static final String ATTRIBUTE_tabStopWidth = "tabStopWidth";
	public static final String ELEMENT_tabStop = "tabStop";
	public static final String ATTRIBUTE_alignment = "alignment";

	/**
	 * JRBreakFactory associated constants
	 */
	public static final String ELEMENT_break = "break";

	public static final String ATTRIBUTE_type = "type";

	/**
	 * JRChartFactory associated constants
	 */
	public static final String ELEMENT_chart = "chart";
	public static final String ELEMENT_chartTitle = "chartTitle";
	public static final String ELEMENT_titleExpression = "titleExpression";
	public static final String ELEMENT_chartSubtitle = "chartSubtitle";
	public static final String ELEMENT_subtitleExpression = "subtitleExpression";
	public static final String ELEMENT_chartLegend = "chartLegend";

	public static final String ELEMENT_pieChart = "pieChart";
	public static final String ELEMENT_pie3DChart = "pie3DChart";
	public static final String ELEMENT_barChart = "barChart";
	public static final String ELEMENT_bar3DChart = "bar3DChart";
	public static final String ELEMENT_bubbleChart = "bubbleChart";
	public static final String ELEMENT_stackedBarChart = "stackedBarChart";
	public static final String ELEMENT_stackedBar3DChart = "stackedBar3DChart";
	public static final String ELEMENT_lineChart = "lineChart";
	public static final String ELEMENT_highLowChart = "highLowChart";
	public static final String ELEMENT_candlestickChart = "candlestickChart";
	public static final String ELEMENT_areaChart = "areaChart";
	public static final String ELEMENT_scatterChart = "scatterChart";
	public static final String ELEMENT_timeSeriesChart = "timeSeriesChart";
	public static final String ELEMENT_xyAreaChart = "xyAreaChart";
	public static final String ELEMENT_xyBarChart = "xyBarChart";
	public static final String ELEMENT_xyLineChart = "xyLineChart";
	public static final String ELEMENT_meterChart = "meterChart";
	public static final String ELEMENT_thermometerChart = "thermometerChart";
	public static final String ELEMENT_multiAxisChart = "multiAxisChart";
	public static final String ELEMENT_stackedAreaChart = "stackedAreaChart";
	public static final String ELEMENT_ganttChart = "ganttChart";

	public static final String ATTRIBUTE_isShowLegend = "isShowLegend";
	public static final String ATTRIBUTE_evaluationTime = "evaluationTime";
	public static final String ATTRIBUTE_evaluationGroup = "evaluationGroup";
	public static final String ATTRIBUTE_bookmarkLevel = "bookmarkLevel";
	public static final String ATTRIBUTE_customizerClass = "customizerClass";
	public static final String ATTRIBUTE_renderType = "renderType";
	public static final String ATTRIBUTE_theme = "theme";

	/**
	 * JRChartAxisFormatFactory associated constants
	 */
	public static final String ELEMENT_axisFormat = "axisFormat";
	public static final String ELEMENT_labelFont = "labelFont";
	public static final String ELEMENT_tickLabelFont = "tickLabelFont";
	public static final String ATTRIBUTE_labelColor = "labelColor";
	public static final String ATTRIBUTE_tickLabelColor = "tickLabelColor";
	public static final String ATTRIBUTE_tickLabelMask = "tickLabelMask";
	public static final String ATTRIBUTE_verticalTickLabels = "verticalTickLabels";
	public static final String ATTRIBUTE_axisLineColor = "axisLineColor";

	/**
	 * JRChartLegendFactory associated constants
	 */
	public static final String ATTRIBUTE_textColor = "textColor";
	public static final String ATTRIBUTE_backgroundColor = "backgroundColor";

	/**
	 * JRChartTitleFactory associated constants
	 */
	public static final String ATTRIBUTE_position = "position";
	public static final String ATTRIBUTE_color = "color";

	/**
	 * JRChartPlotFactory associated constants
	 */
	public static final String ELEMENT_plot = "plot";
	public static final String ELEMENT_piePlot = "piePlot";
	public static final String ELEMENT_pie3DPlot = "pie3DPlot";
	public static final String ELEMENT_barPlot = "barPlot";
	public static final String ELEMENT_bubblePlot = "bubblePlot";
	public static final String ELEMENT_linePlot = "linePlot";
	public static final String ELEMENT_timeSeriesPlot = "timeSeriesPlot";
	public static final String ELEMENT_bar3DPlot = "bar3DPlot";
	public static final String ELEMENT_highLowPlot = "highLowPlot";
	public static final String ELEMENT_candlestickPlot = "candlestickPlot";
	public static final String ELEMENT_areaPlot = "areaPlot";
	public static final String ELEMENT_scatterPlot = "scatterPlot";
	public static final String ELEMENT_multiAxisPlot = "multiAxisPlot";

	public static final String ELEMENT_valueDisplay = "valueDisplay";
	public static final String ELEMENT_itemLabel = "itemLabel";
	public static final String ELEMENT_dataRange = "dataRange";
	public static final String ELEMENT_meterInterval = "meterInterval";
	public static final String ELEMENT_categoryAxisFormat = "categoryAxisFormat";
	public static final String ELEMENT_valueAxisFormat = "valueAxisFormat";
	public static final String ELEMENT_xAxisFormat = "xAxisFormat";
	public static final String ELEMENT_yAxisFormat = "yAxisFormat";
	public static final String ELEMENT_timeAxisFormat = "timeAxisFormat";

	public static final String ELEMENT_lowExpression = "lowExpression";
	public static final String ELEMENT_highExpression = "highExpression";
	public static final String ELEMENT_categoryAxisLabelExpression = "categoryAxisLabelExpression";
	public static final String ELEMENT_valueAxisLabelExpression = "valueAxisLabelExpression";
	public static final String ELEMENT_domainAxisMinValueExpression = "domainAxisMinValueExpression";
	public static final String ELEMENT_domainAxisMaxValueExpression = "domainAxisMaxValueExpression";
	public static final String ELEMENT_rangeAxisMinValueExpression = "rangeAxisMinValueExpression";
	public static final String ELEMENT_rangeAxisMaxValueExpression = "rangeAxisMaxValueExpression";
	public static final String ELEMENT_xAxisLabelExpression = "xAxisLabelExpression";
	public static final String ELEMENT_yAxisLabelExpression = "yAxisLabelExpression";
	public static final String ELEMENT_timeAxisLabelExpression = "timeAxisLabelExpression";
	public static final String ELEMENT_taskExpression = "taskExpression";
	public static final String ELEMENT_subtaskExpression = "subtaskExpression";
	public static final String ELEMENT_percentExpression = "percentExpression";

	public static final String ATTRIBUTE_backgroundAlpha = "backgroundAlpha";
	public static final String ATTRIBUTE_foregroundAlpha = "foregroundAlpha";
	public static final String ATTRIBUTE_labelRotation = "labelRotation";

	public static final String ATTRIBUTE_mask = "mask";
	public static final String ATTRIBUTE_label = "label";
	public static final String ATTRIBUTE_alpha = "alpha";
	public static final String ATTRIBUTE_depthFactor = "depthFactor";
	public static final String ATTRIBUTE_isShowLabels = "isShowLabels";
	public static final String ATTRIBUTE_isShowTickLabels = "isShowTickLabels";
	public static final String ATTRIBUTE_scaleType = "scaleType";
	public static final String ATTRIBUTE_isShowTickMarks = "isShowTickMarks";
	public static final String ATTRIBUTE_isShowLines = "isShowLines";
	public static final String ATTRIBUTE_isShowShapes = "isShowShapes";
	public static final String ATTRIBUTE_xOffset = "xOffset";
	public static final String ATTRIBUTE_yOffset = "yOffset";
	public static final String ATTRIBUTE_isShowOpenTicks = "isShowOpenTicks";
	public static final String ATTRIBUTE_isShowCloseTicks = "isShowCloseTicks";
	public static final String ATTRIBUTE_isShowVolume = "isShowVolume";
	public static final String ATTRIBUTE_isCircular = "isCircular";
	public static final String ATTRIBUTE_labelFormat = "labelFormat";
	public static final String ATTRIBUTE_legendLabelFormat = "legendLabelFormat";

	/**
	 * JRSeriesColorFactory associated constants
	 */
	public static final String ELEMENT_seriesColor = "seriesColor";

	public static final String ATTRIBUTE_seriesOrder = "seriesOrder";

	/**
	 * JRConditionalStyleFactory associated constants
	 */
	public static final String ELEMENT_conditionalStyle = "conditionalStyle";
	public static final String ELEMENT_conditionExpression = "conditionExpression";

	/**
	 * JRStyleFactory associated constants
	 */
	public static final String ELEMENT_style = "style";

	public static final String ATTRIBUTE_isDefault = "isDefault";
	public static final String ATTRIBUTE_mode = "mode";
	public static final String ATTRIBUTE_forecolor = "forecolor";
	public static final String ATTRIBUTE_backcolor = "backcolor";
	public static final String ATTRIBUTE_style = "style";
	public static final String ATTRIBUTE_origin = "origin";

	public static final String ATTRIBUTE_radius = "radius";

	// these are inherited by both images and texts.

	public static final String ATTRIBUTE_rotation = "rotation";
	public static final String ATTRIBUTE_isStyledText = "isStyledText";
	public static final String ATTRIBUTE_markup = "markup";
	public static final String ATTRIBUTE_pattern = "pattern";
	public static final String ATTRIBUTE_isBlankWhenNull = "isBlankWhenNull";

	public static final String ATTRIBUTE_fontSize = "fontSize";

	/**
	 * JRDatasetFactory associated constants
	 */
	public static final String ELEMENT_subDataset = "subDataset";
	public static final String ELEMENT_filterExpression = "filterExpression";

	/**
	 * JRDatasetRunFactory associated constants
	 */
	public static final String ELEMENT_datasetRun = "datasetRun";

	public static final String ELEMENT_parametersMapExpression = "parametersMapExpression";
	public static final String ELEMENT_connectionExpression = "connectionExpression";
	public static final String ELEMENT_dataSourceExpression = "dataSourceExpression";

	public static final String ATTRIBUTE_subDataset = "subDataset";

	/**
	 * JRDatasetRunParameterExpressionFactory associated constants
	 */
	public static final String ELEMENT_datasetParameterExpression = "datasetParameterExpression";

	/**
	 * JRDatasetRunParameterFactory associated constants
	 */
	public static final String ELEMENT_datasetParameter = "datasetParameter";

	/**
	 * JRElementDatasetFactory associated constants
	 */
	public static final String ELEMENT_dataset = "dataset";
	public static final String ELEMENT_categoryDataset = "categoryDataset";
	public static final String ELEMENT_timeSeriesDataset = "timeSeriesDataset";
	public static final String ELEMENT_timePeriodDataset = "timePeriodDataset";
	public static final String ELEMENT_xyzDataset = "xyzDataset";
	public static final String ELEMENT_xyDataset = "xyDataset";
	public static final String ELEMENT_pieDataset = "pieDataset";
	public static final String ELEMENT_valueDataset = "valueDataset";
	public static final String ELEMENT_highLowDataset = "highLowDataset";
	public static final String ELEMENT_ganttDataset = "ganttDataset";

	public static final String ELEMENT_pieSeries = "pieSeries";
	public static final String ELEMENT_categorySeries = "categorySeries";
	public static final String ELEMENT_xyzSeries = "xyzSeries";
	public static final String ELEMENT_xySeries = "xySeries";
	public static final String ELEMENT_timeSeries = "timeSeries";
	public static final String ELEMENT_timePeriodSeries = "timePeriodSeries";
	public static final String ELEMENT_ganttSeries = "ganttSeries";

	public static final String ELEMENT_incrementWhenExpression = "incrementWhenExpression";
	public static final String ELEMENT_keyExpression = "keyExpression";
	public static final String ELEMENT_valueExpression = "valueExpression";
	public static final String ELEMENT_labelExpression = "labelExpression";
	public static final String ELEMENT_otherKeyExpression = "otherKeyExpression";
	public static final String ELEMENT_otherLabelExpression = "otherLabelExpression";
	public static final String ELEMENT_seriesExpression = "seriesExpression";
	public static final String ELEMENT_categoryExpression = "categoryExpression";
	public static final String ELEMENT_xValueExpression = "xValueExpression";
	public static final String ELEMENT_yValueExpression = "yValueExpression";
	public static final String ELEMENT_zValueExpression = "zValueExpression";
	public static final String ELEMENT_timePeriodExpression = "timePeriodExpression";
	public static final String ELEMENT_startDateExpression = "startDateExpression";
	public static final String ELEMENT_endDateExpression = "endDateExpression";
	public static final String ELEMENT_dateExpression = "dateExpression";
	public static final String ELEMENT_openExpression = "openExpression";
	public static final String ELEMENT_closeExpression = "closeExpression";
	public static final String ELEMENT_volumeExpression = "volumeExpression";

	public static final String ATTRIBUTE_maxCount = "maxCount";
	public static final String ATTRIBUTE_minPercentage = "minPercentage";
	public static final String ATTRIBUTE_timePeriod = "timePeriod";

	/**
	 * JRElementFactory associated constants
	 */
	public static final String ELEMENT_reportElement = "reportElement";

	public static final String ATTRIBUTE_key = "key";
	public static final String ATTRIBUTE_positionType = "positionType";
	public static final String ATTRIBUTE_stretchType = "stretchType";
	public static final String ATTRIBUTE_isPrintRepeatedValues = "isPrintRepeatedValues";
	public static final String ATTRIBUTE_x = "x";
	public static final String ATTRIBUTE_y = "y";
	public static final String ATTRIBUTE_width = "width";
	public static final String ATTRIBUTE_isRemoveLineWhenBlank = "isRemoveLineWhenBlank";
	public static final String ATTRIBUTE_isPrintInFirstWholeBand = "isPrintInFirstWholeBand";
	public static final String ATTRIBUTE_isPrintWhenDetailOverflows = "isPrintWhenDetailOverflows";
	public static final String ATTRIBUTE_printWhenGroupChanges = "printWhenGroupChanges";

	/**
	 * JRElementGroupFactory associated constants
	 */
	public static final String ELEMENT_elementGroup = "elementGroup";

	/**
	 * JREllipseFactory associated constants
	 */
	public static final String ELEMENT_ellipse = "ellipse";

	/**
	 * JRFieldFactory associated constants
	 */
	public static final String ELEMENT_field = "field";
	public static final String ELEMENT_fieldDescription = "fieldDescription";

	public static final String ATTRIBUTE_class = "class";

	public static final String ATTRIBUTE_nestedType = "nestedType";

	/**
	 * JRFieldFactory associated constants
	 */
	public static final String ELEMENT_font = "font";

	public static final String ATTRIBUTE_reportFont = "reportFont";
	public static final String ATTRIBUTE_fontName = "fontName";
	public static final String ATTRIBUTE_isBold = "isBold";
	public static final String ATTRIBUTE_isItalic = "isItalic";
	public static final String ATTRIBUTE_isUnderline = "isUnderline";
	public static final String ATTRIBUTE_isStrikeThrough = "isStrikeThrough";
	public static final String ATTRIBUTE_size = "size";
	public static final String ATTRIBUTE_pdfFontName = "pdfFontName";
	public static final String ATTRIBUTE_pdfEncoding = "pdfEncoding";
	public static final String ATTRIBUTE_isPdfEmbedded = "isPdfEmbedded";

	/**
	 * JRFrameFactory associated constants
	 */
	public static final String ELEMENT_frame = "frame";

	/**
	 * JRGraphicElementFactory associated constants
	 */
	public static final String ELEMENT_graphicElement = "graphicElement";

	public static final String ATTRIBUTE_pen = "pen";
	public static final String ATTRIBUTE_fill = "fill";

	/**
	 * JRGroupFactory associated constants
	 */
	public static final String ELEMENT_group = "group";
	public static final String ELEMENT_groupExpression = "groupExpression";
	public static final String ELEMENT_groupHeader = "groupHeader";
	public static final String ELEMENT_groupFooter = "groupFooter";

	public static final String ATTRIBUTE_isStartNewColumn = "isStartNewColumn";
	public static final String ATTRIBUTE_isStartNewPage = "isStartNewPage";
	public static final String ATTRIBUTE_isResetPageNumber = "isResetPageNumber";
	public static final String ATTRIBUTE_isReprintHeaderOnEachPage = "isReprintHeaderOnEachPage";
	public static final String ATTRIBUTE_minHeightToStartNewPage = "minHeightToStartNewPage";
	public static final String ATTRIBUTE_footerPosition = "footerPosition";
	public static final String ATTRIBUTE_keepTogether = "keepTogether";

	/**
	 * JRHyperlinkFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkTooltipExpression = "hyperlinkTooltipExpression";
	public static final String ELEMENT_sectionHyperlink = "sectionHyperlink";
	public static final String ELEMENT_otherSectionHyperlink = "otherSectionHyperlink";
	public static final String ELEMENT_itemHyperlink = "itemHyperlink";
	public static final String ELEMENT_anchorNameExpression = "anchorNameExpression";
	public static final String ELEMENT_hyperlinkReferenceExpression = "hyperlinkReferenceExpression";
	public static final String ELEMENT_hyperlinkAnchorExpression = "hyperlinkAnchorExpression";
	public static final String ELEMENT_hyperlinkPageExpression = "hyperlinkPageExpression";

	public static final String ATTRIBUTE_hyperlinkType = "hyperlinkType";
	public static final String ATTRIBUTE_hyperlinkTarget = "hyperlinkTarget";

	/**
	 * JRHyperlinkFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkParameterExpression = "hyperlinkParameterExpression";

	/**
	 * JRHyperlinkParameterFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkParameter = "hyperlinkParameter";

	/**
	 * JRImageFactory associated constants
	 */
	public static final String ELEMENT_image = "image";
	public static final String ELEMENT_imageSource = "imageSource";
	public static final String ELEMENT_imageExpression = "imageExpression";

	public static final String ATTRIBUTE_scaleImage = "scaleImage";
	public static final String ATTRIBUTE_hAlign = "hAlign";
	public static final String ATTRIBUTE_vAlign = "vAlign";
	public static final String ATTRIBUTE_isUsingCache = "isUsingCache";
	public static final String ATTRIBUTE_isLazy = "isLazy";
	public static final String ATTRIBUTE_onErrorType = "onErrorType";
	
	public static final String ATTRIBUTE_runToBottom = "runToBottom";

	/**
	 * JRLineFactory associated constants
	 */
	public static final String ELEMENT_line = "line";

	public static final String ATTRIBUTE_direction = "direction";

	/**
	 * JRScriptletFactory associated constants
	 */
	public static final String ELEMENT_scriptlet = "scriptlet";
	public static final String ELEMENT_scriptletDescription = "scriptletDescription";

	/**
	 * JRParameterFactory associated constants
	 */
	public static final String ELEMENT_parameter = "parameter";
	public static final String ELEMENT_parameterDescription = "parameterDescription";
	public static final String ELEMENT_defaultValueExpression = "defaultValueExpression";

	public static final String ATTRIBUTE_isForPrompting = "isForPrompting";

	/**
	 * JRPrintHyperlinkParameterValueFactory associated constants
	 */
	public static final String ELEMENT_hyperlinkParameterValue = "hyperlinkParameterValue";

	/**
	 * JRPrintImageFactory associated constants
	 */
	public static final String ATTRIBUTE_anchorName = "anchorName";
	public static final String ATTRIBUTE_hyperlinkReference = "hyperlinkReference";
	public static final String ATTRIBUTE_hyperlinkAnchor = "hyperlinkAnchor";
	public static final String ATTRIBUTE_hyperlinkPage = "hyperlinkPage";
	public static final String ATTRIBUTE_hyperlinkTooltip = "hyperlinkTooltip";

	/**
	 * JRPrintImageSourceFactory associated constants
	 */
	public static final String ATTRIBUTE_isEmbedded = "isEmbedded";

	/**
	 * JRPrintTextFactory associated constants
	 */
	public static final String ATTRIBUTE_textAlignment = "textAlignment";
	public static final String ATTRIBUTE_verticalAlignment = "verticalAlignment";
	public static final String ATTRIBUTE_runDirection = "runDirection";
	public static final String ATTRIBUTE_textHeight = "textHeight";
	/**
	 * @deprecated No longer used.
	 */
	public static final String ATTRIBUTE_lineSpacingFactor = "lineSpacingFactor";
	/**
	 * @deprecated No longer used.
	 */
	public static final String ATTRIBUTE_leadingOffset = "leadingOffset";
	public static final String ATTRIBUTE_valueClass = "valueClass";

	/**
	 * JRQueryFactory associated constants
	 */
	public static final String ELEMENT_queryString = "queryString";

	/**
	 * JRRectangleFactory associated constants
	 */
	public static final String ELEMENT_rectangle = "rectangle";

	/**
	 * JRSortFieldFactory associated constants
	 */
	public static final String ELEMENT_sortField = "sortField";

	public static final String ATTRIBUTE_order = "order";

	/**
	 * JRStaticTextFactory associated constants
	 */
	public static final String ELEMENT_staticText = "staticText";
	public static final String ELEMENT_text = "text";
	public static final String ELEMENT_textContent = "textContent";
	public static final String ATTRIBUTE_truncateIndex = "truncateIndex";
	public static final String ELEMENT_textTruncateSuffix = "textTruncateSuffix";
	public static final String ELEMENT_lineBreakOffsets = "lineBreakOffsets";
	public static final String LINE_BREAK_OFFSET_SEPARATOR = ",";

	/**
	 * JRSubreportExpressionFactory associated constants
	 */
	public static final String ELEMENT_subreportExpression = "subreportExpression";

	/**
	 * JRSubreportFactory associated constants
	 */
	public static final String ELEMENT_subreport = "subreport";

	/**
	 * JRSubreportParameterFactory associated constants
	 */
	public static final String ELEMENT_subreportParameter = "subreportParameter";
	public static final String ELEMENT_subreportParameterExpression = "subreportParameterExpression";

	/**
	 * JRSubreportReturnValueFactory associated constants
	 */
	public static final String ELEMENT_returnValue = "returnValue";

	public static final String ATTRIBUTE_subreportVariable = "subreportVariable";
	public static final String ATTRIBUTE_toVariable = "toVariable";

	/**
	 * JRTextElementFactory associated constants
	 */
	public static final String ELEMENT_textElement = "textElement";

	/**
	 * JRTextFieldExpressionFactory associated constants
	 */
	public static final String ELEMENT_textFieldExpression = "textFieldExpression";

	/**
	 * JRTextFieldFactory  associated constants
	 */
	public static final String ELEMENT_textField = "textField";

	public static final String ATTRIBUTE_isStretchWithOverflow = "isStretchWithOverflow";

	/**
	 * JRVariableFactory  associated constants
	 */
	public static final String ELEMENT_variable = "variable";
	public static final String ELEMENT_variableExpression = "variableExpression";
	public static final String ELEMENT_initialValueExpression = "initialValueExpression";

	public static final String ATTRIBUTE_resetType = "resetType";
	public static final String ATTRIBUTE_resetGroup = "resetGroup";
	public static final String ATTRIBUTE_incrementType = "incrementType";
	public static final String ATTRIBUTE_incrementGroup = "incrementGroup";
	public static final String ATTRIBUTE_calculation = "calculation";
	public static final String ATTRIBUTE_incrementerFactoryClass = "incrementerFactoryClass";

	/**
	 * JRReportFontFactory  associated constants
	 */
	public static final String ELEMENT_reportFont = "reportFont";

	public static final String ELEMENT_template = "template";


	public static final String TEMPLATE_ELEMENT_ROOT = "jasperTemplate";
	public static final String TEMPLATE_ELEMENT_INCLUDED_TEMPLATE = "template";
	
	public static final String ELEMENT_componentElement = "componentElement";
	
	public static final String ELEMENT_genericElement = "genericElement";
	public static final String ELEMENT_genericElementType = "genericElementType";
	public static final String ELEMENT_genericElementParameter = 
		"genericElementParameter";
	public static final String ELEMENT_genericElementParameter_valueExpression = 
		"valueExpression";
	public static final String ATTRIBUTE_namespace = "namespace";
	public static final String ATTRIBUTE_skipWhenNull = "skipWhenNull";

	public static final String ELEMENT_genericElementParameterValue = 
		"genericElementParameterValue";




	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#OPAQUE#getName()}.
	 */
	private static final String POSITION_TYPE_FLOAT = PositionTypeEnum.FLOAT.getName();
	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#POSITION_TYPE_FIX_RELATIVE_TO_TOP#getName()}.
	 */
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_TOP = PositionTypeEnum.FIX_RELATIVE_TO_TOP.getName();
	/**
	 * @deprecated Replaced by {@link PositionTypeEnum#POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM#getName()}.
	 */
	private static final String POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM = PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM.getName();

	/**
	 * @deprecated Replaced by {@link PositionTypeEnum}.
	 */
	private static Map positionTypeMap;

	/**
	 * @deprecated Replaced by {@link PositionTypeEnum}.
	 */
	public static Map getPositionTypeMap()
	{
		if (positionTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(POSITION_TYPE_FLOAT,                  new Byte(JRElement.POSITION_TYPE_FLOAT));
			map.put(POSITION_TYPE_FIX_RELATIVE_TO_TOP,    new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP));
			map.put(POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM, new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM));
			map.put(new Byte(JRElement.POSITION_TYPE_FLOAT),                  POSITION_TYPE_FLOAT);
			map.put(new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP),    POSITION_TYPE_FIX_RELATIVE_TO_TOP);
			map.put(new Byte(JRElement.POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM), POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM);
			positionTypeMap = Collections.unmodifiableMap(map);
		}

		return positionTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link ModeEnum#OPAQUE#getName()}.
	 */
	private static final String MODE_OPAQUE = ModeEnum.OPAQUE.getName();
	/**
	 * @deprecated Replaced by {@link ModeEnum#TRANSPARENT#getName()}.
	 */
	private static final String MODE_TRANSPARENT = ModeEnum.TRANSPARENT.getName();

	/**
	 * @deprecated Replaced by {@link ModeEnum}.
	 */
	private static Map modeMap;

	/**
	 * @deprecated Replaced by {@link ModeEnum}.
	 */
	public static Map getModeMap()
	{
		if (modeMap == null)
		{
			Map map = new HashMap(6);
			map.put(MODE_OPAQUE,      new Byte(JRElement.MODE_OPAQUE));
			map.put(MODE_TRANSPARENT, new Byte(JRElement.MODE_TRANSPARENT));
			map.put(new Byte(JRElement.MODE_OPAQUE),      MODE_OPAQUE);
			map.put(new Byte(JRElement.MODE_TRANSPARENT), MODE_TRANSPARENT);
			modeMap = Collections.unmodifiableMap(map);
		}

		return modeMap;
	}

	/**
	 * @deprecated Replaced by {@link ColorEnum#BLACK#getName()}.
	 */
	private static final String COLOR_BLACK = ColorEnum.BLACK.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#BLUE#getName()}.
	 */
	private static final String COLOR_BLUE = ColorEnum.BLUE.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#CYAN#getName()}.
	 */
	private static final String COLOR_CYAN = ColorEnum.CYAN.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#DARK_GRAY#getName()}.
	 */
	private static final String COLOR_DARK_GRAY = ColorEnum.DARK_GRAY.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#GRAY#getName()}.
	 */
	private static final String COLOR_GRAY = ColorEnum.GRAY.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#GREEN#getName()}.
	 */
	private static final String COLOR_GREEN = ColorEnum.GREEN.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#LIGHT_GRAY#getName()}.
	 */
	private static final String COLOR_LIGHT_GRAY = ColorEnum.LIGHT_GRAY.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#MAGENTA#getName()}.
	 */
	private static final String COLOR_MAGENTA = ColorEnum.MAGENTA.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#ORANGE#getName()}.
	 */
	private static final String COLOR_ORANGE = ColorEnum.ORANGE.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#PINK#getName()}.
	 */
	private static final String COLOR_PINK = ColorEnum.PINK.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#RED#getName()}.
	 */
	private static final String COLOR_RED = ColorEnum.RED.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#YELLOW#getName()}.
	 */
	private static final String COLOR_YELLOW = ColorEnum.YELLOW.getName();
	/**
	 * @deprecated Replaced by {@link ColorEnum#WHITE#getName()}.
	 */
	private static final String COLOR_WHITE = ColorEnum.WHITE.getName();

	/**
	 * @deprecated Replaced by {@link ColorEnum}.
	 */
	private static Map colorMap;

	/**
	 * @deprecated Replaced by {@link ColorEnum}.
	 */
	public static Map getColorMap()
	{
		if (colorMap == null)
		{
			Map map = new HashMap(35);
			map.put(COLOR_BLACK,      Color.black);
			map.put(COLOR_BLUE,       Color.blue);
			map.put(COLOR_CYAN,       Color.cyan);
			map.put(COLOR_DARK_GRAY,  Color.darkGray);
			map.put(COLOR_GRAY,       Color.gray);
			map.put(COLOR_GREEN,      Color.green);
			map.put(COLOR_LIGHT_GRAY, Color.lightGray);
			map.put(COLOR_MAGENTA,    Color.magenta);
			map.put(COLOR_ORANGE,     Color.orange);
			map.put(COLOR_PINK,       Color.pink);
			map.put(COLOR_RED,        Color.red);
			map.put(COLOR_YELLOW,     Color.yellow);
			map.put(COLOR_WHITE,      Color.white);
			map.put(Color.black,      COLOR_BLACK);
			map.put(Color.blue,       COLOR_BLUE);
			map.put(Color.cyan,       COLOR_CYAN);
			map.put(Color.darkGray,   COLOR_DARK_GRAY);
			map.put(Color.gray,       COLOR_GRAY);
			map.put(Color.green,      COLOR_GREEN);
			map.put(Color.lightGray,  COLOR_LIGHT_GRAY);
			map.put(Color.magenta,    COLOR_MAGENTA);
			map.put(Color.orange,     COLOR_ORANGE);
			map.put(Color.pink,       COLOR_PINK);
			map.put(Color.red,        COLOR_RED);
			map.put(Color.yellow,     COLOR_YELLOW);
			map.put(Color.white,      COLOR_WHITE);
			colorMap = Collections.unmodifiableMap(map);
		}

		return colorMap;
	}

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_LEFT#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_LEFT = HorizontalAlignEnum.LEFT.getName();
	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_CENTER#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_CENTER = HorizontalAlignEnum.CENTER.getName();
	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_RIGHT#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_RIGHT = HorizontalAlignEnum.RIGHT.getName();
	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum#HORIZONTAL_ALIGN_JUSTIFIED#getName()}.
	 */
	private static final String HORIZONTAL_ALIGN_JUSTIFIED = HorizontalAlignEnum.JUSTIFIED.getName();

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum}.
	 */
	private static Map horizontalAlignMap;

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum}.
	 */
	public static Map getHorizontalAlignMap()
	{
		if (horizontalAlignMap == null)
		{
			Map map = new HashMap(11);
			map.put(HORIZONTAL_ALIGN_LEFT,      new Byte(JRAlignment.HORIZONTAL_ALIGN_LEFT));
			map.put(HORIZONTAL_ALIGN_CENTER,    new Byte(JRAlignment.HORIZONTAL_ALIGN_CENTER));
			map.put(HORIZONTAL_ALIGN_RIGHT,     new Byte(JRAlignment.HORIZONTAL_ALIGN_RIGHT));
			map.put(HORIZONTAL_ALIGN_JUSTIFIED, new Byte(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED));
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_LEFT),      HORIZONTAL_ALIGN_LEFT);
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_CENTER),    HORIZONTAL_ALIGN_CENTER);
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_RIGHT),     HORIZONTAL_ALIGN_RIGHT);
			map.put(new Byte(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED), HORIZONTAL_ALIGN_JUSTIFIED);
			horizontalAlignMap = Collections.unmodifiableMap(map);
		}

		return horizontalAlignMap;
	}

	/**
	 * @deprecated Replaced by {@link HorizontalAlignEnum}.
	 */
	public static Map getTextAlignMap()
	{
		return getHorizontalAlignMap();
	}

	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum#TOP#getName()}.
	 */
	private static final String VERTICAL_ALIGN_TOP = VerticalAlignEnum.TOP.getName();
	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum#MIDDLE#getName()}.
	 */
	private static final String VERTICAL_ALIGN_MIDDLE = VerticalAlignEnum.MIDDLE.getName();
	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum#BOTTOM#getName()}.
	 */
	private static final String VERTICAL_ALIGN_BOTTOM = VerticalAlignEnum.BOTTOM.getName();

	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum}.
	 */
	private static Map verticalAlignMap;

	/**
	 * @deprecated Replaced by {@link VerticalAlignEnum}.
	 */
	public static Map getVerticalAlignMap()
	{
		if (verticalAlignMap == null)
		{
			Map map = new HashMap(8);
			map.put(VERTICAL_ALIGN_TOP,    new Byte(JRAlignment.VERTICAL_ALIGN_TOP));
			map.put(VERTICAL_ALIGN_MIDDLE, new Byte(JRAlignment.VERTICAL_ALIGN_MIDDLE));
			map.put(VERTICAL_ALIGN_BOTTOM, new Byte(JRAlignment.VERTICAL_ALIGN_BOTTOM));
			map.put(new Byte(JRAlignment.VERTICAL_ALIGN_TOP),    VERTICAL_ALIGN_TOP);
			map.put(new Byte(JRAlignment.VERTICAL_ALIGN_MIDDLE), VERTICAL_ALIGN_MIDDLE);
			map.put(new Byte(JRAlignment.VERTICAL_ALIGN_BOTTOM), VERTICAL_ALIGN_BOTTOM);
			verticalAlignMap = Collections.unmodifiableMap(map);
		}

		return verticalAlignMap;
	}

	/**
	 * @deprecated Replaced by {@link RotationEnum#NONE#getName()}.
	 */
	private static final String ROTATION_NONE = RotationEnum.NONE.getName();
	/**
	 * @deprecated Replaced by {@link RotationEnum#LEFT#getName()}.
	 */
	private static final String ROTATION_LEFT = RotationEnum.LEFT.getName();
	/**
	 * @deprecated Replaced by {@link RotationEnum#RIGHT#getName()}.
	 */
	private static final String ROTATION_RIGHT = RotationEnum.RIGHT.getName();
	/**
	 * @deprecated Replaced by {@link RotationEnum#UPSIDE_DOWN#getName()}.
	 */
	private static final String ROTATION_UPSIDE_DOWN = RotationEnum.UPSIDE_DOWN.getName();

	/**
	 * @deprecated Replaced by {@link RotationEnum}.
	 */
	private static Map rotationMap;

	/**
	 * @deprecated Replaced by {@link RotationEnum}.
	 */
	public static Map getRotationMap()
	{
		if (rotationMap == null)
		{
			Map map = new HashMap(11);
			map.put(ROTATION_NONE,  		new Byte(JRTextElement.ROTATION_NONE));
			map.put(ROTATION_LEFT,  		new Byte(JRTextElement.ROTATION_LEFT));
			map.put(ROTATION_RIGHT, 		new Byte(JRTextElement.ROTATION_RIGHT));
			map.put(ROTATION_UPSIDE_DOWN, 	new Byte(JRTextElement.ROTATION_UPSIDE_DOWN));
			map.put(new Byte(JRTextElement.ROTATION_NONE),  		ROTATION_NONE);
			map.put(new Byte(JRTextElement.ROTATION_LEFT),  		ROTATION_LEFT);
			map.put(new Byte(JRTextElement.ROTATION_RIGHT), 		ROTATION_RIGHT);
			map.put(new Byte(JRTextElement.ROTATION_UPSIDE_DOWN), 	ROTATION_UPSIDE_DOWN);
			rotationMap = Collections.unmodifiableMap(map);
		}

		return rotationMap;
	}

	/**
	 * @deprecated Replaced by {@link BreakTypeEnum#PAGE#getName()}.
	 */
	private static final String BREAK_TYPE_PAGE = BreakTypeEnum.PAGE.getName();
	/**
	 * @deprecated Replaced by {@link BreakTypeEnum#COLUMN#getName()}.
	 */
	private static final String BREAK_TYPE_COLUMN = BreakTypeEnum.COLUMN.getName();

	/**
	 * @deprecated Replaced by {@link BreakTypeEnum}.
	 */
	private static Map breakTypeMap;

	/**
	 * @deprecated Replaced by {@link BreakTypeEnum}.
	 */
	public static Map getBreakTypeMap()
	{
		if (breakTypeMap == null)
		{
			Map map = new HashMap(6);
			map.put(BREAK_TYPE_PAGE,   new Byte(JRBreak.TYPE_PAGE));
			map.put(BREAK_TYPE_COLUMN, new Byte(JRBreak.TYPE_COLUMN));
			map.put(new Byte(JRBreak.TYPE_PAGE),   BREAK_TYPE_PAGE);
			map.put(new Byte(JRBreak.TYPE_COLUMN), BREAK_TYPE_COLUMN);
			breakTypeMap = Collections.unmodifiableMap(map);
		}

		return breakTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link RunDirectionEnum#LTR#getName()}.
	 */
	private static final String RUN_DIRECTION_LTR = RunDirectionEnum.LTR.getName();
	/**
	 * @deprecated Replaced by {@link RunDirectionEnum#RTL#getName()}.
	 */
	private static final String RUN_DIRECTION_RTL = RunDirectionEnum.RTL.getName();

	/**
	 * @deprecated Replaced by {@link RunDirectionEnum}.
	 */
	private static Map runDirectionMap;

	/**
	 * @deprecated Replaced by {@link RunDirectionEnum}.
	 */
	public static Map getRunDirectionMap()
	{
		if (runDirectionMap == null)
		{
			Map map = new HashMap(6);
			map.put(RUN_DIRECTION_LTR, new Byte(JRPrintText.RUN_DIRECTION_LTR));
			map.put(RUN_DIRECTION_RTL, new Byte(JRPrintText.RUN_DIRECTION_RTL));
			map.put(new Byte(JRPrintText.RUN_DIRECTION_LTR), RUN_DIRECTION_LTR);
			map.put(new Byte(JRPrintText.RUN_DIRECTION_RTL), RUN_DIRECTION_RTL);
			runDirectionMap = Collections.unmodifiableMap(map);
		}

		return runDirectionMap;
	}

	/**
	 * @deprecated Replaced by {@link LineSpacingEnum#SINGLE#getName()}.
	 */
	private static final String LINE_SPACING_SINGLE = LineSpacingEnum.SINGLE.getName();
	/**
	 * @deprecated Replaced by {@link LineSpacingEnum#ONE_AND_HALF#getName()}.
	 */
	private static final String LINE_SPACING_1_1_2 = LineSpacingEnum.ONE_AND_HALF.getName();
	/**
	 * @deprecated Replaced by {@link LineSpacingEnum#DOUBLE#getName()}.
	 */
	private static final String LINE_SPACING_DOUBLE = LineSpacingEnum.DOUBLE.getName();

	/**
	 * @deprecated Replaced by {@link LineSpacingEnum}.
	 */
	private static Map lineSpacingMap;

	/**
	 * @deprecated Replaced by {@link LineSpacingEnum}.
	 */
	public static Map getLineSpacingMap()
	{
		if (lineSpacingMap == null)
		{
			Map map = new HashMap(8);
			map.put(LINE_SPACING_SINGLE, new Byte(JRTextElement.LINE_SPACING_SINGLE));
			map.put(LINE_SPACING_1_1_2,  new Byte(JRTextElement.LINE_SPACING_1_1_2));
			map.put(LINE_SPACING_DOUBLE, new Byte(JRTextElement.LINE_SPACING_DOUBLE));
			map.put(new Byte(JRTextElement.LINE_SPACING_SINGLE), LINE_SPACING_SINGLE);
			map.put(new Byte(JRTextElement.LINE_SPACING_1_1_2),  LINE_SPACING_1_1_2);
			map.put(new Byte(JRTextElement.LINE_SPACING_DOUBLE), LINE_SPACING_DOUBLE);
			lineSpacingMap = Collections.unmodifiableMap(map);
		}

		return lineSpacingMap;
	}

	/**
	 * @deprecated Replaced by {@link LineDirectionEnum#TOP_DOWN#getName()}.
	 */
	private static final String DIRECTION_TOP_DOWN = LineDirectionEnum.TOP_DOWN.getName();
	/**
	 * @deprecated Replaced by {@link LineDirectionEnum#BOTTOM_UP#getName()}.
	 */
	private static final String DIRECTION_BOTTOM_UP = LineDirectionEnum.BOTTOM_UP.getName();

	/**
	 * @deprecated Replaced by {@link LineDirectionEnum}.
	 */
	private static Map directionMap;

	/**
	 * @deprecated Replaced by {@link LineDirectionEnum}.
	 */
	public static Map getDirectionMap()
	{
		if (directionMap == null)
		{
			Map map = new HashMap(6);
			map.put(DIRECTION_TOP_DOWN,  new Byte(JRLine.DIRECTION_TOP_DOWN));
			map.put(DIRECTION_BOTTOM_UP, new Byte(JRLine.DIRECTION_BOTTOM_UP));
			map.put(new Byte(JRLine.DIRECTION_TOP_DOWN),  DIRECTION_TOP_DOWN);
			map.put(new Byte(JRLine.DIRECTION_BOTTOM_UP), DIRECTION_BOTTOM_UP);
			directionMap = Collections.unmodifiableMap(map);
		}

		return directionMap;
	}

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#CLIP#getName()}.
	 */
	private static final String SCALE_IMAGE_CLIP = ScaleImageEnum.CLIP.getName();
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#FILL_FRAME#getName()}.
	 */
	private static final String SCALE_IMAGE_FILL_FRAME = ScaleImageEnum.FILL_FRAME.getName();
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#RETAIN_SHAPE#getName()}.
	 */
	private static final String SCALE_IMAGE_RETAIN_SHAPE = ScaleImageEnum.RETAIN_SHAPE.getName();
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#REAL_HEIGHT#getName()}.
	 */
	private static final String SCALE_IMAGE_REAL_HEIGT = ScaleImageEnum.REAL_HEIGHT.getName();
	/**
	 * @deprecated Replaced by {@link ScaleImageEnum#REAL_SIZE#getName()}.
	 */
	private static final String SCALE_IMAGE_REAL_SIZE = ScaleImageEnum.REAL_SIZE.getName();

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum}.
	 */
	private static Map scaleImageMap;

	/**
	 * @deprecated Replaced by {@link ScaleImageEnum}.
	 */
	public static Map getScaleImageMap()
	{
		if (scaleImageMap == null)
		{
			Map map = new HashMap(14);
			map.put(SCALE_IMAGE_CLIP,         new Byte(JRImage.SCALE_IMAGE_CLIP));
			map.put(SCALE_IMAGE_FILL_FRAME,   new Byte(JRImage.SCALE_IMAGE_FILL_FRAME));
			map.put(SCALE_IMAGE_RETAIN_SHAPE, new Byte(JRImage.SCALE_IMAGE_RETAIN_SHAPE));
			map.put(SCALE_IMAGE_REAL_HEIGT, new Byte(JRImage.SCALE_IMAGE_REAL_HEIGHT));
			map.put(SCALE_IMAGE_REAL_SIZE, new Byte(JRImage.SCALE_IMAGE_REAL_SIZE));
			map.put(new Byte(JRImage.SCALE_IMAGE_CLIP),         SCALE_IMAGE_CLIP);
			map.put(new Byte(JRImage.SCALE_IMAGE_FILL_FRAME),   SCALE_IMAGE_FILL_FRAME);
			map.put(new Byte(JRImage.SCALE_IMAGE_RETAIN_SHAPE), SCALE_IMAGE_RETAIN_SHAPE);
			map.put(new Byte(JRImage.SCALE_IMAGE_REAL_HEIGHT), SCALE_IMAGE_REAL_HEIGT);
			map.put(new Byte(JRImage.SCALE_IMAGE_REAL_SIZE), SCALE_IMAGE_REAL_SIZE);
			scaleImageMap = Collections.unmodifiableMap(map);
		}

		return scaleImageMap;
	}

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#ERROR#getName()}.
	 */
	private static final String ON_ERROR_TYPE_ERROR = OnErrorTypeEnum.ERROR.getName();
	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#BLANK#getName()}.
	 */
	private static final String ON_ERROR_TYPE_BLANK = OnErrorTypeEnum.BLANK.getName();
	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum#ICON#getName()}.
	 */
	private static final String ON_ERROR_TYPE_ICON = OnErrorTypeEnum.ICON.getName();

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum}.
	 */
	private static Map onErrorTypeMap;

	/**
	 * @deprecated Replaced by {@link OnErrorTypeEnum}.
	 */
	public static Map getOnErrorTypeMap()
	{
		if (onErrorTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(ON_ERROR_TYPE_ERROR, new Byte(JRImage.ON_ERROR_TYPE_ERROR));
			map.put(ON_ERROR_TYPE_BLANK, new Byte(JRImage.ON_ERROR_TYPE_BLANK));
			map.put(ON_ERROR_TYPE_ICON,  new Byte(JRImage.ON_ERROR_TYPE_ICON));
			map.put(new Byte(JRImage.ON_ERROR_TYPE_ERROR), ON_ERROR_TYPE_ERROR);
			map.put(new Byte(JRImage.ON_ERROR_TYPE_BLANK), ON_ERROR_TYPE_BLANK);
			map.put(new Byte(JRImage.ON_ERROR_TYPE_ICON),  ON_ERROR_TYPE_ICON);
			onErrorTypeMap = Collections.unmodifiableMap(map);
		}

		return onErrorTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link StretchTypeEnum#NO_STRETCH#getName()}.
	 */
	private static final String STRETCH_TYPE_NO_STRETCH = StretchTypeEnum.NO_STRETCH.getName();
	/**
	 * @deprecated Replaced by {@link StretchTypeEnum#RELATIVE_TO_TALLEST_OBJECT#getName()}.
	 */
	private static final String STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT = StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT.getName();
	/**
	 * @deprecated Replaced by {@link StretchTypeEnum#RELATIVE_TO_BAND_HEIGHT#getName()}.
	 */
	private static final String STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT = StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT.getName();

	/**
	 * @deprecated Replaced by {@link StretchTypeEnum}.
	 */
	private static Map stretchTypeMap;

	/**
	 * @deprecated Replaced by {@link StretchTypeEnum}.
	 */
	public static Map getStretchTypeMap()
	{
		if (stretchTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(STRETCH_TYPE_NO_STRETCH,                 new Byte(JRElement.STRETCH_TYPE_NO_STRETCH));
			map.put(STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT, new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT));
			map.put(STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT,    new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT));
			map.put(new Byte(JRElement.STRETCH_TYPE_NO_STRETCH),                 STRETCH_TYPE_NO_STRETCH);
			map.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT), STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT);
			map.put(new Byte(JRElement.STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT),    STRETCH_TYPE_RELATIVE_TO_BAND_HEIGHT);
			stretchTypeMap = Collections.unmodifiableMap(map);
		}

		return stretchTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link LineStyleEnum#SOLID#getName()}.
	 */
	private static final String LINE_STYLE_SOLID = LineStyleEnum.SOLID.getName();
	/**
	 * @deprecated Replaced by {@link LineStyleEnum#DASHED#getName()}.
	 */
	private static final String LINE_STYLE_DASHED = LineStyleEnum.DASHED.getName();
	/**
	 * @deprecated Replaced by {@link LineStyleEnum#DOTTED#getName()}.
	 */
	private static final String LINE_STYLE_DOTTED = LineStyleEnum.DOTTED.getName();
	/**
	 * @deprecated Replaced by {@link LineStyleEnum#DOUBLE#getName()}.
	 */
	private static final String lINE_STYLE_DOUBLE = LineStyleEnum.DOUBLE.getName();

	/**
	 * @deprecated Replaced by {@link LineStyleEnum}.
	 */
	private static Map lineStyleMap;

	/**
	 * @deprecated Replaced by {@link LineStyleEnum}.
	 */
	public static Map getLineStyleMap()
	{
		if (lineStyleMap == null)
		{
			Map map = new HashMap(11);
			map.put(LINE_STYLE_SOLID,  new Byte(JRPen.LINE_STYLE_SOLID));
			map.put(LINE_STYLE_DASHED, new Byte(JRPen.LINE_STYLE_DASHED));
			map.put(LINE_STYLE_DOTTED, new Byte(JRPen.LINE_STYLE_DOTTED));
			map.put(lINE_STYLE_DOUBLE, new Byte(JRPen.LINE_STYLE_DOUBLE));
			map.put(new Byte(JRPen.LINE_STYLE_SOLID),  LINE_STYLE_SOLID);
			map.put(new Byte(JRPen.LINE_STYLE_DASHED), LINE_STYLE_DASHED);
			map.put(new Byte(JRPen.LINE_STYLE_DOTTED), LINE_STYLE_DOTTED);
			map.put(new Byte(JRPen.LINE_STYLE_DOUBLE), lINE_STYLE_DOUBLE);
			lineStyleMap = Collections.unmodifiableMap(map);
		}

		return lineStyleMap;
	}

	/**
	 * @deprecated Replaced by {@link PenEnum#NONE#getName()}.
	 */
	private static final String PEN_NONE = PenEnum.NONE.getName();
	/**
	 * @deprecated Replaced by {@link PenEnum#THIN#getName()}.
	 */
	private static final String PEN_THIN = PenEnum.THIN.getName();
	/**
	 * @deprecated Replaced by {@link PenEnum#ONE_POINT#getName()}.
	 */
	private static final String PEN_1_POINT = PenEnum.ONE_POINT.getName();
	/**
	 * @deprecated Replaced by {@link PenEnum#TWO_POINT#getName()}.
	 */
	private static final String PEN_2_POINT = PenEnum.TWO_POINT.getName();
	/**
	 * @deprecated Replaced by {@link PenEnum#FOUR_POINT#getName()}.
	 */
	private static final String PEN_4_POINT = PenEnum.FOUR_POINT.getName();
	/**
	 * @deprecated Replaced by {@link PenEnum#DOTTED#getName()}.
	 */
	private static final String PEN_DOTTED = PenEnum.DOTTED.getName();

	/**
	 * @deprecated Replaced by {@link PenEnum}.
	 */
	private static Map penMap;

	/**
	 * @deprecated Replaced by {@link PenEnum}.
	 */
	public static Map getPenMap()
	{
		if (penMap == null)
		{
			Map map = new HashMap(16);
			map.put(PEN_NONE,     new Byte(JRGraphicElement.PEN_NONE));
			map.put(PEN_THIN,     new Byte(JRGraphicElement.PEN_THIN));
			map.put(PEN_1_POINT,  new Byte(JRGraphicElement.PEN_1_POINT));
			map.put(PEN_2_POINT,  new Byte(JRGraphicElement.PEN_2_POINT));
			map.put(PEN_4_POINT,  new Byte(JRGraphicElement.PEN_4_POINT));
			map.put(PEN_DOTTED,   new Byte(JRGraphicElement.PEN_DOTTED));
			map.put(new Byte(JRGraphicElement.PEN_NONE),     PEN_NONE);
			map.put(new Byte(JRGraphicElement.PEN_THIN),     PEN_THIN);
			map.put(new Byte(JRGraphicElement.PEN_1_POINT),  PEN_1_POINT);
			map.put(new Byte(JRGraphicElement.PEN_2_POINT),  PEN_2_POINT);
			map.put(new Byte(JRGraphicElement.PEN_4_POINT),  PEN_4_POINT);
			map.put(new Byte(JRGraphicElement.PEN_DOTTED),   PEN_DOTTED);
			penMap = Collections.unmodifiableMap(map);
		}

		return penMap;
	}

	/**
	 * @deprecated Replaced by {@link FillEnum#SOLID#getName()}.
	 */
	private static final String FILL_SOLID = FillEnum.SOLID.getName();

	/**
	 * @deprecated Replaced by {@link FillEnum}.
	 */
	private static Map fillMap;

	/**
	 * @deprecated Replaced by {@link FillEnum}.
	 */
	public static Map getFillMap()
	{
		if (fillMap == null)
		{
			Map map = new HashMap(3);
			map.put(FILL_SOLID, new Byte(JRGraphicElement.FILL_SOLID));
			map.put(new Byte(JRGraphicElement.FILL_SOLID), FILL_SOLID);
			fillMap = Collections.unmodifiableMap(map);
		}

		return fillMap;
	}

	/**
	 * @deprecated Replaced by {@link ResetTypeEnum#NONE#getName()}.
	 */
	private static final String RESET_TYPE_NONE = ResetTypeEnum.NONE.getName();
	/**
	 * @deprecated Replaced by {@link ResetTypeEnum#REPORT#getName()}.
	 */
	private static final String RESET_TYPE_REPORT = ResetTypeEnum.REPORT.getName();
	/**
	 * @deprecated Replaced by {@link ResetTypeEnum#PAGE#getName()}.
	 */
	private static final String RESET_TYPE_PAGE = ResetTypeEnum.PAGE.getName();
	/**
	 * @deprecated Replaced by {@link ResetTypeEnum#COLUMN#getName()}.
	 */
	private static final String RESET_TYPE_COLUMN = ResetTypeEnum.COLUMN.getName();
	/**
	 * @deprecated Replaced by {@link ResetTypeEnum#GROUP#getName()}.
	 */
	private static final String RESET_TYPE_GROUP = ResetTypeEnum.GROUP.getName();

	/**
	 * @deprecated Replaced by {@link ResetTypeEnum}.
	 */
	private static Map resetTypeMap;

	/**
	 * @deprecated Replaced by {@link ResetTypeEnum}.
	 */
	public static Map getResetTypeMap()
	{
		if (resetTypeMap == null)
		{
			Map map = new HashMap(14);
			map.put(RESET_TYPE_NONE,   new Byte(JRVariable.RESET_TYPE_NONE));
			map.put(RESET_TYPE_REPORT, new Byte(JRVariable.RESET_TYPE_REPORT));
			map.put(RESET_TYPE_PAGE,   new Byte(JRVariable.RESET_TYPE_PAGE));
			map.put(RESET_TYPE_COLUMN, new Byte(JRVariable.RESET_TYPE_COLUMN));
			map.put(RESET_TYPE_GROUP,  new Byte(JRVariable.RESET_TYPE_GROUP));
			map.put(new Byte(JRVariable.RESET_TYPE_NONE),   RESET_TYPE_NONE);
			map.put(new Byte(JRVariable.RESET_TYPE_REPORT), RESET_TYPE_REPORT);
			map.put(new Byte(JRVariable.RESET_TYPE_PAGE),   RESET_TYPE_PAGE);
			map.put(new Byte(JRVariable.RESET_TYPE_COLUMN), RESET_TYPE_COLUMN);
			map.put(new Byte(JRVariable.RESET_TYPE_GROUP),  RESET_TYPE_GROUP);
			resetTypeMap = Collections.unmodifiableMap(map);
		}

		return resetTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link CalculationEnum#NOTHING#getName()}.
	 */
	private static final String CALCULATION_NOTHING = CalculationEnum.NOTHING.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#COUNT#getName()}.
	 */
	private static final String CALCULATION_COUNT = CalculationEnum.COUNT.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#SUM#getName()}.
	 */
	private static final String CALCULATION_SUM = CalculationEnum.SUM.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#AVERAGE#getName()}.
	 */
	private static final String CALCULATION_AVERAGE = CalculationEnum.AVERAGE.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#LOWEST#getName()}.
	 */
	private static final String CALCULATION_LOWEST = CalculationEnum.LOWEST.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#HIGHEST#getName()}.
	 */
	private static final String CALCULATION_HIGHEST = CalculationEnum.HIGHEST.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#STANDARD_DEVIATION#getName()}.
	 */
	private static final String CALCULATION_STANDARD_DEVIATION = CalculationEnum.STANDARD_DEVIATION.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#VARIANCE#getName()}.
	 */
	private static final String CALCULATION_VARIANCE = CalculationEnum.VARIANCE.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#SYSTEM#getName()}.
	 */
	private static final String CALCULATION_SYSTEM = CalculationEnum.SYSTEM.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#FIRST#getName()}.
	 */
	private static final String CALCULATION_FIRST = CalculationEnum.FIRST.getName();
	/**
	 * @deprecated Replaced by {@link CalculationEnum#DISTINCT_COUNT#getName()}.
	 */
	private static final String CALCULATION_DISTINCT_COUNT = CalculationEnum.DISTINCT_COUNT.getName();

	/**
	 * @deprecated Replaced by {@link CalculationEnum}.
	 */
	private static Map calculationMap;

	/**
	 * @deprecated Replaced by {@link CalculationEnum}.
	 */
	public static Map getCalculationMap()
	{
		if (calculationMap == null)
		{
			Map map = new HashMap(30);
			map.put(CALCULATION_NOTHING,            new Byte(JRVariable.CALCULATION_NOTHING));
			map.put(CALCULATION_COUNT,              new Byte(JRVariable.CALCULATION_COUNT));
			map.put(CALCULATION_SUM,                new Byte(JRVariable.CALCULATION_SUM));
			map.put(CALCULATION_AVERAGE,            new Byte(JRVariable.CALCULATION_AVERAGE));
			map.put(CALCULATION_LOWEST,             new Byte(JRVariable.CALCULATION_LOWEST));
			map.put(CALCULATION_HIGHEST,            new Byte(JRVariable.CALCULATION_HIGHEST));
			map.put(CALCULATION_STANDARD_DEVIATION, new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION));
			map.put(CALCULATION_VARIANCE,           new Byte(JRVariable.CALCULATION_VARIANCE));
			map.put(CALCULATION_SYSTEM,             new Byte(JRVariable.CALCULATION_SYSTEM));
			map.put(CALCULATION_FIRST,              new Byte(JRVariable.CALCULATION_FIRST));
			map.put(CALCULATION_DISTINCT_COUNT,     new Byte(JRVariable.CALCULATION_DISTINCT_COUNT));
			map.put(new Byte(JRVariable.CALCULATION_NOTHING),            CALCULATION_NOTHING);
			map.put(new Byte(JRVariable.CALCULATION_COUNT),              CALCULATION_COUNT);
			map.put(new Byte(JRVariable.CALCULATION_SUM),                CALCULATION_SUM);
			map.put(new Byte(JRVariable.CALCULATION_AVERAGE),            CALCULATION_AVERAGE);
			map.put(new Byte(JRVariable.CALCULATION_LOWEST),             CALCULATION_LOWEST);
			map.put(new Byte(JRVariable.CALCULATION_HIGHEST),            CALCULATION_HIGHEST);
			map.put(new Byte(JRVariable.CALCULATION_STANDARD_DEVIATION), CALCULATION_STANDARD_DEVIATION);
			map.put(new Byte(JRVariable.CALCULATION_VARIANCE),           CALCULATION_VARIANCE);
			map.put(new Byte(JRVariable.CALCULATION_SYSTEM),             CALCULATION_SYSTEM);
			map.put(new Byte(JRVariable.CALCULATION_FIRST),              CALCULATION_FIRST);
			map.put(new Byte(JRVariable.CALCULATION_DISTINCT_COUNT),     CALCULATION_DISTINCT_COUNT);
			calculationMap = Collections.unmodifiableMap(map);
		}

		return calculationMap;
	}

	/**
	 * @deprecated Replaced by {@link PrintOrderEnum#VERTICAL#getName()}.
	 */
	private static final String PRINT_ORDER_VERTICAL = PrintOrderEnum.VERTICAL.getName();
	/**
	 * @deprecated Replaced by {@link PrintOrderEnum#HORIZONTAL#getName()}.
	 */
	private static final String PRINT_ORDER_HORIZONTAL = PrintOrderEnum.HORIZONTAL.getName();

	/**
	 * @deprecated Replaced by {@link PrintOrderEnum}.
	 */
	private static Map printOrderMap;

	/**
	 * @deprecated Replaced by {@link PrintOrderEnum}.
	 */
	public static Map getPrintOrderMap()
	{
		if (printOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(PRINT_ORDER_VERTICAL,   new Byte(JRReport.PRINT_ORDER_VERTICAL));
			map.put(PRINT_ORDER_HORIZONTAL, new Byte(JRReport.PRINT_ORDER_HORIZONTAL));
			map.put(new Byte(JRReport.PRINT_ORDER_VERTICAL),   PRINT_ORDER_VERTICAL);
			map.put(new Byte(JRReport.PRINT_ORDER_HORIZONTAL), PRINT_ORDER_HORIZONTAL);
			printOrderMap = Collections.unmodifiableMap(map);
		}

		return printOrderMap;
	}

	/**
	 * @deprecated Replaced by {@link OrientationEnum#PORTRAIT#getName()}.
	 */
	private static final String ORIENTATION_PORTRAIT = OrientationEnum.PORTRAIT.getName();
	/**
	 * @deprecated Replaced by {@link OrientationEnum#LANDSCAPE#getName()}.
	 */
	private static final String ORIENTATION_LANDSCAPE = OrientationEnum.LANDSCAPE.getName();

	/**
	 * @deprecated Replaced by {@link OrientationEnum}.
	 */
	private static Map orientationMap;

	/**
	 * @deprecated Replaced by {@link OrientationEnum}.
	 */
	public static Map getOrientationMap()
	{
		if (orientationMap == null)
		{
			Map map = new HashMap(6);
			map.put(ORIENTATION_PORTRAIT,  new Byte(JRReport.ORIENTATION_PORTRAIT));
			map.put(ORIENTATION_LANDSCAPE, new Byte(JRReport.ORIENTATION_LANDSCAPE));
			map.put(new Byte(JRReport.ORIENTATION_PORTRAIT),  ORIENTATION_PORTRAIT);
			map.put(new Byte(JRReport.ORIENTATION_LANDSCAPE), ORIENTATION_LANDSCAPE);
			orientationMap = Collections.unmodifiableMap(map);
		}

		return orientationMap;
	}

	/**
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#NO_PAGES#getName()}.
	 */
	private static final String WHEN_NO_DATA_TYPE_NO_PAGES = WhenNoDataTypeEnum.NO_PAGES.getName();
	/**
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#BLANK_PAGE#getName()}.
	 */
	private static final String WHEN_NO_DATA_TYPE_BLANK_PAGE = WhenNoDataTypeEnum.BLANK_PAGE.getName();
	/**
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#ALL_SECTIONS_NO_DETAIL#getName()}.
	 */
	private static final String WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL = WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL.getName();
	/**
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum#NO_DATA_SECTION#getName()}.
	 */
	private static final String WHEN_NO_DATA_TYPE_NO_DATA_SECTION = WhenNoDataTypeEnum.NO_DATA_SECTION.getName();

	/**
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum}.
	 */
	private static Map whenNoDataTypeMap;

	/**
	 * @deprecated Replaced by {@link WhenNoDataTypeEnum}.
	 */
	public static Map getWhenNoDataTypeMap()
	{
		if (whenNoDataTypeMap == null)
		{
			Map map = new HashMap(11);
			map.put(WHEN_NO_DATA_TYPE_NO_PAGES,               new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES));
			map.put(WHEN_NO_DATA_TYPE_BLANK_PAGE,             new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE));
			map.put(WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL, new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL));
			map.put(WHEN_NO_DATA_TYPE_NO_DATA_SECTION,        new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION));
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_PAGES),               WHEN_NO_DATA_TYPE_NO_PAGES);
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_BLANK_PAGE),             WHEN_NO_DATA_TYPE_BLANK_PAGE);
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL), WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
			map.put(new Byte(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION),        WHEN_NO_DATA_TYPE_NO_DATA_SECTION);
			whenNoDataTypeMap = Collections.unmodifiableMap(map);
		}

		return whenNoDataTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum#NOW#getName()}.
	 */
	private static final String EVALUATION_TIME_NOW = EvaluationTimeEnum.NOW.getName();
	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum#REPORT#getName()}.
	 */
	private static final String EVALUATION_TIME_REPORT = EvaluationTimeEnum.REPORT.getName();
	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum#PAGE#getName()}.
	 */
	private static final String EVALUATION_TIME_PAGE = EvaluationTimeEnum.PAGE.getName();
	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum#COLUMN#getName()}.
	 */
	private static final String EVALUATION_TIME_COLUMN = EvaluationTimeEnum.COLUMN.getName();
	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum#GROUP#getName()}.
	 */
	private static final String EVALUATION_TIME_GROUP = EvaluationTimeEnum.GROUP.getName();
	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum#BAND#getName()}.
	 */
	private static final String EVALUATION_TIME_BAND = EvaluationTimeEnum.BAND.getName();
	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum#AUTO#getName()}.
	 */
	private static final String EVALUATION_TIME_AUTO = EvaluationTimeEnum.AUTO.getName();

	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum}.
	 */
	private static Map evaluationTimeMap;

	/**
	 * @deprecated Replaced by {@link EvaluationTimeEnum}.
	 */
	public static Map getEvaluationTimeMap()
	{
		if (evaluationTimeMap == null)
		{
			Map map = new HashMap(19);
			map.put(EVALUATION_TIME_NOW,    new Byte(JRExpression.EVALUATION_TIME_NOW));
			map.put(EVALUATION_TIME_REPORT, new Byte(JRExpression.EVALUATION_TIME_REPORT));
			map.put(EVALUATION_TIME_PAGE,   new Byte(JRExpression.EVALUATION_TIME_PAGE));
			map.put(EVALUATION_TIME_COLUMN, new Byte(JRExpression.EVALUATION_TIME_COLUMN));
			map.put(EVALUATION_TIME_GROUP,  new Byte(JRExpression.EVALUATION_TIME_GROUP));
			map.put(EVALUATION_TIME_BAND,  new Byte(JRExpression.EVALUATION_TIME_BAND));
			map.put(EVALUATION_TIME_AUTO,  new Byte(JRExpression.EVALUATION_TIME_AUTO));
			map.put(new Byte(JRExpression.EVALUATION_TIME_NOW),    EVALUATION_TIME_NOW);
			map.put(new Byte(JRExpression.EVALUATION_TIME_REPORT), EVALUATION_TIME_REPORT);
			map.put(new Byte(JRExpression.EVALUATION_TIME_PAGE),   EVALUATION_TIME_PAGE);
			map.put(new Byte(JRExpression.EVALUATION_TIME_COLUMN), EVALUATION_TIME_COLUMN);
			map.put(new Byte(JRExpression.EVALUATION_TIME_GROUP),  EVALUATION_TIME_GROUP);
			map.put(new Byte(JRExpression.EVALUATION_TIME_BAND),  EVALUATION_TIME_BAND);
			map.put(new Byte(JRExpression.EVALUATION_TIME_AUTO),  EVALUATION_TIME_AUTO);
			evaluationTimeMap = Collections.unmodifiableMap(map);
		}

		return evaluationTimeMap;
	}

	/**
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#NONE#getName()}.
	 */
	private static final String HYPERLINK_TYPE_NONE = HyperlinkTypeEnum.NONE.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#NONE#getName()}.
	 */
	private static final String HYPERLINK_TYPE_REFERENCE = HyperlinkTypeEnum.REFERENCE.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#NONE#getName()}.
	 */
	private static final String HYPERLINK_TYPE_LOCAL_ANCHOR = HyperlinkTypeEnum.LOCAL_ANCHOR.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#NONE#getName()}.
	 */
	private static final String HYPERLINK_TYPE_LOCAL_PAGE = HyperlinkTypeEnum.LOCAL_PAGE.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#NONE#getName()}.
	 */
	private static final String HYPERLINK_TYPE_REMOTE_ANCHOR = HyperlinkTypeEnum.REMOTE_ANCHOR.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTypeEnum#NONE#getName()}.
	 */
	private static final String HYPERLINK_TYPE_REMOTE_PAGE = HyperlinkTypeEnum.REMOTE_PAGE.getName();

	/**
	 * @deprecated Replaced by {@link HyperlinkTypeEnum}.
	 */
	private static Map hyperlinkTypeMap;


	/**
	 * @deprecated Replaced by {@link JRHyperlinkHelper#getHyperlinkType(String)}.
	 */
	public static Map getHyperlinkTypeMap()
	{
		if (hyperlinkTypeMap == null)
		{
			Map map = new HashMap(16);
			map.put(HYPERLINK_TYPE_NONE,          new Byte(JRHyperlink.HYPERLINK_TYPE_NONE));
			map.put(HYPERLINK_TYPE_REFERENCE,     new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE));
			map.put(HYPERLINK_TYPE_LOCAL_ANCHOR,  new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR));
			map.put(HYPERLINK_TYPE_LOCAL_PAGE,    new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE));
			map.put(HYPERLINK_TYPE_REMOTE_ANCHOR, new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR));
			map.put(HYPERLINK_TYPE_REMOTE_PAGE,   new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE));
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_NONE),          HYPERLINK_TYPE_NONE);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REFERENCE),     HYPERLINK_TYPE_REFERENCE);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR),  HYPERLINK_TYPE_LOCAL_ANCHOR);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE),    HYPERLINK_TYPE_LOCAL_PAGE);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR), HYPERLINK_TYPE_REMOTE_ANCHOR);
			map.put(new Byte(JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE),   HYPERLINK_TYPE_REMOTE_PAGE);
			hyperlinkTypeMap = Collections.unmodifiableMap(map);
		}

		return hyperlinkTypeMap;
	}

	/**
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#SELF#getName()}.
	 */
	private static final String HYPERLINK_TARGET_SELF = HyperlinkTargetEnum.SELF.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#BLANK#getName()}.
	 */
	private static final String HYPERLINK_TARGET_BLANK = HyperlinkTargetEnum.BLANK.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#PARENT#getName()}.
	 */
	private static final String HYPERLINK_TARGET_PARENT = HyperlinkTargetEnum.PARENT.getName();
	/**
	 * @deprecated Replaced by {@link HyperlinkTargetEnum#TOP#getName()}.
	 */
	private static final String HYPERLINK_TARGET_TOP = HyperlinkTargetEnum.TOP.getName();

	/**
	 * @deprecated Replaced by {@link HyperlinkTargetEnum}.
	 */
	private static Map hyperlinkTargetMap;

	/**
	 * @deprecated Replaced by {@link JRHyperlinkHelper#getHyperlinkTarget(String)}.
	 */
	public static Map getHyperlinkTargetMap()
	{
		if (hyperlinkTargetMap == null)
		{
			Map map = new HashMap(11);
			map.put(HYPERLINK_TARGET_SELF,  new Byte(JRHyperlink.HYPERLINK_TARGET_SELF));
			map.put(HYPERLINK_TARGET_BLANK, new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK));
			map.put(HYPERLINK_TARGET_PARENT, new Byte(JRHyperlink.HYPERLINK_TARGET_PARENT));
			map.put(HYPERLINK_TARGET_TOP, new Byte(JRHyperlink.HYPERLINK_TARGET_TOP));
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_SELF),  HYPERLINK_TARGET_SELF);
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_BLANK), HYPERLINK_TARGET_BLANK);
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_PARENT), HYPERLINK_TARGET_PARENT);
			map.put(new Byte(JRHyperlink.HYPERLINK_TARGET_TOP), HYPERLINK_TARGET_TOP);
			hyperlinkTargetMap = Collections.unmodifiableMap(map);
		}

		return hyperlinkTargetMap;
	}


	/**
	 * @deprecated Replaced by {@link EdgeEnum#TOP#getName()}.
	 */
	private static final String EDGE_TOP = EdgeEnum.TOP.getName();
	/**
	 * @deprecated Replaced by {@link EdgeEnum#BOTTOM#getName()}.
	 */
	private static final String EDGE_BOTTOM = EdgeEnum.BOTTOM.getName();
	/**
	 * @deprecated Replaced by {@link EdgeEnum#LEFT#getName()}.
	 */
	private static final String EDGE_LEFT = EdgeEnum.LEFT.getName();
	/**
	 * @deprecated Replaced by {@link EdgeEnum#RIGHT#getName()}.
	 */
	private static final String EDGE_RIGHT = EdgeEnum.RIGHT.getName();

	/**
	 * @deprecated Replaced by {@link EdgeEnum}.
	 */
	private static Map chartEdgeMap;

	/**
	 * @deprecated Replaced by {@link EdgeEnum}.
	 */
	public static Map getChartEdgeMap()
	{
		if (chartEdgeMap == null)
		{
			Map map = new HashMap(11);
			map.put(EDGE_TOP,    new Byte(JRChart.EDGE_TOP));
			map.put(EDGE_BOTTOM, new Byte(JRChart.EDGE_BOTTOM));
			map.put(EDGE_LEFT,   new Byte(JRChart.EDGE_LEFT));
			map.put(EDGE_RIGHT,  new Byte(JRChart.EDGE_RIGHT));
			map.put(new Byte(JRChart.EDGE_TOP),    EDGE_TOP);
			map.put(new Byte(JRChart.EDGE_BOTTOM), EDGE_BOTTOM);
			map.put(new Byte(JRChart.EDGE_LEFT),   EDGE_LEFT);
			map.put(new Byte(JRChart.EDGE_RIGHT),  EDGE_RIGHT);
			chartEdgeMap = Collections.unmodifiableMap(map);
		}

		return chartEdgeMap;
	}

	/**
	 * @deprecated Replaced by {@link #getChartEdgeMap()}.
	 */
	public static Map getChartTitlePositionMap()
	{
		return getChartEdgeMap();
	}
	
	/**
	 * @deprecated Replaced by {@link PlotOrientationEnum#HORIZONTAL#getName()}.
	 */
	private static final String ORIENTATION_HORIZONTAL = PlotOrientationEnum.HORIZONTAL.getName();
	/**
	 * @deprecated Replaced by {@link PlotOrientationEnum#HORIZONTAL#getName()}.
	 */
	private static final String ORIENTATION_VERTICAL = PlotOrientationEnum.VERTICAL.getName();

	/**
	 * @deprecated Replaced by {@link PlotOrientationEnum}.
	 */
	private static Map plotOrientationMap;

	/**
	 * @deprecated Replaced by {@link PlotOrientationEnum}.
	 */
	public static Map getPlotOrientationMap()
	{
		if (plotOrientationMap == null)
		{
			Map map = new HashMap(6);
			map.put(ORIENTATION_HORIZONTAL, PlotOrientation.HORIZONTAL);
			map.put(ORIENTATION_VERTICAL,   PlotOrientation.VERTICAL);
			map.put(PlotOrientation.HORIZONTAL, ORIENTATION_HORIZONTAL);
			map.put(PlotOrientation.VERTICAL,   ORIENTATION_VERTICAL);
			plotOrientationMap = Collections.unmodifiableMap(map);
		}

		return plotOrientationMap;
	}

	/**
	 * @deprecated Replaced by {@link SortOrderEnum#ASCENDING#getName()}.
	 */
	private static final String SORT_ORDER_ASCENDING = SortOrderEnum.ASCENDING.getName();
	/**
	 * @deprecated Replaced by {@link SortOrderEnum#DESCENDING#getName()}.
	 */
	private static final String SORT_ORDER_DESCENDING = SortOrderEnum.DESCENDING.getName();

	/**
	 * @deprecated Replaced by {@link SortOrderEnum}.
	 */
	private static Map sortOrderMap;

	/**
	 * @deprecated Replaced by {@link SortOrderEnum}.
	 */
	public static Map getSortOrderMap()
	{
		if (sortOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(SORT_ORDER_ASCENDING,  new Byte(JRSortField.SORT_ORDER_ASCENDING));
			map.put(SORT_ORDER_DESCENDING, new Byte(JRSortField.SORT_ORDER_DESCENDING));
			map.put(new Byte(JRSortField.SORT_ORDER_ASCENDING),  SORT_ORDER_ASCENDING);
			map.put(new Byte(JRSortField.SORT_ORDER_DESCENDING), SORT_ORDER_DESCENDING);
			sortOrderMap = Collections.unmodifiableMap(map);
		}

		return sortOrderMap;
	}


	/**
	 * @deprecated Replaced by {@link ScaleTypeEnum#ON_BOTH_AXES#getName()}.
	 */
	private static final String SCALE_ON_BOTH_AXES = ScaleTypeEnum.ON_BOTH_AXES.getName();
	/**
	 * @deprecated Replaced by {@link ScaleTypeEnum#ON_DOMAIN_AXIS#getName()}.
	 */
	private static final String SCALE_ON_DOMAIN_AXIS = ScaleTypeEnum.ON_DOMAIN_AXIS.getName();
	/**
	 * @deprecated Replaced by {@link ScaleTypeEnum#ON_RANGE_AXIS#getName()}.
	 */
	private static final String SCALE_ON_RANGE_AXIS = ScaleTypeEnum.ON_RANGE_AXIS.getName();

	/**
	 * @deprecated Replaced by {@link ScaleTypeEnum}.
	 */
	private static Map scaleTypeMap;

	/**
	 * @deprecated Replaced by {@link ScaleTypeEnum}.
	 */
	public static Map  getScaleTypeMap(){
		if( scaleTypeMap == null ){
			Map map = new HashMap( 8 );
			map.put( SCALE_ON_BOTH_AXES, Integer.valueOf( XYBubbleRenderer.SCALE_ON_BOTH_AXES ));
			map.put( SCALE_ON_DOMAIN_AXIS, Integer.valueOf( XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS ));
			map.put( SCALE_ON_RANGE_AXIS, Integer.valueOf( XYBubbleRenderer.SCALE_ON_RANGE_AXIS ));
			map.put( Integer.valueOf( XYBubbleRenderer.SCALE_ON_BOTH_AXES ), SCALE_ON_BOTH_AXES );
			map.put( Integer.valueOf( XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS ), SCALE_ON_DOMAIN_AXIS );
			map.put( Integer.valueOf( XYBubbleRenderer.SCALE_ON_RANGE_AXIS ), SCALE_ON_RANGE_AXIS );
			scaleTypeMap = Collections.unmodifiableMap(map);
		}

		return scaleTypeMap;
	}



	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#YEAR#getName()}.
	 */
	private static final String TIME_PERIOD_YEAR = TimePeriodEnum.YEAR.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#QUARTER#getName()}.
	 */
	private static final String TIME_PERIOD_QUARTER = TimePeriodEnum.QUARTER.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#MONTH#getName()}.
	 */
	private static final String TIME_PERIOD_MONTH = TimePeriodEnum.MONTH.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#WEEK#getName()}.
	 */
	private static final String TIME_PERIOD_WEEK = TimePeriodEnum.WEEK.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#DAY#getName()}.
	 */
	private static final String TIME_PERIOD_DAY = TimePeriodEnum.DAY.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#HOUR#getName()}.
	 */
	private static final String TIME_PERIOD_HOUR = TimePeriodEnum.HOUR.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#MINUTE#getName()}.
	 */
	private static final String TIME_PERIOD_MINUTE = TimePeriodEnum.MINUTE.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#SECOND#getName()}.
	 */
	private static final String TIME_PERIOD_SECOND = TimePeriodEnum.SECOND.getName();
	/**
	 * @deprecated Replaced by {@link TimePeriodEnum#MILLISECOND#getName()}.
	 */
	private static final String TIME_PERIOD_MILISECOND = TimePeriodEnum.MILLISECOND.getName();

	/**
	 * @deprecated Replaced by {@link TimePeriodEnum}.
	 */
	public static Class getTimePeriod( String timePeriod ) {
		if( timePeriod.equals( TIME_PERIOD_YEAR ) ){
			return Year.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_QUARTER )){
			return Quarter.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_MONTH )){
			return Month.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_WEEK )){
			return Week.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_DAY )) {
			return Day.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_HOUR )){
			return Hour.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_MINUTE )){
			return Minute.class;
		}
		else if( timePeriod.equals( TIME_PERIOD_SECOND )){
			return Second.class;
		}
		else {
			return Millisecond.class;
		}

	}

	/**
	 * @deprecated Replaced by {@link TimePeriodEnum}.
	 */
	public static String getTimePeriodName( Class clazz  ){
		if( clazz.equals( Year.class )){
			return TIME_PERIOD_YEAR;
		}
		else if ( clazz.equals( Quarter.class )){
			return TIME_PERIOD_QUARTER;
		}
		else if( clazz.equals( Month.class )){
			return TIME_PERIOD_MONTH;
		}
		else if( clazz.equals( Week.class )){
			return TIME_PERIOD_WEEK;
		}
		else if( clazz.equals( Day.class )){
			return TIME_PERIOD_DAY;
		}
		else if( clazz.equals( Hour.class )){
			return TIME_PERIOD_HOUR;
		}
		else if( clazz.equals( Minute.class )){
			return TIME_PERIOD_MINUTE;
		}
		else if( clazz.equals( Second.class )){
			return TIME_PERIOD_SECOND;
		}
		else {
			return TIME_PERIOD_MILISECOND;
		}
	}


	/**
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#NULL#getName()}.
	 */
	private static final String WHEN_RESOURCE_MISSING_TYPE_NULL = WhenResourceMissingTypeEnum.NULL.getName();
	/**
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#EMPTY#getName()}.
	 */
	private static final String WHEN_RESOURCE_MISSING_TYPE_EMPTY = WhenResourceMissingTypeEnum.EMPTY.getName();
	/**
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#KEY#getName()}.
	 */
	private static final String WHEN_RESOURCE_MISSING_TYPE_KEY = WhenResourceMissingTypeEnum.KEY.getName();
	/**
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum#ERROR#getName()}.
	 */
	private static final String WHEN_RESOURCE_MISSING_TYPE_ERROR = WhenResourceMissingTypeEnum.ERROR.getName();

	/**
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum}.
	 */
	private static Map whenResourceMissingTypeMap;

	/**
	 * @deprecated Replaced by {@link WhenResourceMissingTypeEnum}.
	 */
	public static Map getWhenResourceMissingTypeMap()
	{
		if (whenResourceMissingTypeMap == null)
		{
			Map map = new HashMap(11);
			map.put(WHEN_RESOURCE_MISSING_TYPE_NULL, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL));
			map.put(WHEN_RESOURCE_MISSING_TYPE_EMPTY, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY));
			map.put(WHEN_RESOURCE_MISSING_TYPE_KEY, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY));
			map.put(WHEN_RESOURCE_MISSING_TYPE_ERROR, new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR));
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_NULL), WHEN_RESOURCE_MISSING_TYPE_NULL);
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_EMPTY), WHEN_RESOURCE_MISSING_TYPE_EMPTY);
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_KEY), WHEN_RESOURCE_MISSING_TYPE_KEY);
			map.put(new Byte(JRReport.WHEN_RESOURCE_MISSING_TYPE_ERROR), WHEN_RESOURCE_MISSING_TYPE_ERROR);
			whenResourceMissingTypeMap = Collections.unmodifiableMap(map);
		}

		return whenResourceMissingTypeMap;
	}


	/**
	 * @deprecated Replaced by {@link MeterShapeEnum#CHORD#getName()}
	 */
	private static final String METER_SHAPE_CHORD = "chord";
	/**
	 * @deprecated Replaced by {@link MeterShapeEnum#CIRCLE#getName()}
	 */
	private static final String METER_SHAPE_CIRCLE = "circle";
	/**
	 * @deprecated Replaced by {@link MeterShapeEnum#PIE#getName()}
	 */
	private static final String METER_SHAPE_PIE = "pie";
	/**
	 * @deprecated Replaced by {@link MeterShapeEnum#DIAL#getName()}
	 */
	private static final String METER_SHAPE_DIAL = "dial";

	/**
	 * @deprecated Replaced by {@link MeterShapeEnum}
	 */
	private static Map meterShapeMap;

	/**
	 * @deprecated Replaced by {@link MeterShapeEnum}
	 */
	public static Map getMeterShapeMap()
	{
		if (meterShapeMap == null)
		{
			Map map = new HashMap(11);
			map.put(METER_SHAPE_CHORD, new Byte(JRMeterPlot.SHAPE_CHORD));
			map.put(METER_SHAPE_CIRCLE, new Byte(JRMeterPlot.SHAPE_CIRCLE));
			map.put(METER_SHAPE_PIE, new Byte(JRMeterPlot.SHAPE_PIE));
			map.put(METER_SHAPE_DIAL, new Byte(JRMeterPlot.SHAPE_DIAL));
			map.put(new Byte(JRMeterPlot.SHAPE_CHORD), METER_SHAPE_CHORD);
			map.put(new Byte(JRMeterPlot.SHAPE_CIRCLE), METER_SHAPE_CIRCLE);
			map.put(new Byte(JRMeterPlot.SHAPE_PIE), METER_SHAPE_PIE);
			map.put(new Byte(JRMeterPlot.SHAPE_DIAL), METER_SHAPE_DIAL);
			meterShapeMap = Collections.unmodifiableMap(map);
		}

		return meterShapeMap;
	}


	/**
	 * @deprecated Replaced by {@link ValueLocationEnum#NONE#getName()}
	 */
	private static final String THERMOMETER_VALUE_LOCATION_NONE = "none";
	/**
	 * @deprecated Replaced by {@link ValueLocationEnum#LEFT#getName()}
	 */
	private static final String THERMOMETER_VALUE_LOCATION_LEFT = "left";
	/**
	 * @deprecated Replaced by {@link ValueLocationEnum#RIGHT#getName()}
	 */
	private static final String THERMOMETER_VALUE_LOCATION_RIGHT = "right";
	/**
	 * @deprecated Replaced by {@link ValueLocationEnum#BULB#getName()}
	 */
	private static final String THERMOMETER_VALUE_LOCATION_BULB = "bulb";

	/**
	 * @deprecated Replaced by {@link ValueLocationEnum}
	 */
	private static Map thermometerValueLocationMap;

	/**
	 * @deprecated Replaced by {@link ValueLocationEnum}
	 */
	public static Map getThermometerValueLocationMap()
	{
		if (thermometerValueLocationMap == null)
		{
			Map map = new HashMap(11);
			map.put(THERMOMETER_VALUE_LOCATION_NONE, new Byte(JRThermometerPlot.LOCATION_NONE));
			map.put(THERMOMETER_VALUE_LOCATION_LEFT, new Byte(JRThermometerPlot.LOCATION_LEFT));
			map.put(THERMOMETER_VALUE_LOCATION_RIGHT, new Byte(JRThermometerPlot.LOCATION_RIGHT));
			map.put(THERMOMETER_VALUE_LOCATION_BULB, new Byte(JRThermometerPlot.LOCATION_BULB));
			map.put(new Byte(JRThermometerPlot.LOCATION_NONE), THERMOMETER_VALUE_LOCATION_NONE);
			map.put(new Byte(JRThermometerPlot.LOCATION_LEFT), THERMOMETER_VALUE_LOCATION_LEFT);
			map.put(new Byte(JRThermometerPlot.LOCATION_RIGHT), THERMOMETER_VALUE_LOCATION_RIGHT);
			map.put(new Byte(JRThermometerPlot.LOCATION_BULB), THERMOMETER_VALUE_LOCATION_BULB);
			thermometerValueLocationMap = Collections.unmodifiableMap(map);
		}

		return thermometerValueLocationMap;
	}


	/**
	 * @deprecated Replaced by {@link AxisPositionEnum#LEFT_OR_TOP#getName()}
	 */
	private static final String AXIS_POSITION_LEFT_OR_TOP = "leftOrTop";
	/**
	 * @deprecated Replaced by {@link AxisPositionEnum#RIGHT_OR_BOTTOM#getName()}
	 */
	private static final String AXIS_POSITION_RIGHT_OR_BOTTOM = "rightOrBottom";

	/**
	 * @deprecated Replaced by {@link AxisPositionEnum}
	 */
	private static Map axisPositionMap;

	/**
	 * @deprecated Replaced by {@link AxisPositionEnum}
	 */
	public static Map getAxisPositionMap()
	{
		 if (axisPositionMap == null)
		 {
			Map map = new HashMap(6);
			map.put(AXIS_POSITION_LEFT_OR_TOP, new Byte(JRChartAxis.POSITION_LEFT_OR_TOP));
			map.put(AXIS_POSITION_RIGHT_OR_BOTTOM, new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM));
			map.put(new Byte(JRChartAxis.POSITION_LEFT_OR_TOP), AXIS_POSITION_LEFT_OR_TOP);
			map.put(new Byte(JRChartAxis.POSITION_RIGHT_OR_BOTTOM), AXIS_POSITION_RIGHT_OR_BOTTOM);
			axisPositionMap = Collections.unmodifiableMap(map);
		 }

		 return axisPositionMap;
	}


	/**
	 * @deprecated Replaced by {@link SortOrderEnum#ASCENDING#getName()}.
	 */
	private static final String CROSSTAB_BUCKET_ORDER_ASCENDING = SortOrderEnum.ASCENDING.getName();
	/**
	 * @deprecated Replaced by {@link SortOrderEnum#DESCENDING#getName()}.
	 */
	private static final String CROSSTAB_BUCKET_ORDER_DESCENDING = SortOrderEnum.DESCENDING.getName();

	/**
	 * @deprecated Replaced by {@link SortOrderEnum}.
	 */
	private static Map crosstabBucketOrderMap;

	/**
	 * @deprecated Replaced by {@link SortOrderEnum}.
	 */
	public static Map getCrosstabBucketOrderMap()
	{
		if (crosstabBucketOrderMap == null)
		{
			Map map = new HashMap(6);
			map.put(CROSSTAB_BUCKET_ORDER_ASCENDING, new Byte(BucketDefinition.ORDER_ASCENDING));
			map.put(CROSSTAB_BUCKET_ORDER_DESCENDING, new Byte(BucketDefinition.ORDER_DESCENDING));
			map.put(new Byte(BucketDefinition.ORDER_ASCENDING), CROSSTAB_BUCKET_ORDER_ASCENDING);
			map.put(new Byte(BucketDefinition.ORDER_DESCENDING), CROSSTAB_BUCKET_ORDER_DESCENDING);
			crosstabBucketOrderMap = Collections.unmodifiableMap(map);
		}

		return crosstabBucketOrderMap;
	}


	/**
	 * @deprecated Replaced by {@link CrosstabPercentageEnum#NONE#getName()}.
	 */
	private static final String CROSSTAB_PERCENTAGE_NONE = CrosstabPercentageEnum.NONE.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabPercentageEnum#GRAND_TOTAL#getName()}.
	 */
	private static final String CROSSTAB_PERCENTAGE_GRAND_TOTAL = CrosstabPercentageEnum.GRAND_TOTAL.getName();

	/**
	 * @deprecated Replaced by {@link CrosstabPercentageEnum}.
	 */
	private static Map crosstabPercentageMap;


	/**
	 * @deprecated Replaced by {@link CrosstabPercentageEnum}.
	 */
	public static Map getCrosstabPercentageMap()
	{
		if (crosstabPercentageMap == null)
		{
			Map map = new HashMap(6);
			map.put(CROSSTAB_PERCENTAGE_NONE, new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_NONE));
			map.put(CROSSTAB_PERCENTAGE_GRAND_TOTAL, new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL));
			map.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_NONE), CROSSTAB_PERCENTAGE_NONE);
			map.put(new Byte(JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL), CROSSTAB_PERCENTAGE_GRAND_TOTAL);
			crosstabPercentageMap = Collections.unmodifiableMap(map);
		}

		return crosstabPercentageMap;
	}


	/**
	 * @deprecated Replaced by {@link CrosstabTotalPositionEnum#NONE#getName()}.
	 */
	private static final String CROSSTAB_TOTAL_POSITION_NONE = CrosstabTotalPositionEnum.NONE.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabTotalPositionEnum#START#getName()}.
	 */
	private static final String CROSSTAB_TOTAL_POSITION_START = CrosstabTotalPositionEnum.START.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabTotalPositionEnum#END#getName()}.
	 */
	private static final String CROSSTAB_TOTAL_POSITION_END = CrosstabTotalPositionEnum.END.getName();

	/**
	 * @deprecated Replaced by {@link CrosstabTotalPositionEnum}.
	 */
	private static Map crosstabTotalPositionMap;


	/**
	 * @deprecated Replaced by {@link CrosstabTotalPositionEnum}.
	 */
	public static Map getCrosstabTotalPositionMap()
	{
		if (crosstabTotalPositionMap == null)
		{
			Map map = new HashMap(8);
			map.put(CROSSTAB_TOTAL_POSITION_NONE, new Byte(BucketDefinition.TOTAL_POSITION_NONE));
			map.put(CROSSTAB_TOTAL_POSITION_START, new Byte(BucketDefinition.TOTAL_POSITION_START));
			map.put(CROSSTAB_TOTAL_POSITION_END, new Byte(BucketDefinition.TOTAL_POSITION_END));
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_NONE), CROSSTAB_TOTAL_POSITION_NONE);
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_START), CROSSTAB_TOTAL_POSITION_START);
			map.put(new Byte(BucketDefinition.TOTAL_POSITION_END), CROSSTAB_TOTAL_POSITION_END);
			crosstabTotalPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabTotalPositionMap;
	}


	/**
	 * @deprecated Replaced by {@link CrosstabRowPositionEnum#TOP#getName()}.
	 */
	private static final String CROSSTAB_ROW_POSITION_TOP = CrosstabRowPositionEnum.TOP.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabRowPositionEnum#MIDDLE#getName()}.
	 */
	private static final String CROSSTAB_ROW_POSITION_MIDDLE = CrosstabRowPositionEnum.MIDDLE.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabRowPositionEnum#BOTTOM#getName()}.
	 */
	private static final String CROSSTAB_ROW_POSITION_BOTTOM = CrosstabRowPositionEnum.BOTTOM.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabRowPositionEnum#STRETCH#getName()}.
	 */
	private static final String CROSSTAB_ROW_POSITION_STRETCH = CrosstabRowPositionEnum.STRETCH.getName();

	/**
	 * @deprecated Replaced by {@link CrosstabRowPositionEnum}.
	 */
	private static Map crosstabRowPositionMap;


	/**
	 * @deprecated Replaced by {@link CrosstabRowPositionEnum}.
	 */
	public static Map getCrosstabRowPositionMap()
	{
		if (crosstabRowPositionMap == null)
		{
			Map map = new HashMap(11);
			map.put(CROSSTAB_ROW_POSITION_TOP, new Byte(JRCellContents.POSITION_Y_TOP));
			map.put(CROSSTAB_ROW_POSITION_MIDDLE, new Byte(JRCellContents.POSITION_Y_MIDDLE));
			map.put(CROSSTAB_ROW_POSITION_BOTTOM, new Byte(JRCellContents.POSITION_Y_BOTTOM));
			map.put(CROSSTAB_ROW_POSITION_STRETCH, new Byte(JRCellContents.POSITION_Y_STRETCH));
			map.put(new Byte(JRCellContents.POSITION_Y_TOP), CROSSTAB_ROW_POSITION_TOP);
			map.put(new Byte(JRCellContents.POSITION_Y_MIDDLE), CROSSTAB_ROW_POSITION_MIDDLE);
			map.put(new Byte(JRCellContents.POSITION_Y_BOTTOM), CROSSTAB_ROW_POSITION_BOTTOM);
			map.put(new Byte(JRCellContents.POSITION_Y_STRETCH), CROSSTAB_ROW_POSITION_STRETCH);
			crosstabRowPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabRowPositionMap;
	}


	/**
	 * @deprecated Replaced by {@link CrosstabColumnPositionEnum#LEFT#getName()}.
	 */
	private static final String CROSSTAB_COLUMN_POSITION_LEFT = CrosstabColumnPositionEnum.LEFT.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabColumnPositionEnum#CENTER#getName()}.
	 */
	private static final String CROSSTAB_COLUMN_POSITION_CENTER = CrosstabColumnPositionEnum.CENTER.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabColumnPositionEnum#RIGHT#getName()}.
	 */
	private static final String CROSSTAB_COLUMN_POSITION_RIGHT = CrosstabColumnPositionEnum.RIGHT.getName();
	/**
	 * @deprecated Replaced by {@link CrosstabColumnPositionEnum#STRETCH#getName()}.
	 */
	private static final String CROSSTAB_COLUMN_POSITION_STRETCH = CrosstabColumnPositionEnum.STRETCH.getName();

	/**
	 * @deprecated Replaced by {@link CrosstabColumnPositionEnum}.
	 */
	private static Map crosstabColumnPositionMap;


	/**
	 * @deprecated Replaced by {@link CrosstabColumnPositionEnum}.
	 */
	public static Map getCrosstabColumnPositionMap()
	{
		if (crosstabColumnPositionMap == null)
		{
			Map map = new HashMap(11);
			map.put(CROSSTAB_COLUMN_POSITION_LEFT, new Byte(JRCellContents.POSITION_X_LEFT));
			map.put(CROSSTAB_COLUMN_POSITION_CENTER, new Byte(JRCellContents.POSITION_X_CENTER));
			map.put(CROSSTAB_COLUMN_POSITION_RIGHT, new Byte(JRCellContents.POSITION_X_RIGHT));
			map.put(CROSSTAB_COLUMN_POSITION_STRETCH, new Byte(JRCellContents.POSITION_X_STRETCH));
			map.put(new Byte(JRCellContents.POSITION_X_LEFT), CROSSTAB_COLUMN_POSITION_LEFT);
			map.put(new Byte(JRCellContents.POSITION_X_CENTER), CROSSTAB_COLUMN_POSITION_CENTER);
			map.put(new Byte(JRCellContents.POSITION_X_RIGHT), CROSSTAB_COLUMN_POSITION_RIGHT);
			map.put(new Byte(JRCellContents.POSITION_X_STRETCH), CROSSTAB_COLUMN_POSITION_STRETCH);
			crosstabColumnPositionMap = Collections.unmodifiableMap(map);
		}

		return crosstabColumnPositionMap;
	}

	
	/**
	 * @deprecated Replaced by {@link SplitTypeEnum#STRETCH#getName()}.
	 */
	private static final String SPLIT_TYPE_STRETCH = SplitTypeEnum.STRETCH.getName();
	/**
	 * @deprecated Replaced by {@link SplitTypeEnum#PREVENT#getName()}.
	 */
	private static final String SPLIT_TYPE_PREVENT = SplitTypeEnum.PREVENT.getName();
	/**
	 * @deprecated Replaced by {@link SplitTypeEnum#IMMEDIATE#getName()}.
	 */
	private static final String SPLIT_TYPE_IMMEDIATE = SplitTypeEnum.IMMEDIATE.getName();

	
	/**
	 * @deprecated Replaced by {@link SplitTypeEnum}.
	 */
	private static Map splitTypeMap;

	/**
	 * @deprecated Replaced by {@link SplitTypeEnum}.
	 */
	public static Map getSplitTypeMap()
	{
		if (splitTypeMap == null)
		{
			Map map = new HashMap(8);
			map.put(SPLIT_TYPE_STRETCH,   JRBand.SPLIT_TYPE_STRETCH);
			map.put(SPLIT_TYPE_PREVENT,   JRBand.SPLIT_TYPE_PREVENT);
			map.put(SPLIT_TYPE_IMMEDIATE, JRBand.SPLIT_TYPE_IMMEDIATE);
			map.put(JRBand.SPLIT_TYPE_STRETCH,   SPLIT_TYPE_STRETCH);
			map.put(JRBand.SPLIT_TYPE_PREVENT,   SPLIT_TYPE_PREVENT);
			map.put(JRBand.SPLIT_TYPE_IMMEDIATE, SPLIT_TYPE_IMMEDIATE);
			splitTypeMap = Collections.unmodifiableMap(map);
		}

		return splitTypeMap;
	}

	
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#UNKNOWN#getName()}.
	 */
	private static final String UNKNOWN = BandTypeEnum.UNKNOWN.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#BACKGROUND#getName()}.
	 */
	private static final String BACKGROUND = BandTypeEnum.BACKGROUND.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#PAGE_HEADER#getName()}.
	 */
	private static final String TITLE = BandTypeEnum.PAGE_HEADER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#PAGE_HEADER#getName()}.
	 */
	private static final String PAGE_HEADER = BandTypeEnum.PAGE_HEADER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#COLUMN_HEADER#getName()}.
	 */
	private static final String COLUMN_HEADER = BandTypeEnum.COLUMN_HEADER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#GROUP_HEADER#getName()}.
	 */
	private static final String GROUP_HEADER = BandTypeEnum.GROUP_HEADER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#GROUP_FOOTER#getName()}.
	 */
	private static final String DETAIL = BandTypeEnum.GROUP_FOOTER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#GROUP_FOOTER#getName()}.
	 */
	private static final String GROUP_FOOTER = BandTypeEnum.GROUP_FOOTER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#COLUMN_FOOTER#getName()}.
	 */
	private static final String COLUMN_FOOTER = BandTypeEnum.COLUMN_FOOTER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#PAGE_FOOTER#getName()}.
	 */
	private static final String PAGE_FOOTER = BandTypeEnum.PAGE_FOOTER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#LAST_PAGE_FOOTER#getName()}.
	 */
	private static final String LAST_PAGE_FOOTER = BandTypeEnum.LAST_PAGE_FOOTER.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#SUMMARY#getName()}.
	 */
	private static final String SUMMARY = BandTypeEnum.SUMMARY.getName();
	/**
	 * @deprecated Replaced by {@link BandTypeEnum#NO_DATA#getName()}.
	 */
	private static final String NO_DATA = BandTypeEnum.NO_DATA.getName();

	
	/**
	 * @deprecated Replaced by {@link BandTypeEnum}.
	 */
	private static Map bandTypeMap;

	/**
	 * @deprecated Replaced by {@link BandTypeEnum}.
	 */
	public static Map getBandTypeMap()
	{
		if (bandTypeMap == null)
		{
			Map map = new HashMap(35);
			map.put(UNKNOWN,          new Byte(JROrigin.UNKNOWN));
			map.put(BACKGROUND,       new Byte(JROrigin.BACKGROUND));
			map.put(TITLE,            new Byte(JROrigin.TITLE));
			map.put(PAGE_HEADER,      new Byte(JROrigin.PAGE_HEADER));
			map.put(COLUMN_HEADER,    new Byte(JROrigin.COLUMN_HEADER));
			map.put(GROUP_HEADER,     new Byte(JROrigin.GROUP_HEADER));
			map.put(DETAIL,           new Byte(JROrigin.DETAIL));
			map.put(GROUP_FOOTER,     new Byte(JROrigin.GROUP_FOOTER));
			map.put(COLUMN_FOOTER,    new Byte(JROrigin.COLUMN_FOOTER));
			map.put(PAGE_FOOTER,      new Byte(JROrigin.PAGE_FOOTER));
			map.put(LAST_PAGE_FOOTER, new Byte(JROrigin.LAST_PAGE_FOOTER));
			map.put(SUMMARY,          new Byte(JROrigin.SUMMARY));
			map.put(NO_DATA,          new Byte(JROrigin.NO_DATA));
			map.put(new Byte(JROrigin.UNKNOWN),          UNKNOWN);
			map.put(new Byte(JROrigin.BACKGROUND),       BACKGROUND);
			map.put(new Byte(JROrigin.TITLE),            TITLE);
			map.put(new Byte(JROrigin.PAGE_HEADER),      PAGE_HEADER);
			map.put(new Byte(JROrigin.COLUMN_HEADER),    COLUMN_HEADER);
			map.put(new Byte(JROrigin.GROUP_HEADER),     GROUP_HEADER);
			map.put(new Byte(JROrigin.DETAIL),           DETAIL);
			map.put(new Byte(JROrigin.GROUP_FOOTER),     GROUP_FOOTER);
			map.put(new Byte(JROrigin.COLUMN_FOOTER),    COLUMN_FOOTER);
			map.put(new Byte(JROrigin.PAGE_FOOTER),      PAGE_FOOTER);
			map.put(new Byte(JROrigin.LAST_PAGE_FOOTER), LAST_PAGE_FOOTER);
			map.put(new Byte(JROrigin.SUMMARY),          SUMMARY);
			map.put(new Byte(JROrigin.NO_DATA),          NO_DATA);
			bandTypeMap = Collections.unmodifiableMap(map);
		}

		return bandTypeMap;
	}

	
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#NORMAL#getName()}.
	 */
	private static final String FOOTER_POSITION_NORMAL = FooterPositionEnum.NORMAL.getName();
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#STACK_AT_BOTTOM#getName()}.
	 */
	private static final String FOOTER_POSITION_STACK_AT_BOTTOM = FooterPositionEnum.STACK_AT_BOTTOM.getName();
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#FORCE_AT_BOTTOM#getName()}.
	 */
	private static final String FOOTER_POSITION_FORCE_AT_BOTTOM = FooterPositionEnum.FORCE_AT_BOTTOM.getName();
	/**
	 * @deprecated Replaced by {@link FooterPositionEnum#COLLATE_AT_BOTTOM#getName()}.
	 */
	private static final String FOOTER_POSITION_COLLATE_AT_BOTTOM = FooterPositionEnum.COLLATE_AT_BOTTOM.getName();

	/**
	 * @deprecated Replaced by {@link FooterPositionEnum}.
	 */
	private static Map footerPositionMap;

	/**
	 * @deprecated Replaced by {@link FooterPositionEnum}.
	 */
	public static Map getFooterPositionMap()
	{
		if (footerPositionMap == null)
		{
			Map map = new HashMap(11);
			map.put(FOOTER_POSITION_NORMAL, new Byte(JRGroup.FOOTER_POSITION_NORMAL));
			map.put(FOOTER_POSITION_STACK_AT_BOTTOM, new Byte(JRGroup.FOOTER_POSITION_STACK_AT_BOTTOM));
			map.put(FOOTER_POSITION_FORCE_AT_BOTTOM, new Byte(JRGroup.FOOTER_POSITION_FORCE_AT_BOTTOM));
			map.put(FOOTER_POSITION_COLLATE_AT_BOTTOM, new Byte(JRGroup.FOOTER_POSITION_COLLATE_AT_BOTTOM));
			map.put(new Byte(JRGroup.FOOTER_POSITION_NORMAL), FOOTER_POSITION_NORMAL);
			map.put(new Byte(JRGroup.FOOTER_POSITION_STACK_AT_BOTTOM), FOOTER_POSITION_STACK_AT_BOTTOM);
			map.put(new Byte(JRGroup.FOOTER_POSITION_FORCE_AT_BOTTOM), FOOTER_POSITION_FORCE_AT_BOTTOM);
			map.put(new Byte(JRGroup.FOOTER_POSITION_COLLATE_AT_BOTTOM), FOOTER_POSITION_COLLATE_AT_BOTTOM);
			footerPositionMap = Collections.unmodifiableMap(map);
		}

		return footerPositionMap;
	}

	
	/**
	 * @deprecated Replaced by {@link JRColorUtil#getColor(String, Color)}.
	 */
	public static Color getColor(String strColor, Color defaultColor)
	{
		return JRColorUtil.getColor(strColor, defaultColor);
	}


	private JRXmlConstants()
	{
	}
	
}
