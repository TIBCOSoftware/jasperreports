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

import java.awt.Color;
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
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.web.commands.Command;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
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
			oldEditColumnHeaderData.setFontName(textElement.getOwnFontName());
			oldEditColumnHeaderData.setFontSize(textElement.getOwnFontSize() != null ? String.valueOf(textElement.getOwnFontSize()) : null);
			oldEditColumnHeaderData.setFontBold(textElement.isOwnBold());
			oldEditColumnHeaderData.setFontItalic(textElement.isOwnItalic());
			oldEditColumnHeaderData.setFontUnderline(textElement.isOwnUnderline());
			oldEditColumnHeaderData.setFontColor(textElement.getOwnForecolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnForecolor()) : null);
			oldEditColumnHeaderData.setFontBackColor(textElement.getOwnBackcolor() != null ? JRColorUtil.getColorHexa(textElement.getOwnBackcolor()) : null);
			oldEditColumnHeaderData.setFontHAlign(textElement.getOwnHorizontalAlignmentValue() != null ? textElement.getOwnHorizontalAlignmentValue().getName() : null);
			
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
		textElement.setFontSize(headerData.getFontSize() != null ? Integer.valueOf(headerData.getFontSize()) : null);
		textElement.setBold(headerData.getFontBold());
		textElement.setItalic(headerData.getFontItalic());
		textElement.setUnderline(headerData.getFontUnderline());
		textElement.setForecolor(headerData.getFontColor() != null ? JRColorUtil.getColor("#" + headerData.getFontColor(), textElement.getForecolor()) : null);
		textElement.setHorizontalAlignment(HorizontalAlignEnum.getByName(headerData.getFontHAlign()));
		
		if (headerData.getFontBackColor() != null) {
			if (headerData.getFontBackColor().equalsIgnoreCase("transparent")) {
				textElement.setMode(ModeEnum.TRANSPARENT);
			} else {
				textElement.setMode(ModeEnum.OPAQUE);
				textElement.setBackcolor(JRColorUtil.getColor("#" + headerData.getFontBackColor(), Color.white));
			}
		} else {
			textElement.setBackcolor(null);
			textElement.setMode(null);
		}
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
