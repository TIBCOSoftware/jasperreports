/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * Java Persistence API query executer factory for EJBQL queries.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JRJpaQueryExecuter JRJpaQueryExecuter}
 * query executers. 
 * 
 * @author Marcel Overdijk (marceloverdijk@hotmail.com)
 */
public class JRJpaQueryExecuterFactory extends AbstractQueryExecuterFactory 
{

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
		PARAMETER_JPA_ENTITY_MANAGER,  "javax.persistence.EntityManager",
		PARAMETER_JPA_QUERY_HINTS_MAP, "java.util.Map"
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
	public static final String PROPERTY_JPA_QUERY_PAGE_SIZE = JRPropertiesUtil.PROPERTY_PREFIX + "ejbql.query.page.size";

	/**
	 * Property specifying the prefix for EJBQL query hints.
	 */
	public static final String PROPERTY_JPA_QUERY_HINT_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "ejbql.query.hint.";
	
	public JRQueryExecuter createQueryExecuter(
		JasperReportsContext jasperReportsContext,
		JRDataset dataset, 
		Map<String,? extends JRValueParameter> parameters
		) throws JRException 
	{
		return new JRJpaQueryExecuter(jasperReportsContext, dataset, parameters);
	}

	/**
	 * Returns <code>true</code> for all parameter types.
	 */
	public boolean supportsQueryParameterType(String className) {
		return true;
	}
}
