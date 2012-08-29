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
package net.sf.jasperreports.components.headertoolbar;

import java.awt.GraphicsEnvironment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import net.sf.jasperreports.components.BaseElementHtmlHandler;
import net.sf.jasperreports.components.headertoolbar.actions.EditColumnHeaderData;
import net.sf.jasperreports.components.headertoolbar.actions.EditColumnValueData;
import net.sf.jasperreports.components.headertoolbar.actions.FilterAction;
import net.sf.jasperreports.components.headertoolbar.actions.SortAction;
import net.sf.jasperreports.components.sort.FieldFilter;
import net.sf.jasperreports.components.sort.FilterTypeBooleanOperatorsEnum;
import net.sf.jasperreports.components.sort.FilterTypeDateOperatorsEnum;
import net.sf.jasperreports.components.sort.FilterTypeNumericOperatorsEnum;
import net.sf.jasperreports.components.sort.FilterTypeTextOperatorsEnum;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.sort.actions.FilterCommand;
import net.sf.jasperreports.components.sort.actions.FilterData;
import net.sf.jasperreports.components.sort.actions.SortData;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.JREnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.ReportInteractionHyperlinkProducer;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class HeaderToolbarElementHtmlHandler extends BaseElementHtmlHandler
{
	private static final String DEFAULT_DATE_PATTERN_BUNDLE = "net.sf.jasperreports.components.messages";
	private static final String DEFAULT_DATE_PATTERN_KEY = "net.sf.jasperreports.components.date.pattern";
	private static final String DEFAULT_CALENDAR_DATE_PATTERN_KEY = "net.sf.jasperreports.components.calendar.date.pattern";
	private static final String DATE_PATTERN_BUNDLE = DEFAULT_DATE_PATTERN_KEY + ".bundle";
	private static final String DATE_PATTERN_KEY = DEFAULT_DATE_PATTERN_KEY + ".key";
	private static final String CALENDAR_DATE_PATTERN_BUNDLE = DEFAULT_CALENDAR_DATE_PATTERN_KEY + ".bundle";
	private static final String CALENDAR_DATE_PATTERN_KEY = DEFAULT_CALENDAR_DATE_PATTERN_KEY + ".key";
	
//	private static final String RESOURCE_HEADERTOOLBAR_JS = "net/sf/jasperreports/components/headertoolbar/resources/jasperreports-tableHeaderToolbar.js";
	private static final String RESOURCE_HEADERTOOLBAR_JS = "net/sf/jasperreports/components/headertoolbar/resources/jive.js";
//	private static final String RESOURCE_HEADERTOOLBAR_CSS = "net/sf/jasperreports/components/headertoolbar/resources/jasperreports-tableHeaderToolbar.vm.css";
	private static final String RESOURCE_HEADERTOOLBAR_CSS = "net/sf/jasperreports/components/headertoolbar/resources/jive.vm.css";

	private static final String RESOURCE_JIVE_COLUMN_JS = "net/sf/jasperreports/components/headertoolbar/resources/jive.interactive.column.js";
	private static final String RESOURCE_JIVE_COLUMN_I18N_JS = "net/sf/jasperreports/components/headertoolbar/resources/jive.interactive.column.i18n.vm.js";

	private static final String CSS_FILTER_DISABLED = 		"filterBtnDisabled";
	private static final String CSS_FILTER_DEFAULT = 		"filterBtnDefault";
	private static final String CSS_FILTER_DEFAULT_HOVER = 	"filterBtnDefaultHover";
	private static final String CSS_FILTER_ENABLED = 		"filterBtnEnabled";
	private static final String CSS_FILTER_ENABLED_HOVER = 	"filterBtnEnabledHover";
	private static final String CSS_FILTER_WRONG = 			"filterBtnWrong";
	private static final String CSS_FILTER_WRONG_HOVER = 	"filterBtnWrongHover";
	
	private static final String CSS_SORT_DEFAULT_ASC = 			"sortAscBtnDefault";
	private static final String CSS_SORT_DEFAULT_ASC_HOVER = 	"sortAscBtnDefaultHover";
	private static final String CSS_SORT_ENABLED_ASC = 			"sortAscBtnEnabled";
	private static final String CSS_SORT_ENABLED_ASC_HOVER = 	"sortAscBtnEnabledHover";
	
	private static final String CSS_SORT_DEFAULT_DESC = 		"sortDescBtnDefault";
	private static final String CSS_SORT_DEFAULT_DESC_HOVER = 	"sortDescBtnDefaultHover";
	private static final String CSS_SORT_ENABLED_DESC = 		"sortDescBtnEnabled";
	private static final String CSS_SORT_ENABLED_DESC_HOVER = 	"sortDescBtnEnabledHover";
	
	private static final String SORT_ELEMENT_HTML_TEMPLATE = 	"net/sf/jasperreports/components/headertoolbar/resources/HeaderToolbarElementHtmlTemplate.vm";
//	private static final String SORT_ELEMENT_HTML_TEMPLATE = 	"net/sf/jasperreports/components/headertoolbar/resources/HeaderToolbarElementHtmlTemplate.vm.orig";
	
	private static final String PARAM_GENERATED_TEMPLATE_PREFIX = "net.sf.jasperreports.headertoolbar.";
	
	private static final List<String> datePatterns = new ArrayList<String>(); 
	private static final Map<String, String> numberPatternsMap = new LinkedHashMap<String, String>(); 
	
	static {
		// date patterns
		datePatterns.add("dd/MM/yyyy");
		datePatterns.add("MM/dd/yyyy");
		datePatterns.add("yyyy/MM/dd");
		datePatterns.add("EEEEE dd MMMMM yyyy");
		datePatterns.add("MMMMM dd. yyyy");
		datePatterns.add("dd/MM");
		datePatterns.add("dd/MM/yy");
		datePatterns.add("dd-MMM");
		datePatterns.add("dd-MMM-yy");
		datePatterns.add("MMM-yy");
		datePatterns.add("MMMMM-yy");
		datePatterns.add("dd/MM/yyyy h.mm a");
		datePatterns.add("dd/MM/yyyy HH.mm.ss");
		datePatterns.add("MMM");
		datePatterns.add("d/M/yyyy");
		datePatterns.add("dd-MMM-yyyy");
		datePatterns.add("yyyy.MM.dd G 'at' HH:mm:ss z");
		datePatterns.add("EEE. MMM d. ''yy");
		datePatterns.add("yyyy.MMMMM.dd GGG hh:mm aaa");
		datePatterns.add("EEE. d MMM yyyy HH:mm:ss Z");
		datePatterns.add("yyMMddHHmmssZ");

		numberPatternsMap.put("###0;-###0", "-1234");
		numberPatternsMap.put("###0;###0-", "1234-");
		numberPatternsMap.put("###0;(###0)", "(1234)");
		numberPatternsMap.put("###0;(-###0)", "(-1234)");
		numberPatternsMap.put("###0;(###0-)", "(1234-)");
	}
	
	private static class CustomJRExporterParameter extends JRExporterParameter{

		protected CustomJRExporterParameter(String name) {
			super(name);
		}
	}
	
	private static final CustomJRExporterParameter param = new HeaderToolbarElementHtmlHandler.CustomJRExporterParameter("exporter_first_attempt");

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		boolean templateAlreadyLoaded = false;

		String htmlFragment = null;
		String tableUUID = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_TABLE_UUID);
		ReportContext reportContext = context.getExporter().getReportContext();
		if (reportContext != null && tableUUID != null)//FIXMEJIVE
		{
			String popupId = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_POPUP_ID);
			String columnLabel = (String)element.getParameterValue(HeaderToolbarElement.PARAMETER_COLUMN_LABEL);
			int columnIndex = Integer.parseInt(element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_INDEX));
			
			Map<String, Object> contextMap = new HashMap<String, Object>();
			contextMap.put("JRStringUtil", JRStringUtil.class);
			contextMap.put("tableUUID", tableUUID);
			
			JasperReportsContext jrContext = context.getJasperReportsContext();
			WebUtil webUtil = WebUtil.getInstance(jrContext);
			String webResourcesBasePath = webUtil.getResourcesBasePath();
			
			Locale locale = (Locale) reportContext.getParameterValue(JRParameter.REPORT_LOCALE);
			
			if (locale == null) {
				locale = Locale.getDefault();
			}
			
			if (reportContext.getParameterValue(PARAM_GENERATED_TEMPLATE_PREFIX) != null) {
				templateAlreadyLoaded = true;
			} else {
				reportContext.setParameterValue(PARAM_GENERATED_TEMPLATE_PREFIX, true);
				
				contextMap.put("actionBaseUrl", getActionBaseUrl(context));
				contextMap.put("actionBaseData", getActionBaseJsonData(context));
				contextMap.put("jasperreports_tableHeaderToolbar_js", webUtil.getResourcePath(webResourcesBasePath, HeaderToolbarElementHtmlHandler.RESOURCE_HEADERTOOLBAR_JS));
				contextMap.put("jasperreports_tableHeaderToolbar_css", webUtil.getResourcePath(webResourcesBasePath, HeaderToolbarElementHtmlHandler.RESOURCE_HEADERTOOLBAR_CSS, true));
				contextMap.put("jiveColumnScript", webUtil.getResourcePath(webResourcesBasePath, HeaderToolbarElementHtmlHandler.RESOURCE_JIVE_COLUMN_JS));
				contextMap.put("jiveColumnI18n_js",
								webUtil.getResourcePath(
										webResourcesBasePath,
										HeaderToolbarElementHtmlHandler.RESOURCE_JIVE_COLUMN_I18N_JS,
										"net.sf.jasperreports.components.headertoolbar.messages",
										locale));
				contextMap.put("msgProvider", MessageUtil.getInstance(jrContext).getLocalizedMessageProvider("net.sf.jasperreports.components.headertoolbar.messages", locale));
			}
			
			if (!(context.getExportParameters().containsKey(param) 
					&& tableUUID.equals(context.getExportParameters().get(param)))) {
				Map<String, ColumnInfo> columnNames = getAllColumnNames(element, jrContext, contextMap);
				// column names are normally set on the first column, but check if we got them
				if (!columnNames.isEmpty()) {
					context.getExportParameters().put(param, tableUUID);

					contextMap.put("allColumnNames", JacksonUtil.getInstance(jrContext).getJsonString(columnNames));
					contextMap.put("exporterFirstAttempt", true);
				}
			}
			
			contextMap.put("templateAlreadyLoaded", templateAlreadyLoaded);
			
			Boolean canSort = Boolean.parseBoolean(element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_CAN_SORT));
			
			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				contextMap.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}
			
			contextMap.put("elementX", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			contextMap.put("elementY", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
			contextMap.put("elementWidth", element.getWidth());
			contextMap.put("elementHeight", element.getHeight());

			contextMap.put("popupId", popupId);
			contextMap.put("columnLabel", columnLabel);
			contextMap.put("columnIndex", columnIndex);
			contextMap.put("canSort", canSort);
			
			contextMap.put("fontExtensionsFontNames", getFontExtensionsFontNames());
			contextMap.put("systemFontNames", getSystemFontNames());
			
			setColumnHeaderData(columnLabel, columnIndex, tableUUID, contextMap, jrContext, reportContext);
			setColumnValueData(columnLabel, columnIndex, tableUUID, contextMap, jrContext, reportContext);
			
			if (canSort) {
				String columnName = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_NAME);
				String columnType = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_TYPE);
				
				FilterTypesEnum filterType = FilterTypesEnum.getByName(element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_FILTER_TYPE));
				if (filterType == null)//FIXMEJIVE
				{
					return null;
				}
				
				String filterPattern = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_FILTER_PATTERN);
				String calendarPattern = null;
				if (filterPattern == null) {
					filterPattern = "";
				}
				
				Map<String, String> translatedOperators = null;
				Map<String, String> valuesFormatPatternMap = new LinkedHashMap<String, String>();
				Boolean hasPattern = true;
				Boolean isNumeric = false;
				String formatPatternLabel = "";
				switch (filterType) {
				case NUMERIC:
					translatedOperators = getTranslatedOperators(jrContext, FilterTypeNumericOperatorsEnum.class.getName(), FilterTypeNumericOperatorsEnum.values(), locale);
					valuesFormatPatternMap = numberPatternsMap;//setNumberPatterns(valuesFormatPatternMap, numberPatterns);
					formatPatternLabel = "Number pattern:";
					isNumeric = true;
					break;
				case DATE:
					translatedOperators = getTranslatedOperators(jrContext, FilterTypeDateOperatorsEnum.class.getName(), FilterTypeDateOperatorsEnum.values(), locale);
					setDatePatterns(valuesFormatPatternMap, datePatterns, locale);
					formatPatternLabel = "Date pattern:";

					String datePatternBundleName = JRPropertiesUtil.getInstance(jrContext).getProperty(DATE_PATTERN_BUNDLE);
					if (datePatternBundleName == null)
					{
						datePatternBundleName = DEFAULT_DATE_PATTERN_BUNDLE;
					}
					String datePatternKey = JRPropertiesUtil.getInstance(jrContext).getProperty(DATE_PATTERN_KEY);
					if (datePatternKey == null)
					{
						datePatternKey = DEFAULT_DATE_PATTERN_KEY;
					}
					filterPattern = getBundleMessage(datePatternKey, jrContext, datePatternBundleName, locale);
					
					String calendarDatePatternBundleName = JRPropertiesUtil.getInstance(jrContext).getProperty(CALENDAR_DATE_PATTERN_BUNDLE);
					if (calendarDatePatternBundleName == null)
					{
						calendarDatePatternBundleName = DEFAULT_DATE_PATTERN_BUNDLE;
					}
					String calendarDatePatternKey = JRPropertiesUtil.getInstance(jrContext).getProperty(CALENDAR_DATE_PATTERN_KEY);
					if (calendarDatePatternKey == null)
					{
						calendarDatePatternKey = DEFAULT_CALENDAR_DATE_PATTERN_KEY;
					}
					calendarPattern = getBundleMessage(calendarDatePatternKey, jrContext, calendarDatePatternBundleName, locale);
					
					break;
				case TEXT:
					translatedOperators = getTranslatedOperators(jrContext, FilterTypeTextOperatorsEnum.class.getName(), FilterTypeTextOperatorsEnum.values(), locale);
					hasPattern = false;
					break;
				case BOOLEAN:
					translatedOperators = getTranslatedOperators(jrContext, FilterTypeBooleanOperatorsEnum.class.getName(), FilterTypeBooleanOperatorsEnum.values(), locale);
					hasPattern = false;
					break;
				}
				
				SortData sortAscData = new SortData(tableUUID, columnName, columnType, HeaderToolbarElement.SORT_ORDER_ASC);
				SortData sortDescData = new SortData(tableUUID, columnName, columnType, HeaderToolbarElement.SORT_ORDER_DESC);
				
				String sortAscActive = CSS_SORT_DEFAULT_ASC;
				String sortAscHover = CSS_SORT_DEFAULT_ASC_HOVER;
				String sortDescActive = CSS_SORT_DEFAULT_DESC;
				String sortDescHover = CSS_SORT_DEFAULT_DESC_HOVER;
				String filterActive = CSS_FILTER_DISABLED;
				String filterHover = "";
				
				if (filterType != null) {
					filterActive = CSS_FILTER_DEFAULT;
					filterHover = CSS_FILTER_DEFAULT_HOVER;
				}
				
				String sortField = getCurrentSortField(jrContext, reportContext, tableUUID, columnName, columnType);
				if (sortField != null) 
				{
					String[] sortActionData = HeaderToolbarElementUtils.extractColumnInfo(sortField);
					
					boolean isAscending = HeaderToolbarElement.SORT_ORDER_ASC.equals(sortActionData[2]);
					if (isAscending) {
						sortAscData.setSortOrder(HeaderToolbarElement.SORT_ORDER_NONE);
						sortAscActive = CSS_SORT_ENABLED_ASC;
						sortAscHover = CSS_SORT_ENABLED_ASC_HOVER;
					} else {
						sortDescData.setSortOrder(HeaderToolbarElement.SORT_ORDER_NONE);
						sortDescActive = CSS_SORT_ENABLED_DESC;
						sortDescHover = CSS_SORT_ENABLED_DESC_HOVER;
					}
				}
				
				// existing filters
				String filterValueStart = "";
				String filterValueEnd = "";
				String filterTypeOperatorValue = "";
				List<DatasetFilter> fieldFilters = getExistingFiltersForField(jrContext, reportContext, tableUUID, columnName);
				
				if (fieldFilters.size() > 0) {
					FieldFilter ff = (FieldFilter)fieldFilters.get(0);
					if (ff.getFilterValueStart() != null) {
						filterValueStart = ff.getFilterValueStart();
					}
					if (ff.getFilterValueEnd() != null) {
						filterValueEnd = ff.getFilterValueEnd();
					}
					filterTypeOperatorValue = ff.getFilterTypeOperator();
					if (ff.getIsValid() != null && !ff.getIsValid()) {
						filterActive = CSS_FILTER_WRONG;
						filterHover = CSS_FILTER_WRONG_HOVER;
					} else {
						filterActive = CSS_FILTER_ENABLED;
						filterHover = CSS_FILTER_ENABLED_HOVER;
					}
				}
				
				contextMap.put("hasPattern", hasPattern);
				contextMap.put("isNumeric", isNumeric);
				if (hasPattern) {
					contextMap.put("formatPatternLabel", formatPatternLabel);
					contextMap.put("valuesFormatPatternMap", valuesFormatPatternMap);
				}
				
				// begin: the params that will generate the JSON post object for filtering
				FilterData filterData = new FilterData();
				filterData.setTableUuid(tableUUID);
				filterData.setFieldName(columnName);
				filterData.setFilterType(filterType.getName());
				filterData.setFilterPattern(filterPattern);
				filterData.setCalendarPattern(calendarPattern);
				filterData.setFieldValueStart(filterValueStart);
				filterData.setFieldValueEnd(filterValueEnd);
				filterData.setFilterTypeOperator(filterTypeOperatorValue);
				
				contextMap.put("filterData", JacksonUtil.getInstance(jrContext).getJsonString(filterData));
				contextMap.put("filterTypeValuesMap", translatedOperators);
				contextMap.put("filterTypeOperatorValue", filterTypeOperatorValue);
				contextMap.put("filterTableUuid", tableUUID);
				
				contextMap.put("filterColumnNameLabel", columnLabel != null ? columnLabel : "");
				// end
				
				// begin: params for sorting
				contextMap.put("sortAscData", JacksonUtil.getInstance(jrContext).getJsonString(sortAscData));
				contextMap.put("sortDescData", JacksonUtil.getInstance(jrContext).getJsonString(sortDescData));
				contextMap.put("sortAscActive", sortAscActive);
				contextMap.put("sortAscHover", sortAscHover);
				contextMap.put("sortDescActive", sortDescActive);
				contextMap.put("sortDescHover", sortDescHover);
				contextMap.put("filterActive", filterActive);
				contextMap.put("filterHover", filterHover);
				// end: temp
			}
			
			htmlFragment = VelocityUtil.processTemplate(HeaderToolbarElementHtmlHandler.SORT_ELEMENT_HTML_TEMPLATE, contextMap);
		}
		
		return htmlFragment;
	}
	
	private void setDatePatterns(
			Map<String, String> valuesFormatPatternMap,
			List<String> datePatterns,
			Locale locale) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", locale);
		Date today = new Date();
		
		for(String datePattern: datePatterns) {
			sdf.applyPattern(datePattern);
			valuesFormatPatternMap.put(datePattern, sdf.format(today));
		}
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

	private String getCurrentSortField(
		JasperReportsContext jasperReportsContext,
		ReportContext reportContext, 
		String uuid, 
		String sortColumnName, 
		String sortColumnType
		) 
	{
		JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
		SortAction action = new SortAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(uuid));
		if (target != null)
		{
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
			
			JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getDatasetRun();
			
			String datasetName = datasetRun.getDatasetName();
			
			JasperDesign jasperDesign = cache.getJasperDesign(target.getUri());//FIXMEJIVE getJasperReport not design
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getDatasetMap().get(datasetName);
			
			List<JRSortField> existingFields =  dataset.getSortFieldsList();
			String sortField = null;
	
			if (existingFields != null && existingFields.size() > 0) {
				for (JRSortField field: existingFields) {
					if (field.getName().equals(sortColumnName) && field.getType().getName().equals(sortColumnType)) {
						sortField = sortColumnName + HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR + sortColumnType + HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR;
						switch (field.getOrderValue()) {
							case ASCENDING:
								sortField += HeaderToolbarElement.SORT_ORDER_ASC;
								break;
							case DESCENDING:
								sortField += HeaderToolbarElement.SORT_ORDER_DESC;
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
		) //FIXMEJIVE make utility method for translating enums
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
	
	private List<DatasetFilter> getExistingFiltersForField(
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
		List<DatasetFilter> result = new ArrayList<DatasetFilter>();
		if (target != null)
		{
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
			
			JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getDatasetRun();
			
			String datasetName = datasetRun.getDatasetName();
			
			JasperDesign jasperDesign = cache.getJasperDesign(target.getUri());//FIXMEJIVE getJasperReport not design
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getDatasetMap().get(datasetName);
			
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
						result.add(filter);
						break;
					}
				}
			}
		}
		
		return result;		
	}
	
	private void setColumnHeaderData(String sortColumnLabel, Integer columnIndex, String tableUuid, Map<String, Object> contextMap, JasperReportsContext jasperReportsContext, ReportContext reportContext) {
		FilterAction action = new FilterAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(tableUuid));
		EditColumnHeaderData colHeaderData = new EditColumnHeaderData();
		
		if (target != null){
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
			
			List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
			
			if (columnIndex != null) {
				StandardColumn column = (StandardColumn) tableColumns.get(columnIndex);
				
				JRDesignTextElement textElement = TableUtil.getColumnHeaderTextElement(column);
				
				if (textElement != null) {
					colHeaderData.setHeadingName(JRStringUtil.escapeJavaScript(sortColumnLabel));
					colHeaderData.setColumnIndex(columnIndex);
					colHeaderData.setTableUuid(tableUuid);
					colHeaderData.setFontName(textElement.getFontName());
					colHeaderData.setFontSize(String.valueOf(textElement.getFontSize()));
					colHeaderData.setFontBold(textElement.isBold());
					colHeaderData.setFontItalic(textElement.isItalic());
					colHeaderData.setFontUnderline(textElement.isUnderline());
					colHeaderData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
					colHeaderData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
				}
			}
		}
		contextMap.put("colHeaderData", JacksonUtil.getInstance(jasperReportsContext).getJsonString(colHeaderData));
	}

	private void setColumnValueData(String sortColumnLabel, Integer columnIndex, String tableUuid, Map<String, Object> contextMap, JasperReportsContext jasperReportsContext, ReportContext reportContext) {
		FilterAction action = new FilterAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(tableUuid));
		EditColumnValueData colValueData = new EditColumnValueData();
		
		if (target != null){
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
			
			List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
			
			if (columnIndex != null) {
				StandardColumn column = (StandardColumn) tableColumns.get(columnIndex);
				
				JRDesignTextField textElement = (JRDesignTextField)TableUtil.getColumnDetailTextElement(column);
				
				if (textElement != null) {
					colValueData.setHeadingName(JRStringUtil.escapeJavaScript(sortColumnLabel));
					colValueData.setColumnIndex(columnIndex);
					colValueData.setTableUuid(tableUuid);
					colValueData.setFontName(textElement.getFontName());
					colValueData.setFontSize(String.valueOf(textElement.getFontSize()));
					colValueData.setFontBold(textElement.isBold());
					colValueData.setFontItalic(textElement.isItalic());
					colValueData.setFontUnderline(textElement.isUnderline());
					colValueData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
					colValueData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
					colValueData.setFormatPattern(textElement.getPattern());
				}
			}
		}
		contextMap.put("colValueData", JacksonUtil.getInstance(jasperReportsContext).getJsonString(colValueData));
	}

	public static class ColumnInfo {
		private String index;
		private String label;
		private String uuid;
		private boolean visible;
		
		private ColumnInfo(String index, String label, String uuid, boolean visible) {
			this.index = index;
			this.label = label;
			this.uuid = uuid;
			this.visible = visible;
		}
		
		public String getIndex() {
			return index;
		}
		
		public String getLabel() {
			return label;
		}

		public String getUuid() {
			return uuid;
		}
		
		public boolean getVisible() {
			return visible;
		}
	}

	private Map<String, ColumnInfo> getAllColumnNames(JRGenericPrintElement element, 
			JasperReportsContext jasperReportsContext, Map<String, Object> contextMap) {
		int prefixLength = HeaderToolbarElement.PARAM_COLUMN_LABEL_PREFIX.length();
		Map<String, ColumnInfo> columnNames = new HashMap<String, ColumnInfo>();
		for (String paramName : element.getParameterNames()) {
			if (paramName.startsWith(HeaderToolbarElement.PARAM_COLUMN_LABEL_PREFIX)) {
				String columnName = (String) element.getParameterValue(paramName);
				String[] tokens = paramName.substring(prefixLength).split("\\|");
				if (columnName == null || columnName.trim().length() == 0) {
					columnName = "Column_" + tokens[0];
				}
				columnNames.put(tokens[0], new ColumnInfo(tokens[0], columnName, tokens[1], false));
			}
		}
		return columnNames;
	}
	
	private Set<String> getFontExtensionsFontNames() {
		Set<String> classes = new TreeSet<String>(); 

        Collection<String> extensionFonts = JRFontUtil.getFontFamilyNames();
        for (Iterator<String> it = extensionFonts.iterator(); it.hasNext();) {
            String fname = it.next();
            classes.add(fname);
        }

        return classes;
    } 

	private Set<String> getSystemFontNames() {
		Set<String> fontExtensionsFontNames = getFontExtensionsFontNames();
		Set<String> classes = new TreeSet<String>();

		String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			if (fontExtensionsFontNames.add(name)) {
				classes.add(name);
			}
		}
		
		return classes;
	}
	
	private String getBundleMessage(String key, JasperReportsContext jasperReportsContext, String bundleName, Locale locale) {
		MessageProvider messageProvider = MessageUtil.getInstance(jasperReportsContext).getMessageProvider(bundleName);
		return messageProvider.getMessage(key, null, locale); 
	}

}
