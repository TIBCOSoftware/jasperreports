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
package net.sf.jasperreports.web.util;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ReportInteractionHyperlinkProducer extends ReportExecutionHyperlinkProducer
{
	public static final String HYPERLINK_TYPE_REPORT_INTERACTION = "ReportInteraction";
	
	/**
	 *
	 */
	protected ReportInteractionHyperlinkProducer(JasperReportsContext jasperReportsContext, HttpServletRequest request)
	{
		super(jasperReportsContext, request);
	}

	/**
	 *
	 */
	public static ReportInteractionHyperlinkProducer getInstance(JasperReportsContext jasperReportsContext, HttpServletRequest request)
	{
		return new ReportInteractionHyperlinkProducer(jasperReportsContext, request);
	}


	@Override
	public String getPath() 
	{
		return WebUtil.getInstance(jasperReportsContext).getReportInteractionPath();
	}

}
