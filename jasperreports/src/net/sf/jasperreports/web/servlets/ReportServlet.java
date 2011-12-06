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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.web.WebReportContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ReportServlet extends HttpServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_REPOSITORY_ROOT = "net.sf.jasperreports.web.repository.root";//FIXMEJIVE make this config in file repository?
	public static final String DEFAULT_REPOSITORY_ROOT = "WEB-INF/repository";

	public static final String REQUEST_PARAMETER_REPORT_URI = "jr.uri";
	public static final String REQUEST_PARAMETER_IGNORE_PAGINATION = "jr.ignrpg";
	public static final String REQUEST_PARAMETER_RUN_REPORT = "jr.run";
	public static final String REQUEST_PARAMETER_REPORT_JRXML = "jr.jrxml";
	public static final String REQUEST_PARAMETER_REPORT_VIEWER = "jr.vwr";

//	public static final String REPORT_ACTION = "report.action";
//	public static final String REPORT_CLEAR_SESSION = "report.clear"; 
//	public static final String REPORT_CONTEXT_PREFIX = "fillContext_"; 
	

	private File repositoryRoot;
	private SimpleFileResolver fileResolver;
	
	/**
	 * 
	 */
	public File getRepositoryRoot() 
	{
		return repositoryRoot;
	}

	
	/**
	 * 
	 */
	public FileResolver getFileResolver() 
	{
		return fileResolver;
	}

	
	/**
	 * 
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		
		String repo = getInitParameter("repository.root"); //this is used in Jetty config
		if (repo == null)
		{
			repo = JRProperties.getProperty(PROPERTY_REPOSITORY_ROOT);//FIXMEJIVE constant
		}
		if (repo == null)
		{
			repo = DEFAULT_REPOSITORY_ROOT;
		}
		
		repositoryRoot = new File(repo);
		if (!repositoryRoot.isAbsolute())
		{
			String realPath = config.getServletContext().getRealPath("/");
			if (realPath != null)
			{
				repositoryRoot = new File(new File(realPath), repo);
			}
		}
		
		fileResolver = new SimpleFileResolver(repositoryRoot);
		fileResolver.setResolveAbsolutePath(true);
	}
	

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
			
			String viewer = request.getParameter(REQUEST_PARAMETER_REPORT_VIEWER);
			if (viewer == null || viewer.trim().length() == 0)
			{
				new DefaultViewer().render(request, webReportContext, out);
			}
			else
			{
				//FIXMEJIVE
				new NoDecorationViewer().render(request, webReportContext, out);
			}
		}
		catch (JRException e)
		{
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
	public void runReport(
		HttpServletRequest request, //FIXMEJIVE put request in report context, maybe as a thread local?
		WebReportContext webReportContext
		) throws JRException //IOException, ServletException
	{
		JasperPrint jasperPrint = (JasperPrint)webReportContext.getParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT);
		String run = request.getParameter(REQUEST_PARAMETER_RUN_REPORT);
		if (jasperPrint == null || Boolean.valueOf(run))
		{
			String reportUri = request.getParameter(REQUEST_PARAMETER_REPORT_URI);
			
//				Map<String, Object> parameters = (Map<String, Object>)request.getSession().getAttribute(ReportServlet.REPORT_CONTEXT_PREFIX + reportUri);  
//				if (parameters == null) 
//				{
//					parameters = new HashMap<String, Object>();
//					request.getSession().setAttribute(ReportServlet.REPORT_CONTEXT_PREFIX + reportUri, parameters);
//				}
			
			webReportContext.setParameterValue(JRParameter.REPORT_FILE_RESOLVER, getFileResolver());//FIXME create file resolver for parent folder
//				parameters.put(JRParameter.REPORT_FILE_RESOLVER, getFileResolver());//FIXME create file resolver for parent folder
			JRResourcesUtil.setThreadFileResolver(fileResolver);
			
			Boolean isIgnorePagination = Boolean.valueOf(request.getParameter(REQUEST_PARAMETER_IGNORE_PAGINATION));
			if (isIgnorePagination != null) 
			{
				webReportContext.setParameterValue(JRParameter.IS_IGNORE_PAGINATION, isIgnorePagination);
				//parameters.put(JRParameter.IS_IGNORE_PAGINATION, isIgnorePagination);
			}		
			
			JasperReport jasperReport = null; 
			
			String jrxml = request.getParameter(REQUEST_PARAMETER_REPORT_JRXML);
			if (jrxml != null && jrxml.trim().length() > 0)
			{
				jrxml = jrxml.trim();
				jasperReport =  JasperCompileManager.compileReport(new ByteArrayInputStream(jrxml.getBytes()));
			}
			else if (reportUri != null && reportUri.trim().length() > 0)
			{
				reportUri = reportUri.trim();

				jasperReport = RepositoryUtil.getReport(reportUri);
			}
			
			if (jasperReport == null)
			{
				throw new JRException("Report not found at : " + reportUri);
			}

			//webReportContext.setParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_REPORT, jasperReport);
			
//				Map parameters = new HashMap();
			
//				String clearSession = request.getParameter(REPORT_CLEAR_SESSION);
//				if (clearSession != null && clearSession.equalsIgnoreCase("true")) {
//					request.getSession().setAttribute(ReportServlet.REPORT_CONTEXT_PREFIX + reportUri, null);
//				}
			
			/* data adapter - start */
//			String dataAdapterUri = jasperReport.getProperty("net.sf.jasperreports.data.adapter");
//			if (dataAdapterUri != null)
//			{
//				//repoService.setFileResolver(fileResolver);
//
//				DataAdapter dataAdapter = (DataAdapter)RepositoryUtil.getResource(dataAdapterUri, DataAdapter.class);
//				DataAdapterService dataAdapterService = DataAdapterServiceUtil.getDataAdapterService(dataAdapter);
//				
//				Map<String, Object> dasParams = dataAdapterService.getParameters();
//				//parameters.putAll(dasParams);
//				webReportContext.setParameterValues(dasParams);
//			}
			/* data adapter - end */
			
			jasperPrint = 
				JasperFillManager.fillReport(
					jasperReport, 
					webReportContext.getParameterValues()
					);
						
			webReportContext.setParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT, jasperPrint);
		}
	}


	public static String extractReportUri(String paramReportUri) //FIXMEJIVE consider moving from here
	{
		String lcReportUri = paramReportUri.toLowerCase();
		if (lcReportUri.endsWith(".jasper"))
		{
			paramReportUri = paramReportUri.substring(0, lcReportUri.lastIndexOf(".jasper"));
		}
		else if (lcReportUri.endsWith(".jrxml"))
		{
			paramReportUri = paramReportUri.substring(0, lcReportUri.lastIndexOf(".jrxml"));
		}
		return paramReportUri;
	}
}
