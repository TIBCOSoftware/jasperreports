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
package net.sf.jasperreports.data.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.data.AbstractClasspathAwareDataAdapterService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.util.SecretsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JdbcDataAdapterService extends AbstractClasspathAwareDataAdapterService 
{
	private static final Log log = LogFactory.getLog(JdbcDataAdapterService.class);
	public static final String EXCEPTION_MESSAGE_KEY_PASSWORD_REQUIRED = "data.jdbc.password.required";
	
	private Connection connection = null; 

//	/**
//	 * This classloader is used to load JDBC drivers available in the set of
//	 * paths provided by classpathPaths.
//	 */
//	private ClassLoader classLoader = null;
//
//	/**
//	 * Same as getDriversClassLoader(false)
//	 * 
//	 * @return
//	 */
//	public ClassLoader getClassLoader() {
//		return getClassLoader(false);
//	}
//
//	/**
//	 * Return the classloader, an URLClassLoader made up with all the paths
//	 * defined to look for Drivers (mainly jars).
//	 * 
//	 * @param reload
//	 *            - if true, it forces a classloader rebuilt with the set of
//	 *            paths in classpathPaths.
//	 * @return
//	 */
//	public ClassLoader getClassLoader(boolean reload) {
//		if (classLoader == null || reload) {
//			List<String> paths = ((JdbcDataAdapter) getDataAdapter())
//					.getClasspathPaths();
//			List<URL> urls = new ArrayList<URL>();
//			for (String p : paths) {
//				FileResolver fileResolver = JRResourcesUtil
//						.getFileResolver(null);
//				File f = fileResolver.resolveFile(p);
//
//				if (f != null && f.exists()) {
//					try {
//						urls.add(f.toURI().toURL());
//					} catch (MalformedURLException e) {
//						// e.printStackTrace();
//						// We don't care if the entry cannot be found.
//					}
//				}
//			}
//
//			classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
//		}
//
//		return classLoader;
//	}

	/**
	 * 
	 */
	public JdbcDataAdapterService(JasperReportsContext jasperReportsContext, JdbcDataAdapter jdbcDataAdapter) 
	{
		super(jasperReportsContext, jdbcDataAdapter);
	}

	/**
	 * @deprecated Replaced by {@link #JdbcDataAdapterService(JasperReportsContext, JdbcDataAdapter)}.
	 */
	public JdbcDataAdapterService(JdbcDataAdapter jdbcDataAdapter) 
	{
		this(DefaultJasperReportsContext.getInstance(), jdbcDataAdapter);
	}

	public JdbcDataAdapter getJdbcDataAdapter() {
		return (JdbcDataAdapter) getDataAdapter();
	}

	@Override
	public void contributeParameters(Map<String, Object> parameters) throws JRException 
	{
		try {
			connection = getConnection();
		} catch (SQLException e) {
			throw new JRException(e);
		}
		parameters.put(JRParameter.REPORT_CONNECTION, connection);
	}
	
	public Connection getConnection() throws SQLException{
		JdbcDataAdapter jdbcDataAdapter = getJdbcDataAdapter();
		if (jdbcDataAdapter != null) 
		{
			ClassLoader oldThreadClassLoader = Thread.currentThread().getContextClassLoader();

			try 
			{
				Thread.currentThread().setContextClassLoader(getClassLoader(oldThreadClassLoader));
				
				Class<?> clazz = JRClassLoader.loadClassForRealName(jdbcDataAdapter.getDriver());
				Driver driver = (Driver) clazz.newInstance();
				
//				Driver driver = (Driver) (Class.forName(
//						jdbcDataAdapter.getDriver(), true, getClassLoader()))
//						.newInstance();

				
				Properties	connectProps = new Properties();
				Map<String, String> map = jdbcDataAdapter.getProperties();
				if(map != null)
					for(String key: map.keySet())
						connectProps.setProperty(key, map.get(key));
				

				String password = jdbcDataAdapter.getPassword();
				SecretsUtil secretService = SecretsUtil.getInstance(getJasperReportsContext());
				if (secretService != null)
					password = secretService.getSecret(SECRETS_CATEGORY, password);

				connectProps.setProperty("user", jdbcDataAdapter.getUsername());
				connectProps.setProperty("password", password);
				
				connection = driver.connect(jdbcDataAdapter.getUrl(), connectProps);
				if(connection == null)
					throw new SQLException("No suitable driver found for "+ jdbcDataAdapter.getUrl());
			}
			catch (ClassNotFoundException ex){
				throw new JRRuntimeException(ex);
			} catch (InstantiationException e) {
				throw new JRRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new JRRuntimeException(e);
			} finally {
				Thread.currentThread().setContextClassLoader(oldThreadClassLoader);
			}
			return connection;
		}
		return null;
	}
	 

	public String getPassword() throws JRException {
		throw 
			new JRException(
				EXCEPTION_MESSAGE_KEY_PASSWORD_REQUIRED,
				(Object[])null);
	}

	@Override
	public void dispose() 
	{
		if (connection != null) 
		{
			try 
			{
				connection.close();
			}
			catch (Exception ex) 
			{
				if (log.isErrorEnabled())
					log.error("Error while closing the connection.", ex);
			}
		}
	}
}
