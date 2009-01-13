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
public class JRQueryExecuterUtils
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
		List bundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(QueryExecuterFactoryBundle.class);
		for (Iterator it = bundles.iterator(); it.hasNext();)
		{
			QueryExecuterFactoryBundle bundle = (QueryExecuterFactoryBundle)it.next();
			JRQueryExecuterFactory factory = bundle.getQueryExecuterFactory(language);
			if (factory != null)
			{
				return factory;
			}
		}
		throw new JRRuntimeException("No query executer factory registered for the '" + language + "' language.");
	}
}
