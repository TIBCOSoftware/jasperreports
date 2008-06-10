/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
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
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 * @see #isToExport(JRPrintElement)
 */
public class ExporterFilterContainer implements ExporterFilter
{

	private final List filters;
	
	/**
	 * Constructs a container for a list of exporter filters.
	 * 
	 * @param filters the list of filters
	 */
	public ExporterFilterContainer(List filters)
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
		for (Iterator it = filters.iterator(); it.hasNext();)
		{
			ExporterFilter filter = (ExporterFilter) it.next();
			if (!filter.isToExport(element))
			{
				export = false;
				break;
			}
		}
		return export;
	}

}
