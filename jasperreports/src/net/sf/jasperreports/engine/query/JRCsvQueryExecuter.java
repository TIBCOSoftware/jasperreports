/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRProperties.PropertySuffix;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Csv query executer implementation.
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvQueryExecuter extends JRAbstractQueryExecuter {
	
	private static final Log log = LogFactory.getLog(JRCsvQueryExecuter.class);
	
	private boolean isCsvStreamCreatedLocally;
	
	private JRCsvDataSource datasource;
	
	protected JRCsvQueryExecuter(JRDataset dataset, Map parametersMap) {
		super(dataset, parametersMap);
	}

	public JRDataSource createDatasource() throws JRException {
		Reader csvReader = (Reader) getParameterValue(JRCsvQueryExecuterFactory.CSV_READER); 
		InputStream csvInputStream = (InputStream) getParameterValue(JRCsvQueryExecuterFactory.CSV_INPUTSTREAM);
		File csvFile = (File) getParameterValue(JRCsvQueryExecuterFactory.CSV_FILE);
		String csvCharset = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_CHARSET);
		URL csvUrl = (URL) getParameterValue(JRCsvQueryExecuterFactory.CSV_URL);
		String csvInputStreamSource = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_INPUTSTREAM_SOURCE);
		
		try {
			if (csvReader != null) {
				log.info("Got csv reader");
				datasource = new JRCsvDataSource(csvReader);
			} else if (csvInputStream != null) {
				log.info("Got csv inputstream");
				if (csvCharset != null) {
					datasource = new JRCsvDataSource(csvInputStream, csvCharset);
				} else {
					datasource = new JRCsvDataSource(csvInputStream);
				}
			} else if (csvFile != null) {
				log.info("Got csv file");
				if (csvCharset != null) {
					datasource = new JRCsvDataSource(csvFile, csvCharset);
				} else {
					datasource = new JRCsvDataSource(csvFile);
				}
			} else if (csvUrl != null) {
				log.info("Got csv url");
				csvFile = (File) JRLoader.loadObject(csvUrl);
				isCsvStreamCreatedLocally = true;

				if (csvCharset != null) {
					datasource = new JRCsvDataSource(csvFile, csvCharset);
				} else {
					datasource = new JRCsvDataSource(csvFile);
				}				
			} else if (csvInputStreamSource != null) {
				log.info("Got csv inputstream source");
				csvInputStream = JRLoader.getLocationInputStream(csvInputStreamSource);
				isCsvStreamCreatedLocally = true;
				
				if (csvCharset != null) {
					datasource = new JRCsvDataSource(csvInputStream, csvCharset);
				} else {
					datasource = new JRCsvDataSource(csvInputStream);
				}
			} else {
				if (log.isWarnEnabled()){
					log.warn("No csv source was provided.");
				}
			}
		} catch (FileNotFoundException e) {
			throw new JRException(e);
		} catch (UnsupportedEncodingException e) {
			throw new JRException(e);
		}
		
		if (datasource != null) {
			String columnNames = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_COLUMN_NAMES, true);
			if(columnNames != null) {
				log.info("setting csv datasource columnNames: " + columnNames);
				datasource.setColumnNames(columnNames.split(","));
			}
			
			String[] columnNamesArray = (String[]) getParameterValue(JRCsvQueryExecuterFactory.CSV_COLUMN_NAMES_ARRAY, true);
			if(columnNamesArray != null) {
				log.info("setting csv datasource columnNamesArray");
				datasource.setColumnNames(columnNamesArray);
			} else {
				JRPropertiesMap propsMap = dataset.getPropertiesMap();
				if (propsMap != null) {
					List properties = JRProperties.getProperties(propsMap, JRCsvQueryExecuterFactory.PROPERTY_CSV_COLUMN_NAMES_PREFIX);
					if (properties != null) {
						List columnNamesList = new LinkedList();
						int propertiesNo = properties.size();
						
						for(int i = 0; i < propertiesNo; i++) {
							String property = ((PropertySuffix)properties.get(i)).getValue();
							if(property.indexOf(",") != -1) {
								for(String token: property.split(",")){
									columnNamesList.add(token.trim());
								}
							} else {
								columnNamesList.add(property);
							}
						}
						
						if (columnNamesList.size() > 0) {
							columnNamesArray = (String[]) columnNamesList.toArray(new String[]{});
						}
					}
				}
				
				if (columnNamesArray != null && columnNamesArray.length>0) {
					log.info("setting csv datasource columnNamesArray from properties");
					datasource.setColumnNames(columnNamesArray);
				} else {
					if (log.isWarnEnabled()){
						log.warn("No columns were specified.");
					}
				}
			}
			
			DateFormat dateFormat = (DateFormat) getParameterValue(JRCsvQueryExecuterFactory.CSV_DATE_FORMAT, true);
			String dateFormatPattern = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_DATE_PATTERN, true);
			if (dateFormat!=null) {
				log.info("setting csv dateFormat");
				datasource.setDateFormat(dateFormat);
			} else if(dateFormatPattern != null){
				log.info("setting csv dateFormat with pattern");
				datasource.setDatePattern(dateFormatPattern);
			}
			
			NumberFormat numberFormat = (NumberFormat) getParameterValue(JRCsvQueryExecuterFactory.CSV_NUMBER_FORMAT, true);
			String numberFormatPattern = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_NUMBER_PATTERN, true);
			if (numberFormat != null) {
				log.info("setting csv numberFormat");
				datasource.setNumberFormat(numberFormat);
			} else if(numberFormatPattern != null){
				log.info("setting csv numberFormat with pattern");
				datasource.setNumberPattern(numberFormatPattern);
			}

			Character fieldDelimiter = (Character) getParameterValue(JRCsvQueryExecuterFactory.CSV_FIELD_DELIMITER, true);
			if (fieldDelimiter != null) {
				log.info("setting csv fieldDelimiter: " + fieldDelimiter);
				datasource.setFieldDelimiter(fieldDelimiter);
			}
			
			String recordDelimiter = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_RECORD_DELIMITER, true);
			if (recordDelimiter != null) {
				log.info("setting csv datasource record delimiter: " + recordDelimiter);
				datasource.setRecordDelimiter(recordDelimiter);
			}
			
			Boolean useFirstRowAsHeader = (Boolean) getParameterValue(JRCsvQueryExecuterFactory.CSV_USE_FIRST_ROW_AS_HEADER, true);
			if (useFirstRowAsHeader != null) {
				log.info("setting csv useFirstRowAsHeader: " + useFirstRowAsHeader);
				datasource.setUseFirstRowAsHeader(useFirstRowAsHeader);
			}
			
			Locale csvLocale = (Locale) getParameterValue(JRParameter.REPORT_LOCALE, true);
			String csvLocaleId = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_LOCALE_ID, true);
			if (csvLocale != null) {
				datasource.setLocale(csvLocale);
			} else if (csvLocaleId != null) {
				datasource.setLocale(csvLocaleId);
			}
			
			TimeZone csvTimezone = (TimeZone) getParameterValue(JRParameter.REPORT_TIME_ZONE, true);
			String csvTimezoneId = (String) getParameterValue(JRCsvQueryExecuterFactory.CSV_TIMEZONE_ID, true);
			if (csvTimezone != null) {
				datasource.setTimeZone(csvTimezone);
			} else if (csvTimezoneId != null) {
				datasource.setTimeZone(csvTimezoneId);
			}
		}
		
		return datasource;
	}

	public void close() {
		if(isCsvStreamCreatedLocally && datasource != null){
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
