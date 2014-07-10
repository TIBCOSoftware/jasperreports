/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.data.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import net.sf.jasperreports.data.DataFileConnection;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class HttpDataConnection implements DataFileConnection
{
	
	private static final Log log = LogFactory.getLog(HttpDataConnection.class);

	private final CloseableHttpClient httpClient;
	private final HttpRequestBase request;
	private CloseableHttpResponse response;
	
	public HttpDataConnection(CloseableHttpClient httpClient, HttpRequestBase request)
	{
		this.httpClient = httpClient;
		this.request = request;
	}

	@Override
	public InputStream getInputStream()
	{
		try
		{
			response = httpClient.execute(request);
			if (log.isDebugEnabled())
			{
				log.debug("HTTP response status " + response.getStatusLine());
			}
			
			HttpEntity entity = response.getEntity();
			if (entity == null)
			{
				throw new JRRuntimeException("No response entity");
			}
			
			return entity.getContent();
		}
		catch (ClientProtocolException e)
		{
			throw new JRRuntimeException(e);
		} 
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	@Override
	public void dispose()
	{
		if (response != null)
		{
			try
			{
				response.close();
			}
			catch (IOException e)
			{
				if (log.isWarnEnabled())
				{
					log.warn("Error closing HTTP response", e);
				}
			}
		}
		
		try
		{
			httpClient.close();
		}
		catch (IOException e)
		{
			if (log.isWarnEnabled())
			{
				log.warn("Error closing HTTP client", e);
			}
		}
	}

}
