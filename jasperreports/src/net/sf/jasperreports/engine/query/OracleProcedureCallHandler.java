/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class OracleProcedureCallHandler implements ProcedureCallHandler
{
	
	private static final Log log = LogFactory.getLog(OracleProcedureCallHandler.class);

	private static final Class<?> ORACLE_CONNECTION_CLASS;
	static
	{
		Class<?> oracleConnectionClass;
		try
		{
			oracleConnectionClass = Class.forName("oracle.jdbc.OracleConnection");
		}
		catch (ClassNotFoundException e)
		{
			oracleConnectionClass = null;
		}
		ORACLE_CONNECTION_CLASS = oracleConnectionClass;
	}
	
	private static final String URL_DATADIRECT = "jdbc:datadirect:oracle:";
	private static final String URL_TIBCO = "jdbc:tibcosoftware:oracle:";
	private static final String URL_ORACLE = "jdbc:oracle:";
	
	private static final String DRIVER_NAME_ORACLE = "Oracle JDBC driver";
	private static final String DRIVER_NAME_DATADIRECT = "Oracle";
	
	private static final String DB_PRODUCT = "oracle";
	private static final int ORACLE_CURSOR_TYPE = -10; // oracle.jdbc.OracleTypes.CURSOR

	protected static boolean isOracle(Connection connection) throws SQLException
	{
        String dbVendor = connection.getMetaData().getDatabaseProductName().toLowerCase();
        return DB_PRODUCT.equals(dbVendor);
	}
	
	private CallableStatement statement;
	private boolean isDataDirectDriver;
	private int cursorParameter = -1;

	@Override
	public void init(CallableStatement statement)
	{
		this.statement = statement;
		
		isDataDirectDriver = isDataDirectDriver();
		if (log.isDebugEnabled())
		{
			log.debug("DataDirect driver " + isDataDirectDriver);
		}
	}
	
	protected boolean isDataDirectDriver()
	{
		Connection connection;
		try
		{
			connection = statement.getConnection();
		}
		catch (SQLException e)
		{
			log.error("Failure while detecting driver", e);
			return false;
		}
		
		DatabaseMetaData metaData = null;
		try
		{
			metaData = connection.getMetaData();
		}
		catch (SQLException e)
		{
			log.error("Failure while detecting driver", e);
		}

		String connectionURL = null;
		if (metaData != null)
		{
			try
			{
				connectionURL = metaData.getURL();
			}
			catch (SQLException e)
			{
				log.error("Failure while detecting driver", e);
			}
		}
		
		if (connectionURL != null)
		{
			if (connectionURL.contains(URL_DATADIRECT) || connectionURL.contains(URL_TIBCO))
			{
				return true;
			}
			if (connectionURL.contains(URL_ORACLE))
			{
				return false;
			}
		}

		if (ORACLE_CONNECTION_CLASS != null)
		{
			try
			{
				if (connection.isWrapperFor(ORACLE_CONNECTION_CLASS))
				{
					return false;
				}
			}
			catch (SQLException e)
			{
				log.error("Failure while detecting driver", e);
			}
		}
		
		if (metaData != null)
		{
			try
			{
				String driverName = metaData.getDriverName();
				if (driverName.equals(DRIVER_NAME_ORACLE))
				{
					return false;
				}
				if (driverName.equals(DRIVER_NAME_DATADIRECT))
				{
					return true;
				}
			}
			catch (SQLException e)
			{
				log.error("Failure while detecting driver", e);
			}
		}
		
		//fallback to Oracle
		return false;
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
			if (isDataDirectDriver)
			{
				statement.setInt(parameterIndex, 0);
			}
			else
			{
				statement.registerOutParameter(parameterIndex, ORACLE_CURSOR_TYPE);
			}
			return true;
		}
		
		return false;
	}

	@Override
	public ResultSet execute() throws SQLException
	{
        boolean isResult = statement.execute();
        
		ResultSet resultSet = null;
		if (isDataDirectDriver)
		{
			while (!isResult)
			{
				int updateCount = statement.getUpdateCount();
				if (log.isDebugEnabled())
				{
					log.debug("Update count " + updateCount);
				}
				
				if (updateCount == -1)
				{
					break;
				}
				
				isResult = statement.getMoreResults();
			}
			
			if (isResult)
			{
				resultSet = statement.getResultSet();
			}
			else if (log.isDebugEnabled())
			{
				log.debug("No ResultSet found");
			}
		}
		else
		{
	        if (cursorParameter > 0)
	        {
	            resultSet = (java.sql.ResultSet) statement.getObject(cursorParameter);
	        }
		}
        return resultSet;
	}

}
