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
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.MessageUtil;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class JiveWebResourceHandler extends AbstractWebResourceHandler 
{
	
	private String bundleName;
	private Map<String, String> keyToFileMappings;
	
	public JiveWebResourceHandler(String bundleName) 
	{
		this.bundleName = bundleName;
		this.keyToFileMappings = new HashMap<String, String>();
	}

	public WebResource getResource(JasperReportsContext jasperReportsContext, HttpServletRequest request, String resourceKey) 
	{
		SimpleWebResource resource = null;
		if (resourceKey != null && keyToFileMappings.containsKey(resourceKey)) 
		{
			WebUtil webUtil = WebUtil.getInstance(jasperReportsContext);
			byte[] bytes = null;

			try 
			{
				Locale locale = LocaleResolverUtil.instance(jasperReportsContext).getLocale(request);
				Map<String, Object> contextMap = new HashMap<String, Object>();
				contextMap.put("path", request.getContextPath() + webUtil.getResourcesBasePath());
				contextMap.put("msgProvider", MessageUtil.getInstance(jasperReportsContext).getLocalizedMessageProvider(bundleName, locale)); 
				String resourceString = VelocityUtil.processTemplate(keyToFileMappings.get(resourceKey), contextMap);
				if (resourceString != null) 
				{
					bytes = resourceString.getBytes("UTF-8");
				}
			}
			catch (IOException e) 
			{
				throw new JRRuntimeException(e);
			}
			
			resource = new SimpleWebResource();
			resource.setData(bytes);

			if (resourceKey != null && resourceKey.lastIndexOf(".") != -1) 
			{
				resource.setType(resourceKey.substring(resourceKey.lastIndexOf(".") + 1));
			}
		}
		return resource;
	}
	
	public void addMapping(String key, String fileClasspath) 
	{
		this.keyToFileMappings.put(key, fileClasspath);
	}
	
}
