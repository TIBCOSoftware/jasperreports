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
package net.sf.jasperreports.engine.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import net.sf.jasperreports.engine.JRRuntimeException;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class FileHtmlResourceHandler implements HtmlResourceHandler 
{
	public static final String EXCEPTION_MESSAGE_KEY_RESOURCES_DIRECTORY_NOT_SPECIFIED = "export.html.resources.directory.not.specified";
	/**
	 * 
	 */
	private File parentFolder;
	private String pathPattern;

	/**
	 * 
	 */
	public FileHtmlResourceHandler(File parentFolder, String pathPattern)
	{
		this.parentFolder = parentFolder;
		this.pathPattern = pathPattern;
	}

	/**
	 * 
	 */
	public FileHtmlResourceHandler(File parentFolder)
	{
		this(parentFolder, null);
	}

	/**
	 * 
	 */
	public String getResourcePath(String id)
	{
		if (pathPattern == null)
		{
			return id;
		}
		return MessageFormat.format(pathPattern, new Object[]{id});
	}

	/**
	 * 
	 */
	public void handleResource(String id, byte[] data)
	{
		ensureParentFolder();
		
		FileOutputStream fos = null;
		
		try
		{
			fos = new FileOutputStream(new File(parentFolder, id));
			fos.write(data);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
	}
	
	private void ensureParentFolder()
	{
		if (parentFolder == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_RESOURCES_DIRECTORY_NOT_SPECIFIED,
					(Object[])null);
		}

		if (!parentFolder.exists())
		{
			parentFolder.mkdir();
		}
	}
}
