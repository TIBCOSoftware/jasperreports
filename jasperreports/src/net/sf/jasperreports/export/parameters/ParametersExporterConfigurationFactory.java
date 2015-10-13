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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.NamedEnum;
import net.sf.jasperreports.export.CommonExportConfiguration;
import net.sf.jasperreports.export.PropertiesExporterConfigurationFactory;
import net.sf.jasperreports.export.annotations.ExporterParameter;
import net.sf.jasperreports.export.annotations.ExporterProperty;


/**
 * @deprecated To be removed.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ParametersExporterConfigurationFactory<C extends CommonExportConfiguration>
{
	/**
	 * 
	 */
	private final JasperReportsContext jasperReportsContext;
	private final Map<JRExporterParameter, Object> parameters;
	private final JasperPrint jasperPrint;
	private final ParameterResolver parameterResolver;
	
	/**
	 * 
	 */
	public ParametersExporterConfigurationFactory(
		JasperReportsContext jasperReportsContext,
		Map<JRExporterParameter, Object> parameters,
		JasperPrint jasperPrint
		)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.parameters = parameters;
		this.jasperPrint = jasperPrint;
		
		boolean isParametersOverrideHints = true;
		
		Boolean param = (Boolean) parameters.get(JRExporterParameter.PARAMETERS_OVERRIDE_REPORT_HINTS);
		if (param == null)
		{
			isParametersOverrideHints = 
				JRPropertiesUtil.getInstance(
					jasperReportsContext
					).getBooleanProperty(
						JRExporterParameter.PROPERTY_EXPORT_PARAMETERS_OVERRIDE_REPORT_HINTS
						);
		}
		else
		{
			isParametersOverrideHints = param;
		}
		
		if (isParametersOverrideHints)
		{
			parameterResolver = 
				new ParameterOverrideResolver(
					jasperReportsContext,
					jasperPrint,
					parameters
					);
		}
		else
		{
			parameterResolver = 
				new ParameterOverriddenResolver(
					jasperReportsContext,
					jasperPrint,
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
		C proxy =
			(C)Proxy.newProxyInstance(
				JRAbstractExporter.class.getClassLoader(),
//				(Class<?>[]) allInterfaces.toArray(new Class<?>[allInterfaces.size()]),
				new Class<?>[]{clazz},
				handler
				);
		
		return proxy;
	}

	private static final Object NULL_VALUE_PLACEHOLDER = new Object();

	/**
	 * 
	 */
	class ParametersInvocationHandler implements InvocationHandler
	{
		
		private final Map<Method, Object> values;
		
		/**
		 * 
		 */
		public ParametersInvocationHandler()
		{
			//concurrency might not be involved, but let's be safe
			values = new ConcurrentHashMap<Method, Object>(16, 0.75f, 1);
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
			Object cachedValue = values.get(method);
			if (cachedValue == null)
			{
				Object value = getPropertyValue(method);
				
				//caching the result as getPropertyValue is not that cheap.
				//the result is not expected to change from one invocation to another.
				//(in theory someone might change context properties during an export, but that's not a supported scenario)
				cachedValue = value == null ? NULL_VALUE_PLACEHOLDER : value;
				values.put(method, cachedValue);
			}
			
			return cachedValue == NULL_VALUE_PLACEHOLDER ? null : cachedValue;
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
				parameter = (JRExporterParameter)exporterParameter.type().getField(exporterParameter.name()).get(null);
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
				value = 
					PropertiesExporterConfigurationFactory.getPropertyValue(
						jasperReportsContext, 
						jasperPrint, 
						exporterProperty, 
						method.getReturnType()
						);
			}
		}
		else
		{
			if (exporterProperty == null)
			{
				value = parameters.get(parameter);
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
				else if (Character.class.equals(type))
				{
					value = parameterResolver.getCharacterParameter(parameter, propertyName);
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
				else if (NamedEnum.class.isAssignableFrom(type))
				{
					if (exporterParameter.acceptNull())
					{
						value = parameterResolver.getStringParameter(parameter, propertyName);
					}
					else
					{
						value = parameterResolver.getStringParameterOrDefault(parameter, propertyName);
					}

					try
					{
						Method byNameMethod = type.getMethod("getByName", new Class<?>[]{String.class});
						value = byNameMethod.invoke(null, value);
					}
					catch (NoSuchMethodException e)
					{
						throw new JRRuntimeException(e);
					}
					catch (InvocationTargetException e)
					{
						throw new JRRuntimeException(e);
					}
					catch (IllegalAccessException e)
					{
						throw new JRRuntimeException(e);
					}
				}
				else
				{
					throw 
					new JRRuntimeException(
						PropertiesExporterConfigurationFactory.EXCEPTION_MESSAGE_KEY_EXPORT_PROPERTIES_TYPE_NOT_SUPPORTED, 
						new Object[]{type});
				}
			}
		}
		
		return value;
	}
}
