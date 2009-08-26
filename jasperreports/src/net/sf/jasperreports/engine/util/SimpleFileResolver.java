/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
 *(at your option) any later version.
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
 * @version $Id$
 */
public class SimpleFileResolver implements FileResolver
{

	protected List folders = null;
	private boolean isResolveAbsolutePath = false;
	
	/**
	 *
	 */
	public SimpleFileResolver(File parentFolder)
	{
		folders = new ArrayList();
		folders.add(parentFolder);
	}
	
	/**
	 *
	 */
	public SimpleFileResolver(List parentFolders)
	{
		folders = parentFolders;
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
			for(Iterator it = folders.iterator(); it.hasNext();)
			{
				File folder = (File)it.next();
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
