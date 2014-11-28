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
package net.sf.jasperreports.components.sort.actions;

import net.sf.jasperreports.components.headertoolbar.actions.BaseColumnData;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class FilterData extends BaseColumnData {
	
	private String fieldName;
	private String fieldValueStart;
	private String fieldValueEnd;
	private String filterType;
	private String filterTypeOperator;
	private String filterPattern;
	private String localeCode;
	private String timeZoneId;
	private boolean isField;
	private boolean clearFilter;
	
	public FilterData() {
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValueStart() {
		return fieldValueStart;
	}

	public void setFieldValueStart(String fieldValueStart) {
		this.fieldValueStart = fieldValueStart;
	}

	public String getFieldValueEnd() {
		return fieldValueEnd;
	}

	public void setFieldValueEnd(String fieldValueEnd) {
		this.fieldValueEnd = fieldValueEnd;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterTypeOperator() {
		return filterTypeOperator;
	}

	public void setFilterTypeOperator(String filterTypeOperator) {
		this.filterTypeOperator = filterTypeOperator;
	}

	public String getFilterPattern() {
		return filterPattern;
	}

	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public boolean isClearFilter() {
		return clearFilter;
	}

	public void setClearFilter(boolean clearFilter) {
		this.clearFilter = clearFilter;
	}

	public boolean getIsField() {
		return isField;
	}

	public void setIsField(boolean isField) {
		this.isField = isField;
	}

}
