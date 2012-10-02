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
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.web.commands.Command;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
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
		textElement = (JRDesignTextField) TableUtil.getColumnDetailTextElement(column);
		
		if (textElement != null) {
			oldEditColumnValueData = new EditColumnValueData();
			oldEditColumnValueData.setFontName(textElement.getFontName());
			oldEditColumnValueData.setFontSize(String.valueOf(textElement.getFontSize()));
			oldEditColumnValueData.setFontBold(textElement.isBold());
			oldEditColumnValueData.setFontItalic(textElement.isItalic());
			oldEditColumnValueData.setFontUnderline(textElement.isUnderline());
			oldEditColumnValueData.setFontColor(JRColorUtil.getColorHexa(textElement.getForecolor()));
			oldEditColumnValueData.setFontBackColor(JRColorUtil.getColorHexa(textElement.getBackcolor()));
			oldEditColumnValueData.setFontHAlign(textElement.getHorizontalAlignmentValue().getName());
			
			if (TableUtil.isSortableAndFilterable(textElement)) {
				oldEditColumnValueData.setFormatPattern(textElement.getPattern());
			}
			
			applyColumnHeaderData(editColumnValueData, textElement, true);
		}
	}

	private void applyColumnHeaderData(EditColumnValueData headerData, JRDesignTextField textElement, boolean execute) {
		textElement.setFontName(headerData.getFontName());
		textElement.setFontSize(Integer.valueOf(headerData.getFontSize()));
		textElement.setBold(headerData.getFontBold());
		textElement.setItalic(headerData.getFontItalic());
		textElement.setUnderline(headerData.getFontUnderline());
		textElement.setForecolor(JRColorUtil.getColor("#" + headerData.getFontColor(), textElement.getForecolor()));
		textElement.setHorizontalAlignment(HorizontalAlignEnum.getByName(headerData.getFontHAlign()));
		
		if (TableUtil.isSortableAndFilterable(textElement)) {
			textElement.setPattern(headerData.getFormatPattern());
		}
		
		if (headerData.getFontBackColor() != null) {
			if (headerData.getFontBackColor().equalsIgnoreCase("transparent")) {
				textElement.setMode(ModeEnum.TRANSPARENT);
			} else {
				textElement.setMode(ModeEnum.OPAQUE);
				textElement.setBackcolor(JRColorUtil.getColor("#" + headerData.getFontBackColor(), Color.white));
			}
		}
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
