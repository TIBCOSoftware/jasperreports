/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.compilers;

import net.sf.jasperreports.engine.util.ObjectUtils;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class GroovyFunctionKey
{

	private final String functionName;
	private final Class<?>[] argumentTypes;
	
	public GroovyFunctionKey(String functionName, Class<?>[] argumentTypes)
	{
		this.functionName = functionName;
		this.argumentTypes = argumentTypes;
	}

	@Override
	public int hashCode()
	{
		int hash = functionName.hashCode();
		for (Class<?> type : argumentTypes)
		{
			hash = 67 * hash + (type == null ? 0 : type.hashCode());
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof GroovyFunctionKey))
		{
			return false;
		}
		
		GroovyFunctionKey key = (GroovyFunctionKey) obj;
		if (!functionName.equals(key.functionName) || argumentTypes.length != key.argumentTypes.length)
		{
			return false;
		}
		
		for (int i = 0; i < argumentTypes.length; i++)
		{
			if (!ObjectUtils.equals(argumentTypes[i], key.argumentTypes[i]))
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();
		string.append(functionName);
		string.append('(');
		
		boolean first = true;
		for (Class<?> type : argumentTypes)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				string.append(',');
			}
			
			string.append(type == null ? "null" : type.getName());
		}
		string.append(')');
		return string.toString();
	}
	
}
