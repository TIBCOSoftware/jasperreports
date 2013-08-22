/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.web.actions.ActionException;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class EditColumnHeaderAction extends AbstractVerifiableTableAction {

	public EditColumnHeaderAction() {
	}
	
	public void setEditColumnHeaderData(EditColumnHeaderData editColumnHeaderData) {
		columnData = editColumnHeaderData;
	}

	public EditColumnHeaderData getEditColumnHeaderData() {
		return (EditColumnHeaderData)columnData;
	}

	public String getName() {
		return "edit_column_header_action";
	}

	public void performAction() throws ActionException {
		// execute command
		try {
			getCommandStack().execute(
				new ResetInCacheCommand(
					new EditColumnHeaderCommand(getTargetTextField(), getEditColumnHeaderData()),
					getJasperReportsContext(), 
					getReportContext(), 
					targetUri
					)
				);
		} catch (CommandException e) {
			throw new ActionException(e.getMessage());
		}
	}

    private JRDesignTextElement getTargetTextField() {
        EditColumnHeaderData colValData = getEditColumnHeaderData();
        JRDesignTextElement result = null;

        if ("headings".equals(colValData.getApplyTo())) {
            List<BaseColumn> tableColumns = TableUtil.getAllColumns(table);
            StandardColumn column = (StandardColumn) tableColumns.get(colValData.getColumnIndex());
            result = TableUtil.getColumnHeaderTextElement(column);
        } else if("groupheading".equals(colValData.getApplyTo())) {
            List<ColumnGroup> lst = TableUtil.getAllColumnGroups(table.getColumns());

            ColumnGroup colGroup = lst.get(colValData.getI());
            Cell cell;

            if (colValData.getGroupName() != null && colValData.getGroupName().length() > 0) {
                cell = colGroup.getGroupHeader(colValData.getGroupName());
            } else {
                cell = colGroup.getGroupHeaders().get(colValData.getJ()).getCell();
            }

            result = TableUtil.getCellTextElement(cell, false);
        }

        return result;
    }

	@Override
	public void verify() throws ActionException {
		EditColumnHeaderData editColumnHeaderData = getEditColumnHeaderData();
		if (editColumnHeaderData.getFontSize() != null) {
			try {
				Integer.valueOf(editColumnHeaderData.getFontSize());
			} catch (NumberFormatException e) {
				errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.edit.column.header.invalid.font.size", editColumnHeaderData.getFontSize());
			}
		}
	}

}
