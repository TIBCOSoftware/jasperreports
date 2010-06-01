/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * This data source implementation reads an XLS stream.
 * <p>
 * The default naming convention is to name report fields COLUMN_x and map each column with the field found at index x 
 * in each row (these indices start with 0). To avoid this situation, users can either specify a collection of column 
 * names or set a flag to read the column names from the first row of the CSV file.
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRCsvDataSource.java 3033 2009-08-27 11:46:22Z teodord $
 */
public class JRXlsDataSource implements JRRewindableDataSource
{
	private Workbook workbook = null;

	private DateFormat dateFormat = new SimpleDateFormat();
	private NumberFormat numberFormat = new DecimalFormat();
	private Map columnNames = new HashMap();
	private boolean useFirstRowAsHeader = false;
	private int recordIndex = -1;

	private InputStream inputStream = null;
	private boolean closeWorkbook = false;


	/**
	 * Creates a data source instance from a workbook.
	 * @param workbook the workbook
	 */
	public JRXlsDataSource(Workbook workbook)
	{
		this.workbook = workbook;
		this.closeWorkbook = false;
	}


	/**
	 * Creates a data source instance from an XLS data input stream.
	 * @param is an input stream containing XLS data
	 */
	public JRXlsDataSource(InputStream is) throws JRException, IOException
	{
		try
		{
			this.workbook = Workbook.getWorkbook(is);
			this.closeWorkbook = true;
		}
		catch (BiffException e)
		{
			throw new JRException(e);
		}
	}


	/**
	 * Creates a data source instance from an XLS file.
	 * @param file a file containing XLS data
	 */
	public JRXlsDataSource(File file) throws JRException, FileNotFoundException, IOException
	{
		this(new FileInputStream(file));
	}


	/**
	 *
	 */
	public boolean next() throws JRException
	{
		recordIndex++;

		if (workbook != null)
		{
			if (recordIndex == 0 && useFirstRowAsHeader) 
			{
				readHeader();
				recordIndex++;
			}

			if (recordIndex < workbook.getSheet(0).getRows())
			{
				return true;
			}
			else
			{
				if (closeWorkbook)
				{
					workbook.close();
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
	}


	/**
	 *
	 */
	public Object getFieldValue(JRField jrField) throws JRException
	{
		String fieldName = jrField.getName();

		Integer columnIndex = (Integer) columnNames.get(fieldName);
		if (columnIndex == null && fieldName.startsWith("COLUMN_")) {
			columnIndex = Integer.valueOf(fieldName.substring(7));
		}
		if (columnIndex == null)
		{
			throw new JRException("Unknown column name : " + fieldName);
		}
		Sheet sheet = workbook.getSheet(0);
		Cell cell = sheet.getCell(columnIndex.intValue(), recordIndex);
		String fieldValue = cell.getContents();
		Class valueClass = jrField.getValueClass();
		
		if (valueClass.equals(String.class)) 
		{
			return fieldValue;
		}
		fieldValue = fieldValue.trim();
		
		if (fieldValue.length() == 0)
		{
			return null;
		}		
		try {
			if (valueClass.equals(Boolean.class)) {
				return fieldValue.equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
			}
			else if (valueClass.equals(Byte.class)) {
				return new Byte((numberFormat.parse(fieldValue)).byteValue());
			}
			else if (valueClass.equals(Integer.class)) {
				return Integer.valueOf((numberFormat.parse(fieldValue)).intValue());
			}
			else if (valueClass.equals(Long.class)) {
				return new Long((numberFormat.parse(fieldValue)).longValue());
			}
			else if (valueClass.equals(Short.class)) {
				return new Short((numberFormat.parse(fieldValue)).shortValue());
			}
			else if (valueClass.equals(Double.class)) {
				return new Double((numberFormat.parse(fieldValue)).doubleValue());
			}
			else if (valueClass.equals(Float.class)) {
				return new Float((numberFormat.parse(fieldValue)).floatValue());
			}
			else if (valueClass.equals(BigDecimal.class)) {
				return new BigDecimal((numberFormat.parse(fieldValue)).toString());
			}
			else if (valueClass.equals(BigInteger.class)) {
				return new BigInteger(String.valueOf(numberFormat.parse(fieldValue).longValue()));
			}
			else if(valueClass.equals(java.lang.Number.class)) {
				return numberFormat.parse(fieldValue);
			}
			else if (valueClass.equals(java.util.Date.class)) {
				return dateFormat.parse(fieldValue);
			}
			else if (valueClass.equals(java.sql.Timestamp.class)) {
				return new java.sql.Timestamp(dateFormat.parse(fieldValue).getTime());
			}
			else if (valueClass.equals(java.sql.Time.class)) {
				return new java.sql.Time(dateFormat.parse(fieldValue).getTime());
			}
			else
			{
				throw new JRException("Field '" + jrField.getName() + "' is of class '" + valueClass.getName() + "' and can not be converted");
			}
		} catch (Exception e) {
			throw new JRException("Unable to get value for field '" + jrField.getName() + "' of class '" + valueClass.getName() + "'", e);
		}

		//throw new JRException("Unknown column name : " + fieldName);
	}


	/**
	 *
	 */
	private void readHeader()
	{
		Sheet sheet = workbook.getSheet(0);
		if (columnNames.size() == 0)
		{
			for(int columnIndex = 0; columnIndex < sheet.getColumns(); columnIndex++)
			{
				Cell cell = sheet.getCell(columnIndex, recordIndex);
				String columnName = cell.getContents();
				if (columnName != null && columnName.trim().length() > 0)
				{
					columnNames.put(columnName, Integer.valueOf(columnIndex));
				}
			}
		}
		else
		{
			Map newColumnNames = new HashMap();
			for(Iterator it = columnNames.values().iterator(); it.hasNext();)
			{
				Integer columnIndex = (Integer)it.next();
				Cell cell = sheet.getCell(columnIndex.intValue(), recordIndex);
				String columnName = cell.getContents();
				
				newColumnNames.put(columnName, columnIndex);
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
			inputStream.close();
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
}


