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
package net.sf.jasperreports.engine.query;

import java.util.Map;

import javax.persistence.EntityManager;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Java Persistence API query executer factory for EJBQL queries.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JRJpaQueryExecuter JRJpaQueryExecuter}
 * query executers. 
 * 
 * @author Marcel Overdijk (marceloverdijk@hotmail.com)
 * @version $Id$
 */
public class JRJpaQueryExecuterFactory implements JRQueryExecuterFactory {

	/**
	 * EJBQL query language.
	 */
	public static final String QUERY_LANGUAGE_EJBQL = "ejbql";

	/**
	 * Built-in parameter holding the value of the <code>javax.persistence.EntityManager</code> to be used for creating the query.
	 */
	public static final String PARAMETER_JPA_ENTITY_MANAGER = "JPA_ENTITY_MANAGER";	

	/**
	 * Built-in parameter (optional) holding the value of the query hints map.
	 * Each named/value pair will be set as query hint against the query.
	 */
	public static final String PARAMETER_JPA_QUERY_HINTS_MAP = "JPA_QUERY_HINTS_MAP";	
	
	
	private static final Object[] JPA_BUILTIN_PARAMETERS = {
		PARAMETER_JPA_ENTITY_MANAGER,  EntityManager.class,
		PARAMETER_JPA_QUERY_HINTS_MAP, Map.class
	};	
	
	public Object[] getBuiltinParameters() {
		return JPA_BUILTIN_PARAMETERS;
	}

	/**
	 * Property specifying the number of result rows to be retrieved at once.
	 * <p/>
	 * Result pagination is implemented by <code>javax.persistence.Query.setFirstResult()</code> and <code>javax.persistence.Query.setMaxResults()</code>.
	 * <p/>
	 * By default, all the rows are retrieved (no result pagination is performed).
	 */
	public static final String PROPERTY_JPA_QUERY_PAGE_SIZE = JRProperties.PROPERTY_PREFIX + "ejbql.query.page.size";

	/**
	 * Property specifying the prefix for EJBQL query hints.
	 */
	public static final String PROPERTY_JPA_QUERY_HINT_PREFIX = JRProperties.PROPERTY_PREFIX + "ejbql.query.hint.";
	
	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters) throws JRException {
		return new JRJpaQueryExecuter(dataset, parameters);
	}

	/**
	 * Returns <code>true</code> for all parameter types.
	 */
	public boolean supportsQueryParameterType(String className) {
		return true;
	}
}
