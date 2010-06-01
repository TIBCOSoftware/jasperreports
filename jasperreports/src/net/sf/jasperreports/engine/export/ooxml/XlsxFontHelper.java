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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.export.JRExporterGridCell;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BorderHelper.java 3135 2009-10-22 14:20:23Z teodord $
 */
public class XlsxFontHelper extends BaseHelper
{
	private Map fontCache = new HashMap();//FIXMEXLSX use soft cache? check other exporter caches as well
	

	/**
	 *
	 */
	public XlsxFontHelper(Writer writer)
	{
		super(writer);
	}
	
	/**
	 *
	 */
	public int getFont(JRExporterGridCell gridCell)
	{
		JRFont font = gridCell.getElement() instanceof JRFont ? (JRFont)gridCell.getElement() : null;
		if (font == null)
		{
			return -1;			
		}

		XlsxFontInfo fontInfo = new XlsxFontInfo(gridCell);
		Integer fontIndex = (Integer)fontCache.get(fontInfo.getId());
		if (fontIndex == null)
		{
			fontIndex = Integer.valueOf(fontCache.size());
			export(fontInfo);
			fontCache.put(fontInfo.getId(), fontIndex);
		}
		return fontIndex.intValue();
	}

	/**
	 *
	 */
	private void export(XlsxFontInfo fontInfo)
	{
		write(
			"<font><sz val=\"" + fontInfo.fontSize + "\"/>" 
			+ "<color rgb=\"" + fontInfo.color + "\"/>"
			+ "<name val=\"" + fontInfo.fontName + "\"/>"//FIXMEXLSX use font mappings here
			+ "<b val=\"" + fontInfo.isBold + "\"/>"
			+ "<i val=\"" + fontInfo.isItalic + "\"/>"
			+ "<u val=\"" + (fontInfo.isUnderline ? "single" : "none") + "\"/>"
			+ "<strike val=\"" + fontInfo.isStrikeThrough + "\"/>"
			+ "<family val=\"2\"/></font>\n"
			);
	}

}
