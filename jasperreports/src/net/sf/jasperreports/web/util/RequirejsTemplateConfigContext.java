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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class RequirejsTemplateConfigContext
{

	private static final Log log = LogFactory.getLog(RequirejsTemplateConfigContext.class);
	
	private String contextPath;
	private WebUtil webUtil;
	private Map<String, String> paths;
	private Map<String, String> resourcePaths;

	public RequirejsTemplateConfigContext(WebRequestContext context, Map<String, String> paths, Map<String, String> resourcePaths)
	{
		contextPath = context.getRequestContextPath();
		webUtil = WebUtil.getInstance(context.getJasperReportsContext());
		this.paths = paths;
		this.resourcePaths = resourcePaths;
	}
	
	public String getPath(String key)
	{
		String path = paths.get(key);
		
		if (path == null)
		{
			String resource = resourcePaths.get(key);
			if (resource != null)
			{
				path = contextPath + webUtil.getResourcesBasePath() + resource;
			}
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("path for " + key + " is " + path);
		}
		
		//FIXME exception if not found?
		return path;
	}
}