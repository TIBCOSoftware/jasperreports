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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Narcis Marcu(nmarcu@users.sourceforge.net)
 */
public class ReportPageStatusServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(ReportPageStatusServlet.class);
		
	/**
	 *
	 */
	public void service(
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException, ServletException {

		response.setContentType(JSON_CONTENT_TYPE);
		setNoExpire(response);

		PrintWriter out = response.getWriter();
		String contextId = request.getParameter(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID);

		if (contextId != null && request.getHeader("accept").indexOf(JSON_ACCEPT_HEADER) >= 0) {
			WebReportContext webReportContext = WebReportContext.getInstance(request, false);

			if (webReportContext != null) {
				try {
					pageUpdate(request, response, webReportContext);
				} catch (Exception e) {
					log.error("Error on page status update", e);
					response.setStatus(404);
					out.println("{\"msg\": \"JasperReports encountered an error on page status update!\"}");
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


	protected void pageUpdate(HttpServletRequest request, HttpServletResponse response, 
			WebReportContext webReportContext) throws JRException, IOException
	{
		JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext.getParameterValue(
				WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);
		if (jasperPrintAccessor == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_REPORT_NOT_FOUND,
					(Object[])null);
		}
		
		String pageIdxParam = request.getParameter(WebUtil.REQUEST_PARAMETER_PAGE);
		Integer pageIndex = pageIdxParam == null ? null : Integer.valueOf(pageIdxParam);
		String pageTimestampParam = request.getParameter(WebUtil.REQUEST_PARAMETER_PAGE_TIMESTAMP);
		Long pageTimestamp = pageTimestampParam == null ? null : Long.valueOf(pageTimestampParam);
		
		if (log.isDebugEnabled())
		{
			log.debug("report page update check for pageIndex: " + pageIndex 
					+ ", pageTimestamp: " + pageTimestamp);
		}
		
		Map<String, Object> reportStatus = new LinkedHashMap<String, Object>();
		putReportStatusResult(response, jasperPrintAccessor, reportStatus);
		
		if (pageIndex != null && pageTimestamp != null)
		{
			ReportPageStatus pageStatus = jasperPrintAccessor.pageStatus(pageIndex, pageTimestamp);
			boolean modified = pageStatus.hasModified();
			reportStatus.put("pageModified", modified);
			reportStatus.put("pageFinal", pageStatus.isPageFinal());
			
			if (log.isDebugEnabled())
			{
				log.debug("page " + pageIndex + " modified " + modified);
			}
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", reportStatus);
		
		String resultString = JacksonUtil.getInstance(getJasperReportsContext()).getJsonString(result);
		PrintWriter out = response.getWriter();
		out.write(resultString);
		out.flush();
	}

	protected void putReportStatusResult(HttpServletResponse response,
			JasperPrintAccessor printAccessor, Map<String, Object> result) throws JRException
	{
		ReportExecutionStatus reportStatus = printAccessor.getReportStatus();
		result.put("lastPartialPageIndex", reportStatus.getCurrentPageCount() - 1);
		
		String status;
		switch (reportStatus.getStatus())
		{
		case FINISHED:
			status = "finished";
			Integer totalPageCount = reportStatus.getTotalPageCount();
			result.put("lastPageIndex", totalPageCount-1);
			
			if (log.isDebugEnabled())
			{
				log.debug("report finished " + totalPageCount + " pages");
			}
			break;
		case ERROR:
			status = "error";
			handleReportUpdateError(response, reportStatus);
			break;
		case CANCELED:
			status = "canceled";
			
			if (log.isDebugEnabled())
			{
				log.debug("report canceled");
			}
			break;
		case RUNNING:
		default:
			status = "running";
			
			if (log.isDebugEnabled())
			{
				log.debug("report running");
			}
			break;
		}
		
		result.put("status", status);
	}

	protected void handleReportUpdateError(HttpServletResponse response, ReportExecutionStatus reportStatus) 
			throws JRException
	{
		Throwable error = reportStatus.getError();
		if (log.isDebugEnabled())
		{
			log.debug("report error " + error);// only message
		}
		// set a header so that the UI knows it's a report execution error
		response.setHeader("reportError", "true");
		// set as a header because we don't have other way to pass it
		response.setHeader("lastPartialPageIndex", Integer.toString(reportStatus.getCurrentPageCount() - 1));
		
		// throw an exception to get to the error page
		if (error instanceof JRException)
		{
			throw (JRException) error;
		}
		if (error instanceof JRRuntimeException)
		{
			throw (JRRuntimeException) error;
		}
		throw 
			new JRRuntimeException(
				EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
				(Object[])null,
				error);
	}

}
