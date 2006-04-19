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

import java.util.Arrays;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Query executer factory for SQL queries.
 * <p/>
 * This factory creates JDBC query executers for SQL queries.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see net.sf.jasperreports.engine.query.JRJdbcQueryExecuter
 */
public class JRJdbcQueryExecuterFactory implements JRQueryExecuterFactory
{	
	/**
	 * Property specifying whether field descriptions should be used to determine the mapping between the fields
	 * and the query return values.
	 */
	public static final String PROPERTY_JDBC_FETCH_SIZE = JRProperties.PROPERTY_PREFIX + "jdbc.fetch.size";

	/**
	 * SQL query language.
	 */
	public static final String QUERY_LANGUAGE_SQL = "sql";
	
	
	private static final String[] queryParameterClassNames;
	
	static
	{
		queryParameterClassNames = new String[] {
				java.lang.Object.class.getName(), 
				java.lang.Boolean.class.getName(), 
				java.lang.Byte.class.getName(), 
				java.lang.Double.class.getName(),
				java.lang.Float.class.getName(), 
				java.lang.Integer.class.getName(), 
				java.lang.Long.class.getName(), 
				java.lang.Short.class.getName(), 
				java.math.BigDecimal.class.getName(),
				java.lang.String.class.getName(), 
				java.util.Date.class.getName(), 
				java.sql.Timestamp.class.getName(), 
				java.sql.Time.class.getName() };

		Arrays.sort(queryParameterClassNames);
	}
	
	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters) throws JRException
	{
		return new JRJdbcQueryExecuter(dataset, parameters);
	}

	public Object[] getBuiltinParameters()
	{
		return null;
	}

	public boolean supportsQueryParameterType(String className)
	{
		return Arrays.binarySearch(queryParameterClassNames, className) >= 0;
	}
}
