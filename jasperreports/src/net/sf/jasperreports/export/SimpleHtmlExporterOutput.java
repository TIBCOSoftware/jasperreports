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
package net.sf.jasperreports.export;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import net.sf.jasperreports.engine.export.FileHtmlResourceHandler;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleHtmlExporterOutput extends SimpleWriterExporterOutput implements HtmlExporterOutput
{
	/**
	 * 
	 */
	private HtmlResourceHandler imageHandler;
	private HtmlResourceHandler fontHandler;
	private HtmlResourceHandler resourceHandler;

	
	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(StringBuffer sbuffer)
	{
		super(sbuffer);
	}

	
	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(Writer writer)
	{
		super(writer);
	}
	

	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(OutputStream outputStream)
	{
		super(outputStream);
	}

	
	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(OutputStream outputStream, String encoding)
	{
		super(outputStream, encoding);
	}
	

	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(File file)
	{
		super(file);
		
		setFileHandlers(file);
	}

	
	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(File file, String encoding)
	{
		super(file, encoding);
		
		setFileHandlers(file);
	}

	
	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(String fileName)
	{
		super(fileName);
		
		setFileHandlers(new File(fileName));
	}

	
	/**
	 * 
	 */
	public SimpleHtmlExporterOutput(String fileName, String encoding)
	{
		super(fileName, encoding);
		
		setFileHandlers(new File(fileName));
	}
	
	/**
	 * 
	 */
	public HtmlResourceHandler getImageHandler() 
	{
		return imageHandler;
	}

	/**
	 * 
	 */
	public void setImageHandler(HtmlResourceHandler imageHandler)
	{
		this.imageHandler = imageHandler;
	}
	
	/**
	 * 
	 */
	public HtmlResourceHandler getFontHandler() 
	{
		return fontHandler;
	}

	/**
	 * 
	 */
	public void setFontHandler(HtmlResourceHandler fontHandler)
	{
		this.fontHandler = fontHandler;
	}
	
	/**
	 * 
	 */
	public HtmlResourceHandler getResourceHandler() 
	{
		return resourceHandler;
	}

	/**
	 * 
	 */
	public void setResourceHandler(HtmlResourceHandler resourceHandler)
	{
		this.resourceHandler = resourceHandler;
	}
	
	/**
	 * 
	 */
	private void setFileHandlers(File destFile)
	{
		File resourcesDir = new File(destFile.getParent(), destFile.getName() + "_files");
		String pathPattern = resourcesDir.getName() + "/{0}";
		imageHandler = new FileHtmlResourceHandler(resourcesDir, pathPattern);
		fontHandler = new FileHtmlResourceHandler(resourcesDir, pathPattern);
		resourceHandler = new FileHtmlResourceHandler(resourcesDir);
	}
}
