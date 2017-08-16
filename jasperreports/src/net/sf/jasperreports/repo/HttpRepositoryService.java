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
package net.sf.jasperreports.repo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import net.sf.jasperreports.data.http.HttpDataConnection;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class HttpRepositoryService implements StreamRepositoryService
{
	private static final Log log = LogFactory.getLog(HttpRepositoryService.class);

	public static final String EXCEPTION_MESSAGE_KEY_NOT_IMPLEMENTED = "repo.file.not.implemented";

	private JasperReportsContext jasperReportsContext;
	private String repositoryUrl;

	/**
	 *
	 */
	public HttpRepositoryService(JasperReportsContext jasperReportsContext, String repositoryUrl)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.repositoryUrl = repositoryUrl;
	}
	
	/**
	 * 
	 */
	public void setRoot(String root)
	{
		this.repositoryUrl = root;
	}
	
	/**
	 * 
	 */
	public String getRoot()
	{
		return repositoryUrl;
	}
	
	@Override
	public InputStream getInputStream(String uri)
	{
		CloseableHttpClient httpClient = createHttpClient();
		HttpRequestBase request = createRequest(uri);
		HttpDataConnection dataConnection =  new HttpDataConnection(httpClient, request);

		InputStream is = null;
		
		try
		{
			is = dataConnection.getInputStream();
		}
		catch (Exception e)
		{
			//nothing to do
		}
		
		return is;
	}
	
	@Override
	public OutputStream getOutputStream(String uri)
	{
		throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_NOT_IMPLEMENTED, (Object[])null);
	}
	
	@Override
	public Resource getResource(String uri)
	{
		throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_NOT_IMPLEMENTED, (Object[])null);
	}
	
	@Override
	public void saveResource(String uri, Resource resource)
	{
		PersistenceService persistenceService = PersistenceUtil.getInstance(jasperReportsContext)
				.getService(HttpRepositoryService.class, resource.getClass());

		if (persistenceService != null)
		{
			persistenceService.save(resource, uri, this);
		}
	}
	
	@Override
	public <K extends Resource> K getResource(String uri, Class<K> resourceType)
	{
		PersistenceService persistenceService = PersistenceUtil.getInstance(jasperReportsContext)
				.getService(HttpRepositoryService.class, resourceType);

		if (persistenceService != null)
		{
			return (K)persistenceService.load(uri, this);
		}
		return null;
	}

	protected CloseableHttpClient createHttpClient()
	{
		HttpClientBuilder clientBuilder = HttpClients.custom();

		// single connection
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
		clientBuilder.setConnectionManager(connManager);

		// ignore cookies for now
		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		clientBuilder.setDefaultRequestConfig(requestConfig);

		return clientBuilder.build();
	}

	protected HttpRequestBase createRequest(String resourceUri)
	{
		URI requestURI = getRequestURI(resourceUri);

		return new HttpGet(requestURI);
	}

	protected URI getRequestURI(String resourceUri)
	{
		try
		{
			URIBuilder uriBuilder = new URIBuilder(repositoryUrl);
			uriBuilder.addParameter("fileName", resourceUri);

			URI uri = uriBuilder.build();
			if (log.isDebugEnabled())
			{
				log.debug("request URI " + uri);
			}
			return uri;
		}
		catch (URISyntaxException e)
		{
			throw new JRRuntimeException(e);
		}
	}
}
