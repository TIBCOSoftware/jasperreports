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
package net.sf.jasperreports.data.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.data.AbstractClasspathAwareDataAdapterService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.CompositeClassloader;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class BeanDataAdapterService extends AbstractClasspathAwareDataAdapterService 
{

	public BeanDataAdapterService(BeanDataAdapter beanDataAdapter) {
		super(beanDataAdapter);
	}

	public BeanDataAdapter getBeanDataAdapter() {
		return (BeanDataAdapter) getDataAdapter();
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		BeanDataAdapter beanDataAdapter = getBeanDataAdapter();
		if (beanDataAdapter != null)
		{
			JRAbstractBeanDataSource beanDataSource = null;

			ClassLoader oldThreadClassLoader = Thread.currentThread().getContextClassLoader();

			try 
			{
				Thread.currentThread().setContextClassLoader(
						new CompositeClassloader(getClassLoader(), oldThreadClassLoader)
						);

				Class<?> clazz = JRClassLoader.loadClassForRealName(beanDataAdapter.getFactoryClass());
				Method method = clazz.getMethod(beanDataAdapter.getMethodName());
				Object res = method.invoke(null);
				if (res instanceof Collection) {
					beanDataSource = new JRBeanCollectionDataSource(
							(Collection<?>) res,
							beanDataAdapter.isUseFieldDescription());
				} else if (res instanceof Object[]) {
					beanDataSource = new JRBeanArrayDataSource((Object[]) res,
							beanDataAdapter.isUseFieldDescription());
				} else {
					throw new JRException(
							"Factory method must return Collection<?> or Object[] not: "
									+ clazz.getName());
				}
			}
			catch (ClassNotFoundException e) {
				throw new JRException(e);
			} catch (IllegalAccessException e) {
				throw new JRException(e);
			} catch (SecurityException e) {
				throw new JRException(e);
			} catch (NoSuchMethodException e) {
				throw new JRException(e);
			} catch (IllegalArgumentException e) {
				throw new JRException(e);
			} catch (InvocationTargetException e) {
				throw new JRException(e);
			}
			finally
			{
				Thread.currentThread().setContextClassLoader(oldThreadClassLoader);
			}

			parameters.put(JRParameter.REPORT_DATA_SOURCE, beanDataSource);
		}
	}
}
