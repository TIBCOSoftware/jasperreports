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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PhantomJSProcess
{
	private static final Log log = LogFactory.getLog(PhantomJSProcess.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_FAILED_START = "phantomjs.failed.start";
	
	public static final String PHANTOMJS_CONFIRMATION_MESSAGE = "PROCESS_STARTED";
	
	private static final AtomicLong ID_COUNTER = new AtomicLong();

	private String id;
	private ProcessPool processPool;
	private URI listenURI;
	
	private Process process;
	private ProcessConnection processConnection;

	public PhantomJSProcess(ProcessPool processPool, Inet4Address listenAddress, int listenPort)
	{
		this.id = "phantomjs#" + ID_COUNTER.incrementAndGet();
		this.processPool = processPool;
		this.listenURI = listenURI(listenAddress, listenPort);
	}
	
	private URI listenURI(Inet4Address listenAddress, int listenPort)
	{
		try
		{
			return new URI("http", null, listenAddress.getHostAddress(), listenPort, null, null, null);
		}
		catch (URISyntaxException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public String getId()
	{
		return id;
	}
	
	public URI getListenURI()
	{
		return listenURI;
	}
	
	public void startPhantomJS()
	{
		String mainScriptTempName = processPool.getScriptManager().getScriptFilename(PhantomJS.MAIN_SCRIPT_RESOURCE);

		String listenAddress = listenURI.getHost() + ":" + listenURI.getPort();
		if (log.isDebugEnabled())
		{
			log.debug(id + " starting phantomjs process with command: "
					+ processPool.getPhantomjsExecutablePath() + " \"" + mainScriptTempName + "\""
					+ " -listenAddress \"" + listenAddress + "\""
					+ "-confirmMessage \"" + PHANTOMJS_CONFIRMATION_MESSAGE + "\"");
		}

		ProcessBuilder pb = new ProcessBuilder(processPool.getPhantomjsExecutablePath(), mainScriptTempName,
				"-listenAddress", listenAddress,
				"-confirmMessage", PHANTOMJS_CONFIRMATION_MESSAGE
				);
		pb.redirectErrorStream(true);//TODO lucianc separate streams
		pb.directory(processPool.getScriptManager().getTempFolder());

		try
		{
			process = pb.start();

			ProcessOutputReader outputReader = new ProcessOutputReader(this);
			outputReader.start();
			boolean started = outputReader.waitConfirmation(processPool.getProcessStartTimeout());
			if (!started)
			{
				log.error(id + " failed to start");//TODO lucianc write error output
				process.destroy();
				
				throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_FAILED_START);
			}
			
			processConnection = new ProcessConnection(this);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public Process getProcess()
	{
		return process;
	}
	
	public ProcessConnection getProcessConnection()
	{
		return processConnection;
	}
	
	public void dispose()
	{
		if (processConnection != null)
		{
			processConnection.dispose();
		}
		
		if (process != null && process.isAlive())
		{
			if (log.isDebugEnabled())
			{
				log.debug(id + " to be destroyed");
			}
			
			process.destroy();
		}
	}
}
