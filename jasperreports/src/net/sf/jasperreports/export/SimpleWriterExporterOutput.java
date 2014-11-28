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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleWriterExporterOutput implements WriterExporterOutput
{
	/**
	 * 
	 */
	private final String encoding;
	private Writer writer;
	private StringBuffer sbuffer;
	private boolean toClose;

	
	/**
	 * Creates a {@link WriterExporterOutput} instance which stores its result into the provided string buffer. 
	 * Useful for just storing the result in a string for later use.
	 */
	public SimpleWriterExporterOutput(StringBuffer sbuffer)
	{
		this.encoding = "UTF-8";
		this.sbuffer = sbuffer;
		
		if (sbuffer != null)
		{
			writer = new StringWriter();
			toClose = true;
		}
	}

	
	/**
	 * Creates a {@link WriterExporterOutput} instance that puts the result into the provided <tt>java.io.Writer</tt> object. 
	 * This is useful for sending the export result to a character stream, such as the <tt>PrintWriter</tt> of a servlet.
	 */
	public SimpleWriterExporterOutput(Writer writer)
	{
		this.encoding = "UTF-8";
		this.writer = writer;
		toClose = false;
	}
	

	/**
	 * Creates a {@link WriterExporterOutput} instance that puts the result into provided <tt>java.io.OutputStream</tt> object. 
	 * This is useful for sending the export result to an output stream, such as a <tt>ServletOutputStream</tt>.
	 */
	public SimpleWriterExporterOutput(OutputStream outputStream)
	{
		this(outputStream, "UTF-8");
	}

	
	/**
	 * 
	 */
	public SimpleWriterExporterOutput(OutputStream outputStream, String encoding)
	{
		this.encoding = encoding;
		if (outputStream != null)
		{
			try
			{
				writer = new OutputStreamWriter(outputStream, encoding);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		toClose = false;
	}
	

	/**
	 * Creates a {@link WriterExporterOutput} instance that puts the result into the provided <tt>java.io.File</tt> object. 
	 * This is useful when exporting to a file and the <tt>File</tt> instance is already there.
	 */
	public SimpleWriterExporterOutput(File file)
	{
		this(file, "UTF-8");
	}

	
	/**
	 * 
	 */
	public SimpleWriterExporterOutput(File file, String encoding)
	{
		this.encoding = encoding;
		if (file != null)
		{
			try
			{
				OutputStream os = new FileOutputStream(file);
				writer = new OutputStreamWriter(os, encoding);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
		toClose = true;
	}

	
	/**
	 * Creates a {@link WriterExporterOutput} instance that puts the result into the file designated by the provided file name. 
	 * This is an alternative to the {@link #SimpleWriterExporterOutput(File)}.
	 */
	public SimpleWriterExporterOutput(String fileName)
	{
		this(fileName == null ? null : new File(fileName));
	}

	
	/**
	 * 
	 */
	public SimpleWriterExporterOutput(String fileName, String encoding)
	{
		this(
			fileName == null ? null : new File(fileName),
			encoding
			);
	}

	
	/**
	 * The character encoding used for export.
	 */
	public String getEncoding()
	{
		return encoding;
	}

	
	/**
	 * 
	 */
	public Writer getWriter()
	{
		return writer;
	}

	
	/**
	 * 
	 */
	public void close()
	{
		if (toClose && writer != null)
		{
			try
			{
				writer.close();
			}
			catch (IOException e)
			{
			}

			if (sbuffer != null)
			{
				sbuffer.append(writer.toString());
			}
		}
	}
}
