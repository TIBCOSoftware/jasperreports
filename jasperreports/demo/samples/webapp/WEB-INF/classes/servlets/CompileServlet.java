/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package servlets;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRProperties;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CompileServlet extends HttpServlet
{


	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		ServletContext context = this.getServletConfig().getServletContext();

		JRProperties.setProperty(
			JRProperties.COMPILER_CLASSPATH, 
			context.getRealPath("/WEB-INF/lib/jasperreports-1.0.0.jar") +
			System.getProperty("path.separator") + 
			context.getRealPath("/WEB-INF/classes/")
			);
	
		JRProperties.setProperty(
			JRProperties.COMPILER_TEMP_DIR, 
			context.getRealPath("/reports/")
			);

		try
		{
			JasperCompileManager.compileReportToFile(context.getRealPath("/reports/WebappReport.jrxml"));
		}
		catch (JRException e)
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
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

			return;
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>JasperReports - Web Application Sample</title>");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
		out.println("</head>");
		
		out.println("<body bgcolor=\"white\">");

		out.println("<span class=\"bold\">The XML report design was successfully compiled.</span>");

		out.println("</body>");
		out.println("</html>");
	}


}
