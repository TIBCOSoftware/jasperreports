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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.ContextClassLoaderObjectInputStream;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRResourcesUtil;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DefaultRepositoryService implements RepositoryService
{
//	/**
//	 * 
//	 */
//	public DefaultRepositoryContext createContext() 
//	{
//		return new DefaultRepositoryContext();
//	}
	
	private ThreadLocal<RepositoryContext> localContext = new ThreadLocal<RepositoryContext>();

	/**
	 * 
	 */
	public void setContext(RepositoryContext context) 
	{
		localContext.set(context);
		
		ClassLoader classLoader = (ClassLoader)context.getValue(JRParameter.REPORT_CLASS_LOADER);
		URLStreamHandlerFactory urlHandlerFactory = (URLStreamHandlerFactory)context.getValue(JRParameter.REPORT_URL_HANDLER_FACTORY);
		FileResolver fileResolver = (FileResolver)context.getValue(JRParameter.REPORT_FILE_RESOLVER);

		if (classLoader != null)
		{
			JRResourcesUtil.setThreadClassLoader(classLoader);
		}
		
		if (urlHandlerFactory != null)
		{
			JRResourcesUtil.setThreadURLHandlerFactory(urlHandlerFactory);
		}
		
		if (fileResolver != null)
		{
			JRResourcesUtil.setThreadFileResolver(fileResolver);
		}
	}
	
	public void revertContext()
	{
		RepositoryContext context = localContext.get();
		
		ClassLoader classLoader = (ClassLoader)context.getValue(JRParameter.REPORT_CLASS_LOADER);
		URLStreamHandlerFactory urlHandlerFactory = (URLStreamHandlerFactory)context.getValue(JRParameter.REPORT_URL_HANDLER_FACTORY);
		FileResolver fileResolver = (FileResolver)context.getValue(JRParameter.REPORT_FILE_RESOLVER);

		if (classLoader != null)
		{
			JRResourcesUtil.resetClassLoader();
		}
		
		if (urlHandlerFactory != null)
		{
			JRResourcesUtil.resetThreadURLHandlerFactory();
		}
		
		if (fileResolver != null)
		{
			JRResourcesUtil.resetThreadFileResolver();
		}
	}

	/**
	 * 
	 */
	public InputStream getInputStream(String uri)
	{
		try
		{
			URL url = JRResourcesUtil.createURL(uri, null);
			if (url != null)
			{
				return JRLoader.getInputStream(url);
			}

			File file = JRResourcesUtil.resolveFile(uri, null);
			if (file != null)
			{
				return JRLoader.getInputStream(file);
			}

			url = JRResourcesUtil.findClassLoaderResource(uri, null);
			if (url != null)
			{
				return JRLoader.getInputStream(url);
			}
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	public Resource getResource(String uri)
	{
		throw new JRRuntimeException("Not implemented.");//FIXMEREPO
	}
	
	/**
	 * 
	 */
	public void saveResource(String uri, Resource resource)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 */
	public <K extends Resource> K getResource(String uri, Class<K> resourceType)
	{
		if (ReportResource.class.getName().equals(resourceType.getName()))
		{
			ReportResource resource = null;
			JasperReport report = (JasperReport)loadObject(uri);
			if (report != null)
			{
				resource = new ReportResource();
				resource.setReport(report);
			}
			return (K)resource;
		}
		
		ObjectResource resource = null;
		Object object = loadObject(uri);
		if (object != null)
		{
			resource = new ObjectResource();
			resource.setValue(object);
		}
		return (K)resource;
	}

	/**
	 *
	 */
	private Object loadObject(String location)
	{
		Object obj = null;

		InputStream is = getInputStream(location);
		
		if (is != null)
		{
			ObjectInputStream ois = null;

			try
			{
				ois = new ContextClassLoaderObjectInputStream(is);
				obj = ois.readObject();
			}
			catch (IOException e)
			{
				throw new JRRuntimeException("Error loading object from : " + location, e);
			}
			catch (ClassNotFoundException e)
			{
				throw new JRRuntimeException("Class not found when loading object from : " + location, e);
			}
			finally
			{
				if (is != null)
				{
					try
					{
						is.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		}

		return obj;
	}


}
