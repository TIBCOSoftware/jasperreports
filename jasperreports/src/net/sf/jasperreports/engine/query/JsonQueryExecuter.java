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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JsonData;
import net.sf.jasperreports.engine.data.JsonDataCollection;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.data.JsonDataSourceProvider;
import net.sf.jasperreports.engine.data.TextDataSourceAttributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JSON query executer implementation.
 * 
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class JsonQueryExecuter extends JRAbstractQueryExecuter
{
	private static final Log log = LogFactory.getLog(JsonQueryExecuter.class);

	protected static final String CANONICAL_LANGUAGE = "JSON";
	
	private JsonData datasource;
	
	/**
	 * 
	 */
	public JsonQueryExecuter(
		JasperReportsContext jasperReportsContext,
		JRDataset dataset, 
		Map<String, ? extends JRValueParameter> parametersMap
		)
	{
		super(jasperReportsContext, dataset, parametersMap);
		parseQuery();
	}

	/**
	 * @deprecated Replaced by {@link #JsonQueryExecuter(JasperReportsContext, JRDataset, Map)}.
	 */
	public JsonQueryExecuter(JRDataset dataset, Map<String, ? extends JRValueParameter> parametersMap)
	{
		this(DefaultJasperReportsContext.getInstance(), dataset, parametersMap);
	}

	@Override
	protected String getCanonicalQueryLanguage()
	{
		return CANONICAL_LANGUAGE;
	}

	protected String getParameterReplacement(String parameterName)
	{
		return String.valueOf(getParameterValue(parameterName));
	}

	public JRDataSource createDatasource() throws JRException
	{
		TextDataSourceAttributes textAttributes = getTextAttributes();
		
		InputStream jsonInputStream = (InputStream) getParameterValue(JsonQueryExecuterFactory.JSON_INPUT_STREAM);
		if (jsonInputStream != null) {
			JsonDataSource jsonDatasource = new JsonDataSource(jsonInputStream, getQueryString());
			jsonDatasource.setTextAttributes(textAttributes);
			datasource = jsonDatasource;
		} else {
			String jsonSource = getStringParameterOrProperty(JsonQueryExecuterFactory.JSON_SOURCE);
			if (jsonSource != null) {
				JsonDataSource jsonDatasource = new JsonDataSource(getJasperReportsContext(), jsonSource, getQueryString());
				jsonDatasource.setTextAttributes(textAttributes);
				datasource = jsonDatasource;
			} else {
				List<String> jsonSources = (List<String>) getParameterValue(JsonQueryExecuterFactory.JSON_SOURCES, true);
				if (jsonSources != null) {
					List<JsonDataSourceProvider> jsonProviders = 
							new ArrayList<JsonDataSourceProvider>(jsonSources.size());
					for (String source : jsonSources) {
						JsonDataSourceProvider jsonProvider = new JsonDataSourceProvider(getJasperReportsContext(), 
								source, getQueryString(), textAttributes);
						jsonProviders.add(jsonProvider);
					}
					datasource = new JsonDataCollection<JsonDataSource>(jsonProviders);
				} else {
					if (log.isWarnEnabled()) {
						log.warn("No JSON source was provided.");
					}
				}
			}
		}
		
		return datasource;
	}

	protected TextDataSourceAttributes getTextAttributes()
	{
		TextDataSourceAttributes attributes = new TextDataSourceAttributes();
		String dateFormatPattern = getStringParameterOrProperty(JsonQueryExecuterFactory.JSON_DATE_PATTERN);
		if(dateFormatPattern != null){
			attributes.setDatePattern(dateFormatPattern);
		}
		
		String numberFormatPattern = getStringParameterOrProperty(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN);
		if(numberFormatPattern != null){
			attributes.setNumberPattern(numberFormatPattern);
		}
		
		Locale jsonLocale = (Locale) getParameterValue(JsonQueryExecuterFactory.JSON_LOCALE, true);
		if (jsonLocale != null) {
			attributes.setLocale(jsonLocale);
		} else {
			String jsonLocaleCode = getStringParameterOrProperty(JsonQueryExecuterFactory.JSON_LOCALE_CODE);
			if (jsonLocaleCode != null) {
				attributes.setLocale(jsonLocaleCode);
			}
		}
		
		TimeZone jsonTimezone = (TimeZone) getParameterValue(JsonQueryExecuterFactory.JSON_TIME_ZONE, true);
		if (jsonTimezone != null) {
			attributes.setTimeZone(jsonTimezone);
		} else {
			String jsonTimezoneId = getStringParameterOrProperty(JsonQueryExecuterFactory.JSON_TIMEZONE_ID);
			if (jsonTimezoneId != null) {
				attributes.setTimeZone(jsonTimezoneId);
			}
		}
		
		return attributes;
	}

	public void close()
	{
		//NOP
	}

	public boolean cancelQuery() throws JRException
	{
		//nothing to cancel
		return false;
	}
	
}
