/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFontUtil.java 1498 2006-11-16 12:52:01Z teodord $
 */
public class SimpleFileResolver implements FileResolver
{

	protected List folders = null;
	
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
		}
		return null;
	}
	
	
}
