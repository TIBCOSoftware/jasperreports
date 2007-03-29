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

import java.lang.reflect.Array;
import java.util.Collection;

import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * Base (NOT) IN clause function for SQL queries.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRSqlAbstractInClause implements JRClauseFunction
{
	
	protected static final int POSITION_DB_COLUMN = 1;
	protected static final int POSITION_PARAMETER = 2;

	protected static final String CLAUSE_TRUISM = "0 = 0";
	
	protected JRSqlAbstractInClause()
	{
	}

	/**
	 * Creates a (NOT) IN SQL clause.
	 * 
	 * <p>
	 * The function expects two clause tokens (after the ID token):
	 * <ul>
	 * 	<li>The first token is the SQL column to be used in the clause.</li>
	 * 	<li>The second token is the name of the report parameter that contains the value list.
	 * 		<br/>
	 * 		The value of this parameter has to be an array, a <code>java.util.Collection</code>
	 * 		or <code>null</code>.
	 * 	</li>
	 * </p>
	 * 
	 * <p>
	 * The function constructs a <code>column [NOT] IN (?, ?, .., ?)</code> clause.
	 * If the values list is null or empty, the function generates a SQL clause that
	 * will always evaluate to true (e.g. <code>0 = 0</code>).
	 * </p>
	 */
	public void apply(JRClauseTokens clauseTokens, JRQueryClauseContext queryContext)
	{
		String col = clauseTokens.getToken(POSITION_DB_COLUMN);
		String param = clauseTokens.getToken(POSITION_PARAMETER);
		
		if (col == null)
		{
			throw new JRRuntimeException("SQL IN clause missing DB column token");
		}
		
		if (param == null)
		{
			throw new JRRuntimeException("SQL IN clause missing parameter token");
		}
		
		StringBuffer sbuffer = queryContext.queryBuffer();
		
		Object paramValue = queryContext.getValueParameter(param).getValue();
		if (paramValue == null)
		{
			handleNoValues(queryContext);
		}
		else
		{
			int count = valuesCount(param, paramValue);
			if (count == 0)
			{
				handleNoValues(queryContext);
			}
			else
			{
				sbuffer.append(col);
				sbuffer.append(' ');
				appendInOperator(sbuffer);
				sbuffer.append(' ');
				sbuffer.append('(');
				for (int idx = 0; idx < count; ++idx)
				{
					if (idx > 0)
					{
						sbuffer.append(", ");
					}
					sbuffer.append('?');
				}
				sbuffer.append(')');

				queryContext.addQueryMultiParameters(param, count);
			}
		}
	}
	
	protected void handleNoValues(JRQueryClauseContext queryContext)
	{
		queryContext.queryBuffer().append(CLAUSE_TRUISM);
	}

	protected int valuesCount(String paramName, Object paramValue)
	{
		int count;
		if (paramValue.getClass().isArray())
		{
			count = Array.getLength(paramValue);
		}
		else if (paramValue instanceof Collection)
		{
			count = ((Collection) paramValue).size();
		}
		else
		{
			throw new JRRuntimeException("Invalid type + " + paramValue.getClass().getName() + 
					" for parameter " + paramName + " used in an IN clause; the value must be an array or a collection.");
		}
		return count;
	}

	protected abstract void appendInOperator(StringBuffer sBuffer);
}
