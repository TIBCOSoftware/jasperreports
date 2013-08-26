/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.HtmlFont;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ImageServlet.java 5880 2013-01-07 20:40:06Z teodord $
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
					
					String resourceString = processFont(basePath, htmlFont);
					response.getWriter().write(resourceString);//FIXMEFONT close this properly
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


	protected String processFont(String basePath, HtmlFont htmlFont)
	{
		StringWriter fw = new StringWriter();

		try
		{
			fw.write("@charset \"UTF-8\";\n");//FIXMEFONT this writer is duplicated
			fw.write("@font-face {\n");
			fw.write("\tfont-family: \'" + htmlFont.getId() + "';\n");
			if (htmlFont.getEot() != null)
			{
				String eotFileName = basePath + htmlFont.getEot();
				fw.write("\tsrc: url('" + eotFileName + "');\n");
				fw.write("\tsrc: url('" + eotFileName + "?#iefix') format('embedded-opentype');\n");
				//processFontFile(eotFileName, htmlFont.getEot());
			}
			if (
				htmlFont.getTtf() != null
				|| htmlFont.getSvg() != null
				|| htmlFont.getWoff() != null
				)
			{
				fw.write("\tsrc: local('☺')");
				if (htmlFont.getWoff() != null)
				{
					String woffFileName = basePath + htmlFont.getWoff();
					fw.write(",\n\t\turl('" + woffFileName + "') format('woff')"); 
					//processFontFile(woffFileName, htmlFont.getWoff());
				}
				if (htmlFont.getTtf() != null)
				{
					String ttfFileName = basePath + htmlFont.getTtf();
					fw.write(",\n\t\turl('" + ttfFileName + "') format('truetype')"); 
					//processFontFile(ttfFileName, htmlFont.getTtf());
				}
				if (htmlFont.getSvg() != null)
				{
					String svgFileName = basePath + htmlFont.getSvg();
					fw.write(",\n\t\turl('" + svgFileName + "') format('svg')");
					//processFontFile(svgFileName, htmlFont.getSvg());
				}
				fw.write(";\n");
			}
			fw.write("\tfont-weight: normal;\n");
			fw.write("\tfont-style: normal;\n");
			fw.write("}");
		}
//		catch (IOException e)
//		{
//			throw new JRRuntimeException(e);
//		}
		finally
		{
			if (fw != null)
			{
				try
				{
					fw.close();
				}
				catch(IOException e)
				{
				}
			}
		}
		
		return fw.toString();
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
