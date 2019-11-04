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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
	
	protected ChromeInstanceRepository()
	{
		this.instances = new HashMap<>();
	}
	
	public ChromeInstanceHandle getChromeInstanceHandle(LaunchConfiguration configuration)
	{
		ChromeInstance chromeInstance = instance(configuration);
		
		return new ChromeInstanceHandle()
		{
			@Override
			public <T> T runWithChromeInstance(Function<ChromeInstance, T> execution)
			{
				if (log.isDebugEnabled())
				{
					log.debug("using chrome instance " + chromeInstance.getId());
				}
				return execution.apply(chromeInstance);
			}
		};
	}

	protected ChromeInstance instance(LaunchConfiguration configuration)
	{
		synchronized (instances)
		{
			ChromeInstance instance = instances.get(configuration);
			if (instance == null)
			{
				instance = new ChromeInstance(configuration);
				instance.start();
				
				instances.put(configuration, instance);
			}
			return instance;
		}
	}
	
}
