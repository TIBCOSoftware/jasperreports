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
package net.sf.jasperreports.data.xls;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.AbstractXlsDataSource;
import net.sf.jasperreports.engine.query.AbstractXlsQueryExecuterFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: XlsDataAdapterService.java 6972 2014-03-12 11:41:51Z shertage $
 */
public abstract class AbstractXlsDataAdapterService extends AbstractDataAdapterService 
{
	public static final String PROPERTY_DATA_ADAPTER_USE_LEGACY_JEXCELAPI = JRPropertiesUtil.PROPERTY_PREFIX + "data.adapter.xls.use.legacy.jexcelapi";
	
	/**
	 * 
	 */
	public AbstractXlsDataAdapterService(JasperReportsContext jasperReportsContext, XlsDataAdapter xlsDataAdapter)
	{
		super(jasperReportsContext, xlsDataAdapter);
	}
	
	public XlsDataAdapter getXlsDataAdapter()
	{
		return (XlsDataAdapter)getDataAdapter();
	}
	
	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException
	{
		XlsDataAdapter xlsDataAdapter = getXlsDataAdapter();
		if (xlsDataAdapter != null)
		{
			String datePattern = xlsDataAdapter.getDatePattern();
			String numberPattern = xlsDataAdapter.getNumberPattern();
			String sheetSelection = xlsDataAdapter.getSheetSelection();

			if (xlsDataAdapter.isQueryExecuterMode())
			{	
				parameters.put(AbstractXlsQueryExecuterFactory.XLS_SOURCE, xlsDataAdapter.getFileName());
				if (datePattern != null && datePattern.length() > 0)
				{
					parameters.put( AbstractXlsQueryExecuterFactory.XLS_DATE_FORMAT, new SimpleDateFormat(datePattern) );
				}
				if (numberPattern != null && numberPattern.length() > 0)
				{
					parameters.put( AbstractXlsQueryExecuterFactory.XLS_NUMBER_FORMAT, new DecimalFormat(numberPattern) );
				}
				parameters.put( AbstractXlsQueryExecuterFactory.XLS_USE_FIRST_ROW_AS_HEADER, new Boolean(xlsDataAdapter.isUseFirstRowAsHeader()));

				if (sheetSelection != null && sheetSelection.length() > 0)
				{
					parameters.put( AbstractXlsQueryExecuterFactory.XLS_SHEET_SELECTION, sheetSelection );
				}

				if (!xlsDataAdapter.isUseFirstRowAsHeader())
				{ 
					String[] names = new String[xlsDataAdapter.getColumnNames().size()];
					Integer[] indexes = new Integer[xlsDataAdapter.getColumnNames().size()];
					setupColumns(xlsDataAdapter, names, indexes);

					parameters.put( AbstractXlsQueryExecuterFactory.XLS_COLUMN_NAMES_ARRAY, names);
					parameters.put( AbstractXlsQueryExecuterFactory.XLS_COLUMN_INDEXES_ARRAY, indexes);
				}
			}
			else
			{
				AbstractXlsDataSource ds = getXlsDataSource();

				if (datePattern != null && datePattern.length() > 0)
				{
					ds.setDateFormat(new SimpleDateFormat(datePattern));
				}
				if (numberPattern != null && numberPattern.length() > 0)
				{
					ds.setNumberFormat(new DecimalFormat(numberPattern));
				}
	
				ds.setUseFirstRowAsHeader(xlsDataAdapter.isUseFirstRowAsHeader());
	
				if (sheetSelection != null && sheetSelection.length() > 0)
				{
					ds.setSheetSelection(sheetSelection);
				}

				if (!xlsDataAdapter.isUseFirstRowAsHeader())
				{
					String[] names = new String[xlsDataAdapter.getColumnNames().size()];
					int[] indexes = new int[xlsDataAdapter.getColumnNames().size()];
					setupColumns(xlsDataAdapter, names, indexes);
					ds.setColumnNames( names, indexes);
				}
	
				parameters.put(JRParameter.REPORT_DATA_SOURCE, ds);
			}
		}
	}
	
	/**
	 * 
	 */
	protected abstract AbstractXlsDataSource getXlsDataSource() throws JRException;

	protected void setupColumns(XlsDataAdapter xlsDataAdapter, String[] names,
			int[] indexes) {
		for (int i=0; i< names.length; ++i )
		{
			names[i] = "" + xlsDataAdapter.getColumnNames().get(i);
			indexes[i] = (xlsDataAdapter.getColumnIndexes().size() > i) ? xlsDataAdapter.getColumnIndexes().get(i) : i;
		}
	}
	
	protected void setupColumns(XlsDataAdapter xlsDataAdapter, String[] names,
			Integer[] indexes) {
		for (int i=0; i< names.length; ++i )
		{
			names[i] = "" + xlsDataAdapter.getColumnNames().get(i);
			indexes[i] = (xlsDataAdapter.getColumnIndexes().size() > i) ? xlsDataAdapter.getColumnIndexes().get(i) : i;
		}
	}
	
}
