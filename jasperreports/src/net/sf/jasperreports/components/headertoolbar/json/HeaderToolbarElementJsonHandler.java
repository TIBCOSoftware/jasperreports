/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.components.headertoolbar.json;

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
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.UUID;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElementUtils;
import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingData;
import net.sf.jasperreports.components.headertoolbar.actions.EditTextElementData;
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
import net.sf.jasperreports.components.table.Column;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.GenericElementJsonHandler;
import net.sf.jasperreports.engine.export.JsonExporterContext;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.NamedEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.ReportInteractionHyperlinkProducer;
import net.sf.jasperreports.web.util.VelocityUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class HeaderToolbarElementJsonHandler implements GenericElementJsonHandler
{
	private static final Log log = LogFactory.getLog(HeaderToolbarElementJsonHandler.class);
	
	private static final String HEADER_TOOLBAR_ELEMENT_JSON_TEMPLATE = "net/sf/jasperreports/components/headertoolbar/json/resources/HeaderToolbarElementJsonTemplate.vm";
	private static final String PARAM_GENERATED_TEMPLATE_PREFIX = "net.sf.jasperreports.headertoolbar.";

	private static final List<String> datePatterns = new ArrayList<String>(); 
	private static final List<String> timePatterns = new ArrayList<String>(); 
	private static final Map<String, String> numberPatternsMap = new LinkedHashMap<String, String>();

	private static final String DURATION_PATTERN = "[h]:mm:ss";
	
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
		
		timePatterns.add("hh:mm aaa");
		timePatterns.add("hh:mm:ss aaa");
		timePatterns.add("HH:mm");
		timePatterns.add("HH:mm:ss");

		numberPatternsMap.put("###0;-###0", "-1234");
		numberPatternsMap.put("###0;###0-", "1234-");
		numberPatternsMap.put("###0;(###0)", "(1234)");
		numberPatternsMap.put("###0;(-###0)", "(-1234)");
		numberPatternsMap.put("###0;(###0-)", "(1234-)");
		numberPatternsMap.put(DURATION_PATTERN, "[h]:mm:ss");
	}
	
	private static final String TABLE_UUID = "exporter_first_attempt";

	public String getJsonFragment(JsonExporterContext context, JRGenericPrintElement element)
	{
		boolean templateAlreadyLoaded = false;

		String htmlFragment = null;
		String tableUUID = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_TABLE_UUID);
		ReportContext reportContext = context.getExporterRef().getReportContext();
		if (reportContext != null && tableUUID != null)//FIXMEJIVE
		{
			String columnUuid = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_UUID);
			String columnLabel = (String)element.getParameterValue(HeaderToolbarElement.PARAMETER_COLUMN_LABEL);
			if (columnLabel == null) {
				columnLabel = "";
			}
			int columnIndex = Integer.parseInt(element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_INDEX));
			
			Map<String, Object> contextMap = new HashMap<String, Object>();
			contextMap.put("JRStringUtil", JRStringUtil.class);
			contextMap.put("tableUUID", tableUUID);
			
			JasperReportsContext jrContext = context.getJasperReportsContext();
			
			JasperPrint jasperPrint = context.getExportedReport();
			Locale locale = jasperPrint.getLocaleCode() == null ? Locale.getDefault() : JRDataUtils.getLocale(jasperPrint.getLocaleCode());
			TimeZone timeZone = getFilterTimeZone(jasperPrint, element);
			
			if (log.isDebugEnabled())
			{
				log.debug("column " + columnUuid + " has locale " + locale + ", timezone " + timeZone);
			}
			
			Boolean isClearCache = (Boolean)reportContext.getParameterValue(PARAMETER_CLEAR_CONTEXT_CACHE);

			if (reportContext.getParameterValue(PARAM_GENERATED_TEMPLATE_PREFIX) != null && !(isClearCache != null && isClearCache)) {
				templateAlreadyLoaded = true;
			} else {
				reportContext.setParameterValue(PARAM_GENERATED_TEMPLATE_PREFIX, true);
			}
			
			ColumnFormatting.store(reportContext, tableUUID, columnIndex, locale, timeZone);
			
			/*** begin: FILTER PATTERNS ***/
			// numeric filter pattern
			String numericFilterPattern = HeaderToolbarElementUtils.getNumberPattern(jrContext, locale);

			// date filter pattern
			String dateFilterPattern = HeaderToolbarElementUtils.getDatePattern(jrContext, locale);

			// time filter pattern
			String timeFilterPattern = HeaderToolbarElementUtils.getTimePattern(jrContext, locale);
			/*** end: FILTER PATTERNS ***/

			FilterAction action = new FilterAction();
			action.init(jrContext, reportContext);
			CommandTarget target = action.getCommandTarget(UUID.fromString(tableUUID));

			JasperDesign jasperDesign = null;
			JRDesignDataset dataset = null;
			StandardTable table = null;

			if (target != null) 
			{
				JRIdentifiable identifiable = target.getIdentifiable();
				JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
				table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
				JasperDesignCache cache = JasperDesignCache.getInstance(jrContext, reportContext);
				jasperDesign = cache.getJasperDesign(target.getUri());
				JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getDatasetRun();
				String datasetName = datasetRun.getDatasetName();
				dataset = (JRDesignDataset)jasperDesign.getDatasetMap().get(datasetName);
			}
			
			if (!tableUUID.equals(context.getValue(TABLE_UUID))) 
			{
				Map<String, ColumnInfo> columnNames = getAllColumnNames(element, jrContext, contextMap);
				List<Map<String, Object>> columnGroupsData = getColumnGroupsData(jrContext, jasperDesign, dataset, table, tableUUID, locale, timeZone);
				// column names are normally set on the first column, but check if we got them
				if (!columnNames.isEmpty()) {
					context.setValue(TABLE_UUID, tableUUID);

					// column info
					contextMap.put("allColumnNames", JacksonUtil.getInstance(jrContext).getJsonString(columnNames));
					contextMap.put("allColumnGroupsData", JacksonUtil.getInstance(jrContext).getJsonString(columnGroupsData));
					contextMap.put("tableName", element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_TABLE_NAME));

					// patterns
					contextMap.put("numericPatterns", JacksonUtil.getInstance(jrContext).getJsonString(getNumberPatterns(numberPatternsMap)));
					contextMap.put("datePatterns", JacksonUtil.getInstance(jrContext).getJsonString(getDatePatterns(datePatterns, locale)));
					contextMap.put("timePatterns", JacksonUtil.getInstance(jrContext).getJsonString(getDatePatterns(timePatterns, locale)));

					// operators
					contextMap.put("numericOperators", JacksonUtil.getInstance(jrContext).getJsonString(getTranslatedOperators(jrContext, FilterTypeNumericOperatorsEnum.class.getName(), FilterTypeNumericOperatorsEnum.values(), locale)));
					contextMap.put("dateOperators", JacksonUtil.getInstance(jrContext).getJsonString(getTranslatedOperators(jrContext, FilterTypeDateOperatorsEnum.class.getName(), FilterTypeDateOperatorsEnum.values(), locale)));
					contextMap.put("timeOperators", JacksonUtil.getInstance(jrContext).getJsonString(getTranslatedOperators(jrContext, FilterTypeDateOperatorsEnum.class.getName(), FilterTypeDateOperatorsEnum.values(), locale)));
					contextMap.put("textOperators", JacksonUtil.getInstance(jrContext).getJsonString(getTranslatedOperators(jrContext, FilterTypeTextOperatorsEnum.class.getName(), FilterTypeTextOperatorsEnum.values(), locale)));
					contextMap.put("booleanOperators", JacksonUtil.getInstance(jrContext).getJsonString(getTranslatedOperators(jrContext, FilterTypeBooleanOperatorsEnum.class.getName(), FilterTypeBooleanOperatorsEnum.values(), locale)));

					/*** begin: FILTER PATTERNS ***/
					// numeric filter pattern
					contextMap.put("numericFilterPattern", numericFilterPattern);

					// date filter pattern
					contextMap.put("dateFilterPattern", dateFilterPattern);

					// time filter pattern
					contextMap.put("timeFilterPattern", timeFilterPattern);
					/*** end: FILTER PATTERNS ***/

					/*** begin: CALENDAR PATTERNS ***/
					// date pattern
					contextMap.put("calendarDatePattern", HeaderToolbarElementUtils.getCalendarDatePattern(jrContext, locale));

					// time pattern
					contextMap.put("calendarTimePattern", HeaderToolbarElementUtils.getCalendarTimePattern(jrContext, locale));
					/*** end: CALENDAR PATTERNS ***/

					contextMap.put("exporterFirstAttempt", true);
				}
			}

			contextMap.put("templateAlreadyLoaded", templateAlreadyLoaded);

			Boolean canSort = Boolean.parseBoolean(element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_CAN_SORT));
			Boolean canFilter = Boolean.parseBoolean(element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_CAN_FILTER));

			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				contextMap.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}

			contextMap.put("columnUuid", columnUuid);
			//FIXME conceptually it would be better not to encode for html here 
			//but produce a pure json and encode for html on the client where necessary
			contextMap.put("columnLabel", JRStringUtil.htmlEncode(columnLabel));
			contextMap.put("columnIndex", columnIndex);
			contextMap.put("dataType", FilterTypesEnum.TEXT.getName()); // use Text as default
			contextMap.put("canSort", canSort);
			contextMap.put("canFilter", canFilter);

			contextMap.put("fontExtensionsFontNames", getFontExtensionsFontNames(jrContext));
			contextMap.put("systemFontNames", getSystemFontNames(jrContext));

			setColumnHeaderData(columnLabel, columnIndex, tableUUID, contextMap, jrContext, reportContext, locale);
			EditTextElementData columnValueData = setColumnValueData(columnIndex, tableUUID, contextMap, jrContext, reportContext, locale);

			String columnName = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_NAME);
			String columnComponentName = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_COMPONENT_NAME);
			String columnType = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_TYPE);
			FilterTypesEnum filterType = FilterTypesEnum.getByName(element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_FILTER_TYPE));

			if (columnComponentName == null) {
				columnComponentName = columnName;
			}
			contextMap.put("columnName", columnComponentName);

			if (canFilter)
			{
				FilterData filterData = getFilterData(jrContext, dataset, columnName, columnType, filterType, columnValueData.getFormatPattern(),
						locale, timeZone);

				contextMap.put("dataType", filterType.getName());
				contextMap.put("filterData", JacksonUtil.getInstance(jrContext).getJsonString(filterData));
				contextMap.put("filterTableUuid", filterData.getTableUuid());
			}
			
			if (canSort) {
				SortData sortAscData = new SortData(tableUUID, columnName, columnType, HeaderToolbarElement.SORT_ORDER_ASC);
				SortData sortDescData = new SortData(tableUUID, columnName, columnType, HeaderToolbarElement.SORT_ORDER_DESC);
				String sortField = getCurrentSortField(jrContext, reportContext, tableUUID, columnName, columnType);
				if (sortField != null) 
				{
					String[] sortActionData = HeaderToolbarElementUtils.extractColumnInfo(sortField);
					
					boolean isAscending = HeaderToolbarElement.SORT_ORDER_ASC.equals(sortActionData[2]);
					if (isAscending) {
						sortAscData.setSortOrder(HeaderToolbarElement.SORT_ORDER_NONE);
					} else {
						sortDescData.setSortOrder(HeaderToolbarElement.SORT_ORDER_NONE);
					}
				}
				contextMap.put("sortAscData", JacksonUtil.getInstance(jrContext).getJsonString(sortAscData));
				contextMap.put("sortDescData", JacksonUtil.getInstance(jrContext).getJsonString(sortDescData));
			}
			
			List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
			Column column = (Column)tableColumns.get(columnIndex);
			
			JRDesignTextField detailTextField = TableUtil.getCellElement(JRDesignTextField.class, column.getDetailCell(), true);
			if (detailTextField != null)
			{
				ConditionalFormattingData detailCfd = getConditionalFormattingData(element, jrContext, dataset, detailTextField, null,
						locale, timeZone);
				if (detailCfd != null)
				{
					contextMap.put("conditionalFormattingData", JacksonUtil.getInstance(jrContext).getJsonString(detailCfd));
				}
			}
			
			htmlFragment = VelocityUtil.processTemplate(HeaderToolbarElementJsonHandler.HEADER_TOOLBAR_ELEMENT_JSON_TEMPLATE, contextMap);
		}
		
		return htmlFragment;
	}

	protected TimeZone getFilterTimeZone(JasperPrint jasperPrint, JRGenericPrintElement element)
	{
		String formatTimeZone = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_FORMAT_TIME_ZONE);
		TimeZone timeZone;
		if (formatTimeZone != null)
		{
			timeZone = JRDataUtils.getTimeZone(formatTimeZone);
		}
		else if (jasperPrint.getTimeZoneId() != null)
		{
			timeZone = JRDataUtils.getTimeZone(jasperPrint.getTimeZoneId());
		}
		else
		{
			timeZone = TimeZone.getDefault();
		}
		return timeZone;
	}
	
	private List<HashMap<String, String>> getDatePatterns(List<String> datePatterns, Locale locale) {
		List<HashMap<String, String>> formatPatterns = new ArrayList<HashMap<String, String>>();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", locale);
		Date today = new Date();
		HashMap<String, String> keys;

		for(String datePattern: datePatterns) {
			keys = new HashMap<String, String>();
			sdf.applyPattern(datePattern);
			keys.put("key", datePattern);
			keys.put("val", sdf.format(today));
			formatPatterns.add(keys);
		}

		return formatPatterns;
	}
	private List<HashMap<String, String>> getNumberPatterns(Map<String, String> numberPatternsMap) {
		List<HashMap<String, String>> formatPatterns = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> keys;

		for(Map.Entry<String, String> entry: numberPatternsMap.entrySet()) {
			keys = new HashMap<String, String>();
			keys.put("key", entry.getKey());
			keys.put("val", entry.getValue());
			formatPatterns.add(keys);
		}

		return formatPatterns;
	}

	private String getActionBaseUrl(JsonExporterContext context) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType(ReportInteractionHyperlinkProducer.HYPERLINK_TYPE_REPORT_INTERACTION);
//		return context.getHyperlinkURL(hyperlink);
		return "FIXME HeaderToolbarElementJsonHandler.getActionBaseUrl";
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
	
	private List<LinkedHashMap<String, String>> getTranslatedOperators(
		JasperReportsContext jasperReportsContext, 
		String bundleName, 
		NamedEnum[] operators, 
		Locale locale
		) //FIXMEJIVE make utility method for translating enums
	{
		List<LinkedHashMap<String, String>> result = new ArrayList<LinkedHashMap<String, String>>();
		MessageProvider messageProvider = MessageUtil.getInstance(jasperReportsContext).getMessageProvider(bundleName);
		LinkedHashMap<String, String> keys;
		
		for (NamedEnum operator: operators) 
		{
			keys = new LinkedHashMap<String, String>();
			String key = bundleName + "." + ((Enum<?>)operator).name();
			keys.put("key", ((Enum<?>)operator).name());
			keys.put("val", messageProvider.getMessage(key, null, locale));
			result.add(keys);
		}
		
		return result;
	}
	
	private static FilterData getFilterData(
		JasperReportsContext jasperReportsContext,
		JRDesignDataset dataset,
		String columnName,
		String columnType,
		FilterTypesEnum filterType,
		String columnValuePattern,
		Locale locale,
		TimeZone timeZone
		) 
	{
		// get existing filter as JSON string
		String serializedFilters = "[]";
		JRPropertiesMap propertiesMap = dataset.getPropertiesMap();
		if (propertiesMap.getProperty(FilterCommand.DATASET_FILTER_PROPERTY) != null) {
			serializedFilters = propertiesMap.getProperty(FilterCommand.DATASET_FILTER_PROPERTY);
		}
		
		List<DatasetFilter> filters = new ArrayList<DatasetFilter>();
		
		List<? extends DatasetFilter> existingFilters = JacksonUtil.getInstance(jasperReportsContext).loadList(serializedFilters, FieldFilter.class);
		if (existingFilters.size() > 0) {
			for (DatasetFilter filter: existingFilters) {
				if (((FieldFilter)filter).getField().equals(columnName)) {
					filters.add(filter);
					break;
				}
			}
		}
		
		FilterData filterData = new FilterData();
		filterData.setFieldName(columnName);
		filterData.setFilterType(filterType.getName());
		filterData.setIsField(SortFieldTypeEnum.FIELD.equals(SortFieldTypeEnum.getByName(columnType)));

		if (filters.size() > 0) 
		{
			FieldFilter ff = (FieldFilter)filters.get(0);
			filterData.setFieldValueStart(ff.getFilterValueStart());
			filterData.setFieldValueEnd(ff.getFilterValueEnd());
			filterData.setFilterTypeOperator(ff.getFilterTypeOperator());
			filterData.setFilterType(ff.getFilterType());
			filterData.setFilterPattern(ff.getFilterPattern());
			filterData.setLocaleCode(ff.getLocaleCode());
			filterData.setTimeZoneId(ff.getTimeZoneId());
		}

		if (filterType.getName().equals(filterData.getFilterType()))
		{
			String filterPattern = HeaderToolbarElementUtils.getFilterPattern(jasperReportsContext, locale, filterType);

			if (FilterTypesEnum.NUMERIC.equals(filterType) && DURATION_PATTERN.equals(columnValuePattern)) {
				filterPattern = DURATION_PATTERN;
			}

			HeaderToolbarElementUtils.updateFilterData(
				filterData,
				filterPattern,
				locale,
				timeZone
				);
		}
		else
		{
			//FIXMEJIVE should we raise error?
		}
		
		return filterData;
	}

	private void setColumnHeaderData(String sortColumnLabel, Integer columnIndex, String tableUuid, Map<String, Object> contextMap,
			JasperReportsContext jasperReportsContext, ReportContext reportContext, Locale locale) {
		FilterAction action = new FilterAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(tableUuid));

		if (target != null){
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();

			List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);

			if (columnIndex != null) {
				StandardColumn column = (StandardColumn) tableColumns.get(columnIndex);

				JRDesignTextElement textElement = TableUtil.getCellElement(JRDesignTextElement.class, column.getColumnHeader(), true);

				if (textElement != null) {
					EditTextElementData textElementData = new EditTextElementData();

					textElementData.setHeadingName(JRStringUtil.htmlEncode(sortColumnLabel));
					textElementData.setColumnIndex(columnIndex);
					HeaderToolbarElementUtils.copyTextElementStyle(textElementData, textElement, locale);

					contextMap.put("colHeaderData", JacksonUtil.getInstance(jasperReportsContext).getJsonString(textElementData));
				}
			}
		}
	}
	
	private EditTextElementData setColumnValueData(Integer columnIndex, String tableUuid, Map<String, Object> contextMap,
			JasperReportsContext jasperReportsContext, ReportContext reportContext, Locale locale) {
		FilterAction action = new FilterAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(tableUuid));
		EditTextElementData textElementData = new EditTextElementData();

		if (target != null){
			JRIdentifiable identifiable = target.getIdentifiable();
			JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
			StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();

			List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);

			if (columnIndex != null) {
				StandardColumn column = (StandardColumn) tableColumns.get(columnIndex);

				JRDesignTextField textElement = TableUtil.getCellElement(JRDesignTextField.class, column.getDetailCell(), true);

				if (textElement != null) {
					textElementData.setColumnIndex(columnIndex);
					HeaderToolbarElementUtils.copyTextElementStyle(textElementData, textElement, locale);
				}
			}
		}
		contextMap.put("colValueData", JacksonUtil.getInstance(jasperReportsContext).getJsonString(textElementData));

		return textElementData;
	}

	public static class ColumnInfo {
		private String index;
		private String label;
		private String uuid;
		private boolean visible;
		private boolean interactive;
		
		private ColumnInfo(String index, String label, String uuid, boolean visible, boolean interactive) {
			this.index = index;
			this.label = label;
			this.uuid = uuid;
			this.visible = visible;
			this.interactive = interactive;
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

		public boolean getInteractive() {
			return interactive;
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
				columnNames.put(tokens[0], new ColumnInfo(tokens[0], JRStringUtil.htmlEncode(columnName), tokens[1], false, Boolean.valueOf(tokens[2])));
			}
		}
		return columnNames;
	}

	public static class GroupInfo {
		public static final String TYPE_GROUP_HEADING = "groupheading";
		public static final String TYPE_GROUP_SUBTOTAL = "groupsubtotal";
		public static final String TYPE_TABLE_TOTAL = "tabletotal";

		private String name;
		private String type;
		private List<Integer> forColumns;

		public GroupInfo(String name, String type) {
			this.name = name;
			this.type = type;
			this.forColumns = new ArrayList<Integer>();
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public void addForColumn(Integer index) {
			forColumns.add(index);
		}

		public List<Integer> getForColumns() {
			return this.forColumns;
		}
	}

	private boolean setTextElements(List<GroupCell> groupCells, Map<JRDesignTextElement, GroupInfo> groups, String groupType, Integer columnIndex) {
		boolean result = false;
		if (groupCells != null) {
			for (GroupCell gc: groupCells) {
				JRDesignTextElement textElement = TableUtil.getCellElement(JRDesignTextElement.class, gc.getCell(), false);

				if (textElement != null) {
					result = true;
					if (groups.containsKey(textElement)) {
						groups.get(textElement).addForColumn(columnIndex);
					}
					else {
						GroupInfo gi = new GroupInfo(gc.getGroupName() != null ? gc.getGroupName() : groupType + "_" + columnIndex, groupType);
						gi.addForColumn(columnIndex);
						groups.put(textElement, gi);
					}
				}
			}
		}
		return result;
	}

	private List<Map<String, Object>> getColumnGroupsData(JasperReportsContext jasperReportsContext, JasperDesign jasperDesign,
			JRDesignDataset dataset, StandardTable table, String tableUuid, Locale locale, TimeZone timeZone)
	{
		List<BaseColumn> allColumns = TableUtil.getAllColumns(table);

		int i = 0;
		Map<JRDesignTextElement, GroupInfo> groups = new HashMap<JRDesignTextElement, GroupInfo>();
		boolean found;

		// build the groups map
		for (BaseColumn bc: allColumns) {
			List<ColumnGroup> colGroups = TableUtil.getHierarchicalColumnGroupsForColumn(bc, table.getColumns(), table);

			// group headers
			found = setTextElements(bc.getGroupHeaders(), groups, GroupInfo.TYPE_GROUP_HEADING, i);
			if (!found) {
				for (ColumnGroup cg: colGroups) {
					if (cg.getGroupHeaders() == null) {
						continue;
					}
					found = setTextElements(cg.getGroupHeaders(), groups, GroupInfo.TYPE_GROUP_HEADING, i);
					if (found) break;
				}
			}

			// group footers
			found = setTextElements(bc.getGroupFooters(), groups, GroupInfo.TYPE_GROUP_SUBTOTAL, i);
			if (!found) {
				for (ColumnGroup cg: colGroups) {
					if (cg.getGroupFooters() == null) {
						continue;
					}
					found = setTextElements(cg.getGroupFooters(), groups, GroupInfo.TYPE_GROUP_SUBTOTAL, i);
					if (found) break;
				}
			}

			// table footers
			found = false;
			if (bc.getTableFooter() != null) {
				JRDesignTextElement textElement = TableUtil.getCellElement(JRDesignTextElement.class, bc.getTableFooter(), false);

				if (textElement != null) {
					found = true;
					if (groups.containsKey(textElement)) {
						groups.get(textElement).addForColumn(i);
					}
					else {
						GroupInfo gi = new GroupInfo("Tabletotal" + "_" + i, GroupInfo.TYPE_TABLE_TOTAL);
						gi.addForColumn(i);
						groups.put(textElement, gi);
					}
				}
			}
			if (!found) {
				for (ColumnGroup cg: colGroups) {
					if (cg.getTableFooter() == null) {
						continue;
					}
					JRDesignTextElement textElement = TableUtil.getCellElement(JRDesignTextElement.class, cg.getTableFooter(), false);

					if (textElement != null) {
						found = true;
						if (groups.containsKey(textElement)) {
							groups.get(textElement).addForColumn(i);
						}
						else {
							GroupInfo gi = new GroupInfo("Tabletotal" + "_" + i, GroupInfo.TYPE_TABLE_TOTAL);
							gi.addForColumn(i);
							groups.put(textElement, gi);
						}
					}

					if (found) break;
				}
			}
			i++;
		}

		List<Map<String, Object>> groupsData = new ArrayList<Map<String, Object>>();

		// populate groupsData
		i = 0;
		for (Map.Entry<JRDesignTextElement, GroupInfo> entry: groups.entrySet()) 
		{
			JRDesignTextElement textElement = entry.getKey();
			GroupInfo groupInfo = entry.getValue();

			EditTextElementData textElementData;
			textElementData = new EditTextElementData();
			textElementData.setGroupName(groupInfo.getName());
			HeaderToolbarElementUtils.copyTextElementStyle(textElementData, textElement, locale);

			Map<String, Object> groupData = new HashMap<String, Object>();
			groupData.put("groupType", groupInfo.getType());
			groupData.put("id", groupInfo.getType() + "_" + i);
			groupData.put("groupData", textElementData);
			groupData.put("groupName", groupInfo.getName());
			groupData.put("forColumns", groupInfo.getForColumns());

			JRDesignTextField textField = textElement instanceof JRDesignTextField ? (JRDesignTextField)textElement : null;
			ConditionalFormattingData cfData = 
				textField == null 
					? null 
					: getConditionalFormattingData(
						null,
						jasperReportsContext, 
						dataset, 
						textField,
						groupInfo.getName(),
						locale,
						timeZone
						);
			
			if (cfData != null) 
			{
				groupData.put("dataType", cfData.getConditionType());
				groupData.put("conditionalFormattingData", cfData);
			}
			else
			{
				groupData.put("dataType", FilterTypesEnum.TEXT.getName());
			}

			groupsData.add(groupData);

			i++;
		}

		return groupsData;
	}

	private static ConditionalFormattingData getConditionalFormattingData(
		JRGenericPrintElement element,
		JasperReportsContext jasperReportsContext, 
		JRDesignDataset dataset,
		JRDesignTextField textField,
		String groupName,
		Locale locale,
		TimeZone timeZone
		) 
	{
		FilterTypesEnum conditionType = null;
		String conditionTypeProp;

		// only for the detail values the element will not be null
		if (element != null) {
			conditionTypeProp = element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_FILTER_TYPE);

			if (element.getPropertiesMap().containsProperty(HeaderToolbarElement.PROPERTY_COLUMN_FIELD)) {
				textField.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_FIELD, element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_FIELD));
			} else if (element.getPropertiesMap().containsProperty(HeaderToolbarElement.PROPERTY_COLUMN_VARIABLE)) {
				textField.getPropertiesMap().setProperty(HeaderToolbarElement.PROPERTY_COLUMN_VARIABLE, element.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_COLUMN_VARIABLE));
			}

		} else {
			conditionTypeProp = textField.getPropertiesMap().getProperty(HeaderToolbarElement.PROPERTY_CONDTION_TYPE);
		}


		if (conditionTypeProp == null)
		{
			JRExpression expression = textField.getExpression();
			if (expression != null)
			{
				JRExpressionChunk[] chunks = expression.getChunks();
				if (chunks != null && chunks.length == 1)
				{
					JRExpressionChunk expressionChunk = expression.getChunks()[0];
					String fieldOrVariableName = expressionChunk.getText();

					switch (expressionChunk.getType()) {
						case JRExpressionChunk.TYPE_FIELD:
							JRField field = HeaderToolbarElementUtils.getField(fieldOrVariableName, dataset);
							conditionType = HeaderToolbarElementUtils.getFilterType(field.getValueClass());
							break;

						case JRExpressionChunk.TYPE_VARIABLE:
							JRVariable variable = HeaderToolbarElementUtils.getVariable(fieldOrVariableName, dataset);
							conditionType = HeaderToolbarElementUtils.getFilterType(variable.getValueClass());
							break;

						case JRExpressionChunk.TYPE_TEXT:
						default:
					}
				}
			}
		}
		else
		{
			conditionType = FilterTypesEnum.getByName(conditionTypeProp);
		}

		ConditionalFormattingData cfd = null; 
		if (conditionType != null)
		{
			cfd = 
				HeaderToolbarElementUtils.getConditionalFormattingData(
					textField, 
					jasperReportsContext
					);
			if (cfd == null)
			{
				cfd = new ConditionalFormattingData();
				if (groupName != null)
				{
					cfd.setGroupName(groupName);
				}
				cfd.setConditionType(conditionType.getName());
			}
			
			if (conditionType.getName().equals(cfd.getConditionType()))
			{
				String conditionPattern = HeaderToolbarElementUtils.getFilterPattern(jasperReportsContext, locale, conditionType);

				if (FilterTypesEnum.NUMERIC.equals(conditionType) && DURATION_PATTERN.equals(textField.getPattern())) {
					conditionPattern = DURATION_PATTERN;
				}

				HeaderToolbarElementUtils.updateConditionalFormattingData(
					cfd,
					conditionPattern,
					locale,
					timeZone
					);
			}
			else
			{
				//FIXMEJIVE should we raise error?
			}
		}

		return cfd;
	}

	private Set<String> getFontExtensionsFontNames(JasperReportsContext jasperReportsContext) {
		Set<String> classes = new TreeSet<String>(); 

		Collection<String> extensionFonts = FontUtil.getInstance(jasperReportsContext).getFontFamilyNames();
		for (Iterator<String> it = extensionFonts.iterator(); it.hasNext();) {
			String fname = it.next();
			classes.add(fname);
		}

		return classes;
	} 

	private Set<String> getSystemFontNames(JasperReportsContext jasperReportsContext) {
		Set<String> fontExtensionsFontNames = getFontExtensionsFontNames(jasperReportsContext);
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

}
