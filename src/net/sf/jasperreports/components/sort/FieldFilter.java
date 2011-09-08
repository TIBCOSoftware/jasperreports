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
package net.sf.jasperreports.components.sort;

import java.util.Locale;

import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.EvaluationType;
import net.sf.jasperreports.engine.fill.DatasetFillContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A dataset filter that matches String values based on substrings.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 4370 2011-06-01 13:23:46Z shertage $
 */
public class FieldFilter implements DatasetFilter
{
	
	private static final Log log = LogFactory.getLog(FieldFilter.class);

	private final String field;
	private String filterValue;
	
	private DatasetFillContext context;
	private Locale locale;

	/**
	 * Creates a field filter.
	 * 
	 * @param field the field name
	 * @param filterValue the value to search for
	 */
	public FieldFilter(String field, String filterValue)
	{
		this.field = field;
		this.filterValue = filterValue;
	}
	
	public void init(DatasetFillContext context)
	{
		this.context = context;
		this.locale = context.getLocale();
		filterValue = filterValue.toLowerCase(locale);
	}

	public boolean matches(EvaluationType evaluation)
	{
		Object value = context.getFieldValue(field, evaluation);
		if (value == null)
		{
			return false;
		}
		
		if (!(value instanceof String))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Not filtering non-String value for " + field);
			}
			return true;
		}
		
		String fieldValue = (String) value;
		fieldValue = fieldValue.toLowerCase();
		
		return fieldValue.contains(filterValue);
	}

}
