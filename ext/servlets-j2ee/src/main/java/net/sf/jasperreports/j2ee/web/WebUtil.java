/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.j2ee.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.web.util.WebConstants;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class WebUtil
{
//	private JasperReportsContext jasperReportsContext;
	private JRPropertiesUtil propertiesUtil;
	
	private WebUtil(JasperReportsContext jasperReportsContext) 
	{
//		this.jasperReportsContext = jasperReportsContext;
		this.propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
	}
	
	public static WebUtil getInstance(JasperReportsContext jasperReportsContext) 
	{
		return new WebUtil(jasperReportsContext);
	}
	
	public String getResourceUri(HttpServletRequest request) 
	{
		String resourceUriParamName = propertiesUtil.getProperty(WebConstants.PROPERTY_REQUEST_PARAMETER_RESOURCE_URI);
		return request.getParameter(resourceUriParamName);
	}

	public Locale getResourceLocale(HttpServletRequest request) 
	{
		String resourceLocaleParamName = propertiesUtil.getProperty(WebConstants.PROPERTY_REQUEST_PARAMETER_RESOURCE_LOCALE);
		String localeCode = request.getParameter(resourceLocaleParamName);
		return localeCode == null ? null : JRDataUtils.getLocale(localeCode);
	}

	public String getResourceBundleForResource(HttpServletRequest request) 
	{
		String resourceBundleParamName = propertiesUtil.getProperty(WebConstants.PROPERTY_REQUEST_PARAMETER_RESOURCE_BUNDLE);
		return request.getParameter(resourceBundleParamName);
	}

	public boolean isDynamicResource(HttpServletRequest request) 
	{
		String dynamicResourceParamName = propertiesUtil.getProperty(WebConstants.PROPERTY_REQUEST_PARAMETER_DYNAMIC_RESOURCE);
		return Boolean.parseBoolean(request.getParameter(dynamicResourceParamName));
	}
}
