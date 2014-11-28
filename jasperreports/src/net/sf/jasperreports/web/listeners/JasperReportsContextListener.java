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
package net.sf.jasperreports.web.listeners;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import net.sf.jasperreports.web.servlets.AbstractServlet;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JasperReportsContextListener implements ServletContextListener
{
	/**
	 * 
	 */
	public void	contextInitialized(ServletContextEvent ce) 
	{
		LocalJasperReportsContext localJasperReportsContext = new LocalJasperReportsContext(DefaultJasperReportsContext.getInstance());
		SimpleFileResolver fileResolver = 
			new SimpleFileResolver(
				new File(
					new File(ce.getServletContext().getRealPath("/")), 
					ce.getServletContext().getInitParameter("net.sf.jasperreports.web.file.repository.root")
					)
				);
		localJasperReportsContext.setFileResolver(fileResolver);
		
		AbstractServlet.setJasperReportsContext(localJasperReportsContext);
	}

	/**
	 * 
	 */
	public void	contextDestroyed(ServletContextEvent ce) 
	{
	}
}
