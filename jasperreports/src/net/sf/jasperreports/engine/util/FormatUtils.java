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
package net.sf.jasperreports.engine.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class FormatUtils {
	
	/**
	 * Creates a number from a string value
	 * 
	 * @param numberFormat
	 * @param fieldValue
	 * @param valueClass
	 * @return
	 * @throws ParseException
	 */
	public static Number getFormattedNumber(NumberFormat numberFormat, String fieldValue, Class<?> valueClass) throws ParseException {
		
		if (valueClass.equals(Byte.class)) 
		{
			return new Byte((numberFormat.parse(fieldValue)).byteValue());
		}
		else if (valueClass.equals(Integer.class)) 
		{
			return Integer.valueOf((numberFormat.parse(fieldValue)).intValue());
		}
		else if (valueClass.equals(Long.class)) 
		{
			return new Long((numberFormat.parse(fieldValue)).longValue());
		}
		else if (valueClass.equals(Short.class)) 
		{
			return new Short((numberFormat.parse(fieldValue)).shortValue());
		}
		else if (valueClass.equals(Double.class)) 
		{
			return new Double((numberFormat.parse(fieldValue)).doubleValue());
		}
		else if (valueClass.equals(Float.class)) 
		{
			return new Float((numberFormat.parse(fieldValue)).floatValue());
		}
		else if (valueClass.equals(BigDecimal.class)) 
		{
			return new BigDecimal((numberFormat.parse(fieldValue)).toString());
		}
		else if (valueClass.equals(BigInteger.class)) 
		{
			return new BigInteger(String.valueOf(numberFormat.parse(fieldValue).longValue()));
		}
		else if(valueClass.equals(java.lang.Number.class)) 
		{
			return numberFormat.parse(fieldValue);
		}
		return null;
	}
	
	/**
	 * Creates a date from a string value
	 * 
	 * @param dateFormat
	 * @param fieldValue
	 * @param valueClass
	 * @return
	 * @throws ParseException
	 */
	public static Date getFormattedDate(DateFormat dateFormat, String fieldValue, Class<?> valueClass) throws ParseException {
		
		if (valueClass.equals(java.util.Date.class)) 
		{
			return dateFormat.parse(fieldValue);
		}
		else if (valueClass.equals(java.sql.Timestamp.class)) 
		{
			return new java.sql.Timestamp(dateFormat.parse(fieldValue).getTime());
		}
		else if (valueClass.equals(java.sql.Time.class)) 
		{
			return new java.sql.Time(dateFormat.parse(fieldValue).getTime());
		}
		return null;
	}

}
