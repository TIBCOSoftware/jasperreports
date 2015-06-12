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

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRXmlDigester extends Digester
{

	public static final String EXCEPTION_MESSAGE_KEY_ENTITY_LOADING_ERROR = "xml.digester.entity.loading.error";

	/**
	 *
	 */
	//private static boolean wasWarning;

	private Map<String,String> internalEntityResources;

	private String lastNamespacePrefix;
	private Object lastPopped;
	
	/**
	 *
	 */
	public JRXmlDigester()
	{
		super();
		
		initInternalResources();
	}


	/**
	 *
	 */
	public JRXmlDigester(XMLReader xmlReader)
	{
		super(xmlReader);
		
		initInternalResources();
	}

	public JRXmlDigester(SAXParser parser)
	{
		super(parser);
		
		initInternalResources();
	}

	private void initInternalResources()
	{
		internalEntityResources = new HashMap<String,String>();
		
		internalEntityResources.put(JRXmlConstants.JASPERREPORT_SYSTEM_ID, 
				JRXmlConstants.JASPERREPORT_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERPRINT_SYSTEM_ID, 
				JRXmlConstants.JASPERPRINT_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERTEMPLATE_SYSTEM_ID, 
				JRXmlConstants.JASPERTEMPLATE_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID, 
				JRXmlConstants.JASPERREPORT_XSD_RESOURCE);
		internalEntityResources.put(JRXmlConstants.JASPERPRINT_XSD_SYSTEM_ID, 
				JRXmlConstants.JASPERPRINT_XSD_RESOURCE);
	}


	/**
	 * Adds a mapping of an entity system ID to an internal/classloader resource
	 * name.
	 * 
	 * <p>
	 * This mapping is used by {@link #resolveEntity(String, String)} to
	 * resolve a system ID to a classloader resource.
	 * 
	 * @param systemId the system ID
	 * @param resource the resource name
	 */
	public void addInternalEntityResource(String systemId, String resource)
	{
		internalEntityResources.put(systemId, resource);
	}


	/**
	 *
	 */
	public InputSource resolveEntity(
		String pubId,
		String systemId
		)
	{
		InputSource inputSource = null;

		if (systemId != null)
		{
			String resource = internalEntityResources.get(systemId);
			
			if (resource == null)
			{
				return new InputSource(systemId);
			}

			ClassLoader clsLoader = Thread.currentThread().getContextClassLoader();

			URL url = null;
			if (clsLoader != null)
			{
				url = clsLoader.getResource(resource);
			}
			if (url == null)
			{
				//if (!wasWarning)
				//{
				//	if (log.isWarnEnabled())
				//		log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRXmlDigester class. Using JRXmlDigester.class.getClassLoader() instead.");
				//	wasWarning = true;
				//}
				clsLoader = JRXmlDigester.class.getClassLoader();
			}
			
			InputStream is;
			if (clsLoader == null)
			{
				is = JRXmlDigester.class.getResourceAsStream("/" + resource);
			}
			else
			{
				is = clsLoader.getResourceAsStream(resource);
			}
			
			if (is != null)
			{
				try
				{
					// load the data into the memory so that we don't leave the stream open
					InputStream memoryStream = JRLoader.loadToMemoryInputStream(is);
					inputSource = new InputSource(memoryStream);
				}
				catch (JRException e)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_ENTITY_LOADING_ERROR,
							new Object[]{systemId},
							e);
				}
			}
		}

		return inputSource;
	}


	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException
	{
		lastNamespacePrefix = getNamespacePrefix(qName);
		
		super.endElement(namespaceURI, localName, qName);
	}

	protected String getNamespacePrefix(String qName)
	{
		String prefix;
		if (qName == null)
		{
			prefix = null;
		}
		else
		{
			int sepIdx = qName.indexOf(':');
			if (sepIdx > 0)
			{
				prefix = qName.substring(0, sepIdx);
			}
			else
			{
				prefix = null;
			}
		}
		return prefix;
	}
	
	public String getLastNamespacePrefix()
	{
		return lastNamespacePrefix;
	}


	@Override
	public Object pop()
	{
		// remember the last popped object
		lastPopped = super.pop();
		return lastPopped;
	}
	
	/**
	 * Clears the last popped object.
	 * 
	 * @see #lastPopped()
	 */
	public void clearLastPopped()
	{
		lastPopped = null;
	}
	
	/**
	 * Returns the previously popped object.
	 * 
	 * This method can be used by rules that need to know the object was added and 
	 * popped to the stack by an inner element.
	 * 
	 * @return the previously popped object
	 */
	public Object lastPopped()
	{
		return lastPopped;
	}
}
