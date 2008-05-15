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
package net.sf.jasperreports.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRRuntimeException;

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
	 * 
	 * @deprecated Replaced by {@link net.sf.jasperreports.engine.design#COMPILER_PREFIX}.
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
	 * @deprecated Replaced by {@link net.sf.jasperreports.engine.query#QUERY_EXECUTER_FACTORY_PREFIX}.
	 */
	public static final String QUERY_EXECUTER_FACTORY_PREFIX = PROPERTY_PREFIX + "query.executer.factory.";
	
	/**
	 * @deprecated Replaced by {@link net.sf.jasperreports.engine.fill.JRSubreportRunnerFactory#SUBREPORT_RUNNER_FACTORY}.
	 */
	public static final String SUBREPORT_RUNNER_FACTORY = PROPERTY_PREFIX + "subreport.runner.factory";
	
	/**
	 * @deprecated Replaced by {@link net.sf.jasperreports.engine.export.JRPdfExporterParameter#PROPERTY_FORCE_LINEBREAK_POLICY}.
	 */
	public static final String PDF_FORCE_LINEBREAK_POLICY = PROPERTY_PREFIX + "export.pdf.force.linebreak.policy";
	
	/**
	 * Prefix of properties that specify the formula which has to be applied to a given cell in an excel sheet.
	 */
	public static final String CELL_FORMULA_PREFIX = PROPERTY_PREFIX + "export.xls.formula";
	
	
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
			String propFile = getSystemProperty(PROPERTIES_FILE);
			if (propFile == null)
			{
				props = loadProperties(DEFAULT_PROPERTIES_FILE, defaults);
				if (props == null)
				{
					props = new Properties(defaults);
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
	protected static Properties getDefaults() throws JRException
	{
		Properties defaults = new Properties();
		
		InputStream is = JRProperties.class.getResourceAsStream("/default.jasperreports.properties");
		
		if (is == null)
		{
			throw new JRException("Default properties file not found.");
		}

		try
		{
			defaults.load(is);
		}
		catch (IOException e)
		{
			throw new JRException("Failed to load default properties.", e);
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
		
		String userDir = getSystemProperty("user.dir");
		if (userDir != null)
		{
			defaults.setProperty(COMPILER_TEMP_DIR, userDir);
		}
		
		String classPath = getSystemProperty("java.class.path");
		if (classPath != null)
		{
			defaults.setProperty(COMPILER_CLASSPATH, classPath);
		}

		return defaults;
	}

	/**
	 * 
	 */
	protected static String getSystemProperty(String propertyName)
	{
		try
		{
			return System.getProperty(propertyName);
		}
		catch (SecurityException e)
		{
			// This could fail if we are in the applet viewer or some other 
			// restrictive environment, but it should be safe to simply return null.
			// We cannot log this properly using a logging API, 
			// as we want to keep applet JAR dependencies to a minimum.
			return null;
		}
	}

	protected static void loadSystemProperty(String sysKey, String propKey)
	{
		String val = getSystemProperty(sysKey);
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
		Properties properties = null;
		
		InputStream is = null;
		
		try
		{
			is = JRLoader.getLocationInputStream(name);
		}
		catch (SecurityException e)
		{
			// This could fail if we are in the applet viewer or some other 
			// restrictive environment, but most of the time it should be safe to ignore.
			// We cannot log this properly using a logging API, 
			// as we want to keep applet JAR dependencies to a minimum.
		}
		
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
	 * Returns a property as an integer value.
	 * 
	 * @param key the key
	 * @return the property value as an integer
	 */
	public static int getIntegerProperty (String key)
	{
		return asInteger(props.getProperty(key));
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
	 * Converts a <code>String</code> value into a <code>int</code>.
	 * 
	 * @param value the value
	 * @return the value as a <code>int</code>
	 */
	public static int asInteger(String value)
	{
		return Integer.parseInt(value);
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

	/**
	 * Returns the list of all properties for a key prefix.
	 * Only this holder's own properties are considered.
	 * 
	 * @param propertiesHolder the properties holder
	 * @param prefix the key prefix
	 * @return a list of {@link PropertySuffix PropertySuffix} objects containing the suffix of the key and the value
	 */
	public static List getProperties(JRPropertiesHolder propertiesHolder, String prefix)
	{
		return getProperties(getOwnProperties(propertiesHolder), prefix);
	}
	
	/**
	 * Returns the list of all properties for a key prefix.
	 * 
	 * @param propertiesMap the properties map
	 * @param prefix the key prefix
	 * @return a list of {@link PropertySuffix PropertySuffix} objects containing the suffix of the key and the value
	 */
	public static List getProperties(JRPropertiesMap propertiesMap, String prefix)
	{
		int prefixLength = prefix.length();
		List values = new ArrayList();
		if (propertiesMap != null)
		{
			String[] propertyNames = propertiesMap.getPropertyNames();
			for (int i = 0; i < propertyNames.length; i++)
			{
				String name = propertyNames[i];
				if (name.startsWith(prefix))
				{
					String suffix = name.substring(prefixLength);
					String value = propertiesMap.getProperty(name);
					values.add(new PropertySuffix(name, suffix, value));
				}
			}
		}
		return values;
	}

	/**
	 * Returns the value of a property, looking first in the supplied properties holder
	 * and then in the system properties.
	 * 
	 * @param propertiesHolder the properties holder
	 * @param key the key
	 * @return the property value
	 */
	public static String getProperty (JRPropertiesHolder propertiesHolder, String key)
	{
		String value = null;
		while (propertiesHolder != null && value == null)
		{
			if (propertiesHolder.hasProperties())
			{
				value = propertiesHolder.getPropertiesMap().getProperty(key);
			}
			propertiesHolder = propertiesHolder.getParentProperties();
		}
		
		if (value == null)
		{
			value = props.getProperty(key);
		}
		
		return value;
	}
	
	/**
	 * Returns the value of a property, looking first in the supplied properties map
	 * and then in the system properties.
	 * 
	 * @param propertiesMap the properties map
	 * @param key the key
	 * @return the property value
	 */
	public static String getProperty (JRPropertiesMap propertiesMap, String key)
	{
		String value = null;
		if (propertiesMap != null)
		{
			value = propertiesMap.getProperty(key);
		}
		
		if (value == null)
		{
			value = props.getProperty(key);
		}
		
		return value;
	}

	/**
	 * Returns the value of a property as a boolean, looking first in the supplied properties holder
	 * and then in the system properties.
	 * 
	 * @param propertiesHolder the properties holder
	 * @param key the key
	 * @param defaultValue the default value used if the property is not found
	 * @return the property value
	 */
	public static boolean getBooleanProperty (JRPropertiesHolder propertiesHolder, String key, boolean defaultValue)
	{
		String value = getProperty(propertiesHolder, key);
		
		return value == null ? defaultValue : asBoolean(value);
	}

	/**
	 * Returns the value of a property as a boolean, looking first in the supplied properties map
	 * and then in the system properties.
	 * 
	 * @param propertiesMap the properties map
	 * @param key the key
	 * @param defaultValue the default value used if the property is not found
	 * @return the property value
	 */
	public static boolean getBooleanProperty (JRPropertiesMap propertiesMap, String key, boolean defaultValue)
	{
		String value = getProperty(propertiesMap, key);
		
		return value == null ? defaultValue : asBoolean(value);
	}

	/**
	 * Returns the value of a property as an integer, looking first in the supplied properties holder
	 * and then in the system properties.
	 * 
	 * @param propertiesHolder the properties holder
	 * @param key the key
	 * @param defaultValue the default value used if the property is not found
	 * @return the property value
	 */
	public static int getIntegerProperty (JRPropertiesHolder propertiesHolder, String key, int defaultValue)
	{
		String value = getProperty(propertiesHolder, key);
		
		return value == null ? defaultValue : asInteger(value);
	}
	
	/**
	 * Returns the value of a property as an integer, looking first in the supplied properties map
	 * and then in the system properties.
	 * 
	 * @param propertiesMap the properties map
	 * @param key the key
	 * @param defaultValue the default value used if the property is not found
	 * @return the property value
	 */
	public static int getIntegerProperty (JRPropertiesMap propertiesMap, String key, int defaultValue)
	{
		String value = getProperty(propertiesMap, key);
		
		return value == null ? defaultValue : asInteger(value);
	}

	/**
	 * Returns the value of a property as an integer.
	 * 
	 * @param key the key
	 * @param defaultValue the default value used if the property is not found
	 * @return the property value
	 */
	public static int getIntegerProperty (String key, int defaultValue)
	{
		String value = getProperty(key);
		
		return value == null ? defaultValue : asInteger(value);
	}

	/**
	 * Converts a <code>String</code> value into a <code>long</code>.
	 * 
	 * @param value the value
	 * @return the value as a <code>long</code>
	 */
	public static long asLong(String value)
	{
		return Long.parseLong(value);
	}
	
	/**
	 * Returns a property as a long value.
	 * 
	 * @param key the key
	 * @return the property value as a long
	 */
	public static long getLongProperty (String key)
	{
		return asLong(props.getProperty(key));
	}

	/**
	 * Returns the value of a property as a long, looking first in the supplied properties map
	 * and then in the system properties.
	 * 
	 * @param propertiesMap the properties map
	 * @param key the key
	 * @param defaultValue the default value used if the property is not found
	 * @return the property value
	 */
	public static long getLongProperty (JRPropertiesMap propertiesMap, String key, int defaultValue)
	{
		String value = getProperty(propertiesMap, key);
		
		return value == null ? defaultValue : asLong(value);
	}
	
	protected static JRPropertiesMap getOwnProperties(JRPropertiesHolder propertiesHolder)
	{
		return propertiesHolder.hasProperties() ? propertiesHolder.getPropertiesMap() : null;
	}
	
	/**
	 * Copies properties from one object to another.
	 * 
	 * <p>
	 * The properties to be copied are determined by one or more JasperReports
	 * properties having a specified prefix.  The values of these properties
	 * are interpreted as prefixes of properties to copy.
	 * </p>
	 * 
	 * @param source the source properties holder
	 * @param destination the destination properties holder 
	 * @param tranferPropertiesPrefix the prefix of the JasperReports properties
	 * that specify the object properties to copy 
	 */
	public static void transferProperties(JRPropertiesHolder source,
			JRPropertiesHolder destination, String tranferPropertiesPrefix)
	{
		if (!source.hasProperties())
		{
			return;
		}

		transfer(source.getPropertiesMap(), destination, tranferPropertiesPrefix);
	}

	/**
	 * Copies properties from one object to another.
	 * 
	 * @param source the source properties
	 * @param destination the destination properties holder 
	 * @param tranferPropertiesPrefix the prefix of the JasperReports properties
	 * that specify the object properties to copy 
	 * @see #transferProperties(JRPropertiesHolder, JRPropertiesHolder, String)
	 */
	public static void transferProperties(JRPropertiesMap source,
			JRPropertiesHolder destination, String tranferPropertiesPrefix)
	{
		if (source == null || !source.hasProperties())
		{
			return;
		}

		transfer(source, destination, tranferPropertiesPrefix);
	}

	protected static void transfer(JRPropertiesMap source,
			JRPropertiesHolder destination, String tranferPropertiesPrefix)
	{
		List transferPrefixProps = getProperties(tranferPropertiesPrefix);
		for (Iterator prefixIt = transferPrefixProps.iterator(); prefixIt.hasNext();)
		{
			JRProperties.PropertySuffix transferPrefixProp = (JRProperties.PropertySuffix) prefixIt.next();
			String transferPrefix = transferPrefixProp.getValue();
			if (transferPrefix != null && transferPrefix.length() > 0)
			{
				List transferProps = getProperties(source, transferPrefix);
				for (Iterator propIt = transferProps.iterator(); propIt.hasNext();)
				{
					JRProperties.PropertySuffix property = (JRProperties.PropertySuffix) propIt.next();
					String value = property.getValue();
					destination.getPropertiesMap().setProperty(property.getKey(), value);
				}
			}
		}
	}
}
