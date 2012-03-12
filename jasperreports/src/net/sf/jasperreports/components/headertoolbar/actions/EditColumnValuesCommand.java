package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.web.commands.Command;

public class EditColumnValuesCommand implements Command 
{
	
	private StandardTable table;
	private EditColumnValueData editColumnValueData;
	private EditColumnValueData oldEditColumnValueData;
	private JRDesignTextField textElement;


	public EditColumnValuesCommand(StandardTable table, EditColumnValueData editColumnHeaderData) 
	{
		this.table = table;
		this.editColumnValueData = editColumnHeaderData;
	}


	public void execute() {
		List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
		StandardColumn column = (StandardColumn) tableColumns.get(editColumnValueData.getColumnIndex());
		textElement = (JRDesignTextField) TableUtil.getColumnValueTextElement(column);
		
		if (textElement != null) {
			oldEditColumnValueData = new EditColumnValueData();
			oldEditColumnValueData.setFontName(textElement.getFontName());
			oldEditColumnValueData.setFontSize(textElement.getFontSize());
			oldEditColumnValueData.setFontBold(textElement.isBold());
			oldEditColumnValueData.setFontItalic(textElement.isItalic());
			oldEditColumnValueData.setFontUnderline(textElement.isUnderline());
			oldEditColumnValueData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
			oldEditColumnValueData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
			oldEditColumnValueData.setFormatPattern(textElement.getPattern());
			
			applyColumnHeaderData(editColumnValueData, textElement, true);
		}
	}

	private void applyColumnHeaderData(EditColumnValueData headerData, JRDesignTextField textElement, boolean execute) {
		textElement.setFontName(headerData.getFontName());
		textElement.setFontSize(headerData.getFontSize());
		textElement.setBold(headerData.getFontBold());
		textElement.setItalic(headerData.getFontItalic());
		textElement.setUnderline(headerData.getFontUnderline());
		textElement.setForecolor(JRColorUtil.getColor("#" + headerData.getFontColor(), textElement.getForecolor()));
		textElement.setHorizontalAlignment(HorizontalAlignEnum.getByName(headerData.getFontHAlign()));
		textElement.setPattern(headerData.getFormatPattern());
	}


	public void undo() {
		if (oldEditColumnValueData != null) {
			applyColumnHeaderData(oldEditColumnValueData, textElement, false);
		}
	}


	public void redo() {
		applyColumnHeaderData(editColumnValueData, textElement, true);
	}

}
