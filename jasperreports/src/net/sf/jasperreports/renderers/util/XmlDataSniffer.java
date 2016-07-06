/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.renderers.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Utility class that checks if provided byte data is a valid XML document.
 * It does this by attempting to parse the data using a SAX parser.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class XmlDataSniffer
{
	private static final String SAX_EXCEPTION_MESSAGE_VALID_XML = "something unique";
	
	/**
	 * 
	 */
	public static boolean isXmlData(byte[] data)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		SaxHandler handler = new SaxHandler();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data);

		try 
		{
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(bais, handler);
			return true;
		}
		catch (SAXException e) 
		{
			if (SAX_EXCEPTION_MESSAGE_VALID_XML.equals(e.getMessage()))
			{
				return true;
			}
		}
		catch (ParserConfigurationException e) 
		{
		}
		catch (IOException e) 
		{
		}

		return false;
	}
	
	private static class SaxHandler extends DefaultHandler
	{
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			throw new SAXException(SAX_EXCEPTION_MESSAGE_VALID_XML);
		}
	}
}
