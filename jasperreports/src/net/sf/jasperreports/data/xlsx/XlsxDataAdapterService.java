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
package net.sf.jasperreports.data.xlsx;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRXlsxDataSource;
import net.sf.jasperreports.engine.query.JRXlsxQueryExecuterFactory;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class XlsxDataAdapterService extends AbstractDataAdapterService 
{
	
	/**
	 * 
	 */
	public XlsxDataAdapterService(JasperReportsContext jasperReportsContext, XlsxDataAdapter xlsxDataAdapter)
	{
		super(jasperReportsContext, xlsxDataAdapter);
	}
	
	/**
	 * @deprecated Replaced by {@link #XlsxDataAdapterService(JasperReportsContext, XlsxDataAdapter)}.
	 */
	public XlsxDataAdapterService(XlsxDataAdapter xlsxDataAdapter)
	{
		this(DefaultJasperReportsContext.getInstance(), xlsxDataAdapter);
	}
	
	public XlsxDataAdapter getXlsxDataAdapter()
	{
		return (XlsxDataAdapter)getDataAdapter();
	}
	
	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException
	{
		XlsxDataAdapter xlsxDataAdapter = getXlsxDataAdapter();
		if (xlsxDataAdapter != null)
		{
			try
			{
				String datePattern = xlsxDataAdapter.getDatePattern();
				String numberPattern = xlsxDataAdapter.getNumberPattern();
				String sheetSelection = xlsxDataAdapter.getSheetSelection();
				if (xlsxDataAdapter.isQueryExecuterMode())
				{	
					parameters.put(JRXlsxQueryExecuterFactory.XLSX_SOURCE, xlsxDataAdapter.getFileName());
					if (datePattern != null && datePattern.length() > 0)
					{
						parameters.put( JRXlsxQueryExecuterFactory.XLSX_DATE_FORMAT, new SimpleDateFormat(datePattern) );
					}
					if (numberPattern != null && numberPattern.length() > 0)
					{
						parameters.put( JRXlsxQueryExecuterFactory.XLSX_NUMBER_FORMAT, new DecimalFormat(numberPattern) );
					}
					parameters.put( JRXlsxQueryExecuterFactory.XLSX_USE_FIRST_ROW_AS_HEADER, new Boolean(xlsxDataAdapter.isUseFirstRowAsHeader()));
	
					if (sheetSelection != null && sheetSelection.length() > 0)
					{
						parameters.put( JRXlsxQueryExecuterFactory.XLSX_SHEET_SELECTION, sheetSelection );
					}
	
					if (!xlsxDataAdapter.isUseFirstRowAsHeader())
					{ 
						String[] names = new String[xlsxDataAdapter.getColumnNames().size()];
						Integer[] indexes = new Integer[xlsxDataAdapter.getColumnNames().size()];
						setupColumns(xlsxDataAdapter, names, indexes);
	
						parameters.put( JRXlsxQueryExecuterFactory.XLSX_COLUMN_NAMES_ARRAY, names);
						parameters.put( JRXlsxQueryExecuterFactory.XLSX_COLUMN_INDEXES_ARRAY, indexes);
					}
				}else{				
						JRXlsxDataSource ds = new JRXlsxDataSource(getJasperReportsContext(), xlsxDataAdapter.getFileName());
						if (datePattern != null && datePattern.length() > 0)
						{
							ds.setDateFormat(new SimpleDateFormat(datePattern));
						}
						if (numberPattern != null && numberPattern.length() > 0)
						{
							ds.setNumberFormat(new DecimalFormat(numberPattern));
						}
			
						ds.setUseFirstRowAsHeader(xlsxDataAdapter.isUseFirstRowAsHeader());
			
						if (sheetSelection != null && sheetSelection.length() > 0)
						{
							ds.setSheetSelection(sheetSelection);
						}
						
						if (!xlsxDataAdapter.isUseFirstRowAsHeader())
						{
							String[] names = new String[xlsxDataAdapter.getColumnNames().size()];
							int[] indexes = new int[xlsxDataAdapter.getColumnNames().size()];
							setupColumns(xlsxDataAdapter, names, indexes);
							ds.setColumnNames( names, indexes);
						}
			
						parameters.put(JRParameter.REPORT_DATA_SOURCE, ds);
					
				}
			}
			catch (Exception e)
			{
				throw new JRException(e);
			}
		}
	}

	private void setupColumns(XlsxDataAdapter xlsxDataAdapter, String[] names,
			int[] indexes) {
		for (int i=0; i< names.length; ++i )
		{
			names[i] = "" + xlsxDataAdapter.getColumnNames().get(i);
			indexes[i] = (xlsxDataAdapter.getColumnIndexes().size() > i) ? xlsxDataAdapter.getColumnIndexes().get(i) : i;
		}
	}
	
	private void setupColumns(XlsxDataAdapter xlsxDataAdapter, String[] names,
			Integer[] indexes) {
		for (int i=0; i< names.length; ++i )
		{
			names[i] = "" + xlsxDataAdapter.getColumnNames().get(i);
			indexes[i] = (xlsxDataAdapter.getColumnIndexes().size() > i) ? xlsxDataAdapter.getColumnIndexes().get(i) : i;
		}
	}
	
	
}
