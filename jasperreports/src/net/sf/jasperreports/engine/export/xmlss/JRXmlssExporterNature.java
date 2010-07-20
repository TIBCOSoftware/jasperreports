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

package net.sf.jasperreports.engine.export.xmlss;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlssExporterNature implements ExporterNature
{
	
	public static final String PROPERTY_BREAK_BEFORE_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.break.before.row";
	public static final String PROPERTY_BREAK_AFTER_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.break.after.row";

	private ExporterFilter filter;
	private boolean isIgnorePageMargins;

	/**
	 * 
	 */
	public JRXmlssExporterNature(ExporterFilter filter)
	{
		this.filter = filter;
	}
	
	/**
	 * 
	 */
	protected JRXmlssExporterNature(ExporterFilter filter, boolean isIgnorePageMargins)
	{
		this.filter = filter;
		this.isIgnorePageMargins = isIgnorePageMargins;
	}

	/**
	 * 
	 */
	public boolean isToExport(JRPrintElement element)
	{
		return !(element instanceof JRGenericPrintElement)
			&& (element instanceof JRPrintText || element instanceof JRPrintFrame)
			&& (filter == null || filter.isToExport(element));
	}
	
	/**
	 * 
	 */
	public boolean isDeep(JRPrintFrame frame)
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isSplitSharedRowSpan()
	{
		return true;
	}

	/**
	 * 
	 */
	public boolean isSpanCells()
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isIgnoreLastRow()
	{
		return true;
	}

	public boolean isHorizontallyMergeEmptyCells()
	{
		return false;
	}

	/**
	 * Specifies whether empty page margins should be ignored
	 */
	public boolean isIgnorePageMargins()
	{
		return isIgnorePageMargins;
	}

	/**
	 *
	 */
	public boolean isBreakBeforeRow(JRPrintElement element)
	{
		return Boolean.valueOf(element.getPropertiesMap().getProperty(PROPERTY_BREAK_BEFORE_ROW)).booleanValue();
	}
	
	/**
	 *
	 */
	public boolean isBreakAfterRow(JRPrintElement element)
	{
		return Boolean.valueOf(element.getPropertiesMap().getProperty(PROPERTY_BREAK_AFTER_ROW)).booleanValue();
	}
	
}
