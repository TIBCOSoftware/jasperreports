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
package net.sf.jasperreports.engine.export;

import java.io.UnsupportedEncodingException;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.repo.RepositoryUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class HtmlFontUtil
{

	private final RepositoryUtil repositoryUtil;


	/**
	 *
	 */
	private HtmlFontUtil(JasperReportsContext jasperReportsContext)
	{
		this.repositoryUtil = RepositoryUtil.getInstance(jasperReportsContext);
	}
	
	
	/**
	 *
	 */
	public static HtmlFontUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new HtmlFontUtil(jasperReportsContext);
	}
	
	
	/**
	 * @deprecated Replaced by {@link #handleHtmlFont(HtmlResourceHandler, HtmlFont)}.
	 */
	public static void handleFont(HtmlResourceHandler resourceHandler, HtmlFont htmlFont)
	{
		getInstance(DefaultJasperReportsContext.getInstance()).handleHtmlFont(resourceHandler, htmlFont);
	}
	
	
	/**
	 * @deprecated Replaced by {@link #handleHtmlFont(HtmlResourceHandler, HtmlFontFamily)}.
	 */
	public void handleHtmlFont(HtmlResourceHandler resourceHandler, HtmlFont htmlFont)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("@charset \"UTF-8\";\n");
		sb.append(getHtmlFont(resourceHandler, htmlFont));
		
		if (resourceHandler != null)
		{
			try
			{
				resourceHandler.handleResource(htmlFont.getId(), sb.toString().getBytes("UTF-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}
	
	
	/**
	 *
	 */
	private String getHtmlFont(HtmlResourceHandler resourceHandler, HtmlFont htmlFont)
	{
		StringBuilder sb = new StringBuilder();

		try
		{
			sb.append("@font-face {\n");
			sb.append("\tfont-family: " + (htmlFont.getFamily() == null ? htmlFont.getShortId() : htmlFont.getFamily().getShortId()) + ";\n");
			if (htmlFont.getEot() != null)
			{
				String eotId = htmlFont.getId() + ".eot";
				String eotFileName = resourceHandler.getResourcePath(eotId);
				sb.append("\tsrc: url('" + eotFileName + "');\n");
				sb.append("\tsrc: url('" + eotFileName + "?#iefix') format('embedded-opentype');\n");
				//sbuffer.append("\tsrc: url('" + eotFileName + "?#iefix') format('eot');\n");
				resourceHandler.handleResource(eotId, repositoryUtil.getBytesFromLocation(htmlFont.getEot()));
			}
			if (
				htmlFont.getTtf() != null
				|| htmlFont.getSvg() != null
				|| htmlFont.getWoff() != null
				)
			{
				sb.append("\tsrc: local('\u263A')");
				if (htmlFont.getWoff() != null)
				{
					String woffId = htmlFont.getId() + ".woff";
					String woffFileName = resourceHandler.getResourcePath(woffId);
					sb.append(",\n\t\turl('" + woffFileName + "') format('woff')"); 
					resourceHandler.handleResource(woffId, repositoryUtil.getBytesFromLocation(htmlFont.getWoff()));
				}
				if (htmlFont.getTtf() != null)
				{
					String ttfId = htmlFont.getId() + ".ttf";
					String ttfFileName = resourceHandler.getResourcePath(ttfId);
					sb.append(",\n\t\turl('" + ttfFileName + "') format('truetype')"); 
					resourceHandler.handleResource(ttfId, repositoryUtil.getBytesFromLocation(htmlFont.getTtf()));
				}
				if (htmlFont.getSvg() != null)
				{
					String svgId = htmlFont.getId() + ".svg";
					String svgFileName = resourceHandler.getResourcePath(svgId);
					sb.append(",\n\t\turl('" + svgFileName + "') format('svg')");
					resourceHandler.handleResource(svgId, repositoryUtil.getBytesFromLocation(htmlFont.getSvg()));
				}
				sb.append(";\n");
			}
			sb.append("\tfont-weight: " + (htmlFont.isBold() ? "bold" : "normal") + ";\n");
			sb.append("\tfont-style: " + (htmlFont.isItalic() ? "italic" : "normal") + ";\n");
			sb.append("}");
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 
	 */
	public void handleHtmlFont(HtmlResourceHandler resourceHandler, HtmlFontFamily htmlFontFamily)
	{
		String fontCss = getHtmlFont(resourceHandler, htmlFontFamily);

		if (resourceHandler != null)
		{
			try
			{
				resourceHandler.handleResource(htmlFontFamily.getId(), fontCss.getBytes("UTF-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}
	
	
	/**
	 * 
	 */
	public String getHtmlFont(HtmlResourceHandler resourceHandler, HtmlFontFamily htmlFontFamily)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("@charset \"UTF-8\";\n\n");

		FontFamily fontFamily = htmlFontFamily.getFontFamily();
		
		FontFace fontFace = fontFamily.getNormalFace();
		if (fontFace != null)
		{
			HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, false, false);
			sb.append(getHtmlFont(resourceHandler, htmlFont));
			sb.append("\n\n");
		}
		
		fontFace = fontFamily.getBoldFace();
		if (fontFace != null)
		{
			HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, true, false);
			sb.append(getHtmlFont(resourceHandler, htmlFont));
			sb.append("\n\n");
		}
		
		fontFace = fontFamily.getItalicFace();
		if (fontFace != null)
		{
			HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, false, true);
			sb.append(getHtmlFont(resourceHandler, htmlFont));
			sb.append("\n\n");
		}
		
		fontFace = fontFamily.getBoldItalicFace();
		if (fontFace != null)
		{
			HtmlFont htmlFont = HtmlFont.getInstance(htmlFontFamily, htmlFontFamily.getLocale(), fontFace, true, true);
			sb.append(getHtmlFont(resourceHandler, htmlFont));
			sb.append("\n\n");
		}
		
		return sb.toString();
	}
}
