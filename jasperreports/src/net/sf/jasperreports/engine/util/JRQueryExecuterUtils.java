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

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.query.QueryExecuterFactoryBundle;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

/**
 * Query executer utility class.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public final class JRQueryExecuterUtils
{
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
		List<QueryExecuterFactoryBundle> bundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(
				QueryExecuterFactoryBundle.class);
		for (Iterator<QueryExecuterFactoryBundle> it = bundles.iterator(); it.hasNext();)
		{
			QueryExecuterFactoryBundle bundle = it.next();
			JRQueryExecuterFactory factory = bundle.getQueryExecuterFactory(language);
			if (factory != null)
			{
				return factory;
			}
		}
		throw new JRRuntimeException("No query executer factory registered for the '" + language + "' language.");
	}
	
	
	private JRQueryExecuterUtils()
	{
	}
}
