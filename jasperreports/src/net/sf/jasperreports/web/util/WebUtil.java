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
package net.sf.jasperreports.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRDataUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRConstants.java 5180 2012-03-29 13:23:12Z teodord $
 */
public final class WebUtil
{
	public static final String PROPERTY_REQUEST_PARAMETER_REPORT_URI = JRPropertiesUtil.PROPERTY_PREFIX + "web.request.parameter.report.uri";
	public static final String PROPERTY_REQUEST_PARAMETER_RUN_REPORT = JRPropertiesUtil.PROPERTY_PREFIX + "web.request.parameter.run.report";
	public static final String PROPERTY_REQUEST_PARAMETER_ASYNC_REPORT = JRPropertiesUtil.PROPERTY_PREFIX + "web.request.parameter.async.report";

	public static final String PROPERTY_REQUEST_PARAMETER_RESOURCE_URI = JRPropertiesUtil.PROPERTY_PREFIX + "web.request.parameter.resource.uri";
	public static final String PROPERTY_REQUEST_PARAMETER_RESOURCE_LOCALE = JRPropertiesUtil.PROPERTY_PREFIX + "web.request.parameter.resource.locale";
	public static final String PROPERTY_REQUEST_PARAMETER_RESOURCE_BUNDLE = JRPropertiesUtil.PROPERTY_PREFIX + "web.request.parameter.resource.bundle";
	public static final String PROPERTY_REQUEST_PARAMETER_DYNAMIC_RESOURCE = JRPropertiesUtil.PROPERTY_PREFIX + "web.request.parameter.dynamic.resource";

	public static final String PROPERTY_REPORT_EXECUTION_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "web.report.execution.path";
	public static final String PROPERTY_REPORT_INTERACTION_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "web.report.interaction.path";
	public static final String PROPERTY_REPORT_RESOURCES_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "web.report.resources.path";

	public static final String RESOURCE_JR_GLOBAL_JS = "net/sf/jasperreports/web/servlets/resources/jasperreports-global.js";
	public static final String RESOURCE_JR_GLOBAL_CSS = "net/sf/jasperreports/web/servlets/resources/jasperreports-global.css";
	

	private JasperReportsContext jasperReportsContext;
	
	private WebUtil(JasperReportsContext jasperReportsContext) 
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	public static WebUtil getInstance(JasperReportsContext jasperReportsContext) 
	{
		return new WebUtil(jasperReportsContext);
	}
	
	/**
	 *
	 */
	public String getReportExecutionPath() 
	{
		String path = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REPORT_EXECUTION_PATH);
		if (path == null)
		{
			throw new JRRuntimeException("Configuration property '" + PROPERTY_REPORT_EXECUTION_PATH + "' is not set.");
		}
		return path;
	}
	
	/**
	 *
	 */
	public String getReportInteractionPath() 
	{
		String path = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(WebUtil.PROPERTY_REPORT_INTERACTION_PATH);
		if (path == null)
		{
			throw new JRRuntimeException("Configuration property '" + WebUtil.PROPERTY_REPORT_INTERACTION_PATH + "' is not set.");
		}
		return path;
	}
	
	/**
	 *
	 */
	public String getResourcesPath() 
	{
		String path = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(WebUtil.PROPERTY_REPORT_RESOURCES_PATH);
		if (path == null)
		{
			throw new JRRuntimeException("Configuration property '" + WebUtil.PROPERTY_REPORT_RESOURCES_PATH + "' is not set.");
		}
		return path;
	}

	public String getResourceUri(HttpServletRequest request) 
	{
		String resourceUriParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_RESOURCE_URI);
		return request.getParameter(resourceUriParamName);
	}

	public Locale getResourceLocale(HttpServletRequest request) 
	{
		String resourceLocaleParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_RESOURCE_LOCALE);
		String localeCode = request.getParameter(resourceLocaleParamName);
		return localeCode == null ? null : JRDataUtils.getLocale(localeCode);
	}

	public String getResourceBundleForResource(HttpServletRequest request) 
	{
		String resourceBundleParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_RESOURCE_BUNDLE);
		return request.getParameter(resourceBundleParamName);
	}

	public boolean isDynamicResource(HttpServletRequest request) 
	{
		String dynamicResourceParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_DYNAMIC_RESOURCE);
		return Boolean.parseBoolean(request.getParameter(dynamicResourceParamName));
	}

	public String getResourcesBasePath() 
	{
		String resourcesBasePath = getResourcesPath();
		
		String resourceUriParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_RESOURCE_URI);

		return resourcesBasePath + "?" + resourceUriParamName + "=";
	}

	public String getResourcePath(String resourcePath) 
	{
		return getResourcePath(getResourcesBasePath(), resourcePath, false);
	}

	public String getResourcePath(String resourcePath, boolean isDynamic) 
	{
		return getResourcePath(getResourcesBasePath(), resourcePath, isDynamic);
	}

	public String getResourcePath(String resourcesBasePath, String resourcePath) 
	{
		return getResourcePath(resourcesBasePath, resourcePath, false);
	}

	public String getResourcePath(String resourcesBasePath, String resourcePath, boolean isDynamic) 
	{
		String resourceDynamicParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_DYNAMIC_RESOURCE);
		
		return 
			resourcesBasePath + resourcePath
			+ (isDynamic ? ("&" + resourceDynamicParamName + "=" + isDynamic) : ""); 
//			+ "&" + ResourceServlet.SERVLET_PATH + "=" + UrlUtil.urlEncode(webResourceBasePath);
	}

	public String getResourcePath(String resourcesBasePath, String resourcePath, String resourceBundleName, Locale locale) 
	{
		String resourceBundleParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_RESOURCE_BUNDLE);
		String resourceLocaleParamName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(PROPERTY_REQUEST_PARAMETER_RESOURCE_LOCALE);
		
		return 
			getResourcePath(resourcesBasePath, resourcePath, false)
			+ "&" + resourceBundleParamName + "=" + resourceBundleName
			+ "&" + resourceLocaleParamName + "=" + JRDataUtils.getLocaleCode(locale);
	}

	
	public static String encodeUrl(String url) {
		if (url != null) {
			try {
				return URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new JRRuntimeException(e);
			}
		}
		return null;
	}


	public static String decodeUrl(String url) {
		if (url != null) {
			try {
				return URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new JRRuntimeException(e);
			}
		}
		return null;
	}
}
