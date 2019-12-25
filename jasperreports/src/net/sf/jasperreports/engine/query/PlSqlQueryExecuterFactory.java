/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;

/**
 * Query executer factory for Oracle queries, both inline SQL and stored
 * procedures.
 * The normal JRJdbcQueryExecuterFactory can be used with Oracle for inline SQL,
 * but not stored procedures.
 * <p/>
 * To use with an oracle stored procedure that returns results via a REF CURSOR
 * you declare a parameter of type java.sql.ResultSet, and pass that to the
 * stored procedure.  For example, if you have a stored procedure named "do_stuff"
 * that takes a string as the first parameter and returns results via the
 * second parameter you would use a query that looks like:<br>
 * <code>{call do_stuff($P{the_string_param}, $P{the_result_set_param})}</code>
 * <p/>
 * This factory creates Oracle query executers for SQL queries.
 * 
 * @author Barry Klawans (bklawans@users.sourceforge.net) based off of work
 * by Lucian Chirita (lucianc@users.sourceforge.net) in JRJdbcQueryExecuterFactory.java
 */
public class PlSqlQueryExecuterFactory extends JRJdbcQueryExecuterFactory
{	

    /**
     * Built-in parameter holding the Oracle RefCursor needed to return values 
     * from a stored procedure.
     */
    public static final String PARAMETER_ORACLE_REF_CURSOR = "ORACLE_REF_CURSOR";
    
    private final static Object[] ORACLE_BUILT_IN_PARAMETERS = {
        PARAMETER_ORACLE_REF_CURSOR, java.sql.ResultSet.class
    };

	@Override
	public Object[] getBuiltinParameters()
	{
		return ORACLE_BUILT_IN_PARAMETERS;
	}

	@Override
	public boolean supportsQueryParameterType(String className)
	{
		return super.supportsQueryParameterType(className)
				|| java.sql.ResultSet.class.getName().equals(className);
	}
}
