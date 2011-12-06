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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.data.JRJpaDataSource;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * EJBQL query executer that uses the Java Persistence API.
 * <p/>
 * To use EJBQL in queries, an <code>javax.persistence.EntityManager</code> is needed.
 * When running or filling reports the <code>em</code> need to be supplied with the named parameter {@link net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory#PARAMETER_JPA_ENTITY_MANAGER}.
 * <p/>
 * Example:
 * <code>
 * <pre>
 * Map parameters = new HashMap();
 * EntityManager em = emf.createEntityManager();
 * parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
 * JasperRunManager.runReportToPdfFile(fileName, parameters);
 * </pre>
 * </code>
 * <p/>
 * When dealing with large result sets, pagination can be used by setting the {@link net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory#PROPERTY_JPA_QUERY_PAGE_SIZE} property in the report template.
 * <p/>
 * Example:
 * <code>
 * <pre>
 * &lt;property name="net.sf.jasperreports.ejbql.query.page.size" value="100"/&gt;
 * </pre>
 * </code>
 * <p/>
 * Implementation-specific query hints can be set either using report properties in the report template,
 * or by supplying the named parameter {@link net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory#PARAMETER_JPA_QUERY_HINTS_MAP}
 * containing a <code>java.util.Map</code> with named/value query hints.
 * <p/>
 * Example using report property:
 * <code>
 * <pre>
 * &lt;property name="net.sf.jasperreports.ejbql.query.hint.fetchSize" value="100"/&gt;
 * </pre>
 * </code>
 * The name of the query hint need to be prefixed with <code>net.sf.jasperreports.ejbql.query.hint.</code> ({@link net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory#PROPERTY_JPA_QUERY_HINT_PREFIX}).
 * Above example will set a query hint with the name <code>fetchSize</code> and the <code>String</code> value <code>100</code>.
 * <p/>
 * Example using map:
 * <code>
 * <pre>
 * Map hints = new HashMap();
 * hints.put("fetchSize", Integer.valueOf(100));
 * hints.put("anyName", anyObject());
 * Map parameters = new HashMap();
 * EntityManager em = emf.createEntityManager();
 * parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
 * parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_QUERY_HINTS_MAP, hints);
 * JasperRunManager.runReportToPdfFile(fileName, parameters);
 * </pre>
 * </code>
 * Note that when using report properties only <code>String</code> values can be set as query hint.
 * When using a query hints map, any <code>Object</code> can be set as value.
 * 
 * @author Marcel Overdijk (marceloverdijk@hotmail.com)
 * @version $Id$
 * @see net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory
 */
public class JRJpaQueryExecuter extends JRAbstractQueryExecuter {

	private static final Log log = LogFactory.getLog(JRJpaQueryExecuter.class);
	
	private final Integer reportMaxCount;
	
	private EntityManager em;
	private Query query;
	
	public JRJpaQueryExecuter(JRDataset dataset, Map<String,? extends JRValueParameter> parameters) {
		super(dataset, parameters);
		
		em = (EntityManager)getParameterValue(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER);
		reportMaxCount = (Integer)getParameterValue(JRParameter.REPORT_MAX_COUNT);

		if (em == null) {
			log.warn("The supplied javax.persistence.EntityManager object is null.");
		}
	
		parseQuery();
	}
	
	public JRDataSource createDatasource() throws JRException {
		JRDataSource datasource = null;
		String queryString = getQueryString();
		
		if (em != null && queryString != null && queryString.trim().length() > 0) {
			createQuery(queryString);

			datasource = createResultDatasource();
		}
		
		return datasource;
	}

	/**
	 * Creates the EJBQL query object.
	 *
	 * @param queryString the query string
	 */
	protected synchronized void createQuery(String queryString) {
		
		if (log.isDebugEnabled())
		{
			log.debug("EJBQL query: " + queryString);
		}
		
		query = em.createQuery(queryString);
		
		// Set parameters.
		List<String> parameterNames = getCollectedParameterNames();
		if (!parameterNames.isEmpty()) {
			// Use set to prevent the parameter to be set multiple times.
			Set<String> namesSet = new HashSet<String>();
			for (Iterator<String> iter = parameterNames.iterator(); iter.hasNext();) {
				String parameterName = iter.next();
				if (namesSet.add(parameterName)) {
					JRValueParameter parameter = getValueParameter(parameterName);
					String ejbParamName = getEjbqlParameterName(parameterName);
					Object paramValue = parameter.getValue();
					
					if (log.isDebugEnabled())
					{
						log.debug("Parameter " + ejbParamName + ": " + paramValue);
					}
					
					query.setParameter(ejbParamName, paramValue);
				}
			}
		}

		// Set query hints.
		// First, set query hints supplied by the JPA_QUERY_HINTS_MAP parameter.
		Map<String,Object> queryHintsMap = (Map<String,Object>)getParameterValue(JRJpaQueryExecuterFactory.PARAMETER_JPA_QUERY_HINTS_MAP);
		if (queryHintsMap != null) {
			for (Iterator<Map.Entry<String,Object>> i = queryHintsMap.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry<String,Object> pairs = i.next();
				if (log.isDebugEnabled()) {
					log.debug("EJBQL query hint [" + pairs.getKey() + "] set.");
				}
				query.setHint(pairs.getKey(), pairs.getValue());
			}
		}
		// Second, set query hints supplied by report properties which start with JREjbPersistenceQueryExecuterFactory.PROPERTY_JPA_PERSISTENCE_QUERY_HINT_PREFIX
		// Example: net.sf.jasperreports.ejbql.query.hint.fetchSize
		// This property will result in a query hint set with the name: fetchSize
		List<PropertySuffix> properties = JRProperties.getProperties(dataset, 
				JRJpaQueryExecuterFactory.PROPERTY_JPA_QUERY_HINT_PREFIX);
		for (Iterator<PropertySuffix> it = properties.iterator(); it.hasNext();) {
			PropertySuffix property = it.next();
			String queryHint = property.getSuffix();
			if (queryHint.length() > 0) {
				String value = property.getValue();
				if (log.isDebugEnabled()) {
					log.debug("EJBQL query hint [" + queryHint + "] set to: " + value);
				}
				query.setHint(queryHint, value);
			}
		}
	}	

	/**
	 * Creates a data source out of the query result.
	 * 
	 * @return the data source
	 */
	protected JRDataSource createResultDatasource()	{
		JRDataSource resDatasource;
		
		try {
			int pageSize = JRProperties.getIntegerProperty(dataset, 
					JRJpaQueryExecuterFactory.PROPERTY_JPA_QUERY_PAGE_SIZE,
					0);

			resDatasource = new JRJpaDataSource(this, pageSize);
		}
		catch (NumberFormatException e) {
			throw new JRRuntimeException("The " + JRJpaQueryExecuterFactory.PROPERTY_JPA_QUERY_PAGE_SIZE +
					" property must be numerical.",e);
		}
		
		return resDatasource;
	}

	public synchronized void close() {
		query = null;
	}

	public synchronized boolean cancelQuery() throws JRException {
		return false;
	}
	
	protected String getParameterReplacement(String parameterName) {
		return ':' + getEjbqlParameterName(parameterName);
	}
	
	protected String getEjbqlParameterName(String parameterName) {
		return JRStringUtil.getJavaIdentifier(parameterName);
	}
	
	/**
	 * Runs the query by calling <code>javax.persistence.Query.getResultList</code>.
	 * <p/>
	 * All the result rows are returned.
	 * 
	 * @return the result of the query as a list
	 */
	public List<?> getResultList() {
		if (reportMaxCount != null) {
			query.setMaxResults(reportMaxCount.intValue());
		}
		
		return query.getResultList();
	}
	
	/**
	 * Returns a page of the query results by calling <code>javax.persistence.Query.getResultList</code>.
	 * 
	 * @param firstIndex the index of the first row to return
	 * @param resultCount the number of rows to return
	 * @return result row list
	 */
	public List<?> getResultList(int firstIndex, int resultCount) {
		if (reportMaxCount != null && firstIndex + resultCount > reportMaxCount.intValue()) {
			resultCount = reportMaxCount.intValue() - firstIndex;
		}
		
		query.setFirstResult(firstIndex);
		query.setMaxResults(resultCount);
		
		return query.getResultList();
	}	
}
