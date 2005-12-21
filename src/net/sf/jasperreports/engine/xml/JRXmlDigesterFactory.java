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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodSeries;
import net.sf.jasperreports.charts.design.JRDesignTimeSeries;
import net.sf.jasperreports.charts.design.JRDesignXySeries;
import net.sf.jasperreports.charts.design.JRDesignXyzSeries;
import net.sf.jasperreports.charts.xml.JRAreaChartFactory;
import net.sf.jasperreports.charts.xml.JRAreaPlotFactory;
import net.sf.jasperreports.charts.xml.JRBar3DChartFactory;
import net.sf.jasperreports.charts.xml.JRBar3DPlotFactory;
import net.sf.jasperreports.charts.xml.JRBarChartFactory;
import net.sf.jasperreports.charts.xml.JRBarPlotFactory;
import net.sf.jasperreports.charts.xml.JRBubbleChartFactory;
import net.sf.jasperreports.charts.xml.JRBubblePlotFactory;
import net.sf.jasperreports.charts.xml.JRCandlestickChartFactory;
import net.sf.jasperreports.charts.xml.JRCandlestickPlotFactory;
import net.sf.jasperreports.charts.xml.JRCategoryDatasetFactory;
import net.sf.jasperreports.charts.xml.JRCategorySeriesFactory;
import net.sf.jasperreports.charts.xml.JRHighLowChartFactory;
import net.sf.jasperreports.charts.xml.JRHighLowDatasetFactory;
import net.sf.jasperreports.charts.xml.JRHighLowPlotFactory;
import net.sf.jasperreports.charts.xml.JRLineChartFactory;
import net.sf.jasperreports.charts.xml.JRLinePlotFactory;
import net.sf.jasperreports.charts.xml.JRPie3DChartFactory;
import net.sf.jasperreports.charts.xml.JRPie3DPlotFactory;
import net.sf.jasperreports.charts.xml.JRPieChartFactory;
import net.sf.jasperreports.charts.xml.JRPieDatasetFactory;
import net.sf.jasperreports.charts.xml.JRPiePlotFactory;
import net.sf.jasperreports.charts.xml.JRScatterChartFactory;
import net.sf.jasperreports.charts.xml.JRScatterPlotFactory;
import net.sf.jasperreports.charts.xml.JRStackedBar3DChartFactory;
import net.sf.jasperreports.charts.xml.JRStackedBarChartFactory;
import net.sf.jasperreports.charts.xml.JRTimePeriodDatasetFactory;
import net.sf.jasperreports.charts.xml.JRTimePeriodSeriesFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesChartFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesDatasetFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesPlotFactory;
import net.sf.jasperreports.charts.xml.JRXyAreaChartFactory;
import net.sf.jasperreports.charts.xml.JRXyBarChartFactory;
import net.sf.jasperreports.charts.xml.JRXyDatasetFactory;
import net.sf.jasperreports.charts.xml.JRXyLineChartFactory;
import net.sf.jasperreports.charts.xml.JRXySeriesFactory;
import net.sf.jasperreports.charts.xml.JRXyzDatasetFactory;
import net.sf.jasperreports.charts.xml.JRXyzSeriesFactory;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.xml.JRCellContentsFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabBucketExpressionFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabBucketFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabCellFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabColumnGroupFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabDatasetFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabMeasureExpressionFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabMeasureFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabParameterFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabParameterValueExpressionFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabRowGroupFactory;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.SetNestedPropertiesRule;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * JRXmlDigesterFactory encapsulates the code necessary to construct and configure
 * a digester in order to prepare it for parsing JasperReports xml definition files.
 * 
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlDigesterFactory 
{


	/**
	 * 
	 */
	private JRXmlDigesterFactory() 
	{
		super();
	}


	/**
	 * Configures the given digester for parsing jasperreport xml report definition files.
	 */
	public static void configureDigester(Digester digester) throws SAXException, ParserConfigurationException 
	{
		boolean validating = JRProperties.getBooleanProperty(JRProperties.COMPILER_XML_VALIDATION);
		
		digester.setErrorHandler(new ErrorHandlerImpl());
		digester.setValidating(validating);
		digester.setFeature("http://xml.org/sax/features/validation", validating);
				
		/*   */
		digester.addFactoryCreate("jasperReport", JasperDesignFactory.class.getName());
		digester.addSetNext("jasperReport", "setJasperDesign", JasperDesign.class.getName());

		/*   */
		digester.addCallMethod("*/property", "setProperty", 2);
		digester.addCallParam("*/property", 0, "name");
		digester.addCallParam("*/property", 1, "value");

		/*   */
		digester.addCallMethod("jasperReport/import", "addImport", 1);
		digester.addCallParam("jasperReport/import", 0, "value");

		/*   */
		digester.addFactoryCreate("jasperReport/reportFont", JRReportFontFactory.class.getName());
		digester.addSetNext("jasperReport/reportFont", "addFont", JRReportFont.class.getName());
		
		/*   */
		digester.addFactoryCreate("jasperReport/style", JRStyleFactory.class.getName());
		digester.addSetNext("jasperReport/style", "addStyle", JRStyle.class.getName());

		digester.addFactoryCreate("jasperReport/style/conditionalStyle", JRConditionalStyleFactory.class.getName());
		digester.addFactoryCreate("jasperReport/style/conditionalStyle/conditionExpression", JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext("jasperReport/style/conditionalStyle/conditionExpression", "setConditionExpression", JRExpression.class.getName());
		digester.addCallMethod("jasperReport/style/conditionalStyle/conditionExpression", "setText", 0);
		digester.addFactoryCreate("jasperReport/style/conditionalStyle/style", JRConditionalStyleFillerFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/parameter", JRParameterFactory.class.getName());
		digester.addSetNext("*/parameter", "addParameter", JRParameter.class.getName());
		digester.addCallMethod("*/parameter/parameterDescription", "setDescription", 0);

		/*   */
		digester.addFactoryCreate("*/parameter/defaultValueExpression", JRDefaultValueExpressionFactory.class.getName());
		digester.addSetNext("*/parameter/defaultValueExpression", "setDefaultValueExpression", JRExpression.class.getName());
		digester.addCallMethod("*/parameter/defaultValueExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/queryString", JRQueryFactory.class.getName());
		digester.addSetNext("*/queryString", "setQuery", JRDesignQuery.class.getName());
		digester.addCallMethod("*/queryString", "setText", 0);
  		
		/*   */
		digester.addFactoryCreate("*/field", JRFieldFactory.class.getName());
		digester.addSetNext("*/field", "addField", JRField.class.getName());
		digester.addCallMethod("*/field/fieldDescription", "setDescription", 0);

		/*   */
		digester.addFactoryCreate("*/variable", JRVariableFactory.class.getName());
		digester.addSetNext("*/variable", "addVariable", JRDesignVariable.class.getName());

		/*   */
		digester.addFactoryCreate("*/variable/variableExpression", JRVariableExpressionFactory.class.getName());
		digester.addSetNext("*/variable/variableExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/variable/variableExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/variable/initialValueExpression", JRInitialValueExpressionFactory.class.getName());
		digester.addSetNext("*/variable/initialValueExpression", "setInitialValueExpression", JRExpression.class.getName());
		digester.addCallMethod("*/variable/initialValueExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/group", JRGroupFactory.class.getName());
		digester.addSetNext("*/group", "addGroup", JRDesignGroup.class.getName());

		/*   */
		digester.addFactoryCreate("*/group/groupExpression", JRExpressionFactory.ObjectExpressionFactory.class.getName());
		digester.addSetNext("*/group/groupExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/group/groupExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("jasperReport/background/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/background/band", "setBackground", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/title/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/title/band", "setTitle", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/pageHeader/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/pageHeader/band", "setPageHeader", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/columnHeader/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/columnHeader/band", "setColumnHeader", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/group/groupHeader/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/group/groupHeader/band", "setGroupHeader", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/detail/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/detail/band", "setDetail", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/group/groupFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/group/groupFooter/band", "setGroupFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/columnFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/columnFooter/band", "setColumnFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/pageFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/pageFooter/band", "setPageFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/lastPageFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/lastPageFooter/band", "setLastPageFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/summary/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/summary/band", "setSummary", JRBand.class.getName());

		/*   */
		digester.addFactoryCreate("*/band/printWhenExpression", JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext("*/band/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/band/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/line", JRLineFactory.class.getName());
		digester.addSetNext("*/line", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/reportElement", JRElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/reportElement/printWhenExpression", JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext("*/reportElement/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/reportElement/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/graphicElement", JRGraphicElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/rectangle", JRRectangleFactory.class.getName());
		digester.addSetNext("*/rectangle", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/ellipse", JREllipseFactory.class.getName());
		digester.addSetNext("*/ellipse", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/image", JRImageFactory.class.getName());
		digester.addSetNext("*/image", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/box", JRBoxFactory.class.getName());
//		digester.addSetNext("*/box", "setBox", JRBox.class.getName());

		/*   */
		digester.addFactoryCreate("*/image/imageExpression", JRImageExpressionFactory.class.getName());
		digester.addSetNext("*/image/imageExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/image/imageExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/staticText", JRStaticTextFactory.class.getName());
		digester.addSetNext("*/staticText", "addElement", JRDesignElement.class.getName());
		SetNestedPropertiesRule textRule = new SetNestedPropertiesRule(new String[]{"text", "reportElement", "box", "textElement"}, new String[]{"text"});
		textRule.setTrimData(false);
		textRule.setAllowUnknownChildElements(true);
		digester.addRule("*/staticText", textRule);


		/*   */
		digester.addFactoryCreate("*/textElement", JRTextElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/textElement/font", JRFontFactory.class.getName());
//		digester.addSetNext("*/textElement/font", "setFont", JRFont.class.getName());

		/*   */
		digester.addFactoryCreate("*/textField", JRTextFieldFactory.class.getName());
		digester.addSetNext("*/textField", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/textField/textFieldExpression", JRTextFieldExpressionFactory.class.getName());
		digester.addSetNext("*/textField/textFieldExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/textField/textFieldExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/anchorNameExpression", JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addSetNext("*/anchorNameExpression", "setAnchorNameExpression", JRExpression.class.getName());
		digester.addCallMethod("*/anchorNameExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkReferenceExpression", JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkReferenceExpression", "setHyperlinkReferenceExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkReferenceExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkAnchorExpression", JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkAnchorExpression", "setHyperlinkAnchorExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkAnchorExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkPageExpression", JRExpressionFactory.IntegerExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkPageExpression", "setHyperlinkPageExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkPageExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport", JRSubreportFactory.class.getName());
		digester.addSetNext("*/subreport", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter", JRSubreportParameterFactory.class.getName());
		digester.addSetNext("*/subreport/subreportParameter", "addParameter", JRSubreportParameter.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreport/returnValue", JRSubreportReturnValueFactory.class.getName());
		digester.addSetNext("*/subreport/returnValue", "addReturnValue", JRSubreportReturnValue.class.getName());

		/*   */
		digester.addFactoryCreate("*/parametersMapExpression", JRExpressionFactory.MapExpressionFactory.class.getName());
		digester.addSetNext("*/parametersMapExpression", "setParametersMapExpression", JRExpression.class.getName());
		digester.addCallMethod("*/parametersMapExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter/subreportParameterExpression", JRExpressionFactory.ObjectExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/subreportParameter/subreportParameterExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/subreportParameter/subreportParameterExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/connectionExpression", JRExpressionFactory.ConnectionExpressionFactory.class.getName());
		digester.addSetNext("*/connectionExpression", "setConnectionExpression", JRExpression.class.getName());
		digester.addCallMethod("*/connectionExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/dataSourceExpression", JRExpressionFactory.DataSourceExpressionFactory.class.getName());
		digester.addSetNext("*/dataSourceExpression", "setDataSourceExpression", JRExpression.class.getName());
		digester.addCallMethod("*/dataSourceExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportExpression", JRSubreportExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/subreportExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/subreportExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/elementGroup", JRElementGroupFactory.class.getName());
		digester.addSetNext("*/elementGroup", "addElementGroup", JRDesignElementGroup.class.getName());

		addChartRules(digester);

		addDatasetRules(digester);
		
		addCrosstabRules(digester);
		
		addFrameRules(digester);
	}


	/**
	 * 
	 */
	private static void addChartRules(Digester digester)
	{
		digester.addFactoryCreate("*/dataset", JRElementDatasetFactory.class.getName());
		
		digester.addFactoryCreate("*/plot", JRChartPlotFactory.class.getName());

		digester.addFactoryCreate("*/chart", JRChartFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle", JRChartFactory.JRChartTitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/font", JRFontFactory.class.getName());
		digester.addSetNext("*/chart/chartTitle/font", "setTitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/titleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/chart/chartTitle/titleExpression", "setTitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartTitle/titleExpression", "setText", 0);
		digester.addFactoryCreate("*/chart/chartSubtitle", JRChartFactory.JRChartSubtitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/font", JRFontFactory.class.getName());
		digester.addSetNext("*/chart/chartSubtitle/font", "setSubtitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/subtitleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/chart/chartSubtitle/subtitleExpression", "setSubtitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartSubtitle/subtitleExpression", "setText", 0);

		// pie charts
		digester.addFactoryCreate("*/pieChart", JRPieChartFactory.class.getName());
		digester.addSetNext("*/pieChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/pieChart/piePlot", JRPiePlotFactory.class.getName());

		digester.addFactoryCreate("*/pieDataset", JRPieDatasetFactory.class.getName());
		digester.addFactoryCreate("*/pieDataset/keyExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/pieDataset/keyExpression", "setKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/keyExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/pieDataset/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/pieDataset/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/valueExpression", "setText", 0);
		
		// pie 3D charts
		digester.addFactoryCreate("*/pie3DChart", JRPie3DChartFactory.class.getName());
		digester.addSetNext("*/pie3DChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/pie3DPlot", JRPie3DPlotFactory.class.getName());

		// bar charts
		digester.addFactoryCreate("*/barChart", JRBarChartFactory.class.getName());
		digester.addSetNext("*/barChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/barChart/barPlot", JRBarPlotFactory.class.getName());
		
		digester.addFactoryCreate( "*/barPlot/categoryAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/categoryAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/barPlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/valueAxisLabelExpression", "setText", 0 );
		
		
		
		// area charts
		digester.addFactoryCreate( "*/areaChart", JRAreaChartFactory.class.getName() );
		digester.addSetNext( "*/areaChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/areaChart/areaPlot", JRAreaPlotFactory.class.getName() );
		
		digester.addFactoryCreate( "*/xyAreaChart", JRXyAreaChartFactory.class.getName() );
		digester.addSetNext( "*/xyAreaChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/xyAreaChart/areaPlot", JRAreaPlotFactory.class.getName() );
		
		digester.addFactoryCreate( "*/areaPlot/categoryAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/categoryAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/areaPlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/valueAxisLabelExpression", "setText", 0 );
		
		// bar3d charts
		digester.addFactoryCreate( "*/bar3DChart", JRBar3DChartFactory.class.getName() );
		digester.addSetNext( "*/bar3DChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate("*/bar3DChart/bar3DPlot", JRBar3DPlotFactory.class.getName());
		
		digester.addFactoryCreate( "*/bar3DPlot/categoryAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/categoryAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/bar3DPlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/valueAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate("*/categoryDataset", JRCategoryDatasetFactory.class.getName());
		digester.addFactoryCreate("*/categoryDataset/categorySeries", JRCategorySeriesFactory.class.getName());
		digester.addSetNext("*/categoryDataset/categorySeries", "addCategorySeries", JRDesignCategorySeries.class.getName());

		//digester.addFactoryCreate("*/categorySeries", JRCategoryDatasetFactory.class.getName());
		digester.addFactoryCreate("*/categorySeries/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/categorySeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/categorySeries/categoryExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/categorySeries/categoryExpression", "setCategoryExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/categoryExpression", "setText", 0);
		digester.addFactoryCreate("*/categorySeries/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/categorySeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/categorySeries/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/categorySeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/categorySeries/valueExpression", "setText", 0);

		
		digester.addFactoryCreate( "*/xyzDataset", JRXyzDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/xyzDataset/xyzSeries", JRXyzSeriesFactory.class.getName() );
		digester.addSetNext( "*/xyzDataset/xyzSeries", "addXyzSeries", JRDesignXyzSeries.class.getName() );
		
		digester.addFactoryCreate( "*/xyzSeries/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/seriesExpression", "setText", 0 );
		digester.addFactoryCreate( "*/xyzSeries", JRXyzDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/xyzSeries/xValueExpression", JRExpressionFactory.NumberExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/xValueExpression", "setXValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/xValueExpression", "setText", 0 );
		digester.addFactoryCreate( "*/xyzSeries/yValueExpression", JRExpressionFactory.NumberExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/yValueExpression", "setYValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/yValueExpression", "setText", 0 );
		digester.addFactoryCreate( "*/xyzSeries/zValueExpression", JRExpressionFactory.NumberExpressionFactory.class );
		digester.addSetNext( "*/xyzSeries/zValueExpression", "setZValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/xyzSeries/zValueExpression", "setText", 0 );
		
		
		// time period dataset
		digester.addFactoryCreate( "*/timePeriodDataset", JRTimePeriodDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/timePeriodDataset/timePeriodSeries", JRTimePeriodSeriesFactory.class.getName() );
		digester.addSetNext( "*/timePeriodDataset/timePeriodSeries", "addTimePeriodSeries", JRDesignTimePeriodSeries.class.getName() );
		
		digester.addFactoryCreate("*/timePeriodSeries/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/timePeriodSeries/startDateExpression", JRExpressionFactory.DateExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/startDateExpression", "setStartDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/startDateExpression", "setText", 0);
		digester.addFactoryCreate("*/timePeriodSeries/endDateExpression", JRExpressionFactory.DateExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/endDateExpression", "setEndDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/endDateExpression", "setText", 0);
		digester.addFactoryCreate("*/timePeriodSeries/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/timePeriodSeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timePeriodSeries/valueExpression", "setText", 0);
		digester.addFactoryCreate( "*/timePeriodSeries/labelExpression", JRExpressionFactory.StringExpressionFactory.class );
		digester.addSetNext( "*/timePeriodSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timePeriodSeries/labelExpression", "setText", 0);
		
		digester.addFactoryCreate("*/timeSeriesChart", JRTimeSeriesChartFactory.class.getName());
		digester.addFactoryCreate("*/timeSeriesChart/timeSeriesPlot", JRTimeSeriesPlotFactory.class.getName());
		digester.addSetNext("*/timeSeriesChart", "addElement", JRDesignElement.class.getName());
		
		// add plot labels
		digester.addFactoryCreate( "*/timeSeriesPlot/timeAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext("*/timeSeriesPlot/timeAxisLabelExpression", "setTimeAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/timeSeriesPlot/timeAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/timeSeriesPlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/timeSeriesPlot/valueAxisLabelExpression", "setText", 0 );
			
		// XY bar charts
		digester.addFactoryCreate("*/xyBarChart", JRXyBarChartFactory.class.getName());
		digester.addSetNext("*/xyBarChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/xyBarChart/barPlot", JRBarPlotFactory.class.getName());

		
//		 time series dataset 
		digester.addFactoryCreate( "*/timeSeriesDataset", JRTimeSeriesDatasetFactory.class.getName() );
		digester.addFactoryCreate( "*/timeSeriesDataset/timeSeries", JRTimeSeriesFactory.class.getName());
		digester.addSetNext( "*/timeSeriesDataset/timeSeries", "addTimeSeries", JRDesignTimeSeries.class.getName() );
		
		digester.addFactoryCreate("*/timeSeries/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/timeSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/timeSeries/timePeriodExpression", JRExpressionFactory.DateExpressionFactory.class);
		digester.addSetNext("*/timeSeries/timePeriodExpression", "setTimePeriodExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/timePeriodExpression", "setText", 0);
		digester.addFactoryCreate("*/timeSeries/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/timeSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/timeSeries/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/timeSeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/timeSeries/valueExpression", "setText", 0);

		digester.addFactoryCreate("*/stackedBarChart", JRStackedBarChartFactory.class.getName());
		digester.addSetNext("*/stackedBarChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/stackedBarChart/barPlot", JRBarPlotFactory.class.getName());
		
		digester.addFactoryCreate( "*/lineChart", JRLineChartFactory.class.getName() );
		digester.addSetNext( "*/lineChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/linePlot", JRLinePlotFactory.class.getName() );
		
		
		//add plot labels
		digester.addFactoryCreate( "*/linePlot/categoryAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext("*/linePlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/linePlot/categoryAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/linePlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext("*/linePlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod(  "*/linePlot/valueAxisLabelExpression", "setText", 0 );
		
	
		digester.addFactoryCreate( "*/xyLineChart", JRXyLineChartFactory.class.getName() );
		digester.addSetNext( "*/xyLineChart", "addElement", JRDesignElement.class.getName() );
	
		digester.addFactoryCreate( "*/scatterChart", JRScatterChartFactory.class.getName() );
		digester.addSetNext( "*/scatterChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/scatterPlot", JRScatterPlotFactory.class.getName() );
		
		// add plot labels 
		digester.addFactoryCreate( "*/scatterPlot/xAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/xAxisLabelExpression", "setXAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*scatterPlot/xAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/scatterPlot/yAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/yAxisLabelExpression", "setYAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/yAxisLabelExpression", "setText", 0 );
		
	
		digester.addFactoryCreate("*/xyDataset", JRXyDatasetFactory.class.getName());
		digester.addFactoryCreate("*/xyDataset/xySeries", JRXySeriesFactory.class.getName());
		digester.addSetNext("*/xyDataset/xySeries", "addXySeries", JRDesignXySeries.class.getName());

		digester.addFactoryCreate("*/xySeries", JRXyDatasetFactory.class.getName());
		digester.addFactoryCreate("*/xySeries/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/xySeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/xySeries/xValueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/xySeries/xValueExpression", "setXValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/xValueExpression", "setText", 0);
		digester.addFactoryCreate("*/xySeries/yValueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/xySeries/yValueExpression", "setYValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/yValueExpression", "setText", 0);
		digester.addFactoryCreate("*/xySeries/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/xySeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/xySeries/labelExpression", "setText", 0);

		digester.addFactoryCreate("*/stackedBar3DChart", JRStackedBar3DChartFactory.class.getName());
		digester.addSetNext("*/stackedBar3DChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/stackedBar3DChart/bar3DPlot", JRBar3DPlotFactory.class.getName());
		
		digester.addFactoryCreate( "*/bubbleChart", JRBubbleChartFactory.class.getName() );
		digester.addSetNext( "*/bubbleChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/bubblePlot", JRBubblePlotFactory.class.getName() );
	
		// add plot labels 
		digester.addFactoryCreate( "*/bubblePlot/xAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/xAxisLabelExpression", "setXAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/xAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/bubblePlot/yAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/yAxisLabelExpression", "setYAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/yAxisLabelExpression", "setText", 0 );
		

		// high-low charts
		digester.addFactoryCreate("*/highLowChart", JRHighLowChartFactory.class.getName());
		digester.addSetNext("*/highLowChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/highLowChart/highLowPlot", JRHighLowPlotFactory.class.getName());
		
		digester.addFactoryCreate( "*/highLowPlot/timeAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/timeAxisLabelExpression", "setTimeAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/timeAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/highLowPlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/valueAxisLabelExpression", "setText", 0 );
		

		digester.addFactoryCreate("*/highLowDataset", JRHighLowDatasetFactory.class.getName());
		digester.addFactoryCreate("*/highLowDataset/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/dateExpression", JRExpressionFactory.DateExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/dateExpression", "setDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/dateExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/highExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/highExpression", "setHighExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/highExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/lowExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/lowExpression", "setLowExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/lowExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/openExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/openExpression", "setOpenExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/openExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/closeExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/closeExpression", "setCloseExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/closeExpression", "setText", 0);
		digester.addFactoryCreate("*/highLowDataset/volumeExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/highLowDataset/volumeExpression", "setVolumeExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/highLowDataset/volumeExpression", "setText", 0);

		// candlestick charts
		digester.addFactoryCreate("*/candlestickChart", JRCandlestickChartFactory.class);
		digester.addSetNext("*/candlestickChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/candlestickChart/candlestickPlot", JRCandlestickPlotFactory.class);
		
		digester.addFactoryCreate( "*/candlestickPlot/timeAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/timeAxisLabelExpression", "setTimeAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/timeAxisLabelExpression", "setText", 0 );
		
		digester.addFactoryCreate( "*/candlestickPlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/valueAxisLabelExpression", "setText", 0 );
	}


	private static void addDatasetRules(Digester digester)
	{
		String subDatasetPattern = "jasperReport/" + JRDatasetFactory.TAG_SUB_DATASET;
		digester.addFactoryCreate(subDatasetPattern, JRDatasetFactory.class.getName());
		digester.addSetNext(subDatasetPattern, "addDataset", JRDesignDataset.class.getName());
		
		String datasetRunPattern = "*/dataset/" + JRDatasetRunFactory.TAG_DATASET_RUN;
		digester.addFactoryCreate(datasetRunPattern, JRDatasetRunFactory.class.getName());
		digester.addSetNext(datasetRunPattern, "setDatasetRun", JRDatasetRun.class.getName());
		
		String datasetParamPattern = datasetRunPattern + "/" + JRDatasetRunParameterFactory.TAG_DATASET_PARAMETER;
		digester.addFactoryCreate(datasetParamPattern, JRDatasetRunParameterFactory.class.getName());
		digester.addSetNext(datasetParamPattern, "addParameter", JRDatasetParameter.class.getName());
		
		String datasetParamExprPattern = datasetParamPattern + "/" + JRDatasetRunParameterExpressionFactory.TAG_DATASET_PARAMETER_EXPRESSION;
		digester.addFactoryCreate(datasetParamExprPattern, JRDatasetRunParameterExpressionFactory.class.getName());
		digester.addSetNext(datasetParamExprPattern, "setExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetParamExprPattern, "setText", 0);
	}


	private static void addCrosstabRules(Digester digester)
	{
		digester.addFactoryCreate("*/crosstab", JRCrosstabFactory.class.getName());
		digester.addSetNext("*/crosstab", "addElement", JRDesignElement.class.getName());
		
		digester.addFactoryCreate("*/crosstab/crosstabParameter", JRCrosstabParameterFactory.class.getName());
		digester.addSetNext("*/crosstab/crosstabParameter", "addParameter", JRCrosstabParameter.class.getName());

		digester.addFactoryCreate("*/crosstab/crosstabParameter/parameterValueExpression", JRCrosstabParameterValueExpressionFactory.class.getName());
		digester.addSetNext("*/crosstab/crosstabParameter/parameterValueExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/crosstab/crosstabParameter/parameterValueExpression", "setText", 0);

		digester.addFactoryCreate("*/crosstab/crosstabDataset", JRCrosstabDatasetFactory.class.getName());
		
		digester.addFactoryCreate("*/crosstab/rowGroup", JRCrosstabRowGroupFactory.class.getName());		
		digester.addSetNext("*/crosstab/rowGroup", "addRowGroup", JRDesignCrosstabRowGroup.class.getName());		
		
		digester.addFactoryCreate("*/crosstab/rowGroup/crosstabRowHeader/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/rowGroup/crosstabRowHeader/cellContents", "setHeader", JRDesignCellContents.class.getName());
		
		digester.addFactoryCreate("*/crosstab/rowGroup/crosstabTotalRowHeader/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/rowGroup/crosstabTotalRowHeader/cellContents", "setTotalHeader", JRDesignCellContents.class.getName());
		
		digester.addFactoryCreate("*/crosstab/columnGroup", JRCrosstabColumnGroupFactory.class.getName());		
		digester.addSetNext("*/crosstab/columnGroup", "addColumnGroup", JRDesignCrosstabColumnGroup.class.getName());		
		
		digester.addFactoryCreate("*/crosstab/columnGroup/crosstabColumnHeader/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/columnGroup/crosstabColumnHeader/cellContents", "setHeader", JRDesignCellContents.class.getName());
		
		digester.addFactoryCreate("*/crosstab/columnGroup/crosstabTotalColumnHeader/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/columnGroup/crosstabTotalColumnHeader/cellContents", "setTotalHeader", JRDesignCellContents.class.getName());
		
		digester.addFactoryCreate("*/bucket", JRCrosstabBucketFactory.class.getName());		
		digester.addSetNext("*/bucket", "setBucket", JRDesignCrosstabBucket.class.getName());
		
		digester.addFactoryCreate("*/bucket/bucketExpression", JRCrosstabBucketExpressionFactory.class.getName());
		digester.addSetNext("*/bucket/bucketExpression", "setExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/bucket/bucketExpression", "setText", 0);
		
		digester.addFactoryCreate("*/bucket/comparatorExpression", JRExpressionFactory.ComparatorExpressionFactory.class.getName());
		digester.addSetNext("*/bucket/comparatorExpression", "setComparatorExpression", JRExpression.class.getName());
		digester.addCallMethod("*/bucket/comparatorExpression", "setText", 0);

		digester.addFactoryCreate("*/crosstab/measure", JRCrosstabMeasureFactory.class.getName());		
		digester.addSetNext("*/crosstab/measure", "addMeasure", JRCrosstabMeasure.class.getName());		
		
		digester.addFactoryCreate("*/crosstab/measure/measureExpression", JRCrosstabMeasureExpressionFactory.class.getName());
		digester.addSetNext("*/crosstab/measure/measureExpression", "setValueExpression", JRExpression.class.getName());
		digester.addCallMethod("*/crosstab/measure/measureExpression", "setText", 0);
		
		digester.addFactoryCreate("*/crosstab/crosstabCell", JRCrosstabCellFactory.class.getName());
		digester.addSetNext("*/crosstab/crosstabCell", "addCell", JRDesignCrosstabCell.class.getName());
		digester.addFactoryCreate("*/crosstab/crosstabCell/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/crosstabCell/cellContents", "setContents", JRDesignCellContents.class.getName());
		
		digester.addFactoryCreate("*/crosstab/whenNoDataCell/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/whenNoDataCell/cellContents", "setWhenNoDataCell", JRDesignCellContents.class.getName());
		
		digester.addFactoryCreate("*/crosstab/crosstabHeaderCell/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/crosstabHeaderCell/cellContents", "setHeaderCell", JRDesignCellContents.class.getName());
		
		//TODO style?
		digester.addFactoryCreate("*/cellContents/box", JRBaseBoxFactory.class.getName());
		digester.addSetNext("*/cellContents/box", "setBox", JRBox.class.getName());
	}


	private static void addFrameRules(Digester digester)
	{
		String framePattern = "*/" + JRFrameFactory.TAG_FRAME;
		digester.addFactoryCreate(framePattern, JRFrameFactory.class.getName());
		digester.addSetNext(framePattern, "addElement", JRDesignElement.class.getName());
	}
	
	
	/**
	 * Creates a new instance of digester. The created digester is ready for 
	 * parsing report definition files.
	 */	
	public static JRXmlDigester createDigester() throws ParserConfigurationException, SAXException
	{
		JRXmlDigester digester = new JRXmlDigester();
		configureDigester(digester);
		return digester;
	}
	

	/**
	 * 
	 */
	private static class ErrorHandlerImpl implements ErrorHandler 
	{
		/**
		 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
		 */
		public void error(SAXParseException exception) throws SAXException 
		{
			throw exception;
		}

		/**
		 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
		 */
		public void fatalError(SAXParseException exception) throws SAXException 
		{
			throw exception;
		}

		/**
		 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
		 */
		public void warning(SAXParseException exception) throws SAXException 
		{
			throw exception;
		}
	}
	
}
