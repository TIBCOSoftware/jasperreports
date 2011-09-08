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
package net.sf.jasperreports.web.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.web.servlets.ReportServlet;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ReportExecutionHyperlinkProducer implements JRHyperlinkProducer
{
	private HttpServletRequest request;
	
	/**
	 *
	 */
	private ReportExecutionHyperlinkProducer(HttpServletRequest request)
	{
		this.request = request;
	}

	/**
	 *
	 */
	public static ReportExecutionHyperlinkProducer getInstance(HttpServletRequest request)
	{
		return new ReportExecutionHyperlinkProducer(request);
	}


	/**
	 *
	 */
	public String getHyperlink(JRPrintHyperlink hyperlink) 
	{
		String appContext = request.getContextPath();
		String servletPath = request.getServletPath();
		String reportUri = request.getParameter(ReportServlet.REQUEST_PARAMETER_REPORT_URI);
		String reportAction = null;//request.getParameter(FillServlet.REPORT_ACTION);
		String reportActionData = null;//request.getParameter(FillServlet.REPORT_ACTION_DATA);
		
		StringBuffer allParams = new StringBuffer();
		
		if (hyperlink.getHyperlinkParameters() != null)
		{
			List parameters = hyperlink.getHyperlinkParameters().getParameters();
			if (parameters != null)
			{
				for (int i = 0; i < parameters.size(); i++)
				{
					JRPrintHyperlinkParameter parameter = (JRPrintHyperlinkParameter)parameters.get(i);
					if (ReportServlet.REQUEST_PARAMETER_REPORT_URI.equals(parameter.getName()))
					{
						reportUri = (String)parameter.getValue();
					}
//					else if (FillServlet.REPORT_ACTION.equals(parameter.getName()))
//					{
//						reportAction = (String)parameter.getValue();
//					}
//					else if (FillServlet.REPORT_ACTION_DATA.equals(parameter.getName()))
//					{
//						reportActionData = (String)parameter.getValue();
//					}
					else if (parameter.getValue() != null)
					{
						allParams.append("&").append(parameter.getName()).append("=").append(parameter.getValue());
					}
				}
			}
		}
		
		return 
			appContext + (servletPath != null ? servletPath : "")
				+ "?" + ReportServlet.REQUEST_PARAMETER_REPORT_URI + "=" + reportUri 
//				+ (reportAction == null ? "" : "&" + FillServlet.REPORT_ACTION + "=" + reportAction) 
//				+ (reportActionData == null ? "" : "&" + FillServlet.REPORT_ACTION_DATA + "=" + reportActionData)
				+ allParams.toString();
	}

}
