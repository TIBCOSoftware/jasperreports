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

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;

/**
 * Extensions factory that registers built-in query clause functions for SQL queries.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class SQLQueryClauseFunctionsExtensions implements ExtensionsRegistryFactory
{

	private static SingletonExtensionRegistry<QueryClauseFunctionBundle> registry;

	static
	{
		StandardSingleQueryClauseFunctionBundle bundle = new StandardSingleQueryClauseFunctionBundle(
				JRJdbcQueryExecuter.CANONICAL_LANGUAGE);
		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_IN, JRSqlInClause.instance());
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_NOTIN, JRSqlNotInClause.instance());	
		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_EQUAL, JRSqlEqualClause.instance());		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_NOTEQUAL, JRSqlNotEqualClause.instance());		
		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_LESS, JRSqlLessOrGreaterClause.instance());		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_GREATER, JRSqlLessOrGreaterClause.instance());		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_LESS_OR_EQUAL, JRSqlLessOrGreaterClause.instance());		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_GREATER_OR_EQUAL, JRSqlLessOrGreaterClause.instance());
		
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN, JRSqlBetweenClause.instance());	
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN_CLOSED, JRSqlBetweenClause.instance());	
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN_LEFT_CLOSED, JRSqlBetweenClause.instance());	
		bundle.addFunction(JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN_RIGHT_CLOSED, JRSqlBetweenClause.instance());	
		
		registry = new SingletonExtensionRegistry<QueryClauseFunctionBundle>(
				QueryClauseFunctionBundle.class, bundle);
	}
	
	@Override
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		return registry;
	}

}
