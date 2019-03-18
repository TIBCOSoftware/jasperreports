/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.HtmlFont;
import net.sf.jasperreports.engine.export.HtmlFontFamily;
import net.sf.jasperreports.engine.export.HtmlFontUtil;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FontWebResourceHandler implements WebResourceHandler 
{
	/**
	 *
	 */
	public static final String REQUEST_PARAMETER_FONT_NAME = "font";

			
	@Override
	public boolean handleResource(JasperReportsContext jasperReportsContext, HttpServletRequest request, HttpServletResponse response) 
	{
		String fontName = request.getParameter(REQUEST_PARAMETER_FONT_NAME);
		if (fontName != null) 
		{
			HtmlFontFamily htmlFontFamily = HtmlFontFamily.getInstance(jasperReportsContext, fontName);
			
			if (htmlFontFamily != null)
			{
				response.setContentType("text/css");

				try 
				{
					String basePath = getResourceBasePath(jasperReportsContext, request);
					
					byte[] resourceData = processFont(jasperReportsContext, basePath, htmlFontFamily);
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
	 * @deprecated Replaced by {@link #processFont(JasperReportsContext, String, HtmlFontFamily)}.
	 */
	protected byte[] processFont(String basePath, HtmlFont htmlFont)
	{
		return processFont(DefaultJasperReportsContext.getInstance(), basePath, htmlFont);
	}
	

	/**
	 * @deprecated Replaced by {@link #processFont(JasperReportsContext, String, HtmlFontFamily)}.
	 */
	protected byte[] processFont(JasperReportsContext jasperReportsContext, String basePath, HtmlFont htmlFont)
	{
		return processFont(jasperReportsContext, basePath, htmlFont.getFamily());
	}
	

	/**
	 * 
	 */
	protected byte[] processFont(JasperReportsContext jasperReportsContext, String basePath, HtmlFontFamily htmlFontFamily)
	{
		FontFamilyHtmlResourceHandler resourceHandler = new FontFamilyHtmlResourceHandler(basePath, htmlFontFamily);
		
		String fontCss = HtmlFontUtil.getInstance(jasperReportsContext).getHtmlFont(resourceHandler, null, htmlFontFamily, true, true);
		
		try
		{
			return fontCss.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	

	/**
	 * 
	 */
	public String getResourceBasePath(JasperReportsContext jasperReportsContext, HttpServletRequest request)
	{
		return request.getContextPath() + WebUtil.getInstance(jasperReportsContext).getResourcesPath() + "/";
		//String basePath = request.getContextPath() + webUtil.getResourcesBasePath();
	}

	public static class FontFamilyHtmlResourceHandler implements HtmlResourceHandler
	{
		private String basePath;
		private Map<String, String> fontFaceIds;

		public FontFamilyHtmlResourceHandler(String basePath, HtmlFontFamily htmlFontFamily)
		{
			this.basePath = basePath;
			fontFaceIds = new HashMap<String, String>();

			FontFamily fontFamily = htmlFontFamily.getFontFamily();

			FontFace fontFace = fontFamily.getNormalFace();
			if (fontFace != null)
			{
				HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, false, false);
				addHtmlFont(htmlFont);
			}

			fontFace = fontFamily.getBoldFace();
			if (fontFace != null)
			{
				HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, true, false);
				addHtmlFont(htmlFont);
			}

			fontFace = fontFamily.getItalicFace();
			if (fontFace != null)
			{
				HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, false, true);
				addHtmlFont(htmlFont);
			}

			fontFace = fontFamily.getBoldItalicFace();
			if (fontFace != null)
			{
				HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, true, true);
				addHtmlFont(htmlFont);
			}
		}

		protected void addHtmlFont(HtmlFont htmlFont)
		{
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
		}
	}
}

/**
 * @deprecated Replaced by {@link #FontWebResourceHandler.FontFamilyHtmlResourceHandler}.
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
