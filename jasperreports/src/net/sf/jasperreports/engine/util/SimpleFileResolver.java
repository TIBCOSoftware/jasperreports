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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleFileResolver implements FileResolver
{

	private List<File> folders;
	private boolean isResolveAbsolutePath;
	
	/**
	 *
	 */
	public SimpleFileResolver(File parentFolder)
	{
		folders = new ArrayList<File>();
		folders.add(parentFolder);
	}
	
	/**
	 *
	 */
	public SimpleFileResolver(List<File> parentFolders)
	{
		folders = parentFolders;
	}
	
	/**
	 *
	 */
	public List<File> getFolders()
	{
		return folders;
	}

	/**
	 *
	 */
	public boolean isResolveAbsolutePath()
	{
		return isResolveAbsolutePath;
	}

	/**
	 *
	 */
	public void setResolveAbsolutePath(boolean isResolveAbsolutePath)
	{
		this.isResolveAbsolutePath = isResolveAbsolutePath;
	}

	/**
	 *
	 */
	public File resolveFile(String fileName)
	{
		if (fileName != null)
		{
			for(Iterator<File> it = folders.iterator(); it.hasNext();)
			{
				File folder = it.next();
				File file = new File(folder, fileName);
				if (file.exists() && file.isFile())
				{
					return file;
				}
			}

			if (isResolveAbsolutePath)
			{
				File file = new File(fileName);
				if (file.exists() && file.isFile())
				{
					return file;
				}
			}
		}
		
		return null;
	}
	
	
}
