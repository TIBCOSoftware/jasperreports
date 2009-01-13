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
package net.sf.jasperreports.engine.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSingletonCache;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRQueryExecuterUtils.java 1828 2007-08-24 13:58:43Z teodord $
 */
public class DefaultQueryExecuterFactoryBundle implements QueryExecuterFactoryBundle
{
	private static final JRSingletonCache cache = new JRSingletonCache(JRQueryExecuterFactory.class);
	
	private static final DefaultQueryExecuterFactoryBundle INSTANCE = new DefaultQueryExecuterFactoryBundle();
	
	private DefaultQueryExecuterFactoryBundle()
	{
	}
	
	/**
	 * 
	 */
	public static DefaultQueryExecuterFactoryBundle getInstance()
	{
		return INSTANCE;
	}

	/**
	 * 
	 */
	public String[] getLanguages()
	{
		List languages = new ArrayList();
		List properties = JRProperties.getProperties(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX);
		for (Iterator it = properties.iterator(); it.hasNext();)
		{
			JRProperties.PropertySuffix property = (JRProperties.PropertySuffix) it.next();
			languages.add(property.getSuffix());
		}
		return (String[])languages.toArray(new String[languages.size()]);
	}

	/**
	 * 
	 */
	public JRQueryExecuterFactory getQueryExecuterFactory(String language) throws JRException
	{
		String factoryClassName = JRProperties.getProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + language);
		if (factoryClassName == null)
		{
			throw new JRException("No query executer factory class registered for " + language + " queries.");
		}
		
		return (JRQueryExecuterFactory) cache.getCachedInstance(factoryClassName);
	}

}
