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

import net.sf.jasperreports.components.sort.SortElement;
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
import net.sf.jasperreports.web.util.UrlUtil;
import net.sf.jasperreports.web.util.VelocityUtil;

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
	
	private static final String RESOURCE_GLOBAL_JS = "net/sf/jasperreports/web/servlets/resources/global.js";
	private static final String RESOURCE_GLOBAL_CSS = "net/sf/jasperreports/web/servlets/resources/global.css";

	private static final String PARAMETER_IS_AJAX= "isajax";
	
	private static final String TEMPLATE_HEADER= "net/sf/jasperreports/web/servlets/resources/templates/HeaderTemplate.vm";
	private static final String TEMPLATE_BETWEEN_PAGES= "net/sf/jasperreports/web/servlets/resources/templates/BetweenPagesTemplate.vm";
	private static final String TEMPLATE_FOOTER= "net/sf/jasperreports/web/servlets/resources/templates/FooterTemplate.vm";
	
	protected static final String TEMPLATE_HEADER_NOPAGES = "net/sf/jasperreports/web/servlets/resources/templates/HeaderTemplateNoPages.vm";
	protected static final String TEMPLATE_FOOTER_NOPAGES = "net/sf/jasperreports/web/servlets/resources/templates/FooterTemplateNoPages.vm";
	
	public static final String DEFAULT_PATH = "/servlets/report";//FIXMEJIVE the use of this constant is not right

	public static final String REQUEST_PARAMETER_REPORT_URI = "jr.uri";
	public static final String REQUEST_PARAMETER_IGNORE_PAGINATION = "jr.ignrpg";
	public static final String REQUEST_PARAMETER_RUN_REPORT = "jr.run";
	public static final String REQUEST_PARAMETER_REPORT_VIEWER = "jr.vwr";
	public static final String REQUEST_PARAMETER_ASYNC = "jr.async";
	public static final String REQUEST_PARAMETER_ACTION = "jr.action";
	public static final String REQUEST_PARAMETER_TOOLBAR_ID = "jr.toolbar";
	public static final String REQUEST_PARAMETER_PAGE = "jr.page";
	public static final String REQUEST_PARAMETER_PAGE_TIMESTAMP = "jr.pagetimestamp";


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
		) throws JRException //IOException, ServletException
, JRInteractiveException
	{
		JasperPrintAccessor jasperPrintAccessor = 
			(JasperPrintAccessor) webReportContext.getParameterValue(
				WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR
			);

		String run = request.getParameter(REQUEST_PARAMETER_RUN_REPORT);
		if (jasperPrintAccessor == null || Boolean.valueOf(run))
		{
			String reportUri = request.getParameter(REQUEST_PARAMETER_REPORT_URI);
			if (reportUri != null)
			{
				webReportContext.setParameterValue(REQUEST_PARAMETER_REPORT_URI, reportUri);
			}
			
			Boolean isIgnorePagination = Boolean.valueOf(request.getParameter(REQUEST_PARAMETER_IGNORE_PAGINATION));
			if (isIgnorePagination != null) 
			{
				webReportContext.setParameterValue(JRParameter.IS_IGNORE_PAGINATION, isIgnorePagination);
			}		
			
			String async = request.getParameter(REQUEST_PARAMETER_ASYNC);
			if (async != null)
			{
				webReportContext.setParameterValue(REQUEST_PARAMETER_ASYNC, Boolean.valueOf(async));
			}

			Action action = getAction(webReportContext, UrlUtil.urlDecode(request.getParameter(REQUEST_PARAMETER_ACTION)));

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
		
		Integer pageCount = reportStatus.getTotalPageCount();
		// if the page count is null, it means that the fill is not yet done but there is at least a page
		boolean hasPages = pageCount == null || pageCount > 0;//FIXMEJIVE we should call pageStatus here
		
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
			ReportExecutionHyperlinkProducerFactory.getInstance(request)
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
		if (hasPages) 
		{
			String webResourcesBasePath = JRPropertiesUtil.getInstance(getJasperReportsContext()).getProperty("net.sf.jasperreports.web.resources.base.path");//FIXMEJIVE reuse this code
			if (webResourcesBasePath == null)
			{
				webResourcesBasePath = request.getContextPath() + ResourceServlet.DEFAULT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=";
			}
			contextMap.put("isAjax", request.getParameter(PARAMETER_IS_AJAX) != null && request.getParameter(PARAMETER_IS_AJAX).equals("true"));
			contextMap.put("contextPath", request.getContextPath());
			contextMap.put("globaljs", webResourcesBasePath + RESOURCE_GLOBAL_JS);
			contextMap.put("globalcss", webResourcesBasePath + RESOURCE_GLOBAL_CSS);
	//		headerContext.put("showToolbar", request.getParameter(PARAMETER_TOOLBAR) != null && request.getParameter(PARAMETER_TOOLBAR).equals("true"));
			contextMap.put("showToolbar", Boolean.TRUE);
			contextMap.put("toolbarId", "toolbar_" + request.getSession().getId() + "_" + (int)(Math.random() * 99999));
			contextMap.put("currentUrl", getCurrentUrl(request, webReportContext));
			contextMap.put("strRunReportParam", ReportServlet.REQUEST_PARAMETER_RUN_REPORT + "=false");
	
			JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext.getParameterValue(
					WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);
			contextMap.put("totalPages", jasperPrintAccessor.getReportStatus().getTotalPageCount());
	
			String reportPage = request.getParameter(REQUEST_PARAMETER_PAGE);
			contextMap.put("currentPage", (reportPage != null ? reportPage : "0"));
			
			if (!pageStatus.isPageFinal())
			{
				contextMap.put("pageTimestamp", String.valueOf(pageStatus.getTimestamp()));
			}
			
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
	protected String getCurrentUrl(HttpServletRequest request, WebReportContext webReportContext) 
	{
		String newQueryString = ReportServlet.REQUEST_PARAMETER_REPORT_URI + "=" + webReportContext.getParameterValue(ReportServlet.REQUEST_PARAMETER_REPORT_URI) + 
					"&" + SortElement.REQUEST_PARAMETER_DATASET_RUN + "=" + webReportContext.getParameterValue(SortElement.REQUEST_PARAMETER_DATASET_RUN) +
					"&" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId();
//		return request.getContextPath() + request.getServletPath() + "?" + newQueryString;
		return request.getContextPath() + ReportServlet.DEFAULT_PATH + "?" + newQueryString;
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
