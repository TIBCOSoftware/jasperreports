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
package net.sf.jasperreports.engine.fill;

import java.net.URLStreamHandlerFactory;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.JRResourcesUtil;

/**
 * Resources utility class used for report fills.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see JRResourcesUtil
 */
public final class JRResourcesFillUtil
{
	
	public static ResourcesFillContext setResourcesFillContext(Map<String,Object> parameterValues)
	{
		ResourcesFillContext context = new ResourcesFillContext();
		
		ClassLoader classLoader = (ClassLoader) parameterValues.get(
				JRParameter.REPORT_CLASS_LOADER);
		if (classLoader != null)
		{
			JRResourcesUtil.setThreadClassLoader(classLoader);
			context.setClassLoader(classLoader);
		}
		
		return context;
	}

	public static void revertResourcesFillContext(ResourcesFillContext context)
	{
		if (context.getClassLoader() != null)
		{
			JRResourcesUtil.resetClassLoader();
		}
	}
	
	public static class ResourcesFillContext
	{
		protected ClassLoader classLoader;
		/**
		 * @deprecated To be removed.
		 */
		protected URLStreamHandlerFactory urlHandlerFactory;
		/**
		 * @deprecated To be removed.
		 */
		protected net.sf.jasperreports.engine.util.FileResolver fileResolver;
		
		public ClassLoader getClassLoader()
		{
			return classLoader;
		}
		
		public void setClassLoader(ClassLoader classLoader)
		{
			this.classLoader = classLoader;
		}
		
		/**
		 * @deprecated To be removed.
		 */
		public URLStreamHandlerFactory getUrlHandlerFactory()
		{
			return urlHandlerFactory;
		}
		
		/**
		 * @deprecated To be removed.
		 */
		public void setUrlHandlerFactory(URLStreamHandlerFactory urlHandlerFactory)
		{
			this.urlHandlerFactory = urlHandlerFactory;
		}
		
		/**
		 * @deprecated To be removed.
		 */
		public net.sf.jasperreports.engine.util.FileResolver getFileResolver()
		{
			return fileResolver;
		}
		
		/**
		 * @deprecated To be removed.
		 */
		public void setFileResolver(net.sf.jasperreports.engine.util.FileResolver fileResolver)
		{
			this.fileResolver = fileResolver;
		}
		
	}

	
	private JRResourcesFillUtil()
	{
	}
}
