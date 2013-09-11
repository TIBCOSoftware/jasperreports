/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingCommand;
import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingData;
import net.sf.jasperreports.components.headertoolbar.actions.EditTextElementData;
import net.sf.jasperreports.components.headertoolbar.actions.FormatCondition;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.web.util.JacksonUtil;



/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HeaderToolbarElementUtils 
{
	private static final String DEFAULT_PATTERNS_BUNDLE = "net.sf.jasperreports.components.messages";
	private static final String DEFAULT_DATE_PATTERN_KEY = "net.sf.jasperreports.components.date.pattern";
	private static final String DEFAULT_TIME_PATTERN_KEY = "net.sf.jasperreports.components.time.pattern";
	private static final String DEFAULT_NUMBER_PATTERN_KEY = "net.sf.jasperreports.components.number.pattern";
	private static final String DATE_PATTERN_BUNDLE = DEFAULT_DATE_PATTERN_KEY + ".bundle";
	private static final String DATE_PATTERN_KEY = DEFAULT_DATE_PATTERN_KEY + ".key";
	private static final String TIME_PATTERN_BUNDLE = DEFAULT_TIME_PATTERN_KEY + ".bundle";
	private static final String TIME_PATTERN_KEY = DEFAULT_TIME_PATTERN_KEY + ".key";
	private static final String NUMBER_PATTERN_BUNDLE = DEFAULT_NUMBER_PATTERN_KEY + ".bundle";
	private static final String NUMBER_PATTERN_KEY = DEFAULT_NUMBER_PATTERN_KEY + ".key";
	private static final String DEFAULT_CALENDAR_DATE_PATTERN_KEY = "net.sf.jasperreports.components.calendar.date.pattern";
	private static final String DEFAULT_CALENDAR_DATE_TIME_PATTERN_KEY = "net.sf.jasperreports.components.calendar.date.time.pattern";
	private static final String CALENDAR_DATE_PATTERN_BUNDLE = DEFAULT_CALENDAR_DATE_PATTERN_KEY + ".bundle";
	private static final String CALENDAR_DATE_PATTERN_KEY = DEFAULT_CALENDAR_DATE_PATTERN_KEY + ".key";
	private static final String CALENDAR_DATE_TIME_PATTERN_KEY = DEFAULT_CALENDAR_DATE_TIME_PATTERN_KEY + ".key";
	
	private static Map<String, SortOrderEnum> sortOrderMapping = new HashMap<String, SortOrderEnum>();
	
	static {
		sortOrderMapping.put(HeaderToolbarElement.SORT_ORDER_ASC, SortOrderEnum.ASCENDING);
		sortOrderMapping.put(HeaderToolbarElement.SORT_ORDER_DESC, SortOrderEnum.DESCENDING);
	}
	
	public static String[] extractColumnInfo(String sortColumn) {
		return sortColumn.split(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR);
	}
	
	public static String packSortColumnInfo(String columnName, String columnType, String sortOrder) {
		StringBuffer sb = new StringBuffer();
		sb.append(columnName)
			.append(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR)
			.append(columnType)
			.append(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR)
			.append(sortOrder);
		return sb.toString();
	}
	
	public static boolean isValidSortData(String sortData) {
		return sortData != null 
				&& sortData.indexOf(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR) >= 0 
				&& sortData.split(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR).length > 1;
	}
	
	public static SortOrderEnum getSortOrder(String sortOrder) {
		return sortOrderMapping.get(sortOrder);
	}

	public static FilterTypesEnum getFilterType(Class<?> clazz) {
		FilterTypesEnum result = null;
		if (Number.class.isAssignableFrom(clazz)) 
		{
			result = FilterTypesEnum.NUMERIC;
		}
		else if (String.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.TEXT;
		}
		else if (Time.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.TIME;
		}
		else if (Date.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.DATE;
		}
		else if (Boolean.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.BOOLEAN;
		}
		return result;
	}

	public static void copyOwnTextElementStyle(EditTextElementData textElementData, JRDesignTextElement textElement) {
		textElementData.setFontName(textElement.getOwnFontName());
		textElementData.setFontSize(textElement.getOwnFontSize() != null ? String.valueOf(textElement.getOwnFontSize()) : null);
		textElementData.setFontBold(textElement.isOwnBold());
		textElementData.setFontItalic(textElement.isOwnItalic());
		textElementData.setFontUnderline(textElement.isOwnUnderline());
		textElementData.setFontColor(textElement.getOwnForecolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnForecolor()) : null);
		textElementData.setFontBackColor(textElement.getOwnBackcolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnBackcolor()) : null);
		textElementData.setFontHAlign(textElement.getOwnHorizontalAlignmentValue() != null ? textElement.getOwnHorizontalAlignmentValue().getName() : null);
		textElementData.setMode(textElement.getOwnModeValue() != null ? textElement.getOwnModeValue().getName() : null);
		
		if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
			textElementData.setFormatPattern(((JRDesignTextField) textElement).getOwnPattern());
		}
	}

	public static void copyTextElementStyle(EditTextElementData textElementData, JRDesignTextElement textElement) {
		textElementData.setFontName(JRStringUtil.htmlEncode(textElement.getFontName()));
		textElementData.setFontSize(String.valueOf(textElement.getFontSize()));
		textElementData.setFontBold(textElement.isBold());
		textElementData.setFontItalic(textElement.isItalic());
		textElementData.setFontUnderline(textElement.isUnderline());
		textElementData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
		textElementData.setFontBackColor(JRColorUtil.getColorHexa(textElement.getBackcolor()));
		textElementData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
		textElementData.setMode(textElement.getModeValue().getName());
		
		if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
			textElementData.setFormatPattern(JRStringUtil.htmlEncode(((JRDesignTextField) textElement).getPattern()));
		}
	}

	/**
	 * 
	 */
	public static ConditionalFormattingData getConditionalFormattingData(JRDesignTextElement textElement, JasperReportsContext jasperReportsContext) 
	{
		ConditionalFormattingData result = null;
		if (textElement != null) 
		{
			JRPropertiesMap propertiesMap = textElement.getPropertiesMap();
			if (
				propertiesMap.containsProperty(ConditionalFormattingCommand.COLUMN_CONDITIONAL_FORMATTING_PROPERTY) 
				&& propertiesMap.getProperty(ConditionalFormattingCommand.COLUMN_CONDITIONAL_FORMATTING_PROPERTY) != null
				) 
			{
				result = 
					JacksonUtil.getInstance(jasperReportsContext).loadObject(
						propertiesMap.getProperty(ConditionalFormattingCommand.COLUMN_CONDITIONAL_FORMATTING_PROPERTY), 
						ConditionalFormattingData.class
						);

				// html encode the conditions for text based columns
				if (FilterTypesEnum.TEXT.getName().equals(result.getConditionType())) 
				{
					for (FormatCondition fc: result.getConditions()) 
					{
						fc.setConditionStart(JRStringUtil.htmlEncode(fc.getConditionStart()));
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 */
	public static JRField getField(String name, JRDesignDataset dataSet) 
	{
		JRField found = null;
		for (JRField field : dataSet.getFields())
		{
			if (name.equals(field.getName()))
			{
				found = field;
				break;
			}
		}
		return found;
	}
	
	/**
	 * 
	 */
	public static JRVariable getVariable(String name, JRDesignDataset dataSet) 
	{
		JRVariable found = null;
		for (JRVariable var : dataSet.getVariables())
		{
			if (name.equals(var.getName()))
			{
				found = var;
				break;
			}
		}
		return found;
	}

	/**
	 * 
	 */
	public static String getNumberPattern(JasperReportsContext jrContext, Locale locale)
	{
		String numberPatternBundleName = JRPropertiesUtil.getInstance(jrContext).getProperty(NUMBER_PATTERN_BUNDLE);
		if (numberPatternBundleName == null)
		{
			numberPatternBundleName = DEFAULT_PATTERNS_BUNDLE;
		}
		String numberPatternKey = JRPropertiesUtil.getInstance(jrContext).getProperty(NUMBER_PATTERN_KEY);
		if (numberPatternKey == null)
		{
			numberPatternKey = DEFAULT_NUMBER_PATTERN_KEY;
		}
		return getBundleMessage(numberPatternKey, jrContext, numberPatternBundleName, locale);
	}

	/**
	 * 
	 */
	public static String getDatePattern(JasperReportsContext jrContext, Locale locale)
	{
		String datePatternBundleName = JRPropertiesUtil.getInstance(jrContext).getProperty(DATE_PATTERN_BUNDLE);
		if (datePatternBundleName == null)
		{
			datePatternBundleName = DEFAULT_PATTERNS_BUNDLE;
		}
		String datePatternKey = JRPropertiesUtil.getInstance(jrContext).getProperty(DATE_PATTERN_KEY);
		if (datePatternKey == null)
		{
			datePatternKey = DEFAULT_DATE_PATTERN_KEY;
		}
		return getBundleMessage(datePatternKey, jrContext, datePatternBundleName, locale);
	}

	/**
	 * 
	 */
	public static String getTimePattern(JasperReportsContext jrContext, Locale locale)
	{
		String timePatternBundleName = JRPropertiesUtil.getInstance(jrContext).getProperty(TIME_PATTERN_BUNDLE);
		if (timePatternBundleName == null)
		{
			timePatternBundleName = DEFAULT_PATTERNS_BUNDLE;
		}

		String timePatternKey = JRPropertiesUtil.getInstance(jrContext).getProperty(TIME_PATTERN_KEY);
		if (timePatternKey == null)
		{
			timePatternKey = DEFAULT_TIME_PATTERN_KEY;
		}
		return getBundleMessage(timePatternKey, jrContext, timePatternBundleName, locale);
	}

	/**
	 * 
	 */
	public static String getCalendarDatePattern(JasperReportsContext jrContext, Locale locale)
	{
		String calendarDatePatternBundleName = JRPropertiesUtil.getInstance(jrContext).getProperty(CALENDAR_DATE_PATTERN_BUNDLE);
		if (calendarDatePatternBundleName == null)
		{
			calendarDatePatternBundleName = DEFAULT_PATTERNS_BUNDLE;
		}
		String calendarDatePatternKey = JRPropertiesUtil.getInstance(jrContext).getProperty(CALENDAR_DATE_PATTERN_KEY);
		if (calendarDatePatternKey == null)
		{
			calendarDatePatternKey = DEFAULT_CALENDAR_DATE_PATTERN_KEY;
		}
		return getBundleMessage(calendarDatePatternKey, jrContext, calendarDatePatternBundleName, locale);
	}

	/**
	 * 
	 */
	public static String getCalendarTimePattern(JasperReportsContext jrContext, Locale locale)
	{
		String timePatternBundleName = JRPropertiesUtil.getInstance(jrContext).getProperty(TIME_PATTERN_BUNDLE);
		if (timePatternBundleName == null)
		{
			timePatternBundleName = DEFAULT_PATTERNS_BUNDLE;
		}
		String calendarTimePatternKey = JRPropertiesUtil.getInstance(jrContext).getProperty(CALENDAR_DATE_TIME_PATTERN_KEY);
		if (calendarTimePatternKey == null)
		{
			calendarTimePatternKey = DEFAULT_CALENDAR_DATE_TIME_PATTERN_KEY;
		}
		return getBundleMessage(calendarTimePatternKey, jrContext, timePatternBundleName, locale);
	}
	
	/**
	 * 
	 */
	public static String getBundleMessage(String key, JasperReportsContext jasperReportsContext, String bundleName, Locale locale) 
	{
		MessageProvider messageProvider = MessageUtil.getInstance(jasperReportsContext).getMessageProvider(bundleName);
		return messageProvider.getMessage(key, null, locale); 
	}
}