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
package net.sf.jasperreports.components.headertoolbar;

import java.sql.Time;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingCommand;
import net.sf.jasperreports.components.headertoolbar.actions.ConditionalFormattingData;
import net.sf.jasperreports.components.headertoolbar.actions.EditTextElementData;
import net.sf.jasperreports.components.headertoolbar.actions.FormatCondition;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.sort.actions.FilterData;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.DefaultFormatFactory;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.web.util.JacksonUtil;



/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
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

	public static void copyOwnTextElementStyle(EditTextElementData textElementData, JRDesignTextElement textElement, Locale locale) {
		textElementData.setFontName(textElement.getOwnFontName());
		textElementData.setFontSize(textElement.getOwnFontsize() != null ? NumberFormat.getNumberInstance(locale).format(textElement.getOwnFontsize()) : null);
		textElementData.setFontBold(textElement.isOwnBold());
		textElementData.setFontItalic(textElement.isOwnItalic());
		textElementData.setFontUnderline(textElement.isOwnUnderline());
		textElementData.setFontColor(textElement.getOwnForecolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnForecolor()) : null);
		textElementData.setFontBackColor(textElement.getOwnBackcolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnBackcolor()) : null);
		textElementData.setFontHAlign(textElement.getOwnHorizontalTextAlign() != null ? textElement.getOwnHorizontalTextAlign().getName() : null);
		textElementData.setMode(textElement.getOwnModeValue() != null ? textElement.getOwnModeValue().getName() : null);
		
		if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
			textElementData.setFormatPattern(((JRDesignTextField) textElement).getOwnPattern());
		}
	}

	public static void copyTextElementStyle(EditTextElementData textElementData, JRDesignTextElement textElement, Locale locale) {
		textElementData.setFontName(JRStringUtil.htmlEncode(textElement.getFontName()));
		textElementData.setFontSize(NumberFormat.getNumberInstance(locale).format(textElement.getFontsize()));
		textElementData.setFontBold(textElement.isBold());
		textElementData.setFontItalic(textElement.isItalic());
		textElementData.setFontUnderline(textElement.isUnderline());
		textElementData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
		textElementData.setFontBackColor(JRColorUtil.getColorHexa(textElement.getBackcolor()));
		textElementData.setFontHAlign(textElement.getHorizontalTextAlign().getName());
		textElementData.setMode(textElement.getModeValue().getName());
		
		if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
			textElementData.setFormatPattern(JRStringUtil.htmlEncode(((JRDesignTextField) textElement).getPattern()));
		}
	}


	/**
	 * 
	 */
	public static void updateFilterData(
		FilterData filterData,
		String filterPattern,
		Locale locale,
		TimeZone timeZone
		) 
	{
		switch (FilterTypesEnum.getByName(filterData.getFilterType())) 
		{
			case TEXT :
			{
				// html encode the conditions for text based columns
				filterData.setFieldValueStart(JRStringUtil.htmlEncode(filterData.getFieldValueStart()));
				break;
			}
			case DATE :
			case TIME :
			{
				FormatFactory formatFactory = new DefaultFormatFactory();
				DateFormat newDf = 
					formatFactory.createDateFormat(
						filterPattern, 
						locale, 
						timeZone 
						);
					newDf.setLenient(false);
				DateFormat oldDf = 
					formatFactory.createDateFormat(
						filterData.getFilterPattern(), 
						filterData.getLocaleCode() == null ? locale : JRDataUtils.getLocale(filterData.getLocaleCode()), 
								filterData.getTimeZoneId() == null ? timeZone : JRDataUtils.getTimeZone(filterData.getTimeZoneId()) 
						);
				oldDf.setLenient(false);
				
				try
				{
					if (filterData.getFieldValueStart() != null && !filterData.getFieldValueStart().trim().isEmpty())
					{
						filterData.setFieldValueStart(
							newDf.format(oldDf.parse(filterData.getFieldValueStart()))
							);
					}
					if (filterData.getFieldValueEnd() != null && !filterData.getFieldValueEnd().trim().isEmpty())
					{
						filterData.setFieldValueEnd(
							newDf.format(oldDf.parse(filterData.getFieldValueEnd()))
							);
					}
				}
				catch (ParseException e)
				{
					throw new JRRuntimeException(e);
				}
				break;
			}
			case NUMERIC :
			{
				FormatFactory formatFactory = new DefaultFormatFactory();
				NumberFormat newNf = 
					formatFactory.createNumberFormat(
						filterPattern, 
						locale 
						);
				NumberFormat oldNf = 
					formatFactory.createNumberFormat(
						filterData.getFilterPattern(), 
						filterData.getLocaleCode() == null ? locale : JRDataUtils.getLocale(filterData.getLocaleCode()) 
						);
				
				try
				{
					if (filterData.getFieldValueStart() != null && !filterData.getFieldValueStart().trim().isEmpty())
					{
						filterData.setFieldValueStart(
							newNf.format(oldNf.parse(filterData.getFieldValueStart()))
							);
					}
					if (filterData.getFieldValueEnd() != null && !filterData.getFieldValueEnd().trim().isEmpty())
					{
						filterData.setFieldValueEnd(
							newNf.format(oldNf.parse(filterData.getFieldValueEnd()))
							);
					}
				}
				catch (ParseException e)
				{
					throw new JRRuntimeException(e);
				}
				break;
			}
		}
		
		filterData.setFilterPattern(filterPattern);
		filterData.setLocaleCode(JRDataUtils.getLocaleCode(locale));
		filterData.setTimeZoneId(JRDataUtils.getTimeZoneId(timeZone));//FIXME only set this for date/time?
	}
	
	/**
	 * 
	 */
	public static ConditionalFormattingData getConditionalFormattingData(
		JRDesignTextElement textElement, 
		JasperReportsContext jasperReportsContext
		) 
	{
		ConditionalFormattingData cfd = null;
		if (textElement != null) 
		{
			JRPropertiesMap propertiesMap = textElement.getPropertiesMap();
			if (
				propertiesMap.containsProperty(ConditionalFormattingCommand.COLUMN_CONDITIONAL_FORMATTING_PROPERTY) 
				&& propertiesMap.getProperty(ConditionalFormattingCommand.COLUMN_CONDITIONAL_FORMATTING_PROPERTY) != null
				) 
			{
				cfd = 
					JacksonUtil.getInstance(jasperReportsContext).loadObject(
						propertiesMap.getProperty(ConditionalFormattingCommand.COLUMN_CONDITIONAL_FORMATTING_PROPERTY), 
						ConditionalFormattingData.class
						);
			}
		}
		return cfd;
	}

	/**
	 * 
	 */
	public static void updateConditionalFormattingData(
		ConditionalFormattingData cfd,
		String conditionPattern,
		Locale locale,
		TimeZone timeZone
		) 
	{
		switch (FilterTypesEnum.getByName(cfd.getConditionType())) 
		{
			case TEXT :
			{
				// html encode the conditions for text based columns
				for (FormatCondition fc: cfd.getConditions()) 
				{
					fc.setConditionStart(JRStringUtil.htmlEncode(fc.getConditionStart()));
				}
				break;
			}
			case DATE :
			case TIME :
			{
				FormatFactory formatFactory = new DefaultFormatFactory();
				DateFormat newDf = 
					formatFactory.createDateFormat(
						conditionPattern, 
						locale, 
						timeZone 
						);
					newDf.setLenient(false);
				DateFormat oldDf = 
					formatFactory.createDateFormat(
						cfd.getConditionPattern(), 
						cfd.getLocaleCode() == null ? locale : JRDataUtils.getLocale(cfd.getLocaleCode()), 
						cfd.getTimeZoneId() == null ? timeZone : JRDataUtils.getTimeZone(cfd.getTimeZoneId()) 
						);
				oldDf.setLenient(false);
				
				try
				{
					for (FormatCondition fc: cfd.getConditions()) 
					{
						if (fc.getConditionStart() != null && !fc.getConditionStart().trim().isEmpty())
						{
							fc.setConditionStart(
								newDf.format(oldDf.parse(fc.getConditionStart()))
								);
						}
						if (fc.getConditionEnd() != null && !fc.getConditionEnd().trim().isEmpty())
						{
							fc.setConditionEnd(
								newDf.format(oldDf.parse(fc.getConditionEnd()))
								);
						}
					}
				}
				catch (ParseException e)
				{
					throw new JRRuntimeException(e);
				}
				break;
			}
			case NUMERIC :
			{
				FormatFactory formatFactory = new DefaultFormatFactory();
				NumberFormat newNf = 
					formatFactory.createNumberFormat(
						conditionPattern, 
						locale 
						);
				NumberFormat oldNf = 
					formatFactory.createNumberFormat(
						cfd.getConditionPattern(), 
						cfd.getLocaleCode() == null ? locale : JRDataUtils.getLocale(cfd.getLocaleCode()) 
						);
				
				try
				{
					for (FormatCondition fc: cfd.getConditions()) 
					{
						if (fc.getConditionStart() != null && !fc.getConditionStart().trim().isEmpty())
						{
							fc.setConditionStart(
								newNf.format(oldNf.parse(fc.getConditionStart()))
								);
						}
						if (fc.getConditionEnd() != null && !fc.getConditionEnd().trim().isEmpty())
						{
							fc.setConditionEnd(
								newNf.format(oldNf.parse(fc.getConditionEnd()))
								);
						}
					}
				}
				catch (ParseException e)
				{
					throw new JRRuntimeException(e);
				}
				break;
			}
		}
		
		cfd.setConditionPattern(conditionPattern);
		cfd.setLocaleCode(JRDataUtils.getLocaleCode(locale));
		cfd.setTimeZoneId(JRDataUtils.getTimeZoneId(timeZone));
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


	public static String getFilterPattern(
		JasperReportsContext jasperReportsContext, 
		Locale locale, 
		FilterTypesEnum filterType
		) 
	{
		String pattern = null;
		switch (filterType)
		{
			case DATE :
			{
				pattern = HeaderToolbarElementUtils.getDatePattern(jasperReportsContext, locale);
				break;
			}
			case TIME :
			{
				pattern = HeaderToolbarElementUtils.getTimePattern(jasperReportsContext, locale);
				break;
			}
			case NUMERIC :
			{
				pattern = HeaderToolbarElementUtils.getNumberPattern(jasperReportsContext, locale);
				break;
			}
			case TEXT :
			default : 
			{
			}
		}
		return pattern;
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