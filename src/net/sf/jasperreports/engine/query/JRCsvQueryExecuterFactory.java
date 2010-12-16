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

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Query executer factory for Csv file type.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JRCsvQueryExecuter JRCsvQueryExecuter}
 * query executers.
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JRCsvQueryExecuterFactory implements JRQueryExecuterFactory {
	
	/**
	 * Built-in parameter holding the value of the source for the Csv file. 
	 * <p/>
	 * It can be:
	 * <ul>
	 * 	<li>a resource on the classpath</li>
	 * 	<li>a file from the filesystem, with an absolute or relative path</li>
	 * 	<li>a url</li>
	 * </ul>
	 */
	public static final String CSV_INPUTSTREAM_SOURCE = "CSV_INPUTSTREAM_SOURCE";
	
	/**
	 * Built-in parameter holding the value of the <code>java.io.InputStream</code> to be used for obtaining the Csv data.
	 */
	public static final String CSV_INPUTSTREAM = "CSV_INPUTSTREAM";
	
	/**
	 * Built-in parameter holding the value of the <code>java.net.URL</code> to be used for obtaining the Csv data.
	 */
	public static final String CSV_URL = "CSV_URL";

	/**
	 * Built-in parameter holding the value of the <code>java.io.File</code> to be used for obtaining the Csv data.
	 */
	public static final String CSV_FILE = "CSV_FILE";
	
	/**
	 * Built-in parameter holding the value of the <code>java.io.Reader</code> to be used for obtaining the Csv data.
	 */
	public static final String CSV_READER = "CSV_READER";
	
	/**
	 * Built-in parameter holding the value of the charset used to encode the Csv stream. 
	 * <p/>
	 * It is meaningful only in combination
	 * with {@link #CSV_INPUTSTREAM CSV_INPUTSTREAM} or {@link #CSV_FILE CSV_FILE}.
	 */
	public static final String CSV_CHARSET = "CSV_CHARSET";

	/**
	 * Built-in parameter holding the value of the columns to be extracted from the Csv source, as a <code>java.lang.String</code> object.
	 */
	public static final String CSV_COLUMN_NAMES = "CSV_COLUMN_NAMES";

	/**
	 * Built-in parameter holding the value of the columns to be extracted from the Csv source, as a <code>java.lang.String[]</code> object. 
	 * <p/>
	 * When this parameter is null or missing, its value defaults to the values provided 
	 * by properties prefixed with {@link #PROPERTY_CSV_COLUMN_NAMES_PREFIX PROPERTY_CSV_COLUMN_NAMES_PREFIX}.
	 */
	public static final String CSV_COLUMN_NAMES_ARRAY = "CSV_COLUMN_NAMES_ARRAY";
	
	/**
	 * Built-in parameter holding the <code>java.lang.String</code> value of the locale
	 * <p/>
	 * The allowed format is: language[_country[_variant]] 
	 */
	public static final String CSV_LOCALE_ID = "CSV_LOCALE_ID";
	
	/**
	 * Built-in parameter holding the <code>java.lang.String</code> value of the time zone id
	 */
	public static final String CSV_TIMEZONE_ID = "CSV_TIMEZONE_ID";
	
	/**
	 * Built-in parameter holding the value of the <code>java.text.DateFormat</code> used to format date columns from the Csv source.
	 */
	public static final String CSV_DATE_FORMAT = "CSV_DATE_FORMAT";
	
	/**
	 * Built-in parameter holding the value of the date format pattern.
	 */
	public static final String CSV_DATE_PATTERN = "CSV_DATE_PATTERN";
	
	/**
	 * Built-in parameter holding the value of the field delimiter from the Csv source.
	 */
	public static final String CSV_FIELD_DELIMITER = "CSV_FIELD_DELIMITER";
	
	/**
	 * Built-in parameter holding the value of the <code>java.text.NumberFormat</code> used to format numeric columns from the Csv source.
	 */
	public static final String CSV_NUMBER_FORMAT = "CSV_NUMBER_FORMAT";
	
	/**
	 * Built-in parameter holding the value of the number format pattern.
	 */
	public static final String CSV_NUMBER_PATTERN = "CSV_NUMBER_PATTERN";
	
	/**
	 * Build-in parameter holding the value of the record delimiter from the Csv source
	 */
	public static final String CSV_RECORD_DELIMITER = "CSV_RECORD_DELIMITER";
	
	/**
	 * Built-in parameter holding the <code>java.lang.Boolean</code> value specifying whether or not the column names should be obtained 
	 * from the first row in the Csv source.
	 * 
	 * If this parameter is set to <code>java.lang.Boolean.TRUE</code>, then setting the {@link #CSV_COLUMN_NAMES} or {@link #CSV_COLUMN_NAMES_ARRAY}
	 * would have no effect. 
	 */
	public static final String CSV_USE_FIRST_ROW_AS_HEADER = "CSV_USE_FIRST_ROW_AS_HEADER";

	private final static Object[] CSV_BUILTIN_PARAMETERS = {
			CSV_INPUTSTREAM_SOURCE, "java.lang.String",
			CSV_INPUTSTREAM, "java.io.InputStream",
			CSV_URL, "java.net.URL",
			CSV_FILE, "java.io.File",
			CSV_CHARSET, "java.lang.String",
			CSV_READER, "java.io.Reader",
			CSV_COLUMN_NAMES, "java.lang.String",
			CSV_COLUMN_NAMES_ARRAY, "java.lang.String[]",
			CSV_DATE_FORMAT, "java.text.DateFormat",
			CSV_DATE_PATTERN, "java.lang.String",
			CSV_FIELD_DELIMITER, "java.lang.Character",
			CSV_NUMBER_FORMAT, "java.text.NumberFormat",
			CSV_NUMBER_PATTERN, "java.lang.String",
			CSV_RECORD_DELIMITER, "java.lang.String",
			CSV_USE_FIRST_ROW_AS_HEADER, "java.lang.Boolean",
			CSV_LOCALE_ID, "java.lang.String",
			CSV_TIMEZONE_ID, "java.lang.String"
			};
	
	/**
	 * Prefix for properties specifying the names of the Csv columns in the format: <code>net.sf.jasperreports.csv.column.names.{arbitrary_name}=value1[, value2, ...]</code>
	 */
	public static final String PROPERTY_CSV_COLUMN_NAMES_PREFIX = JRProperties.PROPERTY_PREFIX + "csv.column.names";

	public Object[] getBuiltinParameters() {
		return CSV_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters)
			throws JRException {
		return new JRCsvQueryExecuter(dataset, parameters);
	}

	public boolean supportsQueryParameterType(String className) {
		return true;
	}

}
