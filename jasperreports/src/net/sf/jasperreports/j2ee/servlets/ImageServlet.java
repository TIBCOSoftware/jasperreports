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
package net.sf.jasperreports.j2ee.servlets;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElementIndex;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ImageServlet extends HttpServlet
{


	/**
	 *
	 */
	public static final String DEFAULT_JASPER_PRINT_LIST_SESSION_ATTRIBUTE = "net.sf.jasperreports.j2ee.jasper_print_list";
	public static final String DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE = "net.sf.jasperreports.j2ee.jasper_print";

	public static final String JASPER_PRINT_LIST_REQUEST_PARAMETER = "jrprintlist";
	public static final String JASPER_PRINT_REQUEST_PARAMETER = "jrprint";
	public static final String IMAGE_NAME_REQUEST_PARAMETER = "image";

			
	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		byte[] imageData = null;

		String imageName = request.getParameter(IMAGE_NAME_REQUEST_PARAMETER);
		if ("px".equals(imageName))
		{
			try
			{
				JRRenderable pxRenderer = 
					JRImageRenderer.getInstance(
						"net/sf/jasperreports/engine/images/pixel.GIF",
						JRImage.ON_ERROR_TYPE_ERROR
						);
				imageData = pxRenderer.getImageData();
			}
			catch (JRException e)
			{
				throw new ServletException(e);
			}
		}
		else
		{
			String jasperPrintListSessionAttr = request.getParameter(JASPER_PRINT_LIST_REQUEST_PARAMETER);
			if (jasperPrintListSessionAttr == null)
			{
				jasperPrintListSessionAttr = DEFAULT_JASPER_PRINT_LIST_SESSION_ATTRIBUTE;
			}

			String jasperPrintSessionAttr = request.getParameter(JASPER_PRINT_REQUEST_PARAMETER);
			if (jasperPrintSessionAttr == null)
			{
				jasperPrintSessionAttr = DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE;
			}
			
			List jasperPrintList = (List)request.getSession().getAttribute(jasperPrintListSessionAttr);
			if (jasperPrintList == null)
			{
				JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(jasperPrintSessionAttr);
				if (jasperPrint != null)
				{
					jasperPrintList = new ArrayList();
					jasperPrintList.add(jasperPrint);
				}
			}
			
			JRPrintElementIndex imageIndex = JRHtmlExporter.getPrintElementIndex(imageName);
			
			JasperPrint report = (JasperPrint)jasperPrintList.get(imageIndex.getReportIndex());
			JRPrintPage page = (JRPrintPage)report.getPages().get(imageIndex.getPageIndex());//FIXME J2EE more robust
			JRPrintImage image = (JRPrintImage)page.getElements().get(
					imageIndex.getElementIndexes()[0].intValue() 
							);
			JRRenderable renderer = image.getRenderer();
			if (renderer.getType() == JRRenderable.TYPE_SVG)
			{
				renderer = 
					new JRWrappingSvgRenderer(
						renderer, 
						new Dimension(image.getWidth(), image.getHeight()),
						image.getBackcolor()
						);
			}
			
			try
			{
				imageData = renderer.getImageData();
			}
			catch (JRException e)
			{
				throw new ServletException(e);
			}
		}

		if (imageData != null && imageData.length > 0)
		{
			response.setContentLength(imageData.length);
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(imageData, 0, imageData.length);
			ouputStream.flush();
			ouputStream.close();
		}
	}


}
