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
package net.sf.jasperreports.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.ReportContext;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class WebReportContext implements ReportContext
{
	/**
	 *
	 */
	private static final String SESSION_ATTRIBUTE_REPORT_CONTEXT_ID_PREFIX = "net.sf.jasperreports.web.report.context_";
	public static final String REQUEST_PARAMETER_REPORT_CONTEXT_ID = "jr.ctxid";

	public static final String REPORT_CONTEXT_PARAMETER_JASPER_PRINT = "net.sf.jasperreports.web.jasper_print";
	//public static final String REPORT_CONTEXT_PARAMETER_JASPER_REPORT = "net.sf.jasperreports.web.jasper_report";
	
	/**
	 *
	 */
	//private ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();//FIXMEJIVE
	private HttpServletRequest request;
	private Map<String, Object> parameterValues;
	private String id;
	
	/**
	 *
	 */
	public static final WebReportContext getInstance(HttpServletRequest request)
	{
		return getInstance(request, true);
	}

	/**
	 *
	 */
	public static final WebReportContext getInstance(HttpServletRequest request, boolean create)
	{
		WebReportContext webReportContext = null;

		String reportContextId = request.getParameter(REQUEST_PARAMETER_REPORT_CONTEXT_ID);
		if (reportContextId != null)
		{
			webReportContext = (WebReportContext)request.getSession().getAttribute(getSessionAttributeName(reportContextId));
		}

		if (webReportContext == null && create)
		{
			webReportContext = new WebReportContext();
			request.getSession().setAttribute(webReportContext.getSessionAttributeName(), webReportContext); //FIXMEJIVE use FIFO accessor
		}
		
		if (webReportContext != null)
		{
			webReportContext.setRequest(request);
			webReportContext.setParameterValue("net.sf.jasperreports.web.app.context.path", request.getContextPath());
			webReportContext.setParameterValue(JRParameter.REPORT_CONTEXT, webReportContext);
		}
		
		return webReportContext;
	}

	/**
	 *
	 */
	private WebReportContext()
	{
		parameterValues = new HashMap<String, Object>();
//		parameterValues.put(JRParameter.REPORT_CONTEXT, this);
	}

	/**
	 *
	 */
	public String getId()
	{
		if (id == null)
		{
			id = String.valueOf(System.currentTimeMillis());//FIXMEJIVE make stronger?
		}
		return id;
	}

	/**
	 *
	 */
	public HttpServletRequest getRequest()
	{
		//return threadLocalRequest.get();
		return request;
	}

	/**
	 *
	 */
	public void setRequest(HttpServletRequest request)
	{
		//threadLocalRequest.set(request);
		this.request = request;
	}

	/**
	 *
	 */
	public String getSessionAttributeName()
	{
		return getSessionAttributeName(getId());
	}

	/**
	 *
	 */
	public Object getParameterValue(String parameterName)
	{
		HttpServletRequest request = getRequest();
		String requestParameterValue = request.getParameter(parameterName);
		if (requestParameterValue != null)
		{
			return requestParameterValue;
		}
		
		return parameterValues.get(parameterName);
	}

	/**
	 *
	 */
	public boolean containsParameter(String parameterName)
	{
		HttpServletRequest request = getRequest();
		String requestParameterValue = request.getParameter(parameterName);
		if (requestParameterValue != null)
		{
			return true;
		}
		
		return parameterValues.containsKey(parameterName);
	}

	/**
	 *
	 */
	public void setParameterValue(String parameterName, Object value)
	{
		parameterValues.put(parameterName, value);
	}

	/**
	 *
	 */
	public void setParameterValues(Map<String, Object> newValues)
	{
		parameterValues.putAll(newValues);
	}

	/**
	 *
	 */
	public Map<String, Object> getParameterValues()
	{
		return parameterValues;
	}

	/**
	 *
	 */
	private static final String getSessionAttributeName(String id)
	{
		return SESSION_ATTRIBUTE_REPORT_CONTEXT_ID_PREFIX + id;
	}
}
