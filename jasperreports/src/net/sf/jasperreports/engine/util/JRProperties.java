/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
package net.sf.jasperreports.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;

/**
 * Class that provides static methods for loading, getting and setting properties.
 * <p>
 * The following actions are performed:
 * <ul>
 * <li>The default (hardcoded) properties are set.</li>
 * <li>If the system property "net.sf.jasperreports.properties" has been set 
 * then the specified proprties file is loaded.</li>
 * <li>Otherwise "jasperreports.properties" is loaded if found in the classpath.</li>
 * <li>For backward compatibility, system properties like "jasper.reports.compile.xml.validation"
 * are checked and their values are used.  This way of specifying properties is deprecated.</li> 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRProperties
{
	/**
	 * The default properties file.
	 */
	protected static final String DEFAULT_PROPERTIES_FILE = "jasperreports.properties";
	
	/**
	 * The prefix used by all properties.
	 */
	public static final String PROPERTY_PREFIX = "net.sf.jasperreports.";
	
	/**
	 * The name of the system property that specifies the properties file name.
	 */
	public static final String PROPERTIES_FILE = PROPERTY_PREFIX + "properties";
	
	/**
	 * The name of the class to be used for report compilation.
	 * <p>
	 * No default value.
	 */
	public static final String COMPILER_CLASS = PROPERTY_PREFIX + "compiler.class";
	
	/**
	 * Whether to validate the xml report when compiling.
	 * <p>
	 * Defaults to <code>true</code>.
	 */
	public static final String COMPILER_XML_VALIDATION = PROPERTY_PREFIX + "compiler.xml.validation";
	
	/**
	 * Whether to keep the java file generated when the report is compiled.
	 * <p>
	 * Defaults to <code>false</code>.
	 */
	public static final String COMPILER_KEEP_JAVA_FILE = PROPERTY_PREFIX + "compiler.keep.java.file";
	
	/**
	 * The temporary directory used by the report compiler. 
	 * <p>
	 * Defaults to <code>System.getProperty("user.dir")</code>.
	 */
	public static final String COMPILER_TEMP_DIR = PROPERTY_PREFIX + "compiler.temp.dir";
	
	/**
	 * The classpath used by the report compiler. 
	 * <p>
	 * Defaults to <code>System.getProperty("java.class.path")</code>.
	 */
	public static final String COMPILER_CLASSPATH = PROPERTY_PREFIX + "compiler.classpath";
	
	/**
	 * Validation flag used by the XML exporter.
	 * <p>
	 * Defaults to <code>true</code>.
	 */
	public static final String EXPORT_XML_VALIDATION = PROPERTY_PREFIX + "export.xml.validation";
	
	/**
	 * Prefix of properties that specify font files for the PDF exporter.
	 */
	public static final String PDF_FONT_FILES_PREFIX = PROPERTY_PREFIX + "export.pdf.font.";
	
	/**
	 * Prefix of properties that specify font directories for the PDF exporter.
	 */
	public static final String PDF_FONT_DIRS_PREFIX = PROPERTY_PREFIX + "export.pdf.fontdir.";
	
	/**
	 * Prefix for query executer factory properties.
	 * <p/>
	 * To obtain query executer factories, a property having the query language appended to this prefix is used 
	 * to get the query executer factory name.
	 */
	public static final String QUERY_EXECUTER_FACTORY_PREFIX = PROPERTY_PREFIX + "query.executer.factory.";
	
	protected static Properties props;
	
	protected static Properties savedProps;
	
	static
	{
		initProperties();
	}

	/**
	 * Loads the properties. 
	 */
	protected static void initProperties()
	{
		try
		{
			Properties defaults = getDefaults();
			String propFile = System.getProperty(PROPERTIES_FILE);
			if (propFile == null)
			{
				props = loadProperties(DEFAULT_PROPERTIES_FILE, defaults);
				if (props == null)
				{
					props = defaults;
				}
			}
			else
			{
				props = loadProperties(propFile, defaults);
				if (props == null)
				{
					throw new JRRuntimeException("Could not load properties file \"" + propFile + "\"");
				}
			}

			loadSystemProperties();
		}
		catch (JRException e)
		{
			throw new JRRuntimeException("Error loading the properties", e);
		}
	}
	
	protected static void loadSystemProperties()
	{
		loadSystemProperty("jasper.reports.compiler.class", COMPILER_CLASS);
		loadSystemProperty("jasper.reports.compile.xml.validation", COMPILER_XML_VALIDATION);
		loadSystemProperty("jasper.reports.export.xml.validation", EXPORT_XML_VALIDATION);
		loadSystemProperty("jasper.reports.compile.keep.java.file", COMPILER_KEEP_JAVA_FILE);
		loadSystemProperty("jasper.reports.compile.temp", COMPILER_TEMP_DIR);
		loadSystemProperty("jasper.reports.compile.class.path", COMPILER_CLASSPATH);	
	}
	
	/**
	 * Sets the default properties.
	 * 
	 * @return the default properties
	 */
	protected static Properties getDefaults ()
	{
		Properties defaults = new Properties();
		
		defaults.setProperty(COMPILER_XML_VALIDATION, String.valueOf(true));
		defaults.setProperty(COMPILER_KEEP_JAVA_FILE, String.valueOf(false));
		defaults.setProperty(EXPORT_XML_VALIDATION, String.valueOf(true));
		
		String userDir = System.getProperty("user.dir");
		if (userDir != null)
		{
			defaults.setProperty(COMPILER_TEMP_DIR, userDir);
		}
		
		String classPath = System.getProperty("java.class.path");
		if (classPath != null)
		{
			defaults.setProperty(COMPILER_CLASSPATH, classPath);
		}
		
		defaults.setProperty(QUERY_EXECUTER_FACTORY_PREFIX + JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL, JRJdbcQueryExecuterFactory.class.getName());
		defaults.setProperty(QUERY_EXECUTER_FACTORY_PREFIX + "hql", "net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory");

		return defaults;
	}

	protected static void loadSystemProperty(String sysKey, String propKey)
	{
		String val = System.getProperty(sysKey);
		if (val != null)
		{
			props.setProperty(propKey, val);
		}
	}

	/**
	 * Loads a properties file from the classpath.
	 * 
	 * @param name the resource name
	 * @param defaults the default properties
	 * @return the loaded properties if the resource is found, <code>null</code> otherwise
	 * @throws JRException 
	 */
	public static Properties loadProperties (String name, Properties defaults) throws JRException
	{
		InputStream is = JRLoader.getLocationInputStream(name);
		
		Properties properties = null;
		
		if (is != null)
		{
			properties = new Properties(defaults);
			try
			{
				properties.load(is);
			}
			catch (IOException e)
			{
				throw new JRException("Failed to load properties file \"" + name + "\"", e);
			}
			finally
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		
		return properties;
	}
	
	/**
	 * Returns the value of the property.
	 * 
	 * @param key the key
	 * @return the property value
	 */
	public static String getProperty (String key)
	{
		return props.getProperty(key);
	}
	
	/**
	 * Returns a property as a boolean value.
	 * 
	 * @param key the key
	 * @return the property value as a boolean
	 */
	public static boolean getBooleanProperty (String key)
	{
		return asBoolean(props.getProperty(key));
	}

	/**
	 * Converts a <code>String</code> value into a <code>boolean</code>.
	 * 
	 * @param value the value
	 * @return the value as a <code>boolean</code>
	 */
	public static boolean asBoolean(String value)
	{
		return Boolean.valueOf(value).booleanValue();
	}
	
	/**
	 * Sets the value of a property.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public static void setProperty (String key, String value)
	{
		props.setProperty(key, value);
	}
	
	/**
	 * Sets the value of a property.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public static void setProperty (String key, boolean value)
	{
		props.setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Saves a copy of the current properties.
	 * 
	 * @see #restoreProperties() 
	 */
	public static void backupProperties ()
	{
		savedProps = (Properties) props.clone();
	}
	
	/**
	 * Restores previously saved properties.
	 * 
	 * @see #backupProperties() 
	 */
	public static void restoreProperties ()
	{
		if (savedProps != null)
		{
			try
			{
				props.clear();
				props.putAll(savedProps);
			}
			finally
			{
				savedProps = null;
			}
		}
	}
	
	/**
	 * Class used by {@link JRProperties#getProperties(String)}.
	 * 
	 * @author Lucian Chirita
	 */
	public static class PropertySuffix
	{
		protected final String key;
		protected final String suffix;
		protected final String value;
		
		public PropertySuffix (String key, String suffix, String value)
		{
			this.key = key;
			this.suffix = suffix;
			this.value = value;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public String getSuffix ()
		{
			return suffix;
		}
		
		public String getValue ()
		{
			return value;
		}
	}
	
	/**
	 * Returns the list of all properties for a key prefix.
	 * 
	 * @param prefix the key prefix
	 * @return a list of {@link PropertySuffix PropertySuffix} objects containing the suffix of the key and the value
	 */
	public static List getProperties (String prefix)
	{
		int prefixLength = prefix.length();
		List values = new ArrayList();
		for (Enumeration names = props.propertyNames(); names.hasMoreElements();)
		{
			String name = (String) names.nextElement();
			if (name.startsWith(prefix))
			{
				String suffix = name.substring(prefixLength);
				String value = props.getProperty(name);
				values.add(new PropertySuffix(name, suffix, value));
			}
		}
		return values;
	}
}
