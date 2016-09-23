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

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ProcessPool
{
	private static final Log log = LogFactory.getLog(ProcessPool.class);
	
	private String phantomjsExecutablePath;
	private int processStartTimeout;
	private ScriptManager scriptManager;
	private Inet4Address localAddress;
	
	private PhantomJSProcess process;

	public ProcessPool(JasperReportsContext jasperReportsContext, ScriptManager scriptManager)
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(jasperReportsContext);
		this.phantomjsExecutablePath = properties.getProperty(PhantomJS.PROPERTY_PHANTOMJS_EXECUTABLE_PATH);
		this.processStartTimeout = properties.getIntegerProperty(PhantomJS.PROPERTY_PHANTOMJS_START_TIMEOUT, 
				PhantomJS.DEFAULT_PHANTOMJS_START_TIMEOUT);
		
		this.scriptManager = scriptManager;
		
		this.localAddress = InetUtil.getIPv4Loopback();
		if (this.localAddress == null)
		{
			log.error("Unable to determine an IPv4 loopback address");
		}
	}
	
	public boolean isEnabled()
	{
		return phantomjsExecutablePath != null
				&& localAddress != null;//TODO lucianc sanity checks?
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
	
	public PhantomJSProcess getProcess()
	{
		synchronized (this)
		{
			if (process == null)
			{
				process = new PhantomJSProcess(this, localAddress, 6879);
				process.startPhantomJS();
			}
		}
		
		return process;
	}

	public void dispose()
	{
		synchronized (this)
		{
			if (process != null)
			{
				process.dispose();
			}
		}
	}
	
}
