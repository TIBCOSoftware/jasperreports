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
package net.sf.jasperreports.compilers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.ClassLoaderFilter;
import net.sf.jasperreports.functions.FunctionsBundle;
import net.sf.jasperreports.functions.FunctionsUtil;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ReportClassFilter implements ClassLoaderFilter
{
	
	@Property(
			category = PropertyConstants.CATEGORY_FILL,
			defaultValue = "false",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_13_0,
			valueType = Boolean.class
			)
	public static final String PROPERTY_PREFIX_CLASS_FILTER_ENABLED = 
			JRPropertiesUtil.PROPERTY_PREFIX + "report.class.filter.enabled";
	
	@Property(
			category = PropertyConstants.CATEGORY_FILL,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_13_0,
			name = "net.sf.jasperreports.report.class.whitelist.{arbitrary_name}"
			)
	public static final String PROPERTY_PREFIX_CLASS_WHITELIST = 
			JRPropertiesUtil.PROPERTY_PREFIX + "report.class.whitelist.";
	
	public static final String EXCEPTION_MESSAGE_KEY_CLASS_NOT_VISIBLE = "compilers.class.not.visible";

	private static void addHardcodedWhitelist(StandardReportClassWhitelist whitelist)
	{
		whitelist.addClass("java.lang.Boolean");
		whitelist.addClass("java.lang.String");
		whitelist.addClass("java.lang.StringBuffer");
		whitelist.addClass("java.lang.StringBuilder");
		whitelist.addClass("java.lang.Character");
		whitelist.addClass("java.lang.Byte");
		whitelist.addClass("java.lang.Short");
		whitelist.addClass("java.lang.Integer");
		whitelist.addClass("java.lang.Long");
		whitelist.addClass("java.lang.Float");
		whitelist.addClass("java.lang.Double");
		whitelist.addClass("java.lang.Math");
	}
	
	private boolean filterEnabled;
	private List<ReportClassWhitelist> whitelists;
	
	private Map<String, Boolean> visibilityCache = new ConcurrentHashMap<String, Boolean>();

	public ReportClassFilter(JasperReportsContext jasperReportsContext)
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(jasperReportsContext);
		filterEnabled = properties.getBooleanProperty(PROPERTY_PREFIX_CLASS_FILTER_ENABLED);
		if (filterEnabled)
		{
			whitelists = new ArrayList<>();
			
			StandardReportClassWhitelist whitelist = new StandardReportClassWhitelist();
			addHardcodedWhitelist(whitelist);
			loadPropertiesWhitelist(properties, whitelist);
			loadFunctionsWhitelist(jasperReportsContext, whitelist);
			whitelists.add(whitelist);
			
			List<ReportClassWhitelist> extensionWhitelists = jasperReportsContext.getExtensions(
					ReportClassWhitelist.class);
			whitelists.addAll(extensionWhitelists);			
		}		
	}

	private static void loadPropertiesWhitelist(JRPropertiesUtil propertiesUtil, 
			StandardReportClassWhitelist whitelist)
	{
		List<PropertySuffix> properties = propertiesUtil.getProperties(PROPERTY_PREFIX_CLASS_WHITELIST);
		for (PropertySuffix propertySuffix : properties)
		{
			String whitelistString = propertySuffix.getValue();
			whitelist.addWhitelist(whitelistString);
		}
	}

	private static void loadFunctionsWhitelist(JasperReportsContext jasperReportsContext, 
			StandardReportClassWhitelist whitelist)
	{
		FunctionsUtil functionsUtil = FunctionsUtil.getInstance(jasperReportsContext);
		List<FunctionsBundle> functionBundles = functionsUtil.getAllFunctionBundles();
		for (FunctionsBundle functionsBundle : functionBundles)
		{
			List<Class<?>> functionClasses = functionsBundle.getFunctionClasses();
			for (Class<?> functionClass : functionClasses)
			{
				whitelist.addClass(functionClass.getName());
			}
		}
	}

	public boolean isFilteringEnabled()
	{
		return filterEnabled;
	}
	
	@Override
	public void checkClassVisibility(String className) throws JRRuntimeException
	{
		boolean visible = isClassVisible(className);
		if (!visible)
		{
			throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_CLASS_NOT_VISIBLE, new Object[] {className});
		}
	}
	
	public boolean isClassVisible(String className)
	{
		Boolean visible = visibilityCache.get(className);
		if (visible == null)
		{
			visible = visible(className);
			visibilityCache.put(className, visible);
		}
		return visible;
	}

	protected boolean visible(String className)
	{
		boolean visible;
		if (filterEnabled)
		{
			visible = false;
			for (ReportClassWhitelist whitelist : whitelists)
			{
				if (whitelist.includesClass(className))
				{
					visible = true;
					break;
				}
			}
		}
		else
		{
			visible = true;
		}
		return visible;
	}
	
}
