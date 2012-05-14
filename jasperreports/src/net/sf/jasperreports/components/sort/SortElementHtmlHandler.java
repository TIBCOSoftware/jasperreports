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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.sf.jasperreports.components.BaseElementHtmlHandler;
import net.sf.jasperreports.components.sort.actions.FilterAction;
import net.sf.jasperreports.components.sort.actions.FilterCommand;
import net.sf.jasperreports.components.sort.actions.SortAction;
import net.sf.jasperreports.components.sort.actions.SortData;
import net.sf.jasperreports.engine.CompositeDatasetFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.JREnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.ReportInteractionHyperlinkProducer;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class SortElementHtmlHandler extends BaseElementHtmlHandler
{
	private static final Log log = LogFactory.getLog(SortElementHtmlHandler.class);
	
	private static final String RESOURCE_SORT_JS = "net/sf/jasperreports/components/sort/resources/sort.js";
	private static final String RESOURCE_HEADERTOOLBAR_CSS = "net/sf/jasperreports/components/headertoolbar/resources/jasperreports-tableHeaderToolbar.vm.css";
	
	private static final String CSS_FILTER_DEFAULT = 		"filterBtnDefault";
	private static final String CSS_FILTER_WRONG = 			"filterBtnWrong";
	private static final String CSS_SORT_DEFAULT_ASC = 		"sortAscBtnDefault";
	private static final String CSS_SORT_DEFAULT_DESC = 	"sortDescBtnDefault";

	private static final String SORT_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/sort/resources/SortElementHtmlTemplate.vm";
	
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";
	protected static final String SORT_SYMBOL_ASCENDING = "&#9650;";
	protected static final String SORT_SYMBOL_DESCENDING = "&#9660;";
	
	protected static final String FILTER_SYMBOL_ACTIVE = "Active";
	protected static final String FILTER_SYMBOL_INACTIVE = "Inactive";

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
			if (filterType == null)//FIXMEJIVE
			{
				return null;
			}
			
			String filterPattern = element.getPropertiesMap().getProperty(SortElement.PROPERTY_FILTER_PATTERN);

			Locale locale = (Locale) reportContext.getParameterValue(JRParameter.REPORT_LOCALE);
			
			if (log.isDebugEnabled()) {
				log.debug("report locale: " + locale);
			}
			
			if (locale == null) {
				locale = Locale.getDefault();
			}
			
			Map<String, String> translatedOperators = null;
			boolean isBooleanFilterType = false;
			switch (filterType) {
				case NUMERIC:
					translatedOperators = getTranslatedOperators(context.getJasperReportsContext(), FilterTypeNumericOperatorsEnum.class.getName(), FilterTypeNumericOperatorsEnum.values(), locale);
					break;
				case DATE:
					translatedOperators = getTranslatedOperators(context.getJasperReportsContext(), FilterTypeDateOperatorsEnum.class.getName(), FilterTypeDateOperatorsEnum.values(), locale);
					break;
				case TEXT:
					translatedOperators = getTranslatedOperators(context.getJasperReportsContext(), FilterTypeTextOperatorsEnum.class.getName(), FilterTypeTextOperatorsEnum.values(), locale);
					break;
				case BOOLEAN:
					translatedOperators = getTranslatedOperators(context.getJasperReportsContext(), FilterTypeBooleanOperatorsEnum.class.getName(), FilterTypeBooleanOperatorsEnum.values(), locale);
					isBooleanFilterType = true;
					break;
			}
			
			VelocityContext velocityContext = new VelocityContext();
			WebUtil webUtil = WebUtil.getInstance(context.getJasperReportsContext());
			String webResourcesBasePath = webUtil.getResourcesBasePath();
			velocityContext.put("actionBaseUrl", getActionBaseUrl(context));
			velocityContext.put("actionBaseData", getActionBaseJsonData(context));
			
			velocityContext.put("resourceSortJs", webUtil.getResourcePath(webResourcesBasePath, SortElementHtmlHandler.RESOURCE_SORT_JS));
			velocityContext.put("jasperreports_tableHeaderToolbar_css", webUtil.getResourcePath(webResourcesBasePath, SortElementHtmlHandler.RESOURCE_HEADERTOOLBAR_CSS, true));
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
			String reportUriParamName = JRPropertiesUtil.getInstance(context.getJasperReportsContext()).getProperty(WebUtil.PROPERTY_REQUEST_PARAMETER_REPORT_URI);
			velocityContext.put("filterReportUriParamName", reportUriParamName);
			velocityContext.put("filterReportUriParamValue", reportContext.getParameterValue(reportUriParamName));
			velocityContext.put("filterColumnName", sortColumnName);
			velocityContext.put("filterTableNameValue", sortDatasetName);
			velocityContext.put("filterTypeParamNameValue", filterType.getName());
			velocityContext.put("filterPatternParamValue", filterPattern);
			velocityContext.put("filterTypeValuesMap", translatedOperators);
			velocityContext.put("isBooleanFilterType", isBooleanFilterType);
			
			
			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}

			String sortField = getCurrentSortField(context.getJasperReportsContext(), reportContext, element.getUUID().toString(), sortDatasetName, sortColumnName, sortColumnType);
			SortData sortData = null;
			if (sortField == null) 
			{
				sortData = new SortData(element.getUUID().toString(), sortColumnName, sortColumnType, SortElement.SORT_ORDER_ASC);
				velocityContext.put("isSorted", false);
			}
			else 
			{
				String[] sortActionData = SortElementUtils.extractColumnInfo(sortField);
				boolean isAscending = SortElement.SORT_ORDER_ASC.equals(sortActionData[2]);
				String sortOrder = !isAscending ? SortElement.SORT_ORDER_NONE : SortElement.SORT_ORDER_DESC;
				sortData = new SortData(element.getUUID().toString(), sortColumnName, sortColumnType, sortOrder);
				velocityContext.put("isSorted", true);
				velocityContext.put("sortSymbolResource", isAscending ? CSS_SORT_DEFAULT_ASC : CSS_SORT_DEFAULT_DESC);
			}
			velocityContext.put("tableUuid", element.getUUID().toString());
			velocityContext.put("actionData", getActionData(context, sortData));
			
			// existing filters
			String filterValueStart = "";
			String filterValueEnd = "";
			String filterTypeOperatorValue = "";
			String filterActiveInactive = FILTER_SYMBOL_INACTIVE;
			boolean isFiltered = false;
			boolean enableFilterEndParameter = false;
			List<FieldFilter> fieldFilters = new ArrayList<FieldFilter>();
			String filterSymbolImageResource = CSS_FILTER_DEFAULT;

			fieldFilters = getExistingFiltersForField(context.getJasperReportsContext(), reportContext, element.getUUID().toString(), sortColumnName);
			
			if (fieldFilters.size() > 0) {
				FieldFilter ff = fieldFilters.get(0);
				if (ff.getFilterValueStart() != null) {
					filterValueStart = ff.getFilterValueStart();
				}
				if (ff.getFilterValueEnd() != null) {
					filterValueEnd = ff.getFilterValueEnd();
				}
				filterTypeOperatorValue = ff.getFilterTypeOperator();
				filterActiveInactive = FILTER_SYMBOL_ACTIVE;
				isFiltered = true;
				if (filterTypeOperatorValue != null && filterTypeOperatorValue.toLowerCase().contains("between")) {
					enableFilterEndParameter = true;
				}
				if (ff.getIsValid() != null && !ff.getIsValid()) {
					filterSymbolImageResource = CSS_FILTER_WRONG;
				}
				
			}
			
			velocityContext.put("isFiltered", isFiltered);
			velocityContext.put("filterSymbolImageResource", filterSymbolImageResource);
			velocityContext.put("filterToRemoveParamName", SortElement.REQUEST_PARAMETER_REMOVE_FILTER);
			velocityContext.put("filterToRemoveParamvalue", sortColumnName);
			
			String filtersJsonString = getJsonString(fieldFilters).replaceAll("\\\"", "\\\\\\\"");
			if (log.isDebugEnabled()) {
				log.debug("filtersJsonString: " + filtersJsonString);
			}
			velocityContext.put("filtersJsonString", filtersJsonString);

			velocityContext.put("filterValueStartParamValue", filterValueStart);
			velocityContext.put("filterValueEndParamValue", filterValueEnd);
			velocityContext.put("filterTypeOperatorParamValue", filterTypeOperatorValue);
			velocityContext.put("filterActiveInactive", filterActiveInactive);
			velocityContext.put("enableFilterEndParameter", enableFilterEndParameter);
			
			htmlFragment = VelocityUtil.processTemplate(SortElementHtmlHandler.SORT_ELEMENT_HTML_TEMPLATE, velocityContext);
		}
		
		return htmlFragment;
	}
	
	private String getCurrentSortField(JasperReportsContext jasperReportsContext, ReportContext reportContext, String uuid, String sortDatasetName, String sortColumnName, String sortColumnType) 
	{
		JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
		SortAction action = new SortAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(uuid));
		if (target != null)
		{
			JasperDesign jasperDesign = cache.getJasperDesign(target.getUri());
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getMainDataset();
			
			List<JRSortField> existingFields =  dataset.getSortFieldsList();
			String sortField = null;
	
			if (existingFields != null && existingFields.size() > 0) {
				for (JRSortField field: existingFields) {
					if (field.getName().equals(sortColumnName) && field.getType().getName().equals(sortColumnType)) {
						sortField = sortColumnName + SortElement.SORT_COLUMN_TOKEN_SEPARATOR + sortColumnType + SortElement.SORT_COLUMN_TOKEN_SEPARATOR;
						switch (field.getOrderValue()) {
							case ASCENDING:
								sortField += SortElement.SORT_ORDER_ASC;
								break;
							case DESCENDING:
								sortField += SortElement.SORT_ORDER_DESC;
								break;
						}
						break;
					}
				}
			}
		
			return sortField;
		}
		
		return null;
	}
	
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
	private Map<String, String> getTranslatedOperators(
		JasperReportsContext jasperReportsContext, 
		String bundleName, 
		JREnum[] operators, 
		Locale locale
		) 
	{
		Map<String, String> result = new LinkedHashMap<String, String>();
		MessageProvider messageProvider = MessageUtil.getInstance(jasperReportsContext).getMessageProvider(bundleName);
		
		for (JREnum operator: operators) 
		{
			String key = bundleName + "." + ((Enum<?>)operator).name();
			result.put(((Enum<?>)operator).name(), messageProvider.getMessage(key, null, locale));
		}
		
		return result;
	}
	
	public static void getFieldFilters(DatasetFilter existingFilter, List<FieldFilter> fieldFilters, String fieldName) {//FIXMEJIVE put this in some util and reuse
		if (existingFilter instanceof FieldFilter) {
			if ( fieldName == null || (fieldName != null && ((FieldFilter)existingFilter).getField().equals(fieldName))) {
				fieldFilters.add((FieldFilter)existingFilter);
			} 
		} else if (existingFilter instanceof CompositeDatasetFilter) {
			for (DatasetFilter filter : ((CompositeDatasetFilter)existingFilter).getFilters())
			{
				getFieldFilters(filter, fieldFilters, fieldName);
			}
		}
	}
	
	private String getJsonString(List<FieldFilter> fieldFilters) {
		
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter(128);
		try {
			mapper.writeValue(writer, fieldFilters);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
		
		return writer.getBuffer().toString();
	}
	
	private String getActionBaseUrl(JRHtmlExporterContext context) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType(ReportInteractionHyperlinkProducer.HYPERLINK_TYPE_REPORT_INTERACTION);
		return context.getHyperlinkURL(hyperlink);
	}
	
	private String getActionBaseJsonData(JRHtmlExporterContext context) {
		ReportContext reportContext = context.getExporter().getReportContext();
		Map<String, Object> actionParams = new HashMap<String, Object>();
		actionParams.put(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID, reportContext.getId());
		String runReportParamName = JRPropertiesUtil.getInstance(context.getJasperReportsContext()).getProperty(WebUtil.PROPERTY_REQUEST_PARAMETER_RUN_REPORT);
		actionParams.put(runReportParamName, true);
		
//		return JacksonUtil.getInstance(context.getJasperReportsContext()).getEscapedJsonString(actionParams);
		return JacksonUtil.getInstance(context.getJasperReportsContext()).getJsonString(actionParams);
	}
	
	private String getActionData(JRHtmlExporterContext context, SortData sortData) {
		return "{\"actionName\":\"sortica\",\"sortData\":" + JacksonUtil.getInstance(context.getJasperReportsContext()).getJsonString(sortData)+ "}";
	}
	
	private List<FieldFilter> getExistingFiltersForField(
			JasperReportsContext jasperReportsContext, 
			ReportContext reportContext, 
			String uuid, 
			String filterFieldName
			) 
		{
			JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
			FilterAction action = new FilterAction();
			action.init(jasperReportsContext, reportContext);
			CommandTarget target = action.getCommandTarget(UUID.fromString(uuid));
			List<FieldFilter> result = new ArrayList<FieldFilter>();
			if (target != null)
			{
				JasperDesign jasperDesign = cache.getJasperDesign(target.getUri());
				JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getMainDataset();
				
				// get existing filter as JSON string
				String serializedFilters = "[]";
				JRPropertiesMap propertiesMap = dataset.getPropertiesMap();
				if (propertiesMap.getProperty(FilterCommand.DATASET_FILTER_PROPERTY) != null) {
					serializedFilters = propertiesMap.getProperty(FilterCommand.DATASET_FILTER_PROPERTY);
				}
				
				ObjectMapper mapper = new ObjectMapper();
				List<DatasetFilter> existingFilters = null;
				try {
					existingFilters = mapper.readValue(serializedFilters, new TypeReference<List<FieldFilter>>(){});
				} catch (Exception e) {
					throw new JRRuntimeException(e);
				}
				
				if (existingFilters.size() > 0) {
					for (DatasetFilter filter: existingFilters) {
						if (((FieldFilter)filter).getField().equals(filterFieldName)) {
							result.add((FieldFilter)filter);
							break;
						}
					}
				}
			}
			
			return result;		
		}
}
