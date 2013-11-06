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

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.export.ExporterConfiguration;

import org.apache.commons.lang.ClassUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CompositeExporterConfigurationFactory<C extends ExporterConfiguration>
{
	/**
	 * 
	 */
	private final JRPropertiesUtil propertiesUtil;
	
	/**
	 * 
	 */
	public CompositeExporterConfigurationFactory(JasperReportsContext jasperReportsContext)
	{
		this.propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
	}

	
	/**
	 * 
	 */
	public C getConfiguration(final C parent, final C child)
	{
		if (parent == null)
		{
			return child;
		}
		else
		{
			boolean isOverrideHints = 
				parent.isOverrideHints() == null 
				? propertiesUtil.getBooleanProperty(ExporterConfiguration.PROPERTY_EXPORT_CONFIGURATION_OVERRIDE_REPORT_HINTS)
				: parent.isOverrideHints().booleanValue();
			if (isOverrideHints)
			{
				return getProxy(child.getClass(), new DelegateInvocationHandler(child, parent));
			}
			else
			{
				return getProxy(child.getClass(), new DelegateInvocationHandler(parent, child));
			}
			
		}
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
	class DelegateInvocationHandler implements InvocationHandler
	{
		private final C parent;
		private final C child;
		
		/**
		 * 
		 */
		public DelegateInvocationHandler(final C parent, final C child)
		{
			this.parent = parent;
			this.child = child;
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
			Object value = child == null ? null : method.invoke(child, args);
			if (value == null)
			{
				value = parent == null ? null : method.invoke(parent, args);
			}
			return value;
		}
	}
}
