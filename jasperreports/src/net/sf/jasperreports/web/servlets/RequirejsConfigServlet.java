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
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.web.util.DefaultWebRequestContext;
import net.sf.jasperreports.web.util.RequirejsConfigCreator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Narcis Marcu(nmarcu@users.sourceforge.net)
 */
public class RequirejsConfigServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private static final Log log = LogFactory.getLog(RequirejsConfigServlet.class);


	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		response.setContentType("text/javascript; charset=UTF-8");
		setNoExpire(response);

		JasperReportsContext jrContext = getJasperReportsContext();
		DefaultWebRequestContext webRequestContext = new DefaultWebRequestContext();
		webRequestContext.setJasperReportsContext(jrContext);
		webRequestContext.setRequest(request);
		
		RequirejsConfigCreator configCreator = RequirejsConfigCreator.getInstance(webRequestContext);
		String requirejsConfig = configCreator.getRequirejsConfig();
		
		PrintWriter out = response.getWriter();
		out.write(requirejsConfig);
	}

}
