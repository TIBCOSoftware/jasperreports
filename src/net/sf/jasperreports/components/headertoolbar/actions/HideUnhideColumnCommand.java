package net.sf.jasperreports.components.headertoolbar.actions;

import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.web.commands.Command;

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
