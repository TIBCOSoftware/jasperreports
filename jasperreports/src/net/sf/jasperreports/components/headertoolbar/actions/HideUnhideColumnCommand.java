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

import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.web.commands.Command;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class HideUnhideColumnCommand implements Command {
	
	private String expressionText;
	
	private String oldPrintWhenExpressionText;
	private StandardColumn column;
	private JRDesignExpression newPrintWhenExpression;
	
	private static final String BOOLEAN_FALSE = "Boolean.FALSE";
	private static final String BOOLEAN_TRUE = "Boolean.TRUE";


	public HideUnhideColumnCommand(StandardColumn column, boolean hide) {
		this.column = column;
		if (hide) {
			this.expressionText = BOOLEAN_FALSE;
		} else {
			this.expressionText = BOOLEAN_TRUE;
		}
	}


	public void execute() {
		if (column.getPrintWhenExpression() != null) {
			oldPrintWhenExpressionText = column.getPrintWhenExpression().getText();
			((JRDesignExpression)column.getPrintWhenExpression()).setText(expressionText);
		} else {
			newPrintWhenExpression = new JRDesignExpression();
			newPrintWhenExpression.setText(expressionText);
			column.setPrintWhenExpression(newPrintWhenExpression);
		}
	}


	public void undo() {
		if (oldPrintWhenExpressionText != null) {
			((JRDesignExpression)column.getPrintWhenExpression()).setText(oldPrintWhenExpressionText);
		} else {
			column.setPrintWhenExpression(null);
		}
	}


	public void redo() {
		if (newPrintWhenExpression != null) {
			column.setPrintWhenExpression(newPrintWhenExpression);
		} else {
			((JRDesignExpression)column.getPrintWhenExpression()).setText(expressionText);
		}
	}

}
