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

import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.components.sort.AbstractFieldComparator;
import net.sf.jasperreports.components.sort.FieldComparatorFactory;
import net.sf.jasperreports.components.sort.FilterTypesEnum;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class FormatCondition {
	
	private String conditionStart;
	private String conditionEnd;
	private String conditionTypeOperator;
	private Boolean conditionFontBold;
	private Boolean conditionFontItalic;
	private Boolean conditionFontUnderline;
	private String conditionFontColor;
	private String conditionFontBackColor;
	private String conditionMode;
	
	public FormatCondition() {
	}

	public String getConditionStart() {
		return conditionStart;
	}

	public void setConditionStart(String conditionStart) {
		this.conditionStart = conditionStart;
	}

	public String getConditionEnd() {
		return conditionEnd;
	}

	public void setConditionEnd(String conditionEnd) {
		this.conditionEnd = conditionEnd;
	}

	public String getConditionTypeOperator() {
		return conditionTypeOperator;
	}

	public void setConditionTypeOperator(String conditionTypeOperator) {
		this.conditionTypeOperator = conditionTypeOperator;
	}

	public Boolean isConditionFontBold() {
		return conditionFontBold;
	}

	public void setConditionFontBold(Boolean conditionFontBold) {
		this.conditionFontBold = conditionFontBold;
	}

	public Boolean isConditionFontItalic() {
		return conditionFontItalic;
	}

	public void setConditionFontItalic(Boolean conditionFontItalic) {
		this.conditionFontItalic = conditionFontItalic;
	}

	public Boolean isConditionFontUnderline() {
		return conditionFontUnderline;
	}

	public void setConditionFontUnderline(Boolean conditionFontUnderline) {
		this.conditionFontUnderline = conditionFontUnderline;
	}

	public String getConditionFontColor() {
		return conditionFontColor;
	}

	public void setConditionFontColor(String conditionFontColor) {
		this.conditionFontColor = conditionFontColor;
	}

	public String getConditionFontBackColor() {
		return conditionFontBackColor;
	}

	public void setConditionFontBackColor(String conditionFontBackColor) {
		this.conditionFontBackColor = conditionFontBackColor;
	}

	public String getConditionMode() {
		return conditionMode;
	}

	public void setConditionMode(String conditionMode) {
		this.conditionMode = conditionMode;
	}
	
	public boolean matches(
		Object compareTo, 
		String conditionType, 
		String conditionPattern, 
		String conditionTypeOperator,
		Locale locale,
		TimeZone timeZone
		) 
	{
		AbstractFieldComparator<?> fieldComparator = 
			FieldComparatorFactory
				.createFieldComparator(
					FilterTypesEnum.getByName(conditionType),
					conditionPattern,
					locale,
					timeZone
					);
		
		fieldComparator.setValueStart(conditionStart);
		fieldComparator.setValueEnd(conditionEnd);
		fieldComparator.setCompareTo(compareTo);
		fieldComparator.setCompareToClass(compareTo != null ? compareTo.getClass() : Object.class);
		
		return fieldComparator.compare(conditionTypeOperator);
	}
	
}
