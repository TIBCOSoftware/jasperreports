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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.MessageUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class DefaultWebResourceHandler extends AbstractWebResourceHandler 
{
	private static final Log log = LogFactory.getLog(DefaultWebResourceHandler.class);

	private static DefaultWebResourceHandler INSTANCE = new DefaultWebResourceHandler();
	
	private DefaultWebResourceHandler() 
	{
	}
	
	public static DefaultWebResourceHandler getInstance() 
	{
		return INSTANCE;
	}

	public boolean handlesResource(String resource)
	{
		return true;
	}

	public WebResource getResource(JasperReportsContext jasperReportsContext, HttpServletRequest request, String resourceUri) 
	{
		SimpleWebResource resource = null;
		if (
			resourceUri != null
			&& checkResourceName(jasperReportsContext, resourceUri) 
			//FIXMESORT need to check if the resource exists, before attempting to load it and thus raise exception 
			// before other handlers have the chance to respond
			) 
		{
			WebUtil webUtil = WebUtil.getInstance(jasperReportsContext);
			boolean isDynamicResource = webUtil.isDynamicResource(request);
			String resourceBundleName = webUtil.getResourceBundleForResource(request);
			Locale locale = webUtil.getResourceLocale(request);
			byte[] bytes = null;

			try 
			{
				if (resourceUri.indexOf(".vm.") != -1 && (isDynamicResource || resourceBundleName != null || locale != null)) 
				{
					Map<String, Object> contextMap = new HashMap<String, Object>();
					contextMap.put("path", request.getContextPath() + webUtil.getResourcesBasePath());
					locale = locale == null ? Locale.getDefault() : locale;
					contextMap.put("msgProvider", MessageUtil.getInstance(jasperReportsContext).getLocalizedMessageProvider(resourceBundleName, locale)); 
					String resourceString = VelocityUtil.processTemplate(resourceUri, contextMap);
					if (resourceString != null) 
					{
						bytes = resourceString.getBytes("UTF-8");
					}
				} else {
					bytes = JRLoader.loadBytesFromResource(resourceUri);
				}
			} catch (IOException e) {
				throw new JRRuntimeException(e);
			} catch (JRException e) {
				throw new JRRuntimeException(e);
			}
			
			resource = new SimpleWebResource();
			resource.setData(bytes);
			
			if (resourceUri != null && resourceUri.lastIndexOf(".") != -1) 
			{
				resource.setType(resourceUri.substring(resourceUri.lastIndexOf(".") + 1));
			}
		}
		return resource;
	}
	
	/**
	 * 
	 */
	protected boolean checkResourceName(JasperReportsContext jasperReportsContext, String resourceName) 
	{
		boolean matched = false;

		List<PropertySuffix> patternProps = JRPropertiesUtil.getInstance(jasperReportsContext).getProperties(PROPERTIES_WEB_RESOURCE_PATTERN_PREFIX);//FIXMESORT cache this
		for (Iterator<PropertySuffix> patternIt = patternProps.iterator(); patternIt.hasNext();)
		{
			JRPropertiesUtil.PropertySuffix patternProp = patternIt.next();
			String patternStr = patternProp.getValue();
			if (patternStr != null && patternStr.length() > 0)
			{
				Pattern resourcePattern = Pattern.compile(patternStr);
				if (resourcePattern.matcher(resourceName).matches()) 
				{
					if (log.isDebugEnabled()) 
					{
						log.debug("resource " + resourceName + " matched pattern " + resourcePattern);
					}
					
					matched = true;
					break;
				}
			}
		}

		if (!matched) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("Resource " + resourceName + " does not matched any allowed pattern");
			}
			
//			throw new JRRuntimeException("Resource " + resourceName 
//					+ " does not matched any allowed pattern");
		}
		
		return matched;
	}
}
