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
package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConditionalFormattingData extends BaseColumnData {

	private int columnIndex;
	private String conditionType;
	private String conditionPattern;
	private String localeCode;
	private String timeZoneId;
	private String columnType;
	private String fieldOrVariableName;
	private List<FormatCondition> conditions;

	private String applyTo;
	private String groupName;

	public ConditionalFormattingData() {
		this.conditions = new ArrayList<FormatCondition>();
	}

	@JsonIgnore
	public int getColumnIndex() {
		return columnIndex;
	}

	@JsonProperty
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String filterType) {
		this.conditionType = filterType;
	}

	public String getConditionPattern() {
		return conditionPattern;
	}

	public void setConditionPattern(String filterPattern) {
		this.conditionPattern = filterPattern;
	}

	@JsonIgnore
	public String getLocaleCode() {
		return localeCode;
	}

	@JsonIgnore
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	@JsonIgnore
	public String getTimeZoneId() {
		return timeZoneId;
	}

	@JsonIgnore
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	/**
	 * @deprecated To be removed.
	 */
	@JsonIgnore
	public String getColumnType() {
		return columnType;
	}
	
	/**
	 * @deprecated To be removed.
	 */
	@JsonIgnore
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * @deprecated To be removed.
	 */
	@JsonIgnore
	public String getFieldOrVariableName() {
		return fieldOrVariableName;
	}
	
	/**
	 * @deprecated To be removed.
	 */
	@JsonIgnore
	public void setFieldOrVariableName(String fieldOrVariableName) {
		this.fieldOrVariableName = fieldOrVariableName;
	}

	public List<FormatCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<FormatCondition> conditions) {
		this.conditions = conditions;
	}

	@JsonIgnore
	public String getApplyTo() {
		return applyTo;
	}

	@JsonProperty
	public void setApplyTo(String applyTo) {
		this.applyTo = applyTo;
	}

	@JsonIgnore
	public String getGroupName() {
		return groupName;
	}

	@JsonProperty
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
