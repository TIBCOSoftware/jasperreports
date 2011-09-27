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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.EvaluationType;
import net.sf.jasperreports.engine.fill.DatasetFillContext;
import net.sf.jasperreports.engine.util.JRDateLocaleConverter;
import net.sf.jasperreports.engine.util.JRFloatLocaleConverter;

import org.apache.commons.beanutils.locale.LocaleConvertUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A dataset filter that matches String values based on substrings.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FieldFilter implements DatasetFilter
{
	
	private static final Log log = LogFactory.getLog(FieldFilter.class);

	private LocaleConvertUtilsBean convertBean;
	private final String DEFAULT_NUMBER_PATTERN = "#,##0.##";
	private final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	
	private final String field;

	private String filterValueStart;
	private String filterValueEnd;
	
	private String filterType;
	private String filterTypeOperator;
	
	private DatasetFillContext context;
	private Locale locale;
	
	FilterTypesEnum filterTypeEnum;

	/**
	 * Creates a field filter.
	 * 
	 * 
	 */
	public FieldFilter(String field, String filterValueStart, String filterValueEnd, String filterType, String filterTypeOperator)
	{
		this.field = field;
		this.filterValueStart = filterValueStart;
		this.filterValueEnd = filterValueEnd;
		this.filterType = filterType;
		this.filterTypeOperator = filterTypeOperator;
	}
	
	public void init(DatasetFillContext context)
	{
		this.context = context;
		this.locale = context.getLocale();
		this.filterTypeEnum = FilterTypesEnum.getByName(filterType);
	}

	public boolean matches(EvaluationType evaluation)
	{
		Object value = context.getFieldValue(field, evaluation);
		if (value == null)
		{
			return false;
		}
		
		boolean result = true;
		
		switch (filterTypeEnum) {
			case DATE:
				result = dateMatch(value, FilterTypeDateOperatorsEnum.getByName(filterTypeOperator));
				break;
			case NUMERIC:
				result = numericMatch(value, FilterTypeNumericOperatorsEnum.getByName(filterTypeOperator));
				break;
			case TEXT:
				result = textMatch(value, FilterTypeTextOperatorsEnum.getByName(filterTypeOperator));
				break;
		}
		
		return result;
	}
	
	protected boolean numericMatch(Object value, FilterTypeNumericOperatorsEnum numericEnum) {
		Number compareStart = null, compareEnd = null;
		if (filterValueStart != null && filterValueStart.length() > 0){
			compareStart = (Number) convertStringValue(filterValueStart, value.getClass());
		}
		if (filterValueEnd != null && filterValueEnd.length() > 0){
			compareEnd =  (Number) convertStringValue(filterValueEnd, value.getClass());
		}
		return compareNumbers((Number) value, compareStart, compareEnd, numericEnum);
	}
	
	protected boolean dateMatch(Object value, FilterTypeDateOperatorsEnum dateEnum) {
		Date compareStart = null, compareEnd = null;
		if (filterValueStart != null && filterValueStart.length() > 0){
			compareStart = (Date) convertStringValue(filterValueStart, value.getClass());
		}
		if (filterValueEnd != null && filterValueEnd.length() > 0){
			compareEnd =  (Date) convertStringValue(filterValueEnd, value.getClass());
		}
		return compareDates((Date) value, compareStart, compareEnd, dateEnum);
	}

	protected boolean textMatch(Object value, FilterTypeTextOperatorsEnum textEnum) {
		return compareText((String) value, filterValueStart, textEnum);
	}
	
	protected Object convertStringValue(String text, Class<?> valueClass)
	{
		Object value = null;
		if (String.class.equals(valueClass))
		{
			value = text;
		}
		else if (Number.class.isAssignableFrom(valueClass))
		{
			value = getConvertBean().convert(text.trim(), valueClass, locale, DEFAULT_NUMBER_PATTERN);
		}
		else if (Date.class.isAssignableFrom(valueClass))
		{
			value = getConvertBean().convert(text.trim(), valueClass, locale, DEFAULT_DATE_PATTERN);
		}
		return value;
	}
	
	protected LocaleConvertUtilsBean getConvertBean() 
	{
		if (convertBean == null)
		{
			convertBean = new LocaleConvertUtilsBean();
			if (locale != null)
			{
				convertBean.setDefaultLocale(locale);
				convertBean.deregister();
				//convertBean.lookup();
			}
			convertBean.register(
				new JRDateLocaleConverter(TimeZone.getDefault()), 
				java.util.Date.class,
				locale
				);
			
			// fix for https://issues.apache.org/jira/browse/BEANUTILS-351
			// remove on upgrade to BeanUtils 1.8.1
			JRFloatLocaleConverter floatConverter = new JRFloatLocaleConverter(
					locale == null ? Locale.getDefault() : locale);
			convertBean.register(floatConverter, Float.class, locale);
			convertBean.register(floatConverter, Float.TYPE, locale);
		}
		return convertBean;
	}
	
	protected boolean compareNumbers(Number toCompare, Number compareStart, Number compareEnd, FilterTypeNumericOperatorsEnum numericEnum) {
		boolean defaultResult = true,
				result = defaultResult,
				resultPart1 = true, 
				resultPart2 = true;

		BigDecimal dbA = new BigDecimal(toCompare.toString());
		BigDecimal dbStart = compareStart != null ? new BigDecimal(compareStart.toString()) : null;
		BigDecimal dbEnd = compareEnd != null ? new BigDecimal(compareEnd.toString()) : null;
					
		switch (numericEnum) {
			case DOES_NOT_EQUAL:
				result = dbStart != null ? dbA.compareTo(dbStart) != 0 : defaultResult;
				break;
			case EQUALS:
				result = dbStart != null ? dbA.compareTo(dbStart) == 0 : defaultResult;
				break;
			case GREATER_THAN:
				result = dbStart != null ? dbA.compareTo(dbStart) > 0 : defaultResult;
				break;
			case GREATER_THAN_EQUAL_TO:
				result = dbStart != null ? dbA.compareTo(dbStart) >= 0 : defaultResult;
				break;
			case IS_BETWEEN:
				resultPart1 = dbStart != null ? dbA.compareTo(dbStart) >= 0 : defaultResult;
				resultPart2 = dbEnd != null ? dbA.compareTo(dbEnd) <= 0 : defaultResult;
				result = resultPart1 && resultPart2;
				break;
			case IS_NOT_BETWEEN:
				resultPart1 = dbStart != null ? dbA.compareTo(dbStart) >= 0 : defaultResult;
				resultPart2 = dbEnd != null ? dbA.compareTo(dbEnd) <= 0 : defaultResult;
				result = !(resultPart1 && resultPart2);
				break;
			case LESS_THAN:
				result = dbStart != null ? dbA.compareTo(dbStart) < 0 : defaultResult;
				break;
			case LESS_THAN_EQUAL_TO:
				result = dbStart != null ? dbA.compareTo(dbStart) <= 0 : defaultResult;
				break;
		}
		
		return result;
	}
	
	protected boolean compareDates(Date toCompare, Date compareStart, Date compareEnd, FilterTypeDateOperatorsEnum dateEnum) {
		boolean defaultResult = true,
				result = defaultResult,
				resultPart1 = true, 
				resultPart2 = true;
		
		switch (dateEnum) {
			case EQUALS:
				result = compareStart != null ? toCompare.compareTo(compareStart) == 0 : defaultResult;
				break;
			case IS_AFTER:
				result = compareStart != null ? toCompare.compareTo(compareStart) > 0 : defaultResult;
				break;
			case IS_BEFORE:
				result = compareStart != null ? toCompare.compareTo(compareStart) < 0 : defaultResult;
				break;
			case IS_BETWEEN:
				resultPart1 = compareStart != null ? toCompare.compareTo(compareStart) >= 0 : defaultResult;
				resultPart2 = compareEnd != null ? toCompare.compareTo(compareEnd) <= 0 : defaultResult;
				result = resultPart1 && resultPart2;
				break;
			case IS_NOT_BETWEEN:
				resultPart1 = compareStart != null ? toCompare.compareTo(compareStart) >= 0 : defaultResult;
				resultPart2 = compareEnd != null ? toCompare.compareTo(compareEnd) <= 0 : defaultResult;
				result = !(resultPart1 && resultPart2);
				break;
			case IS_NOT_EQUAL_TO:
				result = compareStart != null ? toCompare.compareTo(compareStart) != 0 : defaultResult;
				break;
			case IS_ON_OR_AFTER:
				result = compareStart != null ? toCompare.compareTo(compareStart) >= 0 : defaultResult;
				break;
			case IS_ON_OR_BEFORE:
				result = compareStart != null ? toCompare.compareTo(compareStart) <= 0 : defaultResult;
				break;
		}
		
		return result;
	}
	
	protected boolean compareText(String toCompare, String compareWith, FilterTypeTextOperatorsEnum textEnum) {
		boolean defaultResult = true,
				result = defaultResult,
				gotCompareWith = compareWith != null && compareWith.length() > 0;
		
		switch (textEnum) {
			case CONTAINS:
				result = gotCompareWith ? toCompare.contains(compareWith) : defaultResult;
				break;
			case DOES_NOT_CONTAIN:
				result = gotCompareWith ? !toCompare.contains(compareWith) : defaultResult;
				break;
			case DOES_NOT_END_WITH:
				result = gotCompareWith ? !toCompare.endsWith(compareWith) : defaultResult;
				break;
			case DOES_NOT_START_WITH:
				result = gotCompareWith ? !toCompare.startsWith(compareWith) : defaultResult;
				break;
			case ENDS_WITH:
				result = gotCompareWith ? toCompare.endsWith(compareWith) : defaultResult;
				break;
			case EQUALS:
				result = gotCompareWith ? toCompare.equals(compareWith) : defaultResult;
				break;
			case IS_NOT_EQUAL_TO:
				result = gotCompareWith ? !toCompare.equals(compareWith) : defaultResult;
				break;
			case STARTS_WITH:
				result = gotCompareWith ? toCompare.startsWith(compareWith) : defaultResult;
				break;
		}
		
		return result;
	}

}
