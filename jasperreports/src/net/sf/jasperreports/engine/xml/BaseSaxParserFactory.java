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
package net.sf.jasperreports.engine.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.ClassUtils;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * Base SAX parser factory.
 * 
 * <p>
 * This factory creates a parser via the default SAX parser factory
 * (<code>javax.xml.parsers.SAXParserFactory.newInstance()</code>).
 * 
 * <p>
 * To improve performance, XML schemas can be cached when using a Xerces
 * SAX parser.  See {@link #PROPERTY_CACHE_SCHEMAS}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class BaseSaxParserFactory implements JRSaxParserFactory
{
	
	private static final Log log = LogFactory.getLog(BaseSaxParserFactory.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_INCOMPATIBLE_CLASS = "xml.sax.parser.factory.incompatible.class";
	public static final String EXCEPTION_MESSAGE_KEY_PARSER_CREATION_ERROR = "xml.sax.parser.factory.parser.creation.error";
	public static final String EXCEPTION_MESSAGE_KEY_RESOURCE_NOT_FOUND = "xml.sax.parser.factory.resource.not.found";
	
	/**
	 * A property that determines whether XML schemas/grammars are to be cached
	 * so that they are not read/initialized each time a report is compiled.
	 * 
	 * <p>
	 * Currently, setting this property is only effective when a Xerces XML
	 * parser is used (either a stock one from Apache or one embedded into a
	 * SUN JDK).
	 */
	// the property was initially created for JRXMLs, but now it's used for XML exports as well.
	// if required at some point, we can create a separate property. 
	public static final String PROPERTY_CACHE_SCHEMAS = JRPropertiesUtil.PROPERTY_PREFIX
		+ "compiler.xml.parser.cache.schemas";

	protected static final String PACKAGE_PREFIX_XERCES = "org.apache.xerces";
	protected static final String POOL_CLASS_XERCES = "org.apache.xerces.util.XMLGrammarPoolImpl";
	
	protected static final String PACKAGE_PREFIX_SUN_XERCES = "com.sun.org.apache.xerces";
	protected static final String POOL_CLASS_SUN_XERCES = "com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl";
	
	protected static final String XERCES_PARSER_PROPERTY_GRAMMAR_POOL = 
		"http://apache.org/xml/properties/internal/grammar-pool";
	
	private final static Object GRAMMAR_POOL_CACHE_NULL_KEY = "Null context classloader";
	private final static ThreadLocal<ReferenceMap> GRAMMAR_POOL_CACHE = new ThreadLocal<ReferenceMap>();
	
	protected final JasperReportsContext jasperReportsContext;
	
	/**
	 * @deprecated Replaced by {@link #BaseSaxParserFactory(JasperReportsContext)}.
	 */
	public BaseSaxParserFactory()
	{
		this(DefaultJasperReportsContext.getInstance());
	}
	
	public BaseSaxParserFactory(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
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
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_PARSER_CREATION_ERROR,
					(Object[])null,
					e);
		}
		catch (ParserConfigurationException e)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_PARSER_CREATION_ERROR,
					(Object[])null,
					e);
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

		boolean validating = isValidating();
		parserFactory.setValidating(validating);
		parserFactory.setFeature("http://xml.org/sax/features/validation", validating);
		return parserFactory;
	}

	protected abstract boolean isValidating();

	protected void configureParser(SAXParser parser)
			throws SAXException
	{
		List<String> schemaLocations = getSchemaLocations();
		parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
			"http://www.w3.org/2001/XMLSchema");
		parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
			schemaLocations.toArray(new String[schemaLocations.size()]));
		
		boolean cache = JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(PROPERTY_CACHE_SCHEMAS);
		if (cache)
		{
			enableSchemaCaching(parser);
		}
	}

	protected abstract List<String> getSchemaLocations();

	protected String getResourceURI(String resource)
	{
		URL location = JRLoader.getResource(resource);
		if (location == null)
		{
			throw 
				new JRRuntimeException(
					EXCEPTION_MESSAGE_KEY_RESOURCE_NOT_FOUND,
					new Object[]{resource});
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
			Object cacheKey = getGrammarPoolCacheKey();
			
			// we're using thread local caches to avoid thread safety problems
			ReferenceMap cacheMap = GRAMMAR_POOL_CACHE.get();
			if (cacheMap == null)
			{
				cacheMap = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.SOFT);
				GRAMMAR_POOL_CACHE.set(cacheMap);
			}
			
			Object grammarPool = cacheMap.get(cacheKey);
			if (grammarPool == null)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Instantiating grammar pool of type " + poolClassName
							+ " for cache key " + cacheKey);
				}

				grammarPool = ClassUtils.instantiateClass(poolClassName, Object.class);
				cacheMap.put(cacheKey, grammarPool);
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

	protected Object getGrammarPoolCacheKey()
	{
		Object key = Thread.currentThread().getContextClassLoader();
		if (key == null)
		{
			key = GRAMMAR_POOL_CACHE_NULL_KEY;
		}
		return key;
	}

	/**
	 * 
	 */
	public static JRSaxParserFactory getFactory(JasperReportsContext jasperReportsContext, String className)
	{
		JRSaxParserFactory factory = null;
		try
		{
			@SuppressWarnings("unchecked")
			Class<? extends JRSaxParserFactory> clazz = (Class<? extends JRSaxParserFactory>) JRClassLoader.loadClassForName(className);
			if (!JRSaxParserFactory.class.isAssignableFrom(clazz))
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_INCOMPATIBLE_CLASS,
						new Object[]{className, JRSaxParserFactory.class.getName()});
			}

			try
			{
				Constructor<? extends JRSaxParserFactory> constr = clazz.getConstructor(new Class[]{JasperReportsContext.class});
				factory = constr.newInstance(jasperReportsContext);
			}
			catch (NoSuchMethodException e)
			{
				//ignore
			}
			catch (InvocationTargetException e)
			{
				//ignore
			}
			
			if (factory == null)
			{
				factory = clazz.newInstance();
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (InstantiationException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new JRRuntimeException(e);
		}

		return factory;
	}
}
