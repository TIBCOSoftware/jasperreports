/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.query;

import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRJavaUtilDateConverter;

import org.apache.commons.beanutils.locale.LocaleConvertUtilsBean;
import org.apache.commons.beanutils.locale.converters.BigDecimalLocaleConverter;
import org.apache.commons.beanutils.locale.converters.BigIntegerLocaleConverter;
import org.apache.commons.beanutils.locale.converters.ByteLocaleConverter;
import org.apache.commons.beanutils.locale.converters.DecimalLocaleConverter;
import org.apache.commons.beanutils.locale.converters.DoubleLocaleConverter;
import org.apache.commons.beanutils.locale.converters.FloatLocaleConverter;
import org.apache.commons.beanutils.locale.converters.IntegerLocaleConverter;
import org.apache.commons.beanutils.locale.converters.LongLocaleConverter;
import org.apache.commons.beanutils.locale.converters.ShortLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlDateLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlTimeLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlTimestampLocaleConverter;
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
 * All the paramters in the XPath query are replaced by calling <code>String.valueOf(Object)</code>
 * on the parameter value.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRXPathQueryExecuter extends JRAbstractQueryExecuter
{
	private static final Log log = LogFactory.getLog(JRXPathQueryExecuter.class);
	
	private final Document document;
	private final Locale xmlLocale;
	private final String xmlTimezone;
	private LocaleConvertUtilsBean convertBean;
	
	public JRXPathQueryExecuter(JRDataset dataset, Map parametersMap)
	{
		super(dataset, parametersMap);
				
		document = (Document) getParameterValue(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT);

		if (document == null)
		{
			log.warn("The supplied org.w3c.dom.Document object is null.");
		}
		convertBean = new LocaleConvertUtilsBean();
		Locale tmpLocale = (Locale) getParameterValue(JRXPathQueryExecuterFactory.XML_LOCALE);
		xmlLocale = tmpLocale == null ? Locale.getDefault() : tmpLocale;
		xmlTimezone = (String) getParameterValue(JRXPathQueryExecuterFactory.XML_TIME_ZONE);
		convertBean.setDefaultLocale(xmlLocale);
		convertBean.deregister();
		registerConverters();
		parseQuery();
	}

	protected String getParameterReplacement(String parameterName)
	{
		return String.valueOf(getParameterValue(parameterName));
	}

	public JRDataSource createDatasource() throws JRException
	{
		JRDataSource datasource = null;
		
		String xPath = getQueryString();
		
		if (log.isDebugEnabled())
		{
			log.debug("XPath query: " + xPath);
		}
		
		if (document != null && xPath != null)
		{
			datasource = new JRXmlDataSource(document, xPath);
			((JRXmlDataSource) datasource).setConvertBean(this.convertBean);
			((JRXmlDataSource) datasource).setXmlLocale(this.xmlLocale);
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
	
	/**
	 * This method adds some particularly localized Converters to the convertBean's mapConverters object 
	 *
	 */
	private void registerConverters()
	{
		Object numberPattern = getParameterValue(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN);
		Object datePattern = getParameterValue(JRXPathQueryExecuterFactory.XML_DATE_PATTERN);
		if(numberPattern != null)
		{
			convertBean.register(
					new DecimalLocaleConverter(xmlLocale, (String) numberPattern), 
					java.lang.Number.class,
					xmlLocale);
			convertBean.register(
					new BigDecimalLocaleConverter(xmlLocale, (String) numberPattern), 
					java.math.BigDecimal.class,
					xmlLocale);
			
			convertBean.register(
					new BigIntegerLocaleConverter(xmlLocale, (String) numberPattern), 
					java.math.BigInteger.class,
					xmlLocale);
			
			convertBean.register(
					new ByteLocaleConverter(xmlLocale, (String) numberPattern), 
					java.lang.Byte.class,
					xmlLocale);
			
			
			convertBean.register(
					new DoubleLocaleConverter(xmlLocale, (String) numberPattern), 
					java.lang.Double.class,
					xmlLocale);
			
			convertBean.register(
					new FloatLocaleConverter(xmlLocale, (String) numberPattern), 
					java.lang.Float.class,
					xmlLocale);
			
			convertBean.register(
					new IntegerLocaleConverter(xmlLocale, (String) numberPattern), 
					java.lang.Integer.class,
					xmlLocale);
			
			convertBean.register(
					new LongLocaleConverter(xmlLocale, (String) numberPattern), 
					java.lang.Long.class,
					xmlLocale);
			
			convertBean.register(
					new ShortLocaleConverter(xmlLocale, (String) numberPattern), 
					java.lang.Short.class,
					xmlLocale);
		}

		if(datePattern != null)
		{
			//used as DateLocaleConverter
			JRJavaUtilDateConverter jrUtilDateConverter = new JRJavaUtilDateConverter(xmlLocale, (String) datePattern);
			jrUtilDateConverter.setTimezone(xmlTimezone);
			convertBean.register(
					jrUtilDateConverter, 
					java.util.Date.class,
					xmlLocale);
			
			convertBean.register(
					new SqlDateLocaleConverter(xmlLocale, (String) datePattern), 
					java.sql.Date.class,
					xmlLocale);
			
			convertBean.register(
					new SqlTimeLocaleConverter(xmlLocale, (String) datePattern), 
					java.sql.Time.class,
					xmlLocale);
			
			convertBean.register(
					new SqlTimestampLocaleConverter(xmlLocale, (String) datePattern), 
					java.sql.Timestamp.class,
					xmlLocale);
		}
		else
		{
			// used as DateLocaleConverter
			// this converter is not automatically registered when instantiating a LocaleConvertUtilsBean
			JRJavaUtilDateConverter jrUtilDateConverter = new JRJavaUtilDateConverter(xmlLocale, "yyyy-MM-dd");
			jrUtilDateConverter.setTimezone(xmlTimezone);
			convertBean.register(
					jrUtilDateConverter, 
					java.util.Date.class,
					xmlLocale);
			
		}
		
		/*
		//temporarily not needed
		convertBean.register(
				new StringLocaleConverter(
						xmlLocale, 
						(String) getParameterValue(JRXPathQueryExecuterFactory.XML_STRING_PATTERN)
				), 
				java.lang.String.class,
				xmlLocale);
		*/
	}
}
