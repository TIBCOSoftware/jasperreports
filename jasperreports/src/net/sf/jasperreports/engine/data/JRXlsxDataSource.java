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
package net.sf.jasperreports.engine.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * This data source implementation reads an XLSX stream.
 * <p>
 * The default naming convention is to name report fields COLUMN_x and map each column with the field found at index x 
 * in each row (these indices start with 0). To avoid this situation, users can either specify a collection of column 
 * names or set a flag to read the column names from the first row of the XLSX file.
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JRXlsxDataSource extends AbstractPoiXlsDataSource
{
	/**
	 * Creates a data source instance from a workbook.
	 * @param workbook the workbook
	 */
	public JRXlsxDataSource(Workbook workbook)
	{
		super(workbook);
	}


	/**
	 * Creates a data source instance from an XLSX data input stream.
	 * @param inputStream an input stream containing XLSX data
	 */
	public JRXlsxDataSource(InputStream inputStream) throws JRException, IOException
	{
		super(inputStream);
	}


	/**
	 * Creates a data source instance from an XLSX file.
	 * @param file a file containing XLSX data
	 */
	public JRXlsxDataSource(File file) throws JRException, FileNotFoundException, IOException
	{
		super(file);
	}

	
	/**
	 * Creates a data source instance that reads XLSX data from a given location.
	 * @param jasperReportsContext the JasperReportsContext
	 * @param location a String representing XLSX data source
	 * @throws IOException 
	 */
	public JRXlsxDataSource(JasperReportsContext jasperReportsContext, String location) throws JRException, IOException
	{
		super(jasperReportsContext, location);
	}

	
	/**
	 * @see #JRXlsxDataSource(JasperReportsContext, String)
	 */
	public JRXlsxDataSource(String location) throws JRException, IOException
	{
		super(location);
	}


	/**
	 *
	 */
	protected Workbook loadWorkbook(InputStream inputStream) throws IOException
	{
		return new XSSFWorkbook(inputStream);
	}


}


