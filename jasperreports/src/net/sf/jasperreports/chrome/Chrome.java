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

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.phantomjs.ScriptManager;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class Chrome
{

	private static final Log log = LogFactory.getLog(Chrome.class);
	
	private static final String PROPERTY_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "chrome.";
	
	public static final String PROPERTY_ENABLED = PROPERTY_PREFIX + "enabled";
	
	public static final String PROPERTY_EXECUTABLE_PATH = PROPERTY_PREFIX + "executable.path";
	
	public static final String PROPERTY_HEADLESS = PROPERTY_PREFIX + "headless";
	
	public static final String PROPERTY_ARGUMENT_PREFIX = PROPERTY_PREFIX + "argument.";
	
	public static final String PROPERTY_TEMPDIR_PATH = PROPERTY_PREFIX + "tempdir.path";

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
	
	public static Chrome instance(JasperReportsContext jasperReportsContext)
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(jasperReportsContext);
		
		boolean enabled = properties.getBooleanProperty(PROPERTY_ENABLED, false);
		if (enabled)
		{
			enabled = CDTClassCheck.FOUND_CDT_CLASS;
		}
		
		ScriptManager scriptManager = null;
		Service service = null;
		
		if (enabled)
		{
			String executable = properties.getProperty(PROPERTY_EXECUTABLE_PATH);
			Path executablePath;
			if (executable == null)
			{
				executablePath = detectedChromeExecutable();
			}
			else
			{
				executablePath = Paths.get(executable);
			}
			
			if (executablePath == null)
			{
				enabled = false;
			}
			else
			{
				scriptManager = ScriptManagerRepository.instance().getService(jasperReportsContext);
				
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
				
				LaunchConfiguration configuration = new LaunchConfiguration(executablePath, headless, args);
				service = ServiceRepository.isntance().getService(configuration);
			}
		}
		
		return new Chrome(enabled, scriptManager, service);
	}
	
	private static Path detectedChromeExecutable()
	{
		Path path = DETECTED_CHROME_PATH;
		if (path == null)
		{
			DETECTED_CHROME_PATH = path = Service.findChromeExecutable();
		}
		return path;
	}
	
	private final boolean enabled;
	
	//TODO move out of phantom package
	private final ScriptManager scriptManager;
	
	private final Service service;
	
	protected Chrome(boolean enabled, ScriptManager scriptManager, Service service)
	{
		this.enabled = enabled;
		this.scriptManager = scriptManager;
		this.service = service;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public ScriptManager getScriptManager()
	{
		return scriptManager;
	}
	
	public Service getService()
	{
		return service;
	}
	
}
