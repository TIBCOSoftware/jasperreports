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
package net.sf.jasperreports.data.jdbc;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.ParameterContributorContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractJdbcUrlParameterProcessingDataAdapterService extends JdbcDataAdapterService 
{
	/**
	 * 
	 */
	public AbstractJdbcUrlParameterProcessingDataAdapterService(ParameterContributorContext paramContribContext, JdbcDataAdapter jdbcDataAdapter) 
	{
		super(paramContribContext, jdbcDataAdapter);
	}


	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		JdbcDataAdapter jdbcDataAdapter = getJdbcDataAdapter();
		
		String connectionUrl = jdbcDataAdapter.getUrl();
		
		connectionUrl = processUrl(connectionUrl);

		jdbcDataAdapter.setUrl(connectionUrl);
		
		super.contributeParameters(parameters);
	}


	protected String processUrl(String url) throws JRException 
	{
		if (url != null && url.trim().length() > 0)
		{
			StringBuffer newUrlBuffer = new StringBuffer();
			
			String urlParams[] = url.split(";");
			for (String urlParam : urlParams)
			{
				newUrlBuffer.append(";");

				String[] tokens = urlParam.split("=");
				String paramName = tokens[0].trim();
				String paramValue = tokens.length > 1 ? tokens[1].trim() : null;

				String urlParamProcessResult = processUrlParameter(paramName, paramValue);

				if (urlParamProcessResult != null)
				{
					newUrlBuffer.append(urlParamProcessResult);
				}
			}
			
			return newUrlBuffer.substring(1);
		}
		
		return url;
	}


	protected abstract String processUrlParameter(String paramName, String paramValue) throws JRException;
}
