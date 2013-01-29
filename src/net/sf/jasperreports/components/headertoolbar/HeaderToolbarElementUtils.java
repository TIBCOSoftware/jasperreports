/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.headertoolbar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.components.headertoolbar.actions.EditColumnHeaderData;
import net.sf.jasperreports.components.headertoolbar.actions.EditColumnValueData;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;



/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HeaderToolbarElementUtils {
	
	private static Map<String, SortOrderEnum> sortOrderMapping = new HashMap<String, SortOrderEnum>();
	
	static {
		sortOrderMapping.put(HeaderToolbarElement.SORT_ORDER_ASC, SortOrderEnum.ASCENDING);
		sortOrderMapping.put(HeaderToolbarElement.SORT_ORDER_DESC, SortOrderEnum.DESCENDING);
	}
	
	public static String[] extractColumnInfo(String sortColumn) {
		return sortColumn.split(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR);
	}
	
	public static String packSortColumnInfo(String columnName, String columnType, String sortOrder) {
		StringBuffer sb = new StringBuffer();
		sb.append(columnName)
			.append(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR)
			.append(columnType)
			.append(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR)
			.append(sortOrder);
		return sb.toString();
	}
	
	public static boolean isValidSortData(String sortData) {
		return sortData != null 
				&& sortData.indexOf(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR) >= 0 
				&& sortData.split(HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR).length > 1;
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

	public static void copyOwnTextElementStyle(EditColumnHeaderData columnHeaderData, JRDesignTextElement textElement) {
		columnHeaderData.setFontName(textElement.getOwnFontName());
		columnHeaderData.setFontSize(textElement.getOwnFontSize() != null ? String.valueOf(textElement.getOwnFontSize()) : null);
		columnHeaderData.setFontBold(textElement.isOwnBold());
		columnHeaderData.setFontItalic(textElement.isOwnItalic());
		columnHeaderData.setFontUnderline(textElement.isOwnUnderline());
		columnHeaderData.setFontColor(textElement.getOwnForecolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnForecolor()) : null);
		columnHeaderData.setFontBackColor(textElement.getOwnBackcolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnBackcolor()) : null);
		columnHeaderData.setFontHAlign(textElement.getOwnHorizontalAlignmentValue() != null ? textElement.getOwnHorizontalAlignmentValue().getName() : null);
		columnHeaderData.setMode(textElement.getOwnModeValue() != null ? textElement.getOwnModeValue().getName() : null);
	}

	public static void copyTextElementStyle(EditColumnHeaderData columnHeaderData, JRDesignTextElement textElement) {
		columnHeaderData.setFontName(textElement.getFontName());
		columnHeaderData.setFontSize(String.valueOf(textElement.getFontSize()));
		columnHeaderData.setFontBold(textElement.isBold());
		columnHeaderData.setFontItalic(textElement.isItalic());
		columnHeaderData.setFontUnderline(textElement.isUnderline());
		columnHeaderData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
		columnHeaderData.setFontBackColor(JRColorUtil.getColorHexa(textElement.getBackcolor()));
		columnHeaderData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
		columnHeaderData.setMode(textElement.getModeValue().getName());
	}

	public static void copyOwnTextFieldStyle(EditColumnValueData columnValueData, JRDesignTextField textField) {
		columnValueData.setFontName(textField.getOwnFontName());
		columnValueData.setFontSize(textField.getOwnFontSize() != null ? String.valueOf(textField.getOwnFontSize()) : null);
		columnValueData.setFontBold(textField.isOwnBold());
		columnValueData.setFontItalic(textField.isOwnItalic());
		columnValueData.setFontUnderline(textField.isOwnUnderline());
		columnValueData.setFontColor(textField.getOwnForecolor() != null ? JRColorUtil.getColorHexa(textField.getOwnForecolor()) : null);
		columnValueData.setFontBackColor(textField.getOwnBackcolor() != null ? JRColorUtil.getColorHexa(textField.getOwnBackcolor()) : null);
		columnValueData.setFontHAlign(textField.getOwnHorizontalAlignmentValue() != null ? textField.getOwnHorizontalAlignmentValue().getName() : null);
		columnValueData.setMode(textField.getOwnModeValue() != null ? textField.getOwnModeValue().getName() : null);
		
		if (TableUtil.hasSingleChunkExpression(textField)) {
			columnValueData.setFormatPattern(textField.getOwnPattern());
		}
	}

	public static void copyTextFieldStyle(EditColumnValueData columnValueData, JRDesignTextField textField) {
		columnValueData.setFontName(textField.getFontName());
		columnValueData.setFontSize(String.valueOf(textField.getFontSize()));
		columnValueData.setFontBold(textField.isBold());
		columnValueData.setFontItalic(textField.isItalic());
		columnValueData.setFontUnderline(textField.isUnderline());
		columnValueData.setFontColor(JRColorUtil.getColorHexa(textField.getForecolor()));
		columnValueData.setFontBackColor(JRColorUtil.getColorHexa(textField.getBackcolor()));
		columnValueData.setFontHAlign(textField.getHorizontalAlignmentValue().getName());
		columnValueData.setMode(textField.getModeValue().getName());
		
		if (TableUtil.hasSingleChunkExpression(textField)) {
			columnValueData.setFormatPattern(textField.getPattern());
		}
	}

}