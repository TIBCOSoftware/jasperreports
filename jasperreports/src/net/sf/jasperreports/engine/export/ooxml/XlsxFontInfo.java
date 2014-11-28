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
package net.sf.jasperreports.engine.export.ooxml;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class XlsxFontInfo
{
	/**
	 *
	 */
	protected String fontName;
	protected float fontSize;
	protected boolean isBold;
	protected boolean isItalic;
	protected boolean isUnderline;
	protected boolean isStrikeThrough;
	protected String color;

	/**
	 *
	 */
	public XlsxFontInfo(JRExporterGridCell gridCell, String fontName, boolean isFontSizeFixEnabled)
	{
		JRPrintElement element = gridCell.getElement();

		if (element != null)
		{
			this.color = JRColorUtil.getColorHexa(element.getForecolor());
		}
		
		JRFont font = element instanceof JRFont ? (JRFont)element : null;
		if (font != null)
		{
			this.fontName = fontName;
			this.fontSize = font.getFontsize() + (isFontSizeFixEnabled ? -1 : 0);
			this.isBold = font.isBold();
			this.isItalic = font.isItalic();
			this.isUnderline = font.isUnderline();
			this.isStrikeThrough = font.isStrikeThrough();
		}
	}
	
	public String getId()
	{
		return 
			fontName + "|" + fontSize
			+ "|" + isBold + "|"+ isItalic 
			+ "|" + isUnderline + "|"+ isStrikeThrough
			+ "|" + color; 
	}
}
