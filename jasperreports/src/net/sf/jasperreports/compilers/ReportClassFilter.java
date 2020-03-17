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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.ClassLoaderFilter;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ReportClassFilter implements ClassLoaderFilter
{
	
	public static final String PROPERTY_PREFIX_CLASS_WHITELIST = 
			JRPropertiesUtil.PROPERTY_PREFIX + "report.class.whitelist.";
	
	public static final String WHITELIST_SEPARATOR = ",";
	
	private static final String WHITELIST_SEPARATOR_PATTERN = Pattern.quote(WHITELIST_SEPARATOR);
	
	public static final String EXCEPTION_MESSAGE_KEY_CLASS_NOT_VISIBLE = "compilers.class.not.visible";

	private Set<String> classWhitelist;

	public ReportClassFilter(JasperReportsContext jasperReportsContext)
	{
		//TODO include function classes by default
		classWhitelist = loadWhitelist(jasperReportsContext);
	}

	private static Set<String> loadWhitelist(JasperReportsContext jasperReportsContext)
	{
		Set<String> whitelist = new HashSet<>();
		List<PropertySuffix> properties = JRPropertiesUtil.getInstance(jasperReportsContext).getProperties(
				PROPERTY_PREFIX_CLASS_WHITELIST);
		for (PropertySuffix propertySuffix : properties)
		{
			String whitelistString = propertySuffix.getValue();
			String[] classes = whitelistString.split(WHITELIST_SEPARATOR_PATTERN);
			for (String whitelistClass : classes)
			{
				whitelistClass = whitelistClass.trim();
				if (!whitelistClass.isEmpty())
				{
					whitelist.add(whitelistClass);
				}
			}
		}
		return whitelist;
	}

	@Override
	public void checkClassVisibility(String className) throws JRRuntimeException
	{
		if (!classWhitelist.contains(className))
		{
			throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_CLASS_NOT_VISIBLE, new Object[] {className});
		}
	}
	
}
