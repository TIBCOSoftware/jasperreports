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

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

/**
 * XPath query executer implementation.
 * <p/>
 * The XPath query of the report is executed against the document specified by the
 * {@link net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory#PARAMETER_XML_DATA_DOCUMENT PARAMETER_XML_DATA_DOCUMENT}
 * parameter.
 * <p/>
 * All the parameters in the XPath query are replaced by calling <code>String.valueOf(Object)</code>
 * on the parameter value.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXPathQueryExecuter extends JRAbstractQueryExecuter
{
	private static final Log log = LogFactory.getLog(JRXPathQueryExecuter.class);
	
	private final Document document;
	
	public JRXPathQueryExecuter(JRDataset dataset, Map<String,? extends JRValueParameter> parametersMap)
	{
		super(dataset, parametersMap);
				
		document = (Document) getParameterValue(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT);

		if (document == null)
		{
			log.warn("The supplied org.w3c.dom.Document object is null.");
		}

		parseQuery();
	}

	protected String getParameterReplacement(String parameterName)
	{
		return String.valueOf(getParameterValue(parameterName));
	}

	public JRDataSource createDatasource() throws JRException
	{
		JRXmlDataSource datasource = null;
		
		String xPath = getQueryString();
		
		if (log.isDebugEnabled())
		{
			log.debug("XPath query: " + xPath);
		}
		
		if (document != null && xPath != null)
		{
			datasource = new JRXmlDataSource(document, xPath);
			datasource.setLocale((Locale)getParameterValue(JRXPathQueryExecuterFactory.XML_LOCALE, true));
			datasource.setDatePattern((String)getParameterValue(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, true));
			datasource.setNumberPattern((String)getParameterValue(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, true));
			datasource.setTimeZone((TimeZone)getParameterValue(JRXPathQueryExecuterFactory.XML_TIME_ZONE, true));
		}
		
		return datasource;
	}

	public void close()
	{
		//nothing to do
	}

	public boolean cancelQuery() throws JRException
	{
		//nothing to cancel
		return false;
	}
	
}
