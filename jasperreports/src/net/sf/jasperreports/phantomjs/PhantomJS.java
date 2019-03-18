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
package net.sf.jasperreports.phantomjs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PhantomJS
{
	private static final Log log = LogFactory.getLog(PhantomJS.class);
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_3_1
			)
	public static final String PROPERTY_PHANTOMJS_EXECUTABLE_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.executable.path";
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_3_1
			)
	public static final String PROPERTY_PHANTOMJS_TEMPDIR_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.tempdir.path";
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "10000",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_3_1,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_START_TIMEOUT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.start.timeout";
	public static final int DEFAULT_PHANTOMJS_START_TIMEOUT = 10000;//10 seconds
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "8",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_4_0,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_MAX_PROCESS_COUNT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.max.process.count";
	public static final int DEFAULT_PHANTOMJS_MAX_PROCESS_COUNT = 8;//8 processes
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "120000",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_4_0,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_POOL_BORROW_TIMEOUT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.pool.borrow.timeout";
	public static final int DEFAULT_PHANTOMJS_POOL_BORROW_TIMEOUT = 120000;//2 minutes
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "300000",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_4_0,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_IDLE_TIMEOUT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.idle.timeout";
	public static final int DEFAULT_PHANTOMJS_IDLE_TIMEOUT = 300000;//5 minutes
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "40000",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_4_0,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_IDLE_PING_INTERVAL = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.idle.ping.interval";
	public static final int DEFAULT_PHANTOMJS_IDLE_PING_INTERVAL = 40000;//40 seconds
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "1000",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_4_0,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_EXPIRATION_COUNT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.expiration.count";
	public static final int DEFAULT_PHANTOMJS_EXPIRATION_COUNT = 1000;//expire after 1000 jobs
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "1200000",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_4_0,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_EXPIRATION_TIME = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.expiration.time";
	public static final int DEFAULT_PHANTOMJS_EXPIRATION_TIME = 1200000;//expire after 20 mins
	
	@Property(
			category = PropertyConstants.CATEGORY_PHANTOM_JS,
			defaultValue = "60000",
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_4_0,
			valueType = Integer.class
			)
	public static final String PROPERTY_PHANTOMJS_REQUEST_TIMEOUT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.request.timeout";
	public static final int DEFAULT_PHANTOMJS_REQUEST_TIMEOUT = 60000;//timeout after 1 minute
	
	public static final String MAIN_SCRIPT_RESOURCE = "net/sf/jasperreports/phantomjs/process.js";
	
	public static boolean isEnabled()
	{
		String phantomjsExecutablePath = DefaultJasperReportsContext.getInstance().getProperty(
				PROPERTY_PHANTOMJS_EXECUTABLE_PATH);
		return phantomjsExecutablePath != null;
	}
	
	private static class PhantomJSInitializer
	{
		private static final PhantomJS INSTANCE = new PhantomJS();
	}
	
	public static PhantomJS instance()
	{
		return PhantomJSInitializer.INSTANCE;
	}
	
	private static volatile boolean INSTANTIATED = false;
	
	public static void disposePhantom()
	{
		if (INSTANTIATED)
		{
			instance().dispose();
		}
	}
	
	private final JasperReportsContext jasperReportsContext;
	private final ScriptManager scriptManager;
	private final ProcessDirector processDirector;
	
	private PhantomJS()
	{
		INSTANTIATED = true;
		this.jasperReportsContext = DefaultJasperReportsContext.getInstance();
		this.scriptManager = new ScriptManager(jasperReportsContext);
		this.processDirector = new ProcessDirector(jasperReportsContext, this.scriptManager);
	}
	
	public ScriptManager getScriptManager()
	{
		return scriptManager;
	}
	
	public String runRequest(String data)
	{
		return processDirector.runRequest(data);
	}
	
	public void  dispose()
	{
		processDirector.dispose();
		scriptManager.dispose();
	}
	
	@Override
	protected void finalize()
	{
		dispose();
	}
}
