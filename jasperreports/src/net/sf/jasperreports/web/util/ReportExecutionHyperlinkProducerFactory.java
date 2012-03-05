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

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ReportExecutionHyperlinkProducerFactory extends JRHyperlinkProducerFactory
{
	public static final String HYPERLINK_TYPE_REPORT_EXECUTION = "ReportExecution";
	
	private HttpServletRequest request;
	
	/**
	 *
	 */
	private ReportExecutionHyperlinkProducerFactory(HttpServletRequest request)
	{
		this.request = request;
	}

	/**
	 *
	 */
	public static ReportExecutionHyperlinkProducerFactory getInstance(HttpServletRequest request)
	{
		return new ReportExecutionHyperlinkProducerFactory(request);
	}

	/**
	 *
	 */
	public JRHyperlinkProducer getHandler(String linkType)
	{
		if (linkType == null || !HYPERLINK_TYPE_REPORT_EXECUTION.equals(linkType))
		{
			return null;
		}
		
		return ReportExecutionHyperlinkProducer.getInstance(request);
	}

}
