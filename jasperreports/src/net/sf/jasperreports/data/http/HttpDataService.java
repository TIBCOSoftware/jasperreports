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
package net.sf.jasperreports.data.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.data.DataFileConnection;
import net.sf.jasperreports.data.DataFileService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.util.SecretsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class HttpDataService implements DataFileService
{
	
	private static final Log log = LogFactory.getLog(HttpDataService.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_NO_HTTP_URL_SET = "data.http.no.http.url.set";
	public static final String EXCEPTION_MESSAGE_KEY_UNKNOWN_REQUEST_METHOD = "data.http.unknown.request.method";
	
	public static final String PARAMETER_URL = "HTTP_DATA_URL";
	
	public static final String PARAMETER_USERNAME = "HTTP_DATA_USERNAME";
	
	public static final String PARAMETER_PASSWORD = "HTTP_DATA_PASSWORD";
	
	public static final String PARAMETER_PREFIX_URL_PARAMETER = "HTTP_DATA_URL_PARAMETER_";
	
	public static final String PARAMETER_PREFIX_POST_PARAMETER = "HTTP_DATA_POST_PARAMETER_";

	private final JasperReportsContext context;
	
	private final HttpDataLocation dataLocation;
	
	public HttpDataService(JasperReportsContext context, HttpDataLocation dataLocation)
	{
		this.context = context;
		this.dataLocation = dataLocation;
	}

	@Override
	public DataFileConnection getDataFileConnection(Map<String, Object> parameters) throws JRException
	{
		CloseableHttpClient httpClient = createHttpClient(parameters);
		HttpRequestBase request = createRequest(parameters);
		return new HttpDataConnection(httpClient, request);
	}

	protected CloseableHttpClient createHttpClient(Map<String, Object> parameters)
	{
		HttpClientBuilder clientBuilder = HttpClients.custom();
		
		// single connection
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
		clientBuilder.setConnectionManager(connManager);
		
		// ignore cookies for now
		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		
		HttpClientContext clientContext = HttpClientContext.create();
		
		setAuthentication(parameters, clientContext);
		
		CloseableHttpClient client = clientBuilder.build();
		return client;
	}

	protected void setAuthentication(Map<String, Object> parameters, HttpClientContext clientContext)
	{
		String username = getUsername(parameters);
		if (username != null)
		{
			String password = getPassword(parameters);
			
			BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			//FIXME proxy authentication?
			credentialsProvider.setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
					new UsernamePasswordCredentials(username, password));
			clientContext.setCredentialsProvider(credentialsProvider);
		}
	}

	protected String getUsername(Map<String, Object> parameters)
	{
		String username = (String) parameters.get(PARAMETER_USERNAME);
		if (username == null)
		{
			username = dataLocation.getUsername();
		}
		return username;
	}

	protected String getPassword(Map<String, Object> parameters)
	{
		String password = (String) parameters.get(PARAMETER_PASSWORD);
		if (password == null)
		{
			password = dataLocation.getPassword();
		}
		
		if (password != null)
		{
			SecretsUtil secrets = SecretsUtil.getInstance(context);
			password = secrets.getSecret(AbstractDataAdapterService.SECRETS_CATEGORY, password);
		}
		
		return password;
	}

	protected HttpRequestBase createRequest(Map<String, Object> parameters)
	{
		List<NameValuePair> postParameters = collectPostParameters(parameters);

		URI requestURI = getRequestURI(parameters);
		
		RequestMethod method = dataLocation.getMethod();
		if (method == null)
		{
			method = postParameters.isEmpty() ? RequestMethod.GET : RequestMethod.POST;
		}
		
		HttpRequestBase request;
		switch (method)
		{
		case GET:
			if (!postParameters.isEmpty())
			{
				log.warn("Ignoring POST parameters for GET request to " + dataLocation.getUrl());
			}
			request = createGetRequest(requestURI);
			break;
		case POST:
			request = createPostRequest(requestURI, postParameters);
			break;
		case PUT:
			request = createPutRequest(requestURI, postParameters);
			break;
		default:
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_UNKNOWN_REQUEST_METHOD,
					new Object[]{method});
		}
		
		return request;
	}

	protected HttpGet createGetRequest(URI requestURI)
	{
		HttpGet httpGet = new HttpGet(requestURI);
		return httpGet;
	}

	protected HttpPost createPostRequest(URI requestURI, List<NameValuePair> postParameters)
	{
		HttpPost httpPost = new HttpPost(requestURI);
		HttpEntity entity = createRequestEntity(postParameters);
		httpPost.setEntity(entity);
		return httpPost;
	}

	protected HttpPut createPutRequest(URI requestURI, List<NameValuePair> postParameters)
	{
		HttpPut httpPost = new HttpPut(requestURI);
		HttpEntity entity = createRequestEntity(postParameters);
		httpPost.setEntity(entity);
		return httpPost;
	}

	protected HttpEntity createRequestEntity(List<NameValuePair> postParameters)
	{
		UrlEncodedFormEntity formEntity;
		try
		{
			formEntity = new UrlEncodedFormEntity(postParameters, "UTF-8");//allow custom?
		}
		catch (UnsupportedEncodingException e)
		{
			// should not happen
			throw new JRRuntimeException(e);
		}
		return formEntity;
	}

	protected List<NameValuePair> collectUrlParameters(Map<String, Object> reportParameters)
	{
		return collectParameters(dataLocation.getUrlParameters(), reportParameters, PARAMETER_PREFIX_URL_PARAMETER);
	}

	protected List<NameValuePair> collectPostParameters(Map<String, Object> reportParameters)
	{
		return collectParameters(dataLocation.getPostParameters(), reportParameters, PARAMETER_PREFIX_POST_PARAMETER);
	}
	
	protected List<NameValuePair> collectParameters(List<HttpLocationParameter> staticParameters,
			Map<String, Object> reportParameters, String reportParameterPrefix)
	{
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		if (staticParameters != null && !staticParameters.isEmpty())
		{
			for (HttpLocationParameter parameter : staticParameters)
			{
				String name = parameter.getName();
				String paramValue = parameter.getValue();
				if (paramValue != null)
				{
					String reportParameterName = reportParameterPrefix + parameter.getName();
					if (!reportParameters.containsKey(reportParameterName))
					{
						if (log.isDebugEnabled())
						{
							log.debug("adding parameter " + name + " with value " + paramValue);
						}
						postParameters.add(new BasicNameValuePair(name, paramValue));
					}
					else
					{
						if (log.isDebugEnabled())
						{
							log.debug("static parameter " + parameter.getName() + " overridden by the report");
						}
					}
				}
			}
		}
		
		for (Entry<String, Object> paramEntry : reportParameters.entrySet())
		{
			String paramName = paramEntry.getKey();
			Object value = paramEntry.getValue();
			if (paramName.startsWith(reportParameterPrefix) && value != null)
			{
				String name = paramName.substring(reportParameterPrefix.length(), paramName.length());
				String paramValue = toHttpParameterValue(value);
				if (log.isDebugEnabled())
				{
					log.debug("adding parameter " + name + " with value " + paramValue);
				}
				postParameters.add(new BasicNameValuePair(name, paramValue));
			}
		}
		return postParameters;
	}

	protected URI getRequestURI(Map<String, Object> parameters)
	{
		String url = getURL(parameters);
		if (url == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_NO_HTTP_URL_SET,
					(Object[])null);
		}
		
		try
		{
			URIBuilder uriBuilder = new URIBuilder(url);
			List<NameValuePair> urlParameters = collectUrlParameters(parameters);
			if (!urlParameters.isEmpty())
			{
				uriBuilder.addParameters(urlParameters);
			}
			
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

	protected String getURL(Map<String, Object> parameters)
	{
		String url = (String) parameters.get(PARAMETER_URL);
		if (url == null)
		{
			url = dataLocation.getUrl();
		}
		return url;
	}

	protected String toHttpParameterValue(Object value)
	{
		return String.valueOf(value);//FIXME do something smarter than String.valueOf
	}
}
