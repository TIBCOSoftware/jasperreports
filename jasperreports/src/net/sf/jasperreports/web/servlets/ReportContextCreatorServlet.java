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
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.web.JRInteractiveException;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Narcis Marcu(nmarcu@users.sourceforge.net)
 */
public class ReportContextCreatorServlet extends AbstractServlet {
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private static final Log log = LogFactory.getLog(ReportContextCreatorServlet.class);

	/**
	 *
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType(JSON_CONTENT_TYPE);
		setNoExpire(response);

		String reportUri = request.getParameter(WebUtil.REQUEST_PARAMETER_REPORT_URI);
		PrintWriter out = response.getWriter();

		// get the contextid and run the report
		if (reportUri != null && request.getHeader("accept").indexOf(JSON_ACCEPT_HEADER) >= 0) {

			WebReportContext webReportContext = WebReportContext.getInstance(request);

			// begin: run report
			JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext.getParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);

			if (jasperPrintAccessor == null) {
				webReportContext.setParameterValue(WebUtil.REQUEST_PARAMETER_REPORT_URI, reportUri);

				String async = request.getParameter(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT);
				if (async != null) {
					webReportContext.setParameterValue(WebUtil.REQUEST_PARAMETER_ASYNC_REPORT, Boolean.valueOf(async));
				}

				String appDomain = request.getParameter(ReportContext.REQUEST_PARAMETER_APPLICATION_DOMAIN);
				if (appDomain != null) {
					if (appDomain.endsWith("/")) {
						appDomain = appDomain.substring(0, appDomain.length() - 1);
					}
					webReportContext.setParameterValue(ReportContext.REQUEST_PARAMETER_APPLICATION_DOMAIN, appDomain);
				}

				Controller controller = new Controller(getJasperReportsContext());
				initWebContext(request, webReportContext);
				try {
					controller.runReport(webReportContext, null);
				} catch (JRInteractiveException e) {
					// there's no action running at this point, so nothing to do
					log.error("Error on report execution", e);
					throw new JRRuntimeException(e);
				} catch (JRException e) {
					log.error("Error on report execution", e);

					response.setStatus(404);
					out.println("{\"msg\": \"JasperReports encountered an error on context creation!\",");
					out.println("\"devmsg\": \"" + JRStringUtil.escapeJavaStringLiteral(e.getMessage()) + "\"}");
					return;
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			// end: run report

			out.println("{\"contextid\": " + webReportContext.getId() + "}");

		} else {
			response.setStatus(400);
			out.println("{\"msg\": \"Wrong parameters!\"}");
		}
	}

	protected void initWebContext(HttpServletRequest request, WebReportContext webReportContext) {

	}

}
