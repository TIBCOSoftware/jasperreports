/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.chrome;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.launch.ChromeLauncher;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.properties.PropertyConstants;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class Chrome
{

	private static final Log log = LogFactory.getLog(Chrome.class);
	
	protected static final String PROPERTY_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "chrome.";
	
	@Property(
			category = PropertyConstants.CATEGORY_CHROME,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_11_0,
			valueType = Boolean.class,
			defaultValue = "false"
			)
	public static final String PROPERTY_ENABLED = PROPERTY_PREFIX + "enabled";
	
	@Property(
			category = PropertyConstants.CATEGORY_CHROME,
					scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_11_0
			)
	public static final String PROPERTY_EXECUTABLE_PATH = PROPERTY_PREFIX + "executable.path";
	
	@Property(
			category = PropertyConstants.CATEGORY_CHROME,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_11_0,
			valueType = Boolean.class,
			defaultValue = "true"
			)
	public static final String PROPERTY_HEADLESS = PROPERTY_PREFIX + "headless";
		
	@Property(
			name = "net.sf.jasperreports.chrome.argument.{name}",
			category = PropertyConstants.CATEGORY_CHROME,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_11_0
			)
	public static final String PROPERTY_ARGUMENT_PREFIX = PROPERTY_PREFIX + "argument.";
	
	@Property(
			category = PropertyConstants.CATEGORY_CHROME,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_11_0,
			valueType = Long.class,
			defaultValue = "900000"
			)
	public static final String PROPERTY_IDLE_TIMEOUT = PROPERTY_PREFIX + "idle.timeout";
		
	@Property(
			category = PropertyConstants.CATEGORY_CHROME,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_11_0,
			valueType = Long.class,
			defaultValue = "7200000"
			)
	public static final String PROPERTY_LIVE_TIMEOUT = PROPERTY_PREFIX + "live.timeout";

	private static Path DETECTED_CHROME_PATH;
	
	private static class CDTClassCheck
	{		
		private final static boolean FOUND_CDT_CLASS;
				
		static
		{
			FOUND_CDT_CLASS = foundCDTClass();
			if (log.isDebugEnabled())
			{
				log.debug("found CDT class " + FOUND_CDT_CLASS);
			}
		}
		
		private static boolean foundCDTClass()
		{
			try
			{
				Class.forName("com.github.kklisura.cdt.launch.ChromeLauncher");
				return true;
			}
			catch (ClassNotFoundException | NoClassDefFoundError e)
			{
				log.warn("Failed to load CDT class: " + e);
				if (log.isDebugEnabled())
				{
					log.debug("error loading CDT class", e);
				}
				return false;
			}
		}
	}
	
	private static class ChromeEnvironment
	{
		public static final Path findChromeExecutable()
		{
			try
			{
				@SuppressWarnings("resource")
				ChromeLauncher launcher = new ChromeLauncher();
				Path chromeBinaryPath = launcher.getChromeBinaryPath();
				if (log.isDebugEnabled())
				{
					log.debug("chrome binary found at " + chromeBinaryPath);
				}
				return chromeBinaryPath;
			}
			catch (RuntimeException e)
			{
				log.warn("Chrome binary not autodetected: " + e.getMessage());
				return null;
			}
		}		
	}
	
	
	public static Chrome instance(JasperReportsContext jasperReportsContext)
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(jasperReportsContext);
		
		boolean enabled = properties.getBooleanProperty(PROPERTY_ENABLED, false);
		if (enabled)
		{
			enabled = CDTClassCheck.FOUND_CDT_CLASS;
		}
		
		BrowserService service = null;
		
		if (enabled)
		{
			String executable = properties.getProperty(PROPERTY_EXECUTABLE_PATH);
			Path executablePath;
			if (executable == null || executable.trim().isEmpty())
			{
				executablePath = detectedChromeExecutable();
			}
			else
			{
				executablePath = Paths.get(executable.trim());
			}
			
			if (executablePath == null)
			{
				enabled = false;
			}
			else
			{
				boolean headless = properties.getBooleanProperty(PROPERTY_HEADLESS, true);
				
				List<PropertySuffix> argProperties = properties.getAllProperties((JRPropertiesMap) null, 
						PROPERTY_ARGUMENT_PREFIX);
				Map<String, String> args = new LinkedHashMap<>();
				for (PropertySuffix propertySuffix : argProperties)
				{
					String value = propertySuffix.getValue();
					if (value != null && !value.isEmpty())
					{
						args.put(propertySuffix.getSuffix(), value);
					}
				}
				
				long idleTimeout = properties.getLongProperty(PROPERTY_IDLE_TIMEOUT, 0);
				long liveTimeout = properties.getLongProperty(PROPERTY_LIVE_TIMEOUT, 0);
				
				LaunchConfiguration configuration = new LaunchConfiguration(executablePath, headless, args,
						idleTimeout, liveTimeout);
				ChromeServiceHandle chromeServiceHandle = new ChromeServiceHandle(configuration);
				service = new BrowserService(jasperReportsContext, chromeServiceHandle);
			}
		}
		
		return new Chrome(enabled, service);
	}
	
	private static Path detectedChromeExecutable()
	{
		Path path = DETECTED_CHROME_PATH;
		if (path == null)
		{
			DETECTED_CHROME_PATH = path = ChromeEnvironment.findChromeExecutable();
		}
		return path;
	}
	
	private final boolean enabled;
	
	private final BrowserService service;
	
	protected Chrome(boolean enabled, BrowserService service)
	{
		this.enabled = enabled;
		this.service = service;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public BrowserService getService()
	{
		return service;
	}
	
}
