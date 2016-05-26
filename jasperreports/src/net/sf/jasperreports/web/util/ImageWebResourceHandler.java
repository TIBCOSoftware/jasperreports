/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRTypeSniffer;
import net.sf.jasperreports.renderers.DataRenderable;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.ResourceRenderer;
import net.sf.jasperreports.renderers.util.RendererUtil;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.servlets.JasperPrintAccessor;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ImageWebResourceHandler implements WebResourceHandler
{
	public static final String EXCEPTION_MESSAGE_KEY_JASPERPRINT_NOT_FOUND = "web.util.jasperprint.not.found";
	public static final String EXCEPTION_MESSAGE_KEY_REPORT_CONTEXT_NOT_FOUND = "web.util.report.context.not.found";
	
	/**
	 *
	 */
	public static final String REQUEST_PARAMETER_IMAGE_NAME = "image";

			
	@Override
	public boolean handleResource(JasperReportsContext jasperReportsContext, HttpServletRequest request, HttpServletResponse response)
	{
		String imageName = request.getParameter(REQUEST_PARAMETER_IMAGE_NAME);
		if (imageName == null)
		{
			return false;
		}

		byte[] imageData = null;
		String imageMimeType = null;

		if ("px".equals(imageName))
		{
			try
			{
				imageData = RepositoryUtil.getInstance(jasperReportsContext).getBytesFromLocation(JRImageLoader.PIXEL_IMAGE_RESOURCE);
				imageMimeType = ImageTypeEnum.GIF.getMimeType();
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		else
		{
			WebReportContext webReportContext = WebReportContext.getInstance(request, false);
			
			if (webReportContext == null)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_REPORT_CONTEXT_NOT_FOUND,
						(Object[])null);
			}
			
			JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext.getParameterValue(
					WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);
			if (jasperPrintAccessor == null)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_JASPERPRINT_NOT_FOUND,
						(Object[])null);
			}
			
			List<JasperPrint> jasperPrintList = Collections.singletonList(jasperPrintAccessor.getJasperPrint());
			
			JRPrintImage image = HtmlExporter.getImage(jasperPrintList, imageName);
			
			Renderable renderer = image.getRenderer();
			
			Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
			Color backcolor = ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null;

			RendererUtil rendererUtil = RendererUtil.getInstance(jasperReportsContext);
			
			try
			{
				imageData = process(jasperReportsContext, renderer, dimension, backcolor);
			}
			catch (Exception e)
			{
				try
				{
					Renderable onErrorRenderer = rendererUtil.handleImageError(e, image.getOnErrorTypeValue());
					if (onErrorRenderer != null)
					{
						imageData = process(jasperReportsContext, onErrorRenderer, dimension, backcolor);
					}
				}
				catch (JRException je)
				{
					throw new JRRuntimeException(je);
				}
			}
			
			imageMimeType =
				RendererUtil.getInstance(jasperReportsContext).isSvgData(imageData)
				? RendererUtil.SVG_MIME_TYPE
				: JRTypeSniffer.getImageTypeValue(imageData).getMimeType();
		}

		if (imageData != null && imageData.length > 0)
		{
			if (imageMimeType != null) 
			{
				response.setHeader("Content-Type", imageMimeType);
			}
			response.setContentLength(imageData.length);
			
			ServletOutputStream outputStream = null;
			try
			{
				outputStream = response.getOutputStream();
				outputStream.write(imageData, 0, imageData.length);
				outputStream.flush();
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
			finally
			{
				if (outputStream != null)
				{
					try
					{
						outputStream.close();
					}
					catch (IOException e)
					{
					}
				}
			}
		}
		
		return true;
	}

	
	protected byte[] process(
		JasperReportsContext jasperReportsContext,
		Renderable renderer,
		Dimension dimension,
		Color backcolor
		) throws JRException
	{
		RendererUtil rendererUtil = RendererUtil.getInstance(jasperReportsContext);
		
		if (renderer instanceof ResourceRenderer)
		{
			renderer = //hard to use a cache here and it would be just for some icon type of images, if any 
				rendererUtil.getNonLazyRenderable(
					((ResourceRenderer)renderer).getResourceLocation(), 
					OnErrorTypeEnum.ERROR
					);
		}
		
		DataRenderable dataRenderer = 
			rendererUtil.getDataRenderable(
				renderer,
				dimension,
				backcolor
				);

		return dataRenderer.getData(jasperReportsContext);
	}
}
