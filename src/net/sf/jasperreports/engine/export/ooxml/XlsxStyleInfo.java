/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.util.JRColorUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BorderHelper.java 3135 2009-10-22 14:20:23Z teodord $
 */
public class XlsxStyleInfo
{
	/**
	 *
	 */
	protected String forecolor = null; 
	protected String backcolor = null; 
	protected String fontName = null;
	protected int fontSize = 0;
	protected boolean isBold = false;
	protected boolean isItalic = false;
	protected boolean isUnderline = false;
	protected boolean isStrikeThrough = false;
	protected String horizontalAlign = null;
	protected String verticalAlign = null;

	/**
	 *
	 */
	public XlsxStyleInfo(JRExporterGridCell gridCell)
	{
		JRPrintElement element = gridCell.getElement();
		
		if (element != null)
		{
			this.forecolor = JRColorUtil.getColorHexa(element.getForecolor());
			this.backcolor = JRColorUtil.getColorHexa(element.getBackcolor());
		}

		JRFont font = element instanceof JRFont ? (JRFont)element : null;
		if (font != null)
		{
			this.fontName = font.getFontName();
			this.fontSize = font.getFontSize();
			this.isBold = font.isBold();
			this.isItalic = font.isItalic();
			this.isUnderline = font.isUnderline();
			this.isStrikeThrough = font.isStrikeThrough();
		}

		JRAlignment align = element instanceof JRAlignment ? (JRAlignment)element : null;
		if (align != null)
		{
			this.horizontalAlign = XlsxParagraphHelper.getHorizontalAlignment(new Byte(align.getHorizontalAlignment()));//FIXMEXLSX use common util
			this.verticalAlign = DocxCellHelper.getVerticalAlignment(new Byte(align.getVerticalAlignment()));//FIXMEXLSX use common util
		}
	}
	
	public String getId()
	{
		return 
			forecolor + "|" + backcolor 
			+ "|" + fontName + "|" + fontSize
			+ "|" + isBold + "|"+ isItalic 
			+ "|" + isUnderline + "|"+ isStrikeThrough 
			+ "|" + horizontalAlign + "|" + verticalAlign;
	}
}
