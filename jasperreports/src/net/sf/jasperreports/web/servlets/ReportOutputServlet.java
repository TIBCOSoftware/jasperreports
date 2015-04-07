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
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JsonExporter;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.ReportExecutionHyperlinkProducerFactory;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import net.sf.jasperreports.web.util.WebUtil;


/**
 * @author Narcis Marcu(nmarcu@users.sourceforge.net)
 */
public class ReportOutputServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final String TEMPLATE_HEADER= "net/sf/jasperreports/web/servlets/resources/templates/HeaderTemplate.vm";
	private static final String TEMPLATE_BETWEEN_PAGES= "net/sf/jasperreports/web/servlets/resources/templates/BetweenPagesTemplate.vm";
	private static final String TEMPLATE_FOOTER= "net/sf/jasperreports/web/servlets/resources/templates/FooterTemplate.vm";

	private static final String TEMPLATE_HEADER_NOPAGES = "net/sf/jasperreports/web/servlets/resources/templates/HeaderTemplateNoPages.vm";
	private static final String TEMPLATE_FOOTER_NOPAGES = "net/sf/jasperreports/web/servlets/resources/templates/FooterTemplateNoPages.vm";


	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		setNoExpire(response);
		
		String contextId = request.getParameter(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID);

		// render the html for a report
		if (contextId != null && request.getHeader("accept").indexOf(HTML_ACCEPT_HEADER) >= 0) 
		{
			WebReportContext webReportContext = WebReportContext.getInstance(request, false);
			if (webReportContext != null) 
			{
				response.setContentType(HTML_CONTENT_TYPE);
				PrintWriter out = response.getWriter();
				try
				{
					render(request, response, webReportContext, out);
				}
				catch (JRException e) 
				{
					response.setContentType(JSON_CONTENT_TYPE);//FIXMEJIVE probably can't change contentType at this point, because getWriter() was already called once
					response.setStatus(404);
					response.getWriter().println("{\"msg\": \"JasperReports encountered an error!\"}");
					return;
				}
				catch (Exception e)
				{
					response.setContentType(JSON_CONTENT_TYPE);//FIXMEJIVE probably can't change contentType at this point, because getWriter() was already called once
					response.setStatus(404);
					out.println("{\"msg\": \"JasperReports encountered an error on report rendering!\"");

					String message = e.getMessage();
					if (message == null && e.getCause() != null) {
						message = e.getCause().getMessage();
					}
					if (message == null) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						message = sw.toString();
					}

					if (message != null) {
						out.println(", \"devmsg\": \"" + JRStringUtil.escapeJavaStringLiteral(message) + "\"");
					}
					out.println("}");
				}
			}
			else 
			{
				response.setContentType(JSON_CONTENT_TYPE);
				response.setStatus(404);
				response.getWriter().println("{\"msg\": \"Resource with id '" + contextId + "' not found!\"}");
				return;
			}
		}
		else
		{
			response.setContentType(JSON_CONTENT_TYPE);
			response.setStatus(400);
			response.getWriter().println("{\"msg\": \"Wrong parameters!\"}");
		}
	}


	/**
	 * 
	 */
	public void render(
			HttpServletRequest request,
			HttpServletResponse response,
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
		
//		JRXhtmlExporter exporter = new JRXhtmlExporter(getJasperReportsContext());
		HtmlExporter exporter = new HtmlExporter(getJasperReportsContext());

		SimpleHtmlExporterConfiguration exporterConfig = new SimpleHtmlExporterConfiguration();
		SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();

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
			
			reportConfig.setPageIndex(pageIdx);
		}
		else
		{
			pageStatus = ReportPageStatus.PAGE_FINAL;
		}
		
		// set report status on response header
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		WebUtil webUtil = WebUtil.getInstance(getJasperReportsContext());
		boolean isComponentMetadataEmbedded = webUtil.isComponentMetadataEmbedded();
		result.put("reportStatus", reportStatus.getStatus().toString().toLowerCase());
		result.put("totalPages", reportStatus.getTotalPageCount());
		result.put("partialPageCount", reportStatus.getCurrentPageCount());
		result.put("pageFinal", pageStatus.isPageFinal());
		result.put("isComponentMetadataEmbedded", isComponentMetadataEmbedded);
		if (!pageStatus.isPageFinal()) {
			result.put("pageTimestamp", String.valueOf(pageStatus.getTimestamp()));
		}
		response.setHeader("jasperreports-report-status", JacksonUtil.getInstance(getJasperReportsContext()).getJsonString(result));
		
		exporter.setReportContext(webReportContext);
		exporter.setExporterInput(new SimpleExporterInput(jasperPrintAccessor.getJasperPrint()));

		SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(writer);

		String applicationDomain = (String) webReportContext.getParameterValue(WebReportContext.REQUEST_PARAMETER_APPLICATION_DOMAIN);
		if (applicationDomain == null) {
			applicationDomain = request.getContextPath();
		}

		String resourcesPath = applicationDomain + webUtil.getResourcesPath() + "?" + WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID + "=" + webReportContext.getId();
		output.setImageHandler(new WebHtmlResourceHandler(resourcesPath + "&image={0}"));
		output.setResourceHandler(new WebHtmlResourceHandler(resourcesPath + "/{0}"));
		output.setFontHandler(new WebHtmlResourceHandler(resourcesPath + "&font={0}"));
		exporter.setExporterOutput(output);

		exporterConfig.setHtmlHeader(getHeader(request, webReportContext, hasPages, pageStatus));
		exporterConfig.setBetweenPagesHtml(getBetweenPages(request, webReportContext));
		exporterConfig.setHtmlFooter(getFooter(request, webReportContext, hasPages, pageStatus, isComponentMetadataEmbedded));

		reportConfig.setHyperlinkProducerFactory(
			ReportExecutionHyperlinkProducerFactory.getInstance(getJasperReportsContext(), request)
			);
		
		exporter.setConfiguration(exporterConfig);
		exporter.setConfiguration(reportConfig);
		
		exporter.exportReport();

		// embed component JSON metadata into report's HTML output
		if (isComponentMetadataEmbedded) {
			JsonExporter jsonExporter = new JsonExporter(getJasperReportsContext());
			StringWriter sw = new StringWriter();

			jsonExporter.setReportContext(webReportContext);
			jsonExporter.setExporterInput(new SimpleExporterInput(jasperPrintAccessor.getJasperPrint()));
			jsonExporter.setExporterOutput(new SimpleWriterExporterOutput(sw));
			jsonExporter.exportReport();

			String serializedJson = sw.getBuffer().toString();
			writer.write("<span id=\"reportComponents\" style=\"display:none\">" + serializedJson.replaceAll("\\s","") + "</span></div>");
		}
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
			ReportPageStatus pageStatus, boolean isComponentMetadataEmbedded)
	{
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("isComponentMetadataEmbedded", isComponentMetadataEmbedded);
		if (hasPages) {
			return VelocityUtil.processTemplate(TEMPLATE_FOOTER, contextMap);
		} else 
		{
			return VelocityUtil.processTemplate(TEMPLATE_FOOTER_NOPAGES, contextMap);
		}
	}



}
