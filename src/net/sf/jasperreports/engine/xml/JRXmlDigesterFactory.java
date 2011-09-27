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
package net.sf.jasperreports.engine.xml;

import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import net.sf.jasperreports.charts.JRChartAxis;
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
import net.sf.jasperreports.charts.xml.JRChartAxisFactory;
import net.sf.jasperreports.charts.xml.JRDataRangeFactory;
import net.sf.jasperreports.charts.xml.JRGanttChartFactory;
import net.sf.jasperreports.charts.xml.JRGanttDatasetFactory;
import net.sf.jasperreports.charts.xml.JRGanttSeriesFactory;
import net.sf.jasperreports.charts.xml.JRHighLowChartFactory;
import net.sf.jasperreports.charts.xml.JRHighLowDatasetFactory;
import net.sf.jasperreports.charts.xml.JRHighLowPlotFactory;
import net.sf.jasperreports.charts.xml.JRItemLabelFactory;
import net.sf.jasperreports.charts.xml.JRLineChartFactory;
import net.sf.jasperreports.charts.xml.JRLinePlotFactory;
import net.sf.jasperreports.charts.xml.JRMeterChartFactory;
import net.sf.jasperreports.charts.xml.JRMeterIntervalFactory;
import net.sf.jasperreports.charts.xml.JRMeterPlotFactory;
import net.sf.jasperreports.charts.xml.JRMultiAxisChartFactory;
import net.sf.jasperreports.charts.xml.JRMultiAxisPlotFactory;
import net.sf.jasperreports.charts.xml.JRPie3DChartFactory;
import net.sf.jasperreports.charts.xml.JRPie3DPlotFactory;
import net.sf.jasperreports.charts.xml.JRPieChartFactory;
import net.sf.jasperreports.charts.xml.JRPieDatasetFactory;
import net.sf.jasperreports.charts.xml.JRPiePlotFactory;
import net.sf.jasperreports.charts.xml.JRPieSeriesFactory;
import net.sf.jasperreports.charts.xml.JRScatterChartFactory;
import net.sf.jasperreports.charts.xml.JRScatterPlotFactory;
import net.sf.jasperreports.charts.xml.JRStackedAreaChartFactory;
import net.sf.jasperreports.charts.xml.JRStackedBar3DChartFactory;
import net.sf.jasperreports.charts.xml.JRStackedBarChartFactory;
import net.sf.jasperreports.charts.xml.JRThermometerChartFactory;
import net.sf.jasperreports.charts.xml.JRThermometerPlotFactory;
import net.sf.jasperreports.charts.xml.JRTimePeriodDatasetFactory;
import net.sf.jasperreports.charts.xml.JRTimePeriodSeriesFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesChartFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesDatasetFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesFactory;
import net.sf.jasperreports.charts.xml.JRTimeSeriesPlotFactory;
import net.sf.jasperreports.charts.xml.JRValueDatasetFactory;
import net.sf.jasperreports.charts.xml.JRValueDisplayFactory;
import net.sf.jasperreports.charts.xml.JRXyAreaChartFactory;
import net.sf.jasperreports.charts.xml.JRXyBarChartFactory;
import net.sf.jasperreports.charts.xml.JRXyDatasetFactory;
import net.sf.jasperreports.charts.xml.JRXyLineChartFactory;
import net.sf.jasperreports.charts.xml.JRXySeriesFactory;
import net.sf.jasperreports.charts.xml.JRXyzDatasetFactory;
import net.sf.jasperreports.charts.xml.JRXyzSeriesFactory;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
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
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.component.ComponentsXmlParser;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.CompositeClassloader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSingletonCache;
import net.sf.jasperreports.engine.xml.JRChartFactory.JRCategoryAxisFormatFactory;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.SetNestedPropertiesRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public final class JRXmlDigesterFactory
{

	private static final Log log = LogFactory.getLog(JRXmlDigesterFactory.class);
	
	protected static final JRSingletonCache<JRSaxParserFactory> reportParserFactories = 
		new JRSingletonCache<JRSaxParserFactory>(JRSaxParserFactory.class);

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
		// set a composite classloader that includes both the JR classloader
		// and the context classloader
		CompositeClassloader digesterClassLoader = new CompositeClassloader(
				JRXmlDigesterFactory.class.getClassLoader(), 
				Thread.currentThread().getContextClassLoader());
		digester.setClassLoader(digesterClassLoader);
		
		digester.setErrorHandler(new ErrorHandlerImpl());
		
		digester.setNamespaceAware(true);
		
		digester.setRuleNamespaceURI(JRXmlConstants.JASPERREPORTS_NAMESPACE);

		/*   */
		digester.addFactoryCreate("jasperReport", JasperDesignFactory.class.getName());
		digester.addSetNext("jasperReport", "setJasperDesign", JasperDesign.class.getName());

		/*   */
		digester.addRule("*/property", new JRPropertyDigesterRule());
		
		String propertyExpressionPattern = "*/" + JRXmlConstants.ELEMENT_propertyExpression;
		digester.addFactoryCreate(propertyExpressionPattern, JRPropertyExpressionFactory.class.getName());
		digester.addSetNext(propertyExpressionPattern, "addPropertyExpression", JRPropertyExpression.class.getName());
		digester.addFactoryCreate(propertyExpressionPattern, JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addSetNext(propertyExpressionPattern, "setValueExpression", JRExpression.class.getName());
		digester.addCallMethod(propertyExpressionPattern, "setText", 0);

		/*   */
		digester.addCallMethod("jasperReport/import", "addImport", 1);
		digester.addCallParam("jasperReport/import", 0, "value");

		addTemplateRules(digester);

		/*   */
		digester.addFactoryCreate("jasperReport/reportFont", JRStyleFactory.class.getName());
		digester.addSetNext("jasperReport/reportFont", "addStyle", JRStyle.class.getName());

		/*   */
		digester.addFactoryCreate("jasperReport/style", JRStyleFactory.class.getName());
		digester.addSetNext("jasperReport/style", "addStyle", JRStyle.class.getName());

		digester.addFactoryCreate("jasperReport/style/conditionalStyle", JRConditionalStyleFactory.class.getName());
		digester.addFactoryCreate("jasperReport/style/conditionalStyle/conditionExpression", JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext("jasperReport/style/conditionalStyle/conditionExpression", "setConditionExpression", JRExpression.class.getName());
		digester.addCallMethod("jasperReport/style/conditionalStyle/conditionExpression", "setText", 0);
		digester.addFactoryCreate("jasperReport/style/conditionalStyle/style", JRConditionalStyleFillerFactory.class.getName());
		digester.addFactoryCreate("*/style/pen", JRPenFactory.Style.class.getName());

		/*   */
		digester.addFactoryCreate("*/scriptlet", JRScriptletFactory.class.getName());
		digester.addSetNext("*/scriptlet", "addScriptlet", JRScriptlet.class.getName());
		digester.addCallMethod("*/scriptlet/scriptletDescription", "setDescription", 0);

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
		digester.addFactoryCreate("*/sortField", JRSortFieldFactory.class.getName());
		digester.addSetNext("*/sortField", "addSortField", JRSortField.class.getName());

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

		String filterExpressionPath = "*/" + JRXmlConstants.ELEMENT_filterExpression;
		digester.addFactoryCreate(filterExpressionPath, JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext(filterExpressionPath, "setFilterExpression", JRExpression.class.getName());
		digester.addCallMethod(filterExpressionPath, "setText", 0);

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
		digester.addFactoryCreate("jasperReport/group/groupHeader", JRSectionFactory.GroupHeaderSectionFactory.class.getName());
//		digester.addSetNext("jasperReport/group/groupHeader", "setGroupHeader", JRSection.class.getName());
		digester.addFactoryCreate("jasperReport/group/groupHeader/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/group/groupHeader/band", "addBand", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/detail", JRSectionFactory.DetailSectionFactory.class.getName());
		//digester.addSetNext("jasperReport/detail", "setDetail", JRSection.class.getName());
		digester.addFactoryCreate("jasperReport/detail/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/detail/band", "addBand", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/group/groupFooter", JRSectionFactory.GroupFooterSectionFactory.class.getName());
//		digester.addSetNext("jasperReport/group/groupFooter", "setGroupFooter", JRSection.class.getName());
		digester.addFactoryCreate("jasperReport/group/groupFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/group/groupFooter/band", "addBand", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/columnFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/columnFooter/band", "setColumnFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/pageFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/pageFooter/band", "setPageFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/lastPageFooter/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/lastPageFooter/band", "setLastPageFooter", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/summary/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/summary/band", "setSummary", JRBand.class.getName());
		digester.addFactoryCreate("jasperReport/noData/band", JRBandFactory.class.getName());
		digester.addSetNext("jasperReport/noData/band", "setNoData", JRBand.class.getName());

		/*   */
		digester.addFactoryCreate("*/band/printWhenExpression", JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext("*/band/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/band/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/break", JRBreakFactory.class.getName());
		digester.addSetNext("*/break", "addElement", JRDesignElement.class.getName());

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
		digester.addFactoryCreate("*/graphicElement/pen", JRPenFactory.class.getName());

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

		/*   */
		digester.addFactoryCreate("*/box/pen", JRPenFactory.Box.class.getName());
		digester.addFactoryCreate("*/box/topPen", JRPenFactory.Top.class.getName());
		digester.addFactoryCreate("*/box/leftPen", JRPenFactory.Left.class.getName());
		digester.addFactoryCreate("*/box/bottomPen", JRPenFactory.Bottom.class.getName());
		digester.addFactoryCreate("*/box/rightPen", JRPenFactory.Right.class.getName());

		/*   */
		digester.addFactoryCreate("*/paragraph", JRParagraphFactory.class.getName());
		digester.addFactoryCreate("*/paragraph/tabStop", TabStopFactory.class.getName());
		digester.addSetNext("*/paragraph/tabStop", "addTabStop", TabStop.class.getName());

		/*   */
		digester.addFactoryCreate("*/image/imageExpression", JRStringExpressionFactory.class.getName());
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
		digester.addFactoryCreate("*/textElement/font", JRFontFactory.TextElementFontFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/textField", JRTextFieldFactory.class.getName());
		digester.addSetNext("*/textField", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/textField/textFieldExpression", JRStringExpressionFactory.class.getName());
		digester.addSetNext("*/textField/textFieldExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/textField/textFieldExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/textField/patternExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/textField/patternExpression", "setPatternExpression", JRExpression.class.getName());
		digester.addCallMethod("*/textField/patternExpression", "setText", 0);

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

		String hyperlinkTooltipExpressionPattern = "*/" + JRXmlConstants.ELEMENT_hyperlinkTooltipExpression;
		digester.addFactoryCreate(hyperlinkTooltipExpressionPattern, JRExpressionFactory.StringExpressionFactory.class.getName());
		digester.addSetNext(hyperlinkTooltipExpressionPattern, "setHyperlinkTooltipExpression", JRExpression.class.getName());
		digester.addCallMethod(hyperlinkTooltipExpressionPattern, "setText", 0);

		addHyperlinkParameterRules(digester);

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
		
		addComponentRules(digester);
		
		addGenericElementRules(digester);
	}


	protected static void addComponentRules(Digester digester)
	{
		digester.addFactoryCreate("*/componentElement", JRComponentElementFactory.class.getName());
		digester.addSetNext("*/componentElement", "addElement", JRDesignElement.class.getName());
		
		Collection<ComponentsBundle> components = ComponentsEnvironment.getComponentBundles();
		for (Iterator<ComponentsBundle> it = components.iterator(); it.hasNext();)
		{
			ComponentsBundle componentsBundle = it.next();
			ComponentsXmlParser xmlParser = componentsBundle.getXmlParser();
			digester.setRuleNamespaceURI(xmlParser.getNamespace());
			
			XmlDigesterConfigurer configurer = xmlParser.getDigesterConfigurer();
			if (configurer != null)
			{
				configurer.configureDigester(digester);
			}
			
			digester.setRuleNamespaceURI(xmlParser.getNamespace());
			for (Iterator<String> namesIt = componentsBundle.getComponentNames().iterator(); 
					namesIt.hasNext();)
			{
				String componentName = namesIt.next();
				digester.addRule("*/componentElement/" + componentName, 
						JRComponentRule.newInstance());
			}
		}
		
		digester.setRuleNamespaceURI(JRXmlConstants.JASPERREPORTS_NAMESPACE);
	}


	protected static void addTemplateRules(Digester digester)
	{
		String templatePattern = JRXmlConstants.ELEMENT_jasperReport + "/" + JRXmlConstants.ELEMENT_template;
		//do not change the order
		digester.addObjectCreate(templatePattern, JRDesignReportTemplate.class);
		digester.addSetNext(templatePattern, "addTemplate", JRReportTemplate.class.getName());
		digester.addFactoryCreate(templatePattern, JRStringExpressionFactory.class);
		digester.addCallMethod(templatePattern, "setText", 0);
		digester.addSetNext(templatePattern, "setSourceExpression", JRExpression.class.getName());
	}


	/**
	 *
	 */
	private static void addChartRules(Digester digester)
	{
		digester.addFactoryCreate("*/dataset", JRElementDatasetFactory.class.getName());

		String datasetIncrementWhenExpressionPath = "*/dataset/" + JRXmlConstants.ELEMENT_incrementWhenExpression;
		digester.addFactoryCreate(datasetIncrementWhenExpressionPath, JRExpressionFactory.BooleanExpressionFactory.class.getName());
		digester.addSetNext(datasetIncrementWhenExpressionPath, "setIncrementWhenExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetIncrementWhenExpressionPath, "setText", 0);

		digester.addFactoryCreate("*/plot", JRChartPlotFactory.class.getName());
		digester.addFactoryCreate("*/plot/seriesColor", JRChartPlotFactory.JRSeriesColorFactory.class.getName());
		digester.addSetNext("*/plot/seriesColor", "addSeriesColor", JRChartPlot.JRSeriesColor.class.getName());

		digester.addFactoryCreate("*/chart", JRChartFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle", JRChartFactory.JRChartTitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/chart/chartTitle/font", "setTitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartTitle/titleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/chart/chartTitle/titleExpression", "setTitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartTitle/titleExpression", "setText", 0);
		digester.addFactoryCreate("*/chart/chartSubtitle", JRChartFactory.JRChartSubtitleFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/chart/chartSubtitle/font", "setSubtitleFont", JRFont.class.getName());
		digester.addFactoryCreate("*/chart/chartSubtitle/subtitleExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/chart/chartSubtitle/subtitleExpression", "setSubtitleExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/chart/chartSubtitle/subtitleExpression", "setText", 0);
		digester.addFactoryCreate("*/chart/chartLegend", JRChartFactory.JRChartLegendFactory.class.getName());
		digester.addFactoryCreate("*/chart/chartLegend/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/chart/chartLegend/font", "setLegendFont", JRFont.class.getName());

		// axis labels

		digester.addFactoryCreate("*/categoryAxisFormat", JRCategoryAxisFormatFactory.class.getName());

		digester.addFactoryCreate("*/categoryAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/categoryAxisFormat/axisFormat", "setCategoryAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/categoryAxisFormat/axisFormat/labelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/categoryAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/categoryAxisFormat/axisFormat/tickLabelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/categoryAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/valueAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/valueAxisFormat/axisFormat", "setValueAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/valueAxisFormat/axisFormat/labelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/valueAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/valueAxisFormat/axisFormat/tickLabelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/valueAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/timeAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/timeAxisFormat/axisFormat", "setTimeAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/timeAxisFormat/axisFormat/labelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/timeAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/timeAxisFormat/axisFormat/tickLabelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/timeAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/xAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/xAxisFormat/axisFormat", "setXAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/xAxisFormat/axisFormat/labelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/xAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/xAxisFormat/axisFormat/tickLabelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/xAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/yAxisFormat/axisFormat", JRChartFactory.JRChartAxisFormatFactory.class.getName());
		digester.addSetNext("*/yAxisFormat/axisFormat", "setYAxisFormat", JRAxisFormat.class.getName());
		digester.addFactoryCreate("*/yAxisFormat/axisFormat/labelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/yAxisFormat/axisFormat/labelFont/font", "setLabelFont", JRFont.class.getName());
		digester.addFactoryCreate("*/yAxisFormat/axisFormat/tickLabelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/yAxisFormat/axisFormat/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		// item labels
		digester.addFactoryCreate("*/itemLabel", JRItemLabelFactory.class.getName());
		digester.addSetNext("*/itemLabel", "setItemLabel", JRDesignItemLabel.class.getName());
		digester.addFactoryCreate("*/itemLabel/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/itemLabel/font", "setFont", JRFont.class.getName());

		// pie charts
		digester.addFactoryCreate("*/pieChart", JRPieChartFactory.class.getName());
		digester.addSetNext("*/pieChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/pieChart/piePlot", JRPiePlotFactory.class.getName());

		digester.addFactoryCreate("*/pieDataset", JRPieDatasetFactory.class.getName());
		digester.addFactoryCreate("*/pieDataset/pieSeries", JRPieSeriesFactory.class.getName());
		digester.addSetNext("*/pieDataset/pieSeries", "addPieSeries", JRDesignPieSeries.class.getName());

		digester.addFactoryCreate("*/pieSeries/keyExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/pieSeries/keyExpression", "setKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieSeries/keyExpression", "setText", 0);
		digester.addFactoryCreate("*/pieSeries/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/pieSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieSeries/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/pieSeries/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/pieSeries/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieSeries/valueExpression", "setText", 0);
		digester.addFactoryCreate("*/pieSeries/sectionHyperlink", JRHyperlinkFactory.class);
		digester.addSetNext("*/pieSeries/sectionHyperlink", "setSectionHyperlink", JRHyperlink.class.getName());

		digester.addFactoryCreate("*/pieDataset/keyExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/pieDataset/keyExpression", "setKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/keyExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/pieDataset/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/labelExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/pieDataset/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/valueExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/sectionHyperlink", JRHyperlinkFactory.class);
		digester.addSetNext("*/pieDataset/sectionHyperlink", "setSectionHyperlink", JRHyperlink.class.getName());

		digester.addFactoryCreate("*/pieDataset/otherKeyExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/pieDataset/otherKeyExpression", "setOtherKeyExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/pieDataset/otherKeyExpression", "setText", 0);
		digester.addFactoryCreate("*/pieDataset/otherLabelExpression", JRExpressionFactory.StringExpressionFactory.class);
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

		digester.addFactoryCreate( "*/barPlot/categoryAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/categoryAxisLabelExpression", "setCategoryAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/categoryAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/valueAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/valueAxisLabelExpression", "setValueAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/valueAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/barPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/barPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/barPlot/rangeAxisMaxValueExpression", "setText", 0 );


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

		digester.addFactoryCreate( "*/areaPlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/areaPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/areaPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/areaPlot/rangeAxisMaxValueExpression", "setText", 0 );

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

		digester.addFactoryCreate( "*/bar3DPlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bar3DPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bar3DPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bar3DPlot/rangeAxisMaxValueExpression", "setText", 0 );

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

		String itemHyperlinkPattern = "*/" + JRXmlConstants.ELEMENT_itemHyperlink;
		digester.addFactoryCreate(itemHyperlinkPattern, JRHyperlinkFactory.class);
		digester.addSetNext(itemHyperlinkPattern, "setItemHyperlink", JRHyperlink.class.getName());

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

		digester.addFactoryCreate( "*/timeSeriesPlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timeSeriesPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timeSeriesPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/timeSeriesPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/timeSeriesPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/timeSeriesPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
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

		digester.addFactoryCreate( "*/linePlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/linePlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/linePlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/linePlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/linePlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/linePlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/linePlot/rangeAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/xyLineChart", JRXyLineChartFactory.class.getName() );
		digester.addSetNext( "*/xyLineChart", "addElement", JRDesignElement.class.getName() );

		digester.addFactoryCreate( "*/scatterChart", JRScatterChartFactory.class.getName() );
		digester.addSetNext( "*/scatterChart", "addElement", JRDesignElement.class.getName() );
		digester.addFactoryCreate( "*/scatterPlot", JRScatterPlotFactory.class.getName() );

		// add plot labels
		digester.addFactoryCreate( "*/scatterPlot/xAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/xAxisLabelExpression", "setXAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/xAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/yAxisLabelExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/yAxisLabelExpression", "setYAxisLabelExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/yAxisLabelExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/scatterPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/scatterPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/scatterPlot/rangeAxisMaxValueExpression", "setText", 0 );

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

		digester.addFactoryCreate( "*/bubblePlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/bubblePlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/bubblePlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/bubblePlot/rangeAxisMaxValueExpression", "setText", 0 );

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

		digester.addFactoryCreate( "*/highLowPlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/domainAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/highLowPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/highLowPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/highLowPlot/rangeAxisMaxValueExpression", "setText", 0 );

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

		digester.addFactoryCreate( "*/candlestickPlot/domainAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/domainAxisMinValueExpression", "setDomainAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/domainAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/domainAxisMaxValueExpression", "setDomainAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/domainAxisMaxValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/rangeAxisMinValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/rangeAxisMinValueExpression", "setRangeAxisMinValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/rangeAxisMinValueExpression", "setText", 0 );

		digester.addFactoryCreate( "*/candlestickPlot/rangeAxisMaxValueExpression", JRExpressionFactory.ComparableExpressionFactory.class );
		digester.addSetNext( "*/candlestickPlot/rangeAxisMaxValueExpression", "setRangeAxisMaxValueExpression", JRDesignExpression.class.getName() );
		digester.addCallMethod( "*/candlestickPlot/rangeAxisMaxValueExpression", "setText", 0 );

		// value datasets
		digester.addFactoryCreate("*/valueDataset", JRValueDatasetFactory.class.getName());
		digester.addFactoryCreate("*/valueDataset/valueExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/valueDataset/valueExpression", "setValueExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/valueDataset/valueExpression", "setText", 0);

		// data ranges - anything that contains a data range must have a "setDataRange" method.
		digester.addFactoryCreate("*/dataRange", JRDataRangeFactory.class.getName());
		digester.addSetNext("*/dataRange", "setDataRange", JRDesignDataRange.class.getName());
		digester.addFactoryCreate("*/dataRange/lowExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/dataRange/lowExpression", "setLowExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/dataRange/lowExpression", "setText", 0);
		digester.addFactoryCreate("*/dataRange/highExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/dataRange/highExpression", "setHighExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/dataRange/highExpression", "setText", 0);

		// value displays - anything that contains a display value must have a "setValueDisplay" method.
		digester.addFactoryCreate("*/valueDisplay", JRValueDisplayFactory.class.getName());
		digester.addSetNext("*/valueDisplay", "setValueDisplay", JRDesignValueDisplay.class.getName());
		digester.addFactoryCreate("*/valueDisplay/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/valueDisplay/font", "setFont", JRFont.class.getName());

		// meter charts
		digester.addFactoryCreate("*/meterChart", JRMeterChartFactory.class.getName());
		digester.addSetNext("*/meterChart", "addElement", JRDesignElement.class.getName());
		digester.addFactoryCreate("*/meterChart/meterPlot", JRMeterPlotFactory.class.getName());
		digester.addFactoryCreate("*/meterPlot/tickLabelFont/font", JRFontFactory.ChartFontFactory.class.getName());
		digester.addSetNext("*/meterPlot/tickLabelFont/font", "setTickLabelFont", JRFont.class.getName());

		digester.addFactoryCreate("*/meterPlot/lowExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/meterPlot/lowExpression", "setLowExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/meterPlot/lowExpression", "setText", 0);
		digester.addFactoryCreate("*/meterPlot/highExpression", JRExpressionFactory.NumberExpressionFactory.class);
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
		digester.addFactoryCreate("*/ganttSeries/seriesExpression", JRExpressionFactory.ComparableExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/seriesExpression", "setSeriesExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/seriesExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/taskExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/taskExpression", "setTaskExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/taskExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/subtaskExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/subtaskExpression", "setSubtaskExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/subtaskExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/startDateExpression", JRExpressionFactory.DateExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/startDateExpression", "setStartDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/startDateExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/endDateExpression", JRExpressionFactory.DateExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/endDateExpression", "setEndDateExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/endDateExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/percentExpression", JRExpressionFactory.NumberExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/percentExpression", "setPercentExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/percentExpression", "setText", 0);
		digester.addFactoryCreate("*/ganttSeries/labelExpression", JRExpressionFactory.StringExpressionFactory.class);
		digester.addSetNext("*/ganttSeries/labelExpression", "setLabelExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/ganttSeries/labelExpression", "setText", 0);
	}


	private static void addDatasetRules(Digester digester)
	{
		String subDatasetPattern = "jasperReport/" + JRXmlConstants.ELEMENT_subDataset;
		digester.addFactoryCreate(subDatasetPattern, JRDatasetFactory.class.getName());
		digester.addSetNext(subDatasetPattern, "addDataset", JRDesignDataset.class.getName());

		String datasetRunPattern = "*/" + JRXmlConstants.ELEMENT_datasetRun;
		digester.addFactoryCreate(datasetRunPattern, JRDatasetRunFactory.class.getName());
		digester.addSetNext(datasetRunPattern, "setDatasetRun", JRDatasetRun.class.getName());

		String datasetParamPattern = datasetRunPattern + "/" + JRXmlConstants.ELEMENT_datasetParameter;
		digester.addFactoryCreate(datasetParamPattern, JRDatasetRunParameterFactory.class.getName());
		digester.addSetNext(datasetParamPattern, "addParameter", JRDatasetParameter.class.getName());

		String datasetParamExprPattern = datasetParamPattern + "/" + JRXmlConstants.ELEMENT_datasetParameterExpression;
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

		String bucketPattern = "*/" + JRCrosstabBucketFactory.ELEMENT_bucket;
		digester.addFactoryCreate(bucketPattern, JRCrosstabBucketFactory.class.getName());
		digester.addSetNext(bucketPattern, "setBucket", JRDesignCrosstabBucket.class.getName());

		digester.addFactoryCreate("*/bucket/bucketExpression", JRCrosstabBucketExpressionFactory.class.getName());
		digester.addSetNext("*/bucket/bucketExpression", "setExpression", JRDesignExpression.class.getName());
		digester.addCallMethod("*/bucket/bucketExpression", "setText", 0);

		String orderByPattern = bucketPattern 
				+ "/" + JRCrosstabBucketFactory.ELEMENT_orderByExpression;
		digester.addFactoryCreate(orderByPattern, 
				new JRExpressionFactory.ArbitraryExpressionFactory(Object.class));
		digester.addSetNext(orderByPattern, "setOrderByExpression", JRExpression.class.getName());
		digester.addCallMethod(orderByPattern, "setText", 0);

		digester.addFactoryCreate("*/bucket/comparatorExpression", JRExpressionFactory.ComparatorExpressionFactory.class.getName());
		digester.addSetNext("*/bucket/comparatorExpression", "setComparatorExpression", JRExpression.class.getName());
		digester.addCallMethod("*/bucket/comparatorExpression", "setText", 0);

		digester.addFactoryCreate("*/crosstab/measure", JRCrosstabMeasureFactory.class.getName());
		digester.addSetNext("*/crosstab/measure", "addMeasure", JRDesignCrosstabMeasure.class.getName());

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
	}


	private static void addFrameRules(Digester digester)
	{
		String framePattern = "*/" + JRXmlConstants.ELEMENT_frame;
		digester.addFactoryCreate(framePattern, JRFrameFactory.class.getName());
		digester.addSetNext(framePattern, "addElement", JRDesignElement.class.getName());
	}


	private static void addHyperlinkParameterRules(Digester digester)
	{
		String hyperlinkParameterPattern = "*/" + JRXmlConstants.ELEMENT_hyperlinkParameter;
		digester.addFactoryCreate(hyperlinkParameterPattern, JRHyperlinkParameterFactory.class.getName());
		digester.addSetNext(hyperlinkParameterPattern, "addHyperlinkParameter", JRHyperlinkParameter.class.getName());

		String hyperlinkParameterExpressionPattern = hyperlinkParameterPattern + '/' + JRXmlConstants.ELEMENT_hyperlinkParameterExpression;
		digester.addFactoryCreate(hyperlinkParameterExpressionPattern, JRStringExpressionFactory.class.getName());
		digester.addSetNext(hyperlinkParameterExpressionPattern, "setValueExpression", JRExpression.class.getName());
		digester.addCallMethod(hyperlinkParameterExpressionPattern, "setText", 0);
	}


	protected static void addGenericElementRules(Digester digester)
	{
		String genericElementPattern = "*/" + JRXmlConstants.ELEMENT_genericElement;
		digester.addFactoryCreate(genericElementPattern, 
				JRGenericElementFactory.class);
		digester.addSetNext(genericElementPattern, "addElement", 
				JRDesignElement.class.getName());
		
		String genericElementTypePattern = genericElementPattern + "/" 
			+ JRXmlConstants.ELEMENT_genericElementType;
		digester.addFactoryCreate(genericElementTypePattern, 
				JRGenericElementTypeFactory.class);
		digester.addSetNext(genericElementTypePattern, "setGenericType", 
				JRGenericElementType.class.getName());
		
		String genericElementParameterPattern = genericElementPattern + "/"
			+ JRXmlConstants.ELEMENT_genericElementParameter;
		digester.addFactoryCreate(genericElementParameterPattern, 
				JRGenericElementParameterFactory.class);
		digester.addSetNext(genericElementParameterPattern, "addParameter", 
				JRGenericElementParameter.class.getName());
		
		String genericElementParameterExpressionPattern = genericElementParameterPattern + "/"
			+ JRXmlConstants.ELEMENT_genericElementParameter_valueExpression;
		digester.addFactoryCreate(genericElementParameterExpressionPattern, 
				JRExpressionFactory.ArbitraryExpressionFactory.class);
		digester.addSetNext(genericElementParameterExpressionPattern, 
				"setValueExpression", JRExpression.class.getName());
		digester.addCallMethod(genericElementParameterExpressionPattern, "setText", 0);
	}
	
	/**
	 * Creates a new instance of digester. The created digester is ready for
	 * parsing report definition files.
	 */
	public static JRXmlDigester createDigester() throws ParserConfigurationException, SAXException
	{
		SAXParser parser = createParser();
		JRXmlDigester digester = new JRXmlDigester(parser);
		
		//normally not required because schemaSource is set, but keeping to be safe
		setComponentsInternalEntityResources(digester);
		
		configureDigester(digester);
		return digester;
	}


	protected static SAXParser createParser()
	{
		try
		{
			String parserFactoryClass = JRProperties.getProperty(
					JRSaxParserFactory.PROPERTY_REPORT_PARSER_FACTORY);
			
			if (log.isDebugEnabled())
			{
				log.debug("Using SAX parser factory class " + parserFactoryClass);
			}
			
			JRSaxParserFactory factory = reportParserFactories.getCachedInstance(parserFactoryClass);
			return factory.createParser();
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	protected static void setComponentsInternalEntityResources(JRXmlDigester digester)
	{
		Collection<ComponentsBundle> components = ComponentsEnvironment.getComponentBundles();
		for (Iterator<ComponentsBundle> it = components.iterator(); it.hasNext();)
		{
			ComponentsBundle componentManager = it.next();
			ComponentsXmlParser xmlParser = componentManager.getXmlParser();
			String schemaResource = xmlParser.getInternalSchemaResource();
			if (schemaResource != null)
			{
				digester.addInternalEntityResource(xmlParser.getPublicSchemaLocation(), 
						schemaResource);
			}
		}
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
