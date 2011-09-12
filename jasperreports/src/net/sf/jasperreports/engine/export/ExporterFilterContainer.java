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

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * An exporter filter that consists of several exporter filter, and filters
 * elements through each of them.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see #isToExport(JRPrintElement)
 */
public class ExporterFilterContainer implements ExporterFilter
{

	private final List<ExporterFilter> filters;
	
	/**
	 * Constructs a container for a list of exporter filters.
	 * 
	 * @param filters the list of filters
	 */
	public ExporterFilterContainer(List<ExporterFilter> filters)
	{
		if (filters == null)
		{
			throw new JRRuntimeException("Null filters list");
		}
		
		this.filters = filters;
	}

	/**
	 * Returns <code>true</code> if the element is not filtered by any of
	 * the contained filters.
	 */
	public boolean isToExport(JRPrintElement element)
	{
		boolean export = true;
		for (Iterator<ExporterFilter> it = filters.iterator(); it.hasNext();)
		{
			ExporterFilter filter = it.next();
			if (!filter.isToExport(element))
			{
				export = false;
				break;
			}
		}
		return export;
	}

}
