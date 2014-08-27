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
package net.sf.jasperreports.types.date;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.query.JRClauseFunction;
import net.sf.jasperreports.engine.query.JRClauseTokens;
import net.sf.jasperreports.engine.query.JRQueryClauseContext;
import net.sf.jasperreports.engine.query.JRSqlAbstractEqualClause;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public abstract class DateRangeBaseSQLEqualityClause implements JRClauseFunction
{
	
	protected DateRangeBaseSQLEqualityClause()
	{
	}
	
	@Override
	public void apply(JRClauseTokens clauseTokens, JRQueryClauseContext queryContext)
	{
		String clauseId = clauseTokens.getClauseId();
		String col = clauseTokens.getToken(JRSqlAbstractEqualClause.POSITION_DB_COLUMN);
		String param = clauseTokens.getToken(JRSqlAbstractEqualClause.POSITION_PARAMETER);
		
		if (col == null)
		{
			throw new JRRuntimeException("SQL EQUAL clause missing DB column token");
		}
		
		if (param == null)
		{
			throw new JRRuntimeException("SQL EQUAL clause missing parameter token");
		}
		
		Object paramValue = queryContext.getValueParameter(param).getValue();
		if (paramValue != null && !(paramValue instanceof DateRange))
		{
			throw new JRRuntimeException("Parameter " + param + " in clause " + clauseId
					+ " is not a date range");
		}
		
		DateRange dateRange = (DateRange) paramValue;
		StringBuffer queryBuffer = queryContext.queryBuffer();
		queryBuffer.append('(');
		applyDateRange(queryContext, col, dateRange);
		queryBuffer.append(')');
	}

	protected abstract void applyDateRange(JRQueryClauseContext queryContext,
			String column, DateRange dateRange);

}
