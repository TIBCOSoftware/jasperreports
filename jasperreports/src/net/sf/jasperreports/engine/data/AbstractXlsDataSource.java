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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;


/**
 * This data source implementation reads an XLSX or XLS stream.
 * <p>
 * The default naming convention is to name report fields COLUMN_x and map each column with the field found at index x 
 * in each row (these indices start with 0). To avoid this situation, users can either specify a collection of column 
 * names or set a flag to read the column names from the first row of the XLSX or XLS file.
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public abstract class AbstractXlsDataSource extends JRAbstractTextDataSource implements JRRewindableDataSource
{
	public static final String EXCEPTION_MESSAGE_KEY_XLS_COLUMN_NAMES_MISMATCH_COLUMN_INDEXES = "data.xls.column.names.mismatch.column.indexes";
	public static final String EXCEPTION_MESSAGE_KEY_XLS_FIELD_VALUE_NOT_RETRIEVED = "data.xls.field.value.not.retrieved";
	public static final String EXCEPTION_MESSAGE_KEY_XLS_SHEET_INDEX_OUT_OF_RANGE = "data.xls.sheet.index.out.of.range";
	public static final String EXCEPTION_MESSAGE_KEY_XLS_SHEET_NOT_FOUND = "data.xls.sheet.not.found";
	
	protected String sheetSelection;
	
	protected DateFormat dateFormat = new SimpleDateFormat();
	protected NumberFormat numberFormat = new DecimalFormat();//these two here prevent commons beanutils conversion from superclass unless explicitly set to null; but it was like this since day one 
	protected Map<String, Integer> columnNames = new LinkedHashMap<String, Integer>();
	protected boolean useFirstRowAsHeader;


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
			this.columnNames.put(columnNames[i], i);
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
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_XLS_COLUMN_NAMES_MISMATCH_COLUMN_INDEXES,
					(Object[])null);
		}
		
		for (int i = 0; i < columnNames.length; i++)
		{
			this.columnNames.put(columnNames[i], columnIndexes[i]);
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
	public abstract void close();

	
	protected abstract void checkReadStarted();

	
	public Map<String, Integer> getColumnNames() 
	{
		return columnNames;
	}
	
	public String getSheetSelection() 
	{
		return sheetSelection;
	}


	public void setSheetSelection(String sheetSelection) 
	{
		checkReadStarted();

		this.sheetSelection = sheetSelection;
	}
	
}


