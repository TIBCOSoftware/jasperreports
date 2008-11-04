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
package net.sf.jasperreports.engine.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.ComponentsEnvironment;
import net.sf.jasperreports.engine.component.ComponentsXmlParser;
import net.sf.jasperreports.engine.util.ClassUtils;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * The default report SAX parser factory.
 * 
 * <p>
 * This factory creates a parser via the default SAX parser factory
 * (<code>javax.xml.parsers.SAXParserFactory.newInstance()</code>).
 * 
 * <p>
 * JRXMLs are always validated using W3C XML schemas.  Reports that refer
 * the JasperReports DTD (which has been deprecated) are validated using an
 * internal XML schema equivalent to the DTD.
 * 
 * <p>
 * To improve performance, XML schemas can be cached when using a Xerces
 * SAX parser.  See {@link #PROPERTY_CACHE_SCHEMAS}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRCrosstab.java 1741 2007-06-08 10:53:33Z lucianc $
 */
public class JRReportSaxParserFactory implements JRSaxParserFactory
{
	
	private static final Log log = LogFactory.getLog(JRXmlDigesterFactory.class);
	
	/**
	 * A property that determines whether XML schemas/grammars are to be cached
	 * so that they are not read/initialized each time a report is compiled.
	 * 
	 * <p>
	 * Currently, setting this property is only effective when a Xerces XML
	 * parser is used (either a stock one from Apache or one embedded into a
	 * SUN JDK).
	 */
	public static final String PROPERTY_CACHE_SCHEMAS = JRProperties.PROPERTY_PREFIX
		+ "compiler.xml.parser.cache.schemas";

	protected static final String PACKAGE_PREFIX_XERCES = "org.apache.xerces";
	protected static final String POOL_CLASS_XERCES = "org.apache.xerces.util.XMLGrammarPoolImpl";
	
	protected static final String PACKAGE_PREFIX_SUN_XERCES = "com.sun.org.apache.xerces";
	protected static final String POOL_CLASS_SUN_XERCES = "com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl";
	
	protected static final String XERCES_PARSER_PROPERTY_GRAMMAR_POOL = 
		"http://apache.org/xml/properties/internal/grammar-pool";
	
	private final static Object GRAMMAR_POOL_CACHE_NULL_KEY = new String("Null context classloader");
	private final static ReferenceMap GRAMMAR_POOL_CACHE = 
		new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.SOFT);
	
	public SAXParser createParser()
	{
		try
		{
			SAXParserFactory parserFactory = createSAXParserFactory();
			SAXParser parser = parserFactory.newSAXParser();
			configureParser(parser);
			return parser;
		}
		catch (SAXException e)
		{
			throw new JRRuntimeException("Error creating SAX parser", e);
		}
		catch (ParserConfigurationException e)
		{
			throw new JRRuntimeException("Error creating SAX parser", e);
		}
	}

	protected SAXParserFactory createSAXParserFactory()
			throws ParserConfigurationException, SAXException
	{
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		
		if (log.isDebugEnabled())
		{
			log.debug("Instantiated SAX parser factory of type " 
					+ parserFactory.getClass().getName());
		}
		
		parserFactory.setNamespaceAware(true);

		boolean validating = JRProperties.getBooleanProperty(JRProperties.COMPILER_XML_VALIDATION);
		parserFactory.setValidating(validating);
		parserFactory.setFeature("http://xml.org/sax/features/validation", validating);
		return parserFactory;
	}

	protected void configureParser(SAXParser parser)
			throws SAXException
	{
		List schemaLocations = getSchemaLocations();
		parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
			"http://www.w3.org/2001/XMLSchema");
		parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
			schemaLocations.toArray(new String[schemaLocations.size()]));
		
		boolean cache = JRProperties.getBooleanProperty(PROPERTY_CACHE_SCHEMAS);
		if (cache)
		{
			enableSchemaCaching(parser);
		}
	}

	protected List getSchemaLocations()
	{
		List schemas = new ArrayList();
		schemas.add(getResourceURI(JRXmlConstants.JASPERREPORT_XSD_RESOURCE));
		schemas.add(getResourceURI(JRXmlConstants.JASPERREPORT_XSD_DTD_COMPAT_RESOURCE));
		
		Collection components = ComponentsEnvironment.getComponentBundles();
		for (Iterator it = components.iterator(); it.hasNext();)
		{
			ComponentsBundle componentManager = (ComponentsBundle) it.next();
			ComponentsXmlParser xmlParser = componentManager.getXmlParser();
			
			String schemaURI;
			String schemaResource = xmlParser.getInternalSchemaResource();
			if (schemaResource != null)
			{
				schemaURI = getResourceURI(schemaResource);
			}
			else
			{
				schemaURI = xmlParser.getPublicSchemaLocation();
			}

			if (log.isDebugEnabled())
			{
				log.debug("Adding components schema at " + schemaURI);
			}
			
			schemas.add(schemaURI);
		}
		return schemas;
	}

	protected String getResourceURI(String resource)
	{
		URL location = JRLoader.getResource(resource);
		if (location == null)
		{
			throw new JRRuntimeException("Could not find resource " + resource);
		}
		return location.toExternalForm();
	}

	protected void enableSchemaCaching(SAXParser parser)
	{
		String parserType = parser.getClass().getName();
		if (parserType.startsWith(PACKAGE_PREFIX_XERCES))
		{
			setGrammarPoolProperty(parser, POOL_CLASS_XERCES);
		}
		else if (parserType.startsWith(PACKAGE_PREFIX_SUN_XERCES))
		{
			setGrammarPoolProperty(parser, POOL_CLASS_SUN_XERCES);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("Schema caching only works with Xerces parsers");
			}
		}
	}
	
	protected void setGrammarPoolProperty(SAXParser parser, String poolClassName)
	{
		try
		{
			Object cacheKey = getGrammerPoolCacheKey();
			Object grammarPool;
			synchronized (GRAMMAR_POOL_CACHE)
			{
				grammarPool = GRAMMAR_POOL_CACHE.get(cacheKey);
				if (grammarPool == null)
				{
					if (log.isDebugEnabled())
					{
						log.debug("Instantiating grammar pool of type " + poolClassName
								+ " for cache key " + cacheKey);
					}

					grammarPool = ClassUtils.instantiateClass(poolClassName, Object.class);
					GRAMMAR_POOL_CACHE.put(cacheKey, grammarPool);
				}
			}
			parser.setProperty(XERCES_PARSER_PROPERTY_GRAMMAR_POOL, grammarPool);
		}
		catch (Exception e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Error setting Xerces grammar pool of type " + poolClassName, e);
			}
		}
	}

	protected Object getGrammerPoolCacheKey()
	{
		Object key = Thread.currentThread().getContextClassLoader();
		if (key == null)
		{
			key = GRAMMAR_POOL_CACHE_NULL_KEY;
		}
		return key;
	}

}
