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

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRProperties;

import org.hibernate.Session;

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
	 * The value of this paramter will be used as the collection to filter using the query.
	 */
	public final static String PARAMETER_HIBERNATE_FILTER_COLLECTION = "HIBERNATE_FILTER_COLLECTION";
	
	private final static Object[] HIBERNATE_BUILTIN_PARAMETERS = {
		PARAMETER_HIBERNATE_SESSION,  Session.class,
		PARAMETER_HIBERNATE_FILTER_COLLECTION,  Object.class,
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
	 * {@link #PARAMETER_HIBERNATE_FILTER_COLLECTION PARAMETER_HIBERNATE_FILTER_COLLECTION} paramters.
	 */
	public Object[] getBuiltinParameters()
	{
		return HIBERNATE_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters) throws JRException
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
