package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.web.commands.Command;

public class EditColumnHeaderCommand implements Command 
{
	
	private StandardTable table;
	private EditColumnHeaderData editColumnHeaderData;
	private EditColumnHeaderData oldEditColumnHeaderData;
	private JRDesignTextElement textElement;
	private String oldText;


	public EditColumnHeaderCommand(StandardTable table, EditColumnHeaderData editColumnHeaderData) 
	{
		this.table = table;
		this.editColumnHeaderData = editColumnHeaderData;
	}


	public void execute() {
		List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
		StandardColumn column = (StandardColumn) tableColumns.get(editColumnHeaderData.getColumnIndex());
		textElement = TableUtil.getColumnHeaderTextElement(column);
		
		if (textElement != null) {
			oldEditColumnHeaderData = new EditColumnHeaderData();
			oldEditColumnHeaderData.setFontName(textElement.getFontName());
			oldEditColumnHeaderData.setFontSize(textElement.getFontSize());
			oldEditColumnHeaderData.setFontBold(textElement.isBold());
			oldEditColumnHeaderData.setFontItalic(textElement.isItalic());
			oldEditColumnHeaderData.setFontUnderline(textElement.isUnderline());
			oldEditColumnHeaderData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
			oldEditColumnHeaderData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
			
			applyColumnHeaderData(editColumnHeaderData, textElement, true);
		}
	}

	private void applyColumnHeaderData(EditColumnHeaderData headerData, JRDesignTextElement textElement, boolean execute) {
		if (textElement instanceof JRDesignTextField) {
			JRDesignTextField designTextField = (JRDesignTextField)textElement;
			if (execute) {
				if (oldText == null) {
					oldText = ((JRDesignExpression)designTextField.getExpression()).getText();
				}
				((JRDesignExpression)designTextField.getExpression()).setText("\"" + headerData.getHeadingName() + "\"");
			} else {
				((JRDesignExpression)designTextField.getExpression()).setText(oldText);
			}
			
		} else if (textElement instanceof JRDesignStaticText){
			JRDesignStaticText staticText = (JRDesignStaticText)textElement;
			if (execute) {
				if (oldText == null) {
					oldText = staticText.getText();
				}
				staticText.setText(headerData.getHeadingName());
			} else {
				staticText.setText(oldText);
			}
		}
		textElement.setFontName(headerData.getFontName());
		textElement.setFontSize(headerData.getFontSize());
		textElement.setBold(headerData.getFontBold());
		textElement.setItalic(headerData.getFontItalic());
		textElement.setUnderline(headerData.getFontUnderline());
		textElement.setForecolor(JRColorUtil.getColor("#" + headerData.getFontColor(), textElement.getForecolor()));
		textElement.setHorizontalAlignment(HorizontalAlignEnum.getByName(headerData.getFontHAlign()));
	}


	public void undo() {
		if (oldEditColumnHeaderData != null) {
			applyColumnHeaderData(oldEditColumnHeaderData, textElement, false);
		}
	}


	public void redo() {
		applyColumnHeaderData(editColumnHeaderData, textElement, true);
	}

}
