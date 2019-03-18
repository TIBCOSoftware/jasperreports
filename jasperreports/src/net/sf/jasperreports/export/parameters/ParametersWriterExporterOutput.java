/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
import java.io.Writer;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.export.WriterExporterOutput;
import net.sf.jasperreports.util.StringBufferWriter;


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
		Map<net.sf.jasperreports.engine.JRExporterParameter, Object> parameters,
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
	
		StringBuffer sb = (StringBuffer)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			writer = new StringBufferWriter(sb);
		}
		else
		{
			writer = (Writer)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_WRITER);
			if (writer == null)
			{
				OutputStream os = (OutputStream)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_STREAM);
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
					File destFile = (File)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_FILE);
					if (destFile == null)
					{
						String fileName = (String)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_FILE_NAME);
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
				net.sf.jasperreports.engine.JRExporterParameter.CHARACTER_ENCODING, 
				PROPERTY_CHARACTER_ENCODING
				);
	}

	@Override
	public String getEncoding()
	{
		return encoding;
	}

	@Override
	public Writer getWriter()
	{
		return writer;
	}

	@Override
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
