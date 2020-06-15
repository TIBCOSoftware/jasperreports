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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kklisura.cdt.protocol.commands.Emulation;
import com.github.kklisura.cdt.protocol.commands.Page;
import com.github.kklisura.cdt.protocol.commands.Runtime;
import com.github.kklisura.cdt.protocol.events.log.EntryAdded;
import com.github.kklisura.cdt.protocol.events.runtime.ConsoleAPICalled;
import com.github.kklisura.cdt.protocol.events.runtime.ExceptionThrown;
import com.github.kklisura.cdt.protocol.types.log.LogEntry;
import com.github.kklisura.cdt.protocol.types.page.Navigate;
import com.github.kklisura.cdt.protocol.types.runtime.ExceptionDetails;
import com.github.kklisura.cdt.protocol.types.runtime.RemoteObject;
import com.github.kklisura.cdt.services.ChromeDevToolsService;

import net.sf.jasperreports.annotations.properties.Property;
import net.sf.jasperreports.annotations.properties.PropertyScope;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.properties.PropertyConstants;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BrowserService
{

	private static final Log log = LogFactory.getLog(BrowserService.class);
	
	@Property(
			category = PropertyConstants.CATEGORY_CHROME,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_11_0,
			valueType = Long.class,
			defaultValue = "60000"
			)
	public static final String PROPERTY_PAGE_TIMEOUT = Chrome.PROPERTY_PREFIX + "page.timeout";
	
	@Property(
			category = PropertyConstants.CATEGORY_CHROME,
			scopes = {PropertyScope.CONTEXT},
			sinceVersion = PropertyConstants.VERSION_6_12_0,
			valueType = Boolean.class,
			defaultValue = "false"
			)
	public static final String PROPERTY_PAGE_ISOLATE = Chrome.PROPERTY_PREFIX + "page.isolate";
	
	private JRPropertiesUtil propertiesUtil;
	private ChromeServiceHandle chromeServiceHandle;
	
	public BrowserService(JasperReportsContext jasperReportsContext, ChromeServiceHandle chromeServiceHandle)
	{
		this.propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
		this.chromeServiceHandle = chromeServiceHandle;
	}

	public <T> T evaluateInPage(String pageURL, PageOptions options, ChromePageEvaluation<T> evaluation)
	{
		if (log.isDebugEnabled())
		{
			log.debug("page evaluation at " + pageURL);
		}
		
		PageCreator pageCreator = isolate(options) ? IsolatedPageCreator.instance() : StandardPageCreator.instance();
		ChromeInstanceHandle chromeInstance = chromeServiceHandle.getChromeInstance();
		T result = pageCreator.runInPage(chromeInstance, devToolsService ->
		{
			try
			{
				com.github.kklisura.cdt.protocol.commands.Log pageLog = devToolsService.getLog();
				pageLog.onEntryAdded(this::pageLogEvent);
				pageLog.enable();
				
				Runtime runtime = devToolsService.getRuntime();
				runtime.onConsoleAPICalled(this::consoleEvent);
				runtime.onExceptionThrown(this::pageExceptionEvent);
				runtime.enable();
				
				setScreenDimensions(options, devToolsService);

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
				Navigate navigate = page.navigate(pageURL);
				if (navigate.getErrorText() != null)
				{
					throw new JRRuntimeException("Page failed to load: " + navigate.getErrorText());
				}
				
				Long timeout = pageTimeout(options);
				if (log.isDebugEnabled())
				{
					log.debug("waiting for result, timeout " + timeout);
				}
				T promiseResult = timeout == null || timeout <= 0 
						? resultFuture.get()
						: resultFuture.get(timeout, TimeUnit.MILLISECONDS);
				return promiseResult;
			}
			catch (InterruptedException | ExecutionException | TimeoutException e)
			{
				throw new JRRuntimeException(e);
			}
		});
		
		if (log.isDebugEnabled())
		{
			log.debug("page evaluation done");
		}
		return result;
	}

	protected boolean isolate(PageOptions options)
	{
		Boolean isolate = options == null ? null : options.getIsolate();
		return isolate == null ? propertiesUtil.getBooleanProperty(PROPERTY_PAGE_ISOLATE, false)
				: isolate;
	}
	
	protected void setScreenDimensions(PageOptions options, ChromeDevToolsService devToolsService)
	{
		if (options != null && (options.getScreenWidth() != null || options.getScreenHeight() != null))
		{
			Emulation emulation = devToolsService.getEmulation();
			if (emulation.canEmulate())
			{
				if (log.isDebugEnabled())
				{
					log.debug("setting screen dimensions " + options.getScreenWidth() + ", " + options.getScreenHeight());
				}
				
				emulation.setDeviceMetricsOverride(
						options.getScreenWidth() != null && options.getScreenWidth() > 0 ? options.getScreenWidth() : 0, 
						options.getScreenHeight() != null && options.getScreenHeight() > 0 ? options.getScreenHeight() : 0, 
						0d, false);
			}
			else
			{
				log.warn("Chrome device emulation is not supported, cannot set page width and height");
			}
		}
	}

	protected Long pageTimeout(PageOptions options)
	{
		Long timeout = options == null ? null : options.getTimeout();
		if (timeout == null)
		{
			String timeoutProp = propertiesUtil.getProperty(PROPERTY_PAGE_TIMEOUT);
			if (timeoutProp != null && !timeoutProp.trim().isEmpty())
			{
				timeout = JRPropertiesUtil.asLong(timeoutProp);
			}
		}
		return timeout;
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
