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

import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * Base LESS/GREATER clause function for SQL queries.
 * 
 * <p>
 * The first token in the $X{...} syntax is the function ID token. Possible values for 
 * the LESS | GREATER clause function ID token are:
 * <ul>
 * <li><code>LESS</code> - in this case the <code>LESS THAN</code> operator <code>&lt;</code> will be applied.</li>
 * <li><code>LESS]</code> - in this case the <code>LESS OR EQUAL</code> operator <code>&lt;=</code> will be applied.</li>
 * <li><code>GREATER</code> - in this case the <code>GREATER THAN</code> operator <code>&gt;</code> will be applied.</li>
 * <li><code>[GREATER</code> - in this case the <code>GREATER OR EQUAL</code> operator <code>&gt;=</code> will be applied.</li>
 * </ul>
 * </p> 
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRSqlLessOrGreaterClause implements JRClauseFunction
{
	
	protected static final int POSITION_CLAUSE_ID = 0;
	protected static final int POSITION_DB_COLUMN = 1;
	protected static final int POSITION_PARAMETER = 2;

	protected static final String OPERATOR_LESS = "<";
	protected static final String OPERATOR_LESS_OR_EQUAL = "<=";
	protected static final String OPERATOR_GREATER = ">";
	protected static final String OPERATOR_GREATER_OR_EQUAL = ">=";
	
	protected static final String CLAUSE_TRUISM = "0 = 0";

	protected static final JRSqlLessOrGreaterClause singleton = new JRSqlLessOrGreaterClause();
	
	/**
	 * Returns the singleton function instance.
	 * 
	 * @return the singleton function instance
	 */
	public static JRSqlLessOrGreaterClause instance()
	{
		return singleton;
	}

	/**
	 * Creates either a LESS or a GREATER SQL clause, depending on the clause ID.
	 * 
	 * <p>
	 * The method expects two clause tokens (after the ID token):
	 * <ul>
	 * 	<li>The first token is the SQL column (or column combination) to be used in the clause.</li>
	 * 	<li>The second token is the name of the report parameter that contains the value to compare to.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The method constructs one of the following clauses:
	 * <ul>
	 * <li><code>column < ?</code> if the clause ID is <code>LESS</code></li>
	 * <li><code>column <= ?</code> if the clause ID is <code>LESS]</code></li>
	 * <li><code>column >= ?</code> if the clause ID is <code>GREATER</code></li>
	 * <li><code>column > ?</code> if the clause ID is <code>[GREATER</code></li>
	 * </ul> 
	 * If the value to compare to is null, the method generates a SQL clause that
	 * will always evaluate to true (e.g. <code>0 = 0</code>).
	 * </p>
	 */
	public void apply(JRClauseTokens clauseTokens, JRQueryClauseContext queryContext)
	{
		String clauseId = clauseTokens.getToken(POSITION_CLAUSE_ID);
		String col = clauseTokens.getToken(POSITION_DB_COLUMN);
		String param = clauseTokens.getToken(POSITION_PARAMETER);

		if (clauseId == null)
		{
			throw new JRRuntimeException("Missing clause name token");
		}
		
		if (col == null)
		{
			throw new JRRuntimeException("SQL LESS/GREATER clause missing DB column token");
		}
		
		if (param == null)
		{
			throw new JRRuntimeException("SQL LESS/GREATER clause missing parameter token");
		}
		
		Object paramValue = queryContext.getValueParameter(param).getValue();
		StringBuffer sbuffer = queryContext.queryBuffer();
		if(paramValue == null)
		{
			sbuffer.append(CLAUSE_TRUISM);
			return;
		}
		
		sbuffer.append(col);
		sbuffer.append(' ');
		handleLessOrGreaterOperator(sbuffer, clauseId);
		sbuffer.append(' ');
		sbuffer.append('?');
		queryContext.addQueryParameter(param);
		
	}
	
	/**
	 * Appends the appropriate inequality sign to the query string, depending on the clause ID value
	 * 
	 * @param sBuffer the StringBuffer that contains the generated query
	 * @param clauseId the clause ID
	 */
	protected void handleLessOrGreaterOperator(StringBuffer sBuffer, String clauseId)
	{
		if(JRJdbcQueryExecuter.CLAUSE_ID_LESS.equals(clauseId))
		{
			sBuffer.append(OPERATOR_LESS);
		}
		else if (JRJdbcQueryExecuter.CLAUSE_ID_LESS_OR_EQUAL.equals(clauseId))
		{
			sBuffer.append(OPERATOR_LESS_OR_EQUAL);
		}
		else if (JRJdbcQueryExecuter.CLAUSE_ID_GREATER.equals(clauseId))
		{
			sBuffer.append(OPERATOR_GREATER);
		}
		else if (JRJdbcQueryExecuter.CLAUSE_ID_GREATER_OR_EQUAL.equals(clauseId))
		{
			sBuffer.append(OPERATOR_GREATER_OR_EQUAL);
		}
	}
}
