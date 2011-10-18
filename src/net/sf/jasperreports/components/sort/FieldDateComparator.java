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

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.FormatUtils;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class FieldDateComparator extends AbstractFieldComparator<Date> {

	public FieldDateComparator(String filterPattern, Locale locale, TimeZone timeZone) {
		formatter = getFormatFactory().createDateFormat(filterPattern, locale, timeZone);
	}
	
	@Override
	public void initValues() throws Exception {
		if (valueStart != null && valueStart.length() > 0) {
			compareStart = FormatUtils.getFormattedDate((DateFormat)formatter, valueStart, compareTo.getClass());
		}
		if (valueEnd != null && valueEnd.length() > 0) {
			compareEnd = FormatUtils.getFormattedDate((DateFormat)formatter, valueEnd, compareTo.getClass());
		}
	}
	
	@Override
	public boolean compare(String filterTypeOperator) {
		boolean defaultResult = true,
				result = defaultResult,
				resultPart1 = true, 
				resultPart2 = true;
		
		try {
			initValues();
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
		
		FilterTypeDateOperatorsEnum dateEnum = FilterTypeDateOperatorsEnum.getByEnumConstantName(filterTypeOperator);
		switch (dateEnum) {
			case EQUALS:
				result = compareStart != null ? compareTo.compareTo(compareStart) == 0 : defaultResult;
				break;
			case IS_AFTER:
				result = compareStart != null ? compareTo.compareTo(compareStart) > 0 : defaultResult;
				break;
			case IS_BEFORE:
				result = compareStart != null ? compareTo.compareTo(compareStart) < 0 : defaultResult;
				break;
			case IS_BETWEEN:
				resultPart1 = compareStart != null ? compareTo.compareTo(compareStart) >= 0 : defaultResult;
				resultPart2 = compareEnd != null ? compareTo.compareTo(compareEnd) <= 0 : defaultResult;
				result = resultPart1 && resultPart2;
				break;
			case IS_NOT_BETWEEN:
				resultPart1 = compareStart != null ? compareTo.compareTo(compareStart) >= 0 : defaultResult;
				resultPart2 = compareEnd != null ? compareTo.compareTo(compareEnd) <= 0 : defaultResult;
				result = !(resultPart1 && resultPart2);
				break;
			case IS_NOT_EQUAL_TO:
				result = compareStart != null ? compareTo.compareTo(compareStart) != 0 : defaultResult;
				break;
			case IS_ON_OR_AFTER:
				result = compareStart != null ? compareTo.compareTo(compareStart) >= 0 : defaultResult;
				break;
			case IS_ON_OR_BEFORE:
				result = compareStart != null ? compareTo.compareTo(compareStart) <= 0 : defaultResult;
				break;
		}
		
		return result;
	}

}
