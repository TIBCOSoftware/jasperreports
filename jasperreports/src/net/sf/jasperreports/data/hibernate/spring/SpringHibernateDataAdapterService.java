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
package net.sf.jasperreports.data.hibernate.spring;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRClassLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Veaceslov Chicu (schicu@users.sourceforge.net)
 */
public class SpringHibernateDataAdapterService extends
		AbstractDataAdapterService {
	private static final Log log = LogFactory
			.getLog(SpringHibernateDataAdapterService.class);
	private Object session;

	/**
	 * 
	 */
	public SpringHibernateDataAdapterService(JasperReportsContext jasperReportsContext, SpringHibernateDataAdapter jsonDataAdapter) 
	{
		super(jasperReportsContext, jsonDataAdapter);
	}

	/**
	 * @deprecated Replaced by {@link #SpringHibernateDataAdapterService(JasperReportsContext, SpringHibernateDataAdapter)}.
	 */
	public SpringHibernateDataAdapterService(SpringHibernateDataAdapter jsonDataAdapter) 
	{
		this(DefaultJasperReportsContext.getInstance(), jsonDataAdapter);
	}

	public SpringHibernateDataAdapter getHibernateDataAdapter() {
		return (SpringHibernateDataAdapter) getDataAdapter();
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters)
			throws JRException {
		SpringHibernateDataAdapter hbmDA = getHibernateDataAdapter();
		if (hbmDA != null) {
			try {
				Class<?> clazz = JRClassLoader
						.loadClassForRealName("org.springframework.context.support.ClassPathXmlApplicationContext");
				if (clazz != null) {
					StringTokenizer parser = new StringTokenizer(
							hbmDA.getSpringConfig(), ",");
					String[] configs = new String[parser.countTokens()];
					int iCount = 0;
					while (parser.hasMoreTokens()) {
						configs[iCount++] = parser.nextToken();
					}
					Object configure = clazz.getConstructor(String[].class)
							.newInstance(configs);
					if (configure != null) {
						Object bsf = clazz.getMethod("getBean", String.class)
								.invoke(configure, hbmDA.getBeanId());

						session = bsf.getClass()
								.getMethod("openSession", new Class[] {})
								.invoke(bsf, new Object[] {});
						session.getClass()
								.getMethod("beginTransaction", new Class[] {})
								.invoke(session, new Object[] {});
						parameters
								.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION,
										session);
					}
				}
			} catch (ClassNotFoundException e) {
				throw new JRException(e);
			} catch (InstantiationException e) {
				throw new JRException(e);
			} catch (IllegalAccessException e) {
				throw new JRException(e);
			} catch (IllegalArgumentException e) {
				throw new JRException(e);
			} catch (SecurityException e) {
				throw new JRException(e);
			} catch (InvocationTargetException e) {
				throw new JRException(e);
			} catch (NoSuchMethodException e) {
				throw new JRException(e);
			}
		}
	}

	@Override
	public void dispose() {
		if (session != null) {
			try {
				session.getClass().getMethod("close", new Class[] {})
						.invoke(session, new Object[] {});
			} catch (Exception ex) {
				if (log.isErrorEnabled())
					log.error("Error while closing the connection.", ex);
			}
		}
	}
}
