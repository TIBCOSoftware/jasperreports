/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 *(at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.util;

import java.util.Locale;
import java.util.TimeZone;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDataUtils
{
	
	
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
