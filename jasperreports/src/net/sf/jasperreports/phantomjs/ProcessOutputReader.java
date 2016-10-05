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
public class ProcessOutputReader implements Runnable
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
		Thread outputThread = new Thread(this, "JR PhantomJS output " + processId);
		outputThread.setDaemon(true);
		outputThread.start();
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

	@Override
	public void run()
	{
		BufferedReader reader = null;
		try
		{
			InputStream processInput = process.getProcess().getInputStream();
			reader = new BufferedReader(new InputStreamReader(processInput, StandardCharsets.UTF_8));
			String line;
			while((line = reader.readLine()) != null)
			{
				if (log.isDebugEnabled())
				{
					log.debug(processId + ": " + line);
				}
				
				if (line.equals(PhantomJSProcess.PHANTOMJS_CONFIRMATION_MESSAGE))
				{
					confirmed = true;
					startLatch.countDown();
				}
			}
			
			log.info("PhantomJS process " + processId + " done");
			
			if (!confirmed)
			{
				startLatch.countDown();
			}
		}
		catch (IOException e)
		{
			log.error(processId + " error reading phantomjs output", e);
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				} 
				catch (IOException e)
				{
					if (log.isWarnEnabled())
					{
						log.warn(processId + " failed to close phantomjs process inputstream", e);
					}
				}
			}
		}
	}

}
