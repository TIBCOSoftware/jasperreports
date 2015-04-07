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
package net.sf.jasperreports.repo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FileRepositoryService implements StreamRepositoryService
{
	public static final String EXCEPTION_MESSAGE_KEY_NOT_IMPLEMENTED = "repo.file.not.implemented";
	
	private JasperReportsContext jasperReportsContext;
	private String root;
	private boolean resolveAbsolutePath;//FIXMEREPO consider giving up on this
	
	/**
	 * 
	 */
	public FileRepositoryService(
		JasperReportsContext jasperReportsContext,
		String root, 
		boolean resolveAbsolutePath
		)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.root = root;
		this.resolveAbsolutePath = resolveAbsolutePath;
	}
	
	/**
	 * 
	 */
	public void setRoot(String root)
	{
		this.root = root;
	}
	
	/**
	 * 
	 */
	public String getRoot()
	{
		return root;
	}
	
	/**
	 * 
	 */
	public InputStream getInputStream(String uri)
	{
		File file = null;

		if (uri != null)
		{
			file = new File(getRoot(), uri);
			if (!file.exists() || !file.isFile())
			{
				if (resolveAbsolutePath)
				{
					file = new File(uri);
					if (!file.exists() || !file.isFile())
					{
						file = null;
					}
				}
				else
				{
					file = null;
				}
			}

		}

		InputStream is = null;

		if (file != null)
		{
			try
			{
				is = new FileInputStream(file);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		
		return is;
	}
	
	/**
	 * 
	 */
	public OutputStream getOutputStream(String uri)
	{
		File file = null;

		if (uri != null)
		{
			file = new File(getRoot(), uri);
		}

		OutputStream os = null;

		if (file != null)
		{
			try
			{
				os = new FileOutputStream(file);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		
		return os;
	}
	
	/**
	 * 
	 */
	public Resource getResource(String uri)
	{
		throw 
			new JRRuntimeException(
				EXCEPTION_MESSAGE_KEY_NOT_IMPLEMENTED,
				(Object[])null);//FIXMEREPO
	}
	
	/**
	 * 
	 */
	public void saveResource(String uri, Resource resource)
	{
		PersistenceService persistenceService = PersistenceUtil.getInstance(jasperReportsContext).getService(FileRepositoryService.class, resource.getClass());
		if (persistenceService != null)
		{
			persistenceService.save(resource, uri, this);
		}
	}
	
	/**
	 * 
	 */
	public <K extends Resource> K getResource(String uri, Class<K> resourceType)
	{
		PersistenceService persistenceService = PersistenceUtil.getInstance(jasperReportsContext).getService(FileRepositoryService.class, resourceType);
		if (persistenceService != null)
		{
			return (K)persistenceService.load(uri, this);
		}
		return null;
	}
}
