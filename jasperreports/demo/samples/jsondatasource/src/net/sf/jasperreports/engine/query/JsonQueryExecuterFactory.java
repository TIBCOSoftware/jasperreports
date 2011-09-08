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

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * JSON query executer factory.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JsonQueryExecuter JRJsonQueryExecuter}
 * query executers.
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class JsonQueryExecuterFactory implements JRQueryExecuterFactory
{
	/**
	 * Built-in parameter holding the value of the <code>java.io.InputStream</code> to be used for obtaining the JSON data.
	 */
	public static final String JSON_INPUT_STREAM = "JSON_INPUT_STREAM";
	
	/**
	 * Built-in parameter holding the value of the source for the JSON file. 
	 * <p/>
	 * It can be:
	 * <ul>
	 * 	<li>a resource on the classpath</li>
	 * 	<li>a file from the filesystem, with an absolute or relative path</li>
	 * 	<li>a url</li>
	 * </ul>
	 */
	public static final String JSON_SOURCE = JRProperties.PROPERTY_PREFIX + "json.source";
	
	/**
	 * Parameter holding the format pattern used to instantiate java.util.Date instances.
	 */
	public final static String JSON_DATE_PATTERN = JRProperties.PROPERTY_PREFIX + "json.date.pattern";
	
	/**
	 * Parameter holding the format pattern used to instantiate java.lang.Number instances.
	 */
	public final static String JSON_NUMBER_PATTERN = JRProperties.PROPERTY_PREFIX + "json.number.pattern";

	/**
	 * Parameter holding the value of the datasource Locale
	 */
	public final static String JSON_LOCALE = "JSON_LOCALE";
	
	/**
	 * Built-in parameter/property holding the <code>java.lang.String</code> code of the locale to be used when parsing the JSON data.
	 * <p/>
	 * The allowed format is: language[_country[_variant]] 
	 */
	public static final String JSON_LOCALE_CODE = JRProperties.PROPERTY_PREFIX + "json.locale.code";
	
	/**
	 * Parameter holding the value of the datasource Timezone
	 */
	public final static String JSON_TIME_ZONE = "JSON_TIME_ZONE";
	
	/**
	 * Built-in parameter/property holding the <code>java.lang.String</code> value of the time zone id to be used when parsing the JSON data.
	 */
	public static final String JSON_TIMEZONE_ID = JRProperties.PROPERTY_PREFIX + "json.timezone.id";
	
	private final static Object[] JSON_BUILTIN_PARAMETERS = {
		JSON_INPUT_STREAM, "java.io.InputStream",
		JSON_SOURCE, "java.lang.String",
		JSON_DATE_PATTERN, "java.lang.String",
		JSON_NUMBER_PATTERN, "java.lang.String",
		JSON_LOCALE, "java.util.Locale",
		JSON_LOCALE_CODE, "java.lang.String",
		JSON_TIME_ZONE, "java.util.TimeZone",
		JSON_TIMEZONE_ID, "java.lang.String"
		};

	public Object[] getBuiltinParameters()
	{
		return JSON_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map parameters)
			throws JRException
	{
		return new JsonQueryExecuter(dataset, parameters);
	}

	public boolean supportsQueryParameterType(String className)
	{
		return true;
	}
}
