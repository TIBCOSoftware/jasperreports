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
package net.sf.jasperreports.engine.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * Exports a JasperReports document to CSV format based on the metadata provided.
 * <p/>
 * The exporter allows users to specify which columns should be included in the CSV export, what other value than the default
 * should they contain and whether the values for some columns should be auto filled when they are empty or missing (e.g. value 
 * for group columns)
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvMetadataExporter extends JRAbstractCsvExporter
{
	/**
	 * 
	 */
	protected boolean writeHeader;
	
	/**
	 * 
	 */
	protected List<String> columnNames;
	
	boolean isFirstRow = true;
	

	/**
	 * 
	 */
	protected void exportPage(JRPrintPage page) throws IOException
	{
		List<JRPrintElement> elements = page.getElements();
		Map<String, String> currentRow = new HashMap<String, String>();
		Map<String, String> repeatedValues = new HashMap<String, String>();
		
		boolean hasDefinedColumns = columnNames != null; // if columns where passed in as property
		String currentTextValue = null;
		
		for (int i = 0; i < elements.size(); ++i) 
		{
			Object element = elements.get(i);
			if (element instanceof JRPrintText) 
			{
				JRPrintText textElement = (JRPrintText) element;
				
				if (textElement.getPropertiesMap().getPropertyNames().length > 0) 
				{
					String currentColumnName = textElement.getPropertiesMap().getProperty(JRCsvMetadataExporterParameter.PROPERTY_COLUMN_NAME);
					String currentColumnData = textElement.getPropertiesMap().getProperty(JRCsvMetadataExporterParameter.PROPERTY_DATA);
					boolean repeatValue = JRProperties.getBooleanProperty(textElement, JRCsvMetadataExporterParameter.PROPERTY_REPEAT_VALUE, false);
					
					if (currentColumnData != null)
					{
						currentTextValue = currentColumnData;
						
					} else
					{
						JRStyledText styledText = getStyledText((JRPrintText)element);
						
						if (styledText != null)
						{
							currentTextValue = styledText.getText();
						} else
						{
							currentTextValue = "";
						}
					}
					
					// when no columns are provided, build the column names list as they are retrieved from the report element property
					if (!hasDefinedColumns)
					{
						if (columnNames == null) 
						{
							columnNames = new ArrayList<String>();
						}
						
						if (currentColumnName != null && currentColumnName.length() > 0 && !columnNames.contains(currentColumnName))
						{
							columnNames.add(currentColumnName);
						}
					}
					
					if (columnNames.size() > 0)
					{
						if (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export but was not read yet and comes in the expected order
						{
							currentRow.put(currentColumnName, currentTextValue);
								
						} else if ( (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && !isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export, was not read yet, but it is read after it should be
								|| (columnNames.contains(currentColumnName) && currentRow.containsKey(currentColumnName)) ) // the column is for export and was already read
						{
							// write header 
							if (isFirstRow && writeHeader)
							{
								writeReportHeader();
							}
							
							if (isFirstRow)	isFirstRow = false;
							writeCurrentRow(currentRow, repeatedValues);
							currentRow = new HashMap<String, String>();
							currentRow.put(currentColumnName, currentTextValue);
						}
						// set auto fill columns
						if (repeatValue && currentColumnName != null && currentColumnName.length() > 0 && currentTextValue.length() > 0)
						{
							repeatedValues.put(currentColumnName, currentTextValue);
						}
					}
				}
			}
		}
		// write last row
		if (columnNames != null && columnNames.size() > 0)
		{
			writeCurrentRow(currentRow, repeatedValues);
		}

		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}
	
	
	@Override
	protected void setParameters() {
		super.setParameters();
		
		writeHeader = getBooleanParameter(
				JRCsvMetadataExporterParameter.WRITE_HEADER, 
				JRCsvMetadataExporterParameter.PROPERTY_WRITE_HEADER,
				false
				); 
		
		setColumnNames();
	}

	
	/**
	 * 
	 */
	protected void setColumnNames()
	{
		String[] columnNamesArray = 
			getStringArrayParameter(
				JRCsvMetadataExporterParameter.COLUMN_NAMES,
				JRCsvMetadataExporterParameter.PROPERTY_COLUMN_NAMES_PREFIX
				);
		if (columnNamesArray != null && columnNamesArray.length > 0)
		{
			columnNames = new ArrayList<String>();
			for(int i = 0; i < columnNamesArray.length; i++)
			{
				if (columnNamesArray[i] == null)
				{
					columnNames.add(null);
				}
				else
				{
					String[] currentColumnNamesArray = columnNamesArray[i].split(",");
					for(int j = 0; j < currentColumnNamesArray.length; j++)
					{
						columnNames.add(currentColumnNamesArray[j].trim());
					}
				}
			}
		}
		
	}
	
	/**
	 * Writes the delimiter-separated column names
	 */
	protected void writeReportHeader() throws IOException 
	{
		StringBuffer rowBuffer = new StringBuffer();
		
		for (int i = 0; i < columnNames.size(); i++)
		{
			rowBuffer.append(columnNames.get(i));

			if (i < (columnNames.size()-1))
			{
				rowBuffer.append(delimiter);
			} else
			{
				rowBuffer.append(recordDelimiter);
			}
		}
		
		if (rowBuffer.length() > 0)
		{
			writer.write(rowBuffer.toString());
		}
	}
	

	/**
	 * Writes the current row.
	 * <p/>
	 * If the row is empty, nothing is written. If the are columns for auto fill (with valid data), they will be set on the current 
	 * row and the row will be written only if it was not originally empty. This prevents the export file from having rows just with auto filled data.
	 * <p/> 
	 * @param currentRow
	 * @param repeatedValues
	 * @throws IOException
	 */
	protected void writeCurrentRow(Map<String, String> currentRow, Map<String, String> repeatedValues) throws IOException
	{
		// FIXME: the rows that are incomplete (e.g. in case of a group, there are rows that contain only the group columns 
		// because the report spanned over a new page and it contains only the values for the group columns, as header, and other information
		// that is not for export, like counts or totals) should not be written

		StringBuffer rowBuffer = new StringBuffer();
		boolean isEmptyRow = true;
		
		for (int i = 0; i < columnNames.size(); i++)
		{
			String currentTextValue = currentRow.get(columnNames.get(i));
			if (currentTextValue != null && currentTextValue.length() > 0)
			{
				isEmptyRow = false;
				rowBuffer.append(prepareText(currentTextValue));
			} else
			{
				String repeatedValue = repeatedValues.get(columnNames.get(i));
				if (repeatedValue != null && repeatedValue.length() > 0)
				{
					rowBuffer.append(prepareText(repeatedValue));
				}
			}
			
			if (i < (columnNames.size()-1))
			{
				rowBuffer.append(delimiter);
			} else
			{
				rowBuffer.append(recordDelimiter);
			}
		}
		
		if (!isEmptyRow)
		{
			writer.write(rowBuffer.toString());
		}
	}
	
	
	/**
	 * Compares the highest index of the currentRow's columns with the index of the column to be inserted
	 * to determine if the current column is read in the proper order
	 * </p>
	 * @param currentRow
	 * @param currentColumnName
	 * @return
	 */
	private boolean isColumnReadOnTime(Map<String, String> currentRow, String currentColumnName)
	{
		int indexOfLastFilledColumn = -1;
		Set<String> currentlyFilledColumns = currentRow.keySet();
		
		for (String column: currentlyFilledColumns)
		{
			indexOfLastFilledColumn = Math.max(indexOfLastFilledColumn, columnNames.indexOf(column));
		}
		
		return indexOfLastFilledColumn < columnNames.indexOf(currentColumnName);
	}
	
}
