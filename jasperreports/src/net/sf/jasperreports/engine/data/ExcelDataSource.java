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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.data.excel.ExcelFormatEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * This data source implementation reads an XLSX or XLS stream.
 * <p>
 * The default naming convention is to name report fields COLUMN_x and map each column with the field found at index x 
 * in each row (these indices start with 0). To avoid this situation, users can either specify a collection of column 
 * names or set a flag to read the column names from the first row of the XLSX or XLS file.
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class ExcelDataSource extends AbstractPoiXlsDataSource
{
	private ExcelFormatEnum format;

	
	/**
	 * Creates a data source instance from a workbook with the default autodetect Excel format.
	 * @param workbook the workbook
	 */
	public ExcelDataSource(Workbook workbook)
	{
		super(workbook);
	}
	
	
	/**
	 * Creates a data source instance from an XLSX or XLS data input stream with the default autodetect Excel format.
	 * @param is an input stream containing XLSX or XLS data
	 */
	public ExcelDataSource(InputStream is) throws JRException, IOException
	{
		this(is, ExcelFormatEnum.AUTODETECT);
	}
	
	
	/**
	 * Creates a data source instance from an XLSX or XLS data input stream.
	 * @param is an input stream containing XLSX or XLS data
	 * @param format the Excel format 
	 */
	public ExcelDataSource(InputStream is, ExcelFormatEnum format) throws JRException, IOException
	{
		super(is);
		
		this.format = format;
	}


	/**
	 * Creates a data source instance from an XLSX or XLS file with the default autodetect Excel format.
	 * @param file a file containing XLSX or XLS data
	 * @throws FileNotFoundException 
	 */
	public ExcelDataSource(File file) throws JRException, IOException
	{
		this(file, ExcelFormatEnum.AUTODETECT);
	}
	
	
	/**
	 * Creates a data source instance from an XLSX or XLS file.
	 * @param file a file containing XLSX or XLS data
	 * @param format the Excel format 
	 * @throws FileNotFoundException 
	 */
	public ExcelDataSource(File file, ExcelFormatEnum format) throws JRException, IOException
	{
		super(file);
		
		this.format = format;
	}

	
	/**
	 * Creates a datasource instance that reads XLSX or XLS data from a given location.
	 * @param jasperReportsContext the JasperReportsContext
	 * @param location a String representing XLSX or XLS data source
	 * @throws IOException 
	 */
	public ExcelDataSource(JasperReportsContext jasperReportsContext, String location) throws JRException, IOException
	{
		this(jasperReportsContext, location, ExcelFormatEnum.AUTODETECT);
	}
	
	
	/**
	 * Creates a datasource instance that reads XLSX or XLS data from a given location.
	 * @param jasperReportsContext the JasperReportsContext
	 * @param location a String representing XLSX or XLS data source
	 * @param format the Excel format 
	 * @throws IOException 
	 */
	public ExcelDataSource(JasperReportsContext jasperReportsContext, String location, ExcelFormatEnum format) throws JRException, IOException
	{
		super(jasperReportsContext, location);
		
		this.format = format;
	}

	
	/**
	 * @see #ExcelDataSource(JasperReportsContext, String)
	 */
	public ExcelDataSource(String location) throws JRException, IOException
	{
		this(location, ExcelFormatEnum.AUTODETECT);
	}
	
	
	/**
	 * @see #ExcelDataSource(JasperReportsContext, String)
	 */
	public ExcelDataSource(String location, ExcelFormatEnum format) throws JRException, IOException
	{
		super(location);
		
		this.format = format;
	}


	/**
	 *
	 */
	protected Workbook loadWorkbook(InputStream inputStream) throws IOException
	{
		Workbook workbook = null;
		
		format = format == null ? ExcelFormatEnum.AUTODETECT : format;

		switch (format) 
		{
			case XLS :
				workbook = new HSSFWorkbook(inputStream);
				break;
			case XLSX : 
				workbook = new XSSFWorkbook(inputStream);
				break;
			case AUTODETECT:
			default:
				BufferedInputStream bis = new BufferedInputStream(inputStream);
				bis.mark(4);
				int test1 = bis.read();
				int test2 = bis.read();
				int test3 = bis.read();
				int test4 = bis.read();
				bis.reset();
				if(test1 == 'P' && test2 == 'K' && test3 == 0x03 && test4 == 0x04) 
				{
					workbook = new XSSFWorkbook(bis);
				} 
				else 
				{
					workbook = new HSSFWorkbook(bis);
				}
		}
		
		return workbook;
	}


}


