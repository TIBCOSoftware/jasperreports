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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.launch.ChromeArguments;
import com.github.kklisura.cdt.launch.ChromeArguments.Builder;
import com.github.kklisura.cdt.launch.support.annotations.ChromeArgument;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ChromeArgumentsBuilder
{
	
	private static final Log log = LogFactory.getLog(ChromeArgumentsBuilder.class);

	private final static ChromeArgumentsBuilder INSTANCE = new ChromeArgumentsBuilder();
	
	public static ChromeArgumentsBuilder instance()
	{
		return INSTANCE;
	}
	
	public ChromeArgumentsBuilder()
	{
	}

	public ChromeArguments toArguments(LaunchConfiguration configuration)
	{
		Builder builder = ChromeArguments.defaults(configuration.isHeadless());
		ChromeArguments arguments = builder.build();
		
		Map<String, String> configArguments = configuration.getArguments();
		if (configArguments != null && !configArguments.isEmpty())
		{
			for (Entry<String, String> entry : configArguments.entrySet())
			{
				String arg = entry.getKey();
				String value = entry.getValue();
				if (value != null && !value.isEmpty())
				{
					setArgument(arguments, arg, value);
				}
			}
		}
		
		return arguments;
	}

	protected void setArgument(ChromeArguments arguments, String name, String value)
	{
		//trying to set as field first because fields override getAdditionalArguments()
		//this allows changing args set by ChromeArguments.defaults()
		boolean setField = false;
		Field[] chromeArgumentsFields = ChromeArguments.class.getDeclaredFields();
		for (Field field : chromeArgumentsFields)
		{
			ChromeArgument fieldAnnotation = field.getAnnotation(ChromeArgument.class);
			if (fieldAnnotation != null && fieldAnnotation.value().equals(name))
			{
				Class<?> type = field.getType();
				Object fieldValue;
				if (type.equals(String.class))
				{
					fieldValue = value;
				}
				else if (type.equals(Boolean.class))
				{
					fieldValue = Boolean.valueOf(value);
				}
				else if (type.equals(Integer.class))
				{
					fieldValue = Integer.valueOf(value);
				}
				else
				{
					log.warn("Chrome arguments field type " + type + " not supported");
					break;
				}
				
				field.setAccessible(true);
				try
				{
					field.set(arguments, fieldValue);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
					throw new JRRuntimeException(e);
				}
				setField = true;
				break;
			}
		}
		
		if (!setField)
		{
			arguments.getAdditionalArguments().put(name, value);
		}
	}
	
}
