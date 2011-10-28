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
package net.sf.jasperreports.olap;

import java.util.Map;

import mondrian.olap.Connection;
import mondrian.olap.Query;
import mondrian.olap.Result;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.query.JRAbstractQueryExecuter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRMondrianQueryExecuter extends JRAbstractQueryExecuter
{
	private static final Log log = LogFactory.getLog(JRMondrianQueryExecuter.class);
	
	private Connection connection;
	private Result result;

	public JRMondrianQueryExecuter(JRDataset dataset, Map<String,? extends JRValueParameter> parametersMap)
	{
		super(dataset, parametersMap);
		
		connection = (Connection) getParameterValue(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION);

		if (connection == null)
		{
			log.warn("The supplied mondrian.olap.Connection object is null.");
		}
		
		parseQuery();
	}

	protected String getParameterReplacement(String parameterName)
	{
		return String.valueOf(getParameterValue(parameterName));
	}

	public JRDataSource createDatasource() throws JRException
	{
		JRDataSource dataSource = null;
		
		String queryStr = getQueryString();
		if (connection != null && queryStr != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("MDX query: " + queryStr);
			}
			
			Query query = connection.parseQuery(queryStr);
			result = connection.execute(query);
			
			dataSource = new JRMondrianDataSource(dataset, result);
		}

		return dataSource;
	}

	public void close()
	{
		if (result != null)
		{
			result.close();
			result = null;
		}
	}

	public boolean cancelQuery() throws JRException
	{
		return false;
	}
}
