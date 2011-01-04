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
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.util.JRFontUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BorderHelper.java 3135 2009-10-22 14:20:23Z teodord $
 */
public class XlsxFontHelper extends BaseHelper
{
	private Map fontCache = new HashMap();//FIXMEXLSX use soft cache? check other exporter caches as well
	
	private Map fontMap;
	private String exporterKey;
	private boolean isFontSizeFixEnabled;

	/**
	 *
	 */
	public XlsxFontHelper(
		Writer writer,
		Map fontMap,
		String exporterKey,
		boolean isFontSizeFixEnabled		
		)
	{
		super(writer);

		this.fontMap = fontMap;
		this.exporterKey = exporterKey;
		this.isFontSizeFixEnabled = isFontSizeFixEnabled;
	}
	
	/**
	 *
	 */
	public int getFont(JRExporterGridCell gridCell, Locale locale)
	{
		JRFont font = gridCell.getElement() instanceof JRFont ? (JRFont)gridCell.getElement() : null;
		if (font == null)
		{
			return -1;			
		}

		String fontName = font.getFontName();
		if (fontMap != null && fontMap.containsKey(fontName))
		{
			fontName = (String) fontMap.get(fontName);
		}
		else
		{
			FontInfo fontInfo = JRFontUtil.getFontInfo(fontName, locale);
			if (fontInfo != null)
			{
				//fontName found in font extensions
				FontFamily family = fontInfo.getFontFamily();
				String exportFont = family.getExportFont(exporterKey);
				if (exportFont != null)
				{
					fontName = exportFont;
				}
			}
		}
		
		XlsxFontInfo fontInfo = new XlsxFontInfo(gridCell, fontName);
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
			"<font><sz val=\"" + (isFontSizeFixEnabled ? fontInfo.fontSize - 1 : fontInfo.fontSize) + "\"/>" 
			+ "<color rgb=\"" + fontInfo.color + "\"/>"
			+ "<name val=\"" + fontInfo.fontName + "\"/>"
			+ "<b val=\"" + fontInfo.isBold + "\"/>"
			+ "<i val=\"" + fontInfo.isItalic + "\"/>"
			+ "<u val=\"" + (fontInfo.isUnderline ? "single" : "none") + "\"/>"
			+ "<strike val=\"" + fontInfo.isStrikeThrough + "\"/>"
			+ "<family val=\"2\"/></font>\n"
			);
	}

}
