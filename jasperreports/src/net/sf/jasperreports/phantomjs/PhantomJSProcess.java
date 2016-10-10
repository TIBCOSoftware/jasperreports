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
	private ProcessDirector director;
	private URI listenURI;
	
	private Process process;
	private ProcessConnection processConnection;

	public PhantomJSProcess(ProcessDirector director, int listenPort)
	{
		this.id = "phantomjs#" + ID_COUNTER.incrementAndGet();
		this.director = director;
		this.listenURI = listenURI(director.getListenAddress(), listenPort);
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
		String mainScriptTempName = director.getScriptManager().getScriptFilename(PhantomJS.MAIN_SCRIPT_RESOURCE);
		String listenAddress = listenURI.getHost() + ":" + listenURI.getPort();
		int idleTimeout = director.getProcessIdleTimeout();

		log.info("PhantomJS process " + id + " starting on port " + listenURI.getPort());
		if (log.isDebugEnabled())
		{
			log.debug(id + " starting phantomjs process with command: "
					+ director.getPhantomjsExecutablePath() + " \"" + mainScriptTempName + "\""
					+ " -listenAddress \"" + listenAddress + "\""
					+ " -confirmMessage \"" + PHANTOMJS_CONFIRMATION_MESSAGE + "\""
					+ " -idleTimeout " + idleTimeout + "");
		}

		ProcessBuilder pb = new ProcessBuilder(director.getPhantomjsExecutablePath(), mainScriptTempName,
				"-listenAddress", listenAddress,
				"-confirmMessage", PHANTOMJS_CONFIRMATION_MESSAGE,
				"-idleTimeout", Integer.toString(idleTimeout)
				);
		pb.redirectErrorStream(false);
		pb.directory(director.getScriptManager().getTempFolder());

		try
		{
			process = pb.start();

			ProcessOutputReader outputReader = new ProcessOutputReader(this);
			outputReader.start();
			boolean started = outputReader.waitConfirmation(director.getProcessStartTimeout());
			if (!started)
			{
				log.error("PhantomJS process " + id + " failed to start");//TODO lucianc write error output
				process.destroy();
				
				throw new JRRuntimeException(EXCEPTION_MESSAGE_KEY_FAILED_START, (Object[]) null);
			}
			
			processConnection = new ProcessConnection(director, this);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	protected Process getProcess()
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
			log.info("PhantomJS process " + id + " to be destroyed");
			
			//TODO lucianc destroyForcibly in Java 8
			process.destroy();
		}
	}
}
