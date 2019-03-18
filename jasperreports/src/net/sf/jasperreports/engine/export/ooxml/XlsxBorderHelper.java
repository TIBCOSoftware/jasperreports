/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.type.LineDirectionEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class XlsxBorderHelper extends BaseHelper
{
	private Map<String,Integer> borderCache = new HashMap<String,Integer>();//FIXMEXLSX use soft cache? check other exporter caches as well
	
	/**
	 *
	 */
	public XlsxBorderHelper(JasperReportsContext jasperReportsContext, Writer writer)
	{
		super(jasperReportsContext, writer);
	}
	
	/**
	 *
	 */
	public int getBorder(JRExporterGridCell gridCell, JRXlsAbstractExporter.SheetInfo sheetInfo, LineDirectionEnum direction)
	{
		if (Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) || gridCell.getBox() == null)
		{
			return -1;			
		}

		XlsxBorderInfo borderInfo = new XlsxBorderInfo(gridCell.getBox(), direction);
		Integer borderIndex = borderCache.get(borderInfo.getId());
		if (borderIndex == null)
		{
			borderIndex = borderCache.size();
			export(borderInfo);
			borderCache.put(borderInfo.getId(), borderIndex);
		}
		return borderIndex;
	}

	/**
	 *
	 */
	public void export(JRLineBox box)
	{
		if (box != null)
		{
			export(new XlsxBorderInfo(box));
		}
	}

	/**
	 *
	 */
	public void export(JRPen pen)
	{
		if (pen != null)
		{
			export(new XlsxBorderInfo(pen));
		}
	}

	/**
	 *
	 */
	private void export(XlsxBorderInfo info)
	{
//		if(info.hasBorder())
		{
			write("<border");
			if(info.getDirection() != null)
			{
				write(info.getDirection().equals(LineDirectionEnum.TOP_DOWN) ? " diagonalDown=\"1\"" : " diagonalUp=\"1\"");
			}
			write(">");
			exportBorder(info, XlsxBorderInfo.LEFT_BORDER);
			exportBorder(info, XlsxBorderInfo.RIGHT_BORDER);
			exportBorder(info, XlsxBorderInfo.TOP_BORDER);
			exportBorder(info, XlsxBorderInfo.BOTTOM_BORDER);
			exportBorder(info, XlsxBorderInfo.DIAGONAL_BORDER);
			write("</border>\n");
		}
//		
//		write("      <w:tcMar>\n");
//		exportPadding(info, BorderInfo.TOP_BORDER);
//		exportPadding(info, BorderInfo.LEFT_BORDER);
//		exportPadding(info, BorderInfo.BOTTOM_BORDER);
//		exportPadding(info, BorderInfo.RIGHT_BORDER);
//		write("      </w:tcMar>\n");
	}

	/**
	 *
	 */
	private void exportBorder(XlsxBorderInfo info, int side)
	{
		write("<" + XlsxBorderInfo.BORDER[side]);
  		if (info.borderStyle[side] != null)
		{
  			write(" style=\"" + info.borderStyle[side] + "\"");
		}
		write(">");
		if (info.borderColor[side] != null)	//FIXMEDOCX check this; use default color?
		{
			write("<color rgb=\"" + info.borderColor[side] + "\"/>");
		}
		write("</" + XlsxBorderInfo.BORDER[side] + ">");
	}
	
//	/**
//	 *
//	 */
//	private void exportPadding(XlsxBorderInfo info, int side)//FIXMEXLSX padding
//	{
//		if (info.borderPadding[side] != null)
//		{
//			write("       <w:" + XlsxBorderInfo.BORDER[side] +" w:w=\"" + info.borderPadding[side] + "\" w:type=\"dxa\" />\n");
//		}
//	}

}
