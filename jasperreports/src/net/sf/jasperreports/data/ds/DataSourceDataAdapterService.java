/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.data.ds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import net.sf.jasperreports.data.AbstractClasspathAwareDataAdapterService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.CompositeClassloader;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class DataSourceDataAdapterService extends
		AbstractClasspathAwareDataAdapterService {

	public DataSourceDataAdapterService(DataSourceDataAdapter dsDataAdapter) {
		super(dsDataAdapter);
	}

	public DataSourceDataAdapter getDataSourceDataAdapter() {
		return (DataSourceDataAdapter) getDataAdapter();
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		DataSourceDataAdapter dsDataAdapter = getDataSourceDataAdapter();
		if (dsDataAdapter != null)
		{
			JRDataSource ds = null;

			ClassLoader oldThreadClassLoader = Thread.currentThread().getContextClassLoader();

			try 
			{
				Thread.currentThread().setContextClassLoader(
					new CompositeClassloader(getClassLoader(), oldThreadClassLoader)
					);

				Class<?> clazz = JRClassLoader.loadClassForRealName(dsDataAdapter.getFactoryClass());
				Object obj = null;
				Method method = clazz.getMethod( dsDataAdapter.getMethodToCall(), new Class[0]);
				if(!Modifier.isStatic(method.getModifiers()))
					obj = clazz.newInstance();
				if(JRDataSource.class.isAssignableFrom(method.getReturnType()))
					ds = (JRDataSource) method.invoke(obj,new Object[0]);
				else
					throw new JRException("Method " + dsDataAdapter.getMethodToCall() + " in " + dsDataAdapter.getFactoryClass() + " class does not return a JRDataSource object.");
			}
			catch (ClassNotFoundException e)
			{
				throw new JRException(e);			
			} 
			catch (NoSuchMethodException e)
			{
				throw new JRException(e);			
			} 
			catch (InvocationTargetException e)
			{
				throw new JRException(e);			
			} 
			catch (IllegalAccessException e)
			{
				throw new JRException(e);			
			} catch (InstantiationException e) {
				throw new JRException(e);			
			} 
			finally
			{
				Thread.currentThread().setContextClassLoader(oldThreadClassLoader);
			}

			parameters.put(JRParameter.REPORT_DATA_SOURCE, ds);
		}
	}
}
