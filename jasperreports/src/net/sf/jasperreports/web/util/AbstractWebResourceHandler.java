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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public abstract class AbstractWebResourceHandler implements WebResourceHandler 
{
	/**
	 * 
	 */
	public static final String PROPERTIES_WEB_RESOURCE_PATTERN_PREFIX = "net.sf.jasperreports.web.resource.pattern.";
	
	/**
	 * 
	 */
	public boolean handleResource(JasperReportsContext jasperReportsContext, HttpServletRequest request, HttpServletResponse response) 
	{
		WebUtil webUtil = WebUtil.getInstance(jasperReportsContext);
		String resourceUri = webUtil.getResourceUri(request);
		
		if (resourceUri == null)
		{
			String requestUrl = null;
			try
			{
				requestUrl = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				throw new JRRuntimeException(e);
			}
			//String servletPath = request.getServletPath();
			String servletPath = webUtil.getResourcesPath();
			int uriStart = requestUrl.indexOf(servletPath);
			if (uriStart >= 0)
			{
				int resourceUriStart = uriStart + servletPath.length() + 1;// +1 from slash separator
				if (resourceUriStart < requestUrl.length())
				{
					resourceUri = requestUrl.substring(resourceUriStart);
				}
			}
		}
		
		if (resourceUri != null && resourceUri.trim().length() > 0)
		{
			WebResource resource = getResource(jasperReportsContext, request, resourceUri);
			if (resource != null)
			{
				String resourceType = resource.getType();
				if (resourceType != null) 
				{
					List<ContentTypeMapping> contentTypeMappings = jasperReportsContext.getExtensions(ContentTypeMapping.class);
					for (ContentTypeMapping contentTypeMapping : contentTypeMappings) 
					{
						if (resourceType.equals(contentTypeMapping.getFileType())) 
						{
							response.setContentType(contentTypeMapping.getContentType());
							break;
						}
					}
				}

				// FIXME: set this header on for font files; required by Firefox
				response.setHeader("Access-Control-Allow-Origin", "*");

				// Set to expire far in the past.
				response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
				// Set standard HTTP/1.1 no-cache headers.
				response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
				// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
				response.addHeader("Cache-Control", "post-check=0, pre-check=0");
				// Set standard HTTP/1.0 no-cache header.
				response.setHeader("Pragma", "no-cache");
				
				byte[] bytes = resource.getData();
				
				response.setContentLength(bytes.length);
				
				OutputStream os = null;
				
				try
				{
					os = response.getOutputStream();
					os.write(bytes);
				}
				catch (IOException e)
				{
					throw new JRRuntimeException(e);
				}
				finally
				{
					if (os != null)
					{
						try
						{
							os.close();
						}
						catch (IOException e)
						{
						}
					}
				}
				
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 
	 */
	public abstract WebResource getResource(JasperReportsContext jasperReportsContext, HttpServletRequest request, String resource);

}
