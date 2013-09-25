/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: HtmlExporter.java 6502 2013-09-11 14:07:25Z lucianc $
 */
public abstract class AbstractHtmlExporter extends JRAbstractExporter
{
	/**
	 * 
	 */
	protected HtmlResourceHandler imageHandler;
	protected HtmlResourceHandler fontHandler;
	protected HtmlResourceHandler resourceHandler;
	
	/**
	 * 
	 */
	public AbstractHtmlExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
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

}
