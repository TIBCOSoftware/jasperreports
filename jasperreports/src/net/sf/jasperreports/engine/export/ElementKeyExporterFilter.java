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

import java.util.Set;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * An exporter filter that excludes elements based on their keys.
 *
 * The filter uses a fixed set of elements keys to exclude.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class ElementKeyExporterFilter implements ExporterFilter
{
	
	private final Set excludedKeys;
	
	/**
	 * Creates a filter instance.
	 * 
	 * @param excludedKeys the set of keys to exclude
	 */
	public ElementKeyExporterFilter(Set excludedKeys)
	{
		if (excludedKeys == null)
		{
			throw new JRRuntimeException("The excluded keys set is null");
		}
		
		this.excludedKeys = excludedKeys;
	}

	/**
	 * Excludes elements whose keys match any of the excluded keys.
	 * 
	 * Elements with no keys are <u>not</u> excluded.
	 */
	public boolean isToExport(JRPrintElement element)
	{
		return element.getKey() == null
				|| !excludedKeys.contains(element.getKey());
	}

}
