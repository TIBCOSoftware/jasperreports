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
package net.sf.jasperreports.web.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class AbstractServlet extends HttpServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static JasperReportsContext jasperReportsContext;

	protected static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
	protected static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";

	protected static final String JSON_ACCEPT_HEADER = "application/json";
	protected static final String HTML_ACCEPT_HEADER = "text/html";

	public static final String EXCEPTION_MESSAGE_KEY_PAGE_NOT_FOUND = "web.servlets.page.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR = "web.servlets.report.generation.error";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_NOT_FOUND = "web.servlets.report.not.found";

	/**
	 *
	 */
	public JasperReportsContext getJasperReportsContext()
	{
		return jasperReportsContext;
	}
	
	/**
	 *
	 */
	public static void setJasperReportsContext(JasperReportsContext jrctx)
	{
		jasperReportsContext = jrctx;
	}

	protected void setNoExpire(HttpServletResponse response) {
		// Set to expire far in the past.
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
	}
}
