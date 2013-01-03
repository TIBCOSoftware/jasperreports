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

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.locale.converters.DecimalLocaleConverter;

/**
 * <code>FloatLocaleConverter</code> extension that fixes 
 * https://issues.apache.org/jira/browse/BEANUTILS-351
 * 
 * To be removed on upgrade to BeanUtils 1.8.1.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFloatLocaleConverter extends DecimalLocaleConverter
{

	public JRFloatLocaleConverter(Locale locale)
	{
		super(locale);
	}

	protected Object parse(Object value, String pattern) throws ParseException
	{
		final Number parsed = (Number) super.parse(value, pattern);
		double doubleValue = parsed.doubleValue();
		double posDouble = (doubleValue >= 0) ? doubleValue : (doubleValue * -1);
		if ((posDouble > 0 && posDouble < Float.MIN_VALUE) || posDouble > Float.MAX_VALUE)
		{
			throw new ConversionException("Supplied number is not of type Float: "+parsed);
		}
		return new Float(parsed.floatValue()); // unlike superclass it returns Float type
	}
	
}
