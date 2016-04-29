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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class OracleProcedureCallHandler implements ProcedureCallHandler
{

	private static final String DB_PRODUCT = "oracle";
	private static final int ORACLE_CURSOR_TYPE = -10; // oracle.jdbc.OracleTypes.CURSOR

	protected static boolean isOracle(Connection connection) throws SQLException
	{
        String dbVendor = connection.getMetaData().getDatabaseProductName().toLowerCase();
        return DB_PRODUCT.equals(dbVendor);
	}
	
	private CallableStatement statement;
	private int cursorParameter = -1;

	@Override
	public void init(CallableStatement statement)
	{
		this.statement = statement;
	}

	@Override
	public boolean setParameterValue(int parameterIndex, 
			Class<?> type, Object value) throws SQLException
	{
		if (java.sql.ResultSet.class.isAssignableFrom(type))
		{
			if (cursorParameter > 0)
			{
				throw new JRRuntimeException("A stored procedure can have at most one cursor parameter : " 
						+ parameterIndex + " class " + type.getName());
			}

			cursorParameter = parameterIndex;
			statement.registerOutParameter(cursorParameter, ORACLE_CURSOR_TYPE);
			return true;
		}
		
		return false;
	}

	@Override
	public ResultSet execute() throws SQLException
	{
        statement.execute();
        
		ResultSet resultSet = null;
        if (cursorParameter > 0)
        {
            resultSet = (java.sql.ResultSet) statement.getObject(cursorParameter);
        }
        return resultSet;
	}

}
