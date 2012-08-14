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
package net.sf.jasperreports.components.headertoolbar.actions;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class FormatCondition {
	
	private String conditionStart;
	private String conditionEnd;
	private String conditionTypeOperator;
	private boolean conditionFontBold;
	private boolean conditionFontItalic;
	private boolean conditionFontUnderline;
	private String conditionFontColor;	
	
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

	public boolean isConditionFontBold() {
		return conditionFontBold;
	}

	public void setConditionFontBold(boolean conditionFontBold) {
		this.conditionFontBold = conditionFontBold;
	}

	public boolean isConditionFontItalic() {
		return conditionFontItalic;
	}

	public void setConditionFontItalic(boolean conditionFontItalic) {
		this.conditionFontItalic = conditionFontItalic;
	}

	public boolean isConditionFontUnderline() {
		return conditionFontUnderline;
	}

	public void setConditionFontUnderline(boolean conditionFontUnderline) {
		this.conditionFontUnderline = conditionFontUnderline;
	}

	public String getConditionFontColor() {
		return conditionFontColor;
	}

	public void setConditionFontColor(String conditionFontColor) {
		this.conditionFontColor = conditionFontColor;
	}
	
}
