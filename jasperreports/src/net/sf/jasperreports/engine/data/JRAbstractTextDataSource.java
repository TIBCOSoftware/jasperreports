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
package net.sf.jasperreports.engine.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.FormatUtils;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRDateLocaleConverter;
import net.sf.jasperreports.engine.util.JRFloatLocaleConverter;

import org.apache.commons.beanutils.locale.LocaleConvertUtilsBean;

/**
 * Abstract text data source, containing methods used to parse text
 * data into numerical or date values.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRAbstractTextDataSource implements JRDataSource
{
	
	private LocaleConvertUtilsBean convertBean;
	
	private Locale locale;
	private String datePattern;
	private String numberPattern;
	private TimeZone timeZone;

	protected Object convertStringValue(String text, Class<?> valueClass)
	{
		Object value = null;
		if (String.class.equals(valueClass))
		{
			value = text;
		}
		else if (Number.class.isAssignableFrom(valueClass))
		{
			value = getConvertBean().convert(text.trim(), valueClass, locale, numberPattern);
		}
		else if (Date.class.isAssignableFrom(valueClass))
		{
			value = getConvertBean().convert(text.trim(), valueClass, locale, datePattern);
		}
		else if (Boolean.class.equals(valueClass))
		{
			value = Boolean.valueOf(text);
		}
		return value;
	}

	protected Object convertNumber(Number number, Class<?> valueClass) throws JRException
	{
		Number value = null;
		if (valueClass.equals(Byte.class))
		{
			value = new Byte(number.byteValue());
		}
		else if (valueClass.equals(Short.class))
		{
			value = new Short(number.shortValue());
		}
		else if (valueClass.equals(Integer.class))
		{
			value = Integer.valueOf(number.intValue());
		}
		else if (valueClass.equals(Long.class))
		{
			value = new Long(number.longValue());
		}
		else if (valueClass.equals(Float.class))
		{
			value = new Float(number.floatValue());
		}
		else if (valueClass.equals(Double.class))
		{
			value = new Double(number.doubleValue());
		}
		else if (valueClass.equals(BigInteger.class))
		{
			value = BigInteger.valueOf(number.longValue());
		}
		else if (valueClass.equals(BigDecimal.class))
		{
			value = new BigDecimal(Double.toString(number.doubleValue()));
		}
		else
		{
			throw new JRException("Unknown number class " + valueClass.getName());
		}
		return value;
	}

	/**
	 * @deprecated Replaced by {@link FormatUtils#getFormattedNumber(NumberFormat, String, Class)}
	 */
	protected Number getFormattedNumber(NumberFormat numberFormat, String fieldValue, Class<?> valueClass) throws ParseException
	{
		return FormatUtils.getFormattedNumber(numberFormat, fieldValue, valueClass);
	}
	
	/**
	 * @deprecated Replaced by {@link FormatUtils#getFormattedDate(DateFormat, String, Class)}
	 */
	protected Date getFormattedDate(DateFormat dateFormat, String fieldValue, Class<?> valueClass) throws ParseException 
	{
		return FormatUtils.getFormattedDate(dateFormat, fieldValue, valueClass);
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
				new JRDateLocaleConverter(timeZone), 
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

	/**
	 * Copy the text parsing attributes for another object.
	 * 
	 * @param textDataSource the object to copy the attributes from
	 */
	public void setTextAttributes(JRAbstractTextDataSource textDataSource)
	{
		setLocale(textDataSource.getLocale());
		setDatePattern(textDataSource.getDatePattern());
		setNumberPattern(textDataSource.getNumberPattern());
		setTimeZone(textDataSource.getTimeZone());
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		convertBean = null;
	}
	
	public void setLocale(String locale) {
		setLocale(JRDataUtils.getLocale(locale));
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
		convertBean = null;
	}

	public String getNumberPattern() {
		return numberPattern;
	}

	public void setNumberPattern(String numberPattern) {
		this.numberPattern = numberPattern;
		convertBean = null;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
		convertBean = null;
	}
	
	public void setTimeZone(String timeZoneId){
		setTimeZone(JRDataUtils.getTimeZone(timeZoneId));
	}
	
}
