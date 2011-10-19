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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.FormatUtils;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class FieldNumberComparator extends AbstractFieldComparator<Number> {


	public FieldNumberComparator(String filterPattern, Locale locale) {
		if (locale == null) {
			formatter = NumberFormat.getNumberInstance();
		} else {
			formatter = NumberFormat.getNumberInstance(locale);
		}
		
		if (filterPattern != null && filterPattern.trim().length() > 0) {
			
			if (formatter instanceof DecimalFormat) {
				((DecimalFormat) formatter).applyPattern(filterPattern);
			}
		}
	}
	
	@Override
	public void initValues() throws Exception {
		if (valueStart != null && valueStart.length() > 0) {
			compareStart = FormatUtils.getFormattedNumber((NumberFormat)formatter, valueStart, compareTo.getClass());
		}
		if (valueEnd != null && valueEnd.length() > 0) {
			compareEnd = FormatUtils.getFormattedNumber((NumberFormat)formatter, valueEnd, compareTo.getClass());
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
		
		FilterTypeNumericOperatorsEnum numericEnum = FilterTypeNumericOperatorsEnum.getByEnumConstantName(filterTypeOperator);
		BigDecimal dbA = new BigDecimal(compareTo.toString());
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

}
