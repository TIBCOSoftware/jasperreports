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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.j2ee.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class XlsServlet extends HttpServlet
{


	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		List jasperPrintList = ServletHelper.getJasperPrintList(request);

		if (jasperPrintList == null)
		{
			throw new ServletException("No JasperPrint documents found on the HTTP session.");
		}
		
		Boolean isBuffered = Boolean.valueOf(request.getParameter(ServletHelper.BUFFERED_OUTPUT_REQUEST_PARAMETER));
		if (isBuffered.booleanValue())
		{
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

			try 
			{
				exporter.exportReport();
			} 
			catch (JRException e) 
			{
				throw new ServletException(e);
			}

			byte[] bytes = baos.toByteArray();
			
			if (bytes != null && bytes.length > 0)
			{
				response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "inline; filename=\"file.xls\"");
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();

				try
				{
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
				}
				finally
				{
					if (ouputStream != null)
					{
						try
						{
							ouputStream.close();
						}
						catch (IOException ex)
						{
						}
					}
				}
			}
//			else
//			{
//				response.setContentType("text/html");
//				PrintWriter out = response.getWriter();
//				out.println("<html>");
//				out.println("<body bgcolor=\"white\">");
//				out.println("<span class=\"bold\">Empty response.</span>");
//				out.println("</body>");
//				out.println("</html>");
//			}
		}
		else
		{
			response.setContentType("application/xls");
			response.setHeader("Content-Disposition", "inline; filename=\"file.xls\"");

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			
			OutputStream ouputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

			try 
			{
				exporter.exportReport();
			} 
			catch (JRException e) 
			{
				throw new ServletException(e);
			}
			finally
			{
				if (ouputStream != null)
				{
					try
					{
						ouputStream.close();
					}
					catch (IOException ex)
					{
					}
				}
			}
		}
	}

	
}
