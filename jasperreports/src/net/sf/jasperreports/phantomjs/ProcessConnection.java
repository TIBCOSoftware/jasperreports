/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.phantomjs;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ProcessConnection
{
	private static final Log log = LogFactory.getLog(ProcessConnection.class);

	private PhantomJSProcess process;
	private CloseableHttpClient httpClient;

	public ProcessConnection(ProcessDirector director, PhantomJSProcess process)
	{
		this.process = process;
		
		HttpClientBuilder clientBuilder = HttpClients.custom();
		
		// single connection
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
		clientBuilder.setConnectionManager(connManager);
		
		RequestConfig requestConfig = RequestConfig.custom()
				// ignore cookies for now
				.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
				.setSocketTimeout(director.getRequestTimeout()).build();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		
		this.httpClient = clientBuilder.build();
	}
	
	public String runRequest(String data)
	{
		HttpPost httpPost = new HttpPost(process.getListenURI());
		HttpEntity postEntity = new ByteArrayEntity(data.getBytes(StandardCharsets.UTF_8));
		httpPost.setEntity(postEntity);
		
		if (log.isDebugEnabled())
		{
			log.debug(process.getId() + " executing HTTP at " + process.getListenURI());
		}
		
		try (CloseableHttpResponse response = httpClient.execute(httpPost))
		{
			
			StatusLine status = response.getStatusLine();
			if (log.isDebugEnabled())
			{
				log.debug(process.getId() + " HTTP response status " + status);
			}
			
			HttpEntity entity = response.getEntity();
			if (entity == null)
			{
				throw new JRRuntimeException("Empty from PhantomJS");
			}
			
			if (status.getStatusCode() >= 300)
			{
				if (entity.getContentType() != null && entity.getContentType().getValue() != null
						&& entity.getContentType().getValue().startsWith("text/plain"))
				{
					String responseContents = EntityUtils.toString(entity, StandardCharsets.UTF_8);
					log.error("PhantomJS process " + process.getId() + " error: " + responseContents);
				}
				else
				{
					EntityUtils.consumeQuietly(entity);
				}
				throw new JRRuntimeException("Unexpected status " + status + " from PhantomJS");
			}
			
			byte[] responseData;
			try (InputStream responseStream = entity.getContent())
			{
				responseData = JRLoader.loadBytes(responseStream);
			}
			
			//TODO lucianc return the bytes
			return new String(responseData, StandardCharsets.UTF_8);
		}
		catch (SocketTimeoutException e)
		{
			log.error(process.getId() + " request timed out");
			throw new RequestTimeoutException(e);
		}
		catch (JRException | IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public void dispose()
	{
		try
		{
			httpClient.close();
		}
		catch (IOException e)
		{
			log.warn(process.getId() + " failed to close HttpClient", e);
		}
	}

}
