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
package net.sf.jasperreports.components.sort;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.components.BaseElementHtmlHandler;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.JREnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.web.servlets.ReportServlet;
import net.sf.jasperreports.web.servlets.ResourceServlet;
import net.sf.jasperreports.web.util.VelocityUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class SortElementHtmlHandler extends BaseElementHtmlHandler
{
	private static final Log log = LogFactory.getLog(SortElementHtmlHandler.class);
	
	private static final String RESOURCE_SORT_JS = "net/sf/jasperreports/components/sort/resources/sort.js";
	private static final String RESOURCE_IMAGE_CLOSE = "net/sf/jasperreports/components/sort/resources/images/delete_edit.gif";
	private static final String SORT_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/sort/resources/SortElementHtmlTemplate.vm";
	
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";
	protected static final String ASCENDING_SORT_SYMBOL = "&#9650;";
	protected static final String DESCENDING_SORT_SYMBOL = "&#9660;";
	

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		String htmlFragment = null;
		ReportContext reportContext = context.getExporter().getReportContext();
		if (reportContext != null)//FIXMEJIVE
		{
			String sortColumnName = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_NAME);
			String sortColumnType = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_TYPE);
			
			String sortHandlerVAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN);
			String sortHandlerHAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN);
			String sortDatasetName = element.getPropertiesMap().getProperty(SortElement.PROPERTY_DATASET_RUN);
			
			JRBaseFont sortHandlerFont = (JRBaseFont) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT);
			if (sortHandlerFont == null) {
				sortHandlerFont = new JRBaseFont(element);
			}

			Color sortHandlerColor = (Color) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_COLOR);
			if (sortHandlerColor == null) {
				sortHandlerColor = Color.WHITE;
			}
			

			FilterTypesEnum filterType = FilterTypesEnum.getByName(element.getPropertiesMap().getProperty(SortElement.PROPERTY_FILTER_TYPE));
			
			Locale locale = (Locale) reportContext.getParameterValue(JRParameter.REPORT_LOCALE);
			
			if (log.isDebugEnabled()) {
				log.debug("report locale: " + locale);
			}
			
			if (locale == null) {
				locale = Locale.getDefault();
			}
			
			Map<String, String> translatedOperators = null;
			switch (filterType) {
				case NUMERIC:
					translatedOperators = getTranslatedOperators(FilterTypeNumericOperatorsEnum.class.getName(), FilterTypeNumericOperatorsEnum.values(), locale);
					break;
				case DATE:
					translatedOperators = getTranslatedOperators(FilterTypeDateOperatorsEnum.class.getName(), FilterTypeDateOperatorsEnum.values(), locale);
					break;
				case TEXT:
					translatedOperators = getTranslatedOperators(FilterTypeTextOperatorsEnum.class.getName(), FilterTypeTextOperatorsEnum.values(), locale);
					break;
			}
			
			String appContextPath = (String)reportContext.getParameterValue("net.sf.jasperreports.web.app.context.path");//FIXMEJIVE define constant
			
			VelocityContext velocityContext = new VelocityContext();
			String webResourcesBasePath = JRProperties.getProperty("net.sf.jasperreports.web.resources.base.path");
			if (webResourcesBasePath == null)
			{
				webResourcesBasePath = ResourceServlet.DEFAULT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=";
			}
			velocityContext.put("resourceSortJs", webResourcesBasePath + SortElementHtmlHandler.RESOURCE_SORT_JS);
			velocityContext.put("elementX", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			velocityContext.put("elementY", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
			velocityContext.put("elementWidth", element.getWidth());
			velocityContext.put("elementHeight", element.getHeight());
			velocityContext.put("sortLinkClass", sortDatasetName);
			velocityContext.put("sortHandlerHAlign", sortHandlerHAlign != null ? sortHandlerHAlign : CSS_TEXT_ALIGN_LEFT);
			velocityContext.put("sortHandlerVAlign", sortHandlerVAlign != null ? sortHandlerVAlign : HTML_VERTICAL_ALIGN_TOP);
			velocityContext.put("sortHandlerColor", JRColorUtil.getColorHexa(sortHandlerColor));
			velocityContext.put("sortHandlerFontSize", sortHandlerFont.getFontSize());
			
			velocityContext.put("isFilterable", filterType != null);
			velocityContext.put("filterDivId", "filter_" + sortDatasetName + "_" + sortColumnName);
			velocityContext.put("filterFormAction", getFilterFormActionLink(context));
			velocityContext.put("filterReportUriParamName", ReportServlet.REQUEST_PARAMETER_REPORT_URI);
			velocityContext.put("filterReportUriParamValue", reportContext.getParameterValue(ReportServlet.REQUEST_PARAMETER_REPORT_URI));
			velocityContext.put("filterFieldParamName", SortElement.REQUEST_PARAMETER_FILTER_FIELD);
//			velocityContext.put("filterValueParamName", SortElement.REQUEST_PARAMETER_FILTER_VALUE);
			velocityContext.put("filterColumnName", sortColumnName);
			velocityContext.put("filterTableNameParam", SortElement.REQUEST_PARAMETER_DATASET_RUN);
			velocityContext.put("filterTableNameValue", sortDatasetName);
			velocityContext.put("filterCloseDialogImageResource", (appContextPath == null ? "" : appContextPath) + webResourcesBasePath + SortElementHtmlHandler.RESOURCE_IMAGE_CLOSE);//FIXMEJIVE

			velocityContext.put("filterTypeParamName", SortElement.REQUEST_PARAMETER_FILTER_TYPE);
			velocityContext.put("filterTypeParamNameValue", filterType.getName());
			velocityContext.put("filterTypeOperatorParamName", SortElement.REQUEST_PARAMETER_FILTER_TYPE_OPERATOR);
			
			velocityContext.put("filterTypeValuesMap", translatedOperators);
			
			velocityContext.put("filterValueStartParamName", SortElement.REQUEST_PARAMETER_FILTER_VALUE_START);
			velocityContext.put("filterValueEndParamName", SortElement.REQUEST_PARAMETER_FILTER_VALUE_END);
			
			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}

			String sortField = getCurrentSortField(reportContext, sortDatasetName, sortColumnName, sortColumnType);
			if (sortField == null) 
			{
				velocityContext.put("href", getSortLink(context, sortColumnName, sortColumnType, SortElement.SORT_ORDER_ASC, sortDatasetName));
				velocityContext.put("sortSymbol", "");
			}
			else 
			{
				String[] sortActionData = SortElementUtils.extractColumnInfo(sortField);
				boolean isAscending = !SortElement.SORT_ORDER_ASC.equals(sortActionData[2]);
				String sortOrder = isAscending ? SortElement.SORT_ORDER_ASC : SortElement.SORT_ORDER_DESC;
				velocityContext.put("href", getSortLink(context, sortColumnName, sortColumnType, sortOrder, sortDatasetName));
				velocityContext.put("sortSymbol", isAscending ? ASCENDING_SORT_SYMBOL : DESCENDING_SORT_SYMBOL);
			}
						
			htmlFragment = VelocityUtil.processTemplate(SortElementHtmlHandler.SORT_ELEMENT_HTML_TEMPLATE, velocityContext);
		}
		
		return htmlFragment;
	}
	
	private String getSortLink(JRHtmlExporterContext context, String sortColumnName, String sortColumnType, String sortOrder, String sortTableName) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType("ReportExecution");
		
		JRPrintHyperlinkParameters parameters = new JRPrintHyperlinkParameters();
		parameters.addParameter(new JRPrintHyperlinkParameter(
				SortElement.REQUEST_PARAMETER_SORT_DATA,
				String.class.getName(), 
				SortElementUtils.packSortColumnInfo(sortColumnName, sortColumnType, sortOrder)));
		parameters.addParameter(new JRPrintHyperlinkParameter(SortElement.REQUEST_PARAMETER_DATASET_RUN, String.class.getName(), sortTableName));
		hyperlink.setHyperlinkParameters(parameters);
		
		return context.getHyperlinkURL(hyperlink);
	}
	
	private String getFilterFormActionLink(JRHtmlExporterContext context) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType("ReportExecution");
		return context.getHyperlinkURL(hyperlink);
	}
	
	
	private String getCurrentSortField(ReportContext reportContext, String sortDatasetName, String sortColumnName, String sortColumnType) 
	{
		String currentSortDataset = (String) reportContext.getParameterValue(
				SortElement.REQUEST_PARAMETER_DATASET_RUN);
		if (sortDatasetName == null || !sortDatasetName.equals(currentSortDataset))
		{
			// sorting is on a different dataset
			return null;
		}
		
		String sortField = null;
		String sortData = (String)reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_SORT_DATA);
		
		if (SortElementUtils.isValidSortData(sortData))
		{
			String[] tokens = sortData.split(",");
			for (int i = 0; i < tokens.length; i++)
			{
				String token = tokens[i];
				String[] sortActionData = SortElementUtils.extractColumnInfo(token);
				if (sortActionData.length > 1 && sortActionData[0].equals(sortColumnName) && sortActionData[1].equals(sortColumnType)) {
					sortField = token;
					break;
				}
			}
		}
		
		return sortField;
	}
	
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
	private Map<String, String> getTranslatedOperators(String bundleName, JREnum[] operators, Locale locale) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle(bundleName, locale);
		
		for (JREnum operator: operators) {
			result.put(((Enum<?>)operator).name(), rb.getString(((Enum<?>)operator).name()));
		}
		
		return result;
	}
	
 }
