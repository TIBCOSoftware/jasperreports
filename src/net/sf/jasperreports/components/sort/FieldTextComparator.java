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

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class FieldTextComparator extends AbstractFieldComparator<String> {
	

	public FieldTextComparator() {
	}
	
	@Override
	public void initValues() {
		compareStart = valueStart;
	}
	
	@Override
	public boolean compare (String filterTypeOperator) {
		boolean defaultResult = true, 
				result = defaultResult, 
				gotCompareWith = compareStart != null;
		
		try {
			initValues();
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
		
		FilterTypeTextOperatorsEnum textEnum = FilterTypeTextOperatorsEnum.getByEnumConstantName(filterTypeOperator);
		
		if (gotCompareWith) {
			switch (textEnum) {
			case CONTAINS:
				result = compareTo.contains(compareStart);
				break;
			case DOES_NOT_CONTAIN:
				result = !compareTo.contains(compareStart);
				break;
			case DOES_NOT_END_WITH:
				result = !compareTo.endsWith(compareStart);
				break;
			case DOES_NOT_START_WITH:
				result = !compareTo.startsWith(compareStart);
				break;
			case ENDS_WITH:
				result = compareTo.endsWith(compareStart);
				break;
			case EQUALS:
				result = compareTo.equals(compareStart);
				break;
			case IS_NOT_EQUAL_TO:
				result = !compareTo.equals(compareStart);
				break;
			case STARTS_WITH:
				result = compareTo.startsWith(compareStart);
				break;
			}
		}

		return result;
	}

}
