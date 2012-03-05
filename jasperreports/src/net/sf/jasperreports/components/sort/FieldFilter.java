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
import java.util.TimeZone;

import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.EvaluationType;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.fill.DatasetFillContext;

/**
 * A dataset filter that matches String values based on substrings.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class FieldFilter implements DatasetFilter {

	private final String field;

	private String filterValueStart;
	private String filterValueEnd;
	
	private String filterType;
	private String filterTypeOperator;
	private String filterPattern;

	private DatasetFillContext context;
	
	private Boolean isValid;

	private FilterTypesEnum filterTypeEnum;
	
	private AbstractFieldComparator<?> fieldComparator;
	
	/**
	 * Creates a field filter.
	 * 
	 * 
	 */
	public FieldFilter(String field, String filterValueStart,
			String filterValueEnd, String filterType, String filterTypeOperator) {
		this.field = field;
		this.filterValueStart = filterValueStart;
		this.filterValueEnd = filterValueEnd;
		this.filterType = filterType;
		this.filterTypeOperator = filterTypeOperator;
	}

	public void init(DatasetFillContext context) {
		this.context = context;
		this.filterTypeEnum = FilterTypesEnum.getByName(filterType);
		if (fieldComparator == null) {
			fieldComparator = FieldComparatorFactory
					.createFieldComparator(
							filterTypeEnum,
							filterPattern,
							context.getLocale() != null ? context.getLocale() : Locale.getDefault(),
							(TimeZone) context.getParameterValue(JRParameter.REPORT_TIME_ZONE));
		}
	}

	public boolean matches(EvaluationType evaluation) {
		Object value = context.getFieldValue(field, evaluation);
		if (value == null) {
			return false;
		}

		fieldComparator.setValueStart(filterValueStart);
		fieldComparator.setValueEnd(filterValueEnd);
		fieldComparator.setCompareTo(value);
		
		if (isValid == null) {
			isValid = fieldComparator.isValid();
		}
		if (!isValid) {
			return true;
		}
		return fieldComparator.compare(filterTypeOperator);
	}

	public String getField() {
		return this.field;
	}

	public String getFilterValueStart() {
		return filterValueStart;
	}

	public void setFilterValueStart(String filterValueStart) {
		this.filterValueStart = filterValueStart;
	}

	public String getFilterValueEnd() {
		return filterValueEnd;
	}

	public void setFilterValueEnd(String filterValueEnd) {
		this.filterValueEnd = filterValueEnd;
	}

	public String getFilterType() {
		return filterType;
	}

	public String getFilterTypeOperator() {
		return filterTypeOperator;
	}

	public void setFilterTypeOperator(String filterTypeOperator) {
		this.filterTypeOperator = filterTypeOperator;
	}

	public FilterTypesEnum getFilterTypeEnum() {
		return filterTypeEnum;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}
	
}
