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
package net.sf.jasperreports.engine.util;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Class utilities.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class ClassUtils
{

	/**
	 * Instantiates a class.
	 * 
	 * <p>
	 * The class is expected to have a public no-argument constructor.
	 * 
	 * @param className the class name
	 * @param expectedType the expected (super) type of the result
	 * @return
	 * @throws JRRuntimeException if the class cannot be loaded or instantiated,
	 * or if it does not implement the expected type
	 */
	public static final Object instantiateClass(String className, Class expectedType)
	{
		try
		{
			Class clazz = JRClassLoader.loadClassForName(className);
			if (!expectedType.isAssignableFrom(clazz))
			{
				throw new JRRuntimeException("Class " + className 
						+ " does not implement/extend " + expectedType.getName());
			}
			Object instance = clazz.newInstance();
			return instance;
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
	
}
