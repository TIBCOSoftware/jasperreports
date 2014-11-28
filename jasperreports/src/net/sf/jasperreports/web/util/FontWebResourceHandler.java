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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.HtmlFont;
import net.sf.jasperreports.engine.export.HtmlFontUtil;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FontWebResourceHandler implements WebResourceHandler 
{
	/**
	 *
	 */
	public static final String REQUEST_PARAMETER_FONT_NAME = "font";

			
	/**
	 *
	 */
	public boolean handleResource(JasperReportsContext jasperReportsContext, HttpServletRequest request, HttpServletResponse response) 
	{
		String fontName = request.getParameter(REQUEST_PARAMETER_FONT_NAME);
		if (fontName != null) 
		{
			HtmlFont htmlFont = HtmlFont.getInstance(jasperReportsContext, fontName);
			
			if (htmlFont != null)
			{
				response.setContentType("text/css");

				try 
				{
					String basePath = getResourceBasePath(jasperReportsContext, request);
					
					byte[] resourceData = processFont(jasperReportsContext, basePath, htmlFont);
					response.getOutputStream().write(resourceData);//FIXMEFONT close this properly
				}
				catch (IOException e) 
				{
					throw new JRRuntimeException(e);
				}

				return true;
			}
		}
		return false;
	}


	/**
	 * @deprecated Replaced by {@link #processFont(JasperReportsContext, String, HtmlFont)}.
	 */
	protected byte[] processFont(String basePath, HtmlFont htmlFont)
	{
		return processFont(DefaultJasperReportsContext.getInstance(), basePath, htmlFont);
	}
	

	protected byte[] processFont(JasperReportsContext jasperReportsContext, String basePath, HtmlFont htmlFont)
	{
		FontHtmlResourceHandler resourceHandler = new FontHtmlResourceHandler(basePath, htmlFont);
		
		HtmlFontUtil.getInstance(jasperReportsContext).handleHtmlFont(resourceHandler, htmlFont);
		
		return resourceHandler.getFontCss();
	}
	

	/**
	 * 
	 */
	public String getResourceBasePath(JasperReportsContext jasperReportsContext, HttpServletRequest request)
	{
		return request.getContextPath() + WebUtil.getInstance(jasperReportsContext).getResourcesPath() + "/";
		//String basePath = request.getContextPath() + webUtil.getResourcesBasePath();
	}
}


/**
 * 
 */
class FontHtmlResourceHandler implements HtmlResourceHandler
{
	private String basePath;
	private HtmlFont htmlFont;
	private Map<String, String> fontFaceIds;
	private byte[] fontCss;
	
	protected FontHtmlResourceHandler(String basePath, HtmlFont htmlFont)
	{
		this.basePath = basePath;
		this.htmlFont = htmlFont;
		fontFaceIds = new HashMap<String, String>();
		fontFaceIds.put(htmlFont.getId() + ".ttf", htmlFont.getTtf());
		fontFaceIds.put(htmlFont.getId() + ".eot", htmlFont.getEot());
		fontFaceIds.put(htmlFont.getId() + ".woff", htmlFont.getWoff());
		fontFaceIds.put(htmlFont.getId() + ".svg", htmlFont.getSvg());
	}

	@Override
	public String getResourcePath(String id) 
	{
		if (fontFaceIds.containsKey(id))
		{
			return basePath + fontFaceIds.get(id);
		}
		return basePath + id;
	}

	@Override
	public void handleResource(String id, byte[] data) 
	{
		if (id.equals(htmlFont.getId()))
		{
			fontCss = data;
		}
	}

	/**
	 * 
	 */
	protected byte[] getFontCss() 
	{
		return fontCss;
	}
	
}
