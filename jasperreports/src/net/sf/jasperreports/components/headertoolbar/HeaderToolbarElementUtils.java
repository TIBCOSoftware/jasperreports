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

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.components.headertoolbar.actions.EditTextElementData;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;



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
		else if (Time.class.isAssignableFrom(clazz))
		{
			result = FilterTypesEnum.TIME;
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

	public static void copyOwnTextElementStyle(EditTextElementData textElementData, JRDesignTextElement textElement) {
		textElementData.setFontName(textElement.getOwnFontName());
		textElementData.setFontSize(textElement.getOwnFontSize() != null ? String.valueOf(textElement.getOwnFontSize()) : null);
		textElementData.setFontBold(textElement.isOwnBold());
		textElementData.setFontItalic(textElement.isOwnItalic());
		textElementData.setFontUnderline(textElement.isOwnUnderline());
		textElementData.setFontColor(textElement.getOwnForecolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnForecolor()) : null);
		textElementData.setFontBackColor(textElement.getOwnBackcolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnBackcolor()) : null);
		textElementData.setFontHAlign(textElement.getOwnHorizontalAlignmentValue() != null ? textElement.getOwnHorizontalAlignmentValue().getName() : null);
		textElementData.setMode(textElement.getOwnModeValue() != null ? textElement.getOwnModeValue().getName() : null);
		
		if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
			textElementData.setFormatPattern(((JRDesignTextField) textElement).getOwnPattern());
		}
	}

	public static void copyTextElementStyle(EditTextElementData textElementData, JRDesignTextElement textElement) {
		textElementData.setFontName(JRStringUtil.htmlEncode(textElement.getFontName()));
		textElementData.setFontSize(String.valueOf(textElement.getFontSize()));
		textElementData.setFontBold(textElement.isBold());
		textElementData.setFontItalic(textElement.isItalic());
		textElementData.setFontUnderline(textElement.isUnderline());
		textElementData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
		textElementData.setFontBackColor(JRColorUtil.getColorHexa(textElement.getBackcolor()));
		textElementData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
		textElementData.setMode(textElement.getModeValue().getName());
		
		if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
			textElementData.setFormatPattern(JRStringUtil.htmlEncode(((JRDesignTextField) textElement).getPattern()));
		}
	}

}