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
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.export.OutputStreamExporterOutput;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParametersOutputStreamExporterOutput extends AbstractParametersExporterOutput implements OutputStreamExporterOutput
{
	/**
	 * 
	 */
	private OutputStream outputStream;
	private boolean toClose;
	
	/**
	 * 
	 */
	public ParametersOutputStreamExporterOutput(
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
		
		toClose = false;
		outputStream = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
		if (outputStream == null)
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
				outputStream = new FileOutputStream(destFile);
				toClose = true;
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
	public OutputStream getOutputStream()
	{
		return outputStream;
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
				outputStream.close();
			}
			catch (IOException e)
			{
			}
		}
	}
}
