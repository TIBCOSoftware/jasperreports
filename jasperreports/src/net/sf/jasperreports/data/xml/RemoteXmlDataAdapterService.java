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
package net.sf.jasperreports.data.xml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.w3c.dom.Document;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class RemoteXmlDataAdapterService extends XmlDataAdapterService
{
	public static final String XML_URL = "XML_URL";

	public RemoteXmlDataAdapterService(RemoteXmlDataAdapter remoteXmlDataAdapter) 
	{
		super(remoteXmlDataAdapter);
	}
	
	public RemoteXmlDataAdapter getRemoteXmlDataAdapter() 
	{
		return (RemoteXmlDataAdapter)getDataAdapter();
	}
	
	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		RemoteXmlDataAdapter remoteXmlDataAdapter = getRemoteXmlDataAdapter();
		if (remoteXmlDataAdapter != null)
		{
			if (remoteXmlDataAdapter.isUseConnection()) 
			{
				String fileName = remoteXmlDataAdapter.getFileName();
				if (fileName.toLowerCase().startsWith("https://") ||
					fileName.toLowerCase().startsWith("http://") ||
					fileName.toLowerCase().startsWith("file:")) {
					
					// JRXPathQueryExecuterFactory.XML_URL not available.
					// Once this is available, remove XML_URL from this class.
					parameters.put(XML_URL, fileName);
				}
				else 
				{
					Document document = JRXmlUtils.parse(new File(fileName));
					parameters.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
				}
				
				Locale locale = remoteXmlDataAdapter.getLocale();
				if (locale != null) {
					parameters.put(JRXPathQueryExecuterFactory.XML_LOCALE, locale);
				}

				TimeZone timeZone = remoteXmlDataAdapter.getTimeZone();
				if (timeZone != null) {
					parameters.put(JRXPathQueryExecuterFactory.XML_TIME_ZONE, timeZone);
				}
				
				String datePattern = remoteXmlDataAdapter.getDatePattern();
				if (datePattern != null && datePattern.trim().length()>0) {
					parameters.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, datePattern);
				}

				String numberPattern = remoteXmlDataAdapter.getNumberPattern();
				if (numberPattern != null && numberPattern.trim().length()>0) {
					parameters.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, numberPattern);
				}
			}
		}
	}

	@Override
	public void test() throws JRException 
	{
		RemoteXmlDataAdapter remoteXmlDataAdapter = (RemoteXmlDataAdapter)getDataAdapter();
		URL url = null;
		InputStream is = null;
		try {
			String fileName = remoteXmlDataAdapter.getFileName(); 
			url = new URL(fileName);
			if (fileName.startsWith("file://")) {
				is = url.openStream();
			}
		}
		catch (Exception e) {
			throw new JRException(e.getMessage());
		} finally {

			if (is != null) {
				try { is.close(); } catch (Exception ex){}
			}
		}
	}
}
