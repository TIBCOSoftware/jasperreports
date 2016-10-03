/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PhantomJS
{
	private static final Log log = LogFactory.getLog(PhantomJS.class);
	
	public static final String PROPERTY_PHANTOMJS_EXECUTABLE_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.executable.path";
	
	public static final String PROPERTY_PHANTOMJS_TEMPDIR_PATH = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.tempdir.path";
	
	public static final String PROPERTY_PHANTOMJS_START_TIMEOUT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.start.timeout";
	public static final int DEFAULT_PHANTOMJS_START_TIMEOUT = 10000;//10 seconds
	
	public static final String PROPERTY_PHANTOMJS_MAX_PROCESS_COUNT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.max.process.count";
	public static final int DEFAULT_PHANTOMJS_MAX_PROCESS_COUNT = 8;//8 processes
	
	public static final String PROPERTY_PHANTOMJS_POOL_BORROW_TIMEOUT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.pool.borrow.timeout";
	public static final int DEFAULT_PHANTOMJS_POOL_BORROW_TIMEOUT = 120000;//2 minutes
	
	public static final String PROPERTY_PHANTOMJS_IDLE_TIMEOUT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.idle.timeout";
	public static final int DEFAULT_PHANTOMJS_IDLE_TIMEOUT = 300000;//5 minutes
	
	public static final String PROPERTY_PHANTOMJS_IDLE_PING_INTERVAL = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.idle.ping.interval";
	public static final int DEFAULT_PHANTOMJS_IDLE_PING_INTERVAL = 40000;//30 seconds
	
	public static final String PROPERTY_PHANTOMJS_EXPIRATION_COUNT = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.expiration.count";
	public static final int DEFAULT_PHANTOMJS_EXPIRATION_COUNT = 1000;//expire after 1000 jobs
	
	public static final String PROPERTY_PHANTOMJS_EXPIRATION_TIME = JRPropertiesUtil.PROPERTY_PREFIX + "phantomjs.expiration.time";
	public static final int DEFAULT_PHANTOMJS_EXPIRATION_TIME = 1200000;//expire after 20 mins
	
	public static final String MAIN_SCRIPT_RESOURCE = "net/sf/jasperreports/phantomjs/process.js";

	private static final PhantomJS INSTANCE;
	
	static
	{
		INSTANCE = new PhantomJS();
	}
	
	public static PhantomJS instance()
	{
		return INSTANCE;
	}
	
	private final JasperReportsContext jasperReportsContext;
	private final ScriptManager scriptManager;
	private final ProcessDirector processDirector;
	
	protected PhantomJS()
	{
		this.jasperReportsContext = DefaultJasperReportsContext.getInstance();
		this.scriptManager = new ScriptManager(jasperReportsContext);
		this.processDirector = new ProcessDirector(jasperReportsContext, this.scriptManager);
	}
	
	public boolean isEnabled()
	{
		return processDirector.isEnabled();
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
		//TODO lucianc dispose only when loaded
		processDirector.dispose();
		scriptManager.dispose();
	}
	
	@Override
	protected void finalize()
	{
		dispose();
	}
}
