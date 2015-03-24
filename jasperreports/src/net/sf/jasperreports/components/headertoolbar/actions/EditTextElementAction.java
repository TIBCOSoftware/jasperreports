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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElementUtils;
import net.sf.jasperreports.components.sort.FilterTypesEnum;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.actions.ActionException;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class EditTextElementAction extends AbstractVerifiableTableAction {

	public EditTextElementAction() {
	}
	
	public void setEditTextElementData(EditTextElementData editTextElementData) {
		columnData = editTextElementData;
	}

	public EditTextElementData getEditTextElementData() {
		return (EditTextElementData)columnData;
	}

	public void performAction() throws ActionException {
		// execute command
		try {
			getCommandStack().execute(
				new ResetInCacheCommand(
					new EditTextElementCommand(getTargetTextElement(), getEditTextElementData(), getReportContext()),
					getJasperReportsContext(), 
					getReportContext(), 
					targetUri
					)
				);
		} catch (CommandException e) {
			throw new ActionException(e);
		}
	}

	private JRDesignTextElement getTargetTextElement() {
		EditTextElementData textElementData = getEditTextElementData();
		List<BaseColumn> allCols = TableUtil.getAllColumns(table);
		StandardColumn col = (StandardColumn)allCols.get(textElementData.getColumnIndex());
		JRDesignTextElement result = null;

		if (EditTextElementData.APPLY_TO_DETAIL_ROWS.equals(textElementData.getApplyTo())) {
			result = TableUtil.getCellElement(JRDesignTextElement.class, col.getDetailCell(), true);
		} else if (EditTextElementData.APPLY_TO_GROUP_SUBTOTAL.equals(textElementData.getApplyTo())) {
			result = TableUtil.getCellElement(JRDesignTextElement.class, col, TableUtil.COLUMN_GROUP_FOOTER, textElementData.getGroupName(), table);
		} else if (EditTextElementData.APPLY_TO_HEADING.equals(textElementData.getApplyTo())) {
			result = TableUtil.getCellElement(JRDesignTextElement.class, col.getColumnHeader(), true);
		} else if (EditTextElementData.APPLY_TO_GROUPHEADING.equals(textElementData.getApplyTo())) {
			result = TableUtil.getCellElement(JRDesignTextElement.class, col, TableUtil.COLUMN_GROUP_HEADER, textElementData.getGroupName(), table);
		} else if (EditTextElementData.APPLY_TO_TABLE_TOTAL.equals(textElementData.getApplyTo())) {
			result = TableUtil.getCellElement(JRDesignTextElement.class, col, TableUtil.TABLE_FOOTER, null, table);
		}

		return result;
	}

	@Override
	public void verify() throws ActionException {
		EditTextElementData colValData = getEditTextElementData();

		Locale locale = (Locale)getReportContext().getParameterValue(JRParameter.REPORT_LOCALE);
		if (locale == null) {
			locale = Locale.getDefault();
		}

		if (colValData.getFontSize() != null) {
			try {
				NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
				ParsePosition pp = new ParsePosition(0);
				Number formattedNumber = numberFormat.parse(colValData.getFontSize(), pp);

				if (formattedNumber != null && pp.getIndex() == colValData.getFontSize().length()) {
					colValData.setFloatFontSize(formattedNumber.floatValue());
				} else {
					errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.edit.values.invalid.font.size", colValData.getFontSize());
				}

			} catch (NumberFormatException e) {
				errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.edit.values.invalid.font.size", colValData.getFontSize());
			}
		}
		JRDesignTextElement textField = getTargetTextElement();

		if (textField instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textField)) {
			JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getDatasetRun();
			String datasetName = datasetRun.getDatasetName();
			JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());
			JasperDesign jasperDesign = cache.getJasperDesign(targetUri);
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getDatasetMap().get(datasetName);

			String textFieldName = ((JRDesignTextField) textField).getExpression().getChunks()[0].getText();
			FilterTypesEnum filterType = null;

			for (JRField field: dataset.getFields()) {
				if (textFieldName.equals(field.getName())) {
					filterType = HeaderToolbarElementUtils.getFilterType(field.getValueClass());
					break;
				}
			}

			if (filterType != null) {
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
