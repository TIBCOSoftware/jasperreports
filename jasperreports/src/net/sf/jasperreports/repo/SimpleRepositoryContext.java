/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.repo;

import java.util.Map;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class SimpleRepositoryContext implements RepositoryContext
{
	private Map<String, Object> parameterValues;

	public SimpleRepositoryContext(Map<String, Object> parameterValues)
	{
		this.parameterValues = parameterValues;
	}

	public Object getValue(String parameterName)
	{
		return parameterValues == null ? null : parameterValues.get(parameterName);
	}

//	private ClassLoader classLoader;
//	private URLStreamHandlerFactory urlHandlerFactory;
//	private FileResolver fileResolver;
	
//	public void setContext(Map<String, Object> parameterValues)
//	{
//		classLoader = (ClassLoader)parameterValues.get(JRParameter.REPORT_CLASS_LOADER);
//		urlHandlerFactory = (URLStreamHandlerFactory)parameterValues.get(JRParameter.REPORT_URL_HANDLER_FACTORY);
//		fileResolver = (FileResolver)parameterValues.get(JRParameter.REPORT_FILE_RESOLVER);
//
//		if (classLoader != null)
//		{
//			JRResourcesUtil.setThreadClassLoader(classLoader);
//		}
//		
//		if (urlHandlerFactory != null)
//		{
//			JRResourcesUtil.setThreadURLHandlerFactory(urlHandlerFactory);
//		}
//		
//		if (fileResolver != null)
//		{
//			JRResourcesUtil.setThreadFileResolver(fileResolver);
//		}
//	}
//
//	public void revertContext()
//	{
//		if (classLoader != null)
//		{
//			JRResourcesUtil.resetClassLoader();
//		}
//		
//		if (urlHandlerFactory != null)
//		{
//			JRResourcesUtil.resetThreadURLHandlerFactory();
//		}
//		
//		if (fileResolver != null)
//		{
//			JRResourcesUtil.resetThreadFileResolver();
//		}
//	}
//
//	public ClassLoader getClassLoader()
//	{
//		return classLoader;
//	}
//	
//	public void setClassLoader(ClassLoader classLoader)
//	{
//		this.classLoader = classLoader;
//	}
//	
//	public URLStreamHandlerFactory getUrlHandlerFactory()
//	{
//		return urlHandlerFactory;
//	}
//	
//	public void setUrlHandlerFactory(URLStreamHandlerFactory urlHandlerFactory)
//	{
//		this.urlHandlerFactory = urlHandlerFactory;
//	}
//	
//	public FileResolver getFileResolver()
//	{
//		return fileResolver;
//	}
//	
//	public void setFileResolver(FileResolver fileResolver)
//	{
//		this.fileResolver = fileResolver;
//	}
}
