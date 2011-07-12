/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
import java.io.StringWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.ReportExecutionHyperlinkProducerFactory;
import net.sf.jasperreports.web.util.VelocityUtil;

import org.apache.velocity.VelocityContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: HtmlServlet.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class NoDecorationViewer
{
	/**
	 *
	 */
	public static final JRHtmlExporterParameter HTTP_REQUEST = new JRHtmlExporterParameter("HTTP Request");

	private static final String RESOURCE_GLOBAL_JS = "net/sf/jasperreports/web/servlets/resources/global.js";
	private static final String RESOURCE_GLOBAL_CSS = "net/sf/jasperreports/web/servlets/resources/global.css";

	public static final String APPLICATION_CONTEXT_PATH_VAR = "APPLICATION_CONTEXT_PATH";

	public static final String REQUEST_PARAMETER_PAGE = "jr.page";
	public static final String PARAMETER_TOOLBAR = "toolbar";
	public static final String PARAMETER_IS_AJAX= "isajax";
	
	public static final String TEMPLATE_HEADER= "net/sf/jasperreports/web/servlets/resources/dashboard/HeaderTemplate.vm";
	public static final String TEMPLATE_BETWEEN_PAGES= "net/sf/jasperreports/web/servlets/resources/dashboard/BetweenPagesTemplate.vm";
	public static final String TEMPLATE_FOOTER= "net/sf/jasperreports/web/servlets/resources/dashboard/FooterTemplate.vm";
	public static final String TEMPLATE_EXCEPTION= "net/sf/jasperreports/web/servlets/resources/dashboard/ExceptionTemplate.vm";
	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response,
		WebReportContext webReportContext
		) throws IOException, ServletException
	{
//		ServletContext context = this.getServletConfig().getServletContext();

		response.setContentType("text/html");
		
		// Set to expire far in the past.
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		PrintWriter out = response.getWriter();

		try
		{
			JasperPrint jasperPrint = (JasperPrint)webReportContext.getParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT);
			
			JRXhtmlExporter exporter = new JRXhtmlExporter();
		
//			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION-_ATTRIBUTE, jasperPrint);
			
			String reportPage = request.getParameter(REQUEST_PARAMETER_PAGE);
			
			if (reportPage == null) 
			{
				exporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.valueOf(0));
			}
			else
			{
				exporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.parseInt(reportPage));
			}

			exporter.setParameter(HTTP_REQUEST, request);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId() + "&image=");
			
			// HEADER
			VelocityContext headerContext = new VelocityContext();
			headerContext.put("isAjax", request.getParameter(PARAMETER_IS_AJAX) != null && request.getParameter(PARAMETER_IS_AJAX).equals("true"));
			headerContext.put("contextPath", request.getContextPath());
			headerContext.put("globaljs", request.getContextPath() + ResourceServlet.DEFAULT_CONTEXT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=" + RESOURCE_GLOBAL_JS);
			headerContext.put("globalcss", request.getContextPath() + ResourceServlet.DEFAULT_CONTEXT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=" + RESOURCE_GLOBAL_CSS);
//			headerContext.put("showToolbar", request.getParameter(PARAMETER_TOOLBAR) != null && request.getParameter(PARAMETER_TOOLBAR).equals("true"));
			headerContext.put("showToolbar", Boolean.TRUE);
			headerContext.put("toolbarId", "toolbar_" + request.getSession().getId() + "_" + (int)(Math.random() * 99999));
			headerContext.put("currentUrl", getCurrentUrl(request));
			headerContext.put("totalPages", jasperPrint.getPages().size());
			headerContext.put("currentPage", (reportPage != null ? reportPage : "0"));
			exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, VelocityUtil.processTemplate(TEMPLATE_HEADER, headerContext));
			
			// BETWEEN PAGES
			VelocityContext betweenPagesContext = new VelocityContext();
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, VelocityUtil.processTemplate(TEMPLATE_BETWEEN_PAGES, betweenPagesContext));
			
			// FOOTER
			VelocityContext footerContext = new VelocityContext();
			exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, VelocityUtil.processTemplate(TEMPLATE_FOOTER, footerContext));

			
			exporter.setParameter(
				JRHtmlExporterParameter.HYPERLINK_PRODUCER_FACTORY, 
				ReportExecutionHyperlinkProducerFactory.getInstance(request, response)
				);
			
			exporter.exportReport();
		}
		catch (JRException e)
		{
			StringWriter stackTraceWriter = new StringWriter(128);
			e.printStackTrace(new PrintWriter(stackTraceWriter));
			stackTraceWriter.flush();
			stackTraceWriter.close();
			VelocityContext exceptionContext = new VelocityContext();
			exceptionContext.put("stackTrace", stackTraceWriter.getBuffer().toString());

			out.print(VelocityUtil.processTemplate(TEMPLATE_EXCEPTION, exceptionContext));
		}
	}

	private String getCurrentUrl(HttpServletRequest request) {
		String newQueryString = "";
		
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (!paramName.equals(NoDecorationViewer.REQUEST_PARAMETER_PAGE)) {
				newQueryString += paramName + "=" + request.getParameter(paramName) + "&";
			}
		}
		
		return request.getContextPath() + request.getServletPath() + "?" + newQueryString.substring(0, newQueryString.lastIndexOf("&"));
	}

}
