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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Class utilities.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public final class ClassUtils
{

	/**
	 * Instantiates a class.
	 * 
	 * <p>
	 * The class is expected to have a public no-argument constructor.
	 * 
	 * @param className the class name
	 * @param expectedType the expected (super) type of the result
	 * @return a newly created instance of the specified class
	 * @throws JRRuntimeException if the class cannot be loaded or instantiated,
	 * or if it does not implement the expected type
	 */
	public static final Object instantiateClass(String className, Class<?> expectedType)
	{
		try
		{
			Class<?> clazz = JRClassLoader.loadClassForName(className);
			if (!expectedType.isAssignableFrom(clazz))
			{
				throw new JRRuntimeException("Class " + className 
						+ " does not implement/extend " + expectedType.getName());
			}
			return clazz.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			throw new JRRuntimeException(
					"Could not load class " + className, e);
		}
		catch (InstantiationException e)
		{
			throw new JRRuntimeException(
					"Could not instantiate class " + className, e);
		}
		catch (IllegalAccessException e)
		{
			throw new JRRuntimeException(
					"Could not instantiate class " + className, e);
		}
	}
	

	private ClassUtils()
	{
	}
}
