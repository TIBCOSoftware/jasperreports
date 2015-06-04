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

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * Query executer factory for XLSX file type.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JRXlsxQueryExecuter JRXlsxQueryExecuter}
 * query executers.
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JRXlsxQueryExecuterFactory extends AbstractXlsQueryExecuterFactory 
{

	/**
	 * Built-in parameter holding the value of the <code>org.apache.poi.ss.usermodel.Workbook</code> to be used for obtaining the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_WORKBOOK}.
	 */
	public static final String XLSX_WORKBOOK = "XLSX_WORKBOOK";	
	
	/**
	 * Built-in parameter holding the value of the <code>java.io.InputStream</code> to be used for obtaining the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_INPUT_STREAM}.
	 */
	public static final String XLSX_INPUT_STREAM = "XLSX_INPUT_STREAM";
	
	/**
	 * Built-in parameter holding the value of the <code>java.io.File</code> to be used for obtaining the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_FILE}.
	 */
	public static final String XLSX_FILE = "XLSX_FILE";
	
	/**
	 * Built-in parameter holding the value of the <code>java.lang.String</code> source to be used for obtaining the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_SOURCE}.
	 */
	public static final String XLSX_SOURCE = JRPropertiesUtil.PROPERTY_PREFIX + "xlsx.source";
	
	/**
	 * Built-in parameter/property holding the value of the columns to be extracted from the XLSX source.
	 * When used as report parameter, the value has to be a <code>java.lang.String</code> object containing column names separated by commas.
	 * It can also be used as the prefix for custom dataset properties specifying the names of the XLSX columns in the format:
	 * <code>net.sf.jasperreports.xlsx.column.names.{arbitrary_name}=value1[, value2, ...]</code>
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_COLUMN_NAMES}.
	 */
	public static final String XLSX_COLUMN_NAMES = JRPropertiesUtil.PROPERTY_PREFIX + "xlsx.column.names";

	/**
	 * Built-in parameter/property holding the value of the column indexs to be extracted from the XLSX source.
	 * When used as report parameter, the value has to be a <code>java.lang.String</code> object containing column indexes separated by comma.
	 * It can also be used as the prefix for custom dataset properties specifying the names of the XLSX column indexes in the format:
	 * <code>net.sf.jasperreports.xls.column.indexes.{arbitrary_name}=value1[, value2, ...]</code>
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_COLUMN_INDEXES}.
	 */
	public static final String XLSX_COLUMN_INDEXES = JRPropertiesUtil.PROPERTY_PREFIX + "xlsx.column.indexes";

	/**
	 * Built-in parameter holding the value of the columns to be extracted from the XLSX source, as a <code>java.lang.String[]</code> object. 
	 * <p/>
	 * When this parameter is null or missing, its value defaults to the values provided 
	 * by properties prefixed with {@link #XLSX_COLUMN_NAMES XLSX_COLUMN_NAMES}.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_COLUMN_NAMES_ARRAY}.
	 */
	public static final String XLSX_COLUMN_NAMES_ARRAY = "XLSX_COLUMN_NAMES_ARRAY";

	/**
	 * Built-in parameter holding the value of the column indexes to be extracted from the XLSX source, as a <code>java.lang.Integer[]</code> object. 
	 * <p/>
	 * When this parameter is null or missing, its value defaults to the values provided 
	 * by properties prefixed with {@link #XLSX_COLUMN_INDEXES XLSX_COLUMN_INDEXES}.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_COLUMN_INDEXES_ARRAY}.
	 */
	public static final String XLSX_COLUMN_INDEXES_ARRAY = "XLSX_COLUMN_INDEXES_ARRAY";
	
	/**
	 * Built-in parameter holding the <code>java.util.Locale</code> value of the locale to be used when parsing the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_LOCALE}.
	 */
	public static final String XLSX_LOCALE = "XLSX_LOCALE";
	
	/**
	 * Built-in parameter/property holding the <code>java.lang.String</code> code of the locale to be used when parsing the XLSX data.
	 * <p/>
	 * The allowed format is: language[_country[_variant]] 
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_LOCALE_CODE}.
	 */
	public static final String XLSX_LOCALE_CODE = JRPropertiesUtil.PROPERTY_PREFIX + "xlsx.locale.code";
	
	/**
	 * Built-in parameter holding the <code>java.util.TimeZone</code> value of the timezone to be used when parsing the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_TIMEZONE}.
	 */
	public static final String XLSX_TIMEZONE = "XLSX_TIMEZONE";
	
	/**
	 * Built-in parameter/property holding the <code>java.lang.String</code> value of the time zone id to be used when parsing the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_TIMEZONE_ID}.
	 */
	public static final String XLSX_TIMEZONE_ID = JRPropertiesUtil.PROPERTY_PREFIX + "xlsx.timezone.id";
	
	/**
	 * Built-in parameter holding the value of the <code>java.text.DateFormat</code> used to format date columns from the XLSX source.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_DATE_FORMAT}.
	 */
	public static final String XLSX_DATE_FORMAT = "XLSX_DATE_FORMAT";
	
	/**
	 * Built-in parameter/property holding the value of the date format pattern to be used when parsing the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_DATE_PATTERN}.
	 */
	public static final String XLSX_DATE_PATTERN = JRPropertiesUtil.PROPERTY_PREFIX + "xlsx.date.pattern";

	/**
	 * Built-in parameter holding the value of the <code>java.text.NumberFormat</code> used to format numeric columns from the XLSX source.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_NUMBER_FORMAT}.
	 */
	public static final String XLSX_NUMBER_FORMAT = "XLSX_NUMBER_FORMAT";
	
	/**
	 * Built-in parameter/property holding the value of the number format pattern to be used when parsing the XLSX data.
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_NUMBER_PATTERN}.
	 */
	public static final String XLSX_NUMBER_PATTERN = JRPropertiesUtil.PROPERTY_PREFIX + "xlsx.number.pattern";
	
	/**
	 * Built-in parameter/property specifying whether or not the column names should be obtained 
	 * from the first row in the XLSX source.
	 * As parameter, it should hold a <code>java.lang.Boolean</code> value, while as custom dataset property, it should be true or false. 
	 * 
	 * If this parameter is set to true, then setting the {@link #XLSX_COLUMN_NAMES} or {@link #XLSX_COLUMN_NAMES_ARRAY}
	 * would have no effect. 
	 * @deprecated Replaced by {@link AbstractXlsQueryExecuterFactory#XLS_USE_FIRST_ROW_AS_HEADER}.
	 */
	public static final String XLSX_USE_FIRST_ROW_AS_HEADER = "XLSX_USE_FIRST_ROW_AS_HEADER";

	private final static Object[] XLSX_BUILTIN_PARAMETERS = {
			XLSX_WORKBOOK, "org.apache.poi.ss.usermodel.Workbook",
			XLS_WORKBOOK, "org.apache.poi.ss.usermodel.Workbook",
			XLSX_INPUT_STREAM, "java.io.InputStream",
			XLS_INPUT_STREAM, "java.io.InputStream",
			XLSX_FILE, "java.io.File",
			XLS_FILE, "java.io.File",
			XLSX_SOURCE, "java.lang.String",
			XLS_SOURCE, "java.lang.String",
			XLSX_COLUMN_NAMES, "java.lang.String",
			XLS_COLUMN_NAMES, "java.lang.String",
			XLSX_COLUMN_INDEXES, "java.lang.String",
			XLS_COLUMN_INDEXES, "java.lang.String",
			XLSX_COLUMN_NAMES_ARRAY, "java.lang.String[]",
			XLS_COLUMN_NAMES_ARRAY, "java.lang.String[]",
			XLSX_COLUMN_INDEXES_ARRAY, "java.lang.Integer[]",
			XLS_COLUMN_INDEXES_ARRAY, "java.lang.Integer[]",
			XLSX_DATE_FORMAT, "java.text.DateFormat",
			XLS_DATE_FORMAT, "java.text.DateFormat",
			XLSX_DATE_PATTERN, "java.lang.String",
			XLS_DATE_PATTERN, "java.lang.String",
			XLSX_NUMBER_FORMAT, "java.text.NumberFormat",
			XLS_NUMBER_FORMAT, "java.text.NumberFormat",
			XLSX_NUMBER_PATTERN, "java.lang.String",
			XLS_NUMBER_PATTERN, "java.lang.String",
			XLSX_USE_FIRST_ROW_AS_HEADER, "java.lang.Boolean",
			XLS_USE_FIRST_ROW_AS_HEADER, "java.lang.Boolean",
			XLSX_LOCALE, "java.util.Locale",
			XLS_LOCALE, "java.util.Locale",
			XLSX_LOCALE_CODE, "java.lang.String",
			XLS_LOCALE_CODE, "java.lang.String",
			XLSX_TIMEZONE, "java.util.TimeZone",
			XLS_TIMEZONE, "java.util.TimeZone",
			XLSX_TIMEZONE_ID, "java.lang.String",
			XLS_TIMEZONE_ID, "java.lang.String",
			XLS_SHEET_SELECTION, "java.lang.String"
			};
	
	public Object[] getBuiltinParameters() {
		return XLSX_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(
		JasperReportsContext jasperReportsContext,
		JRDataset dataset, 
		Map<String,? extends JRValueParameter> parameters
		) throws JRException 
	{
		return new JRXlsxQueryExecuter(jasperReportsContext, dataset, parameters);
	}

	public boolean supportsQueryParameterType(String className) {
		return true;
	}
}
