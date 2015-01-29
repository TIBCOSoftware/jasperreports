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
package net.sf.jasperreports.data.excel;

import java.io.IOException;
import java.util.Map;

import net.sf.jasperreports.data.xls.AbstractXlsDataAdapterService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.AbstractXlsDataSource;
import net.sf.jasperreports.engine.data.ExcelDataSource;
import net.sf.jasperreports.engine.query.ExcelQueryExecuterFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ExcelDataAdapterService extends AbstractXlsDataAdapterService 
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
		super.contributeParameters(parameters);

		ExcelDataAdapter xlsDataAdapter = getExcelDataAdapter();
		if (xlsDataAdapter != null)
		{
			ExcelFormatEnum format = xlsDataAdapter.getFormat();

			if (xlsDataAdapter.isQueryExecuterMode())
			{	
				if(format != null) 
				{
					parameters.put( ExcelQueryExecuterFactory.XLS_FORMAT, format);
				}
			}
		}
	}

	@Override
	protected AbstractXlsDataSource getXlsDataSource() throws JRException
	{
		ExcelDataAdapter excelDataAdapter = getExcelDataAdapter();
		
		AbstractXlsDataSource dataSource = null;
		try
		{
			dataSource =
				new ExcelDataSource(
					dataStream,
					excelDataAdapter.getFormat()
					);
		}
		catch (IOException e)
		{
			throw new JRException(e);
		}
		return dataSource;
	}

}
