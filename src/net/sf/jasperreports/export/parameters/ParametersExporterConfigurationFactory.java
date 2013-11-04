/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRExporterContext;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class ParametersExporterConfigurationFactory<C extends ExporterConfiguration>
{
	/**
	 * 
	 */
	private final JRExporterContext exporterContext;
	private final JRPropertiesUtil propertiesUtil;

	private final ParameterResolver parameterResolver;
	
	/**
	 * 
	 */
	public ParametersExporterConfigurationFactory(JRExporterContext exporterContext)
	{
		this.exporterContext = exporterContext;
		this.propertiesUtil = JRPropertiesUtil.getInstance(exporterContext.getJasperReportsContext());

		Map<JRExporterParameter, Object> parameters = exporterContext.getExportParameters();

		boolean isParametersOverrideHints = true;
		
		Boolean param = (Boolean) parameters.get(JRExporterParameter.PARAMETERS_OVERRIDE_REPORT_HINTS);
		if (param == null)
		{
			isParametersOverrideHints = propertiesUtil.getBooleanProperty(JRExporterParameter.PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS);
		}
		else
		{
			isParametersOverrideHints = param;
		}
		
		if (isParametersOverrideHints)
		{
			parameterResolver = 
				new ParameterOverrideResolver(
					exporterContext.getJasperReportsContext(),
					exporterContext.getExportedReport(),
					parameters
					);
		}
		else
		{
			parameterResolver = 
				new ParameterOverriddenResolver(
					exporterContext.getJasperReportsContext(),
					exporterContext.getExportedReport(),
					parameters
					);
		}
	}

	
	/**
	 * 
	 */
	public C getConfiguration(Class<C> configurationInterface)
	{
		return getProxy(configurationInterface, new ParametersInvocationHandler());
	}


	/**
	 * 
	 */
	private final C getProxy(Class<?> clazz, InvocationHandler handler)
	{
//		@SuppressWarnings("rawtypes")
//		List allInterfaces = ClassUtils.getAllInterfaces(clazz);

		@SuppressWarnings("unchecked")
		C composite =
			(C)Proxy.newProxyInstance(
				JRAbstractExporter.class.getClassLoader(),
//				(Class<?>[]) allInterfaces.toArray(new Class<?>[allInterfaces.size()]),
				new Class<?>[]{clazz},
				handler
				);
		
		return composite;
	}


	/**
	 * 
	 */
	class ParametersInvocationHandler implements InvocationHandler
	{
		/**
		 * 
		 */
		public ParametersInvocationHandler()
		{
		}
		
		/**
		 * 
		 */
		public Object invoke(
			Object proxy, 
			Method method, 
			Object[] args
			) throws Throwable 
		{
			return getPropertyValue(method);
		}
	}
	
	
	/**
	 * 
	 */
	protected Object getPropertyValue(Method method)
	{
		Object value = null;

		JRExporterParameter parameter = null;
		ExporterParameter exporterParameter = method.getAnnotation(ExporterParameter.class);
		if (exporterParameter != null)
		{
			try
			{
				parameter = (JRExporterParameter)exporterParameter.parameterClass().getField(exporterParameter.parameterName()).get(null);
			}
			catch (NoSuchFieldException e)
			{
				throw new JRRuntimeException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new JRRuntimeException(e);
			}
		}

		ExporterProperty exporterProperty = method.getAnnotation(ExporterProperty.class);
		
		if (parameter == null)
		{
			if (exporterProperty == null)
			{
				//nothing to do
			}
			else
			{
				String propertyName = exporterProperty.value();
				
				Class<?> type = method.getReturnType();
				
				if (String[].class.equals(type))
				{
					List<PropertySuffix> properties = JRPropertiesUtil.getProperties(exporterContext.getExportedReport(), propertyName);
					if (properties != null && !properties.isEmpty())
					{
						String[] values = new String[properties.size()];
						for(int i = 0; i < values.length; i++)
						{
							values[i] = properties.get(i).getValue();
						}
						
						value = values;
					}
				}
				else
				{
					if (String.class.equals(type))
					{
						value = propertiesUtil.getProperty(exporterContext.getExportedReport(), propertyName);
					}
					else if (Integer.class.equals(type))
					{
						value = propertiesUtil.getIntegerProperty(exporterContext.getExportedReport(), propertyName, exporterProperty.intDefault());
					}
					else if (Long.class.equals(type))
					{
						value = propertiesUtil.getLongProperty(exporterContext.getExportedReport(), propertyName, exporterProperty.longDefault());
					}
					else if (Float.class.equals(type))
					{
						value = propertiesUtil.getFloatProperty(exporterContext.getExportedReport(), propertyName, exporterProperty.floatDefault());
					}
					else if (Boolean.class.equals(type))
					{
						value = propertiesUtil.getBooleanProperty(exporterContext.getExportedReport(), propertyName, exporterProperty.booleanDefault());
					}
				}
			}
		}
		else
		{
			if (exporterProperty == null)
			{
				value = exporterContext.getExportParameters().get(parameter);
			}
			else
			{
				String propertyName = exporterProperty.value();

				Class<?> type = method.getReturnType();
				
				if (String[].class.equals(type))
				{
					value = parameterResolver.getStringArrayParameter(parameter, propertyName);
				}
				else if (String.class.equals(type))
				{
					if (exporterParameter.acceptNull())
					{
						value = parameterResolver.getStringParameter(parameter, propertyName);
					}
					else
					{
						value = parameterResolver.getStringParameterOrDefault(parameter, propertyName);
					}
				}
				else if (Integer.class.equals(type))
				{
					value = parameterResolver.getIntegerParameter(parameter, propertyName, exporterProperty.intDefault());
				}
				else if (Float.class.equals(type))
				{
					value = parameterResolver.getFloatParameter(parameter, propertyName, exporterProperty.floatDefault());
				}
				else if (Boolean.class.equals(type))
				{
					value = parameterResolver.getBooleanParameter(parameter, propertyName, exporterProperty.booleanDefault());
				}
				else
				{
					throw new JRRuntimeException("Export property type " + type + " not supported.");
				}
			}
		}
		
		return value;
	}
}
