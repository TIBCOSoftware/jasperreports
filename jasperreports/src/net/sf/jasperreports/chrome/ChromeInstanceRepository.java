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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ChromeInstanceRepository
{

	private static final Log log = LogFactory.getLog(ChromeInstanceRepository.class);
	
	private static final ChromeInstanceRepository INSTANCE = new ChromeInstanceRepository();
	
	public static ChromeInstanceRepository instance()
	{
		return INSTANCE;
	}
	
	private final Map<LaunchConfiguration, ChromeInstance> instances;
	private final ScheduledExecutorService timeoutExecutor;
	
	protected ChromeInstanceRepository()
	{
		this.instances = new HashMap<>();
		this.timeoutExecutor = Executors.newScheduledThreadPool(1, runnable ->
		{
			Thread thread = new Thread(runnable);
			thread.setName("Chrome timeout");
			thread.setDaemon(true);
			return thread;
		});
	}
	
	public ChromeInstanceHandle getChromeInstanceHandle(LaunchConfiguration configuration)
	{
		ChromeInstance chromeInstance = instance(configuration);
		return chromeInstance;
	}

	protected ChromeInstance instance(LaunchConfiguration configuration)
	{
		synchronized (instances)
		{
			ChromeInstance instance = instances.get(configuration);
			
			if (instance != null && !instance.isAlive())
			{
				log.warn("Chrome instance " + instance.getId() + " not available");
				instance = null;
			}
			
			if (instance == null)
			{
				instance = new ChromeInstance(configuration);
				instance.start();//TODO do this without holding the lock
				scheduleTimeoutChecks(configuration, instance);
				
				instances.put(configuration, instance);
			}
			return instance;
		}
	}
	
	protected void scheduleTimeoutChecks(LaunchConfiguration configuration, ChromeInstance chromeInstance)
	{
		long idleTimeout = configuration.getIdleTimeout();
		if (idleTimeout > 0)
		{
			scheduleIdleTimeoutCheck(configuration, chromeInstance, idleTimeout, idleTimeout);
		}
		
		long liveTimeout = configuration.getLiveTimeout();
		if (liveTimeout > 0)
		{
			scheduleLiveTimeoutCheck(configuration, chromeInstance, liveTimeout);
		}
	}

	protected void scheduleIdleTimeoutCheck(LaunchConfiguration configuration, ChromeInstance chromeInstance, 
			long idleTimeout, long delay)
	{
		if (log.isDebugEnabled())
		{
			log.debug("schedule chrome instance " + chromeInstance.getId() + " idle timeout check after " + delay);
		}
		
		WeakReference<ChromeInstance> instanceReference = new WeakReference<ChromeInstance>(chromeInstance);
		timeoutExecutor.schedule(() ->
		{
			checkIdle(configuration, instanceReference, idleTimeout);
		}, delay, TimeUnit.MILLISECONDS);
	}

	protected void checkIdle(LaunchConfiguration configuration, WeakReference<ChromeInstance> instanceReference,
			long idleTimeout)
	{
		ChromeInstance instance = instanceReference.get();
		if (instance == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("cleared chrome instance reference on idle check");
			}
			return;
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("checking idle timeout for chrome instance " + instance.getId());
		}
		
		boolean toClose;
		synchronized (instances)
		{
			long now = System.currentTimeMillis();
			ChromeInstanceState state = instance.getState();
			if (state.isClosed())
			{
				log.debug("chrome instance " + instance.getId() + " already closed");
				toClose = false;
			}
			else
			{
				toClose = state.getUseCount() == 0 && state.getUseTimestamp() + idleTimeout <= now;
				if (toClose)
				{
					removeCachedInstance(configuration, instance);
				}
				else
				{
					//reschedule check at lastUsed + idleTimeout
					scheduleIdleTimeoutCheck(configuration, instance, idleTimeout, 
							state.getUseCount() == 0 ? (state.getUseTimestamp() + idleTimeout - now) : idleTimeout);
				}
			}
		}
		
		if (toClose)
		{
			if (log.isDebugEnabled())
			{
				log.debug("chrome instance " + instance.getId() + " exceeded idle timeout");
			}
			
			instance.close();
		}
	}

	protected void scheduleLiveTimeoutCheck(LaunchConfiguration configuration, ChromeInstance chromeInstance, 
			long liveTimeout)
	{
		if (log.isDebugEnabled())
		{
			log.debug("schedule chrome instance " + chromeInstance.getId() + " timeout after " + liveTimeout);
		}
		
		WeakReference<ChromeInstance> instanceReference = new WeakReference<ChromeInstance>(chromeInstance);
		timeoutExecutor.schedule(() ->
		{
			closeInstance(configuration, instanceReference);
		}, liveTimeout, TimeUnit.MILLISECONDS);
	}

	protected void closeInstance(LaunchConfiguration configuration, WeakReference<ChromeInstance> instanceReference)
	{
		ChromeInstance instance = instanceReference.get();
		if (instance == null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("cleared chrome instance reference on close");
			}
			return;
		}
		
		if (log.isDebugEnabled())
		{
			log.debug("live timeout for chrome instance " + instance.getId());
		}
		
		boolean toClose;
		synchronized (instances)
		{
			ChromeInstanceState state = instance.getState();
			toClose = !state.isClosed();
			if (toClose)
			{
				removeCachedInstance(configuration, instance);
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug("chrome instance " + instance.getId() + " already closed");
				}
			}
		}

		if (toClose)
		{
			instance.close();
		}
	}

	protected void removeCachedInstance(LaunchConfiguration configuration, ChromeInstance chromeInstance)
	{		
		ChromeInstance cachedInstance = instances.get(configuration);
		if (cachedInstance == chromeInstance)//this should always be the case
		{
			instances.remove(configuration);
		}
		else
		{
			//should not happen
			if (log.isDebugEnabled())
			{
				log.debug("found different cached chrome instance " 
						+ (cachedInstance == null ? "null" : cachedInstance.getId()));
			}
		}
	}
}
