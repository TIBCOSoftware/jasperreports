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

import jxl.Workbook;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.data.JRXlsDataSource;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * XLS query executer implementation.
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsQueryExecuter extends JRAbstractQueryExecuter {
	
	private static final Log log = LogFactory.getLog(JRXlsQueryExecuter.class);
	
	private JRXlsDataSource datasource;
	
	protected JRXlsQueryExecuter(JRDataset dataset, Map<String,? extends JRValueParameter> parametersMap) {
		super(dataset, parametersMap);
	}

	public JRDataSource createDatasource() throws JRException {
		try {
			Workbook workbook = (Workbook) getParameterValue(JRXlsQueryExecuterFactory.XLS_WORKBOOK);
			if (workbook != null) {
				datasource = new JRXlsDataSource(workbook);
			} else {
				InputStream xlsInputStream = (InputStream) getParameterValue(JRXlsQueryExecuterFactory.XLS_INPUT_STREAM);
				if (xlsInputStream != null) {
					datasource = new JRXlsDataSource(xlsInputStream);
				} else {
					File xlsFile = (File) getParameterValue(JRXlsQueryExecuterFactory.XLS_FILE);
					if (xlsFile != null) {
						datasource = new JRXlsDataSource(xlsFile);
					} else {
						String xlsSource = getStringParameterOrProperty(JRXlsQueryExecuterFactory.XLS_SOURCE);
						if (xlsSource != null) {
							datasource = new JRXlsDataSource(xlsSource);
						} else {
							if (log.isWarnEnabled()){
								log.warn("No XLS source was provided.");
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
			String columnNames = getStringParameterOrProperty(JRXlsQueryExecuterFactory.XLS_COLUMN_NAMES);
			
			if(columnNames != null) {
				columnNamesList = new ArrayList<String>();
				columnNamesList.add(columnNames);
			} else {
				String[] columnNamesArray = (String[]) getParameterValue(JRXlsQueryExecuterFactory.XLS_COLUMN_NAMES_ARRAY, true);
				if(columnNamesArray != null) {
					columnNamesList = Arrays.asList(columnNamesArray);
				} else {
					String propertiesPrefix = JRXlsQueryExecuterFactory.XLS_COLUMN_NAMES;
					List<PropertySuffix> properties = JRProperties.getAllProperties(dataset, propertiesPrefix);
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
			String columnIndexes = getStringParameterOrProperty(JRXlsQueryExecuterFactory.XLS_COLUMN_INDEXES);
			
			if (columnIndexes != null) {
				columnIndexesList = new ArrayList<Integer>();
				for (String colIndex: columnIndexes.split(",")){
					columnIndexesList.add(Integer.valueOf(colIndex.trim()));
				}
			} else {
				Integer[] columnIndexesArray = (Integer[]) getParameterValue(JRXlsQueryExecuterFactory.XLS_COLUMN_INDEXES_ARRAY, true);
				if (columnIndexesArray != null) {
					columnIndexesList = Arrays.asList(columnIndexesArray);
				} else {
					String propertiesPrefix = JRXlsQueryExecuterFactory.XLS_COLUMN_INDEXES;
					List<PropertySuffix> properties = JRProperties.getAllProperties(dataset, propertiesPrefix);
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
			
			
			DateFormat dateFormat = (DateFormat) getParameterValue(JRXlsQueryExecuterFactory.XLS_DATE_FORMAT, true);
			if (dateFormat!=null) {
				datasource.setDateFormat(dateFormat);
			} else {
				String dateFormatPattern = getStringParameterOrProperty(JRXlsQueryExecuterFactory.XLS_DATE_PATTERN);
				if(dateFormatPattern != null){
					datasource.setDatePattern(dateFormatPattern);
				}
			}
			
			NumberFormat numberFormat = (NumberFormat) getParameterValue(JRXlsQueryExecuterFactory.XLS_NUMBER_FORMAT, true);
			if (numberFormat != null) {
				datasource.setNumberFormat(numberFormat);
			} else {
				String numberFormatPattern = getStringParameterOrProperty(JRXlsQueryExecuterFactory.XLS_NUMBER_PATTERN);
				if(numberFormatPattern != null){
					datasource.setNumberPattern(numberFormatPattern);
				}
			}

			datasource.setUseFirstRowAsHeader(getBooleanParameterOrProperty(JRXlsQueryExecuterFactory.XLS_USE_FIRST_ROW_AS_HEADER, false));
			
			Locale csvLocale = (Locale) getParameterValue(JRParameter.REPORT_LOCALE, true);
			if (csvLocale != null) {
				datasource.setLocale(csvLocale);
			} else {
				String csvLocaleCode = getStringParameterOrProperty(JRXlsQueryExecuterFactory.XLS_LOCALE_CODE);
				if (csvLocaleCode != null) {
					datasource.setLocale(csvLocaleCode);
				}
			}
			
			TimeZone csvTimezone = (TimeZone) getParameterValue(JRParameter.REPORT_TIME_ZONE, true);
			if (csvTimezone != null) {
				datasource.setTimeZone(csvTimezone);
			} else {
				String csvTimezoneId = getStringParameterOrProperty(JRXlsQueryExecuterFactory.XLS_TIMEZONE_ID);
				if (csvTimezoneId != null) {
					datasource.setTimeZone(csvTimezoneId);
				}
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
