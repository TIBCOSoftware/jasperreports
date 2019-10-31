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
package net.sf.jasperreports.chrome;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.launch.ChromeArguments;
import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.protocol.commands.Page;
import com.github.kklisura.cdt.protocol.commands.Runtime;
import com.github.kklisura.cdt.protocol.events.log.EntryAdded;
import com.github.kklisura.cdt.protocol.events.runtime.ConsoleAPICalled;
import com.github.kklisura.cdt.protocol.events.runtime.ExceptionThrown;
import com.github.kklisura.cdt.protocol.types.log.LogEntry;
import com.github.kklisura.cdt.protocol.types.runtime.ExceptionDetails;
import com.github.kklisura.cdt.protocol.types.runtime.RemoteObject;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class Service
{

	private static final Log log = LogFactory.getLog(Service.class);

	public static final Path findChromeExecutable()
	{
		try
		{
			@SuppressWarnings("resource")
			ChromeLauncher launcher = new ChromeLauncher();
			Path chromeBinaryPath = launcher.getChromeBinaryPath();
			if (log.isDebugEnabled())
			{
				log.debug("chrome binary found at " + chromeBinaryPath);
			}
			return chromeBinaryPath;
		}
		catch (RuntimeException e)
		{
			if (log.isDebugEnabled())
			{
				log.debug("chrome binary not found", e);
			}
			return null;
		}
	}
	
	private LaunchConfiguration configuration;
	
	private ChromeLauncher launcher;
	private ChromeService chromeService;
	
	public Service(LaunchConfiguration configuration)
	{
		this.configuration = configuration;
	}

	public void start()
	{
		launcher = new ChromeLauncher();
		ChromeArguments args = ChromeArguments.defaults(configuration.isHeadless()).build();
		
		log.info("Launching Chrome with configuration " + configuration);
		chromeService = launcher.launch(configuration.getExecutablePath(), args);
	}

	public <T> T evaluateInPage(String pageURL, ChromePageEvaluation<T> evaluation)
	{
		ChromeTab tab = null;
		try
		{
			tab = chromeService.createTab();
			ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);
			
			com.github.kklisura.cdt.protocol.commands.Log pageLog = devToolsService.getLog();
			pageLog.onEntryAdded(this::pageLogEvent);
			pageLog.enable();
			
			Runtime runtime = devToolsService.getRuntime();
			runtime.onConsoleAPICalled(this::consoleEvent);
			runtime.onExceptionThrown(this::pageExceptionEvent);
			runtime.enable();

			CompletableFuture<T> resultFuture = new CompletableFuture<>();
			ChromePage chromePage = new ChromePage(devToolsService);
			
			Page page = devToolsService.getPage();
			page.onLoadEventFired(event -> 
			{
				try
				{
					T evaluationResult = evaluation.runInPage(chromePage);
					resultFuture.complete(evaluationResult);
				}
				catch (Exception e)
				{
					resultFuture.completeExceptionally(e);
				}
			});

			page.enable();
			page.navigate(pageURL);
			
			T promiseResult = resultFuture.get();//TODO timeout
			return promiseResult;
		}
		catch (InterruptedException | ExecutionException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (tab != null)
			{
				chromeService.closeTab(tab);
			}
		}		
	}

	protected void pageLogEvent(EntryAdded event)
	{
		LogEntry entry = event.getEntry();
		switch (entry.getLevel())
		{
		case ERROR:
			log.error("Page error: " + entry.getText());
			break;
		case WARNING:
			log.warn("Page warning: " + entry.getText());
			break;
		case INFO:
			log.info("Page info: " + entry.getText());
			break;
		default:
			if (log.isDebugEnabled())
			{
				log.info("Page message: " + entry.getText());
			}
			break;
		}
	}

	protected void consoleEvent(ConsoleAPICalled event)
	{
		if (log.isDebugEnabled())
		{
			StringBuilder eventString = new StringBuilder();
			eventString.append("Page console ");
			eventString.append(event.getType());
			eventString.append(": ");
			List<RemoteObject> args = event.getArgs();
			if (args != null)
			{
				for (RemoteObject argObject : args)
				{
					eventString.append(argObject.getValue() == null 
							? argObject.getUnserializableValue()
							: argObject.getValue());
					eventString.append(", ");
				}
			}
			
			log.debug(eventString.substring(0, eventString.length() - 2));
		}
	}

	protected void pageExceptionEvent(ExceptionThrown event)
	{
		if (log.isWarnEnabled())
		{
			ExceptionDetails exceptionDetails = event.getExceptionDetails();
			log.warn("Script exception: " + exceptionDetails.getText());
			
			if (log.isDebugEnabled())
			{
				//TODO stacktrace?
				RemoteObject exception = exceptionDetails.getException();
				String exceptionString = null;
				if (exception != null)
				{
					if (exception.getDescription() != null)
					{
						exceptionString = exception.getDescription();
					}
					else if (exception.getValue() != null)
					{
						exceptionString = exception.getValue().toString();
					}
				}
				if (exceptionString != null)
				{
					log.debug("Exception description: " + exceptionString);
				}
			}
		}
	}
	
}
