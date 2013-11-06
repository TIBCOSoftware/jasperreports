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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.JREnum;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.annotations.ExporterProperty;

import org.apache.commons.lang.ClassUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class PropertiesExporterConfigurationFactory<C extends ExporterConfiguration>
{
	/**
	 * 
	 */
	private final JasperReportsContext jasperReportsContext;
	
	/**
	 * 
	 */
	public PropertiesExporterConfigurationFactory(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}

	
	/**
	 * 
	 */
	public C getConfiguration(final C parent, final JRPropertiesHolder propertiesHolder)
	{
		final C child = getProxy(parent.getClass(), new PropertiesInvocationHandler(propertiesHolder));

		CompositeExporterConfigurationFactory<C> factory = new CompositeExporterConfigurationFactory<C>(jasperReportsContext);
		
		return factory.getConfiguration(parent, child);
	}


	/**
	 * 
	 */
	private final C getProxy(Class<?> clazz, InvocationHandler handler)
	{
		@SuppressWarnings("rawtypes")
		List allInterfaces = ClassUtils.getAllInterfaces(clazz);

		@SuppressWarnings("unchecked")
		C composite =
			(C)Proxy.newProxyInstance(
				JRAbstractExporter.class.getClassLoader(),
				(Class<?>[]) allInterfaces.toArray(new Class<?>[allInterfaces.size()]),
				handler
				);
		
		return composite;
	}


	/**
	 * 
	 */
	class PropertiesInvocationHandler implements InvocationHandler
	{
		private final JRPropertiesHolder propertiesHolder;
		
		/**
		 * 
		 */
		public PropertiesInvocationHandler(final JRPropertiesHolder propertiesHolder)
		{
			this.propertiesHolder = propertiesHolder;
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
			return getPropertyValue(method, propertiesHolder);
		}
	}
	
	
	/**
	 * 
	 */
	protected Object getPropertyValue(Method method, JRPropertiesHolder propertiesHolder)
	{
		Object value = null;
		ExporterProperty exporterProperty = method.getAnnotation(ExporterProperty.class);
		if (exporterProperty != null)
		{
			value = getPropertyValue(jasperReportsContext, propertiesHolder, exporterProperty, method.getReturnType());
		}
		return value;
	}
	
	
	/**
	 * 
	 */
	public static Object getPropertyValue(
		JasperReportsContext jasperReportsContext,
		JRPropertiesHolder propertiesHolder,
		ExporterProperty exporterProperty, 
		Class<?> type 
		)
	{
		Object value = null;
		
		String propertyName = exporterProperty.value();
		
		if (String[].class.equals(type))
		{
			List<PropertySuffix> properties = JRPropertiesUtil.getProperties(propertiesHolder, propertyName);
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
			JRPropertiesUtil propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
			if (String.class.equals(type))
			{
				value = propertiesUtil.getProperty(propertiesHolder, propertyName);
			}
			else if (Character.class.equals(type))
			{
				value = propertiesUtil.getCharacterProperty(propertiesHolder, propertyName);
			}
			else if (Integer.class.equals(type))
			{
				if (exporterProperty.acceptNull())
				{
					value = propertiesUtil.getIntegerProperty(propertiesHolder, propertyName);
				}
				else
				{
					value = propertiesUtil.getIntegerProperty(propertiesHolder, propertyName, exporterProperty.intDefault());
				}
			}
			else if (Long.class.equals(type))
			{
				value = propertiesUtil.getLongProperty(propertiesHolder, propertyName, exporterProperty.longDefault());
			}
			else if (Float.class.equals(type))
			{
				value = propertiesUtil.getFloatProperty(propertiesHolder, propertyName, exporterProperty.floatDefault());
			}
			else if (Boolean.class.equals(type))
			{
				value = propertiesUtil.getBooleanProperty(propertiesHolder, propertyName, exporterProperty.booleanDefault());
			}
			else if (JREnum.class.isAssignableFrom(type))
			{
				value = propertiesUtil.getProperty(propertiesHolder, propertyName);
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
				throw new JRRuntimeException("Export property type " + type + " not supported.");
			}
		}
		
		return value;
	}
}
