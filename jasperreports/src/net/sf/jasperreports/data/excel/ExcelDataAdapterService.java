/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.data.excel;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.ExcelDataSource;
import net.sf.jasperreports.engine.data.JRXlsDataSource;
import net.sf.jasperreports.engine.query.JRXlsQueryExecuterFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: XlsDataAdapterService.java 6920 2014-02-24 09:42:04Z teodord $
 */
public class ExcelDataAdapterService extends AbstractDataAdapterService 
{
	
	/**
	 * 
	 */
	public ExcelDataAdapterService(JasperReportsContext jasperReportsContext, ExcelDataAdapter excelDataAdapter)
	{
		super(jasperReportsContext, excelDataAdapter);
	}
	
	public ExcelDataAdapter getExcelDataAdapter()
	{
		return (ExcelDataAdapter)getDataAdapter();
	}
	
	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException
	{
		ExcelDataAdapter excelDataAdapter = getExcelDataAdapter();
		if (excelDataAdapter != null)
		{
			try
			{
				String datePattern = excelDataAdapter.getDatePattern();
				String numberPattern = excelDataAdapter.getNumberPattern();
				String sheetSelection = excelDataAdapter.getSheetSelection();

				if (excelDataAdapter.isQueryExecuterMode())
				{	
					parameters.put(JRXlsQueryExecuterFactory.XLS_SOURCE, excelDataAdapter.getFileName());
					if (datePattern != null && datePattern.length() > 0)
					{
						parameters.put( JRXlsQueryExecuterFactory.XLS_DATE_FORMAT, new SimpleDateFormat(datePattern) );
					}
					if (numberPattern != null && numberPattern.length() > 0)
					{
						parameters.put( JRXlsQueryExecuterFactory.XLS_NUMBER_FORMAT, new DecimalFormat(numberPattern) );
					}
					parameters.put( JRXlsQueryExecuterFactory.XLS_USE_FIRST_ROW_AS_HEADER, new Boolean(excelDataAdapter.isUseFirstRowAsHeader()));
					
					if (sheetSelection != null && sheetSelection.length() > 0)
					{
						parameters.put( JRXlsQueryExecuterFactory.XLS_SHEET_SELECTION, sheetSelection );
					}
	
					if (!excelDataAdapter.isUseFirstRowAsHeader())
					{ 
						String[] names = new String[excelDataAdapter.getColumnNames().size()];
						int[] indexes = new int[excelDataAdapter.getColumnNames().size()];
						setupColumns(excelDataAdapter, names, indexes);
	
						parameters.put( JRXlsQueryExecuterFactory.XLS_COLUMN_NAMES_ARRAY, names);
						parameters.put( JRXlsQueryExecuterFactory.XLS_COLUMN_INDEXES_ARRAY, indexes);
					}
				}
				else
				{		
					ExcelDataSource ds = new ExcelDataSource(
												getJasperReportsContext(), 
												excelDataAdapter.getFileName(), 
												excelDataAdapter.getFormat());
					if (datePattern != null && datePattern.length() > 0)
					{
						ds.setDateFormat(new SimpleDateFormat(datePattern));
					}
					if (numberPattern != null && numberPattern.length() > 0)
					{
						ds.setNumberFormat(new DecimalFormat(numberPattern));
					}
		
					ds.setUseFirstRowAsHeader(excelDataAdapter.isUseFirstRowAsHeader());
		
					if (sheetSelection != null && sheetSelection.length() > 0)
					{
						ds.setSheetSelection(sheetSelection);
					}
					if (!excelDataAdapter.isUseFirstRowAsHeader())
					{
						String[] names = new String[excelDataAdapter.getColumnNames().size()];
						int[] indexes = new int[excelDataAdapter.getColumnNames().size()];
						setupColumns(excelDataAdapter, names, indexes);
						ds.setColumnNames( names, indexes);
					}
		
					parameters.put(JRParameter.REPORT_DATA_SOURCE, ds);
				}
			}
			catch (IOException e)
			{
				throw new JRException(e);
			}
		}
	}

	private void setupColumns(ExcelDataAdapter excelDataAdapter, String[] names,
			int[] indexes) {
		for (int i=0; i< names.length; ++i )
		{
			names[i] = "" + excelDataAdapter.getColumnNames().get(i);
			indexes[i] = (excelDataAdapter.getColumnIndexes().size() > i) ? excelDataAdapter.getColumnIndexes().get(i) : i;
		}
	}
	
	
}
