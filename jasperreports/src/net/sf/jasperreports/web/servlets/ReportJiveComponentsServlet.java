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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JsonExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleJsonReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.ReportExecutionHyperlinkProducerFactory;
import net.sf.jasperreports.web.util.WebUtil;


/**
 * @author Narcis Marcu(nmarcu@users.sourceforge.net)
 */
public class ReportJiveComponentsServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		setNoExpire(response);
		response.setContentType(JSON_CONTENT_TYPE);
		
		PrintWriter out = response.getWriter();
		String contextId = request.getParameter(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID);

		if (contextId != null && request.getHeader("accept").indexOf(JSON_ACCEPT_HEADER) >= 0) {
			
			WebReportContext webReportContext = WebReportContext.getInstance(request, false);
			if (webReportContext != null) {
				try {
					getReportComponents(request, webReportContext, out);
				} catch (JRException e) {
					response.setStatus(404);
					out.println("{\"msg\": \"JasperReports encountered an error!\"}");
					return;
				}
			} else {
				response.setStatus(404);
				out.println("{\"msg\": \"Resource with id '" + contextId + "' not found!\"}");
				return;
			}
		} else {
			response.setStatus(400);
			out.println("{\"msg\": \"Wrong parameters!\"}");
		}
	}


	/**
	 * 
	 */
	public void getReportComponents(
			HttpServletRequest request,
			WebReportContext webReportContext,
			PrintWriter writer
			) throws JRException //IOException, ServletException
	{
		JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext.getParameterValue(
				WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);
		
		ReportExecutionStatus reportStatus = jasperPrintAccessor.getReportStatus();
		if (reportStatus.getStatus() == ReportExecutionStatus.Status.ERROR)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
					(Object[])null,
					reportStatus.getError());
		}
		
		//Integer pageCount = reportStatus.getTotalPageCount();
		// if the page count is null, it means that the fill is not yet done but there is at least a page
		//boolean hasPages = pageCount == null || pageCount > 0;//FIXMEJIVE we should call pageStatus here
		boolean hasPages = jasperPrintAccessor.pageStatus(0, null).pageExists();
		
		JsonExporter exporter = new JsonExporter(getJasperReportsContext());
		
		SimpleJsonReportConfiguration configuration = new SimpleJsonReportConfiguration();

		ReportPageStatus pageStatus;
		if (hasPages)
		{
			String reportPage = request.getParameter(WebUtil.REQUEST_PARAMETER_PAGE);
			int pageIdx = reportPage == null ? 0 : Integer.parseInt(reportPage);
			String pageTimestamp = request.getParameter(WebUtil.REQUEST_PARAMETER_PAGE_TIMESTAMP);
			Long timestamp = pageTimestamp == null ? null : Long.valueOf(pageTimestamp);
			
			pageStatus = jasperPrintAccessor.pageStatus(pageIdx, timestamp);
			
			if (!pageStatus.pageExists())
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_PAGE_NOT_FOUND,
						new Object[]{pageIdx});
			}
			
			configuration.setPageIndex(pageIdx);
		}
		else
		{
			pageStatus = ReportPageStatus.PAGE_FINAL;
		}
		
		exporter.setReportContext(webReportContext);
		exporter.setExporterInput(new SimpleExporterInput(jasperPrintAccessor.getJasperPrint()));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(writer));
		
		configuration.setHyperlinkProducerFactory( 
			ReportExecutionHyperlinkProducerFactory.getInstance(getJasperReportsContext(), request)
			);
		exporter.setConfiguration(configuration);
		
		exporter.exportReport();

	}


	/**
	 * 
	 */
	protected String getHeader(HttpServletRequest request, WebReportContext webReportContext, boolean hasPages, 
			ReportPageStatus pageStatus)
	{
//		Map<String, Object> contextMap = new HashMap<String, Object>();
//
//		JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext.getParameterValue(
//				WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);
//		contextMap.put("totalPages", jasperPrintAccessor.getReportStatus().getTotalPageCount());
//
//		String reportPage = request.getParameter(WebUtil.REQUEST_PARAMETER_PAGE);
//		contextMap.put("currentPage", (reportPage != null ? reportPage : "0"));
//		
//		if (!pageStatus.isPageFinal())
//		{
//			contextMap.put("pageTimestamp", String.valueOf(pageStatus.getTimestamp()));
//		}
//		
//		if (hasPages) 
//		{
//			return VelocityUtil.processTemplate(TEMPLATE_HEADER, contextMap);
//		} else 
//		{
//			return VelocityUtil.processTemplate(TEMPLATE_HEADER_NOPAGES, contextMap);
//		}
		
		return null;
	}


	/**
	 * 
	 */
	protected String getBetweenPages(HttpServletRequest request, WebReportContext webReportContext) 
	{
//		return VelocityUtil.processTemplate(TEMPLATE_BETWEEN_PAGES, new HashMap<String, Object>());
		return null;
	}


	/**
	 * 
	 */
	protected String getFooter(HttpServletRequest request, WebReportContext webReportContext, boolean hasPages, 
			ReportPageStatus pageStatus) 
	{
//		Map<String, Object> contextMap = new HashMap<String, Object>();
//		if (hasPages) {
//			return VelocityUtil.processTemplate(TEMPLATE_FOOTER, contextMap);
//		} else 
//		{
//			return VelocityUtil.processTemplate(TEMPLATE_FOOTER_NOPAGES, contextMap);
//		}
		
		return null;
	}

}
