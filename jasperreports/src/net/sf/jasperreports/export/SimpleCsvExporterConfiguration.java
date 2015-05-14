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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleCsvExporterConfiguration extends SimpleExporterConfiguration implements CsvExporterConfiguration
{
	private String fieldDelimiter;	
	private String recordDelimiter;	
	private Boolean isWriteBOM;	

	/**
	 * 
	 */
	public SimpleCsvExporterConfiguration()
	{
	}
	
	/**
	 * 
	 */
	public String getFieldDelimiter()
	{
		return fieldDelimiter;
	}
	
	/**
	 * 
	 */
	public void setFieldDelimiter(String fieldDelimiter)
	{
		this.fieldDelimiter = fieldDelimiter;
	}
	
	/**
	 * 
	 */
	public String getRecordDelimiter()
	{
		return recordDelimiter;
	}
	
	/**
	 * 
	 */
	public void setRecordDelimiter(String recordDelimiter)
	{
		this.recordDelimiter = recordDelimiter;
	}
	
	/**
	 * 
	 */
	public Boolean isWriteBOM()
	{
		return isWriteBOM;
	}
	
	/**
	 * 
	 */
	public void setWriteBOM(Boolean isWriteBOM)
	{
		this.isWriteBOM = isWriteBOM;
	}
}
