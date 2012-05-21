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
package net.sf.jasperreports.engine.export.zip;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class EmptyZipEntry implements ExportZipEntry 
{
	/**
	 * 
	 */
	private String name;
	
	/**
	 * 
	 */
	public EmptyZipEntry(String name)
	{
		this.name = name;
	}
	
	/**
	 * 
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 
	 */
	public Writer getWriter()
	{
		throw new JRRuntimeException("This method should not be called on this type");
	}
	
	/**
	 * 
	 */
	public OutputStream getOutputStream()
	{
		throw new JRRuntimeException("This method should not be called on this type");
	}
	
	/**
	 * 
	 */
	public void writeData(OutputStream os) throws IOException
	{
	}
	
	/**
	 * 
	 */
	public void dispose()
	{
	}
	
}
