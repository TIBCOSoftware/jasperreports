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
package net.sf.jasperreports.web.util;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public interface WebConstants
{
	public static final String REQUEST_PARAMETER_APPLICATION_DOMAIN = "jr_app_domain";
	public static final String REQUEST_PARAMETER_REPORT_URI = "jr_report_uri";
	public static final String REQUEST_PARAMETER_ASYNC_REPORT = "jr_async";
	public static final String REQUEST_PARAMETER_PAGE = "jr_page";
	public static final String REQUEST_PARAMETER_PAGE_TIMESTAMP = "jr_page_timestamp";
	public static final String REQUEST_PARAMETER_PAGE_UPDATE = "jr_page_update";
	public static final String REQUEST_PARAMETER_RUN_REPORT = "jr_run";
	
	/**
	 * 
	 */
	public static final String PROPERTIES_WEB_RESOURCE_PATTERN_PREFIX = "net.sf.jasperreports.web.resource.pattern.";

	/**
	 * Boolean property to control the setting of the response header Access-Control-Allow-Origin to *
	 */
	public static final String PROPERTY_ACCESS_CONTROL_ALLOW_ORIGIN = "net.sf.jasperreports.web.resource.cors.header.allow.origin.all";
}
