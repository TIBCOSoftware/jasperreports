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
 *(at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRHtmlExporterNature implements ExporterNature
{
	
	private ExporterFilter filter = null;
	private final boolean deep;
	private final boolean ignorePageMargins;
	
	/**
	 * 
	 */
	public JRHtmlExporterNature(ExporterFilter filter, boolean deep)
	{
		this(filter, deep, false);
	}
	
	/**
	 * 
	 */
	public JRHtmlExporterNature(ExporterFilter filter, boolean deep, boolean ignorePageMargins)
	{
		this.filter = filter;
		this.deep = deep;
		this.ignorePageMargins = ignorePageMargins;
	}
	
	/**
	 * 
	 */
	public boolean isToExport(JRPrintElement element)
	{
		if (element instanceof JRGenericPrintElement)
		{
			JRGenericPrintElement genericElement = (JRGenericPrintElement) element;
			GenericElementHtmlHandler handler = (GenericElementHtmlHandler) 
			GenericElementHandlerEnviroment.getHandler(
					genericElement.getGenericType(), JRHtmlExporter.HTML_EXPORTER_KEY);
			if (!handler.toExport(genericElement))
			{
				return false;
			}
		}

		return filter == null || filter.isToExport(element);
	}
	
	/**
	 * 
	 */
	public boolean isDeep()
	{
		return deep;
	}
	
	/**
	 * 
	 */
	public boolean isSplitSharedRowSpan()
	{
		return false;
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
		return false;
	}

	public boolean isHorizontallyMergeEmptyCells()
	{
		return true;
	}

	/**
	 * Specifies whether empty page margins should be ignored
	 */
	public boolean isIgnorePageMargins()
	{
		return ignorePageMargins;
	}
	
	/**
	 *
	 */
	public boolean isBreakBeforeRow(JRPrintElement element)
	{
		return false;
	}
	
	/**
	 *
	 */
	public boolean isBreakAfterRow(JRPrintElement element)
	{
		return false;
	}
	
}
