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
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.type.ModeEnum;
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
	protected int formatIndex; 
	protected int fontIndex; 
	protected int borderIndex; 
	protected String backcolor; 
	protected String horizontalAlign;
	protected String verticalAlign;
	protected boolean isWrapText = true;
	protected boolean isHidden;
	protected boolean isLocked;

	/**
	 *
	 */
	public XlsxStyleInfo(
		int formatIndex, 
		int fontIndex, 
		int borderIndex, 
		JRExporterGridCell gridCell, 
		boolean isWrapText,
		boolean isHidden,
		boolean isLocked
		)
	{
		this.formatIndex = formatIndex;
		this.fontIndex = fontIndex;
		this.borderIndex = borderIndex;
		
		JRPrintElement element = gridCell.getElement();
		
		if (element != null && element.getModeValue() == ModeEnum.OPAQUE)
		{
			this.backcolor = JRColorUtil.getColorHexa(element.getBackcolor());
		}
		else if (gridCell.getBackcolor() != null)
		{
			this.backcolor = JRColorUtil.getColorHexa(gridCell.getBackcolor());
		}

		JRAlignment align = element instanceof JRAlignment ? (JRAlignment)element : null;
		if (align != null)
		{
			this.horizontalAlign = XlsxParagraphHelper.getHorizontalAlignment(align.getHorizontalAlignmentValue());//FIXMEXLSX use common util
			this.verticalAlign = DocxCellHelper.getVerticalAlignment(align.getVerticalAlignmentValue());//FIXMEXLSX use common util
		}
		
		this.isWrapText = isWrapText;
		this.isHidden = isHidden;
		this.isLocked = isLocked;
	}
	
	public String getId()
	{
		return 
		formatIndex + "|" + fontIndex + "|" + borderIndex + "|" + backcolor + "|" + horizontalAlign + "|" + verticalAlign 
		+ "|" + isWrapText + "|" + isHidden + "|" + isLocked;
	}
}
