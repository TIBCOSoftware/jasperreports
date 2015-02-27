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
package net.sf.jasperreports.data.json;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.data.DataFileStream;
import net.sf.jasperreports.data.DataFileUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;

/**
 * @author Veaceslov Chicu (schicu@users.sourceforge.net)
 */
public class JsonDataAdapterService extends AbstractDataAdapterService 
{
	
	private DataFileStream dataStream;
	
	/**
	 * 
	 */
	public JsonDataAdapterService(JasperReportsContext jasperReportsContext, JsonDataAdapter jsonDataAdapter) 
	{
		super(jasperReportsContext, jsonDataAdapter);
	}

	/**
	 * @deprecated Replaced by {@link #JsonDataAdapterService(JasperReportsContext, JsonDataAdapter)}.
	 */
	public JsonDataAdapterService(JsonDataAdapter jsonDataAdapter) 
	{
		this(DefaultJasperReportsContext.getInstance(), jsonDataAdapter);
	}

	public JsonDataAdapter getJsonDataAdapter() {
		return (JsonDataAdapter) getDataAdapter();
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters)
			throws JRException {
		JsonDataAdapter jsonDataAdapter = getJsonDataAdapter();
		if (jsonDataAdapter != null) {
			dataStream = DataFileUtils.instance(getJasperReportsContext()).getDataStream(
					jsonDataAdapter.getDataFile(), parameters);
			
			if (jsonDataAdapter.isUseConnection()) {
				parameters.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, dataStream);

				Locale locale = jsonDataAdapter.getLocale();
				if (locale != null) {
					parameters.put(JsonQueryExecuterFactory.JSON_LOCALE,
							locale);
				}

				TimeZone timeZone = jsonDataAdapter.getTimeZone();
				if (timeZone != null) {
					parameters.put(
							JsonQueryExecuterFactory.JSON_TIME_ZONE,
							timeZone);
				}

				String datePattern = jsonDataAdapter.getDatePattern();
				if (datePattern != null && datePattern.trim().length() > 0) {
					parameters.put(
							JsonQueryExecuterFactory.JSON_DATE_PATTERN,
							datePattern);
				}

				String numberPattern = jsonDataAdapter.getNumberPattern();
				if (numberPattern != null
						&& numberPattern.trim().length() > 0) {
					parameters.put(
							JsonQueryExecuterFactory.JSON_NUMBER_PATTERN,
							numberPattern);
				}
			} else {
				JsonDataSource ds = 
					new JsonDataSource(
						dataStream,
						jsonDataAdapter.getSelectExpression()
						);

				Locale locale = jsonDataAdapter.getLocale();
				if (locale != null) {
					ds.setLocale(locale);
				}

				TimeZone timeZone = jsonDataAdapter.getTimeZone();
				if (timeZone != null) {
					ds.setTimeZone(timeZone);
				}

				String datePattern = jsonDataAdapter.getDatePattern();
				if (datePattern != null && datePattern.trim().length() > 0) {
					ds.setDatePattern(datePattern);
				}

				String numberPattern = jsonDataAdapter.getNumberPattern();
				if (numberPattern != null && numberPattern.trim().length() > 0) {
					ds.setNumberPattern(numberPattern);
				}

				parameters.put(JRParameter.REPORT_DATA_SOURCE, ds);
			}
		}
	}

	@Override
	public void dispose()
	{
		if (dataStream != null)
		{
			dataStream.dispose();
		}
		
		super.dispose();
	}
}
