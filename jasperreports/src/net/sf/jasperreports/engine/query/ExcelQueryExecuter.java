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
package net.sf.jasperreports.engine.query;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import net.sf.jasperreports.data.excel.ExcelFormatEnum;
import net.sf.jasperreports.data.xls.AbstractXlsDataAdapterService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.AbstractXlsDataSource;
import net.sf.jasperreports.engine.util.JRClassLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Excel query executer implementation.
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class ExcelQueryExecuter extends AbstractXlsQueryExecuter 
{
	private static final Log log = LogFactory.getLog(ExcelQueryExecuter.class);

	private static final String EXCEL_DATA_SOURCE_CLASS = "net.sf.jasperreports.engine.data.ExcelDataSource";
	private static final String XLS_DATA_SOURCE_CLASS = "net.sf.jasperreports.engine.data.XlsDataSource";
	private static final String XLSX_DATA_SOURCE_CLASS = "net.sf.jasperreports.engine.data.JRXlsxDataSource";
	private static final String JXL_DATA_SOURCE_CLASS = "net.sf.jasperreports.engine.data.JRXlsDataSource";
	private static final String JXL_WORKBOOK_CLASS = "jxl.Workbook";
	private static final String XLS_WORKBOOK_CLASS = "org.apache.poi.hssf.usermodel.HSSFWorkbook";
	private static final String XLSX_WORKBOOK_CLASS = "org.apache.poi.xssf.usermodel.XSSFWorkbook";
	
	
	/**
	 * 
	 */
	protected ExcelQueryExecuter(
		JasperReportsContext jasperReportsContext, 
		JRDataset dataset, 
		Map<String,? extends JRValueParameter> parametersMap
		) 
	{
		super(jasperReportsContext, dataset, parametersMap);
	}

	protected ExcelQueryExecuter(JRDataset dataset, Map<String,? extends JRValueParameter> parametersMap) 
	{
		this(DefaultJasperReportsContext.getInstance(), dataset, parametersMap);
	}

	public JRDataSource createDatasource() throws JRException 
	{
		String dataSourceClassName = null;
		Class<?>[] constrParamTypes = null;
		Object[] constrParamValues = null;

		@SuppressWarnings("deprecation")
		Object workbook = getParameterValue(JRXlsxQueryExecuterFactory.XLSX_WORKBOOK, true);
		if (workbook == null)
		{
			workbook = getParameterValue(AbstractXlsQueryExecuterFactory.XLS_WORKBOOK, true);
		}
		if (workbook != null) 
		{
			String workbookClassName = workbook.getClass().getName();
			if (JXL_WORKBOOK_CLASS.equals(workbookClassName))
			{
				dataSourceClassName = JXL_DATA_SOURCE_CLASS;
			}
			else if (XLS_WORKBOOK_CLASS.equals(workbookClassName))
			{
				dataSourceClassName = XLS_DATA_SOURCE_CLASS;
			}
			else if (XLSX_WORKBOOK_CLASS.equals(workbookClassName))
			{
				dataSourceClassName = XLSX_DATA_SOURCE_CLASS;
			}
			constrParamTypes = new Class<?>[]{workbook.getClass()};
			constrParamValues = new Object[]{workbook};
		}
		else 
		{
			ExcelFormatEnum format = null;
			Object objFormat = getParameterValue(ExcelQueryExecuterFactory.XLS_FORMAT, true);
			if (objFormat instanceof ExcelFormatEnum)
			{
				format = (ExcelFormatEnum)objFormat;
			}
			if (format == null)
			{
				format = ExcelFormatEnum.getByName(getStringParameterOrProperty(ExcelQueryExecuterFactory.XLS_FORMAT));
			}
			if (format == null)
			{
				format = ExcelFormatEnum.AUTODETECT;
			}
			
			switch (format)
			{
				case XLS :
				{
					if (getBooleanParameterOrProperty(AbstractXlsDataAdapterService.PROPERTY_DATA_ADAPTER_USE_LEGACY_JEXCELAPI, false))
					{
						dataSourceClassName = JXL_DATA_SOURCE_CLASS;
					}
					else
					{
						dataSourceClassName = XLS_DATA_SOURCE_CLASS;
					}
					break;
				}
				case XLSX :
				{
					dataSourceClassName = XLSX_DATA_SOURCE_CLASS;
					break;
				}
				case AUTODETECT :
				default:
				{
					dataSourceClassName = EXCEL_DATA_SOURCE_CLASS;
				}
			}
			
			@SuppressWarnings("deprecation")
			InputStream xlsInputStream = (InputStream)getParameterValue(JRXlsxQueryExecuterFactory.XLSX_INPUT_STREAM, true);
			if (xlsInputStream == null)
			{
				xlsInputStream = (InputStream)getParameterValue(AbstractXlsQueryExecuterFactory.XLS_INPUT_STREAM, true);
			}
			if (xlsInputStream != null) 
			{
				constrParamTypes = new Class<?>[]{InputStream.class};
				constrParamValues = new Object[]{xlsInputStream};
			}
			else 
			{
				@SuppressWarnings("deprecation")
				File xlsFile = (File)getParameterValue(JRXlsxQueryExecuterFactory.XLSX_FILE, true);
				if (xlsFile == null)
				{
					xlsFile = (File)getParameterValue(AbstractXlsQueryExecuterFactory.XLS_FILE, true);
				}
				if (xlsFile != null) 
				{
					constrParamTypes = new Class<?>[]{File.class};
					constrParamValues = new Object[]{xlsFile};
				}
				else 
				{
					@SuppressWarnings("deprecation")
					String xlsSource = getStringParameterOrProperty(JRXlsxQueryExecuterFactory.XLSX_SOURCE);
					if (xlsSource == null)
					{
						xlsSource = getStringParameterOrProperty(AbstractXlsQueryExecuterFactory.XLS_SOURCE);
					}
					if (xlsSource != null) 
					{
						constrParamTypes = new Class<?>[]{JasperReportsContext.class, String.class};
						constrParamValues = new Object[]{getJasperReportsContext(), xlsSource};
					}
					else 
					{
						if (log.isWarnEnabled())
						{
							log.warn("No Excel source was provided.");
						}
					}
				}
			}
		}
		
		AbstractXlsDataSource datasource = createDatasource(dataSourceClassName, constrParamTypes, constrParamValues);
		
		if (datasource != null) {
			initDatasource(datasource);
		}
		
		return datasource;
	}
	

	private AbstractXlsDataSource createDatasource(
		String dataSourceClassName, 
		Class<?>[] constrParamTypes,
		Object[] constrParamValues
		) throws JRException 
	{
		AbstractXlsDataSource datasource = null;
		
		try
		{
			@SuppressWarnings("unchecked")
			Class<? extends AbstractXlsDataSource> dataSourceClass = (Class<? extends AbstractXlsDataSource>) JRClassLoader.loadClassForName(dataSourceClassName);
			Constructor<? extends AbstractXlsDataSource> constructor = dataSourceClass.getConstructor(constrParamTypes);
			datasource = constructor.newInstance(constrParamValues);
		}
		catch (InvocationTargetException e)
		{
			throw new JRException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new JRException(e);
		}
		catch (InstantiationException e)
		{
			throw new JRException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new JRException(e);
		}
		catch (ClassNotFoundException e)
		{
			throw new JRException(e);
		}
		
		return datasource;
	}
	
}
