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




/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: FileRepositoryService.java 4882 2012-01-09 14:54:19Z teodord $
 */
public class WebFileRepositoryService extends FileRepositoryService
{
	private String root;
	private static String applicationRealPath;
	private String realRoot;
	
	/**
	 * 
	 */
	public WebFileRepositoryService(String root, boolean resolveAbsolutePath)
	{
		super(root, resolveAbsolutePath);
		
		this.root = root;
	}
	
	/**
	 * 
	 */
	public static void setApplicationRealPath(String appRealPath)
	{
		applicationRealPath = appRealPath;
	}
	
	/**
	 * 
	 */
	public static String getApplicationRealPath()
	{
		return applicationRealPath;
	}

	@Override
	public String getRoot()
	{
		if (realRoot == null && applicationRealPath != null)
		{
			realRoot = new File(new File(applicationRealPath), root).getAbsolutePath();
		}
		return realRoot;
	}

	@Override
	public InputStream getInputStream(String uri)
	{
		if (getRoot() != null)
		{
			return super.getInputStream(uri);
		}
		
		return null;
	}
}
