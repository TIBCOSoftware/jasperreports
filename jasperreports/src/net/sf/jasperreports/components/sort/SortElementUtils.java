/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.type.SortOrderEnum;



/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SortElementUtils {
	
	private static Map<String, SortOrderEnum> sortOrderMapping = new HashMap<String, SortOrderEnum>();
	
	static {
		sortOrderMapping.put(SortElement.SORT_ORDER_ASC, SortOrderEnum.ASCENDING);
		sortOrderMapping.put(SortElement.SORT_ORDER_DESC, SortOrderEnum.DESCENDING);
	}
	
	public static String[] extractColumnInfo(String sortColumn) {
		return sortColumn.split(SortElement.SORT_COLUMN_TOKEN_SEPARATOR);
	}
	
	public static String packSortColumnInfo(String columnName, String columnType, String sortOrder) {
		StringBuffer sb = new StringBuffer();
		sb.append(columnName)
			.append(SortElement.SORT_COLUMN_TOKEN_SEPARATOR)
			.append(columnType)
			.append(SortElement.SORT_COLUMN_TOKEN_SEPARATOR)
			.append(sortOrder);
		return sb.toString();
	}
	
	public static boolean isValidSortData(String sortData) {
		return sortData != null 
				&& sortData.indexOf(SortElement.SORT_COLUMN_TOKEN_SEPARATOR) >= 0 
				&& sortData.split(SortElement.SORT_COLUMN_TOKEN_SEPARATOR).length > 1;
	}
	
	public static SortOrderEnum getSortOrder(String sortOrder) {
		return sortOrderMapping.get(sortOrder);
	}

	public static FilterTypesEnum getFilterType(Class<?> clazz) {
		FilterTypesEnum result = null;
		if (Number.class.isAssignableFrom(clazz)) 
		{
			result = FilterTypesEnum.NUMERIC;
		}
		else if (String.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.TEXT;
		}
		else if (Date.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.DATE;
		}
		else if (Boolean.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.BOOLEAN;
		}
		return result;
	}
}