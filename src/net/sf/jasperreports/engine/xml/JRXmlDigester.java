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

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXmlDigester extends Digester
{


	/**
	 *
	 */
	//private static boolean wasWarning = false;

	private Map internalEntityResources;

	private String lastNamespacePrefix;
	
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
		internalEntityResources = new HashMap();
		
		internalEntityResources.put(JRXmlConstants.JASPERREPORT_SYSTEM_ID, 
				JRXmlConstants.JASPERREPORT_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERPRINT_SYSTEM_ID, 
				JRXmlConstants.JASPERPRINT_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERTEMPLATE_SYSTEM_ID, 
				JRXmlConstants.JASPERTEMPLATE_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID, 
				JRXmlConstants.JASPERREPORT_XSD_RESOURCE);
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
			String resource = (String) internalEntityResources.get(systemId);
			
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
				inputSource = new InputSource(is);
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
}
