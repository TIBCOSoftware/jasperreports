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
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.FileHtmlResourceHandler;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.export.JsonExporterOutput;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParametersJsonExporterOutput extends ParametersWriterExporterOutput implements JsonExporterOutput
{
	/**
	 * 
	 */
	private HtmlResourceHandler fontHandler;
	private HtmlResourceHandler resourceHandler;
	
	/**
	 * 
	 */
	public ParametersJsonExporterOutput(
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
		
		StringBuffer sb = (StringBuffer)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb == null)
		{
			Writer writer = (Writer)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_WRITER);
			if (writer == null)
			{
				OutputStream os = (OutputStream)parameters.get(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_STREAM);
				if (os == null)
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

					if (fontHandler == null || resourceHandler == null)
					{
						File resourcesDir = new File(destFile.getParent(), destFile.getName() + "_files");
						FileHtmlResourceHandler defaultHandler = new FileHtmlResourceHandler(resourcesDir, resourcesDir.getName() + "/{0}");
						fontHandler = fontHandler == null ? defaultHandler : fontHandler;
						resourceHandler = resourceHandler == null ? defaultHandler : resourceHandler;
					}
				}
			}
		}
	}
	
	@Override
	public HtmlResourceHandler getFontHandler() 
	{
		return fontHandler;
	}

	@Override
	public HtmlResourceHandler getResourceHandler() 
	{
		return resourceHandler;
	}
}
