/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.beanutils.locale.LocaleConvertUtilsBean;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRDateLocaleConverter;

/**
 * Abstract text data source, containing methods used to parse text
 * data into numerical or date values.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public abstract class JRAbstractTextDataSource implements JRDataSource
{
	
	private LocaleConvertUtilsBean convertBean = null;
	
	private Locale locale = null;
	private String datePattern = null;
	private String numberPattern = null;
	private TimeZone timeZone = null;

	protected Object convertStringValue(String text, Class valueClass)
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

	protected Object convertNumber(Number number, Class valueClass) throws JRException
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
			value = new Integer(number.intValue());
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

}
