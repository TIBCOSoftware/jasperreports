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
package net.sf.jasperreports.engine.util;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ValuePropertiesWrapper implements Comparable<ValuePropertiesWrapper>
{

	private final Object value;
	private final Object[] propertyValues;
	
	public ValuePropertiesWrapper(Object value, Object[] propertyValues)
	{
		this.value = value;
		this.propertyValues = propertyValues;
	}

	@Override
	public int hashCode()
	{
		return value == null ? 0 : value.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ValuePropertiesWrapper))
		{
			return false;
		}
		
		ValuePropertiesWrapper wrapper = (ValuePropertiesWrapper) obj;
		return (value == null) ? (wrapper.value == null) : (wrapper.value != null && value.equals(wrapper.value));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int compareTo(ValuePropertiesWrapper o)
	{
		return ((Comparable) value).compareTo(o.value);
	}

	public Object getValue()
	{
		return value;
	}

	public Object[] getPropertyValues()
	{
		return propertyValues;
	}

}
