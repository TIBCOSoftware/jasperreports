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

import java.awt.Color;
import java.util.Locale;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElementUtils;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.web.commands.Command;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class EditTextElementCommand implements Command
{

	private EditTextElementData editTextElementData;
	private EditTextElementData oldEditTextElementData;
	private JRDesignTextElement textElement;
	private String oldText;
	private ReportContext reportContext;

	public EditTextElementCommand(JRDesignTextElement textElement, EditTextElementData editTextElementData, ReportContext reportContext)
	{
		this.textElement = textElement;
		this.editTextElementData = editTextElementData;
		this.reportContext = reportContext;
	}


	public void execute() {
		if (textElement != null) {
			Locale locale = (Locale)reportContext.getParameterValue(JRParameter.REPORT_LOCALE);
			if (locale == null) {
				locale = Locale.getDefault();
			}
			oldEditTextElementData = new EditTextElementData();
			oldEditTextElementData.setApplyTo(editTextElementData.getApplyTo());
			HeaderToolbarElementUtils.copyOwnTextElementStyle(oldEditTextElementData, textElement, locale);
			applyColumnHeaderData(editTextElementData, textElement, true);
		}
	}

	private void applyColumnHeaderData(EditTextElementData textElementData, JRDesignTextElement textElement, boolean execute) {
		if (EditTextElementData.APPLY_TO_HEADING.equals(textElementData.getApplyTo())) {
			if (textElement instanceof JRDesignTextField) {
				JRDesignTextField designTextField = (JRDesignTextField)textElement;
				if (execute) {
					if (oldText == null) {
						oldText = (designTextField.getExpression()).getText();
					}
					((JRDesignExpression)designTextField.getExpression()).setText("\"" + JRStringUtil.escapeJavaStringLiteral(textElementData.getHeadingName()) + "\"");
				} else {
					((JRDesignExpression)designTextField.getExpression()).setText(oldText);
				}

			} else if (textElement instanceof JRDesignStaticText){
				JRDesignStaticText staticText = (JRDesignStaticText)textElement;
				if (execute) {
					if (oldText == null) {
						oldText = staticText.getText();
					}
					staticText.setText(textElementData.getHeadingName());
				} else {
					staticText.setText(oldText);
				}
			}
		}
		
		textElement.setFontName(textElementData.getFontName());
		textElement.setFontSize(textElementData.getFontSize() != null ? textElementData.getFloatFontSize() : null);
		textElement.setBold(textElementData.getFontBold());
		textElement.setItalic(textElementData.getFontItalic());
		textElement.setUnderline(textElementData.getFontUnderline());
		textElement.setForecolor(textElementData.getFontColor() != null ? JRColorUtil.getColor("#" + textElementData.getFontColor(), textElement.getForecolor()) : null);
		textElement.setHorizontalTextAlign(HorizontalTextAlignEnum.getByName(textElementData.getFontHAlign()));
		textElement.setBackcolor(textElementData.getFontBackColor() != null ? JRColorUtil.getColor("#" + textElementData.getFontBackColor(), Color.white) : null);
		textElement.setMode(ModeEnum.getByName(textElementData.getMode()));
		
		if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
			((JRDesignTextField) textElement).setPattern(textElementData.getFormatPattern());
		}
	}


	public void undo() {
		if (oldEditTextElementData != null) {
			applyColumnHeaderData(oldEditTextElementData, textElement, false);
		}
	}


	public void redo() {
		applyColumnHeaderData(editTextElementData, textElement, true);
	}

}
