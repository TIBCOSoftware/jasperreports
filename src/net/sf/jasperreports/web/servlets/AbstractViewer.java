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

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.components.sort.SortElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.ReportExecutionHyperlinkProducerFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class AbstractViewer
{
	//public static final String APPLICATION_CONTEXT_PATH_VAR = "APPLICATION_CONTEXT_PATH";

	public static final String REQUEST_PARAMETER_PAGE = "jr.page";
	
	/**
	 *
	 */
	public void render(
		HttpServletRequest request,
		WebReportContext webReportContext,
		PrintWriter writer
		) throws JRException //IOException, ServletException
	{
		JasperPrint jasperPrint = (JasperPrint)webReportContext.getParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT);
		
		JRXhtmlExporter exporter = new JRXhtmlExporter();
	
		exporter.setReportContext(webReportContext);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, writer);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId() + "&image=");

		String reportPage = request.getParameter(REQUEST_PARAMETER_PAGE);
		if (reportPage == null) 
		{
			reportPage = "0";
		}
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.parseInt(reportPage));
		
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, getHeader(request, webReportContext));
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, getBetweenPages(request, webReportContext));
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, getFooter(request, webReportContext));
		
		exporter.setParameter(
			JRHtmlExporterParameter.HYPERLINK_PRODUCER_FACTORY, 
			ReportExecutionHyperlinkProducerFactory.getInstance(request)
			);
		
		exporter.exportReport();

//		try
//		{
//		}
//		catch (JRException e)
//		{
//			StringWriter stackTraceWriter = new StringWriter(128);
//			e.printStackTrace(new PrintWriter(stackTraceWriter));
//			stackTraceWriter.flush();
//			stackTraceWriter.close();
//			VelocityContext exceptionContext = new VelocityContext();
//			exceptionContext.put("stackTrace", stackTraceWriter.getBuffer().toString());
//
//			out.print(VelocityUtil.processTemplate(TEMPLATE_EXCEPTION, exceptionContext));
//		}
	}

	protected String getCurrentUrl(HttpServletRequest request, WebReportContext webReportContext) 
	{
		String newQueryString = "";
		
//		Enumeration<String> paramNames = request.getParameterNames();
//		while (paramNames.hasMoreElements()) {
//			String paramName = paramNames.nextElement();
//			if (!paramName.equals(AbstractViewer.REQUEST_PARAMETER_PAGE)) {
//				newQueryString += paramName + "=" + request.getParameter(paramName) + "&";
//			}
//		}
//		
//		newQueryString = newQueryString.substring(0, newQueryString.lastIndexOf("&"));
		
//		if (!newQueryString.contains(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID))
//		{
//			newQueryString += "&" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId();
//		}
		
		newQueryString = ReportServlet.REQUEST_PARAMETER_REPORT_URI + "=" + webReportContext.getParameterValue(ReportServlet.REQUEST_PARAMETER_REPORT_URI) + 
					"&" + SortElement.REQUEST_PARAMETER_DATASET_RUN + "=" + webReportContext.getParameterValue(SortElement.REQUEST_PARAMETER_DATASET_RUN) +
					"&" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId();
		return request.getContextPath() + request.getServletPath() + "?" + newQueryString;
	}

	protected abstract String getHeader(HttpServletRequest request, WebReportContext webReportContext); 

	protected abstract String getBetweenPages(HttpServletRequest request, WebReportContext webReportContext); 

	protected abstract String getFooter(HttpServletRequest request, WebReportContext webReportContext); 

}
