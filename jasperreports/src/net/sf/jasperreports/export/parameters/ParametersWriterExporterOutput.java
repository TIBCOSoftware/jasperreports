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
package net.sf.jasperreports.export.parameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.export.WriterExporterOutput;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParametersWriterExporterOutput extends AbstractParametersExporterOutput implements WriterExporterOutput
{
	/**
	 * 
	 */
	protected String encoding;
	private Writer writer;
	private boolean toClose;
	
	/**
	 * 
	 */
	public ParametersWriterExporterOutput(
		JasperReportsContext jasperReportsContext,
		Map<JRExporterParameter, Object> parameters,
		JasperPrint jasperPrint
		)
	{
		super(
			jasperReportsContext,
			parameters,
			jasperPrint
			);
		
		setEncoding();
		
		toClose = false;
	
		StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			writer = new StringBufferWrapperWriter(sb);
			toClose = true;
		}
		else
		{
			writer = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
			if (writer == null)
			{
				OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
				if (os != null)
				{
					try
					{
						writer = new OutputStreamWriter(os, getEncoding());
					}
					catch (IOException e)
					{
						throw new JRRuntimeException(e);
					}
				}
				else
				{
					File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
					if (destFile == null)
					{
						String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
						if (fileName != null)
						{
							destFile = new File(fileName);
						}
						else
						{
							throw 
								new JRRuntimeException(
									EXCEPTION_MESSAGE_KEY_NO_OUTPUT_SPECIFIED,
									(Object[])null);
						}
					}

					try
					{
						os = new FileOutputStream(destFile);
						writer = new OutputStreamWriter(os, getEncoding());
						toClose = true;
					}
					catch (IOException e)
					{
						throw new JRRuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	protected void setEncoding()
	{
		encoding = 
			getParameterResolver().getStringParameterOrDefault(
				JRExporterParameter.CHARACTER_ENCODING, 
				PROPERTY_CHARACTER_ENCODING
				);
	}

	/**
	 * 
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
		if (toClose)
		{
			try
			{
				writer.close();
			}
			catch (IOException e)
			{
			}
		}
	}
}

class StringBufferWrapperWriter extends StringWriter
{
	private final StringBuffer sb;
	
	public StringBufferWrapperWriter(StringBuffer sb)
	{
		super();
		this.sb = sb;
	}

	@Override
	public void close() throws IOException
	{
		super.close();
		
		// this preserves existing functionality
		// implementing a writer that directly writes to sb might be better
		sb.append(toString());
	}
}