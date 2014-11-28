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
import java.io.OutputStream;
import java.io.Writer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleXmlExporterOutput extends SimpleWriterExporterOutput implements XmlExporterOutput
{
	/**
	 * 
	 */
	private Boolean isEmbeddingImages;

	
	/**
	 * 
	 */
	public SimpleXmlExporterOutput(StringBuffer sbuffer)
	{
		super(sbuffer);
	}

	
	/**
	 * 
	 */
	public SimpleXmlExporterOutput(Writer writer)
	{
		super(writer);
	}
	

	/**
	 * 
	 */
	public SimpleXmlExporterOutput(OutputStream outputStream)
	{
		super(outputStream);
	}

	
	/**
	 * 
	 */
	public SimpleXmlExporterOutput(OutputStream outputStream, String encoding)
	{
		super(outputStream, encoding);
	}
	

	/**
	 * 
	 */
	public SimpleXmlExporterOutput(File file)
	{
		super(file);
	}

	
	/**
	 * 
	 */
	public SimpleXmlExporterOutput(File file, String encoding)
	{
		super(file, encoding);
	}

	
	/**
	 * 
	 */
	public SimpleXmlExporterOutput(String fileName)
	{
		super(fileName);
	}

	
	/**
	 * 
	 */
	public SimpleXmlExporterOutput(String fileName, String encoding)
	{
		super(fileName, encoding);
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
	public void setEmbeddingImages(Boolean isEmbeddingImages)
	{
		this.isEmbeddingImages = isEmbeddingImages;
	}
}
