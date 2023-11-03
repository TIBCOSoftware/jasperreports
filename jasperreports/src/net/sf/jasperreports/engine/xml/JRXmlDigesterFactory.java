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
package net.sf.jasperreports.engine.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.SetNestedPropertiesRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.DesignCrosstabColumnCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.crosstabs.xml.JRCellContentsFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabBucketExpressionFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabBucketFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabCellFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabColumnGroupFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabDatasetFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabMeasureFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabParameterFactory;
import net.sf.jasperreports.crosstabs.xml.JRCrosstabRowGroupFactory;
import net.sf.jasperreports.engine.DatasetPropertyExpression;
import net.sf.jasperreports.engine.ExpressionReturnValue;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPart;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.analytics.data.Axis;
import net.sf.jasperreports.engine.analytics.dataset.BucketOrder;
import net.sf.jasperreports.engine.analytics.dataset.DesignDataAxis;
import net.sf.jasperreports.engine.analytics.dataset.DesignDataAxisLevel;
import net.sf.jasperreports.engine.analytics.dataset.DesignDataLevelBucket;
import net.sf.jasperreports.engine.analytics.dataset.DesignDataLevelBucketProperty;
import net.sf.jasperreports.engine.analytics.dataset.DesignDataMeasure;
import net.sf.jasperreports.engine.analytics.dataset.DesignMultiAxisData;
import net.sf.jasperreports.engine.analytics.dataset.DesignMultiAxisDataset;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.component.ComponentsXmlParser;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.design.DesignReturnValue;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.part.PartComponentsBundle;
import net.sf.jasperreports.engine.part.PartComponentsEnvironment;
import net.sf.jasperreports.engine.type.BorderSplitType;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.HorizontalPosition;
import net.sf.jasperreports.engine.type.OverflowType;
import net.sf.jasperreports.engine.util.CompositeClassloader;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * JRXmlDigesterFactory encapsulates the code necessary to construct and configure
 * a digester in order to prepare it for parsing JasperReports xml definition files.
 *
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 */
public final class JRXmlDigesterFactory
{
	private static final Log log = LogFactory.getLog(JRXmlDigesterFactory.class);
	
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
	public static void configureDigester(JasperReportsContext jasperReportsContext, Digester digester) throws SAXException, ParserConfigurationException
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
		digester.addFactoryCreate("*/property", JRPropertyFactory.class.getName());
		digester.addCallMethod("*/property", "setValue", 0);
		
		String propertyExpressionPattern = "jasperReport/" + JRXmlConstants.ELEMENT_propertyExpression;
		digester.addFactoryCreate(propertyExpressionPattern, DatasetPropertyExpressionFactory.class.getName());
		digester.addSetNext(propertyExpressionPattern, "addPropertyExpression", DatasetPropertyExpression.class.getName());
		digester.addFactoryCreate(propertyExpressionPattern, JRExpressionFactory.class.getName());
		digester.addSetNext(propertyExpressionPattern, "setValueExpression", JRExpression.class.getName());
		digester.addCallMethod(propertyExpressionPattern, "setText", 0);

		propertyExpressionPattern = "*/subDataset/" + JRXmlConstants.ELEMENT_propertyExpression;
		digester.addFactoryCreate(propertyExpressionPattern, DatasetPropertyExpressionFactory.class.getName());
		digester.addSetNext(propertyExpressionPattern, "addPropertyExpression", DatasetPropertyExpression.class.getName());
		digester.addFactoryCreate(propertyExpressionPattern, JRExpressionFactory.class.getName());
		digester.addSetNext(propertyExpressionPattern, "setValueExpression", JRExpression.class.getName());
		digester.addCallMethod(propertyExpressionPattern, "setText", 0);

		propertyExpressionPattern = "*/" + JRXmlConstants.ELEMENT_propertyExpression;
		digester.addFactoryCreate(propertyExpressionPattern, JRPropertyExpressionFactory.class.getName());
		digester.addSetNext(propertyExpressionPattern, "addPropertyExpression", JRPropertyExpression.class.getName());
		digester.addFactoryCreate(propertyExpressionPattern, JRExpressionFactory.class.getName());
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
		digester.addFactoryCreate("jasperReport/style/conditionalStyle/conditionExpression", JRExpressionFactory.class.getName());
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
		digester.addFactoryCreate("*/parameter/defaultValueExpression", JRExpressionFactory.class.getName());
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
		digester.addCallMethod("*/variable/variableDescription", "setDescription", 0);

		/*   */
		digester.addFactoryCreate("*/variable/variableExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/variable/variableExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/variable/variableExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/variable/initialValueExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/variable/initialValueExpression", "setInitialValueExpression", JRExpression.class.getName());
		digester.addCallMethod("*/variable/initialValueExpression", "setText", 0);

		String filterExpressionPath = "*/" + JRXmlConstants.ELEMENT_filterExpression;
		digester.addFactoryCreate(filterExpressionPath, JRExpressionFactory.class.getName());
		digester.addSetNext(filterExpressionPath, "setFilterExpression", JRExpression.class.getName());
		digester.addCallMethod(filterExpressionPath, "setText", 0);

		/*   */
		digester.addFactoryCreate("*/group", JRGroupFactory.class.getName());
		digester.addSetNext("*/group", "addGroup", JRDesignGroup.class.getName());

		/*   */
		digester.addFactoryCreate("*/group/groupExpression", JRExpressionFactory.class.getName());
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
		digester.addFactoryCreate("*/band/printWhenExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/band/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/band/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/part/printWhenExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/part/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/part/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/band/returnValue", ExpressionReturnValueFactory.class.getName());
		digester.addSetNext("*/band/returnValue", "addReturnValue", ExpressionReturnValue.class.getName());
		digester.addFactoryCreate("*/band/returnValue/expression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/band/returnValue/expression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/band/returnValue/expression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/break", JRBreakFactory.class.getName());
		digester.addSetNext("*/break", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/line", JRLineFactory.class.getName());
		digester.addSetNext("*/line", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/reportElement", JRElementFactory.class.getName());

		/*   */
		digester.addFactoryCreate("*/reportElement/printWhenExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/reportElement/printWhenExpression", "setPrintWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/reportElement/printWhenExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/reportElement/styleExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/reportElement/styleExpression", "setStyleExpression", JRExpression.class.getName());
		digester.addCallMethod("*/reportElement/styleExpression", "setText", 0);

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
		digester.addFactoryCreate("*/image/imageExpression", JRExpressionFactory.class.getName());
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
		digester.addFactoryCreate("*/textField/textFieldExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/textField/textFieldExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/textField/textFieldExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/textField/patternExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/textField/patternExpression", "setPatternExpression", JRExpression.class.getName());
		digester.addCallMethod("*/textField/patternExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/anchorNameExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/anchorNameExpression", "setAnchorNameExpression", JRExpression.class.getName());
		digester.addCallMethod("*/anchorNameExpression", "setText", 0);
		digester.addFactoryCreate("*/bookmarkLevelExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/bookmarkLevelExpression", "setBookmarkLevelExpression", JRExpression.class.getName());
		digester.addCallMethod("*/bookmarkLevelExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkReferenceExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkReferenceExpression", "setHyperlinkReferenceExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkReferenceExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkWhenExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkWhenExpression", "setHyperlinkWhenExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkWhenExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkAnchorExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkAnchorExpression", "setHyperlinkAnchorExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkAnchorExpression", "setText", 0);
		digester.addFactoryCreate("*/hyperlinkPageExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/hyperlinkPageExpression", "setHyperlinkPageExpression", JRExpression.class.getName());
		digester.addCallMethod("*/hyperlinkPageExpression", "setText", 0);

		String hyperlinkTooltipExpressionPattern = "*/" + JRXmlConstants.ELEMENT_hyperlinkTooltipExpression;
		digester.addFactoryCreate(hyperlinkTooltipExpressionPattern, JRExpressionFactory.class.getName());
		digester.addSetNext(hyperlinkTooltipExpressionPattern, "setHyperlinkTooltipExpression", JRExpression.class.getName());
		digester.addCallMethod(hyperlinkTooltipExpressionPattern, "setText", 0);

		addHyperlinkParameterRules(digester);

		/*   */
		digester.addFactoryCreate("*/subreport", JRSubreportFactory.class.getName());
		digester.addRule("*/subreport", new XmlConstantPropertyRule(JRXmlConstants.ATTRIBUTE_overflowType, OverflowType.values()));
		digester.addSetNext("*/subreport", "addElement", JRDesignElement.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter", JRSubreportParameterFactory.class.getName());
		digester.addSetNext("*/subreport/subreportParameter", "addParameter", JRSubreportParameter.class.getName());

		/*   */
		digester.addFactoryCreate("*/subreport/returnValue", JRSubreportReturnValueFactory.class.getName());
		digester.addSetNext("*/subreport/returnValue", "addReturnValue", JRSubreportReturnValue.class.getName());

		/*   */
		digester.addFactoryCreate("*/parametersMapExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/parametersMapExpression", "setParametersMapExpression", JRExpression.class.getName());
		digester.addCallMethod("*/parametersMapExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportParameter/subreportParameterExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/subreportParameter/subreportParameterExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/subreportParameter/subreportParameterExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/connectionExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/connectionExpression", "setConnectionExpression", JRExpression.class.getName());
		digester.addCallMethod("*/connectionExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/dataSourceExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/dataSourceExpression", "setDataSourceExpression", JRExpression.class.getName());
		digester.addCallMethod("*/dataSourceExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/subreport/subreportExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/subreport/subreportExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/subreport/subreportExpression", "setText", 0);

		/*   */
		digester.addFactoryCreate("*/elementGroup", JRElementGroupFactory.class.getName());
		digester.addSetNext("*/elementGroup", "addElementGroup", JRDesignElementGroup.class.getName());

		digester.addFactoryCreate("*/dataset", JRElementDatasetFactory.class.getName());

		String datasetIncrementWhenExpressionPath = "*/dataset/" + JRXmlConstants.ELEMENT_incrementWhenExpression;
		digester.addFactoryCreate(datasetIncrementWhenExpressionPath, JRExpressionFactory.class.getName());
		digester.addSetNext(datasetIncrementWhenExpressionPath, "setIncrementWhenExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetIncrementWhenExpressionPath, "setText", 0);

		addChartRules(digester);

		addDatasetRules(digester);

		addCrosstabRules(digester);

		addFrameRules(digester);
		
		addComponentRules(jasperReportsContext, digester);
		
		addPartComponentRules(jasperReportsContext, digester);
		
		addGenericElementRules(digester);
		
		addMultiAxisDataRules(digester);
	}


	protected static void addComponentRules(JasperReportsContext jasperReportsContext, Digester digester)
	{
		digester.addFactoryCreate("*/componentElement", JRComponentElementFactory.class.getName());
		digester.addSetNext("*/componentElement", "addElement", JRDesignElement.class.getName());
		
		Collection<ComponentsBundle> components = ComponentsEnvironment.getInstance(jasperReportsContext).getBundles();
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


	protected static void addPartComponentRules(JasperReportsContext jasperReportsContext, Digester digester)
	{
		digester.addFactoryCreate("*/part", JRPartFactory.class.getName());
		digester.addRule("*/part", new UuidPropertyRule("uuid", "UUID"));
		digester.addSetNext("*/part", "addPart", JRPart.class.getName());

		addExpressionRules(digester, "*/part/" + JRXmlConstants.ELEMENT_partNameExpression, "setPartNameExpression");

		Collection<PartComponentsBundle> components = PartComponentsEnvironment.getInstance(jasperReportsContext).getBundles();
		for (Iterator<PartComponentsBundle> it = components.iterator(); it.hasNext();)
		{
			PartComponentsBundle componentsBundle = it.next();
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
				digester.addRule("*/part/" + componentName, 
						JRPartComponentRule.newInstance());
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
		digester.addFactoryCreate(templatePattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(templatePattern, "setText", 0);
		digester.addSetNext(templatePattern, "setSourceExpression", JRExpression.class.getName());
	}


	/**
	 *
	 */
	private static void addChartRules(Digester digester) //FIXME7 use extension?
	{
		Class clazz = null;
		
		try
		{
			clazz = JRClassLoader.loadClassForName("net.sf.jasperreports.charts.xml.ChartsXmlDigesterConfigurer");
		}
		catch (ClassNotFoundException e)
		{
			//nothing to do
		}
		
		if (clazz != null)
		{
			try
			{
				Constructor<XmlDigesterConfigurer> constructor = clazz.getConstructor();
				XmlDigesterConfigurer chartsXmlDigesterConfigurer = constructor.newInstance();
				chartsXmlDigesterConfigurer.configureDigester(digester);
			}
			catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
			{
				throw new JRRuntimeException(e);
			}
		}
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
		digester.addFactoryCreate(datasetParamExprPattern, JRExpressionFactory.class.getName());
		digester.addSetNext(datasetParamExprPattern, "setExpression", JRExpression.class.getName());
		digester.addCallMethod(datasetParamExprPattern, "setText", 0);

		String returnValuePattern = datasetRunPattern + "/" + JRXmlConstants.ELEMENT_returnValue;
		digester.addObjectCreate(returnValuePattern, DesignReturnValue.class.getName());
		digester.addSetProperties(returnValuePattern, 
				new String[]{JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, JRXmlConstants.ATTRIBUTE_calculation}, 
				new String[]{"incrementerFactoryClassName"});
		digester.addRule(returnValuePattern, new XmlConstantPropertyRule(
				JRXmlConstants.ATTRIBUTE_calculation, CalculationEnum.values()));
		digester.addSetNext(returnValuePattern, "addReturnValue");
	}


	private static void addCrosstabRules(Digester digester)
	{
		digester.addFactoryCreate("*/crosstab", JRCrosstabFactory.class.getName());
		digester.addSetNext("*/crosstab", "addElement", JRDesignElement.class.getName());
		digester.addRule("*/crosstab", new XmlConstantPropertyRule("horizontalPosition", HorizontalPosition.values()));

		digester.addFactoryCreate("*/crosstab/crosstabParameter", JRCrosstabParameterFactory.class.getName());
		digester.addSetNext("*/crosstab/crosstabParameter", "addParameter", JRCrosstabParameter.class.getName());

		digester.addFactoryCreate("*/crosstab/crosstabParameter/parameterValueExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/crosstab/crosstabParameter/parameterValueExpression", "setExpression", JRExpression.class.getName());
		digester.addCallMethod("*/crosstab/crosstabParameter/parameterValueExpression", "setText", 0);

		digester.addFactoryCreate("*/crosstab/crosstabDataset", JRCrosstabDatasetFactory.class.getName());

		digester.addFactoryCreate("*/crosstab/rowGroup", JRCrosstabRowGroupFactory.class.getName());
		digester.addSetNext("*/crosstab/rowGroup", "addRowGroup", JRDesignCrosstabRowGroup.class.getName());

		digester.addFactoryCreate("*/crosstab/rowGroup/crosstabRowHeader/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/rowGroup/crosstabRowHeader/cellContents", "setHeader", JRDesignCellContents.class.getName());

		digester.addFactoryCreate("*/crosstab/rowGroup/crosstabTotalRowHeader/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext("*/crosstab/rowGroup/crosstabTotalRowHeader/cellContents", "setTotalHeader", JRDesignCellContents.class.getName());

		String columnGroupPattern = "*/crosstab/columnGroup";
		digester.addFactoryCreate(columnGroupPattern, JRCrosstabColumnGroupFactory.class.getName());
		digester.addSetNext(columnGroupPattern, "addColumnGroup", JRDesignCrosstabColumnGroup.class.getName());

		String columnGroupCrosstabHeaderPattern = columnGroupPattern + "/" + JRCrosstabColumnGroupFactory.ELEMENT_crosstabHeader;
		digester.addFactoryCreate(columnGroupCrosstabHeaderPattern + "/cellContents", JRCellContentsFactory.class.getName());
		digester.addSetNext(columnGroupCrosstabHeaderPattern + "/cellContents", "setCrosstabHeader", JRDesignCellContents.class.getName());

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

		String orderByPattern = bucketPattern + "/" + JRCrosstabBucketFactory.ELEMENT_orderByExpression;
		digester.addFactoryCreate(orderByPattern, JRExpressionFactory.class.getName());
		digester.addSetNext(orderByPattern, "setOrderByExpression", JRExpression.class.getName());
		digester.addCallMethod(orderByPattern, "setText", 0);

		digester.addFactoryCreate("*/bucket/comparatorExpression", JRExpressionFactory.class.getName());
		digester.addSetNext("*/bucket/comparatorExpression", "setComparatorExpression", JRExpression.class.getName());
		digester.addCallMethod("*/bucket/comparatorExpression", "setText", 0);

		digester.addFactoryCreate("*/crosstab/measure", JRCrosstabMeasureFactory.class.getName());
		digester.addSetNext("*/crosstab/measure", "addMeasure", JRDesignCrosstabMeasure.class.getName());

		digester.addFactoryCreate("*/crosstab/measure/measureExpression", JRExpressionFactory.class.getName());
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
		
		String titleCellPattern = "*/crosstab/titleCell";
		digester.addObjectCreate(titleCellPattern, DesignCrosstabColumnCell.class);
		digester.addSetProperties(titleCellPattern,
				new String[]{JRXmlConstants.ATTRIBUTE_height, JRXmlConstants.ATTRIBUTE_contentsPosition},
				new String[]{"height"});
		digester.addRule(titleCellPattern, 
				new XmlConstantPropertyRule(JRXmlConstants.ATTRIBUTE_contentsPosition, CrosstabColumnPositionEnum.values()));
		digester.addSetNext(titleCellPattern, "setTitleCell", DesignCrosstabColumnCell.class.getName());
		
		String titleCellContentsPattern = titleCellPattern + "/cellContents";
		digester.addFactoryCreate(titleCellContentsPattern, JRCellContentsFactory.class.getName());
		digester.addSetNext(titleCellContentsPattern, "setCellContents", JRDesignCellContents.class.getName());
	}


	private static void addFrameRules(Digester digester)
	{
		String framePattern = "*/" + JRXmlConstants.ELEMENT_frame;
		digester.addFactoryCreate(framePattern, JRFrameFactory.class.getName());
		digester.addSetNext(framePattern, "addElement", JRDesignElement.class.getName());
		digester.addRule(framePattern, new XmlConstantPropertyRule(JRXmlConstants.ATTRIBUTE_borderSplitType, BorderSplitType.values()));
	}


	private static void addHyperlinkParameterRules(Digester digester)
	{
		String hyperlinkParameterPattern = "*/" + JRXmlConstants.ELEMENT_hyperlinkParameter;
		digester.addFactoryCreate(hyperlinkParameterPattern, JRHyperlinkParameterFactory.class.getName());
		digester.addSetNext(hyperlinkParameterPattern, "addHyperlinkParameter", JRHyperlinkParameter.class.getName());

		String hyperlinkParameterExpressionPattern = hyperlinkParameterPattern + '/' + JRXmlConstants.ELEMENT_hyperlinkParameterExpression;
		digester.addFactoryCreate(hyperlinkParameterExpressionPattern, JRExpressionFactory.class.getName());
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
		digester.addFactoryCreate(genericElementParameterExpressionPattern, JRExpressionFactory.class);
		digester.addSetNext(genericElementParameterExpressionPattern, 
				"setValueExpression", JRExpression.class.getName());
		digester.addCallMethod(genericElementParameterExpressionPattern, "setText", 0);
	}


	private static void addMultiAxisDataRules(Digester digester)
	{
		String dataPattern = "*/" + JRXmlConstants.ELEMENT_multiAxisData;
		digester.addObjectCreate(dataPattern, DesignMultiAxisData.class);
		digester.addSetNext(dataPattern, "setMultiAxisData");// TODO lucianc move to containing element
		
		String datasetPattern = dataPattern + "/" + JRXmlConstants.ELEMENT_multiAxisDataset;
		digester.addObjectCreate(datasetPattern, DesignMultiAxisDataset.class);
		digester.addSetNext(datasetPattern, "setDataset");
		
		String dataAxisPattern = dataPattern + "/" + JRXmlConstants.ELEMENT_dataAxis;
		digester.addObjectCreate(dataAxisPattern, DesignDataAxis.class);
		digester.addRule(dataAxisPattern, new XmlConstantPropertyRule(JRXmlConstants.ATTRIBUTE_axis, Axis.values()));
		digester.addSetNext(dataAxisPattern, "addDataAxis");
		
		String axisLevelPattern = dataAxisPattern + "/" + JRXmlConstants.ELEMENT_axisLevel;
		digester.addObjectCreate(axisLevelPattern, DesignDataAxisLevel.class);
		digester.addSetProperties(axisLevelPattern);
		digester.addSetNext(axisLevelPattern, "addLevel");
		addExpressionRules(digester, axisLevelPattern + "/" + JRXmlConstants.ELEMENT_labelExpression, 
				"setLabelExpression");
		
		String bucketPattern = axisLevelPattern + "/" + JRXmlConstants.ELEMENT_axisLevelBucket;
		digester.addObjectCreate(bucketPattern, DesignDataLevelBucket.class);
		digester.addSetProperties(bucketPattern, 
				new String[]{JRXmlConstants.ATTRIBUTE_class, JRXmlConstants.ATTRIBUTE_order}, 
				new String[]{"valueClassName"});
		digester.addRule(bucketPattern, new XmlConstantPropertyRule(JRXmlConstants.ATTRIBUTE_order, BucketOrder.values()));
		digester.addSetNext(bucketPattern, "setBucket");
		
		addExpressionRules(digester, bucketPattern + "/" + JRCrosstabBucketFactory.ELEMENT_bucketExpression, "setExpression");
		addExpressionRules(digester, bucketPattern + "/" + JRXmlConstants.ELEMENT_MULTI_AXIS_BUCKET_LABEL_EXPRESSION, "setLabelExpression");
		addExpressionRules(digester, bucketPattern + "/" + JRCrosstabBucketFactory.ELEMENT_comparatorExpression, "setComparatorExpression");
		
		String bucketExpressionPattern = bucketPattern + "/" + JRXmlConstants.ELEMENT_bucketProperty;
		digester.addObjectCreate(bucketExpressionPattern, DesignDataLevelBucketProperty.class);
		digester.addSetProperties(bucketExpressionPattern);
		digester.addSetNext(bucketExpressionPattern, "addBucketProperty");		
		addExpressionRules(digester, bucketExpressionPattern, "setExpression");
		
		String measurePattern = dataPattern + "/" + JRXmlConstants.ELEMENT_multiAxisMeasure;
		digester.addObjectCreate(measurePattern, DesignDataMeasure.class);
		digester.addSetNext(measurePattern, "addMeasure");
		digester.addSetProperties(measurePattern, 
				new String[]{JRXmlConstants.ATTRIBUTE_class, JRXmlConstants.ATTRIBUTE_incrementerFactoryClass, 
						JRXmlConstants.ATTRIBUTE_calculation}, 
				new String[]{"valueClassName", "incrementerFactoryClassName"});
		digester.addRule(measurePattern, new XmlConstantPropertyRule(
				JRXmlConstants.ATTRIBUTE_calculation, CalculationEnum.values()));
		addExpressionRules(digester, measurePattern + "/" + JRXmlConstants.ELEMENT_labelExpression, 
				"setLabelExpression");
		addExpressionRules(digester, measurePattern + "/" + JRXmlConstants.ELEMENT_valueExpression, 
				"setValueExpression");
	}

	protected static void addExpressionRules(Digester digester, String expressionPattern,
			String setterMethod)
	{
		digester.addFactoryCreate(expressionPattern, JRExpressionFactory.class);
		digester.addCallMethod(expressionPattern, "setText", 0);
		digester.addSetNext(expressionPattern, setterMethod,
				JRExpression.class.getName());
	}


	/**
	 * Creates a new instance of digester. The created digester is ready for
	 * parsing report definition files.
	 */
	public static JRXmlDigester createDigester(JasperReportsContext jasperReportsContext) throws ParserConfigurationException, SAXException
	{
		SAXParser parser = createParser(jasperReportsContext);
		JRXmlDigester digester = new JRXmlDigester(parser);
		
		//normally not required because schemaSource is set, but keeping to be safe
		setComponentsInternalEntityResources(jasperReportsContext, digester);
		
		configureDigester(jasperReportsContext, digester);
		return digester;
	}


	protected static SAXParser createParser(JasperReportsContext jasperReportsContext)
	{
		String parserFactoryClass = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(
				JRSaxParserFactory.PROPERTY_REPORT_PARSER_FACTORY);
		
		if (log.isDebugEnabled())
		{
			log.debug("Using SAX parser factory class " + parserFactoryClass);
		}
		
		JRSaxParserFactory factory = BaseSaxParserFactory.getFactory(jasperReportsContext, parserFactoryClass);
		return factory.createParser();
	}


	public static void setComponentsInternalEntityResources(
		JasperReportsContext jasperReportsContext,
		JRXmlDigester digester
		)
	{
		Collection<ComponentsBundle> components = ComponentsEnvironment.getInstance(jasperReportsContext).getBundles();
		for (Iterator<ComponentsBundle> it = components.iterator(); it.hasNext();)
		{
			ComponentsBundle componentManager = it.next();
			ComponentsXmlParser xmlParser = componentManager.getXmlParser();
			String schemaResource = xmlParser.getInternalSchemaResource();
			digester.addInternalEntityResource(xmlParser.getPublicSchemaLocation(), 
					schemaResource);
		}
		
		Collection<PartComponentsBundle> parts = PartComponentsEnvironment.getInstance(jasperReportsContext).getBundles();
		for (Iterator<PartComponentsBundle> it = parts.iterator(); it.hasNext();)
		{
			PartComponentsBundle componentManager = it.next();
			ComponentsXmlParser xmlParser = componentManager.getXmlParser();
			String schemaResource = xmlParser.getInternalSchemaResource();
			digester.addInternalEntityResource(xmlParser.getPublicSchemaLocation(), 
					schemaResource);
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
		@Override
		public void error(SAXParseException exception) throws SAXException
		{
			throw exception;
		}

		/**
		 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
		 */
		@Override
		public void fatalError(SAXParseException exception) throws SAXException
		{
			throw exception;
		}

		/**
		 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
		 */
		@Override
		public void warning(SAXParseException exception) throws SAXException
		{
			throw exception;
		}
	}

}
