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
package net.sf.jasperreports.engine.query;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.data.excel.ExcelFormatEnum;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.ExcelDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel query executer implementation.
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class ExcelQueryExecuter extends JRAbstractQueryExecuter {
	
	private static final Log log = LogFactory.getLog(ExcelQueryExecuter.class);
	
	private ExcelDataSource datasource;
	
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

	public JRDataSource createDatasource() throws JRException {
		try {
			Workbook workbook = (Workbook) getParameterValue(ExcelQueryExecuterFactory.EXCEL_WORKBOOK);
			ExcelFormatEnum format = (ExcelFormatEnum) getParameterValue(ExcelQueryExecuterFactory.EXCEL_FORMAT);
			if (workbook != null) {
				datasource = new ExcelDataSource(workbook);
			} else {
				InputStream xlsxInputStream = (InputStream) getParameterValue(ExcelQueryExecuterFactory.EXCEL_INPUT_STREAM);
				if (xlsxInputStream != null) {
					datasource = new ExcelDataSource(xlsxInputStream, format);
				} else {
					File xlsxFile = (File) getParameterValue(ExcelQueryExecuterFactory.EXCEL_FILE);
					if (xlsxFile != null) {
						datasource = new ExcelDataSource(xlsxFile, format);
					} else {
						String xlsxSource = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_SOURCE);
						if (xlsxSource != null) {
							datasource = new ExcelDataSource(getJasperReportsContext(), xlsxSource, format);
						} else {
							if (log.isWarnEnabled()){
								log.warn("No Excel source was provided.");
							}
						}
					}
				}
			}
		} catch (IOException e) {
			throw new JRException(e);
		}
		
		if (datasource != null) {
			// build column names list
			List<String> columnNamesList = null;
			String columnNames = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_COLUMN_NAMES);
			
			if(columnNames != null) {
				columnNamesList = new ArrayList<String>();
				columnNamesList.add(columnNames);
			} else {
				String[] columnNamesArray = (String[]) getParameterValue(ExcelQueryExecuterFactory.EXCEL_COLUMN_NAMES_ARRAY, true);
				if(columnNamesArray != null) {
					columnNamesList = Arrays.asList(columnNamesArray);
				} else {
					String propertiesPrefix = ExcelQueryExecuterFactory.EXCEL_COLUMN_NAMES;
					List<PropertySuffix> properties = getPropertiesUtil().getAllProperties(dataset, propertiesPrefix);
					if (properties != null && !properties.isEmpty()) {
						columnNamesList = new ArrayList<String>();
						for(int i = 0; i < properties.size(); i++) {
							PropertySuffix property = properties.get(i);
							columnNamesList.add(property.getValue());
						}
					} else {
						JRField[] fields = dataset.getFields();
						if (fields != null && fields.length > 0)
						{
							columnNamesList = new ArrayList<String>();
							for (int i = 0; i < fields.length; i++)
							{
								columnNamesList.add(fields[i].getName());
							}
						}
					}
				}
			}
			List<String> splitColumnNamesList = null;
			if (columnNamesList != null && columnNamesList.size() > 0) {
				splitColumnNamesList = new ArrayList<String>();
				for(int i = 0; i < columnNamesList.size(); i++) {
					String names = columnNamesList.get(i);
					for(String token: names.split(",")){
						splitColumnNamesList.add(token.trim());
					}
				}
			} 
			
			// build column indexes list
			List<Integer> columnIndexesList = null;
			String columnIndexes = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_COLUMN_INDEXES);
			
			if (columnIndexes != null) {
				columnIndexesList = new ArrayList<Integer>();
				for (String colIndex: columnIndexes.split(",")){
					columnIndexesList.add(Integer.valueOf(colIndex.trim()));
				}
			} else {
				Integer[] columnIndexesArray = (Integer[]) getParameterValue(ExcelQueryExecuterFactory.EXCEL_COLUMN_INDEXES_ARRAY, true);
				if (columnIndexesArray != null) {
					columnIndexesList = Arrays.asList(columnIndexesArray);
				} else {
					String propertiesPrefix = ExcelQueryExecuterFactory.EXCEL_COLUMN_INDEXES;
					List<PropertySuffix> properties = getPropertiesUtil().getAllProperties(dataset, propertiesPrefix);
					if (properties != null && !properties.isEmpty()) {
						columnIndexesList = new ArrayList<Integer>();
						for(int i = 0; i < properties.size(); i++) {
							String propertyValue = properties.get(i).getValue();
							for (String colIndex: propertyValue.split(",")){
								columnIndexesList.add(Integer.valueOf(colIndex.trim()));
							}
						}
					}
				}
			}
			
			// set column names or column indexes or both
			if (splitColumnNamesList != null) {
				if (columnIndexesList != null) {
					int[] indexesArray = new int[columnIndexesList.size()];
					for (int i=0; i<columnIndexesList.size(); i++) {
						indexesArray[i] = columnIndexesList.get(i);
					}
					datasource.setColumnNames(splitColumnNamesList.toArray(new String[splitColumnNamesList.size()]), indexesArray);
				} else {
					datasource.setColumnNames(splitColumnNamesList.toArray(new String[splitColumnNamesList.size()]));
				}
			} else if (columnIndexesList != null) {
				datasource.setColumnIndexes(columnIndexesList.toArray(new Integer[columnIndexesList.size()]));
			} else {
				if (log.isWarnEnabled()){
					log.warn("No column names or column indexes were specified.");
				}
			}
			
			
			DateFormat dateFormat = (DateFormat) getParameterValue(ExcelQueryExecuterFactory.EXCEL_DATE_FORMAT, true);
			if (dateFormat!=null) {
				datasource.setDateFormat(dateFormat);
			} else {
				String dateFormatPattern = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_DATE_PATTERN);
				if(dateFormatPattern != null){
					datasource.setDatePattern(dateFormatPattern);
				}
			}
			
			NumberFormat numberFormat = (NumberFormat) getParameterValue(ExcelQueryExecuterFactory.EXCEL_NUMBER_FORMAT, true);
			if (numberFormat != null) {
				datasource.setNumberFormat(numberFormat);
			} else {
				String numberFormatPattern = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_NUMBER_PATTERN);
				if(numberFormatPattern != null){
					datasource.setNumberPattern(numberFormatPattern);
				}
			}

			datasource.setUseFirstRowAsHeader(getBooleanParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_USE_FIRST_ROW_AS_HEADER, false));
			
			Locale xlsxLocale = (Locale) getParameterValue(JRParameter.REPORT_LOCALE, true);
			if (xlsxLocale != null) {
				datasource.setLocale(xlsxLocale);
			} else {
				String xlsxLocaleCode = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_LOCALE_CODE);
				if (xlsxLocaleCode != null) {
					datasource.setLocale(xlsxLocaleCode);
				}
			}
			
			TimeZone xlsxTimezone = (TimeZone) getParameterValue(JRParameter.REPORT_TIME_ZONE, true);
			if (xlsxTimezone != null) {
				datasource.setTimeZone(xlsxTimezone);
			} else {
				String xlsxTimezoneId = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_TIMEZONE_ID);
				if (xlsxTimezoneId != null) {
					datasource.setTimeZone(xlsxTimezoneId);
				}
			}
			
			String sheetSelection = getStringParameterOrProperty(ExcelQueryExecuterFactory.EXCEL_SHEET_SELECTION);
			if (sheetSelection != null && sheetSelection.length() > 0) 
			{
				datasource.setSheetSelection(sheetSelection);
			}
		}
		
		return datasource;
	}

	public void close() {
		if(datasource != null){
			datasource.close();
		}
	}

	public boolean cancelQuery() throws JRException {
		return false;
	}

	@Override
	protected String getParameterReplacement(String parameterName) {
		return String.valueOf(getParameterValue(parameterName));
	}
	
}
