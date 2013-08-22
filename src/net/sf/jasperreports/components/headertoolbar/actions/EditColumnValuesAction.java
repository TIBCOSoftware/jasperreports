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
import java.util.Locale;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElementUtils;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.actions.ActionException;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class EditColumnValuesAction extends AbstractVerifiableTableAction {

	public EditColumnValuesAction() {
	}
	
	public void setEditColumnValueData(EditColumnValueData editColumnValueData) {
		columnData = editColumnValueData;
	}

	public EditColumnValueData getEditColumnValueData() {
		return (EditColumnValueData)columnData;
	}

	public void performAction() throws ActionException {
		// execute command
		try {
			getCommandStack().execute(
				new ResetInCacheCommand(
					new EditColumnValuesCommand(getTargetTextField(), getEditColumnValueData()),
					getJasperReportsContext(), 
					getReportContext(), 
					targetUri
					)
				);
		} catch (CommandException e) {
			throw new ActionException(e.getMessage());
		}
	}

    private JRTextField getTargetTextField() {
        EditColumnValueData colValData = getEditColumnValueData();
        JRTextField result = null;

        if ("detailrows".equals(colValData.getApplyTo())) {
            List<BaseColumn> allCols = TableUtil.getAllColumns(table);
            StandardColumn col = (StandardColumn)allCols.get(colValData.getColumnIndex());
            result = TableUtil.getColumnDetailTextElement(col);
        } else if("groupsubtotal".equals(colValData.getApplyTo())) {
            List<ColumnGroup> lst = TableUtil.getAllColumnGroups(table.getColumns());
            ColumnGroup colGroup = lst.get(colValData.getI());
            Cell cell;

            if (colValData.getGroupName() != null && colValData.getGroupName().length() > 0) {
                cell = colGroup.getGroupFooter(colValData.getGroupName());
            } else {
                cell = colGroup.getGroupFooters().get(colValData.getJ()).getCell();
            }

            result =  TableUtil.getCellDetailTextElement(cell, false);
        }

        return result;
    }

	@Override
	public void verify() throws ActionException {
		EditColumnValueData colValData = getEditColumnValueData();
		
		if (colValData.getFontSize() != null) {
			try {
				Integer.valueOf(colValData.getFontSize());
			} catch (NumberFormatException e) {
				errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.edit.values.invalid.font.size", colValData.getFontSize());
			}
		}
		JRTextField textField = getTargetTextField();

		if (TableUtil.hasSingleChunkExpression(textField)) {
			JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getDatasetRun();
			String datasetName = datasetRun.getDatasetName();
			JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());
			JasperDesign jasperDesign = cache.getJasperDesign(targetUri);
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getDatasetMap().get(datasetName);
			
			String textFieldName = textField.getExpression().getChunks()[0].getText();
			FilterTypesEnum filterType = null;
			
			for (JRField field: dataset.getFields()) {
				if (textFieldName.equals(field.getName())) {
					filterType = HeaderToolbarElementUtils.getFilterType(field.getValueClass());
					break;
				}
			}
			
			if (filterType != null) {
				Locale locale = (Locale)getReportContext().getParameterValue(JRParameter.REPORT_LOCALE);
				if (locale == null) {
					locale = Locale.getDefault();
				}
				
				if (filterType.equals(FilterTypesEnum.DATE)) {
					try {
						formatFactory.createDateFormat(colValData.getFormatPattern(), locale, null);
					} catch (IllegalArgumentException e){
						errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.edit.column.values.invalid.date.pattern", new Object[] {colValData.getFormatPattern()});
					}
				} else if (filterType.equals(FilterTypesEnum.NUMERIC)) {
					try {
						formatFactory.createNumberFormat(colValData.getFormatPattern(), locale);
					} catch (IllegalArgumentException e){
						errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.edit.column.values.invalid.number.pattern", new Object[] {colValData.getFormatPattern()});
					}
				}
			}
		}
	}

}
