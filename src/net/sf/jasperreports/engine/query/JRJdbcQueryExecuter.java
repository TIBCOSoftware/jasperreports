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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * JDBC query executer for SQL queries.
 * <p/>
 * This query executer implementation offers built-in support for SQL queries.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRJdbcQueryExecuter extends JRAbstractQueryExecuter
{
	private static final Log log = LogFactory.getLog(JRJdbcQueryExecuter.class);


	private Connection connection;
	
	/**
	 * The statement used to fire the query.
	 */
	private PreparedStatement statement;

	private ResultSet resultSet;

	
	public JRJdbcQueryExecuter(JRDataset dataset, Map parameters)
	{
		super(dataset, parameters);
		
		connection = (Connection) getParameterValue(JRParameter.REPORT_CONNECTION);

		if (connection == null)
		{
			if (log.isWarnEnabled())
				log.warn("The supplied java.sql.Connection object is null.");
		}
		
		parseQuery();		
	}

	
	protected String getParameterReplacement(String parameterName)
	{
		return "?";
	}

	
	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.util.JRQueryExecuter#createDatasource()
	 */
	public JRDataSource createDatasource() throws JRException
	{
		JRDataSource dataSource = null;
		
		createStatement();
		
		if (statement != null)
		{
			try
			{
				Integer reportMaxCount = (Integer) getParameterValue(JRParameter.REPORT_MAX_COUNT);
				if (reportMaxCount != null)
				{
					statement.setMaxRows(reportMaxCount.intValue());
				}

				resultSet = statement.executeQuery();
				
				dataSource = new JRResultSetDataSource(resultSet);
			}
			catch (SQLException e)
			{
				throw new JRException("Error executing SQL statement for : " + dataset.getName(), e);
			}
		}
		
		return dataSource;
	}
	
	
	private void createStatement() throws JRException
	{
		String queryString = getQueryString();
		
		if (log.isDebugEnabled())
		{
			log.debug("SQL query string: " + queryString);
		}
		
		if (connection != null && queryString != null && queryString.trim().length() > 0)
		{
			try
			{
				statement = connection.prepareStatement(queryString);
				
				int fetchSize = JRProperties.getIntegerProperty(dataset.getPropertiesMap(),
						JRJdbcQueryExecuterFactory.PROPERTY_JDBC_FETCH_SIZE,
						0);
				if (fetchSize > 0)
				{
					statement.setFetchSize(fetchSize);
				}
				
				List parameterNames = getCollectedParameterNames();
				if (!parameterNames.isEmpty())
				{
					for(int i = 0; i < parameterNames.size(); i++)
					{
						String parameterName = (String)parameterNames.get(i);
						setStatementParameter(i + 1, parameterName);
					}
				}
			}
			catch (SQLException e)
			{
				throw new JRException("Error preparing statement for executing the report query : " + "\n\n" + queryString + "\n\n", e);
			}
		}
	}


	protected void setStatementParameter(int parameterIndex, String parameterName) throws SQLException, JRException
	{
		JRValueParameter parameter = getValueParameter(parameterName);
		Class clazz = parameter.getValueClass();
		Object parameterValue = parameter.getValue();
		
		if (log.isDebugEnabled())
		{
			log.debug("Parameter #" + parameterIndex + " (" + parameterName + " of type " + clazz.getName() + "): " + parameterValue);
		}

		if ( clazz.equals(java.lang.Object.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.JAVA_OBJECT);
			}
			else
			{
				statement.setObject(parameterIndex, parameterValue);
			}
		}
		else if ( clazz.equals(java.lang.Boolean.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.BIT);
			}
			else
			{
				statement.setBoolean(parameterIndex, ((Boolean)parameterValue).booleanValue());
			}
		}
		else if ( clazz.equals(java.lang.Byte.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.TINYINT);
			}
			else
			{
				statement.setByte(parameterIndex, ((Byte)parameterValue).byteValue());
			}
		}
		else if ( clazz.equals(java.lang.Double.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.DOUBLE);
			}
			else
			{
				statement.setDouble(parameterIndex, ((Double)parameterValue).doubleValue());
			}
		}
		else if ( clazz.equals(java.lang.Float.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.FLOAT);
			}
			else
			{
				statement.setFloat(parameterIndex, ((Float)parameterValue).floatValue());
			}
		}
		else if ( clazz.equals(java.lang.Integer.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.INTEGER);
			}
			else
			{
				statement.setInt(parameterIndex, ((Integer)parameterValue).intValue());
			}
		}
		else if ( clazz.equals(java.lang.Long.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.BIGINT);
			}
			else
			{
				statement.setLong(parameterIndex, ((Long)parameterValue).longValue());
			}
		}
		else if ( clazz.equals(java.lang.Short.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.SMALLINT);
			}
			else
			{
				statement.setShort(parameterIndex, ((Short)parameterValue).shortValue());
			}
		}
		else if ( clazz.equals(java.math.BigDecimal.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.DECIMAL);
			}
			else
			{
				statement.setBigDecimal(parameterIndex, (BigDecimal)parameterValue);
			}
		}
		else if ( clazz.equals(java.lang.String.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.VARCHAR);
			}
			else
			{
				statement.setString(parameterIndex, parameterValue.toString());
			}
		}
		else if ( clazz.equals(java.util.Date.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.DATE);
			}
			else
			{
				statement.setDate( parameterIndex, new java.sql.Date( ((java.util.Date)parameterValue).getTime() ) );
			}
		}
		else if ( clazz.equals(java.sql.Timestamp.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.TIMESTAMP);
			}
			else
			{
				statement.setTimestamp( parameterIndex, (java.sql.Timestamp)parameterValue );
			}
		}
		else if ( clazz.equals(java.sql.Time.class) )
		{
			if (parameterValue == null)
			{
				statement.setNull(parameterIndex, Types.TIME);
			}
			else
			{
				statement.setTime( parameterIndex, (java.sql.Time)parameterValue );
			}
		}
		else
		{
			throw new JRException("Parameter type not supported in query : " + parameterName + " class " + clazz.getName());
		}
	}

	
	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.util.JRQueryExecuter#close()
	 */
	public synchronized void close()
	{
		if (resultSet != null)
		{
			try
			{
				resultSet.close();
			}
			catch (SQLException e)
			{
				log.error("Error while closing result set.", e);
			}
			finally
			{
				resultSet = null;
			}
		}
		
		if (statement != null)
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				log.error("Error while closing statement.", e);
			}
			finally
			{
				statement = null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.util.JRQueryExecuter#cancelQuery()
	 */
	public synchronized boolean cancelQuery() throws JRException
	{
		if (statement != null)
		{
			try
			{
				statement.cancel();
				return true;
			}
			catch (Throwable t)
			{
				throw new JRException("Error cancelling SQL statement", t);
			}
		}
		
		return false;
	}
}
