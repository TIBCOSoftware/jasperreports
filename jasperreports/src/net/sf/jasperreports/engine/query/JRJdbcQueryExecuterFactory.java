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

import java.util.Arrays;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JRValueParameter;
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
	 * Property specifying the ResultSet fetch size.
	 */
	public static final String PROPERTY_JDBC_FETCH_SIZE = JRProperties.PROPERTY_PREFIX + "jdbc.fetch.size";

	/**
	 * Property specifying the ResultSet type.
	 */
	public static final String PROPERTY_JDBC_RESULT_SET_TYPE = JRProperties.PROPERTY_PREFIX + "jdbc.result.set.type";

	/**
	 * Property specifying the ResultSet concurrency.
	 */
	public static final String PROPERTY_JDBC_CONCURRENCY = JRProperties.PROPERTY_PREFIX + "jdbc.concurrency";

	/**
	 * Property specifying the ResultSet holdability.
	 */
	public static final String PROPERTY_JDBC_HOLDABILITY = JRProperties.PROPERTY_PREFIX + "jdbc.holdability";

	/**
	 * Property specifying the statement max field size.
	 */
	public static final String PROPERTY_JDBC_MAX_FIELD_SIZE = JRProperties.PROPERTY_PREFIX + "jdbc.max.field.size";

	/**
	 * Flag property specifying if data will be stored in a cached rowset.
	 */
	public static final String PROPERTY_CACHED_ROWSET = JRProperties.PROPERTY_PREFIX + "jdbc.cached.rowset";

	/**
	 * Property specifying the default time zone to be used for sending and retrieving 
	 * date/time values to and from the database.
	 * 
	 * In most cases no explicit time zone conversion would be required, and this property 
	 * should not be set.
	 * 
	 * <p>
	 * The property can be set globally, at dataset level, at parameter and field levels,
	 * and as a report/dataset parameter.  Note that sending a value as parameter will 
	 * override all properties, and the time zone will be used for all date/time parameters
	 * and fields in the report. 
	 * </p>
	 * 
	 * @see JRResultSetDataSource#setTimeZone(java.util.TimeZone)
	 */
	public static final String PROPERTY_TIME_ZONE = JRProperties.PROPERTY_PREFIX + "jdbc.time.zone";

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
				java.sql.Date.class.getName(), 
				java.sql.Timestamp.class.getName(), 
				java.sql.Time.class.getName() };

		Arrays.sort(queryParameterClassNames);
	}
	
	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map<String,? extends JRValueParameter> parameters) throws JRException
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
