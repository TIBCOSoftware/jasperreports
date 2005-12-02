/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;

/**
 * Query executer utility class.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRQueryExecuterUtils
{
	private static final Map factoryCache = new HashMap();
	
	
	/**
	 * Returns a query executer factory for a query language.
	 * 
	 * @param language the query language
	 * @return a query executer factory
	 * @throws JRException
	 * @see JRProperties#QUERY_EXECUTER_FACTORY_PREFIX
	 */
	public static JRQueryExecuterFactory getQueryExecuterFactory(String language) throws JRException
	{
		String factoryClassName = JRProperties.getProperty(JRProperties.QUERY_EXECUTER_FACTORY_PREFIX + language);
		if (factoryClassName == null)
		{
			throw new JRException("No query executer factory class registered for " + language + " queries.  " +
					"Create a propery named " + JRProperties.QUERY_EXECUTER_FACTORY_PREFIX + language + ".");
		}
		
		JRQueryExecuterFactory queryExecuterFactory = getCachedFactory(factoryClassName);
		
		if (queryExecuterFactory == null)
		{
			try
			{
				Class queryExecuterFactoryClass = JRClassLoader.loadClassForName(factoryClassName);
				queryExecuterFactory = (JRQueryExecuterFactory) queryExecuterFactoryClass.newInstance();
			}
			catch (ClassNotFoundException e)
			{
				throw new JRException("Query executer factory class " + factoryClassName + " not found.", e);
			}
			catch (InstantiationException e)
			{
				throw new JRException("Error instantiating query executer factory " + factoryClassName + ".", e);
			}
			catch (IllegalAccessException e)
			{
				throw new JRException("Error instantiating query executer factory " + factoryClassName + ".", e);
			}
			
			cacheFactory(factoryClassName, queryExecuterFactory);
		}

		return queryExecuterFactory;
	}

	private static synchronized JRQueryExecuterFactory getCachedFactory(String factoryClassName)
	{
		return (JRQueryExecuterFactory) factoryCache.get(factoryClassName);
	}

	private static synchronized void cacheFactory(String factoryClassName, JRQueryExecuterFactory queryExecuterFactory)
	{
		factoryCache.put(factoryClassName, queryExecuterFactory);
	}
}
