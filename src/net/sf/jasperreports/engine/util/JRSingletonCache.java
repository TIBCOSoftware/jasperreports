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

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections.ReferenceMap;


/**
 * Utility to use as a soft cache of singleton instances.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRSingletonCache
{
	private final ReferenceMap cache;
	private final Class itf;

	/**
	 * Creates a cache of singleton instances.
	 * 
	 * @param itf a interface or class that should be implemented by all classes cached by this object
	 */
	public JRSingletonCache(Class itf)
	{
		cache = new ReferenceMap();
		this.itf = itf;
	}

	/**
	 * Returns the singleton instance corresponding to a class.
	 * <p>
	 * The instance is first searched into the cache and created if not found.
	 * <p>
	 * The class is expected to have a no-argument constructor.
	 * 
	 * @param className
	 * @return
	 * @throws JRException
	 */
	public synchronized Object getCachedInstance(String className) throws JRException
	{
		Object instance = cache.get(className);
		if (instance == null)
		{
			try
			{
				Class clazz = JRClassLoader.loadClassForName(className);
				if (itf != null && !itf.isAssignableFrom(clazz))
				{
					throw new JRException("Class \"" + className + "\" should be compatible with \"" + itf.getName() + "\"");
				}

				instance = clazz.newInstance();
			}
			catch (ClassNotFoundException e)
			{
				throw new JRException("Class " + className + " not found.", e);
			}
			catch (InstantiationException e)
			{
				throw new JRException("Error instantiating class " + className + ".", e);
			}
			catch (IllegalAccessException e)
			{
				throw new JRException("Error instantiating class " + className + ".", e);
			}
			
			cache.put(className, instance);
		}
		return instance;
	}
}
