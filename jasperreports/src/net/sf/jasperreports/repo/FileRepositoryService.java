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
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.SimpleFileResolver;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class FileRepositoryService implements StreamRepositoryService
{
	/**
	 * 
	 */
	public void setContext(RepositoryContext context) //FIXMEREPO the context is useless here; consider refactoring
	{
	}
	
	public void revertContext()
	{
	}

	/**
	 * 
	 */
	public InputStream getInputStream(String uri)
	{
		try
		{
//			URL url = JRResourcesUtil.createURL(uri, null);
//			if (url != null)
//			{
//				return JRLoader.getInputStream(url);
//			}

			File file = JRResourcesUtil.resolveFile(uri, null);//FIXMEREPO give up file resolver and configure file repo service directly
			if (file != null)
			{
				return JRLoader.getInputStream(file);
			}

//			url = JRResourcesUtil.findClassLoaderResource(uri, null);
//			if (url != null)
//			{
//				return JRLoader.getInputStream(url);
//			}
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
	public OutputStream getOutputStream(String uri)
	{
		throw new UnsupportedOperationException();
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
	public void saveResource(String uri, Resource resource)//FIXMEREPO check this one
	{
		if (ReportResource.class.getName().equals(resource.getClass().getName()))
		{
			SimpleFileResolver fileResolver = (SimpleFileResolver)JRResourcesUtil.getFileResolver(null);
			File rootFolder = fileResolver.getFolders().get(0);
			File jasperFile = new File(rootFolder, uri);
			
			try
			{
				JRSaver.saveObject(((ReportResource)resource).getValue(), jasperFile);
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}
	
	/**
	 * 
	 */
	public <K extends Resource> K getResource(String uri, Class<K> resourceType)
	{
		if (InputStreamResource.class.getName().equals(resourceType.getName()))
		{
			return (K)new StreamPersistenceService().load(uri, this);
		}
		else if (ReportResource.class.getName().equals(resourceType.getName()))
		{
			return (K)new ReportPersistenceService().load(uri, this);
		}
		else if (DataAdapter.class.isAssignableFrom(resourceType))
		{
			return (K)new CastorDataAdapterPersistenceService().load(uri, this);
		}
		
		return null;
		//return (K)super.getResource(uri, resourceType);
	}
}
