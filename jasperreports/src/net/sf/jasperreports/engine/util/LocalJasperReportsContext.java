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
package net.sf.jasperreports.engine.util;

import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.repo.DefaultRepositoryService;
import net.sf.jasperreports.repo.RepositoryService;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class LocalJasperReportsContext extends SimpleJasperReportsContext
{
	/**
	 *
	 */
	private List<RepositoryService> localRepositoryServices;
	protected DefaultRepositoryService localRepositoryService;

	/**
	 *
	 */
	public LocalJasperReportsContext(JasperReportsContext parent)
	{
		super(parent);
	}

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public static JasperReportsContext getLocalContext(JasperReportsContext jasperReportsContext, Map<String,Object> parameterValues)
	{
		if (
			parameterValues.containsKey(JRParameter.REPORT_CLASS_LOADER)
			|| parameterValues.containsKey(JRParameter.REPORT_URL_HANDLER_FACTORY)
			|| parameterValues.containsKey(JRParameter.REPORT_FILE_RESOLVER)
			)
		{
			LocalJasperReportsContext localJasperReportsContext = new LocalJasperReportsContext(jasperReportsContext);

			if (parameterValues.containsKey(JRParameter.REPORT_CLASS_LOADER))
			{
				localJasperReportsContext.setClassLoader((ClassLoader)parameterValues.get(JRParameter.REPORT_CLASS_LOADER));
			}

			if (parameterValues.containsKey(JRParameter.REPORT_URL_HANDLER_FACTORY))
			{
				localJasperReportsContext.setURLStreamHandlerFactory((URLStreamHandlerFactory)parameterValues.get(JRParameter.REPORT_URL_HANDLER_FACTORY));
			}

			if (parameterValues.containsKey(JRParameter.REPORT_FILE_RESOLVER))
			{
				localJasperReportsContext.setFileResolver((FileResolver)parameterValues.get(JRParameter.REPORT_FILE_RESOLVER));
			}
			
			return localJasperReportsContext;
		}

		return jasperReportsContext;
	}

	/**
	 *
	 */
	protected DefaultRepositoryService getLocalRepositoryService()
	{
		if (localRepositoryService == null)
		{
			localRepositoryService = new DefaultRepositoryService(this);
		}
		return localRepositoryService;
	}

	/**
	 *
	 */
	public void setClassLoader(ClassLoader classLoader)
	{
		getLocalRepositoryService().setClassLoader(classLoader);
	}

	/**
	 *
	 */
	public void setURLStreamHandlerFactory(URLStreamHandlerFactory urlHandlerFactory)
	{
		getLocalRepositoryService().setURLStreamHandlerFactory(urlHandlerFactory);
	}

	/**
	 *
	 */
	public void setFileResolver(FileResolver fileResolver)
	{
		getLocalRepositoryService().setFileResolver(fileResolver);
	}

	@Override
	public <T> List<T> getExtensions(Class<T> extensionType)
	{
		if (
			localRepositoryService != null
			&& RepositoryService.class.equals(extensionType)
			)
		{
			// we cache repository service extensions from parent and replace the DefaultRepositoryService instance, if present among them 
			if (localRepositoryServices == null)
			{
				List<RepositoryService> repoServices = super.getExtensions(RepositoryService.class);
				if (repoServices != null && repoServices.size() > 0)
				{
					localRepositoryServices = new ArrayList<RepositoryService>();
					for (RepositoryService repoService : repoServices)
					{
						if (repoService instanceof DefaultRepositoryService)
						{
							localRepositoryServices.add(localRepositoryService);
						}
						else
						{
							localRepositoryServices.add(repoService);
						}
					}
				}
			}
			return (List<T>)localRepositoryServices;
		}
		return super.getExtensions(extensionType);
	}
	
}
