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
package net.sf.jasperreports.export.parameters;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParameterOverrideResolver implements ParameterResolver
{
	private final JRPropertiesUtil propertiesUtil;
	private final JasperPrint jasperPrint;
	private final Map<JRExporterParameter, Object> parameters;
	

	/**
	 *
	 */
	public ParameterOverrideResolver(
		JasperReportsContext jasperReportsContext,
		JasperPrint jasperPrint,
		Map<JRExporterParameter, Object> parameters
		)
	{
		this.propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
		this.jasperPrint = jasperPrint;
		this.parameters = parameters;
	}
	
	
	public String getStringParameter(JRExporterParameter parameter, String property)
	{
		if (parameters.containsKey(parameter))
		{
			return (String)parameters.get(parameter);
		}
		else
		{
			return 
				getPropertiesUtil().getProperty(
					jasperPrint.getPropertiesMap(),
					property
					);
		}
	}

	public String[] getStringArrayParameter(JRExporterParameter parameter, String propertyPrefix)
	{
		String[] values = null; 
		if (parameters.containsKey(parameter))
		{
			values = (String[])parameters.get(parameter);
		}
		else
		{
			List<PropertySuffix> properties = JRPropertiesUtil.getProperties(jasperPrint.getPropertiesMap(), propertyPrefix);
			if (properties != null && !properties.isEmpty())
			{
				values = new String[properties.size()];
				for(int i = 0; i < values.length; i++)
				{
					values[i] = properties.get(i).getValue();
				}
			}
		}
		return values;
	}

	public String getStringParameterOrDefault(JRExporterParameter parameter, String property)
	{
		if (parameters.containsKey(parameter))
		{
			String value = (String)parameters.get(parameter);
			if (value == null)
			{
				return getPropertiesUtil().getProperty(property);
			}
			else
			{
				return value;
			}
		}
		else
		{
			return
				getPropertiesUtil().getProperty(
					jasperPrint.getPropertiesMap(),
					property
					);
		}
	}

	public boolean getBooleanParameter(JRExporterParameter parameter, String property, boolean defaultValue)
	{
		if (parameters.containsKey(parameter))
		{
			Boolean booleanValue = (Boolean)parameters.get(parameter);
			if (booleanValue == null)
			{
				return getPropertiesUtil().getBooleanProperty(property);
			}
			else
			{
				return booleanValue.booleanValue();
			}
		}
		else
		{
			return 
				getPropertiesUtil().getBooleanProperty(
					jasperPrint.getPropertiesMap(),
					property,
					defaultValue
					);
		}
	}

	public int getIntegerParameter(JRExporterParameter parameter, String property, int defaultValue)
	{
		if (parameters.containsKey(parameter))
		{
			Integer integerValue = (Integer)parameters.get(parameter);
			if (integerValue == null)
			{
				return getPropertiesUtil().getIntegerProperty(property);
			}
			else
			{
				return integerValue.intValue();
			}
		}
		else
		{
			return 
				getPropertiesUtil().getIntegerProperty(
					jasperPrint.getPropertiesMap(),
					property,
					defaultValue
					);
		}
	}
	
	public float getFloatParameter(JRExporterParameter parameter, String property, float defaultValue)
	{
		if (parameters.containsKey(parameter))
		{
			Float floatValue = (Float)parameters.get(parameter);
			if (floatValue == null)
			{
				return getPropertiesUtil().getFloatProperty(property);
			}
			else
			{
				return floatValue.floatValue();
			}
		}
		else
		{
			return 
				getPropertiesUtil().getFloatProperty(
					jasperPrint.getPropertiesMap(),
					property,
					defaultValue
					);
		}
	}
	
	public Character getCharacterParameter(JRExporterParameter parameter, 
			String property)
	{
		if (parameters.containsKey(parameter))
		{
			return (Character) parameters.get(parameter);
		}
		else
		{
			return getPropertiesUtil().getCharacterProperty(
					jasperPrint.getPropertiesMap(), property);
		}
	}

	/**
	 *
	 */
	private JRPropertiesUtil getPropertiesUtil()
	{
		return propertiesUtil;
	}
}
