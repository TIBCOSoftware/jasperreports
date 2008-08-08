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
package net.sf.jasperreports.engine.export.oasis.zip;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class EmptyOasisZipEntry implements OasisZipEntry 
{
	/**
	 * 
	 */
	private String name = null;
	
	/**
	 * 
	 */
	public EmptyOasisZipEntry(String name)
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
	public Writer getWriter() throws IOException
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
