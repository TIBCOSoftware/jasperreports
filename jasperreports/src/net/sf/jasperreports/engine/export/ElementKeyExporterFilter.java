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

import java.util.Set;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * An exporter filter that excludes elements based on their keys.
 *
 * The filter uses a fixed set of elements keys to exclude.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ElementKeyExporterFilter implements ExporterFilter
{
	
	private final Set<String> excludedKeys;
	
	/**
	 * Creates a filter instance.
	 * 
	 * @param excludedKeys the set of keys to exclude
	 */
	public ElementKeyExporterFilter(Set<String> excludedKeys)
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
