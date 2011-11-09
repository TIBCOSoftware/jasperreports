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

import java.util.Locale;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class FieldTextComparator extends AbstractFieldComparator<String> {

	private Locale locale;

	public FieldTextComparator(Locale locale) {
		this.locale = locale;
	}
	
	@Override
	public void initValues() {
		compareStart = valueStart;
	}
	
	@Override
	public boolean compare (String filterTypeOperator) {
		boolean defaultResult = true, 
				result = defaultResult;
		
		try {
			initValues();
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
		
		FilterTypeTextOperatorsEnum textEnum = FilterTypeTextOperatorsEnum.getByEnumConstantName(filterTypeOperator);
		
		if (compareStart != null) {
			String lcCompareStart = compareStart.toLowerCase(locale);
			String lcCompareTo = compareTo.toLowerCase(locale);
			switch (textEnum) {
				case CONTAINS:
					result = lcCompareTo.contains(lcCompareStart);
					break;
				case DOES_NOT_CONTAIN:
					result = !lcCompareTo.contains(lcCompareStart);
					break;
				case DOES_NOT_END_WITH:
					result = !lcCompareTo.endsWith(lcCompareStart);
					break;
				case DOES_NOT_START_WITH:
					result = !lcCompareTo.startsWith(lcCompareStart);
					break;
				case ENDS_WITH:
					result = lcCompareTo.endsWith(lcCompareStart);
					break;
				case EQUALS:
					result = lcCompareTo.equals(lcCompareStart);
					break;
				case IS_NOT_EQUAL_TO:
					result = !lcCompareTo.equals(lcCompareStart);
					break;
				case STARTS_WITH:
					result = lcCompareTo.startsWith(lcCompareStart);
					break;
			}
		}

		return result;
	}

}
