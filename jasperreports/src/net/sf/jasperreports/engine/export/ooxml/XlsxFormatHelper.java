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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BorderHelper.java 3135 2009-10-22 14:20:23Z teodord $
 */
public class XlsxFormatHelper extends BaseHelper
{
	private Map formatCache = new HashMap();//FIXMEXLSX use soft cache? check other exporter caches as well

	/**
	 *
	 */
	public XlsxFormatHelper(Writer writer)
	{
		super(writer);
	}
	
	/**
	 *
	 */
	public int getFormat(String pattern)
	{
		if (pattern == null)
		{
			return -1;			
		}
		
		XlsxFormatInfo formatInfo = new XlsxFormatInfo(pattern);
		Integer formatIndex = (Integer)formatCache.get(formatInfo.getId());
		if (formatIndex == null)
		{
			formatIndex = Integer.valueOf(formatCache.size());
			export(formatInfo, formatIndex);
			formatCache.put(formatInfo.getId(), formatIndex);
		}
		return formatIndex.intValue();
	}

	/**
	 *
	 */
	private void export(XlsxFormatInfo formatInfo, Integer formatIndex)
	{
		write("<numFmt numFmtId=\"" + (formatIndex + 1) + "\"");
		if (formatInfo.pattern != null && formatInfo.pattern.trim().length() > 0)
		{
			write(" formatCode=\"" + formatInfo.pattern + "\"");
		}
		else
		{
			write(" formatCode=\"General\"");
		}
		write("/>\n");
	}

}
