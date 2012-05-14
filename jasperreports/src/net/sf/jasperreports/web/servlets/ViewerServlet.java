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
package net.sf.jasperreports.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ViewerServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(ViewerServlet.class);
	
	private static final String TEMPLATE_HEADER = "net/sf/jasperreports/web/servlets/resources/viewer/HeaderTemplate.vm";
	private static final String TEMPLATE_BODY = "net/sf/jasperreports/web/servlets/resources/viewer/BodyTemplate.vm";
	private static final String TEMPLATE_FOOTER = "net/sf/jasperreports/web/servlets/resources/viewer/FooterTemplate.vm";
	
	private static final String RESOURCE_JR_GLOBAL_JS = "net/sf/jasperreports/web/servlets/resources/jasperreports-global.js";
	private static final String RESOURCE_JR_GLOBAL_CSS = "net/sf/jasperreports/web/servlets/resources/jasperreports-global.css";
	private static final String RESOURCE_VIEWER_TOOLBAR_JS = "net/sf/jasperreports/web/servlets/resources/jasperreports-reportViewerToolbar.js";

	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		response.setContentType("text/html; charset=UTF-8");
		
		// Set to expire far in the past.
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		
		PrintWriter out = response.getWriter();

		WebReportContext webReportContext = WebReportContext.getInstance(request);

		try
		{
			render(request, webReportContext, out);
		}
		catch (Exception e)
		{
			log.error("Error on report execution", e);
			
			out.println("<html>");//FIXMEJIVE do we need to render this? or should this be done by the viewer?
			out.println("<head>");
			out.println("<title>JasperReports - Web Application Sample</title>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
			out.println("</head>");
			
			out.println("<body bgcolor=\"white\">");

			out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");
			out.println("<pre>");

			e.printStackTrace(out);

			out.println("</pre>");

			out.println("</body>");
			out.println("</html>");
		}

	}


	/**
	 *
	 */
	public void render(
		HttpServletRequest request,
		WebReportContext webReportContext,
		PrintWriter writer
		)
	{
		String toolbarId = "toolbar_" + request.getSession().getId() + "_" + (int)(Math.random() * 99999);
		
		writer.write(getHeader(request, webReportContext, toolbarId));
		writer.write(getBody(request, webReportContext, toolbarId));
		writer.write(getFooter());
	}

	protected String getCurrentUrl(HttpServletRequest request, WebReportContext webReportContext) 
	{
		String newQueryString = request.getQueryString();
		//return request.getContextPath() + request.getServletPath() + "?" + newQueryString + "&" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId();
		//return request.getContextPath() + ReportServlet.DEFAULT_PATH + "?" + newQueryString + "&" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId();
		String reportUrl = WebUtil.getInstance(getJasperReportsContext()).getReportInteractionPath();
		return request.getContextPath() + reportUrl + "?" + newQueryString + "&" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId();
	}


	protected String getHeader(HttpServletRequest request, WebReportContext webReportContext, String toolbarId)
	{
		Map<String, Object> contextMap = new HashMap<String, Object>();
		WebUtil webUtil = WebUtil.getInstance(getJasperReportsContext());
		String webResourcesBasePath = webUtil.getResourcesBasePath();
		contextMap.put("contextPath", request.getContextPath());
		contextMap.put("jasperreports_global_js", request.getContextPath() + webUtil.getResourcePath(webResourcesBasePath, RESOURCE_JR_GLOBAL_JS));
		contextMap.put("jasperreports_reportViewerToolbar_js", request.getContextPath() + webUtil.getResourcePath(webResourcesBasePath, RESOURCE_VIEWER_TOOLBAR_JS));
		contextMap.put("jasperreports_global_css", request.getContextPath() + webUtil.getResourcePath(webResourcesBasePath, RESOURCE_JR_GLOBAL_CSS));
		contextMap.put("showToolbar", Boolean.TRUE);
		contextMap.put("toolbarId", toolbarId);
		contextMap.put("currentUrl", getCurrentUrl(request, webReportContext));

		return VelocityUtil.processTemplate(TEMPLATE_HEADER, contextMap);
	}
	
	protected String getBody(HttpServletRequest request, WebReportContext webReportContext, String toolbarId) {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		
		@SuppressWarnings("unchecked")
		Enumeration<String> paramsEnum = request.getParameterNames();
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		while(paramsEnum.hasMoreElements()) {
			String param = paramsEnum.nextElement();
			paramsMap.put(param, request.getParameter(param));
		}
		paramsMap.put(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID, String.valueOf(webReportContext.getId()));
//		paramsMap.put(ReportServlet.REQUEST_PARAMETER_TOOLBAR_ID, toolbarId);
		
		//contextMap.put("reportUrl", request.getContextPath() + request.getServletPath());
		//contextMap.put("reportUrl", request.getContextPath() + ReportServlet.DEFAULT_PATH);
		String reportUrl = WebUtil.getInstance(getJasperReportsContext()).getReportInteractionPath();
		//contextMap.put("contextPath", request.getContextPath());
		contextMap.put("reportUrl", request.getContextPath() + reportUrl);
		contextMap.put("jsonParamsObject", JacksonUtil.getInstance(getJasperReportsContext()).getEscapedJsonString(paramsMap));
		contextMap.put("toolbarId", toolbarId);
		
		return VelocityUtil.processTemplate(TEMPLATE_BODY, contextMap);
	}

	protected String getFooter() 
	{
		return VelocityUtil.processTemplate(TEMPLATE_FOOTER, new HashMap<String, Object>());
	}

}
