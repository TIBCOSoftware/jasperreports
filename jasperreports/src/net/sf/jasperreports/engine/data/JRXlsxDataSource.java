/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * This data source implementation reads an XLSX stream.
 * <p>
 * The default naming convention is to name report fields COLUMN_x and map each column with the field found at index x 
 * in each row (these indices start with 0). To avoid this situation, users can either specify a collection of column 
 * names or set a flag to read the column names from the first row of the CSV file.
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsxDataSource extends JRAbstractTextDataSource implements JRRewindableDataSource
{
	private Workbook workbook;
	private int sheetIndex;
	private DateFormat dateFormat = new SimpleDateFormat();
	private NumberFormat numberFormat = new DecimalFormat();
	private Map<String, Integer> columnNames = new HashMap<String, Integer>();
	private boolean useFirstRowAsHeader;
	private int recordIndex = -1;

	private InputStream inputStream;
	private boolean closeWorkbook;
	private boolean closeInputStream;


	/**
	 * Creates a data source instance from a workbook.
	 * @param workbook the workbook
	 */
	public JRXlsxDataSource(Workbook workbook)
	{
		this.workbook = workbook;
		this.closeWorkbook = false;
	}


	/**
	 * Creates a data source instance from an XLSX data input stream.
	 * @param is an input stream containing XLSX data
	 */
	public JRXlsxDataSource(InputStream is) throws JRException, IOException
	{
		try
		{
			this.inputStream = is;
			this.workbook = new XSSFWorkbook(is);
			this.closeWorkbook = true;
			this.closeInputStream = false;
		}
		catch (Exception e)
		{
			throw new JRException(e);
		}
	}


	/**
	 * Creates a data source instance from an XLSX file.
	 * @param file a file containing XLSX data
	 */
	public JRXlsxDataSource(File file) throws JRException, FileNotFoundException, IOException
	{
		this(new FileInputStream(file));
		this.closeInputStream = true;
	}

	
	/**
	 * Creates a datasource instance that reads XLSX data from a given location.
	 * @param location a String representing XLSX data source
	 * @throws IOException 
	 */
	public JRXlsxDataSource(String location) throws JRException, IOException
	{
		this(RepositoryUtil.getInputStream(location));
		this.closeInputStream = true;
	}
	

	/**
	 *
	 */
	public boolean next() throws JRException
	{
		recordIndex++;
		
		if (workbook != null)
		{

			if (recordIndex > workbook.getSheetAt(sheetIndex).getLastRowNum())
			{
				if( sheetIndex + 1 < workbook.getNumberOfSheets() 
					&& workbook.getSheetAt(sheetIndex + 1).getLastRowNum() > 0)
				{
					sheetIndex++;
					recordIndex = -1;
					return next();
				}
			}
			
			if (sheetIndex == 0 && useFirstRowAsHeader && recordIndex == 0) 
			{
				readHeader();
				recordIndex++;
			}

			if (recordIndex <= workbook.getSheetAt(sheetIndex).getLastRowNum())
			{
				return true;
			}
			else
			{
				if (closeWorkbook)
				{
					//FIXME: close workbook
					//workbook.close();
				}
			}
		}

		return false;
	}


	/**
	 *
	 */
	public void moveFirst()
	{
		this.recordIndex = -1;
		this.sheetIndex = 0;
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField jrField) throws JRException
	{
		String fieldName = jrField.getName();

		Integer columnIndex = columnNames.get(fieldName);
		if (columnIndex == null && fieldName.startsWith("COLUMN_")) {
			columnIndex = Integer.valueOf(fieldName.substring(7));
		}
		if (columnIndex == null)
		{
			throw new JRException("Unknown column name : " + fieldName);
		}
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		Cell cell = sheet.getRow(recordIndex).getCell(columnIndex);
		Class<?> valueClass = jrField.getValueClass();
		
		if (valueClass.equals(String.class)) 
		{
			return cell.getStringCellValue();
		}
		try {
			if (valueClass.equals(Boolean.class)) {
				return cell.getBooleanCellValue();
			}
			else if (Number.class.isAssignableFrom(valueClass))
			{
				if (numberFormat != null)
				{
					return getFormattedNumber(numberFormat, String.valueOf(cell.getNumericCellValue()), valueClass);
				}
				else 
				{
					return convertStringValue(String.valueOf(cell.getNumericCellValue()), valueClass);
				}
			}
			else if (Date.class.isAssignableFrom(valueClass)){
				if (dateFormat != null)
				{
					return getFormattedDate(dateFormat, String.valueOf(cell.getDateCellValue()), valueClass);
				} 
				else
				{
					return convertStringValue(String.valueOf(cell.getDateCellValue()), valueClass);
				}
			}
			else
			{
				throw new JRException("Field '" + jrField.getName() + "' is of class '" + valueClass.getName() + "' and can not be converted");
			}
		} catch (Exception e) {
			throw new JRException("Unable to get value for field '" + jrField.getName() + "' of class '" + valueClass.getName() + "'", e);
		}
	}


	/**
	 *
	 */
	private void readHeader()
	{
		Sheet sheet = workbook.getSheetAt(0);
		if (columnNames.size() == 0)
		{
			Row row = sheet.getRow(recordIndex);
			for(int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++)
			{
				Cell cell = row.getCell(columnIndex);
				if(cell != null)
				{
					columnNames.put(cell.toString(), Integer.valueOf(columnIndex));
				}
				else
				{
					columnNames.put("COLUMN_"+columnIndex, Integer.valueOf(columnIndex));
				}
			}
		}
		else
		{
			Map<String, Integer> newColumnNames = new HashMap<String, Integer>();
			for(Iterator<Integer> it = columnNames.values().iterator(); it.hasNext();)
			{
				Integer columnIndex = it.next();
				Row row = sheet.getRow(recordIndex) ;
				Cell cell = row.getCell(columnIndex);
				if(cell != null)
				{
					newColumnNames.put(cell.toString(), columnIndex);
				}
			}
			columnNames = newColumnNames;
		}
	}
	
	
	/**
	 * Gets the date format that will be used to parse date fields.
	 */
	public DateFormat getDateFormat()
	{
		return dateFormat;
	}


	/**
	 * Sets the desired date format to be used for parsing date fields.
	 */
	public void setDateFormat(DateFormat dateFormat)
	{
		checkReadStarted();
		
		this.dateFormat = dateFormat;
	}


	/**
	 * Gets the number format that will be used to parse numeric fields.
	 */
	public NumberFormat getNumberFormat() 
	{
		return numberFormat;
	}


	/**
	 * Sets the desired number format to be used for parsing numeric fields.
	 */
	public void setNumberFormat(NumberFormat numberFormat) 
	{
		checkReadStarted();
		
		this.numberFormat = numberFormat;
	}

	
	/**
	 * Specifies an array of strings representing column names matching field names in the report template.
	 */
	public void setColumnNames(String[] columnNames)
	{
		checkReadStarted();
		
		for (int i = 0; i < columnNames.length; i++)
		{
			this.columnNames.put(columnNames[i], Integer.valueOf(i));
		}
	}


	/**
	 * Specifies an array of strings representing column names matching field names in the report template 
	 * and an array of integers representing the column indexes in the sheet.
	 * Both array parameters must be not-null and have the same number of values.
	 */
	public void setColumnNames(String[] columnNames, int[] columnIndexes)
	{
		checkReadStarted();
		
		if (columnNames.length != columnIndexes.length)
		{
			throw new JRRuntimeException("The number of column names must be equal to the number of column indexes.");
		}
		
		for (int i = 0; i < columnNames.length; i++)
		{
			this.columnNames.put(columnNames[i], Integer.valueOf(columnIndexes[i]));
		}
	}


	/**
	 * Specifies an array of integers representing the column indexes in the sheet.
	 */
	public void setColumnIndexes(Integer[] columnIndexes)
	{
		checkReadStarted();
		
		for (int i = 0; i < columnIndexes.length; i++)
		{
			this.columnNames.put("COLUMN_" + i, columnIndexes[i]);
		}
	}


	/**
	 * Specifies whether the first row of the XLS file should be considered a table
	 * header, containing column names matching field names in the report template.
	 */
	public void setUseFirstRowAsHeader(boolean useFirstRowAsHeader)
	{
		checkReadStarted();
		
		this.useFirstRowAsHeader = useFirstRowAsHeader;
	}


	/**
	 * Closes the reader. Users of this data source should close it after usage.
	 */
	public void close()
	{
		try
		{
			if (closeInputStream)
			{
				inputStream.close();
			}
		}
		catch(IOException e)
		{
			//nothing to do
		}
	}


	private void checkReadStarted()
	{
		if (recordIndex > 0)
		{
			throw new JRRuntimeException("Cannot modify data source properties after data reading has started.");
		}
	}

	
	public Map<String, Integer> getColumnNames() {
		return columnNames;
	}
}


