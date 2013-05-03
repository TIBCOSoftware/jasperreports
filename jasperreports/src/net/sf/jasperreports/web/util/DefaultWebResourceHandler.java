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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.MessageUtil;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class DefaultWebResourceHandler implements WebResourceHandler {

	private static DefaultWebResourceHandler INSTANCE = new DefaultWebResourceHandler();
	
	private DefaultWebResourceHandler() {
	}
	
	public static DefaultWebResourceHandler getInstance() {
		return INSTANCE;
	}

	public boolean hadlesResource(String resourceKey) {
		return true;
	}

	public String getResourceType(String resourceKey) {
		if (resourceKey != null && resourceKey.lastIndexOf(".") != -1) {
			return resourceKey.substring(resourceKey.lastIndexOf(".") + 1);
		}
		return null;
	}

	public byte[] getData(String resourceKey, HttpServletRequest request, JasperReportsContext jrContext) {
		if (resourceKey != null) {
			WebUtil webUtil = WebUtil.getInstance(jrContext);
			boolean isDynamicResource = webUtil.isDynamicResource(request);
			String resourceBundleName = webUtil.getResourceBundleForResource(request);
			Locale locale = webUtil.getResourceLocale(request);
			byte[] bytes = null;

			try {
				if (resourceKey.indexOf(".vm.") != -1 && (isDynamicResource || resourceBundleName != null || locale != null)) {
					Map<String, Object> contextMap = new HashMap<String, Object>();
					contextMap.put("path", request.getContextPath() + webUtil.getResourcesBasePath());
					locale = locale == null ? Locale.getDefault() : locale;
					contextMap.put("msgProvider", MessageUtil.getInstance(jrContext).getLocalizedMessageProvider(resourceBundleName, locale)); 
					String resourceString = VelocityUtil.processTemplate(resourceKey, contextMap);
					if (resourceString != null) {
						bytes = resourceString.getBytes("UTF-8");
					}
				} else {
					bytes = JRLoader.loadBytesFromResource(resourceKey);
				}
			} catch (IOException e) {
				throw new JRRuntimeException(e);
			} catch (JRException e) {
				throw new JRRuntimeException(e);
			}
			return bytes;
		}
		return null;
	}
	
}
