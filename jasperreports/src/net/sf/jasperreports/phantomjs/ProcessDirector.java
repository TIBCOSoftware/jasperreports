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

import java.net.Inet4Address;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.SwallowedExceptionListener;
import org.apache.commons.pool2.impl.GenericObjectPool;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ProcessDirector
{
	private static final Log log = LogFactory.getLog(ProcessDirector.class);
	
	private String phantomjsExecutablePath;
	private int processStartTimeout;
	private ScriptManager scriptManager;
	private Inet4Address listenAddress;
	private GenericObjectPool<PhantomJSProcess> processPool;

	public ProcessDirector(JasperReportsContext jasperReportsContext, ScriptManager scriptManager)
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(jasperReportsContext);
		this.phantomjsExecutablePath = properties.getProperty(PhantomJS.PROPERTY_PHANTOMJS_EXECUTABLE_PATH);
		this.processStartTimeout = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_START_TIMEOUT, 
				PhantomJS.DEFAULT_PHANTOMJS_START_TIMEOUT);
		
		this.scriptManager = scriptManager;
		
		if (this.phantomjsExecutablePath != null)
		{
			this.listenAddress = InetUtil.getIPv4Loopback();
			if (this.listenAddress == null)
			{
				log.error("Unable to determine an IPv4 loopback address");
			}
		}
		
		processPool = createProcessPool(properties);
	}
	
	private GenericObjectPool<PhantomJSProcess> createProcessPool(JRPropertiesUtil properties)
	{
		ProcessFactory processFactory = new ProcessFactory(this);
		GenericObjectPool<PhantomJSProcess> pool = new GenericObjectPool<>(processFactory);
		pool.setLifo(true);
		
		int maxProcessCount = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_MAX_PROCESS_COUNT, 
				PhantomJS.DEFAULT_PHANTOMJS_MAX_PROCESS_COUNT);
		pool.setMaxTotal(maxProcessCount);
		pool.setMaxIdle(maxProcessCount);
		
		int borrowTimeout = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_POOL_BORROW_TIMEOUT, 
				PhantomJS.DEFAULT_PHANTOMJS_POOL_BORROW_TIMEOUT);
		pool.setMaxWaitMillis(borrowTimeout);
		
		int idleTimeout = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_IDLE_TIMEOUT, 
				PhantomJS.DEFAULT_PHANTOMJS_IDLE_TIMEOUT);
		pool.setMinEvictableIdleTimeMillis(idleTimeout);
		
		int idlePingInterval = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_IDLE_PING_INTERVAL, 
				PhantomJS.DEFAULT_PHANTOMJS_IDLE_PING_INTERVAL);
		pool.setTimeBetweenEvictionRunsMillis(idlePingInterval);
		
		pool.setTestWhileIdle(true);
		pool.setNumTestsPerEvictionRun(Integer.MAX_VALUE);
		
		pool.setSwallowedExceptionListener(new SwallowedExceptionListener()
		{
			@Override
			public void onSwallowException(Exception e)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Pool exception", e);
				}
			}
		});
		
		return pool;
	}
	
	public boolean isEnabled()
	{
		return phantomjsExecutablePath != null
				&& listenAddress != null;//TODO lucianc sanity checks?
	}
	
	public String getPhantomjsExecutablePath()
	{
		return phantomjsExecutablePath;
	}
	
	public int getProcessStartTimeout()
	{
		return processStartTimeout;
	}
	
	public ScriptManager getScriptManager()
	{
		return scriptManager;
	}
	
	public Inet4Address getListenAddress()
	{
		return listenAddress;
	}
	
	public String runRequest(String data)
	{
		PhantomJSProcess process = null;
		try
		{
			process = processPool.borrowObject();
			return process.getProcessConnection().runRequest(data);
		}
		catch (Exception e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			processPool.returnObject(process);
		}
	}

	public void dispose()
	{
		synchronized (this)
		{
			processPool.close();
		}
	}
	
}
