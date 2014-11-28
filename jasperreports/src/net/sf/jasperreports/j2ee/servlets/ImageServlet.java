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
package net.sf.jasperreports.j2ee.servlets;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ImageServlet extends BaseHttpServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	/**
	 *
	 */
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
		String imageMimeType = null;

		String imageName = request.getParameter(IMAGE_NAME_REQUEST_PARAMETER);
		if ("px".equals(imageName))
		{
			try
			{
				Renderable pxRenderer = 
					RenderableUtil.getInstance(getJasperReportsContext()).getRenderable("net/sf/jasperreports/engine/images/pixel.GIF");
				imageData = pxRenderer.getImageData(getJasperReportsContext());
				imageMimeType = ImageTypeEnum.GIF.getMimeType();
			}
			catch (JRException e)
			{
				throw new ServletException(e);
			}
		}
		else
		{
			List<JasperPrint> jasperPrintList = BaseHttpServlet.getJasperPrintList(request);
			
			if (jasperPrintList == null)
			{
				throw new ServletException("No JasperPrint documents found on the HTTP session.");
			}
			
			JRPrintImage image = HtmlExporter.getImage(jasperPrintList, imageName);
			
			Renderable renderer = image.getRenderable();
			if (renderer.getTypeValue() == RenderableTypeEnum.SVG)
			{
				renderer = 
					new JRWrappingSvgRenderer(
						renderer, 
						new Dimension(image.getWidth(), image.getHeight()),
						ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
						);
			}

			imageMimeType = renderer.getImageTypeValue().getMimeType();
			
			try
			{
				imageData = renderer.getImageData(getJasperReportsContext());
			}
			catch (JRException e)
			{
				throw new ServletException(e);
			}
		}

		if (imageData != null && imageData.length > 0)
		{
			if (imageMimeType != null) 
			{
				response.setHeader("Content-Type", imageMimeType);
			}
			response.setContentLength(imageData.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(imageData, 0, imageData.length);
			outputStream.flush();
			outputStream.close();
		}
	}


}
