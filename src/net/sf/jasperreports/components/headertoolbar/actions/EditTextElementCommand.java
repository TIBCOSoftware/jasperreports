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

import java.awt.*;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElementUtils;
import net.sf.jasperreports.components.table.util.TableUtil;
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
public class EditTextElementCommand implements Command
{

	private EditTextElementData editTextElementData;
	private EditTextElementData oldEditTextElementData;
	private JRDesignTextElement textElement;


	public EditTextElementCommand(JRDesignTextElement textElement, EditTextElementData editTextElementData)
	{
        this.textElement = textElement;
		this.editTextElementData = editTextElementData;
	}


	public void execute() {
		if (textElement != null) {
            oldEditTextElementData = new EditTextElementData();
			HeaderToolbarElementUtils.copyOwnTextElementStyle(oldEditTextElementData, textElement);
			applyColumnHeaderData(editTextElementData, textElement);
		}
	}

	private void applyColumnHeaderData(EditTextElementData textElementData, JRDesignTextElement textElement) {
		textElement.setFontName(textElementData.getFontName());
		textElement.setFontSize(textElementData.getFontSize() != null ? Integer.valueOf(textElementData.getFontSize()) : null);
		textElement.setBold(textElementData.getFontBold());
		textElement.setItalic(textElementData.getFontItalic());
		textElement.setUnderline(textElementData.getFontUnderline());
		textElement.setForecolor(textElementData.getFontColor() != null ? JRColorUtil.getColor("#" + textElementData.getFontColor(), textElement.getForecolor()) : null);
		textElement.setHorizontalAlignment(HorizontalAlignEnum.getByName(textElementData.getFontHAlign()));
		textElement.setBackcolor(textElementData.getFontBackColor() != null ? JRColorUtil.getColor("#" + textElementData.getFontBackColor(), Color.white) : null);
		textElement.setMode(ModeEnum.getByName(textElementData.getMode()));
		
        if (textElement instanceof JRDesignTextField && TableUtil.hasSingleChunkExpression((JRDesignTextField) textElement)) {
            ((JRDesignTextField) textElement).setPattern(textElementData.getFormatPattern());
        }
	}


	public void undo() {
		if (oldEditTextElementData != null) {
			applyColumnHeaderData(oldEditTextElementData, textElement);
		}
	}


	public void redo() {
		applyColumnHeaderData(editTextElementData, textElement);
	}

}
