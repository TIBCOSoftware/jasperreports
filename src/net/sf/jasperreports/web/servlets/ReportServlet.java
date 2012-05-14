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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.web.JRInteractiveException;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.actions.AbstractAction;
import net.sf.jasperreports.web.actions.Action;
import net.sf.jasperreports.web.actions.MultiAction;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.ReportExecutionHyperlinkProducerFactory;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ReportServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Log log = LogFactory.getLog(ReportServlet.class);
		
	private static final String TEMPLATE_HEADER= "net/sf/jasperreports/web/servlets/resources/templates/HeaderTemplate.vm";
	private static final String TEMPLATE_BETWEEN_PAGES= "net/sf/jasperreports/web/servlets/resources/templates/BetweenPagesTemplate.vm";
	private static final String TEMPLATE_FOOTER= "net/sf/jasperreports/web/servlets/resources/templates/FooterTemplate.vm";
	
	private static final String TEMPLATE_HEADER_NOPAGES = "net/sf/jasperreports/web/servlets/resources/templates/HeaderTemplateNoPages.vm";
	private static final String TEMPLATE_FOOTER_NOPAGES = "net/sf/jasperreports/web/servlets/resources/templates/FooterTemplateNoPages.vm";
	
	private static final String REQUEST_PARAMETER_IGNORE_PAGINATION = "jr.ignrpg";
	private static final String REQUEST_PARAMETER_ACTION = "jr.action";
	private static final String REQUEST_PARAMETER_PAGE = "jr.page";
	private static final String REQUEST_PARAMETER_PAGE_TIMESTAMP = "jr.pagetimestamp";


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
			runReport(request, webReportContext);
			render(request, webReportContext, out);
		}
		catch (JRInteractiveException e) 
		{
			log.error("Jasper Interactive error", e);
			
			out.println("<div><pre id=\"jrInteractiveError\">");
			out.println(e.getMessage());
			out.println("</pre></div>");
		}
		catch (Exception e)
		{
			log.error("Error on report execution", e);
			
			out.println("<html>");//FIXMEJIVE do we need to render this? or should this be done by the viewer?
			out.println("<head>");
			out.println("<title>JasperReports - Web Application Sample</title>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
			
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
	 * @throws JRInteractiveException 
	 *
	 */
	public void runReport(
		HttpServletRequest request, //FIXMEJIVE put request in report context, maybe as a thread local?
		WebReportContext webReportContext
		) throws JRException, JRInteractiveException //IOException, ServletException
	{
		JasperPrintAccessor jasperPrintAccessor = 
			(JasperPrintAccessor) webReportContext.getParameterValue(
				WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR
			);

		JRPropertiesUtil propUtil = JRPropertiesUtil.getInstance(getJasperReportsContext());
		String runReportParamName = propUtil.getProperty(WebUtil.PROPERTY_REQUEST_PARAMETER_RUN_REPORT);
		String runReport = request.getParameter(runReportParamName);
		if (jasperPrintAccessor == null || Boolean.valueOf(runReport))
		{
			String reportUriParamName = propUtil.getProperty(WebUtil.PROPERTY_REQUEST_PARAMETER_REPORT_URI);
			String reportUri = request.getParameter(reportUriParamName);
			if (reportUri != null)
			{
				webReportContext.setParameterValue(reportUriParamName, reportUri);
			}
			
			Boolean isIgnorePagination = Boolean.valueOf(request.getParameter(REQUEST_PARAMETER_IGNORE_PAGINATION));
			if (isIgnorePagination != null) 
			{
				webReportContext.setParameterValue(JRParameter.IS_IGNORE_PAGINATION, isIgnorePagination);
			}		
			
			String asyncParamName = propUtil.getProperty(WebUtil.PROPERTY_REQUEST_PARAMETER_ASYNC_REPORT);
			String async = request.getParameter(asyncParamName);
			if (async != null)
			{
				webReportContext.setParameterValue(asyncParamName, Boolean.valueOf(async));
			}

			Action action = getAction(webReportContext, WebUtil.decodeUrl(request.getParameter(REQUEST_PARAMETER_ACTION)));

			Controller controller = new Controller(getJasperReportsContext());
			
			controller.runReport(webReportContext, action);
		}
	}


	/**
	 * 
	 */
	public void render(
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
			throw new JRRuntimeException("Error occurred during report generation", reportStatus.getError());
		}
		
		//Integer pageCount = reportStatus.getTotalPageCount();
		// if the page count is null, it means that the fill is not yet done but there is at least a page
		//boolean hasPages = pageCount == null || pageCount > 0;//FIXMEJIVE we should call pageStatus here
		boolean hasPages = jasperPrintAccessor.pageStatus(0, null).pageExists();
		
		JRXhtmlExporter exporter = new JRXhtmlExporter(getJasperReportsContext());

		ReportPageStatus pageStatus;
		if (hasPages)
		{
			String reportPage = request.getParameter(REQUEST_PARAMETER_PAGE);
			int pageIdx = reportPage == null ? 0 : Integer.parseInt(reportPage);
			String pageTimestamp = request.getParameter(REQUEST_PARAMETER_PAGE_TIMESTAMP);
			Long timestamp = pageTimestamp == null ? null : Long.valueOf(pageTimestamp);
			
			pageStatus = jasperPrintAccessor.pageStatus(pageIdx, timestamp);
			
			if (!pageStatus.pageExists())
			{
				throw new JRRuntimeException("Page " + pageIdx + " not found in report");
			}
			
			exporter.setParameter(JRExporterParameter.PAGE_INDEX, pageIdx);
		}
		else
		{
			pageStatus = ReportPageStatus.PAGE_FINAL;
		}
		
		exporter.setReportContext(webReportContext);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrintAccessor.getJasperPrint());
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, writer);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId() + "&image=");
		
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, getHeader(request, webReportContext, hasPages, pageStatus));
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, getBetweenPages(request, webReportContext));
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, getFooter(request, webReportContext, hasPages, pageStatus));
		
		exporter.setParameter(
			JRHtmlExporterParameter.HYPERLINK_PRODUCER_FACTORY, 
			ReportExecutionHyperlinkProducerFactory.getInstance(getJasperReportsContext(), request)
			);
		
		//TODO lucianc do not export if the page has not modified
		exporter.exportReport();

	}


	/**
	 * 
	 */
	protected String getHeader(HttpServletRequest request, WebReportContext webReportContext, boolean hasPages, 
			ReportPageStatus pageStatus)
	{
		Map<String, Object> contextMap = new HashMap<String, Object>();

		JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext.getParameterValue(
				WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);
		contextMap.put("totalPages", jasperPrintAccessor.getReportStatus().getTotalPageCount());

		String reportPage = request.getParameter(REQUEST_PARAMETER_PAGE);
		contextMap.put("currentPage", (reportPage != null ? reportPage : "0"));
		
		if (!pageStatus.isPageFinal())
		{
			contextMap.put("pageTimestamp", String.valueOf(pageStatus.getTimestamp()));
		}
		
		if (hasPages) 
		{
			return VelocityUtil.processTemplate(TEMPLATE_HEADER, contextMap);
		} else 
		{
			return VelocityUtil.processTemplate(TEMPLATE_HEADER_NOPAGES, contextMap);
		}
	}


	/**
	 * 
	 */
	protected String getBetweenPages(HttpServletRequest request, WebReportContext webReportContext) 
	{
		return VelocityUtil.processTemplate(TEMPLATE_BETWEEN_PAGES, new HashMap<String, Object>());
	}


	/**
	 * 
	 */
	protected String getFooter(HttpServletRequest request, WebReportContext webReportContext, boolean hasPages, 
			ReportPageStatus pageStatus) 
	{
		Map<String, Object> contextMap = new HashMap<String, Object>();
		if (hasPages) {
			return VelocityUtil.processTemplate(TEMPLATE_FOOTER, contextMap);
		} else 
		{
			return VelocityUtil.processTemplate(TEMPLATE_FOOTER_NOPAGES, contextMap);
		}
	}


	/**
	 *
	 */
	private Action getAction(ReportContext webReportContext, String jsonData)
	{
		Action result = null;
		List<AbstractAction> actions = JacksonUtil.getInstance(getJasperReportsContext()).loadAsList(jsonData, AbstractAction.class);
		if (actions != null)
		{
			if (actions.size() == 1) {
				result = actions.get(0);
			} else if (actions.size() > 1){
				result = new MultiAction(actions);
			}
			
			((AbstractAction)result).init(getJasperReportsContext(), webReportContext);
		}
		return result;
	}

}
