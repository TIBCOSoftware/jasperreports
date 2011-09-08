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
package net.sf.jasperreports.j2ee.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class XmlServlet extends BaseHttpServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	public static final String START_PAGE_INDEX_REQUEST_PARAMETER = "startPage";
	public static final String END_PAGE_INDEX_REQUEST_PARAMETER = "endPage";
	public static final String PAGE_INDEX_REQUEST_PARAMETER = "page";
	

	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		List<JasperPrint> jasperPrintList = BaseHttpServlet.getJasperPrintList(request);

		if (jasperPrintList == null)
		{
			throw new ServletException("No JasperPrint documents found on the HTTP session.");
		}
		
		int startPageIndex = -1;

		String startPageStr = request.getParameter(START_PAGE_INDEX_REQUEST_PARAMETER);
		try
		{
			startPageIndex = Integer.parseInt(startPageStr);
		}
		catch(Exception e)
		{
		}
		
		int endPageIndex = -1;

		String endPageStr = request.getParameter(END_PAGE_INDEX_REQUEST_PARAMETER);
		try
		{
			endPageIndex = Integer.parseInt(endPageStr);
		}
		catch(Exception e)
		{
		}
		
		int pageIndex = -1;

		String pageStr = request.getParameter(PAGE_INDEX_REQUEST_PARAMETER);
		try
		{
			pageIndex = Integer.parseInt(pageStr);
		}
		catch(Exception e)
		{
		}
		
		if (pageIndex >= 0)
		{
			startPageIndex = pageIndex;
			endPageIndex = pageIndex;
		}
		
		Boolean isBuffered = Boolean.valueOf(request.getParameter(BaseHttpServlet.BUFFERED_OUTPUT_REQUEST_PARAMETER));
		if (isBuffered.booleanValue())
		{
			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
			JRXmlExporter exporter = getExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			if (startPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, Integer.valueOf(startPageIndex));
			}
			if (endPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, Integer.valueOf(endPageIndex));
			}
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);

			try 
			{
				exporter.exportReport();
				fbos.close();
			
				if (fbos.size() > 0)
				{
					response.setContentType("text/xml");
					response.setHeader("Content-Disposition", "inline; filename=\"file.jrpxml\"");
					response.setContentLength(fbos.size());
					ServletOutputStream ouputStream = response.getOutputStream();
	
					try
					{
						fbos.writeData(ouputStream);
						fbos.dispose();
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
			} 
			catch (JRException e) 
			{
				throw new ServletException(e);
			}
			finally
			{
				fbos.close();
				fbos.dispose();
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
			response.setContentType("text/xml");
			response.setHeader("Content-Disposition", "inline; filename=\"file.jrpxml\"");

			JRXmlExporter exporter = getExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			if (startPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, Integer.valueOf(startPageIndex));
			}
			if (endPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, Integer.valueOf(endPageIndex));
			}
			
			OutputStream ouputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

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

	
	/**
	 * 
	 */
	public JRXmlExporter getExporter()
	{
		return new JRXmlExporter();
	}
}

