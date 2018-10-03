/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ProcessOutputReader
{
	private static final Log log = LogFactory.getLog(ProcessOutputReader.class);
	
	private PhantomJSProcess process;
	private String processId;
	private CountDownLatch startLatch;
	private volatile boolean confirmed;

	public ProcessOutputReader(PhantomJSProcess process)
	{
		this.process = process;
		this.processId = process.getId();
		this.startLatch = new CountDownLatch(1);
	}
	
	public void start()
	{
		//FIXME is two threads per process too much?  consider stream polling.
		OutputReader outputReader = new OutputReader();
		Thread outputThread = new Thread(outputReader, "JR PhantomJS output " + processId);
		outputThread.setDaemon(true);
		outputThread.start();
		
		ErrorReader errorReader = new ErrorReader();
		Thread errorThread = new Thread(errorReader, "JR PhantomJS error " + processId);
		errorThread.setDaemon(true);
		errorThread.start();
	}

	public boolean waitConfirmation(int processStartTimeout)
	{
		try
		{
			boolean done = startLatch.await(processStartTimeout, TimeUnit.MILLISECONDS);
			if (log.isDebugEnabled())
			{
				log.debug(processId + " done " + done + ", confirmed " + confirmed);
			}
			return confirmed;
		}
		catch (InterruptedException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	protected void signalEnd()
	{
		process.signalEnd();
		
		if (!confirmed)
		{
			startLatch.countDown();
		}
	}
	
	private class OutputReader implements Runnable
	{
		@Override
		public void run()
		{
			InputStream processInput = process.getProcess().getInputStream();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(processInput, StandardCharsets.UTF_8)))
			{
				String line;
				while((line = reader.readLine()) != null)
				{
					if (log.isDebugEnabled())
					{
						log.debug(processId + ": " + line);
					}
					
					if (line.trim().equals(PhantomJSProcess.PHANTOMJS_CONFIRMATION_MESSAGE))
					{
						confirmed = true;
						startLatch.countDown();
					}
				}
				
				if (log.isDebugEnabled())
				{
					log.debug(processId + " stream ended");
				}
			}
			catch (IOException e)
			{
				log.error(processId + " error reading phantomjs output", e);
			}
			finally
			{
				signalEnd();
			}
		}
	}
	
	private class ErrorReader implements Runnable
	{
		@Override
		public void run()
		{
			InputStream processInput = process.getProcess().getErrorStream();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(processInput, StandardCharsets.UTF_8)))
			{
				String line;
				while((line = reader.readLine()) != null)
				{
					log.error("PhantomJS " + processId + " error: " + line);
				}
				
				if (log.isDebugEnabled())
				{
					log.debug(processId + " error stream ended");
				}
			}
			catch (IOException e)
			{
				log.error(processId + " error reading phantomjs output", e);
			}
			finally
			{
				signalEnd();
			}
		}
	}

}
