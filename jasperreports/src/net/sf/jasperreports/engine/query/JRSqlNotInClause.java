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


/**
 * SQL "NOT IN" clause function.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see #apply(JRClauseTokens, JRQueryClauseContext)
 */
public class JRSqlNotInClause extends JRSqlAbstractInClause
{

	protected static final String OPERATOR_NOT_IN = "NOT IN";
	protected static final String OPERATOR_NULL = "IS NOT NULL";
	protected static final String OPERATOR_AND_OR = " AND ";
	
	protected static final JRSqlNotInClause singleton = new JRSqlNotInClause();
	
	/**
	 * Returns the singleton function instance.
	 * 
	 * @return the singleton function instance
	 */
	public static JRSqlNotInClause instance()
	{
		return singleton;
	}
	
	protected void appendInOperator(StringBuffer sBuffer)
	{
		sBuffer.append(OPERATOR_NOT_IN);
	}

	protected void appendNullOperator(StringBuffer sBuffer)
	{
		sBuffer.append(OPERATOR_NULL);
	}

	protected void appendAndOrOperator(StringBuffer sBuffer)
	{
		sBuffer.append(OPERATOR_AND_OR);
	}
}
