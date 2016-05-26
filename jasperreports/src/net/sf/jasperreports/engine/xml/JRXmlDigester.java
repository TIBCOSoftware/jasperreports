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

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net 
 */
package net.sf.jasperreports.engine.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRXmlDigester extends Digester
{
	@SuppressWarnings("hiding")
	private static final Log log = LogFactory.getLog(JRXmlDigester.class);

	public static final String EXCEPTION_MESSAGE_KEY_ENTITY_LOADING_ERROR = "xml.digester.entity.loading.error";

	public static final String EXCEPTION_MESSAGE_UNKOWN_ENTITY_NOT_LOADING = "xml.digester.unknown.entity.not.loading";

	/**
	 * Property that determines whether loading entities that are not known to the engine is allowed.
	 * 
	 * <p>
	 * By default the property is set to <code>false</code>.
	 */
	public static final String PROPERTY_LOAD_UNKNOWN_ENTITIES = 
			JRPropertiesUtil.PROPERTY_PREFIX + "xml.load.unknown.entities";
	
	/**
	 *
	 */
	//private static boolean wasWarning;

	private Map<String, URL> internalEntityResources;
	private Set<String> entityURLs;
	private boolean loadUnknownEntities;

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
		internalEntityResources = new HashMap<String, URL>();
		entityURLs = new HashSet<String>();
		
		//FIXME only add entities relevant to the current document type (report, print, template)
		addEntityResource(JRXmlConstants.JASPERREPORT_SYSTEM_ID, 
				JRXmlConstants.JASPERREPORT_DTD);
		addEntityResource(JRXmlConstants.JASPERPRINT_SYSTEM_ID, 
				JRXmlConstants.JASPERPRINT_DTD);
		addEntityResource(JRXmlConstants.JASPERTEMPLATE_SYSTEM_ID, 
				JRXmlConstants.JASPERTEMPLATE_DTD);
		addEntityResource(JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID, 
				JRXmlConstants.JASPERREPORT_XSD_RESOURCE);
		addEntityResource(null, 
				JRXmlConstants.JASPERREPORT_XSD_DTD_COMPAT_RESOURCE);
		addEntityResource(JRXmlConstants.JASPERPRINT_XSD_SYSTEM_ID, 
				JRXmlConstants.JASPERPRINT_XSD_RESOURCE);
		addEntityResource(null, 
				JRXmlConstants.JASPERPRINT_XSD_DTD_COMPAT_RESOURCE);
		addEntityResource(JRXmlConstants.JASPERTEMPLATE_XSD_SYSTEM_ID, 
				JRXmlConstants.JASPERTEMPLATE_XSD_RESOURCE);
		addEntityResource(null, 
				JRXmlConstants.JASPERTEMPLATE_XSD_DTD_COMPAT_RESOURCE);
		
		loadUnknownEntities = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).getBooleanProperty(
				PROPERTY_LOAD_UNKNOWN_ENTITIES, false);
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
		if (resource == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("adding entity URL " + systemId);
			}
			
			entityURLs.add(systemId);
		}
		else
		{
			addEntityResource(systemId, resource);
		}
	}
	
	private void addEntityResource(String systemId, String resource)
	{
		URL resourceURL = JRLoader.getResource(resource);
		if (resourceURL == null)
		{
			log.warn("Could not find entity resource " + resource);
			return;
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("Entity " + systemId + " resolved to " + resourceURL);
		}
		
		if (systemId != null)
		{
			internalEntityResources.put(systemId, resourceURL);
		}
		entityURLs.add(resourceURL.toExternalForm());
	}


	@Override
	public InputSource resolveEntity(
		String pubId,
		String systemId
		)
	{
		InputSource inputSource = null;

		if (systemId != null)
		{
			URL resourceURL = internalEntityResources.get(systemId);
			
			if (resourceURL == null)
			{
				if (entityURLs.contains(systemId) || loadUnknownEntities)
				{
					if (log.isDebugEnabled())
					{
						log.debug("loading entity " + systemId);
					}
					
					//FIXME load from resource URLs?
					inputSource = new InputSource(systemId);
				}
				else
				{
					throw new JRRuntimeException(EXCEPTION_MESSAGE_UNKOWN_ENTITY_NOT_LOADING, 
							new Object[]{systemId});
				}
			}
			else
			{
				try
				{
					// load the data into the memory
					byte[] resourceData = JRLoader.loadBytes(resourceURL);
					InputStream memoryStream = new ByteArrayInputStream(resourceData);
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


	@Override
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
