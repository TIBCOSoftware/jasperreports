/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.fonts.FontFamily;

/**
 * @deprecated Replaced by {@link FontFamily#getExportFont(String)}.
 * 
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class FontKey
{
	private String fontName;
	private boolean isBold;
	private boolean isItalic;

	public FontKey(String fontName, boolean bold, boolean italic)
	{
		this.fontName = fontName;
		isBold = bold;
		isItalic = italic;
	}

	public String getFontName()
	{
		return fontName;
	}

	public boolean isBold()
	{
		return isBold;
	}

	public boolean isItalic()
	{
		return isItalic;
	}

	public boolean equals(Object o)
	{
		if (this == o) 
		{
			return true;
		}
		if (o == null || getClass() != o.getClass()) 
		{	
			return false;
		}

		final FontKey key = (FontKey) o;

		if (isBold != key.isBold) 
		{
			return false;
		}
		if (isItalic != key.isItalic) 
		{	
			return false;
		}
		if (fontName != null ? !fontName.equals(key.fontName) : key.fontName != null) 
		{
			return false;
		}

		return true;
	}

	public int hashCode()
	{
		int result;
		result = (fontName != null ? fontName.hashCode() : 0);
		result = 29 * result + (isBold ? 1 : 0);
		result = 29 * result + (isItalic ? 1 : 0);
		return result;
	}
}
