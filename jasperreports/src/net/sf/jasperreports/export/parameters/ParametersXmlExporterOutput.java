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
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.export.XmlExporterOutput;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParametersXmlExporterOutput extends ParametersWriterExporterOutput implements XmlExporterOutput
{
	private static final String DEFAULT_XML_ENCODING = "UTF-8";
	private static final String XML_FILES_SUFFIX = "_files";

	/**
	 * 
	 */
	private Boolean isEmbeddingImages;
	private File imagesDir;
	
	/**
	 * 
	 */
	public ParametersXmlExporterOutput(
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
		
		isEmbeddingImages = Boolean.TRUE;

		StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb == null)
		{
			Writer writer = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
			if (writer == null)
			{
				OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
				if (os == null)
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
					
					imagesDir = new File(destFile.getParent(), destFile.getName() + XML_FILES_SUFFIX);
					
					Boolean isEmbeddingImagesParameter = (Boolean)parameters.get(JRXmlExporterParameter.IS_EMBEDDING_IMAGES);
					if (isEmbeddingImagesParameter == null)
					{
						isEmbeddingImagesParameter = Boolean.TRUE;
					}
					isEmbeddingImages = isEmbeddingImagesParameter.booleanValue();
				}
			}
		}
	}

	@Override
	protected void setEncoding()//FIXMEEXPORT why do we need override here?
	{
		encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = DEFAULT_XML_ENCODING;
		}
	}

	/**
	 * 
	 */
	public Boolean isEmbeddingImages()
	{
		return isEmbeddingImages;
	}
	
	/**
	 * 
	 */
	public File getImagesDir()
	{
		return imagesDir;
	}
}
