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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class FileBufferedOasisZipEntry implements OasisZipEntry 
{
	/**
	 * 
	 */
	private String name = null;
	private FileBufferedOutputStream fbos = null;
	private Writer writer = null;
	
	/**
	 * 
	 */
	public FileBufferedOasisZipEntry(String name)
	{
		this(name, null);
	}
	
	/**
	 * 
	 */
	public FileBufferedOasisZipEntry(String name, byte[] bytes)
	{
		this.name = name;

		if (bytes == null)
		{
			fbos = new FileBufferedOutputStream();
		}
		else
		{
			fbos = new FileBufferedOutputStream(bytes.length);
			try
			{
				fbos.write(bytes);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
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
		if (writer == null)
		{

			writer = new BufferedWriter(new OutputStreamWriter(fbos, "UTF-8"));
		}
		
		return writer;
	}

	/**
	 * 
	 */
	public OutputStream getOutputStream()
	{
		return fbos;
	}

	/**
	 * 
	 */
	public void writeData(OutputStream os) throws IOException
	{
		fbos.writeData(os);
	}

	/**
	 * 
	 */
	public void dispose()
	{
		fbos.dispose();
	}
	
}
