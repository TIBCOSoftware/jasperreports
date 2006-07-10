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
package net.sf.jasperreports.engine.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRExpression;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDataUtils
{

	/**
	 * Used in the date pattern to specify the default style.
	 * @see java.text.DateFormat#DEFAULT
	 */
	public static final String STANDARD_DATE_FORMAT_DEFAULT = "default";

	/**
	 * Used in the date pattern to specify the short style.
	 * @see java.text.DateFormat#SHORT
	 */
	public static final String STANDARD_DATE_FORMAT_SHORT = "short";

	/**
	 * Used in the date pattern to specify the medium style.
	 * @see java.text.DateFormat#MEDIUM
	 */
	public static final String STANDARD_DATE_FORMAT_MEDIUM = "medium";

	/**
	 * Used in the date pattern to specify the long style.
	 * @see java.text.DateFormat#LONG
	 */
	public static final String STANDARD_DATE_FORMAT_LONG = "long";

	/**
	 * Used in the date pattern to specify the full style.
	 * @see java.text.DateFormat#FULL
	 */
	public static final String STANDARD_DATE_FORMAT_FULL = "full";

	/**
	 * Used in the date pattern to specify that the date or time should not be included.
	 */
	public static final String STANDARD_DATE_FORMAT_HIDE = "hide";

	/**
	 * Used in the date format pattern to separate the date and time styles.
	 */
	public static final String STANDARD_DATE_FORMAT_SEPARATOR = ",";


	public static DateFormat getDateFormat(String pattern, Locale locale, TimeZone tz)
	{
		int[] dateStyle = null;
		int[] timeStyle = null;
		if (pattern != null && pattern.trim().length() > 0)
		{			
			int sepIdx = pattern.indexOf(STANDARD_DATE_FORMAT_SEPARATOR);
			String dateTok = sepIdx < 0 ? pattern : pattern.substring(0, sepIdx);
			dateStyle = getDateStyle(dateTok);
			if (dateStyle != null)
			{
				if (sepIdx >= 0)
				{
					String timeTok = pattern.substring(sepIdx + STANDARD_DATE_FORMAT_SEPARATOR.length());
					timeStyle = getDateStyle(timeTok);
				}
				else
				{
					timeStyle = dateStyle;
				}
			}
		}
		
		DateFormat format;
		if (dateStyle != null && timeStyle != null)
		{
			format = getDateFormat(dateStyle, timeStyle, locale);
		}
		else
		{			
			if (locale == null)
			{
				format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
			}
			else
			{
				format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
			}
			if (
				pattern != null && pattern.trim().length() > 0
				&& format instanceof SimpleDateFormat
				)
			{
				((SimpleDateFormat) format).applyPattern(pattern);
			}
		}
		
		format.setTimeZone(tz);
		
		return format;
	}


	protected static int[] getDateStyle(String pattern)
	{
		if (pattern.equalsIgnoreCase(STANDARD_DATE_FORMAT_DEFAULT))
		{
			return new int[]{DateFormat.DEFAULT};
		}
		else if (pattern.equalsIgnoreCase(STANDARD_DATE_FORMAT_SHORT))
		{
			return new int[]{DateFormat.SHORT};
		}
		else if (pattern.equalsIgnoreCase(STANDARD_DATE_FORMAT_MEDIUM))
		{
			return new int[]{DateFormat.MEDIUM};
		}
		else if (pattern.equalsIgnoreCase(STANDARD_DATE_FORMAT_LONG))
		{
			return new int[]{DateFormat.LONG};
		}
		else if (pattern.equalsIgnoreCase(STANDARD_DATE_FORMAT_FULL))
		{
			return new int[]{DateFormat.FULL};
		}
		else if (pattern.equalsIgnoreCase(STANDARD_DATE_FORMAT_HIDE))
		{
			return new int[0];
		}
		else
		{
			return null;
		}
	}

	
	protected static DateFormat getDateFormat(int[] dateStyle, int[] timeStyle, Locale locale)
	{
		if (dateStyle.length == 0)
		{
			if (timeStyle.length == 0)
			{
				return new SimpleDateFormat("");
			}

			return locale == null ? 
					DateFormat.getTimeInstance(timeStyle[0]) :
						DateFormat.getTimeInstance(timeStyle[0], locale);
		}

		if (timeStyle.length == 0)
		{
			return locale == null ? 
					DateFormat.getDateInstance(dateStyle[0]) :
					DateFormat.getDateInstance(dateStyle[0], locale);
		}

		return locale == null ? 
				DateFormat.getDateTimeInstance(dateStyle[0], timeStyle[0]) :
				DateFormat.getDateTimeInstance(dateStyle[0], timeStyle[0], locale);
	}
	
	
	public static NumberFormat getNumberFormat(String pattern, Locale locale)
	{
		NumberFormat format = null;
		if (pattern != null && pattern.trim().length() > 0)
		{
			if (locale == null)
			{
				format = NumberFormat.getNumberInstance();
			}
			else
			{
				format = NumberFormat.getNumberInstance(locale);
			}
			
			if (format instanceof DecimalFormat)
			{
				((DecimalFormat) format).applyPattern(pattern);
			}
		}
		return format;
	}
	
	
	public static Format getFormat(JRExpression valueExpression, String pattern, Locale locale, TimeZone timezone)
	{
		Format format = null;
		if (valueExpression != null)
		{
			Class valueClass = valueExpression.getValueClass();
			if (java.util.Date.class.isAssignableFrom(valueClass))
			{
				format = JRDataUtils.getDateFormat(pattern, locale, timezone);
			}
			else if (java.lang.Number.class.isAssignableFrom(valueClass))
			{
				format = JRDataUtils.getNumberFormat(pattern, locale);
			}
		}
		return format;
	}
	
	
	public static boolean useFormat(JRExpression valueExpression)
	{
		boolean format = false;
		if (valueExpression != null)
		{
			Class valueClass = valueExpression.getValueClass();
			format = java.util.Date.class.isAssignableFrom(valueClass)
				|| java.lang.Number.class.isAssignableFrom(valueClass);
		}
		return format;
	}
	
	
	public static String getPattern(JRExpression valueExpression, Format format, String originalPattern)
	{
		String pattern = null;
		if (format != null && valueExpression != null)
		{
			Class valueClass = valueExpression.getValueClass();
			if (java.util.Date.class.isAssignableFrom(valueClass))
			{
				if (format instanceof SimpleDateFormat)
				{
					pattern = ((SimpleDateFormat) format).toPattern();
				}
			}
			else if (Number.class.isAssignableFrom(valueClass))
			{
				if (format instanceof DecimalFormat)
				{
					pattern = ((DecimalFormat) format).toPattern();
				}
			}
		}
		
		if (pattern == null)//fallback to the original pattern
		{
			pattern = originalPattern;
		}
		
		return pattern;		
	}
	
	
	public static String getLocaleCode(Locale locale)
	{
		return locale.toString();
	}

	
	public static Locale getLocale(String code)
	{
		String language;
		String country;
		String variant;
		
		int firstSep = code.indexOf('_');
		if (firstSep < 0) {
			language = code;
			country = variant = "";
		} else {
			language = code.substring(0, firstSep);
			
			int secondSep = code.indexOf('_', firstSep + 1);
			if (secondSep < 0) {
				country = code.substring(firstSep + 1);
				variant = "";
			} else {
				country = code.substring(firstSep + 1, secondSep);
				variant = code.substring(secondSep + 1);
			}
		}
		
		return new Locale(language, country, variant);
	}
	
	
	public static String getTimeZoneId(TimeZone tz)
	{
		return tz.getID();
	}
	
	
	public static TimeZone getTimeZone(String id)
	{
		return TimeZone.getTimeZone(id);
	}
	
}
