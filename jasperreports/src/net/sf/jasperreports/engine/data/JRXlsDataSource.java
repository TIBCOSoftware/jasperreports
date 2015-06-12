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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.FormatUtils;
import net.sf.jasperreports.repo.RepositoryUtil;


/**
 * This data source implementation reads an XLS stream.
 * <p>
 * The default naming convention is to name report fields COLUMN_x and map each column with the field found at index x 
 * in each row (these indices start with 0). To avoid this situation, users can either specify a collection of column 
 * names or set a flag to read the column names from the first row of the CSV file.
 *
 * @deprecated Replaced by {@link XlsDataSource}.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRXlsDataSource extends AbstractXlsDataSource
{
	private Workbook workbook;

	private int sheetIndex = -1;
	private int recordIndex = -1;

	private InputStream inputStream;
	private boolean closeWorkbook;
	private boolean closeInputStream;


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
			this.inputStream = is;
			this.workbook = Workbook.getWorkbook(is);
			this.closeWorkbook = true;
			this.closeInputStream = false;
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
		this.closeInputStream = true;
	}

	
	/**
	 * Creates a datasource instance that reads XLS data from a given location.
	 * @param jasperReportsContext the JasperReportsContext
	 * @param location a String representing XLS data source
	 * @throws IOException 
	 */
	public JRXlsDataSource(JasperReportsContext jasperReportsContext, String location) throws JRException, IOException
	{
		this(RepositoryUtil.getInstance(jasperReportsContext).getInputStreamFromLocation(location));
		this.closeInputStream = true;
	}

	
	/**
	 * @see #JRXlsDataSource(JasperReportsContext, String)
	 */
	public JRXlsDataSource(String location) throws JRException, IOException
	{
		this(DefaultJasperReportsContext.getInstance(), location);
	}
	

	/**
	 *
	 */
	public boolean next() throws JRException
	{
		if (workbook != null)
		{
			//initialize sheetIndex before first record
			if (sheetIndex < 0)
			{
				if (sheetSelection == null)
				{
					sheetIndex = 0;
				}
				else
				{
					try
					{
						sheetIndex = Integer.parseInt(sheetSelection);
						if (sheetIndex < 0 || sheetIndex > workbook.getNumberOfSheets() - 1)
						{
							throw 
								new JRRuntimeException(
									EXCEPTION_MESSAGE_KEY_XLS_SHEET_INDEX_OUT_OF_RANGE,
									new Object[]{sheetIndex, (workbook.getNumberOfSheets() - 1)});
						}
					}
					catch (NumberFormatException e)
					{
					}
					
					if (sheetIndex < 0)
					{
						for (int i = 0; i < workbook.getSheets().length; i++) 
						{	
							if (sheetSelection.equals(workbook.getSheet(i).getName())) 
							{
								this.sheetIndex = i;
								break;
							}
						}

						if (sheetIndex < 0)
						{
							throw 
								new JRRuntimeException(
									EXCEPTION_MESSAGE_KEY_XLS_SHEET_NOT_FOUND,
									new Object[]{sheetSelection});
						}
					}
				}
			}

			recordIndex++;

			if (sheetSelection == null) 
			{
				if (recordIndex > workbook.getSheet(sheetIndex).getRows() - 1)
				{
					if (sheetIndex + 1 < workbook.getNumberOfSheets() 
						&& workbook.getSheet(sheetIndex + 1).getRows() > 0)
					{
						sheetIndex++;
						recordIndex = -1;
						return next();
					}
				}
			}
			
			if ((sheetSelection != null || sheetIndex == 0) && useFirstRowAsHeader && recordIndex == 0) 
			{
				readHeader();
				recordIndex++;
			}

			if (recordIndex <= workbook.getSheet(sheetIndex).getRows()-1)
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
		this.sheetIndex = -1;
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
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_UNKNOWN_COLUMN_NAME,
					new Object[]{fieldName});
		}
		Sheet sheet = workbook.getSheet(sheetIndex);
		Cell cell = sheet.getCell(columnIndex.intValue(), recordIndex);
		String fieldValue = cell.getContents();
		Class<?> valueClass = jrField.getValueClass();
		
		if (valueClass.equals(String.class)) 
		{
			return fieldValue;
		}
		fieldValue = fieldValue.trim();
		
		if (fieldValue.length() == 0)
		{
			return null;
		}		
		try 
		{
			if (valueClass.equals(Boolean.class)) 
			{
				return convertStringValue(fieldValue, valueClass);
			}
			else if (Number.class.isAssignableFrom(valueClass))
			{
				if (numberFormat != null)
				{
					return FormatUtils.getFormattedNumber(numberFormat, fieldValue, valueClass);
				}
				else 
				{
					return convertStringValue(fieldValue, valueClass);
				}
			}
			else if (Date.class.isAssignableFrom(valueClass))
			{
				if (dateFormat != null)
				{
					return FormatUtils.getFormattedDate(dateFormat, fieldValue, valueClass);
				} 
				else
				{
					return convertStringValue(fieldValue, valueClass);
				}
			}
			else
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_CANNOT_CONVERT_FIELD_TYPE,
						new Object[]{jrField.getName(), valueClass.getName()});
			}
		}
		catch (Exception e) 
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_XLS_FIELD_VALUE_NOT_RETRIEVED,
					new Object[]{jrField.getName(), valueClass.getName()}, 
					e);
		}
	}


	/**
	 *
	 */
	private void readHeader()
	{
		Sheet sheet = workbook.getSheet(sheetSelection != null ? sheetIndex : 0);
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
				else
				{
					columnNames.put("COLUMN_" + columnIndex, Integer.valueOf(columnIndex));
				}				
			}
		}
		else
		{
			Map<String, Integer> newColumnNames = new LinkedHashMap<String, Integer>();
			for(Iterator<Integer> it = columnNames.values().iterator(); it.hasNext();)
			{
				Integer columnIndex = it.next();
				Cell cell = sheet.getCell(columnIndex.intValue(), recordIndex);
				String columnName = cell.getContents();
				
				newColumnNames.put(columnName, columnIndex);
			}
			columnNames = newColumnNames;
		}
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


	protected void checkReadStarted()
	{
		if (sheetIndex >= 0)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_CANNOT_MODIFY_PROPERTIES_AFTER_START,
					(Object[])null);
		}
	}
}


