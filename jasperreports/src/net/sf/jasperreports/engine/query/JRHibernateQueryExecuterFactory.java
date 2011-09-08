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
package net.sf.jasperreports.engine.query;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Query executer factory for HQL queries that uses Hibernate 3.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JRHibernateQueryExecuter JRHibernateQueryExecuter}
 * query executers. 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRHibernateQueryExecuterFactory implements JRQueryExecuterFactory
{

	/**
	 * HQL query language.
	 */
	public static final String QUERY_LANGUAGE_HQL = "hql";
	
	/**
	 * Built-in parameter holding the value of the Hibernate session to be used for creating the query.
	 */
	public final static String PARAMETER_HIBERNATE_SESSION = "HIBERNATE_SESSION";
	
	/**
	 * Built-in parameter used for collection filter queries.
	 * <p/>
	 * The value of this parameter will be used as the collection to filter using the query.
	 */
	public final static String PARAMETER_HIBERNATE_FILTER_COLLECTION = "HIBERNATE_FILTER_COLLECTION";
	
	private final static Object[] HIBERNATE_BUILTIN_PARAMETERS = {
		//passing the parameter type as class name and not class in order to 
		//avoid a dependency on Hibernate classes so that reports that have
		//HQL queries would load even when Hibernate is not present
		PARAMETER_HIBERNATE_SESSION,  "org.hibernate.Session",
		PARAMETER_HIBERNATE_FILTER_COLLECTION,  "java.lang.Object",
		};

	/**
	 * Property specifying the query execution type.
	 * <p/>
	 * Possible values are:
	 * <ul>
	 * 	<li><em>list</em> (default) - the query will be run by calling <code>org.hibernate.Query.list()</code></li>
	 * 	<li><em>iterate</em> - the query will be run by calling <code>org.hibernate.Query.iterate()</code></li>
	 * 	<li><em>scroll</em> - the query will be run by calling <code>org.hibernate.Query.scroll()</code></li>
	 * </ul>
	 */
	public static final String PROPERTY_HIBERNATE_QUERY_RUN_TYPE = JRProperties.PROPERTY_PREFIX + "hql.query.run.type";
	
	/**
	 * Property specifying the number of result rows to be retrieved at once when the execution type is <em>list</em>.
	 * <p/>
	 * Result pagination is implemented by <code>org.hibernate.Query.setFirstResult()</code> and <code>org.hibernate.Query.setMaxResults()</code>.
	 * <p/>
	 * By default, all the rows are retrieved (no result pagination is performed).
	 */
	public static final String PROPERTY_HIBERNATE_QUERY_LIST_PAGE_SIZE = JRProperties.PROPERTY_PREFIX + "hql.query.list.page.size";
	
	/**
	 * Property specifying whether hibernate session cache should be cleared between two consecutive fetches when using pagination.
	 * <p/>
	 * By default, the cache cleanup is not performed.
	 * <p/>
	 * @see net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory#PROPERTY_HIBERNATE_QUERY_LIST_PAGE_SIZE
	 */
	public static final String PROPERTY_HIBERNATE_CLEAR_CACHE = JRProperties.PROPERTY_PREFIX + "hql.clear.cache";
	
	/**
	 * Property specifying whether field descriptions should be used to determine the mapping between the fields
	 * and the query return values.
	 */
	public static final String PROPERTY_HIBERNATE_FIELD_MAPPING_DESCRIPTIONS = JRProperties.PROPERTY_PREFIX + "hql.field.mapping.descriptions";
	
	/**
	 * Value of the {@link #PROPERTY_HIBERNATE_QUERY_RUN_TYPE PROPERTY_HIBERNATE_QUERY_RUN_TYPE} property
	 * corresponding to <em>list</em> execution type.
	 */
	public static final String VALUE_HIBERNATE_QUERY_RUN_TYPE_LIST = "list"; 
	
	/**
	 * Value of the {@link #PROPERTY_HIBERNATE_QUERY_RUN_TYPE PROPERTY_HIBERNATE_QUERY_RUN_TYPE} property
	 * corresponding to <em>iterate</em> execution type.
	 */
	public static final String VALUE_HIBERNATE_QUERY_RUN_TYPE_ITERATE = "iterate"; 
	
	/**
	 * Value of the {@link #PROPERTY_HIBERNATE_QUERY_RUN_TYPE PROPERTY_HIBERNATE_QUERY_RUN_TYPE} property
	 * corresponding to <em>scroll</em> execution type.
	 */
	public static final String VALUE_HIBERNATE_QUERY_RUN_TYPE_SCROLL = "scroll";

	
	/**
	 * Returns an array containing the {@link #PARAMETER_HIBERNATE_SESSION PARAMETER_HIBERNATE_SESSION} and
	 * {@link #PARAMETER_HIBERNATE_FILTER_COLLECTION PARAMETER_HIBERNATE_FILTER_COLLECTION} parameters.
	 */
	public Object[] getBuiltinParameters()
	{
		return HIBERNATE_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map<String, ? extends JRValueParameter> parameters) throws JRException
	{
		return new JRHibernateQueryExecuter(dataset, parameters);
	}

	/**
	 * Returns <code>true</code> for all parameter types.
	 */
	public boolean supportsQueryParameterType(String className)
	{
		return true;
	}
}
