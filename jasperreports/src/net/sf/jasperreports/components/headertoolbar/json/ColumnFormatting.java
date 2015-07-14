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
package net.sf.jasperreports.components.headertoolbar.json;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.Pair;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ColumnFormatting implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String CONTEXT_PARAMETER_COLUMN_FORMATTING = "net.sf.jasperreports.headertoolbar.column.formatting";

	public static void store(ReportContext reportContext, String tableUUID, int columnIndex, 
			Locale locale, TimeZone timeZone)
	{
		// storing locale and timezone per column to be used in ConditionalFormattingAction.
		// we need to do that because, unlike for filters, conditional formatting action json does not contain the locale and timezone.
		//FIXME for now this is never cleared, we need to find a place do do that.
		Map<Pair<String, Integer>, ColumnFormatting> columnFormattingMap = (Map<Pair<String, Integer>, ColumnFormatting>) 
				reportContext.getParameterValue(CONTEXT_PARAMETER_COLUMN_FORMATTING);
		if (columnFormattingMap == null)
		{
			//FIXME concurrency?
			columnFormattingMap = new ConcurrentHashMap<Pair<String,Integer>, ColumnFormatting>();
			reportContext.setParameterValue(CONTEXT_PARAMETER_COLUMN_FORMATTING, columnFormattingMap);
		}
		
		ColumnFormatting columnFormatting = new ColumnFormatting(
				JRDataUtils.getLocaleCode(locale), JRDataUtils.getTimeZoneId(timeZone));
		columnFormattingMap.put(new Pair<String, Integer>(tableUUID, columnIndex), columnFormatting);
	}
	
	public static ColumnFormatting get(ReportContext reportContext, String tableUUID, int columnIndex)
	{
		Map<Pair<String, Integer>, ColumnFormatting> columnFormattingMap = (Map<Pair<String, Integer>, ColumnFormatting>) 
				reportContext.getParameterValue(CONTEXT_PARAMETER_COLUMN_FORMATTING);
		ColumnFormatting columnFormatting = columnFormattingMap == null ? null
				: columnFormattingMap.get(new Pair<String, Integer>(tableUUID, columnIndex));
		return columnFormatting;
	}
	
	private final String localeCode;
	private final String timeZoneId;
	
	public ColumnFormatting(String localeCode, String timeZoneId)
	{
		this.localeCode = localeCode;
		this.timeZoneId = timeZoneId;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public String getTimeZoneId()
	{
		return timeZoneId;
	}

}
